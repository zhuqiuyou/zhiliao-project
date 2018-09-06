<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script type="text/javascript">
	
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false" title="" style="overflow: hidden;padding: 3px;">
		<form id="productEditForm" method="post">
			<table class="grid">
				<tr>
					<td>产品名称</td>
					<td><input name="productName" type="text" class="easyui-validatebox"  value="${product.productName}"  readonly="readonly"/></td>
					<td>密钥版本</td>
					<td>
					<select name="versionId" class="easyui-combobox" data-options="width:140,height:29,editable:false,panelHeight:'auto'" readonly="readonly">
						<c:forEach items="${versiontypeJson}" var="ktj">
								<option value="${ktj.versionId}" <c:if test="${ktj.versionId == product.versionId}">selected="selected"</c:if>>${ktj.versionCode }</option>
						</c:forEach>
					</select></td>
				</tr>
				<tr>
					<td>卡BIN</td>
					<td><input name="cardBin" type="text"  class="easyui-validatebox"   value="${product.cardBin }"  readonly="readonly"></td>
					<td>署名类型</td>
					<td>	<select name="onymousStat" class="easyui-combobox" data-options="width:140,height:29,editable:false,panelHeight:'auto'" readonly="readonly">
						<c:forEach items="${onymousStatList}" var="ktj">
							<option value="${ktj.key}" <c:if test="${ktj.key == product.onymousStat}">selected="selected"</c:if>>${ktj.value }</option>
						</c:forEach>
					</select></td>
				</tr>
				<tr>
					<td>业务类型</td>
					<td><select name="businessType" class="easyui-combobox" data-options="width:140,height:29,editable:false,panelHeight:'auto'" readonly="readonly">
						<c:forEach items="${businessTypeList}" var="ktj">
							<option value="${ktj.key}" <c:if test="${ktj.key == product.businessType}">selected="selected"</c:if>>${ktj.value }</option>
						</c:forEach>
					</select></td>
					<td>卡产品类型</td>
					<td><select name="productType" class="easyui-combobox" data-options="width:140,height:29,editable:false,panelHeight:'auto'" readonly="readonly">
						<c:forEach items="${productTypeList}" var="ktj">
							<option value="${ktj.key}" <c:if test="${ktj.key == product.productType}">selected="selected"</c:if>>${ktj.value }</option>
						</c:forEach>
					</select></td>
				</tr>
				<tr>
					<td>有效期月数</td>
					<td><input name="validityPeriod" type="text"  class="easyui-numberbox" value="${product.validityPeriod}"  readonly="readonly"></td>
					<td>最大余额</td>
					<td><input name="maxBalance" type="text"  class="easyui-numberbox"  precision="2" value="${product.maxBalance}" readonly="readonly"></td>
				</tr>
				<tr>
					<td>消费次数</td>
					<td><input name="consumTimes" type="text"  class="easyui-numberbox" value="${product.consumTimes}"  readonly="readonly"></td>
					<td>充值次数</td>
					<td><input name="rechargeTimes" type="text"  class="easyui-numberbox" value="${product.rechargeTimes}" readonly="readonly"></td>
				</tr>
				<tr>
					<td>cvv2错误次数</td>
					<td><input name="cvv2ErrTimes" type="text"  class="easyui-validatebox" value="${product.cvv2ErrTimes}"  readonly="readonly"></td>
					<td>最终卡序号</td>
					<td><input name="lastCardNum" type="text"  class="easyui-validatebox" value="${product.lastCardNum}"  readonly="readonly"></td>
				</tr>				
			</table>
		</form>
	</div>
</div>