<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<jsp:include page="../inc.jsp"></jsp:include>
<meta http-equiv="X-UA-Compatible" content="edge" />
<c:if test="${fn:contains(sessionInfo.resourceList, '/card/lost')}">
	<script type="text/javascript">
		$.canLost = true;
	</script>
</c:if>
<c:if test="${fn:contains(sessionInfo.resourceList, '/card/lock')}">
	<script type="text/javascript">
		$.canLock = true;
	</script>
</c:if>
<c:if test="${fn:contains(sessionInfo.resourceList, '/card/freeze')}">
	<script type="text/javascript">
		$.canFreeze = true;
	</script>
</c:if>
<c:if test="${fn:contains(sessionInfo.resourceList, '/card/cancel')}">
	<script type="text/javascript">
		$.canCancel = true;
	</script>
</c:if>
<c:if test="${fn:contains(sessionInfo.resourceList, '/card/active')}">
	<script type="text/javascript">
		$.canActive = true;
	</script>
</c:if>
<c:if test="${fn:contains(sessionInfo.resourceList, '/card/setPsw')}">
	<script type="text/javascript">
		$.canSetPsw = true;
	</script>
</c:if>
<c:if test="${fn:contains(sessionInfo.resourceList, '/card/resetPsw')}">
	<script type="text/javascript">
		$.canResetPsw = true;
	</script>
</c:if>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>单卡维护</title>

<script type="text/javascript">
	function searchFun() {
		var isValid = $('#cardForm').form('validate');
		var card = $('#cardNO').val();
		if (!isValid || !(/^[0-9]{19}$/.test(card))) {
			parent.$.messager.alert('提示', '请填写十九位的卡号(数字)', 'info');
			return;
		}
		$.post('${ctx}/card/search', {
			card : card
		}, function(result) {
			if (result.success) {
				setCardStsFormValue(result.obj);
			} else {
				parent.$.messager.alert('提示', result.msg, 'info');
			}
		}, 'JSON');
	}

	function setCardStsFormValue(card) {
		var cardStat = card.cardStat;
		var stockStat = card.stockStat;
		var cardStatCN = card.cardStatCN;
		$('#cno').val(card.cardNo);
		$('#cardStat').val(cardStat);
		$('#cardStatCN').val(cardStatCN);
		$('#stockStat').combobox('setValue', card.stockStat)
		$('#accountNo').val(card.account.accountNo);
		$('#accountStat').combobox('setValue', card.account.accountStat);
		$('#accBal').val(card.account.accBal);
		$('#validDate').val(card.validDate);
		
		$('#lost').hide();
		$('#lock').hide();
		$('#freeze').hide();
		$('#cancel').hide();
		$('#active').hide();
		$('#resetPsw').hide();
		$('#lost').text('挂失');
		$('#lock').text('锁定');
		$('#freeze').text('冻结');
		$('#cancel').text('注销');
		
		// 卡号为已出库时可操作
		if (stockStat && stockStat == '20') {
			var lost = card.lostStat;
			var lock = card.lockStat;
			var freeze = card.freezeStat;
			//var cancel = card.cancelStat;
			if (cardStatCN.indexOf('正常') > 0) {
				if ($.canLost) {
					$('#lost').show();
					$('#lost').text('挂失');
				}
				if ($.canLock) {
					$('#lock').show();
					$('#lock').text('锁定');
				}
				if ($.canFreeze) {
					$('#freeze').show();
					$('#freeze').text('冻结');
				}
				if ($.canCancel) {
					$('#cancel').show();
					$('#cancel').text('注销');
				}
				if ($.canResetPsw) {
					$('#resetPsw').show();
				}
				if ($.canSetPsw) {
					$('#setPsw').show();
				}
			}
			if (lost == "0" && $.canLost) {
				$('#lost').show();
				$('#lost').text('解挂失');
			}
			if (lock == "0" && $.canLock) {
				$('#lock').show();
				$('#lock').text('解锁');
			}
			if (freeze == "0" && $.canFreeze) {
				$('#freeze').show();
				$('#freeze').text('解冻');
			}
			//if (cancel == "0" && $.canCancel) {
			//	$('#cancel').show();
			//	$('#cancel').text('解注销');
			//}
			
			if (cardStat && cardStat == '00' && $.canActive) {
				$('#active').show();
			}
		} else {

		}
	}
	function lost() {
		var card = $('#cardNO').val();
		if (card == undefined || card == '') {
			return;
		}
		parent.$.messager.confirm('询问', '您确认挂失此卡吗？', function(b) {
			if (b) {
				progressLoad();
				$.post('${ctx}/card/lost', {
					cardNo : card
				}, function(result) {
					if (result.success) {
						searchFun();
					}
					progressClose();
				}, 'JSON');
			}
		});
	}
	function lock() {
		var card = $('#cardNO').val();
		if (card == undefined || card == '') {
			return;
		}
		parent.$.messager.confirm('询问', '您确认锁定此卡吗？', function(b) {
			if (b) {
				progressLoad();
				$.post('${ctx}/card/lock', {
					cardNo : card
				}, function(result) {
					if (result.success) {
						searchFun();
					}
					progressClose();
				}, 'JSON');
			}
		});
	}
	function freeze() {
		var card = $('#cardNO').val();
		if (card == undefined || card == '') {
			return;
		}
		parent.$.messager.confirm('询问', '您确认冻结此卡吗？', function(b) {
			if (b) {
				progressLoad();
				$.post('${ctx}/card/freeze', {
					cardNo : card
				}, function(result) {
					if (result.success) {
						searchFun();
					}
					progressClose();
				}, 'JSON');
			}
		});
	}
	function cancel() {
		var card = $('#cardNO').val();
		if (card == undefined || card == '') {
			return;
		}
		parent.$.messager.confirm('询问', '您确认注销此卡吗？', function(b) {
			if (b) {
				progressLoad();
				$.post('${ctx}/card/cancel', {
					cardNo : card
				}, function(result) {
					if (result.success) {
						searchFun();
					}
					progressClose();
				}, 'JSON');
			}
		});
	}

	function setPsw() {
		var card = $('#cardNO').val();
		if (card == undefined || card == '') {
			return;
		}
		parent.$.modalDialog({
			title : '密码修改',
			width : 500,
			height : 300,
			href : '${ctx}/card/setPsw?cardNo=' + card,
			buttons : [ {
				text : '确认',
				handler : function() {
					var f = parent.$.modalDialog.handler
							.find('#resetPasswordForm');
					f.submit();
				}
			} ]
		});
	}

	function resetPsw() {
		var card = $('#cardNO').val();
		if (card == undefined || card == '') {
			return;
		}
		parent.$.modalDialog({
			title : '密码重置',
			width : 500,
			height : 300,
			href : '${ctx}/card/resetPsw?cardNo=' + card,
			buttons : [ {
				text : '确认',
				handler : function() {
					var f = parent.$.modalDialog.handler
							.find('#resetPasswordForm');
					f.submit();
				}
			} ]
		});
	}
	function active() {
		var card = $('#cardNO').val();
		if (card == undefined || card == '') {
			return;
		}
		parent.$.messager.confirm('询问', '您确认激活此卡吗？', function(b) {
			if (b) {
				progressLoad();
				$.post('${ctx}/card/active', {
					cardNo : card
				}, function(result) {
					if (result.success) {
						searchFun();
					}
					progressClose();
				}, 'JSON');
			}
		});
	}
