<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="../inc.jsp"></jsp:include>
<meta http-equiv="X-UA-Compatible" content="edge" />
<c:if test="${fn:contains(sessionInfo.resourceList, '/contract/edit')}">
	<script type="text/javascript">
		$.canEdit = true;
	</script>
</c:if>
<c:if test="${fn:contains(sessionInfo.resourceList, '/contract/delete')}">
	<script type="text/javascript">
		$.canDelete = true;
	</script>
</c:if>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>合同管理</title>
	<script type="text/javascript">
	var dataGrid;
	$(function() {
		dataGrid = $('#dataGrid').datagrid({
			url : '${ctx}/contract/dataGrid',
			striped : true,
			rownumbers : true,
			pagination : true,
			singleSelect : true,
			fitColumns:true,
			idField : 'id',
			sortName : 'id',
			sortOrder : 'desc',
			pageSize : 50,
			pageList : [ 10, 20, 30, 40, 50, 100, 200, 300, 400, 500 ],
			frozenColumns : [[ {
				width : '100',
				title : '合同号',
				field : 'id',
				sortable : true
			}] ],
			columns : [ [{
				width : '120',
				title : '商户编号',
				field : 'mchntCode',
				sortable : true
			}, {
				width : '120',
				title : '商户名称',
				field : 'mchntName',
				sortable : true
			},{
				width : '80',
				title : '结算方式',
				field : 'settleType',
				sortable : true,
				formatter : function(value, row, index) {
					var jsonObjs = ${contractSettleTypeList};
					for(var i =0;i<jsonObjs.length;i++){
						var jsonobj = jsonObjs[i];
						if(jsonobj.key==value){
							return jsonobj.value;
						}
					}
					return "未知类型";
				}
			}, {
				width : '80',
				title : '结算周期',
				field : 'settleCycle',
				sortable : true,
				formatter : function(value, row, index) {
					var jsonObjs =${contractSettleCycleList};
					for(var i =0;i<jsonObjs.length;i++){
						var jsonobj = jsonObjs[i];
						if(jsonobj.key==value){
							return jsonobj.value;
						}
					}
					return "未知类型";
				}
			}, {
				width : '80',
				title : '开始时间',
				field : 'contractStartDate',
				sortable : true
			}, {
				width : '80',
				title : '结束时间',
				field : 'contractEndDate',
				sortable : true
			}, {
				width : '80',
				title : '上一个结算日',
				field : 'preSettleDate',
				sortable : true
			}, {
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
					return "未知类型";
				}
			},{
				field : 'action',
				title : '操作',
				width : 100,
				formatter : function(value, row, index) {
					var str = '';
					if(row.isdefault!=0 ){
						str += $.formatString('<a href="javascript:void(0)" onclick="viewFun(\'{0}\');" >查看</a>', row.id);
						if(row.dataStat==0){
							if ($.canEdit) {
								str += '&nbsp;&nbsp;|&nbsp;&nbsp;';
								str += $.formatString('<a href="javascript:void(0)" onclick="editFun(\'{0}\');" >编辑</a>', row.id);
							}
							str += '&nbsp;&nbsp;|&nbsp;&nbsp;';
							if ($.canDelete) {
								str += $.formatString('<a href="javascript:void(0)" onclick="deleteFun(\'{0}\');" >删除</a>', row.id);
							}
						}
					}
					return str;
				}
			}]] ,
			toolbar : '#toolbar'
		});
	});
	
	function addFun() {
		parent.$.modalDialog({
			title : '添加合同',
			width : 700,
			height : 550,
			href : '${ctx}/contract/addPage',
			buttons : [ {
				text : '添加合同',
				handler : function() {
					parent.$.modalDialog.openner_dataGrid = dataGrid;
					var f = parent.$.modalDialog.handler.find('#contractAddForm');
					f.submit();
				}
			} ]
		}); 
	}
	
	function editFun(id) {
		if (id == undefined) {
			var rows = dataGrid.datagrid('getSelections');
			id = rows[0].id;
		} else {
			dataGrid.datagrid('unselectAll').datagrid('uncheckAll');
		}
		parent.$.modalDialog({
			title : '编辑',
			width : 700,
			height : 550,
			href : '${ctx}/contract/editPage?id=' + id,
			buttons : [{
				text : '编辑',
				handler : function() {
					parent.$.modalDialog.openner_dataGrid = dataGrid;
					var f = parent.$.modalDialog.handler.find('#contractEditForm');
					f.submit();
				}
			} ]
		});
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
			width : 700,
			height : 600,
			href : '${ctx}/contract/viewPage?id=' + id
		});
	}
	
	function deleteFun(id) {
		if (id == undefined) {
			var rows = dataGrid.datagrid('getSelections');
			id = rows[0].id;
		} else {
			dataGrid.datagrid('unselectAll').datagrid('uncheckAll');
		}
		parent.$.messager.confirm('询问', '您是否要终止当前合同？', function(b) {
			if (b) {
				progressLoad();
				$.post('${ctx}/contract/delete', {
					id : id
				}, function(result) {
					if (result.success) {
						parent.$.messager.alert('提示', result.msg, 'info');
						dataGrid.datagrid('reload');
					}
					progressClose();
				}, 'JSON');
			}
		});
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
					<th>商户编号:</th>
					<td><input name="mchntCode" type="text"  class="easyui-validatebox"   value=""></td>
					<th>商户名称:</th>
					<td><input name="mchntName" type="text"  class="easyui-validatebox"   value=""></td>
					<th >合同号:</th>
					<td><input name="id" type="text"  class="easyui-validatebox"   value=""></td>
					<th ></th>
				</tr>
				<tr>
					<th>开始日期:</th>
					<td><input name="contractStartDate" onclick="WdatePicker({readOnly:true,dateFmt:'yyyyMMdd'})" readonly="readonly" />
					</td>
					<th>结束日期:</th>
					<td><input  name="contractEndDate"  onclick="WdatePicker({readOnly:true,dateFmt:'yyyyMMdd'})" readonly="readonly" />
					</td>
					<td><a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon_search',plain:true" onclick="searchFun();">查询</a><a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon_cancel',plain:true" onclick="cleanFun();">清空</a></td>
				</tr>
			</table>
		</form>
	</div>
	<div data-options="region:'center',border:false,title:'合同列表'" >
		<table id="dataGrid" data-options="fit:true,border:false"></table>
	</div>
	<div id="toolbar" style="display: none;">
		<c:if test="${fn:contains(sessionInfo.resourceList, '/contract/add')}">
			<a id="add" onclick="addFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon_add'">添加</a>
		</c:if>
	</div>
</body>
</html>