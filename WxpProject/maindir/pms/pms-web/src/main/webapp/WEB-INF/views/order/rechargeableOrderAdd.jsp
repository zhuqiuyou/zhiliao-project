<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script type="text/javascript">
var cardListGrid ;
var isWaring = false;
	$(function() {
		$('#orderAddForm').form({
			url : '${ctx}/order/add',
			onSubmit : function() {
				$('#productName').val($('#productCode').combobox('getText'));
				var cardNoList = cardListGrid.datagrid("getRows");
				if( $('#cardListGrid').datagrid("getRows").length==0){
					parent.$.messager.alert('提示', "必须选择需要充值的卡号", 'warning');
					return false;
				}else{
					$('#cardNoList').val(JSON.stringify(cardNoList));  
				}
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
		cardListGrid = $('#cardListGrid').datagrid({
			striped : true,
			rownumbers : true,
			pagination : true,
			singleSelect : true,
			idField : 'cardNo',
			fitColumns:true,
			pageSize : 15,
			pageList : [ 10, 15,20, 30, 40, 50, 100, 200, 300, 400, 500 ],
			onLoadSuccess: function (data) {
				if (data.total == 0) {
					if (!isWaring) {
						isWaring = true;
						parent.$.messager.alert('提示', '请确认输入的产品/卡号信息是否正确', 'warning');
					}
				}
			},
			columns : [ [{
				width : '120',
				title : '卡号',
				field : 'cardNo'
			}]] 
		});
	});
	function searchCardFun() {
		isWaring = false;
		var productCode =$('#productCode').combobox('getValue');
		if(productCode && productCode!=''){
			 $('#productSearchCode').val( productCode);
			 $('#cardListGrid').datagrid( {	url : '${ctx}/order/cardDataGrid/200100', queryParams: $.serializeObject($('#searchCardForm'))});
		}
	}
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'north',border:false" title="" style="overflow: hidden;padding: 3px;">
		<form id="orderAddForm" method="post">
			<table class="grid">
				<tr>
					<td>产品名称</td>
					<td>
						<input id='productName'  name="productName" type="hidden"  value="">
						<input id='orderType'  name="orderType" type="hidden"  value="200100">
						<input id='cardNoList'  name="cardNoList" type="hidden"  value="">
						<select id='productCode' name="productCode" class="easyui-combobox" data-options="width:140,height:29,editable:false,panelHeight:'auto'">
							<c:forEach items="${productList}" var="ktj">
								<option value="${ktj.productCode}">${ktj.productName }</option>
							</c:forEach>
						</select>
					</td>
					<td>充值金额</td>
					<td><input id ="amount" name="amount" type="text"  class="easyui-numberbox" precision="2"  value="" data-options="required:true"  ></td>
				</tr>
			</table>
		</form>
	</div>
	<div id="searchForm" data-options="region:'center',border:false" style="height: 30px; overflow: hidden;background-color: #f4f4f4; padding-top: 15px" >
		<form id="searchCardForm">
			<input id = "productSearchCode" name="productSearchCode"  type="hidden" />
			<table>
				<tr>
					<th>卡号:</th>
					<td><input name="startCardNum" type="text"  style="width: 200px; " class="easyui-validatebox"  validType='length[19,19]''  invalidMessage='长度为19位''  maxlength="19" />至
							<input name="endCardNum" type="text"  style="width: 200px; " class="easyui-validatebox"  validType='length[19,19]''  invalidMessage='长度为19位''  maxlength="19" />
							<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon_search',plain:true" onclick="searchCardFun();">查询</a>
					</td>
				</tr>
			</table>
		</form>
	</div>
	<div id ="searchGrid" data-options="region:'south',border:false,title:'查询结果'" style="height: 80%; overflow: hidden;background-color: #f4f4f4">
		<table id="cardListGrid" data-options="fit:true,border:false"></table>
	</div>
</div>