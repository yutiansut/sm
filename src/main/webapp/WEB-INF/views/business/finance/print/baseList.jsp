<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>

<div id="container" class="wrapper wrapper-content animated fadeInRight">
    <div id="breadcrumb">
        <bread-crumb :crumbs="breadcrumbs"></bread-crumb>
    </div>
    <!-- ibox start -->
    <div class="ibox">
        <div class="ibox-content">
            <div class="row">
                <form id="searchForm" @submit.prevent="query">

                    <div class="col-md-3 form-group">
                        <input v-model="form.keyword" type="text"
                               placeholder="项目编号|客户姓名|手机号" class="form-control"/>
                    </div>

                    <div class="col-md-2 form-group">
                        <input v-model="form.changeApplyStartDate" v-el:change-apply-start-date id="changeApplyStartDate"
                               name="paystartTime" type="text" readonly
                               class="form-control datepicker" placeholder="变更完成开始时间">
                    </div>

                    <div class="col-md-2 form-group">
                        <input v-model="form.changeApplyEndDate" v-el:change-apply-end-date id="changeApplyEndDate"
                               name="payendTime" type="text" readonly
                               class="form-control datepicker" placeholder="变更完成结束时间">
                    </div>

                    <div class="col-md-2">
                        <div class="form-group" >
                           <select v-model="form.printCount"  class="form-control" >
                                <option value="">--请选择打印状态--</option>
                                <option value="0" >未打印</option>
                                <option value="1" >已打印</option>
                           </select>
                        </div>
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
<script src="${ctx}/static/business/finance/print/baseList.js"></script>