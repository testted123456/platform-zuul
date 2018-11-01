package com.platform.gateway.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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

	private static final String ROLE_ADMIN = "admin";

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
		
		Map<String, String> roleUrlMap = null;
		
		String sysAddress = null;
		
		if(url.toLowerCase().startsWith("/platform-interface")){
			roleUrlMap = urlMap.get("inter");
			sysAddress = "/platform-interface";
		}else if(url.toLowerCase().startsWith("/platform-testcase")){
			roleUrlMap = urlMap.get("case");
			sysAddress = "/platform-testcase";
		}else if(url.toLowerCase().startsWith("/platform-testgroup")){
			roleUrlMap = urlMap.get("group");
			sysAddress = "/platform-testgroup";
		}else if(url.toLowerCase().startsWith("/platform-user")){
			roleUrlMap = urlMap.get("user");
			sysAddress = "/platform-user";
		}
		
		if (roleUrlMap == null || roleUrlMap.keySet().size() == 0) {
			return;
		}

		// 判断是否有相应的角色
		String needRole = (String) roleUrlMap.get(url);
		
		if(null == needRole){//没有匹配到url，则找出父目录
			final String preUrl = sysAddress;
			
			final String finalUrl = url;
			
			Set<String> keyUrls = roleUrlMap.keySet();
			
			Optional<String> matchedUrl = keyUrls.stream().filter(x->{
				String fullUrl = preUrl + x;
				return finalUrl.contains(fullUrl);
			}).max((s1,s2)->s1.length()-s2.length());
			
			if(!matchedUrl.isPresent()){
				return;
			}else{
				needRole = (String) roleUrlMap.get(matchedUrl.get());
			}
		}
		
		for (GrantedAuthority ga : authentication.getAuthorities()) {
			if (ga.getAuthority().equals(ROLE_ADMIN) || needRole.trim().contains(ga.getAuthority().trim())) {
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
