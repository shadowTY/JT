package com.jt.controller.web;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.service.ItemService;
import com.jt.util.ObjectMapperUtil;
import com.jt.vo.EasyUITable;

@RestController
public class JSONPController {
	@Autowired
	private ItemService itemService;
	/**
	 *  利用API简化调用
	 * @param callback
	 * @return
	 */
	@RequestMapping("/web/testJSONP")
	public JSONPObject testJSONPObject(String callback) {
		ItemDesc itemDesc = new ItemDesc();
		itemDesc.setItemId(150010L).setItemDesc("测试跨域实现").setCreated(new Date()).setUpdated(itemDesc.getCreated());
		return new JSONPObject(callback, itemDesc);
	}
	
	@RequestMapping("/web/JSONPITEM")
	public JSONPObject testJsonpObjectItem(String callback) {
		List<Item> list = itemService.findItemAll();
		System.out.println(list);
		return new JSONPObject(callback, list);
	}
	
	
	
	public String testJSONP(String callback) {
		ItemDesc itemDesc = new ItemDesc();
		itemDesc.setItemId(150010L).setItemDesc("测试跨域实现").setCreated(new Date()).setUpdated(itemDesc.getCreated());
		String json = ObjectMapperUtil.toJSON(itemDesc);
		return callback+"("+json+")";
	}

}
