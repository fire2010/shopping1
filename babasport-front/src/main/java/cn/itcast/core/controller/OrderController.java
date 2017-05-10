package cn.itcast.core.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import cn.itcast.common.web.session.SessionProvider;
import cn.itcast.core.bean.BuyerCart;
import cn.itcast.core.bean.BuyerItem;
import cn.itcast.core.bean.order.Order;
import cn.itcast.core.bean.product.Sku;
import cn.itcast.core.service.order.OrderService;
import cn.itcast.core.service.product.SkuService;
import cn.itcast.core.web.Constants;

/**
 * 提交订单
 * 
 * @author lx
 * 
 */
@Controller
public class OrderController {

	@Autowired
	private SessionProvider sessionProvider;
	@Autowired
	private JedisPool jedisPool;
	@Autowired
	private SkuService skuService;
	@Autowired
	private OrderService orderService;

	// 提交订单按钮
	@RequestMapping(value = "/buyer/confirmOrder.shtml")
	public String confirmOrder(Order order, HttpServletRequest request,
			HttpServletResponse response) {

		// 用户名取购物车
		// 判断用户是否登陆
		String username = sessionProvider.getAttribute(request, response,
				Constants.USER_SESSION);
		
		//订单所属用户
		order.setBuyerId(username);
		
		// 从Reds中取此用户的购物车
		Jedis jedis = jedisPool.getResource();
		List<String> lrange = jedis.lrange("buyerCart:" + username, 0, -1);
		if (null != lrange && lrange.size() > 0) {
			// 创建购物车
			BuyerCart buyerCart = new BuyerCart();

			// 有商品、、判断商品是否有库存
			for (String l : lrange) {
				// 创建 Sku
				Sku sku = new Sku();
				sku.setId(Long.parseLong(l));
				// 创建购物项
				BuyerItem item = new BuyerItem();
				item.setSku(sku);
				// 设置购买数量
				item.setAmount(Integer.parseInt(jedis.hget("buyerItem:"
						+ username, l)));
				// 把购物项添加到购物车中
				buyerCart.addItem(item);
			}
			// 购物车数据装满
			// 把购物车数据装满
			// 购物车中有购物项(有商品 （之前Cookie里的。也可以当前新追加的）
			// 购物车中无购物项（购物项是空的）
			List<BuyerItem> items = buyerCart.getItems();
			if (items.size() > 0) {
				for (BuyerItem buyerItem : items) {
					// 通过SkuID查询（SKu表、颜色表、商品表、图片表）
					Sku sku = skuService.selectSkuById(buyerItem.getSku()
							.getId());
					// 设置全数据的SKu到购物项中
					buyerItem.setSku(sku);
				}
			}
			//保存
			orderService.insertOrder(order, buyerCart);
			//清空购物车
			jedis.del("buyerCart:" + username,"buyerItem:" + username);
		}

		return "product/confirmOrder";
	}
}
