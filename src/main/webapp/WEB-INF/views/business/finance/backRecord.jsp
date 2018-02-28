<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<template id="backRecordTmpl">
    <style>
        .table-ded{display: none;}
        .td-light tbody tr:last-child{ color: red}
        @media screen and (max-width: 500px) {
            .table-de{ display: none;}
            .table-ded{ display: block}
        }

    </style>
    <div>
        <div style="padding-bottom: 10px;text-align: right" v-if="flag!='1'">
            <button type="button" @click="chargeback" class="btn btn-primary">退单</button>
        </div>
        <div class="panel-group table-responsive">
            <table width="100%" class="table table-striped table-bordered table-hover table-de panel-heading border-bottom-style">
                <thead>
                    <tr align="center">
                        <td>退款金额</td>
                        <td>客户姓名</td>
                        <td>客户电话</td>
                        <td>申请人</td>
                        <td>审批人</td>
                        <td>操作人</td>
                        <td>操作时间</td>
                        <td>退单原因</td>
                    </tr>
                </thead>
                <tbody>
                    <tr align="center" v-for="item in backRecord">
                        <td>{{item.expectRefundAmount}}</td>
                        <td>{{item.customerName}}</td>
                        <td>{{item.customerMobile}}</td>
                        <td>{{item.applyer}}</td>
                        <td>{{item.checker}}</td>
                        <td>{{item.executor}}</td>
                        <td>{{item.executTime | goDate}}</td>
                        <td>{{item.closeReason}}</td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>
</template>