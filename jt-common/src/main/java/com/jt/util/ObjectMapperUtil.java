package com.jt.util;
/**
 *  作用： 实现对象域json串之间的转化
 * @author TY
 *
 */

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectMapperUtil {
	// 常量对象，可以调用对象的方法  线程安全
	private static final ObjectMapper MAPPER = new ObjectMapper();
	/**
	 *  将检查异常转化为运行异常
	 * @param data
	 * @return
	 */
	public static String toJSON(Object data) {
		String json = null;
		try {
			json = MAPPER.writeValueAsString(data);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return json;
	}
	
	/**
	 *  根据json转化为对象
	 * @param <T>
	 * @param json
	 * @param object
	 * @return
	 */
	public static <T> T toObject(String json,Class<T> object) {
		T list = null;
		try {
			list  = MAPPER.readValue(json, object);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} 
		return list;
	}

}
