<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script type="text/javascript">
	$(function() {
		$('#contractEditForm').form({
			url : '${ctx}/contract/edit',
			onSubmit : function() {
				var rows = $("#contractListEditGrid").datagrid('getData').rows;
				progressLoad();
				var isValid = $(this).form('validate');
				if (!isValid) {
					progressClose();
				}
					if(rows && rows.length>0){
						$('#merchantContractListStr').val(JSON.stringify(rows));  
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
	
	var contractListEditGrid;
	var 	array = [];
	if( ${contract.merchantContractListStr}&& ${contract.merchantContractListStr}!=''){
		array = ${contract.merchantContractListStr};
	}
	
	$(function() {
		contractListEditGrid = $('#contractListEditGrid').datagrid({
			striped : true,
			rownumbers : true,
			pagination : true,
			singleSelect : true,
			idField : 'id',
			fitColumns:true,
			pageSize : 50,
			pageList : [ 10, 20, 30, 40, 50, 100, 200, 300, 400, 500 ],
			frozenColumns : [[ {
				width : '100',
				title : '明细类型',
				field : 'contractType',
				sortable : true,
				formatter : function(value, row, index) {
					var jsonObjs = ${contractListTypeList};
					for(var i =0;i<jsonObjs.length;i++){
						var jsonobj = jsonObjs[i];
						if(jsonobj.key==value){
							return jsonobj.value;
						}
					}
					return "未知类型";
				}
			}] ],
			columns : [ [{
				width : '80',
				title : '卡产品',
				field : 'productCode',
				sortable : true,
				formatter : function(value, row, index) {
					var jsonObjs = $.parseJSON('${productList}');
					for(var i =0;i<jsonObjs.length;i++){
						var jsonobj = jsonObjs[i];
						if(jsonobj.productCode==value){
							return jsonobj.productName;
						}
					}
					return "未知类型";
				}
			},{
				width : '80',
				title : '开始日期',
				field : 'contractStartDate',
				sortable : true
			},{
				width : '80',
				title : '结束日期',
				field : 'contractEndDate',
				sortable : true
			},{
				width : '80',
				title : '费率(万分比)',
				field : 'contractRate',
				sortable : true
			},{
				width : '80',
				title : '状态',
				field : 'dataStat',
				sortable : true,
				formatter : function(value, row, index) {
					var jsonObjs = ${statelistList};
					for(var i =0;i<jsonObjs.length;i++){
						var jsonobj = jsonObjs[i];
						if(jsonobj.key==value){
							return jsonobj.value;
						}
					}
					if(!value || value==''){
						return "草稿";
					}
					return "未知类型";
				}
			}]]
		});
		if(array && array.length>0){
			 $('#contractListEditGrid').datagrid("loadData",{total:array.length,rows:array} ); 
		}
	});
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'north',border:false" title=""
		style="overflow: hidden;padding: 3px;">
		<form id="contractViewForm" method="post">
			<input type="hidden" id="merchantContractListStr"		name="merchantContractListStr" />
			<table class="grid">
				<tr>
					<td>商户名称</td>
					<td><select id="merchantInfId" name="merchantInfId" class="easyui-combobox"  readonly="readonly"
						data-options="width:140,height:29,editable:false,panelHeight:'auto'" value="${contract.merchantInfId}">
							<c:forEach items="${merchantInfJson}" var="ktj">
								<option value="${ktj.mchntId }" <c:if test="${ktj.mchntId == contract.merchantInfId}">selected="selected"</c:if>>${ktj.mchntName }</option>
							</c:forEach>
					</select></td>
					<td>合同号</td>
					<td><input id="id" name="id" type="text"	class="easyui-validatebox"  width="80%"
						readonly="readonly" value="${contract.id}"></td>
				</tr>
				<tr>
					<td>开始时间</td>
					<td><input id="contractStartDate" name="contractStartDate"	 type="text"  value="${contract.contractStartDate}"
						readonly="readonly" ></td>
					<td>结束时间</td>
					<td><input id="contractEndDate" name="contractEndDate" 	type="text"  value="${contract.contractEndDate}"
						readonly="readonly"></td>
				</tr>
				<tr>
					<td>结算周期</td>
					<td><select id="settleCycle" name="settleCycle"
						class="easyui-combobox" readonly="readonly"  value="${contract.settleCycle}"
						data-options="width:140,height:29,editable:false,panelHeight:'auto'">
							<c:forEach items="${contractSettleCycleList}" var="ktj">
								<option value="${ktj.code }" <c:if test="${ktj.code == contract.settleCycle}">selected="selected"</c:if>>${ktj.text }</option>
							</c:forEach>
					</select></td>
					<td>结算规律</td>
					<td><select id="settleType" name="settleType"
						class="easyui-combobox" readonly="readonly"  value="${contract.settleType}"
						data-options="width:140,height:29,editable:false,panelHeight:'auto'">
							<c:forEach items="${contractSettleTypeList}" var="ktj">
								<option value="${ktj.code }" <c:if test="${ktj.code == contract.settleType}">selected="selected"</c:if>>${ktj.text }</option>
							</c:forEach>
					</select></td>
				</tr>
			</table>
		</form>
	</div>
	<div data-options="region:'center',border:false,title:'合同明细'">
		<table id="contractListEditGrid" data-options="fit:true,border:false"></table>
	</div>
</div>