package cn.itcast.common.fastdfs;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.io.FilenameUtils;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.multipart.MultipartFile;

/**
 * 上传图片到分布式文件系统
 * @author lx
 *
 */
public class FastDFSUtils {

	//上传图片  返回路径
	public static String uploadPic(MultipartFile pic) throws Exception{
		//Spring 提供 帮助动态 获取 classpath 下的文件路径
		ClassPathResource resource = new ClassPathResource("fdfs_client.conf");
		//读取配置文件
		ClientGlobal.init(resource.getClassLoader().getResource("fdfs_client.conf").getPath());
		
		//tracker客户端
		TrackerClient trackerClient = new TrackerClient();
		//与Tracker连接
		TrackerServer trackerServer = trackerClient.getConnection();
		
		//创建一个NUll值 的Stage的服务端
		StorageServer storageServer = null;
		
		//创建Stoage的客户端 
		StorageClient1 storageClient1 = new StorageClient1(trackerServer,storageServer);
		
		String ext = FilenameUtils.getExtension(pic.getOriginalFilename());
		
		NameValuePair[] meta_list = new NameValuePair[3];
		
		//图片原始名 
		meta_list[0] = new NameValuePair("filename",pic.getOriginalFilename());
		//图片大小
		meta_list[1] = new NameValuePair("filelength",String.valueOf(pic.getSize()));
		//图片扩展名
		meta_list[2] = new NameValuePair("ext",ext);
		
		//上传图片
		//http://192.168.200.128/group1/M00/00/01/wKjIgFWOYc6APpjAAAD-qk29i78248.jpg
		String path = storageClient1.upload_file1(pic.getBytes(), ext, meta_list);
		
		return path;
	}
}
