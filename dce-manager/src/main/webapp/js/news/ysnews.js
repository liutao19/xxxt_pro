var basePath="/dce-manager";
$(function(){
/*#############################################search form begin#################################*/	
		
	$("#searchysNewsForm #searchButton").on("click",function(){
		var dataUrl = basePath+"/ysnews/listYsNews.html";
		$("#tt_YsNews").datagrid('options').url = dataUrl;
		$("#tt_YsNews").datagrid('load',{
			'title': $("#searchysNewsForm #title").val(),
			'createName': $("#searchysNewsForm #createName").val(),
			'updateName': $("#searchysNewsForm #updateName").val(),
			'startDate':$("#searchysNewsForm #startDate").datebox('getValue'),
			'endDate':$("#searchysNewsForm #endDate").datebox('getValue')
		});
	});
	
	$("#searchysNewsForm #resetButton").on("click",function(){
		$("#searchysNewsForm").form('reset');
	});
	
/*#############################################search form end#################################*/		
	
/*##########################grid init begin####################################################*/
/*##########################grid toolbar begin#################################################*/
	var toolbar_tt = [
					{
						iconCls:"icon-edit",
						text:"新增",
						handler:to_addysNews
					}
	          	];
	
/*######################grid toolbar end##############################*/
/*######################grid columns begin##############################*/
	var columns_tt = [
      			[	 				
							{field:'id',title:'id',width:100,hidden:true},						
								{field:"title",title:"标题",width:180,align:"center"},
								{field:"image",title:"图片",width:180,align:"center"},
								{field:"content",title:"内容",width:180,align:"center"},
								{field:"author",title:"作者",width:180,align:"center"},
								{field:"topNews",title:"置顶新闻",width:180,align:"center"},
								{field:"remark",title:"备注",width:180,align:"center"},
								{field:"status",title:"状态",width:180,align:"center"},
								{field:"createDate",title:"创建日期",width:180,align:"center",formatter:dateTimeFormatter},
								{field:"createName",title:"创建人",width:180,align:"center"},
								{field:"updateDate",title:"修改日期",width:180,align:"center",formatter:dateTimeFormatter},
								{field:"updateName",title:"修改人",width:180,align:"center"},
					{field:"操作",title:"操作",width:180,align:"center",
	 					formatter:function(value,row,index){
	 					  var str= '<a href="javascript:void(0);" onclick="to_editysNews(\''+row.id+'\');">编辑</a>  <a href="javascript:void(0);" onclick="deleteNotice(\''+row.id+'\');">删除</a>';
	 					  return str;
	 					}
	 				}	 				
	 			]
	 	];
/*######################grid columns end##############################*/
	
	$("#tt_YsNews").datagrid({
		url:httpUrl+"/ysnews/listYsNews.html?&rand=" + Math.random(),
		height:$("#body").height()-$('#search_areaYsNews').height()-10,
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
			'searchStr': $("#searchysNewsForm #searchStr").val(),
			'searchCodeStr':$("#searchysNewsForm #searchCodeStr").val()
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
function to_addysNews(){
	to_editysNews('');
}
/**
 * 编辑
 * @param id
 */
function to_editysNews(id){
	
	var url = httpUrl+"/ysnews/addYsNews.html?&rand=" + Math.random()+"&id="+id;
	$('#editYsNewsDiv').dialog({
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
					handler:save_YsNews
				},
				{
					iconCls:"icon-no",text:"关闭",
					handler:function(){
						$("#editYsNewsDiv").dialog("close");
				}
		}]
	});
}

/**
 * 保存更新
 */
function save_YsNews(){
	/*var formdata = $("#editYsNewsForm").serialize();
	console.info("formdata");
	console.info(formdata);*/
	
	var object =new FormData();
	
	//获取表单数据
	var id=document.getElementById("id").value;
	var title=$("#editYsNewsForm #title").val();
	var file=document.getElementById("image").files[0];
	var content=$("#editYsNewsForm #content").val();
	var author=document.getElementById("author").value;
	var topNews=$("#editYsNewsForm #topNews").combobox('getValue');
	var remark=document.getElementById("remark").value;
	var topNews=$("#editYsNewsForm #status").combobox('getValue');
	var createDate=$("#editYsNewsForm #createDate").datebox('getValue');
	var createName=$("#editYsNewsForm #createName").val();
	var updateDate=$("#editYsNewsForm #updateDate").datebox('getValue');
	var updateName=$("#editYsNewsForm #updateName").val();
	
	if($.isEmptyObject(title)){
		$.messager.alert("新闻标题","不能为空");
	}else if($.isEmptyObject(content)){
		$.messager.alert("内容","不能为空");
	}else if($.isEmptyObject(createName)){
		$.messager.alert("创建人","不能为空");
	}
	
	/* alert("id---->>"+id+"---title---"+title+"----file--"+
			 file+"---content---"+content+"---author----"+author+
			 "----topNews----"+topNews+"----remark---"+remark+"----status---"
			 +status+"----createDate---"+createDate+"---createName----"+createName+"----updateDate----"+updateDate+"-----updateName---"+updateName);
	*/
	 object.append("id",id);
	object.append("title",title);
	object.append("file",file);
	object.append("content",content);
	object.append("author",author);
	object.append("topNews",topNews);
	object.append("remark",remark);
	object.append("status",status);
	object.append("createDate",createDate);
	object.append("createName",createName);
	object.append("updateDate",updateDate);
	object.append("updateName",updateName);

	var  url =httpUrl+"/ysnews/saveYsNews.html?&rand=" + Math.random();
	 $.ajax({   
		 type: 'POST',
		 dataType: 'json',
		 url: url,  
		 data:object,
		 processData: false,
		 contentType : false,
		 success: function(data){ 
			 if(data.code ==="0"){
				 $("#editYsNewsDiv").dialog("close");
				 $('tt_YsNews').datagrid('reload');
				 $.messager.alert("提示","操作成功","info");
			 }else{
				 $.messager.alert("提示","操作失败","error");
			 }   
		 } 
	});
}



/**
 * 删除
 */

function deleteNotice(id){
	if(!id){
		$.messager.alert("新闻","id不能为空");
		return;
	}
	$.messager.confirm("新闻","确认删除该新闻吗，删除后不可恢复",function(r){
		if(r){
			$.ajax({
				url:httpUrl+"/ysnews/deleteYsNews.html?id="+id,
				type:"post",
				data:{},
				success:function(data){
					if(data.ret==1){
						$.messager.alert("新闻","删除成功");
						$('#tableGrid').datagrid('reload');
					}else{
						$.messager.alert("新闻","删除失败，请稍后再试");
					}
				}
			});
		}
	});
}



function reloadDataGrid()
{
	$("tt_YsNews").datagrid("reload");
}




/*##########################公用方法##begin############################*/

//监听窗口大小变化
window.onresize = function(){
	setTimeout(domresize,300);
};
//改变表格和查询表单宽高
function domresize(){
	$('tt_YsNews').datagrid('resize',{  
		height:$("#body").height()-$('#search_areaYsNews').height()-5,
		width:$("#body").width()
	});
	$('#search_areaYsNews').panel('resize',{
		width:$("#body").width()
	});
	$('#detailLoanDiv').dialog('resize',{  
		height:$("#body").height()-25,
		width:$("#body").width()-30
	});
}
 
/*##########################公用方法##end############################*/