package cn.itcast;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import cn.itcast.common.junit.SpringJunitTest;

/**
 * 测试Jedis
 * @author lx
 *
 */
public class TestJedis extends SpringJunitTest{
	
	//将Redis集群版的Java接口交由Spring管理
	@Autowired
	private JedisCluster jedisCluster;
	
	@Test
	public void testJedisClusterSpring() throws Exception {
		//保存数据
		jedisCluster.set("xiena", "36");
		//取数据
		String num = jedisCluster.get("xiena");
		System.out.println(num);
	}
	
	
	//集群版Jedis
	@Test
	public void testJedisCluster() throws Exception {
		//创建Jedis的连接池配置类
		JedisPoolConfig config = new JedisPoolConfig();
		//设置最大连接数
		config.setMaxTotal(5);
		//设置最大间隔数
		config.setMaxIdle(3);
		
		//节点集
		Set<HostAndPort> hostAndPorts = new HashSet<HostAndPort>();
		//配置多个IP及端口号
		hostAndPorts.add(new HostAndPort("192.168.200.128",6379));
		hostAndPorts.add(new HostAndPort("192.168.200.128",6380));
		hostAndPorts.add(new HostAndPort("192.168.200.128",6381));
		hostAndPorts.add(new HostAndPort("192.168.200.128",6382));
		hostAndPorts.add(new HostAndPort("192.168.200.128",6383));
		hostAndPorts.add(new HostAndPort("192.168.200.128",6384));
		//创建客户端（集群版）
		JedisCluster jedisCluster = new JedisCluster(hostAndPorts,config);
		
		//保存数据
		jedisCluster.set("hejiong", "40");
		//取数据
		String num = jedisCluster.get("hejiong");
		System.out.println(num);
		
		
	}
	

	@Autowired
	JedisPool jedisPool;
	
	//测试Spring管理的Jedis
	@Test
	public void testJedisAndSpring() throws Exception {
		//客户端
		Jedis redis = jedisPool.getResource();
		
		redis.set("ppp", "haha");
	}
	
	
	//连接池
	@Test
	public void testJedisPool() throws Exception {
		//创建Jedis的连接池配置类
		JedisPoolConfig config = new JedisPoolConfig();
		//设置最大连接数
		config.setMaxTotal(5);
		//设置最大间隔数
		config.setMaxIdle(3);
		
		//创建Jedis客户端
		JedisPool jedisPool = new JedisPool(config,"192.168.200.128",6379);
		//Jedis简单客户端
		Jedis jedis = jedisPool.getResource();
		
		//jedis.set("pname", "cxqd");
		
		String name = jedis.get("pname");
		System.out.println(name);
		
	}
	
	
	
	
	//简单连接
	@Test
	public void testJedis() throws Exception {
		//创建Jedis的客户端
		Jedis jedis = new Jedis("192.168.200.128",6379);
		//设置商品name   春夏秋冬衣服 
		//jedis.set("pname", "春夏秋冬衣服");
		String name = jedis.get("pname");
		System.out.println(name);
		
	}
}
