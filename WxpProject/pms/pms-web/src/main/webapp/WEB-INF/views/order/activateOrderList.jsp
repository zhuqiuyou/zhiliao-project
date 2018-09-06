<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<jsp:include page="../inc.jsp"></jsp:include>
<meta http-equiv="X-UA-Compatible" content="edge" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>激活订单明细</title>
<script type="text/javascript">
	var dataGrid;
	var shopTree;
	$(function() {
		shopTree = $('#orderTree').tree({
			url : '${ctx}/order/tree/300100',
			parentField : 'pid',
			lines : true,
			onClick : function(node) {
				dataGrid.datagrid('load', {
					orderId : node.id
				});
			}
		});
		dataGrid = $('#dataGrid')
				.datagrid(
						{
							url : '${ctx}/orderList/dataGrid/300100',
							striped : true,
							rownumbers : true,
							pagination : true,
							singleSelect : true,
							idField : 'id',
							sortName : 'createTime',
							sortOrder : 'asc',
							pageSize : 50,
							pageList : [ 10, 20, 30, 40, 50, 100, 200, 300,
									400, 500 ],
							frozenColumns : [ [ {
								width : '150',
								title : '卡号',
								field : 'cardNo',
								sortable : true
							}, {
								width : '100',
								title : '卡片有效期',
								field : 'validDate',
								sortable : true
							} , {
								width : '100',
								title : '金额',
								field : 'amount',
								sortable : true
							} ] ],
							columns : [ [
									{
										width : '80',
										title : '状态',
										field : 'stat',
										sortable : true,
										formatter : function(value, row, index) {
											var jsonObjs = ${orderListTypeList};
											for(var i =0;i<jsonObjs.length;i++){
												var jsonobj = jsonObjs[i];
												if(jsonobj.code==value){
													return jsonobj.text;
												}
											}
											return "未知类型";
										}
									},{
										width : '100',
										title : '创建时间',
										field : 'createTime',
										sortable : true
									},
									{
										width : '80',
										title : '创建人',
										field : 'createUser'
									} ] ],
							toolbar : '#toolbar'
						});
	});
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
	<div data-options="region:'north',border:false"
		style="height: 30px; overflow: hidden;background-color: #f4f4f4">
		<form id="searchForm">
			<table>
				<tr>
					<th>创建时间:</th>
					<td><input id ='createTimeStart'  name="createTimeStart" placeholder="点击选择时间"
						onclick="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})"
						readonly="readonly" />
						至
						<input name="createTimeEnd"	placeholder="点击选择时间"	onclick="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})"	readonly="readonly" />
						 <a href="javascript:void(0);"	class="easyui-linkbutton"		data-options="iconCls:'icon_search',plain:true"		onclick="searchFun();">查询</a>
						 <a href="javascript:void(0);"	class="easyui-linkbutton"	data-options="iconCls:'icon_cancel',plain:true"		onclick="cleanFun();">清空</a>
					</td>
				</tr>
			</table>
		</form>
	</div>
	<div data-options="region:'center',border:false,title:'订单明细'">
		<table id="dataGrid" data-options="fit:true,border:false"></table>
	</div>
	<div data-options="region:'west',border:false,split:true,title:'订单'"
		style="width:200px;overflow: hidden; ">
		<ul id="orderTree" style="width:180px;margin: 10px 10px 10px 10px">
		</ul>
	</div>
</body>
</html>