<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<link rel="stylesheet" href="/static/core/vendor/webuploader/webuploader.css">
<title>订单列表</title>
<div id="container" class="wrapper wrapper-content">
    <!-- 面包屑 -->
    <div id="breadcrumb">
        <bread-crumb :crumbs="breadcrumbs"></bread-crumb>
    </div>
    <!-- ibox start -->
    <div class="ibox">
        <div class="ibox-content">
            <div class="row">
                <form id="searchForm">
                    <div class="row">
                        <div class="col-md-3">
                            <div class="form-group">
                                <label class="sr-only" for="keyword"></label>
                                <input
                                        v-model="form.keyword"
                                        id="keyword"
                                        name="keyword"
                                        type="text"
                                        placeholder="订单|客户|设计师|客服姓名|手机" class="form-control"/>
                            </div>
                        </div>
                        <div class="col-md-2 form-group">
                            <label class="sr-only" for="operateType"></label>
                            <select v-model="form.operateType"
                                    id="operateType"
                                    name="operateType"
                                    class="form-control">
                                <option value="">--选择查询时间类别--</option>
                                <option value="planHouseTime">计划量房时间</option>
                                <option value="bookHouseTime">预约量房时间</option>
                                <option value="createTime">创建订单时间</option>
                            </select>
                        </div>
                        <div class="col-md-2 form-group">
                            <input v-model="form.startTime" v-el:start-time id="startTime"
                                   name="startTime" type="text" readonly
                                   class="form-control datepicker" placeholder="请选择开始时间">
                        </div>
                        <div class="col-md-2 form-group">
                            <input v-model="form.endTime" v-el:end-time id="endTime"
                                   name="endTime" type="text" readonly
                                   class="form-control datepicker" placeholder="请选择结束时间">
                        </div>
                        <div class="col-md-2">
                            <div class="form-group">
                                <label class="sr-only"></label>
                                <select v-model="form.orderFlowStatus"
                                        id=""
                                        name=""
                                        class="form-control">
                                    <option value="">选择订单状态</option>
                                    <option value="STAY_DESIGN" v-if="roleNameList && (roleNameList.indexOf(roleNameAllList[0]) != -1
                                    || roleNameList.indexOf(roleNameAllList[1]) != -1
                                    || roleNameList.indexOf(roleNameAllList[2]) != -1
                                    || roleNameList.indexOf(roleNameAllList[3]) != -1
                                    || roleNameList.indexOf(roleNameAllList[4]) != -1
                                    || roleNameList.indexOf(roleNameAllList[5]) != -1)">待设计
                                    </option>
                                    <option value="STAY_SIGN" v-if="roleNameList && (roleNameList.indexOf(roleNameAllList[0]) != -1
                                    || roleNameList.indexOf(roleNameAllList[1]) != -1
                                    || roleNameList.indexOf(roleNameAllList[2]) != -1
                                    || roleNameList.indexOf(roleNameAllList[3]) != -1
                                    || roleNameList.indexOf(roleNameAllList[4]) != -1
                                    || roleNameList.indexOf(roleNameAllList[5]) != -1)">待签约
                                    </option>
                                    <option value="STAY_SEND_SINGLE_AGAIN" v-if="roleNameList && (roleNameList.indexOf(roleNameAllList[0]) != -1
                                    || roleNameList.indexOf(roleNameAllList[1]) != -1
                                    || roleNameList.indexOf(roleNameAllList[2]) != -1
                                    || roleNameList.indexOf(roleNameAllList[3]) != -1
                                    || roleNameList.indexOf(roleNameAllList[4]) != -1
                                    || roleNameList.indexOf(roleNameAllList[5]) != -1)">待重新派单
                                    </option>
                                    <option value="STAY_CONSTRUCTION" v-if="roleNameList && (roleNameList.indexOf(roleNameAllList[0]) != -1
                                    || roleNameList.indexOf(roleNameAllList[1]) != -1
                                    || roleNameList.indexOf(roleNameAllList[2]) != -1
                                    || roleNameList.indexOf(roleNameAllList[3]) != -1
                                    || roleNameList.indexOf(roleNameAllList[4]) != -1
                                    || roleNameList.indexOf(roleNameAllList[5]) != -1)">待施工
                                    </option>
                                    <option value="ON_CONSTRUCTION" v-if="roleNameList && (roleNameList.indexOf(roleNameAllList[0]) != -1
                                    || roleNameList.indexOf(roleNameAllList[1]) != -1
                                    || roleNameList.indexOf(roleNameAllList[3]) != -1
                                    || roleNameList.indexOf(roleNameAllList[4]) != -1
                                    || roleNameList.indexOf(roleNameAllList[5]) != -1)">施工中
                                    </option>
                                    <option value="PROJECT_COMPLETE" v-if="roleNameList && (roleNameList.indexOf(roleNameAllList[0]) != -1
                                    || roleNameList.indexOf(roleNameAllList[1]) != -1
                                    || roleNameList.indexOf(roleNameAllList[3]) != -1
                                    || roleNameList.indexOf(roleNameAllList[4]) != -1
                                    || roleNameList.indexOf(roleNameAllList[5]) != -1)">已竣工
                                    </option>
                                    <option value="STAY_TURN_DETERMINE" v-if="roleNameList && (roleNameList.indexOf(roleNameAllList[1]) != -1
                                    || roleNameList.indexOf(roleNameAllList[3]) != -1
                                    || roleNameList.indexOf(roleNameAllList[5]) != -1)">待转大定
                                    </option>
                                    <option value="SUPERVISOR_STAY_ASSIGNED" v-if="roleNameList && (roleNameList.indexOf(roleNameAllList[1]) != -1
                                    || roleNameList.indexOf(roleNameAllList[3]) != -1
                                    || roleNameList.indexOf(roleNameAllList[5]) != -1)">督导组长待分配
                                    </option>
                                    <option value="DESIGN_STAY_ASSIGNED" v-if="roleNameList && (roleNameList.indexOf(roleNameAllList[1]) != -1
                                    || roleNameList.indexOf(roleNameAllList[2]) != -1
                                    || roleNameList.indexOf(roleNameAllList[3]) != -1
                                    || roleNameList.indexOf(roleNameAllList[5]) != -1)">设计待分配
                                    </option>

                                    <option value="APPLY_REFUND" v-if="roleNameList && (roleNameList.indexOf(roleNameAllList[1]) != -1
                                    || roleNameList.indexOf(roleNameAllList[3]) != -1
                                    || roleNameList.indexOf(roleNameAllList[5]) != -1)">申请分配退回
                                    </option>

                                    <option value="ORDER_CLOSE" v-if="roleNameList && (roleNameList.indexOf(roleNameAllList[0]) != -1
                                    || roleNameList.indexOf(roleNameAllList[1]) != -1
                                    || roleNameList.indexOf(roleNameAllList[3]) != -1
                                    || roleNameList.indexOf(roleNameAllList[4]) != -1
                                    || roleNameList.indexOf(roleNameAllList[5]) != -1)">订单关闭
                                    </option>


                                </select>
                            </div>
                        </div>
                        <div class="col-md-1">
                            <div class="form-group">
                                <button id="searchBtn" type="submit" @click.prevent="query"
                                        class="btn btn-block btn-outline btn-default" alt="搜索"
                                        title="搜索">
                                    <i class="fa fa-search"></i>
                                </button>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
        </div>
        <table v-el:data-table id="dataTable" width="100%"
               class="table table-striped table-bordered table-hover">
        </table>
    </div>
