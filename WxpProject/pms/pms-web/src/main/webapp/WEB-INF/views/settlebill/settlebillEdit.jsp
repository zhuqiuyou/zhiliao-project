<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script type="text/javascript">

$(function() {
		$('#settlebillEditForm').form({
			url : '${ctx}/settlebill/edit',
			onSubmit : function() {
				//$('#productName').val($("#productCode").find('option:selected').text());
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

</script>
<div class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'north',border:false" title="" style="overflow: hidden;padding: 3px;">
		<form id="settlebillEditForm" method="post">
			<input name="settleId" type="hidden"  value="${settlebill.settleId}">
			<input name="settleType" type="hidden"   value="${settlebill.settleType}">
			<input name="settleDateStart" type="hidden"   value="${settlebill.settleDateStart}">
			<input name="settleDateEnd" type="hidden"   value="${settlebill.settleDateEnd}">
			<input name="contractId" type="hidden"   value="${settlebill.contractId}">
			<input name="settleSrc" type="hidden"   value="${settlebill.settleSrc}">
			<input name="settleDes" type="hidden"   value="${settlebill.settleDes}">
			<input name="feeOffset" type="hidden"   value="${settlebill.feeOffset}">
			<input name="remark" type="hidden"   value="${settlebill.remark}">
			
			<input name="lockVersion" type="hidden"  value="${settlebill.lockVersion}">
			<table class="grid">
				<tr>
					<td>商户名称</td>
					<td><input  name="mchntName" type="text"  value="${settlebill.mchntName}" readonly="readonly">
					</td>
					<td>结算单状态</td>
					<td> 
						<select  name="billStat" class="easyui-combobox" data-options="width:140,height:29,editable:false,panelHeight:'auto'" readonly="readonly">
							<c:forEach items="${billStatList}" var="ojb">
								<option value="${ojb.code}"  <c:if test="${ojb.code == settlebill.billStat}">selected="selected"</c:if> >${ojb.text}</option>
							</c:forEach>
						</select>
					</td>
				</tr>
				<tr>
					<td>结算日期</td>
					<td>
						<input name="settleDate"  type="text"  value="${settlebill.settleDate}"  readonly="readonly">
					</td>
					<td>结算本金</td>
					<td>
						<input name="transAtSum"  type="text"  value="${settlebill.transAtSum}"  readonly="readonly">
					</td>
				</tr>
				
				<tr>
					<td>结算金额</td>
					<td>
						<input name="settleAtSum"  type="text"  value="${settlebill.settleAtSum}"  readonly="readonly">
					</td>
					<td>结算手续费</td>
					<td>
						<input name="settleFee"  type="text"  value="${settlebill.settleFee}"  class="easyui-numberbox" precision="2">
					</td>
				</tr>
				
			</table>
		</form>
	</div>
</div>