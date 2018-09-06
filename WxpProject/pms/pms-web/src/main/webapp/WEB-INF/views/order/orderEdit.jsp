<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script type="text/javascript">

$(function() {
		$('#orderEditForm').form({
			url : '${ctx}/order/edit',
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
$("#pinStat").combobox({
		onChange: function(value,o){
			nopintTxnAmt(value);
		},
		onBeforeLoad: function(value,o){
			var pinStat = '${order.pinStat}';
			$(this).combobox('setValue',pinStat);
		}
		
	});
	
function nopintTxnAmt(value) {
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
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'north',border:false" title="" style="overflow: hidden;padding: 3px;">
		<form id="orderEditForm" method="post">
			<input id='productCode'  name="productCode" type="hidden"  value="${order.productCode}" >
			<input id='id'  name="id" type="hidden"  value="${order.id}">
			<input id='lockVersion'  name="lockVersion" type="hidden"  value="${order.lockVersion}">
			<input id='orderStat'  name="orderStat" type="hidden"  value="${order.orderStat}">
			<input id='actStat'  name="actStat" type="hidden"  value="${order.actStat}">
			<input id='orderDate'  name="orderDate" type="hidden"  value="${order.orderDate}">
			<input name="orderType" type="hidden"   value="100100">
			<table class="grid">
				<tr>
					<td>产品名称</td>
					<td><input id='productName'  name="productName" type="text"  value="${order.productName}" readonly="readonly">
					</td>
					<td>张数</td>
					<td><input name="cardNum" type="text"  class="easyui-numberbox" data-options="required:true"  value="${order.cardNum}"/></td>
				</tr>
				<tr>
					<td>卡PIN状态</td>
					<td>
					<select id="pinStat" name="pinStat" class="easyui-combobox" data-options="required:true,width:140,height:29,editable:false,panelHeight:'auto'" >
						<c:forEach items="${cardPinStatList}" var="ojb">
							<option value="${ojb.key}"  <c:if test="${ojb.key == order.pinStat}">selected="selected"</c:if> >${ojb.value}</option>
						</c:forEach>
						 
					</select>
					</td>
					<td>无PIN限额</td>
					<td>
					<input name="nopintTxnAmt" id="nopintTxnAmt" type="text"  class="easyui-numberbox"  precision="2" data-options="required:true,validType:'maxLength[12]'" value='${order.nopintTxnAmt}' maxlength="12" invalidMessage="不能超过 12个数字.">
					</td>
				</tr>
			</table>
		</form>
	</div>
</div>