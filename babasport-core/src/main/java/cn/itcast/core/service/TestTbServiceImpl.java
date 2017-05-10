package cn.itcast.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.core.bean.TestTb;
import cn.itcast.core.dao.TestTbDao;

/**
 * 测试事务
 * @author lx
 *
 */
@Service
@Transactional
public class TestTbServiceImpl implements TestTbService {

	@Autowired
	private TestTbDao testTbDao;
	//添加
	public void addTestTb(TestTb testTb){
		
		testTbDao.addTestTb(testTb);
		
		throw new RuntimeException();
	}
	
}
