
$(function(){
/*#############################################search form begin#################################*/	
		
	$("#searchgoodsForm #searchButton").on("click",function(){
		$("#tt_Goods").datagrid('load',{
			'searchStr': $("#searchgoodsForm #searchStr").val()		
		});
	});
	
	$("#searchgoodsForm #resetButton").on("click",function(){
		$("#searchgoodsForm").form('reset');
	});
	
/*#############################################search form end#################################*/		
	
/*##########################grid init begin####################################################*/
/*##########################grid toolbar begin#################################################*/
	var toolbar_tt = [
					{
						iconCls:"icon-edit",
						text:"新增",
						handler:to_addgoods
					}
	          	];
	
/*######################grid toolbar end##############################*/
/*######################grid columns begin##############################*/
	var columns_tt = [
      			[	 				
							{field:'id',title:'id',width:100,hidden:true},						
								{field:"title",title:"商品名称",width:180,align:"center"},
								{field:"shopPrice",title:"价格",width:180,align:"center"},
								{field:"goodsUnit",title:"单位",width:180,align:"center"},
								{field:"goodsDesc",title:"内容",width:180,align:"center"},
								{field:"goodsImg",title:"商品图片地址",width:180,align:"center"},
								{field:"status",title:"商品上架的状态",width:180,align:"center"},
								{field:"saleTime",title:"商品上架时间",width:180,align:"center",formatter:dateTimeFormatter},
								{field:"createTime",title:"商品创建时间",width:180,align:"center",formatter:dateTimeFormatter},
					{field:"操作",title:"操作",width:80,align:"left",
	 					formatter:function(value,row,index){
	 					  var str= '<a href="javascript:void(0);" onclick="to_editgoods(\''+row.goodsId+'\');">编辑</a>';
	 					  return str;
	 					}
	 				}	 				
	 			]
	 	];
/*######################grid columns end##############################*/
	
	$("#tt_Goods").datagrid({
		url:httpUrl+"/goods/listGoods.html?&rand=" + Math.random(),
		height:$("#body").height()-$('#search_areaGoods').height()-10,
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
		idField:"id",
		columns:columns_tt,
		toolbar:toolbar_tt,
		queryParams:{
			'searchStr': $("#searchgoodsForm #searchStr").val()
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
 * 新增
 * @param id
 */
function to_addgoods(){
	to_editgoods('');
}
/**
 * 编辑
 * @param id
 */
function to_editgoods(id){
	
	var url = httpUrl+"/goods/addGoods.html?&rand=" + Math.random()+"&id="+id;
	$('#editGoodsDiv').dialog({
		title: "新增",
		width: 760,
		height: 500,
		closed: false,
		closable:false,
		cache: false,
		href: url,
		modal: true,
		toolbar:[
				{
					iconCls:"icon-save",text:"保存",
					handler:save_Goods
				},
				{
					iconCls:"icon-no",text:"关闭",
					handler:function(){
						$("#editGoodsDiv").dialog("close");
				}
		}]
	});
}

function save_Goods(){
	var formdata = $("#editGoodsForm").serialize();
	console.info("formdata");
	console.info(formdata);
	var  url =httpUrl+"/goods/saveGoods.html?&rand=" + Math.random();
	 $.ajax({   
		 type: 'POST',
		 dataType: 'json',
		 url: url,  
		 data:$("#editGoodsForm").serialize(),
		 success: function(data){ 
			 if(data.code ==="0"){
				 $("#editGoodsDiv").dialog("close");
				 $('tt_Goods').datagrid('reload');
				 $.messager.alert("提示","操作成功","info");
			 }else{
				 $.messager.alert("提示","操作失败","error");
			 }   
		 } 
	});
}


function reloadDataGrid()
{
	$("tt_Goods").datagrid("reload");
}




/*##########################公用方法##begin############################*/

//监听窗口大小变化
window.onresize = function(){
	setTimeout(domresize,300);
};
//改变表格和查询表单宽高
function domresize(){
	$('tt_Goods').datagrid('resize',{  
		height:$("#body").height()-$('#search_areaGoods').height()-5,
		width:$("#body").width()
	});
	$('#search_areaGoods').panel('resize',{
		width:$("#body").width()
	});
	$('#detailLoanDiv').dialog('resize',{  
		height:$("#body").height()-25,
		width:$("#body").width()-30
	});
}
 
/*##########################公用方法##end############################*/