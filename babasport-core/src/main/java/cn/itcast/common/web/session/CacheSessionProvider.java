package cn.itcast.common.web.session;

import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import cn.itcast.common.web.Constants;

/**
 * 远程Session
 * @author lx
 *
 */
public class CacheSessionProvider implements SessionProvider {

	@Autowired
	private JedisPool jedisPool;
	
	//声明分钟变量
	private Integer exp = 30;
	public void setExp(Integer exp) {
		this.exp = exp;
	}
	//远程存储用户名
	public void setAttributeForUserName(HttpServletRequest request,
			HttpServletResponse response, String name, String value) {
		// TODO Auto-generated method stub
		//获取Jedis 
		Jedis jedis = jedisPool.getResource();
		//K V  jedis保存String数据类型
		//K  USER_SESSION + JSESSIONID    V = username
		jedis.set(name + getSessionId(request,response), value);
		//设置过期时间 30分钟
		jedis.expire(name + getSessionId(request,response), 60*exp);
		
		
	}
	//远程存放验证码
	public void setAttributeForCode(HttpServletRequest request,
			HttpServletResponse response, String name, String value) {
		// TODO Auto-generated method stub
		//获取Jedis 
		Jedis jedis = jedisPool.getResource();
		//K V  jedis保存String数据类型
		//K  CODE_SESSION + JSESSIONID    V = code
		jedis.set(name + getSessionId(request,response), value);
		//设置过期时间 1分钟
		jedis.expire(name + getSessionId(request,response), 60*1);
	}

	//远程获取用户名(Constants.USER_SESSION)或验证码(Constants.CODE_SESSION)
	public String getAttribute(HttpServletRequest request,
			HttpServletResponse response, String name) {
		// TODO Auto-generated method stub
		Jedis jedis = jedisPool.getResource();
		//
		return jedis.get(name + getSessionId(request,response));
	}
	//远程获取SessionID
	public String getSessionId(HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		//JSESSIONID
		//购物车
		//CJSESSIONID
		Cookie[] cookies = request.getCookies();
		//判断 
		if(null != cookies && cookies.length > 0){
			//遍历Cookies
			for (Cookie cookie : cookies) {
				//获取cookie sessionID
				if(Constants.CJSESSIONID.equals(cookie.getName())){
					return cookie.getValue();
				}
				
			}
		}
		//生成CJESSIONID的值   32位长度的字符串
		String sessionId = UUID.randomUUID().toString().replaceAll("-","");
		//写到用户的浏览器 的Cookie中
		Cookie cookie = new Cookie(Constants.CJSESSIONID,sessionId);
		//设置Cookie路径
		cookie.setPath("/");
		//不设置时间  让默认（关闭浏览器就消毁）
		response.addCookie(cookie);
		
		return sessionId;
	}

}
