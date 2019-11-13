package com.jt.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jt.mapper.CartMapper;
import com.jt.pojo.Cart;

@Service
public class DubboCartServiceImpl implements DubboCartService{
	@Autowired
	private CartMapper cartMapper;

	@Override
	public List<Cart> findCartListByUserId(Long userId) {
		QueryWrapper<Cart> queryWrapper = new QueryWrapper<Cart>();
		queryWrapper.eq("user_id", userId);
		return cartMapper.selectList(queryWrapper);	
	}

	@Override
	public void updateCartNum(Cart cart) {
		Cart cartTemp = new Cart();
		cartTemp.setNum(cart.getNum());
		cartTemp.setUpdated(new Date());
		UpdateWrapper<Cart> updateWrapper = new UpdateWrapper<>();
		updateWrapper.eq("item_id", cart.getItemId()).eq("user_id", cart.getUserId());
		cartMapper.update(cartTemp, updateWrapper);
	}
	
	/**
	 * 对象中属性不为空的元素当作where条件
	 */
	@Override
	public void deleteCart(Cart cart) {
		QueryWrapper<Cart> queryWrapper = new QueryWrapper<Cart>(cart);
		cartMapper.delete(queryWrapper);
		
	}
	
	/**
	 *  如果购物车列表中有该数据，则做数量修改
	 *  如果购物车中没有该数据，则做数据的新增
	 *  
	 */
	@Override
	public void saveCart(Cart cart) {
		Cart cartTemp = new Cart();
		cartTemp.setUserId(cart.getUserId()).setItemId(cart.getItemId());
		QueryWrapper<Cart> queryWrapper = new QueryWrapper<Cart>(cartTemp);
		Cart cartDB = cartMapper.selectOne(queryWrapper);
		if (cartDB == null) {
			// 数据库中没有记录，所以新增
			cart.setCreated(new Date()).setUpdated(cart.getCreated());
			cartMapper.insert(cart);
		} else {
			// 只修改数量
			int num = cart.getNum() + cartDB.getNum();
			cartDB.setNum(num).setUpdated(new Date());
			// 条件只有一个，主键充当where条件，其他数据当作要修改的数据
			// cartMapper.updateById(cartDB);
			cartMapper.updateCart(cartDB);
		}
	
	}

	
	

}
