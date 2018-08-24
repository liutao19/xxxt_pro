var basePath = "/dce-manager";
$(function() {
	/*
	 * #############################################search form
	 * begin#################################
	 */
	// 产品类型
	$("#searchForm #searchButton").on(
			"click",
			function() {
				var dataUrl = basePath + "/user/list.html";
				$("#usertable").datagrid('options').url = dataUrl;
				$("#usertable").datagrid(
						'load',
						{
							'userName' : $("#searchForm #userName").val(),
							'userMobile' : $("#searchForm #userMobile").val(),
							'startDate' : $("#searchForm #user_reg_startDate")
									.datebox('getValue'),
							'endDate' : $("#searchForm #user_reg_endDate")
									.datebox('getValue')
						});
			});

	$("#searchForm #resetButton").on("click", function() {
		$("#searchForm").form('reset');
	});

	/*
	 * #############################################search form
	 * end#################################
	 */

	/*
	 * ##########################grid init
	 * begin###################################################
	 */
	var toolbar_tt = [ {
		iconCls : 'icon-edit',
		text : '新增会员',
		handler : addVipr
	}, {
		iconCls : 'icon-edit',
		text : '查看推荐人',
		handler : queryOrgTree
	}, {
		iconCls : 'icon-edit',
		text : '修改客户信息',
		handler : editUser
	} ];

	/* ######################grid toolbar end############################## */
	var columns_tt = [ [
			{
				field : 'id',
				title : 'id',
				width : 30,
				halign : "center",
				align : "left",
				checkbox : true
			},
			{
				field : "userName",
				title : "用户名",
				width : 100,
				align : "center",
				formatter : function(value, row, index) {
					var str = "";
					if (value != null && value != undefined) {
						str += value;
					}
					if (row.trueName != null && row.trueName != undefined) {
						str += "[" + row.trueName + "]";
					}
					return str;
				}
			},
			{
				field : "userLevel",
				title : "级别",
				width : 80,
				align : "center",
				formatter : function(value, row, index) {
					if (value == "0") {
						return "未购卖";
					} else if (value == "1") {
						return "普通";
					} else if (value == "2") {
						return "会员"; // 铂金会员
					} else if (value == "3") {
						return "VIP"; // 黄金会员
					} else if (value == "4") {
						return "合伙人";
					}
				}
			},
			{
				field : "trueName",
				title : "用户姓名",
				width : 80,
				align : "center"
			},
			{
				field : "mobile",
				title : "手机号码",
				width : 80,
				align : "center"
			},
			{
				field : "refereeUserMobile",
				title : "推荐人",
				width : 80,
				align : "center"
			},
			{
				field : "userPassword",
				title : "登录密码",
				width : 80,
				align : "center"
			},
			{
				field : "twoPassword",
				title : "支付密码",
				width : 80,
				align : "center"
			},
			{
				field : "regTime",
				title : "注册时间",
				width : 100,
				align : "center",
				formatter : function(value, row, index) {
					if (value == null || value == 0 || value == undefined) {
						return "";
					} else {

						return formatDate(value);
					}
				}
			},
			{
				field : "certification",
				title : "激活状态",
				width : 80,
				align : "center",
				formatter : function(value, row, index) {
					if (value == null || value == undefined) {
						return "";
					}
					if (value == "1") {
						return "已激活";
					} else {
						return "未激活";
					}
				}
			},
			{
				field : "status",
				title : "冻结状态",
				width : 80,
				align : "center",
				formatter : function(value, row, index) {
					if (value == null || value == undefined) {
						return "";
					}
					if (value == "1") {
						return "已冻结";
					} else {
						return "已解冻";
					}
				}
			},
			{
				field : "edit",
				title : "操作",
				width : 80,
				align : "center",
				formatter : function(value, row, index) {
					var href = "";
					if (row.status == '0') {
						href = href
								+ '<a href="javascript:void(0);"  onclick="to_lock('
								+ row.id + ',\'' + 1 + '\');">冻结账户</a>';
					} else if (row.status == '1') {

						href = href
								+ '<a href="javascript:void(0);"  onclick="to_lock('
								+ row.id + ',\'' + 0 + '\');">解冻账户</a>';
					}

					if (row.certification != undefined
							&& row.certification != 1) {
						href = href
								+ ' <a href="javascript:void(0);"  onclick="baoKongDan('
								+ row.id + ');">激活</a>';
					}

					return href;
				}
			}, ] ];

	/* ######################grid columns end############################## */

	$("#usertable").datagrid({
		url : basePath + "/user/list.html",
		height : $("#body").height() - $('#search_area').height() - 10,
		width : $("#body").width(),
		rownumbers : true,
		fitColumns : true,
		singleSelect : true,// 配合根据状态限制checkbox
		autoRowHeight : true,
		striped : true,
		checkOnSelect : true,// 配合根据状态限制checkbox
		selectOnCheck : true,// 配合根据状态限制checkbox
		/*
		 * loadFilter : function(data){ return { 'rows' : data.datas, 'total' :
		 * data.total, 'pageSize' : data.pageSize, 'pageNumber' : data.page }; },
		 */

		pagination : true,
		showPageList : true,
		pageSize : 20,
		pageList : [ 10, 20, 30 ],
		idField : "id",
		// frozenColumns : [[{field:'ck',checkbox:true}]],
		columns : columns_tt,
		toolbar : toolbar_tt
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
 * 使用方法: 开启:MaskUtil.mask(); 关闭:MaskUtil.unmask();
 * 
 * MaskUtil.mask('其它提示文字...');
 */
var MaskUtil = (function() {

	var $mask, $maskMsg;

	var defMsg = '正在处理，请稍待。。。';

	function init() {
		if (!$mask) {
			$mask = $("<div class=\"datagrid-mask mymask\"></div>").appendTo(
					"body");
		}
		if (!$maskMsg) {
			$maskMsg = $(
					"<div class=\"datagrid-mask-msg mymask\">" + defMsg
							+ "</div>").appendTo("body").css({
				'font-size' : '12px'
			});
		}

		$mask.css({
			width : "100%",
			height : $(document).height()
		});

		var scrollTop = $(document.body).scrollTop();

		$maskMsg.css({
			left : ($(document.body).outerWidth(true) - 190) / 2,
			top : (($(window).height() - 45) / 2) + scrollTop
		});

	}

	return {
		mask : function(msg) {
			init();
			$mask.show();
			$maskMsg.html(msg || defMsg).show();
		},
		unmask : function() {
			$mask.hide();
			$maskMsg.hide();
		}
	}

}());

function to_lock(userId, optType) {

	var msg = optType == '0' ? '解冻' : '冻结';
	$.messager.confirm("确认", "确认" + msg + "账户", function(r) {
		if (r) {
			$.ajax({
				url : basePath + "/user/lockUser.html",
				type : "post",
				dataType : 'json',
				data : {
					"userId" : userId,
					"optType" : optType
				},
				success : function(ret) {
					if (ret.code == 0) {
						$.messager.alert("成功", msg + "成功");
						reloadDataGrid();
					} else {
						$.messager.alert("失败", ret.msg);
					}
				}
			});
		}
	});

}

function baoKongDan(id) {

	var url = basePath + "/user/toActivity.html?userId=" + id;
	$('#activityUserDiv').dialog({
		title : "用户激活",
		width : 400,
		height : 500,
		closed : false,
		closable : false,
		cache : false,
		href : url,
		modal : true,
		toolbar : [ {
			iconCls : "icon-save",
			text : "保存",
			handler : save_activity
		}, {
			iconCls : "icon-no",
			text : "关闭",
			handler : function() {
				$("#activityUserDiv").dialog("close");
			}
		} ]
	});

}

function addVipr() {
	return;
	var url = basePath + "/user/edit.html?userId=" + id;
	$('#editLevelDiv').dialog({
		title : "新增一个会员",
		width : 400,
		height : 500,
		closed : false,
		closable : false,
		cache : false,
		href : url,
		modal : true,
		toolbar : [ {
			iconCls : "icon-save",
			text : "保存",
			handler : save_edit
		}, {
			iconCls : "icon-no",
			text : "关闭",
			handler : function() {
				$("#editLevelDiv").dialog("close");
			}
		} ]
	});
}

function editUser() {
	var id = get_id();
	if (id == null || id == "") {
		return;
	}
	var url = basePath + "/user/edit.html?userId=" + id;
	$('#editLevelDiv').dialog({
		title : "修改用户信息",
		width : 400,
		height : 500,
		closed : false,
		closable : false,
		cache : false,
		href : url,
		modal : true,
		toolbar : [ {
			iconCls : "icon-save",
			text : "保存",
			handler : save_edit
		}, {
			iconCls : "icon-no",
			text : "关闭",
			handler : function() {
				$("#editLevelDiv").dialog("close");
			}
		} ]
	});
}
/**
 * 把页面数据传到后台
 */
function save_edit() {
	var userId = $("#change_level_userId").val();
	var trueName = $("#edit_true_name").val();
	var userName = $("#edit_true_userName").val();
	var mobile = $("#edit_user_mobile").val();
	var login_password = $("#edit_user_login_password").val();
	var seconde_password = $("#edit_user_seconde_password").val();
	var userLevel = $("#change_level").combobox('getValue');
	var isBlankOrder = $("#isBlankOrder").combobox('getValue');

	$.ajax({
		url : basePath + "/user/saveEdit.html",
		type : "post",
		dataType : 'json',
		data : {
			"userId" : userId,
			"userName" : userName,
			"trueName" : trueName,
			"mobile" : mobile,
			"login_password" : login_password,
			"seconde_password" : seconde_password,
			"isBlankOrder" : isBlankOrder,
			"userLevel" : userLevel
		},
		success : function(ret) {
			if (ret.code == 0) {
				$.messager.alert("成功", ret.msg);
				$("#editLevelDiv").dialog("close");
				reloadDataGrid();
			} else {
				$.messager.alert("失败", ret.msg);
			}
		}
	});
}
function save_activity() {
	var userId = $("#change_level_userId").val();
	var userLevel = $("#change_level").combobox('getValue');

	$.ajax({
		url : basePath + "/user/saveActivity.html",
		type : "post",
		dataType : 'json',
		data : {
			"userId" : userId,
			"userLevel" : userLevel
		},
		success : function(ret) {
			if (ret.code == 0) {
				$.messager.alert("成功", ret.msg);
				$("#activityUserDiv").dialog("close");
				reloadDataGrid();
			} else {
				$.messager.alert("失败", ret.msg);
			}
		}
	});
}

function reloadDataGrid() {
	$("#usertable").datagrid("reload");
}

/**
 * 订单明细
 * 
 * @param id
 */
function queryOrgTree() {
	var id = get_id();
	if (id == null || id == "") {
		return;
	}
	var url = basePath + "/user/orgtree.html?&rand=" + Math.random()
			+ "&userId=" + id;
	var content = '<iframe src="'
			+ url
			+ '" width="100%" height="99%" frameborder="0" scrolling="no"></iframe>';
	$('#userOrgTreeDiv').dialog({
		title : "查看直推树",
		content : content,
		width : 600,
		height : 400,
		closed : false,
		closable : false,
		cache : false,
		// href: url,
		modal : true,
		toolbar : [ {
			iconCls : "icon-no",
			text : "关闭",
			handler : function() {
				$("#userOrgTreeDiv").dialog("close");
			}
		} ]
	});
}

/**
 * 格式化日期时间
 */
function formatDate(value) {
	var date = new Date(value);// long转换成date
	var year = date.getFullYear().toString();
	var month = (date.getMonth() + 1);
	var day = date.getDate().toString();
	var h = date.getHours();
	var m = date.getMinutes()
	var s = date.getSeconds();
	if (month < 10) {
		month = "0" + month;
	}
	if (day < 10) {
		day = "0" + day;
	}
	return year + "-" + month + "-" + day + " " + h + ":" + m + ":" + s;
}
/* ################***导出**begin*################### */
function export_excel() {
	var searchStr = $("#searchForm #searchStr").val();
	var productCode = $("#searchForm #search_productCode").combobox('getValue');
	var loanStatus = $("#searchForm #search_loanStatus").combobox('getValue');
	var loanType = $("#searchForm #search_loanType").combobox('getValue');
	var cityCode = $("#searchForm #search_city").combobox('getValue');
	var channelType = $("#searchForm #search_channelType").combobox('getValue');
	var startDate = $("#searchForm #search_startDate").datebox('getValue');
	var endDate = $("#searchForm #search_endDate").datebox('getValue');
	var processNextStep = $("#searchForm #search_processNextStep").combobox(
			'getValue');
	// document.getElementById("exportExcel").disabled = true;
	// document.getElementById("exportExcel").value = "正在导出";
	var exportIframe = document.createElement('iframe');
	exportIframe.src = basePath + "/loan/export/excel.html" + "?searchStr="
			+ searchStr + "&productCode=" + productCode + "&loanType="
			+ loanType + "&loanStatus=" + loanStatus + "&cityCode=" + cityCode
			+ "&channelType=" + channelType + "&startDate=" + startDate
			+ "&endDate=" + endDate + "&processNextStep=" + processNextStep;

	exportIframe.style.display = 'none';
	document.body.appendChild(exportIframe);
}
/* ################***导出**end*################### */

/* ##########################公用方法##begin############################ */
/**
 * 得到选中的行
 * 
 * @returns {String}
 */
function get_ids() {
	var ids = '';
	var rows = $('#tt').datagrid('getChecked');
	if (rows.length == 0) {
		$.messager.alert("提示", "请选择需要操作的数据", "info");
		return false;
	} else {
		for (var i = 0; i < rows.length; i++) {
			ids += rows[i].loanId + ',';
		}
		ids = ids.substring(0, ids.length - 1);
		return ids;
	}
}
/**
 * 得到一条数据
 * 
 * @returns {String}
 */
function get_id() {
	var rows = $('#usertable').datagrid('getChecked');
	if (rows.length == 0) {
		$.messager.alert("提示", "请选择需要操作的数据", "info");
		return "";
	} else if (rows.length > 1) {
		$.messager.alert("提示", "每次只能操作一条数据", "info");
		return "";
	} else {
		return rows[0].id;
	}
}

// 监听窗口大小变化
window.onresize = function() {
	setTimeout(domresize, 300);
};
// 改变表格和查询表单宽高
function domresize() {
	$('#usertable').datagrid('resize', {
		height : $("#body").height() - $('#search_area').height() - 5,
		width : $("#body").width()
	});
	$('#search_area').panel('resize', {
		width : $("#body").width()
	});
	$('#detailLoanDiv').dialog('resize', {
		height : $("#body").height() - 25,
		width : $("#body").width() - 30
	});
}
/* ##########################公用方法##end############################ */