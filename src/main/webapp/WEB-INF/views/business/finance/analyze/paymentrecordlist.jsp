<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<head>
    <style>
        .progress {
            height: 0px;
            transition: all .6s ease;
        }

        .progress-uploading {
            margin-top: 2px;
            height: 20px;
        }
    </style>
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

                    <div class="row">

                        <div class="col-md-3 form-group">
                            <input v-model="form.keyword" type="text"
                                   placeholder="项目编号/客户姓名/手机号/单据号" class="form-control"/>
                        </div>

                        <div class="col-md-3 form-group">
                            <input v-model="form.paymethodName" id="paymethodName"
                                   name="paymethodName" type="text"
                                   class="form-control datepicker" placeholder="请输入交款方式">
                        </div>

                        <div class="col-md-3 form-group">
                            <input v-model="form.paystartTime" v-el:paystart-time id="paystartTime"
                                   name="paystartTime" type="text" readonly
                                   class="form-control datepicker" placeholder="请选择收款开始时间">
                        </div>

                        <div class="col-md-3 form-group">
                            <input v-model="form.payendTime" v-el:payend-time id="payendTime"
                                   name="payendTime" type="text" readonly
                                   class="form-control datepicker" placeholder="请选择收款结束时间">
                        </div>

                    </div>

                    <div class="row">

                        <div class="col-md-3 form-group">
                            <select v-model="form.templateStageId"
                                    id="templateStageId"
                                    name="templateStageId"
                                    class="form-control">
                                <option value="">请选择交款阶段</option>
                                <option v-for="stage in stageType" :value="stage.itemId">{{stage.itemName}}</option>
                            </select>
                        </div>

                        <div class="col-md-3 form-group">
                            <select v-model="form.ifRcw"
                                    id="ifRcw"
                                    name="ifRcw"
                                    class="form-control" placeholder="请选择是否红冲">
                                <option value="">请选择是否红冲</option>
                                <option value="0" selected>否</option>
                                <option value="1">是</option>
                            </select>
                        </div>

                        <div class="col-md-1">
                            <div class="form-group">
                                <button id="searchBtn" type="button"  @click.prevent="query" class="btn btn-block btn-outline btn-default" alt="搜索"
                                        title="搜索">
                                    <i class="fa fa-search"></i>
                                </button>
                            </div>
                        </div>

                        <div class="col-md-1">
                            <div class="form-group">
                                <button id="exportData" type="button"
                                        class="btn btn-primary" alt="导出" title="导出"
                                        @click="exportData">
                                    <i class="fa ">导出</i>
                                </button>
                            </div>
                        </div>

                    </div>

                </form>

            </div>
            <table id="dataTable" width="100%"
                   class="table table-striped table-bordered table-hover">
            </table>
        </div>
    </div>
    <!-- ibox end -->
</div>


</div>
<script src="${ctx}/static/business/finance/analyze/paymentrecordlist.js"></script>
