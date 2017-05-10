<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>我的购物车</title>
<link rel="stylesheet" href="/res/css/style.css" />
<script src="/res/js/jquery.js"></script>
<script src="/res/js/com.js"></script>
<script type="text/javascript">
//删除
function delProduct(skuId){
	//刷新页面
	window.location.href = "/shopping/delProduct.shtml?skuId=" + skuId;
}
// + 
function addProductAmount(skuId,upperLimit){
	//获取购买数量
	var num = $("#num" + skuId).val();
	num++;
	//如果此数量大于购买限制时
	if(num > upperLimit){
		alert("不能再买了");
		return;
	}
	//+后数量
	$("#num" + skuId).val(num);
	
	//刷新页面  ajax方式
	var url = "/shopping/productAmount.shtml";
	var params = {"skuId" : skuId,"amount" : 1};
	$.post(url,params,function(data){
		//更改购物车小计
		$("#productAmount").html(data.productAmount);
		//金额
		$("#productPrice").html(data.productPrice);
		//运费
		$("#fee").html(data.fee);
		//应付金额
		$("#totalPrice").html(data.totalPrice);
		
	});
}
//-
function subProductAmount(skuId){
	//获取购买数量
	var num = $("#num" + skuId).val();
	num--;
	//判断是否为0
	if(num == 0){
		//删除
		delProduct(skuId);
	}else{
		//设置回-后的数量
		$("#num" + skuId).val(num);
		
		//刷新页面  ajax方式
		var url = "/shopping/productAmount.shtml";
		var params = {"skuId" : skuId,"amount" : -1};
		$.post(url,params,function(data){
			//更改购物车小计
			$("#productAmount").html(data.productAmount);
			//金额
			$("#productPrice").html(data.productPrice);
			//运费
			$("#fee").html(data.fee);
			//应付金额
			$("#totalPrice").html(data.totalPrice);
			
		});
	}
}
//清空购物车
function clearCart(){
	//
	window.location.href = "/shopping/clearCart.shtml";
}
//结算
function trueBuy(){
 	window.location.href = "/buyer/trueBuy.shtml?returnUrl=" + window.location.pathname;
}
</script>
</head>
<body>
<div class="bar"><div class="bar_w">
	<p class="l">
		<span class="l">
			收藏本网站！北京<a href="#" title="更换">[更换]</a>
		</span>
	</p>
	<ul class="r uls">
		<li class="dev">
			您好,欢迎来到新巴巴运动网！
		</li>
	<li class="dev"><a href="javascript:void(0)" onclick="login()"  title="登陆">[登陆]</a></li>
	<li class="dev"><a href="javascript:void(0)" onclick="register()" title="免费注册">[免费注册]</a></li>
	<li class="dev"><a href="javascript:void(0)" onclick="logout()" title="退出">[退出]</a></li>
	<li class="dev"><a href="javascript:void(0)" onclick="myOrder()" title="我的订单">我的订单</a></li>
	<li class="dev"><a href="#" title="在线客服">在线客服</a></li>
	<li class="dev after"><a href="#" title="English">English</a></li>
	</ul>
