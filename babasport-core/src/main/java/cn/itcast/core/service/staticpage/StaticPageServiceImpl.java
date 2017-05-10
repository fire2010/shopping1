package cn.itcast.core.service.staticpage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;

import javax.servlet.ServletContext;

import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * 静态化服务类
 * @author lx
 *
 */
public class StaticPageServiceImpl implements StaticPageService,ServletContextAware{

	private Configuration conf;
	public void setFreeMarkerConfigurer(FreeMarkerConfigurer freeMarkerConfigurer) {
		this.conf = freeMarkerConfigurer.getConfiguration();
	}


	//静态化方法
	public void index(Map<String,Object> root,Long id) {
		//获取到全路径
		String path = getPath("/html/product/" + id + ".html");
		//创建文件
		File f = new File(path);
		//父文件夹
		File parentFile = f.getParentFile();
		//判断父文件夹存在不存在
		if(!parentFile.exists()){
			parentFile.mkdirs();
		}
		//输出流
		Writer out = null;
		//处理template
		try {
			//流写的过程 指定编码UTF-8
			out = new OutputStreamWriter(new FileOutputStream(f), "UTF-8");
			//指定模板 返回模板对象 读UTF-8
			Template template = conf.getTemplate("productDetail.html");
			
			template.process(root, out);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			//关流
			if(out != null){
				try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		
	}
	
	//获取真实全路径
	public String getPath(String name){
		return servletContext.getRealPath(name);
	}

	//声明一个ServletContext
	private ServletContext servletContext;

	public void setServletContext(ServletContext servletContext) {
		// TODO Auto-generated method stub
		this.servletContext = servletContext;
		
	}
}
