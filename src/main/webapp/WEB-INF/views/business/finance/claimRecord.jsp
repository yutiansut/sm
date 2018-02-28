<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<template id="claimRecordTmpl">
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
            <span v-if="complete=='0'">
                <button type="button" @click="reparation" class="btn btn-primary" disabled>赔款</button>
            </span>
            <span v-else>
                <button type="button" @click="reparation" class="btn btn-primary">赔款</button>
            </span>
        </div>
        <div class="panel-group table-responsive">
            <table width="100%" class="table table-striped table-bordered table-hover table-de panel-heading border-bottom-style">
                <thead>
                    <tr align="center">
                        <td>赔款编号</td>
                        <td>财务阶段</td>
                        <td>金额</td>
                        <td>客户姓名</td>
                        <td>客户电话</td>
                        <td>操作人</td>
                        <td>操作时间</td>
                        <td>清算状况</td>
                        <td>赔款原因</td>
                    </tr>
                </thead>
                <tbody>
                    <tr align="center" v-for="item in claimRecord">
                        <td>{{item.reparationNo}}</td>
                        <td>{{item.stageName}}</td>
                        <td>{{item.reparationAmount}}</td>
                        <td>{{item.name}}</td>
                        <td>{{item.mobile}}</td>
                        <td>{{item.creator}}</td>
                        <td>{{item.createTime | goDate}}</td>
                        <td>{{item.cleared | goType}}</td>
                        <td>{{item.reparationReason}}</td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>
</template>