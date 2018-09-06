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
<c:if test="${fn:contains(sessionInfo.resourceList, '/keyindex/edit')}">
	<script type="text/javascript">
		$.canEdit = true;
	</script>
</c:if>
<c:if test="${fn:contains(sessionInfo.resourceList, '/keyindex/delete')}">
	<script type="text/javascript">
		$.canDelete = true;
	</script>
</c:if>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>密钥索引</title>
<script type="text/javascript">
	var dataGrid;
	var keyVersionTree;
	$(function() {

		keyVersionTree = $('#keyVersionTree').tree({
			url : '${ctx}/keyversion/tree',
			parentField : 'pid',
			lines : true,
			onClick : function(node) {
				dataGrid.datagrid('load', {
					versionId : node.id
				});
			}
		});

		dataGrid = $('#dataGrid')
				.datagrid(
						{
							url : '${ctx}/keyindex/dataGrid',
							striped : true,
							rownumbers : true,
							pagination : true,
							singleSelect : true,
							idField : 'keyId',
							sortName : 'createTime',
							sortOrder : 'desc',
							pageSize : 15,
							pageList : [ 10, 15,20, 30, 40, 50, 100, 200, 300,
									400, 500 ],
							frozenColumns : [ [{
								width : '80',
								title : '密钥版本',
								field : 'versionId',
								sortable : true,
								formatter : function(value, row, index) {
									var jsonObjs = $
											.parseJSON('${versiontypeJson}');
									for (var i = 0; i < jsonObjs.length; i++) {
										var jsonobj = jsonObjs[i];
										if (jsonobj.versionId == value) {
											return jsonobj.versionCode;
										}
									}
									return '';
								}
							}] ],
						columns : [ [ {
								width : '120',
								title : '索引名',
								field : 'keyName',
								sortable : true,
								formatter : function(value, row, index) {
									var jsonObjs = $
											.parseJSON('${keyindextypeJson}');
									for (var i = 0; i < jsonObjs.length; i++) {
										var jsonobj = jsonObjs[i];
										if (jsonobj.code == value) {
											return jsonobj.text;
										}
									}
									return '';
								}
							},{
										width : '100',
										title : '密钥值',
										field : 'keyIndex',
										sortable : true
									},
									{
										width : '150',
										title : '备注',
										field : 'remarks',
										sortable : true
									},
									{
										width : '150',
										title : '创建时间',
										field : 'createTime',
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
																	row.keyId);
												}
											}
											return str;
										}
									} ] ],

							toolbar : '#toolbar'
						});
	});

	function addFun() {
		parent.$.modalDialog({
			title : '添加',
			width : 650,
			height : 380,
			href : '${ctx}/keyindex/addPage',
			buttons : [ {
				text : '添加',
				handler : function() {
					parent.$.modalDialog.openner_dataGrid = dataGrid;
					var f = parent.$.modalDialog.handler
							.find('#keyindexAddForm');
					f.submit();
					initTreeStyle();
				}
			} ]
		});
	}

	function deleteFun(id) {
		if (id == undefined) {//点击右键菜单才会触发这个
			var rows = dataGrid.datagrid('getSelections');
			id = rows[0].keyId;
		} else {//点击操作里面的删除图标会触发这个
			dataGrid.datagrid('unselectAll').datagrid('uncheckAll');
		}
		parent.$.messager.confirm('询问', '您是否要删除当前索引吗？', function(b) {
			if (b) {
				progressLoad();
				$.post('${ctx}/keyindex/delete', {
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
			id = rows[0].keyId;
		} else {
			dataGrid.datagrid('unselectAll').datagrid('uncheckAll');
		}
		parent.$.modalDialog({
			title : '编辑',
			width : 500,
			height : 300,
			href : '${ctx}/keyindex/editPage?id=' + id,
			buttons : [ {
				text : '编辑',
				handler : function() {
					parent.$.modalDialog.openner_dataGrid = dataGrid;//因为添加成功之后，需要刷新这个dataGrid，所以先预定义好
					var f = parent.$.modalDialog.handler
							.find('#keyindexEditForm');
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
		keyVersionTree.find('.tree-node-selected').removeClass('tree-node-selected');
	}
</script>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'north',border:false"
		style="height: 30px; overflow: hidden;background-color: #f4f4f4">
		<form id="searchForm">
			<table>
				<tr>
					<th>创建时间:</th>
					<td><input name="createTimeStart" placeholder="点击选择时间"
						onclick="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})"
						readonly="readonly" />至<input name="createTimeEnd"
						placeholder="点击选择时间"
						onclick="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})"
						readonly="readonly" /> <a href="javascript:void(0);"
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
	<div data-options="region:'center',border:false,title:'索引列表'">
		<table id="dataGrid" data-options="fit:true,border:false" ></table>
	</div>
	<div data-options="region:'west',border:false,split:true,title:'密钥版本'"
		style="width:200px;">
		<ul id="keyVersionTree"
			style="width:180px;margin: 10px 10px 10px 10px">
		</ul>
	</div>
	<div id="toolbar" style="display: none;">
		<c:if test="${fn:contains(sessionInfo.resourceList, '/keyindex/add')}">
			<a onclick="addFun();" href="javascript:void(0);"
				class="easyui-linkbutton"
				data-options="plain:true,iconCls:'icon_add'">添加</a>
		</c:if>
	</div>
</body>
</html>