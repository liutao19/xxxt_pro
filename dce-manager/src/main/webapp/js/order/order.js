
$(function(){
/*#############################################search form begin#################################*/	
		
	$("#searchorderForm #searchButton").on("click",function(){
		var dataUrl = httpUrl+"/order/listOrder.html";
		$("#tt_Order").datagrid('options').url = dataUrl;
		$("#tt_Order").datagrid('load',{
			'userName': $("#searchorderForm #userName").val(),
			'startDate':$("#searchorderForm #startDate").datebox('getValue'),
			'endDate':$("#searchorderForm #endDate").datebox('getValue'),
		});
		
	});
	
	$("#searchorderForm #resetButton").on("click",function(){
		$("#searchorderForm").form('reset');
	});
	
/*#############################################search form end#################################*/		
	
/*##########################grid init begin####################################################*/
/*##########################grid toolbar begin#################################################*/
	/*var toolbar_tt = [
					{
						iconCls:"icon-edit",
						text:"新增",
						handler:to_addorder
					}
	          	];*/
	
/*######################grid toolbar end##############################*/
/*######################grid columns begin##############################*/
	var columns_tt = [
      			[	 				
							{field:'orderid',title:'orderid',width:100,hidden:true},						
								{field:"ordercode",title:"订单编号",width:180,align:"center"},
								{field:"trueName",title:"收货人",width:180,align:"center"},
								{field:"qty",title:"数量",width:180,align:"center"},
								{field:"totalprice",title:"总金额",width:180,align:"center"},
								{field:"createtime",title:"创建时间",width:180,align:"center",formatter:dateTimeFormatter},
								{field:"paystatus",title:"付款状态",width:180,align:"center",
			 						formatter:function(value,row,index){
			 						if(value == "0"){
			 							return "待付";
			 						}else if(value == "1"){
			 							return "已付";
			 						}
			 					}},
								/*{field:"payTime",title:"支付时间",width:180,align:"center",formatter:dateTimeFormatter},*/
								{field:"ordertype",title:"支付方式",width:180,align:"center",
									formatter:function(value,row,index){
				 						if(value == "1"){
				 							return "微信";
				 						}else if(value == "2"){
				 							return "支付宝";
				 						}else if(value == "3"){
				 							return "其他";
				 						}
				 					}},
								{field:"alipayStatus",title:"订单支付状态",width:180,align:"center",
				 						formatter:function(value,row,index){
					 						if(value == "0"){
					 							return "支付失败";
					 						}else if(value == "1"){
					 							return "支付成功";
					 						}else if(value == "2"){
					 							return "未确定";
					 						}
					 					}},
								{field:"address",title:"地址",width:180,align:"center"},
								{field:"orderstatus",title:"订单状态",width:180,align:"center",
									formatter:function(value,row,index){
			 						if(value == "0"){
			 							return "未发货";
			 						}else if(value == "1"){
			 							return "已发货";
			 						}
			 					}},
					{field:"edit",title:"操作",width:80,align:"left",
	 					formatter:function(value,row,index){
	 						if(row.orderstatus == "0"){
	 							return '<a href="javascript:void(0);" onclick="to_editorder(\''+row.orderid+'\');">发货</a>';
	 						}else{
	 							return '';
	 						}
	 					}
	 				}					
	 			]
	 	];
/*######################grid columns end##############################*/
	
	$("#tt_Order").datagrid({
		url:httpUrl+"/order/listOrder.html?&rand=" + Math.random(),
		height:$("#body").height()-$('#search_areaOrder').height()-10,
		width:$("#body").width(),
		rownumbers:true,
		fitColumns:true,
		singleSelect:false,//配合根据状态限制checkbox
		autoRowHeight:true,
		striped:true,
		checkOnSelect:false,//配合根据状态限制checkbox
		selectOnCheck:false,//配合根据状态限制checkbox
		loadFilter : function(data){
			return {
				'rows' : data.datas,
				'total' : data.total,
				'pageSize' : data.pageSize,
				'pageNumber' : data.page
			};
		},
		pagination:true,
		showPageList:true,
		pageSize:20,
		pageList:[10,20,30],
		idField:"orderid",
		columns:columns_tt,
		/*toolbar:toolbar_tt,*/
		queryParams:{
			'userName': $("#searchorderForm #userName").val(),
			'startDate':$("#searchorderForm #startDate").val(),
			'endDate':$("#searchorderForm #endDate").val()
		},
		onLoadSuccess:function(data){//根据状态限制checkbox
			
		}
	});
	
	/*$(window).resize(function (){
		domresize();
	 }); */
/*##########################grid init end###################################################*/
});


/**
 * 发货
 * @param id
 */
function to_editorder(id){
	console.info("订单id："+id);
	var url = httpUrl+"/order/sendOut.html?&rand=" + Math.random()+"&orderid="+id;
	 $.ajax({   
		 type: 'POST',
		 dataType: 'json',
		 url: url,  
		 /*data:$("#editOrderForm").serialize(),*/
		 success: function(data){ 
			 if(data.code =="0"){
				 $('#tt_Order').datagrid('reload');
				 $.messager.alert("提示","发货成功","info");
			 }else{
				 $.messager.alert("提示","发货失败","error");
			 }   
		 } 
	});
}

function save_Order(){
	var formdata = $("#editOrderForm").serialize();
	console.info("formdata");
	console.info(formdata);
	var  url =httpUrl+"/order/saveOrder.html?&rand=" + Math.random();
	 $.ajax({   
		 type: 'POST',
		 dataType: 'json',
		 url: url,  
		 data:$("#editOrderForm").serialize(),
		 success: function(data){ 
			 if(data.code ==="0"){
				 $("#editOrderDiv").dialog("close");
				 $('#tt_Order').datagrid('reload');
				 $.messager.alert("提示","操作成功","info");
			 }else{
				 $.messager.alert("提示","操作失败","error");
			 }   
		 } 
	});
}


function reloadDataGrid()
{
	$("#tt_Order").datagrid("reload");
}




/*##########################公用方法##begin############################*/

//监听窗口大小变化
window.onresize = function(){
	setTimeout(domresize,300);
};
//改变表格和查询表单宽高
function domresize(){
	$('#tt_Order').datagrid('resize',{  
		height:$("#body").height()-$('#search_areaOrder').height()-5,
		width:$("#body").width()
	});
	$('#search_areaOrder').panel('resize',{
		width:$("#body").width()
	});
	$('#detailLoanDiv').dialog('resize',{  
		height:$("#body").height()-25,
		width:$("#body").width()-30
	});
}
 
/*##########################公用方法##end############################*/