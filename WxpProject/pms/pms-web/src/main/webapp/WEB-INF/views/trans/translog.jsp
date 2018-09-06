<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="../inc.jsp"></jsp:include>
<meta http-equiv="X-UA-Compatible" content="edge" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>交易明细查询</title>
	<script type="text/javascript">
	var dataGrid;
	$(function() {
	
		var isCurrent = $('#isCurrent').combobox('getValue');
		if (isCurrent && isCurrent == '0') {
			$('#settleTimeStart').val(getCurrentDate());
			$('#settleTimeEnd').val(getCurrentDate());
		}
		
		dataGrid = $('#dataGrid').datagrid({
			url : '${ctx}/translog/dataGrid',
			striped : true,
			rownumbers : true,
			pagination : true,
			singleSelect : true,
			fitColumns:true,
			idField : 'transId',
			sortName : 'CREATE_TIME',
			sortOrder : 'desc',
			pageSize : 15,
			pageList : [ 10, 15, 20, 30, 40, 50, 100, 200, 300, 400, 500 ],
			frozenColumns : [[ {
				width : '160',
				title : '交易流水号',
				field : 'txnPrimaryKey',
				sortable : true				
			}] ],
			columns : [ [{
				width : '160',
				title : '交易参考号',
				field : 'dmsRelatedKey'
			}, {
				width : '80',
				title : '清算日',
				field : 'settleDate'
			},{
				width : '160',
				title : '卡号',
				field : 'cardNo',
				sortable : true
			}, {
				width : '160',
				title : '账户号',
				field : 'priAcctNo',
				sortable : true
			}, {
				width : '100',
				title : '商户名称',
				field : 'mchntName',
				sortable : true
			}, {
				width : '100',
				title : '门店名称',
				field : 'shopName',
				sortable : true
			}, {
				width : '80',
				title : '终端号',
				field : 'termCode'
			}, {
				width : '160',
				title : '交易类型',
				field : 'transId',
				sortable : true,
				formatter : function(value, row, index) {
					 var jsonObjs = ${tranTypeList};
					for(var i =0;i<jsonObjs.length;i++){
						var jsonobj = jsonObjs[i];
						if(jsonobj.transId==value){
							return jsonobj.transName;
						}
					} 
					return "未知类型";
				}
			}, {
				width : '80',
				title : '交易金额',
				field : 'transAmt'
			},{
				width : '80',
				title : '交易状态',
				field : 'respCode',
				sortable : true,
				formatter : function(value, row, index) {
					if (value && value == '00') 
						return "交易成功";
					return "交易失败";
				}
			}, {
				width : '120',
				title : '交易时间',
				field : 'createTime',
				sortable : true
			}]] ,
			toolbar : '#toolbar'
		});
		
		searchFun();
		
		$("#isCurrent").combobox({
			onChange: function(value,o){
				if(value == "0"){
					$('#condition').hide();
				} else {
					$('#settleTimeStart').val('');
					$('#settleTimeEnd').val('');
					$('#condition').show();
				}
			},
			onBeforeLoad: function(value,o){
				$(this).combobox('setValue','0');
			}
		});
	});
	
	function exportFun(){
	
		if (!checkData()) {
			return;
		}
		var param = '?mchntCode=' + $('#mchntCode').val();
		param += '&shopCode=' + $('#shopCode').val();
		param += '&termCode=' + $('#termCode').val();
		param += '&cardNo=' + $('#cardNo').val();
		param += '&transId=' + $('#transId').val();
		param += '&isCurrent=' + $('#isCurrent').combobox('getValue');
		param += '&settleTimeStart=' + $('#settleTimeStart').val();
		param += '&settleTimeEnd=' + $('#settleTimeEnd').val();
		window.location='${ctx}/translog/exportLog'+param;
	}
	
	function searchFun() {
		
		if (checkData()) {
		
			dataGrid.datagrid('load', $.serializeObject($('#searchForm')));
		}
	}
	
	function checkData() {
		var isCurrent = $('#isCurrent').combobox('getValue');
		if (isCurrent && isCurrent == '0') {
			$('#settleTimeStart').val(getCurrentDate());
			$('#settleTimeEnd').val(getCurrentDate());
		} 
		
		var settleTimeStart = $('#settleTimeStart').val();
		var settleTimeEnd = $('#settleTimeEnd').val();
		if (!settleTimeStart || !settleTimeEnd) {
			$.messager.alert('提示', '开始日期与结束日期不能为空', 'warning');
			return false;
		}
		var isCurrent = $('#isCurrent').combobox('getValue');
		if (isCurrent && isCurrent == '0') {
			//if (settleTimeStart != getCurrentDate() || settleTimeEnd != getCurrentDate()) {
			//	$.messager.alert('提示', '查询当天记录时，开始日期与结束日期应设置为当天时间', 'warning');
			//	return;
			//}
		} else {
			if (settleTimeStart > settleTimeEnd) {
				$.messager.alert('提示', '开始日期不能大于结束日期', 'warning');
				return false;
			} else if (getCurrentDate () <= settleTimeEnd) {
				$.messager.alert('提示', '结束日期应小于当前日期', 'warning');
				return false;
			}
		}
		return true;
	}
	
	function cleanFun() {
		$('#searchForm input').val('');
		dataGrid.datagrid('load', {});
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
</head>
<body class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'north',border:false" style="height:100px; overflow: hidden;background-color: #f4f4f4">
		<form id="searchForm">
			<table>
				<tr>
					<th>商户号:</th>
					<td><input id="mchntCode" name="mchntCode" type="text"  class="easyui-validatebox"   value=""></td>
					<th >门店号:</th>
					<td><input id="shopCode" name="shopCode" type="text"  class="easyui-validatebox"   value=""></td>
					<th>终端号:</th>
					<td><input id="termCode" name="termCode" type="text"  class="easyui-validatebox"   value=""></td>
					<th >卡号:</th>
					<td><input id="cardNo" name="cardNo" type="text"  class="easyui-validatebox"   value=""></td>
				</tr>
				<tr>
					<th>交易类型:</th>
					<td><select id="transId" name="transId"  class="easyui-combobox" data-options="required:true,width:140,height:29,editable:false,panelHeight:'auto'">
						<option selected value="">请选择</option>
						<c:forEach items="${tranTypeListObj}" var="obj" >
							<option value="${obj.transId }" >${obj.transName }</option>
						</c:forEach>
						</select>
					</td>
					<th >查询记录:</th>
					<td><select id="isCurrent" name="isCurrent"  class="easyui-combobox" data-options="required:true,width:140,height:29,editable:false,panelHeight:'auto'">
						<c:forEach items="${curOrHisListObj}" var="obj" >
							 <option value="${obj.key }" >${obj.value }</option> 
						</c:forEach>
						</select>
					</td>
					<td><a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon_search',plain:true" onclick="searchFun();">查询</a><a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon_cancel',plain:true" onclick="cleanFun();">清空</a></td>
					<tbody id="condition" style="display: none">  
					<th>开始日期:</th>
					<td><input id="settleTimeStart" name="settleTimeStart" onclick="WdatePicker({readOnly:true,dateFmt:'yyyyMMdd'})" readonly="readonly" />
					</td>
					<th>结束日期:</th>
					<td><input id="settleTimeEnd"  name="settleTimeEnd"  onclick="WdatePicker({readOnly:true,dateFmt:'yyyyMMdd'})" readonly="readonly" />
					</td>
					</tbody>
					
				</tr>
			</table>
		</form>
	</div>
	<div data-options="region:'center',border:false,title:'交易明细列表'" >
		<table id="dataGrid" data-options="fit:true,border:false"></table>
	</div>
	<div id="toolbar" style="display: none;">
		<c:if test="${fn:contains(sessionInfo.resourceList, '/translog/exportLog')}">
			<a onclick="exportFun();" href="javascript:void(0);"
				class="easyui-linkbutton"
				data-options="plain:true,iconCls:'icon_add'">导出</a>
		</c:if>
	</div>
</body>
</html>