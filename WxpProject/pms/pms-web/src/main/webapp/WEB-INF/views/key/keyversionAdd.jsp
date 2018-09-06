<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script type="text/javascript">
	$(function() {
		
		$('#keyversionAddForm').form({
			url : '${ctx}/keyversion/add',
			onSubmit : function() {
				progressLoad();
				var isValid = $(this).form('validate');
				if (!isValid) {
					progressClose();
				}
				return isValid;
			},
			success : function(result) {
				progressClose();
				result = $.parseJSON(result);
				if (result.success) {
					parent.$.modalDialog.openner_dataGrid.datagrid('reload');//之所以能在这里调用到parent.$.modalDialog.openner_dataGrid这个对象，是因为user.jsp页面预定义好了
					parent.$.modalDialog.handler.dialog('close');
				} else {
					parent.$.messager.alert('提示', result.msg, 'warning');
				}
			}
		});
		
	});
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false" title="" style="overflow: hidden;padding: 3px;">
		<form id="keyversionAddForm" method="post">
			<table class="grid">
				<tr>
					<td>版本号</td>
					<td><input name="versionCode" type="text" placeholder="请输入版本号" class="easyui-validatebox" data-options="required:true,validType:['number','minLength[2]' ]" maxlength="2" invalidMessage="请输入2位数字" ></td>
					<td>类型</td>
					<td><select id = 'versionType' name="versionType" class="easyui-combobox" data-options="width:140,height:29,editable:false,panelHeight:'auto'">
						<c:forEach items="${keyTypelist}" var="keyTypelist">
							<option value="${keyTypelist.key}" >${keyTypelist.value}</option>
						</c:forEach>
					</select></td>
				</tr>
				<tr>
					<td>是否当前默认</td>
					<td><select name="dftStat" class="easyui-combobox" data-options="width:140,height:29,editable:false,panelHeight:'auto'">
						<c:forEach items="${commonDefaultlist}" var="commonDefaultlist">
							<option value="${commonDefaultlist.key}" >${commonDefaultlist.value}</option>
						</c:forEach>
					</select></td>
				</tr>
			</table>
		</form>
	</div>
</div>