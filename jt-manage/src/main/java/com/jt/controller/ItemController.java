package com.jt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jt.anno.Cache_Find;
import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.service.ItemService;
import com.jt.vo.EasyUITable;
import com.jt.vo.SysResult;

@RestController  // 为ajax返回数据，不能跳转页面
@RequestMapping("/item/")
public class ItemController {
	@Autowired
	private ItemService itemService;
	
	/**
	 *  展现商品列表数据
	 */
	@RequestMapping("query")
	@Cache_Find
	public EasyUITable findItemByPage(Integer page,Integer rows) {
		return itemService.findItemByPage(page,rows);
	}
	
	/**
	 *  实现商品的新增
	 *  try {
			itemService.saveItem(item);
			return SysResult.success();
		} catch (Exception e) {
			e.printStackTrace();
			return SysResult.fail();
		}
	 */
	@RequestMapping("save")
	public SysResult saveItem(Item item, ItemDesc itemDesc) {
		itemService.saveItem(item, itemDesc);
		return SysResult.success();	
	}
	
	@RequestMapping("update")
	public SysResult updateItem(Item item,ItemDesc itemDesc) {
		itemService.updateItem(item,itemDesc);
		return SysResult.success();
	}
	
	/**
	 *  实现商品的参数操作
	 */
	@RequestMapping("delete")
	public SysResult deleteItems(Long[] ids) {
		itemService.deleteItems(ids);
		return SysResult.success();
	}
	
	/**
	 *  实现商品的下架操作
	 */
	@RequestMapping("{moduleName}")
	public SysResult instock(@PathVariable String moduleName, Long...ids) {
		if ("instock".equals(moduleName)) {
			// 下架操作
			int status = 2;
			itemService.updateStatus(status,ids);
		} else if ("reshelf".equals(moduleName)) {
			// 上架操作
			int status = 1;
			itemService.updateStatus(status,ids);
		} else {
			return SysResult.fail();
		}
		return SysResult.success();
	}
	
	/**
	 *  展现商品详情信息
	 *  url： /item/query/item/desc/1474391970
	 *  参数：id信息
	 *  业务操作： 根据商品id信息查询商品详情信息
	 *  返回值数据：检查js中的回调函数
	 */
	@RequestMapping("query/item/desc/{id}")
	public SysResult findItemDescById(@PathVariable Long id) {
		ItemDesc itemDesc = itemService.findItemDescById(id);
		return SysResult.success(itemDesc);
	}
	
}
