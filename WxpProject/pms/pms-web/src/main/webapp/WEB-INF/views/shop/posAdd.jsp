<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script type="text/javascript">
	$(function() {
		$('#posAddForm').form({
			url : '${ctx}/pos/add',
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
	$("#shopId").combobox({
			onChange: function(newShopId,o){
				$("#mchntName").val("");
				$("#mchntCode").val("");
				$("#shopCode").val("");
				if(newShopId==null || newShopId==''){
					return;
				}
				$.post('${ctx}/pos/initRef', {
					shopId : newShopId
				}, function(result) {
					if (result.success) {
						if(result.obj.MerchantInf){
							$("#mchntName").val(result.obj.MerchantInf.mchntName);
							$("#mchntCode").val(result.obj.MerchantInf.mchntCode);
						}
						if(result.obj.ShopInf){
							$("#shopCode").val(result.obj.ShopInf.shopCode);
						}
					}
				}, 'JSON');
			}
	});
	$("#versionId").combobox({
		onChange: function(versionId,o){
			$("#pikIndex").val("");
			$("#tmkIndex").val("");
			$("#makIndex").val("");
			$("#posIndex").val("");
			if(versionId==null || versionId==''){
				return;
			}
			$.post('${ctx}/pos/initRef', {
				versionId : versionId
			}, function(result) {
				if (result.success) {
					var indexList = result.obj.KeyIndexList
					if(indexList){
						for(var i=0;i<indexList.length;i++){
							if (indexList[i].keyName=='PIK') {
								$("#pikIndex").val(indexList[i].keyIndex);
								continue;
							}
							if (indexList[i].keyName=='TMK') {
								$("#tmkIndex").val(indexList[i].keyIndex);	
								continue;
							}
							if (indexList[i].keyName=='MAK') {
								$("#makIndex").val(indexList[i].keyIndex);
								continue;
							}
							if (indexList[i].keyName=='POS') {
								$("#posIndex").val(indexList[i].keyIndex);
								continue;
							}
						}
					}
				}
			}, 'JSON');
		},
		onBeforeLoad: function(value,o){
			var defaultVersionId = '${defaultVersionId}';
			$(this).combobox('setValue',defaultVersionId);
		}
	}) ;
});

function doSome (value) {
	if(value != "20"){
		$('#nopintTxnAmt').numberbox({
		    value:0,
		    editable:false
		});
	} else {
		$('#nopintTxnAmt').numberbox({
		    editable:true
		});
	}
}
</script>
<div style="padding: 3px;">
	<form id="posAddForm" method="post">
		<input type="hidden" name="termSigninStat" value="0"/>
		<input type="hidden" name="termBatchNo"  value="1" />
		<input type="hidden" name="prmType3"  value="10003">
		<input type="hidden" name="prmVersion3"  value="0">
		<input type="hidden" name="prmChangeFlag3" value="1" >
		<input type="hidden" name="prmType1" value="10001">
		<input type="hidden" name="prmType2" value="10002">
		<input type="hidden" name="currTxnCnt"  value="0">
		<input type="hidden" name="lastTxnTime" value="">
		<input type="hidden" name="tmkDownTime" value="0">
		<input type="hidden" name="termTransFlag" value="1" >
		<table class="grid">
				<tr>
					<td>终端号</td>
					<td><input name="termId" type="text" placeholder="请输入终端号"  data-options="required:true,validType:'maxLengthChar[8]'"  class="easyui-validatebox" value="" maxlength="8"></td>
					<td>门店</td>
					<td>
						<select id="shopId" name="shopId"  class="easyui-combobox" data-options="required:true,width:140,height:29,editable:false,panelHeight:'auto'">
						<option selected value="">请选择</option>
						<c:forEach items="${shopInfList}" var="ktj" >
							<option value="${ktj.shopId }" >${ktj.shopName }</option>
						</c:forEach>
						</select>
					</td>
					<td>密钥版本</td>
					<td>
						<select id="versionId" name="versionId"  class="easyui-combobox" data-options="required:true,width:140,height:29,editable:false,panelHeight:'auto'">
							<option selected value="">请选择</option>
							<c:forEach items="${keyVersionList}" var="ktj">
								<option value="${ktj.versionId }" >${ktj.versionCode }</option>
							</c:forEach>
						</select>
					</td>
				</tr>
				<tr>
					<td>门店编号</td>
					<td><input id="shopCode" name="shopCode" type="text" class="easyui-validatebox" readonly="readonly"></td>
					<td>商户名称</td>
					<td><input id="mchntName"  name="mchntName" type="text" class="easyui-validatebox"readonly="readonly"></td>
					<td>商户号</td>
					<td><input id="mchntCode"  name="mchntCode" type="text" class="easyui-validatebox" readonly="readonly"></td>
				</tr>
				<tr>		
					<td>联机参数版本</td>
					<td>
						<select id="prmVersion1" name="prmVersion1" class="easyui-combobox" data-options="width:140,height:29,editable:false,panelHeight:'auto'" >
							<c:forEach items="${prmVersion1List}" var="ktj">
								<option value="${ktj.prmVersion}" >${ktj.prmVersion}</option>
							</c:forEach>
						</select>
						</td>
					<td>联机下载参数标志</td>
					<td>
						<select id="prmChangeFlag1" name="prmChangeFlag1" class="easyui-combobox" data-options="width:140,height:29,editable:false,panelHeight:'auto'" >
							<c:forEach items="${pos_IC_download_flag}" var="ktj">
								<option value="${ktj.key}" >${ktj.value}</option>
							</c:forEach>
						</select>
					</td>
					<td>终端类型</td>
					<td><input name="termModel" type="text" class="easyui-validatebox" data-options="required:true,validType:'maxLengthChar[64]'" value="" maxlength="64"></td>
				</tr>	
				<tr>			
					<td>卡BIN版本</td>
					<td>
						<select id="prmVersion2" name="prmVersion2" class="easyui-combobox" data-options="width:140,height:29,editable:false,panelHeight:'auto'" >
							<c:forEach items="${prmVersion2List}" var="ktj">
								<option value="${ktj.prmVersion}" >${ktj.prmVersion}</option>
							</c:forEach>
						</select>
					</td>
					<td>卡BIN下载标志</td>
					<td>
						<select id="prmChangeFlag2" name="prmChangeFlag2" class="easyui-combobox" data-options="width:140,height:29,editable:false,panelHeight:'auto'" >
							<c:forEach items="${pos_IC_download_flag}" var="ktj">
								<option value="${ktj.key}" >${ktj.value}</option>
							</c:forEach>
						</select>
					</td>
					<td>有效标志</td>
					<td>
						<select id="termStat" name="termStat" class="easyui-combobox" data-options="width:140,height:29,editable:false,panelHeight:'auto'" >
							<c:forEach items="${pos_effectlist}" var="ktj">
								<option value="${ktj.key}" <c:if test="${ktj.key == '0'}">selected="selected"</c:if>>${ktj.value}</option>
							</c:forEach>
						</select>
					</td>
							
				</tr>				
				<tr>				
					<td>消费允许标志</td>
					<td><select id="consumePerFlag" name="consumePerFlag" class="easyui-combobox" data-options="width:140,height:29,editable:false,panelHeight:'auto'" >
							<c:forEach items="${allowlist}" var="ktj">
								<option value="${ktj.key}" >${ktj.value}</option>
							</c:forEach>
						</select>
					</td>
					<td>充值允许标志</td>
					<td>
						<select id="reloadPerFlag" name="reloadPerFlag" class="easyui-combobox" data-options="width:140,height:29,editable:false,panelHeight:'auto'" >
							<c:forEach items="${allowlist}" var="ktj">
								<option value="${ktj.key}" >${ktj.value}</option>
							</c:forEach>
						</select>
					</td>
					<td>重打印允许标志</td>
					<td>
						<select id="reprintCtrlFlag" name="reprintCtrlFlag" class="easyui-combobox" data-options="width:140,height:29,editable:false,panelHeight:'auto'" >
							<c:forEach items="${allowlist}" var="ktj">
								<option value="${ktj.key}" >${ktj.value}</option>
							</c:forEach>
						</select>
					</td>
				</tr>
				<tr>
					<td>验证MAC标志</td>
					<td>
						<select id="isValidateMac" name="isValidateMac" class="easyui-combobox" data-options="width:140,height:29,editable:false,panelHeight:'auto'" >
							<c:forEach items="${pos_testList}" var="ktj">
								<option value="${ktj.key}" >${ktj.value}</option>
							</c:forEach>
						</select>
					</td>
					<td>最大交易次数</td>
					<td><input name="maxTxnCnt" type="text" class="easyui-numberbox"  value="99999"></td>
					<td>备注</td>
					<td><input name="remarks" type="text" class="easyui-validatebox" data-options="validType:'maxLengthChar[256]'" value=""></td>
				</tr>
				<tr>	
					<td>主密钥索引</td>
					<td><input id ='posIndex'  name="posIndex" type="text" class="easyui-validatebox" value=""  data-options="required:true" readonly="readonly"></td>
					<td>tmk密钥索引</td>
					<td><input id ='tmkIndex'  name="tmkIndex" type="text" class="easyui-validatebox" value="" data-options="required:true" readonly="readonly"></td>
					<td>pik密钥索引</td>
					<td><input id ='pikIndex'  name="pikIndex" type="text" class="easyui-validatebox" value=""  data-options="required:true" readonly="readonly"></td>
				</tr>
				<tr>	
					<td>mak密钥索引</td>
					<td><input id ='makIndex'   name="makIndex" type="text" class="easyui-validatebox" value=""  data-options="required:true" readonly="readonly"></td>
				</tr>
			</table>
	</form>
</div>