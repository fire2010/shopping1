package cn.itcast.core.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 后台管理中心   内部转发视图
 * @author lx
 *
 */
@Controller
@RequestMapping(value = "/control")
public class CenterController {

	
	//入口页面  index.jsp
	@RequestMapping(value = "/index.do")
	public String index(){
		
		return "index";
	}
	//top
	@RequestMapping(value = "/top.do")
	public String top(){
		
		return "top";
	}
	//main
	@RequestMapping(value = "/main.do")
	public String main(){
		
		return "main";
	}
	//left
	@RequestMapping(value = "/left.do")
	public String left(){
		
		return "left";
	}
	//right
	@RequestMapping(value = "/right.do")
	public String right(){
		
		return "right";
	}
	//第一个Springmvc
/*	@RequestMapping(value = "/test/index.do")
	public String index(){
		
		return "head";
	}*/
}
