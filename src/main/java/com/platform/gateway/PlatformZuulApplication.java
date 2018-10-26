package com.platform.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@EnableZuulProxy
@EnableEurekaClient
@EnableRedisHttpSession
@RefreshScope
@EnableDiscoveryClient
@Configuration
@EnableFeignClients
@SpringBootApplication
public class PlatformZuulApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlatformZuulApplication.class, args);
	}
}
