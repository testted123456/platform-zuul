package com.platform.gateway.component;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import com.platform.gateway.util.RedisUtil;

@Component
public class UrlRoleCache {
	
	public static Logger logger = LoggerFactory.getLogger(UrlRoleCache.class);

	
	@Autowired
	RedisUtil redisUtil;
	
	private Map<String, Map<String, String>> urlMap = new HashMap<String, Map<String,String>>(); 

	public Map<String, Map<String, String>> getUrlMap() {
		return urlMap;
	}

	public void setUrlMap(Map<String, Map<String, String>> urlMap) {
		this.urlMap = urlMap;
	}
	
	/**
	 * 程序启动时读取redis中url权限对应关系
	 */
	@EventListener(ApplicationReadyEvent.class)
    public void initUrlMap() {
		logger.info("开始获取url权限对应关系...");
		
		Map<String, String> userUrlMap = (Map<String, String>)redisUtil.getHashObject("user");
		Map<String, String> interUrlMap = (Map<String, String>)redisUtil.getHashObject("inter");
		Map<String, String> caseUrlMap = (Map<String, String>)redisUtil.getHashObject("case");
		Map<String, String> groupUrlMap = (Map<String, String>)redisUtil.getHashObject("group");
		
		if(userUrlMap != null && userUrlMap.size()>0){
			urlMap.put("user", userUrlMap);
		}
		
		if(interUrlMap != null && interUrlMap.size()>0){
			urlMap.put("inter", interUrlMap);
		}
		
		if(caseUrlMap != null && caseUrlMap.size()>0){
			urlMap.put("case", caseUrlMap);
		}
		
		if(groupUrlMap != null && groupUrlMap.size()>0){
			urlMap.put("group", groupUrlMap);
		}
	}

}
