package com.jt.service;

import java.util.List;

import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.vo.EasyUITable;

public interface ItemService {

	EasyUITable findItemByPage(Integer page, Integer rows);

	void updateItem(Item item, ItemDesc itemDesc);

	void deleteItems(Long[] ids);

	void updateStatus(int status, Long...ids);

	void saveItem(Item item, ItemDesc itemDesc);

	ItemDesc findItemDescById(Long id);

	Item findItemById(Long id);

	List<Item> findItemAll();

	



}
