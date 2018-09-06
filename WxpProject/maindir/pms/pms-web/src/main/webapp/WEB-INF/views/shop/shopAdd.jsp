<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script type="text/javascript">
	$(function() {
		$('#pid').combotree({
			url : '${ctx}/merchant/tree',
			parentField : 'pid',
			lines : true,
			required:true,
			panelHeight : 'auto'
		});
		
		$('#shopAddForm').form({
			url : '${ctx}/shop/add',
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
					parent.$.modalDialog.openner_dataGrid.datagrid('reload');//
					parent.$.modalDialog.handler.dialog('close');
				}else{
					parent.$.messager.alert('提示', result.msg, 'warning');
				}
			}
		});
		
	});
</script>
<div style="padding: 3px;">
	<form id="shopAddForm" method="post">
		<table class="grid">
				<tr>
					<td>门店名称</td>
					<td><input name="shopName" type="text" placeholder="请输入门店名称" class="easyui-validatebox" data-options="required:true,validType:'maxLengthChar[128]'" maxlength="128" value=""></td>
					<td>门店编号</td>
					<td><input name="shopCode" type="text" class="easyui-validatebox" data-options="required:true,validType:['number','maxLengthChar[8]']" maxlength="8" value=""></td>
				</tr>
				<tr>
					<td>商户名称</td>
					<td><select name="mchntId" class="easyui-combobox" data-options="width:140,height:29,editable:false,panelHeight:'auto'">
						<c:forEach items="${merchantInfJson}" var="ktj">
							<option value="${ktj.mchntId }" <c:if test="${ktj.mchntId == dftMchant}">selected="selected"</c:if>>${ktj.mchntName }</option>
						</c:forEach>
					</select></td>
					<td>备注</td>
					<td ><textarea name="remarks"  data-options="validType:'maxLengthChar[256]'" class="textarea easyui-validatebox"/></td>
				</tr>
			</table>
	</form>
</div>