<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script type="text/javascript">
var contractListGrid ;
var 	array = [];
if( ${order.cardNoList}&& ${order.cardNoList}!=''){
	array = ${order.cardNoList};
}
$(function() {		
		contractListGrid = $('#contractListGrid').datagrid({
			striped : true,
			rownumbers : true,
			pagination : true,
			singleSelect : true,
			idField : 'cardNo',
			fitColumns:true,
			pageSize : 15,
			pageList : [ 10,15, 20, 30, 40, 50, 100, 200, 300, 400, 500 ],
			columns : [ [{
				width : '120',
				title : '卡号',
				field : 'cardNo'
			}]] 
		});
	});
if(array && array.length>0){
	 $('#contractListGrid').datagrid("loadData",{total:array.length,rows:array} ); 
}
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'north',border:false" title="" style="overflow: hidden;padding: 3px;">
		<form id="orderEditForm" method="post">
			<table class="grid">
				<tr>
					<td>产品名称</td>
					<td>
						<input id='cardNoList'  name="cardNoList" type="hidden"  value="">
						<select id='productCode' name="productCode" class="easyui-combobox"  data-options="width:140,height:29,editable:false,panelHeight:'auto'" readonly="readonly" >
							<c:forEach items="${productList}" var="ktj">
								<option value="${ktj.productCode}" <c:if test="${ktj.productCode == order.productCode}">selected="selected"</c:if>>${ktj.productName }</option>
							</c:forEach>
						</select>
					</td>
					<td>充值金额</td>
					<td><input id ="amount" name="amount" type="text"  class="easyui-numberbox"  precision="2" value="${order.amount}"  readonly="readonly" /></td>
				</tr>
			</table>
		</form>
	</div>

	<div id ="searchGrid" data-options="region:'center',border:false,title:'卡号列表'" style="height: 78%; overflow: hidden;background-color: #f4f4f4">
		<table id="contractListGrid" data-options="fit:true,border:false"></table>
	</div>
</div>