package com.jt;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.jt.pojo.ItemDesc;
import com.jt.util.ObjectMapperUtil;

public class TestObjectUtil {
	
	@SuppressWarnings("unchecked")
	@Test
	public void testToJson() {
		List<ItemDesc> list = new ArrayList<ItemDesc>();
		ItemDesc itemDesc1 = new ItemDesc();
		itemDesc1.setItemId(100L);
		itemDesc1.setItemDesc("测试测试");
		ItemDesc itemDesc = new ItemDesc();
		itemDesc.setItemId(100L);
		itemDesc.setItemDesc("测试测试");
		list.add(itemDesc);
		list.add(itemDesc1);
		String json = ObjectMapperUtil.toJSON(list);
		System.out.println(json);
		System.out.println("----------------");
		List<ItemDesc> descList = ObjectMapperUtil.toObject(json, list.getClass());
		System.out.println(descList);
	}
	

}