</div>

<div id="singlePreviewModal" class="modal fade" tabindex="-1" data-width="1000">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal"
                aria-hidden="true">×
        </button>
    </div>
    <div class="modal-body">
        <h3>项目信息</h3>
        <table id="singlePreviewTable" width="100%"
               class="table table-striped table-bordered table-hover">
        </table>
    </div>
</div>

<div id="detailModal" class="modal fade" tabindex="-1" data-width="1000">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal"
                aria-hidden="true">×
        </button>
    </div>
    <div class="modal-body">
        <h3>项目信息</h3>
        <table class="table table-striped table-bordered table-hover">
            <tr>
                <td><label for="">项目编号：</label></td>
                <td>{{customerContract.contractCode}}</td>
                <td><label for="">客户姓名&nbsp;&nbsp;/&nbsp;&nbsp;电话：</label></td>
                <td>{{customerContract.customerName}}&nbsp;&nbsp;/&nbsp;&nbsp;{{customerContract.customerMobile}}</td>
                <td><label for="">第二联系人&nbsp;&nbsp;/&nbsp;&nbsp;电话：</label></td>
                <td>
                    {{customerContract.secondContact}}&nbsp;&nbsp;/&nbsp;&nbsp;{{customerContract.secondContactMobile}}
                </td>
            </tr>
            <tr class="">
                <td><label for="">户型：</label></td>
                <td>{{customerContract.layout}}</td>
                <td><label for="">房屋面积：</label></td>
                <td>{{customerContract.buildArea}}</td>
                <td><label for="">房屋状况：</label></td>
                <td>{{customerContract.houseCondition | goHoseType}}</td>
            </tr>
            <tr>
                <td><label for="">是否期房：</label></td>
                <td>{{customerContract.forwardDeliveryHousing | goForwardDeliveryHousing}}</td>
                <td><label for="">有无电梯：</label></td>
                <td>{{customerContract.elevator | goElevator}}</td>
                <td><label for="">工程地址：</label></td>
                <td>{{customerContract.addressProvince}}&nbsp;{{customerContract.addressCity}}&nbsp;{{customerContract.addressArea}}&nbsp;{{customerContract.houseAddr}}</td>
            </tr>
            <tr>
                <td><label for="">计划量房时间：</label></td>
                <td>{{customerContract.planHouseTime}}</td>
                <td><label for="">计划装修时间：</label></td>
                <td>{{customerContract.planDecorateTime}}</td>
                <td><label for="">创建时间：</label></td>
                <td>{{customerContract.createTime}}</td>
            </tr>
            <tr>
                <td><label for="">活动：</label></td>
                <td>{{customerContract.activityName}}</td>
                <td><label for="">折扣：</label></td>
                <td>{{customerContract.discountName}}</td>
                <td></td>
                <td></td>
            </tr>
        </table>

        <h3>预订合同信息</h3>
        <table class="table table-striped table-bordered table-hover">
            <tr class="">
                <td><label for="">套餐类型：</label></td>
                <td>{{customerContract.packageType}}</td>
                <td><label for="">拆除修复费：</label></td>
                <td>{{customerContract.dismantleRepairCost}}</td>
                <td><label for="">远程费：</label></td>
                <td>{{customerContract.longRangeCost}}</td>
            </tr>
            <tr>
                <td><label for="">搬运费：</label></td>
                <td>{{customerContract.carryCost}}</td>
                <td><label for="">预算总金额：</label></td>
                <td>{{customerContract.totalBudgetAmount}}</td>
                <td><label for="">预付款：</label></td>
                <td>{{customerContract.advancePayment}}</td>
            </tr>
            <tr>
                <td><label for="">备注：</label></td>
                <td colspan="5">{{customerContract.remark}}</td>
            </tr>

        </table>
        <h3>项目施工信息</h3>
        <table class="table table-striped table-bordered table-hover">
            <tr>
                <td><label for="">项目经理&nbsp;&nbsp;/&nbsp;&nbsp;电话：</label></td>
                <td>{{customerContract.projectManager}}&nbsp;&nbsp;/&nbsp;&nbsp;{{customerContract.pmMobile}}</td>
                <td><label for="">监理&nbsp;&nbsp;/&nbsp;&nbsp;电话：</label></td>
                <td>{{customerContract.supervisor}}&nbsp;&nbsp;/&nbsp;&nbsp;{{customerContract.supervisorMobile}}</td>
                <td><label for="">客服&nbsp;&nbsp;/&nbsp;&nbsp;电话</label></td>
                <td>{{customerContract.serviceName}}&nbsp;&nbsp;/&nbsp;&nbsp;{{customerContract.serviceMobile}}</td>
            </tr>
            <tr>
                <td><label for="">合同工期：</label></td>
                <td>{{customerContract.contractSignTrem}}</td>
                <td><label for="">合同开工&nbsp;&nbsp;/&nbsp;&nbsp;竣工：</label></td>
                <td>{{customerContract.contractStartTime}}&nbsp;&nbsp;/&nbsp;&nbsp;{{customerContract.contractCompleteTime}}</td>
                <td><label for="">实际开工&nbsp;&nbsp;/&nbsp;&nbsp;竣工：</label></td>
                <td>{{customerContract.startConstructionTime}}&nbsp;&nbsp;/&nbsp;&nbsp;{{customerContract.completeConstructionTime}}</td>
            </tr>
        </table>
    </div>
