package cn.itcast.core.service.product;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import cn.itcast.common.page.Pagination;
import cn.itcast.core.bean.product.Color;
import cn.itcast.core.bean.product.Img;
import cn.itcast.core.bean.product.ImgQuery;
import cn.itcast.core.bean.product.Product;
import cn.itcast.core.bean.product.ProductQuery;
import cn.itcast.core.bean.product.Sku;
import cn.itcast.core.bean.product.SkuQuery;
import cn.itcast.core.dao.product.ImgDao;
import cn.itcast.core.dao.product.ProductDao;
import cn.itcast.core.dao.product.SkuDao;
import cn.itcast.core.service.staticpage.StaticPageService;

/**
 * 商品Service
 * @author lx
 *
 */
@Service
@Transactional
public class ProductServiceImpl implements ProductService{

	@Autowired
	private ProductDao productDao;
	@Autowired
	private ImgDao imgDao;
	@Autowired
	private SkuDao skuDao;
	
	//返回分页对象
	public Pagination selectPaginationByQuery(ProductQuery productQuery){
		//创建分页对象
		//当前页
		//每页数
		//总条数(符合条件的）
		Pagination pagination = new Pagination(
				productQuery.getPageNo(),
				productQuery.getPageSize(),
				productDao.countByExample(productQuery)
				);
		//超过最大当前页时，使用最后一页
		productQuery.setPageNo(pagination.getPageNo());
		
		//把商品结果中设置图片数据（默认）
		List<Product> products = productDao.selectByExample(productQuery);
		//遍历商品
		for (Product product : products) {
			
			//查询商品ID对应的默认图片
			ImgQuery imgQuery = new ImgQuery();
			//设置商品ID
			imgQuery.createCriteria()
				.andProductIdEqualTo(product.getId())
				.andIsDefEqualTo(true);
			//集合中只有一张图片
			List<Img> imgs = imgDao.selectByExample(imgQuery);
			product.setImg(imgs.get(0));
		}
		//设置结果集
		pagination.setList(products);
		
		return pagination;
	}
	
	@Autowired
	JedisPool jedisPool;
	//商品添加
	public void insertProduct(Product product){
		//设置Redis统一生成的商品编号
		Jedis jedis = jedisPool.getResource();
		//自增长
		Long id = jedis.incr("pno");
		//设置给商品对象
		product.setId(id);
		//设置上下架  默认要求是下架
		product.setIsShow(false);
		//设置是否删除   不删除 1
		product.setIsDel(true);
		//保存商品
		productDao.insertSelective(product);
		//图片保存
		Img img = product.getImg();
		//设置商品ID
		img.setProductId(product.getId());
		//设置是否是默认 
		img.setIsDef(true);
		//保存图片表
		imgDao.insertSelective(img);
		//保存Sku
		// 商品ID 、颜色、尺码 （最小销售）
		for(String color : product.getColors().split(",")){
			//9 转Long
			//创建Sku对象
			Sku sku = new Sku();
			//设置颜色
			sku.setColorId(Long.parseLong(color));
			//设置商品ID
			sku.setProductId(product.getId());
			for(String size : product.getSizes().split(",")){
				//设置尺码 
				sku.setSize(size);
				//设置运费
				sku.setDeliveFee(10f);
				//市场价格
				sku.setMarketPrice(0f);
				//售价
				sku.setPrice(0f);
				//库存
				sku.setStock(0);
				//默认200件  可更改
				sku.setUpperLimit(200);
				//遍历尺码 处  保存Sku
				skuDao.insertSelective(sku);
			}
			
		}
	}
	
	@Autowired
	private SolrServer solrServer;
	//上架
	public void isShow(Long[] ids){
		//遍历商品ID
		for (Long id : ids) {
			//创建商品对象
			Product product = new Product();
			//设置ID
			product.setId(id);
			//设置商品状态  上架true 下架是 false
			product.setIsShow(true);
			//更改商品信息
			productDao.updateByPrimaryKeySelective(product);
			//保存商品信息到Solr服务器
			SolrInputDocument doc = new SolrInputDocument();
			//商品ID
			doc.setField("id", id);
			//商品名称
			Product p = productDao.selectByPrimaryKey(id);
			doc.setField("name_ik", p.getName());
			//图片的Url（默认图片）
			ImgQuery imgQuery = new ImgQuery();
			//商品ID
			//默认图片  1
			imgQuery.createCriteria().andProductIdEqualTo(id).andIsDefEqualTo(true);
			//只有一条数据
			List<Img> imgs = imgDao.selectByExample(imgQuery);
			doc.setField("url", imgs.get(0).getUrl());
			//价格  select price from bbs_sku where product_id = 276 order by price asc limit 0, 1 
			SkuQuery skuQuery = new SkuQuery();
			//指定价格字段进行查询
			skuQuery.setFields("price");
			//设置 商品ID  
			skuQuery.createCriteria().andProductIdEqualTo(id);
			//按照价格降序排序
			skuQuery.setOrderByClause("price asc");
			//设置当前页为1
			skuQuery.setPageNo(1);
			//每页数设置为1
			skuQuery.setPageSize(1);
			List<Sku> skus = skuDao.selectByExample(skuQuery);
			doc.setField("price", skus.get(0).getPrice());
			//品牌ID
			doc.setField("brandId", p.getBrandId());
			//保存到Solr服务器
			//提交
			try {
				solrServer.add(doc);
				solrServer.commit();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//静态化（商品详情页面）
			Map<String,Object> root = new HashMap<String,Object>();
			
			//加载商品及图片
			Product pp = selectProductAndImgById(id);
			root.put("product", pp);
			//Sku结果集 及 颜色
			List<Sku> ss = skuService.selectSkuListWithStockByProductId(id);
			
			//Set集合 去掉重复的
			Set<Color> colors = new HashSet<Color>();
			//遍历Sku
			for (Sku sku : ss) {
				//Sku中的Color对象放到Set<Color> 中
				colors.add(sku.getColor());
			}
			
			root.put("skus", ss);
			root.put("colors", colors);
			
			staticPageService.index(root, id);
		}
	}
	@Autowired
	private StaticPageService staticPageService;
	@Autowired
	private SkuService skuService;
	//通过商品ID查询商品及图片
	public Product selectProductAndImgById(Long id){
		//通过商品ID
		Product product = productDao.selectByPrimaryKey(id);
		//通过商品ID查询图片
		ImgQuery imgQuery = new ImgQuery();
		//商品ID  默认  true  1
		imgQuery.createCriteria().andProductIdEqualTo(id).andIsDefEqualTo(true);
		//返回唯一的一张图片
		List<Img> imgs = imgDao.selectByExample(imgQuery);
		//设置一张图片到商品对象中
		product.setImg(imgs.get(0));
		
		return product;
	}
}
