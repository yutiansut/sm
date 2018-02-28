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
                                   placeholder="项目编号|客户姓名|手机号" class="form-control"/>
                        </div>

                        <div class="col-md-2 form-group">
                            <input v-model="form.creator" type="text"
                                   placeholder="操作人" class="form-control"/>
                        </div>

                        <div class="col-md-2 form-group">
                            <input v-model="form.paystartTime" v-el:paystart-time id="paystartTime"
                                   name="paystartTime" type="text" readonly
                                   class="form-control datepicker" placeholder="请选择收款开始时间">
                        </div>

                        <div class="col-md-2 form-group">
                            <input v-model="form.payendTime" v-el:payend-time id="payendTime"
                                   name="payendTime" type="text" readonly
                                   class="form-control datepicker" placeholder="请选择收款结束时间">
                        </div>

                        <div class="col-md-1">
                            <div class="form-group">
                                <button id="searchBtn" type="button"  @click.prevent="query" class="btn btn-block btn-outline btn-default" alt="搜索"
                                        title="搜索">
                                    <i class="fa fa-search"></i>
                                </button>
                            </div>
                        </div>

                        <div class="col-md-1 form-group">
                            <button id="printMany" type="button"
                                    class="btn" :class="btnClass" :disabled="btnDisabled" alt="批量打印" title="批量打印"
                                    @click="printMany">
                                <i class="fa ">批量打印</i>
                            </button>
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
<script src="${ctx}/static/business/finance/print/receiptlist.js"></script>
