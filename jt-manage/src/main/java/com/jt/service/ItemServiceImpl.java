package com.jt.service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jt.mapper.ItemDescMapper;
import com.jt.mapper.ItemMapper;
import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.vo.EasyUITable;

@Service
public class ItemServiceImpl implements ItemService {
	@Autowired
	private ItemMapper itemMapper;
	@Autowired
	private ItemDescMapper itemDescMapper;
	
	/**
	 *  需求：进行分页查询
	 *  参数： page 当前页数
	 *  rows： 条数
	 *  sql： select * from tb_item limit 起始位置,页面条数
	 *  查询第N页
	 *  	select * from tb_item limit (n-1)*20,20
	 */
	@Override
	public EasyUITable findItemByPage(Integer page, Integer rows) {
		// 1.获取记录总数
		int total = itemMapper.selectCount(null);
		// 2.分页查询
		int start = (page-1)*rows;
		List<Item> itemList = itemMapper.findItemByPage(start,rows);
		return new EasyUITable(total, itemList);
	}
	
	/**
	 *  参数：页面表单数据 $("#itemAddForm").serialize()
	 *  获取参数名称： 通过反射实例化对象，getXXX()获取属性名称~~~~get去掉~~~XXX首字母小写~~~~获得参数名称
	 *  赋值： 先取值 request.getParameter("参数名称"); 对象.setXXX(value)；
	 */
	@Override
	public void saveItem(Item item, ItemDesc itemDesc) {
		item.setStatus(1);
		item.setCreated(new Date());
		item.setUpdated(item.getCreated());
		itemMapper.insert(item);
		
		itemDesc.setItemId(item.getId());
		itemDesc.setCreated(item.getCreated());
		itemDesc.setUpdated(item.getCreated());
		itemDescMapper.insert(itemDesc);
		
		
	}
	
	@Override
	public void updateItem(Item item,ItemDesc itemDesc) {
		item.setUpdated(new Date());
		itemMapper.updateById(item);
		
		itemDesc.setUpdated(item.getUpdated());
		itemDesc.setItemId(item.getId());
		itemDescMapper.updateById(itemDesc);
		
	}
	
	@Override
	public void deleteItems(Long[] ids) {
		// 数组转化为集合
		List<Long> idList = Arrays.asList(ids);
		itemMapper.deleteBatchIds(idList);
		itemDescMapper.deleteBatchIds(idList);
		
	}
	
	// 批量更新 一般都会将批量更新转化为单独的更新
	@Override
	public void updateStatus(int status, Long... ids) {
		for (Long id : ids) {
			Item item = new Item();
			item.setId(id);
			item.setStatus(status);
			item.setUpdated(new Date());
			itemMapper.updateById(item);
		}
		
	}
	
	@Override
	public ItemDesc findItemDescById(Long id) {
		return itemDescMapper.selectById(id);
	}

	@Override
	public Item findItemById(Long id) {
		return itemMapper.selectById(id);
	}

	@Override
	public List<Item> findItemAll() {
		return itemMapper.selectList(null);
	}

}
