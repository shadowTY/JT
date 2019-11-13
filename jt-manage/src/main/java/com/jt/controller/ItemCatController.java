package com.jt.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jt.anno.Cache_Find;
import com.jt.anno.Cache_Find2;
import com.jt.anno.Cache_Find3;
import com.jt.pojo.ItemCat;
import com.jt.service.ItemCatService;
import com.jt.vo.EasyUITree;

@RestController
@RequestMapping("/item/cat/")
public class ItemCatController {
	@Autowired
	private ItemCatService itemCatService;
	
	/**
	 *  根据商品分类id信息，查询商品分类名称
	 * @param
	 * @return
	 */
	@RequestMapping("queryItemName")
	public String findItemCatNameById(Long itemCatId) {
		ItemCat itemCat = itemCatService.findItemCatNameById(itemCatId);
		return itemCat.getName();
		
	}
	
	/**
	 *  查询商品分类信息，进行树形结构展现
	 *  @RequestParam 设定参数
	 *  name/value: 接收参数名称
	 *  defaultValue： 默认值
	 *  required：是否为必填项
	 */
	@RequestMapping("list")
	@Cache_Find
	// @Cache_Find2()
	// @Cache_Find3
	public List<EasyUITree> findItemCatAll(
			@RequestParam(name = "id",
						defaultValue = "0",
						required = true )Long parentId){
		// parentId= 0L;
		return itemCatService.findItemCatAll(parentId);
		// return itemCatService.findItemCatCache(parentId);
	}
	
	

}
