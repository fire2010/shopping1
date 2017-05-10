package cn.itcast.core.service.product;

import cn.itcast.common.page.Pagination;
import cn.itcast.core.bean.product.Product;
import cn.itcast.core.bean.product.ProductQuery;

public interface ProductService {

	// 返回分页对象
	public Pagination selectPaginationByQuery(ProductQuery productQuery);

	// 商品添加
	public void insertProduct(Product product);
	
	//上架
	public void isShow(Long[] ids);
	
	//通过商品ID查询商品及图片
	public Product selectProductAndImgById(Long id);
}
