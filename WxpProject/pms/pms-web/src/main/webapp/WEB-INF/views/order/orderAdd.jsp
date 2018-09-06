<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script type="text/javascript">
	$(function() {
		$('#orderAddForm').form({
			url : '${ctx}/order/add',
			onSubmit : function() {
				$('#productName').val($("#productCode").combobox('getText'));
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
	$("#pinStat").combobox({
		onChange: function(value,o){
			if(value != "20"){
				$('#nopintTxnAmt').numberbox({
				    value:0,
				    editable:false
				});
			} else {
				$('#nopintTxnAmt').numberbox({
				    editable:true
				});
			}
		}
	});
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'north',border:false" title="" style="overflow: hidden;padding: 3px;">
		<form id="orderAddForm" method="post">
			<table class="grid">
				<tr>
					<td>产品名称</td>
					<td>
						<input id='productName'  name="productName" type="hidden"  value="">
						<input name="orderType" type="hidden" readonly="readonly"   value="100100">
						<select id='productCode' name="productCode" class="easyui-combobox" data-options="width:140,height:29,editable:false,panelHeight:'auto'">
							<c:forEach items="${productList}" var="ktj">
								<option value="${ktj.productCode}">${ktj.productName }</option>
							</c:forEach>
						</select>
					</td>
					<td>张数</td>
					<td><input name="cardNum" type="text"  class="easyui-numberbox" data-options="required:true" value=""></td>
				</tr>
				<tr>
					<td>卡PIN状态</td>
					<td>
					<select  id ="pinStat"  name="pinStat" class="easyui-combobox" data-options="required:true, width:140,height:29,editable:false,panelHeight:'auto'">
						<c:forEach items="${cardPinStatList}" var="ojb">
							<option ></option>
							<option value="${ojb.key}">${ojb.value}</option>
						</c:forEach>
					</select>
					</td>
					<td>无PIN限额</td>
					<td>
					<input name="nopintTxnAmt" id="nopintTxnAmt" type="text"  class="easyui-numberbox" precision="2" data-options="required:true,validType:['maxLength[12]']" value='0' maxlength="12" invalidMessage="请输入不超过12位的数字." >
					</td>
				</tr>
			</table>
		</form>
	</div>
</div>