</div>

<div id="sendGroupModal" class="modal fade" tabindex="-1" data-width="700">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal"
                aria-hidden="true">×
        </button>
        <h4 align="center">督导组长分配设计组</h4>
    </div>
    <div class="modal-body">
        <validator name="validation" v-model="validation">
            <div class="col-sm-10" style="margin-left: 50px;margin-top: 50px">
                <div class="form-group">
                    <label class="control-label col-sm-4">选择设计组:</label>
                    <div class="col-sm-6" :class="{'has-error':($validation.orgCode.invalid && $validation.touched)}">
                        <select v-model="orgCode"
                                v-validate:org-code="{required:true}"
                                class="form-control">
                            <option value=''>选择设计组</option>
                            <option v-for="group in designerGroup" :value="group.orgCode">
                                {{ group.orgName }}
                            </option>
                        </select>
                        <div v-if="$validation.orgCode.invalid && $validation.touched"
                             class="help-absolute">
                            <span v-if="$validation.orgCode.invalid">请选择设计组</span>
                        </div>
                    </div>
                </div>
            </div>
        </validator>
    </div>
    <div class="modal-footer">
        <button type="button" data-dismiss="modal" class="btn">关闭</button>
        <button type="button" @click="save" class="btn btn-primary">保存</button>
    </div>
