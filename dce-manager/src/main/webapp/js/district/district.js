
$(function(){
/*#############################################search form begin#################################*/	
		
	$("#searchdistrictForm #searchButton").on("click",function(){
		$("#tt_District").datagrid('load',{
			'searchStr': $("#searchdistrictForm #searchStr").val(),
			'searchCodeStr':$("#searchdistrictForm #searchCodeStr").val()		
		});
	});
	$("#searchdistrictForm #resetButton").on("click",function(){
		$("#searchdistrictForm").form('reset');
	});
	
/*#############################################search form end#################################*/		
	
/*##########################grid init begin####################################################*/
/*##########################grid toolbar begin#################################################*/
	var toolbar_tt = [
					{
						iconCls:"icon-edit",
						text:"新增",
						handler:to_adddistrict
					}
	          	];
	
/*######################grid toolbar end##############################*/
/*######################grid columns begin##############################*/
	var columns_tt = [
      			[	 				
							{field:'id',title:'id',width:100,hidden:true},						
								{field:"distrctName",title:"区域",width:180,align:"center"},
								{field:"user_id",title:"id",width:180,align:"center"},
								{field:"true_name",title:"真实姓名",width:180,align:"center"},
								{field:"user_name",title:"用户呢称",width:180,align:"center"},
								{field:"districtStatus",title:"封地状态",width:180,align:"center"},
					{field:"操作",title:"操作",width:80,align:"left",
	 					formatter:function(value,row,index){
	 					  var str= '<a href="javascript:void(0);" onclick="to_editdistrict(\''+row.districtId+'\');">编辑</a>';
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