<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="../inc.jsp"></jsp:include>
<meta http-equiv="X-UA-Compatible" content="edge" />
<c:if test="${fn:contains(sessionInfo.resourceList, '/systemparam/edit')}">
	<script type="text/javascript">
		$.canEdit = true;
	</script>
</c:if>
<c:if test="${fn:contains(sessionInfo.resourceList, '/systemparam/delete')}">
	<script type="text/javascript">
		$.canDelete = true;
	</script>
</c:if>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>终端参数管理</title>
	<script type="text/javascript">
	var dataGrid;
	$(function() {
		dataGrid = $('#dataGrid').datagrid({
			url : '${ctx}/systemparam/dataGrid',
			striped : true,
			rownumbers : true,
			pagination : true,
			singleSelect : true,
			idField : 'id',
			sortName : 'prmVersion,prmType,id',
			sortOrder : 'asc',
			pageSize : 15,
			pageList : [ 10,15, 20, 30, 40, 50, 100, 200, 300, 400, 500 ],
			frozenColumns : [ [ {
				width : '100',
				title : '参数名称',
				field : 'prmName',
				sortable : true
			}, {
				width : '120',
				title : '参数值',
				field : 'prmVal',
				sortable : true
			},{
				width : '120',
				title : '参数类型',
				field : 'prmType',
				sortable : true,
				formatter : function(value, row, index) {
					var jsonObjs = $.parseJSON('${linkdownparams}');
					for (var i = 0; i < jsonObjs.length; i++) {
						var jsonobj = jsonObjs[i];
						if (jsonobj.code == value) {
							return jsonobj.name;
						}
					}
					return '';
				}
			}, {
				width : '80',
				title : '版本',
				field : 'prmVersion',
				sortable : true
			}, {
				width : '80',
				title : '参数长度',
				field : 'prmLen'
			},{
				width : '80',
				title : '参数描述',
				field : 'prmDesc'
			}] ],
			columns : [ [ {
				width : '80',
				title : '状态',
				field : 'prmStat',
				sortable : true,
				formatter : function(value, row, index) {
					if(value){
						return '停用';
					}else{
						return '正常';
					}
				}
			} , {
				field : 'action',
				title : '操作',
				width : 120,
				formatter : function(value, row, index) {
					var str = $.formatString('<a href="javascript:void(0)" onclick="viewFun(\'{0}\',\'{1}\',\'{2}\');" >查看</a>', row.id ,row.prmType ,row.prmVersion);
					if(row.isdefault!=0){
						if ($.canEdit && !row.prmStat) {
							str += '&nbsp;&nbsp;|&nbsp;&nbsp;';
							str += $.formatString('<a href="javascript:void(0)" onclick="editFun(\'{0}\',\'{1}\',\'{2}\');" >编辑</a>', row.id ,row.prmType ,row.prmVersion);
						}
						if ($.canDelete &&  !row.prmStat) {
							str += '&nbsp;&nbsp;|&nbsp;&nbsp;';
							str += $.formatString('<a href="javascript:void(0)" onclick="deleteFun(\'{0}\',\'{1}\',\'{2}\');" >删除</a>', row.id ,row.prmType ,row.prmVersion);
						}
					}
					return str;
				}
			}] ],
			toolbar : '#toolbar'
		});
	});
	function addLink(){
		parent.$.modalDialog({
			title : '添加',
			width :700,
			height : 600,
			href : '${ctx}/systemparam/addPage?type=10001',
			buttons : [ {
				text : '添加',
				handler : function() {
					parent.$.modalDialog.openner_dataGrid = dataGrid;
					var f = parent.$.modalDialog.handler.find('#sysParamAddForm');
					f.submit();
				}
			} ]
		});
	}
	function addFun() {
		parent.$.modalDialog({
			title : '添加',
			width : 500,
			height : 300,
			href : '${ctx}/systemparam/addPage',
			buttons : [ {
				text : '添加',
				handler : function() {
					parent.$.modalDialog.openner_dataGrid = dataGrid;
					var f = parent.$.modalDialog.handler.find('#sysParamAddForm');
					f.submit();
				}
			} ]
		});
	}
	
	function deleteFun(id ,prmType ,prmVersion) {
		if (id == undefined) {
			var rows = dataGrid.datagrid('getSelections');
			id = rows[0].id;
			prmType = rows[0].prmType;
			prmVersion = rows[0].prmVersion;
		} else {
			dataGrid.datagrid('unselectAll').datagrid('uncheckAll');
		}
		parent.$.messager.confirm('询问', '您是否要停用该参数？', function(b) {
			if (b) {
				var currentUserId = '${sessionInfo.id}';
				if (currentUserId != id) {
					progressLoad();
					$.post('${ctx}/systemparam/delete', {
						id : id,
						prmType:prmType,
						prmVersion:prmVersion 
					}, function(result) {
						if (result.success) {
							parent.$.messager.alert('提示', result.msg, 'info');
							dataGrid.datagrid('reload');
						}
						progressClose();
					}, 'JSON');
				} else {
					parent.$.messager.show({
						title : '提示',
						msg : '不可以删除自己！'
					});
				}
			}
		});
	}
	
	function editFun(id ,prmType ,prmVersion) {
		if (id == undefined) {
			var rows = dataGrid.datagrid('getSelections');
			id = rows[0].id;
			prmType = rows[0].prmType;
			prmVersion = rows[0].prmVersion;
		} else {
			dataGrid.datagrid('unselectAll').datagrid('uncheckAll');
		}
		parent.$.modalDialog({
			title : '编辑',
			width : 500,
			height : 300,
			href : '${ctx}/systemparam/editPage?id=' + id+'&prmType='+prmType+'&prmVersion='+prmVersion,
			buttons : [ {
				text : '编辑',
				handler : function() {
					parent.$.modalDialog.openner_dataGrid = dataGrid;
					var f = parent.$.modalDialog.handler.find('#sysParamEditForm');
					f.submit();
				}
			} ]
		});
	}

	function viewFun(id ,prmType ,prmVersion) {
		if (id == undefined) {
			var rows = dataGrid.datagrid('getSelections');
			id = rows[0].id;
			prmType = rows[0].prmType;
			prmVersion = rows[0].prmVersion;
		} else {
			dataGrid.datagrid('unselectAll').datagrid('uncheckAll');
		}
		parent.$.modalDialog({
			title : '查看',
			width : 500,
			height : 300,
			href : '${ctx}/systemparam/viewPage?id=' + id+'&prmType='+prmType+'&prmVersion='+prmVersion
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
	<div data-options="region:'north',border:false" style="height: 30px; overflow: hidden;background-color: #f4f4f4">
		<form id="searchForm">
			<table>
				<tr>
					<th>参数名:</th>
					<td><input name="prmName"   /></td>
					<th>版本:</th>
					<td><input name="prmVersion"  class="easyui-numberbox"   validType='length[0,5]'  /></td>
					<th>类型:</th>
					<td><select name="prmType"  class="easyui-combobox" data-options="width:140,height:29,editable:false,panelHeight:'auto'" >
							<c:forEach items="${downParams}" var="ktj">
								<option value="${ktj.code }" >${ktj.name }</option>
							</c:forEach>
						</select>
					<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon_search',plain:true" onclick="searchFun();">查询</a><a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon_cancel',plain:true" onclick="cleanFun();">清空</a></td>
				</tr>
			</table>
		</form>
	</div>
	<div data-options="region:'center',border:false,title:'参数列表'" >
		<table id="dataGrid" data-options="fit:true,border:false"></table>
	</div>
	<div id="toolbar" style="display: none;">
		<c:if test="${fn:contains(sessionInfo.resourceList, '/systemparam/addLink')}">
			<a onclick="addLink();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon_add'">添加联机下载参数</a>
		</c:if>
		<c:if test="${fn:contains(sessionInfo.resourceList, '/systemparam/add')}">
			<a onclick="addFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon_add'">添加卡Bin参数</a>
		</c:if>
	</div>
</body>
</html>