</div>

<div id="sendDesignerModal" class="modal fade" tabindex="-1" data-width="700">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal"
                aria-hidden="true">×
        </button>
        <h4 align="center">设计组长分配设计师</h4>
    </div>
    <div class="modal-body">
        <validator name="validation" v-model="validation">
            <div class="col-sm-10" style="margin-left: 50px;margin-top: 50px">
                <div class="form-group">
                    <label class="control-label col-sm-4">选择设计师:</label>
                    <div class="col-sm-6" :class="{'has-error':($validation.orgCode.invalid && $validation.touched)}">
                        <select v-model="orgCode"
                                v-validate:org-code="{required:true}"
                                class="form-control">
                            <option value=''>选择设计师</option>
                            <option v-for="designer in designers" :value="designer.orgCode">
                                {{ designer.name }}/{{designer.mobile}}
                            </option>
                        </select>
                        <div v-if="$validation.orgCode.invalid && $validation.touched"
                             class="help-absolute">
                            <span v-if="$validation.orgCode.invalid">请选择设计师</span>
                        </div>
                    </div>
                </div>
            </div>
        </validator>
    </div>
    <div class="modal-footer">
        <button type="button" data-dismiss="modal" class="btn">关闭</button>
        <button type="button" @click="save" class="btn btn-primary">保存</button>
    </div>
</div>

<div id="appointMeasureHouseModal" class="modal fade" tabindex="-1" data-width="700">
    <form name="banner" novalidate class="form-horizontal" role="form">
        <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal"
                    aria-hidden="true">×
            </button>
            <div>
                <h4 align="center">预约量房</h4>
            </div>
        </div>
        <div class="modal-body">
            <div class="form-group">
                <div class="col-sm-12">
                    <label for="bookHouseCompleteTime" class="control-label col-sm-3">预约量房时间</label>
                    <div class="col-sm-6">
                        <input v-model="bookHouseTime" v-el:book-house-time id="bookHouseTime"
                               name="bookHouseTime" type="text" readonly
                               class="form-control datepicker" placeholder="请选择时间">
                    </div>
                </div>
            </div>
        </div>
        <div class="modal-footer">
            <button type="button" data-dismiss="modal" class="btn">关闭</button>
            <button type="button" @click="saveAppointMeasureHouseTime" class="btn btn-primary">保存</button>
        </div>
    </form>
