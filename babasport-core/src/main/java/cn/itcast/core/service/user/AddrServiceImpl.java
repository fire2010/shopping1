package cn.itcast.core.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.itcast.core.bean.user.Addr;
import cn.itcast.core.bean.user.AddrQuery;
import cn.itcast.core.dao.user.AddrDao;

/**
 * 收货地址
 * @author lx
 *
 */
@Service
public class AddrServiceImpl implements AddrService{

	
	@Autowired
	private AddrDao addrDao;
	//查询用户的收货地址
	public Addr selectAddrByUserName(String username){
		//创建收货地址的条件对象
		AddrQuery addrQuery = new AddrQuery();
		addrQuery.createCriteria().andBuyerIdEqualTo(username).andIsDefEqualTo(true);
		return addrDao.selectByExample(addrQuery).get(0);
	}
}
