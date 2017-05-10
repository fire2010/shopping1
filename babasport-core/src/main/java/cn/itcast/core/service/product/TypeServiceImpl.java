package cn.itcast.core.service.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.itcast.core.bean.product.Type;
import cn.itcast.core.bean.product.TypeQuery;
import cn.itcast.core.dao.product.TypeDao;

/**
 * 类型
 * @author lx
 *
 */
@Service
public class TypeServiceImpl implements TypeService{

	
	@Autowired
	private TypeDao typeDao;
	//加载类型
	public List<Type> selectTypeListByQuery(TypeQuery typeQuery){
		return typeDao.selectByExample(typeQuery);
	}
}
