<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<div class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false" title="" style="overflow: hidden;padding: 3px;">
		<form id="sysParamEditForm" method="post">
			<table class="grid">
				<tr>
					<td>参数名称</td>
					<td><input name="prmName" type="text" class="easyui-validatebox" data-options="required:true" value="${systemParam.prmName}" readonly="readonly"></td>
					<td>版本</td>
					<td><input id="prmVersion" name="prmVersion"  type="text"  class="easyui-numberbox"   value="${systemParam.prmVersion}"  readonly="readonly"/></td>
				</tr>
				<tr>
					<td>参数值</td>
					<td><input name="prmVal" type="text"  class="easyui-validatebox" data-options="required:true" value="${systemParam.prmVal}"  readonly="readonly"></td>
					<td>长度</td>
					<td><input id="prmLen" name="prmLen"  type="text"  class="easyui-numberbox"   value="${systemParam.prmLen}" style="width: 140px; height: 29px;"   readonly="readonly"/></td>
				</tr>
				<tr>
					<td>描述</td>
					<td colspan="3"><textarea rows="3"  name="prmDesc"  style="width: 80%"  value="${systemParam.prmDesc}" readonly="readonly"/></td>
				</tr>
			</table>
		</form>
	</div>
</div>