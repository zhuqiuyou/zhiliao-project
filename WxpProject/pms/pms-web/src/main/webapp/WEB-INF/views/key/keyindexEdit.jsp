<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script type="text/javascript">
	$(function() {
		
		$('#pid').combotree({
			url : '${ctx}/keyversion/tree',
			parentField : 'pid',
			lines : true,
			required:true,
			panelHeight : 'auto',
			value:'${keyindex.versionId}'
		});
		
		$('#keyindexEditForm').form({
			url : '${ctx}/keyindex/edit',
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
				}
			}
		});
		pubMethod.bind('usertype', 'usertype');
	});
</script>
<div style="padding: 3px;">
	<form id="keyindexEditForm" method="post">
		<table class="grid">
			<tr>
				<input type="hidden" name="keyId" value="${keyindex.keyId}"/>
				<input name="lockVersion" type="hidden"  value="${keyindex.lockVersion}">
				<td>版本</td>
				<td><select id="pid" name="versionId" style="width: 140px; height: 29px;" disabled="disabled">
				</select></td>
				<td>索引名</td>
				<td><select name="keyName" class="easyui-combobox" data-options="width:140,height:29,editable:false,panelHeight:'auto'" readonly="readonly">
						<c:forEach items="${keyindextypeJson}" var="ktj">
							<option value="${ktj.code }" <c:if test="${ktj.code == keyindex.keyName}">selected="selected"</c:if>>${ktj.text }</option>
						</c:forEach>
					</select></td>
			</tr>
			<tr>
				<td>索引值</td>
				<td><input name="keyIndex"  class="easyui-validatebox"   value="${keyindex.keyIndex}"   data-options="required:true,validType:'fromOneToShousand'"    maxlength="3" style="width: 140px; height: 29px;" /></td>
				<td>备注</td>
				<td><textarea  name="remarks"  rows="3" cols="20" data-options="validType:'maxLengthChar[256]'" maxlength="256" class="textarea easyui-validatebox">${keyindex.remarks}</textarea></td>
			</tr>
		</table>
	</form>
</div>
