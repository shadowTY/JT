package com.jt.interceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import com.jt.pojo.User;
import com.jt.util.ObjectMapperUtil;
import com.jt.util.UserThreadLocalUtil;

import redis.clients.jedis.JedisCluster;

@Component		// 交给spring容器管理
public class UserInterceptor implements HandlerInterceptor{
	
	private static final String TICKET = "JT_TICKET";
	@Autowired
	private JedisCluster jedisCluster;
	/**
	 *  实现用户权限认证
	 *  	用户不登陆，不允许访问涉密操作，重定向到用户登录页面
	 *  	如果用户登录，则请求予以放行
	 *  
	 *  方法说明：
	 *  	1. boolean true：放行   false： 拦截  配合重定向使用
	 *  
	 *  实现步骤：
	 *  	1.获取用户cookie信息		获取密钥
	 *  	2.从redis中获取数据
	 */
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		Cookie[] cookies = request.getCookies();
		String ticket = null;
		// 判断cookie是有效的
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
			// 校验redis中是否有数据
			String userJSON = jedisCluster.get(ticket);
			if (!StringUtils.isEmpty(userJSON)) {
				// 实现用户信息的动态获取
				User user = ObjectMapperUtil.toObject(userJSON, User.class);
				request.setAttribute("JT_USER", user);
				UserThreadLocalUtil.set(user);
				return true;
			}
		}
		
		// 如果用户没有登录需要重定向到登陆页面
		response.sendRedirect("/user/login.html");
		return false;
	}
}
