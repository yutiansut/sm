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

                    <div class="col-md-2 form-group">
                        <input v-model="form.keyword" type="text"
                               placeholder="项目单号，客户姓名" class="form-control"/>
                    </div>

                    <%--<div class="col-md-4 form-group">--%>
                        <%--<label class="sr-only" for="designerName"></label>--%>
                        <%--<select v-model="form.designerName"--%>
                                <%--id="designerName"--%>
                                <%--name="designerName"--%>
                                <%--class="form-control">--%>
                            <%--<option value="">请选择设计师</option>--%>
                            <%--<option v-for="designer in designers" :value="designer.name">{{designer.name}}</option>--%>
                        <%--</select>--%>
                    <%--</div>--%>

                    <%--<div class="col-md-4 form-group">--%>
                        <%--<label class="sr-only" for="auditorName"></label>--%>
                        <%--<select v-model="form.auditorName"--%>
                                <%--id="auditorName"--%>
                                <%--name="auditorName"--%>
                                <%--class="form-control">--%>
                            <%--<option value="">请选择审计员</option>--%>
                            <%--<option v-for="auditor in auditors" :value="auditor.name">{{auditor.name}}</option>--%>
                        <%--</select>--%>
                    <%--</div>--%>

                    <%--<div class="col-md-4 form-group">--%>
                        <%--<label class="sr-only" for="changeStatus"></label>--%>
                        <%--<select v-model="form.changeStatus"--%>
                                <%--id="changeStatus"--%>
                                <%--name="changeStatus"--%>
                                <%--class="form-control">--%>
                            <%--<option value="">选择时间类别</option>--%>
                            <%--<option value="LATEST_CHANGE">最新变更时间</option>--%>
                            <%--<option value="SUBMIT_AUDIT_TIME">提交审计时间</option>--%>
                            <%--<option value="AUDIT_PASS_TIME">审计通过时间</option>--%>
                            <%--<option value="AUDIT_FAILED_TIME">审计未通过时间</option>--%>
                            <%--<option value="SINGLE_TURN_TIME">转单时间</option>--%>
                        <%--</select>--%>
                    <%--</div>--%>
                    <%--<div class="col-md-4 form-group">--%>
                        <%--<input v-model="form.startDate" v-el:start-date id="startDate"--%>
                               <%--name="startDate" type="text" readonly--%>
                               <%--class="form-control datepicker" placeholder="请选择开始时间">--%>
                    <%--</div>--%>
                    <%--<div class="col-md-4 form-group">--%>
                        <%--<input v-model="form.endDate" v-el:end-date id="endDate"--%>
                               <%--name="endDate" type="text" readonly--%>
                               <%--class="form-control datepicker" placeholder="请选择结束时间">--%>
                    <%--</div>--%>

                    <div class="col-md-2 form-group">
                        <label class="sr-only" for="currentStatus"></label>
                        <select v-model="form.currentStatus"
                                id="currentStatus"
                                name="currentStatus"
                                class="form-control">
                            <option value="">选择选材单状态</option>
                            <option value="MATERIALDEPARTMENTAUDIT" v-if="roleNameList && (roleNameList.indexOf(roleNameAllList[0]) != -1
                                    || roleNameList.indexOf(roleNameAllList[1]) != -1
                                    || roleNameList.indexOf(roleNameAllList[3]) != -1
                                    || roleNameList.indexOf(roleNameAllList[4]) != -1
                                    || roleNameList.indexOf(roleNameAllList[5]) != -1)">材料部审核中</option>
                            <option value="DESIGNDIRECTORINTHEAUDIT">设计总监审核中</option>
                            <option value="AUDITREVIEW">审计审核中</option>
                            <option value="CHANGEORDERRECALL" v-if="roleNameList && (roleNameList.indexOf(roleNameAllList[0]) != -1
                                    || roleNameList.indexOf(roleNameAllList[2]) != -1
                                    || roleNameList.indexOf(roleNameAllList[3]) != -1
                                    || roleNameList.indexOf(roleNameAllList[4]) != -1
                                    || roleNameList.indexOf(roleNameAllList[5]) != -1)">
                                变更单撤回
                            </option>
                            <option value="EXAMINATIONPASSED">审计通过</option>
                            <option value="AUDITFAILED">审计未通过</option>
                        </select>
                    </div>
                    <div class="col-md-1 form-group">
                        <button id="searchBtn" type="submit"
                                class="btn btn-block btn-outline btn-default" alt="搜索"
                                title="搜索">
                            <i class="fa fa-search"></i>
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

