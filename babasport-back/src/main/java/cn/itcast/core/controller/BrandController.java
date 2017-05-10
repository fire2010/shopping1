package cn.itcast.core.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.itcast.common.page.Pagination;
import cn.itcast.core.bean.product.Brand;
import cn.itcast.core.bean.product.BrandQuery;
import cn.itcast.core.service.product.BrandService;

/**
 * 品牌管理
 * 
 * @author lx
 * 
 */
@Controller
public class BrandController {

	@Autowired
	private BrandService brandService;

	// 品牌管理
	@RequestMapping(value = "/brand/list.do")
	public String list(Integer pageNo, String name, Integer isDisplay,
			Model model) {

		// 创建品牌条件对象
		BrandQuery brandQuery = new BrandQuery();
		//拼接条件
		StringBuilder params = new StringBuilder();
		
		//当前页  如果页号为NULl 或小于1 把页号变成1
		brandQuery.setPageNo(Pagination.cpn(pageNo));
		//每页数
		brandQuery.setPageSize(3);
		
		// 判断条件是否为Null 
		if(null != name){
			//把名称设置给条件对象
			brandQuery.setName(name);
			//回显品牌名称
			model.addAttribute("name", name);
			
			params.append("name=").append(name);
		}
		//判断是否可见
		if(null != isDisplay){
			brandQuery.setIsDisplay(isDisplay);
			//回显是否可见
			model.addAttribute("isDisplay", isDisplay);
			
			params.append("&isDisplay=").append(isDisplay);
		}else{
			//设置是否可见的默认值   1 可见
			brandQuery.setIsDisplay(1);
			//回显是否可见 默认值
			model.addAttribute("isDisplay", 1);
			
			params.append("&isDisplay=").append(1);
		}
		//   /brand/list.do?&name=金啦啦&isDisplay=1&pageNo=2

		// 调用service接口中的方法 查询品牌结果集
		Pagination pagination = brandService.selectPaginationByQuery(brandQuery);
		
		String url = "/brand/list.do";
		//页面展示
		pagination.pageView(url, params.toString());
		
		// 传递结果集到页面
		model.addAttribute("pagination", pagination);
		model.addAttribute("pageNo", pagination.getPageNo());

		return "brand/list";
	}
	//去添加页面
	@RequestMapping(value = "/brand/toAdd.do")
	public String toAdd(){
		
		return "brand/add";
	}
	//添加保存
	@RequestMapping(value = "/brand/add.do")
	public String add(Brand brand){
		
		//保存
		brandService.insertBrandById(brand);
		
		return "redirect:/brand/list.do";
	}
	//删除
	@RequestMapping(value = "/brand/deletes.do")
	public String deletes(Long[] ids,Integer pageNo,String name,Integer isDisplay,Model model){
		//删除
		brandService.deleteBrandByIds(ids);
		//判断
		if(null != pageNo){
			model.addAttribute("pageNo", pageNo);
		}
		//判断
		if(null != name){
			model.addAttribute("name", name);
		}
		//判断
		if(null != isDisplay){
			model.addAttribute("isDisplay", isDisplay);
		}
		return "redirect:/brand/list.do";
	}
	//去修改页面
	@RequestMapping(value = "/brand/toEdit.do")
	public String toEdit(Long id,Model model){
		
		//通过ID查询Brand对象
		Brand brand = brandService.selectBrandById(id);
		
		model.addAttribute("brand", brand);
		
		return "brand/edit";
				
	}
	
	//修改
	@RequestMapping(value = "/brand/edit.do")
	public String edit(Brand brand){
		//修改
		brandService.updateBrandById(brand);
		
		
		return "redirect:/brand/list.do";
	}
	
}
