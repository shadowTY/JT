package com.jt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {
	/**
	 *  需求：通过一个方法拦截，实现通用页面跳转
	 *  思路：只要动态获取url中的参数，可以实现页面跳转
	 *  
	 *  语法规则：SpringMvc 动态获取url中的数据
	 *  	1. 参数必须用{}包裹
	 *  	2.参数与参数之间使用/分割，参数位置固定
	 *  	3.利用@PathVariable注解接收，并且名称一致
	 * @param ui
	 * @return
	 * 
	 * 	restFul风格：
	 * 		1. 动态获取用户参数
	 * 		2. 请求路径是不变的，根据请求方式的不同实现CRUD操作
	 * 
	 */
	@RequestMapping("/page/{ui}")
	public String doLoadUI(@PathVariable String ui) {
		return ui;
	}
	
	@RequestMapping("/getMsg")
	@ResponseBody
	public String getMassage() {
		return "我是8083项目";
	}

}
