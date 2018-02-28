<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<template id="changeRecordTmpl">
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
                        <td>变更编号</td>
                        <td>变更类型</td>
                        <td>财务阶段</td>
                        <td>变更金额</td>
                        <td>交款人姓名</td>
                        <td>交款人电话</td>
                        <td>操作人</td>
                        <td>清算时间</td>
                        <td>清算状况</td>
                        <td v-if="flag!='1'">操作</td>
                    </tr>
                </thead>
                <tbody>
                    <tr align="center" v-for="item in changeRecord">
                        <td>{{item.changeNo}}</td>
                        <td>{{item.changeMode | goChangeType}}</td>
                        <td>{{item.stageName}}</td>
                        <td>{{item.changeAmount}}</td>
                        <td>{{item.payerName}}</td>
                        <td>{{item.payerMobile}}</td>
                        <td>{{item.cashier}}</td>
                        <td>{{item.clearedTime | goDate}}</td>
                        <td>{{item.cleared | goType}}</td>
                        <td v-if="flag!='1'">
                            <span v-if="item.changeType == 'BASIC'">
                                <button type="button" @click="viewDetail(item.basicId,item.changeType)" class="btn btn-xs btn-primary">查看详情</button>
                            </span>
                            <span v-if="item.changeType == 'MATERIAL'">
                                <button type="button" @click="viewDetail(item.materialId,item.changeType)" class="btn btn-xs btn-primary">查看详情</button>
                            </span>
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>
</template>