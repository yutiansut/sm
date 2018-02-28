<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<head>
    <title>详情</title>
    <link rel="stylesheet" href="/static/core/css/tab.css">
    <style>
        .panel-stranding-book .panel-heading {
            background: #f5f7f9;
            padding: 5px 15px 7px 15px;
        }

        .panel-stranding-book .panel-title a {
            display: inline-block;
            width: 100%;
            color: #657180;
            font-weight: normal;
        }

        .panel-stranding-book .panel-title .title {
            margin-right: 40px;
        }

        .detail-stranding-book {
            margin-bottom: 20px;
        }

        [v-cloak] {
            display: none;
        }

        .border-bottom-style {
            border-bottom: 1px solid #ddd !important
        }

        .table-last-color-light tbody tr:last-child {
            color: red
        }

        .order-content .title-state {
            display: none
        }

        .order-content .panel-default {
            border: none
        }

        @media screen and (max-width: 500px) {
            .order-content .title-state {
                display: block;
                border-bottom: none !important;
                margin-bottom: 15px;
            }

            .order-content .detail-state {
                margin: 0 0;
                border-radius: 0 0 5px 5px;
                max-height: 0px;
                min-height: 0px;
                overflow: hidden;
                -webkit-transition: max-height .4s;
                transition: max-height .4s;
            }

            .order-content .hasHeight {
                max-height: 1000px;
                transition: max-height ease-in .4s;
            }
        }
    </style>
</head>


<body id="app" class="fixed-sidebar full-height-layout gray-bg">
<div id="container" class="wrapper wrapper-content">
    <!-- 面包屑 -->
    <div id="breadcrumb">
        <bread-crumb :crumbs="breadcrumbs"></bread-crumb>
    </div>
    <!-- ibox start -->
    <div class="ibox">
        <div class="ibox-content order-content">
            <div class="panel-group">
                <div class="panel panel-default">
                    <div class="row" style="padding-left: 15px">
                        <label>项目编号：</label>
                        <a @click="queryProjectDetail(contractPayment.contractCode)"><u>{{contractPayment.contractCode}}</u></a><span style="color:red;">({{orderFlowStatus}})</span>
                    </div>
                    <div :class="{hasHeight:isShow }" class="row detail-stranding-book detail-state" v-cloak>

                        <div class="col-sm-3 col-xs-12">
                            <label>客户姓名/电话：</label>
                            {{contractPayment.customerName}}/{{contractPayment.customerMobile}}
                        </div>
                        <div class="col-sm-3 col-xs-12">
                            <label>第二联系人/电话：</label>
                            {{contractPayment.secondContact}}/{{contractPayment.secondContactMobile}}
                        </div>
                        <div class="col-sm-3 col-xs-12">
                            <label>客户来源：</label>
                            {{contractPayment.incomeSource | goOrigin}}
                        </div>
                        <div class="col-sm-3 col-xs-12">
                            <label>户型/面积：</label>
                            {{contractPayment.layout}}/{{contractPayment.buildArea}}㎡
                        </div>
                        <div class="col-sm-3 col-xs-12">
                            <label>客服/签单时间：</label>
                            {{contractPayment.serviceName}}/{{contractPayment.createTime | goDate}}
                        </div>
                        <div class="col-sm-3 col-xs-12">
                            <label>参加活动：</label>
                            {{contractPayment.activityName}}
                        </div>
                        <div class="col-sm-3 col-xs-12">
                            <label>套餐类型：</label>
                            {{contractPayment.mealName}}
                        </div>
                        <div class="col-sm-3 col-xs-12">
                            <label>特殊优惠：</label>
                            {{contractPayment.discountName}}
                        </div>
                        <div class="col-sm-3 col-xs-12">
                            <label>设计师姓名/电话：</label>
                            {{contractPayment.designer}}/{{contractPayment.designerMobile}}
                        </div>
                        <div class="col-sm-9">
                            <label>客户地址：</label>
                            {{contractPayment.houseAddr}}
                        </div>
                    </div>
                </div>
                <div :class="{hasHeight:isShow }" class="row detail-stranding-book detail-state" v-cloak style="position:relative">

                    <div class="col-sm-12">
                        <label>预定合同实收总金额 / 已抵扣金额 :</label>
                        {{finaProjectAccount.depositTotalPayed || 0}} / {{finaProjectAccount.depositTotalDeduct || 0}}
                    </div>
                    <div class="col-sm-12">
                        <label>拆改合同原始总金额 / 实收总金额 :</label>
                        {{finaProjectAccount.modifyExpectAmount || 0}} / {{finaProjectAccount.modifyTotalPayed || 0}}
                    </div>
                    <div class="col-sm-12">
                        <label>施工合同原始总金额 / 实收总金额 :</label>
                        {{finaProjectAccount.constructExpectAmount || 0}} / {{finaProjectAccount.constructTotalPayed || 0}}
                    </div>
                    <div class="col-sm-12">
                        <label>变更总金额 :</label>
                        {{finaProjectAccount.changeAmount || 0}}
                    </div>
                    <div class="col-sm-12">
                        <label>赔款总金额 :</label>
                        {{finaProjectAccount.reparationAmount || 0}}
                    </div>
                    <span v-if="flag!='1'">
                        <button type="button" @click="selectProject" class="btn btn-primary" style="position: absolute;right:15px;top:85px;">选择项目</button>
                    </span>
                </div>
            </div>
            <div>
                <Tabs v-ref:tabs type="card" @on-click="clickEvent">
                    <Tab-pane v-for="($index,item) in tabList" :label="item.label">
                        <payment-info v-if="$index == 0" :msg="index"></payment-info>
                        <payment-plan v-if="$index == 1" :msg="index"></payment-plan>
                        <change-record v-if="$index == 2" :msg="index"></change-record>
                        <claim-record v-if="$index == 3" :msg="index"></claim-record>
                        <refund-record v-if="$index == 4" :msg="index"></refund-record>
                        <back-record v-if="$index == 5" :msg="index"></back-record>
                        <operation-record v-if="$index == 6" :msg="index"></operation-record>
                    </Tab-pane>
                </Tabs>
            </div>
        </div>
        <!-- ibox end -->
    </div>
    <!-- container end-->
</div>

<%--选择项目弹框--%>
<div id="mdnOrder" class="modal modal-dialog fade" tabindex="-1" data-width="900">
    <div class="modal-header">
        <form id="searchForm" action='#'>
            <div class="col-md-4">
                <div class="form-group">
                    <input v-model="form.keyword"
                           type="text"
                           placeholder="姓名/手机号/项目编号" class="form-control" @keyup.13="query"/>
                </div>
            </div>
            <input type="text" value="解决bootstrap与AJAX异步提交表单的冲突(回车刷新页面问题)" hidden />
            <div class="col-md-3 text-right">
                <div class="form-group">
                    <button id="searchBtn" type="button" @click.prevent="query"
                            class="btn btn-block btn-outline btn-default" alt="搜索"
                            title="搜索">
                        <i class="fa fa-search"></i>
                    </button>
                </div>
            </div>
        </form>
    </div>
    <div class="modal-body" style="text-align: center;margin-top: 5px">
        <table id="dataTable" width="100%" class="table table-striped table-bordered table-hover">
        </table>
    </div>
    <div class="modal-footer">
        <button type="button" data-dismiss="modal" class="btn">关闭</button>
        <button v-if="flag" type="button" @click="commitUsers" class="btn btn-primary">确定</button>
    </div>
