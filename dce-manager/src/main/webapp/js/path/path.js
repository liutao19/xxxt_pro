
$(function(){
/*#############################################search form begin#################################*/	
		
	$("#searchpathForm #searchButton").on("click",function(){
		$("#tt_Path").datagrid('load',{
			'searchStr': $("#searchpathForm #searchStr").val(),
			'searchCodeStr':$("#searchpathForm #searchCodeStr").val()		
		});
	});
	
	$("#searchpathForm #resetButton").on("click",function(){
		$("#searchpathForm").form('reset');
	});
	
/*#############################################search form end#################################*/		
	
/*##########################grid init begin####################################################*/
/*##########################grid toolbar begin#################################################*/
	var toolbar_tt = [
					{
						iconCls:"icon-edit",
						text:"新增",
						handler:to_addpath
					}
	          	];
	
/*######################grid toolbar end##############################*/
/*######################grid columns begin##############################*/
	var columns_tt = [
      			[	 				
							{field:'id',title:'id',width:100,hidden:true},						
								{field:"linename",title:"编码",width:180,align:"center"},
								{field:"state",title:"编码",width:180,align:"center"},
								{field:"remake",title:"编码",width:180,align:"center"},
					{field:"操作",title:"操作",width:80,align:"left",
	 					formatter:function(value,row,index){
	 					  var str= '<a href="javascript:void(0);" onclick="to_editpath(\''+row.id+'\');">编辑</a>';
	 					  return str;
	 					}
	 				}	 				
	 			]
	 	];
/*######################grid columns end##############################*/
	
	$("#tt_Path").datagrid({
		url:httpUrl+"/path/listPath.html?&rand=" + Math.random(),
		height:$("#body").height()-$('#search_areaPath').height()-10,
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
			'searchStr': $("#searchpathForm #searchStr").val(),
			'searchCodeStr':$("#searchpathForm #searchCodeStr").val()
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
function to_addpath(){
	to_editpath('');
	$('#editUserFeedbackDiv').dialog({
		title: "新增",
	});
}

/**
 * 删除
 */

function deletePath(id){
	if(!id){
		$.messager.alert("消息","id不能为空");
		return;
	}
	$.messager.confirm("消息","确认删除该路线吗，删除后不可恢复",function(r){
		if(r){
			$.ajax({
				url:httpUrl+"/path/deletePath.html?id="+id,
				type:"post",
				data:{},
				success:function(data){
					if(data.ret==1){
						$.messager.alert("消息","删除成功");
						$('#tableGrid').datagrid('reload');
					}else{
						$.messager.alert("消息","删除失败，请稍后再试");
					}
				}
			});
		}
	});
}

/**
 * 编辑
 * @param id
 */
function to_editpath(id){
	
	var url = httpUrl+"/path/addPath.html?&rand=" + Math.random()+"&id="+id;
	$('#editPathDiv').dialog({
		title: "编辑",
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
					handler:save_Path
				},
				{
					iconCls:"icon-no",text:"关闭",
					handler:function(){
						$("#editPathDiv").dialog("close");
				}
		}]
	});
}

function save_Path(){
	var formdata = $("#editPathForm").serialize();
	console.info("formdata");
	console.info(formdata);
	var  url =httpUrl+"/path/savePath.html?&rand=" + Math.random();
	 $.ajax({   
		 type: 'POST',
		 dataType: 'json',
		 url: url,  
		 data:$("#editPathForm").serialize(),
		 success: function(data){ 
			 if(data.code ==="0"){
				 $("#editPathDiv").dialog("close");
				 $('tt_Path').datagrid('reload');
				 $.messager.alert("提示","操作成功","info");
			 }else{
				 $.messager.alert("提示","操作失败","error");
			 }   
		 } 
	});
}


function reloadDataGrid()
{
	$("tt_Path").datagrid("reload");
}




/*##########################公用方法##begin############################*/

//监听窗口大小变化
window.onresize = function(){
	setTimeout(domresize,300);
};
//改变表格和查询表单宽高
function domresize(){
	$('tt_Path').datagrid('resize',{  
		height:$("#body").height()-$('#search_areaPath').height()-5,
		width:$("#body").width()
	});
	$('#search_areaPath').panel('resize',{
		width:$("#body").width()
	});
	$('#detailLoanDiv').dialog('resize',{  
		height:$("#body").height()-25,
		width:$("#body").width()-30
	});
}
 
/*##########################公用方法##end############################*/