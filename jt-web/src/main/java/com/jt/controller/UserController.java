package com.jt.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.pojo.User;
import com.jt.service.DubboUserService;
import com.jt.vo.SysResult;

import redis.clients.jedis.JedisCluster;

/**
 *  UserController 涉及页面跳转
 * @author TY
 *
 */
@Controller
@RequestMapping("/user")
public class UserController {
	// 标识当前变量不可变时
	private static final String TICKET = "JT_TICKET";
	/**
	 *   注意是否为json返回
	 */
	@Reference(check = false)
	private DubboUserService userService;
	@Autowired
	private JedisCluster jedisCluster;
	
	@RequestMapping("/{moduleName}")
	public String module(@PathVariable String moduleName) {
		return moduleName;
	}
	
	@RequestMapping("/doRegister")
	@ResponseBody
	public SysResult saveUser(User user) {
		userService.saveUser(user);
		return SysResult.success();
	}
	
	/**
	 *  关于cookie的使用
	 *   cookie.setPath("/");	默认
	 *   全部用户都可见该cookie信息。
	 *   cookie.setPath("/AA");
	 *   只有再/aa路径下的url才能访问该cookie
	 *   
	 *   cookie.setMaxAge(>0);	cookie的存活时间
	 *   cookie.setMaxAge(0);	删除cookie
	 *   cookie.setMaxAge(-1);  关闭会话之后，删除cookie
	 *   
	 * @param user
	 * @return
	 */
	@RequestMapping("/doLogin")
	@ResponseBody
	public SysResult login(User user,HttpServletResponse response) {
		// 1. 校验数据是否正确，获取密钥
		String ticket = userService.findUserByUP(user);
		if (StringUtils.isEmpty(ticket)) {
			return SysResult.fail();
		}
		
		// 2. 如果程序执行到这里，表示密钥有值，写入cookie
		Cookie cookie = new Cookie("JT_TICKET", ticket);
		cookie.setMaxAge(7*24*3600);
		cookie.setPath("/");
		// 设定cookie的共享
		cookie.setDomain("jt.com");
		// 将cookie写入客户端
		response.addCookie(cookie);
		return SysResult.success();
	}
	
	/**
	 * 实现用户登出操作
	 * 	0.
	 * @param request
	 * @return
	 */
	@RequestMapping("/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response) {
		Cookie[] cookies = request.getCookies();
		String ticket = null;
		if (cookies.length > 0) {
			for (Cookie cookie : cookies) {
				if (TICKET.equals(cookie.getName())) {
					cookie.getValue();
					ticket = cookie.getValue();
					break;
				}
			}	
		}
		
		if (!StringUtils.isEmpty(ticket)) {
			// 如果数据不为null，删除这个元素
			jedisCluster.del(ticket);
			
			Cookie cookie = new Cookie(TICKET, "");
			cookie.setMaxAge(0);
			cookie.setPath("/");
			cookie.setDomain("jt.com");
			response.addCookie(cookie);
		}
		// 重定向到系统首页
		return "redirect:/";
	}
	
	
}
