package com.jt.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.jt.interceptor.UserInterceptor;

@Configuration
public class MvcConfigurer implements WebMvcConfigurer{
	
	//开启匹配后缀型配置			伪静态支持
	@Override
	public void configurePathMatch(PathMatchConfigurer configurer) {
		
		configurer.setUseSuffixPatternMatch(true);
	}
	@Autowired
	private UserInterceptor userInterceptor;
	/**
	 *  /cart/* 拦截url请求中/cart/xxx 下的一级路径
	 *  /cart/** 拦截cart下的所有路径
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(userInterceptor).addPathPatterns("/cart/**","/order/**");
	}
}
