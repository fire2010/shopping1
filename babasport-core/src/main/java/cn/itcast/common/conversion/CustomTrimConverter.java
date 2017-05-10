package cn.itcast.common.conversion;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

/**
 * 处理传参 字符串
 * @author lx
 * String类型转String类型
 *
 */
public class CustomTrimConverter implements Converter<String, String> {

	//转换
	public String convert(String source) {
		// TODO Auto-generated method stub
		try {
			if(null != source){
				//去掉前后空格
				source = source.trim();
				//本身就是空串
				if(!"".equals(source)){
					return source;
				}
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

}
