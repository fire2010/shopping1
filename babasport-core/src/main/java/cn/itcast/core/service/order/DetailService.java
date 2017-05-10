package cn.itcast.core.service.order;

import java.util.List;

import cn.itcast.core.bean.order.Detail;

public interface DetailService {
	
	//查询订单详情 通过订单ID
	public List<Detail> selectDetailListByOrderId(Long id);

}
