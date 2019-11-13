package com.jt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jt.anno.Cache_Find;
import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.util.HttpClientService;
import com.jt.util.ObjectMapperUtil;
/**
 *  根据httpClient的方式获取数据
 * @author TY
 *
 */
@Service
public class ItemServiceImpl implements ItemService {
	@Autowired
	private HttpClientService httpClient;
	

	@Override
	@Cache_Find
	public Item findItemById(Long id) {
		String url = "http://manage.jt.com/web/item/findItemById/" + id;
		String itemJson = httpClient.doGet(url);
		return ObjectMapperUtil.toObject(itemJson, Item.class);
	}

	@Override
	@Cache_Find
	public ItemDesc findItemDescById(Long id) {
		String url = "http://manage.jt.com/web/item/findItemDescById/" + id;
		String itemDescJson = httpClient.doGet(url);
		return ObjectMapperUtil.toObject(itemDescJson, ItemDesc.class);
	}

}
