package com.jt.aop;

import java.util.Date;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/*@Order(1)
@Aspect
@Component*/
public class TimeAspect {
	// @Pointcut("execution(* com.jt.service..*.*(..))")
	public void dopoint() {}
	
	// @Around("dopoint()")
	public Object getMenthodTime(ProceedingJoinPoint joinPoint) throws Throwable {
		System.out.println("切面执行");
		long time = System.nanoTime();
		Object data = joinPoint.proceed();
		time = System.nanoTime() - time;
		System.out.println("执行时长是" + time);
		return data;
	}

}