<div id="auditModal" class="modal modal-dialog fade" tabindex="1">
    <div class="modal-header" style="text-align: center">
        <h3>审计审核</h3>
    </div>
    <div class="modal-body">
        <label class="control-label col-sm-3">备注：</label>
        <input type="text" v-model="remark">
    </div>
    <div class="modal-footer" style="text-align: center">
        <button :disabled="disabled" type="button" @click="pass" class="btn btn-primary">通过</button>
        <button :disabled="disabled" type="button" @click="nopass" class="btn btn-danger">不通过</button>
        <button :disabled="disabled" type="button" data-dismiss="modal" class="btn">关闭</button>
    </div>
</div>

<div id="showDetailModal" class="modal fade" tabindex="-1" data-width="1200">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal"
                aria-hidden="true">×
        </button>
    </div>
    <div class="modal-body">
        <div class="ibox">
            <div class="ibox-title">
                <div class="col-sm-2">客户信息</div>
            </div>
            <div class="ibox-content">
                <div class="panel-heading" role="tab" id="headingOne">
                    <div class="row detail-stranding-book detail-state" v-cloak>
                        <div class="col-sm-12">
                            <%--<label class="control-label col-sm-2">客户信息</label>--%>
                            <table width="100%" class="table table-striped table-bordered table-hover">
                                <tbody align="center">
                                <tr>
                                    <td style="width: 70px">客户姓名</td>
                                    <td style="width: 70px">{{customerContract.customerName}}</td>
                                    <td style="width: 70px">所选套餐</td>
                                    <td style="width: 70px">{{customerContract.mealName}}</td>
                                    <td style="width: 70px">建筑面积</td>
                                    <td style="width: 70px">{{customerContract.buildArea}}</td>
                                </tr>
                                <tr>
                                    <td>联系方式</td>
                                    <td>{{customerContract.customerMobile}}</td>
                                    <td>房屋地址</td>
                                    <td>{{customerContract.addressProvince}}&nbsp;{{customerContract.addressCity}}&nbsp;{{customerContract.addressArea}}&nbsp;{{customerContract.houseAddr}}</td>
                                    <td>房屋户型</td>
                                    <td>{{customerContract.layout}}</td>
                                </tr>
                                <tr>
                                    <td>有无电梯</td>
                                    <td>{{customerContract.elevator | goType}}</td>
                                    <td>房屋状况</td>
                                    <td>{{customerContract.houseCondition | houseStatus}}</td>
                                    <td>计价面积</td>
                                    <td>{{customerContract.valuateArea}}</td>
                                </tr>
                                <tr>
                                    <td>计划开工时间</td>
                                    <td>{{customerContract.contractStartTime | goDate}}</td>
                                    <td>计划完工时间</td>
                                    <td>{{customerContract.contractCompleteTime | goDate}}</td>
                                    <td></td>
                                    <td></td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="ibox">
            <div class="ibox-heading">
                变更减项商品信息 &nbsp;&nbsp;<span  style="color: red;font-weight:bold" >合计：{{subMoney}}元</span>
            </div>
            <div class="ibox-content">
                <table class="table table-striped table-bordered table-hover">
                    <tr>
                        <td hidden>id</td>
                        <th>类别</th>
                        <th>商品类目</th>
                        <th>品牌</th>
                        <th>名称</th>
                        <th>型号</th>
                        <th>属性</th>
                        <th>位置</th>
                        <th >原用量</th>
                        <th >现用量</th>
                        <th>减少量</th>
                        <th>价格</th>
                        <th>设计备注</th>
                        <th>材料状态</th>
                        <th >合计</th>
                    </tr>
                    <template v-for=" item in skuList" v-if="item.num<0">
                        <tr>
                            <td hidden><input type="text" v-model="ids[$index]" :value="item.id"></td>
                            <td>{{item.categoryCode | categoryFilter}}</td>
                            <td>{{item.cataLogName}}</td>
                            <td>{{item.brand}}</td>
                            <td>{{item.productName}}</td>
                            <td>{{item.skuModel}}</td>
                            <td>{{item.attribute1}}-{{item.attribute2}}-{{item.attribute3}}</td>
                            <td>{{item.domainName}}</td>
                            <td >{{item.originalDosage}}</td>
                            <td >{{item.lossDosage}}</td>
                            <td>{{item.num | absNum}}{{item.materialUnit | unitFilter}}</td>
                            <td>{{item.price}}</td>
                            <td>{{item.designRemark}}</td>
                            <td><select  disabled type="text" v-model="item.materialsStatus">
                                <option value="">请选择</option>
                                <option value="NOT_MEASURED">未测量</option>
                                <option value="MEASURED">已测量</option>
                                <option value="NO_ORDERS">未下单</option>
                                <option value="ALREADY_ORDERED">已下单</option>
                                <option value="NOT_SHIPPED">未发货</option>
                                <option value="SHIPPED">已发货</option>
                                <option value="NOT_INSTALLED">未安装</option>
                                <option value="INSTALLED">已安装</option>
                                <option value="UNFILLED_ORDER_CONDITIONS">未满足下单条件</option>
                            </select>
                            </td>
                            <template v-if="item.categoryCode=='PACKAGESTANDARD'">
                                <td >0元</td>
                            </template>
                            <template v-else >
                                <td >{{item.num *item.price}}元</td>
                            </template>
                        </tr>
                    </template>
                </table>
            </div>
        </div>
        <div class="ibox">
            <div class="ibox-heading">
                变更增项商品信息 &nbsp;&nbsp;<span  style="color: red;font-weight:bold">合计：{{addMoney}}元</span>
            </div>
            <div class="ibox-content">
                <table class="table table-striped table-bordered table-hover">
                    <tr>
                        <th>类别</th>
                        <th>商品类目</th>
                        <th>品牌</th>
                        <th>名称</th>
                        <th>型号</th>
                        <th>属性</th>
                        <th>位置</th>
                        <th>原用量</th>
                        <th>现用量</th>
                        <th>增加量</th>
                        <th>价格</th>
                        <th>设计备注</th>
                        <th >合计</th>
                    </tr>
                    <template v-for=" item in skuList" v-if="item.num>0 ">
                        <tr>
                            <td>{{item.categoryCode |categoryFilter}}</td>
                            <td>{{item.cataLogName}}</td>
                            <td>{{item.brand}}</td>
                            <td>{{item.productName}}</td>
                            <td>{{item.skuModel}}</td>
                            <td>{{item.attribute1}}-{{item.attribute2}}-{{item.attribute3}}</td>
                            <td>{{item.domainName}}</td>
                            <td>{{item.originalDosage}}</td>
                            <td>{{item.lossDosage}}</td>
                            <td>{{item.num}}{{item.materialUnit |unitFilter}}</td>
                            <td>{{item.price}}</td>
                            <td>{{item.designRemark}}</td>
                            <template v-if="item.categoryCode=='PACKAGESTANDARD'">
                                <td >0元</td>
                            </template>
                            <template v-else >
                                <td >{{item.num *item.price}}元</td>
                            </template>
                        </tr>
                    </template>
                </table>
            </div>
        </div>

        <div class="ibox">
            <div class="ibox-heading">
                其他金额增减信息 &nbsp;&nbsp;<span style="color: red;font-weight:bold" >合计：{{otherMoney}}元</span>
            </div>
            <div class="ibox-content">
                <table class="table table-striped table-bordered table-hover">
                    <tr>
                        <td>增减金额类型</td>
                        <td>增减金额原因</td>
                        <td>增减金额</td>

                    </tr>
                    <template v-for=" item in otherList">
                        <tr>
                            <td>{{item.addReduceType | addReduceType}}</td>
                            <td>{{item.addReduceReason}}</td>
                            <td>{{item.quota}}元</td>
                        </tr>
                    </template>
                </table>
            </div>
        </div>
        <div class="ibox" >
            <div class="ibox-heading">
                <span  style="color: red;font-weight:bold"  >&nbsp;&nbsp;合计：{{addMoney+subMoney+otherMoney}}元</span>
            </div>
            <div class="ibox-content">
            </div>
        </div>
        <div class="ibox">
            <div class="ibox-content">
                <div class="form-group" >
                    <label class="control-label col-sm-1">备注：</label>
                    <div class="col-sm-12">
                        <p v-if="remarks.remarks1!=null">材料部备注：{{remarks.remarks1}}</p>
                        <p v-if="remarks.remarks2!=null">设计总监备注： {{remarks.remarks2}}</p>
                        <p v-if="remarks.remarks3!=null">审计备注： {{remarks.remarks3}}</p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>



<%@include file="/WEB-INF/views/business/component/contractinfo.jsp" %>
<script src="/static/business/component/contractInfo.js"></script>
<script src="/static/business/customercontract/city.js"></script>
<script src="/static/business/material/changeordermange.js"></script>
