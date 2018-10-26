package com.platform.gateway.security;

import javax.annotation.Resource;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private AccessDecisionManager accessDecisionManager;
    
    @Resource
    AuthenticationEntryPoint authenticationEntryPoint;
    
    @Resource
    AccessDeniedHandler accessDeniedHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	
    	http.csrf().disable().
    	authorizeRequests().anyRequest().authenticated().accessDecisionManager(accessDecisionManager);
    	
    	//处理没有登陆用户
    	http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint);
    	
    	//没有权限
    	http.exceptionHandling().accessDeniedHandler(accessDeniedHandler);
    }

}

