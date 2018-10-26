package com.platform.gateway.security;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Component;

import com.platform.gateway.component.UrlRoleCache;

@Component
public class ZuulAccessDecisionManager implements AccessDecisionManager {
	
	@Autowired
	UrlRoleCache urlRoleCache;
	
	public static final String NO_LOGIN = "no login";

	private static final String ANONYMOUS_USER = "anonymousUser";

	private static final String ROLE_ADMIN = "Admin";

	@Value("${ignore.urlPath}")
	String urlPathIgnore;

	public boolean checkIgnore(String value, String ignoreConf) {
		if (ignoreConf == null || ignoreConf.length() == 0) {
			return false;
		}
		String[] igonres = ignoreConf.split(",");
		for (String ingore : igonres) {
			if (value.equals(ingore.trim()) || value.endsWith(ingore.trim())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断是否匿名用户
	 *
	 * @param authentication
	 * @return
	 */
	public static boolean isAnonymous(Authentication authentication) {
		if (authentication == null) {
			return true;
		} else {
			return authentication.getPrincipal().toString().equals(ANONYMOUS_USER);
		}
	}

	@Override
	public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes)
			throws AccessDeniedException, InsufficientAuthenticationException {

		if (((FilterInvocation) object).getRequest().getMethod().equals("OPTIONS")) {
			return;
		}

		//判断url是否需要忽略
		String url = ((FilterInvocation) object).getRequestUrl();
		if (url.indexOf("?") != -1) {
			url = url.substring(0, url.indexOf("?"));
		}
		if (checkIgnore(url, urlPathIgnore)) {
			return;
		}

		//匿名用户即为非法访问
		if (isAnonymous(authentication)) {
			if (url.startsWith("/apidocs")) {
				return;
			}
			throw new AccessDeniedException(NO_LOGIN);
		}

		//获取urlmap
		Map<String, Map<String, String>> urlMap = urlRoleCache.getUrlMap();
		
		final Map<String, String> systemUrlMap = null;
		
		final String finalUrl = url;
		
		urlMap.forEach((k, v)->{
			if(finalUrl.startsWith("/" + k)){
				v.forEach((k1,v1)->{
					systemUrlMap.put(k1, v1);
				});
			}
		});
		
		if (systemUrlMap == null || systemUrlMap.keySet().size() == 0) {
			return;
		}

		// 判断是否有相应的角色
		String needRole = (String) systemUrlMap.get(url);

		if (null == needRole) {
			return;
		}

		for (GrantedAuthority ga : authentication.getAuthorities()) {
			if (ga.getAuthority().equals(ROLE_ADMIN) || needRole.trim().equals(ga.getAuthority().trim())) {
				return;
			}
		}

		if (configAttributes == null) {
			return;
		}

		throw new AccessDeniedException("");

	}

	@Override
	public boolean supports(ConfigAttribute attribute) {
		return true;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return true;
	}

}
