
$(function(){
/*
 * #############################################search form
 * begin#################################
 */	
		
	$("#searchapplyTravelForm #searchButton").on("click",function(){
		var dataUrl = httpUrl+"/applytravel/listApplyTravel.html";
		$("#tt_ApplyTravel").datagrid('options').url = dataUrl;
		$("#tt_ApplyTravel").datagrid('load',{
			'userName': $("#searchapplyTravelForm #userName").val(),
			'startDate':$("#searchapplyTravelForm #startDate").datebox('getValue'),
			'endDate':$("#searchapplyTravelForm #endDate").datebox('getValue')		
		});
	});
	
	$("#searchapplyTravelForm #resetButton").on("click",function(){
		$("#searchapplyTravelForm").form('reset');
	});
	
/*
 * #############################################search form
 * end#################################
 */		
	
/*
 * ##########################grid init
 * begin####################################################
 */
/*
 * ##########################grid toolbar
 * begin#################################################
 */
	var toolbar_tt = [
					{
						iconCls:"icon-edit",
						text:"新增",
						handler:to_addapplyTravel
					}
	          	];
	
/* ######################grid toolbar end############################## */
/* ######################grid columns begin############################## */
	var columns_tt = [
      			[	 				
							{field:'id',title:'id',width:100,hidden:true},						
								{field:"truename",title:"真实姓名",width:100,align:"center"},
								{field:"sex",title:"性别",width:80,align:"center",
									formatter:function(value,row,index){
				 						if(row.state == "0"){
				 							return "男";
				 						}else if(row.state == "1"){
				 							return "女";
				 						}
									}
								},
								{field:"nation",title:"民族",width:80,align:"center"},
								{field:"identity",title:"身份证",width:150,align:"center"},
								{field:"phone",title:"手机号码",width:150,align:"center"},
								{field:"address",title:"地址",width:150,align:"center"},
								{field:"isbeen",title:"是否去过",width:80,align:"center",
									formatter:function(value,row,index){
				 						if(row.state == "0"){
				 							return "是";
				 						}else if(row.state == "1"){
				 							return "否";
				 						}
									}
								},
								{field:"people",title:"同行人数",width:80,align:"center"},
								{field:"linename",title:"路线名称",width:150,align:"center"},
								{field:"createtime",title:"申请时间",width:150,align:"center",formatter:dateTimeFormatter},
								{field:"state",title:"是否通过",width:80,align:"center",
									formatter:function(value,row,index){
				 						if(row.state == "0"){
				 							return "未通过";
				 						}else if(row.state == "1"){
				 							return "通过";
				 						}
				 					}
								},
								{field:"操作",title:"操作",width:100,align:"left",
				 					formatter:function(value,row,index){
				 					  var str= '<a href="javascript:void(0);" onclick="to_editapplyTravel(\''+row.id+'\');">编辑</a> <a href="javascript:void(0);" onclick="agree_applyTravel(\''+row.id+'\');">同意申請</a>';
				 					  return str;
				 					}
				 				}	 				
	 			]
	 	];
/* ######################grid columns end############################## */
	
	$("#tt_ApplyTravel").datagrid({
		url:httpUrl+"/applytravel/listApplyTravel.html?&rand=" + Math.random(),
		height:$("#body").height()-$('#search_areaApplyTravel').height()-10,
		width:$("#body").width(),
		rownumbers:true,
		fitColumns:true,
		singleSelect:false,// 配合根据状态限制checkbox
		autoRowHeight:true,
		striped:true,
		checkOnSelect:false,// 配合根据状态限制checkbox
		selectOnCheck:false,// 配合根据状态限制checkbox
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
			'userName': $("#searchapplyTravelForm #userName").val(),
			'startDate':$("#searchapplyTravelForm #startDate").datebox('getValue'),
			'endDate':$("#searchapplyTravelForm #endDate").datebox('getValue')	
		},
		onLoadSuccess:function(data){// 根据状态限制checkbox
			
		}
	});
	
	/*
	 * $(window).resize(function (){ domresize(); });
	 */
/*
 * ##########################grid init
 * end###################################################
 */
});


/**
 * 新增
 * 
 * @param id
 */
function to_addapplyTravel(){
	to_editapplyTravel('');
	$('#editUserFeedbackDiv').dialog({
		title: "新增",
	});
}
/**
 * 编辑
 * 
 * @param id
 */
function to_editapplyTravel(id){
	
	var url = httpUrl+"/applytravel/addApplyTravel.html?&rand=" + Math.random()+"&id="+id;
	$('#editApplyTravelDiv').dialog({
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
/**
 * 同意申请
 */
function agree_applyTravel(id){
	if(!id){
		$.messager.alert("消息","id不能为空");
		return;
	}
	$.messager.confirm("消息","确认同意申请吗，同意后不可恢复",function(r){
		if(r){
			$.ajax({
				url:httpUrl+"/applytravel/agreeApply.html?id="+id,
				type:"post",
				data:{},
				success:function(data){
					if(data.ret==1){
						$.messager.alert("消息","操作成功");
						$('#tableGrid').datagrid('reload');
					}else{
						$.messager.alert("消息","操作失败，请稍后再试");
					}
				}
			});
		}
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




/* ##########################公用方法##begin############################ */

// 监听窗口大小变化
window.onresize = function(){
	setTimeout(domresize,300);
};
// 改变表格和查询表单宽高
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
 
/* ##########################公用方法##end############################ */