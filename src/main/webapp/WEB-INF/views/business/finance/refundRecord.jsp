<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<template id="refundRecordTmpl">
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
            <button type="button" @click="refundment" class="btn btn-primary">退款</button>
        </div>
        <div class="panel-group table-responsive">
            <table width="100%" class="table table-striped table-bordered table-hover table-de panel-heading border-bottom-style">
                <thead>
                    <tr align="center">
                        <td>退款编号</td>
                        <td>退款类型</td>
                        <td>财务阶段</td>
                        <td>退款金额</td>
                        <td>收款人姓名</td>
                        <td>收款人电话</td>
                        <td>收款账号</td>
                        <td>操作人</td>
                        <td>退款时间</td>
                        <td>退款原因</td>
                    </tr>
                </thead>
                <tbody>
                    <tr align="center" v-for="item in refundRecord">
                        <td>{{item.refundNo}}</td>
                        <td>{{item.refundType | goType}}</td>
                        <td>{{item.stageName}}</td>
                        <td>{{item.refundActualAmount}}</td>
                        <td>{{item.refundReceiverName}}</td>
                        <td>{{item.refundReceiverMobile}}</td>
                        <td>{{item.refundReceiverAccount}}</td>
                        <td>{{item.operator}}</td>
                        <td>{{item.refundTime | goDate}}</td>
                        <td>{{item.refundReson}}</td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>
</template>