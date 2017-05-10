package cn.itcast;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import cn.itcast.common.junit.SpringJunitTest;
import cn.itcast.core.bean.product.Product;
import cn.itcast.core.bean.product.ProductQuery;
import cn.itcast.core.dao.product.ProductDao;

/**
 * 测试增强版Mybatis逆向工程
 * @author lx
 *
 */
public class TestProduct extends SpringJunitTest{

	@Autowired
	private ProductDao productDao;
	//测试商品
	@Test
	public void testProduct() throws Exception {
		//查询
		//指定字段查询  . 物理分页  、 设置多个条件 、 id排序 
		// select * from bbs_brand <where> and is_show = 1
		ProductQuery example = new ProductQuery();
		//设置多个条件
		example.createCriteria().andIsShowEqualTo(true);
		//指定字段查询   id name
		example.setFields("id,name");
		//按照id排序 
		example.setOrderByClause("id desc");
		//物理分页  设置页号 、每页数
		example.setPageNo(2);
		example.setPageSize(3);
		
		
		//int countByExample = productDao.countByExample(example);
		
		//System.out.println(countByExample);
		
		//查询一条数据
	/*	Product product = productDao.selectByPrimaryKey(275L);
		System.out.println(product);*/
		//查询多条数据 结果集
		List<Product> products = productDao.selectByExample(example);
		for (Product p : products) {
			System.out.println(p);
		}
		
		//保存
		//修改
		//删除	
		
	}
	
}
