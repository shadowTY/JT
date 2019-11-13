package com.jt.util;

import com.jt.pojo.User;

public class UserThreadLocalUtil {
	
	private static ThreadLocal<User> userThreadLocal = new ThreadLocal<>();
	
	public static void set(User user){
		userThreadLocal.set(user);
		
	}
	
	public static User get() {
		return userThreadLocal.get();
	}
	
	public static void remove() {
		userThreadLocal.remove();
	}

}
