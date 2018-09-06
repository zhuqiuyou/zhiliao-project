<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script type="text/javascript">
	$(function() {
		$('#productEditForm').form({
			url : '${ctx}/product/edit',
			onSubmit : function() {
				progressLoad();
				var isValid = $(this).form('validate');
				 var t = $('input[name=cardBin]').val();
					if(!(/^[0-9]{10}$/.test(t))){
						parent.$.messager.alert('提示',"卡Bin应该为10位数字", 'warning');
						isValid = false ; 
					}
				if (!isValid) {
					progressClose();
				}
				return isValid;
			},
			success : function(result) {
				progressClose();
				result = $.parseJSON(result);
				if (result.success) {
					parent.$.modalDialog.openner_dataGrid.datagrid('reload');//之所以能在这里调用到parent.$.modalDialog.openner_dataGrid这个对象，是因为user.jsp页面预定义好了
					parent.$.modalDialog.handler.dialog('close');
				} else {
					parent.$.messager.alert('错误', result.msg, 'error');
				}
			}
		});
		pubMethod.bind('usertype', 'usertype');
	});
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false" title="" style="overflow: hidden;padding: 3px;">
		<form id="productEditForm" method="post">
			<table class="grid">
				<tr>
					<input name="productCode" type="hidden"  value="${product.productCode}">
					<input name="lockVersion" type="hidden"  value="${product.lockVersion}">
					<input name="dataStat" type="hidden"  value="${product.dataStat}">
					<td>产品名称</td>
					<td><input name="productName" type="text" placeholder="请输入产品名称" class="easyui-validatebox" data-options="required:true" value="${product.productName}" maxlength="128"></td>
					<td>密钥版本</td>
					<td>
					<select name="versionId" class="easyui-combobox" data-options="width:140,height:29,editable:false,panelHeight:'auto'">
						<c:forEach items="${versiontypeJson}" var="ktj">
								<option value="${ktj.versionId}" <c:if test="${ktj.versionId == product.versionId}">selected="selected"</c:if>>${ktj.versionCode }</option>
						</c:forEach>
					</select></td>
				</tr>
				<tr>
					<td>卡BIN</td>
					<td><input name="cardBin" type="text"  class="easyui-validatebox" data-options="required:true"  validType='length[10,10]''  invalidMessage='卡Bin的长度为10位''  maxlength="10" value="${product.cardBin }" ></td>
					<td>署名类型</td>
					<td>	<select name="onymousStat" class="easyui-combobox" data-options="width:140,height:29,editable:false,panelHeight:'auto'">
						<c:forEach items="${onymousStatList}" var="ktj">
							<option value="${ktj.key}" <c:if test="${ktj.key == product.onymousStat}">selected="selected"</c:if>>${ktj.value }</option>
						</c:forEach>
					</select></td>
				</tr>
				<tr>
					<td>业务类型</td>
					<td><select name="businessType" class="easyui-combobox" data-options="width:140,height:29,editable:false,panelHeight:'auto'">
						<c:forEach items="${businessTypeList}" var="ktj">
							<option value="${ktj.key}" <c:if test="${ktj.key == product.businessType}">selected="selected"</c:if>>${ktj.value }</option>
						</c:forEach>
					</select></td>
					<td>卡产品类型</td>
					<td><select name="productType" class="easyui-combobox" data-options="width:140,height:29,editable:false,panelHeight:'auto'">
						<c:forEach items="${productTypeList}" var="ktj">
							<option value="${ktj.key}" <c:if test="${ktj.key == product.productType}">selected="selected"</c:if>>${ktj.value }</option>
						</c:forEach>
					</select></td>
				</tr>
				<tr>
					<td>有效期月数</td>
					<td><input name="validityPeriod" type="text"  class="easyui-numberbox" value="${product.validityPeriod}"  data-options="validType:['compareTo[0]','maxLength[4]']" ></td>
					<td>最大余额</td>
					<td><input name="maxBalance" type="text"  class="easyui-numberbox"  precision="2" value="${product.maxBalance}" data-options="validType:['compareTo[0]','maxLength[12]']" ></td>
				</tr>
				<tr>
					<td>消费次数</td>
					<td><input name="consumTimes" type="text"  class="easyui-numberbox" value="${product.consumTimes}" data-options="validType:['number','maxLength[6]']"  ></td>
					<td>充值次数</td>
					<td><input name="rechargeTimes" type="text"  class="easyui-numberbox" value="${product.rechargeTimes}" data-options="validType:['number','maxLength[6]']" ></td>
				</tr>
				<tr>
					<td>cvv2错误次数</td>
					<td><input name="cvv2ErrTimes" type="text"  class="easyui-numberbox" value="${product.cvv2ErrTimes}" data-options="validType:['number','maxLength[2]']"  ></td>
					<td>最终卡序号</td>
					<td><input name="lastCardNum" type="text"  class="easyui-validatebox" value="${product.lastCardNum}"  readonly="readonly"></td>
				</tr>				
			</table>
		</form>
	</div>
</div>