</script>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'north',border:false" title=""
		style="overflow: hidden;padding: 3px;">
		<form id="cardForm" method="post">
			<table class="grid">
				<tr>
					<td>卡号</td>
					<td colspan="2"><input id='cardNO' name="cardNO" type="text"
						class="easyui-validatebox" data-options="required:true"
						validType='length[19,19]' '  invalidMessage='卡号长度为19'
						maxlength="19" value=""> <a href="javascript:void(0);"
						class="easyui-linkbutton"
						data-options="iconCls:'icon_search',plain:true"
						onclick="searchFun();">查询</a></td>
				</tr>
			</table>
		</form>
	</div>
	<div data-options="region:'center',border:false,title:'卡状态'"
		style="height: 30%; overflow: hidden;background-color: #f4f4f4; padding-top: 15px;padding-left: 30px">
		<form id="cardStsForm" name="cardStsForm">
			<table id="contractListGrid" data-options="fit:true,border:false">
				<tr>
					<td style="width: 100px;">卡号</td>
					<td><input id="cno" name="cno" readonly="readonly"
						style="width: 180px"></td>
				</tr>
				<tr>
					<td>卡状态</td>
					<input id="cardStat" name="cardStat" type="hidden">
					<td><input id="cardStatCN" name="cardStatCN"
						readonly="readonly" style="width: 180px"></td>
				</tr>
				<tr>
					<td>库存状态</td>
					<td><select id='stockStat' name="stockStat"
						class="easyui-combobox" style="width: 180px" readonly="readonly">
							<c:forEach items="${stockStatList}" var="stockStatList">
								<option value="${stockStatList.code}">${stockStatList.text}</option>
							</c:forEach>
					</select></td>
				</tr>
				<tr>
					<td>账户号</td>
					<td><input id="accountNo" name="accountNo" readonly="readonly"
						style="width: 180px"></td>
				</tr>
				<tr>
					<td>账户状态</td>
					<td><select id='accountStat' name="accountStat"
						class="easyui-combobox" style="width: 180px" readonly="readonly">
							<c:forEach items="${accountStatList}" var="accountStatList">
								<option value="${accountStatList.code}">${accountStatList.text}</option>
							</c:forEach>
					</select></td>
				</tr>
				<tr>
				<td>账户余额</td>
				<td><input id="accBal" name="accBal" style="width: 180px" readonly="readonly"></td>
				</tr>
				<tr>
				<td>卡有效期截止日</td>
				<td><input id="validDate" name="validDate" style="width: 180px" readonly="readonly"></td>
				</tr>
			</table>
		</form>
	</div>
	<div id="contractListToolbar" class="toolbar"
		data-options="region:'south',border:false,title:'操作'"
		style="height:30%; overflow: hidden;background-color: #f4f4f4;padding-top: 30px;padding-left: 30px">
		<button id="lost" style="width: 80px" onclick="lost();"
			hidden="hidden">挂失</button>
		<button id="lock" style=" width: 80px" onclick="lock();"
			hidden="hidden">锁定</button>
		<button id="freeze" style="width: 80px" onclick="freeze();"
			hidden="hidden">冻结</button>
		<button id="cancel" style="width: 80px" onclick="cancel();"
			hidden="hidden">注销</button>
		<button id="active" style="width: 80px" onclick="active();"
			hidden="hidden">激活</button>
<!-- 		<button id="setPsw" style="width: 100px" onclick="setPsw();"
			hidden="hidden">修改密码</button> -->
			<button id="resetPsw" style="width: 100px" onclick="resetPsw();"
			hidden="hidden">重置密码</button>
	</div>
</body>
</html>