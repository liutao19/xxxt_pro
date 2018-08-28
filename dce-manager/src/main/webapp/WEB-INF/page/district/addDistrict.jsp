
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>编辑字典</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="Cache-Control" content="no-cache" />
<jsp:include page="../common_easyui_cus.jsp"></jsp:include>
</head>



<body  >
	<!-- 内容 -->
	<form id="editDistrictForm" method="post" style="padding-top: 105px;
    padding-left: 100px;"
		action="<c:url value='district/saveDistrict.html'/>">
		<div >

			<table width="100%" border="0" align="center" cellpadding="3">
				<input type="hidden" id="districtId" name="districtId"
					value="${district.districtId}" />
				<tr>
					<td align="right"><label for="name">区域名称:</label></td>
					<td><select id="country" onclick="toProvince();">
							<option value="-1">--请选择省份--</option>
					</select> <select id="city" onclick="toCity();">
							<option value="-1">--请选择市---</option>
					</select> <select id="children">
							<option value="-1">--请选择区---</option>
					</select></td>
				</tr>
				<%-- <tr>	
						<td align="right">
							<label for="name">用户id</label>
						</td>	
						<td>
								<input type="text" id="userId" name="userId" value="${district.userId}"/>												
						</td>						   
					</tr>
					<tr>	
						<td align="right">
							<label for="name">区域状态</label>
						</td>	
						<td>
								<input type="text" id="districtStatus" name="districtStatus" value="${district.districtStatus}"/>												
						</td>						   
					</tr> --%>

				<!-- <tr>
					<td colspan="2">
						<input id="submitButton" name="submitButton" type="button" onclick="district_submit();"  value="提交" />	
					</td>
				<tr> -->
			</table>
		</div>
	</form>
	<script type="text/javascript">
	
	$().ready(function(){
		init();
	})
	
	
		function district_submit() {
			alert($("#editDistrictForm").serialize());
			$.ajax({
				url : "<c:url value='/district/saveDistrict.html'/>",
				data : $("#editDistrictForm").serialize(),
				type : "post",
				dataType : "json",
				success : function(ret) {
					if (ret.code === "0") {
						$.messager.confirm("保存成功", '是否继续添加？', function(r) {
							if (r == false) {
								$("#editDistrictDiv").dialog("close");
							}
						});
					} else {
						$.messager.alert("error", ret.msg);
					}
				}
			});
		};
	</script>






</body>

</html>