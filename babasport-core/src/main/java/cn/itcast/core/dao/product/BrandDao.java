package cn.itcast.core.dao.product;

import java.util.List;

import cn.itcast.core.bean.product.Brand;
import cn.itcast.core.bean.product.BrandQuery;

/**
 * 品牌的Dao接口
 * @author lx
 *
 */
public interface BrandDao {

	
	//查询结果集  
	public List<Brand> selectBrandListByQuery(BrandQuery brandQuery);
	
	//查询总条数（符合条件的）
	public Integer selectCounts(BrandQuery brandQuery);
	
	//保存
	public void insertBrandById(Brand brand);
	
	//删除
	public void deleteBrandByIds(Long[] ids);
	
	//查询品牌ID
	public Brand selectBrandById(Long id);
	
	//修改
	public void updateBrandById(Brand brand);
	
	
	
}
