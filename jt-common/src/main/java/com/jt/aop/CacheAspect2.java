package com.jt.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.eclipse.jdt.internal.compiler.ast.FalseLiteral;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.anno.Cache_Find2;
import com.jt.util.ObjectMapperUtil;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.ShardedJedis;

@Aspect
@Component
public class CacheAspect2 {
	@Autowired(required = false)
	private ShardedJedis jedis;
	
	@Around("@annotation(cache)")
	public Object doCacheObject(ProceedingJoinPoint joinPoint,Cache_Find2 cache) {
		// System.out.println("这是一个环绕切面的测试");
		Object data = null;
		String keyString = getKeyString(joinPoint,cache);
		String result = jedis.get(keyString);
		if (StringUtils.isEmpty(result)) {
			try {
				data = joinPoint.proceed();
				String json = ObjectMapperUtil.toJSON(data);
				if (cache.secondsTime() > 0) {
					jedis.setex(keyString, cache.secondsTime(), json);
				} else {
					jedis.set(keyString, json);	
				}
				// System.out.println("从数据库读数据成功");
			} catch (Throwable e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		} else {
			Class<?> returnClass = getReturnClass(joinPoint);
			data = ObjectMapperUtil.toObject(result, returnClass);
			
			// System.out.println("从缓存中读数据成功");
		}
		
		return data;
	}

	private Class<?> getReturnClass(ProceedingJoinPoint joinPoint) {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Class<?> returnType = signature.getReturnType();
		return returnType;
	}

	private String getKeyString(ProceedingJoinPoint joinPoint, Cache_Find2 cache) {
		String keyString = cache.keyString();
		if (StringUtils.isEmpty(keyString)) {
			String className = joinPoint.getSignature().getDeclaringTypeName();
			String name = joinPoint.getSignature().getName();
			// System.out.println(className+"-----------"+name);
			if (joinPoint.getArgs().length > 0) {
				String parentIdName = joinPoint.getArgs()[0].toString();
				keyString = className + "." + name + "::" + parentIdName;
			} else {
				keyString = className + "." + name;
			}	
		}
		return keyString;
	}
	

}
