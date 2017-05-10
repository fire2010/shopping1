package cn.itcast.core.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.itcast.common.page.Pagination;
import cn.itcast.core.bean.product.Brand;
import cn.itcast.core.bean.product.BrandQuery;
import cn.itcast.core.bean.product.Color;
import cn.itcast.core.bean.product.ColorQuery;
import cn.itcast.core.bean.product.Feature;
import cn.itcast.core.bean.product.FeatureQuery;
import cn.itcast.core.bean.product.Product;
import cn.itcast.core.bean.product.ProductQuery;
import cn.itcast.core.bean.product.ProductQuery.Criteria;
import cn.itcast.core.bean.product.Type;
import cn.itcast.core.bean.product.TypeQuery;
import cn.itcast.core.service.product.BrandService;
import cn.itcast.core.service.product.ColorService;
import cn.itcast.core.service.product.FeatueService;
import cn.itcast.core.service.product.ProductService;
import cn.itcast.core.service.product.TypeService;

/**
 * 商品管理
 * 列表
 * 添加
 * 库存
 * 上架
 * @author lx
 *
 */
@Controller
public class ProductController {
	
	@Autowired
	private BrandService brandService;
	@Autowired
	private ProductService productService;

	//查询商品
	@RequestMapping(value = "/product/list.do")
	public String list(Integer pageNo,String name,Long brandId,Boolean isShow,Model model){
		//创建品牌条件
		BrandQuery brandQuery = new BrandQuery();
		//是否可见
		brandQuery.setIsDisplay(1);
		//加载品牌结果集
		List<Brand> brands = brandService.selectBrandListByQuery(brandQuery);
		//回显条件
		model.addAttribute("brands", brands);
		//创建商品条件对象
		ProductQuery productQuery = new ProductQuery();
		Criteria criteria = productQuery.createCriteria();
		
		//设置当前页
		productQuery.setPageNo(Pagination.cpn(pageNo));
		//设置每页数
		productQuery.setPageSize(3);
		
		//按照ID倒排
		productQuery.setOrderByClause("id desc");
		//分页请求路径的参数
		StringBuilder params = new StringBuilder();
		//判断传递的参数不能为Null
		if(null != name){
			criteria.andNameLike("%" + name + "%");
			//回显条件
			model.addAttribute("name", name);
			
			params.append("name=").append(name);
		}
		//品牌ID
		if(null != brandId){
			criteria.andBrandIdEqualTo(brandId);
			//回显条件
			model.addAttribute("brandId", brandId);
			params.append("&brandId=").append(brandId);
		}
		//上下架
		if(null != isShow){
			criteria.andIsShowEqualTo(isShow);
			//回显条件
			model.addAttribute("isShow", isShow);
			
			params.append("&isShow=").append(isShow);
		}else{
			//默认值 
			criteria.andIsShowEqualTo(false);
			//回显条件
			model.addAttribute("isShow", false);
			
			params.append("&isShow=").append(false);
		}
		//查询商品分页对象
		Pagination pagination = productService.selectPaginationByQuery(productQuery);
		
		//设置分页在页面上的展示
		String url = "/product/list.do";
		pagination.pageView(url, params.toString());
		
		//回显条件
		model.addAttribute("pagination", pagination);
		
		return "product/list";
	}
	//去添加页面
	@RequestMapping(value = "/product/toAdd.do")
	public String toAdd(Model model){
		//加载品牌
		//创建品牌条件
		BrandQuery brandQuery = new BrandQuery();
		//是否可见
		brandQuery.setIsDisplay(1);
		//加载品牌结果集
		List<Brand> brands = brandService.selectBrandListByQuery(brandQuery);
		//回显条件
		model.addAttribute("brands", brands);
		//、类型
		TypeQuery typeQuery = new TypeQuery();
		typeQuery.createCriteria().andParentIdNotEqualTo(0L);
		List<Type> types = typeService.selectTypeListByQuery(typeQuery);
		//回显条件
		model.addAttribute("types", types);
		//、材质、
		FeatureQuery featureQuery = new FeatureQuery();
		featureQuery.createCriteria().andIsDelEqualTo(true);
		List<Feature> features = featureService.selectFeatureListByQuery(featureQuery);
		//回显条件
		model.addAttribute("features", features);
		//颜色
		ColorQuery colorQuery = new ColorQuery();
		colorQuery.createCriteria().andParentIdNotEqualTo(0L);
		List<Color> colors = colorService.selectColorListByQuery(colorQuery);
		//回显条件
		model.addAttribute("colors", colors);
		
		return "product/add";
	}
	//添加商品
	@RequestMapping(value = "/product/add.do")
	public String add(Product product){
		//保存   商品表、图片表  事务 最好写到Service
		productService.insertProduct(product);
		return "redirect:/product/list.do";
	}
	//上架
	@RequestMapping(value = "/product/isShow.do")
	public String isShow(Long[] ids){
		//上架 商品信息
		//保存商品信息到Solr服务器
		
		productService.isShow(ids);
		return "redirect:/product/list.do";
	}
	
	@Autowired
	private TypeService typeService;
	@Autowired
	private FeatueService featureService;
	@Autowired
	private ColorService colorService;
}
