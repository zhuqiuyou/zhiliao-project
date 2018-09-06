<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script type="text/javascript">
	$(function() {
		
		$('#productAddForm').form({
			url : '${ctx}/product/add',
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
					parent.$.modalDialog.openner_dataGrid.datagrid('reload');
					parent.$.modalDialog.handler.dialog('close');
				} else {
					parent.$.messager.alert('提示', result.msg, 'warning');
				}
			}
		});
	});
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false" title="" style="overflow: hidden;padding: 3px;">
		<form id="productAddForm" method="post">
			<table class="grid">
				<tr>
					<td>产品名称</td>
					<td><input name="productName" type="text" placeholder="请输入产品名称" class="easyui-validatebox" data-options="required:true" value="" maxlength="128"></td>
					<td>密钥版本</td>
					<td>
					<select name="versionId" class="easyui-combobox" data-options="width:140,height:29,editable:false,panelHeight:'auto'">
						<c:forEach items="${versiontypeJson}" var="ktj">
							<option value="${ktj.versionId}" <c:if test="${ktj.versionCode == defaultCode}">selected="selected"</c:if>>${ktj.versionCode }</option>
						</c:forEach>
					</select></td>
				</tr>
				<tr>
					<td>卡BIN</td>
					<td><input name="cardBin" type="text"  class="easyui-validatebox" data-options="required:true"  validType='length[10,10]''  invalidMessage='卡Bin的长度为10位''  maxlength="10" value="" ></td>
					<td>署名类型</td>
					<td>	<select name="onymousStat" class="easyui-combobox" data-options="width:140,height:29,editable:false,panelHeight:'auto'">
						<c:forEach items="${onymousStatList}" var="ktj">
							<option value="${ktj.key}">${ktj.value }</option>
						</c:forEach>
					</select></td>
				</tr>
				<tr>
					<td>业务类型</td>
					<td><select name="businessType" class="easyui-combobox" data-options="width:140,height:29,editable:false,panelHeight:'auto'">
						<c:forEach items="${businessTypeList}" var="ktj">
							<option value="${ktj.key}">${ktj.value }</option>
						</c:forEach>
					</select></td>
					<td>卡产品类型</td>
					<td><select name="productType" class="easyui-combobox" data-options="width:140,height:29,editable:false,panelHeight:'auto'">
						<c:forEach items="${productTypeList}" var="ktj">
							<option value="${ktj.key}">${ktj.value }</option>
						</c:forEach>
					</select></td>
				</tr>
				<tr>
					<td>有效期月数</td>
					<td><input name="validityPeriod" type="text"  class="easyui-numberbox" value="" data-options="validType:['compareTo[0]','maxLength[4]']" ></td>
					<td>最大余额</td>
					<td>
					 <input name="maxBalance" type="text"  class="easyui-numberbox"  precision="2" value="" data-options="validType:['compareTo[0]','maxLength[12]']" >
					</td>
				</tr>
				<tr>
					<td>消费次数</td>
					<td><input name="consumTimes" type="text"  class="easyui-numberbox" data-options="validType:['number','maxLength[6]']"  value="999999"></td>
					<td>充值次数</td>
					<td><input name="rechargeTimes" type="text"  class="easyui-numberbox" data-options="validType:['number','maxLength[6]']"  value="999999"></td>
				</tr>
				<tr>
					<td>cvv2错误次数</td>
					<td><input name="cvv2ErrTimes" type="text"  class="easyui-numberbox" value="" data-options="validType:['number','maxLength[2]']"  ></td>
					<td>最终卡序号</td>
					<td><input name="lastCardNum" type="text"  class="easyui-validatebox"  value="0" disabled="disabled"></td>
				</tr>				
			</table>
		</form>
	</div>
</div>