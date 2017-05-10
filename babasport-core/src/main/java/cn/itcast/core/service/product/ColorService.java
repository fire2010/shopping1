package cn.itcast.core.service.product;

import java.util.List;

import cn.itcast.core.bean.product.Color;
import cn.itcast.core.bean.product.ColorQuery;

public interface ColorService {

	// 查询颜色
	public List<Color> selectColorListByQuery(ColorQuery colorQuery);
}
