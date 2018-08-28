var basePath = "/dce-manager";
$(function() {
	/*
	 * #############################################search form
	 * begin#################################
	 */

	$("#searchdistrictForm #searchButton").on("click", function() {
		var dataUrl = basePath + "/district/listDistrict.html";
		$("#tt_District").datagrid('options').url = dataUrl;
		$("#tt_District").datagrid('load', {
			'distrct_name' : $("#searchdistrictForm #distrct_name").val(),
			'true_name' : $("#searchdistrictForm #true_name").val()
		});
	});
	$("#searchdistrictForm #resetButton").on("click", function() {
		$("#searchdistrictForm").form('reset');
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

	];

	/* ######################grid toolbar end############################## */
	/* ######################grid columns begin############################## */
	var columns_tt = [ [
			{
				field : 'district_id',
				title : 'id',
				width : 100,
				hidden : true
			},
			{
				field : "distrct_name",
				title : "区域",
				width : 180,
				align : "center"
			},
			/* {field:"user_id",title:"用户id",width:180,align:"center"}, */
			{
				field : "true_name",
				title : "真实姓名",
				width : 180,
				align : "center"
			},
			{
				field : "user_name",
				title : "用户呢称",
				width : 180,
				align : "center"
			},
			/* {field:"districtStatus",title:"封地状态",width:180,align:"center"}, */
			{
				field : "操作",
				title : "操作",
				width : 80,
				align : "left",
				formatter : function(value, row, index) {
					var str = '<a href="javascript:void(0);" onclick="to_editdistrict(\''
							+ row.district_id
							+ '\');">分配区域</a>   <a href="javascript:void(0);" onclick="deleteDistrict(\''
							+ row.district_id
							+ '\',\''
							+ row.user_id
							+ '\');">删除</a>';
					return str;
				}
			} ] ];
	/* ######################grid columns end############################## */

	$("#tt_District").datagrid({
		url : httpUrl + "/district/listDistrict.html?&rand=" + Math.random(),
		height : $("#body").height() - $('#search_areaDistrict').height() - 10,
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
			'searchStr' : $("#searchdistrictForm #searchStr").val(),
			'searchCodeStr' : $("#searchdistrictForm #searchCodeStr").val()
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
function to_adddistrict() {
	to_editdistrict('');
}
/**
 * 编辑
 * 
 * @param id
 */
function to_editdistrict(id) {

	var url = httpUrl + "/district/addDistrict.html?&rand=" + Math.random()
			+ "&id=" + id;
	$('#editDistrictDiv').dialog({
		title : "分配区域",
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
			handler : save_District
		}, {
			iconCls : "icon-no",
			text : "关闭",
			handler : function() {
				$("#editDistrictDiv").dialog("close");
			}
		} ]
	});
}

/**
 * 删除
 */

function deleteDistrict(id, userId) {
	if (!id) {
		$.messager.alert("区域", "id不能为空");
		return;
	}
	$.messager.confirm("区域", "确认删除该区域吗，删除后不可恢复", function(r) {
		if (r) {
			$.ajax({
				url : httpUrl + "/district/deleteDistrict.html?districtId="
						+ id + "&userId=" + userId,
				type : "post",
				dataType : "json",
				success : function(data) {
					if (data.ret == 1) {
						$('#tableGrid').datagrid('reload');
						$.messager.alert("区域", "删除成功");
					} else {
						$.messager.alert("区域", "删除失败，请稍后再试");
					}
				}
			});
		}
	});
}

function save_District() {
	var formdata = $("#editDistrictForm").serialize();
	console.info("formdata");
	var province = $("#editDistrictForm #country").find("option:selected")
			.text();
	var city = $("#editDistrictForm #city").find("option:selected").text();
	var district = $("#editDistrictForm #children").find("option:selected")
			.text();
	var districtId = $("#districtId").val();
	var userId=$("#userId").val();
	if (province == "--请选择省份--" || city == "--请选择市---") {
		$.messager.alert("提示", "请选择地区", "error");
		return;
	}
	var object = new FormData();
	object.append("districtId", districtId);
	object.append("userId",userId);
	object.append("distrctName", province + city + district);
	console.info(province + city + district + "id==" + districtId+"userId="+userId);
	var url = httpUrl + "/district/saveDistrict.html?&rand=" + Math.random();
	$.ajax({
		type : 'POST',
		dataType : 'json',
		url : url,
		data : object,
		processData : false,
		contentType : false,
		success : function(data) {
			
			if (data.code === "0") {
				$("#editDistrictDiv").dialog("close");
				$('#tt_District').datagrid('reload');
				$.messager.alert("提示", "操作成功", "info");
			} else {
				$.messager.alert("提示", "操作失败", "error");
			}
			
		}
	});
}

function reloadDataGrid() {
	$("tt_District").datagrid("reload");
}

var list = [ {
	"id" : "c101020000",
	"name" : "北京",
	"type" : 1,
	"idx" : 0,
	"city" : [ {
		"id" : "c101021100",
		"name" : "昌平区",
		"type" : 2,
		"idx" : 0,
		"children" : []
	}, {
		"id" : "c101020300",
		"name" : "朝阳区",
		"type" : 2,
		"idx" : 1,
		"children" : []
	}, {
		"id" : "c101021200",
		"name" : "大兴区",
		"type" : 2,
		"idx" : 2,
		"children" : []
	}, {
		"id" : "c101020100",
		"name" : "东城区",
		"type" : 2,
		"idx" : 3,
		"children" : []
	}, {
		"id" : "c101020800",
		"name" : "房山区",
		"type" : 2,
		"idx" : 4,
		"children" : []
	}, {
		"id" : "c101020400",
		"name" : "丰台区",
		"type" : 2,
		"idx" : 5,
		"children" : []
	}, {
		"id" : "c101020600",
		"name" : "海淀区",
		"type" : 2,
		"idx" : 6,
		"children" : []
	}, {
		"id" : "c101021300",
		"name" : "怀柔区",
		"type" : 2,
		"idx" : 7,
		"children" : []
	}, {
		"id" : "c101020700",
		"name" : "门头沟区",
		"type" : 2,
		"idx" : 8,
		"children" : []
	}, {
		"id" : "c101021500",
		"name" : "密云",
		"type" : 2,
		"idx" : 9,
		"children" : []
	}, {
		"id" : "c101021400",
		"name" : "平谷区",
		"type" : 2,
		"idx" : 10,
		"children" : []
	}, {
		"id" : "c101020500",
		"name" : "石景山区",
		"type" : 2,
		"idx" : 11,
		"children" : []
	}, {
		"id" : "c101021000",
		"name" : "顺义区",
		"type" : 2,
		"idx" : 12,
		"children" : []
	}, {
		"id" : "c101020900",
		"name" : "通州区",
		"type" : 2,
		"idx" : 13,
		"children" : []
	}, {
		"id" : "c101020200",
		"name" : "西城区",
		"type" : 2,
		"idx" : 14,
		"children" : []
	}, {
		"id" : "c101021600",
		"name" : "延庆",
		"type" : 2,
		"idx" : 15,
		"children" : []
	} ]
}, {
	"id" : "c101010000",
	"name" : "广东",
	"type" : 1,
	"idx" : 1,
	"city" : [ {
		"id" : "c101010100",
		"name" : "广州",
		"type" : 2,
		"idx" : 0,
		"children" : [ {
			"id" : "c101010105",
			"name" : "白云区",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101010112",
			"name" : "从化",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101010107",
			"name" : "番禺区",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101010103",
			"name" : "海珠区",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101010108",
			"name" : "花都区",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101010106",
			"name" : "黄埔区",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101010101",
			"name" : "荔湾区",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101010110",
			"name" : "萝岗区",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101010109",
			"name" : "南沙区",
			"type" : 3,
			"idx" : 8,
			"children" : []
		}, {
			"id" : "c101010104",
			"name" : "天河区",
			"type" : 3,
			"idx" : 9,
			"children" : []
		}, {
			"id" : "c101010102",
			"name" : "越秀区",
			"type" : 3,
			"idx" : 10,
			"children" : []
		}, {
			"id" : "c101010111",
			"name" : "增城",
			"type" : 3,
			"idx" : 11,
			"children" : []
		} ]
	}, {
		"id" : "c101010300",
		"name" : "深圳",
		"type" : 2,
		"idx" : 1,
		"children" : [ {
			"id" : "c101010304",
			"name" : "宝安区",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101010310",
			"name" : "大鹏新区",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101010302",
			"name" : "福田区",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101010308",
			"name" : "光明新区",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101010305",
			"name" : "龙岗区",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101010307",
			"name" : "龙华新区",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101010301",
			"name" : "罗湖区",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101010303",
			"name" : "南山区",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101010309",
			"name" : "坪山新区",
			"type" : 3,
			"idx" : 8,
			"children" : []
		}, {
			"id" : "c101010306",
			"name" : "盐田区",
			"type" : 3,
			"idx" : 9,
			"children" : []
		} ]
	}, {
		"id" : "c101011700",
		"name" : "东莞",
		"type" : 2,
		"idx" : 2,
		"children" : [ {
			"id" : "c101011701",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 0,
			"children" : []
		} ]
	}, {
		"id" : "c101010600",
		"name" : "佛山",
		"type" : 2,
		"idx" : 3,
		"children" : [ {
			"id" : "c101010601",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 0,
			"children" : []
		} ]
	}, {
		"id" : "c101011100",
		"name" : "惠州",
		"type" : 2,
		"idx" : 4,
		"children" : [ {
			"id" : "c101011102",
			"name" : "博罗",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101011103",
			"name" : "惠东",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101011104",
			"name" : "龙门",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101011101",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 3,
			"children" : []
		} ]
	}, {
		"id" : "c101011800",
		"name" : "中山",
		"type" : 2,
		"idx" : 5,
		"children" : [ {
			"id" : "c101011801",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 0,
			"children" : []
		} ]
	}, {
		"id" : "c101010400",
		"name" : "珠海",
		"type" : 2,
		"idx" : 6,
		"children" : [ {
			"id" : "c101010401",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 0,
			"children" : []
		} ]
	}, {
		"id" : "c101011900",
		"name" : "潮州",
		"type" : 2,
		"idx" : 7,
		"children" : [ {
			"id" : "c101011902",
			"name" : "潮安",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101011903",
			"name" : "饶平",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101011901",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 2,
			"children" : []
		} ]
	}, {
		"id" : "c101011400",
		"name" : "河源",
		"type" : 2,
		"idx" : 8,
		"children" : [ {
			"id" : "c101011406",
			"name" : "东源",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101011405",
			"name" : "和平",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101011404",
			"name" : "连平",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101011403",
			"name" : "龙川",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101011401",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101011402",
			"name" : "紫金",
			"type" : 3,
			"idx" : 5,
			"children" : []
		} ]
	}, {
		"id" : "c101010700",
		"name" : "江门",
		"type" : 2,
		"idx" : 9,
		"children" : [ {
			"id" : "c101010705",
			"name" : "恩平",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101010704",
			"name" : "鹤山",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101010703",
			"name" : "开平",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101010701",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101010702",
			"name" : "台山",
			"type" : 3,
			"idx" : 4,
			"children" : []
		} ]
	}, {
		"id" : "c101012000",
		"name" : "揭阳",
		"type" : 2,
		"idx" : 10,
		"children" : [ {
			"id" : "c101012004",
			"name" : "惠来",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101012002",
			"name" : "揭东",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101012003",
			"name" : "揭西",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101012005",
			"name" : "普宁",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101012001",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 4,
			"children" : []
		} ]
	}, {
		"id" : "c101010900",
		"name" : "茂名",
		"type" : 2,
		"idx" : 11,
		"children" : [ {
			"id" : "c101010902",
			"name" : "电白",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101010903",
			"name" : "高州",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101010904",
			"name" : "化州",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101010901",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101010905",
			"name" : "信宜",
			"type" : 3,
			"idx" : 4,
			"children" : []
		} ]
	}, {
		"id" : "c101011200",
		"name" : "梅州",
		"type" : 2,
		"idx" : 12,
		"children" : [ {
			"id" : "c101011203",
			"name" : "大埔",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101011204",
			"name" : "丰顺",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101011207",
			"name" : "蕉岭",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101011202",
			"name" : "梅县",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101011206",
			"name" : "平远",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101011201",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101011205",
			"name" : "五华",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101011208",
			"name" : "兴宁",
			"type" : 3,
			"idx" : 7,
			"children" : []
		} ]
	}, {
		"id" : "c101011600",
		"name" : "清远",
		"type" : 2,
		"idx" : 13,
		"children" : [ {
			"id" : "c101011602",
			"name" : "佛冈",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101011605",
			"name" : "连南",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101011604",
			"name" : "连山",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101011608",
			"name" : "连州",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101011606",
			"name" : "清新",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101011601",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101011603",
			"name" : "阳山",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101011607",
			"name" : "英德",
			"type" : 3,
			"idx" : 7,
			"children" : []
		} ]
	}, {
		"id" : "c101010500",
		"name" : "汕头",
		"type" : 2,
		"idx" : 14,
		"children" : [ {
			"id" : "c101010502",
			"name" : "南澳",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101010501",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 1,
			"children" : []
		} ]
	}, {
		"id" : "c101011300",
		"name" : "汕尾",
		"type" : 2,
		"idx" : 15,
		"children" : [ {
			"id" : "c101011302",
			"name" : "海丰",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101011304",
			"name" : "陆丰",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101011303",
			"name" : "陆河",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101011301",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 3,
			"children" : []
		} ]
	}, {
		"id" : "c101010200",
		"name" : "韶关",
		"type" : 2,
		"idx" : 16,
		"children" : [ {
			"id" : "c101010207",
			"name" : "乐昌",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101010208",
			"name" : "南雄",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101010203",
			"name" : "仁化",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101010205",
			"name" : "乳源",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101010202",
			"name" : "始兴",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101010201",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101010204",
			"name" : "翁源",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101010206",
			"name" : "新丰",
			"type" : 3,
			"idx" : 7,
			"children" : []
		} ]
	}, {
		"id" : "c101011500",
		"name" : "阳江",
		"type" : 2,
		"idx" : 17,
		"children" : [ {
			"id" : "c101011501",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101011504",
			"name" : "阳春",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101011503",
			"name" : "阳东",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101011502",
			"name" : "阳西",
			"type" : 3,
			"idx" : 3,
			"children" : []
		} ]
	}, {
		"id" : "c101012100",
		"name" : "云浮",
		"type" : 2,
		"idx" : 18,
		"children" : [ {
			"id" : "c101012105",
			"name" : "罗定",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101012101",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101012102",
			"name" : "新兴",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101012103",
			"name" : "郁南",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101012104",
			"name" : "云安",
			"type" : 3,
			"idx" : 4,
			"children" : []
		} ]
	}, {
		"id" : "c101010800",
		"name" : "湛江",
		"type" : 2,
		"idx" : 19,
		"children" : [ {
			"id" : "c101010805",
			"name" : "雷州",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101010804",
			"name" : "廉江",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101010801",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101010802",
			"name" : "遂溪",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101010806",
			"name" : "吴川",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101010803",
			"name" : "徐闻",
			"type" : 3,
			"idx" : 5,
			"children" : []
		} ]
	}, {
		"id" : "c101011000",
		"name" : "肇庆",
		"type" : 2,
		"idx" : 20,
		"children" : [ {
			"id" : "c101011005",
			"name" : "德庆",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101011004",
			"name" : "封开",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101011006",
			"name" : "高要",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101011002",
			"name" : "广宁",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101011003",
			"name" : "怀集",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101011001",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101011007",
			"name" : "四会",
			"type" : 3,
			"idx" : 6,
			"children" : []
		} ]
	} ]
}, {
	"id" : "c101030000",
	"name" : "上海",
	"type" : 1,
	"idx" : 2,
	"city" : [ {
		"id" : "c101031000",
		"name" : "宝山区",
		"type" : 2,
		"idx" : 0,
		"children" : []
	}, {
		"id" : "c101030300",
		"name" : "长宁区",
		"type" : 2,
		"idx" : 1,
		"children" : []
	}, {
		"id" : "c101031700",
		"name" : "崇明",
		"type" : 2,
		"idx" : 2,
		"children" : []
	}, {
		"id" : "c101031600",
		"name" : "奉贤区",
		"type" : 2,
		"idx" : 3,
		"children" : []
	}, {
		"id" : "c101030700",
		"name" : "虹口区",
		"type" : 2,
		"idx" : 4,
		"children" : []
	}, {
		"id" : "c101030100",
		"name" : "黄浦区",
		"type" : 2,
		"idx" : 5,
		"children" : []
	}, {
		"id" : "c101031100",
		"name" : "嘉定区",
		"type" : 2,
		"idx" : 6,
		"children" : []
	}, {
		"id" : "c101031300",
		"name" : "金山区",
		"type" : 2,
		"idx" : 7,
		"children" : []
	}, {
		"id" : "c101030400",
		"name" : "静安区",
		"type" : 2,
		"idx" : 8,
		"children" : []
	}, {
		"id" : "c101030900",
		"name" : "闵行区",
		"type" : 2,
		"idx" : 9,
		"children" : []
	}, {
		"id" : "c101031200",
		"name" : "浦东新区",
		"type" : 2,
		"idx" : 10,
		"children" : []
	}, {
		"id" : "c101030500",
		"name" : "普陀区",
		"type" : 2,
		"idx" : 11,
		"children" : []
	}, {
		"id" : "c101031500",
		"name" : "青浦区",
		"type" : 2,
		"idx" : 12,
		"children" : []
	}, {
		"id" : "c101031400",
		"name" : "松江区",
		"type" : 2,
		"idx" : 13,
		"children" : []
	}, {
		"id" : "c101030200",
		"name" : "徐汇区",
		"type" : 2,
		"idx" : 14,
		"children" : []
	}, {
		"id" : "c101030800",
		"name" : "杨浦区",
		"type" : 2,
		"idx" : 15,
		"children" : []
	}, {
		"id" : "c101030600",
		"name" : "闸北区",
		"type" : 2,
		"idx" : 16,
		"children" : []
	} ]
}, {
	"id" : "c101040000",
	"name" : "天津",
	"type" : 1,
	"idx" : 3,
	"city" : [ {
		"id" : "c101041200",
		"name" : "宝坻区",
		"type" : 2,
		"idx" : 0,
		"children" : []
	}, {
		"id" : "c101041000",
		"name" : "北辰区",
		"type" : 2,
		"idx" : 1,
		"children" : []
	}, {
		"id" : "c101041300",
		"name" : "滨海新区",
		"type" : 2,
		"idx" : 2,
		"children" : []
	}, {
		"id" : "c101040700",
		"name" : "东丽区",
		"type" : 2,
		"idx" : 3,
		"children" : []
	}, {
		"id" : "c101040100",
		"name" : "和平区",
		"type" : 2,
		"idx" : 4,
		"children" : []
	}, {
		"id" : "c101040500",
		"name" : "河北区",
		"type" : 2,
		"idx" : 5,
		"children" : []
	}, {
		"id" : "c101040200",
		"name" : "河东区",
		"type" : 2,
		"idx" : 6,
		"children" : []
	}, {
		"id" : "c101040300",
		"name" : "河西区",
		"type" : 2,
		"idx" : 7,
		"children" : []
	}, {
		"id" : "c101040600",
		"name" : "红桥区",
		"type" : 2,
		"idx" : 8,
		"children" : []
	}, {
		"id" : "c101041600",
		"name" : "蓟县",
		"type" : 2,
		"idx" : 9,
		"children" : []
	}, {
		"id" : "c101040900",
		"name" : "津南区",
		"type" : 2,
		"idx" : 10,
		"children" : []
	}, {
		"id" : "c101041500",
		"name" : "静海",
		"type" : 2,
		"idx" : 11,
		"children" : []
	}, {
		"id" : "c101040400",
		"name" : "南开区",
		"type" : 2,
		"idx" : 12,
		"children" : []
	}, {
		"id" : "c101041400",
		"name" : "宁河",
		"type" : 2,
		"idx" : 13,
		"children" : []
	}, {
		"id" : "c101041100",
		"name" : "武清区",
		"type" : 2,
		"idx" : 14,
		"children" : []
	}, {
		"id" : "c101040800",
		"name" : "西青区",
		"type" : 2,
		"idx" : 15,
		"children" : []
	} ]
}, {
	"id" : "c101310000",
	"name" : "浙江",
	"type" : 1,
	"idx" : 4,
	"city" : [ {
		"id" : "c101310100",
		"name" : "杭州",
		"type" : 2,
		"idx" : 0,
		"children" : [ {
			"id" : "c101310106",
			"name" : "滨江区",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101310110",
			"name" : "淳安",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101310112",
			"name" : "富阳",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101310104",
			"name" : "拱墅区",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101310111",
			"name" : "建德",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101310103",
			"name" : "江干区",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101310113",
			"name" : "临安",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101310101",
			"name" : "上城区",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101310109",
			"name" : "桐庐",
			"type" : 3,
			"idx" : 8,
			"children" : []
		}, {
			"id" : "c101310105",
			"name" : "西湖区",
			"type" : 3,
			"idx" : 9,
			"children" : []
		}, {
			"id" : "c101310102",
			"name" : "下城区",
			"type" : 3,
			"idx" : 10,
			"children" : []
		}, {
			"id" : "c101310107",
			"name" : "萧山区",
			"type" : 3,
			"idx" : 11,
			"children" : []
		}, {
			"id" : "c101310108",
			"name" : "余杭区",
			"type" : 3,
			"idx" : 12,
			"children" : []
		} ]
	}, {
		"id" : "c101310200",
		"name" : "宁波",
		"type" : 2,
		"idx" : 1,
		"children" : [ {
			"id" : "c101310204",
			"name" : "北仑区",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101310210",
			"name" : "慈溪",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101310211",
			"name" : "奉化",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101310201",
			"name" : "海曙区",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101310203",
			"name" : "江北区",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101310202",
			"name" : "江东区",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101310208",
			"name" : "宁海",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101310207",
			"name" : "象山",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101310206",
			"name" : "鄞州区",
			"type" : 3,
			"idx" : 8,
			"children" : []
		}, {
			"id" : "c101310209",
			"name" : "余姚",
			"type" : 3,
			"idx" : 9,
			"children" : []
		}, {
			"id" : "c101310205",
			"name" : "镇海区",
			"type" : 3,
			"idx" : 10,
			"children" : []
		} ]
	}, {
		"id" : "c101310300",
		"name" : "温州",
		"type" : 2,
		"idx" : 2,
		"children" : [ {
			"id" : "c101310305",
			"name" : "苍南",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101310302",
			"name" : "洞头",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101310309",
			"name" : "乐清",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101310304",
			"name" : "平阳",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101310308",
			"name" : "瑞安",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101310301",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101310307",
			"name" : "泰顺",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101310306",
			"name" : "文成",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101310303",
			"name" : "永嘉",
			"type" : 3,
			"idx" : 8,
			"children" : []
		} ]
	}, {
		"id" : "c101310500",
		"name" : "湖州",
		"type" : 2,
		"idx" : 3,
		"children" : [ {
			"id" : "c101310504",
			"name" : "安吉",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101310503",
			"name" : "长兴",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101310502",
			"name" : "德清",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101310501",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 3,
			"children" : []
		} ]
	}, {
		"id" : "c101310400",
		"name" : "嘉兴",
		"type" : 2,
		"idx" : 4,
		"children" : [ {
			"id" : "c101310404",
			"name" : "海宁",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101310403",
			"name" : "海盐",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101310402",
			"name" : "嘉善",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101310405",
			"name" : "平湖",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101310401",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101310406",
			"name" : "桐乡",
			"type" : 3,
			"idx" : 5,
			"children" : []
		} ]
	}, {
		"id" : "c101310700",
		"name" : "金华",
		"type" : 2,
		"idx" : 5,
		"children" : [ {
			"id" : "c101310706",
			"name" : "义乌",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101310707",
			"name" : "东阳",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101310705",
			"name" : "兰溪",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101310704",
			"name" : "磐安",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101310703",
			"name" : "浦江",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101310701",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101310702",
			"name" : "武义",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101310708",
			"name" : "永康",
			"type" : 3,
			"idx" : 7,
			"children" : []
		} ]
	}, {
		"id" : "c101311100",
		"name" : "丽水",
		"type" : 2,
		"idx" : 6,
		"children" : [ {
			"id" : "c101311103",
			"name" : "缙云",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101311108",
			"name" : "景宁",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101311109",
			"name" : "龙泉",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101311102",
			"name" : "青田",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101311107",
			"name" : "庆元",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101311101",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101311105",
			"name" : "松阳",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101311104",
			"name" : "遂昌",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101311106",
			"name" : "云和",
			"type" : 3,
			"idx" : 8,
			"children" : []
		} ]
	}, {
		"id" : "c101310800",
		"name" : "衢州",
		"type" : 2,
		"idx" : 7,
		"children" : [ {
			"id" : "c101310802",
			"name" : "常山",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101310805",
			"name" : "江山",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101310803",
			"name" : "开化",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101310804",
			"name" : "龙游",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101310801",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 4,
			"children" : []
		} ]
	}, {
		"id" : "c101310600",
		"name" : "绍兴",
		"type" : 2,
		"idx" : 8,
		"children" : [ {
			"id" : "c101310605",
			"name" : "上虞",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101310602",
			"name" : "绍兴县",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101310606",
			"name" : "嵊州",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101310601",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101310603",
			"name" : "新昌",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101310604",
			"name" : "诸暨",
			"type" : 3,
			"idx" : 5,
			"children" : []
		} ]
	}, {
		"id" : "c101311000",
		"name" : "台州",
		"type" : 2,
		"idx" : 9,
		"children" : [ {
			"id" : "c101311007",
			"name" : "临海",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101311003",
			"name" : "三门",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101311001",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101311004",
			"name" : "天台",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101311006",
			"name" : "温岭",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101311005",
			"name" : "仙居",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101311002",
			"name" : "玉环",
			"type" : 3,
			"idx" : 6,
			"children" : []
		} ]
	}, {
		"id" : "c101310900",
		"name" : "舟山",
		"type" : 2,
		"idx" : 10,
		"children" : [ {
			"id" : "c101310902",
			"name" : "岱山",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101310903",
			"name" : "嵊泗",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101310901",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 2,
			"children" : []
		} ]
	} ]
}, {
	"id" : "c101050000",
	"name" : "重庆",
	"type" : 1,
	"idx" : 5,
	"city" : [ {
		"id" : "c101051300",
		"name" : "巴南区",
		"type" : 2,
		"idx" : 0,
		"children" : []
	}, {
		"id" : "c101050900",
		"name" : "北碚区",
		"type" : 2,
		"idx" : 1,
		"children" : []
	}, {
		"id" : "c101052300",
		"name" : "璧山",
		"type" : 2,
		"idx" : 2,
		"children" : []
	}, {
		"id" : "c101051500",
		"name" : "长寿区",
		"type" : 2,
		"idx" : 3,
		"children" : []
	}, {
		"id" : "c101052500",
		"name" : "城口",
		"type" : 2,
		"idx" : 4,
		"children" : []
	}, {
		"id" : "c101050400",
		"name" : "大渡口区",
		"type" : 2,
		"idx" : 5,
		"children" : []
	}, {
		"id" : "c101051100",
		"name" : "大足区",
		"type" : 2,
		"idx" : 6,
		"children" : []
	}, {
		"id" : "c101052700",
		"name" : "垫江",
		"type" : 2,
		"idx" : 7,
		"children" : []
	}, {
		"id" : "c101052600",
		"name" : "丰都",
		"type" : 2,
		"idx" : 8,
		"children" : []
	}, {
		"id" : "c101053200",
		"name" : "奉节",
		"type" : 2,
		"idx" : 9,
		"children" : []
	}, {
		"id" : "c101050200",
		"name" : "涪陵区",
		"type" : 2,
		"idx" : 10,
		"children" : []
	}, {
		"id" : "c101051700",
		"name" : "合川区",
		"type" : 2,
		"idx" : 11,
		"children" : []
	}, {
		"id" : "c101050500",
		"name" : "江北区",
		"type" : 2,
		"idx" : 12,
		"children" : []
	}, {
		"id" : "c101051600",
		"name" : "江津区",
		"type" : 2,
		"idx" : 13,
		"children" : []
	}, {
		"id" : "c101050700",
		"name" : "九龙坡区",
		"type" : 2,
		"idx" : 14,
		"children" : []
	}, {
		"id" : "c101053000",
		"name" : "开县",
		"type" : 2,
		"idx" : 15,
		"children" : []
	}, {
		"id" : "c101052400",
		"name" : "梁平",
		"type" : 2,
		"idx" : 16,
		"children" : []
	}, {
		"id" : "c101050800",
		"name" : "南岸区",
		"type" : 2,
		"idx" : 17,
		"children" : []
	}, {
		"id" : "c101051900",
		"name" : "南川区",
		"type" : 2,
		"idx" : 18,
		"children" : []
	}, {
		"id" : "c101053800",
		"name" : "彭水",
		"type" : 2,
		"idx" : 19,
		"children" : []
	}, {
		"id" : "c101051000",
		"name" : "綦江区",
		"type" : 2,
		"idx" : 20,
		"children" : []
	}, {
		"id" : "c101051400",
		"name" : "黔江区",
		"type" : 2,
		"idx" : 21,
		"children" : []
	}, {
		"id" : "c101052200",
		"name" : "荣昌",
		"type" : 2,
		"idx" : 22,
		"children" : []
	}, {
		"id" : "c101050600",
		"name" : "沙坪坝区",
		"type" : 2,
		"idx" : 23,
		"children" : []
	}, {
		"id" : "c101053500",
		"name" : "石柱",
		"type" : 2,
		"idx" : 24,
		"children" : []
	}, {
		"id" : "c101052100",
		"name" : "铜梁",
		"type" : 2,
		"idx" : 25,
		"children" : []
	}, {
		"id" : "c101052000",
		"name" : "潼南",
		"type" : 2,
		"idx" : 26,
		"children" : []
	}, {
		"id" : "c101050100",
		"name" : "万州区",
		"type" : 2,
		"idx" : 27,
		"children" : []
	}, {
		"id" : "c101053300",
		"name" : "巫山",
		"type" : 2,
		"idx" : 28,
		"children" : []
	}, {
		"id" : "c101053400",
		"name" : "巫溪",
		"type" : 2,
		"idx" : 29,
		"children" : []
	}, {
		"id" : "c101052800",
		"name" : "武隆",
		"type" : 2,
		"idx" : 30,
		"children" : []
	}, {
		"id" : "c101053600",
		"name" : "秀山",
		"type" : 2,
		"idx" : 31,
		"children" : []
	}, {
		"id" : "c101051800",
		"name" : "永川区",
		"type" : 2,
		"idx" : 32,
		"children" : []
	}, {
		"id" : "c101053700",
		"name" : "酉阳",
		"type" : 2,
		"idx" : 33,
		"children" : []
	}, {
		"id" : "c101051200",
		"name" : "渝北区",
		"type" : 2,
		"idx" : 34,
		"children" : []
	}, {
		"id" : "c101050300",
		"name" : "渝中区",
		"type" : 2,
		"idx" : 35,
		"children" : []
	}, {
		"id" : "c101053100",
		"name" : "云阳",
		"type" : 2,
		"idx" : 36,
		"children" : []
	}, {
		"id" : "c101052900",
		"name" : "忠县",
		"type" : 2,
		"idx" : 37,
		"children" : []
	} ]
}, {
	"id" : "c101060000",
	"name" : "安徽",
	"type" : 1,
	"idx" : 6,
	"city" : [ {
		"id" : "c101060100",
		"name" : "合肥",
		"type" : 2,
		"idx" : 0,
		"children" : [ {
			"id" : "c101060104",
			"name" : "包河区",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101060105",
			"name" : "长丰",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101060109",
			"name" : "巢湖",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101060106",
			"name" : "肥东",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101060107",
			"name" : "肥西",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101060108",
			"name" : "庐江",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101060102",
			"name" : "庐阳区",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101060103",
			"name" : "蜀山区",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101060101",
			"name" : "瑶海区",
			"type" : 3,
			"idx" : 8,
			"children" : []
		} ]
	}, {
		"id" : "c101060800",
		"name" : "安庆",
		"type" : 2,
		"idx" : 1,
		"children" : [ {
			"id" : "c101060803",
			"name" : "枞阳",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101060802",
			"name" : "怀宁",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101060804",
			"name" : "潜山",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101060801",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101060806",
			"name" : "宿松",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101060805",
			"name" : "太湖",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101060809",
			"name" : "桐城",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101060807",
			"name" : "望江",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101060808",
			"name" : "岳西",
			"type" : 3,
			"idx" : 8,
			"children" : []
		} ]
	}, {
		"id" : "c101060300",
		"name" : "蚌埠",
		"type" : 2,
		"idx" : 2,
		"children" : [ {
			"id" : "c101060304",
			"name" : "固镇",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101060302",
			"name" : "怀远",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101060301",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101060303",
			"name" : "五河",
			"type" : 3,
			"idx" : 3,
			"children" : []
		} ]
	}, {
		"id" : "c101061400",
		"name" : "亳州",
		"type" : 2,
		"idx" : 3,
		"children" : [ {
			"id" : "c101061404",
			"name" : "利辛",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101061403",
			"name" : "蒙城",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101061401",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101061402",
			"name" : "涡阳",
			"type" : 3,
			"idx" : 3,
			"children" : []
		} ]
	}, {
		"id" : "c101061500",
		"name" : "池州",
		"type" : 2,
		"idx" : 4,
		"children" : [ {
			"id" : "c101061502",
			"name" : "东至",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101061504",
			"name" : "青阳",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101061503",
			"name" : "石台",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101061501",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 3,
			"children" : []
		} ]
	}, {
		"id" : "c101061000",
		"name" : "滁州",
		"type" : 2,
		"idx" : 5,
		"children" : [ {
			"id" : "c101061004",
			"name" : "定远",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101061005",
			"name" : "凤阳",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101061002",
			"name" : "来安",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101061007",
			"name" : "明光",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101061003",
			"name" : "全椒",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101061001",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101061006",
			"name" : "天长",
			"type" : 3,
			"idx" : 6,
			"children" : []
		} ]
	}, {
		"id" : "c101061100",
		"name" : "阜阳",
		"type" : 2,
		"idx" : 6,
		"children" : [ {
			"id" : "c101061104",
			"name" : "阜南",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101061106",
			"name" : "界首",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101061102",
			"name" : "临泉",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101061101",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101061103",
			"name" : "太和",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101061105",
			"name" : "颍上",
			"type" : 3,
			"idx" : 5,
			"children" : []
		} ]
	}, {
		"id" : "c101060600",
		"name" : "淮北",
		"type" : 2,
		"idx" : 7,
		"children" : [ {
			"id" : "c101060601",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101060602",
			"name" : "濉溪",
			"type" : 3,
			"idx" : 1,
			"children" : []
		} ]
	}, {
		"id" : "c101060400",
		"name" : "淮南",
		"type" : 2,
		"idx" : 8,
		"children" : [ {
			"id" : "c101060402",
			"name" : "凤台",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101060401",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 1,
			"children" : []
		} ]
	}, {
		"id" : "c101060900",
		"name" : "黄山",
		"type" : 2,
		"idx" : 9,
		"children" : [ {
			"id" : "c101060905",
			"name" : "祁门",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101060901",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101060902",
			"name" : "歙县",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101060903",
			"name" : "休宁",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101060904",
			"name" : "黟县",
			"type" : 3,
			"idx" : 4,
			"children" : []
		} ]
	}, {
		"id" : "c101061300",
		"name" : "六安",
		"type" : 2,
		"idx" : 10,
		"children" : [ {
			"id" : "c101061303",
			"name" : "霍邱",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101061306",
			"name" : "霍山",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101061305",
			"name" : "金寨",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101061301",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101061302",
			"name" : "寿县",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101061304",
			"name" : "舒城",
			"type" : 3,
			"idx" : 5,
			"children" : []
		} ]
	}, {
		"id" : "c101060500",
		"name" : "马鞍山",
		"type" : 2,
		"idx" : 11,
		"children" : [ {
			"id" : "c101060502",
			"name" : "当涂",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101060503",
			"name" : "含山",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101060504",
			"name" : "和县",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101060501",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 3,
			"children" : []
		} ]
	}, {
		"id" : "c101061200",
		"name" : "宿州",
		"type" : 2,
		"idx" : 12,
		"children" : [ {
			"id" : "c101061202",
			"name" : "砀山",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101061204",
			"name" : "灵璧",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101061201",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101061205",
			"name" : "泗县",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101061203",
			"name" : "萧县",
			"type" : 3,
			"idx" : 4,
			"children" : []
		} ]
	}, {
		"id" : "c101060700",
		"name" : "铜陵",
		"type" : 2,
		"idx" : 13,
		"children" : [ {
			"id" : "c101060701",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101060702",
			"name" : "铜陵县",
			"type" : 3,
			"idx" : 1,
			"children" : []
		} ]
	}, {
		"id" : "c101060200",
		"name" : "芜湖",
		"type" : 2,
		"idx" : 14,
		"children" : [ {
			"id" : "c101060203",
			"name" : "繁昌",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101060204",
			"name" : "南陵",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101060201",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101060205",
			"name" : "无为",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101060202",
			"name" : "芜湖县",
			"type" : 3,
			"idx" : 4,
			"children" : []
		} ]
	}, {
		"id" : "c101061600",
		"name" : "宣城",
		"type" : 2,
		"idx" : 15,
		"children" : [ {
			"id" : "c101061603",
			"name" : "广德",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101061605",
			"name" : "绩溪",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101061604",
			"name" : "泾县",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101061606",
			"name" : "旌德",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101061602",
			"name" : "郎溪",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101061607",
			"name" : "宁国",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101061601",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 6,
			"children" : []
		} ]
	} ]
}, {
	"id" : "c101320000",
	"name" : "澳门",
	"type" : 1,
	"idx" : 7,
	"city" : [ {
		"id" : "c101320100",
		"name" : "澳门",
		"type" : 2,
		"idx" : 0,
		"children" : []
	} ]
}, {
	"id" : "c101070000",
	"name" : "福建",
	"type" : 1,
	"idx" : 8,
	"city" : [ {
		"id" : "c101070100",
		"name" : "福州",
		"type" : 2,
		"idx" : 0,
		"children" : [ {
			"id" : "c101070103",
			"name" : "仓山区",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101070113",
			"name" : "长乐",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101070112",
			"name" : "福清",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101070101",
			"name" : "鼓楼区",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101070105",
			"name" : "晋安区",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101070107",
			"name" : "连江",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101070108",
			"name" : "罗源",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101070104",
			"name" : "马尾区",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101070106",
			"name" : "闽侯",
			"type" : 3,
			"idx" : 8,
			"children" : []
		}, {
			"id" : "c101070109",
			"name" : "闽清",
			"type" : 3,
			"idx" : 9,
			"children" : []
		}, {
			"id" : "c101070111",
			"name" : "平潭",
			"type" : 3,
			"idx" : 10,
			"children" : []
		}, {
			"id" : "c101070102",
			"name" : "台江区",
			"type" : 3,
			"idx" : 11,
			"children" : []
		}, {
			"id" : "c101070110",
			"name" : "永泰",
			"type" : 3,
			"idx" : 12,
			"children" : []
		} ]
	}, {
		"id" : "c101070200",
		"name" : "厦门",
		"type" : 2,
		"idx" : 1,
		"children" : [ {
			"id" : "c101070202",
			"name" : "海沧区",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101070203",
			"name" : "湖里区",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101070204",
			"name" : "集美区",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101070201",
			"name" : "思明区",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101070205",
			"name" : "同安区",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101070206",
			"name" : "翔安区",
			"type" : 3,
			"idx" : 5,
			"children" : []
		} ]
	}, {
		"id" : "c101070800",
		"name" : "龙岩",
		"type" : 2,
		"idx" : 2,
		"children" : [ {
			"id" : "c101070802",
			"name" : "长汀",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101070806",
			"name" : "连城",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101070804",
			"name" : "上杭",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101070801",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101070805",
			"name" : "武平",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101070803",
			"name" : "永定",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101070807",
			"name" : "漳平",
			"type" : 3,
			"idx" : 6,
			"children" : []
		} ]
	}, {
		"id" : "c101070700",
		"name" : "南平",
		"type" : 2,
		"idx" : 3,
		"children" : [ {
			"id" : "c101070704",
			"name" : "光泽",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101070709",
			"name" : "建瓯",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101070710",
			"name" : "建阳",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101070703",
			"name" : "浦城",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101070707",
			"name" : "邵武",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101070701",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101070702",
			"name" : "顺昌",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101070705",
			"name" : "松溪",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101070708",
			"name" : "武夷山",
			"type" : 3,
			"idx" : 8,
			"children" : []
		}, {
			"id" : "c101070706",
			"name" : "政和",
			"type" : 3,
			"idx" : 9,
			"children" : []
		} ]
	}, {
		"id" : "c101070900",
		"name" : "宁德",
		"type" : 2,
		"idx" : 4,
		"children" : [ {
			"id" : "c101070908",
			"name" : "福安",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101070909",
			"name" : "福鼎",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101070903",
			"name" : "古田",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101070904",
			"name" : "屏南",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101070901",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101070905",
			"name" : "寿宁",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101070902",
			"name" : "霞浦",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101070907",
			"name" : "柘荣",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101070906",
			"name" : "周宁",
			"type" : 3,
			"idx" : 8,
			"children" : []
		} ]
	}, {
		"id" : "c101070300",
		"name" : "莆田",
		"type" : 2,
		"idx" : 5,
		"children" : [ {
			"id" : "c101070301",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101070302",
			"name" : "仙游",
			"type" : 3,
			"idx" : 1,
			"children" : []
		} ]
	}, {
		"id" : "c101070500",
		"name" : "泉州",
		"type" : 2,
		"idx" : 6,
		"children" : [ {
			"id" : "c101070503",
			"name" : "安溪",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101070505",
			"name" : "德化",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101070502",
			"name" : "惠安",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101070506",
			"name" : "金门",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101070508",
			"name" : "晋江",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101070509",
			"name" : "南安",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101070507",
			"name" : "石狮",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101070501",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101070504",
			"name" : "永春",
			"type" : 3,
			"idx" : 8,
			"children" : []
		} ]
	}, {
		"id" : "c101070400",
		"name" : "三明",
		"type" : 2,
		"idx" : 7,
		"children" : [ {
			"id" : "c101070405",
			"name" : "大田",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101070410",
			"name" : "建宁",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101070408",
			"name" : "将乐",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101070402",
			"name" : "明溪",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101070404",
			"name" : "宁化",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101070403",
			"name" : "清流",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101070407",
			"name" : "沙县",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101070401",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101070409",
			"name" : "泰宁",
			"type" : 3,
			"idx" : 8,
			"children" : []
		}, {
			"id" : "c101070411",
			"name" : "永安",
			"type" : 3,
			"idx" : 9,
			"children" : []
		}, {
			"id" : "c101070406",
			"name" : "尤溪",
			"type" : 3,
			"idx" : 10,
			"children" : []
		} ]
	}, {
		"id" : "c101070600",
		"name" : "漳州",
		"type" : 2,
		"idx" : 8,
		"children" : [ {
			"id" : "c101070605",
			"name" : "长泰",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101070606",
			"name" : "东山",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101070609",
			"name" : "华安",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101070610",
			"name" : "龙海",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101070607",
			"name" : "南靖",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101070608",
			"name" : "平和",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101070601",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101070602",
			"name" : "云霄",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101070603",
			"name" : "漳浦",
			"type" : 3,
			"idx" : 8,
			"children" : []
		}, {
			"id" : "c101070604",
			"name" : "诏安",
			"type" : 3,
			"idx" : 9,
			"children" : []
		} ]
	} ]
}, {
	"id" : "c101080000",
	"name" : "甘肃",
	"type" : 1,
	"idx" : 9,
	"city" : [ {
		"id" : "c101080400",
		"name" : "白银",
		"type" : 2,
		"idx" : 0,
		"children" : [ {
			"id" : "c101080403",
			"name" : "会宁",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101080404",
			"name" : "景泰",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101080402",
			"name" : "靖远",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101080401",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 3,
			"children" : []
		} ]
	}, {
		"id" : "c101081100",
		"name" : "定西",
		"type" : 2,
		"idx" : 1,
		"children" : [ {
			"id" : "c101081105",
			"name" : "临洮",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101081103",
			"name" : "陇西",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101081107",
			"name" : "岷县",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101081101",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101081102",
			"name" : "通渭",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101081104",
			"name" : "渭源",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101081106",
			"name" : "漳县",
			"type" : 3,
			"idx" : 6,
			"children" : []
		} ]
	}, {
		"id" : "c101081400",
		"name" : "甘南",
		"type" : 2,
		"idx" : 2,
		"children" : [ {
			"id" : "c101081405",
			"name" : "迭部",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101081401",
			"name" : "合作",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101081402",
			"name" : "临潭",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101081407",
			"name" : "碌曲",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101081406",
			"name" : "玛曲",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101081408",
			"name" : "夏河",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101081404",
			"name" : "舟曲",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101081403",
			"name" : "卓尼",
			"type" : 3,
			"idx" : 7,
			"children" : []
		} ]
	}, {
		"id" : "c101080200",
		"name" : "嘉峪关",
		"type" : 2,
		"idx" : 3,
		"children" : [ {
			"id" : "c101080201",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 0,
			"children" : []
		} ]
	}, {
		"id" : "c101080300",
		"name" : "金昌",
		"type" : 2,
		"idx" : 4,
		"children" : [ {
			"id" : "c101080301",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101080302",
			"name" : "永昌",
			"type" : 3,
			"idx" : 1,
			"children" : []
		} ]
	}, {
		"id" : "c101080900",
		"name" : "酒泉",
		"type" : 2,
		"idx" : 5,
		"children" : [ {
			"id" : "c101080905",
			"name" : "阿克塞",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101080907",
			"name" : "敦煌",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101080903",
			"name" : "瓜州",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101080902",
			"name" : "金塔",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101080901",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101080904",
			"name" : "肃北",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101080906",
			"name" : "玉门",
			"type" : 3,
			"idx" : 6,
			"children" : []
		} ]
	}, {
		"id" : "c101080100",
		"name" : "兰州",
		"type" : 2,
		"idx" : 6,
		"children" : [ {
			"id" : "c101080104",
			"name" : "安宁区",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101080101",
			"name" : "城关区",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101080107",
			"name" : "皋兰",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101080105",
			"name" : "红古区",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101080102",
			"name" : "七里河区",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101080103",
			"name" : "西固区",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101080106",
			"name" : "永登",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101080108",
			"name" : "榆中",
			"type" : 3,
			"idx" : 7,
			"children" : []
		} ]
	}, {
		"id" : "c101081300",
		"name" : "临夏",
		"type" : 2,
		"idx" : 7,
		"children" : [ {
			"id" : "c101081307",
			"name" : "东乡族自治县",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101081305",
			"name" : "广河",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101081306",
			"name" : "和政",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101081308",
			"name" : "积石山",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101081303",
			"name" : "康乐",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101081301",
			"name" : "临夏市",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101081302",
			"name" : "临夏县",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101081304",
			"name" : "永靖",
			"type" : 3,
			"idx" : 7,
			"children" : []
		} ]
	}, {
		"id" : "c101081200",
		"name" : "陇南",
		"type" : 2,
		"idx" : 8,
		"children" : [ {
			"id" : "c101081202",
			"name" : "成县",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101081204",
			"name" : "宕昌",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101081208",
			"name" : "徽县",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101081205",
			"name" : "康县",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101081207",
			"name" : "礼县",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101081209",
			"name" : "两当",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101081201",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101081203",
			"name" : "文县",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101081206",
			"name" : "西和",
			"type" : 3,
			"idx" : 8,
			"children" : []
		} ]
	}, {
		"id" : "c101080800",
		"name" : "平凉",
		"type" : 2,
		"idx" : 9,
		"children" : [ {
			"id" : "c101080804",
			"name" : "崇信",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101080805",
			"name" : "华亭",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101080802",
			"name" : "泾川",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101080807",
			"name" : "静宁",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101080803",
			"name" : "灵台",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101080801",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101080806",
			"name" : "庄浪",
			"type" : 3,
			"idx" : 6,
			"children" : []
		} ]
	}, {
		"id" : "c101081000",
		"name" : "庆阳",
		"type" : 2,
		"idx" : 10,
		"children" : [ {
			"id" : "c101081005",
			"name" : "合水",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101081004",
			"name" : "华池",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101081003",
			"name" : "环县",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101081007",
			"name" : "宁县",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101081002",
			"name" : "庆城",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101081001",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101081008",
			"name" : "镇原",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101081006",
			"name" : "正宁",
			"type" : 3,
			"idx" : 7,
			"children" : []
		} ]
	}, {
		"id" : "c101080500",
		"name" : "天水",
		"type" : 2,
		"idx" : 11,
		"children" : [ {
			"id" : "c101080504",
			"name" : "甘谷",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101080503",
			"name" : "秦安",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101080502",
			"name" : "清水",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101080501",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101080505",
			"name" : "武山",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101080506",
			"name" : "张家川",
			"type" : 3,
			"idx" : 5,
			"children" : []
		} ]
	}, {
		"id" : "c101080600",
		"name" : "武威",
		"type" : 2,
		"idx" : 12,
		"children" : [ {
			"id" : "c101080603",
			"name" : "古浪",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101080602",
			"name" : "民勤",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101080601",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101080604",
			"name" : "天祝",
			"type" : 3,
			"idx" : 3,
			"children" : []
		} ]
	}, {
		"id" : "c101080700",
		"name" : "张掖",
		"type" : 2,
		"idx" : 13,
		"children" : [ {
			"id" : "c101080705",
			"name" : "高台",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101080704",
			"name" : "临泽",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101080703",
			"name" : "民乐",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101080706",
			"name" : "山丹",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101080701",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101080702",
			"name" : "肃南",
			"type" : 3,
			"idx" : 5,
			"children" : []
		} ]
	} ]
}, {
	"id" : "c101090000",
	"name" : "广西",
	"type" : 1,
	"idx" : 10,
	"city" : [ {
		"id" : "c101090100",
		"name" : "南宁",
		"type" : 2,
		"idx" : 0,
		"children" : [ {
			"id" : "c101090111",
			"name" : "宾阳",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101090112",
			"name" : "横县",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101090103",
			"name" : "江南区",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101090105",
			"name" : "良庆区",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101090108",
			"name" : "隆安",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101090109",
			"name" : "马山",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101090102",
			"name" : "青秀区",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101090110",
			"name" : "上林",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101090107",
			"name" : "武鸣",
			"type" : 3,
			"idx" : 8,
			"children" : []
		}, {
			"id" : "c101090104",
			"name" : "西乡塘区",
			"type" : 3,
			"idx" : 9,
			"children" : []
		}, {
			"id" : "c101090101",
			"name" : "兴宁区",
			"type" : 3,
			"idx" : 10,
			"children" : []
		}, {
			"id" : "c101090106",
			"name" : "邕宁区",
			"type" : 3,
			"idx" : 11,
			"children" : []
		} ]
	}, {
		"id" : "c101091000",
		"name" : "百色",
		"type" : 2,
		"idx" : 1,
		"children" : [ {
			"id" : "c101091005",
			"name" : "德保",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101091006",
			"name" : "靖西",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101091009",
			"name" : "乐业",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101091008",
			"name" : "凌云",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101091012",
			"name" : "隆林",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101091007",
			"name" : "那坡",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101091004",
			"name" : "平果",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101091001",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101091003",
			"name" : "田东",
			"type" : 3,
			"idx" : 8,
			"children" : []
		}, {
			"id" : "c101091010",
			"name" : "田林",
			"type" : 3,
			"idx" : 9,
			"children" : []
		}, {
			"id" : "c101091002",
			"name" : "田阳",
			"type" : 3,
			"idx" : 10,
			"children" : []
		}, {
			"id" : "c101091011",
			"name" : "西林",
			"type" : 3,
			"idx" : 11,
			"children" : []
		} ]
	}, {
		"id" : "c101090500",
		"name" : "北海",
		"type" : 2,
		"idx" : 2,
		"children" : [ {
			"id" : "c101090502",
			"name" : "合浦",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101090501",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 1,
			"children" : []
		} ]
	}, {
		"id" : "c101091400",
		"name" : "崇左",
		"type" : 2,
		"idx" : 3,
		"children" : [ {
			"id" : "c101091405",
			"name" : "大新",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101091402",
			"name" : "扶绥",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101091404",
			"name" : "龙州",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101091403",
			"name" : "宁明",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101091407",
			"name" : "凭祥",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101091401",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101091406",
			"name" : "天等",
			"type" : 3,
			"idx" : 6,
			"children" : []
		} ]
	}, {
		"id" : "c101090600",
		"name" : "防城港",
		"type" : 2,
		"idx" : 4,
		"children" : [ {
			"id" : "c101090603",
			"name" : "东兴",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101090602",
			"name" : "上思",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101090601",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 2,
			"children" : []
		} ]
	}, {
		"id" : "c101090800",
		"name" : "贵港",
		"type" : 2,
		"idx" : 5,
		"children" : [ {
			"id" : "c101090803",
			"name" : "桂平",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101090802",
			"name" : "平南",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101090801",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 2,
			"children" : []
		} ]
	}, {
		"id" : "c101090300",
		"name" : "桂林",
		"type" : 2,
		"idx" : 6,
		"children" : [ {
			"id" : "c101090313",
			"name" : "恭城",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101090308",
			"name" : "灌阳",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101090312",
			"name" : "荔浦",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101090303",
			"name" : "临桂",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101090304",
			"name" : "灵川",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101090309",
			"name" : "龙胜",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101090311",
			"name" : "平乐",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101090305",
			"name" : "全州",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101090301",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 8,
			"children" : []
		}, {
			"id" : "c101090306",
			"name" : "兴安",
			"type" : 3,
			"idx" : 9,
			"children" : []
		}, {
			"id" : "c101090302",
			"name" : "阳朔",
			"type" : 3,
			"idx" : 10,
			"children" : []
		}, {
			"id" : "c101090307",
			"name" : "永福",
			"type" : 3,
			"idx" : 11,
			"children" : []
		}, {
			"id" : "c101090310",
			"name" : "资源",
			"type" : 3,
			"idx" : 12,
			"children" : []
		} ]
	}, {
		"id" : "c101091200",
		"name" : "河池",
		"type" : 2,
		"idx" : 7,
		"children" : [ {
			"id" : "c101091208",
			"name" : "巴马",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101091210",
			"name" : "大化",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101091205",
			"name" : "东兰",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101091209",
			"name" : "都安",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101091204",
			"name" : "凤山",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101091207",
			"name" : "环江",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101091206",
			"name" : "罗城",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101091202",
			"name" : "南丹",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101091201",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 8,
			"children" : []
		}, {
			"id" : "c101091203",
			"name" : "天峨",
			"type" : 3,
			"idx" : 9,
			"children" : []
		}, {
			"id" : "c101091211",
			"name" : "宜州",
			"type" : 3,
			"idx" : 10,
			"children" : []
		} ]
	}, {
		"id" : "c101091100",
		"name" : "贺州",
		"type" : 2,
		"idx" : 8,
		"children" : [ {
			"id" : "c101091104",
			"name" : "富川",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101091101",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101091102",
			"name" : "昭平",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101091103",
			"name" : "钟山",
			"type" : 3,
			"idx" : 3,
			"children" : []
		} ]
	}, {
		"id" : "c101091300",
		"name" : "来宾",
		"type" : 2,
		"idx" : 9,
		"children" : [ {
			"id" : "c101091306",
			"name" : "合山",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101091305",
			"name" : "金秀",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101091301",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101091304",
			"name" : "武宣",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101091303",
			"name" : "象州",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101091302",
			"name" : "忻城",
			"type" : 3,
			"idx" : 5,
			"children" : []
		} ]
	}, {
		"id" : "c101090200",
		"name" : "柳州",
		"type" : 2,
		"idx" : 10,
		"children" : [ {
			"id" : "c101090203",
			"name" : "柳城",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101090202",
			"name" : "柳江",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101090204",
			"name" : "鹿寨",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101090205",
			"name" : "融安",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101090206",
			"name" : "融水",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101090207",
			"name" : "三江",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101090201",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 6,
			"children" : []
		} ]
	}, {
		"id" : "c101090700",
		"name" : "钦州",
		"type" : 2,
		"idx" : 11,
		"children" : [ {
			"id" : "c101090702",
			"name" : "灵山",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101090703",
			"name" : "浦北",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101090701",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 2,
			"children" : []
		} ]
	}, {
		"id" : "c101090400",
		"name" : "梧州",
		"type" : 2,
		"idx" : 12,
		"children" : [ {
			"id" : "c101090402",
			"name" : "苍梧",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101090405",
			"name" : "岑溪",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101090404",
			"name" : "蒙山",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101090401",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101090403",
			"name" : "藤县",
			"type" : 3,
			"idx" : 4,
			"children" : []
		} ]
	}, {
		"id" : "c101090900",
		"name" : "玉林",
		"type" : 2,
		"idx" : 13,
		"children" : [ {
			"id" : "c101090906",
			"name" : "北流",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101090904",
			"name" : "博白",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101090903",
			"name" : "陆川",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101090902",
			"name" : "容县",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101090901",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101090905",
			"name" : "兴业",
			"type" : 3,
			"idx" : 5,
			"children" : []
		} ]
	} ]
}, {
	"id" : "c101100000",
	"name" : "贵州",
	"type" : 1,
	"idx" : 11,
	"city" : [ {
		"id" : "c101100400",
		"name" : "安顺",
		"type" : 2,
		"idx" : 0,
		"children" : [ {
			"id" : "c101100405",
			"name" : "关岭",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101100402",
			"name" : "平坝",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101100403",
			"name" : "普定",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101100401",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101100404",
			"name" : "镇宁",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101100406",
			"name" : "紫云",
			"type" : 3,
			"idx" : 5,
			"children" : []
		} ]
	}, {
		"id" : "c101100500",
		"name" : "毕节",
		"type" : 2,
		"idx" : 1,
		"children" : [ {
			"id" : "c101100502",
			"name" : "大方",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101100508",
			"name" : "赫章",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101100504",
			"name" : "金沙",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101100506",
			"name" : "纳雍",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101100503",
			"name" : "黔西",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101100501",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101100507",
			"name" : "威宁",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101100505",
			"name" : "织金",
			"type" : 3,
			"idx" : 7,
			"children" : []
		} ]
	}, {
		"id" : "c101100100",
		"name" : "贵阳",
		"type" : 2,
		"idx" : 2,
		"children" : [ {
			"id" : "c101100105",
			"name" : "白云区",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101100103",
			"name" : "花溪区",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101100107",
			"name" : "开阳",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101100101",
			"name" : "南明区",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101100110",
			"name" : "清镇",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101100104",
			"name" : "乌当区",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101100108",
			"name" : "息烽",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101100106",
			"name" : "小河区",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101100109",
			"name" : "修文",
			"type" : 3,
			"idx" : 8,
			"children" : []
		}, {
			"id" : "c101100102",
			"name" : "云岩区",
			"type" : 3,
			"idx" : 9,
			"children" : []
		} ]
	}, {
		"id" : "c101100200",
		"name" : "六盘水",
		"type" : 2,
		"idx" : 3,
		"children" : [ {
			"id" : "c101100202",
			"name" : "六枝特区",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101100204",
			"name" : "盘县",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101100203",
			"name" : "水城",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101100201",
			"name" : "钟山区",
			"type" : 3,
			"idx" : 3,
			"children" : []
		} ]
	}, {
		"id" : "c101100800",
		"name" : "黔东南",
		"type" : 2,
		"idx" : 4,
		"children" : [ {
			"id" : "c101100806",
			"name" : "岑巩",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101100813",
			"name" : "从江",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101100816",
			"name" : "丹寨",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101100802",
			"name" : "黄平",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101100809",
			"name" : "剑河",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101100808",
			"name" : "锦屏",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101100801",
			"name" : "凯里",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101100814",
			"name" : "雷山",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101100811",
			"name" : "黎平",
			"type" : 3,
			"idx" : 8,
			"children" : []
		}, {
			"id" : "c101100815",
			"name" : "麻江",
			"type" : 3,
			"idx" : 9,
			"children" : []
		}, {
			"id" : "c101100812",
			"name" : "榕江",
			"type" : 3,
			"idx" : 10,
			"children" : []
		}, {
			"id" : "c101100804",
			"name" : "三穗",
			"type" : 3,
			"idx" : 11,
			"children" : []
		}, {
			"id" : "c101100803",
			"name" : "施秉",
			"type" : 3,
			"idx" : 12,
			"children" : []
		}, {
			"id" : "c101100810",
			"name" : "台江",
			"type" : 3,
			"idx" : 13,
			"children" : []
		}, {
			"id" : "c101100807",
			"name" : "天柱",
			"type" : 3,
			"idx" : 14,
			"children" : []
		}, {
			"id" : "c101100805",
			"name" : "镇远",
			"type" : 3,
			"idx" : 15,
			"children" : []
		} ]
	}, {
		"id" : "c101100900",
		"name" : "黔南",
		"type" : 2,
		"idx" : 5,
		"children" : [ {
			"id" : "c101100909",
			"name" : "长顺",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101100901",
			"name" : "都匀",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101100906",
			"name" : "独山",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101100902",
			"name" : "福泉",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101100904",
			"name" : "贵定",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101100911",
			"name" : "惠水",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101100903",
			"name" : "荔波",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101100910",
			"name" : "龙里",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101100908",
			"name" : "罗甸",
			"type" : 3,
			"idx" : 8,
			"children" : []
		}, {
			"id" : "c101100907",
			"name" : "平塘",
			"type" : 3,
			"idx" : 9,
			"children" : []
		}, {
			"id" : "c101100912",
			"name" : "三都",
			"type" : 3,
			"idx" : 10,
			"children" : []
		}, {
			"id" : "c101100905",
			"name" : "瓮安",
			"type" : 3,
			"idx" : 11,
			"children" : []
		} ]
	}, {
		"id" : "c101100700",
		"name" : "黔西南",
		"type" : 2,
		"idx" : 6,
		"children" : [ {
			"id" : "c101100708",
			"name" : "安龙",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101100707",
			"name" : "册亨",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101100703",
			"name" : "普安",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101100704",
			"name" : "晴隆",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101100706",
			"name" : "望谟",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101100702",
			"name" : "兴仁",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101100701",
			"name" : "兴义",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101100705",
			"name" : "贞丰",
			"type" : 3,
			"idx" : 7,
			"children" : []
		} ]
	}, {
		"id" : "c101100600",
		"name" : "铜仁",
		"type" : 2,
		"idx" : 7,
		"children" : [ {
			"id" : "c101100607",
			"name" : "德江",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101100602",
			"name" : "江口",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101100604",
			"name" : "石阡",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101100601",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101100605",
			"name" : "思南",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101100609",
			"name" : "松桃",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101100608",
			"name" : "沿河",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101100606",
			"name" : "印江",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101100603",
			"name" : "玉屏",
			"type" : 3,
			"idx" : 8,
			"children" : []
		} ]
	}, {
		"id" : "c101100300",
		"name" : "遵义",
		"type" : 2,
		"idx" : 8,
		"children" : [ {
			"id" : "c101100312",
			"name" : "赤水",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101100306",
			"name" : "道真",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101100308",
			"name" : "凤冈",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101100309",
			"name" : "湄潭",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101100313",
			"name" : "仁怀",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101100301",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101100304",
			"name" : "绥阳",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101100303",
			"name" : "桐梓",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101100307",
			"name" : "务川",
			"type" : 3,
			"idx" : 8,
			"children" : []
		}, {
			"id" : "c101100311",
			"name" : "习水",
			"type" : 3,
			"idx" : 9,
			"children" : []
		}, {
			"id" : "c101100310",
			"name" : "余庆",
			"type" : 3,
			"idx" : 10,
			"children" : []
		}, {
			"id" : "c101100305",
			"name" : "正安",
			"type" : 3,
			"idx" : 11,
			"children" : []
		}, {
			"id" : "c101100302",
			"name" : "遵义县",
			"type" : 3,
			"idx" : 12,
			"children" : []
		} ]
	} ]
}, {
	"id" : "c101110000",
	"name" : "海南",
	"type" : 1,
	"idx" : 12,
	"city" : [ {
		"id" : "c101110100",
		"name" : "海口",
		"type" : 2,
		"idx" : 0,
		"children" : [ {
			"id" : "c101110102",
			"name" : "龙华区",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101110104",
			"name" : "美兰区",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101110103",
			"name" : "琼山区",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101110101",
			"name" : "秀英区",
			"type" : 3,
			"idx" : 3,
			"children" : []
		} ]
	}, {
		"id" : "c101110400",
		"name" : "三沙",
		"type" : 2,
		"idx" : 1,
		"children" : []
	}, {
		"id" : "c101110200",
		"name" : "三亚",
		"type" : 2,
		"idx" : 2,
		"children" : [ {
			"id" : "c101110201",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 0,
			"children" : []
		} ]
	}, {
		"id" : "c101110300",
		"name" : "省直辖县级",
		"type" : 2,
		"idx" : 3,
		"children" : [ {
			"id" : "c101110311",
			"name" : "白沙",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101110315",
			"name" : "保亭",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101110312",
			"name" : "昌江",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101110309",
			"name" : "澄迈",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101110303",
			"name" : "儋州",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101110307",
			"name" : "定安",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101110306",
			"name" : "东方",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101110313",
			"name" : "乐东",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101110310",
			"name" : "临高",
			"type" : 3,
			"idx" : 8,
			"children" : []
		}, {
			"id" : "c101110314",
			"name" : "陵水",
			"type" : 3,
			"idx" : 9,
			"children" : []
		}, {
			"id" : "c101110318",
			"name" : "南沙群岛",
			"type" : 3,
			"idx" : 10,
			"children" : []
		}, {
			"id" : "c101110302",
			"name" : "琼海",
			"type" : 3,
			"idx" : 11,
			"children" : []
		}, {
			"id" : "c101110316",
			"name" : "琼中",
			"type" : 3,
			"idx" : 12,
			"children" : []
		}, {
			"id" : "c101110308",
			"name" : "屯昌",
			"type" : 3,
			"idx" : 13,
			"children" : []
		}, {
			"id" : "c101110305",
			"name" : "万宁",
			"type" : 3,
			"idx" : 14,
			"children" : []
		}, {
			"id" : "c101110304",
			"name" : "文昌",
			"type" : 3,
			"idx" : 15,
			"children" : []
		}, {
			"id" : "c101110301",
			"name" : "五指山",
			"type" : 3,
			"idx" : 16,
			"children" : []
		}, {
			"id" : "c101110317",
			"name" : "西沙群岛",
			"type" : 3,
			"idx" : 17,
			"children" : []
		}, {
			"id" : "c101110319",
			"name" : "中沙群岛的岛礁及其海域",
			"type" : 3,
			"idx" : 18,
			"children" : []
		} ]
	} ]
}, {
	"id" : "c101120000",
	"name" : "河北",
	"type" : 1,
	"idx" : 13,
	"city" : [ {
		"id" : "c101120100",
		"name" : "石家庄",
		"type" : 2,
		"idx" : 0,
		"children" : [ {
			"id" : "c101120101",
			"name" : "长安区",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101120112",
			"name" : "高邑",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101120120",
			"name" : "藁城",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101120121",
			"name" : "晋州",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101120107",
			"name" : "井陉",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101120105",
			"name" : "井陉矿区",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101120111",
			"name" : "灵寿",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101120123",
			"name" : "鹿泉",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101120109",
			"name" : "栾城",
			"type" : 3,
			"idx" : 8,
			"children" : []
		}, {
			"id" : "c101120116",
			"name" : "平山",
			"type" : 3,
			"idx" : 9,
			"children" : []
		}, {
			"id" : "c101120102",
			"name" : "桥东区",
			"type" : 3,
			"idx" : 10,
			"children" : []
		}, {
			"id" : "c101120103",
			"name" : "桥西区",
			"type" : 3,
			"idx" : 11,
			"children" : []
		}, {
			"id" : "c101120113",
			"name" : "深泽",
			"type" : 3,
			"idx" : 12,
			"children" : []
		}, {
			"id" : "c101120115",
			"name" : "无极",
			"type" : 3,
			"idx" : 13,
			"children" : []
		}, {
			"id" : "c101120119",
			"name" : "辛集",
			"type" : 3,
			"idx" : 14,
			"children" : []
		}, {
			"id" : "c101120104",
			"name" : "新华区",
			"type" : 3,
			"idx" : 15,
			"children" : []
		}, {
			"id" : "c101120122",
			"name" : "新乐",
			"type" : 3,
			"idx" : 16,
			"children" : []
		}, {
			"id" : "c101120110",
			"name" : "行唐",
			"type" : 3,
			"idx" : 17,
			"children" : []
		}, {
			"id" : "c101120106",
			"name" : "裕华区",
			"type" : 3,
			"idx" : 18,
			"children" : []
		}, {
			"id" : "c101120117",
			"name" : "元氏",
			"type" : 3,
			"idx" : 19,
			"children" : []
		}, {
			"id" : "c101120114",
			"name" : "赞皇",
			"type" : 3,
			"idx" : 20,
			"children" : []
		}, {
			"id" : "c101120118",
			"name" : "赵县",
			"type" : 3,
			"idx" : 21,
			"children" : []
		}, {
			"id" : "c101120108",
			"name" : "正定",
			"type" : 3,
			"idx" : 22,
			"children" : []
		} ]
	}, {
		"id" : "c101120200",
		"name" : "唐山",
		"type" : 2,
		"idx" : 1,
		"children" : [ {
			"id" : "c101120204",
			"name" : "乐亭",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101120203",
			"name" : "滦南",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101120202",
			"name" : "滦县",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101120209",
			"name" : "迁安",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101120205",
			"name" : "迁西",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101120201",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101120207",
			"name" : "唐海",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101120206",
			"name" : "玉田",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101120208",
			"name" : "遵化",
			"type" : 3,
			"idx" : 8,
			"children" : []
		} ]
	}, {
		"id" : "c101120600",
		"name" : "保定",
		"type" : 2,
		"idx" : 2,
		"children" : [ {
			"id" : "c101120622",
			"name" : "安国",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101120613",
			"name" : "安新",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101120618",
			"name" : "博野",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101120607",
			"name" : "定兴",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101120621",
			"name" : "定州",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101120605",
			"name" : "阜平",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101120623",
			"name" : "高碑店",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101120609",
			"name" : "高阳",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101120604",
			"name" : "涞水",
			"type" : 3,
			"idx" : 8,
			"children" : []
		}, {
			"id" : "c101120611",
			"name" : "涞源",
			"type" : 3,
			"idx" : 9,
			"children" : []
		}, {
			"id" : "c101120616",
			"name" : "蠡县",
			"type" : 3,
			"idx" : 10,
			"children" : []
		}, {
			"id" : "c101120602",
			"name" : "满城",
			"type" : 3,
			"idx" : 11,
			"children" : []
		}, {
			"id" : "c101120603",
			"name" : "清苑",
			"type" : 3,
			"idx" : 12,
			"children" : []
		}, {
			"id" : "c101120615",
			"name" : "曲阳",
			"type" : 3,
			"idx" : 13,
			"children" : []
		}, {
			"id" : "c101120610",
			"name" : "容城",
			"type" : 3,
			"idx" : 14,
			"children" : []
		}, {
			"id" : "c101120601",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 15,
			"children" : []
		}, {
			"id" : "c101120617",
			"name" : "顺平",
			"type" : 3,
			"idx" : 16,
			"children" : []
		}, {
			"id" : "c101120608",
			"name" : "唐县",
			"type" : 3,
			"idx" : 17,
			"children" : []
		}, {
			"id" : "c101120612",
			"name" : "望都",
			"type" : 3,
			"idx" : 18,
			"children" : []
		}, {
			"id" : "c101120619",
			"name" : "雄县",
			"type" : 3,
			"idx" : 19,
			"children" : []
		}, {
			"id" : "c101120606",
			"name" : "徐水",
			"type" : 3,
			"idx" : 20,
			"children" : []
		}, {
			"id" : "c101120614",
			"name" : "易县",
			"type" : 3,
			"idx" : 21,
			"children" : []
		}, {
			"id" : "c101120620",
			"name" : "涿州",
			"type" : 3,
			"idx" : 22,
			"children" : []
		} ]
	}, {
		"id" : "c101120900",
		"name" : "沧州",
		"type" : 2,
		"idx" : 3,
		"children" : [ {
			"id" : "c101120912",
			"name" : "泊头",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101120902",
			"name" : "沧县",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101120904",
			"name" : "东光",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101120905",
			"name" : "海兴",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101120915",
			"name" : "河间",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101120914",
			"name" : "黄骅",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101120911",
			"name" : "孟村",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101120908",
			"name" : "南皮",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101120903",
			"name" : "青县",
			"type" : 3,
			"idx" : 8,
			"children" : []
		}, {
			"id" : "c101120913",
			"name" : "任丘",
			"type" : 3,
			"idx" : 9,
			"children" : []
		}, {
			"id" : "c101120901",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 10,
			"children" : []
		}, {
			"id" : "c101120907",
			"name" : "肃宁",
			"type" : 3,
			"idx" : 11,
			"children" : []
		}, {
			"id" : "c101120909",
			"name" : "吴桥",
			"type" : 3,
			"idx" : 12,
			"children" : []
		}, {
			"id" : "c101120910",
			"name" : "献县",
			"type" : 3,
			"idx" : 13,
			"children" : []
		}, {
			"id" : "c101120906",
			"name" : "盐山",
			"type" : 3,
			"idx" : 14,
			"children" : []
		} ]
	}, {
		"id" : "c101120800",
		"name" : "承德",
		"type" : 2,
		"idx" : 4,
		"children" : [ {
			"id" : "c101120802",
			"name" : "承德县",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101120807",
			"name" : "丰宁",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101120808",
			"name" : "宽城",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101120806",
			"name" : "隆化",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101120805",
			"name" : "滦平",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101120804",
			"name" : "平泉",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101120801",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101120809",
			"name" : "围场",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101120803",
			"name" : "兴隆",
			"type" : 3,
			"idx" : 8,
			"children" : []
		} ]
	}, {
		"id" : "c101120400",
		"name" : "邯郸",
		"type" : 2,
		"idx" : 5,
		"children" : [ {
			"id" : "c101120404",
			"name" : "成安",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101120407",
			"name" : "磁县",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101120405",
			"name" : "大名",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101120408",
			"name" : "肥乡",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101120413",
			"name" : "馆陶",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101120412",
			"name" : "广平",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101120402",
			"name" : "邯郸县",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101120411",
			"name" : "鸡泽",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101120403",
			"name" : "临漳",
			"type" : 3,
			"idx" : 8,
			"children" : []
		}, {
			"id" : "c101120410",
			"name" : "邱县",
			"type" : 3,
			"idx" : 9,
			"children" : []
		}, {
			"id" : "c101120415",
			"name" : "曲周",
			"type" : 3,
			"idx" : 10,
			"children" : []
		}, {
			"id" : "c101120406",
			"name" : "涉县",
			"type" : 3,
			"idx" : 11,
			"children" : []
		}, {
			"id" : "c101120401",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 12,
			"children" : []
		}, {
			"id" : "c101120414",
			"name" : "魏县",
			"type" : 3,
			"idx" : 13,
			"children" : []
		}, {
			"id" : "c101120416",
			"name" : "武安",
			"type" : 3,
			"idx" : 14,
			"children" : []
		}, {
			"id" : "c101120409",
			"name" : "永年",
			"type" : 3,
			"idx" : 15,
			"children" : []
		} ]
	}, {
		"id" : "c101121100",
		"name" : "衡水",
		"type" : 2,
		"idx" : 6,
		"children" : [ {
			"id" : "c101121106",
			"name" : "安平",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101121109",
			"name" : "阜城",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101121107",
			"name" : "故城",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101121110",
			"name" : "冀州",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101121108",
			"name" : "景县",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101121105",
			"name" : "饶阳",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101121111",
			"name" : "深州",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101121101",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101121104",
			"name" : "武强",
			"type" : 3,
			"idx" : 8,
			"children" : []
		}, {
			"id" : "c101121103",
			"name" : "武邑",
			"type" : 3,
			"idx" : 9,
			"children" : []
		} ]
	}, {
		"id" : "c101121000",
		"name" : "廊坊",
		"type" : 2,
		"idx" : 7,
		"children" : [ {
			"id" : "c101121008",
			"name" : "霸州",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101121007",
			"name" : "大厂",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101121005",
			"name" : "大城",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101121002",
			"name" : "固安",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101121009",
			"name" : "三河",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101121001",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101121006",
			"name" : "文安",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101121004",
			"name" : "香河",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101121003",
			"name" : "永清",
			"type" : 3,
			"idx" : 8,
			"children" : []
		} ]
	}, {
		"id" : "c101120300",
		"name" : "秦皇岛",
		"type" : 2,
		"idx" : 8,
		"children" : [ {
			"id" : "c101120303",
			"name" : "昌黎",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101120304",
			"name" : "抚宁",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101120305",
			"name" : "卢龙",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101120302",
			"name" : "青龙",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101120301",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 4,
			"children" : []
		} ]
	}, {
		"id" : "c101120500",
		"name" : "邢台",
		"type" : 2,
		"idx" : 9,
		"children" : [ {
			"id" : "c101120505",
			"name" : "柏乡",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101120512",
			"name" : "广宗",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101120510",
			"name" : "巨鹿",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101120503",
			"name" : "临城",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101120516",
			"name" : "临西",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101120506",
			"name" : "隆尧",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101120504",
			"name" : "内丘",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101120517",
			"name" : "南宫",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101120508",
			"name" : "南和",
			"type" : 3,
			"idx" : 8,
			"children" : []
		}, {
			"id" : "c101120509",
			"name" : "宁晋",
			"type" : 3,
			"idx" : 9,
			"children" : []
		}, {
			"id" : "c101120513",
			"name" : "平乡",
			"type" : 3,
			"idx" : 10,
			"children" : []
		}, {
			"id" : "c101120515",
			"name" : "清河",
			"type" : 3,
			"idx" : 11,
			"children" : []
		}, {
			"id" : "c101120507",
			"name" : "任县",
			"type" : 3,
			"idx" : 12,
			"children" : []
		}, {
			"id" : "c101120518",
			"name" : "沙河",
			"type" : 3,
			"idx" : 13,
			"children" : []
		}, {
			"id" : "c101120501",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 14,
			"children" : []
		}, {
			"id" : "c101120514",
			"name" : "威县",
			"type" : 3,
			"idx" : 15,
			"children" : []
		}, {
			"id" : "c101120511",
			"name" : "新河",
			"type" : 3,
			"idx" : 16,
			"children" : []
		}, {
			"id" : "c101120502",
			"name" : "邢台县",
			"type" : 3,
			"idx" : 17,
			"children" : []
		} ]
	}, {
		"id" : "c101120700",
		"name" : "张家口",
		"type" : 2,
		"idx" : 10,
		"children" : [ {
			"id" : "c101120713",
			"name" : "赤城",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101120714",
			"name" : "崇礼",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101120705",
			"name" : "沽源",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101120709",
			"name" : "怀安",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101120711",
			"name" : "怀来",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101120704",
			"name" : "康保",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101120706",
			"name" : "尚义",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101120701",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101120710",
			"name" : "万全",
			"type" : 3,
			"idx" : 8,
			"children" : []
		}, {
			"id" : "c101120707",
			"name" : "蔚县",
			"type" : 3,
			"idx" : 9,
			"children" : []
		}, {
			"id" : "c101120702",
			"name" : "宣化",
			"type" : 3,
			"idx" : 10,
			"children" : []
		}, {
			"id" : "c101120708",
			"name" : "阳原",
			"type" : 3,
			"idx" : 11,
			"children" : []
		}, {
			"id" : "c101120703",
			"name" : "张北",
			"type" : 3,
			"idx" : 12,
			"children" : []
		}, {
			"id" : "c101120712",
			"name" : "涿鹿",
			"type" : 3,
			"idx" : 13,
			"children" : []
		} ]
	} ]
}, {
	"id" : "c101130000",
	"name" : "河南",
	"type" : 1,
	"idx" : 14,
	"city" : [ {
		"id" : "c101130100",
		"name" : "郑州",
		"type" : 2,
		"idx" : 0,
		"children" : [ {
			"id" : "c101130112",
			"name" : "登封",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101130102",
			"name" : "二七区",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101130108",
			"name" : "巩义",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101130103",
			"name" : "管城回族区",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101130106",
			"name" : "惠济区",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101130104",
			"name" : "金水区",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101130105",
			"name" : "上街区",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101130110",
			"name" : "新密",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101130111",
			"name" : "新郑",
			"type" : 3,
			"idx" : 8,
			"children" : []
		}, {
			"id" : "c101130109",
			"name" : "荥阳",
			"type" : 3,
			"idx" : 9,
			"children" : []
		}, {
			"id" : "c101130107",
			"name" : "中牟",
			"type" : 3,
			"idx" : 10,
			"children" : []
		}, {
			"id" : "c101130101",
			"name" : "中原区",
			"type" : 3,
			"idx" : 11,
			"children" : []
		} ]
	}, {
		"id" : "c101130500",
		"name" : "安阳",
		"type" : 2,
		"idx" : 1,
		"children" : [ {
			"id" : "c101130502",
			"name" : "安阳县",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101130504",
			"name" : "滑县",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101130506",
			"name" : "林州",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101130505",
			"name" : "内黄",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101130501",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101130503",
			"name" : "汤阴",
			"type" : 3,
			"idx" : 5,
			"children" : []
		} ]
	}, {
		"id" : "c101130600",
		"name" : "鹤壁",
		"type" : 2,
		"idx" : 2,
		"children" : [ {
			"id" : "c101130602",
			"name" : "浚县",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101130603",
			"name" : "淇县",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101130601",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 2,
			"children" : []
		} ]
	}, {
		"id" : "c101130800",
		"name" : "焦作",
		"type" : 2,
		"idx" : 3,
		"children" : [ {
			"id" : "c101130803",
			"name" : "博爱",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101130807",
			"name" : "孟州",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101130806",
			"name" : "沁阳",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101130801",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101130805",
			"name" : "温县",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101130804",
			"name" : "武陟",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101130802",
			"name" : "修武",
			"type" : 3,
			"idx" : 6,
			"children" : []
		} ]
	}, {
		"id" : "c101130200",
		"name" : "开封",
		"type" : 2,
		"idx" : 4,
		"children" : [ {
			"id" : "c101130205",
			"name" : "开封",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101130206",
			"name" : "兰考",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101130202",
			"name" : "杞县",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101130201",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101130203",
			"name" : "通许",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101130204",
			"name" : "尉氏",
			"type" : 3,
			"idx" : 5,
			"children" : []
		} ]
	}, {
		"id" : "c101130300",
		"name" : "洛阳",
		"type" : 2,
		"idx" : 5,
		"children" : [ {
			"id" : "c101130304",
			"name" : "栾川",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101130308",
			"name" : "洛宁",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101130302",
			"name" : "孟津",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101130306",
			"name" : "汝阳",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101130301",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101130305",
			"name" : "嵩县",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101130303",
			"name" : "新安",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101130310",
			"name" : "偃师",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101130309",
			"name" : "伊川",
			"type" : 3,
			"idx" : 8,
			"children" : []
		}, {
			"id" : "c101130307",
			"name" : "宜阳",
			"type" : 3,
			"idx" : 9,
			"children" : []
		} ]
	}, {
		"id" : "c101131100",
		"name" : "漯河",
		"type" : 2,
		"idx" : 6,
		"children" : [ {
			"id" : "c101131103",
			"name" : "临颍",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101131101",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101131102",
			"name" : "舞阳",
			"type" : 3,
			"idx" : 2,
			"children" : []
		} ]
	}, {
		"id" : "c101131300",
		"name" : "南阳",
		"type" : 2,
		"idx" : 7,
		"children" : [ {
			"id" : "c101131312",
			"name" : "邓州",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101131303",
			"name" : "方城",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101131306",
			"name" : "内乡",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101131302",
			"name" : "南召",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101131308",
			"name" : "社旗",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101131301",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101131309",
			"name" : "唐河",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101131311",
			"name" : "桐柏",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101131304",
			"name" : "西峡",
			"type" : 3,
			"idx" : 8,
			"children" : []
		}, {
			"id" : "c101131307",
			"name" : "淅川",
			"type" : 3,
			"idx" : 9,
			"children" : []
		}, {
			"id" : "c101131310",
			"name" : "新野",
			"type" : 3,
			"idx" : 10,
			"children" : []
		}, {
			"id" : "c101131305",
			"name" : "镇平",
			"type" : 3,
			"idx" : 11,
			"children" : []
		} ]
	}, {
		"id" : "c101130400",
		"name" : "平顶山",
		"type" : 2,
		"idx" : 8,
		"children" : [ {
			"id" : "c101130402",
			"name" : "宝丰",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101130405",
			"name" : "郏县",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101130404",
			"name" : "鲁山",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101130407",
			"name" : "汝州",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101130401",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101130406",
			"name" : "舞钢",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101130403",
			"name" : "叶县",
			"type" : 3,
			"idx" : 6,
			"children" : []
		} ]
	}, {
		"id" : "c101130900",
		"name" : "濮阳",
		"type" : 2,
		"idx" : 9,
		"children" : [ {
			"id" : "c101130904",
			"name" : "范县",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101130903",
			"name" : "南乐",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101130906",
			"name" : "濮阳",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101130902",
			"name" : "清丰",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101130901",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101130905",
			"name" : "台前",
			"type" : 3,
			"idx" : 5,
			"children" : []
		} ]
	}, {
		"id" : "c101131200",
		"name" : "三门峡",
		"type" : 2,
		"idx" : 10,
		"children" : [ {
			"id" : "c101131206",
			"name" : "灵宝",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101131204",
			"name" : "卢氏",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101131203",
			"name" : "陕县",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101131202",
			"name" : "渑池",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101131201",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101131205",
			"name" : "义马",
			"type" : 3,
			"idx" : 5,
			"children" : []
		} ]
	}, {
		"id" : "c101131400",
		"name" : "商丘",
		"type" : 2,
		"idx" : 11,
		"children" : [ {
			"id" : "c101131402",
			"name" : "民权",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101131404",
			"name" : "宁陵",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101131401",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101131403",
			"name" : "睢县",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101131407",
			"name" : "夏邑",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101131408",
			"name" : "永城",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101131406",
			"name" : "虞城",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101131405",
			"name" : "柘城",
			"type" : 3,
			"idx" : 7,
			"children" : []
		} ]
	}, {
		"id" : "c101131800",
		"name" : "省直辖县级",
		"type" : 2,
		"idx" : 12,
		"children" : [ {
			"id" : "c101131801",
			"name" : "济源",
			"type" : 3,
			"idx" : 0,
			"children" : []
		} ]
	}, {
		"id" : "c101130700",
		"name" : "新乡",
		"type" : 2,
		"idx" : 13,
		"children" : [ {
			"id" : "c101130707",
			"name" : "长垣",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101130706",
			"name" : "封丘",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101130709",
			"name" : "辉县",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101130703",
			"name" : "获嘉",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101130701",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101130708",
			"name" : "卫辉",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101130702",
			"name" : "新乡县",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101130705",
			"name" : "延津",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101130704",
			"name" : "原阳",
			"type" : 3,
			"idx" : 8,
			"children" : []
		} ]
	}, {
		"id" : "c101131500",
		"name" : "信阳",
		"type" : 2,
		"idx" : 14,
		"children" : [ {
			"id" : "c101131506",
			"name" : "固始",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101131503",
			"name" : "光山",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101131508",
			"name" : "淮滨",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101131507",
			"name" : "潢川",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101131502",
			"name" : "罗山",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101131505",
			"name" : "商城",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101131501",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101131509",
			"name" : "息县",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101131504",
			"name" : "新县",
			"type" : 3,
			"idx" : 8,
			"children" : []
		} ]
	}, {
		"id" : "c101131000",
		"name" : "许昌",
		"type" : 2,
		"idx" : 15,
		"children" : [ {
			"id" : "c101131006",
			"name" : "长葛",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101131001",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101131004",
			"name" : "襄城",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101131002",
			"name" : "许昌县",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101131003",
			"name" : "鄢陵",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101131005",
			"name" : "禹州",
			"type" : 3,
			"idx" : 5,
			"children" : []
		} ]
	}, {
		"id" : "c101131600",
		"name" : "周口",
		"type" : 2,
		"idx" : 16,
		"children" : [ {
			"id" : "c101131606",
			"name" : "郸城",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101131602",
			"name" : "扶沟",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101131607",
			"name" : "淮阳",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101131609",
			"name" : "鹿邑",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101131604",
			"name" : "商水",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101131605",
			"name" : "沈丘",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101131601",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101131608",
			"name" : "太康",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101131603",
			"name" : "西华",
			"type" : 3,
			"idx" : 8,
			"children" : []
		}, {
			"id" : "c101131610",
			"name" : "项城",
			"type" : 3,
			"idx" : 9,
			"children" : []
		} ]
	}, {
		"id" : "c101131700",
		"name" : "驻马店",
		"type" : 2,
		"idx" : 17,
		"children" : [ {
			"id" : "c101131707",
			"name" : "泌阳",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101131704",
			"name" : "平舆",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101131706",
			"name" : "确山",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101131708",
			"name" : "汝南",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101131703",
			"name" : "上蔡",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101131701",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101131709",
			"name" : "遂平",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101131702",
			"name" : "西平",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101131710",
			"name" : "新蔡",
			"type" : 3,
			"idx" : 8,
			"children" : []
		}, {
			"id" : "c101131705",
			"name" : "正阳",
			"type" : 3,
			"idx" : 9,
			"children" : []
		} ]
	} ]
}, {
	"id" : "c101140000",
	"name" : "黑龙江",
	"type" : 1,
	"idx" : 15,
	"city" : [ {
		"id" : "c101140100",
		"name" : "哈尔滨",
		"type" : 2,
		"idx" : 0,
		"children" : [ {
			"id" : "c101140108",
			"name" : "阿城区",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101140112",
			"name" : "巴彦",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101140111",
			"name" : "宾县",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101140101",
			"name" : "道里区",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101140103",
			"name" : "道外区",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101140110",
			"name" : "方正",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101140107",
			"name" : "呼兰区",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101140113",
			"name" : "木兰",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101140102",
			"name" : "南岗区",
			"type" : 3,
			"idx" : 8,
			"children" : []
		}, {
			"id" : "c101140104",
			"name" : "平房区",
			"type" : 3,
			"idx" : 9,
			"children" : []
		}, {
			"id" : "c101140117",
			"name" : "尚志",
			"type" : 3,
			"idx" : 10,
			"children" : []
		}, {
			"id" : "c101140116",
			"name" : "双城",
			"type" : 3,
			"idx" : 11,
			"children" : []
		}, {
			"id" : "c101140105",
			"name" : "松北区",
			"type" : 3,
			"idx" : 12,
			"children" : []
		}, {
			"id" : "c101140114",
			"name" : "通河",
			"type" : 3,
			"idx" : 13,
			"children" : []
		}, {
			"id" : "c101140118",
			"name" : "五常",
			"type" : 3,
			"idx" : 14,
			"children" : []
		}, {
			"id" : "c101140106",
			"name" : "香坊区",
			"type" : 3,
			"idx" : 15,
			"children" : []
		}, {
			"id" : "c101140115",
			"name" : "延寿",
			"type" : 3,
			"idx" : 16,
			"children" : []
		}, {
			"id" : "c101140109",
			"name" : "依兰",
			"type" : 3,
			"idx" : 17,
			"children" : []
		} ]
	}, {
		"id" : "c101140600",
		"name" : "大庆",
		"type" : 2,
		"idx" : 1,
		"children" : [ {
			"id" : "c101140605",
			"name" : "杜尔伯特",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101140604",
			"name" : "林甸",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101140601",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101140603",
			"name" : "肇源",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101140602",
			"name" : "肇州",
			"type" : 3,
			"idx" : 4,
			"children" : []
		} ]
	}, {
		"id" : "c101141300",
		"name" : "大兴安岭",
		"type" : 2,
		"idx" : 2,
		"children" : [ {
			"id" : "c101141301",
			"name" : "呼玛",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101141303",
			"name" : "漠河",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101141302",
			"name" : "塔河",
			"type" : 3,
			"idx" : 2,
			"children" : []
		} ]
	}, {
		"id" : "c101140400",
		"name" : "鹤岗",
		"type" : 2,
		"idx" : 3,
		"children" : [ {
			"id" : "c101140402",
			"name" : "萝北",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101140401",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101140403",
			"name" : "绥滨",
			"type" : 3,
			"idx" : 2,
			"children" : []
		} ]
	}, {
		"id" : "c101141100",
		"name" : "黑河",
		"type" : 2,
		"idx" : 4,
		"children" : [ {
			"id" : "c101141105",
			"name" : "北安",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101141102",
			"name" : "嫩江",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101141101",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101141104",
			"name" : "孙吴",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101141106",
			"name" : "五大连池",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101141103",
			"name" : "逊克",
			"type" : 3,
			"idx" : 5,
			"children" : []
		} ]
	}, {
		"id" : "c101140300",
		"name" : "鸡西",
		"type" : 2,
		"idx" : 5,
		"children" : [ {
			"id" : "c101140303",
			"name" : "虎林",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101140302",
			"name" : "鸡东",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101140304",
			"name" : "密山",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101140301",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 3,
			"children" : []
		} ]
	}, {
		"id" : "c101140800",
		"name" : "佳木斯",
		"type" : 2,
		"idx" : 6,
		"children" : [ {
			"id" : "c101140805",
			"name" : "抚远",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101140807",
			"name" : "富锦",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101140803",
			"name" : "桦川",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101140802",
			"name" : "桦南",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101140801",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101140804",
			"name" : "汤原",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101140806",
			"name" : "同江",
			"type" : 3,
			"idx" : 6,
			"children" : []
		} ]
	}, {
		"id" : "c101141000",
		"name" : "牡丹江",
		"type" : 2,
		"idx" : 7,
		"children" : [ {
			"id" : "c101141002",
			"name" : "东宁",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101141005",
			"name" : "海林",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101141003",
			"name" : "林口",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101141007",
			"name" : "穆棱",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101141006",
			"name" : "宁安",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101141001",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101141004",
			"name" : "绥芬河",
			"type" : 3,
			"idx" : 6,
			"children" : []
		} ]
	}, {
		"id" : "c101140900",
		"name" : "七台河",
		"type" : 2,
		"idx" : 8,
		"children" : [ {
			"id" : "c101140902",
			"name" : "勃利",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101140901",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 1,
			"children" : []
		} ]
	}, {
		"id" : "c101140200",
		"name" : "齐齐哈尔",
		"type" : 2,
		"idx" : 9,
		"children" : [ {
			"id" : "c101140209",
			"name" : "拜泉",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101140206",
			"name" : "富裕",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101140205",
			"name" : "甘南",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101140208",
			"name" : "克东",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101140207",
			"name" : "克山",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101140202",
			"name" : "龙江",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101140210",
			"name" : "讷河",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101140201",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101140204",
			"name" : "泰来",
			"type" : 3,
			"idx" : 8,
			"children" : []
		}, {
			"id" : "c101140203",
			"name" : "依安",
			"type" : 3,
			"idx" : 9,
			"children" : []
		} ]
	}, {
		"id" : "c101140500",
		"name" : "双鸭山",
		"type" : 2,
		"idx" : 10,
		"children" : [ {
			"id" : "c101140504",
			"name" : "宝清",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101140502",
			"name" : "集贤",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101140505",
			"name" : "饶河",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101140501",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101140503",
			"name" : "友谊",
			"type" : 3,
			"idx" : 4,
			"children" : []
		} ]
	}, {
		"id" : "c101141200",
		"name" : "绥化",
		"type" : 2,
		"idx" : 11,
		"children" : [ {
			"id" : "c101141208",
			"name" : "安达",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101141210",
			"name" : "海伦",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101141203",
			"name" : "兰西",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101141206",
			"name" : "明水",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101141204",
			"name" : "青冈",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101141205",
			"name" : "庆安",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101141201",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101141207",
			"name" : "绥棱",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101141202",
			"name" : "望奎",
			"type" : 3,
			"idx" : 8,
			"children" : []
		}, {
			"id" : "c101141209",
			"name" : "肇东",
			"type" : 3,
			"idx" : 9,
			"children" : []
		} ]
	}, {
		"id" : "c101140700",
		"name" : "伊春",
		"type" : 2,
		"idx" : 12,
		"children" : [ {
			"id" : "c101140702",
			"name" : "嘉荫",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101140701",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101140703",
			"name" : "铁力",
			"type" : 3,
			"idx" : 2,
			"children" : []
		} ]
	} ]
}, {
	"id" : "c101150000",
	"name" : "湖北",
	"type" : 1,
	"idx" : 16,
	"city" : [ {
		"id" : "c101150100",
		"name" : "武汉",
		"type" : 2,
		"idx" : 0,
		"children" : [ {
			"id" : "c101150110",
			"name" : "蔡甸区",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101150108",
			"name" : "东西湖区",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101150109",
			"name" : "汉南区",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101150104",
			"name" : "汉阳区",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101150107",
			"name" : "洪山区",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101150112",
			"name" : "黄陂区",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101150101",
			"name" : "江岸区",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101150102",
			"name" : "江汉区",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101150111",
			"name" : "江夏区",
			"type" : 3,
			"idx" : 8,
			"children" : []
		}, {
			"id" : "c101150103",
			"name" : "硚口区",
			"type" : 3,
			"idx" : 9,
			"children" : []
		}, {
			"id" : "c101150106",
			"name" : "青山区",
			"type" : 3,
			"idx" : 10,
			"children" : []
		}, {
			"id" : "c101150105",
			"name" : "武昌区",
			"type" : 3,
			"idx" : 11,
			"children" : []
		}, {
			"id" : "c101150113",
			"name" : "新洲区",
			"type" : 3,
			"idx" : 12,
			"children" : []
		} ]
	}, {
		"id" : "c101150600",
		"name" : "鄂州",
		"type" : 2,
		"idx" : 1,
		"children" : [ {
			"id" : "c101150601",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 0,
			"children" : []
		} ]
	}, {
		"id" : "c101151300",
		"name" : "恩施",
		"type" : 2,
		"idx" : 2,
		"children" : [ {
			"id" : "c101151304",
			"name" : "巴东",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101151301",
			"name" : "恩施市",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101151308",
			"name" : "鹤峰",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101151303",
			"name" : "建始",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101151307",
			"name" : "来凤",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101151302",
			"name" : "利川",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101151306",
			"name" : "咸丰",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101151305",
			"name" : "宣恩",
			"type" : 3,
			"idx" : 7,
			"children" : []
		} ]
	}, {
		"id" : "c101151000",
		"name" : "黄冈",
		"type" : 2,
		"idx" : 3,
		"children" : [ {
			"id" : "c101151003",
			"name" : "红安",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101151008",
			"name" : "黄梅",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101151004",
			"name" : "罗田",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101151009",
			"name" : "麻城",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101151007",
			"name" : "蕲春",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101151001",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101151002",
			"name" : "团风",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101151010",
			"name" : "武穴",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101151006",
			"name" : "浠水",
			"type" : 3,
			"idx" : 8,
			"children" : []
		}, {
			"id" : "c101151005",
			"name" : "英山",
			"type" : 3,
			"idx" : 9,
			"children" : []
		} ]
	}, {
		"id" : "c101150200",
		"name" : "黄石",
		"type" : 2,
		"idx" : 4,
		"children" : [ {
			"id" : "c101150203",
			"name" : "大冶",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101150201",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101150202",
			"name" : "阳新",
			"type" : 3,
			"idx" : 2,
			"children" : []
		} ]
	}, {
		"id" : "c101150700",
		"name" : "荆门",
		"type" : 2,
		"idx" : 5,
		"children" : [ {
			"id" : "c101150702",
			"name" : "京山",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101150703",
			"name" : "沙洋",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101150701",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101150704",
			"name" : "钟祥",
			"type" : 3,
			"idx" : 3,
			"children" : []
		} ]
	}, {
		"id" : "c101150900",
		"name" : "荆州",
		"type" : 2,
		"idx" : 6,
		"children" : [ {
			"id" : "c101150902",
			"name" : "公安",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101150906",
			"name" : "洪湖",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101150903",
			"name" : "监利",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101150904",
			"name" : "江陵",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101150905",
			"name" : "石首",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101150901",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101150907",
			"name" : "松滋",
			"type" : 3,
			"idx" : 6,
			"children" : []
		} ]
	}, {
		"id" : "c101151400",
		"name" : "省直辖县级",
		"type" : 2,
		"idx" : 7,
		"children" : [ {
			"id" : "c101151402",
			"name" : "潜江",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101151404",
			"name" : "神农架",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101151403",
			"name" : "天门",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101151401",
			"name" : "仙桃",
			"type" : 3,
			"idx" : 3,
			"children" : []
		} ]
	}, {
		"id" : "c101150300",
		"name" : "十堰",
		"type" : 2,
		"idx" : 8,
		"children" : [ {
			"id" : "c101150307",
			"name" : "丹江口",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101150306",
			"name" : "房县",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101150301",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101150303",
			"name" : "郧西",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101150302",
			"name" : "郧县",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101150304",
			"name" : "竹山",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101150305",
			"name" : "竹溪",
			"type" : 3,
			"idx" : 6,
			"children" : []
		} ]
	}, {
		"id" : "c101151200",
		"name" : "随州",
		"type" : 2,
		"idx" : 9,
		"children" : [ {
			"id" : "c101151203",
			"name" : "广水",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101151201",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101151202",
			"name" : "随县",
			"type" : 3,
			"idx" : 2,
			"children" : []
		} ]
	}, {
		"id" : "c101151100",
		"name" : "咸宁",
		"type" : 2,
		"idx" : 10,
		"children" : [ {
			"id" : "c101151106",
			"name" : "赤壁",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101151104",
			"name" : "崇阳",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101151102",
			"name" : "嘉鱼",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101151101",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101151103",
			"name" : "通城",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101151105",
			"name" : "通山",
			"type" : 3,
			"idx" : 5,
			"children" : []
		} ]
	}, {
		"id" : "c101150500",
		"name" : "襄阳",
		"type" : 2,
		"idx" : 11,
		"children" : [ {
			"id" : "c101150504",
			"name" : "保康",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101150503",
			"name" : "谷城",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101150505",
			"name" : "老河口",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101150502",
			"name" : "南漳",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101150501",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101150507",
			"name" : "宜城",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101150506",
			"name" : "枣阳",
			"type" : 3,
			"idx" : 6,
			"children" : []
		} ]
	}, {
		"id" : "c101150800",
		"name" : "孝感",
		"type" : 2,
		"idx" : 12,
		"children" : [ {
			"id" : "c101150803",
			"name" : "大悟",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101150807",
			"name" : "汉川",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101150801",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101150802",
			"name" : "孝昌",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101150805",
			"name" : "应城",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101150804",
			"name" : "云梦",
			"type" : 3,
			"idx" : 6,
			"children" : []
		} ]
	}, {
		"id" : "c101150400",
		"name" : "宜昌",
		"type" : 2,
		"idx" : 13,
		"children" : [ {
			"id" : "c101150405",
			"name" : "长阳",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101150408",
			"name" : "当阳",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101150401",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101150406",
			"name" : "五峰",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101150403",
			"name" : "兴山",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101150407",
			"name" : "宜都",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101150402",
			"name" : "远安",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101150409",
			"name" : "枝江",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101150404",
			"name" : "秭归",
			"type" : 3,
			"idx" : 8,
			"children" : []
		} ]
	} ]
}, {
	"id" : "c101160000",
	"name" : "湖南",
	"type" : 1,
	"idx" : 17,
	"city" : [ {
		"id" : "c101160100",
		"name" : "长沙",
		"type" : 2,
		"idx" : 0,
		"children" : [ {
			"id" : "c101160107",
			"name" : "长沙县",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101160101",
			"name" : "芙蓉区",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101160104",
			"name" : "开福区",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101160109",
			"name" : "浏阳",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101160108",
			"name" : "宁乡",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101160102",
			"name" : "天心区",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101160106",
			"name" : "望城区",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101160105",
			"name" : "雨花区",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101160103",
			"name" : "岳麓区",
			"type" : 3,
			"idx" : 8,
			"children" : []
		} ]
	}, {
		"id" : "c101160700",
		"name" : "常德",
		"type" : 2,
		"idx" : 1,
		"children" : [ {
			"id" : "c101160702",
			"name" : "安乡",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101160703",
			"name" : "汉寿",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101160708",
			"name" : "津市",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101160704",
			"name" : "澧县",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101160705",
			"name" : "临澧",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101160707",
			"name" : "石门",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101160701",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101160706",
			"name" : "桃源",
			"type" : 3,
			"idx" : 7,
			"children" : []
		} ]
	}, {
		"id" : "c101161000",
		"name" : "郴州",
		"type" : 2,
		"idx" : 2,
		"children" : [ {
			"id" : "c101161009",
			"name" : "安仁",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101161008",
			"name" : "桂东",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101161002",
			"name" : "桂阳",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101161005",
			"name" : "嘉禾",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101161006",
			"name" : "临武",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101161007",
			"name" : "汝城",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101161001",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101161003",
			"name" : "宜章",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101161004",
			"name" : "永兴",
			"type" : 3,
			"idx" : 8,
			"children" : []
		}, {
			"id" : "c101161010",
			"name" : "资兴",
			"type" : 3,
			"idx" : 9,
			"children" : []
		} ]
	}, {
		"id" : "c101160400",
		"name" : "衡阳",
		"type" : 2,
		"idx" : 3,
		"children" : [ {
			"id" : "c101160408",
			"name" : "常宁",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101160405",
			"name" : "衡东",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101160403",
			"name" : "衡南",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101160404",
			"name" : "衡山",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101160402",
			"name" : "衡阳县",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101160407",
			"name" : "耒阳",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101160406",
			"name" : "祁东",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101160401",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 7,
			"children" : []
		} ]
	}, {
		"id" : "c101161200",
		"name" : "怀化",
		"type" : 2,
		"idx" : 4,
		"children" : [ {
			"id" : "c101161204",
			"name" : "辰溪",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101161212",
			"name" : "洪江",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101161206",
			"name" : "会同",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101161210",
			"name" : "靖州",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101161207",
			"name" : "麻阳",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101161201",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101161211",
			"name" : "通道",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101161208",
			"name" : "新晃",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101161205",
			"name" : "溆浦",
			"type" : 3,
			"idx" : 8,
			"children" : []
		}, {
			"id" : "c101161203",
			"name" : "沅陵",
			"type" : 3,
			"idx" : 9,
			"children" : []
		}, {
			"id" : "c101161209",
			"name" : "芷江",
			"type" : 3,
			"idx" : 10,
			"children" : []
		}, {
			"id" : "c101161202",
			"name" : "中方",
			"type" : 3,
			"idx" : 11,
			"children" : []
		} ]
	}, {
		"id" : "c101161300",
		"name" : "娄底",
		"type" : 2,
		"idx" : 5,
		"children" : [ {
			"id" : "c101161304",
			"name" : "冷水江",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101161305",
			"name" : "涟源",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101161301",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101161302",
			"name" : "双峰",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101161303",
			"name" : "新化",
			"type" : 3,
			"idx" : 4,
			"children" : []
		} ]
	}, {
		"id" : "c101160500",
		"name" : "邵阳",
		"type" : 2,
		"idx" : 6,
		"children" : [ {
			"id" : "c101160509",
			"name" : "城步",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101160506",
			"name" : "洞口",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101160505",
			"name" : "隆回",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101160502",
			"name" : "邵东",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101160504",
			"name" : "邵阳县",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101160501",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101160507",
			"name" : "绥宁",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101160510",
			"name" : "武冈",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101160508",
			"name" : "新宁",
			"type" : 3,
			"idx" : 8,
			"children" : []
		}, {
			"id" : "c101160503",
			"name" : "新邵",
			"type" : 3,
			"idx" : 9,
			"children" : []
		} ]
	}, {
		"id" : "c101160300",
		"name" : "湘潭",
		"type" : 2,
		"idx" : 7,
		"children" : [ {
			"id" : "c101160304",
			"name" : "韶山",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101160301",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101160302",
			"name" : "湘潭县",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101160303",
			"name" : "湘乡",
			"type" : 3,
			"idx" : 3,
			"children" : []
		} ]
	}, {
		"id" : "c101161400",
		"name" : "湘西",
		"type" : 2,
		"idx" : 8,
		"children" : [ {
			"id" : "c101161405",
			"name" : "保靖",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101161403",
			"name" : "凤凰",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101161406",
			"name" : "古丈",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101161404",
			"name" : "花垣",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101161401",
			"name" : "吉首",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101161408",
			"name" : "龙山",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101161402",
			"name" : "泸溪",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101161407",
			"name" : "永顺",
			"type" : 3,
			"idx" : 7,
			"children" : []
		} ]
	}, {
		"id" : "c101160900",
		"name" : "益阳",
		"type" : 2,
		"idx" : 9,
		"children" : [ {
			"id" : "c101160904",
			"name" : "安化",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101160902",
			"name" : "南县",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101160901",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101160903",
			"name" : "桃江",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101160905",
			"name" : "沅江",
			"type" : 3,
			"idx" : 4,
			"children" : []
		} ]
	}, {
		"id" : "c101161100",
		"name" : "永州",
		"type" : 2,
		"idx" : 10,
		"children" : [ {
			"id" : "c101161105",
			"name" : "道县",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101161103",
			"name" : "东安",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101161110",
			"name" : "江华",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101161106",
			"name" : "江永",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101161108",
			"name" : "蓝山",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101161107",
			"name" : "宁远",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101161102",
			"name" : "祁阳",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101161101",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101161104",
			"name" : "双牌",
			"type" : 3,
			"idx" : 8,
			"children" : []
		}, {
			"id" : "c101161109",
			"name" : "新田",
			"type" : 3,
			"idx" : 9,
			"children" : []
		} ]
	}, {
		"id" : "c101160600",
		"name" : "岳阳",
		"type" : 2,
		"idx" : 11,
		"children" : [ {
			"id" : "c101160603",
			"name" : "华容",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101160607",
			"name" : "临湘",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101160606",
			"name" : "汨罗",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101160605",
			"name" : "平江",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101160601",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101160604",
			"name" : "湘阴",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101160602",
			"name" : "岳阳县",
			"type" : 3,
			"idx" : 6,
			"children" : []
		} ]
	}, {
		"id" : "c101160800",
		"name" : "张家界",
		"type" : 2,
		"idx" : 12,
		"children" : [ {
			"id" : "c101160802",
			"name" : "慈利",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101160803",
			"name" : "桑植",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101160801",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 2,
			"children" : []
		} ]
	}, {
		"id" : "c101160200",
		"name" : "株洲",
		"type" : 2,
		"idx" : 13,
		"children" : [ {
			"id" : "c101160204",
			"name" : "茶陵",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101160206",
			"name" : "醴陵",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101160201",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101160205",
			"name" : "炎陵",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101160203",
			"name" : "攸县",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101160202",
			"name" : "株洲县",
			"type" : 3,
			"idx" : 5,
			"children" : []
		} ]
	} ]
}, {
	"id" : "c101170000",
	"name" : "吉林",
	"type" : 1,
	"idx" : 18,
	"city" : [ {
		"id" : "c101170100",
		"name" : "长春",
		"type" : 2,
		"idx" : 0,
		"children" : [ {
			"id" : "c101170103",
			"name" : "朝阳区",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101170110",
			"name" : "德惠",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101170104",
			"name" : "二道区",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101170108",
			"name" : "九台",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101170102",
			"name" : "宽城区",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101170105",
			"name" : "绿园区",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101170101",
			"name" : "南关区",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101170107",
			"name" : "农安",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101170106",
			"name" : "双阳区",
			"type" : 3,
			"idx" : 8,
			"children" : []
		}, {
			"id" : "c101170109",
			"name" : "榆树",
			"type" : 3,
			"idx" : 9,
			"children" : []
		} ]
	}, {
		"id" : "c101170800",
		"name" : "白城",
		"type" : 2,
		"idx" : 1,
		"children" : [ {
			"id" : "c101170805",
			"name" : "大安",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101170801",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101170804",
			"name" : "洮南",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101170803",
			"name" : "通榆",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101170802",
			"name" : "镇赉",
			"type" : 3,
			"idx" : 4,
			"children" : []
		} ]
	}, {
		"id" : "c101170600",
		"name" : "白山",
		"type" : 2,
		"idx" : 2,
		"children" : [ {
			"id" : "c101170604",
			"name" : "长白",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101170602",
			"name" : "抚松",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101170603",
			"name" : "靖宇",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101170605",
			"name" : "临江",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101170601",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 4,
			"children" : []
		} ]
	}, {
		"id" : "c101170200",
		"name" : "吉林",
		"type" : 2,
		"idx" : 3,
		"children" : [ {
			"id" : "c101170204",
			"name" : "桦甸",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101170203",
			"name" : "蛟河",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101170201",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101170205",
			"name" : "舒兰",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101170202",
			"name" : "永吉",
			"type" : 3,
			"idx" : 5,
			"children" : []
		} ]
	}, {
		"id" : "c101170400",
		"name" : "辽源",
		"type" : 2,
		"idx" : 4,
		"children" : [ {
			"id" : "c101170402",
			"name" : "东丰",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101170403",
			"name" : "东辽",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101170401",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 2,
			"children" : []
		} ]
	}, {
		"id" : "c101170300",
		"name" : "四平",
		"type" : 2,
		"idx" : 5,
		"children" : [ {
			"id" : "c101170304",
			"name" : "公主岭",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101170302",
			"name" : "梨树",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101170301",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101170305",
			"name" : "双辽",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101170303",
			"name" : "伊通",
			"type" : 3,
			"idx" : 4,
			"children" : []
		} ]
	}, {
		"id" : "c101170700",
		"name" : "松原",
		"type" : 2,
		"idx" : 6,
		"children" : [ {
			"id" : "c101170703",
			"name" : "长岭",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101170705",
			"name" : "扶余",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101170702",
			"name" : "前郭尔罗斯",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101170704",
			"name" : "乾安",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101170701",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 4,
			"children" : []
		} ]
	}, {
		"id" : "c101170500",
		"name" : "通化",
		"type" : 2,
		"idx" : 7,
		"children" : [ {
			"id" : "c101170503",
			"name" : "辉南",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101170506",
			"name" : "集安",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101170504",
			"name" : "柳河",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101170505",
			"name" : "梅河口",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101170501",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101170502",
			"name" : "通化县",
			"type" : 3,
			"idx" : 5,
			"children" : []
		} ]
	}, {
		"id" : "c101170900",
		"name" : "延边",
		"type" : 2,
		"idx" : 8,
		"children" : [ {
			"id" : "c101170908",
			"name" : "安图",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101170903",
			"name" : "敦化",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101170906",
			"name" : "和龙",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101170904",
			"name" : "珲春",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101170905",
			"name" : "龙井",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101170902",
			"name" : "图们",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101170907",
			"name" : "汪清",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101170901",
			"name" : "延吉",
			"type" : 3,
			"idx" : 7,
			"children" : []
		} ]
	} ]
}, {
	"id" : "c101180000",
	"name" : "江苏",
	"type" : 1,
	"idx" : 19,
	"city" : [ {
		"id" : "c101180100",
		"name" : "南京",
		"type" : 2,
		"idx" : 0,
		"children" : [ {
			"id" : "c101180102",
			"name" : "白下区",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101180113",
			"name" : "高淳",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101180105",
			"name" : "鼓楼区",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101180104",
			"name" : "建邺区",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101180110",
			"name" : "江宁区",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101180112",
			"name" : "溧水",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101180111",
			"name" : "六合区",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101180107",
			"name" : "浦口区",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101180108",
			"name" : "栖霞区",
			"type" : 3,
			"idx" : 8,
			"children" : []
		}, {
			"id" : "c101180103",
			"name" : "秦淮区",
			"type" : 3,
			"idx" : 9,
			"children" : []
		}, {
			"id" : "c101180106",
			"name" : "下关区",
			"type" : 3,
			"idx" : 10,
			"children" : []
		}, {
			"id" : "c101180101",
			"name" : "玄武区",
			"type" : 3,
			"idx" : 11,
			"children" : []
		}, {
			"id" : "c101180109",
			"name" : "雨花台区",
			"type" : 3,
			"idx" : 12,
			"children" : []
		} ]
	}, {
		"id" : "c101180500",
		"name" : "苏州",
		"type" : 2,
		"idx" : 1,
		"children" : [ {
			"id" : "c101180504",
			"name" : "昆山",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101180503",
			"name" : "张家港",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101180502",
			"name" : "常熟",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101180501",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101180506",
			"name" : "太仓",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101180505",
			"name" : "吴江",
			"type" : 3,
			"idx" : 5,
			"children" : []
		} ]
	}, {
		"id" : "c101180200",
		"name" : "无锡",
		"type" : 2,
		"idx" : 2,
		"children" : [ {
			"id" : "c101180202",
			"name" : "江阴",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101180201",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101180203",
			"name" : "宜兴",
			"type" : 3,
			"idx" : 2,
			"children" : []
		} ]
	}, {
		"id" : "c101180400",
		"name" : "常州",
		"type" : 2,
		"idx" : 3,
		"children" : [ {
			"id" : "c101180403",
			"name" : "金坛",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101180402",
			"name" : "溧阳",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101180401",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 2,
			"children" : []
		} ]
	}, {
		"id" : "c101180800",
		"name" : "淮安",
		"type" : 2,
		"idx" : 4,
		"children" : [ {
			"id" : "c101180803",
			"name" : "洪泽",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101180805",
			"name" : "金湖",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101180802",
			"name" : "涟水",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101180801",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101180804",
			"name" : "盱眙",
			"type" : 3,
			"idx" : 4,
			"children" : []
		} ]
	}, {
		"id" : "c101180700",
		"name" : "连云港",
		"type" : 2,
		"idx" : 5,
		"children" : [ {
			"id" : "c101180703",
			"name" : "东海",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101180702",
			"name" : "赣榆",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101180705",
			"name" : "灌南",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101180704",
			"name" : "灌云",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101180701",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 4,
			"children" : []
		} ]
	}, {
		"id" : "c101180600",
		"name" : "南通",
		"type" : 2,
		"idx" : 6,
		"children" : [ {
			"id" : "c101180602",
			"name" : "海安",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101180606",
			"name" : "海门",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101180604",
			"name" : "启东",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101180603",
			"name" : "如东",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101180605",
			"name" : "如皋",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101180601",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 5,
			"children" : []
		} ]
	}, {
		"id" : "c101181300",
		"name" : "宿迁",
		"type" : 2,
		"idx" : 7,
		"children" : [ {
			"id" : "c101181301",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101181302",
			"name" : "沭阳",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101181304",
			"name" : "泗洪",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101181303",
			"name" : "泗阳",
			"type" : 3,
			"idx" : 3,
			"children" : []
		} ]
	}, {
		"id" : "c101181200",
		"name" : "泰州",
		"type" : 2,
		"idx" : 8,
		"children" : [ {
			"id" : "c101181205",
			"name" : "姜堰",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101181203",
			"name" : "靖江",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101181201",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101181204",
			"name" : "泰兴",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101181202",
			"name" : "兴化",
			"type" : 3,
			"idx" : 4,
			"children" : []
		} ]
	}, {
		"id" : "c101180300",
		"name" : "徐州",
		"type" : 2,
		"idx" : 9,
		"children" : [ {
			"id" : "c101180302",
			"name" : "丰县",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101180303",
			"name" : "沛县",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101180306",
			"name" : "邳州",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101180301",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101180304",
			"name" : "睢宁",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101180305",
			"name" : "新沂",
			"type" : 3,
			"idx" : 5,
			"children" : []
		} ]
	}, {
		"id" : "c101180900",
		"name" : "盐城",
		"type" : 2,
		"idx" : 10,
		"children" : [ {
			"id" : "c101180903",
			"name" : "滨海",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101180908",
			"name" : "大丰",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101180907",
			"name" : "东台",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101180904",
			"name" : "阜宁",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101180906",
			"name" : "建湖",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101180905",
			"name" : "射阳",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101180901",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101180902",
			"name" : "响水",
			"type" : 3,
			"idx" : 7,
			"children" : []
		} ]
	}, {
		"id" : "c101181000",
		"name" : "扬州",
		"type" : 2,
		"idx" : 11,
		"children" : [ {
			"id" : "c101181002",
			"name" : "宝应",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101181004",
			"name" : "高邮",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101181001",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101181003",
			"name" : "仪征",
			"type" : 3,
			"idx" : 3,
			"children" : []
		} ]
	}, {
		"id" : "c101181100",
		"name" : "镇江",
		"type" : 2,
		"idx" : 12,
		"children" : [ {
			"id" : "c101181102",
			"name" : "丹阳",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101181104",
			"name" : "句容",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101181101",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101181103",
			"name" : "扬中",
			"type" : 3,
			"idx" : 3,
			"children" : []
		} ]
	} ]
}, {
	"id" : "c101190000",
	"name" : "江西",
	"type" : 1,
	"idx" : 20,
	"city" : [ {
		"id" : "c101190100",
		"name" : "南昌",
		"type" : 2,
		"idx" : 0,
		"children" : [ {
			"id" : "c101190108",
			"name" : "安义",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101190101",
			"name" : "东湖区",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101190109",
			"name" : "进贤",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101190106",
			"name" : "南昌县",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101190105",
			"name" : "青山湖区",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101190103",
			"name" : "青云谱区",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101190104",
			"name" : "湾里区",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101190102",
			"name" : "西湖区",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101190107",
			"name" : "新建",
			"type" : 3,
			"idx" : 8,
			"children" : []
		} ]
	}, {
		"id" : "c101191000",
		"name" : "抚州",
		"type" : 2,
		"idx" : 1,
		"children" : [ {
			"id" : "c101191005",
			"name" : "崇仁",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101191010",
			"name" : "东乡",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101191011",
			"name" : "广昌",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101191008",
			"name" : "金溪",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101191006",
			"name" : "乐安",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101191003",
			"name" : "黎川",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101191002",
			"name" : "南城",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101191004",
			"name" : "南丰",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101191001",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 8,
			"children" : []
		}, {
			"id" : "c101191007",
			"name" : "宜黄",
			"type" : 3,
			"idx" : 9,
			"children" : []
		}, {
			"id" : "c101191009",
			"name" : "资溪",
			"type" : 3,
			"idx" : 10,
			"children" : []
		} ]
	}, {
		"id" : "c101190700",
		"name" : "赣州",
		"type" : 2,
		"idx" : 2,
		"children" : [ {
			"id" : "c101190707",
			"name" : "安远",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101190706",
			"name" : "崇义",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101190704",
			"name" : "大余",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101190709",
			"name" : "定南",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101190702",
			"name" : "赣县",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101190714",
			"name" : "会昌",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101190708",
			"name" : "龙南",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101190718",
			"name" : "南康",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101190711",
			"name" : "宁都",
			"type" : 3,
			"idx" : 8,
			"children" : []
		}, {
			"id" : "c101190710",
			"name" : "全南",
			"type" : 3,
			"idx" : 9,
			"children" : []
		}, {
			"id" : "c101190717",
			"name" : "瑞金",
			"type" : 3,
			"idx" : 10,
			"children" : []
		}, {
			"id" : "c101190705",
			"name" : "上犹",
			"type" : 3,
			"idx" : 11,
			"children" : []
		}, {
			"id" : "c101190716",
			"name" : "石城",
			"type" : 3,
			"idx" : 12,
			"children" : []
		}, {
			"id" : "c101190701",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 13,
			"children" : []
		}, {
			"id" : "c101190703",
			"name" : "信丰",
			"type" : 3,
			"idx" : 14,
			"children" : []
		}, {
			"id" : "c101190713",
			"name" : "兴国",
			"type" : 3,
			"idx" : 15,
			"children" : []
		}, {
			"id" : "c101190715",
			"name" : "寻乌",
			"type" : 3,
			"idx" : 16,
			"children" : []
		}, {
			"id" : "c101190712",
			"name" : "于都",
			"type" : 3,
			"idx" : 17,
			"children" : []
		} ]
	}, {
		"id" : "c101190800",
		"name" : "吉安",
		"type" : 2,
		"idx" : 3,
		"children" : [ {
			"id" : "c101190810",
			"name" : "安福",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101190802",
			"name" : "吉安县",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101190803",
			"name" : "吉水",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101190812",
			"name" : "井冈山",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101190801",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101190808",
			"name" : "遂川",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101190807",
			"name" : "泰和",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101190809",
			"name" : "万安",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101190804",
			"name" : "峡江",
			"type" : 3,
			"idx" : 8,
			"children" : []
		}, {
			"id" : "c101190805",
			"name" : "新干",
			"type" : 3,
			"idx" : 9,
			"children" : []
		}, {
			"id" : "c101190806",
			"name" : "永丰",
			"type" : 3,
			"idx" : 10,
			"children" : []
		}, {
			"id" : "c101190811",
			"name" : "永新",
			"type" : 3,
			"idx" : 11,
			"children" : []
		} ]
	}, {
		"id" : "c101190200",
		"name" : "景德镇",
		"type" : 2,
		"idx" : 4,
		"children" : [ {
			"id" : "c101190202",
			"name" : "浮梁",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101190203",
			"name" : "乐平",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101190201",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 2,
			"children" : []
		} ]
	}, {
		"id" : "c101190400",
		"name" : "九江",
		"type" : 2,
		"idx" : 5,
		"children" : [ {
			"id" : "c101190406",
			"name" : "德安",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101190408",
			"name" : "都昌",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101190412",
			"name" : "共青城",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101190409",
			"name" : "湖口",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101190402",
			"name" : "九江县",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101190410",
			"name" : "彭泽",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101190411",
			"name" : "瑞昌",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101190401",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101190403",
			"name" : "武宁",
			"type" : 3,
			"idx" : 8,
			"children" : []
		}, {
			"id" : "c101190407",
			"name" : "星子",
			"type" : 3,
			"idx" : 9,
			"children" : []
		}, {
			"id" : "c101190404",
			"name" : "修水",
			"type" : 3,
			"idx" : 10,
			"children" : []
		}, {
			"id" : "c101190405",
			"name" : "永修",
			"type" : 3,
			"idx" : 11,
			"children" : []
		} ]
	}, {
		"id" : "c101190300",
		"name" : "萍乡",
		"type" : 2,
		"idx" : 6,
		"children" : [ {
			"id" : "c101190302",
			"name" : "莲花",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101190304",
			"name" : "芦溪",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101190303",
			"name" : "上栗",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101190301",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 3,
			"children" : []
		} ]
	}, {
		"id" : "c101191100",
		"name" : "上饶",
		"type" : 2,
		"idx" : 7,
		"children" : [ {
			"id" : "c101191103",
			"name" : "广丰",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101191106",
			"name" : "横峰",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101191109",
			"name" : "鄱阳",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101191105",
			"name" : "铅山",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101191102",
			"name" : "上饶县",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101191101",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101191110",
			"name" : "万年",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101191111",
			"name" : "婺源",
			"type" : 3,
			"idx" : 8,
			"children" : []
		}, {
			"id" : "c101191107",
			"name" : "弋阳",
			"type" : 3,
			"idx" : 9,
			"children" : []
		}, {
			"id" : "c101191108",
			"name" : "余干",
			"type" : 3,
			"idx" : 10,
			"children" : []
		}, {
			"id" : "c101191104",
			"name" : "玉山",
			"type" : 3,
			"idx" : 11,
			"children" : []
		} ]
	}, {
		"id" : "c101190500",
		"name" : "新余",
		"type" : 2,
		"idx" : 8,
		"children" : [ {
			"id" : "c101190502",
			"name" : "分宜",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101190501",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 1,
			"children" : []
		} ]
	}, {
		"id" : "c101190900",
		"name" : "宜春",
		"type" : 2,
		"idx" : 9,
		"children" : [ {
			"id" : "c101190908",
			"name" : "丰城",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101190902",
			"name" : "奉新",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101190910",
			"name" : "高安",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101190906",
			"name" : "靖安",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101190904",
			"name" : "上高",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101190901",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101190907",
			"name" : "铜鼓",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101190903",
			"name" : "万载",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101190905",
			"name" : "宜丰",
			"type" : 3,
			"idx" : 8,
			"children" : []
		}, {
			"id" : "c101190909",
			"name" : "樟树",
			"type" : 3,
			"idx" : 9,
			"children" : []
		} ]
	}, {
		"id" : "c101190600",
		"name" : "鹰潭",
		"type" : 2,
		"idx" : 10,
		"children" : [ {
			"id" : "c101190603",
			"name" : "贵溪",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101190601",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101190602",
			"name" : "余江",
			"type" : 3,
			"idx" : 2,
			"children" : []
		} ]
	} ]
}, {
	"id" : "c101200000",
	"name" : "辽宁",
	"type" : 1,
	"idx" : 21,
	"city" : [ {
		"id" : "c101200200",
		"name" : "大连",
		"type" : 2,
		"idx" : 0,
		"children" : [ {
			"id" : "c101200207",
			"name" : "长海",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101200204",
			"name" : "甘井子区",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101200206",
			"name" : "金州区",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101200205",
			"name" : "旅顺口区",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101200209",
			"name" : "普兰店",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101200203",
			"name" : "沙河口区",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101200208",
			"name" : "瓦房店",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101200202",
			"name" : "西岗区",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101200201",
			"name" : "中山区",
			"type" : 3,
			"idx" : 8,
			"children" : []
		}, {
			"id" : "c101200210",
			"name" : "庄河",
			"type" : 3,
			"idx" : 9,
			"children" : []
		} ]
	}, {
		"id" : "c101200100",
		"name" : "沈阳",
		"type" : 2,
		"idx" : 1,
		"children" : [ {
			"id" : "c101200103",
			"name" : "大东区",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101200107",
			"name" : "东陵区",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101200112",
			"name" : "法库",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101200101",
			"name" : "和平区",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101200104",
			"name" : "皇姑区",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101200111",
			"name" : "康平",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101200110",
			"name" : "辽中",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101200108",
			"name" : "沈北新区",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101200102",
			"name" : "沈河区",
			"type" : 3,
			"idx" : 8,
			"children" : []
		}, {
			"id" : "c101200106",
			"name" : "苏家屯区",
			"type" : 3,
			"idx" : 9,
			"children" : []
		}, {
			"id" : "c101200105",
			"name" : "铁西区",
			"type" : 3,
			"idx" : 10,
			"children" : []
		}, {
			"id" : "c101200113",
			"name" : "新民",
			"type" : 3,
			"idx" : 11,
			"children" : []
		}, {
			"id" : "c101200109",
			"name" : "于洪区",
			"type" : 3,
			"idx" : 12,
			"children" : []
		} ]
	}, {
		"id" : "c101200300",
		"name" : "鞍山",
		"type" : 2,
		"idx" : 2,
		"children" : [ {
			"id" : "c101200304",
			"name" : "海城",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101200301",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101200302",
			"name" : "台安",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101200303",
			"name" : "岫岩",
			"type" : 3,
			"idx" : 3,
			"children" : []
		} ]
	}, {
		"id" : "c101200500",
		"name" : "本溪",
		"type" : 2,
		"idx" : 3,
		"children" : [ {
			"id" : "c101200502",
			"name" : "本溪县",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101200503",
			"name" : "桓仁",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101200501",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 2,
			"children" : []
		} ]
	}, {
		"id" : "c101201300",
		"name" : "朝阳",
		"type" : 2,
		"idx" : 4,
		"children" : [ {
			"id" : "c101201305",
			"name" : "北票",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101201302",
			"name" : "朝阳县",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101201303",
			"name" : "建平",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101201304",
			"name" : "喀喇沁左翼",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101201306",
			"name" : "凌源",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101201301",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 5,
			"children" : []
		} ]
	}, {
		"id" : "c101200600",
		"name" : "丹东",
		"type" : 2,
		"idx" : 5,
		"children" : [ {
			"id" : "c101200603",
			"name" : "东港",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101200604",
			"name" : "凤城",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101200602",
			"name" : "宽甸",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101200601",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 3,
			"children" : []
		} ]
	}, {
		"id" : "c101200400",
		"name" : "抚顺",
		"type" : 2,
		"idx" : 6,
		"children" : [ {
			"id" : "c101200402",
			"name" : "抚顺县",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101200404",
			"name" : "清原",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101200401",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101200403",
			"name" : "新宾",
			"type" : 3,
			"idx" : 3,
			"children" : []
		} ]
	}, {
		"id" : "c101200900",
		"name" : "阜新",
		"type" : 2,
		"idx" : 7,
		"children" : [ {
			"id" : "c101200902",
			"name" : "阜新县",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101200901",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101200903",
			"name" : "彰武",
			"type" : 3,
			"idx" : 2,
			"children" : []
		} ]
	}, {
		"id" : "c101201400",
		"name" : "葫芦岛",
		"type" : 2,
		"idx" : 8,
		"children" : [ {
			"id" : "c101201403",
			"name" : "建昌",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101201401",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101201402",
			"name" : "绥中",
			"type" : 3,
			"idx" : 2,
			"children" : []
		} ]
	}, {
		"id" : "c101200700",
		"name" : "锦州",
		"type" : 2,
		"idx" : 9,
		"children" : [ {
			"id" : "c101200705",
			"name" : "北镇",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101200702",
			"name" : "黑山",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101200704",
			"name" : "凌海",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101200701",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101200703",
			"name" : "义县",
			"type" : 3,
			"idx" : 4,
			"children" : []
		} ]
	}, {
		"id" : "c101201000",
		"name" : "辽阳",
		"type" : 2,
		"idx" : 10,
		"children" : [ {
			"id" : "c101201003",
			"name" : "灯塔",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101201002",
			"name" : "辽阳县",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101201001",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 2,
			"children" : []
		} ]
	}, {
		"id" : "c101201100",
		"name" : "盘锦",
		"type" : 2,
		"idx" : 11,
		"children" : [ {
			"id" : "c101201102",
			"name" : "大洼",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101201103",
			"name" : "盘山",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101201101",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 2,
			"children" : []
		} ]
	}, {
		"id" : "c101201200",
		"name" : "铁岭",
		"type" : 2,
		"idx" : 12,
		"children" : [ {
			"id" : "c101201204",
			"name" : "昌图",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101201205",
			"name" : "调兵山",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101201206",
			"name" : "开原",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101201201",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101201202",
			"name" : "铁岭县",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101201203",
			"name" : "西丰",
			"type" : 3,
			"idx" : 5,
			"children" : []
		} ]
	}, {
		"id" : "c101200800",
		"name" : "营口",
		"type" : 2,
		"idx" : 13,
		"children" : [ {
			"id" : "c101200803",
			"name" : "大石桥",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101200802",
			"name" : "盖州",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101200801",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 2,
			"children" : []
		} ]
	} ]
}, {
	"id" : "c101210000",
	"name" : "内蒙古",
	"type" : 1,
	"idx" : 22,
	"city" : [ {
		"id" : "c101211200",
		"name" : "阿拉善盟",
		"type" : 2,
		"idx" : 0,
		"children" : [ {
			"id" : "c101211202",
			"name" : "阿拉善右旗",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101211201",
			"name" : "阿拉善左旗",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101211203",
			"name" : "额济纳旗",
			"type" : 3,
			"idx" : 2,
			"children" : []
		} ]
	}, {
		"id" : "c101210800",
		"name" : "巴彦淖尔",
		"type" : 2,
		"idx" : 1,
		"children" : [ {
			"id" : "c101210803",
			"name" : "磴口",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101210807",
			"name" : "杭锦后旗",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101210801",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101210806",
			"name" : "乌拉特后旗",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101210804",
			"name" : "乌拉特前旗",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101210805",
			"name" : "乌拉特中旗",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101210802",
			"name" : "五原",
			"type" : 3,
			"idx" : 6,
			"children" : []
		} ]
	}, {
		"id" : "c101210200",
		"name" : "包头",
		"type" : 2,
		"idx" : 2,
		"children" : [ {
			"id" : "c101210204",
			"name" : "达尔罕茂明安联合旗",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101210203",
			"name" : "固阳",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101210201",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101210202",
			"name" : "土默特右旗",
			"type" : 3,
			"idx" : 3,
			"children" : []
		} ]
	}, {
		"id" : "c101210400",
		"name" : "赤峰",
		"type" : 2,
		"idx" : 3,
		"children" : [ {
			"id" : "c101210402",
			"name" : "阿鲁科尔沁旗",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101210410",
			"name" : "敖汉旗",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101210404",
			"name" : "巴林右旗",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101210403",
			"name" : "巴林左旗",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101210408",
			"name" : "喀喇沁旗",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101210406",
			"name" : "克什克腾旗",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101210405",
			"name" : "林西",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101210409",
			"name" : "宁城",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101210401",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 8,
			"children" : []
		}, {
			"id" : "c101210407",
			"name" : "翁牛特旗",
			"type" : 3,
			"idx" : 9,
			"children" : []
		} ]
	}, {
		"id" : "c101210600",
		"name" : "鄂尔多斯",
		"type" : 2,
		"idx" : 4,
		"children" : [ {
			"id" : "c101210602",
			"name" : "达拉特旗",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101210605",
			"name" : "鄂托克旗",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101210604",
			"name" : "鄂托克前旗",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101210606",
			"name" : "杭锦旗",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101210601",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101210607",
			"name" : "乌审旗",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101210608",
			"name" : "伊金霍洛旗",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101210603",
			"name" : "准格尔旗",
			"type" : 3,
			"idx" : 7,
			"children" : []
		} ]
	}, {
		"id" : "c101210100",
		"name" : "呼和浩特",
		"type" : 2,
		"idx" : 5,
		"children" : [ {
			"id" : "c101210107",
			"name" : "和林格尔",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101210102",
			"name" : "回民区",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101210108",
			"name" : "清水河",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101210104",
			"name" : "赛罕区",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101210105",
			"name" : "土默特左旗",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101210106",
			"name" : "托克托",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101210109",
			"name" : "武川",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101210101",
			"name" : "新城区",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101210103",
			"name" : "玉泉区",
			"type" : 3,
			"idx" : 8,
			"children" : []
		} ]
	}, {
		"id" : "c101210700",
		"name" : "呼伦贝尔",
		"type" : 2,
		"idx" : 6,
		"children" : [ {
			"id" : "c101210702",
			"name" : "阿荣旗",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101210706",
			"name" : "陈巴尔虎旗",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101210712",
			"name" : "额尔古纳",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101210704",
			"name" : "鄂伦春自治旗",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101210705",
			"name" : "鄂温克族自治旗",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101210713",
			"name" : "根河",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101210709",
			"name" : "满洲里",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101210703",
			"name" : "莫力达瓦达斡尔族自治旗",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101210701",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 8,
			"children" : []
		}, {
			"id" : "c101210708",
			"name" : "新巴尔虎右旗",
			"type" : 3,
			"idx" : 9,
			"children" : []
		}, {
			"id" : "c101210707",
			"name" : "新巴尔虎左旗",
			"type" : 3,
			"idx" : 10,
			"children" : []
		}, {
			"id" : "c101210710",
			"name" : "牙克石",
			"type" : 3,
			"idx" : 11,
			"children" : []
		}, {
			"id" : "c101210711",
			"name" : "扎兰屯",
			"type" : 3,
			"idx" : 12,
			"children" : []
		} ]
	}, {
		"id" : "c101210500",
		"name" : "通辽",
		"type" : 2,
		"idx" : 7,
		"children" : [ {
			"id" : "c101210508",
			"name" : "霍林郭勒",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101210504",
			"name" : "开鲁",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101210503",
			"name" : "科尔沁左翼后旗",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101210502",
			"name" : "科尔沁左翼中旗",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101210505",
			"name" : "库伦旗",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101210506",
			"name" : "奈曼旗",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101210501",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101210507",
			"name" : "扎鲁特旗",
			"type" : 3,
			"idx" : 7,
			"children" : []
		} ]
	}, {
		"id" : "c101210300",
		"name" : "乌海",
		"type" : 2,
		"idx" : 8,
		"children" : [ {
			"id" : "c101210301",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 0,
			"children" : []
		} ]
	}, {
		"id" : "c101210900",
		"name" : "乌兰察布",
		"type" : 2,
		"idx" : 9,
		"children" : [ {
			"id" : "c101210909",
			"name" : "察哈尔右翼后旗",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101210907",
			"name" : "察哈尔右翼前旗",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101210908",
			"name" : "察哈尔右翼中旗",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101210911",
			"name" : "丰镇",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101210903",
			"name" : "化德",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101210906",
			"name" : "凉城",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101210904",
			"name" : "商都",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101210901",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101210910",
			"name" : "四子王旗",
			"type" : 3,
			"idx" : 8,
			"children" : []
		}, {
			"id" : "c101210905",
			"name" : "兴和",
			"type" : 3,
			"idx" : 9,
			"children" : []
		}, {
			"id" : "c101210902",
			"name" : "卓资",
			"type" : 3,
			"idx" : 10,
			"children" : []
		} ]
	}, {
		"id" : "c101211100",
		"name" : "锡林郭勒盟",
		"type" : 2,
		"idx" : 10,
		"children" : [ {
			"id" : "c101211103",
			"name" : "阿巴嘎旗",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101211106",
			"name" : "东乌珠穆沁旗",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101211112",
			"name" : "多伦",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101211101",
			"name" : "二连浩特",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101211105",
			"name" : "苏尼特右旗",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101211104",
			"name" : "苏尼特左旗",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101211108",
			"name" : "太仆寺旗",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101211107",
			"name" : "西乌珠穆沁旗",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101211102",
			"name" : "锡林浩特",
			"type" : 3,
			"idx" : 8,
			"children" : []
		}, {
			"id" : "c101211109",
			"name" : "镶黄旗",
			"type" : 3,
			"idx" : 9,
			"children" : []
		}, {
			"id" : "c101211111",
			"name" : "正蓝旗",
			"type" : 3,
			"idx" : 10,
			"children" : []
		}, {
			"id" : "c101211110",
			"name" : "正镶白旗",
			"type" : 3,
			"idx" : 11,
			"children" : []
		} ]
	}, {
		"id" : "c101211000",
		"name" : "兴安盟",
		"type" : 2,
		"idx" : 11,
		"children" : [ {
			"id" : "c101211002",
			"name" : "阿尔山",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101211003",
			"name" : "科尔沁右翼前旗",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101211004",
			"name" : "科尔沁右翼中旗",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101211006",
			"name" : "突泉",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101211001",
			"name" : "乌兰浩特",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101211005",
			"name" : "扎赉特旗",
			"type" : 3,
			"idx" : 5,
			"children" : []
		} ]
	} ]
}, {
	"id" : "c101220000",
	"name" : "宁夏",
	"type" : 1,
	"idx" : 23,
	"city" : [ {
		"id" : "c101220400",
		"name" : "固原",
		"type" : 2,
		"idx" : 0,
		"children" : [ {
			"id" : "c101220404",
			"name" : "泾源",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101220403",
			"name" : "隆德",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101220405",
			"name" : "彭阳",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101220401",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101220402",
			"name" : "西吉",
			"type" : 3,
			"idx" : 4,
			"children" : []
		} ]
	}, {
		"id" : "c101220200",
		"name" : "石嘴山",
		"type" : 2,
		"idx" : 1,
		"children" : [ {
			"id" : "c101220202",
			"name" : "平罗",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101220201",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 1,
			"children" : []
		} ]
	}, {
		"id" : "c101220300",
		"name" : "吴忠",
		"type" : 2,
		"idx" : 2,
		"children" : [ {
			"id" : "c101220304",
			"name" : "青铜峡",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101220301",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101220303",
			"name" : "同心",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101220302",
			"name" : "盐池",
			"type" : 3,
			"idx" : 3,
			"children" : []
		} ]
	}, {
		"id" : "c101220100",
		"name" : "银川",
		"type" : 2,
		"idx" : 3,
		"children" : [ {
			"id" : "c101220105",
			"name" : "贺兰",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101220103",
			"name" : "金凤区",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101220106",
			"name" : "灵武",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101220102",
			"name" : "西夏区",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101220101",
			"name" : "兴庆区",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101220104",
			"name" : "永宁",
			"type" : 3,
			"idx" : 5,
			"children" : []
		} ]
	}, {
		"id" : "c101220500",
		"name" : "中卫",
		"type" : 2,
		"idx" : 4,
		"children" : [ {
			"id" : "c101220503",
			"name" : "海原",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101220501",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101220502",
			"name" : "中宁",
			"type" : 3,
			"idx" : 2,
			"children" : []
		} ]
	} ]
}, {
	"id" : "c101230000",
	"name" : "青海",
	"type" : 1,
	"idx" : 24,
	"city" : [ {
		"id" : "c101230600",
		"name" : "果洛",
		"type" : 2,
		"idx" : 0,
		"children" : [ {
			"id" : "c101230602",
			"name" : "班玛",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101230604",
			"name" : "达日",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101230603",
			"name" : "甘德",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101230605",
			"name" : "久治",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101230606",
			"name" : "玛多",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101230601",
			"name" : "玛沁",
			"type" : 3,
			"idx" : 5,
			"children" : []
		} ]
	}, {
		"id" : "c101230300",
		"name" : "海北",
		"type" : 2,
		"idx" : 1,
		"children" : [ {
			"id" : "c101230304",
			"name" : "刚察",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101230303",
			"name" : "海晏",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101230301",
			"name" : "门源",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101230302",
			"name" : "祁连",
			"type" : 3,
			"idx" : 3,
			"children" : []
		} ]
	}, {
		"id" : "c101230200",
		"name" : "海东",
		"type" : 2,
		"idx" : 2,
		"children" : [ {
			"id" : "c101230204",
			"name" : "互助",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101230205",
			"name" : "化隆",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101230203",
			"name" : "乐都",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101230202",
			"name" : "民和",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101230201",
			"name" : "平安",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101230206",
			"name" : "循化",
			"type" : 3,
			"idx" : 5,
			"children" : []
		} ]
	}, {
		"id" : "c101230500",
		"name" : "海南",
		"type" : 2,
		"idx" : 3,
		"children" : [ {
			"id" : "c101230501",
			"name" : "共和",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101230503",
			"name" : "贵德",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101230505",
			"name" : "贵南",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101230502",
			"name" : "同德",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101230504",
			"name" : "兴海",
			"type" : 3,
			"idx" : 4,
			"children" : []
		} ]
	}, {
		"id" : "c101230800",
		"name" : "海西",
		"type" : 2,
		"idx" : 4,
		"children" : [ {
			"id" : "c101230802",
			"name" : "德令哈",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101230804",
			"name" : "都兰",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101230801",
			"name" : "格尔木",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101230805",
			"name" : "天峻",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101230803",
			"name" : "乌兰",
			"type" : 3,
			"idx" : 4,
			"children" : []
		} ]
	}, {
		"id" : "c101230400",
		"name" : "黄南",
		"type" : 2,
		"idx" : 5,
		"children" : [ {
			"id" : "c101230404",
			"name" : "河南",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101230402",
			"name" : "尖扎",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101230401",
			"name" : "同仁",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101230403",
			"name" : "泽库",
			"type" : 3,
			"idx" : 3,
			"children" : []
		} ]
	}, {
		"id" : "c101230100",
		"name" : "西宁",
		"type" : 2,
		"idx" : 6,
		"children" : [ {
			"id" : "c101230104",
			"name" : "城北区",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101230101",
			"name" : "城东区",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101230103",
			"name" : "城西区",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101230102",
			"name" : "城中区",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101230105",
			"name" : "大通",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101230107",
			"name" : "湟源",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101230106",
			"name" : "湟中",
			"type" : 3,
			"idx" : 6,
			"children" : []
		} ]
	}, {
		"id" : "c101230700",
		"name" : "玉树",
		"type" : 2,
		"idx" : 7,
		"children" : [ {
			"id" : "c101230703",
			"name" : "称多",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101230705",
			"name" : "囊谦",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101230706",
			"name" : "曲麻莱",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101230701",
			"name" : "玉树县",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101230702",
			"name" : "杂多",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101230704",
			"name" : "治多",
			"type" : 3,
			"idx" : 5,
			"children" : []
		} ]
	} ]
}, {
	"id" : "c101240000",
	"name" : "山东",
	"type" : 1,
	"idx" : 25,
	"city" : [ {
		"id" : "c101240100",
		"name" : "济南",
		"type" : 2,
		"idx" : 0,
		"children" : [ {
			"id" : "c101240106",
			"name" : "长清区",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101240103",
			"name" : "槐荫区",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101240108",
			"name" : "济阳",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101240105",
			"name" : "历城区",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101240101",
			"name" : "历下区",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101240107",
			"name" : "平阴",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101240109",
			"name" : "商河",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101240102",
			"name" : "市中区",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101240104",
			"name" : "天桥区",
			"type" : 3,
			"idx" : 8,
			"children" : []
		}, {
			"id" : "c101240110",
			"name" : "章丘",
			"type" : 3,
			"idx" : 9,
			"children" : []
		} ]
	}, {
		"id" : "c101240200",
		"name" : "青岛",
		"type" : 2,
		"idx" : 1,
		"children" : [ {
			"id" : "c101240207",
			"name" : "城阳区",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101240204",
			"name" : "黄岛区",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101240209",
			"name" : "即墨",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101240211",
			"name" : "胶南",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101240208",
			"name" : "胶州",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101240212",
			"name" : "莱西",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101240205",
			"name" : "崂山区",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101240206",
			"name" : "李沧区",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101240210",
			"name" : "平度",
			"type" : 3,
			"idx" : 8,
			"children" : []
		}, {
			"id" : "c101240202",
			"name" : "市北区",
			"type" : 3,
			"idx" : 9,
			"children" : []
		}, {
			"id" : "c101240201",
			"name" : "市南区",
			"type" : 3,
			"idx" : 10,
			"children" : []
		}, {
			"id" : "c101240203",
			"name" : "四方区",
			"type" : 3,
			"idx" : 11,
			"children" : []
		} ]
	}, {
		"id" : "c101240600",
		"name" : "烟台",
		"type" : 2,
		"idx" : 2,
		"children" : [ {
			"id" : "c101240602",
			"name" : "长岛",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101240609",
			"name" : "海阳",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101240604",
			"name" : "莱阳",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101240605",
			"name" : "莱州",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101240603",
			"name" : "龙口",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101240606",
			"name" : "蓬莱",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101240608",
			"name" : "栖霞",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101240601",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101240607",
			"name" : "招远",
			"type" : 3,
			"idx" : 8,
			"children" : []
		} ]
	}, {
		"id" : "c101240300",
		"name" : "淄博",
		"type" : 2,
		"idx" : 3,
		"children" : [ {
			"id" : "c101240303",
			"name" : "高青",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101240302",
			"name" : "桓台",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101240301",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101240304",
			"name" : "沂源",
			"type" : 3,
			"idx" : 3,
			"children" : []
		} ]
	}, {
		"id" : "c101241600",
		"name" : "滨州",
		"type" : 2,
		"idx" : 4,
		"children" : [ {
			"id" : "c101241606",
			"name" : "博兴",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101241602",
			"name" : "惠民",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101241601",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101241604",
			"name" : "无棣",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101241603",
			"name" : "阳信",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101241605",
			"name" : "沾化",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101241607",
			"name" : "邹平",
			"type" : 3,
			"idx" : 6,
			"children" : []
		} ]
	}, {
		"id" : "c101241400",
		"name" : "德州",
		"type" : 2,
		"idx" : 5,
		"children" : [ {
			"id" : "c101241410",
			"name" : "乐陵",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101241405",
			"name" : "临邑",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101241402",
			"name" : "陵县",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101241403",
			"name" : "宁津",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101241407",
			"name" : "平原",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101241406",
			"name" : "齐河",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101241404",
			"name" : "庆云",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101241401",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101241409",
			"name" : "武城",
			"type" : 3,
			"idx" : 8,
			"children" : []
		}, {
			"id" : "c101241408",
			"name" : "夏津",
			"type" : 3,
			"idx" : 9,
			"children" : []
		}, {
			"id" : "c101241411",
			"name" : "禹城",
			"type" : 3,
			"idx" : 10,
			"children" : []
		} ]
	}, {
		"id" : "c101240500",
		"name" : "东营",
		"type" : 2,
		"idx" : 6,
		"children" : [ {
			"id" : "c101240504",
			"name" : "广饶",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101240502",
			"name" : "垦利",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101240503",
			"name" : "利津",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101240501",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 3,
			"children" : []
		} ]
	}, {
		"id" : "c101241700",
		"name" : "菏泽",
		"type" : 2,
		"idx" : 7,
		"children" : [ {
			"id" : "c101241704",
			"name" : "成武",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101241703",
			"name" : "单县",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101241708",
			"name" : "定陶",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101241709",
			"name" : "东明",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101241705",
			"name" : "巨野",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101241707",
			"name" : "鄄城",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101241701",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101241706",
			"name" : "郓城",
			"type" : 3,
			"idx" : 8,
			"children" : []
		} ]
	}, {
		"id" : "c101240800",
		"name" : "济宁",
		"type" : 2,
		"idx" : 8,
		"children" : [ {
			"id" : "c101240805",
			"name" : "嘉祥",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101240804",
			"name" : "金乡",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101240808",
			"name" : "梁山",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101240809",
			"name" : "曲阜",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101240801",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101240807",
			"name" : "泗水",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101240802",
			"name" : "微山",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101240806",
			"name" : "汶上",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101240810",
			"name" : "兖州",
			"type" : 3,
			"idx" : 8,
			"children" : []
		}, {
			"id" : "c101240803",
			"name" : "鱼台",
			"type" : 3,
			"idx" : 9,
			"children" : []
		}, {
			"id" : "c101240811",
			"name" : "邹城",
			"type" : 3,
			"idx" : 10,
			"children" : []
		} ]
	}, {
		"id" : "c101241200",
		"name" : "莱芜",
		"type" : 2,
		"idx" : 9,
		"children" : [ {
			"id" : "c101241201",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 0,
			"children" : []
		} ]
	}, {
		"id" : "c101241500",
		"name" : "聊城",
		"type" : 2,
		"idx" : 10,
		"children" : [ {
			"id" : "c101241504",
			"name" : "茌平",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101241505",
			"name" : "东阿",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101241507",
			"name" : "高唐",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101241506",
			"name" : "冠县",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101241508",
			"name" : "临清",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101241501",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101241503",
			"name" : "莘县",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101241502",
			"name" : "阳谷",
			"type" : 3,
			"idx" : 7,
			"children" : []
		} ]
	}, {
		"id" : "c101241300",
		"name" : "临沂",
		"type" : 2,
		"idx" : 11,
		"children" : [ {
			"id" : "c101241305",
			"name" : "苍山",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101241306",
			"name" : "费县",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101241308",
			"name" : "莒南",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101241310",
			"name" : "临沭",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101241309",
			"name" : "蒙阴",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101241307",
			"name" : "平邑",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101241301",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101241303",
			"name" : "郯城",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101241302",
			"name" : "沂南",
			"type" : 3,
			"idx" : 8,
			"children" : []
		}, {
			"id" : "c101241304",
			"name" : "沂水",
			"type" : 3,
			"idx" : 9,
			"children" : []
		} ]
	}, {
		"id" : "c101241100",
		"name" : "日照",
		"type" : 2,
		"idx" : 12,
		"children" : [ {
			"id" : "c101241103",
			"name" : "莒县",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101241101",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101241102",
			"name" : "五莲",
			"type" : 3,
			"idx" : 2,
			"children" : []
		} ]
	}, {
		"id" : "c101240900",
		"name" : "泰安",
		"type" : 2,
		"idx" : 13,
		"children" : [ {
			"id" : "c101240903",
			"name" : "东平",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101240905",
			"name" : "肥城",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101240902",
			"name" : "宁阳",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101240901",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101240904",
			"name" : "新泰",
			"type" : 3,
			"idx" : 4,
			"children" : []
		} ]
	}, {
		"id" : "c101241000",
		"name" : "威海",
		"type" : 2,
		"idx" : 14,
		"children" : [ {
			"id" : "c101241003",
			"name" : "荣成",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101241004",
			"name" : "乳山",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101241001",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101241002",
			"name" : "文登",
			"type" : 3,
			"idx" : 3,
			"children" : []
		} ]
	}, {
		"id" : "c101240700",
		"name" : "潍坊",
		"type" : 2,
		"idx" : 15,
		"children" : [ {
			"id" : "c101240707",
			"name" : "安丘",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101240703",
			"name" : "昌乐",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101240709",
			"name" : "昌邑",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101240708",
			"name" : "高密",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101240702",
			"name" : "临朐",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101240704",
			"name" : "青州",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101240701",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101240706",
			"name" : "寿光",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101240705",
			"name" : "诸城",
			"type" : 3,
			"idx" : 8,
			"children" : []
		} ]
	}, {
		"id" : "c101240400",
		"name" : "枣庄",
		"type" : 2,
		"idx" : 16,
		"children" : [ {
			"id" : "c101240401",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101240402",
			"name" : "滕州",
			"type" : 3,
			"idx" : 1,
			"children" : []
		} ]
	} ]
}, {
	"id" : "c101250000",
	"name" : "山西",
	"type" : 1,
	"idx" : 26,
	"city" : [ {
		"id" : "c101250100",
		"name" : "太原",
		"type" : 2,
		"idx" : 0,
		"children" : [ {
			"id" : "c101250110",
			"name" : "古交",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101250104",
			"name" : "尖草坪区",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101250106",
			"name" : "晋源区",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101250109",
			"name" : "娄烦",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101250107",
			"name" : "清徐",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101250105",
			"name" : "万柏林区",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101250101",
			"name" : "小店区",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101250103",
			"name" : "杏花岭区",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101250108",
			"name" : "阳曲",
			"type" : 3,
			"idx" : 8,
			"children" : []
		}, {
			"id" : "c101250102",
			"name" : "迎泽区",
			"type" : 3,
			"idx" : 9,
			"children" : []
		} ]
	}, {
		"id" : "c101250400",
		"name" : "长治",
		"type" : 2,
		"idx" : 1,
		"children" : [ {
			"id" : "c101250402",
			"name" : "长治县",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101250408",
			"name" : "长子",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101250407",
			"name" : "壶关",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101250406",
			"name" : "黎城",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101250412",
			"name" : "潞城",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101250405",
			"name" : "平顺",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101250410",
			"name" : "沁县",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101250411",
			"name" : "沁源",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101250401",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 8,
			"children" : []
		}, {
			"id" : "c101250404",
			"name" : "屯留",
			"type" : 3,
			"idx" : 9,
			"children" : []
		}, {
			"id" : "c101250409",
			"name" : "武乡",
			"type" : 3,
			"idx" : 10,
			"children" : []
		}, {
			"id" : "c101250403",
			"name" : "襄垣",
			"type" : 3,
			"idx" : 11,
			"children" : []
		} ]
	}, {
		"id" : "c101250200",
		"name" : "大同",
		"type" : 2,
		"idx" : 2,
		"children" : [ {
			"id" : "c101250208",
			"name" : "大同县",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101250204",
			"name" : "广灵",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101250206",
			"name" : "浑源",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101250205",
			"name" : "灵丘",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101250201",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101250203",
			"name" : "天镇",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101250202",
			"name" : "阳高",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101250207",
			"name" : "左云",
			"type" : 3,
			"idx" : 7,
			"children" : []
		} ]
	}, {
		"id" : "c101250500",
		"name" : "晋城",
		"type" : 2,
		"idx" : 3,
		"children" : [ {
			"id" : "c101250506",
			"name" : "高平",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101250504",
			"name" : "陵川",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101250502",
			"name" : "沁水",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101250501",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101250503",
			"name" : "阳城",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101250505",
			"name" : "泽州",
			"type" : 3,
			"idx" : 5,
			"children" : []
		} ]
	}, {
		"id" : "c101250700",
		"name" : "晋中",
		"type" : 2,
		"idx" : 4,
		"children" : [ {
			"id" : "c101250704",
			"name" : "和顺",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101250711",
			"name" : "介休",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101250710",
			"name" : "灵石",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101250709",
			"name" : "平遥",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101250708",
			"name" : "祁县",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101250701",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101250706",
			"name" : "寿阳",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101250707",
			"name" : "太谷",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101250705",
			"name" : "昔阳",
			"type" : 3,
			"idx" : 8,
			"children" : []
		}, {
			"id" : "c101250702",
			"name" : "榆社",
			"type" : 3,
			"idx" : 9,
			"children" : []
		}, {
			"id" : "c101250703",
			"name" : "左权",
			"type" : 3,
			"idx" : 10,
			"children" : []
		} ]
	}, {
		"id" : "c101251000",
		"name" : "临汾",
		"type" : 2,
		"idx" : 5,
		"children" : [ {
			"id" : "c101251007",
			"name" : "安泽",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101251011",
			"name" : "大宁",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101251015",
			"name" : "汾西",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101251008",
			"name" : "浮山",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101251006",
			"name" : "古县",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101251005",
			"name" : "洪洞",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101251016",
			"name" : "侯马",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101251017",
			"name" : "霍州",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101251009",
			"name" : "吉县",
			"type" : 3,
			"idx" : 8,
			"children" : []
		}, {
			"id" : "c101251014",
			"name" : "蒲县",
			"type" : 3,
			"idx" : 9,
			"children" : []
		}, {
			"id" : "c101251002",
			"name" : "曲沃",
			"type" : 3,
			"idx" : 10,
			"children" : []
		}, {
			"id" : "c101251001",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 11,
			"children" : []
		}, {
			"id" : "c101251012",
			"name" : "隰县",
			"type" : 3,
			"idx" : 12,
			"children" : []
		}, {
			"id" : "c101251010",
			"name" : "乡宁",
			"type" : 3,
			"idx" : 13,
			"children" : []
		}, {
			"id" : "c101251004",
			"name" : "襄汾",
			"type" : 3,
			"idx" : 14,
			"children" : []
		}, {
			"id" : "c101251003",
			"name" : "翼城",
			"type" : 3,
			"idx" : 15,
			"children" : []
		}, {
			"id" : "c101251013",
			"name" : "永和",
			"type" : 3,
			"idx" : 16,
			"children" : []
		} ]
	}, {
		"id" : "c101251100",
		"name" : "吕梁",
		"type" : 2,
		"idx" : 6,
		"children" : [ {
			"id" : "c101251109",
			"name" : "方山",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101251113",
			"name" : "汾阳",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101251103",
			"name" : "交城",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101251111",
			"name" : "交口",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101251108",
			"name" : "岚县",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101251105",
			"name" : "临县",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101251106",
			"name" : "柳林",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101251107",
			"name" : "石楼",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101251101",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 8,
			"children" : []
		}, {
			"id" : "c101251102",
			"name" : "文水",
			"type" : 3,
			"idx" : 9,
			"children" : []
		}, {
			"id" : "c101251112",
			"name" : "孝义",
			"type" : 3,
			"idx" : 10,
			"children" : []
		}, {
			"id" : "c101251104",
			"name" : "兴县",
			"type" : 3,
			"idx" : 11,
			"children" : []
		}, {
			"id" : "c101251110",
			"name" : "中阳",
			"type" : 3,
			"idx" : 12,
			"children" : []
		} ]
	}, {
		"id" : "c101250600",
		"name" : "朔州",
		"type" : 2,
		"idx" : 7,
		"children" : [ {
			"id" : "c101250605",
			"name" : "怀仁",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101250602",
			"name" : "山阴",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101250601",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101250603",
			"name" : "应县",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101250604",
			"name" : "右玉",
			"type" : 3,
			"idx" : 4,
			"children" : []
		} ]
	}, {
		"id" : "c101250900",
		"name" : "忻州",
		"type" : 2,
		"idx" : 8,
		"children" : [ {
			"id" : "c101250912",
			"name" : "保德",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101250904",
			"name" : "代县",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101250902",
			"name" : "定襄",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101250905",
			"name" : "繁峙",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101250911",
			"name" : "河曲",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101250907",
			"name" : "静乐",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101250910",
			"name" : "岢岚",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101250906",
			"name" : "宁武",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101250913",
			"name" : "偏关",
			"type" : 3,
			"idx" : 8,
			"children" : []
		}, {
			"id" : "c101250908",
			"name" : "神池",
			"type" : 3,
			"idx" : 9,
			"children" : []
		}, {
			"id" : "c101250901",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 10,
			"children" : []
		}, {
			"id" : "c101250903",
			"name" : "五台",
			"type" : 3,
			"idx" : 11,
			"children" : []
		}, {
			"id" : "c101250909",
			"name" : "五寨",
			"type" : 3,
			"idx" : 12,
			"children" : []
		}, {
			"id" : "c101250914",
			"name" : "原平",
			"type" : 3,
			"idx" : 13,
			"children" : []
		} ]
	}, {
		"id" : "c101250300",
		"name" : "阳泉",
		"type" : 2,
		"idx" : 9,
		"children" : [ {
			"id" : "c101250302",
			"name" : "平定",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101250301",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101250303",
			"name" : "盂县",
			"type" : 3,
			"idx" : 2,
			"children" : []
		} ]
	}, {
		"id" : "c101250800",
		"name" : "运城",
		"type" : 2,
		"idx" : 10,
		"children" : [ {
			"id" : "c101250813",
			"name" : "河津",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101250805",
			"name" : "稷山",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101250807",
			"name" : "绛县",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101250802",
			"name" : "临猗",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101250810",
			"name" : "平陆",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101250811",
			"name" : "芮城",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101250801",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101250803",
			"name" : "万荣",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101250804",
			"name" : "闻喜",
			"type" : 3,
			"idx" : 8,
			"children" : []
		}, {
			"id" : "c101250809",
			"name" : "夏县",
			"type" : 3,
			"idx" : 9,
			"children" : []
		}, {
			"id" : "c101250806",
			"name" : "新绛",
			"type" : 3,
			"idx" : 10,
			"children" : []
		}, {
			"id" : "c101250812",
			"name" : "永济",
			"type" : 3,
			"idx" : 11,
			"children" : []
		}, {
			"id" : "c101250808",
			"name" : "垣曲",
			"type" : 3,
			"idx" : 12,
			"children" : []
		} ]
	} ]
}, {
	"id" : "c101260000",
	"name" : "陕西",
	"type" : 1,
	"idx" : 27,
	"city" : [ {
		"id" : "c101260100",
		"name" : "西安",
		"type" : 2,
		"idx" : 0,
		"children" : [ {
			"id" : "c101260104",
			"name" : "灞桥区",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101260102",
			"name" : "碑林区",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101260109",
			"name" : "长安区",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101260113",
			"name" : "高陵",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101260112",
			"name" : "户县",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101260110",
			"name" : "蓝田",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101260103",
			"name" : "莲湖区",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101260108",
			"name" : "临潼区",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101260105",
			"name" : "未央区",
			"type" : 3,
			"idx" : 8,
			"children" : []
		}, {
			"id" : "c101260101",
			"name" : "新城区",
			"type" : 3,
			"idx" : 9,
			"children" : []
		}, {
			"id" : "c101260107",
			"name" : "阎良区",
			"type" : 3,
			"idx" : 10,
			"children" : []
		}, {
			"id" : "c101260106",
			"name" : "雁塔区",
			"type" : 3,
			"idx" : 11,
			"children" : []
		}, {
			"id" : "c101260111",
			"name" : "周至",
			"type" : 3,
			"idx" : 12,
			"children" : []
		} ]
	}, {
		"id" : "c101260900",
		"name" : "安康",
		"type" : 2,
		"idx" : 1,
		"children" : [ {
			"id" : "c101260910",
			"name" : "白河",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101260902",
			"name" : "汉阴",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101260906",
			"name" : "岚皋",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101260904",
			"name" : "宁陕",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101260907",
			"name" : "平利",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101260903",
			"name" : "石泉",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101260901",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101260909",
			"name" : "旬阳",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101260908",
			"name" : "镇坪",
			"type" : 3,
			"idx" : 8,
			"children" : []
		}, {
			"id" : "c101260905",
			"name" : "紫阳",
			"type" : 3,
			"idx" : 9,
			"children" : []
		} ]
	}, {
		"id" : "c101260300",
		"name" : "宝鸡",
		"type" : 2,
		"idx" : 2,
		"children" : [ {
			"id" : "c101260309",
			"name" : "凤县",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101260302",
			"name" : "凤翔",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101260304",
			"name" : "扶风",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101260308",
			"name" : "麟游",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101260306",
			"name" : "陇县",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101260305",
			"name" : "眉县",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101260303",
			"name" : "岐山",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101260307",
			"name" : "千阳",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101260301",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 8,
			"children" : []
		}, {
			"id" : "c101260310",
			"name" : "太白",
			"type" : 3,
			"idx" : 9,
			"children" : []
		} ]
	}, {
		"id" : "c101260700",
		"name" : "汉中",
		"type" : 2,
		"idx" : 3,
		"children" : [ {
			"id" : "c101260703",
			"name" : "城固",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101260711",
			"name" : "佛坪",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101260710",
			"name" : "留坝",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101260708",
			"name" : "略阳",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101260706",
			"name" : "勉县",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101260702",
			"name" : "南郑",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101260707",
			"name" : "宁强",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101260701",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101260705",
			"name" : "西乡",
			"type" : 3,
			"idx" : 8,
			"children" : []
		}, {
			"id" : "c101260704",
			"name" : "洋县",
			"type" : 3,
			"idx" : 9,
			"children" : []
		}, {
			"id" : "c101260709",
			"name" : "镇巴",
			"type" : 3,
			"idx" : 10,
			"children" : []
		} ]
	}, {
		"id" : "c101261000",
		"name" : "商洛",
		"type" : 2,
		"idx" : 4,
		"children" : [ {
			"id" : "c101261003",
			"name" : "丹凤",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101261002",
			"name" : "洛南",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101261005",
			"name" : "山阳",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101261004",
			"name" : "商南",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101261001",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101261006",
			"name" : "镇安",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101261007",
			"name" : "柞水",
			"type" : 3,
			"idx" : 6,
			"children" : []
		} ]
	}, {
		"id" : "c101260200",
		"name" : "铜川",
		"type" : 2,
		"idx" : 5,
		"children" : [ {
			"id" : "c101260201",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101260202",
			"name" : "宜君",
			"type" : 3,
			"idx" : 1,
			"children" : []
		} ]
	}, {
		"id" : "c101260500",
		"name" : "渭南",
		"type" : 2,
		"idx" : 6,
		"children" : [ {
			"id" : "c101260508",
			"name" : "白水",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101260506",
			"name" : "澄城",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101260504",
			"name" : "大荔",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101260509",
			"name" : "富平",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101260510",
			"name" : "韩城",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101260505",
			"name" : "合阳",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101260502",
			"name" : "华县",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101260511",
			"name" : "华阴",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101260507",
			"name" : "蒲城",
			"type" : 3,
			"idx" : 8,
			"children" : []
		}, {
			"id" : "c101260501",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 9,
			"children" : []
		}, {
			"id" : "c101260503",
			"name" : "潼关",
			"type" : 3,
			"idx" : 10,
			"children" : []
		} ]
	}, {
		"id" : "c101260400",
		"name" : "咸阳",
		"type" : 2,
		"idx" : 7,
		"children" : [ {
			"id" : "c101260407",
			"name" : "彬县",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101260408",
			"name" : "长武",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101260410",
			"name" : "淳化",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101260403",
			"name" : "泾阳",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101260405",
			"name" : "礼泉",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101260404",
			"name" : "乾县",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101260402",
			"name" : "三原",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101260401",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101260411",
			"name" : "武功",
			"type" : 3,
			"idx" : 8,
			"children" : []
		}, {
			"id" : "c101260412",
			"name" : "兴平",
			"type" : 3,
			"idx" : 9,
			"children" : []
		}, {
			"id" : "c101260409",
			"name" : "旬邑",
			"type" : 3,
			"idx" : 10,
			"children" : []
		}, {
			"id" : "c101260406",
			"name" : "永寿",
			"type" : 3,
			"idx" : 11,
			"children" : []
		} ]
	}, {
		"id" : "c101260600",
		"name" : "延安",
		"type" : 2,
		"idx" : 8,
		"children" : [ {
			"id" : "c101260605",
			"name" : "安塞",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101260609",
			"name" : "富县",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101260608",
			"name" : "甘泉",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101260613",
			"name" : "黄陵",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101260612",
			"name" : "黄龙",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101260610",
			"name" : "洛川",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101260601",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101260607",
			"name" : "吴起",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101260602",
			"name" : "延长",
			"type" : 3,
			"idx" : 8,
			"children" : []
		}, {
			"id" : "c101260603",
			"name" : "延川",
			"type" : 3,
			"idx" : 9,
			"children" : []
		}, {
			"id" : "c101260611",
			"name" : "宜川",
			"type" : 3,
			"idx" : 10,
			"children" : []
		}, {
			"id" : "c101260606",
			"name" : "志丹",
			"type" : 3,
			"idx" : 11,
			"children" : []
		}, {
			"id" : "c101260604",
			"name" : "子长",
			"type" : 3,
			"idx" : 12,
			"children" : []
		} ]
	}, {
		"id" : "c101260800",
		"name" : "榆林",
		"type" : 2,
		"idx" : 9,
		"children" : [ {
			"id" : "c101260806",
			"name" : "定边",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101260803",
			"name" : "府谷",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101260804",
			"name" : "横山",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101260809",
			"name" : "佳县",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101260805",
			"name" : "靖边",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101260808",
			"name" : "米脂",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101260811",
			"name" : "清涧",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101260802",
			"name" : "神木",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101260801",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 8,
			"children" : []
		}, {
			"id" : "c101260807",
			"name" : "绥德",
			"type" : 3,
			"idx" : 9,
			"children" : []
		}, {
			"id" : "c101260810",
			"name" : "吴堡",
			"type" : 3,
			"idx" : 10,
			"children" : []
		}, {
			"id" : "c101260812",
			"name" : "子洲",
			"type" : 3,
			"idx" : 11,
			"children" : []
		} ]
	} ]
}, {
	"id" : "c101270000",
	"name" : "四川",
	"type" : 1,
	"idx" : 28,
	"city" : [ {
		"id" : "c101270100",
		"name" : "成都",
		"type" : 2,
		"idx" : 0,
		"children" : [ {
			"id" : "c101270105",
			"name" : "成华区",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101270119",
			"name" : "崇州",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101270113",
			"name" : "大邑",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101270116",
			"name" : "都江堰",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101270103",
			"name" : "金牛区",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101270110",
			"name" : "金堂",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101270101",
			"name" : "锦江区",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101270106",
			"name" : "龙泉驿区",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101270117",
			"name" : "彭州",
			"type" : 3,
			"idx" : 8,
			"children" : []
		}, {
			"id" : "c101270112",
			"name" : "郫县",
			"type" : 3,
			"idx" : 9,
			"children" : []
		}, {
			"id" : "c101270114",
			"name" : "蒲江",
			"type" : 3,
			"idx" : 10,
			"children" : []
		}, {
			"id" : "c101270107",
			"name" : "青白江区",
			"type" : 3,
			"idx" : 11,
			"children" : []
		}, {
			"id" : "c101270102",
			"name" : "青羊区",
			"type" : 3,
			"idx" : 12,
			"children" : []
		}, {
			"id" : "c101270118",
			"name" : "邛崃",
			"type" : 3,
			"idx" : 13,
			"children" : []
		}, {
			"id" : "c101270111",
			"name" : "双流",
			"type" : 3,
			"idx" : 14,
			"children" : []
		}, {
			"id" : "c101270109",
			"name" : "温江区",
			"type" : 3,
			"idx" : 15,
			"children" : []
		}, {
			"id" : "c101270104",
			"name" : "武侯区",
			"type" : 3,
			"idx" : 16,
			"children" : []
		}, {
			"id" : "c101270108",
			"name" : "新都区",
			"type" : 3,
			"idx" : 17,
			"children" : []
		}, {
			"id" : "c101270115",
			"name" : "新津",
			"type" : 3,
			"idx" : 18,
			"children" : []
		} ]
	}, {
		"id" : "c101271900",
		"name" : "阿坝",
		"type" : 2,
		"idx" : 1,
		"children" : [ {
			"id" : "c101271911",
			"name" : "阿坝县",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101271908",
			"name" : "黑水",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101271913",
			"name" : "红原",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101271906",
			"name" : "金川",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101271905",
			"name" : "九寨沟",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101271902",
			"name" : "理县",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101271909",
			"name" : "马尔康",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101271903",
			"name" : "茂县",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101271910",
			"name" : "壤塘",
			"type" : 3,
			"idx" : 8,
			"children" : []
		}, {
			"id" : "c101271912",
			"name" : "若尔盖",
			"type" : 3,
			"idx" : 9,
			"children" : []
		}, {
			"id" : "c101271904",
			"name" : "松潘",
			"type" : 3,
			"idx" : 10,
			"children" : []
		}, {
			"id" : "c101271901",
			"name" : "汶川",
			"type" : 3,
			"idx" : 11,
			"children" : []
		}, {
			"id" : "c101271907",
			"name" : "小金",
			"type" : 3,
			"idx" : 12,
			"children" : []
		} ]
	}, {
		"id" : "c101271700",
		"name" : "巴中",
		"type" : 2,
		"idx" : 2,
		"children" : [ {
			"id" : "c101271703",
			"name" : "南江",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101271704",
			"name" : "平昌",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101271701",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101271702",
			"name" : "通江",
			"type" : 3,
			"idx" : 3,
			"children" : []
		} ]
	}, {
		"id" : "c101271500",
		"name" : "达州",
		"type" : 2,
		"idx" : 3,
		"children" : [ {
			"id" : "c101271502",
			"name" : "达县",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101271505",
			"name" : "大竹",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101271504",
			"name" : "开江",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101271506",
			"name" : "渠县",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101271501",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101271507",
			"name" : "万源",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101271503",
			"name" : "宣汉",
			"type" : 3,
			"idx" : 6,
			"children" : []
		} ]
	}, {
		"id" : "c101270500",
		"name" : "德阳",
		"type" : 2,
		"idx" : 4,
		"children" : [ {
			"id" : "c101270504",
			"name" : "广汉",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101270503",
			"name" : "罗江",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101270506",
			"name" : "绵竹",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101270505",
			"name" : "什邡",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101270501",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101270502",
			"name" : "中江",
			"type" : 3,
			"idx" : 5,
			"children" : []
		} ]
	}, {
		"id" : "c101272000",
		"name" : "甘孜",
		"type" : 2,
		"idx" : 5,
		"children" : [ {
			"id" : "c101272015",
			"name" : "巴塘",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101272011",
			"name" : "白玉",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101272004",
			"name" : "丹巴",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101272006",
			"name" : "道孚",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101272017",
			"name" : "稻城",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101272018",
			"name" : "得荣",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101272010",
			"name" : "德格",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101272008",
			"name" : "甘孜县",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101272001",
			"name" : "九龙",
			"type" : 3,
			"idx" : 8,
			"children" : []
		}, {
			"id" : "c101272002",
			"name" : "康定",
			"type" : 3,
			"idx" : 9,
			"children" : []
		}, {
			"id" : "c101272014",
			"name" : "理塘",
			"type" : 3,
			"idx" : 10,
			"children" : []
		}, {
			"id" : "c101272003",
			"name" : "泸定",
			"type" : 3,
			"idx" : 11,
			"children" : []
		}, {
			"id" : "c101272007",
			"name" : "炉霍",
			"type" : 3,
			"idx" : 12,
			"children" : []
		}, {
			"id" : "c101272013",
			"name" : "色达",
			"type" : 3,
			"idx" : 13,
			"children" : []
		}, {
			"id" : "c101272012",
			"name" : "石渠",
			"type" : 3,
			"idx" : 14,
			"children" : []
		}, {
			"id" : "c101272016",
			"name" : "乡城",
			"type" : 3,
			"idx" : 15,
			"children" : []
		}, {
			"id" : "c101272009",
			"name" : "新龙",
			"type" : 3,
			"idx" : 16,
			"children" : []
		}, {
			"id" : "c101272005",
			"name" : "雅江",
			"type" : 3,
			"idx" : 17,
			"children" : []
		} ]
	}, {
		"id" : "c101271400",
		"name" : "广安",
		"type" : 2,
		"idx" : 6,
		"children" : [ {
			"id" : "c101271405",
			"name" : "华蓥",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101271404",
			"name" : "邻水",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101271401",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101271403",
			"name" : "武胜",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101271402",
			"name" : "岳池",
			"type" : 3,
			"idx" : 4,
			"children" : []
		} ]
	}, {
		"id" : "c101270700",
		"name" : "广元",
		"type" : 2,
		"idx" : 7,
		"children" : [ {
			"id" : "c101270705",
			"name" : "苍溪",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101270704",
			"name" : "剑阁",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101270703",
			"name" : "青川",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101270701",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101270702",
			"name" : "旺苍",
			"type" : 3,
			"idx" : 4,
			"children" : []
		} ]
	}, {
		"id" : "c101271000",
		"name" : "乐山",
		"type" : 2,
		"idx" : 8,
		"children" : [ {
			"id" : "c101271006",
			"name" : "峨边",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101271008",
			"name" : "峨眉山",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101271004",
			"name" : "夹江",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101271002",
			"name" : "犍为",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101271003",
			"name" : "井研",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101271007",
			"name" : "马边",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101271001",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 7,
			"children" : []
		} ]
	}, {
		"id" : "c101272100",
		"name" : "凉山",
		"type" : 2,
		"idx" : 9,
		"children" : [ {
			"id" : "c101272109",
			"name" : "布拖",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101272104",
			"name" : "德昌",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101272115",
			"name" : "甘洛",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101272106",
			"name" : "会东",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101272105",
			"name" : "会理",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101272110",
			"name" : "金阳",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101272117",
			"name" : "雷波",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101272116",
			"name" : "美姑",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101272113",
			"name" : "冕宁",
			"type" : 3,
			"idx" : 8,
			"children" : []
		}, {
			"id" : "c101272102",
			"name" : "木里",
			"type" : 3,
			"idx" : 9,
			"children" : []
		}, {
			"id" : "c101272107",
			"name" : "宁南",
			"type" : 3,
			"idx" : 10,
			"children" : []
		}, {
			"id" : "c101272108",
			"name" : "普格",
			"type" : 3,
			"idx" : 11,
			"children" : []
		}, {
			"id" : "c101272101",
			"name" : "西昌",
			"type" : 3,
			"idx" : 12,
			"children" : []
		}, {
			"id" : "c101272112",
			"name" : "喜德",
			"type" : 3,
			"idx" : 13,
			"children" : []
		}, {
			"id" : "c101272103",
			"name" : "盐源",
			"type" : 3,
			"idx" : 14,
			"children" : []
		}, {
			"id" : "c101272114",
			"name" : "越西",
			"type" : 3,
			"idx" : 15,
			"children" : []
		}, {
			"id" : "c101272111",
			"name" : "昭觉",
			"type" : 3,
			"idx" : 16,
			"children" : []
		} ]
	}, {
		"id" : "c101270400",
		"name" : "泸州",
		"type" : 2,
		"idx" : 10,
		"children" : [ {
			"id" : "c101270405",
			"name" : "古蔺",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101270403",
			"name" : "合江",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101270402",
			"name" : "泸县",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101270401",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101270404",
			"name" : "叙永",
			"type" : 3,
			"idx" : 4,
			"children" : []
		} ]
	}, {
		"id" : "c101271200",
		"name" : "眉山",
		"type" : 2,
		"idx" : 11,
		"children" : [ {
			"id" : "c101271205",
			"name" : "丹棱",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101271204",
			"name" : "洪雅",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101271203",
			"name" : "彭山",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101271206",
			"name" : "青神",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101271202",
			"name" : "仁寿",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101271201",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 5,
			"children" : []
		} ]
	}, {
		"id" : "c101270600",
		"name" : "绵阳",
		"type" : 2,
		"idx" : 12,
		"children" : [ {
			"id" : "c101270604",
			"name" : "安县",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101270606",
			"name" : "北川",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101270608",
			"name" : "江油",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101270607",
			"name" : "平武",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101270602",
			"name" : "三台",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101270601",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101270603",
			"name" : "盐亭",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101270605",
			"name" : "梓潼",
			"type" : 3,
			"idx" : 7,
			"children" : []
		} ]
	}, {
		"id" : "c101270900",
		"name" : "内江",
		"type" : 2,
		"idx" : 13,
		"children" : [ {
			"id" : "c101270904",
			"name" : "隆昌",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101270901",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101270902",
			"name" : "威远",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101270903",
			"name" : "资中",
			"type" : 3,
			"idx" : 3,
			"children" : []
		} ]
	}, {
		"id" : "c101271100",
		"name" : "南充",
		"type" : 2,
		"idx" : 14,
		"children" : [ {
			"id" : "c101271107",
			"name" : "阆中",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101271102",
			"name" : "南部",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101271104",
			"name" : "蓬安",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101271101",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101271106",
			"name" : "西充",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101271105",
			"name" : "仪陇",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101271103",
			"name" : "营山",
			"type" : 3,
			"idx" : 6,
			"children" : []
		} ]
	}, {
		"id" : "c101270300",
		"name" : "攀枝花",
		"type" : 2,
		"idx" : 15,
		"children" : [ {
			"id" : "c101270302",
			"name" : "米易",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101270301",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101270303",
			"name" : "盐边",
			"type" : 3,
			"idx" : 2,
			"children" : []
		} ]
	}, {
		"id" : "c101270800",
		"name" : "遂宁",
		"type" : 2,
		"idx" : 16,
		"children" : [ {
			"id" : "c101270804",
			"name" : "大英",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101270802",
			"name" : "蓬溪",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101270803",
			"name" : "射洪",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101270801",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 3,
			"children" : []
		} ]
	}, {
		"id" : "c101271600",
		"name" : "雅安",
		"type" : 2,
		"idx" : 17,
		"children" : [ {
			"id" : "c101271608",
			"name" : "宝兴",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101271604",
			"name" : "汉源",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101271607",
			"name" : "芦山",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101271602",
			"name" : "名山",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101271605",
			"name" : "石棉",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101271601",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101271606",
			"name" : "天全",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101271603",
			"name" : "荥经",
			"type" : 3,
			"idx" : 7,
			"children" : []
		} ]
	}, {
		"id" : "c101271300",
		"name" : "宜宾",
		"type" : 2,
		"idx" : 18,
		"children" : [ {
			"id" : "c101271304",
			"name" : "长宁",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101271305",
			"name" : "高县",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101271306",
			"name" : "珙县",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101271303",
			"name" : "江安",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101271307",
			"name" : "筠连",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101271309",
			"name" : "屏山",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101271301",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101271308",
			"name" : "兴文",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101271302",
			"name" : "宜宾县",
			"type" : 3,
			"idx" : 8,
			"children" : []
		} ]
	}, {
		"id" : "c101271800",
		"name" : "资阳",
		"type" : 2,
		"idx" : 19,
		"children" : [ {
			"id" : "c101271802",
			"name" : "安岳",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101271804",
			"name" : "简阳",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101271803",
			"name" : "乐至",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101271801",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 3,
			"children" : []
		} ]
	}, {
		"id" : "c101270200",
		"name" : "自贡",
		"type" : 2,
		"idx" : 20,
		"children" : [ {
			"id" : "c101270203",
			"name" : "富顺",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101270202",
			"name" : "荣县",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101270201",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 2,
			"children" : []
		} ]
	} ]
}, {
	"id" : "c101340000",
	"name" : "台湾",
	"type" : 1,
	"idx" : 29,
	"city" : [ {
		"id" : "c101340300",
		"name" : "高雄",
		"type" : 2,
		"idx" : 0,
		"children" : []
	}, {
		"id" : "c101340900",
		"name" : "花莲",
		"type" : 2,
		"idx" : 1,
		"children" : []
	}, {
		"id" : "c101341400",
		"name" : "基隆",
		"type" : 2,
		"idx" : 2,
		"children" : []
	}, {
		"id" : "c101341700",
		"name" : "嘉义",
		"type" : 2,
		"idx" : 3,
		"children" : []
	}, {
		"id" : "c101340700",
		"name" : "苗栗",
		"type" : 2,
		"idx" : 4,
		"children" : []
	}, {
		"id" : "c101341100",
		"name" : "南投",
		"type" : 2,
		"idx" : 5,
		"children" : []
	}, {
		"id" : "c101341800",
		"name" : "澎湖",
		"type" : 2,
		"idx" : 6,
		"children" : []
	}, {
		"id" : "c101340800",
		"name" : "屏东",
		"type" : 2,
		"idx" : 7,
		"children" : []
	}, {
		"id" : "c101340100",
		"name" : "台北",
		"type" : 2,
		"idx" : 8,
		"children" : []
	}, {
		"id" : "c101341300",
		"name" : "台东",
		"type" : 2,
		"idx" : 9,
		"children" : []
	}, {
		"id" : "c101341600",
		"name" : "台南",
		"type" : 2,
		"idx" : 10,
		"children" : []
	}, {
		"id" : "c101341500",
		"name" : "台中",
		"type" : 2,
		"idx" : 11,
		"children" : []
	}, {
		"id" : "c101340400",
		"name" : "桃园",
		"type" : 2,
		"idx" : 12,
		"children" : []
	}, {
		"id" : "c101340200",
		"name" : "新北",
		"type" : 2,
		"idx" : 13,
		"children" : []
	}, {
		"id" : "c101340500",
		"name" : "新竹",
		"type" : 2,
		"idx" : 14,
		"children" : []
	}, {
		"id" : "c101340600",
		"name" : "宜兰",
		"type" : 2,
		"idx" : 15,
		"children" : []
	}, {
		"id" : "c101341200",
		"name" : "云林",
		"type" : 2,
		"idx" : 16,
		"children" : []
	}, {
		"id" : "c101341000",
		"name" : "彰化",
		"type" : 2,
		"idx" : 17,
		"children" : []
	} ]
}, {
	"id" : "c101280000",
	"name" : "西藏",
	"type" : 1,
	"idx" : 30,
	"city" : [ {
		"id" : "c101280600",
		"name" : "阿里",
		"type" : 2,
		"idx" : 0,
		"children" : [ {
			"id" : "c101280607",
			"name" : "措勤",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101280603",
			"name" : "噶尔",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101280606",
			"name" : "改则",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101280605",
			"name" : "革吉",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101280601",
			"name" : "普兰",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101280604",
			"name" : "日土",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101280602",
			"name" : "札达",
			"type" : 3,
			"idx" : 6,
			"children" : []
		} ]
	}, {
		"id" : "c101280200",
		"name" : "昌都",
		"type" : 2,
		"idx" : 1,
		"children" : [ {
			"id" : "c101280207",
			"name" : "八宿",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101280211",
			"name" : "边坝",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101280206",
			"name" : "察雅",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101280201",
			"name" : "昌都县",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101280205",
			"name" : "丁青",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101280203",
			"name" : "贡觉",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101280202",
			"name" : "江达",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101280210",
			"name" : "洛隆",
			"type" : 3,
			"idx" : 8,
			"children" : []
		}, {
			"id" : "c101280209",
			"name" : "芒康",
			"type" : 3,
			"idx" : 9,
			"children" : []
		}, {
			"id" : "c101280208",
			"name" : "左贡",
			"type" : 3,
			"idx" : 10,
			"children" : []
		} ]
	}, {
		"id" : "c101280100",
		"name" : "拉萨",
		"type" : 2,
		"idx" : 2,
		"children" : [ {
			"id" : "c101280101",
			"name" : "城关区",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101280107",
			"name" : "达孜",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101280103",
			"name" : "当雄",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101280106",
			"name" : "堆龙德庆",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101280102",
			"name" : "林周",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101280108",
			"name" : "墨竹工卡",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101280104",
			"name" : "尼木",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101280105",
			"name" : "曲水",
			"type" : 3,
			"idx" : 7,
			"children" : []
		} ]
	}, {
		"id" : "c101280700",
		"name" : "林芝",
		"type" : 2,
		"idx" : 3,
		"children" : [ {
			"id" : "c101280705",
			"name" : "波密",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101280706",
			"name" : "察隅",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101280702",
			"name" : "工布江达",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101280707",
			"name" : "朗县",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101280701",
			"name" : "林芝县",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101280703",
			"name" : "米林",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101280704",
			"name" : "墨脱",
			"type" : 3,
			"idx" : 6,
			"children" : []
		} ]
	}, {
		"id" : "c101280500",
		"name" : "那曲",
		"type" : 2,
		"idx" : 4,
		"children" : [ {
			"id" : "c101280505",
			"name" : "安多",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101280509",
			"name" : "巴青",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101280508",
			"name" : "班戈",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101280503",
			"name" : "比如",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101280502",
			"name" : "嘉黎",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101280501",
			"name" : "那曲县",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101280510",
			"name" : "尼玛",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101280504",
			"name" : "聂荣",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101280506",
			"name" : "申扎",
			"type" : 3,
			"idx" : 8,
			"children" : []
		}, {
			"id" : "c101280507",
			"name" : "索县",
			"type" : 3,
			"idx" : 9,
			"children" : []
		} ]
	}, {
		"id" : "c101280400",
		"name" : "日喀则",
		"type" : 2,
		"idx" : 5,
		"children" : [ {
			"id" : "c101280407",
			"name" : "昂仁",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101280409",
			"name" : "白朗",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101280412",
			"name" : "定结",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101280404",
			"name" : "定日",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101280418",
			"name" : "岗巴",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101280415",
			"name" : "吉隆",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101280403",
			"name" : "江孜",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101280411",
			"name" : "康马",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101280406",
			"name" : "拉孜",
			"type" : 3,
			"idx" : 8,
			"children" : []
		}, {
			"id" : "c101280402",
			"name" : "南木林",
			"type" : 3,
			"idx" : 9,
			"children" : []
		}, {
			"id" : "c101280416",
			"name" : "聂拉木",
			"type" : 3,
			"idx" : 10,
			"children" : []
		}, {
			"id" : "c101280410",
			"name" : "仁布",
			"type" : 3,
			"idx" : 11,
			"children" : []
		}, {
			"id" : "c101280401",
			"name" : "日喀则市",
			"type" : 3,
			"idx" : 12,
			"children" : []
		}, {
			"id" : "c101280417",
			"name" : "萨嘎",
			"type" : 3,
			"idx" : 13,
			"children" : []
		}, {
			"id" : "c101280405",
			"name" : "萨迦",
			"type" : 3,
			"idx" : 14,
			"children" : []
		}, {
			"id" : "c101280408",
			"name" : "谢通门",
			"type" : 3,
			"idx" : 15,
			"children" : []
		}, {
			"id" : "c101280414",
			"name" : "亚东",
			"type" : 3,
			"idx" : 16,
			"children" : []
		}, {
			"id" : "c101280413",
			"name" : "仲巴",
			"type" : 3,
			"idx" : 17,
			"children" : []
		} ]
	}, {
		"id" : "c101280300",
		"name" : "山南",
		"type" : 2,
		"idx" : 6,
		"children" : [ {
			"id" : "c101280307",
			"name" : "措美",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101280311",
			"name" : "错那",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101280303",
			"name" : "贡嘎",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101280309",
			"name" : "加查",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101280312",
			"name" : "浪卡子",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101280310",
			"name" : "隆子",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101280308",
			"name" : "洛扎",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101280301",
			"name" : "乃东",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101280305",
			"name" : "琼结",
			"type" : 3,
			"idx" : 8,
			"children" : []
		}, {
			"id" : "c101280306",
			"name" : "曲松",
			"type" : 3,
			"idx" : 9,
			"children" : []
		}, {
			"id" : "c101280304",
			"name" : "桑日",
			"type" : 3,
			"idx" : 10,
			"children" : []
		}, {
			"id" : "c101280302",
			"name" : "扎囊",
			"type" : 3,
			"idx" : 11,
			"children" : []
		} ]
	} ]
}, {
	"id" : "c101330000",
	"name" : "香港",
	"type" : 1,
	"idx" : 31,
	"city" : [ {
		"id" : "c101330200",
		"name" : "九龙",
		"type" : 2,
		"idx" : 0,
		"children" : [ {
			"id" : "c101330205",
			"name" : "观塘区",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101330204",
			"name" : "黄大仙区",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101330203",
			"name" : "九龙城区",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101330202",
			"name" : "深水埗区",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101330201",
			"name" : "油尖旺区",
			"type" : 3,
			"idx" : 4,
			"children" : []
		} ]
	}, {
		"id" : "c101330100",
		"name" : "香港岛",
		"type" : 2,
		"idx" : 1,
		"children" : [ {
			"id" : "c101330103",
			"name" : "东区",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101330104",
			"name" : "南区",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101330102",
			"name" : "湾仔区",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101330101",
			"name" : "中西区",
			"type" : 3,
			"idx" : 3,
			"children" : []
		} ]
	}, {
		"id" : "c101330300",
		"name" : "新界",
		"type" : 2,
		"idx" : 2,
		"children" : [ {
			"id" : "c101330301",
			"name" : "北区",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101330302",
			"name" : "大埔区",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101330308",
			"name" : "葵青区",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101330309",
			"name" : "离岛区",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101330305",
			"name" : "荃湾区",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101330303",
			"name" : "沙田区",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101330306",
			"name" : "屯门区",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101330304",
			"name" : "西贡区",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101330307",
			"name" : "元朗区",
			"type" : 3,
			"idx" : 8,
			"children" : []
		} ]
	} ]
}, {
	"id" : "c101290000",
	"name" : "新疆",
	"type" : 1,
	"idx" : 32,
	"city" : [ {
		"id" : "c101290800",
		"name" : "阿克苏",
		"type" : 2,
		"idx" : 0,
		"children" : [ {
			"id" : "c101290801",
			"name" : "阿克苏市",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101290808",
			"name" : "阿瓦提",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101290806",
			"name" : "拜城",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101290809",
			"name" : "柯坪",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101290803",
			"name" : "库车",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101290804",
			"name" : "沙雅",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101290802",
			"name" : "温宿",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101290807",
			"name" : "乌什",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101290805",
			"name" : "新和",
			"type" : 3,
			"idx" : 8,
			"children" : []
		} ]
	}, {
		"id" : "c101291400",
		"name" : "阿勒泰",
		"type" : 2,
		"idx" : 1,
		"children" : [ {
			"id" : "c101291401",
			"name" : "阿勒泰市",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101291402",
			"name" : "布尔津",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101291404",
			"name" : "福海",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101291403",
			"name" : "富蕴",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101291405",
			"name" : "哈巴河",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101291407",
			"name" : "吉木乃",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101291406",
			"name" : "青河",
			"type" : 3,
			"idx" : 6,
			"children" : []
		} ]
	}, {
		"id" : "c101290700",
		"name" : "巴音郭楞",
		"type" : 2,
		"idx" : 2,
		"children" : [ {
			"id" : "c101290709",
			"name" : "博湖",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101290707",
			"name" : "和静",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101290708",
			"name" : "和硕",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101290701",
			"name" : "库尔勒",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101290702",
			"name" : "轮台",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101290705",
			"name" : "且末",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101290704",
			"name" : "若羌",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101290703",
			"name" : "尉犁",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101290706",
			"name" : "焉耆",
			"type" : 3,
			"idx" : 8,
			"children" : []
		} ]
	}, {
		"id" : "c101291600",
		"name" : "北屯",
		"type" : 2,
		"idx" : 3,
		"children" : []
	}, {
		"id" : "c101290600",
		"name" : "博尔塔拉",
		"type" : 2,
		"idx" : 4,
		"children" : [ {
			"id" : "c101290601",
			"name" : "博乐",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101290602",
			"name" : "精河",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101290603",
			"name" : "温泉",
			"type" : 3,
			"idx" : 2,
			"children" : []
		} ]
	}, {
		"id" : "c101290500",
		"name" : "昌吉",
		"type" : 2,
		"idx" : 5,
		"children" : [ {
			"id" : "c101290501",
			"name" : "昌吉市",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101290502",
			"name" : "阜康",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101290503",
			"name" : "呼图壁",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101290506",
			"name" : "吉木萨尔",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101290504",
			"name" : "玛纳斯",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101290507",
			"name" : "木垒",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101290505",
			"name" : "奇台",
			"type" : 3,
			"idx" : 6,
			"children" : []
		} ]
	}, {
		"id" : "c101290400",
		"name" : "哈密",
		"type" : 2,
		"idx" : 6,
		"children" : [ {
			"id" : "c101290402",
			"name" : "巴里坤",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101290401",
			"name" : "哈密市",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101290403",
			"name" : "伊吾",
			"type" : 3,
			"idx" : 2,
			"children" : []
		} ]
	}, {
		"id" : "c101291100",
		"name" : "和田",
		"type" : 2,
		"idx" : 7,
		"children" : [ {
			"id" : "c101291106",
			"name" : "策勒",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101291101",
			"name" : "和田市",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101291102",
			"name" : "和田县",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101291105",
			"name" : "洛浦",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101291108",
			"name" : "民丰",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101291103",
			"name" : "墨玉",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101291104",
			"name" : "皮山",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101291107",
			"name" : "于田",
			"type" : 3,
			"idx" : 7,
			"children" : []
		} ]
	}, {
		"id" : "c101291000",
		"name" : "喀什",
		"type" : 2,
		"idx" : 8,
		"children" : [ {
			"id" : "c101291011",
			"name" : "巴楚",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101291010",
			"name" : "伽师",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101291001",
			"name" : "喀什市",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101291008",
			"name" : "麦盖提",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101291006",
			"name" : "莎车",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101291002",
			"name" : "疏附",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101291003",
			"name" : "疏勒",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101291012",
			"name" : "塔什库尔干",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101291007",
			"name" : "叶城",
			"type" : 3,
			"idx" : 8,
			"children" : []
		}, {
			"id" : "c101291004",
			"name" : "英吉沙",
			"type" : 3,
			"idx" : 9,
			"children" : []
		}, {
			"id" : "c101291009",
			"name" : "岳普湖",
			"type" : 3,
			"idx" : 10,
			"children" : []
		}, {
			"id" : "c101291005",
			"name" : "泽普",
			"type" : 3,
			"idx" : 11,
			"children" : []
		} ]
	}, {
		"id" : "c101290200",
		"name" : "克拉玛依",
		"type" : 2,
		"idx" : 9,
		"children" : [ {
			"id" : "c101290201",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 0,
			"children" : []
		} ]
	}, {
		"id" : "c101290900",
		"name" : "克孜勒苏",
		"type" : 2,
		"idx" : 10,
		"children" : [ {
			"id" : "c101290903",
			"name" : "阿合奇",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101290902",
			"name" : "阿克陶",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101290901",
			"name" : "阿图什",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101290904",
			"name" : "乌恰",
			"type" : 3,
			"idx" : 3,
			"children" : []
		} ]
	}, {
		"id" : "c101291700",
		"name" : "米泉",
		"type" : 2,
		"idx" : 11,
		"children" : []
	}, {
		"id" : "c101291300",
		"name" : "塔城",
		"type" : 2,
		"idx" : 12,
		"children" : [ {
			"id" : "c101291303",
			"name" : "额敏",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101291307",
			"name" : "和布克赛尔",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101291304",
			"name" : "沙湾",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101291301",
			"name" : "塔城市",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101291305",
			"name" : "托里",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101291302",
			"name" : "乌苏",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101291306",
			"name" : "裕民",
			"type" : 3,
			"idx" : 6,
			"children" : []
		} ]
	}, {
		"id" : "c101290300",
		"name" : "吐鲁番",
		"type" : 2,
		"idx" : 13,
		"children" : [ {
			"id" : "c101290302",
			"name" : "鄯善",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101290301",
			"name" : "吐鲁番市",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101290303",
			"name" : "托克逊",
			"type" : 3,
			"idx" : 2,
			"children" : []
		} ]
	}, {
		"id" : "c101290100",
		"name" : "乌鲁木齐",
		"type" : 2,
		"idx" : 14,
		"children" : [ {
			"id" : "c101290106",
			"name" : "达坂城区",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101290107",
			"name" : "米东区",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101290102",
			"name" : "沙依巴克区",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101290104",
			"name" : "水磨沟区",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101290101",
			"name" : "天山区",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101290105",
			"name" : "头屯河区",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101290108",
			"name" : "乌鲁木齐县",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101290103",
			"name" : "新市区",
			"type" : 3,
			"idx" : 7,
			"children" : []
		} ]
	}, {
		"id" : "c101291200",
		"name" : "伊犁",
		"type" : 2,
		"idx" : 15,
		"children" : [ {
			"id" : "c101291204",
			"name" : "察布查尔锡伯",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101291206",
			"name" : "巩留",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101291205",
			"name" : "霍城",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101291202",
			"name" : "奎屯",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101291210",
			"name" : "尼勒克",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101291209",
			"name" : "特克斯",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101291207",
			"name" : "新源",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101291201",
			"name" : "伊宁市",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101291203",
			"name" : "伊宁县",
			"type" : 3,
			"idx" : 8,
			"children" : []
		}, {
			"id" : "c101291208",
			"name" : "昭苏",
			"type" : 3,
			"idx" : 9,
			"children" : []
		} ]
	}, {
		"id" : "c101291500",
		"name" : "自治区直辖县级",
		"type" : 2,
		"idx" : 16,
		"children" : [ {
			"id" : "c101291502",
			"name" : "阿拉尔",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101291501",
			"name" : "石河子",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101291503",
			"name" : "图木舒克",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101291504",
			"name" : "五家渠",
			"type" : 3,
			"idx" : 3,
			"children" : []
		} ]
	} ]
}, {
	"id" : "c101300000",
	"name" : "云南",
	"type" : 1,
	"idx" : 33,
	"city" : [ {
		"id" : "c101300100",
		"name" : "昆明",
		"type" : 2,
		"idx" : 0,
		"children" : [ {
			"id" : "c101300114",
			"name" : "安宁",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101300106",
			"name" : "呈贡区",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101300105",
			"name" : "东川区",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101300108",
			"name" : "富民",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101300103",
			"name" : "官渡区",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101300107",
			"name" : "晋宁",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101300112",
			"name" : "禄劝",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101300102",
			"name" : "盘龙区",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101300110",
			"name" : "石林",
			"type" : 3,
			"idx" : 8,
			"children" : []
		}, {
			"id" : "c101300111",
			"name" : "嵩明",
			"type" : 3,
			"idx" : 9,
			"children" : []
		}, {
			"id" : "c101300101",
			"name" : "五华区",
			"type" : 3,
			"idx" : 10,
			"children" : []
		}, {
			"id" : "c101300104",
			"name" : "西山区",
			"type" : 3,
			"idx" : 11,
			"children" : []
		}, {
			"id" : "c101300113",
			"name" : "寻甸",
			"type" : 3,
			"idx" : 12,
			"children" : []
		}, {
			"id" : "c101300109",
			"name" : "宜良",
			"type" : 3,
			"idx" : 13,
			"children" : []
		} ]
	}, {
		"id" : "c101300400",
		"name" : "保山",
		"type" : 2,
		"idx" : 1,
		"children" : [ {
			"id" : "c101300405",
			"name" : "昌宁",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101300404",
			"name" : "龙陵",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101300402",
			"name" : "施甸",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101300401",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101300403",
			"name" : "腾冲",
			"type" : 3,
			"idx" : 4,
			"children" : []
		} ]
	}, {
		"id" : "c101300900",
		"name" : "楚雄",
		"type" : 2,
		"idx" : 2,
		"children" : [ {
			"id" : "c101300901",
			"name" : "楚雄市",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101300906",
			"name" : "大姚",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101300910",
			"name" : "禄丰",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101300903",
			"name" : "牟定",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101300904",
			"name" : "南华",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101300902",
			"name" : "双柏",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101300909",
			"name" : "武定",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101300905",
			"name" : "姚安",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101300907",
			"name" : "永仁",
			"type" : 3,
			"idx" : 8,
			"children" : []
		}, {
			"id" : "c101300908",
			"name" : "元谋",
			"type" : 3,
			"idx" : 9,
			"children" : []
		} ]
	}, {
		"id" : "c101301300",
		"name" : "大理",
		"type" : 2,
		"idx" : 3,
		"children" : [ {
			"id" : "c101301304",
			"name" : "宾川",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101301301",
			"name" : "大理市",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101301310",
			"name" : "洱源",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101301312",
			"name" : "鹤庆",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101301311",
			"name" : "剑川",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101301305",
			"name" : "弥渡",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101301306",
			"name" : "南涧",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101301307",
			"name" : "巍山",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101301303",
			"name" : "祥云",
			"type" : 3,
			"idx" : 8,
			"children" : []
		}, {
			"id" : "c101301302",
			"name" : "漾濞",
			"type" : 3,
			"idx" : 9,
			"children" : []
		}, {
			"id" : "c101301308",
			"name" : "永平",
			"type" : 3,
			"idx" : 10,
			"children" : []
		}, {
			"id" : "c101301309",
			"name" : "云龙",
			"type" : 3,
			"idx" : 11,
			"children" : []
		} ]
	}, {
		"id" : "c101301400",
		"name" : "德宏",
		"type" : 2,
		"idx" : 4,
		"children" : [ {
			"id" : "c101301403",
			"name" : "梁河",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101301405",
			"name" : "陇川",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101301402",
			"name" : "芒市",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101301401",
			"name" : "瑞丽",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101301404",
			"name" : "盈江",
			"type" : 3,
			"idx" : 4,
			"children" : []
		} ]
	}, {
		"id" : "c101301600",
		"name" : "迪庆",
		"type" : 2,
		"idx" : 5,
		"children" : [ {
			"id" : "c101301602",
			"name" : "德钦",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101301603",
			"name" : "维西",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101301601",
			"name" : "香格里拉",
			"type" : 3,
			"idx" : 2,
			"children" : []
		} ]
	}, {
		"id" : "c101301000",
		"name" : "红河州",
		"type" : 2,
		"idx" : 6,
		"children" : [ {
			"id" : "c101301001",
			"name" : "个旧",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101301013",
			"name" : "河口",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101301010",
			"name" : "红河",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101301005",
			"name" : "建水",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101301002",
			"name" : "开远",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101301008",
			"name" : "泸西",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101301012",
			"name" : "绿春",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101301003",
			"name" : "蒙自",
			"type" : 3,
			"idx" : 8,
			"children" : []
		}, {
			"id" : "c101301007",
			"name" : "弥勒",
			"type" : 3,
			"idx" : 9,
			"children" : []
		}, {
			"id" : "c101301004",
			"name" : "屏边",
			"type" : 3,
			"idx" : 10,
			"children" : []
		}, {
			"id" : "c101301006",
			"name" : "石屏",
			"type" : 3,
			"idx" : 11,
			"children" : []
		}, {
			"id" : "c101301009",
			"name" : "元阳",
			"type" : 3,
			"idx" : 12,
			"children" : []
		} ]
	}, {
		"id" : "c101300600",
		"name" : "丽江",
		"type" : 2,
		"idx" : 7,
		"children" : [ {
			"id" : "c101300604",
			"name" : "华坪",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101300605",
			"name" : "宁蒗",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101300601",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101300603",
			"name" : "永胜",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101300602",
			"name" : "玉龙",
			"type" : 3,
			"idx" : 4,
			"children" : []
		} ]
	}, {
		"id" : "c101300800",
		"name" : "临沧",
		"type" : 2,
		"idx" : 8,
		"children" : [ {
			"id" : "c101300808",
			"name" : "沧源",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101300802",
			"name" : "凤庆",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101300807",
			"name" : "耿马",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101300801",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101300806",
			"name" : "双江",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101300804",
			"name" : "永德",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101300803",
			"name" : "云县",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101300805",
			"name" : "镇康",
			"type" : 3,
			"idx" : 7,
			"children" : []
		} ]
	}, {
		"id" : "c101301500",
		"name" : "怒江",
		"type" : 2,
		"idx" : 9,
		"children" : [ {
			"id" : "c101301502",
			"name" : "福贡",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101301503",
			"name" : "贡山",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101301504",
			"name" : "兰坪",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101301501",
			"name" : "泸水",
			"type" : 3,
			"idx" : 3,
			"children" : []
		} ]
	}, {
		"id" : "c101300700",
		"name" : "普洱",
		"type" : 2,
		"idx" : 10,
		"children" : [ {
			"id" : "c101300707",
			"name" : "江城",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101300704",
			"name" : "景东",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101300705",
			"name" : "景谷",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101300709",
			"name" : "澜沧",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101300708",
			"name" : "孟连",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101300703",
			"name" : "墨江",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101300702",
			"name" : "宁洱",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101300701",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101300710",
			"name" : "西盟",
			"type" : 3,
			"idx" : 8,
			"children" : []
		}, {
			"id" : "c101300706",
			"name" : "镇沅",
			"type" : 3,
			"idx" : 9,
			"children" : []
		} ]
	}, {
		"id" : "c101300200",
		"name" : "曲靖",
		"type" : 2,
		"idx" : 11,
		"children" : [ {
			"id" : "c101300206",
			"name" : "富源",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101300207",
			"name" : "会泽",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101300203",
			"name" : "陆良",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101300205",
			"name" : "罗平",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101300202",
			"name" : "马龙",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101300204",
			"name" : "师宗",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101300201",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101300209",
			"name" : "宣威",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101300208",
			"name" : "沾益",
			"type" : 3,
			"idx" : 8,
			"children" : []
		} ]
	}, {
		"id" : "c101301100",
		"name" : "文山",
		"type" : 2,
		"idx" : 12,
		"children" : [ {
			"id" : "c101301108",
			"name" : "富宁",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101301107",
			"name" : "广南",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101301104",
			"name" : "麻栗坡",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101301105",
			"name" : "马关",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101301106",
			"name" : "丘北",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101301101",
			"name" : "文山市",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101301103",
			"name" : "西畴",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101301102",
			"name" : "砚山",
			"type" : 3,
			"idx" : 7,
			"children" : []
		} ]
	}, {
		"id" : "c101301200",
		"name" : "西双版纳",
		"type" : 2,
		"idx" : 13,
		"children" : [ {
			"id" : "c101301201",
			"name" : "景洪",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101301202",
			"name" : "勐海",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101301203",
			"name" : "勐腊",
			"type" : 3,
			"idx" : 2,
			"children" : []
		} ]
	}, {
		"id" : "c101300300",
		"name" : "玉溪",
		"type" : 2,
		"idx" : 14,
		"children" : [ {
			"id" : "c101300303",
			"name" : "澄江",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101300307",
			"name" : "峨山",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101300305",
			"name" : "华宁",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101300302",
			"name" : "江川",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101300301",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101300304",
			"name" : "通海",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101300308",
			"name" : "新平",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101300306",
			"name" : "易门",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101300309",
			"name" : "元江",
			"type" : 3,
			"idx" : 8,
			"children" : []
		} ]
	}, {
		"id" : "c101300500",
		"name" : "昭通",
		"type" : 2,
		"idx" : 15,
		"children" : [ {
			"id" : "c101300505",
			"name" : "大关",
			"type" : 3,
			"idx" : 0,
			"children" : []
		}, {
			"id" : "c101300502",
			"name" : "鲁甸",
			"type" : 3,
			"idx" : 1,
			"children" : []
		}, {
			"id" : "c101300503",
			"name" : "巧家",
			"type" : 3,
			"idx" : 2,
			"children" : []
		}, {
			"id" : "c101300501",
			"name" : "市辖区",
			"type" : 3,
			"idx" : 3,
			"children" : []
		}, {
			"id" : "c101300511",
			"name" : "水富",
			"type" : 3,
			"idx" : 4,
			"children" : []
		}, {
			"id" : "c101300507",
			"name" : "绥江",
			"type" : 3,
			"idx" : 5,
			"children" : []
		}, {
			"id" : "c101300510",
			"name" : "威信",
			"type" : 3,
			"idx" : 6,
			"children" : []
		}, {
			"id" : "c101300504",
			"name" : "盐津",
			"type" : 3,
			"idx" : 7,
			"children" : []
		}, {
			"id" : "c101300509",
			"name" : "彝良",
			"type" : 3,
			"idx" : 8,
			"children" : []
		}, {
			"id" : "c101300506",
			"name" : "永善",
			"type" : 3,
			"idx" : 9,
			"children" : []
		}, {
			"id" : "c101300508",
			"name" : "镇雄",
			"type" : 3,
			"idx" : 10,
			"children" : []
		} ]
	} ]
} ];

function init() {
	var _country = document.getElementById("country");
	for ( var e in list) {
		var opt_1 = new Option(list[e].name, list[e].id);
		_country.add(opt_1);
	}
};

function toProvince() {
	var _country = document.getElementById("country");
	var _province = document.getElementById("city");
	var _city = document.getElementById("children");
	var v_country = _country.value;

	_province.options.length = 1;
	_city.options.length = 1;

	for ( var e in list) {
		if (list[e].id == v_country) {
			for ( var p in list[e].city) {
				var opt_2 = new Option(list[e].city[p].name, list[e].city[p].id);
				_province.add(opt_2);

			}
			break;
		}
	}
};

function toCity() {
	var _country = document.getElementById("country");
	var _province = document.getElementById("city");
	var _city = document.getElementById("children");

	var v_country = _country.value;
	var v_province = _province.value;

	// _province.options.length=1;
	_city.options.length = 1;

	for ( var e in list) {
		if (list[e].id == v_country) {
			for ( var p in list[e].city) {
				// alert(list[e].province[p].value);
				if (list[e].city[p].id == v_province) {
					// alert(list[e].province[p].value);
					for ( var cc in list[e].city[p].children) {
						var opt_3 = new Option(
								list[e].city[p].children[cc].name,
								list[e].city[p].children[cc].id);
						_city.add(opt_3);
					}

					return;
				}

			}
			break;
		}
	}
};

/* ##########################公用方法##begin############################ */

// 监听窗口大小变化
window.onresize = function() {
	setTimeout(domresize, 300);
};
// 改变表格和查询表单宽高
function domresize() {
	$('tt_District').datagrid('resize', {
		height : $("#body").height() - $('#search_areaDistrict').height() - 5,
		width : $("#body").width()
	});
	$('#search_areaDistrict').panel('resize', {
		width : $("#body").width()
	});
	$('#detailLoanDiv').dialog('resize', {
		height : $("#body").height() - 25,
		width : $("#body").width() - 30
	});
}

/* ##########################公用方法##end############################ */