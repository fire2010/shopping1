package cn.itcast.core.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import cn.itcast.common.page.Pagination;
import cn.itcast.core.bean.product.Brand;
import cn.itcast.core.bean.product.Color;
import cn.itcast.core.bean.product.Img;
import cn.itcast.core.bean.product.Product;
import cn.itcast.core.bean.product.ProductQuery;
import cn.itcast.core.bean.product.Sku;
import cn.itcast.core.service.product.ProductService;
import cn.itcast.core.service.product.SkuService;

/**
 * 前台管理
 * 商品的检索
 * 商品的详情页面
 * @author lx
 *
 */
@Controller
public class ProductController {

	@Autowired
	private SolrServer solrServer;
	@Autowired
	private JedisPool jedisPool;
	//去检索页面
	@RequestMapping(value = "/product/display/list.shtml")
	public String list(Integer pageNo,String keyword,Long brandId,String price,Model model) throws Exception{
		//从Redis服务器中加载多个品牌
		Jedis jedis = jedisPool.getResource();
		//取多个
		Set<String> keys = jedis.keys("brand:*");
		//创建品牌结果集
		List<Brand> brands = new ArrayList<Brand>();
		//遍历
		for (String key : keys) {
			//创建 品牌对象
			Brand brand = new Brand();
			//取一个品牌全部 id name
			//Map<String, String> hgetAll = jedis.hgetAll(key);
			String id = jedis.hget(key, "id");
			brand.setId(Long.parseLong(id));
			//取名称
			String name = jedis.hget(key, "name");
			brand.setName(name);
			//加入结果集中
			brands.add(brand);
		}
		//显示页面上
		model.addAttribute("brands", brands);
		
		//查询条件对象
		ProductQuery productQuery = new ProductQuery();
		//设置当前页
		productQuery.setPageNo(Pagination.cpn(pageNo));
		//设置每页数
		productQuery.setPageSize(4);
		
		//创建分页对象中需要的的条件参数容器
		StringBuilder sb = new StringBuilder();
		
		//查询
		SolrQuery params = new SolrQuery();
		//关键词
		params.set("q", "name_ik:" + keyword);
		sb.append("keyword=").append(keyword);
		
		//设置开始行
		params.setStart(productQuery.getStartRow());
		//设置每页数
		params.setRows(productQuery.getPageSize());
		
		//设置价格从低到高
		params.addSort("price", ORDER.asc);
		
		//已选条件标识
		Boolean flag = false;
		//已选条件存储
		Map<String,String> query = new HashMap<String,String>();
		
		//判断 品牌ID
		if(null != brandId){
			//添加过滤条件
			params.addFilterQuery("brandId:" + brandId);
			//回显品牌ID
			model.addAttribute("brandId", brandId);
			//已选条件标识
			flag = true;
			//添加过滤条件到query
			query.put("品牌", jedis.hget("brand:" + brandId, "name"));
		}
		//判断价格
		if(null != price){
			//添加价格过滤条件
			String[] p = price.split("-");
			if(p.length == 2){
				//第一种： 0-79 
				//开始价
				Float pStart = new Float(p[0]);
				//结束价格
				Float pEnd = new Float(p[1]);
				//价格过滤
				params.addFilterQuery("price:[" + pStart +" TO " + pEnd + "]");
				//添加过滤条件到query
				query.put("价格",price);
			}else if(p.length == 1){
				//第二种：600~无限大
				//开始价
				Float pStart = new Float(p[0]);
				//结束价格
				Float pEnd = new Float("99999999");
				//价格过滤
				params.addFilterQuery("price:[" + pStart +" TO " + pEnd + "]");
				//添加过滤条件到query
				query.put("价格",price + "以上");
			}
			//回显价格
			model.addAttribute("price", price);
			//已选条件标识
			flag = true;
		}
		//回显已选条件标识
		model.addAttribute("flag", flag);
		//已选条件回显到页面
		model.addAttribute("query", query);
		
		//设置高亮
		//开启高亮
		params.setHighlight(true);
		//设置高亮字段
		params.addHighlightField("name_ik");
		//设置高亮字段的前缀 <span style='color:red'>瑜伽服</span>
		params.setHighlightSimplePre("<span style='color:red'>");
		//设置高亮字段的后缀
		params.setHighlightSimplePost("</span>");
		
		//Map<String,Map<String,List<String>>> highlightMap
		//highlightMap.get(276)  ==> Map<String,List<String>> map
		//map.get("name_ik")  ==> List<String> list
		// list.get(0) == > 商品名称
		
		
		//执行查询  返回查询结果集
		QueryResponse response = solrServer.query(params);
		
		
		//获取结果集
		SolrDocumentList docs = response.getResults();
		//取出结果的总条数
		long numFound = docs.getNumFound();
		
		System.out.println("总条数：" + numFound);
		
		//创建商品结果集
		List<Product> products = new ArrayList<Product>();
		//遍历SolrDocumentList
		for(SolrDocument doc : docs){
			//创建商品对象
			Product product = new Product();
			//获取商品ID
			String id = (String) doc.get("id");
			//设置ID给商品对象
			product.setId(Long.parseLong(id));
			//获取商品名称
			Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
			Map<String, List<String>> map = highlighting.get(id);
			List<String> list = map.get("name_ik");
	/*		String name = (String) doc.get("name_ik");*/
			//设置name给商品对象
			product.setName(list.get(0));
			//图片URL
			String url = (String) doc.get("url");
			Img img = new Img();
			img.setUrl(url);
			//把图片对象设置给商品对象
			product.setImg(img);
			//价格
			//设置价格到商品对象中
			product.setPrice((Float) doc.get("price"));
			//品牌ID
			//设置品牌ID
			product.setBrandId(Long.parseLong(String.valueOf((Integer) doc.get("brandId"))));
			//设置商品对象到结果集中
			products.add(product);
		}
		
		//创建分页对象
		//当前页
		//每页数
		//总条数
		Pagination pagination = new Pagination(
				productQuery.getPageNo(),
				productQuery.getPageSize(),
				(int)numFound
				);
		
		//设置商品结果集给分页对象
		pagination.setList(products);
		//设置分页在页面上的展示
		String url = "/product/display/list.shtml";
		//设置URl及条件参数
		pagination.pageView(url, sb.toString());
		
		//回显分页对象到页面
		model.addAttribute("pagination", pagination);
		
		
		return "product/product";
	}
	//去商品详情页面
	@RequestMapping(value = "/product/detail.shtml")
	public String detail(Long id,Model model){
		
		//加载商品及图片
		Product product = productService.selectProductAndImgById(id);
		model.addAttribute("product", product);
		//Sku结果集 及 颜色
		List<Sku> skus = skuService.selectSkuListWithStockByProductId(id);
		
		//Set集合 去掉重复的
		Set<Color> colors = new HashSet<Color>();
		//遍历Sku
		for (Sku sku : skus) {
			//Sku中的Color对象放到Set<Color> 中
			colors.add(sku.getColor());
		}
		
		model.addAttribute("skus", skus);
		model.addAttribute("colors", colors);
		
		
		return "product/productDetail";
	}
	@Autowired
	private ProductService productService;
	@Autowired
	private SkuService skuService;
	
}
