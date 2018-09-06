<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="../inc.jsp"></jsp:include>
<meta http-equiv="X-UA-Compatible" content="edge" />
<c:if test="${fn:contains(sessionInfo.resourceList, '/product/edit')}">
	<script type="text/javascript">
		$.canEdit = true;
	</script>
</c:if>
<c:if test="${fn:contains(sessionInfo.resourceList, '/product/delete')}">
	<script type="text/javascript">
		$.canDelete = true;
	</script>
</c:if>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>卡产品管理</title>
	<script type="text/javascript">
	var dataGrid;
	$(function() {
		dataGrid = $('#dataGrid').datagrid({
			url : '${ctx}/product/dataGrid',
			striped : true,
			rownumbers : true,
			pagination : true,
			singleSelect : true,
			fitColumns:true,
			idField : 'productCode',
			sortName : 'createTime',
			sortOrder : 'desc',
			pageSize : 50,
			pageList : [ 10, 20, 30, 40, 50, 100, 200, 300, 400, 500 ],
			frozenColumns : [[ {
				width : '100',
				title : '产品名称',
				field : 'productName',
				sortable : true
			}] ],
			columns : [ [{
				width : '70',
				title : '密钥版本',
				field : 'versionId',
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
			}, {
				width : '80',
				title : '卡BIN',
				field : 'cardBin',
				sortable : true
			},{
				width : '70',
				title : '署名类型',
				field : 'onymousStat',
				sortable : true,
				formatter : function(value, row, index) {
					var jsonObjs = ${onymousStatList};
					for(var i =0;i<jsonObjs.length;i++){
						var jsonobj = jsonObjs[i];
						if(jsonobj.key==value){
							return jsonobj.value;
						}
					}
					return "未知类型";
				}
			},{
				width : '90',
				title : '业务类型',
				field : 'businessType',
				sortable : true,
				formatter : function(value, row, index) {
					var jsonObjs = ${businessTypeList};
					for(var i =0;i<jsonObjs.length;i++){
						var jsonobj = jsonObjs[i];
						if(jsonobj.key==value){
							return jsonobj.value;
						}
					}
					return "未知类型";
				}
			},{
				width : '80',
				title : '卡产品类型',
				field : 'productType',
				sortable : true,
				formatter : function(value, row, index) {
					var jsonObjs = ${productTypeList};
					for(var i =0;i<jsonObjs.length;i++){
						var jsonobj = jsonObjs[i];
						if(jsonobj.key==value){
							return jsonobj.value;
						}
					}
					return "未知类型";
				}
			},{
				width : '70',
				title : '有效期月数',
				field : 'validityPeriod',
				sortable : true
			},{
				width : '70',
				title : '最大余额',
				field : 'maxBalance',
				sortable : true
			},{
				width : '70',
				title : '消费次数',
				field : 'consumTimes',
				sortable : true
			},{
				width : '70',
				title : '充值次数',
				field : 'rechargeTimes',
				sortable : true
			},{
				width : '80',
				title : 'cvv2错误次数',
				field : 'cvv2ErrTimes',
				sortable : true
			},{
				width : '80',
				title : '最终卡序号',
				field : 'lastCardNum',
				sortable : true
			},{
				width : '70',
				title : '启用状态',
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
					var str = $.formatString('<a href="javascript:void(0)" onclick="viewFun(\'{0}\');" >查看</a>', row.productCode);
					if(row.isdefault!=0&&row.dataStat==0){

						if ($.canEdit) {
							str += '&nbsp;&nbsp;|&nbsp;&nbsp;';
							str += $.formatString('<a href="javascript:void(0)" onclick="editFun(\'{0}\');" >编辑</a>', row.productCode);
						}
						if ($.canDelete) {
							str += '&nbsp;&nbsp;|&nbsp;&nbsp;';
							str += $.formatString('<a href="javascript:void(0)" onclick="deleteFun(\'{0}\');" >删除</a>', row.productCode);
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
			title : '添加',
			width : 500,
			height : 350,
			href : '${ctx}/product/addPage',
			buttons : [ {
				text : '添加',
				handler : function() {
					parent.$.modalDialog.openner_dataGrid = dataGrid;//因为添加成功之后,需要刷新这个dataGrid,所以先预定义好
					var f = parent.$.modalDialog.handler.find('#productAddForm');
					f.submit();
				}
			} ]
		});
	}
	
	function viewFun(id) {
		if (id == undefined) {
			var rows = dataGrid.datagrid('getSelections');
			id = rows[0].productCode;
		} else {
			dataGrid.datagrid('unselectAll').datagrid('uncheckAll');
		}
		parent.$.modalDialog({
			title : '查看',
			width : 500,
			height : 350,
			href : '${ctx}/product/viewPage?id=' + id
		});
	}
	function editFun(id) {
		if (id == undefined) {
			var rows = dataGrid.datagrid('getSelections');
			id = rows[0].productCode;
		} else {
			dataGrid.datagrid('unselectAll').datagrid('uncheckAll');
		}
		parent.$.modalDialog({
			title : '编辑',
			width : 500,
			height : 350,
			href : '${ctx}/product/editPage?id=' + id,
			buttons : [ {
				text : '编辑',
				handler : function() {
					parent.$.modalDialog.openner_dataGrid = dataGrid;
					var f = parent.$.modalDialog.handler.find('#productEditForm');
					f.submit();
				}
			} ]
		});
	}
	function deleteFun(productCode) {
		if (productCode == undefined) {//点击右键菜单才会触发这个
			var rows = dataGrid.datagrid('getSelections');
			productCode = rows[0].productCode;
		} else {//点击操作里面的删除图标会触发这个
			dataGrid.datagrid('unselectAll').datagrid('uncheckAll');
		}
		parent.$.messager.confirm('询问', '您是否要删除当前产品？', function(b) {
			if (b) {
				progressLoad();
				$.post('${ctx}/product/delete', {
					id : productCode
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
	<div data-options="region:'north',border:false" style="height: 30px; overflow: hidden;background-color: #f4f4f4">
		<form id="searchForm">
			<table>
				<tr>
					<th>产品名称:</th>
					<td><input name="productName" type="text"  class="easyui-validatebox"   value="">
					</td>
					<th>密钥版本:</th>
					<td><input name="versionCode" type="text"  class="easyui-validatebox"   value="">
					</td>
					<th>创建时间:</th>
					<td><input name="createTimeStart" placeholder="点击选择时间" onclick="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})" readonly="readonly" />至<input  name="createTimeEnd" placeholder="点击选择时间" onclick="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})" readonly="readonly" />
					<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon_search',plain:true" onclick="searchFun();">查询</a><a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon_cancel',plain:true" onclick="cleanFun();">清空</a></td>
				</tr>
			</table>
		</form>
	</div>
	<div data-options="region:'center',border:false,title:'产品列表'" >
		<table id="dataGrid" data-options="fit:true,border:false"></table>
	</div>
	<div id="toolbar" style="display: none;">
		<c:if test="${fn:contains(sessionInfo.resourceList, '/product/add')}">
			<a onclick="addFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon_add'">添加</a>
		</c:if>
	</div>
</body>
</html>