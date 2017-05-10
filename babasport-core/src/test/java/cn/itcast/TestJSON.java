package cn.itcast;

import java.io.StringWriter;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.junit.Test;

import cn.itcast.core.bean.user.Buyer;

/**
 * 购物车转JSON  JSON转购物车
 * @author lx
 *
 */
public class TestJSON {

	@Test
	public void testJSON() throws Exception {
		//转JSON的类
		ObjectMapper om = new ObjectMapper();
		//不转换NUll值
		om.setSerializationInclusion(Inclusion.NON_NULL);
		
		Buyer buyer = new Buyer();
		buyer.setUsername("范冰冰");
		
		//创建字符串写入流
		StringWriter w = new StringWriter();
		
		//转用户对象到JSON字符串
		om.writeValue(w, buyer);
		
		System.out.println(w.toString());
		
		
		//转成用户对象
		Buyer b = om.readValue(w.toString(), Buyer.class);
		
		System.out.println(b);
		
		
		
		
		
	}
}
