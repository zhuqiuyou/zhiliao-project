<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<div class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'north',border:false" title="" style="overflow: hidden;padding: 3px;">
			<table class="grid">
				<tr>
					<td>商户号</td>
					<td><input  name="mchntCode" type="text"  value="${settlebill.mchntCode}" readonly="readonly">
					</td>
					<td>商户名称</td>
					<td><input  name="mchntName" type="text"  value="${settlebill.mchntName}" readonly="readonly">
					</td>
					<td>结算单号</td>
					<td><input  name="mchntName" type="text"  value="${settlebill.settleId}" readonly="readonly">
					</td>
					
				</tr>
				<tr>
					<td>合同号</td>
					<td><input  name="contractId" type="text"  value="${settlebill.contractId}" readonly="readonly">
					<td>开始日期</td>
					<td>
						<input name="settleDateStart"  type="text"  value="${settlebill.settleDateStart}"  readonly="readonly">
					</td>
					<td>结束日期</td>
					<td>
						<input name="settleDateEnd"  type="text"  value="${settlebill.settleDateEnd}"  readonly="readonly">
					</td>
				</tr>
				
				<tr>
					<td>结算本金</td>
					<td>
						<input name="transAtSum"  type="text"  value="${settlebill.transAtSum}"  readonly="readonly">
					</td>
					
					<td>结算手续费</td>
					<td>
						<input name="settleFee"  type="text"  value="${settlebill.settleFee}"  readonly="readonly">
					</td>
					<td>结算金额</td>
					<td>
						<input name="settleAtSum"  type="text"  value="${settlebill.settleAtSum}"  readonly="readonly">
					</td>
				</tr>
				
				<tr>
					<td>手续费偏移量</td>
					<td>
						<input name="feeOffset"  type="text"  value="${settlebill.feeOffset}"  readonly="readonly">
					</td>
					<td>结算单状态</td>
					<td> 
						<select  name="billStat" class="easyui-combobox" data-options="width:140,height:29,editable:false,panelHeight:'auto'" readonly="readonly">
							<c:forEach items="${billStatList}" var="ojb">
								<option value="${ojb.code}"  <c:if test="${ojb.code == settlebill.billStat}">selected="selected"</c:if> >${ojb.text}</option>
							</c:forEach>
						</select>
					</td>
				</tr>
					
			</table>
	</div>
</div>