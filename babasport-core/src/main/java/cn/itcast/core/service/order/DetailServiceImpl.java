package cn.itcast.core.service.order;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.itcast.core.bean.order.Detail;
import cn.itcast.core.bean.order.DetailQuery;
import cn.itcast.core.dao.order.DetailDao;

/**
 * 查询订单详情
 * @author lx
 *
 */
@Service
public class DetailServiceImpl implements DetailService {

	@Autowired
	private DetailDao detailDao;
	//查询订单详情 通过订单ID
	public List<Detail> selectDetailListByOrderId(Long id){
		//创建Detail条件对象
		DetailQuery detailQuery = new DetailQuery();
		//设置订单ID
		detailQuery.createCriteria().andOrderIdEqualTo(id);
		return detailDao.selectByExample(detailQuery);
	}
}