</div>

<div id="designSendModal" class="modal fade" tabindex="-1" data-width="700">
    <form name="banner" novalidate class="form-horizontal" role="form">
        <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal"
                    aria-hidden="true">×
            </button>
            <div>
                <h4 align="center">量房</h4>
            </div>
        </div>
        <div class="modal-body">
            <div class="form-group">
                <div class="col-sm-12">
                    <label for="planHouseTime" class="control-label col-sm-4">计划量房时间</label>
                    <div class="col-sm-8">
                        <input v-model="planHouseTime" id="planHouseTime" type="text"
                               class="form-control" readonly>
                    </div>
                </div>
            </div>
            <div class="form-group">
                <div class="col-sm-12">
                    <label for="bookHouseCompleteTime" class="control-label col-sm-4">量房完成时间</label>
                    <div class="col-sm-8">
                        <input v-model="bookHouseCompleteTime" v-el:book-house-complete-time id="bookHouseCompleteTime"
                               name="bookHouseCompleteTime" type="text" readonly
                               class="form-control datepicker" placeholder="请选择时间">
                    </div>
                </div>
            </div>
            <div class="form-group">
                <div class="col-sm-12">
                    <label for="bookHouseExecutor" class="control-label col-sm-4">执行人</label>
                    <div class="col-sm-8">
                        <input v-model="bookHouseExecutor" id="bookHouseExecutor" type="text"
                               class="form-control" placeholder="请填写执行人">
                    </div>
                </div>
            </div>
            <div class="form-group">
                <div class="col-sm-12">
                    <label for="engineerArea" class="control-label col-sm-4">工程区域</label>
                    <div class="col-sm-8">
                        <input v-model="engineerArea" id="engineerArea" type="text"
                               class="form-control" readonly>
                    </div>
                </div>
            </div>
            <div class="form-group">
                <div class="col-sm-12">
                    <label for="houseAddr" class="control-label col-sm-4">详细地址</label>
                    <div class="col-sm-8">
                        <input v-model="houseAddr" id="houseAddr" type="text"
                               class="form-control" readonly>
                    </div>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-4 control-label">CAD上传：</label>
                <label class="col-sm-4 control-label"
                       style="text-align: left; padding: 0px 10px" v-clickoutside="">
                    <web-uploader
                            :type="webUploaderAccessories.type" :w-server="webUploaderAccessories.server"
                            :w-accept="webUploaderAccessories.accept"
                            :w-file-size-limit="webUploaderAccessories.fileSizeLimit"
                            :w-file-single-size-limit="webUploaderAccessories.fileSingleSizeLimit"
                            :w-form-data="{category:'CONTRACT'}">
                        <button type="button" class="btn btn-primary btn-outline">CAD上传</button>
                    </web-uploader>
                </label>
            </div>
            <div class="form-group">
                <label class="col-md-4 control-label">上传进度:</label>
                <label class="col-md-4 control-label"
                       style="text-align: left; padding: 0px 10px" v-clickoutside="">
                    <div class="progress">
                        <div class="progress-bar" role="progressbar"
                             aria-valuemin="0" aria-valuemax="100" style="width:{{accessories_percentage}}%;">
                            <span>{{accessories_percentage}}%</span>
                        </div>
                    </div>

                </label>
            </div>
        </div>
        <div class="modal-footer">
            <button type="button" data-dismiss="modal" class="btn">关闭</button>
            <button type="button" @click="saveDesignSend" class="btn btn-primary">保存</button>
        </div>
    </form>
</div>

