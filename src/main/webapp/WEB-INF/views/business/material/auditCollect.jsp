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
            <div class="row">
                <form id="searchForm" @submit.prevent="query">

                    <div class="col-md-2 form-group">
                        <input v-model="form.startDate" v-el:start-date id="startDate"
                               name="startDate" type="text" readonly
                               class="form-control datepicker" placeholder="请选择开始时间">
                    </div>
                    <div class="col-md-2 form-group">
                        <input v-model="form.endDate" v-el:end-date id="endDate"
                               name="endDate" type="text" readonly
                               class="form-control datepicker" placeholder="请选择结束时间">
                    </div>

                    <div class="col-md-2 form-group">
                        <button id="searchBtn" type="submit"
                                class="btn btn-block btn-outline btn-default" alt="查询"
                                title="查询">
                            <i class="fa fa-search"></i>
                        </button>
                    </div>
                </form>
            </div>
            <!-- <div class="columns columns-right btn-group pull-right"></div> -->
            <table id="dataTable" width="100%"
                   class="table table-striped table-bordered table-hover">
            </table>
        </div>
    </div>
    <!-- ibox end -->
</div>
<div id="detailModal" class="modal fade" tabindex="-1">

    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal"
                aria-hidden="true">×
        </button>
        <h3 align="center">审计员审核详情</h3>
    </div>
    <div class="modal-body">
        <table  class="table table-striped table-bordered table-hover table-de panel-heading border-bottom-style">
            <thead>
            <tr align="center">
                <td>审计员</td>
                <td>项目编号</td>
                <td>变更单号</td>
                <td>审核时间</td>
            </tr>
            </thead>
            <tbody>
            <tr align="center" v-for="item in auditData">
                <td>{{item.auditUser}}</td>
                <td>{{item.contractCode}}</td>
                <td>{{item.changeNo}}</td>
                <td>{{item.auditTime | goDate}}</td>

            </tr>
            </tbody>
        </table>

    </div>
</div>

<%--<%@include file="/WEB-INF/views/admin/components/select.jsp" %>--%>
<script src="${ctx}/static/business/material/auditCollect.js"></script>
