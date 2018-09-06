<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script type="text/javascript">
	$(function() {
		$('#setPasswordForm').form({
			url : '${ctx}/card/submitPsw',
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
					parent.$.messager.alert('提示', result.msg, 'warning');
				} else {
					parent.$.messager.alert('提示', result.msg, 'warning');
				}
			}
		});
	});
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false" title="" style="overflow: hidden;padding: 3px;">
		<form id="setPasswordForm" method="post">
			<table class="grid">
				<tr>
					<td>卡号</td>
					<td><input name="cardNo" type="text"  class="easyui-validatebox"  value="${cardNo}"  readonly="readonly"></td>
				</tr>
				<tr>
					<td>当前密码</td>
					<td><input id="password" name="password"  class="easyui-validatebox" required="true" type="password" value=""/> 
					</td>
				</tr>
				<tr>
					<td>新密码</td>
					<td><input id="newPassword" name="newPassword"  class="easyui-validatebox" required="true" type="password" value=""/> 
					</td>
				</tr>
				<tr>
					<td>确认密码</td>
					<td><input id="comfirmPassword" name="comfirmPassword"    validType="equalTo['#newPassword']" invalidMessage="两次输入密码不匹配"  class="easyui-validatebox" required="true" type="password" value=""/> 
				</tr>
			</table>
		</form>
	</div>
</div>