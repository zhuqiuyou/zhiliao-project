<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!doctype html>
<html>
<head>
    <meta charset="UTF-8">  
    <meta name="viewport" content="initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <title>Basic DataGrid - jQuery EasyUI Mobile Demo</title>  
    <link rel="stylesheet" type="text/css" href="${ctx}/jslib/easyui1.4/themes/gray/easyui.css">  
    <link rel="stylesheet" type="text/css" href="${ctx}/jslib/easyui1.4/themes/icon.css">  
    <script src="${ctx}/jslib/jquery-1.8.3.js" type="text/javascript" charset="utf-8"></script>
    <script type="text/javascript" src="${ctx}/jslib/easyui1.4/jquery.easyui.min.js"></script> 
</head>
   <body class="easyui-layout">
    <div data-options="region:'north',border:false" style="overflow:hidden">
        <div class="panel-header" style="padding:0 0 0 5px;border-width:1px 0;">
            <div style="float:right;padding:2px 5px">
                <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="removeit()"></a>
                <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true" onclick="accept()"></a>
                <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-undo',plain:true" onclick="reject()"></a>
            </div>
            <span class="panel-title" style="line-height:30px">Row Editing</span>
            <div style="clear:both"></div>
        </div>
    </div>
    <div data-options="region:'center',border:false">
    <table id="dg" data-options="
                iconCls: 'icon-edit',
                singleSelect: true,
                fit:true,
                fitColumns:true,
                border: false,
                scrollbarSize: 0,
                data: data,
                onClickRow: onClickRow
            ">
        <thead>
            <tr>
                <th data-options="field:'itemid',width:80">Item ID</th>
                <th data-options="field:'productid',width:100,editor:'text'">Product</th>
                <th data-options="field:'listprice',width:80,align:'right',editor:{type:'numberbox',options:{precision:1}}">List Price</th>
                <th data-options="field:'unitcost',width:80,align:'right',editor:'numberbox'">Unit Cost</th>
            </tr>
        </thead>
    </table>
    </div>
    
    <script type="text/javascript">
        var data =     [
            {"productid":"FI-SW-01","productname":"Koi","unitcost":10.00,"status":"P","listprice":36.50,"attr1":"Large","itemid":"EST-1"},
            {"productid":"K9-DL-01","productname":"Dalmation","unitcost":12.00,"status":"P","listprice":18.50,"attr1":"Spotted Adult Female","itemid":"EST-10"},
            {"productid":"RP-SN-01","productname":"Rattlesnake","unitcost":12.00,"status":"P","listprice":38.50,"attr1":"Venomless","itemid":"EST-11"},
            {"productid":"RP-SN-01","productname":"Rattlesnake","unitcost":12.00,"status":"P","listprice":26.50,"attr1":"Rattleless","itemid":"EST-12"},
            {"productid":"RP-LI-02","productname":"Iguana","unitcost":12.00,"status":"P","listprice":35.50,"attr1":"Green Adult","itemid":"EST-13"},
            {"productid":"FL-DSH-01","productname":"Manx","unitcost":12.00,"status":"P","listprice":158.50,"attr1":"Tailless","itemid":"EST-14"},
            {"productid":"FL-DSH-01","productname":"Manx","unitcost":12.00,"status":"P","listprice":83.50,"attr1":"With tail","itemid":"EST-15"},
            {"productid":"FL-DLH-02","productname":"Persian","unitcost":12.00,"status":"P","listprice":23.50,"attr1":"Adult Female","itemid":"EST-16"},
            {"productid":"FL-DLH-02","productname":"Persian","unitcost":12.00,"status":"P","listprice":89.50,"attr1":"Adult Male","itemid":"EST-17"},
            {"productid":"AV-CB-01","productname":"Amazon Parrot","unitcost":92.00,"status":"P","listprice":63.50,"attr1":"Adult Male","itemid":"EST-18"}
        ];
        $(function(){
            $('#dg').datagrid({
                data: data
            });
        });
        
        var editIndex = undefined;
        function endEditing(){
            if (editIndex == undefined){return true}
            if ($('#dg').datagrid('validateRow', editIndex)){
                $('#dg').datagrid('endEdit', editIndex);
                editIndex = undefined;
                return true;
            } else {
                return false;
            }
        }
        function onClickRow(index){
            if (editIndex != index){
                if (endEditing()){
                    $('#dg').datagrid('selectRow', index)
                            .datagrid('beginEdit', index);
                    editIndex = index;
                } else {
                    $('#dg').datagrid('selectRow', editIndex);
                }
            }
        }
        function removeit(){
            if (editIndex == undefined){return}
            $('#dg').datagrid('cancelEdit', editIndex)
                    .datagrid('deleteRow', editIndex);
            editIndex = undefined;
        }
        function accept(){
            if (endEditing()){
                $('#dg').datagrid('acceptChanges');
            }
        }
        function reject(){
            $('#dg').datagrid('rejectChanges');
            editIndex = undefined;
        }
    </script>
    <style scoped>
        .panel-title{
            text-align:center;
            font-size:14px;
            font-weight:bold;
            text-shadow:0 -1px rgba(0,0,0,0.3);
        }
        .datagrid-row,.datagrid-header-row{
            height:35px;
        }
        .datagrid-body td{  
            border-right:1px dotted transparent;  
            border-bottom:1px dotted transparent;  
        }  
    </style>
</body>
</html>