<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>权限菜单</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="Cache-Control" content="no-cache" />
<jsp:include page="../common.jsp"></jsp:include>
</head>
<body class="easyui-layout" style="margin-left:10px;">
<input type="hidden" id="authId" value="${auth.id }"/>
    <%--<div style="margin:0px 0;"></div>--%>
    <c:set var="basePath" value="/dce-manager"></c:set>
    <div class="easyui-layout" style="width:1000px;height:360px;">
        <div data-options="region:'north'" style="height:50px;">
            <form id="searchForm">
                <table style="margin-top: 10px;margin-left: 10px;font-size: 13px">
                    <tr>
                        <td>菜单：</td>
                        <td>
                            <input id="filterName" style="height:25px" class="easyui-textbox" searcher="" prompt="请输入菜单名字" placeHolder="请输入菜单名字" menu="#mm" />
                        </td>
                        <td>链接：</td>
                        <td>
                            <input id="filterLinks" style="height:25px" class="easyui-textbox" searcher="" prompt="请输入衔接路径" placeHolder="请输入衔接地址" menu="#mm" />
                        </td>
                        <td>
                            <a  href="javascript:void(0);" id="searchButton" class="easyui-linkbutton" iconCls="icon-search" plain="true">查询</a>
                        </td>
                        <td>
                            <a  href="javascript:void(0);" id="resetButton" class="easyui-linkbutton" iconCls="icon-reset" plain="true" >重置</a>
                        </td>
                    </tr>
                </table>
            </form>
        </div>
        <div data-options="region:'south',split:true" style="height:20px;"></div>
        <div data-options="region:'east',split:true,iconCls:'icon-ok'" title="已加入菜单(双击可删除)" style="width:450px;">
          <table class="easyui-datagrid" id="auth_resource_hasAdd"
          pagination="true" striped="true"
            rownumbers="true" fitColumns="true"
                    data-options="url:'${basePath }/homemng/authority/resourcesInAuthorities.action?authorityId=${auth.id }&inOrNot=1&pageSize=10',method:'get',border:false,singleSelect:true,fit:true,fitColumns:true
                    ,onDblClickRow:removeResources">
                <thead>
                    <tr>
                        <th field="name" width="40">菜单</th>
                        <th field="module" width="50">模块</th>
                    </tr>
                </thead>
            </table>
        </div>
        <div data-options="region:'west',split:true" title="权限" style="width:100px;">
        ${auth.name }
        <div>
        ${auth.authDesc }
        </div>
        </div>
        <div data-options="region:'center',title:'菜单项（双击可添加到右边）'" >
            <table id="auth_resource_unAdd" class="easyui-datagrid"
            pagination="true"
            striped="true"
            rownumbers="true" fitColumns="true"
                    data-options="url:'${basePath }/homemng/authority/resourcesInAuthorities.action?authorityId=${auth.id }&inOrNot=2&pageSize=10',method:'get',border:false,singleSelect:true,fit:true,fitColumns:true
                    ,onDblClickRow:addResources">
                <thead>
                    <tr>
                        <th field="name" width="40">菜单</th>
                        <th field="resourceStr" width="140">链接</th>
                        <th field="module" width="50">模块</th>
                    </tr>
                </thead>
            </table>
        </div>
    </div>
    <script type="text/javascript">
    	var basePath="/dce-manager";
        /*查询和重置按钮事件*/
        $("#resetButton").on("click",function () {

            $("#searchForm").form('reset');
        });

        $("#searchForm #searchButton").on("click",function () {

            $("#auth_resource_unAdd").datagrid('reload',{
                'name':encodeURIComponent($("#searchForm #filterName").val()),
                'resourceStr':encodeURIComponent($("#searchForm #filterLinks").val()),
            });
        });

    function reloadTwoTables(){
    	$('#auth_resource_hasAdd').datagrid('reload');
    	$('#auth_resource_unAdd').datagrid('reload');
    }
    function addResources(index,row){
    	var authId=$("#authId").val();
    	$.messager.confirm("消息","确认在该权限下加入该菜单",function(r){
    		if(r){
    			$.ajax({
        			url:basePath + "/homemng/authority/addResource2Authority.action",
        			type:"post",
        			data:{authorityId:authId,resourceId:row.id},
        			success:function(ret){
        				if(ret.ret==1){
        					reloadTwoTables();
        				}else{
        					$.messager.alert("添加失败","添加失败，请稍后再试");
        				}
        			}
        		});
    		}
    	});
    }
    function removeResources(index,row){
    	var authId=$("#authId").val();
    	$.messager.confirm("消息","确认删除该权限下的菜单吗，删除后不可恢复",function(r){
    		if(r){
    			$.ajax({
        			url:basePath + "/homemng/authority/deleteResource2Authority.action",
        			type:"post",
        			data:{authorityId:authId,resourceId:row.id},
        			success:function(ret){
        				if(ret.ret==1){
        					reloadTwoTables();
        				}else{
        					$.messager.alert("添加失败","添加失败，请稍后再试");
        				}
        			}
        		});
    		}
    	});
    }
    </script>
</body>
</html>