</div>

<%--详情弹框--%>
<div id="detailModal" class="modal fade" tabindex="-1" data-width="700">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal"
                aria-hidden="true">×
        </button>
    </div>
    <div class="modal-body">
        订单项目信息
        <div class="panel-heading" role="tab" id="headingOne" style="padding: 10px 9%;border:1px solid #c4c4c4">
            <div :class="{hasHeight:isShow }" class="row detail-stranding-book detail-state" v-cloak>
                <div class="col-sm-4 col-xs-12">
                    <label>户型：</label>
                    {{customerContract.layout}}{{aa}}
                </div>
                <div class="col-sm-4 col-xs-12">
                    <label>房屋面积：</label>
                    {{customerContract.buildArea}}
                </div>
                <div class="col-sm-4 col-xs-12">
                    <label>房屋状况：</label>
                    {{customerContract.houseCondition | goHoseType}}
                </div>
                <div class="col-sm-4 col-xs-12">
                    <label>是否期房：</label>
                    {{customerContract.forwardDeliveryHousing | goForwardDeliveryHousing}}
                </div>
                <div class="col-sm-4 col-xs-12">
                    <label>有无电梯：</label>
                    {{customerContract.elevator | goElevator}}
                </div>
                <div class="col-sm-4 col-xs-12">
                    <label>工程地址：</label>
                    {{customerContract.addressProvince}}{{customerContract.addressCity}}{{customerContract.addressArea}}{{customerContract.houseAddr}}
                </div>
                <div class="col-sm-4 col-xs-12">
                    <label>计划量房时间：</label>
                    {{customerContract.planHouseTime}}
                </div>
                <div class="col-sm-4 col-xs-12">
                    <label>计划装修时间：</label>
                    {{customerContract.planDecorateTime}}
                </div>
                <div class="col-sm-4 col-xs-12">
                    <label>创建时间：</label>
                    {{customerContract.createTime}}
                </div>
                <div class="col-sm-4 col-xs-12">
                    <label>活动：</label>
                    {{customerContract.activityName}}
                </div>
                <div class="col-sm-4 col-xs-12">
                    <label>折扣：</label>
                    {{customerContract.discountName}}
                </div>
            </div>
        </div>
        订单预约合同信息
        <div class="panel-heading" role="tab" id="headingTwo" style="padding: 10px 9%;border:1px solid #c4c4c4">
            <div :class="{hasHeight:isShow }" class="row detail-stranding-book detail-state" v-cloak>
                <div class="col-sm-4 col-xs-12">
                    <label>套餐类型：</label>
                    {{customerContract.packageType}}
                </div>
                <div class="col-sm-4 col-xs-12">
                    <label>拆除修复费：</label>
                    {{customerContract.dismantleRepairCost}}
                </div>
                <div class="col-sm-4 col-xs-12">
                    <label>远程费：</label>
                    {{customerContract.longRangeCost}}
                </div>
                <div class="col-sm-4 col-xs-12">
                    <label>搬运费：</label>
                    {{customerContract.carryCost}}
                </div>
                <div class="col-sm-4 col-xs-12">
                    <label>预算总金额：</label>
                    {{customerContract.totalBudgetAmount}}
                </div>
                <div class="col-sm-4 col-xs-12">
                    <label>预付款：</label>
                    {{customerContract.advancePayment}}
                </div>
                <div class="col-sm-12">
                    <label>备注：</label>
                    {{customerContract.remark}}
                </div>
            </div>
        </div>
    </div>
</div>

