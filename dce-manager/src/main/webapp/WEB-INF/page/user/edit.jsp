<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>添加用户</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="Cache-Control" content="no-cache" />
<style type="text/css">
.center{
  text-align: center;
  margin-top:5px;
}
</style>
</head>
<body>
	<input type="hidden" id="change_level_userId" value="${user.id }"/>
	<table style="margin-left: 10px;margin-right: 10px;">
		<tr>
			<td>用户名:</td>
			<td><input id="edit_true_userName" class="easyui-validatebox" type="text" name="userName" readonly="readonly" value="${user.userName }" style="height:30px;"/></td>
		</tr>
		<tr>
			<td>真实姓名:</td>
			<td><input id="edit_true_name" class="easyui-validatebox" type="text" name="trueName"  value="${user.trueName }" style="height:30px;"/></td>
		</tr>
		<tr>
			<td>手机号码:</td>
			<td><input id="edit_user_mobile" class="easyui-validatebox" type="text" name="trueName"  value="${user.mobile }" style="height:30px;"/></td>
		</tr>
		<tr>
			<td>登录密码:</td>
			<td><input id="edit_user_login_password" class="easyui-validatebox" type="text" name="trueName"  value="${user.userPassword }" style="height:30px;"/></td>
		</tr>
		<tr>
			<td>支付密码:</td>
			<td><input id="edit_user_seconde_password" class="easyui-validatebox" type="text" name="trueName"  value="${user.twoPassword }" style="height:30px;"/></td>
		</tr>
		<tr>
			<td>用户级别:</td>
			<td>
				<select class="easyui-combobox" id="change_level" name="levelType" style="width:140px;" style="height:30px;">
		       		<option value="">--请选择用户级别--</option>
		       		<option value="1" <c:if test="${user.userLevel==1 }">selected="selected"</c:if> >F1</option>
		       		<option value="2" <c:if test="${user.userLevel==2 }">selected="selected"</c:if> >F2</option>
		       		<option value="3" <c:if test="${user.userLevel==3 }">selected="selected"</c:if> >F3</option>
		       		<option value="4" <c:if test="${user.userLevel==4 }">selected="selected"</c:if> >F4</option>
		       		<option value="5" <c:if test="${user.userLevel==5 }">selected="selected"</c:if> >F5</option>
		       		<option value="6" <c:if test="${user.userLevel==6 }">selected="selected"</c:if> >F6</option>
		       </select>
			</td>
		</tr>
		<tr>
			<td>设置是否空单:</td>
			<td>
				<select class="easyui-combobox" id="isBlankOrder" name="isBlankOrder" style="width:140px;" style="height:30px;">
		       		<option value="1" selected="selected" >空单</option>
		       		<option value="2"  >非空单</option>
		       </select>
			</td>
		</tr>
		<tr>
			<td colspan='2' style="word-wrap:break-word;word-break:break-all;"><font color="red">注：<br>1.当设置为空单时,不会对用户的业绩做修改。<br>2.当设置为非空单时,如果用户原先没有业绩则会产生业绩,如果用户原来有业绩,但等级发生了变化则业绩做相应等级的变化调整</font></td>
		</tr>
	</table>
    
</body>
</html>