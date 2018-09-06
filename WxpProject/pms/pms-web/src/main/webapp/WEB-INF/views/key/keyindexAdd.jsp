<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script type="text/javascript">
	$(function() {
		$('#pid').combotree({
			url : '${ctx}/keyversion/treeWithoutIndex',
			parentField : 'pid',
			lines : true,
			required:true,
			panelHeight : 'auto',
			onSelect:function(node) {
				if(node && node.attributes){
					if(node.attributes[0]=='01'){ // 账户密钥版本
						$('#account').show();
						$('#pos').hide();
					}else if(node.attributes[0]=='02'){ // 终端密钥版本
						$('#account').hide();
						$('#pos').show();
					}else{
						$('#account').show();
						$('#pos').show();
					}
					$('#versionTypeId').val(node.attributes[0]);
					$('#versionType').val(node.attributes[1]);
					
					var accs = $("input[name='accountKeyType']");
					for (var int = 0; int < accs.length; int++) {
						accs[int].value='';
					}
					var pos = $("input[name='posKeyType']");
					for (var int = 0; int < pos.length; int++) {
						pos[int].value='';
					}
				}
            }
		});
		$('#keyindexAddForm').form({
			url : '${ctx}/keyindex/add',
			onSubmit : function() {
				progressLoad();
				var isValid = true;
				var versionTypeId = $('#versionTypeId').val();
				var keyName=[] ;
				var keyIndex=[];
				var remarks =[];
				if(versionTypeId =='01'){
					var accs = $("input[name='accountKeyType']");
					var accKeyTypes = $("input[name='accountKeyName']");
					var remark = $("input[name='accountRemarks']");
					for (var int = 0; int < accs.length; int++) {
						isValid = $("#account").form("validate");
						if (!isValid) {
							isValid = false;
							break;
						}
						keyName[int] = accs[int].value;
						keyIndex[int] = accKeyTypes[int].value;
						remarks[int] = remark[int].value;
					}
				}else if(versionTypeId =='02'){
					var pos = $("input[name='posKeyType']");
					var posKeyTypes = $("input[name='posKeyName']");
					var remark = $("input[name='posRemarks']");
					for (var int = 0; int < pos.length; int++) {
						isValid = $("#pos").form("validate");
						if (!isValid) {
							isValid = false;
							break;
						}
						keyName[int] = pos[int].value;
						keyIndex[int] = posKeyTypes[int].value;
						remarks[int] = remark[int].value;
					}
				}else{
					isValid = false;
				}
				if (!isValid) {
					progressClose();
				}
				$('#keyName').val(keyName);
				$('#keyIndex').val(keyIndex);
				$('#remarks').val(remarks);
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
	<form id="keyindexAddForm" method="post">
		<input type="hidden" id = "keyName" name="keyName"  />
		<input type="hidden" id = "keyIndex" name="keyIndex"  />
		<input type="hidden" id = "remarks" name="remarks"  />
		<table class="grid">
			<tr>
				<td>版本</td>
				<td><select id="pid" name="versionId" style="width: 140px; height: 29px;" ></select></td>
				<td>类型</td>
				<input type="hidden" id = "versionTypeId" name="versionTypeId"  />
				<td><input id ='versionType' name="versionType" type="text"  class="easyui-validatebox"  style="width: 140px; height: 29px;"  readonly="readonly"></td>
			</tr>
		</table>
		<div id ='account' style="display:none;"> 账户密钥索引明细
			<table class="grid" >
					<c:forEach items="${accountTypeJson}" var="ktj">
						<tr>
							<td>索引名</td>
							<input type="hidden" name="accountKeyName" value="${ktj.code }"/>
							<td><input  type="text"  class="easyui-validatebox"  style="width: 90px; height: 29px;"  value=' ${ktj.text }' readonly="readonly"></td>
							<td>索引值</td>
							<td><input name="accountKeyType" type="text"  class="easyui-validatebox"  data-options="required:true,validType:'fromOneToShousand'"  maxlength="3" style="width: 140px; height: 29px;" ></td>
							<td>备注</td>
							<td><input name="accountRemarks" type="text"  class="easyui-validatebox"  data-options="validType:'maxLengthChar[256]'"  maxlength="256"  style="width: 140px; height: 29px;" value='' /></td>
						</tr>
					</c:forEach>	
			</table>
		</div>
		<div id ='pos'  style="display:none;">终端密钥索引明细
			<table class="grid" >
					<c:forEach items="${posTypeJson}"  var="ktj">
						<tr>
							<td>索引名</td>
								<input type="hidden" name="posKeyName" value="${ktj.code }"/>
							<td><input  type="text"  class="easyui-validatebox"  style="width: 90px; height: 29px;"  value=' ${ktj.text }'  readonly="readonly"></td>
							<td>索引值</td>
							<td><input name="posKeyType" type="text"  class="easyui-validatebox"   data-options="required:true,validType:'fromOneToShousand'"   maxlength="3" style="width: 140px; height: 29px;" ></td>
							<td>备注</td>
							<td><input name="posRemarks" type="text"  class="easyui-validatebox"   data-options="validType:'maxLengthChar[256]'" maxlength="256"  style="width: 140px; height: 29px;" value='' /></td>
						</tr>
					</c:forEach>	
			</table>
		</div>
	</form>
</div>