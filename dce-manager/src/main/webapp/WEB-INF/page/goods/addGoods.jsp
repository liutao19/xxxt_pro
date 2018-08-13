<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"  prefix="fmt" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>编辑字典</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="Cache-Control" content="no-cache" />
<jsp:include page="../common_easyui_cus.jsp"></jsp:include>
</head>
<body>

<!-- 内容 -->
<form id="editGoodsForm" method="post" action="<c:url value='goods/saveGoods.html'/>"> 
		<div>
		
			<table width="100%" border="0" align="center" cellpadding="3">			  
					<input type="hidden" id="goodsId" name="goodsId" value="${goods.goodsId}"/>
					<%-- <tr>	
						<td align="right">
							<label for="name">goodsSn</label>
						</td>	
						<td>
								<input type="text" id="goodsSn" name="goodsSn" value="${goods.goodsSn}"/>												
						</td>						   
					</tr> --%>
					<tr>	
						<td align="right">
							<label for="name">商品名称：</label>
						</td>	
						<td>
								<input type="text" id="title" name="title" value="${goods.title}"/>												
						</td>						   
					</tr>
					<tr>	
						<td align="right">
							<label for="name">商品图片地址：</label>
						</td>	
						<td>
								<input type="text" id="goodsImg" name="goodsImg" value="${goods.goodsImg}"/>												
						</td>						   
					</tr>
					<%-- <tr>	
						<td align="right">
							<label for="name">goodsThums</label>
						</td>	
						<td>
								<input type="text" id="goodsThums" name="goodsThums" value="${goods.goodsThums}"/>												
						</td>						   
					</tr>
					<tr>	
						<td align="right">
							<label for="name">brandId</label>
						</td>	
						<td>
								<input type="text" id="brandId" name="brandId" value="${goods.brandId}"/>												
						</td>						   
					</tr>
					<tr>	
						<td align="right">
							<label for="name">shopId</label>
						</td>	
						<td>
								<input type="text" id="shopId" name="shopId" value="${goods.shopId}"/>												
						</td>						   
					</tr>
					<tr>	
						<td align="right">
							<label for="name">marketPrice</label>
						</td>	
						<td>
								<input type="text" id="marketPrice" name="marketPrice" value="${goods.marketPrice}"/>												
						</td>						   
					</tr> --%>
					<tr>	
						<td align="right">
							<label for="name">商品价格：</label>
						</td>	
						<td>
								<input type="text" id="shopPrice" name="shopPrice" value="${goods.shopPrice}"/>												
						</td>						   
					</tr>
					<%-- <tr>	
						<td align="right">
							<label for="name">goodsStock</label>
						</td>	
						<td>
								<input type="text" id="goodsStock" name="goodsStock" value="${goods.goodsStock}"/>												
						</td>						   
					</tr>
					<tr>	
						<td align="right">
							<label for="name">saleCount</label>
						</td>	
						<td>
								<input type="text" id="saleCount" name="saleCount" value="${goods.saleCount}"/>												
						</td>						   
					</tr>
					<tr>	
						<td align="right">
							<label for="name">isBook</label>
						</td>	
						<td>
								<input type="text" id="isBook" name="isBook" value="${goods.isBook}"/>												
						</td>						   
					</tr>
					<tr>	
						<td align="right">
							<label for="name">bookQuantity</label>
						</td>	
						<td>
								<input type="text" id="bookQuantity" name="bookQuantity" value="${goods.bookQuantity}"/>												
						</td>						   
					</tr>
					<tr>	
						<td align="right">
							<label for="name">warnStock</label>
						</td>	
						<td>
								<input type="text" id="warnStock" name="warnStock" value="${goods.warnStock}"/>												
						</td>						   
					</tr>
					<tr>	
						<td align="right">
							<label for="name">goodsUnit</label>
						</td>	
						<td>
								<input type="text" id="goodsUnit" name="goodsUnit" value="${goods.goodsUnit}"/>												
						</td>						   
					</tr>
					<tr>	
						<td align="right">
							<label for="name">goodsSpec</label>
						</td>	
						<td>
								<input type="text" id="goodsSpec" name="goodsSpec" value="${goods.goodsSpec}"/>												
						</td>						   
					</tr>
					<tr>	
						<td align="right">
							<label for="name">isSale</label>
						</td>	
						<td>
								<input type="text" id="isSale" name="isSale" value="${goods.isSale}"/>												
						</td>						   
					</tr>
					<tr>	
						<td align="right">
							<label for="name">isBest</label>
						</td>	
						<td>
								<input type="text" id="isBest" name="isBest" value="${goods.isBest}"/>												
						</td>						   
					</tr>
					<tr>	
						<td align="right">
							<label for="name">isHot</label>
						</td>	
						<td>
								<input type="text" id="isHot" name="isHot" value="${goods.isHot}"/>												
						</td>						   
					</tr>
					<tr>	
						<td align="right">
							<label for="name">isRecomm</label>
						</td>	
						<td>
								<input type="text" id="isRecomm" name="isRecomm" value="${goods.isRecomm}"/>												
						</td>						   
					</tr>
					<tr>	
						<td align="right">
							<label for="name">isNew</label>
						</td>	
						<td>
								<input type="text" id="isNew" name="isNew" value="${goods.isNew}"/>												
						</td>						   
					</tr>
					<tr>	
						<td align="right">
							<label for="name">isAdminBest</label>
						</td>	
						<td>
								<input type="text" id="isAdminBest" name="isAdminBest" value="${goods.isAdminBest}"/>												
						</td>						   
					</tr>
					<tr>	
						<td align="right">
							<label for="name">isAdminRecom</label>
						</td>	
						<td>
								<input type="text" id="isAdminRecom" name="isAdminRecom" value="${goods.isAdminRecom}"/>												
						</td>						   
					</tr>
					<tr>	
						<td align="right">
							<label for="name">recommDesc</label>
						</td>	
						<td>
								<input type="text" id="recommDesc" name="recommDesc" value="${goods.recommDesc}"/>												
						</td>						   
					</tr>
					<tr>	
						<td align="right">
							<label for="name">cid1</label>
						</td>	
						<td>
								<input type="text" id="cid1" name="cid1" value="${goods.cid1}"/>												
						</td>						   
					</tr>
					<tr>	
						<td align="right">
							<label for="name">cid2</label>
						</td>	
						<td>
								<input type="text" id="cid2" name="cid2" value="${goods.cid2}"/>												
						</td>						   
					</tr>
					<tr>	
						<td align="right">
							<label for="name">cid3</label>
						</td>	
						<td>
								<input type="text" id="cid3" name="cid3" value="${goods.cid3}"/>												
						</td>						   
					</tr>
					<tr>	
						<td align="right">
							<label for="name">shopCatId1</label>
						</td>	
						<td>
								<input type="text" id="shopCatId1" name="shopCatId1" value="${goods.shopCatId1}"/>												
						</td>						   
					</tr>
					<tr>	
						<td align="right">
							<label for="name">shopCatId2</label>
						</td>	
						<td>
								<input type="text" id="shopCatId2" name="shopCatId2" value="${goods.shopCatId2}"/>												
						</td>						   
					</tr> --%>
					<tr>	
						<td align="right">
							<label for="name">商品内容：</label>
						</td>	
						<td>
								<input type="text" id="goodsDesc" name="goodsDesc" value="${goods.goodsDesc}"/>												
						</td>						   
					</tr>
					<%-- <tr>	
						<td align="right">
							<label for="name">isShopRecomm</label>
						</td>	
						<td>
								<input type="text" id="isShopRecomm" name="isShopRecomm" value="${goods.isShopRecomm}"/>												
						</td>						   
					</tr>
					<tr>	
						<td align="right">
							<label for="name">isIndexRecomm</label>
						</td>	
						<td>
								<input type="text" id="isIndexRecomm" name="isIndexRecomm" value="${goods.isIndexRecomm}"/>												
						</td>						   
					</tr>
					<tr>	
						<td align="right">
							<label for="name">isActivityRecomm</label>
						</td>	
						<td>
								<input type="text" id="isActivityRecomm" name="isActivityRecomm" value="${goods.isActivityRecomm}"/>												
						</td>						   
					</tr>
					<tr>	
						<td align="right">
							<label for="name">isInnerRecomm</label>
						</td>	
						<td>
								<input type="text" id="isInnerRecomm" name="isInnerRecomm" value="${goods.isInnerRecomm}"/>												
						</td>						   
					</tr> --%>
					<tr>	
						<td align="right">
							<label for="name">商品上架状态·：</label>
						</td>	·
						<td>
								<input type="text" id="status" name="status" value="${goods.status}"/>												
						</td>						   
					</tr>
					
					<%-- <tr>	
						<td align="right">
							<label for="name">attrCatId</label>
						</td>	
						<td>
								<input type="text" id="attrCatId" name="attrCatId" value="${goods.attrCatId}"/>												
						</td>						   
					</tr>
					<tr>	
						<td align="right">
							<label for="name">goodsKeywords</label>
						</td>	
						<td>
								<input type="text" id="goodsKeywords" name="goodsKeywords" value="${goods.goodsKeywords}"/>												
						</td>						   
					</tr>
					<tr>	
						<td align="right">
							<label for="name">goodsFlag</label>
						</td>	
						<td>
								<input type="text" id="goodsFlag" name="goodsFlag" value="${goods.goodsFlag}"/>												
						</td>						   
					</tr>
					<tr>	
						<td align="right">
							<label for="name">statusRemarks</label>
						</td>	
						<td>
								<input type="text" id="statusRemarks" name="statusRemarks" value="${goods.statusRemarks}"/>												
						</td>						   
					</tr> --%>
		
			</table>	   
		</div>	
	</form>
	<script type="text/javascript">
		
    function goods_submit(){
    	$.ajax({ 
    			url: "<c:url value='/goods/saveGoods.html'/>", 
    			data: $("#editGoodsForm").serialize(),
    			type:"post",
    			dataType:"json",
    			success: function(ret){
    	   	 		if(ret.code==="0"){
    	   	 			$.messager.confirm("保存成功",
    	   	 				           '是否继续添加？', 
    	   	 				           function(r){
						   	   			   if(r==false){
						   	   				$("#editGoodsDiv").dialog("close");
						   	   			   }
    	   						});
    	   	 		}else{
    	   	 			$.messager.alert("error",ret.msg);
    	   	 		}
    	      	}
    	        });
    }
	
</script>

</body>

</html>