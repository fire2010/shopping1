package cn.itcast;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import cn.itcast.common.junit.SpringJunitTest;

/**
 * 测试Solr服务器
 * @author lx
 *
 */
public class TestSolr extends SpringJunitTest {
	
	@Autowired
	private CloudSolrServer cloudSolrServer;
	//交由Spring管理
	@Test
	public void testSolrCloudSpring() throws Exception {
		//保存数据
		//创建一个存储数据的Solr对象
		SolrInputDocument doc = new SolrInputDocument();
		//保存ID
		doc.setField("id", 5);
		//保存Name
		doc.setField("name", "测试集群版Solr交由Spring管理");
		//保存数据
		cloudSolrServer.add(doc);
		//手动提交
		cloudSolrServer.commit();
		
	}
	
	//集群版 Solr测试
	@Test
	public void testSolrCloud() throws Exception {
		//配置三台zk的IP
		String zkHost = "192.168.200.128:2181,192.168.200.133:2181,192.168.200.134:2181";
		//创建集群版客户端
		CloudSolrServer solrServer = new CloudSolrServer(zkHost);
		//设置collection1
		solrServer.setDefaultCollection("collection1");
		//保存数据
		//创建一个存储数据的Solr对象
		SolrInputDocument doc = new SolrInputDocument();
		//保存ID
		doc.setField("id", 4);
		//保存Name
		doc.setField("name", "我是中国人");
		//保存数据
		solrServer.add(doc);
		//手动提交
		solrServer.commit();
		
	}
	
	
	
	@Autowired
	private SolrServer solrServer;
	//Solr交给Spring之 后的测试
	@Test
	public void testSpringSolr() throws Exception {
		
		//创建一个存储数据的Solr对象
		SolrInputDocument doc = new SolrInputDocument();
		//保存ID
		doc.setField("id", 2);
		//保存Name
		doc.setField("name", "测试Solr交由Spring来管理");
		//保存数据
		solrServer.add(doc);
		//手动提交
		solrServer.commit();
		
	}
	
	
	
	@Test
	public void testAdd() throws Exception {
		//连接服务器的请求路径
		String baseURL = "http://192.168.200.128:8080/solr";
		//创建Solr的客户端  连接Solr服务器
		SolrServer solrServer = new HttpSolrServer(baseURL);
		//创建一个存储数据的Solr对象
		SolrInputDocument doc = new SolrInputDocument();
		//保存ID
		doc.setField("id", 2);
		//保存Name
		doc.setField("name", "我是中国人");
		//保存数据
		solrServer.add(doc);
		//手动提交
		solrServer.commit();
		
	}
	//Solr服务器的Java接口查询
	@Test
	public void testQuery() throws Exception {
		//连接服务器的请求路径
		String baseURL = "http://192.168.200.128:8080/solr";
		//创建Solr的客户端  连接Solr服务器
		SolrServer solrServer = new HttpSolrServer(baseURL);
		
		
		//查询
		SolrQuery params = new SolrQuery();
		//关键词
		params.set("q", "*:*");
		//执行查询  返回查询结果集
		QueryResponse response = solrServer.query(params);
		//获取结果集
		SolrDocumentList docs = response.getResults();
		//取出结果的总条数
		long numFound = docs.getNumFound();
		
		System.out.println("总条数：" + numFound);
		
		//结果集遍历
		for (SolrDocument doc : docs) {
			//获取ID
			String id = (String) doc.get("id");
			
			//获取标题
			String name = (String) doc.get("name");
			
			System.out.println(id);
			System.out.println(name);
		}
	}
}