</div></div>
<ul class="ul step st3_1">
<li title="1.我的购物车" class="here">1.我的购物车</li>
<li title="2.填写核对订单信息">2.填写核对订单信息</li>
<li title="3.成功提交订单">3.成功提交订单</li>
</ul>
<c:if test="${fn:length(buyerCart.items) != 0 }">
<div class="w ofc case">
	<div class="confirm">
		<div class="tl"></div><div class="tr"></div>
		<div class="ofc pb40">

			<div class="page">
				<b class="l f14 blue pt48">
					我挑选的商品：
				</b>
			</div>
			<table cellspacing="0" class="tab tab4" summary="">
			<thead>
			<tr>
			<th class="wp">商品</th>
			<th>单价（元）</th>
			<th>数量</th>
			<th>操作</th>
			</tr>     
			</thead>
			<tbody>
				<c:forEach items="${buyerCart.items }" var="item">
				<tr>
					<td class="nwp pic">
						<ul class="uls">
							<li>
								<a class="pic" title=" 喜悦2014秋冬新款瑜伽服三件套装 韩版女士瑜珈舞蹈服 愈加服正品送胸垫 酒红+黑+黑 M 开启年终大促 全场优惠到底 买贵就赔 支持货到付款" href="javascript:void(0)"><img alt="" src="${item.sku.product.img.allUrl }"></a>
								<dl>
									<dt><a title=" 喜悦2014秋冬新款瑜伽服三件套装 韩版女士瑜珈舞蹈服 愈加服正品送胸垫 酒红+黑+黑 M 开启年终大促 全场优惠到底 买贵就赔 支持货到付款" href="javascript:void(0)">${item.sku.product.name }--${item.sku.color.name }--${item.sku.size }</a></dt>
									<dd><span class="red">【赠品】</span>
										<p class="box_d bg_gray2 gray"><a title="瑜伽丝带" href="#">瑜伽丝带</a><br></p>
									</dd>
								</dl>
							</li>
						</ul>
					</td>
					<td>￥${item.sku.price }</td>
					<td>
						<a onclick="subProductAmount('${item.sku.id}')" class="inb arr" title="减" href="javascript:void(0);">-</a>
						<input type="text" id="num${item.sku.id }" readonly="readonly" value="${item.amount }" name="" size="1" class="txts">
						<a onclick="addProductAmount('${item.sku.id}','${item.sku.upperLimit }')" class="inb arr" title="加" href="javascript:void(0);">+</a>
						<dl style="margin-left: 20px;color: red">
						<c:if test="${item.isHave }">有货</c:if> 
						<c:if test="${!item.isHave }">无货</c:if> 
						</dl>
					</td>
					<td class="blue"><a onclick="delProduct('${item.sku.id}')" title="删除" href="javascript:void(0);">删除</a></td>
				</tr>
				</c:forEach>
				           
			</tbody>
			</table>
			<div class="page">
				<span class="l">
					<input type="button" onclick="window.open('/product/detail.shtml?id=274')" class="hand btn100x26c" title="继续购物" value="继续购物">
					<input type="button" onclick="clearCart()" class="hand btn100x26c" title="清空购物车" value="清空购物车">
				</span>
				<span class="r box_gray">
					<dl class="total">
						<dt>购物车金额小计：<cite>(共<var id="productAmount">${buyerCart.productAmount }</var>个商品)</cite></dt>
						<dd><em class="l">商品金额：</em>￥<var id="productPrice">${buyerCart.productPrice }</var>元</dd>
						<dd><em class="l">运费：</em>￥<var id="fee">${buyerCart.fee }</var>元</dd>
						<dd class="orange"><em class="l">应付总额：</em>￥<var id="totalPrice">${buyerCart.totalPrice }</var>元</dd>
						<dd class="alg_c"><input type="button" onclick="trueBuy();" class="hand btn136x36a" value="结算" id="settleAccountId"></dd>
					</dl>
				</span>
			</div>
		</div>
	</div>
</div>
</c:if>
<c:if test="${fn:length(buyerCart.items) == 0 }">
<div class="w ofc case" >
	<div class="confirm">
		<div class="tl"></div><div class="tr"></div>
		<div class="ofc pb40" style="text-align: center;height: 200px;margin-top: 80px">
       		 <a href="http://localhost:8080" style="color: red;font-size: 30px;">去首页</a>挑选喜欢的商品
		</div>
	</div>
</div>
</c:if>
<div class="mode">
	<div class="tl"></div><div class="tr"></div>
	<ul class="uls">
		<li class="first">
			<span class="guide"></span>
			<dl>
			<dt title="购物指南">购物指南</dt>
			<dd><a href="#" title="购物流程">购物流程</a></dd>
			<dd><a href="#" title="购物流程">购物流程</a></dd>
			<dd><a href="#" target="_blank" title="联系客服">联系客服</a></dd>
			<dd><a href="#" target="_blank" title="联系客服">联系客服</a></dd>
			</dl>
		</li>
		<li>
			<span class="way"></span>
			<dl>
			<dt title="支付方式">支付方式</dt>
			<dd><a href="#" title="货到付款">货到付款</a></dd>
			<dd><a href="#" title="在线支付">在线支付</a></dd>
			<dd><a href="#" title="分期付款">分期付款</a></dd>
			<dd><a href="#" title="分期付款">分期付款</a></dd>
			</dl>
		</li>
		<li>
			<span class="help"></span>
			<dl>
			<dt title="配送方式">配送方式</dt>
			<dd><a href="#" title="上门自提">上门自提</a></dd>
			<dd><a href="#" title="上门自提">上门自提</a></dd>
			<dd><a href="#" title="上门自提">上门自提</a></dd>
			<dd><a href="#" title="上门自提">上门自提</a></dd>
			</dl>
		</li>
		<li>
			<span class="service"></span>
			<dl>
			<dt title="售后服务">售后服务</dt>
			<dd><a href="#" target="_blank" title="售后策略">售后策略</a></dd>
			<dd><a href="#" target="_blank" title="售后策略">售后策略</a></dd>
			<dd><a href="#" target="_blank" title="售后策略">售后策略</a></dd>
			<dd><a href="#" target="_blank" title="售后策略">售后策略</a></dd>
			</dl>
		</li>
		<li>
			<span class="problem"></span>
			<dl>
			<dt title="特色服务">特色服务</dt>
			<dd><a href="#" target="_blank" title="夺宝岛">夺宝岛</a></dd>
			<dd><a href="#" target="_blank" title="夺宝岛">夺宝岛</a></dd>
			<dd><a href="#" target="_blank" title="夺宝岛">夺宝岛</a></dd>
			<dd><a href="#" target="_blank" title="夺宝岛">夺宝岛</a></dd>
			</dl>
		</li>
	</ul>
</div>
</body>
</html>