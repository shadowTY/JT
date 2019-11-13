package com.jt.aop;

import javax.management.RuntimeErrorException;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.jt.anno.Cache_Find3;
import com.jt.util.ObjectMapperUtil;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.ShardedJedis;

@Aspect    // 声明这是一个切面
@Component   // 把这个类交给spring管理
public class CacheAspect3 {
	@Autowired(required = false)
	private ShardedJedis jedis;
	
	@Around("@annotation(cache)")
	public Object doCacheObject(ProceedingJoinPoint joinPoint,Cache_Find3 cache) {
		// System.out.println("切面运行");
		Object data = null;
		String key = getKey(joinPoint,cache);
		// 通过key值获取对应的value值
		String result = jedis.get(key);
		// 如果没有获取到对应的value值
		if (StringUtils.isEmpty(result)) {
			// 执行目标方法，从数据库中查询数据
			try {
				data = joinPoint.proceed();
				// 将对象转化为json串
				String json = ObjectMapperUtil.toJSON(data);
				// 判断用户是否设置了失效时间
				if (cache.seconds() > 0) {
					// 如果是，设置失效时间
					jedis.setex(key, cache.seconds(), json);
				} else {
					jedis.set(key, json);
				}
				System.out.println("从数据库取数据成功");
			} catch (Throwable e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
			
		} else {
			// 获取目标函数的返回值类型
			Class<?> returnType = getReturnTypes(joinPoint);
			// 将json串转化为对象
			data = ObjectMapperUtil.toObject(result, returnType);
			System.out.println("从内存中取数据成功+++++++++++++");
		}
		return data;
	}
	/**
	 *  获取目标函数返回值类型的方法
	 * @param joinPoint
	 * @return
	 */
	private Class<?> getReturnTypes(ProceedingJoinPoint joinPoint) {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		return signature.getReturnType();
	}

	/**
	 *  获取key的方法
	 *  如果用户传入key值，则使用用户给的key值
	 *  如果用户没有传入key值，则自动生成一个key值
	 * @param joinPoint
	 * @param cache
	 * @return
	 */
	private String getKey(ProceedingJoinPoint joinPoint, Cache_Find3 cache) {
		String key = cache.key();
		// 判断用户是否传入key值，即判断key值是否为null或"";
		if (StringUtils.isEmpty(key)) {
			// 获取包名+类名
			String className = joinPoint.getSignature().getDeclaringTypeName();
			// 获取方法名
			String methodName = joinPoint.getSignature().getName();
			// 判断连接点的函数是否传入了参数，如果是，将第一个参数拼接到key值中
			if (joinPoint.getArgs().length > 0) {
				key = className + "." + methodName + "::" + joinPoint.getArgs()[0];
			} else {
				key = className + "." + methodName;
			}
		}
		return key;
	}
	

}
