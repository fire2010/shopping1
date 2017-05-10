package cn.itcast.core.bean;

import cn.itcast.core.bean.product.Sku;

/**
 * 购物项
 * @author lx
 *
 */
public class BuyerItem {

	
	//SKuID
	private Sku sku;
	//数量
	private Integer amount = 1;
	//是否有货的标识   有货：true  无货：false
	private Boolean isHave = true;
	public Sku getSku() {
		return sku;
	}
	public void setSku(Sku sku) {
		this.sku = sku;
	}
	public Integer getAmount() {
		return amount;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	public Boolean getIsHave() {
		return isHave;
	}
	public void setIsHave(Boolean isHave) {
		this.isHave = isHave;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sku == null) ? 0 : sku.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) //地址比较
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())  //cn.itcast.core.bean.BuyerItem
			return false;
		BuyerItem other = (BuyerItem) obj;
		if (sku == null) {
			if (other.sku != null)
				return false;
		} else if (!sku.getId().equals(other.sku.getId()))
			return false;
		return true;
	}
	
	
	
	
}
