<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"  prefix="fmt" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>编辑字典</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="Cache-Control" content="no-cache" />
<jsp:include page="../common_easyui_cus.jsp"></jsp:include>
</head>
<body>

<!-- 内容 -->
<form id="editApplyTravelForm" method="post" action="<c:url value='applytravel/saveApplyTravel.html'/>"> 
		<div>
		
			<table width="100%" border="0" align="center" cellpadding="3">			  
					<input type="hidden" id="id" name="id" value="${applytravel.id}"/>
					<tr>	
						<td align="right">
							<label for="name">userid</label>
						</td>	
						<td>
								<input type="text" id="userid" name="userid" value="${applytravel.userid}"/>												
						</td>						   
					</tr>
					<tr>	
						<td align="right">
							<label for="name">sex</label>
						</td>	
						<td>
								<input type="text" id="sex" name="sex" value="${applytravel.sex}"/>												
						</td>						   
					</tr>
					<tr>	
						<td align="right">
							<label for="name">nation</label>
						</td>	
						<td>
								<input type="text" id="nation" name="nation" value="${applytravel.nation}"/>												
						</td>						   
					</tr>
					<tr>	
						<td align="right">
							<label for="name">identity</label>
						</td>	
						<td>
								<input type="text" id="identity" name="identity" value="${applytravel.identity}"/>												
						</td>						   
					</tr>
					<tr>	
						<td align="right">
							<label for="name">phone</label>
						</td>	
						<td>
								<input type="text" id="phone" name="phone" value="${applytravel.phone}"/>												
						</td>						   
					</tr>
					<tr>	
						<td align="right">
							<label for="name">address</label>
						</td>	
						<td>
								<input type="text" id="address" name="address" value="${applytravel.address}"/>												
						</td>						   
					</tr>
					<tr>	
						<td align="right">
							<label for="name">isbeen</label>
						</td>	
						<td>
								<input type="text" id="isbeen" name="isbeen" value="${applytravel.isbeen}"/>												
						</td>						   
					</tr>
					<tr>	
						<td align="right">
							<label for="name">people</label>
						</td>	
						<td>
								<input type="text" id="people" name="people" value="${applytravel.people}"/>												
						</td>						   
					</tr>
					<tr>	
						<td align="right">
							<label for="name">pathid</label>
						</td>	
						<td>
								<input type="text" id="pathid" name="pathid" value="${applytravel.pathid}"/>												
						</td>						   
					</tr>
					<tr>	
						<td align="right">
							<label for="name">createtime</label>
						</td>	
						<td>
								<input type="text" 
								id="createtime" 
								name="createtime" 
								value="<fmt:formatDate value="${applytravel.createtime}" pattern="yyyy-MM-dd"/>"
								class="easyui-datebox" size="14" data-options="editable : true"  
								/>
						</td>						   
					</tr>
					<tr>	
						<td align="right">
							<label for="name">state</label>
						</td>	
						<td>
								<input type="text" id="state" name="state" value="${applytravel.state}"/>												
						</td>						   
					</tr>
		
				<tr>
					<td colspan="2">
						<input id="submitButton" name="submitButton" type="button" onclick="applytravel_submit();"  value="提交" />	
					</td>
				<tr>			 
			</table>	   
		</div>	
	</form>
	<script type="text/javascript">
		
    function applytravel_submit(){
    	$.ajax({ 
    			url: "<c:url value='/applytravel/saveApplyTravel.html'/>", 
    			data: $("#editApplyTravelForm").serialize(),
    			type:"post",
    			dataType:"json",
    			success: function(ret){
    	   	 		if(ret.code==="0"){
    	   	 			$.messager.confirm("保存成功",
    	   	 				           '是否继续添加？', 
    	   	 				           function(r){
						   	   			   if(r==false){
						   	   				$("#editApplyTravelDiv").dialog("close");
						   	   			   }
    	   						});
    	   	 		}else{
    	   	 			$.messager.alert("error",ret.msg);
    	   	 		}
    	      	}
    	        });
    }
	
</script>

</body>

</html>