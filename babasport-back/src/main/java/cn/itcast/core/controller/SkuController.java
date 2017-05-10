package cn.itcast.core.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.itcast.core.bean.product.Sku;
import cn.itcast.core.service.product.SkuService;

/**
 * 库存管理
 * 加载库存页面数据
 * 修改库存数据
 * @author lx
 *
 */
@Controller
public class SkuController {

	@Autowired
	private SkuService skuService;
	
	//去库存列表页面
	@RequestMapping(value = "/sku/list.do")
	public String list(Long productId,Model model){
		
		//加载Sku结果集 通过商品ID
		List<Sku> skus = skuService.selectSkuListByProductId(productId);
		//回显到页面
		model.addAttribute("skus", skus);
		
		return "sku/list";
		
	}
	//修改库存 信息  异步提交  JSON 
	@RequestMapping(value = "/sku/update.do")
	public void update(Sku sku ,HttpServletResponse response) throws IOException{
		//修改库存 信息
		skuService.updateSkuById(sku);
		//设置页面上的提示信息   {message:'保存成功!'}
		JSONObject jo = new JSONObject();
		jo.put("message", "保存成功!");
		//数据回调
		response.setContentType("application/json;charset=UTF-8");
		response.getWriter().write(jo.toString());
		
	}
}
