<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script type="text/javascript">
	$(function() {
		$('#sysParamEditForm').form({
			url : '${ctx}/systemparam/edit',
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
					parent.$.modalDialog.openner_dataGrid.datagrid('reload');
					parent.$.modalDialog.handler.dialog('close');
				} else {
					parent.$.messager.alert('错误', result.msg, 'error');
				}
			}
		});
	});
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false" title="" style="overflow: hidden;padding: 3px;">
		<form id="sysParamEditForm" method="post">
			<table class="grid">
				<tr>
					<td>参数名称</td>
					<td><input name="id" type="hidden"  value="${systemParam.id}">
					<input name="prmStat" type="hidden"  value="${systemParam.prmStat}">
					<input name="prmType"  type="hidden"  value="${systemParam.prmType}" />
					<input name="prmName" type="text" class="easyui-validatebox" data-options="required:true" value="${systemParam.prmName}" readonly="readonly"></td>
					<td>版本</td>
					<td><input id="prmVersion" name="prmVersion"  type="text"  class="easyui-numberbox"   value="${systemParam.prmVersion}"  readonly="readonly"/></td>
				</tr>
				<tr>
					<td>参数值</td>
					<td><input name="prmVal" type="text"  class="easyui-validatebox" data-options="required:true,validType:['number','fixedLength[8]']" value="${systemParam.prmVal}" maxlength="8"></td>
					<td>长度</td>
					<td><input id="prmLen" name="prmLen"  type="text"  class="easyui-numberbox"   value="${systemParam.prmLen}" style="width: 140px; height: 29px;" readonly="readonly"></td>
				</tr>
				<tr>
					<td>描述</td>
					<td colspan="3"><textarea rows="3"  name="prmDesc"  style="width: 80%"  validType='length[0,64]' >${systemParam.prmDesc}</textarea></td>
				</tr>
			</table>
		</form>
	</div>
</div>