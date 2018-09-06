<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script type="text/javascript">
	$(function() {
		
		$('#shopEditForm').form({
			url : '${ctx}/shop/edit',
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
	});
</script>
<div style="padding: 3px;">
	<form id="shopEditForm" method="post">
		<table class="grid">
			<tr>
				<input type="hidden" name="shopId" value="${shop.shopId}"/>
				<input name="lockVersion" type="hidden"  value="${shop.lockVersion}">
				<input name="dataStat" type="hidden"  value="${shop.dataStat}">
				<td>门店名称</td>
				<td><input name="shopName" type="text"  value="${shop.shopName}" class="easyui-validatebox" data-options="required:true,validType:'maxLengthChar[128]'" maxlength="128" style="width: 140px; height: 29px;" /></td>
				<td>门店编号</td>
				<td><input name="shopCode" type="text"  value="${shop.shopCode}" class="easyui-validatebox" data-options="required:true,validType:['number','maxLengthChar[8]']" maxlength="8" style="width: 140px; height: 29px;" /></td>
			</tr>
			<tr>
				<td>商户名称</td>
				<td><select name="mchntId" class="easyui-combobox" data-options="width:140,height:29,editable:false,panelHeight:'auto'">
						<c:forEach items="${merchantInfJson}" var="ktj">
							<option value="${ktj.mchntId }" <c:if test="${ktj.mchntId == shop.mchntId}">selected="selected"</c:if>>${ktj.mchntName }</option>
						</c:forEach>
					</select></td>
				<td>备注</td>
				<td><textarea  name="remarks" type="text"  data-options="validType:'maxLengthChar[256]'" class="textarea easyui-validatebox" rows="3" cols="20">${shop.remarks}</textarea></td>
			</tr>
		</table>
	</form>
</div>
