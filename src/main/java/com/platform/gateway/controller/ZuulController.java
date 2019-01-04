package com.platform.gateway.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.platform.gateway.component.UrlRoleCache;
import com.platform.gateway.result.Result;
import com.platform.gateway.result.ResultUtil;

@Controller(value="zuul")
public class ZuulController {
	
	public static Logger logger = LoggerFactory.getLogger(ZuulController.class);
	
	@Autowired
	UrlRoleCache urlRoleCache;

	/**
	 * 刷新权限
	 * @return
	 */
	@GetMapping(value="initURLMap")
	@ResponseBody
	public Result initURLMap(){
		logger.info("开始初始化资源权限对应关系...");
		urlRoleCache.initUrlMap();
		return ResultUtil.success();
	}
}
