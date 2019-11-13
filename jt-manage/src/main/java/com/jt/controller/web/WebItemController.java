package com.jt.controller.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 *  要求从后台服务器返回json数据
 * @author TY
 *
 */

import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.service.ItemService;

@RestController
@RequestMapping("/web/item")
public class WebItemController {
	@Autowired
	private ItemService itemService;
	/**
	 *  根据itemId查询商品信息
	 *  1. 查询item信息
	 *  2. 查询itemDesc信息
	 * @return
	 */
	@RequestMapping("/findItemById/{id}")
	public Item findItemById(@PathVariable Long id) {
		return itemService.findItemById(id);
	}
	
	@RequestMapping("/findItemDescById/{id}")
	public ItemDesc findItemDescById(@PathVariable Long id) {
		return itemService.findItemDescById(id);
	}
	

}
