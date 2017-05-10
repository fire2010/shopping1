package cn.itcast.core.service.order;

import java.util.List;

import cn.itcast.core.bean.BuyerCart;
import cn.itcast.core.bean.order.Order;
import cn.itcast.core.bean.order.OrderQuery;

public interface OrderService {
	
	//保存订单
	public void insertOrder(Order order,BuyerCart buyerCart);
	
	//查询订单结果集
	public List<Order> selectOrderListByState(OrderQuery orderQuery);
	
	//通过ID查询
	public Order selectOrderById(Long id);

}
