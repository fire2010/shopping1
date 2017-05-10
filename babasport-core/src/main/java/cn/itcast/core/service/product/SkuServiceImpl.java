package cn.itcast.core.service.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.core.bean.product.Img;
import cn.itcast.core.bean.product.ImgQuery;
import cn.itcast.core.bean.product.Product;
import cn.itcast.core.bean.product.Sku;
import cn.itcast.core.bean.product.SkuQuery;
import cn.itcast.core.dao.product.ColorDao;
import cn.itcast.core.dao.product.ImgDao;
import cn.itcast.core.dao.product.ProductDao;
import cn.itcast.core.dao.product.SkuDao;

/**
 * 库存管理
 * @author lx
 *
 */
@Service
@Transactional
public class SkuServiceImpl implements SkuService {

	@Autowired
	private SkuDao skuDao;
	@Autowired
	private ColorDao colorDao;
	@Autowired
	private ProductDao productDao;
	
	@Autowired
	private ImgDao imgDao;
	
	//查询Sku结果集 通过商品ID
	public List<Sku> selectSkuListByProductId(Long productId){
		SkuQuery skuQuery = new SkuQuery();
		//设置商品ID
		skuQuery.createCriteria().andProductIdEqualTo(productId);
		//返回的Sku结果集进行遍历
		List<Sku> skus = skuDao.selectByExample(skuQuery);
		for (Sku sku : skus) {
			sku.setColor(colorDao.selectByPrimaryKey(sku.getColorId()));
		}
		return skus;
	}
	//修改库存信息 通过SkuID
	public void updateSkuById(Sku sku){
		skuDao.updateByPrimaryKeySelective(sku);
	}
	
	//查询Sku结果集 通过商品ID及库存大于0
	public List<Sku> selectSkuListWithStockByProductId(Long productId){
		SkuQuery skuQuery = new SkuQuery();
		//设置商品ID  及 库存大于0
		skuQuery.createCriteria().andProductIdEqualTo(productId).andStockGreaterThan(0);
		//返回的Sku结果集进行遍历
		List<Sku> skus = skuDao.selectByExample(skuQuery);
		for (Sku sku : skus) {
			sku.setColor(colorDao.selectByPrimaryKey(sku.getColorId()));
		}
		return skus;
	}
	//通过SKUID 查询 SKU数据及颜色  商品 图片
	public Sku selectSkuById(Long id){
		
		//通过ID获取SKu数据
		Sku sku = skuDao.selectByPrimaryKey(id);
		//通过SKU数据中的colorId查询颜色表
		sku.setColor(colorDao.selectByPrimaryKey(sku.getColorId()));
		//通过SKU数据中的商品ID查询商品数据
		Product product = productDao.selectByPrimaryKey(sku.getProductId());
		//通过商品ID及默认查询默认图片
		ImgQuery imgQuery = new ImgQuery();
		imgQuery.createCriteria().andProductIdEqualTo(product.getId()).andIsDefEqualTo(true);
		List<Img> imgs = imgDao.selectByExample(imgQuery);
		product.setImg(imgs.get(0));
		//设置商品对象到SKu对象中
		sku.setProduct(product);
		return sku;
	}
	//通过SKUID查询一个SKU对象
	public Sku selectSkuBySkuId(Long skuId){
		return skuDao.selectByPrimaryKey(skuId);
	}
}
