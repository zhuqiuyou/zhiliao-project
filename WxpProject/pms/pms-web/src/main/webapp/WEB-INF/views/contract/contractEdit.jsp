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
				
				var currentDate= getCurrentDate ();
				if (isValid) {
					var contractEndDate = $('#contractEndDate').val();
					if (currentDate > contractEndDate) {
						parent.$.messager.alert('提示',"结束时间不能小于当前时间", 'warning');
						isValid = false;
					}
				}
				
				if (isValid) {
					var contractStartDate = $('#contractStartDate').val();
					var contractEndDate = $('#contractEndDate').val();
					if (currentDate == contractEndDate && currentDate < contractStartDate) {
						$('#contractStartDate').val(currentDate);
						contractStartDate = currentDate;
					}
					if (contractStartDate > contractEndDate) {
						parent.$.messager.alert('提示',"开始时间不能大于结束时间", 'warning');
						isValid = false;
					}
				}
				
				var changeListEndDate = false;
				if (isValid) {
					var rows = contractListEditGrid.datagrid("getRows");
					for(var i=0;i<rows.length;i++){
						
						/* if (rows[i].dataStat && rows[i].dataStat =='0') {
						
						} else {
							if (!rows[i].dataStat) {
								if( rows[i].contractStartDate < $('#contractStartDate').val()) {
									parent.$.messager.alert('提示',"新增合同明细开始时间应不小于合同开始时间", 'warning');
									isValid = false;
									break;
								}
							}
						} */
						if (!rows[i].dataStat) {
							if( rows[i].contractStartDate < $('#contractStartDate').val()) {
								parent.$.messager.alert('提示',"新增合同明细开始时间应不小于合同开始时间", 'warning');
								isValid = false;
								break;
							}
						}
						if(rows[i].contractEndDate > $('#contractEndDate').val()) {
							changeListEndDate = true;
						}
					}
				}
				if (!isValid) {
					progressClose();
					return isValid;
				}
				if (changeListEndDate) {
					 if(confirm("合同明细的结束时间晚于合同的结束时间，将会被默认为合同的结束时间")) {
							var rows = contractListEditGrid.datagrid("getRows");
							for(var i=0;i<rows.length;i++){
								//获取每一行的数据
								if(rows[i].contractEndDate > $('#contractEndDate').val()) {
									//parent.$.messager.alert('提示',"合同结束时间应不小于合同明细结束时间", 'warning');
									//isValid = false;
									//break;
									rows[i].contractEndDate = $('#contractEndDate').val();
									if (rows[i].contractEndDate < rows[i].contractStartDate) {
										rows[i].contractStartDate = rows[i].contractEndDate;
									}
								}
							}
							if(rows && rows.length>0){
								$('#merchantContractListStr').val(JSON.stringify(rows));  
							}
							return isValid;
						 } else {
						 	isValid = false;
						 	if (!isValid) {
								progressClose();
							}
						 	return isValid;
						 }
				} else {
					if(rows && rows.length>0){
						$('#merchantContractListStr').val(JSON.stringify(rows));  
					}
					return isValid;
				}
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
	var array = [];
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
			}]] ,
			toolbar : '#contractListToolbar'
		});
		if(array && array.length>0){
			 $('#contractListEditGrid').datagrid("loadData",{total:array.length,rows:array} ); 
		}
	});
	function deleteContractListFun(){
		var selectedRow = contractListEditGrid.datagrid('getSelected');  //获取选中行  
		 if(selectedRow && selectedRow.dataStat && selectedRow.dataStat =='0'){
		 	 parent.$.messager.confirm('询问', '是否删除该明细？', function(b) {
				if (b) {
					var idx = contractListEditGrid.datagrid('getRowIndex',selectedRow);//获取某行的行号
					contractListEditGrid.datagrid('updateRow',{ index: idx,    row: { dataStat: '1'}});
				 }
			 });
			 
		 } else if (selectedRow && !selectedRow.dataStat) {
	 	 	 parent.$.messager.confirm('询问', '是否删除该明细？', function(b) {
				if (b) {
					var idx = contractListEditGrid.datagrid('getRowIndex',selectedRow);//获取某行的行号
		 			contractListEditGrid.datagrid('deleteRow',idx); //通过行号移除该行
				 }
			 });
		 } else if (selectedRow && selectedRow.dataStat && selectedRow.dataStat=='1') {
		 	parent.$.messager.alert('提示', '请不要重复操作', 'warning');
		 } else {
		 	parent.$.messager.alert('提示', '请选择要删除的行', 'warning');
		 }
	}
	function addContractListFun(){
		 var windowStatus = "dialogWidth:500px;dialogHeight:300px;center:1;status:0;resizable:0";  
		 var url = "${ctx}/contract/addListPage";  
		 var temp = window.showModalDialog(url,{'contractListGrid':contractListEditGrid},windowStatus);  
	}
	
	function getCurrentDate () {
	
		var date = new Date();
		var month = date.getMonth() + 1;
		
		if (month < 10){
	 		month = "0" +month;
	 	}
	 	var day = date.getDate();
		
		if (day < 10){
	 		day = "0" +day;
	 	}
		return "" + date.getFullYear() +  month + day;
	}
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'north',border:false" title=""
		style="overflow: hidden;padding: 3px;">
		<form id="contractEditForm" method="post">
			<input type="hidden" id="merchantContractListStr"		name="merchantContractListStr" />
			<input type="hidden" id="lockVersion"		name="${contract.lockVersion}" />
			<input type="hidden" id="dataStat"		name="${contract.dataStat}" />
			<table class="grid">
				<tr>
					<td>商户信息</td>
					<td><select id="merchantInfId" name="merchantInfId" class="easyui-combobox"  readonly="readonly"
						data-options="width:180,height:29,editable:false,panelHeight:'auto'" value="${contract.merchantInfId}">
							<c:forEach items="${merchantInfJson}" var="ktj">
								<option value="${ktj.mchntId }" <c:if test="${ktj.mchntId == contract.merchantInfId}">selected="selected"</c:if>>${ktj.mchntName }-${ktj.mchntCode }</option>
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
					<td><input id="contractEndDate" name="contractEndDate" 	
						type="text"
						class="easyui-validatebox"
						data-options="required:true"
						value="${contract.contractEndDate}"
						onclick="WdatePicker({readOnly:true,dateFmt:'yyyyMMdd'})"
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
	<div id="contractListToolbar" style="display: none;">
		<a onclick="addContractListFun();" href="javascript:void(0);"
			class="easyui-linkbutton"
			data-options="plain:true,iconCls:'icon_add'">添加</a> <a
			onclick="deleteContractListFun();" href="javascript:void(0);"
			class="easyui-linkbutton"
			data-options="plain:true,iconCls:'icon_add'">删除</a>
	</div>
</div>