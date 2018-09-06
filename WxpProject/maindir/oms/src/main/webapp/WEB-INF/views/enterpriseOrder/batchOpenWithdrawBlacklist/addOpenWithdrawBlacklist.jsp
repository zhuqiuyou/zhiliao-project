<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <%@ include file="/WEB-INF/views/common/init.jsp"%>
    <%@ include file="/WEB-INF/views/common/head.jsp"%>
    <link rel="stylesheet" href="${ctx}/resource/datetimepicker/css/bootstrap-datetimepicker.0.0.11.min.css" />
    <script src="${ctx}/resource/datetimepicker/js/bootstrap-datetimepicker.0.0.11.min.js"></script>
    <script src="${ctx}/resource/datetimepicker/js/locales/bootstrap-datetimepicker.zh-CN.js"></script>
    <script src="${ctx}/resource/js/module/enterpriseOrder/batchOpenWBAccountlist/addOpenWBAccount.js"></script>
    <script src="${ctx}/resource/js/jquery/jquery.form.js"></script>
</head>
<body>
	   <%@ include file="/WEB-INF/views/common/navbar.jsp"%>
            <div id="contentwrapper">
                <div class="main_content">
                	<nav>
			            <div id="jCrumbs" class="breadCrumb module">
			                <ul>
			                    <li><a href="#"><i class="icon-home"></i></a></li>
			                    <li>福利管理</li>
			                    <li><a href="${ctx }/enterpriseOrder/batchOpenWBAccount/listOpenWBAccount.do">批量提现黑名单</a></li>
			                     <li>批量提现黑名单列表</li>
			                </ul>
			            </div>
			        </nav>
					<h3 class="heading">录入提现黑名单</h3>
					<div class="row-fluid" >
					  <div class="span12">
	                     <div class="control-group formSep">
	                       	<label class="control-label">数量:</label>${count}
	                       	<div class="pull-right">
                                   <button class="btn btn-primary btn-account-list" type="button">文件导入</button>
                                   <button class="btn btn-primary btn-mould-download" type="button">模板下载</button>
                           	</div>
	                     </div>
					  </div>
					</div>
			         <div>
			         <button class="btn btn-primary btn-addAccount" type="button"> 添 加 </button>
			         </div>
				     <br/>
				        <form id="pageMainForm" action="${ctx}/enterpriseOrder/batchOpenWBAccount/intoAddOpenWBAccount.do" class="form-inline form_validation_tip" method="post">
				         <table class="table table-striped table-bordered dTableR table-hover" id="dt_gal" >
				             <thead>
				             <tr>
				               <th>序号</th>
				               <th>用户姓名</th>
				               <th>用户手机号</th>
				               <th>操作</th>
				             </tr>
				             </thead>
				             <tbody>
				             <c:forEach var="entity" items="${pageInfo.list}" varStatus="st">
				                 <tr>
				                 	<td>${st.index+1 }</td>
				                 	<td>${entity.userName}</td>
									<td>${entity.userPhone}</td>
				                    <td>
									<a puid="${entity.puid }" title="删除" class="btn-mini btn-delete" href="#"><i class="icon-remove"></i></a>
				                    </td>
				                 </tr>
				             </c:forEach>
				             </tbody>
				         </table>
				         <%@ include file="/WEB-INF/views/common/pagination.jsp"%>
                      </form>
                      <br/>
                      <sec:authorize access="hasRole('ROLE_WITHDRAW_BLACKLIST_ADDCOMMIT')">
                      <button class="btn btn-primary btn-sub" id = "saveWBAccount" type="button">保 存</button> 
                      </sec:authorize>
                      <a href="${ctx }/enterpriseOrder/batchOpenWBAccount/listOpenWBAccount.do"><button class="btn btn-primary" type="button">返 回</button></a>
			   </div>
	    </div>
	    
	    <div id="accountListModal" class="modal hide fade" tabindex="-1" role="dialog" aria-hidden="true" >
            <div class="modal-header">
                <button class="close" data-dismiss="modal">&times;</button>
                <h3>文件导入</h3>
            </div>
            <form id="uploadMainForm" action="${ctx}/common/excelImport/excelImp.do "   method="post" enctype="multipart/form-data">
            <div class="modal-body">
                <div class="control-group">
                              <div class="controls" style="text-align: center;">
                                <div data-provides="fileupload" class="fileupload fileupload-new"><input type="hidden" />
                              <span class="control-label">上传文件 ：</span>
                                    <div class="input-append">
                                        <div class="uneditable-input"><i class="icon-file fileupload-exists"></i> <span class="fileupload-preview"></span></div><span class="btn btn-file"><span class="fileupload-new">选择文件</span><span class="fileupload-exists">重新选择</span><input type="file"  name="file"/></span><a data-dismiss="fileupload" class="btn fileupload-exists" href="#">删除文件</a>
                                    </div>
                                </div>
                            </div>
                     </div>
                <div style="text-align: center;">
                    <button class="btn btn-primary btn-import" type="submit">导 入  </button>
                </div>
            </div>
            </form>
        </div>  
	    
	    
	    <div id="addAccountModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
            <form class="form-horizontal">
                <div class="modal-header">
                    <button class="close" data-dismiss="modal">&times;</button>
                    <h3 id="commodityInfModal_h">添加名单</h3>
                </div>
                <div class="modal-body">
                    <input type="hidden" id="commodity_id" />
                    <fieldset>
                        <div class="control-group">
                            <label class="control-label">用户姓名：</label>
                            <div class="controls">
                                <input type="text" class="span3" id="name" name="name"/>
                                <span class="help-block"></span>
                            </div>
                        </div>
                        <div class="control-group">
                            <label class="control-label">用户手机号：</label>
                            <div class="controls">
                                <input type="text" class="span3" id="phone"  name = "phone"/>
                                <span class="help-block"></span>
                            </div>  
                        </div>
                    </fieldset>
                </div>
            </form>
            <div class="modal-footer" style="text-align: center;">
                <button class="btn btn-primary btn-submit" id = "subWBAccount">确 定  </button>
                <button class="btn" data-dismiss="modal" aria-hidden="true">取 消</button>
            </div>
        </div>
        <div id="imorptMsg" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="height: 200px;">
              <div class="modal-header">
                    
                    <h3 id="commodityInfModal_h">温馨提示</h3>
                </div>
                <br/><br/><br/>
              <h3 align="center">文件上传中......</h3>
        </div>
        
        <div id="msg" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="height: 200px;">
              <div class="modal-header">
                    
                    <h3 id="commodityInfModal_h">温馨提示</h3>
                </div>
                <br/><br/><br/>
              <h3 align="center">信息正在处理......</h3>
        </div>         
</body>
</html>
