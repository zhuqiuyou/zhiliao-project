<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="../inc.jsp"></jsp:include>
<meta http-equiv="X-UA-Compatible" content="edge" />
<c:if test="${fn:contains(sessionInfo.resourceList, '/order/sellOrderEdit')}">
	<script type="text/javascript">
		$.canEdit = true;
	</script>
</c:if>
<c:if test="${fn:contains(sessionInfo.resourceList, '/order/sellOrderDelete')}">
	<script type="text/javascript">
		$.canDelete = true;
	</script>
</c:if>
<c:if test="${fn:contains(sessionInfo.resourceList, '/order/sellOrderSubmit')}">
	<script type="text/javascript">
		$.canSubmit= true;
	</script>
</c:if>
<c:if test="${fn:contains(sessionInfo.resourceList, '/order/sellOrderDetail')}">
	<script type="text/javascript">
		$.detailList= true;
	</script>
</c:if>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>售卡订单管理</title>
	<script type="text/javascript">
	var dataGrid;
	$(function() {
		dataGrid = $('#dataGrid').datagrid({
			url : '${ctx}/order/dataGrid/400100',
			striped : true,
			rownumbers : true,
			pagination : true,
			singleSelect : true,
			fitColumns:true,
			idField : 'id',
			sortName : 'createTime',
			sortOrder : 'desc',
			pageSize : 20,
			pageList : [ 10, 20, 30, 40, 50, 100, 200, 300, 400, 500 ],
			frozenColumns : [[ 
			{
				width : '180',
				title : '订单号',
				field : 'id',
				sortable : true
			},{
				width : '100',
				title : '产品名称',
				field : 'productName',
				sortable : true
			}] ],
			columns : [ [{
				width : '80',
				title : '订单类型',
				field : 'orderType',
				sortable : true,
				formatter : function(value, row, index) {
					var jsonObjs = ${orderTypeList};
					for(var i =0;i<jsonObjs.length;i++){
						var jsonobj = jsonObjs[i];
						if(jsonobj.code==value){
							return jsonobj.text;
						}
					}
					return "未知类型";
				}
			}, {
				width : '80',
				title : '订单日期',
				field : 'orderDate',
				sortable : true
			}, {
				width : '80',
				title : '张数',
				field : 'cardNum',
				sortable : true
			}, {
				width : '80',
				title : '激活状态',
				field : 'actStat',
				sortable : true,
				formatter : function(value, row, index) {
					var jsonObjs = ${orderActivateStsJson};
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
				title : '订单状态',
				field : 'orderStat',
				sortable : true,
				formatter : function(value, row, index) {
					var jsonObjs = ${makeorderstatusList};
					for(var i =0;i<jsonObjs.length;i++){
						var jsonobj = jsonObjs[i];
						if(jsonobj.code==value){
							return jsonobj.text;
						}
					}
					return "未知类型";
				}
			},{
				width : '120',
				title : '创建时间',
				field : 'createTime',
				sortable : true
			},{
				width : '120',
				title : '修改时间',
				field : 'updateTime',
				sortable : true
			},{
				field : 'action',
				title : '操作',
				width : 220,
				formatter : function(value, row, index) {
					var str = $.formatString('<a href="javascript:void(0)" onclick="viewFun(\'{0}\');" >查看</a>', row.id);
					if(row.isdefault!=0){
						if ($.detailList) {
							str += '&nbsp;&nbsp;|&nbsp;&nbsp;';
							str += $.formatString('<a href="javascript:void(0)" onclick="detailList(\'{0}\');" >明细</a>', row.id);
						}
						if ($.canEdit && row.orderStat=='01') {
								str += '&nbsp;&nbsp;|&nbsp;&nbsp;';
								str += $.formatString('<a href="javascript:void(0)" onclick="editFun(\'{0}\');" >编辑</a>', row.id);
						}
						if ($.canDelete && row.orderStat=='01') {
							str += '&nbsp;&nbsp;|&nbsp;&nbsp;';
							str += $.formatString('<a href="javascript:void(0)" onclick="deleteFun(\'{0}\');" >删除</a>', row.id);
						}
						if ($.canSubmit && row.orderStat=='01') {
							str += '&nbsp;&nbsp;|&nbsp;&nbsp;';
							str += $.formatString('<a href="javascript:void(0)" onclick="submitFun(\'{0}\');" >提交订单</a>', row.id);
						}
					}
					return str;
				}
			}]] ,
			toolbar : '#toolbar'
		});
	});
	function downLoadFun(id){
		window.location='${ctx}/order/download?id='+id;
	}
	function addFun() {
		parent.$.modalDialog({
			title : '添加',
			width : 600,
			height : 600,
			href : '${ctx}/order/addPage/400100',
			buttons : [ {
				text : '添加',
				handler : function() {
					parent.$.modalDialog.openner_dataGrid = dataGrid;
					var f = parent.$.modalDialog.handler.find('#orderAddForm');
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
			width :600,
			height : 600,
			href : '${ctx}/order/viewPage?type=400100&id=' + id
		});
	}
	
	function detailList(id) {
		if (id == undefined) {
			var rows = dataGrid.datagrid('getSelections');
			id = rows[0].id;
		} else {
			dataGrid.datagrid('unselectAll').datagrid('uncheckAll');
		}
		parent.$.modalDialog({
			title : '订单明细',
			width :700,
			height : 400,
			href : '${ctx}/order/detailList?id=' + id
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
			width :600,
			height : 600,
			href : '${ctx}/order/editPage?type=400100&id=' + id,
			buttons : [{
				text : '编辑',
				handler : function() {
					parent.$.modalDialog.openner_dataGrid = dataGrid;
					var f = parent.$.modalDialog.handler.find('#orderEditForm');
					f.submit();
				}
			} ]
		});
	}
	function submitFun(id){
		if (id == undefined) {//点击右键菜单才会触发这个
			var rows = dataGrid.datagrid('getSelections');
			id = rows[0].id;
		} else {//点击操作里面的删除图标会触发这个
			dataGrid.datagrid('unselectAll').datagrid('uncheckAll');
		}
		parent.$.messager.confirm('询问', '您确定要提交并完成当前售卡订单？', function(b) {
			if (b) {
				progressLoad();
				$.post('${ctx}/order/submitOrder', {
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
	function deleteFun(id) {
		if (id == undefined) {//点击右键菜单才会触发这个
			var rows = dataGrid.datagrid('getSelections');
			id = rows[0].id;
		} else {//点击操作里面的删除图标会触发这个
			dataGrid.datagrid('unselectAll').datagrid('uncheckAll');
		}
		parent.$.messager.confirm('询问', '您是否要删除当前订单？', function(b) {
			if (b) {
				progressLoad();
				$.post('${ctx}/order/delete', {
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
					<th>产品名称:</th>
					<td><input name="productName" type="text"  class="easyui-validatebox"   value=""></td>
					<th>订单编号:</th>
					<td><input name="id" type="text"  class="easyui-validatebox"   value="">
					</td>
					<th>订单状态:</th>
					<td>
						<select name="orderStat" class="easyui-combobox" data-options="width:140,height:29,editable:false,panelHeight:'auto'">
							<option value="" >全部</option>
							<c:forEach items="${orderstatusList}" var="type">
								<option value="${type.code}" >${type.text}</option>
							</c:forEach>
						</select>
					</td>
					<th>激活状态:</th>
					<td>
						<select name="actStat" class="easyui-combobox" data-options="width:140,height:29,editable:false,panelHeight:'auto'">
							<option value="" >全部</option>
							<c:forEach items="${orderActivateSts}" var="type">
								<option value="${type.key}" >${type.value}</option>
							</c:forEach>
						</select>
					</td>
				</tr>
				<tr>
					<th>创建时间:</th>
					<td><input name="createTimeStart" placeholder="点击选择时间"
						onclick="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})"
						readonly="readonly" />
					</td>
					<td>至</td>
					<td><input name="createTimeEnd" placeholder="点击选择时间"
						onclick="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})"
						readonly="readonly" />
					</td>
					<td><a href="javascript:void(0);" class="easyui-linkbutton"
						data-options="iconCls:'icon_search',plain:true"
						onclick="searchFun();">查询</a><a href="javascript:void(0);"
						class="easyui-linkbutton"
						data-options="iconCls:'icon_cancel',plain:true"
						onclick="cleanFun();">清空</a>
					</td>
				</tr>

			</table>
		</form>
	</div>
	<div data-options="region:'center',border:false,title:'订单列表'" >
		<table id="dataGrid" data-options="fit:true,border:false"></table>
	</div>
	<div id="toolbar" style="display: none;">
		<c:if test="${fn:contains(sessionInfo.resourceList, '/order/sellOrderAdd')}">
			<a onclick="addFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon_add'">添加</a>
		</c:if>
	</div>
</body>
</html>