<%--交款弹框页面--%>
<div id="depositModal" class="modal modal-dialog fade" tabindex="-1">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
        <h3 align="center">收款信息</h3>
    </div>
    <div class="modal-body">
        <validator name="validation">
            <form name="banner" novalidate class="form-horizontal" role="form">
                <input type="hidden" v-model="id">
                <div class="form-group">
                    <label class="control-label col-sm-3">收款阶段：</label>
                    <div class="col-sm-4">
                        <span v-if="!paymoneyRecordDto.payStage||paymoneyRecordDto.payStage.stageName=='定金'">
                            <input v-model="paymoneyRecord.itemName" type="radio" value="定金" checked="checked"/>定金
                        </span>
                        <span v-else>
                            <input v-model="paymoneyRecord.itemName" type="radio" value="定金"/>定金
                        </span>
                        <span v-if="paymoneyRecordDto.payStage&&paymoneyRecordDto.payStage.stageName!='定金'">
                            <input v-model="paymoneyRecord.itemName" v-if="paymoneyRecordDto.payStage&&paymoneyRecordDto.payStage.stageName!='定金'"
                                   type="radio" value="paymoneyRecordDto.payStage.stageName" checked="checked"/>
                            <span v-if="paymoneyRecordDto.payStage&&paymoneyRecordDto.payStage.stageName!='定金'">{{paymoneyRecordDto.payStage.stageName}}</span>
                        </span>

                    </div>
                </div>
                <div class="form-group">
                    <div class="form-group" :class="{'has-error':$validation.payTime.invalid && $validation.touched}">
                        <label class="col-sm-3 control-label">收款日期：</label>
                        <div class="col-sm-4" :class="{'has-error':$validation.payTime.invalid && $validation.touched}">
                            <input v-model="paymoneyRecord.payTime"
                                   v-validate:pay-time="{required:{rule:true,message:'请输入收款日期'}}"
                                   v-el:pay-time
                                   maxlength="50"
                                   tabindex="4"
                                   name="payTime" type="text" class="form-control datepicker" readonly>
                            <span v-cloak v-if="$validation.payTime.invalid && $validation.touched" class="help-absolute">
                              <span v-for="error in $validation.payTime.errors">
                                {{error.message}} {{($index !== ($validation.payTime.errors.length -1)) ? ',':''}}
                              </span>
                            </span>
                        </div>
                    </div>
                </div>
                <template v-if="paymoneyRecord.itemName == '定金'">
                    <div class="form-group" :class="{'has-error':$validation.customerName.invalid && $validation.touched}">
                        <label class="control-label col-sm-3">客户姓名：</label>
                        <div class="col-sm-4">
                            <input v-model="paymoneyRecordDto.customerName"
                                   name="customerName" type="text" placeholder="客户姓名" maxlength="20" class="form-control"/>
                            <span style="color:red;">请认真核对，确认后不可更改</span>
                            <span v-cloak v-if="$validation.customerName.invalid && $validation.touched" class="help-absolute">
                                  <span v-for="error in $validation.customerName.errors">
                                    {{error.message}} {{($index !== ($validation.customerName.errors.length -1)) ? ',':''}}
                                  </span>
                            </span>
                        </div>
                    </div>
                    <div class="form-group" :class="{'has-error':$validation.customerMobile.invalid && $validation.touched}">
                        <label class="control-label col-sm-3">客户手机号：</label>
                        <div class="col-sm-4">
                            <input v-model="paymoneyRecordDto.customerMobile"
                                   v-validate:customer-mobile="['mobile']"
                                   name="customerMobile" type="text" placeholder="客户手机号" class="form-control"/>
                            <span style="color:red;">请认真核对，确认后不可更改</span>
                            <span v-cloak v-if="$validation.customerMobile.invalid && $validation.touched" class="help-absolute" />
                            <span v-for="error in $validation.customerMobile.errors">
                                {{error.message}} {{($index !== ($validation.customerMobile.errors.length -1)) ? ',':''}}
                              </span>
                            </span>
                        </div>
                    </div>
                    <div class="form-group" >
                        <label class="control-label col-sm-3">定金选项：</label>
                        <div class="col-sm-4">
                            <label >
                                <input v-model="paymoneyRecordDto.signedDepositContract" type="checkbox" @click="changeContract(paymoneyRecordDto.signedDepositContract)"/>有预定合同
                            </label>
                            <label >
                                <input v-model="paymoneyRecordDto.depositEnableReturnBack" type="checkbox"/>合同中无可退字样
                            </label>
                        </div>
                    </div>
                    <div class="form-group" :class="{'has-error':$validation.payerName.invalid && $validation.touched}">
                        <label class="control-label col-sm-3">交款人姓名：</label>
                        <div class="col-sm-4">
                            <input v-model="paymoneyRecord.payerName"
                                   name="payerName" type="text" placeholder="交款人姓名" class="form-control"/>
                            <span v-cloak v-if="$validation.payerName.invalid && $validation.touched" class="help-absolute" />
                                <span v-for="error in $validation.payerName.errors">
                                    {{error.message}} {{($index !== ($validation.payerName.errors.length -1)) ? ',':''}}
                                </span>
                            </span>
                        </div>
                    </div>
                    <div class="form-group" :class="{'has-error':$validation.payerMobile.invalid && $validation.touched}">
                        <label class="control-label col-sm-3">交款人电话：</label>
                        <div class="col-sm-4">
                            <input v-model="paymoneyRecord.payerMobile"
                                   v-validate:payer-mobile="['mobile']"
                                   name="payerMobile" type="text" placeholder="交款人电话" class="form-control"/>
                            <span v-cloak v-if="$validation.payerMobile.invalid && $validation.touched" class="help-absolute" />
                                <span v-for="error in $validation.payerMobile.errors">
                                    {{error.message}} {{($index !== ($validation.payerMobile.errors.length -1)) ? ',':''}}
                                </span>
                            </span>
                        </div>
                    </div>


                    <div class="form-group" :class="{'has-error':$validation.methodCode.invalid && $validation.touched}">
                        <label class="control-label col-sm-3">交款方式：</label>
                        <div class="col-sm-4">
                            <select class="form-control" v-validate:method-code="{
                                        required:{rule:true,message:'请选择支付方式'}
                                   }" v-model="paymethodObj" @change="changePayType">
                                <option value="">请选择支付方式</option>
                                <option :value="item.values" v-for="item in depMethodList">{{item.methodName}}</option>
                            </select>

                            <span v-cloak v-if="$validation.paymethodCode.invalid && $validation.touched"
                                  class="help-absolute">
                                <span v-for="error in $validation.paymethodCode.errors">
                                    {{error.message}} {{($index !==
                                    ($validation.paymethodCode.errors.length -1)) ? ',':''}}
                                </span>
                            </span>
                        </div>
                    </div>

                    <%--银行/支行--%>
                    <div v-if="payType == 'POS'" class="form-group" :class="{'has-error':$validation.bankObj.invalid && $validation.touched}">
                        <label class="control-label col-sm-3">银行：</label>
                        <div class="col-sm-4">
                            <select class="form-control" v-model="bankObj" @change="changeBank" v-validate:bank-obj="{
                                        required:{rule:true,message:'请选择银行'}}">
                                <option value="">请选择银行</option>
                                <option :value="item.values" v-for="item in attrBankList">{{item.attrName}}</option>
                            </select>
                            <span v-cloak v-if="$validation.bankObj.invalid && $validation.touched"
                                  class="help-absolute">
                                <span v-for="error in $validation.bankObj.errors">
                                    {{error.message}} {{($index !==
                                    ($validation.bankObj.errors.length -1)) ? ',':''}}
                                </span>
                            </span>
                        </div>
                    </div>
                    <div v-if="payType == 'POS'" class="form-group" :class="{'has-error':$validation.branchObj.invalid && $validation.touched}">
                        <label class="control-label col-sm-3">支行：</label>
                        <div class="col-sm-4">
                            <select class="form-control" v-model="branchObj" @change="changeBranch" v-validate:branch-obj="{
                                        required:{rule:true,message:'请选择支行'}}">
                                <option value="">请选择支行</option>
                                <option :value="item.values" v-for="item in attrBranchList">{{item.attrName}}</option>
                            </select>
                            <span v-cloak v-if="$validation.branchObj.invalid && $validation.touched"
                                  class="help-absolute">
                                <span v-for="error in $validation.branchObj.errors">
                                    {{error.message}} {{($index !==
                                    ($validation.branchObj.errors.length -1)) ? ',':''}}
                                </span>
                            </span>
                        </div>
                    </div>
                    <div class="form-group" :class="{'has-error':$validation.payManualFlag.invalid && $validation.touched}">
                        <label class="control-label col-sm-3">款项类别：</label>
                        <%--定金--%>
                        <div class="col-sm-4">
                            <select class="form-control" v-validate:pay-manual-flag="{
                                        required:{rule:true,message:'请选择款项类别'}
                                   }" v-model="paymoneyRecord.payManualFlag">
                                <option value="">请选择款项类别</option>{{item}}
                                <option :value="item" v-for="item in paymoneyRecordDto.depositPaymentFlagList">{{item}}</option>
                            </select>
                            <span v-cloak v-if="$validation.payManualFlag.invalid && $validation.touched"
                                  class="help-absolute">
                                <span v-for="error in $validation.payManualFlag.errors">
                                    {{error.message}} {{($index !==
                                    ($validation.payManualFlag.errors.length -1)) ? ',':''}}
                                </span>
                            </span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label  class="control-label col-sm-3">应收金额：</label>
                        <div class="col-sm-4">
                            <input v-model="paymoneyRecordDto.depositExpectPay" readonly="readonly" type="text" placeholder="应收金额" class="form-control"/>
                        </div>
                    </div>
                    <div class="form-group" :class="{'has-error':$validation.actualReceived.invalid && $validation.touched}">
                        <label  class="control-label col-sm-3">实收金额：</label>
                        <div class="col-sm-4">
                            <input @keyup="caluCharge" v-model="paymoneyRecord.actualReceived" v-validate:actual-received="['num']" type="text"  class="form-control"/>
                            <span v-cloak
                                  v-if="$validation.actualReceived.invalid && $validation.touched"
                                  class="help-absolute">
                                <span v-for="error in $validation.actualReceived.errors">
                                    {{error.message}} {{($index !== ($validation.actualReceived.errors.length -1)) ? ',':''}}
                                </span>
                            </span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label  class="control-label col-sm-3">手续费：</label>
                        <div class="col-sm-4">
                            <input v-model="paymoneyRecord.costfeeAmount" readonly="readonly" type="text" placeholder="手续费" class="form-control"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label  class="control-label col-sm-3">收据号：</label>
                        <div class="col-sm-4">
                            <input v-model="paymoneyRecordDto.receiptNum" type="text" placeholder="收据号" class="form-control" disabled/>
                            <span v-cloak
                                  v-if="$validation.receiptNum.invalid && $validation.touched"
                                  class="help-absolute">
                                <span v-for="error in $validation.receiptNum.errors">
                                    {{error.message}} {{($index !== ($validation.receiptNum.errors.length -1)) ? ',':''}}
                                </span>
                            </span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label  class="control-label col-sm-3">摘要：</label>
                        <div class="col-sm-4">
                            <textarea v-model="paymoneyRecord.remark" placeholder="摘要" class="form-control"></textarea>
                            <span v-cloak
                                  v-if="$validation.remark.invalid && $validation.touched"
                                  class="help-absolute">
                                <span v-for="error in $validation.remark.errors">
                                    {{error.message}} {{($index !== ($validation.remark.errors.length -1)) ? ',':''}}
                                </span>
                            </span>
                        </div>
                    </div>
                </template>

                <%--其他交款阶段页面--%>
                <template v-else class="form-group">
                    <div class="form-group" :class="{'has-error':$validation.payerName.invalid && $validation.touched}">
                        <label class="control-label col-sm-3">交款人姓名：</label>
                        <div class="col-sm-4">
                            <input v-model="paymoneyRecord.payerName"
                                   name="payerName" type="text" placeholder="交款人姓名" class="form-control"/>
                            <span v-cloak v-if="$validation.payerName.invalid && $validation.touched" class="help-absolute" />
                            <span v-for="error in $validation.payerName.errors">
                                    {{error.message}} {{($index !== ($validation.payerName.errors.length -1)) ? ',':''}}
                                </span>
                            </span>
                        </div>
                    </div>
                    <div class="form-group" :class="{'has-error':$validation.payerMobile.invalid && $validation.touched}">
                        <label class="control-label col-sm-3">交款人电话：</label>
                        <div class="col-sm-4">
                            <input v-model="paymoneyRecord.payerMobile"
                                   v-validate:payer-mobile="['mobile']"
                                   name="payerMobile" type="text" placeholder="交款人电话" class="form-control"/>
                            <span v-cloak v-if="$validation.payerMobile.invalid && $validation.touched" class="help-absolute" />
                            <span v-for="error in $validation.payerMobile.errors">
                                    {{error.message}} {{($index !== ($validation.payerMobile.errors.length -1)) ? ',':''}}
                                </span>
                            </span>
                        </div>
                    </div>


                    <div class="form-group" :class="{'has-error':$validation.methodCode.invalid && $validation.touched}">
                        <label class="control-label col-sm-3">交款方式：</label>
                        <div class="col-sm-4">
                            <select class="form-control" v-validate:method-code="{
                                        required:{rule:true,message:'请选择支付方式'}
                                   }" v-model="paymethodObj" @change="changePayType">
                                <option value="">请选择支付方式</option>
                                <option :value="item.values" v-for="item in curMethodList">{{item.methodName}}</option>
                            </select>

                            <span v-cloak v-if="$validation.paymethodCode.invalid && $validation.touched"
                                  class="help-absolute">
                                <span v-for="error in $validation.paymethodCode.errors">
                                    {{error.message}} {{($index !==
                                    ($validation.paymethodCode.errors.length -1)) ? ',':''}}
                                </span>
                            </span>
                        </div>
                    </div>

                    <%--抵定金列表--%>
                    <div v-if = "payType =='DEDUCT'" class="form-group">
                        <label class="control-label col-sm-3">可抵用定金</label>
                    </div>
                    <div v-if = "payType =='DEDUCT'" class="form-group" class="col-sm-9">
                        <div class="col-sm-3"></div>
                        <div class="col-sm-9">
                            <table >
                                <tr v-for="item in depositList">
                                    <td>
                                        <input type="radio" name="deposit" value="" @click="chanReceiptNo(item)"/>
                                    </td>
                                    <td>
                                        收据号:{{item.receiptNo}}
                                    </td>
                                    <td>
                                        金额:{{(item.maxDeductAmount - item.deductedAmount).toFixed(2)}}
                                    </td>
                                </tr>
                            </table>
                        </div>
                    </div>


                <%--银行/支行--%>
                    <div v-if="payType == 'POS'" class="form-group" :class="{'has-error':$validation.bankObj.invalid && $validation.touched}">
                        <label class="control-label col-sm-3">银行：</label>
                        <div class="col-sm-4">
                            <select class="form-control" v-model="bankObj" @change="changeBank" v-validate:bank-obj="{
                                        required:{rule:true,message:'请选择银行'}}">
                                <option value="">请选择银行</option>
                                <option :value="item.values" v-for="item in attrBankList">{{item.attrName}}</option>
                            </select>
                            <span v-cloak v-if="$validation.bankObj.invalid && $validation.touched"
                                  class="help-absolute">
                                <span v-for="error in $validation.bankObj.errors">
                                    {{error.message}} {{($index !==
                                    ($validation.bankObj.errors.length -1)) ? ',':''}}
                                </span>
                            </span>
                        </div>
                    </div>
                    <div v-if="payType == 'POS'" class="form-group" :class="{'has-error':$validation.branchObj.invalid && $validation.touched}">
                        <label class="control-label col-sm-3">支行：</label>
                        <div class="col-sm-4">
                            <select class="form-control" v-model="branchObj" @change="changeBranch" v-validate:branch-obj="{
                                        required:{rule:true,message:'请选择支行'}}">
                                <option value="">请选择支行</option>
                                <option :value="item.values" v-for="item in attrBranchList">{{item.attrName}}</option>
                            </select>
                            <span v-cloak v-if="$validation.branchObj.invalid && $validation.touched"
                                  class="help-absolute">
                                <span v-for="error in $validation.branchObj.errors">
                                    {{error.message}} {{($index !==
                                    ($validation.branchObj.errors.length -1)) ? ',':''}}
                                </span>
                            </span>
                        </div>
                    </div>

                    <div class="form-group" :class="{'has-error':$validation.payManualFlag.invalid && $validation.touched}">
                        <label class="control-label col-sm-3">款项类别：</label>
                        <div class="col-sm-4">
                            <select class="form-control" v-validate:pay-manual-flag="{
                                        required:{rule:true,message:'请选择款项类别'}
                                   }" v-model="paymoneyRecord.payManualFlag">
                                <option value="">请选择款项类别</option>
                                <option :value="item" v-for="item in paymoneyRecordDto.curStagePaymentFlagList">{{item}}</option>
                            </select>
                            <span v-cloak v-if="$validation.payManualFlag.invalid && $validation.touched"
                                  class="help-absolute">
                                <span v-for="error in $validation.payManualFlag.errors">
                                    {{error.message}} {{($index !==
                                    ($validation.payManualFlag.errors.length -1)) ? ',':''}}
                                </span>
                            </span>
                        </div>
                    </div>

                    <div class="form-group">
                        <label  class="control-label col-sm-3">应收金额：</label>
                        <div class="col-sm-3">
                            <input v-model="paymoneyRecordDto.stageExpectedPay" readonly="readonly" type="text" placeholder="应收金额" class="form-control"/>
                        </div>
                        <span @click="toggle()">交款明细</span>
                    </div>

                    <%--交款明细--%>
                    <div v-show="isShow" style="padding-left: 220px;">
                        <form>
                            <div class="form-group">
                                <label>本期应收款：</label>
                                {{paymoneyRecordDto.payStage.expectReceived}}
                            </div>
                            <div class="form-group">
                                <label>本期已收款：</label>
                                {{paymoneyRecordDto.payStage.actualTotalReceived}}
                            </div>
                            <div class="form-group">
                                <label>往期欠款：</label>
                                {{paymoneyRecordDto.payStage.agoUnpayAmount}}
                            </div>
                            <div class="form-group">
                                <label>变更总金额：</label>
                                {{paymoneyRecordDto.stageTotalChange}}
                            </div>
                            <div class="form-group">
                                <label>赔款总金额：</label>
                                {{paymoneyRecordDto.stageTotalReparation}}
                            </div>
                        </form>
                    </div>


                    <div v-if = "payType =='DEDUCT'" class="form-group" :class="{'has-error':$validation.actualReceived.invalid && $validation.touched}">
                        <label  class="control-label col-sm-3">实收金额：</label>
                        <div class="col-sm-4">
                            <input @keyup="caluCharge" v-model="paymoneyRecord.actualReceived" v-validate:actual-received="{
                                   checkNum:{rule:paymoneyRecord.actualReceived,message:'请输入正确金额'},
                                   actualReceivedMax:{rule:(paymoneyRecord.actualReceived,ableDeductAmount),message:'输入金额不能大于定金'}
                                   }" type="text"  class="form-control"/>
                            <span v-cloak
                                  v-if="$validation.actualReceived.invalid && $validation.touched"
                                  class="help-absolute">
                                <span v-for="error in $validation.actualReceived.errors">
                                    {{error.message}} {{($index !== ($validation.actualReceived.errors.length -1)) ? ',':''}}
                                </span>
                            </span>
                        </div>
                    </div>
                    <div v-else class="form-group" :class="{'has-error':$validation.actualReceived.invalid && $validation.touched}">
                        <label  class="control-label col-sm-3">实收金额：</label>
                        <div class="col-sm-4">
                            <input @keyup="caluCharge" v-model="paymoneyRecord.actualReceived" v-validate:actual-received="['num']" type="text"  class="form-control"/>
                            <span v-cloak
                                  v-if="$validation.actualReceived.invalid && $validation.touched"
                                  class="help-absolute">
                                <span v-for="error in $validation.actualReceived.errors">
                                    {{error.message}} {{($index !== ($validation.actualReceived.errors.length -1)) ? ',':''}}
                                </span>
                            </span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label  class="control-label col-sm-3">手续费：</label>
                        <div class="col-sm-4">
                            <input v-model="paymoneyRecord.costfeeAmount" readonly="readonly" type="text" placeholder="手续费" class="form-control"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label  class="control-label col-sm-3">收据号：</label>
                        <div class="col-sm-4">
                            <input v-model="paymoneyRecordDto.receiptNum" type="text" placeholder="收据号" class="form-control"/>
                            <span v-cloak
                                  v-if="$validation.receiptNum.invalid && $validation.touched"
                                  class="help-absolute">
                                <span v-for="error in $validation.receiptNum.errors">
                                    {{error.message}} {{($index !== ($validation.receiptNum.errors.length -1)) ? ',':''}}
                                </span>
                            </span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label  class="control-label col-sm-3">摘要：</label>
                        <div class="col-sm-4">
                            <textarea v-model="paymoneyRecord.remark" placeholder="摘要" class="form-control"></textarea>
                            <span v-cloak
                                  v-if="$validation.remark.invalid && $validation.touched"
                                  class="help-absolute">
                                <span v-for="error in $validation.remark.errors">
                                    {{error.message}} {{($index !== ($validation.remark.errors.length -1)) ? ',':''}}
                                </span>
                            </span>
                        </div>
                    </div>
                </template>
                <div class="modal-footer" style="text-align: center;padding-top:30px;padding-bottom:30px;" >
                    <button type="button" data-dismiss="modal" class="btn">关闭</button>
                    <button type="button" @click="insert" class="btn btn-primary">保存</button>
                </div>
            </form>
        </validator>
    </div>

