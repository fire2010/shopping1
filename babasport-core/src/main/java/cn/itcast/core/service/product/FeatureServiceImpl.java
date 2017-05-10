package cn.itcast.core.service.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.itcast.core.bean.product.Feature;
import cn.itcast.core.bean.product.FeatureQuery;
import cn.itcast.core.dao.product.FeatureDao;

/**
 * 材质
 * @author lx
 *
 */
@Service
public class FeatureServiceImpl implements FeatueService{

	@Autowired
	FeatureDao featureDao;
	
	//查询材质 
	public List<Feature> selectFeatureListByQuery(FeatureQuery featureQuery){
		return featureDao.selectByExample(featureQuery);
	}
}
