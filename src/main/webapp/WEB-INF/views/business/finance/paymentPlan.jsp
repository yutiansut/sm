<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<template id="paymentPlanTmpl">
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
                        <td>财务阶段</td>
                        <td>应收金额</td>
                        <td>应收占比</td>
                        <td>阶段开始时间</td>
                        <td>状态</td>
                    </tr>
                </thead>
                <tbody>
                    <tr align="center" v-for="item in paymentPlan">
                        <td>{{item.itemName}}</td>
                        <td>{{item.expectReceived}}</td>
                        <td>{{item.finaTransRate*100}}%</td>
                        <td>{{item.startTime | goDate}}</td>
                        <td>{{item.stageFinished | goType}}</td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>
</template>