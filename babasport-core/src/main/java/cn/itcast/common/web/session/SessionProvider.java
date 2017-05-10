package cn.itcast.common.web.session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Session提供类
 * @author lx
 *
 */
public interface SessionProvider {

	//四个方法
	//用户的信息（用户名放到Session域中）   
	public void setAttributeForUserName(HttpServletRequest request,HttpServletResponse response,String name,String value);
	//验证码放到Session域中
	public void setAttributeForCode(HttpServletRequest request,HttpServletResponse response,String name,String value);
	//从Session域中获取验证码或用户名
	public String getAttribute(HttpServletRequest request,HttpServletResponse response,String name);
	
	//获取SessionID
	public String getSessionId(HttpServletRequest request,HttpServletResponse response);
}