<div id="designModal" class="modal fade" tabindex="-1" data-width="700">
    <form name="banner" novalidate class="form-horizontal" role="form">
        <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal"
                    aria-hidden="true">×
            </button>
            <div>
                <h4 align="center">出图</h4>
            </div>
        </div>
        <div class="modal-body">
            <div class="form-group">
                <div class="col-sm-12">
                    <label for="outMapCompleteTime" class="control-label col-sm-4">出图完成时间</label>
                    <div class="col-sm-8">
                        <input v-model="outMapCompleteTime" v-el:out-map-complete-time id="outMapCompleteTime"
                               name="outMapCompleteTime" type="text" readonly
                               class="form-control datepicker" placeholder="请选择时间">
                    </div>
                </div>
            </div>
            <div class="form-group">
                <div class="col-sm-12">
                    <label for="outMapExecutor" class="control-label col-sm-4">执行人</label>
                    <div class="col-sm-8">
                        <input v-model="outMapExecutor" id="outMapExecutor" type="text"
                               class="form-control" placeholder="请填写执行人">
                    </div>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-4 control-label">CAD上传：</label>
                <label class="col-sm-4 control-label"
                       style="text-align: left; padding: 0px 10px" v-clickoutside="">
                    <web-uploader
                            :type="webUploaderAccessories.type" :w-server="webUploaderAccessories.server"
                            :w-accept="webUploaderAccessories.accept"
                            :w-file-size-limit="webUploaderAccessories.fileSizeLimit"
                            :w-file-single-size-limit="webUploaderAccessories.fileSingleSizeLimit"
                            :w-form-data="{category:'CONTRACT'}">
                        <button type="button" class="btn btn-primary btn-outline">CAD上传</button>
                    </web-uploader>
                </label>
            </div>
            <div class="form-group">
                <label class="col-md-4 control-label">上传进度:</label>
                <label class="col-md-4 control-label"
                       style="text-align: left; padding: 0px 10px" v-clickoutside="">
                    <div class="progress">
                        <div class="progress-bar" role="progressbar"
                             aria-valuemin="0" aria-valuemax="100" style="width:{{accessories_percentage}}%;">
                            <span>{{accessories_percentage}}%</span>
                        </div>
                    </div>

                </label>
            </div>
        </div>
        <div class="modal-footer">
            <button type="button" data-dismiss="modal" class="btn">关闭</button>
            <button type="button" @click="saveDesign" class="btn btn-primary">保存</button>
        </div>
    </form>
</div>

<div id="signModal" class="modal fade" tabindex="-1" data-width="700">
    <validator name="validation" v-model="validation">
        <form name="banner" novalidate class="form-horizontal" role="form">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-hidden="true">×
                </button>
                <div>

                </div>
            </div>
            <div class="modal-body">
                <div class="form-group">
                    <div class="col-sm-12"
                         :class="{'has-error':($validation.completeTime.invalid && $validation.touched)}">
                        <label for="completeTime" class="control-label col-sm-4">完成时间</label>
                        <div class="col-sm-8">
                            <input v-model="completeTime" v-el:complete-time id="completeTime"
                                   name="completeTime" type="text" readonly
                                   v-validate:complete-time="{required:true}"
                                   class="form-control datepicker" placeholder="请选择时间">
                            <div v-if="$validation.completeTime.invalid && $validation.touched"
                                 class="help-absolute">
                                <span v-if="$validation.completeTime.invalid">请填写完成时间</span>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-sm-12">
                        <label for="signExecutor" class="control-label col-sm-4">签约执行人</label>
                        <div class="col-sm-8">
                            <input v-model="signExecutor" id="signExecutor" type="text"
                                   class="form-control" placeholder="请填写签约执行人">
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-sm-12">
                        <label for="contractCode" class="control-label col-sm-4">项目编号</label>
                        <div class="col-sm-8">
                            <input v-model="contractCode" id="contractCode" type="text"
                                   class="form-control" readonly>
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-sm-12">
                        <label for="contractAmount" class="control-label col-sm-4">合同金额</label>
                        <div class="col-sm-8">
                            <input v-model="contractAmount" id="contractAmount" type="text"
                                   class="form-control" readonly>
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-sm-12">
                        <label for="modifyAmount" class="control-label col-sm-4">拆改费</label>
                        <div class="col-sm-8">
                            <input v-model="modifyAmount" id="modifyAmount" type="text"
                                   class="form-control" readonly>
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-sm-12"
                         :class="{'has-error':($validation.contractSignTrem.invalid && $validation.touched)}">
                        <label for="contractSignTrem" class="control-label col-sm-4">合同签订工期</label>
                        <div class="col-sm-8">
                            <input v-model="contractSignTrem" id="contractSignTrem" type="number"
                                   class="form-control" v-validate:contract-sign-trem="{required:true}">
                            <div v-if="$validation.contractSignTrem.invalid && $validation.touched"
                                 class="help-absolute">
                                <span v-if="$validation.contractSignTrem.invalid">请填写合同签订工期</span>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-sm-12"
                         :class="{'has-error':($validation.contractStartTime.invalid && $validation.touched)}">
                        <label for="contractStartTime" class="control-label col-sm-4">合同签订开工时间</label>
                        <div class="col-sm-8">
                            <input v-model="contractStartTime" v-el:contract-start-time id="contractStartTime"
                                   :disabled="aa"
                                   v-validate:contract-start-time="{required:true}"
                                   name="contractStartTime" type="text" readonly
                                   class="form-control datepicker" placeholder="请选择时间">
                            <div v-if="$validation.contractStartTime.invalid && $validation.touched"
                                 class="help-absolute">
                                <span v-if="$validation.contractStartTime.invalid">请合同签订开工时间</span>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-sm-12">
                        <label for="contractCompleteTime" class="control-label col-sm-4">合同签订竣工时间</label>
                        <div class="col-sm-8">
                            <input v-model="contractCompleteTime" v-el:contract-complete-time id="contractCompleteTime"
                                   name="contractCompleteTime" type="text" readonly
                                   class="form-control datepicker" placeholder="请选择时间" disabled>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" data-dismiss="modal" class="btn">关闭</button>
                <button type="button" @click="saveSign" class="btn btn-primary">保存</button>
            </div>
        </form>
    </validator>
