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
<form id="editYsNoticeForm" method="post" action="<c:url value='ysnotice/saveYsNotice.html'/>"> 
		<div>
		
			<table width="100%" border="0" align="center" cellpadding="3">			  
					<input type="hidden" id="id" name="id" value="${ysnotice.id}"/>
					<tr>	
						<td align="right">
							<label for="name">noticeType</label>
						</td>	
						<td>
								<input type="text" id="noticeType" name="noticeType" value="${ysnotice.noticeType}"/>												
						</td>						   
					</tr>
					<tr>	
						<td align="right">
							<label for="name">title</label>
						</td>	
						<td>
								<input type="text" id="title" name="title" value="${ysnotice.title}"/>												
						</td>						   
					</tr>
					<tr>	
						<td align="right">
							<label for="name">image</label>
						</td>	
						<td>
								<input type="text" id="image" name="image" value="${ysnotice.image}"/>												
						</td>						   
					</tr>
					<tr>	
						<td align="right">
							<label for="name">content</label>
						</td>	
						<td>
								<input type="text" id="content" name="content" value="${ysnotice.content}"/>												
						</td>						   
					</tr>
					<tr>	
						<td align="right">
							<label for="name">author</label>
						</td>	
						<td>
								<input type="text" id="author" name="author" value="${ysnotice.author}"/>												
						</td>						   
					</tr>
					<tr>	
						<td align="right">
							<label for="name">topNotice</label>
						</td>	
						<td>
								<input type="text" id="topNotice" name="topNotice" value="${ysnotice.topNotice}"/>												
						</td>						   
					</tr>
					<tr>	
						<td align="right">
							<label for="name">remark</label>
						</td>	
						<td>
								<input type="text" id="remark" name="remark" value="${ysnotice.remark}"/>												
						</td>						   
					</tr>
					<tr>	
						<td align="right">
							<label for="name">status</label>
						</td>	
						<td>
								<input type="text" id="status" name="status" value="${ysnotice.status}"/>												
						</td>						   
					</tr>
					<tr>	
						<td align="right">
							<label for="name">createDate</label>
						</td>	
						<td>
								<input type="text" 
								id="createDate" 
								name="createDate" 
								value="<fmt:formatDate value="${ysnotice.createDate}" pattern="yyyy-MM-dd"/>"
								class="easyui-datebox" size="14" data-options="editable : true"  
								/>
						</td>						   
					</tr>
					<tr>	
						<td align="right">
							<label for="name">createName</label>
						</td>	
						<td>
								<input type="text" id="createName" name="createName" value="${ysnotice.createName}"/>												
						</td>						   
					</tr>
					<tr>	
						<td align="right">
							<label for="name">updateDate</label>
						</td>	
						<td>
								<input type="text" 
								id="updateDate" 
								name="updateDate" 
								value="<fmt:formatDate value="${ysnotice.updateDate}" pattern="yyyy-MM-dd"/>"
								class="easyui-datebox" size="14" data-options="editable : true"  
								/>
						</td>						   
					</tr>
					<tr>	
						<td align="right">
							<label for="name">updateName</label>
						</td>	
						<td>
								<input type="text" id="updateName" name="updateName" value="${ysnotice.updateName}"/>												
						</td>						   
					</tr>
		
				<tr>
					<td colspan="2">
						<input id="submitButton" name="submitButton" type="button" onclick="ysnotice_submit();"  value="提交" />	
					</td>
				<tr>			 
			</table>	   
		</div>	
	</form>
	<script type="text/javascript">
		
    function ysnotice_submit(){
    	$.ajax({ 
    			url: "<c:url value='/ysnotice/saveYsNotice.html'/>", 
    			data: $("#editYsNoticeForm").serialize(),
    			type:"post",
    			dataType:"json",
    			success: function(ret){
    	   	 		if(ret.code==="0"){
    	   	 			$.messager.confirm("保存成功",
    	   	 				           '是否继续添加？', 
    	   	 				           function(r){
						   	   			   if(r==false){
						   	   				$("#editYsNoticeDiv").dialog("close");
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