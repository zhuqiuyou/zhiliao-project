<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="../inc.jsp"></jsp:include>
<meta http-equiv="X-UA-Compatible" content="edge" />
<c:if test="${fn:contains(sessionInfo.resourceList, '/settlebillPay/download')}">
	<script type="text/javascript">
		$.canDownload = true;
	</script>
</c:if>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>结算单付款</title>
	<script type="text/javascript">
	var dataGrid;
	$(function() {
		dataGrid = $('#dataGrid').datagrid({
			url : '${ctx}/settlebill/dataGrid/10',
			striped : true,
			rownumbers : true,
			pagination : true,
			singleSelect : true,
			fitColumns:true,
			idField : 'settleId',
			sortName : 'settleId',
			sortOrder : 'desc',
			singleSelect: false,
			selectOnCheck: true,
			checkOnSelect: true,
			pageSize : 15,
			pageList : [ 10, 15, 20, 30, 40, 50, 100, 200, 300, 400, 500 ],
			frozenColumns : [[
			   { field:'ck',checkbox:true },{
				width : '160',
				title : '结算单号',
				field : 'settleId',
				sortable : true,
				formatter : function(value, row, index) {
					return '<a href="#" onclick="viewFun(\'' + value+ '\')">'+value + '</a>';
				}
				
			}] ],
			columns : [ [{
				width : '120',
				title : '商户号',
				field : 'mchntCode',
				sortable : true
			}, {
				width : '120',
				title : '商户名称',
				field : 'mchntName',
				sortable : true
			},{
				width : '100',
				title : '结算日期',
				field : 'settleDate',
				sortable : true
			}, {
				width : '80',
				title : '结算本金',
				field : 'transAtSum',
				sortable : true
			}, {
				width : '80',
				title : '结算金额',
				field : 'settleAtSum',
				sortable : true
			}, {
				width : '80',
				title : '结算手续费',
				field : 'settleFee',
				sortable : true
			}, {
				width : '80',
				title : '结算单状态',
				field : 'billStat',
				sortable : true,
				formatter : function(value, row, index) {
					var jsonObjs = ${billStatListJson};
					for(var i =0;i<jsonObjs.length;i++){
						var jsonobj = jsonObjs[i];
						if(jsonobj.code==value){
							return jsonobj.text;
						}
					}
					return "未知状态";
				}
			},{
				field : 'action',
				title : '操作',
				width : 100,
				formatter : function(value, row, index) {
					var str = '';
					if(row.isdefault!=0 ){
						if ($.canDownload) {
							str += $.formatString('<a href="javascript:void(0)" onclick="downloadBill(\'{0}\');" >明细单下载</a>', row.settleId);
						}
					}
					return str;
				}
			}]] ,
			toolbar : '#toolbar'
		});
	});
	
	function updateStat() {
		var checkedItems = $('#dataGrid').datagrid('getChecked');
		var ids = '';
		$.each(checkedItems, function(index, item){
			//ids.push(item.settleId);
			ids += item.settleId;
			if(index < checkedItems.length -1) {
				ids += ','
			}
		});
		if (ids) {
			if (confirm('是否结算单付款？')){
				$.ajax({
			        url: '${ctx}/settlebill/updateStat',
			        type: 'post',
			        data: 'settleId=' + ids +'&stat=' + '20',
			        error: function (request, message, ex) {
			          alert(message + request + ex);
			        },
			        success: function (returnValue) {
			        	 dataGrid.datagrid('reload');
			        	 alert('操作成功');
			        }
			      });
		      }
			
		} else {
			alert('请选择付款单');
		}
		
	}

	function viewFun(id) {
		if (id == undefined) {
			var rows = dataGrid.datagrid('getSelections');
			id = rows[0].id;
		} else {
			dataGrid.datagrid('unselectAll').datagrid('uncheckAll');
		}
		parent.$.modalDialog({
			title : '查看',
			width :700,
			height : 350,
			href : '${ctx}/settlebill/viewPage?settleId=' + id
		});
	}
	
	function downloadBill(settleId){
		window.location='${ctx}/settlebill/download?settleId='+settleId;
	}
	
	function searchFun() {
		dataGrid.datagrid('load', $.serializeObject($('#searchForm')));
	}
	function cleanFun() {
		$('#searchForm input').val('');
		dataGrid.datagrid('load', {});
	}
	</script>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'north',border:false" style="height: 70px; overflow: hidden;background-color: #f4f4f4">
		<form id="searchForm">
			<table>
				<tr>
					<th>商户号:</th>
					<td><input name="mchntCode" type="text"  class="easyui-validatebox"   value=""></td>
				</tr>
				<tr>
					<th>开始日期:</th>
					<td><input name="settleDateStart" onclick="WdatePicker({readOnly:true,dateFmt:'yyyyMMdd'})" readonly="readonly" />
					</td>
					<th>结束日期:</th>
					<td><input  name="settleDateEnd"  onclick="WdatePicker({readOnly:true,dateFmt:'yyyyMMdd'})" readonly="readonly" />
					</td>
					<td><a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon_search',plain:true" onclick="searchFun();">查询</a><a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon_cancel',plain:true" onclick="cleanFun();">清空</a></td>
				</tr>
			</table>
		</form>
	</div>
	
	<div data-options="region:'center',border:false,title:'结算单列表'" >
		<table id="dataGrid" data-options="fit:true,border:false"></table>
	</div>
	
	<div id="toolbar" style="display: none;">
		<c:if test="${fn:contains(sessionInfo.resourceList, '/settlebillPay/updateStat')}">
			<a onclick="updateStat();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon_add'">结算单付款</a>
		</c:if>
	</div>
</body>
</html>