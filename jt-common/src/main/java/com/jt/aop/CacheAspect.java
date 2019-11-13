package com.jt.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.jt.anno.Cache_Find;
import com.jt.util.ObjectMapperUtil;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.ShardedJedis;

// 切面 = 切入点 + 通知
@Aspect		// 标识切面
@Component	// 交给spring容器管理
public class CacheAspect {
	// required = false 当用户使用时才注入
//	@Autowired(required = false)
//	private ShardedJedis jedis;
//	@Autowired
//	private Jedis jedis;
	@Autowired(required = false)
	private JedisCluster jedis;  // 集群对象
	/**
	 *  利用aop规则：动态获取注解对象
	 *  步骤：
	 *  	1. 根据key查询erdis
	 *  	2.没有数据，需要让目标方法执行。查询的结果保存到redis
	 *  	3.将json串转化为返回值对象返回
	 * @param joinPoint
	 * @param CacheFind
	 * @return
	 */
	@Around("@annotation(cacheFind)")
	public Object around(ProceedingJoinPoint joinPoint,Cache_Find cacheFind) {
		Object data = null;
		String key = getKey(joinPoint,cacheFind);
		// 1 .从redis中获取数据
		String result = jedis.get(key);
		// 2. 判断缓存中是否有数据
		try {
			if (StringUtils.isEmpty(result)) {
				// 2.1 缓存中没有数据
				data = joinPoint.proceed();		// 目标方法执行
				// 2.2 将返回值的结果转化为json
				String json = ObjectMapperUtil.toJSON(data);
				// 2.3 判断用户是否编辑时间
				// 如果有时间，必须设定超时时间
				if (cacheFind.seconds() > 0 ) {
					jedis.setex(key, cacheFind.seconds(), json);
				} else {
					jedis.set(key, json);	
				}
				System.out.println("aop查询数据库");
			} else {
				// 表是缓存数据不为空，将缓存的数据转化为具体的对象
				Class<?> returnClass = getReturnClass(joinPoint);
				data = ObjectMapperUtil.toObject(result, returnClass);
				// data = ObjectMapperUtil.toObject(result, Object.class);
				// data = ObjectMapperUtil.toObject(result, data.getClass());
				System.out.println("查询缓存");
			}

		} catch (Throwable e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return data;
	}
	/**
	 *  获取目标方法的返回值类型
	 * @param joinPoint
	 * @return
	 */
	private Class<?> getReturnClass(ProceedingJoinPoint joinPoint) {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		return signature.getReturnType();
	}
	/**
	 *  动态获取key
	 * @param joinPoint
	 * @param cacheFind
	 * @return
	 */
	private String getKey(ProceedingJoinPoint joinPoint, Cache_Find cacheFind) {
		String key = cacheFind.key();
		if (StringUtils.isEmpty(key)) {
			// key 自动生成
			String className = joinPoint.getSignature().getDeclaringTypeName();
			String methodName = joinPoint.getSignature().getName();
			if (joinPoint.getArgs().length > 0) {
				key = className + "." + methodName + "::" + joinPoint.getArgs()[0];				
			} else {
				key = className + "." + methodName;
			}
		} 
		return key;
	}

}