</div>

<%--赔款弹框页面--%>
<div id="reparationModal" class="modal modal-dialog fade" tabindex="-1">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
        <h3 align="center">赔款信息</h3>
    </div>
    <div class="modal-body">
        <validator name="validation">
            <form name="banner" novalidate class="form-horizontal" role="form">
                <input type="hidden" v-model="id">
                <div class="form-group">
                    <label class="control-label col-sm-3">付款类型：</label>
                    <div class="col-sm-4">
                        <input v-model="itemName" name="itemName" type="radio" value="1" checked="checked"/>赔款
                    </div>
                </div>
                <div class="form-group" >
                    <label class="control-label col-sm-3">客户姓名：</label>
                    <div class="col-sm-4">
                        <input v-model="projectStage.name" readonly="readonly" name="name" type="text" placeholder="客户姓名" maxlength="20" class="form-control"/>
                    </div>
                </div>


                <div class="form-group">
                    <label class="control-label col-sm-3">客户手机号：</label>
                    <div class="col-sm-4">
                        <input v-model="projectStage.mobile" readonly="readonly" name="mobile" type="text" placeholder="客户手机号" class="form-control"/>
                    </div>
                </div>

                <div class="form-group" :class="{'has-error':$validation.reparationNo.invalid && $validation.touched}">
                    <label class="control-label col-sm-3">赔款编号：</label>
                    <div class="col-sm-4">
                        <input v-model="projectStage.reparationNo"
                               name="reparationNo" type="text" placeholder="赔款编号" maxlength="15" class="form-control" readonly/>
                        <span v-cloak v-if="$validation.reparationNo.invalid && $validation.touched" class="help-absolute">
                              <span v-for="error in $validation.reparationNo.errors">
                                {{error.message}} {{($index !== ($validation.reparationNo.errors.length -1)) ? ',':''}}
                              </span>
                        </span>
                    </div>
                </div>

                <div class="form-group" :class="{'has-error':$validation.reparationAmount.invalid && $validation.touched}">
                    <label  class="control-label col-sm-3">赔款金额：</label>
                    <div class="col-sm-4">
                        <input v-model="reparationMoney.reparationAmount" v-validate:reparation-amount="['num']" type="text" placeholder="赔款金额" class="form-control"/>
                        <span v-cloak
                              v-if="$validation.reparationAmount.invalid && $validation.touched"
                              class="help-absolute">
                            <span v-for="error in $validation.reparationAmount.errors">
                                {{error.message}} {{($index !== ($validation.reparationAmount.errors.length -1)) ? ',':''}}
                            </span>
                        </span>
                    </div>
                </div>

                <div class="form-group" :class="{'has-error':$validation.createTime.invalid && $validation.touched}">
                    <label class="col-sm-3 control-label">赔款录入日期：</label>
                    <div class="col-sm-4" :class="{'has-error':$validation.createTime.invalid && $validation.touched}">
                        <input v-model="reparationMoney.createTime"
                               v-validate:create-time="{required:{rule:true,message:'请输入赔款录入日期'}}"
                               v-el:create-time
                               maxlength="50"
                               tabindex="4"
                               name="createTime" type="text" class="form-control datepicker" readonly>
                        <span v-cloak v-if="$validation.createTime.invalid && $validation.touched" class="help-absolute">
                          <span v-for="error in $validation.createTime.errors">
                            {{error.message}} {{($index !== ($validation.createTime.errors.length -1)) ? ',':''}}
                          </span>
                        </span>
                    </div>
                </div>


                <div class="form-group">
                    <label  class="control-label col-sm-3">赔款原因：</label>
                    <div class="col-sm-4">
                        <textarea v-model="reparationMoney.reparationReason" placeholder="赔款原因" class="form-control"></textarea>
                        <span v-cloak
                              v-if="$validation.reparationReason.invalid && $validation.touched"
                              class="help-absolute">
                            <span v-for="error in $validation.reparationReason.errors">
                                {{error.message}} {{($index !== ($validation.reparationReason.errors.length -1)) ? ',':''}}
                            </span>
                        </span>
                    </div>
                </div>
            </form>
        </validator>
    </div>
    <div class="modal-footer" style="text-align: center;padding-top:30px;padding-bottom:30px;" >
        <button type="button" data-dismiss="modal" class="btn">关闭</button>
        <button type="button" @click="insert" class="btn btn-primary">提交</button>
    </div>
