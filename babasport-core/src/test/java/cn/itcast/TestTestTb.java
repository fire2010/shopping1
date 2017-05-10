package cn.itcast;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import cn.itcast.common.junit.SpringJunitTest;
import cn.itcast.core.bean.TestTb;
import cn.itcast.core.service.TestTbService;

/**
 * Junit单元测试是基于Spring   注解式
 * @author lx
 *
 */
public class TestTestTb extends SpringJunitTest {

	@Autowired
	private TestTbService testService;
	
	@Test
	public void testAdd() throws Exception {
		TestTb testTb = new TestTb();
		testTb.setName("小明3");
		
		testService.addTestTb(testTb);
	}
	
}
