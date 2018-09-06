<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script type="text/javascript">
	$(function() {
		$('#sysParamAddForm').form({
			url : '${ctx}/systemparam/addLink',
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
					parent.$.modalDialog.openner_dataGrid.datagrid('reload');
					parent.$.modalDialog.handler.dialog('close');
				} else {
					parent.$.messager.alert('提示', result.msg, 'warning');
				}
			}
		});
		
	});
	
	$("#prmType").combobox({
		onChange: function(newShopId,o){
		}
	});
	var prms = ${prms};
	if(prms&& prms.length>0){
		for(var i =0 ;i<prms.length;i++){
			var seletor="tr:eq("+(2+i)+") input";
			var inputs = $(seletor);
			if(inputs && inputs.length>=4){
				var input=inputs[0];
				if(prms[i].prmName==input.value){
					if(prms[i].prmVal && prms[i].prmVal!='')
						inputs[1].value=prms[i].prmVal;
					if(prms[i].prmLen && prms[i].prmLen!='')
						inputs[2].value=prms[i].prmLen;
					if(prms[i].prmDesc && prms[i].prmDesc!='')
						inputs[3].value=prms[i].prmDesc;
				}
			}
		}
	}
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false" title="" style="overflow: hidden;padding: 3px;">
		<form id="sysParamAddForm" method="post">
			<table class="grid">
				<tr>
					<input name="prmType" type="hidden"  value="10001"/>
					<td >版本</td>
					<td colspan="7"><input name="prmVersion" type="text"  class="easyui-numberbox"  style="width: 100px;"  validType='length[0,5]' data-options="required:true" ></td>
				</tr>
				<tr >
					<td>参数名称</td>
					<td><input name="prmName" type="text"  class="easyui-validatebox" style="width: 90px;" value="pos终端应用类型" readonly="readonly"></td>
					<td>参数值</td>
					<td><input name="prmVal" type="text"   class="easyui-validatebox" data-options="required:true"  style="width: 100px;"  value=""  validType='length[0,64]'></td>
					<td>长度</td>
					<td><input name="prmLen" type="text"    class="easyui-numberbox"  style="width: 50px;" data-options="required:true"  validType='length[0,2]'></td>
					<td>描述</td>
					<td><input type="prmDesc"  name="prmDesc" validType='length[0,64]'></td>
				</tr>
				<tr>
					<td>参数名称</td>
					<td><input name="prmName" type="text"  class="easyui-validatebox"  style="width: 90px;" value="超时时间" readonly="readonly"></td>
					<td>参数值</td>
					<td><input name="prmVal" type="text"  class="easyui-validatebox" data-options="required:true"  style="width: 100px;"  value="" validType='length[0,64]'></td>
					<td>长度</td>
					<td><input name="prmLen" type="text"  class="easyui-numberbox"  style="width: 50px;"  data-options="required:true"  validType='length[0,2]'></td>
					<td>描述</td>
					<td><input type="prmDesc" name="prmDesc" validType='length[0,64]'></td>
				</tr>
				<tr>
					<td>参数名称</td>
					<td><input name="prmName" type="text"  class="easyui-validatebox"  style="width: 90px;" value="重试次数" readonly="readonly"></td>
					<td>参数值</td>
					<td><input name="prmVal" type="text"  class="easyui-validatebox" data-options="required:true"  style="width: 100px;"  value="" validType='length[0,64]'></td>
					<td>长度</td>
					<td><input name="prmLen" type="text"  class="easyui-numberbox"  style="width: 50px;" data-options="required:true"   validType='length[0,2]'></td>
					<td>描述</td>
					<td><input type="prmDesc" name="prmDesc" validType='length[0,64]'></td>
				</tr>
				<tr>
					<td>参数名称</td>
					<td><input name="prmName" type="text"  class="easyui-validatebox"  style="width: 90px;" value="电话号码一"  readonly="readonly"></td>
					<td>参数值</td>
					<td><input name="prmVal" type="text"  class="easyui-validatebox" data-options="required:true"  style="width: 100px;"  value="" validType='length[0,64]'></td>
					<td>长度</td>
					<td><input name="prmLen" type="text"  class="easyui-numberbox"  style="width: 50px;"data-options="required:true"   validType='length[0,2]'></td>
					<td>描述</td>
					<td><input type="prmDesc" name="prmDesc" validType='length[0,64]'></td>
				</tr>
				<tr>
					<td>参数名称</td>
					<td><input name="prmName" type="text"  class="easyui-validatebox"  style="width: 90px;" value="电话号码二"  readonly="readonly"></td>
					<td>参数值</td>
					<td><input name="prmVal" type="text"  class="easyui-validatebox" data-options="required:true"  style="width: 100px;"  value="" validType='length[0,64]'></td>
					<td>长度</td>
					<td><input name="prmLen" type="text"  class="easyui-numberbox"  style="width: 50px;" data-options="required:true"  validType='length[0,2]'></td>
					<td>描述</td>
					<td><input type="prmDesc" name="prmDesc" validType='length[0,64]'></td>
				</tr>
				<tr>
					<td>参数名称</td>
					<td><input name="prmName" type="text"  class="easyui-validatebox" style="width: 90px;" value="主机地址一" readonly="readonly"></td>
					<td>参数值</td>
					<td><input name="prmVal" type="text"  class="easyui-validatebox" data-options="required:true"  style="width: 100px;"  value="" validType='length[0,64]'></td>
					<td>长度</td>
					<td><input name="prmLen" type="text"  class="easyui-numberbox"  style="width: 50px;" data-options="required:true"  validType='length[0,2]'></td>
					<td>描述</td>
					<td><input type="prmDesc" name="prmDesc" validType='length[0,64]'></td>
				</tr>
				<tr>
					<td>参数名称</td>
					<td><input name="prmName" type="text"  class="easyui-validatebox" style="width: 90px;" value="主机端口一" readonly="readonly"></td>
					<td>参数值</td>
					<td><input name="prmVal" type="text"  class="easyui-validatebox" data-options="required:true"  style="width: 100px;"  value=""  validType='length[0,64]'></td>
					<td>长度</td>
					<td><input name="prmLen" type="text"  class="easyui-numberbox"  style="width: 50px;" data-options="required:true"  validType='length[0,2]'></td>
					<td>描述</td>
					<td><input type="prmDesc" name="prmDesc" validType='length[0,64]'></td>
				</tr>
				<tr>
					<td>参数名称</td>
					<td><input name="prmName" type="text"  class="easyui-validatebox" style="width: 90px;" value="主机地址二" readonly="readonly"></td>
					<td>参数值</td>
					<td><input name="prmVal" type="text"  class="easyui-validatebox" data-options="required:true"  style="width: 100px;"  value=""  validType='length[0,64]'></td>
					<td>长度</td>
					<td><input name="prmLen" type="text"  class="easyui-numberbox"  style="width: 50px;"  data-options="required:true" validType='length[0,2]'></td>
					<td>描述</td>
					<td><input type="prmDesc" name="prmDesc" validType='length[0,64]'></td>
				</tr>
				<tr>
					<td>参数名称</td>
					<td><input name="prmName" type="text"  class="easyui-validatebox" style="width: 90px;" value="主机端口二" readonly="readonly"></td>
					<td>参数值</td>
					<td><input name="prmVal" type="text"  class="easyui-validatebox" data-options="required:true"  style="width: 100px;"  value=""  validType='length[0,64]'></td>
					<td>长度</td>
					<td><input name="prmLen" type="text"  class="easyui-numberbox"  style="width: 50px;" data-options="required:true"  validType='length[0,2]'></td>
					<td>描述</td>
					<td><input type="prmDesc" name="prmDesc" validType='length[0,64]'></td>
				</tr>
				<tr>
					<td>参数名称</td>
					<td><input name="prmName" type="text"  class="easyui-validatebox"  style="width: 90px;" value="商户交易启用标志" readonly="readonly"></td>
					<td>参数值</td>
					<td><input name="prmVal" type="text"  class="easyui-validatebox" data-options="required:true"  style="width: 100px;"  value=""  validType='length[0,64]'></td>
					<td>长度</td>
					<td><input name="prmLen" type="text"  class="easyui-numberbox"  style="width: 50px;" data-options="required:true"  validType='length[0,2]'></td>
					<td>描述</td>
					<td><input type="prmDesc" name="prmDesc" validType='length[0,64]'></td>
				</tr>
				<tr>
					<td>参数名称</td>
					<td><input name="prmName" type="text"  class="easyui-validatebox"  style="width: 90px;" value="商户充值启用标志" readonly="readonly"></td>
					<td>参数值</td>
					<td><input name="prmVal" type="text"  class="easyui-validatebox" data-options="required:true"  style="width: 100px;"  value="" validType='length[0,64]'></td>
					<td>长度</td>
					<td><input name="prmLen" type="text"  class="easyui-numberbox"  style="width: 50px;" data-options="required:true"  validType='length[0,2]'></td>
					<td>描述</td>
					<td><input type="prmDesc" name="prmDesc" validType='length[0,64]'></td>
				</tr>
				<tr>
					<td>参数名称</td>
					<td><input name="prmName" type="text"  class="easyui-validatebox"  style="width: 90px;" value="商户名称" readonly="readonly"></td>
					<td>参数值</td>
					<td><input name="prmVal" type="text"  class="easyui-validatebox" data-options="required:true"  style="width: 100px;"  value="" validType='length[0,64]'></td>
					<td>长度</td>
					<td><input name="prmLen" type="text"  class="easyui-numberbox"  style="width: 50px;" data-options="required:true"   validType='length[0,2]'></td>
					<td>描述</td>
					<td><input type="prmDesc" name="prmDesc" validType='length[0,64]'></td>
				</tr>
				<tr>
					<td>参数名称</td>
					<td><input name="prmName" type="text"  class="easyui-validatebox"  style="width: 90px;" value="交易重发次数" readonly="readonly"></td>
					<td>参数值</td>
					<td><input name="prmVal" type="text"  class="easyui-validatebox" data-options="required:true"  style="width: 100px;"  value="" validType='length[0,64]'></td>
					<td>长度</td>
					<td><input name="prmLen" type="text"  class="easyui-numberbox"  style="width: 50px;"  data-options="required:true"  validType='length[0,2]'></td>
					<td>描述</td>
					<td><input type="prmDesc" name="prmDesc" validType='length[0,64]'></td>
				</tr>
				<tr>
					<td>参数名称</td>
					<td><input name="prmName" type="text"  class="easyui-validatebox"  style="width: 90px;" value="重打密码控制" readonly="readonly"></td>
					<td>参数值</td>
					<td><input name="prmVal" type="text"  class="easyui-validatebox" data-options="required:true"  style="width: 100px;"  value="" validType='length[0,64]'></td>
					<td>长度</td>
					<td><input name="prmLen" type="text"  class="easyui-numberbox"  style="width: 50px;" data-options="required:true"   validType='length[0,2]'></td>
					<td>描述</td>
					<td><input type="prmDesc" name="prmDesc" validType='length[0,64]'></td>
				</tr>
			</table>
		</form>
	</div>
</div>