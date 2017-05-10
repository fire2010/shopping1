package cn.itcast.core.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.itcast.core.bean.order.Detail;
import cn.itcast.core.bean.order.Order;
import cn.itcast.core.bean.order.OrderQuery;
import cn.itcast.core.bean.order.OrderQuery.Criteria;
import cn.itcast.core.bean.user.Addr;
import cn.itcast.core.service.order.DetailService;
import cn.itcast.core.service.order.OrderService;
import cn.itcast.core.service.user.AddrService;

/**
 * 订单管理
 * 订单列表
 * 查看
 * @author lx
 *
 */
@Controller
public class OrderController {
	
	@Autowired
	private OrderService orderService;

	//订单列表
	@RequestMapping(value = "/order/list.do")
	public String list(Integer orderState,Integer isPaiy,Model model){
		//创建订单的查询条件
		OrderQuery orderQuery = new OrderQuery();
		//增加一个条件对象
		Criteria createCriteria = orderQuery.createCriteria();
		//判断订单状态是否为Null
		if(null != orderState){
			//设置订单状态
			createCriteria.andOrderStateEqualTo(orderState);
		}
		//判断支付状态
		if(null != isPaiy){
			//设置支付状态
			createCriteria.andIsPaiyEqualTo(isPaiy);
		}
		//通过状态查询订单结果集
		List<Order> orders = orderService.selectOrderListByState(orderQuery);
		model.addAttribute("orders", orders);
		
		return "order/list";
	}
	
	//查看
	@RequestMapping(value = "/order/view.do")
	public String view(Long id,Model model){
		
		
		//通过ID查订单对象
		Order order = orderService.selectOrderById(id);
		model.addAttribute("order", order);
		//通过订单里的用户ID 查询默认收货地址
		Addr addr = addrService.selectAddrByUserName(order.getBuyerId());
		model.addAttribute("addr", addr);
		//通过订单ID  查询订单详情表中数据 List结果集
		List<Detail> details = detailService.selectDetailListByOrderId(id);
		model.addAttribute("details", details);
		
		return "order/view";
	}
	
	@Autowired
	private AddrService addrService;
	@Autowired
	private DetailService detailService;
	
}
