<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script type="text/javascript">
	$(function() {
		$('#keyversionEditForm').form({
			url : '${ctx}/keyversion/edit',
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
					parent.$.messager.alert('错误', result.msg, 'error');
				}
			}
		});
		
		pubMethod.bind('usertype', 'usertype');
	});
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false" title="" style="overflow: hidden;padding: 3px;">
		<form id="keyversionEditForm" method="post">
			<table class="grid">
				<tr>
					<input name="versionId" type="hidden"  value="${keyversion.versionId}">
					<input name="lockVersion" type="hidden"  value="${keyversion.lockVersion}">
					<td>版本号</td>
					<td>
					<input name="versionCode" type="text"  value="${keyversion.versionCode}" readonly="readonly"/>
					</td>
					<td>类型</td>
					<td><select name="versionType" class="easyui-combobox" data-options="width:140,height:29,editable:false,panelHeight:'auto'" readonly="readonly">
						<c:forEach items="${keyTypelist}" var="keyTypelist">
							<option value="${keyTypelist.key}" <c:if test="${keyTypelist.key == keyversion.versionType}">selected="selected"</c:if>>${keyTypelist.value}</option>
						</c:forEach>
					</select></td>
				</tr>
				<tr>
					<td>是否当前默认</td>
					<td><select name="dftStat" class="easyui-combobox" data-options="width:140,height:29,editable:false,panelHeight:'auto'">
						<c:forEach items="${commonDefaultlist}" var="commonDefaultlist">
							<option value="${commonDefaultlist.key}" <c:if test="${commonDefaultlist.key == keyversion.dftStat}">selected="selected"</c:if>>${commonDefaultlist.value}</option>
						</c:forEach>
					</select></td>
				</tr>
			</table>
		</form>
	</div>
</div>