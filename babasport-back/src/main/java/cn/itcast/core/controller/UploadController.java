package cn.itcast.core.controller;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.fckeditor.response.UploadResponse;

import org.apache.commons.io.FilenameUtils;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import cn.itcast.common.fastdfs.FastDFSUtils;
import cn.itcast.core.web.Constants;

/**
 * 上传图片
 * @author lx
 *
 */
@Controller
public class UploadController {

	//上传图片
	@RequestMapping(value = "/upload/uploadPic.do")
	public void uploadPic(@RequestParam MultipartFile pic,HttpServletRequest request,HttpServletResponse response) throws Exception{
		
/*		System.out.println(pic.getOriginalFilename());
		
		//日期格式化
		DateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		//当前时间
		//图片的名称
		String name = df.format(new Date());
		
		//三位随机数
		Random r = new Random();
		for (int i = 0; i < 3; i++) {
			name += r.nextInt(10);
		}
		//获取扩展名 apache  common.io.jar中已经提供获取方法
		String ext = FilenameUtils.getExtension(pic.getOriginalFilename());
		//  /upload/ + name ".jpg" 全路径
		//相对路径
		String path = "/upload/" + name + "." + ext;
		//上传路径 
		String url = request.getSession().getServletContext().getRealPath("") + path;
		
		//上传图片的方法
		pic.transferTo(new File(url));
		
		//JSON对象
		JSONObject jo = new JSONObject();
		jo.put("path", path);
		
		//1：对响应设置类型 JSON
		response.setContentType("application/json;charset=UTF-8");
		//2:在响应中写入JSON格式的字符串
		response.getWriter().write(jo.toString());*/
		
		//FastDFSUtils 上传图片
		//   group1/M00/00/01/wKjIgFWOYc6APpjAAAD-qk29i78248.jpg
		String path = FastDFSUtils.uploadPic(pic);
		
		//显示图片Url
		String url = Constants.IMG_URL + path;
		
		//JSON对象
		JSONObject jo = new JSONObject();
		jo.put("path", path);
		jo.put("url", url);
		
		//1：对响应设置类型 JSON
		response.setContentType("application/json;charset=UTF-8");
		//2:在响应中写入JSON格式的字符串
		response.getWriter().write(jo.toString());
		
	}
	//上传Fck
	@RequestMapping(value = "/upload/uploadFck.do")
	public void uploadFck(HttpServletRequest request,HttpServletResponse response) throws Exception{
		//强转request  Spring提供的 MultipartRequest
		MultipartRequest mr = (MultipartRequest)request;
		//获取FileMap
		Map<String, MultipartFile> fileMap = mr.getFileMap();
		//遍历Map
		Set<Entry<String, MultipartFile>> entrySet = fileMap.entrySet();
		for (Entry<String, MultipartFile> entry : entrySet) {
			//获取
			MultipartFile pic = entry.getValue();
			
			//FastDFSUtils 上传图片
			//group1/M00/00/01/wKjIgFWOYc6APpjAAAD-qk29i78248.jpg
			String path = FastDFSUtils.uploadPic(pic);
			//显示图片Url
			String url = Constants.IMG_URL + path;
			//返回Fck的上传路径
			UploadResponse ok = UploadResponse.getOK(url);
			
			response.getWriter().print(ok);
			
		}
		
	}
}
