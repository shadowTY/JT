package com.jt.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.pojo.Cart;
import com.jt.pojo.User;
import com.jt.service.DubboCartService;
import com.jt.util.UserThreadLocalUtil;
import com.jt.vo.SysResult;

@Controller
@RequestMapping("/cart")
public class CartController {
	@Reference(check = false)
	private DubboCartService cartService;
	
	/**
	 *  要求回显购物车列表信息
	 * @return
	 */
	@RequestMapping("/show")
	public String show(Model model) {
		// User user = (User) request.getAttribute("JT_USER");
		// Long userId = user.getId();
		User user = UserThreadLocalUtil.get();
		Long userId = user.getId();
		List<Cart> cartList = cartService.findCartListByUserId(userId);
		model.addAttribute("cartList", cartList);
		return "cart";
	}
	
	@RequestMapping("/update/num/{itemId}/{num}")
	@ResponseBody		// 表示返回json数据
	public SysResult updateCartNum(Cart cart) {
		User user = UserThreadLocalUtil.get();
		Long userId = user.getId();
		cart.setUserId(userId);
		cartService.updateCartNum(cart);
		return SysResult.success();
	}
	
	@RequestMapping("/delete/{itemId}")
	public String deleteCart(Cart cart) {
		User user = UserThreadLocalUtil.get();
		Long userId = user.getId();
		cart.setUserId(userId);
		cartService.deleteCart(cart);
		return "redirect:/cart/show.html";
	}
	
	@RequestMapping("/add/{itemId}")
	public String saveCart(Cart cart) {
		User user = UserThreadLocalUtil.get();
		Long userId = user.getId();
		cart.setUserId(userId);
		cartService.saveCart(cart);
		return "redirect:/cart/show.html";
	}

}