</div>

<%-- 退款 弹框页面--%>
<div id="refundmentModal" class="modal modal-dialog fade" tabindex="-1">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
        <h3 align="center">退款信息</h3>
    </div>
    <div class="modal-body">
        <validator name="validation">
            <form name="banner" novalidate class="form-horizontal" role="form">
                <div class="form-group">
                    <label class="control-label col-sm-3">退款单号：</label>
                    <div class="col-sm-4">
                        <input v-model="refundRecord.refundNo" type="text" placeholder="退款单号" class="form-control"/>
                    </div>
                </div>

                <div class="form-group">
                    <label class="control-label col-sm-3">收款人姓名：</label>
                    <div class="col-sm-4">
                        <input v-model="refundRecord.refundReceiverName" type="text" placeholder="收款人姓名" maxlength="20" class="form-control"/>
                    </div>
                </div>
                <div class="form-group" >
                    <label class="control-label col-sm-3">收款人电话：</label>
                    <div class="col-sm-4">
                        <input v-model="refundRecord.refundReceiverMobile" type="text" placeholder="收款人电话" maxlength="20" class="form-control"/>
                    </div>
                </div>

                <%--<div class="form-group">
                    <label class="control-label col-sm-3">银行卡号：</label>

                    <div class="col-sm-6" >
                        <input v-model="refundRecord.refundReceiverAccount"
                               maxlength="19" name="refundReceiverAccount" type="text" class="form-control"/>
                    </div>
                </div>--%>

                <div class="form-group" :class="{'has-error':$validation.refundTime.invalid && $validation.touched}">
                    <label class="control-label col-sm-3">退款日期：</label>
                    <div class="col-sm-4" :class="{'has-error':$validation.refundTime.invalid && $validation.touched}">
                        <input v-model="refundRecord.refundTime"
                               v-validate:refund-time="{required:{rule:true,message:'请输入退款日期'}}"
                               v-el:refund-time
                               maxlength="50"
                               tabindex="4"
                               name="refundTime" type="text" class="form-control datepicker" readonly>
                        <span v-cloak v-if="$validation.refundTime.invalid && $validation.touched" class="help-absolute">
                          <span v-for="error in $validation.refundTime.errors">
                            {{error.message}} {{($index !== ($validation.refundTime.errors.length -1)) ? ',':''}}
                          </span>
                        </span>
                    </div>
                </div>

                <div class="form-group" v-if="refundRecord.depositAbleBackAmount != '0'">
                    <label class="control-label col-sm-3">定金：</label>
                    <div class="col-sm-3"  class="form-control">
                        <input v-model="refundRecord.depositAbleBackAmount" readonly="readonly" class="form-control" type="text"/>
                    </div>
                    <div class="col-sm-2"  class="form-control">
                        <input v-model="checkedOne" type="checkbox" @click="sum('checkedOne')"/>退
                    </div>
                </div>

                <div class="form-group" v-if="checkedOne" v-for="(index, item) in depositList">
                    <div class="col-sm-3"></div>
                    <div class="col-sm-3">
                        <input type="checkbox" name="checks" v-model="item.itemCheck" value="{{index}}" />可退定金：{{item.maxDeductAmount - item.deductedAmount}}
                    </div>
                    <div v-if="item.itemCheck" class="col-sm-3" :class="{'has-error':$validation[item.brandField].invalid && $validation.touched}">
                        <input v-model="item.refundDepositAmount"
                               :id="item.brandField"
                               :field="item.brandField"
                               v-validate="item.brandValidate"
                               type="text" class="form-control" @keyup="changeMoney"/>
                    </div>
                    <div class="col-sm-3" style="padding-top: 10px;" :class="{'has-error':$validation[item.brandField].invalid && $validation.touched}">
                        <span v-cloak v-if="$validation[item.brandField].invalid && $validation.touched" class="help-absolute" />
                        <span v-for="error in $validation[item.brandField].errors">
                                    {{error.message}} {{($index !== ($validation[item.brandField].errors.length -1)) ? ',':''}}
                            </span>
                        </span>
                    </div>
                </div>


                <div class="form-group" v-if="refundRecord.modifyAbleBackAmount != '0'">
                    <label class="control-label col-sm-3">拆改费：</label>
                    <div class="col-sm-3"  class="form-control">
                        <input v-model="refundRecord.modifyAbleBackAmount" readonly="readonly" class="form-control" type="text"/>
                    </div>
                    <div class="col-sm-2">
                        <input v-model="checkedTwo" type="checkbox" @click="sum('checkedTwo')"/>退
                    </div>
                    <div class="col-sm-3" v-if="checkedTwo" :class="{'has-error':$validation.refundModifyAmount.invalid && $validation.touched}">
                        <input v-model="refundRecord.refundModifyAmount" @click="sum"
                               v-validate:refund-modify-amount="{
                               checkNum:{rule:refundRecord.refundConstructAmount,message:'请输入正确金额'},
                               refundModifyAmountMax:{rule:refundRecord.refundModifyAmount,message:'输入金额不能大于拆改费'}
                               }" type="text" class="form-control" @keyup="changeMoney"/>
                        <span v-cloak v-if="$validation.refundModifyAmount.invalid && $validation.touched" class="help-absolute" />
                            <span v-for="error in $validation.refundModifyAmount.errors">
                                {{error.message}} {{($index !== ($validation.refundModifyAmount.errors.length -1)) ? ',':''}}
                            </span>
                        </span>
                    </div>
                </div>

                <div class="form-group" v-if="refundRecord.constructAbleBackAmount != '0'">
                    <label class="control-label col-sm-3">施工款：</label>
                    <div class="col-sm-3"  class="form-control">
                        <input v-model="refundRecord.constructAbleBackAmount" readonly="readonly" class="form-control" type="text"/>
                    </div>
                    <div class="col-sm-2"  class="form-control">
                        <input v-model="checkedThree" type="checkbox" @click="sum('checkedThree')"/>退
                    </div>
                    <div class="col-sm-3" v-if="checkedThree" :class="{'has-error':$validation.refundConstructAmount.invalid && $validation.touched}">
                        <input v-model="refundRecord.refundConstructAmount" @change="sum"
                               v-validate:refund-construct-amount="{
                               checkNum:{rule:refundRecord.refundConstructAmount,message:'请输入正确金额'},
                               refundConstructAmountMax:{rule:refundRecord.refundConstructAmount,message:'输入金额不能大于施工款'}
                               }" type="text" class="form-control" @keyup="changeMoney"/>
                        <span v-cloak v-if="$validation.refundConstructAmount.invalid && $validation.touched" class="help-absolute" />
                            <span v-for="error in $validation.refundConstructAmount.errors">
                                {{error.message}} {{($index !== ($validation.refundConstructAmount.errors.length -1)) ? ',':''}}
                            </span>
                        </span>
                    </div>
                </div>

                <div class="form-group">
                    <label  class="control-label col-sm-3">最大可退金额：</label>
                    <div class="col-sm-4">
                        <input v-model="refundRecord.refundExpectAmount" readonly type="text" placeholder="最大可退金额" class="form-control"/>
                    </div>
                    <div class="col-sm-3">
                        <span style="color:red;font-weight: bold" >(未抵用定金)</span>
                    </div>
                </div>


                <div class="form-group">
                    <label  class="control-label col-sm-3">实退金额：</label>
                    <div class="col-sm-4">
                        <input v-model="refundRecord.refundActualAmount" readonly type="text" placeholder="实退金额" class="form-control"/>
                    </div>
                </div>


                <div class="form-group">
                    <label  class="control-label col-sm-3">退款原因：</label>
                    <div class="col-sm-8">
                        <input v-model="refundRecord.refundReson" value="已交大定，退小定" type="radio"/>已交大定，退小定<br/>
                        <input v-model="refundRecord.refundReson" value="用户工程款交多了，退部分钱" type="radio"/>用户工程款交多了，退部分钱<br/>
                        <input v-model="refundRecord.refundReson" value="赔偿款已录入，但需要提现" type="radio"/>赔偿款已录入，但需要提现<br/>
                        <input v-model="refundRecord.refundReson" value="客户交大定后，部分退款改为小定" type="radio"/>客户交大定后，部分退款改为小定<br/>
                        <input v-model="refundRecord.refundReson" value="同一订单更换付款账户重新支付，款项退回原账户" type="radio"/>同一订单更换付款账户重新支付，款项退回原账户
                        <span v-cloak
                              v-if="$validation.reparationReason.invalid && $validation.touched"
                              class="help-absolute">
                            <span v-for="error in $validation.reparationReason.errors">
                                {{error.message}} {{($index !== ($validation.reparationReason.errors.length -1)) ? ',':''}}
                            </span>
                        </span>
                    </div>
                </div>
            </form>
        </validator>
    </div>
    <div class="modal-footer" style="text-align: center;padding-top:30px;padding-bottom:30px;" >
        <button type="button" data-dismiss="modal" class="btn">关闭</button>
        <button type="button" @click="insert" class="btn btn-primary">提交</button>
    </div>