</div>

<div id="signPreviewModal" class="modal fade" tabindex="-1" data-width="700">
    <form name="banner" novalidate class="form-horizontal" role="form">
        <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal"
                    aria-hidden="true">×
            </button>
            <div>

            </div>
        </div>
        <div class="modal-body">
            <div class="form-group">
                <div class="col-sm-12">
                    <label for="complete" class="control-label col-sm-4">签约是否完成</label>
                    <div class="col-sm-4">
                        <div class="col-sm-3">
                            <input type="radio" :value="1"
                                   v-model="complete">
                            <label for="complete">是</label>
                        </div>
                        <div class="col-sm-3">
                            <input type="radio" id="complete" :value="0"
                                   v-model="complete">
                            <label for="complete">否</label>
                        </div>
                    </div>
                </div>
            </div>
            <div class="form-group">
                <div class="col-sm-12">
                    <label for="completeTime" class="control-label col-sm-4">完成时间</label>
                    <div class="col-sm-8">
                        <input v-model="completeTime"
                               name="completeTime" type="text" class="form-control" readonly>
                    </div>
                </div>
            </div>
            <div class="form-group">
                <div class="col-sm-12">
                    <label for="signExecutor" class="control-label col-sm-4">签约执行人</label>
                    <div class="col-sm-8">
                        <input v-model="signExecutor" type="text"
                               class="form-control" readonly>
                    </div>
                </div>
            </div>
            <div class="form-group">
                <div class="col-sm-12">
                    <label for="contractCode" class="control-label col-sm-4">项目编号</label>
                    <div class="col-sm-8">
                        <input v-model="contractCode" type="text"
                               class="form-control" readonly>
                    </div>
                </div>
            </div>
            <div class="form-group">
                <div class="col-sm-12">
                    <label for="contractAmount" class="control-label col-sm-4">合同金额</label>
                    <div class="col-sm-8">
                        <input v-model="contractAmount" type="text"
                               class="form-control" readonly>
                    </div>
                </div>
            </div>
            <div class="form-group">
                <div class="col-sm-12">
                    <label for="modifyAmount" class="control-label col-sm-4">拆改费</label>
                    <div class="col-sm-8">
                        <input v-model="modifyAmount" type="text"
                               class="form-control" readonly>
                    </div>
                </div>
            </div>
            <div class="form-group">
                <div class="col-sm-12">
                    <label for="contractSignTrem" class="control-label col-sm-4">合同签订工期</label>
                    <div class="col-sm-8">
                        <input v-model="contractSignTrem" type="text" class="form-control" readonly>
                    </div>
                </div>
            </div>
            <div class="form-group">
                <div class="col-sm-12">
                    <label for="contractStartTime" class="control-label col-sm-4">合同签订开工时间</label>
                    <div class="col-sm-8">
                        <input v-model="contractStartTime"
                               name="contractStartTime" type="text" class="form-control" readonly>
                    </div>
                </div>
            </div>
            <div class="form-group">
                <div class="col-sm-12">
                    <label for="contractCompleteTime" class="control-label col-sm-4">合同签订竣工时间</label>
                    <div class="col-sm-8">
                        <input v-model="contractCompleteTime"
                               name="contractCompleteTime" type="text" class="form-control" readonly>
                    </div>
                </div>
            </div>
        </div>
        <div class="modal-footer">
            <button type="button" data-dismiss="modal" class="btn">关闭</button>
        </div>
    </form>
