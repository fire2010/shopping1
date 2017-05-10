package cn.itcast.core.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 内部转发 模块对象的身体 及身体右
 * @author lx
 *
 */
@Controller
@RequestMapping(value = "/control")
public class FrameController {

	//商品 身体
	@RequestMapping(value = "/frame/product_main.do")
	public String product_main(){
		return "frame/product_main";
	}
	//商品 身体 左
	@RequestMapping(value = "/frame/product_left.do")
	public String product_left(){
		return "frame/product_left";
	}
	//订单 身体
	@RequestMapping(value = "/frame/order_main.do")
	public String order_main(){
		return "frame/order_main";
	}
	//订单 身体 左
	@RequestMapping(value = "/frame/order_left.do")
	public String order_left(){
		return "frame/order_left";
	}
	
}