</div>

<%--退单弹框页面--%>
<div id="chargebackModal" class="modal modal-dialog fade" tabindex="-1">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
        <h3 align="center">退单信息</h3>
    </div>
    <div class="modal-body">
        <validator name="validation">
            <form name="banner" novalidate class="form-horizontal" role="form">
                <div class="form-group">
                    <label class="control-label col-sm-3">收款人姓名：</label>
                    <div class="col-sm-4">
                        <input v-model="refundRecord.refundReceiverName" type="text" placeholder="收款人姓名" maxlength="20" class="form-control"/>
                    </div>
                </div>
                <div class="form-group" >
                    <label class="control-label col-sm-3">收款人电话：</label>
                    <div class="col-sm-4">
                        <input v-model="refundRecord.refundReceiverMobile" type="text" placeholder="收款人电话" maxlength="20" class="form-control"/>
                    </div>
                </div>

                <div class="form-group">
                    <label class="control-label col-sm-3">银行卡号：</label>

                    <div class="col-sm-6">
                        <input v-model="refundRecord.refundReceiverAccount"
                               maxlength="19" name="refundReceiverAccount" type="text" class="form-control"/>
                    </div>
                </div>

                <div class="form-group" :class="{'has-error':$validation.refundTime.invalid && $validation.touched}">
                    <label class="control-label col-sm-3">退款日期：</label>
                    <div class="col-sm-4" :class="{'has-error':$validation.refundTime.invalid && $validation.touched}">
                        <input v-model="refundRecord.refundTime"
                               v-validate:refund-time="{required:{rule:true,message:'请输入退款日期'}}"
                               v-el:refund-time
                               maxlength="50"
                               tabindex="4"
                               name="refundTime" type="text" class="form-control datepicker" readonly>
                        <span v-cloak v-if="$validation.refundTime.invalid && $validation.touched" class="help-absolute">
                          <span v-for="error in $validation.refundTime.errors">
                            {{error.message}} {{($index !== ($validation.refundTime.errors.length -1)) ? ',':''}}
                          </span>
                        </span>
                    </div>
                </div>

                <div class="form-group" v-if="refundRecord.depositAbleBackAmount != '0'">
                    <label class="control-label col-sm-3">定金：</label>
                    <div class="col-sm-3"  class="form-control">
                        <input v-model="refundRecord.depositAbleBackAmount" readonly="readonly" class="form-control" type="text"/>
                    </div>
                    <div class="col-sm-2"  class="form-control">
                        <input v-model="checkedOne" type="checkbox" @click="sum('checkedOne')"/>退
                    </div>
                </div>

                <div class="form-group" v-if="checkedOne" v-for="(index, item) in depositList">
                    <div class="col-sm-3"></div>
                    <div class="col-sm-3">
                        <input type="checkbox" name="checks" v-model="item.itemCheck" value="{{index}}" />可退定金：{{item.maxDeductAmount - item.deductedAmount}}
                    </div>
                    <div v-if="item.itemCheck" class="col-sm-6"></div>
                </div>


                <div class="form-group" v-if="refundRecord.modifyAbleBackAmount != '0'">
                    <label class="control-label col-sm-3">拆改费：</label>
                    <div class="col-sm-3"  class="form-control">
                        <input v-model="refundRecord.modifyAbleBackAmount" readonly="readonly" class="form-control" type="text"/>
                    </div>
                    <div class="col-sm-2">
                        <input v-model="checkedTwo" type="checkbox" @click="sum('checkedTwo')"/>退
                    </div>
                </div>

                <div class="form-group" v-if="refundRecord.constructAbleBackAmount != '0'">
                    <label class="control-label col-sm-3">施工款：</label>
                    <div class="col-sm-3"  class="form-control">
                        <input v-model="refundRecord.constructAbleBackAmount" readonly="readonly" class="form-control" type="text"/>
                    </div>
                    <div class="col-sm-2"  class="form-control">
                        <input v-model="checkedThree" type="checkbox" @click="sum('checkedThree')"/>退
                    </div>
                </div>

                <div class="form-group">
                    <label  class="control-label col-sm-3">最大可退金额：</label>
                    <div class="col-sm-4">
                        <input v-model="refundRecord.refundExpectAmount" readonly type="text" placeholder="最大可退金额" class="form-control"/>
                    </div>
                </div>


                <div class="form-group" :class="{'has-error':$validation.deductDesignAmount.invalid && $validation.touched}">
                    <label  class="control-label col-sm-3">设计费扣款：</label>
                    <div class="col-sm-4">
                        <input v-model="refundRecord.deductDesignAmount" v-validate:deduct-design-amount="{
                               checkNum:{rule:refundRecord.deductDesignAmount,message:'请输入正确金额'}}" type="text" class="form-control"/>
                        <span v-cloak
                              v-if="$validation.deductDesignAmount.invalid && $validation.touched"
                              class="help-absolute">
                            <span v-for="error in $validation.deductDesignAmount.errors">
                                {{error.message}} {{($index !== ($validation.deductDesignAmount.errors.length -1)) ? ',':''}}
                            </span>
                        </span>
                    </div>
                </div>

                <div class="form-group" :class="{'has-error':$validation.deductOtherAmount.invalid && $validation.touched}">
                    <label  class="control-label col-sm-3">其他扣款：</label>
                    <div class="col-sm-4">
                        <input v-model="refundRecord.deductOtherAmount" v-validate:deduct-other-amount="{
                               checkNum:{rule:refundRecord.deductOtherAmount,message:'请输入正确金额'}}" type="text" class="form-control"/>
                        <span v-cloak
                              v-if="$validation.deductOtherAmount.invalid && $validation.touched"
                              class="help-absolute">
                            <span v-for="error in $validation.deductOtherAmount.errors">
                                {{error.message}} {{($index !== ($validation.deductOtherAmount.errors.length -1)) ? ',':''}}
                            </span>
                        </span>
                    </div>
                </div>

                <div class="form-group" :class="{'has-error':$validation.refundActualAmount.invalid && $validation.touched}">
                    <label  class="control-label col-sm-3">实退金额：</label>
                    <div class="col-sm-4">
                        <input :value="((depositTotalAmount || 0)+(refundRecord.refundModifyAmount || 0)+
                        (refundRecord.refundConstructAmount || 0)-(refundRecord.deductDesignAmount || 0)-(refundRecord.deductOtherAmount || 0)).toFixed(2)"
                               v-model="refundRecord.refundActualAmount" v-validate:refund-actual-amount="{
                               refundActualAmountCheck:{rule:refundRecord.refundActualAmount,message:'请检查输入的其他扣款和设计扣款是否有误!'}
                               }" readonly type="text" placeholder="实退金额" class="form-control"/>
                        <span v-cloak
                              v-if="$validation.refundActualAmount.invalid && $validation.touched"
                              class="help-absolute">
                            <span v-for="error in $validation.refundActualAmount.errors">
                                {{error.message}} {{($index !== ($validation.refundActualAmount.errors.length -1)) ? ',':''}}
                            </span>
                        </span>
                    </div>
                </div>

                <div class="form-group" :class="{'has-error':$validation.refundMemo.invalid && $validation.touched}">
                    <label  class="control-label col-sm-3">退款所处阶段：</label>
                    <div class="col-sm-4">
                        <select class="form-control" v-validate:refund-memo="{
                                        required:{rule:true,message:'请选择所处阶段'}
                                   }" v-model="refundRecord.refundMemo">
                            <option value="">请选择所处阶段</option>
                            <option value="已交定金未量房">已交定金未量房</option>
                            <option value="已交定金已量房">已交定金已量房</option>
                            <option value="已首期款未施工">已首期款未施工</option>
                            <option value="已施工未完工">已施工未完工</option>
                        </select>
                        <span v-cloak v-if="$validation.refundMemo.invalid && $validation.touched"
                              class="help-absolute">
                                <span v-for="error in $validation.refundMemo.errors">
                                    {{error.message}} {{($index !==
                                    ($validation.refundMemo.errors.length -1)) ? ',':''}}
                                </span>
                        </span>
                    </div>
                </div>
            </form>
        </validator>
    </div>
    <div class="modal-footer" style="text-align: center;padding-top:30px;padding-bottom:30px;" >
        <button type="button" data-dismiss="modal" class="btn">关闭</button>
        <button type="button" @click="insert" class="btn btn-primary">提交</button>
    </div>
</div>

<%@include file="paymentInfo.jsp" %>
    <%@include file="paymentPlan.jsp" %>
    <%@include file="changeRecord.jsp" %>
    <%@include file="claimRecord.jsp" %>
    <%@include file="refundRecord.jsp" %>
    <%@include file="backRecord.jsp" %>
    <%@include file="operationRecord.jsp" %>

    <script src="${ctx}/static/core/js/components/tab.js?v=1.0"></script>

    <script src="${ctx}/static/business/finance/paymentInfo.js"></script>
    <script src="${ctx}/static/business/finance/paymentPlan.js"></script>
    <script src="${ctx}/static/business/finance/changeRecord.js"></script>
    <script src="${ctx}/static/business/finance/claimRecord.js"></script>
    <script src="${ctx}/static/business/finance/refundRecord.js"></script>
    <script src="${ctx}/static/business/finance/backRecord.js"></script>
    <script src="${ctx}/static/business/finance/operationRecord.js"></script>

    <script src="${ctx}/static/business/finance/refundDetail.js"></script>
</body>

