<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<script type="text/javascript">
	var dataGridDetail;
	$(function() {
	
		var title = '金额';
		if ('${type}' && '${type}' == '200100') {
			title = '充值金额';
		}
		
		dataGridDetail = $('#dataGridDetail').datagrid({
							url : '${ctx}/orderList/dataGrid?orderId='+'${id}',
							striped : true,
							rownumbers : true,
							pagination : true,
							singleSelect : true,
							idField : 'id',
							sortName : 'createTime',
							sortOrder : 'asc',
							pageSize : 15,
							pageList : [ 10, 15, 20, 30, 40, 50, 100, 200, 300,
									400, 500 ],
							frozenColumns : [ [ {
								width : '150',
								title : '卡号',
								field : 'cardNo',
								sortable : true
							}, {
								width : '80',
								title : '卡片有效期',
								field : 'validDate',
								sortable : true
							}, {
								width : '80',
								title : title,
								field : 'amount',
								sortable : true
							} ] ],
							onLoadSuccess : function () {
						        $(this).datagrid("fixRownumber");
						    },
							
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
										width : '120',
										title : '创建时间',
										field : 'createTime',
										sortable : true
									},
									{
										width : '80',
										title : '创建人',
										field : 'createUser'
									} ] ]
						});
	});
	function searchFun() {
		dataGridDetail.datagrid('load', $.serializeObject($('#searchForm')));
	}
	function cleanFun() {
		$('#searchForm input').val('');
		dataGridDetail.datagrid('load', {});
	}
	
	$.extend($.fn.datagrid.methods, {
		fixRownumber : function (jq) {
		return jq.each(function () {
			var panel = $(this).datagrid("getPanel");
			//获取最后一行的number容器,并拷贝一份
			var clone = $(".datagrid-cell-rownumber", panel).last().clone();
			//由于在某些浏览器里面,是不支持获取隐藏元素的宽度,所以取巧一下
			clone.css({
				"position" : "absolute",
				left : -1000
			}).appendTo("body");
			var width = clone.width("auto").width();
			//默认宽度是25,所以只有大于25的时候才进行fix
			if (width > 25) {
				//多加5个像素,保持一点边距
				$(".datagrid-header-rownumber,.datagrid-cell-rownumber", panel).width(width + 5);
				//修改了宽度之后,需要对容器进行重新计算,所以调用resize
				$(this).datagrid("resize");
				//一些清理工作
				clone.remove();
				clone = null;
			} else {
				//还原成默认状态
				$(".datagrid-header-rownumber,.datagrid-cell-rownumber", panel).removeAttr("style");
			}
		});
	}
});
	
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'north',border:false"
		style="height: 30px; overflow: hidden;background-color: #f4f4f4">
		<form id="searchForm">
			<table>
				<tr>
					<th>卡号:</th>
					<td><input name="cardNo" type="text"  class="easyui-validatebox"   value="">
					<a href="javascript:void(0);"	class="easyui-linkbutton"		data-options="iconCls:'icon_search',plain:true"		onclick="searchFun();">查询</a>
						 <a href="javascript:void(0);"	class="easyui-linkbutton"	data-options="iconCls:'icon_cancel',plain:true"		onclick="cleanFun();">清空</a>
					</td>
				</tr>
			</table>
		</form>
	</div>
	<div id ="searchGrid" data-options="region:'center',border:false,title:'订单明细'">
		<table id="dataGridDetail" data-options="fit:true,border:false"></table>
	</div>
</div>