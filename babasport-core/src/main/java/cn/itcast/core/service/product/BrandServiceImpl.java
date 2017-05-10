package cn.itcast.core.service.product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import cn.itcast.common.page.Pagination;
import cn.itcast.core.bean.product.Brand;
import cn.itcast.core.bean.product.BrandQuery;
import cn.itcast.core.dao.product.BrandDao;

/**
 * 品牌管理Service 
 * @author lx
 *
 */
@Service
@Transactional
public class BrandServiceImpl implements BrandService {

	@Autowired
	private BrandDao brandDao;
	
	@Autowired
	private JedisPool jedisPool;
	
	//查询品牌结果集
	public List<Brand> selectBrandListByQuery(BrandQuery brandQuery){
		return brandDao.selectBrandListByQuery(brandQuery);
	}
	//查询品牌分页对象
	public Pagination selectPaginationByQuery(BrandQuery brandQuery){
		//创建分页对象
		//当前页  页号
		//每页数  3
		//总条数（符合条件）
		Pagination pagination = new Pagination(
				brandQuery.getPageNo(),
				brandQuery.getPageSize(),
				brandDao.selectCounts(brandQuery)
				);
		//分页对象中当前页数  设置给品牌条件对象BrandQuery 当前页
		brandQuery.setPageNo(pagination.getPageNo());
		
		//设置查询的结果集（要求符合   3）
		pagination.setList(brandDao.selectBrandListByQuery(brandQuery));
		
		return pagination;
	}
	public void insertBrandById(Brand brand) {
		// TODO Auto-generated method stub
		brandDao.insertBrandById(brand);
	}
	public void deleteBrandByIds(Long[] ids) {
		// TODO Auto-generated method stub
		brandDao.deleteBrandByIds(ids);
	}
	public Brand selectBrandById(Long id) {
		// TODO Auto-generated method stub
		return brandDao.selectBrandById(id);
	}
	public void updateBrandById(Brand brand) {
		// TODO Auto-generated method stub
		//保存品牌数据到Redis中
		Jedis jedis = jedisPool.getResource();
		//创建Map
		Map<String,String> map = new HashMap<String,String>();
		//id
		map.put("id", String.valueOf(brand.getId()));
		//name
		map.put("name", brand.getName());
		
		jedis.hmset("brand:" + brand.getId(),map);
		
		//修改品牌在Mysql中的数据
		brandDao.updateBrandById(brand);
	}
	
}
