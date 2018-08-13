
$(function(){
/*#############################################search form begin#################################*/	
		
	$("#searchapplyTravelForm #searchButton").on("click",function(){
		$("#tt_ApplyTravel").datagrid('load',{
			'searchStr': $("#searchapplyTravelForm #searchStr").val(),
			'searchCodeStr':$("#searchapplyTravelForm #searchCodeStr").val()		
		});
	});
	
	$("#searchapplyTravelForm #resetButton").on("click",function(){
		$("#searchapplyTravelForm").form('reset');
	});
	
/*#############################################search form end#################################*/		
	
/*##########################grid init begin####################################################*/
/*##########################grid toolbar begin#################################################*/
	var toolbar_tt = [
					{
						iconCls:"icon-edit",
						text:"新增",
						handler:to_addapplyTravel
					}
	          	];
	
/*######################grid toolbar end##############################*/
/*######################grid columns begin##############################*/
	var columns_tt = [
      			[	 				
							{field:'id',title:'id',width:100,hidden:true},						
								{field:"userid",title:"编码",width:180,align:"center"},
								{field:"sex",title:"编码",width:180,align:"center"},
								{field:"nation",title:"编码",width:180,align:"center"},
								{field:"identity",title:"编码",width:180,align:"center"},
								{field:"phone",title:"编码",width:180,align:"center"},
								{field:"address",title:"编码",width:180,align:"center"},
								{field:"isbeen",title:"编码",width:180,align:"center"},
								{field:"people",title:"编码",width:180,align:"center"},
								{field:"pathid",title:"编码",width:180,align:"center"},
								{field:"createtime",title:"编码",width:180,align:"center",formatter:dateTimeFormatter},
								{field:"state",title:"编码",width:180,align:"center"},
					{field:"操作",title:"操作",width:80,align:"left",
	 					formatter:function(value,row,index){
	 					  var str= '<a href="javascript:void(0);" onclick="to_editapplyTravel(\''+row.id+'\');">编辑</a>';
	 					  return str;
	 					}
	 				}	 				
	 			]
	 	];
/*######################grid columns end##############################*/
	
	$("#tt_ApplyTravel").datagrid({
		url:httpUrl+"/applytravel/listApplyTravel.html?&rand=" + Math.random(),
		height:$("#body").height()-$('#search_areaApplyTravel').height()-10,
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
			'searchStr': $("#searchapplyTravelForm #searchStr").val(),
			'searchCodeStr':$("#searchapplyTravelForm #searchCodeStr").val()
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
function to_addapplyTravel(){
	to_editapplyTravel('');
}
/**
 * 编辑
 * @param id
 */
function to_editapplyTravel(id){
	
	var url = httpUrl+"/applytravel/addApplyTravel.html?&rand=" + Math.random()+"&id="+id;
	$('#editApplyTravelDiv').dialog({
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
					handler:save_ApplyTravel
				},
				{
					iconCls:"icon-no",text:"关闭",
					handler:function(){
						$("#editApplyTravelDiv").dialog("close");
				}
		}]
	});
}

function save_ApplyTravel(){
	var formdata = $("#editApplyTravelForm").serialize();
	console.info("formdata");
	console.info(formdata);
	var  url =httpUrl+"/applytravel/saveApplyTravel.html?&rand=" + Math.random();
	 $.ajax({   
		 type: 'POST',
		 dataType: 'json',
		 url: url,  
		 data:$("#editApplyTravelForm").serialize(),
		 success: function(data){ 
			 if(data.code ==="0"){
				 $("#editApplyTravelDiv").dialog("close");
				 $('tt_ApplyTravel').datagrid('reload');
				 $.messager.alert("提示","操作成功","info");
			 }else{
				 $.messager.alert("提示","操作失败","error");
			 }   
		 } 
	});
}


function reloadDataGrid()
{
	$("tt_ApplyTravel").datagrid("reload");
}




/*##########################公用方法##begin############################*/

//监听窗口大小变化
window.onresize = function(){
	setTimeout(domresize,300);
};
//改变表格和查询表单宽高
function domresize(){
	$('tt_ApplyTravel').datagrid('resize',{  
		height:$("#body").height()-$('#search_areaApplyTravel').height()-5,
		width:$("#body").width()
	});
	$('#search_areaApplyTravel').panel('resize',{
		width:$("#body").width()
	});
	$('#detailLoanDiv').dialog('resize',{  
		height:$("#body").height()-25,
		width:$("#body").width()-30
	});
}
 
/*##########################公用方法##end############################*/