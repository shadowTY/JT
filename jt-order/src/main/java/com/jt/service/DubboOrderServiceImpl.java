package com.jt.service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.mapper.OrderItemMapper;
import com.jt.mapper.OrderMapper;
import com.jt.mapper.OrderShippingMapper;
import com.jt.pojo.Order;
import com.jt.pojo.OrderItem;
import com.jt.pojo.OrderShipping;

@Service
public class DubboOrderServiceImpl implements DubboOrderService {
	@Autowired
	private OrderMapper orderMapper;
	@Autowired
	private OrderItemMapper orderItemMapper;
	@Autowired
	private OrderShippingMapper orderShippingMapper;
	/**
	 *  1.准备orderId
	 *  2.分别完成入库操作
	 */
	@Override
	@Transactional
	public String saveOrder(Order order) {
		String uuid = UUID.randomUUID().toString().replace("-", "");
		Date date = new Date();
		// 1. 入库订单
		order.setOrderId(uuid).setCreated(date).setUpdated(date);
		orderMapper.insert(order);
		System.out.println("订单入库成功");
		// 2 .入库物流表
		OrderShipping orderShipping = order.getOrderShipping();
		orderShipping.setOrderId(uuid).setCreated(date).setUpdated(date);
		orderShippingMapper.insert(orderShipping);
		System.out.println("物流表入库成功");
		// 3.入库订单商品
		List<OrderItem> orderItems = order.getOrderItems();
		for (OrderItem orderItem : orderItems) {
			orderItem.setOrderId(uuid).setCreated(date).setUpdated(date);
			orderItemMapper.insert(orderItem);
		}
		System.out.println("订单商品入库成功");
		return uuid;
	}
	@Override
	public Order findOrderById(String id) {
//		Order order = orderMapper.selectById(id);
//		OrderShipping shipping = orderShippingMapper.selectById(id);
//		QueryWrapper<OrderItem> queryWrapper = new QueryWrapper<OrderItem>();
//		queryWrapper.eq("order_id", id);
//		List<OrderItem> orderItems = orderItemMapper.selectList(queryWrapper);
//		order.setOrderItems(orderItems).setOrderShipping(shipping);
		Order order = orderMapper.findOrderById(id);
		return order;
	}

}
