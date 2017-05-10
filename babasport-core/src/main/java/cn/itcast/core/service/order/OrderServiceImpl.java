package cn.itcast.core.service.order;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import cn.itcast.core.bean.BuyerCart;
import cn.itcast.core.bean.BuyerItem;
import cn.itcast.core.bean.order.Detail;
import cn.itcast.core.bean.order.Order;
import cn.itcast.core.bean.order.OrderQuery;
import cn.itcast.core.dao.order.DetailDao;
import cn.itcast.core.dao.order.OrderDao;

/**
 * 保存订单
 * 保存订单详情
 * @author lx
 *
 */
@Service
@Transactional
public class OrderServiceImpl implements OrderService {

	@Autowired
	private JedisPool jedisPool;
	@Autowired
	private OrderDao orderDao;
	@Autowired
	private DetailDao detailDao;
	
	//保存订单
	public void insertOrder(Order order,BuyerCart buyerCart){
		// 保存订单表
		Jedis jedis = jedisPool.getResource();
		//生成订单号
		Long id = jedis.incr("oid");
		order.setId(id);
		//运费
		order.setDeliverFee(buyerCart.getFee());
		//订单金额
		order.setOrderPrice(buyerCart.getProductPrice());
		//总金额
		order.setTotalFee(buyerCart.getTotalPrice());
		//判断是否为到付
		if(order.getPaymentWay() == 0){
			//到付  0
			order.setIsPaiy(0);
			
		}else{
			//待付款  1 
			order.setIsPaiy(1);
		}
		//订单状态
		order.setOrderState(0);
		//订单生成时间
		order.setCreateDate(new Date());
		//保存订单
		orderDao.insertSelective(order);
		
		// 保存订单详情表
		List<BuyerItem> items = buyerCart.getItems();
		//遍历购物项
		for (BuyerItem buyerItem : items) {
			//订单详情对象
			Detail detail = new Detail();
			//设置订单ID
			detail.setOrderId(id);
			//商品ID
			detail.setProductId(buyerItem.getSku().getProductId());
			//商品名称
			detail.setProductName(buyerItem.getSku().getProduct().getName());
			//颜色名
			detail.setColor(buyerItem.getSku().getColor().getName());
			//尺码
			detail.setSize(buyerItem.getSku().getSize());
			//价格
			detail.setPrice(buyerItem.getSku().getPrice());
			//购买的数量
			detail.setAmount(buyerItem.getAmount());
			//保存订单详情表
			detailDao.insertSelective(detail);

		}
		
	}
	//查询订单结果集
	public List<Order> selectOrderListByState(OrderQuery orderQuery){
		return orderDao.selectByExample(orderQuery);
	}
	
	//通过ID查询
	public Order selectOrderById(Long id){
		return orderDao.selectByPrimaryKey(id);
	}
}
