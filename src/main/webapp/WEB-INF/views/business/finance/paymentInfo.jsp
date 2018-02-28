<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<template id="paymentInfoTmpl">
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
            <button  type="button" @click="receivables" class="btn btn-primary">收款</button>
        </div>
        <div class="panel-group table-responsive">
            <table class="table table-striped table-bordered table-hover table-de panel-heading border-bottom-style">
                <thead>
                    <tr align="center">
                        <td>收款阶段</td>
                        <td>款项类别</td>
                        <td>交款人姓名</td>
                        <td>交款人电话</td>
                        <td>收款日期</td>
                        <td>交款方式</td>
                        <td>收据号</td>
                        <td>应收总金额</td>
                        <td>实收金额</td>
                        <td>应/实收差额</td>
                        <td>操作人</td>
                        <td v-if="flag!='1'">操作</td>
                    </tr>
                </thead>
                <tbody  v-for="item in paymentInfo">
                    <%--红冲显示红色--%>
                    <tr align="center" v-if="item.ifRcw=='1'" style="color: red">
                        <td>{{item.itemName}}</td>
                        <td>{{item.payManualFlag}}</td>
                        <td>{{item.payerName}}</td>
                        <td>{{item.payerMobile}}</td>
                        <td>{{item.payTime | goDate}}</td>
                        <td>{{item.paymethodName}}</td>
                        <td>{{item.receiptNum}}</td>
                        <td>{{item.expectReceived}}</td>
                        <td>{{item.actualReceived}}</td>
                        <td>{{(item.expectReceived - item.actualReceived).toFixed(2)}}</td>
                        <td>{{item.cashier}}</td>
                        <td v-if="flag!='1'">
                            <button type="button" @click="printed(item.id)" class="btn btn-xs btn-info" >打印</button>
                            <span v-if="item.payId == ableRcwPayid">
                                <button type="button" @click="redPunch(item.payId)" class="btn btn-xs btn-danger" >红冲</button>
                            </span>
                            <span v-if="item.payId == ableFinishPayid">
                                <button type="button" @click="endReceipt(item.payId)" class="btn btn-xs btn-warning" >收款结束</button>
                            </span>
                        </td>
                    </tr>
                    <tr align="center" v-if="item.ifRcw!='1' && (item.payType == 'DEPOSIT' ||
                        item.payType == 'MODIFY' || item.payType == 'CONSTRUCT' || item.payType == 'DEDUCT_DESIGN_FEE' || item.payType == 'DEDUCT_OTHER_FEE')">
                        <td>{{item.itemName}}</td>
                        <td>{{item.payManualFlag}}</td>
                        <td>{{item.payerName}}</td>
                        <td>{{item.payerMobile}}</td>
                        <td>{{item.payTime | goDate}}</td>
                        <td>{{item.paymethodName}}</td>
                        <td>{{item.receiptNum}}</td>
                        <td>{{item.expectReceived}}</td>
                        <td>{{item.actualReceived}}</td>
                        <td>{{(item.expectReceived - item.actualReceived).toFixed(2)}}</td>
                        <td>{{item.cashier}}</td>
                        <td v-if="flag!='1'">
                            <button type="button" @click="printed(item.id)" class="btn btn-xs btn-info" >打印</button>
                            <span v-if="item.payId == ableRcwPayid">
                                <button type="button" @click="redPunch(item.payId)" class="btn btn-xs btn-danger" >红冲</button>
                            </span>
                            <span v-if="item.payId == ableFinishPayid">
                                <button type="button" @click="endReceipt(item.payId)" class="btn btn-xs btn-warning" >收款结束</button>
                            </span>
                        </td>
                    </tr>
                    <%--退款项显示紫色--%>
                    <tr align="center" v-if="item.payType == 'RETURN_CONSTRUCT' ||
                        item.payType == 'RETURN_MODIFY' || item.payType == 'RETURN_DEPOSIT'" style="color: #5c2699">
                        <td>{{item.itemName}}</td>
                        <td>{{item.payManualFlag}}</td>
                        <td>{{item.payerName}}</td>
                        <td>{{item.payerMobile}}</td>
                        <td>{{item.payTime | goDate}}</td>
                        <td>{{item.paymethodName}}</td>
                        <td>{{item.receiptNum}}</td>
                        <td>{{item.expectReceived}}</td>
                        <td>{{item.actualReceived}}</td>
                        <td>{{(item.expectReceived - item.actualReceived).toFixed(2)}}</td>
                        <td>{{item.cashier}}</td>
                        <td v-if="flag!='1'">
                            <button type="button" @click="printed(item.id)" class="btn btn-xs btn-info" >打印</button>
                            <span v-if="item.payId == ableRcwPayid">
                                <button type="button" @click="redPunch(item.payId)" class="btn btn-xs btn-danger" >红冲</button>
                            </span>
                            <span v-if="item.payId == ableFinishPayid">
                                <button type="button" @click="endReceipt(item.payId)" class="btn btn-xs btn-warning" >收款结束</button>
                            </span>
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>
</template>
