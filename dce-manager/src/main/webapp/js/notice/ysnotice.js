
$(function(){
/*#############################################search form begin#################################*/	
		
	$("#searchysNoticeForm #searchButton").on("click",function(){
		$("#tt_YsNotice").datagrid('load',{
			'searchStr': $("#searchysNoticeForm #searchStr").val(),
			'searchCodeStr':$("#searchysNoticeForm #searchCodeStr").val()		
		});
	});
	
	$("#searchysNoticeForm #resetButton").on("click",function(){
		$("#searchysNoticeForm").form('reset');
	});
	
/*#############################################search form end#################################*/		
	
/*##########################grid init begin####################################################*/
/*##########################grid toolbar begin#################################################*/
	var toolbar_tt = [
					{
						iconCls:"icon-edit",
						text:"新增",
						handler:to_addysNotice
					}
	          	];
	
/*######################grid toolbar end##############################*/
/*######################grid columns begin##############################*/
	var columns_tt = [
      			[	 				
							{field:'id',title:'id',width:100,hidden:true},						
								{field:"noticeType",title:"编码",width:180,align:"center"},
								{field:"title",title:"编码",width:180,align:"center"},
								{field:"image",title:"编码",width:180,align:"center"},
								{field:"content",title:"编码",width:180,align:"center"},
								{field:"author",title:"编码",width:180,align:"center"},
								{field:"topNotice",title:"编码",width:180,align:"center"},
								{field:"remark",title:"编码",width:180,align:"center"},
								{field:"status",title:"编码",width:180,align:"center"},
								{field:"createDate",title:"编码",width:180,align:"center",formatter:dateTimeFormatter},
								{field:"createName",title:"编码",width:180,align:"center"},
								{field:"updateDate",title:"编码",width:180,align:"center",formatter:dateTimeFormatter},
								{field:"updateName",title:"编码",width:180,align:"center"},
					{field:"操作",title:"操作",width:80,align:"left",
	 					formatter:function(value,row,index){
	 					  var str= '<a href="javascript:void(0);" onclick="to_editysNotice(\''+row.id+'\');">编辑</a>';
	 					  return str;
	 					}
	 				}	 				
	 			]
	 	];
/*######################grid columns end##############################*/
	
	$("#tt_YsNotice").datagrid({
		url:httpUrl+"/ysnotice/listYsNotice.html?&rand=" + Math.random(),
		height:$("#body").height()-$('#search_areaYsNotice').height()-10,
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
			'searchStr': $("#searchysNoticeForm #searchStr").val(),
			'searchCodeStr':$("#searchysNoticeForm #searchCodeStr").val()
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
function to_addysNotice(){
	to_editysNotice('');
}
/**
 * 编辑
 * @param id
 */
function to_editysNotice(id){
	
	var url = httpUrl+"/ysnotice/addYsNotice.html?&rand=" + Math.random()+"&id="+id;
	$('#editYsNoticeDiv').dialog({
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
					handler:save_YsNotice
				},
				{
					iconCls:"icon-no",text:"关闭",
					handler:function(){
						$("#editYsNoticeDiv").dialog("close");
				}
		}]
	});
}

function save_YsNotice(){
	var formdata = $("#editYsNoticeForm").serialize();
	console.info("formdata");
	console.info(formdata);
	var  url =httpUrl+"/ysnotice/saveYsNotice.html?&rand=" + Math.random();
	 $.ajax({   
		 type: 'POST',
		 dataType: 'json',
		 url: url,  
		 data:$("#editYsNoticeForm").serialize(),
		 success: function(data){ 
			 if(data.code ==="0"){
				 $("#editYsNoticeDiv").dialog("close");
				 $('tt_YsNotice').datagrid('reload');
				 $.messager.alert("提示","操作成功","info");
			 }else{
				 $.messager.alert("提示","操作失败","error");
			 }   
		 } 
	});
}


function reloadDataGrid()
{
	$("tt_YsNotice").datagrid("reload");
}




/*##########################公用方法##begin############################*/

//监听窗口大小变化
window.onresize = function(){
	setTimeout(domresize,300);
};
//改变表格和查询表单宽高
function domresize(){
	$('tt_YsNotice').datagrid('resize',{  
		height:$("#body").height()-$('#search_areaYsNotice').height()-5,
		width:$("#body").width()
	});
	$('#search_areaYsNotice').panel('resize',{
		width:$("#body").width()
	});
	$('#detailLoanDiv').dialog('resize',{  
		height:$("#body").height()-25,
		width:$("#body").width()-30
	});
}
 
/*##########################公用方法##end############################*/