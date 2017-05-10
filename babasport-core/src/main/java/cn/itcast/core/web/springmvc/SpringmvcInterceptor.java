package cn.itcast.core.web.springmvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import cn.itcast.common.web.session.SessionProvider;
import cn.itcast.core.web.Constants;

/**
 * 拦截器
 * 上下文
 * 处理请求路径（有些页面是不拦截 、而有些是拦截）
 * 用户是否登陆
 * @author lx
 *
 */
public class SpringmvcInterceptor implements HandlerInterceptor{

	//注入Session提供 类
	@Autowired
	private SessionProvider sessionProvider;
	
	//必须登陆路径
	private static final String URI_INTERCEPTOR = "/buyer";
	
	//开发时  项目 处理登陆状态  不用每次都登陆
	private Integer adminId;
	public void setAdminId(Integer adminId) {
		this.adminId = adminId;
	}
	//进入Handler之前 
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		// TODO Auto-generated method stub
		//判断是否为开发阶段
		if(null != adminId){
			//开发阶段
			sessionProvider.setAttributeForUserName(request, response,
					Constants.USER_SESSION, "fbb2014");
			
			//传递页面一个标记 （是否登陆）
			request.setAttribute("isLogin", true);
		}else{
			//判断用户是否登陆
			String username = sessionProvider.getAttribute(request, response, Constants.USER_SESSION);
			//判断当前的请求路径是否必须登陆  
			//URL：http://localhost:8080/buyer/trueBuy.shtml
			//URI:/buyer/trueBuy.shtml
			String requestURI = request.getRequestURI();
			if(requestURI.startsWith(URI_INTERCEPTOR)){
				//必须登陆
				if(null != username){
					//没有Model  但有Request
					//传递页面一个标记 （是否登陆）
					request.setAttribute("isLogin", true);
				}else{
					//传递页面一个标记 （是否登陆）
					request.setAttribute("isLogin", false);
					//重定向到登陆页面
					response.sendRedirect("/shopping/login.shtml?returnUrl=" + 
							request.getParameter("returnUrl"));
					//不能进入Handler方法
					return false;
				}
			}else{
				//可登可不登陆
				if(null != username){
					//没有Model  但有Request
					//传递页面一个标记 （是否登陆）
					request.setAttribute("isLogin", true);
				}else{
					//传递页面一个标记 （是否登陆）
					request.setAttribute("isLogin", false);
				}
			}
			
		}
		
		//正常进入Handler true
		//不让进入Handler false
		return true;
	}
	//进入Handler之后
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		
	}
	//页面渲染后
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

}
