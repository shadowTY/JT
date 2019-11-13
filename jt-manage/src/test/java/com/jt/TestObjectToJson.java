package com.jt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.pojo.ItemDesc;

public class TestObjectToJson {
	private ObjectMapper mapper = new ObjectMapper();
	
	@Test
	public void toJson() throws Exception {
		ItemDesc itemDesc = new ItemDesc();
		itemDesc.setItemId(100L);
		itemDesc.setItemDesc("葫芦娃救爷爷");
		String json = mapper.writeValueAsString(itemDesc);
		System.out.println(json);
		// 2. json转化为对象
		// src：代表jason串 valueType代表类型
		ItemDesc instance = mapper.readValue(json, ItemDesc.class);
		System.out.println(instance);
	}
	/**
	 *  List集合转化为json
	 * @throws IOException 
	 */
	@Test
	@SuppressWarnings("unchecked")
	public void toList() throws IOException {
		List<ItemDesc> list = new ArrayList<ItemDesc>();
		ItemDesc itemDesc1 = new ItemDesc();
		itemDesc1.setItemId(100L);
		itemDesc1.setItemDesc("葫芦娃救爷爷");
		ItemDesc itemDesc2 = new ItemDesc();
		itemDesc2.setItemId(100L);
		itemDesc2.setItemDesc("葫芦娃救爷爷");
		list.add(itemDesc1);
		list.add(itemDesc2);
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(list);
		System.out.println(json);
		List<ItemDesc> descList = mapper.readValue(json, list.getClass());
		System.out.println(descList);
	}

}
