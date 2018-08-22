$(function() {
	/*
	 * #############################################search form
	 * begin#################################
	 */

	$("#searchregionalawardsForm #searchButton").on(
			"click",
			function() {
				$("#tt_Regionalawards").datagrid(
						'load',
						{
							'searchStr' : $(
									"#searchregionalawardsForm #searchStr")
									.val(),
							'searchCodeStr' : $(
									"#searchregionalawardsForm #searchCodeStr")
									.val()
						});
			});

	$("#searchregionalawardsForm #resetButton").on("click", function() {
		$("#searchregionalawardsForm").form('reset');
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
	var toolbar_tt = [ {
		iconCls : "icon-edit",
		text : "新增",
		handler : to_addregionalawards
	} ];

	/* ######################grid toolbar end############################## */
	/* ######################grid columns begin############################## */
	var columns_tt = [ [
			{
				field : 'rewardsareaid',
				title : 'id',
				width : 100,
				hidden : true
			},
			{
				field : "rewardbalance",
				title : "编码",
				width : 180,
				align : "center"
			},
			{
				field : "algebra",
				title : "编码",
				width : 180,
				align : "center"
			},
			{
				field : "操作",
				title : "操作",
				width : 80,
				align : "left",
				formatter : function(value, row, index) {
					var str = '<a href="javascript:void(0);" onclick="to_editregionalawards(\''
							+ row.rewardsareaid + '\');">编辑</a>';
					return str;
				}
			} ] ];
	/* ######################grid columns end############################## */

	$("#tt_Regionalawards").datagrid(
			{
				url : httpUrl
						+ "/regionalawards/listRegionalawards.html?&rand="
						+ Math.random(),
				height : $("#body").height()
						- $('#search_areaRegionalawards').height() - 10,
				width : $("#body").width(),
				rownumbers : true,
				fitColumns : true,
				singleSelect : false,// 配合根据状态限制checkbox
				autoRowHeight : true,
				striped : true,
				checkOnSelect : false,// 配合根据状态限制checkbox
				selectOnCheck : false,// 配合根据状态限制checkbox
				loadFilter : function(data) {
					return {
						'rows' : data.datas,
						'total' : data.total,
						'pageSize' : data.pageSize,
						'pageNumber' : data.page
					};
				},
				pagination : true,
				showPageList : true,
				pageSize : 20,
				pageList : [ 10, 20, 30 ],
				idField : "id",
				columns : columns_tt,
				toolbar : toolbar_tt,
				queryParams : {
					'searchStr' : $("#searchregionalawardsForm #searchStr")
							.val(),
					'searchCodeStr' : $(
							"#searchregionalawardsForm #searchCodeStr").val()
				},
				onLoadSuccess : function(data) {// 根据状态限制checkbox

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
function to_addregionalawards() {
	to_editregionalawards('');
}
/**
 * 编辑
 * 
 * @param id
 */
function to_editregionalawards(id) {

	var url = httpUrl + "/regionalawards/addRegionalawards.html?&rand="
			+ Math.random() + "&id=" + id;
	$('#editRegionalawardsDiv').dialog({
		title : "新增",
		width : 760,
		height : 500,
		closed : false,
		closable : false,
		cache : false,
		href : url,
		modal : true,
		toolbar : [ {
			iconCls : "icon-save",
			text : "保存",
			handler : save_Regionalawards
		}, {
			iconCls : "icon-no",
			text : "关闭",
			handler : function() {
				$("#editRegionalawardsDiv").dialog("close");
			}
		} ]
	});
}

function save_Regionalawards() {
	var formdata = $("#editRegionalawardsForm").serialize();
	console.info("formdata");
	console.info(formdata);
	var url = httpUrl + "/regionalawards/saveRegionalawards.html?&rand="
			+ Math.random();
	$.ajax({
		type : 'POST',
		dataType : 'json',
		url : url,
		data : $("#editRegionalawardsForm").serialize(),
		success : function(data) {
			if (data.code === "0") {
				$("#editRegionalawardsDiv").dialog("close");
				$('tt_Regionalawards').datagrid('reload');
				$.messager.alert("提示", "操作成功", "info");
			} else {
				$.messager.alert("提示", "操作失败", "error");
			}
		}
	});
}

function reloadDataGrid() {
	$("tt_Regionalawards").datagrid("reload");
}

/* ##########################公用方法##begin############################ */

// 监听窗口大小变化
window.onresize = function() {
	setTimeout(domresize, 300);
};
// 改变表格和查询表单宽高
function domresize() {
	$('tt_Regionalawards').datagrid(
			'resize',
			{
				height : $("#body").height()
						- $('#search_areaRegionalawards').height() - 5,
				width : $("#body").width()
			});
	$('#search_areaRegionalawards').panel('resize', {
		width : $("#body").width()
	});
	$('#detailLoanDiv').dialog('resize', {
		height : $("#body").height() - 25,
		width : $("#body").width() - 30
	});
}

/* ##########################公用方法##end############################ */