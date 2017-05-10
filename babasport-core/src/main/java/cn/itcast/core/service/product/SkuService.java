package cn.itcast.core.service.product;

import java.util.List;

import cn.itcast.core.bean.product.Sku;

public interface SkuService {

	//查询Sku结果集 通过商品ID
	public List<Sku> selectSkuListByProductId(Long productId);
	
	//修改库存信息 通过SkuID
	public void updateSkuById(Sku sku);
	
	//查询Sku结果集 通过商品ID及库存大于0
	public List<Sku> selectSkuListWithStockByProductId(Long productId);
	
	//通过SKUID 查询 SKU数据及颜色  商品 图片
	public Sku selectSkuById(Long id);
	
	//通过SKUID查询一个SKU对象
	public Sku selectSkuBySkuId(Long skuId);
}
