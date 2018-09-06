<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script type="text/javascript">
	$(function() {
		$('#sysParamAddForm').form({
			url : '${ctx}/systemparam/add',
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
	});

	$("#prmType").combobox({
		onChange: function(newShopId,o){
		}
	});
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false" title="" style="overflow: hidden;padding: 3px;">
		<form id="sysParamAddForm" method="post">
			<table class="grid">
				<tr>
					<input name="prmType" type="hidden"  value="10002">
					<td>参数名称</td>
					<td><input name="prmName" type="text"  value="卡BIN参数" readonly="readonly" /></td>
					<td>版本</td>
					<td><input name="prmVersion" type="text"  class="easyui-numberbox"  data-options="required:true"  validType='length[0,5]'></td>
				</tr>
				<tr>
					<td>参数值</td>
					<td><input name="prmVal" type="text"  class="easyui-validatebox" data-options="required:true,validType:['number','fixedLength[8]']" value=""  maxlength="8"></td>
					<td>长度</td>
					<td><input name="prmLen" type="text"  class="easyui-numberbox" value="8" readonly="readonly"></td>
				</tr>
				<tr>
					<td>描述</td>
					<td colspan="3"><textarea rows="3"  name="prmDesc"  style="width: 80%" validType='length[0,64]'/></td>
				</tr>
			</table>
		</form>
	</div>
</div>