</div>

<div id="retreatModal" class="modal fade" tabindex="-1" data-width="500">
    <validator name="validation" v-model="validation">
        <form name="banner" novalidate class="form-horizontal" role="form">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-hidden="true">×
                </button>
            </div>
            <div class="modal-body">
                <div class="form-group">
                    <div class="col-sm-12"
                         :class="{'has-error':($validation.returnReason.invalid && $validation.touched)}">
                        <label class="control-label col-sm-4">分配退回原因</label>
                        <div class="col-sm-8">
                            <select v-model="returnReason" v-validate:return-reason="{required:true}"
                                    class="form-control">
                                <option value="">请选择分配退回原因</option>
                                <option value="用户退单">用户退单</option>
                                <option value="暂时不能量房">暂时不能量房</option>
                                <option value="其它原因">其它原因</option>
                                <div v-if="$validation.returnReason.invalid && $validation.touched"
                                     class="help-absolute">
                                    <span v-if="$validation.returnReason.invalid">请选择分配退回原因</span>
                                </div>
                            </select>
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-sm-12">
                        <label for="signExecutor" class="control-label col-sm-4">退回原因描述</label>
                        <div class="col-sm-8">
                        <textarea v-model="returnReasonDescribe" type="text"
                                  class="form-control">
                        </textarea>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" data-dismiss="modal" class="btn">关闭</button>
                <button type="button" @click="saveRetreat" class="btn btn-primary">保存</button>
            </div>
        </form>
    </validator>
</div>

<div id="recoveryModal" class="modal fade" tabindex="-1" data-width="700">
    <form name="banner" novalidate class="form-horizontal" role="form">
        <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal"
                    aria-hidden="true">×
            </button>
        </div>
        <div class="modal-body">
            <div class="form-group">
                <div class="col-sm-12">
                    <label class="control-label col-sm-4">分配退回原因</label>
                    <div class="col-sm-8">
                        <input v-model="returnReason"
                               class="form-control" readonly/>
                    </div>
                </div>
            </div>
            <div class="form-group">
                <div class="col-sm-12">
                    <label for="signExecutor" class="control-label col-sm-4">退回原因描述</label>
                    <div class="col-sm-8">
                        <textarea v-model="returnReasonDescribe" type="text"
                                  class="form-control">
                        </textarea>
                    </div>
                </div>
            </div>
        </div>
        <div class="modal-footer">
            <button type="button" data-dismiss="modal" class="btn">关闭</button>
            <button type="button" @click="saveRecovery" class="btn btn-primary">保存</button>
        </div>
    </form>
</div>

<!-- container end-->
<script src="${ctx}/static/core/vendor/viewer/viewer.js"></script>
<script src="${ctx}/static/core/vendor/webuploader/webuploader.js"></script>
<script src="${ctx}/static/core/js/components/webuploader.js"></script>
<script src="${ctx}/static/business/orderflow/list.js"></script>
