<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script type="text/javascript">
	$(function() {
		$('#posEditForm').form({
			url : '${ctx}/pos/edit',
			onSubmit : function() {
				progressLoad();
				var isValid = $(this).form('validate');
				if (!isValid) {
					progressClose();
				}
				return isValid;
			},
			success : function(result) {
				progressClose();
				result = $.parseJSON(result);
				if (result.success) {
					parent.$.modalDialog.openner_dataGrid.datagrid('reload');//
					parent.$.modalDialog.handler.dialog('close');
				}else{
					parent.$.messager.alert('提示', result.msg, 'warning');
				}
			}
		});
});
</script>
<div style="padding: 3px;">
	<form id="posEditForm" method="post">
		<input type="hidden" name="id" value="${pos.id}"/>
		<input type="hidden" name="termSigninStat" value="${pos.termSigninStat}"/>
		<input type="hidden" name="termBatchNo"  value="${pos.termBatchNo}" />
		<input type="hidden" name="prmType3"  value="${pos.prmType3}">
		<input type="hidden" name="prmVersion3"  value="${pos.prmVersion3}">
		<input type="hidden" name="prmChangeFlag3" value="${pos.prmChangeFlag3}" >
		<input type="hidden" name="prmType1" value="${pos.prmType1}">
		<input type="hidden" name="prmType2" value="${pos.prmType2}">
		<input type="hidden" name="currTxnCnt"  value="${pos.currTxnCnt}">
		<input type="hidden" name="lastTxnTime" value="${pos.lastTxnTime}">
		<input type="hidden" name="termTransFlag" value="${pos.termTransFlag}">
		<input type="hidden" name="lockVersion" value="${pos.lockVersion}">
		<table class="grid">
				<tr>
					<td>终端号</td>
					<td><input name="termId" type="text"  value="${pos.termId}" readonly = "readonly"></td>
					<td>门店</td>
					<td>
						<select name="shopId"  class="easyui-combobox" data-options="required:true,width:140,height:29,editable:false,panelHeight:'auto'" readonly = "readonly">
							<c:forEach items="${shopInfList}" var="ktj" >
								<option value="${ktj.shopId }" <c:if test="${ktj.shopId == pos.shopId}">selected="selected"</c:if>>${ktj.shopName }</option>
							</c:forEach>
					</select>
					<td>密钥版本</td>
					<td>
						<select name="versionId"  class="easyui-combobox" data-options="required:true,width:140,height:29,editable:false,panelHeight:'auto'" readonly = "readonly">
							<c:forEach items="${keyVersionList}" var="ktj">
								<option value="${ktj.versionId }" <c:if test="${ktj.versionId == pos.versionId}">selected="selected"</c:if>>${ktj.versionCode }</option>
							</c:forEach>
						</select>
					</td>
				</tr>
				<tr>
					<td>门店编号</td>
					<td><input id="shopCode" name="shopCode" type="text" class="easyui-validatebox" readonly="readonly" value="${pos.shopCode}"></td>
					<td>商户名称</td>
					<td><input id="mchntName"  name="mchntName" type="text" class="easyui-validatebox"readonly="readonly" value="${pos.mchntName}"></td>
					<td>商户号</td>
					<td><input id="mchntCode"  name="mchntCode" type="text" class="easyui-validatebox" readonly="readonly" value="${pos.mchntCode}"></td>
				</tr>
				<tr>		
					<td>联机参数版本</td>
					<td>
						<select id="prmVersion1" name="prmVersion1" class="easyui-combobox" data-options="width:140,height:29,editable:false,panelHeight:'auto'" >
							<c:forEach items="${prmVersion1List}" var="ktj">
								<option value="${ktj.prmVersion}"   <c:if test="${ktj.prmVersion == pos.prmVersion1}">selected="selected"</c:if>>${ktj.prmVersion}</option>
							</c:forEach>
						</select>
					</td>
					<td>联机下载参数标志</td>
					<td>
						<select name="prmChangeFlag1" class="easyui-combobox" data-options="width:140,height:29,editable:false,panelHeight:'auto'" >
							<c:forEach items="${pos_IC_download_flag}" var="ktj">
								<option value="${ktj.key}" <c:if test="${ktj.key == pos.prmChangeFlag1}">selected="selected"</c:if>>${ktj.value}</option>
							</c:forEach>
						</select>
					</td>
					<td>终端类型</td>
					<td><input name="termModel" type="text" class="easyui-validatebox" data-options="required:true,validType:'maxLengthChar[64]'" maxlength="64" value="${pos.termModel}" ></td>
				
				</tr>	
				<tr>			
					<td>卡BIN版本</td>
					<td>
						<select id="prmVersion2" name="prmVersion2" class="easyui-combobox" data-options="width:140,height:29,editable:false,panelHeight:'auto'" >
							<c:forEach items="${prmVersion2List}" var="ktj">
								<option value="${ktj.prmVersion}"  <c:if test="${ktj.prmVersion == pos.prmVersion2}">selected="selected"</c:if>>${ktj.prmVersion}</option>
							</c:forEach>
						</select>
					</td>
					<td>卡BIN下载标志</td>
					<td>
						<select name="prmChangeFlag2" class="easyui-combobox" data-options="width:140,height:29,editable:false,panelHeight:'auto'" >
							<c:forEach items="${pos_IC_download_flag}" var="ktj">
								<option value="${ktj.key}" <c:if test="${ktj.key == pos.prmChangeFlag2}">selected="selected"</c:if>>${ktj.value}</option>
							</c:forEach>
						</select>
					</td>
					<td>有效标志</td>
					<td>
						<select name="termStat" class="easyui-combobox" data-options="width:140,height:29,editable:false,panelHeight:'auto'" >
							<c:forEach items="${pos_effectlist}" var="ktj">
								<option value="${ktj.key}" <c:if test="${ktj.key == pos.termStat}">selected="selected"</c:if>>${ktj.value}</option>
							</c:forEach>
						</select></td>
					
				</tr>				
				<tr>				
					<td>消费允许标志</td>
					<td>
						<select  name="consumePerFlag" class="easyui-combobox" data-options="width:140,height:29,editable:false,panelHeight:'auto'" >
							<c:forEach items="${allowlist}" var="ktj">
								<option value="${ktj.key}" <c:if test="${ktj.key == pos.consumePerFlag}">selected="selected"</c:if>>${ktj.value}</option>
							</c:forEach>
						</select></td>
					<td>充值允许标志</td>
					<td>
						<select name="reloadPerFlag" class="easyui-combobox" data-options="width:140,height:29,editable:false,panelHeight:'auto'" >
							<c:forEach items="${allowlist}" var="ktj">
								<option value="${ktj.key}" <c:if test="${ktj.key == pos.reloadPerFlag}">selected="selected"</c:if>>${ktj.value}</option>
							</c:forEach>
						</select>
					</td>
					<td>重打印允许标志</td>
					<td>
						<select  name="reprintCtrlFlag" class="easyui-combobox" data-options="width:140,height:29,editable:false,panelHeight:'auto'" >
							<c:forEach items="${allowlist}" var="ktj">
								<option value="${ktj.key}" <c:if test="${ktj.key == pos.reprintCtrlFlag}">selected="selected"</c:if>>${ktj.value}</option>
							</c:forEach>
						</select>
					</td>
				</tr>
				<tr>
					<td>验证MAC标志</td>
					<td>
						<select name="isValidateMac" class="easyui-combobox" data-options="width:140,height:29,editable:false,panelHeight:'auto'" >
							<c:forEach items="${pos_testList}" var="ktj">
								<option value="${ktj.key}" <c:if test="${ktj.key == pos.isValidateMac}">selected="selected"</c:if>>${ktj.value}</option>
							</c:forEach>
						</select>
					</td>
					<td>最大交易次数</td>
					<td><input name="maxTxnCnt" type="text" class="easyui-numberbox"  value="${pos.maxTxnCnt}"></td>
					<td>备注</td>
					<td><input name="remarks" type="text" class="easyui-validatebox" data-options="validType:'maxLengthChar[256]'" value="${pos.remarks}"></td>
					
				</tr>
				<tr>	
					<td>主密钥索引</td>
					<td><input id ='posIndex'  name="posIndex" type="text" class="easyui-validatebox" value="${pos.posIndex}"  data-options="required:true" readonly="readonly"></td>
					<td>tmk密钥索引</td>
					<td><input id ='tmkIndex'  name="tmkIndex" type="text" class="easyui-validatebox" value="${pos.tmkIndex}" data-options="required:true" readonly="readonly"></td>
					<td>pik密钥索引</td>
					<td><input id ='pikIndex'  name="pikIndex" type="text" class="easyui-validatebox" value="${pos.pikIndex}"  data-options="required:true" readonly="readonly"></td>
				
				</tr>
				<tr>	
					<td>mak密钥索引</td>
					<td><input id ='makIndex'   name="makIndex" type="text" class="easyui-validatebox" value="${pos.makIndex}"  data-options="required:true" readonly="readonly"></td>
					<td>TMK下载</td>
					<td>
						<select name="tmkDownTime" class="easyui-combobox" data-options="width:140,height:29,editable:false,panelHeight:'auto'" >
							<c:forEach items="${pos_TMK_download_flag}" var="ktj">
								<option value="${ktj.key}"  <c:if test="${ktj.key == pos.tmkDownTime}">selected="selected"</c:if>>${ktj.value}</option>
							</c:forEach>
						</select>
					</td>
				</tr>
			</table>
	</form>
</div>