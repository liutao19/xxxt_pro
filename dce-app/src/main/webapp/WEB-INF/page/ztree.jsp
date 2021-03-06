<%@ page language="java" contentType="text/html; UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1,minimum-scale=1,user-scalable=no" />
	<link rel="stylesheet" type="text/css" href="../css/demo.css" />
	<link rel="stylesheet" type="text/css" href="../css/zTreeStyle.css" />
	<script type="text/javascript" src="../js/jquery-1.9.1.min.js"></script>
	<!-- <script type="text/javascript" src="../js/jquery-1.4.4.min.js"></script> -->
	<script type="text/javascript" src="../js/jquery.ztree.core.js"></script>
	<SCRIPT type="text/javascript">

	var userId = "${userId}";
	var ts = "${ts}";
	var sign = "${sign}";
	
	 var setting = {
		data: {
			key : {  
                name : "user_name"  
            },
			simpleData: {
				idKey: "userid",//使用简单必须标明的的节点对应字段  
	            pIdKey: "refereeid",//使用简单必须标明的的父节点对应字段  
				enable: true
			}
		},
		async: {  
            enable: true,//异步加载  
            //请求地址，可用function动态获取  
            url:"../memberAccount/listMyRef.do",  
            contentType:"application/x-www-form-urlencoded",//按照标准的 Form 格式提交参数  
            autoParam:["userid"],//提交的节点参数，可用“id=xx”取请求提交时的别名  
            otherParam:{
            	"userId":userId,
            	"ts":ts,
            	"sign":sign
            	},//提交的其他参数,json的形式  
            dataType:"json",//返回数据类型  
            type:"post",//请求方式  
            dataFilter: filter//数据过滤  
        }, 
		callback: {
			onClick: zTreeOnClick
		}
	};

	 function filter(treeId, parentNode, childNodes) {
		 	console.log("treeId:" + treeId);
		 	console.log("parentNode:" + parentNode);
		 	console.log("childNodes:" + childNodes);
		 	console.log("childNodes.data:" + childNodes.data);
			if (!childNodes) return null;
			
			var data = childNodes.data;
			for (var i=0, l=data.length; i<l; i++) {
				data[i].icon = "../css/img/t1.png";
				data[i].isParent = true;
			}
			return data;
	}
	 function zTreeOnClick(event, treeId, treeNode){
		 var zTree_Menu = $.fn.zTree.getZTreeObj("treeDemo");
		 
		 zTree_Menu.expandNode(treeNode);
	 }

	$(document).ready(function(){
		
		console.log(userId + "," + ts + "," + sign);
		/*  $.ajax({
            url :'../memberAccount/listMyRef.do',
            data : {
            	"userId":userId,
            	"ts":ts,
            	"sign":sign,
            	"userid":userId
            	} , 
            type : 'post',
            dataType : 'json', //返回数据类型
            beforeSend : function(){

            },
            success : function(data){
            	console.log("success:" + data.data);
            }}); */ 
		
		$.fn.zTree.init($("#treeDemo"), setting);
		
		
		$("#liveCommunity").on("touchstart",function(){
			
		});
	}); 
	

	

	</SCRIPT>
	
</HEAD>

<BODY>
<div class="content_wrap">
	<div class="zTreeDemoBackground center">
		<ul id="treeDemo" class="ztree" style="width: 100%;height:100%;"></ul>
	</div>
</div>
</BODY>
</html>