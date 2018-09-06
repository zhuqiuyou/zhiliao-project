<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<title>合同明细</title>
<meta http-equiv="Expires" CONTENT="0">
<meta http-equiv="Cache-Control" CONTENT="no-cache">
<meta http-equiv="Pragma" CONTENT="no-cache"> 
<jsp:include page="../inc.jsp"></jsp:include>
<script type="text/javascript" src="${ctx}/jslib/easyui1.4/jquery.easyui.patch.js" charset="utf-8"></script>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script type="text/javascript">
	var contractListGrid=window.dialogArguments.contractListGrid;
	
	function addContractListFun(){
		var contractType =$('#contractType').combobox('getValue');
		var productCode=$('#productCode').combobox('getValue');
		var contractStartDate=$('#contractStartDate').val();
		var contractEndDate=$('#contractEndDate').val();
		var contractRate=$('#contractRate').val();
		if (!contractStartDate) {
			alert("开始时间不能为空");
			return;
		}
		if (!contractEndDate) {
			alert("结束时间不能为空");
			return;
		}
		if (contractStartDate > contractEndDate) {
			alert("开始时间不能大于结束时间");
			return;
		}
		
		var currentDate= getCurrentDate ();
		if (currentDate > contractEndDate) {
			alert("结束时间不能小于当前时间");
			return;
		}
		
		var rows = contractListGrid.datagrid("getRows");
		if (rows) {
			for (var i=0;i<rows.length;i++) {
				if ((rows[i].dataStat && rows[i].dataStat == '0' || !rows[i].dataStat) 
					&& contractStartDate <= rows[i].contractEndDate 
					&& contractEndDate >= rows[i].contractStartDate) {
					alert("合同明细时间不能重复");
					return;
				}
			}
		}
		contractListGrid.datagrid('insertRow',{
			index: 0,
			row: {'contractType':contractType,'productCode':productCode,'contractStartDate':contractStartDate,
				'contractEndDate':contractEndDate,'contractRate':contractRate
			}   
		});  
		
		winClose();
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
	
	function winClose()	{
		window.close();
	} 
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false" title="合同明细" style="overflow: hidden;padding: 3px;">
		<form id="contractListAddForm" method="post">
			<table class="grid">
				<tr>
					<td>明细类型</td>
					<td>	<select id='contractType' name="contractType" class="easyui-combobox" data-options="width:140,height:29,editable:false,panelHeight:'auto'">
							<c:forEach items="${contractListTypeList}" var="ktj">
								<option value="${ktj.code}">${ktj.text }</option>
							</c:forEach>
						</select>
					</td>
					<td>卡产品</td>
					<td >
						<select id='productCode' name="productCode" class="easyui-combobox" data-options="width:140,height:29,editable:false,panelHeight:'auto'">
							<c:forEach items="${productList}" var="ktj">
								<option value="${ktj.productCode}">${ktj.productName }</option>
							</c:forEach>
						</select>
					</td>
				</tr>
				<tr>
					<td>开始时间</td>
					<td>	
						<input  id="contractStartDate" name="contractStartDate"
						readonly="readonly"  
						class="easyui-validatebox"
						data-options="required:true"
						onclick="WdatePicker({readOnly:false,dateFmt:'yyyyMMdd'})"/>
					</td>
					<td>截止时间</td>
					<td >
						<input id="contractEndDate" name="contractEndDate"  
						readonly="readonly"  
						class="easyui-validatebox"
						data-options="required:true"
						onclick="WdatePicker({readOnly:false,dateFmt:'yyyyMMdd'})" />
					</td>
				</tr>
				<tr>
					<td>手续费</td>
					<td><input  id="contractRate" name="contractRate" type="text"  class="easyui-numberbox"  min="0" max="10000"  value="0" >万分比</td>
				</tr>
				<tr >
					<td colspan="4">
						<input id="save" onclick="addContractListFun()" type="button" value="保存" name="save">
						<input id="close" onclick="winClose()" type="button" value="关闭" name="close">
					</td>
				</tr>
			</table>
		</form>
	</div>
</div>