<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script type="text/javascript">
	$(function() {
		
		$('#merchantAddForm').form({
			url : '${ctx}/merchant/add',
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
					parent.$.messager.alert('提示', result.msg, 'warning');
				}
			}
		});
		
		pubMethod.bind('usertype', 'usertype');
	});
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false" title="" style="overflow: hidden;padding: 3px;">
		<form id="merchantAddForm" method="post">
			<table class="grid">
				<tr>
					<td>商户名称</td>
					<td><input name="mchntName" type="text" placeholder="请输入商户名称" class="easyui-validatebox" data-options="required:true,validType:'maxLengthChar[128]'" value=""></td>
					<td>商户编号</td>
					<td><input name="mchntCode" type="text" class="easyui-validatebox" data-options="required:true,validType:['number','fixedLength[15]']" value="" maxlength="15"></td>
				</tr>
				<tr>
					<td>备注</td>
					<td colspan='3'><textarea name="remarks" data-options="validType:'maxLengthChar[256]'" class="textarea easyui-validatebox" /></td>
				</tr>
			</table>
		</form>
	</div>
</div>