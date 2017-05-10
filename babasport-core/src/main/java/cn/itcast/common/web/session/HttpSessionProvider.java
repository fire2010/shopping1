package cn.itcast.common.web.session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Session提供类的实现类
 * @author lx
 *
 */
public class HttpSessionProvider implements SessionProvider{

	public void setAttributeForUserName(HttpServletRequest request,
			HttpServletResponse response, String name, String value) {
		// TODO Auto-generated method stub
		//获取Session对象
		HttpSession session = request.getSession();
		//把用户名放到Session域中
		session.setAttribute(name, value);
		
	}

	public void setAttributeForCode(HttpServletRequest request,
			HttpServletResponse response, String name, String value) {
		// TODO Auto-generated method stub
		//获取Session对象
		HttpSession session = request.getSession();
		//把验证码放到Session域中
		session.setAttribute(name, value);
	}

	public String getAttribute(HttpServletRequest request,
			HttpServletResponse response, String name) {
		// TODO Auto-generated method stub
		//获取Session对象（之前存放用户或验证码信息的那个Session、不要新生成的Session）
		HttpSession session = request.getSession(false);
		//判断之前的Session是否存在
		if(null != session){
			return (String) session.getAttribute(name);
		}
		return null;
	}

	public String getSessionId(HttpServletRequest request,HttpServletResponse response) {
		// TODO Auto-generated method stub
		return request.getSession().getId();
	}

}
