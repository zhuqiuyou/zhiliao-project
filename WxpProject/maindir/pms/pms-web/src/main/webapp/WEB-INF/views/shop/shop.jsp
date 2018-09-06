<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<jsp:include page="../inc.jsp"></jsp:include>
<meta http-equiv="X-UA-Compatible" content="edge" />
<c:if test="${fn:contains(sessionInfo.resourceList, '/shop/edit')}">
	<script type="text/javascript">
		$.canEdit = true;
	</script>
</c:if>
<c:if test="${fn:contains(sessionInfo.resourceList, '/shop/delete')}">
	<script type="text/javascript">
		$.canDelete = true;
	</script>
</c:if>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>门店管理</title>
<script type="text/javascript">
	var dataGrid;
	var merchantTree;
	var mchntId;
	$(function() {
		merchantTree = $('#merchantTree').tree({
			url : '${ctx}/merchant/tree',
			parentField : 'pid',
			lines : true,
			onClick : function(node) {
				mchntId = node.id;
				dataGrid.datagrid('load', {
					mchntId : node.id
				});
			}
		});

		dataGrid = $('#dataGrid')
				.datagrid(
						{
							url : '${ctx}/shop/dataGrid',
							striped : true,
							rownumbers : true,
							pagination : true,
							singleSelect : true,
							idField : 'shopId',
							sortName : 'createTime',
							sortOrder : 'desc',
							pageSize : 50,
							pageList : [ 10, 20, 30, 40, 50, 100, 200, 300,
									400, 500 ],
							frozenColumns : [ [
									{
										width : '100',
										title : '门店名称',
										field : 'shopName',
										sortable : true
									},
									{
										width : '100',
										title : '门店号	',
										field : 'shopCode',
										sortable : true
									},{
										width : '100',
										title : '商户名称',
										field : 'mchntId',
										formatter : function(value, row, index) {
											var jsonObjs = $
													.parseJSON('${merchantInfJson}');
											for (var i = 0; i < jsonObjs.length; i++) {
												var jsonobj = jsonObjs[i];
												if (jsonobj.mchntId == value) {
													return jsonobj.mchntName;
												}
											}
											return '';
										}
									},
									{
										width : '70',
										title : '门店状态',
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
									},
									{
										width : '150',
										title : '创建时间',
										field : 'createTime',
										sortable : true
									},
									{
										width : '150',
										title : '修改时间',
										field : 'updateTime',
										sortable : true
									},
									{
										width : '80',
										title : '创建人',
										field : 'createUser'
									},
									{
										field : 'action',
										title : '操作',
										width : 100,
										formatter : function(value, row, index) {
											var str = '';
											if (row.isdefault != 0) {
												if ($.canEdit) {
													str += $
															.formatString(
																	'<a href="javascript:void(0)" onclick="editFun(\'{0}\');" >编辑</a>',
																	row.shopId);
												}
												str += '&nbsp;&nbsp;|&nbsp;&nbsp;';
												if ($.canDelete) {
													str += $
															.formatString(
																	'<a href="javascript:void(0)" onclick="deleteFun(\'{0}\');" >删除</a>',
																	row.shopId);
												}
											}
											return str;
										}
									} ] ],

							toolbar : '#toolbar'
						});
	});

	function addFun() {
		var href = '${ctx}/shop/addPage';
		if (mchntId) {
			href = href +'?mchntId=' + mchntId;
		}
		parent.$.modalDialog({
			title : '添加',
			width : 500,
			height : 300,
			href : href,
			buttons : [ {
				text : '添加',
				handler : function() {
					parent.$.modalDialog.openner_dataGrid = dataGrid;
					var f = parent.$.modalDialog.handler.find('#shopAddForm');
					initTreeStyle();
					f.submit();
				}
			} ]
		});
	}

	function deleteFun(id) {
		if (id == undefined) {//点击右键菜单才会触发这个
			var rows = dataGrid.datagrid('getSelections');
			id = rows[0].shopId;
		} else {//点击操作里面的删除图标会触发这个
			dataGrid.datagrid('unselectAll').datagrid('uncheckAll');
		}
		parent.$.messager.confirm('询问', '您是否要删除当前索引吗？', function(b) {
			if (b) {
				progressLoad();
				$.post('${ctx}/shop/delete', {
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

	function editFun(id) {
		if (id == undefined) {
			var rows = dataGrid.datagrid('getSelections');
			id = rows[0].shopId;
		} else {
			dataGrid.datagrid('unselectAll').datagrid('uncheckAll');
		}
		parent.$.modalDialog({
			title : '编辑',
			width : 500,
			height : 300,
			href : '${ctx}/shop/editPage?id=' + id,
			buttons : [ {
				text : '编辑',
				handler : function() {
					parent.$.modalDialog.openner_dataGrid = dataGrid;//因为添加成功之后，需要刷新这个dataGrid，所以先预定义好
					var f = parent.$.modalDialog.handler
							.find('#shopEditForm');
					f.submit();
					initTreeStyle();
				}
			} ]
		});
	}

	function searchFun() {
		
		dataGrid.datagrid('load', $.serializeObject($('#searchForm')));
		initTreeStyle();
	}
	function cleanFun() {
		$('#searchForm input').val('');
		dataGrid.datagrid('load', {});
		initTreeStyle();
	}
	
	function initTreeStyle() {
		mchntId = '';
		merchantTree.find('.tree-node-selected').removeClass('tree-node-selected');
	}
</script>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'north',border:false"
		style="height: 70px; overflow: hidden;background-color: #f4f4f4">
		<form id="searchForm">
			<table>
				<tr>
					<th>商户名称:</th>
					<td><input name="mchntName" type="text"  class="easyui-validatebox"   value=""></td>
					<th>门店名称:</th>
					<td><input name="shopName" type="text"  class="easyui-validatebox"   value=""></td>
					<th>门店号:</th>
					<td><input name="shopCode" type="text"  class="easyui-validatebox"   value=""></td>
				</tr>
				<tr>
					<th>创建时间:</th>
					<td><input name="createTimeStart" placeholder="点击选择时间"
						onclick="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})"
						readonly="readonly" /><td >至<td><input name="createTimeEnd"
						placeholder="点击选择时间"
						onclick="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})"
						readonly="readonly" /> </td>
						<th></th>
						<td ><a href="javascript:void(0);"
						class="easyui-linkbutton"
						data-options="iconCls:'icon_search',plain:true"
						onclick="searchFun();">查询</a><a href="javascript:void(0);"
						class="easyui-linkbutton"
						data-options="iconCls:'icon_cancel',plain:true"
						onclick="cleanFun();">清空</a></td>
				</tr>
			</table>
		</form>
	</div>
	<div data-options="region:'center',border:false,title:'门店列表'">
		<table id="dataGrid" data-options="fit:true,border:false"></table>
	</div>
	<div data-options="region:'west',border:false,split:true,title:'商户'"
		style="width:200px;">
		<ul id="merchantTree" style="width:180px;margin: 10px 10px 10px 10px">
		</ul>
	</div>
	<div id="toolbar" style="display: none;">
		<c:if test="${fn:contains(sessionInfo.resourceList, '/shop/add')}">
			<a onclick="addFun();" href="javascript:void(0);"class="easyui-linkbutton"
				data-options="plain:true,iconCls:'icon_add'">添加</a>
		</c:if>
	</div>
</body>
</html>