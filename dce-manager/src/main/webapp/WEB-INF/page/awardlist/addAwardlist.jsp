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
<form id="editAwardlistForm" method="post" action="<c:url value='awardlist/saveAwardlist.html'/>"> 
		<div>
		
			<table width="100%" border="0" align="center" cellpadding="3">			  
					<input type="hidden" id=" awardId" name=" awardId" value="${awardlist. awardId}"/>
					<tr>	
						<td align="right">
							<label for="name">buyerLecel</label>
						</td>	
						<td>
								<input type="text" id="buyerLecel" name="buyerLecel" value="${awardlist.buyerLecel}"/>												
						</td>						   
					</tr>
					<tr>	
						<td align="right">
							<label for="name">p1Level0</label>
						</td>	
						<td>
								<input type="text" id="p1Level0" name="p1Level0" value="${awardlist.p1Level0}"/>												
						</td>						   
					</tr>
					<tr>	
						<td align="right">
							<label for="name">p1Level1</label>
						</td>	
						<td>
								<input type="text" id="p1Level1" name="p1Level1" value="${awardlist.p1Level1}"/>												
						</td>						   
					</tr>
					<tr>	
						<td align="right">
							<label for="name">p1Level2</label>
						</td>	
						<td>
								<input type="text" id="p1Level2" name="p1Level2" value="${awardlist.p1Level2}"/>												
						</td>						   
					</tr>
					<tr>	
						<td align="right">
							<label for="name">p1Level3</label>
						</td>	
						<td>
								<input type="text" id="p1Level3" name="p1Level3" value="${awardlist.p1Level3}"/>												
						</td>						   
					</tr>
					<tr>	
						<td align="right">
							<label for="name">p1Level4</label>
						</td>	
						<td>
								<input type="text" id="p1Level4" name="p1Level4" value="${awardlist.p1Level4}"/>												
						</td>						   
					</tr>
					<tr>	
						<td align="right">
							<label for="name">p2Level0</label>
						</td>	
						<td>
								<input type="text" id="p2Level0" name="p2Level0" value="${awardlist.p2Level0}"/>												
						</td>						   
					</tr>
					<tr>	
						<td align="right">
							<label for="name">p2Level1</label>
						</td>	
						<td>
								<input type="text" id="p2Level1" name="p2Level1" value="${awardlist.p2Level1}"/>												
						</td>						   
					</tr>
					<tr>	
						<td align="right">
							<label for="name">p2Level2</label>
						</td>	
						<td>
								<input type="text" id="p2Level2" name="p2Level2" value="${awardlist.p2Level2}"/>												
						</td>						   
					</tr>
					<tr>	
						<td align="right">
							<label for="name">p2Level3</label>
						</td>	
						<td>
								<input type="text" id="p2Level3" name="p2Level3" value="${awardlist.p2Level3}"/>												
						</td>						   
					</tr>
					<tr>	
						<td align="right">
							<label for="name">p2Level4</label>
						</td>	
						<td>
								<input type="text" id="p2Level4" name="p2Level4" value="${awardlist.p2Level4}"/>												
						</td>						   
					</tr>
					<tr>	
						<td align="right">
							<label for="name">buyerAward</label>
						</td>	
						<td>
								<input type="text" id="buyerAward" name="buyerAward" value="${awardlist.buyerAward}"/>												
						</td>						   
					</tr>
					<tr>	
						<td align="right">
							<label for="name">buyQty</label>
						</td>	
						<td>
								<input type="text" id="buyQty" name="buyQty" value="${awardlist.buyQty}"/>												
						</td>						   
					</tr>
		
				<tr>
					<td colspan="2">
						<input id="submitButton" name="submitButton" type="button" onclick="awardlist_submit();"  value="提交" />	
					</td>
				<tr>			 
			</table>	   
		</div>	
	</form>
	<script type="text/javascript">
		
    function awardlist_submit(){
    	$.ajax({ 
    			url: "<c:url value='/awardlist/saveAwardlist.html'/>", 
    			data: $("#editAwardlistForm").serialize(),
    			type:"post",
    			dataType:"json",
    			success: function(ret){
    	   	 		if(ret.code==="0"){
    	   	 			$.messager.confirm("保存成功",
    	   	 				           '是否继续添加？', 
    	   	 				           function(r){
						   	   			   if(r==false){
						   	   				$("#editAwardlistDiv").dialog("close");
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