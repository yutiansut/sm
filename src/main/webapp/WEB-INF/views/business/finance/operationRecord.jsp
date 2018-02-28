<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<template id="operationRecordTmpl">
    <style>
        .table-ded{display: none;}
        .td-light tbody tr:last-child{ color: red}
        @media screen and (max-width: 500px) {
           .table-de{ display: none;}
           .table-ded{ display: block}
        }

    </style>
    <div>
        <div class="panel-group table-responsive">
            <table width="100%" class="table table-striped table-bordered table-hover table-de panel-heading border-bottom-style">
                <thead>
                    <tr align="center">
                        <td>操作类型</td>
                        <td>操作人</td>
                        <td>操作时间</td>
                        <td>备注</td>
                    </tr>
                </thead>
                <tbody>
                    <tr align="center" v-for="item in operationRecord">
                        <td>{{item.operatType | goType}}</td>
                        <td>{{item.operator}}</td>
                        <td>{{item.operatTime | goDate}}</td>
                        <td>{{item.operatMsg}}</td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>
</template>