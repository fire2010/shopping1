package cn.itcast.core.service.user;

import cn.itcast.core.bean.user.Buyer;

public interface BuyerService {
	//查询用户
	public Buyer selectBuyerByUserName(String username);

}
