package cn.itcast.core.bean;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;

import cn.itcast.core.bean.product.Sku;

/**
 * 购物车
 * @author lx
 *
 */
public class BuyerCart {

	//集合 泛型 《购物项>
	private List<BuyerItem> items = new ArrayList<BuyerItem>();
	
	//清空购物车
	public void clearCart(){
		items.clear();
	}
	
	//追加购物项到购物车
	public void addItem(BuyerItem item){
		//判断是否为同款商品
		if(items.contains(item)){
			for (BuyerItem it : items) {
				//判断是同款  并合并购买数量
				if(it.equals(item)){
					//合并购买数量
					it.setAmount(it.getAmount() + item.getAmount());
				}
			}
		}else{
			items.add(item);
		}
	}
	//删除购物项
	public void delProduct(Long skuId){
		//创建 Sku
		Sku sku = new Sku();
		sku.setId(skuId);
		BuyerItem item = new BuyerItem();
		//设置给购物项
		item.setSku(sku);
		//集合删除
		items.remove(item);
	}
	//小计
	
	//商品数量
	@JsonIgnore
	public Integer getProductAmount(){
		Integer result = 0;
		//计算商品数量
		for(BuyerItem it : items){
			result += it.getAmount();
		}
		return result;
	}
	//商品金额
	@JsonIgnore
	public Float getProductPrice(){
		Float result = 0f;
		//计算商品金额
		for(BuyerItem it : items){
			result += it.getAmount()*it.getSku().getPrice();
		}
		return result;
	}
	//运费
	@JsonIgnore
	public Float getFee(){
		Float result = 0f;
		//公司要求
		if(getProductPrice() < 79){
			result = 5f;
		}
		return result;
	}
	//应付金额
	@JsonIgnore
	public Float getTotalPrice(){
		return getFee() + getProductPrice();
	}

	public List<BuyerItem> getItems() {
		return items;
	}

	public void setItems(List<BuyerItem> items) {
		this.items = items;
	}
	
	
	
	
}
