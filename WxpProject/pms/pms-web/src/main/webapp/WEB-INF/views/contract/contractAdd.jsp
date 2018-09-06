<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<script type="text/javascript">
	$(function() {
		$('#contractAddForm').form({
			url : '${ctx}/contract/add',
			onSubmit : function() {
				progressLoad();
				var isValid = $(this).form('validate');
				
				var currentDate= getCurrentDate ();
				var contractStartDate = $('#contractStartDate').val();
				var contractEndDate = $('#contractEndDate').val();
				if (isValid) {
					
					if (contractStartDate < currentDate) {
						parent.$.messager.alert('提示',"开始时间不能小于当前时间", 'warning');
						isValid = false;
					}
				}
				if (isValid) {
					
					if (contractStartDate > contractEndDate) {
						parent.$.messager.alert('提示',"开始时间不能大于结束时间", 'warning');
						isValid = false;
					}
				}
				
				if (isValid) {
					var rows = contractListGrid.datagrid("getRows");
					for(var i=0;i<rows.length;i++){
						if(rows[i].contractStartDate < $('#contractStartDate').val()) {
							parent.$.messager.alert('提示',"合同明细开始时间应不能小于合同开始时间", 'warning');
							isValid = false;
							break;
						}
						if(rows[i].contractEndDate > $('#contractEndDate').val()) {
							parent.$.messager.alert('提示',"合同明细结束时间不能大于合同结束时间", 'warning');
							isValid = false;
							break;
						}
					}
				}
				
				if (!isValid) {
					progressClose();
				}else{
					var contractList = contractListGrid.datagrid("getRows");
					$('#merchantContractListStr').val(JSON.stringify(contractList));  
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
	
	var contractListGrid;
	$(function() {
		contractListGrid = $('#contractListGrid').datagrid({
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
	});
	function deleteContractListFun(){
		var selectedRow = contractListGrid.datagrid('getSelected');  //获取选中行  
		 var idx = contractListGrid.datagrid('getRowIndex',selectedRow);//获取某行的行号
		 if(selectedRow && selectedRow.dataStat && selectedRow.dataStat!=''){
/* 			 selectedRow.dataStat='1';
			 contractListGrid.datagrid('updateRow',{ index: idx,    row: { selectedRow } }); */
		 }else{
			 contractListGrid.datagrid('deleteRow',idx); //通过行号移除该行
		 }
	}
	function addContractListFun(){
			 var windowStatus = "dialogWidth:500px;dialogHeight:300px;center:1;status:0;resizable:0";  
			 var url = "${ctx}/contract/addListPage";  
			 var temp = window.showModalDialog(url,{'contractListGrid':contractListGrid},windowStatus);  
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
		<form id="contractAddForm" method="post">
			<input type="hidden" id="merchantContractListStr"
				name="merchantContractListStr" />
			<table class="grid">
				<tr>
					<td>商户信息</td>
					<td><select id="merchantInfId" name="merchantInfId"
						class="easyui-combobox"
						data-options="width:180,height:29,editable:false,panelHeight:'auto'">
							<c:forEach items="${merchantInfJson}" var="ktj">
								<option value="${ktj.mchntId }">${ktj.mchntName }-${ktj.mchntCode }</option>
							</c:forEach>
					</select></td>
					<td>合同号</td>
					<td><input id="id" name="id" type="text"
						class="easyui-validatebox" value="${id}" width="80%"
						readonly="readonly"></td>
				</tr>
				<tr>
					<td>开始时间</td>
					<td><input id="contractStartDate" name="contractStartDate"
						type="text"
						class="easyui-validatebox"
						data-options="required:true"
						onclick="WdatePicker({readOnly:true,dateFmt:'yyyyMMdd'})"
						readonly="readonly"></td>
					<td>结束时间</td>
					<td><input id="contractEndDate" name="contractEndDate"
						type="text"
						class="easyui-validatebox"
						data-options="required:true"
						onclick="WdatePicker({readOnly:true,dateFmt:'yyyyMMdd'})"
						readonly="readonly"></td>
				</tr>
				<tr>
					<td>结算周期</td>
					<td><select id="settleCycle" name="settleCycle"
						class="easyui-combobox"
						data-options="width:140,height:29,editable:false,panelHeight:'auto'">
							<c:forEach items="${contractSettleCycleList}" var="ktj">
								<option value="${ktj.code }">${ktj.text }</option>
							</c:forEach>
					</select></td>
					<td>结算规律</td>
					<td><select id="settleType" name="settleType"
						class="easyui-combobox"
						data-options="width:140,height:29,editable:false,panelHeight:'auto'">
							<c:forEach items="${contractSettleTypeList}" var="ktj">
								<option value="${ktj.code }">${ktj.text }</option>
							</c:forEach>
					</select></td>
				</tr>
			</table>
		</form>
	</div>
	<div data-options="region:'center',border:false,title:'合同明细'">
		<table id="contractListGrid" data-options="fit:true,border:false"></table>
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