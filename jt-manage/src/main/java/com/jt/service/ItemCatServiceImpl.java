package com.jt.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.mapper.ItemCatMapper;
import com.jt.pojo.ItemCat;
import com.jt.util.ObjectMapperUtil;
import com.jt.vo.EasyUITree;

import redis.clients.jedis.Jedis;

@Service
public class ItemCatServiceImpl implements ItemCatService {
	@Autowired
	private ItemCatMapper itemCatMapper;
	// @Autowired		// 引入哨兵监控的缓存对象
	private Jedis jedis;

	@Override
	public ItemCat findItemCatNameById(Long itemCatId) {
		ItemCat itemCat = itemCatMapper.selectById(itemCatId);
		return itemCat;
	}

	@Override
	public List<EasyUITree> findItemCatAll(Long parentId) {
		List<EasyUITree> easyUITreeList = new ArrayList<EasyUITree>();
		// 获取数据库数据！！
		List<ItemCat> itemCatList = findItemCatList(parentId);
		//实现数据转化 一级/二级 closed 三级 open
		for (ItemCat itemCat : itemCatList) {
			EasyUITree easyUITree = new EasyUITree();
			easyUITree.setId(itemCat.getId());
			easyUITree.setText(itemCat.getName());
			String state = itemCat.getIsParent()?"closed":"open";
			easyUITree.setState(state);
			easyUITreeList.add(easyUITree);
		}
		return easyUITreeList;
	}

	private List<ItemCat> findItemCatList(Long parentId) {
		QueryWrapper<ItemCat> queryWrapper = new QueryWrapper<ItemCat>();
		queryWrapper.eq("parent_id", parentId);
		List<ItemCat> list = itemCatMapper.selectList(queryWrapper);
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<EasyUITree> findItemCatCache(Long parentId) {
		List<EasyUITree> list = new ArrayList<>();
		// 1. 根据key查询缓存
		String key = "Item_Cat::" + parentId;
		String result = jedis.get(key);
		// 2. 检查是否有数据
		if (StringUtils.isEmpty(result)) {
			// 缓存中没有数据，查询数据库
			list = findItemCatAll(parentId);
			// 将数据转化为json串
			String json = ObjectMapperUtil.toJSON(list);
			jedis.set(key, json);
			System.out.println("第一次查询，从数据库查询");
		} else {
			list = ObjectMapperUtil.toObject(result, list.getClass());
			System.out.println("从缓存中查询");
		}
		return list;
	}

}
