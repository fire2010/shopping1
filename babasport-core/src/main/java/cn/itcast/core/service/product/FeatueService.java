package cn.itcast.core.service.product;

import java.util.List;

import cn.itcast.core.bean.product.Feature;
import cn.itcast.core.bean.product.FeatureQuery;

public interface FeatueService {

	// 查询材质
	public List<Feature> selectFeatureListByQuery(FeatureQuery featureQuery);
}
