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
                                   placeholder="项目编号/客户姓名/手机号" class="form-control"/>
                        </div>

                        <div class="col-md-2 form-group">
                            <select v-model="form.orderFlowStatus"
                                    id="orderFlowStatus"
                                    name="orderFlowStatus"
                                    class="form-control">
                                <option value="" selected>请选择订单状态</option>
                                <option value="STAY_TURN_DETERMINE">待转大定</option>
                                <option value="SUPERVISOR_STAY_ASSIGNED">督导组长待分配</option>
                                <option value="DESIGN_STAY_ASSIGNED">设计待分配</option>
                                <option value="APPLY_REFUND">申请退回</option>
                                <option value="STAY_DESIGN">待设计</option>
                                <option value="STAY_SIGN">待签约</option>
                                <option value="STAY_SEND_SINGLE_AGAIN">待重新派单</option>
                                <option value="STAY_CONSTRUCTION">待施工</option>
                                <option value="ON_CONSTRUCTION">施工中</option>
                                <option value="PROJECT_COMPLETE">已竣工</option>
                                <option value="ORDER_CLOSE">订单关闭</option>
                            </select>
                        </div>

                        <div class="col-md-2 form-group">
                            <input v-model="form.stratDate" v-el:strat-date id="stratDate"
                                   name="stratDate" type="text" readonly
                                   class="form-control datepicker" placeholder="请选择生单开始时间">
                        </div>

                        <div class="col-md-2 form-group">
                            <input v-model="form.endDate" v-el:end-date id="endDate"
                                   name="endDate" type="text" readonly
                                   class="form-control datepicker" placeholder="请选择生单结束时间">
                        </div>


                        <div class="col-md-1">
                            <div class="form-group">
                                <button id="searchBtn" type="button"  @click.prevent="query" class="btn btn-block btn-outline btn-default" alt="搜索"
                                        title="搜索" :disabled="loadAbledStoreFlag">
                                    <i class="fa fa-search"></i>
                                </button>
                            </div>
                        </div>

                        <div class="col-md-1">
                            <div class="form-group">
                                <button id="searchmanyBtn" type="button" @click="searchmanyBtn"
                                        class="btn btn-primary" :disabled="btnDisabled" alt="精准查询" title="精准查询">
                                    <i class="fa">精准查询</i>
                                </button>
                            </div>
                        </div>

                        <div class="col-md-1">
                            <div class="form-group">
                                <button id="exportData" type="button"
                                        class="btn btn-primary" alt="导出" title="导出"
                                        @click="exportData" :disabled="loadAbledStoreFlag">
                                    <i class="fa">导出</i>
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

<div id="searchmanyModel" class="modal modal-dialog fade" tabindex="-1" data-width="400">
    <form name="banner" novalidate class="form-horizontal" role="form">
        <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
            <h3 align="center">订单号(一行一个，用换行符分割)：</h3>
        </div>
        <div class="modal-body" style="text-align: center;margin-top: 15px">
            <textarea v-model="contractCodeMany" name="contractCodeMany" rows="14" cols="70"></textarea>
        </div>
        <div class="modal-footer">
            <button type="button" data-dismiss="modal" class="btn">取消</button>
            <button :disabled="submitting" type="button" @click="saveOrUpdate" class="btn btn-primary">确定</button>
        </div>
    </form>
</div>

</div>
<script src="${ctx}/static/business/finance/analyze/projectsummarizlist.js"></script>
