package com.jt.aop;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.jt.vo.SysResult;

import lombok.extern.slf4j.Slf4j;

// @ControllerAdvice   // 定义controller层的切面     切面的定义
@RestControllerAdvice
@Slf4j
public class ExceptionAspect {
	
	// 当出现某类异常，该方法执行
	@ExceptionHandler(RuntimeException.class)  
	// @ResponseBody
	public SysResult result(Exception exception) {
		exception.printStackTrace();  // 为方便程序员调错 准备的
		log.error(exception.getMessage());
		return SysResult.fail();
	}

}
