var basePath="/dce-manager";
$(function(){
/*#############################################search form begin#################################*/	
		
	$("#searchdistrictForm #searchButton").on("click",function(){
		var dataUrl = basePath+"/district/listDistrict.html";
		$("#tt_District").datagrid('options').url = dataUrl;
		$("#tt_District").datagrid('load',{
			'distrct_name':$("#searchdistrictForm #distrct_name").val(),
			'true_name':$("#searchdistrictForm #true_name").val()	
		});
	});
	$("#searchdistrictForm #resetButton").on("click",function(){
		$("#searchdistrictForm").form('reset');
	});
	
/*#############################################search form end#################################*/		
	
/*##########################grid init begin####################################################*/
/*##########################grid toolbar begin#################################################*/
	var toolbar_tt = [
					
	          	];
	
/*######################grid toolbar end##############################*/
/*######################grid columns begin##############################*/
	var columns_tt = [
      			[	 				
							    {field:'district_id',title:'id',width:100,hidden:true},						
								{field:"distrct_name",title:"区域",width:180,align:"center"},
								{field:"user_id",title:"用户id",width:180,align:"center"},
								{field:"true_name",title:"真实姓名",width:180,align:"center"},
								{field:"user_name",title:"用户呢称",width:180,align:"center"},
								{field:"districtStatus",title:"封地状态",width:180,align:"center"},
					{field:"操作",title:"操作",width:80,align:"left",
	 					formatter:function(value,row,index){
	 					  var str= '<a href="javascript:void(0);" onclick="to_editdistrict(\''+row.district_id+'\');">分配区域</a>   <a href="javascript:void(0);" onclick="deleteDistrict(\''+row.district_id+'\',\''+row.user_id+'\');">删除</a>';
	 					  return str;
	 					}
	 				}	 				
	 			]
	 	];
/*######################grid columns end##############################*/
	
	$("#tt_District").datagrid({
		url:httpUrl+"/district/listDistrict.html?&rand=" + Math.random(),
		height:$("#body").height()-$('#search_areaDistrict').height()-10,
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
			'searchStr': $("#searchdistrictForm #searchStr").val(),
			'searchCodeStr':$("#searchdistrictForm #searchCodeStr").val()
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
function to_adddistrict(){
	to_editdistrict('');
}
/**
 * 编辑
 * @param id
 */
function to_editdistrict(id){
	
	var url = httpUrl+"/district/addDistrict.html?&rand=" + Math.random()+"&id="+id;
	$('#editDistrictDiv').dialog({
		title: "分配",
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
					handler:save_District
				},
				{
					iconCls:"icon-no",text:"关闭",
					handler:function(){
						$("#editDistrictDiv").dialog("close");
				}
		}]
	});
}


/**
 * 删除
 */

function deleteDistrict(id,userId){	
	if(!id){
		$.messager.alert("区域","id不能为空");
		return;
	}
	$.messager.confirm("区域","确认删除该区域吗，删除后不可恢复",function(r){
		if(r){
			$.ajax({
				url:httpUrl+"/district/deleteDistrict.html?districtId="+id+"&userId="+userId,
				type:"post",
				dataType:"json",
				success:function(data){
					if(data.ret==1){
						$.messager.alert("区域","删除成功");
						$('#tableGrid').datagrid('reload');
					}else{
						$.messager.alert("区域","删除失败，请稍后再试");
					}
				}
			});
		}
	});
}






function save_District(){
	var formdata = $("#editDistrictForm").serialize();
	console.info("formdata");
	console.info(formdata);
	var  url =httpUrl+"/district/saveDistrict.html?&rand=" + Math.random();
	 $.ajax({   
		 type: 'POST',
		 dataType: 'json',
		 url: url,  
		 data:$("#editDistrictForm").serialize(),
		 success: function(data){ 
			 if(data.code ==="0"){
				 $("#editDistrictDiv").dialog("close");
				 $('tt_District').datagrid('reload');
				 $.messager.alert("提示","操作成功","info");
			 }else{
				 $.messager.alert("提示","操作失败","error");
			 }   
		 } 
	});
}


function reloadDataGrid()
{
	$("tt_District").datagrid("reload");
}




/*##########################公用方法##begin############################*/

//监听窗口大小变化
window.onresize = function(){
	setTimeout(domresize,300);
};
//改变表格和查询表单宽高
function domresize(){
	$('tt_District').datagrid('resize',{  
		height:$("#body").height()-$('#search_areaDistrict').height()-5,
		width:$("#body").width()
	});
	$('#search_areaDistrict').panel('resize',{
		width:$("#body").width()
	});
	$('#detailLoanDiv').dialog('resize',{  
		height:$("#body").height()-25,
		width:$("#body").width()-30
	});
}
 
/*##########################公用方法##end############################*/