<%@ page contentType="text/html;charset=UTF-8" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<head>
</head>
<div id="container" class="wrapper wrapper-content animated fadeInRight">
    <div id="breadcrumb">
        <bread-crumb :crumbs="breadcrumbs"></bread-crumb>
    </div>
    <!-- ibox start -->
    <div class="ibox">

        <div class="ibox-content">
            <div>
                <h3>已串单项目</h3>
            </div>
            <table class="table table-striped table-bordered table-hover table-de panel-heading border-bottom-style" width="100%">
                <tr align="center">
                    <td>客户/订单</td>
                    <td>客户级别</td>
                    <td>付款状态</td>
                    <td>第二联系人/电话</td>
                    <td>工程地址/面积</td>
                    <td>客服/下单时间</td>
                    <td>设计师/签约时间</td>
                    <td>量房类型</td>
                    <td>量房时间</td>
                    <td>订单状态</td>
                    <td>操作</td>
                </tr>
                <tr align="center" v-for="item in projectManageList">
                    <td>{{item.customerName}}<br/>/{{item.contractCode}}</td>
                    <td>{{item.customerTag}}</td>
                    <td>{{item.stageName}}</td>
                    <td>{{item.secondContact}}<br/>/{{item.secondContactMobile}}</td>
                    <td>{{item.addressProvince}}{{item.addressCity}}{{item.addressArea}}<br/>/{{item.houseAddr}}<br/>/{{item.buildArea}}</td>
                    <td>{{item.serviceName}}<br/>/{{item.serviceMobile}}<br/>/{{item.createTime | goDate}}</td>
                    <td>{{item.designerDepName}}<br/>/{{item.designer}}<br/>/{{item.designerMobile}}<br/>/{{item.completeTime | goDate}}</td>
                    <td>{{item != null ? '普通量房':''}}</td>
                    <td v-if="item.bookHouseTime != null">{{item.bookHouseTime | goDate}}</td>
                    <td v-else>{{item.planHouseTime | goDate}}</td>
                    <td>{{item.orderFlowStatus | goType}}</td>
                    <td><button class="btn btn-xs btn-danger" @click="remove(item.contractCode)">{{item == null ? '':'移除'}}</button></td>
                </tr>
            </table>
        </div>
        <div class="ibox-content">
            <div>
                <h3>可串单项目</h3>
            </div>
            <div class="row">
                <form id="searchForm" @submit.prevent="getProjectManage">
                    <div class="col-md-2 form-group">
                        <input v-model="form.keyword" type="text" class="form-control"/>
                    </div>
                    <div class="col-md-2 form-group">
                        <button id="searchBtn" type="submit"
                                class="btn btn-block btn-outline btn-default" alt="搜索订单"
                                title="搜索订单">
                            <i class="fa fa-search"></i>
                        </button>
                    </div>
                </form>
            </div>
            <div class="panel-group table-responsive">
                <table class="table table-striped table-bordered table-hover table-de panel-heading border-bottom-style" width="100%">
                    <thead>
                        <tr align="center">
                            <td>客户/订单</td>
                            <td>客户级别</td>
                            <td>付款状态</td>
                            <td>第二联系人/电话</td>
                            <td>工程地址/面积</td>
                            <td>客服/下单时间</td>
                            <td>设计师/签约时间</td>
                            <td>量房类型</td>
                            <td>量房时间</td>
                            <td>订单状态</td>
                            <td>操作</td>
                        </tr>
                    </thead>
                    <tbody>
                        <tr align="center" v-for="item in projectManage">
                            <td>{{item.customerName}}<br/>/{{item.contractCode}}</td>
                            <td>{{item.customerTag}}</td>
                            <td>{{item.stageName}}</td>
                            <td>{{item.secondContact}}<br/>/{{item.secondContactMobile}}</td>
                            <td>{{item.addressProvince}}{{item.addressCity}}{{item.addressArea}}<br/>/{{item.houseAddr}}<br/>/{{item.buildArea}}</td>
                            <td>{{item.serviceName}}<br/>/{{item.serviceMobile}}<br/>/{{item.createTime | goDate}}</td>
                            <td>{{item.designerDepName}}<br/>/{{item.designer}}<br/>/{{item.designerMobile}}<br/>/{{item.completeTime | goDate}}</td>
                            <td>{{item == null ? '':'普通量房'}}</td>
                            <td v-if="item.bookHouseTime != null">{{item.bookHouseTime | goDate}}</td>
                            <td v-else>{{item.planHouseTime | goDate}}</td>
                            <td>{{item.orderFlowStatus | goType}}</td>
                            <td><button class="btn btn-xs btn-info" @click="singleString(item.contractCode)">{{item == null ? '':'串单'}}</button></td>
                        </tr>
                        <tr v-if="projectManage.length == 0" align="center">
                            <td colspan="11">
                                没有找到匹配的记录
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
    <!-- ibox end -->
</div>
<%--<%@include file="/WEB-INF/views/admin/components/select.jsp" %>--%>
<script src="/static/admin/vendor/webuploader/webuploader.js"></script>
<script src="/static/business/orderflow/projectManage.js"></script>