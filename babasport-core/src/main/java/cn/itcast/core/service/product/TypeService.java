package cn.itcast.core.service.product;

import java.util.List;

import cn.itcast.core.bean.product.Type;
import cn.itcast.core.bean.product.TypeQuery;

public interface TypeService {

	
	//加载类型
	public List<Type> selectTypeListByQuery(TypeQuery typeQuery);
}
