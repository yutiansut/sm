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

        [v-cloak] {
            display: none;
        }
    </style>
    <link href="/static/core/css/layout.css" rel="stylesheet" type="text/css"/>
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

                    <div class="col-md-3 form-group">
                        <input v-model="form.keyword" type="text"
                               placeholder="项目单号，客户姓名" class="form-control"/>
                    </div>

                    <div class="col-md-3 form-group">
                        <label class="sr-only" for="designerName"></label>
                        <select v-model="form.designerName"
                                id="designerName"
                                name="designerName"
                                class="form-control">
                            <option value="">请选择设计师</option>
                            <option v-for="designer in designers" :value="designer.name">{{designer.name}}</option>
                        </select>
                    </div>

                    <div class="col-md-3 form-group">
                        <label class="sr-only" for="auditorName"></label>
                        <select v-model="form.auditorName"
                                id="auditorName"
                                name="auditorName"
                                class="form-control">
                            <option value="">请选择审计员</option>
                            <option v-for="auditor in auditors" :value="auditor.name">{{auditor.name}}</option>
                        </select>
                    </div>

                    <div class="col-md-3 form-group">
                        <label class="sr-only" for="contractStatus"></label>
                        <select v-model="form.contractStatus"
                                id="contractStatus"
                                name="contractStatus"
                                class="form-control">
                            <option value="">选择选材单状态</option>
                            <option value="CHANGING" v-if="roleNameList && (roleNameList.indexOf(roleNameAllList[0]) != -1
                                    || roleNameList.indexOf(roleNameAllList[1]) != -1
                                    || roleNameList.indexOf(roleNameAllList[2]) != -1
                                    || roleNameList.indexOf(roleNameAllList[3]) != -1
                                    || roleNameList.indexOf(roleNameAllList[4]) != -1
                                    || roleNameList.indexOf(roleNameAllList[5]) != -1
                                    || roleNameList.indexOf(roleNameAllList[6]) != -1
                                    || roleNameList.indexOf(roleNameAllList[7]) != -1)">变更中
                            </option>
                            <option value="CHANGE_AUDIT" v-if="roleNameList && (roleNameList.indexOf(roleNameAllList[0]) != -1
                                    || roleNameList.indexOf(roleNameAllList[1]) != -1
                                    || roleNameList.indexOf(roleNameAllList[2]) != -1
                                    || roleNameList.indexOf(roleNameAllList[3]) != -1
                                    || roleNameList.indexOf(roleNameAllList[4]) != -1
                                    || roleNameList.indexOf(roleNameAllList[5]) != -1
                                    || roleNameList.indexOf(roleNameAllList[6]) != -1
                                    || roleNameList.indexOf(roleNameAllList[7]) != -1)">变更审计中
                            </option>
                            <option value="TRANSFER_COMPLETE" v-if="roleNameList && (roleNameList.indexOf(roleNameAllList[0]) != -1
                                    || roleNameList.indexOf(roleNameAllList[1]) != -1
                                    || roleNameList.indexOf(roleNameAllList[2]) != -1
                                    || roleNameList.indexOf(roleNameAllList[3]) != -1
                                    || roleNameList.indexOf(roleNameAllList[4]) != -1
                                    || roleNameList.indexOf(roleNameAllList[5]) != -1
                                    || roleNameList.indexOf(roleNameAllList[6]) != -1
                                    || roleNameList.indexOf(roleNameAllList[7]) != -1)">转单完成
                            </option>
                        </select>
                    </div>
                    <div class="col-md-3 form-group">
                        <label class="sr-only" for="changeTime"></label>
                        <select v-model="form.changeTime"
                                id="changeTime"
                                name="changeTime"
                                class="form-control">
                            <option value="">选择时间类别</option>
                            <option value="LATEST_CHANGE">最新变更时间</option>
                            <option value="SUBMIT_AUDIT_TIME">提交审计时间</option>
                            <option value="AUDIT_PASS_TIME">审计通过时间</option>
                        </select>
                    </div>
                    <div class="col-md-3 form-group">
                        <input v-model="form.startDate" v-el:start-date id="startDate"
                               name="startDate" type="text" readonly
                               class="form-control datepicker" placeholder="请选择开始时间">
                    </div>
                    <div class="col-md-3 form-group">
                        <input v-model="form.endDate" v-el:end-date id="endDate"
                               name="endDate" type="text" readonly
                               class="form-control datepicker" placeholder="请选择结束时间">
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

<div id="detailModal" class="modal fade" tabindex="-1" data-width="1000">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal"
                aria-hidden="true">×
        </button>
        <h3 align="center">变更历史列表</h3>
    </div>
    <div class="modal-body">
        <div class="ibox">
            <div class="ibox-content">
                <div class="row">
                    <form id="search">
                        <div class="col-md-4 form-group">
                            <label class="sr-only" for="changeStatus">变更版本号：</label>
                            <select v-model="form.changeVersionNo"
                                    name="changeVersionNo"
                                    class="form-control">
                                <option value="">请选择变更版本号</option>
                                <option v-for="item in changeVersionNoList" :value="item.changeVersionNo">
                                    {{item.changeVersionNo}}
                                </option>
                            </select>
                        </div>
                        <div class="col-md-4 form-group">
                            <button id="searchbutten" type="button" @click.prevent="query"
                                    class="btn btn-block btn-outline btn-default" alt="搜索"
                                    title="搜索">
                                <i class="fa fa-search"></i>
                            </button>
                        </div>
                    </form>
                </div>
                <table id="dataTable2" width="100%"
                       class="table table-striped table-bordered table-hover">
                </table>
            </div>
        </div>
    </div>
</div>

<div id="moreModal" class="modal fade" tabindex="-1" data-width="700">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal"
                aria-hidden="true">×
        </button>
        <h3 align="center">更多操作</h3>
    </div>
    <div class="modal-body">
        <div style="margin-top: 15px; text-align: center" v-cloak>
            <button type="button" @click="originalMaterialPreview" class="btn btn-primary" v-if="displayObj[0]">原始选材单预览
            </button>&nbsp;&nbsp;
            <button type="button" @click="materialDownload" class="btn btn-primary" v-if="displayObj[1]">选材单下载</button>&nbsp;&nbsp;
            <button type="button" @click="demolitionHouses" class="btn btn-primary" v-if="displayObj[2]">老房拆改项下载
            </button>&nbsp;&nbsp;
            <button type="button" @click="submitAudit" class="btn btn-primary" v-if="displayObj[3]">提交变更审核</button>&nbsp;&nbsp;
        </div>
    </div>
</div>

<div id="auditorModal" class="modal fade" tabindex="-1" data-width="700">
    <div class="modal-body">
        <div class="ibox">
            <div class="col-md-4 form-group">
                <label class="sr-only" for="auditorName"></label>
                <select v-model="auditorName"
                        id="auditorName1"
                        name="auditorName"
                        class="form-control">
                    <option value="">请选择审计员</option>
                    <option v-for="auditor in auditors" :value="auditor.name">{{auditor.name}}</option>
                </select>
            </div>
            <div class="col-sm-12">
                <button type="button" @click="saveAuditor" class="btn btn-primary">确定</button>
                <button type="button" data-dismiss="modal" class="btn">取消</button>
            </div>
        </div>
        <div class="ibox">
            <div class="col-sm-12">
                <label for="auditor" class="control-label col-sm-2">历史指派</label>
                <table width="100%" class="table table-striped table-bordered table-hover">
                    <tbody align="center">
                    <tr>
                        <td>{{item.pdCategoryName}}</td>
                        <td>{{item.pdName}}</td>
                        <td>本选材单指派给</td>
                        <td>{{item.addCount}}</td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

<div id="materialPreviewModal" class="modal fade" tabindex="-1" data-width="1100">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal"
                aria-hidden="true">×
        </button>
    </div>
    <div class="modal-body">
        <%--客户信息--%>
        <div class="panel-group table-responsive">
            <div class="panel-group table-responsive panel panel-default" style="padding:0px 15px">
                <div class="row panel panel-heading" style="padding: 10px">
                    <div class="col-sm-2">客户信息</div>
                </div>
                <div class="row detail-stranding-book detail-state" v-cloak>
                    <div class="col-sm-12" style="margin-top: 15px">
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
                                <td>计价面积</td>
                                <td>{{customerContract.valuateArea}}</td>
                            </tr>
                            <tr>
                                <td>第二联系人联系人</td>
                                <td>{{customerContract.secondContact}}</td>
                                <td>有无电梯</td>
                                <td>{{customerContract.elevator | goType}}</td>
                                <td>房屋状况</td>
                                <td>{{customerContract.houseCondition | houseStatus}}</td>
                            </tr>
                            <tr>
                                <td>第二联系人电话</td>
                                <td>{{customerContract.secondContactMobile}}</td>
                                <td>房屋户型</td>
                                <td>{{customerContract.layout}}</td>
                                <td>房屋类型</td>
                                <td>{{customerContract.houseType | houseType}}</td>
                            </tr>
                            <tr>
                                <td>量房完成时间</td>
                                <td>{{customerContract.bookHouseCompleteTime}}</td>
                                <td>出图完成时间</td>
                                <td>{{customerContract.outMapCompleteTime}}</td>
                                <td>活动名称</td>
                                <td>{{customerContract.activityName}}</td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
        <%--套餐标配--%>
        <div class="panel-group table-responsive">
            <div class="panel-group table-responsive panel panel-default" style="padding:0px 15px" v-cloak>
                <div class="row panel panel-heading" style="padding: 10px">
                    <div class="col-sm-2" style="text-align: left">
                        <h4>套餐标配 ({{getProjectMaterialAmount()}})</h4>
                    </div>
                    <div class="col-sm-3"></div>
                    <%--<div class="col-sm-1" style="text-align: right">--%>
                    <%--<a href="#" @click="chooseCatalog('all')"><span id="allId">全部</span></a>--%>
                    <%--</div>--%>
                    <%--<div class="col-sm-6">--%>
                    <%--&lt;%&ndash;遍历一级分类集合&ndash;%&gt;--%>
                    <%--<a v-for="catalog in lowerCatalogList" @click="chooseCatalog(catalog.url)">--%>
                    <%--<span id="{{catalog.url}}Id">{{catalog.name}}&nbsp;&nbsp;</span>--%>
                    <%--</a>--%>
                    <%--</div>--%>
                </div>

                <div class="row">
                    <div class="col-sm-2" style="text-align: center">
                        <img src="/static/business/image/jz.png" style="width:80px;height:80px;"/>
                    </div>
                    <div class="col-sm-10" style="text-align: left">
                        基础装修(水电基材及人工) <br><br>
                        <label for="hangCeiling" class="pointSty">
                            <input id="hangCeiling" type="checkbox" v-model="hangCeiling" disabled/>吊顶
                        </label>
                        <label for="plasterLine" class="pointSty">
                            <input id="plasterLine" type="checkbox" v-model="plasterLine" disabled/>石膏线
                        </label>
                        <label for="telWall" class="pointSty">
                            <input id="telWall" type="checkbox" v-model="telWall" disabled/>电视背景墙
                        </label>
                    </div>
                </div>

                <table width="100%" class="table table-striped table-bordered table-hover
                    table-de panel-heading border-bottom-style" style="margin-top: 10px">
                    <thead>
                    <%--遍历二级分类--%>
                    <tr align="center" v-for="catalog in lowerCatalogList" v-if="catalog.parent.id != 0">
                        <td width="10%" style="vertical-align: middle; text-align: center">
                            {{catalog.parent.name}} <br>
                            {{catalog.name}} <br>
                        </td>
                        <td width="90%">
                            <%--大类目明细--%>
                            <div class="table-bd" v-for="projectMaterial in catalog.projectMaterialList">
                                <%--选材!--%>
                                <div v-if="!projectMaterial.backColorFlag">
                                    <!--商品projectMaterial属性-->
                                    <div class="table-row clearfix">
                                        <div class="item item-proname clearfix">
                                            <img class="goods-media pull-left" :src="projectMaterial.skuImagePath"
                                                 alt="商品图" width="80" height="80"/>
                                            <div class="item-inner pull-left">
                                                <div class="goods-title _ellipsis">
                                                    {{projectMaterial.productName}}
                                                </div>
                                                <div class="goods-item _ellipsis4">
                                                    {{projectMaterial.attribute1}}<br>
                                                    {{projectMaterial.attribute2}}<br>
                                                    {{projectMaterial.attribute3}}<br>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="item item-pay">
                                            <p>
                                                <input type="text" class="input-num"
                                                       v-model="projectMaterial.lossDosageAmount" readonly>
                                                <%--当平米转片时,单位显示为 片--%>
                                                <span v-if="projectMaterial.skuDosageList[0].convertUnit == 'square_meter_turn'">
                                                        片
                                                    </span>
                                                <span v-else>
                                                        {{projectMaterial.materialUnit.substring(projectMaterial.materialUnit.indexOf("/")+1, projectMaterial.materialUnit.length)}}
                                                    </span>
                                            </p>
                                            <p>单 价：￥ {{projectMaterial.storeSalePrice}}&nbsp;
                                                <%--当平米转片时,单位显示为 元/片--%>
                                                <span v-if="projectMaterial.skuDosageList[0].convertUnit == 'square_meter_turn'">
                                                        元/片
                                                    </span>
                                                <span v-else>
                                                        {{projectMaterial.materialUnit}}
                                                    </span></p>
                                            <p>合 计：<span class="text-red">￥ {{projectMaterial.skuSumMoney}}</span></p>
                                        </div>
                                    </div>
                                    <%--用量集合 dosageList--%>
                                    <!-- /.table-row -->
                                    <div class="table-row clearfix" v-for="skuDosage in projectMaterial.skuDosageList">
                                        <div class="item item-proname" v-if="skuDosage.lossDosage != 0">
                                            <div class="item-inner">
                                                <div class="goods-item clearfix">
                                                    <span>功能区：{{skuDosage.domainName}}</span>
                                                    <span>预算用量：{{skuDosage.budgetDosage}}&nbsp;
                                                        <%--当平米转片时,单位显示为㎡--%>
                                                        <span v-if="skuDosage.convertUnit == 'square_meter_turn'">
                                                            ㎡
                                                        </span>
                                                        <span v-else>
                                                            {{projectMaterial.materialUnit.substring(projectMaterial.materialUnit.indexOf("/")+1, projectMaterial.materialUnit.length)}}
                                                        </span>
                                                    </span>
                                                    <span>含损耗用量：{{skuDosage.lossDosage}}&nbsp;
                                                        <%--当平米转片时,单位显示为片--%>
                                                        <span v-if="skuDosage.convertUnit == 'square_meter_turn'">
                                                            片
                                                        </span>
                                                        <span v-else>
                                                            {{projectMaterial.materialUnit.substring(projectMaterial.materialUnit.indexOf("/")+1, projectMaterial.materialUnit.length)}}
                                                        </span>
                                                    </span>
                                                    <span>用量备注：<font color="#008b8b">{{skuDosage.dosageRemark}}</font> </span>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <!-- /.table-row -->
                                    <div class="table-row clearfix">
                                        <div class="item item-proname">
                                            <div class="item-inner">
                                                <div class="goods-item">
                                                    <span>预算用量合计：{{projectMaterial.budgetDosageAmount}}&nbsp;
                                                        <%--当平米转片时,单位显示为㎡--%>
                                                        <span v-if="projectMaterial.skuDosageList[0].convertUnit == 'square_meter_turn'">
                                                            ㎡
                                                        </span>
                                                        <span v-else>
                                                            {{projectMaterial.materialUnit.substring(projectMaterial.materialUnit.indexOf("/")+1, projectMaterial.materialUnit.length)}}
                                                        </span>
                                                    </span>
                                                    <span>含损耗用量合计：{{projectMaterial.lossDosageAmount}}&nbsp;
                                                        <%--当平米转片时,单位显示为片--%>
                                                        <span v-if="projectMaterial.skuDosageList[0].convertUnit == 'square_meter_turn'">
                                                            片
                                                        </span>
                                                        <span v-else>
                                                            {{projectMaterial.materialUnit.substring(projectMaterial.materialUnit.indexOf("/")+1, projectMaterial.materialUnit.length)}}
                                                        </span>
                                                    </span>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <!-- /.table-row -->
                                    <div class="table-row clearfix">
                                        <div class="item item-proname">
                                            <div class="item-inner">
                                                <div class="goods-item"><span>设计备注：<span style="color:#ff9900">{{projectMaterial.designRemark}}</span> </span>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <!-- /.table-row -->
                                    <div class="table-row clearfix">
                                        <div class="item item-proname">
                                            <div class="item-inner">
                                                <div class="goods-item"><span>审计备注：<span style="color:#FF3030">{{projectMaterial.auditRemark}}</span></span>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <!-- /.table-row -->
                                </div>

                                <%--变更!--%>
                                <div v-if="projectMaterial.backColorFlag == 'change'"
                                     :class="backColorObj.changeBackColor">
                                    <!--商品projectMaterial属性-->
                                    <div class="table-row clearfix">
                                        <div class="item item-proname clearfix">
                                            <img class="goods-media pull-left" :src="projectMaterial.skuImagePath"
                                                 alt="商品图" width="80" height="80"/>
                                            <div class="item-inner pull-left">
                                                <div class="goods-title _ellipsis">
                                                    {{projectMaterial.productName}}
                                                </div>
                                                <div class="goods-item _ellipsis4">
                                                    {{projectMaterial.attribute1}}<br>
                                                    {{projectMaterial.attribute2}}<br>
                                                    {{projectMaterial.attribute3}}<br>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="item item-pay">
                                            <p>
                                                <input type="text" class="input-num"
                                                       v-model="projectMaterial.lossDosageAmount" readonly>
                                                {{projectMaterial.materialUnit}}
                                            </p>
                                            <p>单 价：￥ {{projectMaterial.storeSalePrice}}
                                                /{{projectMaterial.materialUnit}}</p>
                                            <p>合 计：<span class="text-red">￥ {{projectMaterial.skuSumMoney}}</span></p>
                                        </div>
                                    </div>
                                    <%--用量集合 dosageList--%>
                                    <!-- /.table-row -->
                                    <div class="table-row clearfix" v-for="skuDosage in projectMaterial.skuDosageList">
                                        <div class="item item-proname" v-if="skuDosage.lossDosage != 0">
                                            <div class="item-inner">
                                                <div class="goods-item clearfix">
                                                    <span>功能区：{{skuDosage.domainName}}</span>
                                                    <span>预算用量：{{skuDosage.budgetDosage}} {{projectMaterial.materialUnit}}</span>
                                                    <span>含损耗用量：{{skuDosage.lossDosage}} {{projectMaterial.materialUnit}}</span>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <!-- /.table-row -->
                                    <div class="table-row clearfix">
                                        <div class="item item-proname">
                                            <div class="item-inner">
                                                <div class="goods-item">
                                                    <span>预算用量合计：{{projectMaterial.budgetDosageAmount}} {{projectMaterial.materialUnit}}</span>
                                                    <span>含损耗用量合计：{{projectMaterial.lossDosageAmount}} {{projectMaterial.materialUnit}}</span>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <!-- /.table-row -->
                                    <div class="table-row clearfix">
                                        <div class="item item-proname">
                                            <div class="item-inner">
                                                <div class="goods-item"><span>设计备注：<span style="color:#ff9900">{{projectMaterial.designRemark}}</span> </span>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <!-- /.table-row -->
                                    <div class="table-row clearfix">
                                        <div class="item item-proname">
                                            <div class="item-inner">
                                                <div class="goods-item"><span>审计备注：<span style="color:#FF3030">{{projectMaterial.auditRemark}}</span></span>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <!-- /.table-row -->
                                </div>

                                <%--被打回!--%>
                                <div v-if="projectMaterial.backColorFlag == 'turnBack'"
                                     :class="backColorObj.turnBackColor">
                                    <!--商品projectMaterial属性-->
                                    <div class="table-row clearfix">
                                        <div class="item item-proname clearfix">
                                            <img class="goods-media pull-left" :src="projectMaterial.skuImagePath"
                                                 alt="商品图" width="80" height="80"/>
                                            <div class="item-inner pull-left">
                                                <div class="goods-title _ellipsis">
                                                    {{projectMaterial.productName}}
                                                </div>
                                                <div class="goods-item _ellipsis4">
                                                    {{projectMaterial.attribute1}}<br>
                                                    {{projectMaterial.attribute2}}<br>
                                                    {{projectMaterial.attribute3}}<br>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="item item-pay">
                                            <p>
                                                <input type="text" class="input-num"
                                                       v-model="projectMaterial.lossDosageAmount" readonly>
                                                {{projectMaterial.materialUnit}}
                                            </p>
                                            <p>单 价：￥ {{projectMaterial.storeSalePrice}}
                                                /{{projectMaterial.materialUnit}}</p>
                                            <p>合 计：<span class="text-red">￥ {{projectMaterial.skuSumMoney}}</span></p>
                                        </div>
                                    </div>
                                    <%--用量集合 dosageList--%>
                                    <!-- /.table-row -->
                                    <div class="table-row clearfix" v-for="skuDosage in projectMaterial.skuDosageList">
                                        <div class="item item-proname" v-if="skuDosage.lossDosage != 0">
                                            <div class="item-inner">
                                                <div class="goods-item clearfix">
                                                    <span>功能区：{{skuDosage.domainName}}</span>
                                                    <span>预算用量：{{skuDosage.budgetDosage}} {{projectMaterial.materialUnit}}</span>
                                                    <span>含损耗用量：{{skuDosage.lossDosage}} {{projectMaterial.materialUnit}}</span>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <!-- /.table-row -->
                                    <div class="table-row clearfix">
                                        <div class="item item-proname">
                                            <div class="item-inner">
                                                <div class="goods-item">
                                                    <span>预算用量合计：{{projectMaterial.budgetDosageAmount}} {{projectMaterial.materialUnit}}</span>
                                                    <span>含损耗用量合计：{{projectMaterial.lossDosageAmount}} {{projectMaterial.materialUnit}}</span>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <!-- /.table-row -->
                                    <div class="table-row clearfix">
                                        <div class="item item-proname">
                                            <div class="item-inner">
                                                <div class="goods-item"><span>设计备注：<span style="color:#ff9900">{{projectMaterial.designRemark}}</span> </span>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <!-- /.table-row -->
                                    <div class="table-row clearfix">
                                        <div class="item item-proname">
                                            <div class="item-inner">
                                                <div class="goods-item"><span>审计备注：<span style="color:#FF3030">{{projectMaterial.auditRemark}}</span></span>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <!-- /.table-row -->
                                </div>
                            </div>
                        </td>
                    </tr>
                    </thead>
                </table>
                <div class="row panel panel-body" style="padding: 10px">
                    <h4 align="right" style="color: red">
                        套餐标配价：{{customerContract.valuateArea}}*{{customerContract.mealPrice}}={{amount.packagestandardprice}}</h4>
                </div>
            </div>

        </div>
        <%--升级项--%>
        <div class="panel-group table-responsive">
            <div class="panel-group table-responsive panel panel-default" style="padding:0px 15px" v-cloak>
                <div class="row panel panel-heading" style="padding: 10px">
                    <div class="col-sm-2">
                        <h4>升级项 ({{getUpgradeAmount()}})</h4>
                    </div>
                </div>
                <div class="row panel-body">
                    <table width="100%" class="table table-striped table-bordered table-hover
                    table-de panel-heading border-bottom-style" style="margin-top: 10px">
                        <thead>
                        <%--遍历二级分类 并且当二级分类下面有sku数据时,展示--%>
                        <tr align="center" v-for="catalog in upgradeLowerCatalogList"
                            v-show="catalog.projectMaterialList != null && catalog.projectMaterialList.length > 0">
                            <td width="10%" style="vertical-align: middle; text-align: center">
                                {{catalog.parent.name}} <br>
                                {{catalog.name}} <br>
                            </td>
                            <td width="90%">

                                <%--大类目明细--%>
                                <div class="table-bd" v-for="projectMaterial in catalog.projectMaterialList">
                                    <%--选材!--%>
                                    <div v-if="!projectMaterial.backColorFlag">
                                        <!--商品projectMaterial属性-->
                                        <div class="table-row clearfix">
                                            <div class="item item-proname clearfix">
                                                <img class="goods-media pull-left" :src="projectMaterial.skuImagePath"
                                                     alt="商品图" width="80" height="80"/>
                                                <div class="item-inner pull-left">
                                                    <div class="goods-title _ellipsis">
                                                        {{projectMaterial.productName}}
                                                    </div>
                                                    <div class="goods-item _ellipsis4">
                                                        {{projectMaterial.attribute1}}<br>
                                                        {{projectMaterial.attribute2}}<br>
                                                        {{projectMaterial.attribute3}}<br>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="item item-pay">
                                                <p>
                                                    <input type="text" class="input-num"
                                                           v-model="projectMaterial.lossDosageAmount" readonly>
                                                    <%--当平米转片时,单位显示为 片--%>
                                                    <span v-if="projectMaterial.skuDosageList[0].convertUnit == 'square_meter_turn'">
                                                        片
                                                    </span>
                                                    <span v-else>
                                                        {{projectMaterial.materialUnit.substring(projectMaterial.materialUnit.indexOf("/")+1, projectMaterial.materialUnit.length)}}
                                                    </span>
                                                </p>
                                                <p>单 价：￥ {{projectMaterial.storeSalePrice}}&nbsp;
                                                    <%--当平米转片时,单位显示为 元/片--%>
                                                    <span v-if="projectMaterial.skuDosageList[0].convertUnit == 'square_meter_turn'">
                                                        元/片
                                                    </span>
                                                    <span v-else>
                                                        {{projectMaterial.materialUnit}}
                                                    </span>
                                                </p>
                                                <p>合 计：<span class="text-red">￥ {{projectMaterial.skuSumMoney}}</span>
                                                </p>
                                            </div>
                                        </div>
                                        <%--用量集合 dosageList--%>
                                        <!-- /.table-row -->
                                        <div class="table-row clearfix"
                                             v-for="skuDosage in projectMaterial.skuDosageList">
                                            <div class="item item-proname" v-if="skuDosage.lossDosage != 0">
                                                <div class="item-inner">
                                                    <div class="goods-item clearfix">
                                                        <span>功能区：{{skuDosage.domainName}}</span>
                                                        <span>预算用量：{{skuDosage.budgetDosage}}&nbsp;
                                                            <%--当平米转片时,单位显示为㎡--%>
                                                            <span v-if="skuDosage.convertUnit == 'square_meter_turn'">
                                                                ㎡
                                                            </span>
                                                            <span v-else>
                                                                {{projectMaterial.materialUnit.substring(projectMaterial.materialUnit.indexOf("/")+1, projectMaterial.materialUnit.length)}}
                                                            </span>
                                                        </span>
                                                        <span>含损耗用量：{{skuDosage.lossDosage}}&nbsp;
                                                            <%--当平米转片时,单位显示为片--%>
                                                            <span v-if="skuDosage.convertUnit == 'square_meter_turn'">
                                                                片
                                                            </span>
                                                            <span v-else>
                                                                {{projectMaterial.materialUnit.substring(projectMaterial.materialUnit.indexOf("/")+1, projectMaterial.materialUnit.length)}}
                                                            </span>
                                                        </span>
                                                        <span>用量备注：<font
                                                                color="#008b8b">{{skuDosage.dosageRemark}}</font> </span>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <!-- /.table-row -->
                                        <div class="table-row clearfix">
                                            <div class="item item-proname">
                                                <div class="item-inner">
                                                    <div class="goods-item">
                                                        <span>预算用量合计：{{projectMaterial.budgetDosageAmount}}&nbsp;
                                                            <%--当平米转片时,单位显示为㎡--%>
                                                            <span v-if="projectMaterial.skuDosageList[0].convertUnit == 'square_meter_turn'">
                                                                ㎡
                                                            </span>
                                                            <span v-else>
                                                                {{projectMaterial.materialUnit.substring(projectMaterial.materialUnit.indexOf("/")+1, projectMaterial.materialUnit.length)}}
                                                            </span>
                                                        </span>
                                                        <span>含损耗用量合计：{{projectMaterial.lossDosageAmount}}&nbsp;
                                                            <%--当平米转片时,单位显示为片--%>
                                                            <span v-if="projectMaterial.skuDosageList[0].convertUnit == 'square_meter_turn'">
                                                                片
                                                            </span>
                                                            <span v-else>
                                                                {{projectMaterial.materialUnit.substring(projectMaterial.materialUnit.indexOf("/")+1, projectMaterial.materialUnit.length)}}
                                                            </span>
                                                        </span>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <!-- /.table-row -->
                                        <div class="table-row clearfix">
                                            <div class="item item-proname">
                                                <div class="item-inner">
                                                    <div class="goods-item"><span>设计备注：<span style="color:#ff9900">{{projectMaterial.designRemark}}</span> </span>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <!-- /.table-row -->
                                        <div class="table-row clearfix">
                                            <div class="item item-proname">
                                                <div class="item-inner">
                                                    <div class="goods-item"><span>审计备注：<span style="color:#FF3030">{{projectMaterial.auditRemark}}</span></span>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <!-- /.table-row -->

                                    </div>
                                </div>
                            </td>
                        </tr>
                        </thead>
                    </table>
                </div>
                <div class="row panel-body">
                    <h4 align="right" style="color: red">升级价：+{{amount.upgradeitemprice}}</h4>
                </div>
            </div>
        </div>
        <%--增项--%>
        <div class="panel-group table-responsive">
            <div class="panel-group table-responsive panel panel-default" style="padding:0px 15px" v-cloak>
                <div class="row panel panel-heading" style="padding: 10px">
                    <div class="col-sm-2" style="text-align: left">
                        <h4>增项 ({{getAddAmount()}})</h4>
                    </div>
                </div>
                <%--主材--%>
                <div class="panel-group table-responsive">
                    <div class="panel-group table-responsive panel panel-default" style="padding:0px 15px">
                        <div class="row panel panel-heading" style="padding: 10px">
                            <div class="col-sm-2" style="text-align: left">
                                <h4>主材 ({{getAddMaterialAmount()}})</h4>
                            </div>
                        </div>
                        <div class="row panel-body">
                            <table width="100%" class="table table-striped table-bordered table-hover
                    table-de panel-heading border-bottom-style" style="margin-top: 10px">
                                <thead>
                                <%--遍历二级分类 并且当二级分类下面有sku数据时,展示--%>
                                <tr align="center" v-for="catalog in addLowerCatalogList"
                                    v-show="catalog.projectMaterialList != null && catalog.projectMaterialList.length > 0">
                                    <td width="10%" style="vertical-align: middle; text-align: center">
                                        {{catalog.parent.name}} <br>
                                        {{catalog.name}} <br>
                                    </td>
                                    <td width="90%">
                                        <%--大类目明细--%>
                                        <div class="table-bd"
                                             v-for="projectMaterial in catalog.projectMaterialList">
                                            <%--选材!--%>
                                            <div v-if="!projectMaterial.backColorFlag">
                                                <!--商品projectMaterial属性-->
                                                <div class="table-row clearfix">
                                                    <div class="item item-proname clearfix">
                                                        <img class="goods-media pull-left"
                                                             :src="projectMaterial.skuImagePath" alt="商品图"
                                                             width="80"
                                                             height="80"/>
                                                        <div class="item-inner pull-left">
                                                            <div class="goods-title _ellipsis">
                                                                {{projectMaterial.productName}}
                                                            </div>
                                                            <div class="goods-item _ellipsis4">
                                                                {{projectMaterial.attribute1}}<br>
                                                                {{projectMaterial.attribute2}}<br>
                                                                {{projectMaterial.attribute3}}<br>
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="item item-pay">
                                                        <p>
                                                            <input type="text" class="input-num"
                                                                   v-model="projectMaterial.lossDosageAmount" readonly>
                                                            <%--当平米转片时,单位显示为 片--%>
                                                            <span v-if="projectMaterial.skuDosageList[0].convertUnit == 'square_meter_turn'">
                                                                片
                                                            </span>
                                                            <span v-else>
                                                                {{projectMaterial.materialUnit.substring(projectMaterial.materialUnit.indexOf("/")+1, projectMaterial.materialUnit.length)}}
                                                            </span>
                                                        </p>
                                                        <p>单 价：￥ {{projectMaterial.storeSalePrice}}&nbsp;
                                                            <%--当平米转片时,单位显示为 元/片--%>
                                                            <span v-if="projectMaterial.skuDosageList[0].convertUnit == 'square_meter_turn'">
                                                                元/片
                                                            </span>
                                                            <span v-else>
                                                                {{projectMaterial.materialUnit}}
                                                            </span>
                                                        </p>
                                                        <p>合 计：<span
                                                                class="text-red">￥ {{projectMaterial.skuSumMoney}}</span>
                                                        </p>
                                                    </div>
                                                </div>
                                                <%--用量集合 dosageList--%>
                                                <!-- /.table-row -->
                                                <div class="table-row clearfix"
                                                     v-for="skuDosage in projectMaterial.skuDosageList">
                                                    <div class="item item-proname">
                                                        <div class="item-inner">
                                                            <div class="goods-item clearfix">
                                                                <span>功能区：{{skuDosage.domainName}}</span>
                                                                <span>预算用量：{{skuDosage.budgetDosage}}&nbsp;
                                                                    <%--当平米转片时,单位显示为㎡--%>
                                                                    <span v-if="skuDosage.convertUnit == 'square_meter_turn'">
                                                                        ㎡
                                                                    </span>
                                                                    <span v-else>
                                                                        {{projectMaterial.materialUnit.substring(projectMaterial.materialUnit.indexOf("/")+1, projectMaterial.materialUnit.length)}}
                                                                    </span>
                                                                </span>
                                                                <span>含损耗用量：{{skuDosage.lossDosage}}&nbsp;
                                                                    <%--当平米转片时,单位显示为片--%>
                                                                    <span v-if="skuDosage.convertUnit == 'square_meter_turn'">
                                                                        片
                                                                    </span>
                                                                    <span v-else>
                                                                        {{projectMaterial.materialUnit.substring(projectMaterial.materialUnit.indexOf("/")+1, projectMaterial.materialUnit.length)}}
                                                                    </span>
                                                                </span>
                                                                <span>用量备注：<font color="#008b8b">{{skuDosage.dosageRemark}}</font> </span>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                                <!-- /.table-row -->
                                                <div class="table-row clearfix">
                                                    <div class="item item-proname">
                                                        <div class="item-inner">
                                                            <div class="goods-item">
                                                                <span>预算用量合计：{{projectMaterial.budgetDosageAmount}}&nbsp;
                                                                    <%--当平米转片时,单位显示为㎡--%>
                                                                    <span v-if="projectMaterial.skuDosageList[0].convertUnit == 'square_meter_turn'">
                                                                        ㎡
                                                                    </span>
                                                                    <span v-else>
                                                                        {{projectMaterial.materialUnit.substring(projectMaterial.materialUnit.indexOf("/")+1, projectMaterial.materialUnit.length)}}
                                                                    </span>
                                                                </span>
                                                                <span>含损耗用量合计：{{projectMaterial.lossDosageAmount}}&nbsp;
                                                                    <%--当平米转片时,单位显示为片--%>
                                                                    <span v-if="projectMaterial.skuDosageList[0].convertUnit == 'square_meter_turn'">
                                                                        片
                                                                    </span>
                                                                    <span v-else>
                                                                        {{projectMaterial.materialUnit.substring(projectMaterial.materialUnit.indexOf("/")+1, projectMaterial.materialUnit.length)}}
                                                                    </span>
                                                                </span>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                                <!-- /.table-row -->
                                                <div class="table-row clearfix">
                                                    <div class="item item-proname">
                                                        <div class="item-inner">
                                                            <div class="goods-item"><span>设计备注：<span
                                                                    style="color:#ff9900">{{projectMaterial.designRemark}}</span> </span>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                                <!-- /.table-row -->
                                                <div class="table-row clearfix">
                                                    <div class="item item-proname">
                                                        <div class="item-inner">
                                                            <div class="goods-item"><span>审计备注：<span
                                                                    style="color:#FF3030">{{projectMaterial.auditRemark}}</span></span>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                                <!-- /.table-row -->
                                            </div>
                                        </div>
                                    </td>
                                </tr>
                                </thead>
                            </table>

                        </div>
                    </div>
                </div>
                <%--定额--%>
                <div class="panel-group table-responsive">
                    <div class="panel-group table-responsive panel panel-default" style="padding:0px 15px">
                        <div class="row panel panel-heading" style="padding: 10px">
                            <div class="col-sm-2" style="text-align: left">
                                <h4>定额 ({{addBaseInstallQuotaList.length + addBaseInstallComfeeList.length}})</h4>
                                基装定额 ({{addBaseInstallQuotaList.length}})
                            </div>
                        </div>
                        <div class="row panel-body" v-show="addBaseInstallQuotaList.length > 0">
                            <table width="100%" class="table table-striped table-bordered table-hover
                    table-de panel-heading border-bottom-style" style="margin-top: 10px">
                                <thead>
                                <%--遍历基装增项综合费--%>
                                <tr align="center" v-for="projectMaterial in addBaseInstallQuotaList">
                                    <td width="10%" style="vertical-align: middle; text-align: center">
                                        <%--瓦工 分类--%>
                                        {{projectMaterial.skuDosageList[0].domainName}} <br>
                                    </td>
                                    <td width="90%">
                                        <%--大类目明细--%>
                                        <table align="center" width="100%">
                                            <tr style="height: 25px;">
                                                <%--大类目明细--%>
                                                <td width="50%"><%--功能区名称--%>
                                                    <strong> {{projectMaterial.productName}} </strong>
                                                </td>
                                                <td width="20%">
                                                    <input type="text"
                                                           style="width:50px;height:100%;text-align: center;"
                                                           class="form-control"
                                                           v-model="projectMaterial.skuDosageList[0].lossDosage || 0"
                                                           readonly>
                                                </td>
                                                <td width="20%">
                                    <span v-show="projectMaterial.skuDosageList[0].dosagePricingMode == 'fixedUnitPrice'">
                                        单 价：￥ {{projectMaterial.storeSalePrice}} /{{projectMaterial.materialUnit}}<br>
                                        合 计：<span class="text-red">￥ {{(projectMaterial.storeSalePrice * projectMaterial.skuDosageList[0].lossDosage).toFixed(2)}}</span>
                                    </span>
                                                </td>
                                            </tr>
                                            <tr style="height: 25px;">
                                                <td colspan="4">
                                                    <%--定额描述--%>
                                                    {{projectMaterial.quotaDescribe}}
                                                </td>
                                            </tr>
                                            <tr style="height: 25px;">
                                                <td colspan="4">设计备注：<span style="color:#ff9900">{{projectMaterial.designRemark}}</span>
                                                </td>
                                            </tr>
                                            <tr style="height: 25px;">
                                                <td colspan="4">审计备注：<span style="color:#FF3030">{{projectMaterial.auditRemark}}</span>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                                </thead>
                            </table>
                        </div>
                        <%--基装增项综合费--%>
                        <div class="row panel panel-heading" style="padding: 10px">
                            <div class="col-sm-2" style="text-align: left">
                                基装增项综合费 ({{addBaseInstallComfeeList.length}})
                            </div>
                        </div>
                        <div class="row panel-body" v-show="addBaseInstallComfeeList.length > 0">
                            <table width="100%" class="table table-striped table-bordered table-hover
                    table-de panel-heading border-bottom-style" style="margin-top: 10px">
                                <thead>
                                <%--遍历基装增项综合费--%>
                                <tr align="center" v-for="projectMaterial in addBaseInstallComfeeList">
                                    <td width="10%" style="vertical-align: middle; text-align: center">
                                        <%--瓦工 分类--%>
                                        {{projectMaterial.skuDosageList[0].domainName}} <br>
                                    </td>
                                    <td width="90%">
                                        <%--大类目明细--%>
                                        <table align="center" width="100%">
                                            <tr style="height: 25px;">
                                                <%--大类目明细--%>
                                                <td width="50%"><%--功能区名称--%>
                                                    <strong> {{projectMaterial.productName}} </strong>
                                                </td>
                                                <td width="20%">
                                            <span v-show="projectMaterial.skuDosageList[0].dosagePricingMode == 'foundationPileTotal'">
                                                基装增项总价 <br>
                                                {{totalAmount.baseloadrating1}} 元
                                            </span>
                                                    <%--<span v-show="projectMaterial.skuDosageList[0].dosagePricingMode == 'renovationFoundationPile'">
                                                        装修工程总价占比 <br>
                                                        {{totalAmount.renovationAmount}} 元
                                                    </span>--%>
                                                    <span v-show="projectMaterial.skuDosageList[0].dosagePricingMode == 'fixedUnitPrice'">
                                                <input type="text" style="width:50px;height:100%;text-align: center;"
                                                       class="form-control"
                                                       v-model="projectMaterial.skuDosageList[0].lossDosage || 0"
                                                       readonly>
                                            </span>
                                                </td>
                                                <td width="20%">
                                            <span v-show="projectMaterial.skuDosageList[0].dosagePricingMode == 'fixedUnitPrice'">
                                                单 价：￥ {{projectMaterial.storeSalePrice}} /{{projectMaterial.materialUnit}}<br>
                                                合 计：<span class="text-red">￥ {{projectMaterial.storeSalePrice * projectMaterial.skuDosageList[0].lossDosage}}</span>
                                            </span>
                                                    <%--基装增项总价--%>
                                                    <span v-show="projectMaterial.skuDosageList[0].dosagePricingMode == 'foundationPileTotal'">
                                                占 比：{{projectMaterial.skuDosageList[0].projectProportion}} % <br>
                                                合 计：<span class="text-red">
                                                    ￥ {{((projectMaterial.skuDosageList[0].projectProportion || 0)/100 * (totalAmount.baseloadrating1 || 0)).toFixed(2)}}
                                                  </span>
                                            </span>
                                                </td>
                                            </tr>
                                            <tr style="height: 25px;">
                                                <td colspan="4">
                                                    <%--定额描述--%>
                                                    {{projectMaterial.quotaDescribe}}
                                                </td>
                                            </tr>
                                            <tr style="height: 25px;">
                                                <td colspan="4">设计备注：<span style="color:#ff9900">{{projectMaterial.designRemark}}</span>
                                                </td>
                                            </tr>
                                            <tr style="height: 25px;">
                                                <td colspan="4">审计备注：<span style="color:#FF3030">{{projectMaterial.auditRemark}}</span>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                                </thead>
                            </table>
                        </div>
                    </div>
                </div>
                <div class="row panel panel-body" style="padding: 10px">
                    <h4 align="right" style="color: red">
                        增项：+{{amount.increment}}</h4>
                    <h4 align="right" style="color: red">
                        主材：+{{amount.mainmaterial1}}</h4>
                    <h4 align="right" style="color: red">
                        基装定额：+{{totalAmount.baseloadrating1}}</h4>
                    <h4 align="right" style="color: red">
                        基装增项综合费：+{{amount.comprehensivefee1}}</h4>
                </div>
            </div>
        </div>
        <%--减项--%>
        <div class="panel-group table-responsive">
            <div class="panel-group table-responsive panel panel-default" style="padding:0px 15px" v-cloak>
                <div class="row panel panel-heading" style="padding: 10px">
                    <div class="col-sm-2" style="text-align: left">
                        <h4>减项({{getReduceAmount()}})</h4>
                    </div>
                </div>
                <%--主材--%>
                <div class="panel-group table-responsive">
                    <div class="panel-group table-responsive panel panel-default" style="padding:0px 15px">
                        <div class="row panel panel-heading" style="padding: 10px">
                            <div class="col-sm-2" style="text-align: left">
                                <h4>主材 ({{getReduceMaterialAmount()}})</h4>
                            </div>
                        </div>
                        <div class="row panel-body">
                            <table width="100%" class="table table-striped table-bordered table-hover
                    table-de panel-heading border-bottom-style" style="margin-top: 10px">
                                <thead>
                                <%--遍历二级分类 并且当二级分类下面有sku数据时,展示--%>
                                <tr align="center" v-for="catalog in reduceLowerCatalogList"
                                    v-show="catalog.projectMaterialList != null && catalog.projectMaterialList.length > 0">
                                    <td width="10%" style="vertical-align: middle; text-align: center">
                                        {{catalog.parent.name}} <br>
                                        {{catalog.name}} <br>
                                    </td>
                                    <td width="90%">
                                        <%--大类目明细--%>
                                        <div class="table-bd"
                                             v-for="projectMaterial in catalog.projectMaterialList">
                                            <%--选材!--%>
                                            <div v-if="!projectMaterial.backColorFlag">
                                                <!--商品projectMaterial属性-->
                                                <div class="table-row clearfix">
                                                    <div class="item item-proname clearfix">
                                                        <img class="goods-media pull-left"
                                                             :src="projectMaterial.skuImagePath" alt="商品图"
                                                             width="80"
                                                             height="80"/>
                                                        <div class="item-inner pull-left">
                                                            <div class="goods-title _ellipsis">
                                                                {{projectMaterial.productName}}
                                                            </div>
                                                            <div class="goods-item _ellipsis4">
                                                                {{projectMaterial.attribute1}}<br>
                                                                {{projectMaterial.attribute2}}<br>
                                                                {{projectMaterial.attribute3}}<br>
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="item item-pay">
                                                        <p>
                                                            <input type="text" class="input-num"
                                                                   v-model="projectMaterial.lossDosageAmount" readonly>
                                                            <%--当平米转片时,单位显示为 片--%>
                                                            <span v-if="projectMaterial.skuDosageList[0].convertUnit == 'square_meter_turn'">
                                                                片
                                                            </span>
                                                            <span v-else>
                                                                {{projectMaterial.materialUnit.substring(projectMaterial.materialUnit.indexOf("/")+1, projectMaterial.materialUnit.length)}}
                                                            </span>
                                                        </p>
                                                        <p>单 价：￥ {{projectMaterial.storeSalePrice}}&nbsp;
                                                            <%--当平米转片时,单位显示为 元/片--%>
                                                            <span v-if="projectMaterial.skuDosageList[0].convertUnit == 'square_meter_turn'">
                                                                元/片
                                                            </span>
                                                            <span v-else>
                                                                {{projectMaterial.materialUnit}}
                                                            </span>
                                                        </p>
                                                        <p>合 计：<span
                                                                class="text-red">￥ {{projectMaterial.skuSumMoney}}</span>
                                                        </p>
                                                    </div>
                                                </div>
                                                <%--用量集合 dosageList--%>
                                                <!-- /.table-row -->
                                                <div class="table-row clearfix"
                                                     v-for="skuDosage in projectMaterial.skuDosageList">
                                                    <div class="item item-proname">
                                                        <div class="item-inner">
                                                            <div class="goods-item clearfix">
                                                                <span>功能区：{{skuDosage.domainName}}</span>
                                                                <span>预算用量：{{skuDosage.budgetDosage}}&nbsp;
                                                                    <%--当平米转片时,单位显示为㎡--%>
                                                                    <span v-if="skuDosage.convertUnit == 'square_meter_turn'">
                                                                        ㎡
                                                                    </span>
                                                                    <span v-else>
                                                                        {{projectMaterial.materialUnit.substring(projectMaterial.materialUnit.indexOf("/")+1, projectMaterial.materialUnit.length)}}
                                                                    </span>
                                                                </span>
                                                                <span>含损耗用量：{{skuDosage.lossDosage}}&nbsp;
                                                                    <%--当平米转片时,单位显示为片--%>
                                                                    <span v-if="skuDosage.convertUnit == 'square_meter_turn'">
                                                                        片
                                                                    </span>
                                                                    <span v-else>
                                                                        {{projectMaterial.materialUnit.substring(projectMaterial.materialUnit.indexOf("/")+1, projectMaterial.materialUnit.length)}}
                                                                    </span>
                                                                </span>
                                                                <span>用量备注：<font color="#008b8b">{{skuDosage.dosageRemark}}</font> </span>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                                <!-- /.table-row -->
                                                <div class="table-row clearfix">
                                                    <div class="item item-proname">
                                                        <div class="item-inner">
                                                            <div class="goods-item">
                                                                <span>预算用量合计：{{projectMaterial.budgetDosageAmount}}&nbsp;
                                                                    <%--当平米转片时,单位显示为㎡--%>
                                                                    <span v-if="projectMaterial.skuDosageList[0].convertUnit == 'square_meter_turn'">
                                                                        ㎡
                                                                    </span>
                                                                    <span v-else>
                                                                        {{projectMaterial.materialUnit.substring(projectMaterial.materialUnit.indexOf("/")+1, projectMaterial.materialUnit.length)}}
                                                                    </span>
                                                                </span>
                                                                <span>含损耗用量合计：{{projectMaterial.lossDosageAmount}}&nbsp;
                                                                    <%--当平米转片时,单位显示为片--%>
                                                                    <span v-if="projectMaterial.skuDosageList[0].convertUnit == 'square_meter_turn'">
                                                                        片
                                                                    </span>
                                                                    <span v-else>
                                                                        {{projectMaterial.materialUnit.substring(projectMaterial.materialUnit.indexOf("/")+1, projectMaterial.materialUnit.length)}}
                                                                    </span>
                                                                </span>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                                <!-- /.table-row -->
                                                <div class="table-row clearfix">
                                                    <div class="item item-proname">
                                                        <div class="item-inner">
                                                            <div class="goods-item"><span>设计备注：<span
                                                                    style="color:#ff9900">{{projectMaterial.designRemark}}</span> </span>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                                <!-- /.table-row -->
                                                <div class="table-row clearfix">
                                                    <div class="item item-proname">
                                                        <div class="item-inner">
                                                            <div class="goods-item"><span>审计备注：<span
                                                                    style="color:#FF3030">{{projectMaterial.auditRemark}}</span></span>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                                <!-- /.table-row -->
                                            </div>
                                        </div>
                                    </td>
                                </tr>
                                </thead>
                            </table>
                        </div>
                    </div>
                </div>
                <%--定额--%>
                <div class="panel-group table-responsive">
                    <div class="panel-group table-responsive panel panel-default" style="padding:0px 15px">
                        <div class="row panel panel-heading" style="padding: 10px">
                            <div class="col-sm-2" style="text-align: left">
                                <h4>定额 ({{reduceBaseInstallQuotaList.length}})</h4>
                                基装定额 ({{reduceBaseInstallQuotaList.length}})
                            </div>
                        </div>
                        <div class="row panel-body">
                            <table width="100%" class="table table-striped table-bordered table-hover
                    table-de panel-heading border-bottom-style" style="margin-top: 10px">
                                <thead>
                                <%--遍历基装增项综合费--%>
                                <tr align="center" v-for="projectMaterial in reduceBaseInstallQuotaList">
                                    <td width="10%" style="vertical-align: middle; text-align: center">
                                        <%--瓦工 分类--%>
                                        {{projectMaterial.skuDosageList[0].domainName}} <br>
                                    </td>
                                    <td width="90%">
                                        <table align="center" width="100%">
                                            <tr style="height: 25px;">
                                                <%--大类目明细--%>
                                                <td width="50%"><%--功能区名称--%>
                                                    <strong> {{projectMaterial.productName}} </strong>
                                                </td>
                                                <td width="20%">
                                                    <input type="text"
                                                           style="width:50px;height:100%;text-align: center;"
                                                           class="form-control"
                                                           v-model="projectMaterial.skuDosageList[0].lossDosage || 0"
                                                           readonly>
                                                </td>
                                                <td width="20%">
                                        <span v-show="projectMaterial.skuDosageList[0].dosagePricingMode == 'fixedUnitPrice'">
                                            单 价：￥ {{projectMaterial.storeSalePrice}} /{{projectMaterial.materialUnit}}<br>
                                            合 计：<span class="text-red"> ￥ {{(projectMaterial.storeSalePrice || 0) * (projectMaterial.skuDosageList[0].lossDosage || 0)}}</span>
                                        </span>
                                                </td>
                                            </tr>
                                            <tr style="height: 25px;">
                                                <td colspan="4">
                                                    <%--定额描述--%>
                                                    {{projectMaterial.quotaDescribe}}
                                                </td>
                                            </tr>
                                            <tr style="height: 25px;">
                                                <td colspan="4">设计备注：<span style="color:#ff9900">{{projectMaterial.designRemark}}</span>
                                                </td>
                                            </tr>
                                            <tr style="height: 25px;">
                                                <td colspan="4">审计备注：<span style="color:#FF3030">{{projectMaterial.auditRemark}}</span>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                                </thead>
                            </table>
                        </div>
                    </div>
                </div>
                <div class="row panel panel-body" style="padding: 10px">
                    <h4 align="right" style="color: red">
                        减项：-{{amount.subtraction.toFixed(2)}}</h4>
                    <h4 align="right" style="color: red">
                        主材：-{{amount.mainmaterial2.toFixed(2)}}</h4>
                    <h4 align="right" style="color: red">
                        基装定额：-{{amount.baseloadrating2.toFixed(2)}}</h4>
                </div>
            </div>
        </div>
        <%--活动、优惠及其它金额增减--%>
        <div class="panel-group table-responsive">
            <div class="panel-group table-responsive panel panel-default" style="padding:0px 15px" v-cloak>
                <div class="row panel panel-heading" style="padding: 10px">
                    <div class="col-sm-3" style="text-align: left">
                        <h4>活动、优惠及其它金额增减 ({{otherMoneyList.length}})</h4>
                    </div>
                </div>
                <div class="panel-body">
                    <div v-for="otherMoney in otherMoneyList" style="padding-bottom: 5px">

                        <div class="row">
                            <div class="col-sm-2">
                                {{otherMoney.itemName}}
                            </div>
                            <div class="col-sm-4">
                                {{otherMoney.addReduceReason}}
                            </div>
                            <div class="col-sm-2">
                                {{otherMoney.addReduceType}}{{otherMoney.quota}}
                                <span v-show="otherMoney.taxedAmount == '1'">(税后减额)</span>
                            </div>
                            <div class="col-sm-2">
                                批准人：{{otherMoney.approver}}
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row panel panel-body" style="padding: 10px">
                    <h4 align="right" style="color: red">
                        增减合计：{{amount.otheramountsincreaseordecrease.toFixed(2)}}</h4>
                    <h4 align="right" style="color: red">
                        增：+{{amount.otherincrease.toFixed(2)}}</h4>
                    <h4 align="right" style="color: red">
                        减：-{{amount.otherminus.toFixed(2)}}</h4>
                </div>
            </div>
        </div>
        <%--其它综合费--%>
        <div class="panel-group table-responsive">
            <div class="panel-group table-responsive panel panel-default" style="padding:0px 15px" v-cloak>
                <div class="row panel panel-heading" style="padding: 10px">
                    <div class="col-sm-2" style="text-align: left">
                        <h4>其它综合费 ({{otherComprehensiveFeeList.length}})</h4>
                    </div>
                </div>
                <div class="row panel-body">
                    <table width="100%" class="table table-striped table-bordered table-hover
                    table-de panel-heading border-bottom-style" style="margin-top: 10px">
                        <thead>
                        <%--遍历其他综合费--%>
                        <tr align="center" v-for="projectMaterial in otherComprehensiveFeeList">
                            <td width="10%" style="vertical-align: middle; text-align: center">
                                <%--瓦工 分类--%>
                                {{projectMaterial.skuDosageList[0].domainName}} <br>
                            </td>
                            <td width="90%">
                                <table align="center" width="100%">
                                    <tr style="height: 25px;">
                                        <%--大类目明细--%>
                                        <td width="50%"><%--功能区名称--%>
                                            <strong> {{projectMaterial.productName}} </strong>
                                        </td>
                                        <td width="20%">
                                            <span v-show="projectMaterial.skuDosageList[0].dosagePricingMode == 'foundationPileTotal'">
                                                基装增项总价 <br>
                                                {{totalAmount.baseloadrating1.toFixed(2)}} 元
                                            </span>
                                            <span v-show="projectMaterial.skuDosageList[0].dosagePricingMode == 'renovationFoundationPile'">
                                                装修工程总价占比 <br>
                                                {{totalAmount.renovationAmount.toFixed(2)}} 元
                                            </span>
                                            <span v-show="projectMaterial.skuDosageList[0].dosagePricingMode == 'fixedUnitPrice'">
                                                <input type="text" style="width:50px;height:100%;text-align: center;"
                                                       class="form-control"
                                                       v-model="projectMaterial.skuDosageList[0].lossDosage || 0"
                                                       readonly>
                                            </span>
                                        </td>
                                        <td width="20%">
                                            <span v-show="projectMaterial.skuDosageList[0].dosagePricingMode == 'fixedUnitPrice'">
                                                单 价：￥ {{projectMaterial.storeSalePrice}} /{{projectMaterial.materialUnit}}<br>
                                                合 计：<span class="text-red">￥ {{projectMaterial.storeSalePrice * projectMaterial.skuDosageList[0].lossDosage}}</span>
                                            </span>
                                            <%--基装增项总价--%>
                                            <span v-show="projectMaterial.skuDosageList[0].dosagePricingMode == 'foundationPileTotal'">
                                                占 比：{{projectMaterial.skuDosageList[0].projectProportion}} % <br>
                                                合 计：<span class="text-red">
                                                    ￥ {{((projectMaterial.skuDosageList[0].projectProportion || 0)/100 * (totalAmount.baseloadrating1 || 0)).toFixed(2)}}
                                                  </span>
                                            </span>
                                            <%--工程总价--%>
                                            <span v-show="projectMaterial.skuDosageList[0].dosagePricingMode == 'renovationFoundationPile'">
                                                占 比：{{projectMaterial.skuDosageList[0].projectProportion}} % <br>
                                                合 计：<span class="text-red">
                                                    ￥ {{((projectMaterial.skuDosageList[0].projectProportion || 0)/100 * (totalAmount.renovationAmount || 0)).toFixed(2)}}
                                                  </span>
                                            </span>
                                        </td>
                                    </tr>
                                    <tr style="height: 25px;">
                                        <td colspan="4">
                                            <%--定额描述--%>
                                            {{projectMaterial.quotaDescribe}}
                                        </td>
                                    </tr>
                                    <tr style="height: 25px;">
                                        <td colspan="4">设计备注：<span style="color:#ff9900">{{projectMaterial.designRemark}}</span>
                                        </td>
                                    </tr>
                                    <tr style="height: 25px;">
                                        <td colspan="4">审计备注：<span
                                                style="color:#FF3030">{{projectMaterial.auditRemark}}</span>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                        </thead>
                    </table>
                </div>
                <div class="row panel-body">
                    <h4 align="right" style="color: red">
                        其它综合费：+{{getOtherFee().toFixed(2)}}</h4>
                </div>
            </div>
        </div>
        <%--拆除工程增项--%>
        <div class="panel-group table-responsive">
            <div class="panel-group table-responsive panel panel-default" style="padding:0px 15px" v-cloak>
                <div class="row panel panel-heading" style="padding: 10px">
                    <div class="col-sm-2" style="text-align: left;">
                        <h4>拆除工程增项 ({{dismantleBaseinstallquotaList.length + dismantleBaseinstallCompFeeList.length +
                            dismantleOtherCompFeeList.length}})</h4>
                    </div>
                </div>

                <%--拆除基装定额--%>
                <div class="row panel panel-heading" style="padding: 10px">
                    <div class="col-sm-2" style="text-align: left">
                        拆除基装定额 ({{dismantleBaseinstallquotaList.length}})
                    </div>
                </div>
                <div class="row panel-body" v-show="dismantleBaseinstallquotaList.length > 0">
                    <table width="100%" class="table table-striped table-bordered table-hover
                    table-de panel-heading border-bottom-style" style="margin-top: 10px">
                        <thead>
                        <%--遍历拆除基装定额集合--%>
                        <tr align="center" v-for="projectMaterial in dismantleBaseinstallquotaList">
                            <td width="10%" style="vertical-align: middle; text-align: center">
                                <%--瓦工 分类--%>
                                {{projectMaterial.skuDosageList[0].domainName}} <br>
                            </td>
                            <td width="90%">
                                <table align="center" width="100%">
                                    <tr style="height: 25px;">
                                        <%--大类目明细--%>
                                        <td width="50%"><%--功能区名称--%>
                                            <strong> {{projectMaterial.productName}} </strong>
                                        </td>
                                        <td width="20%">
                                            <span v-show="projectMaterial.skuDosageList[0].dosagePricingMode == 'dismantleFoundationPile'">
                                                拆除基桩定额总价占比 <br>
                                                {{totalAmount.baseloadrating3.toFixed(2)}} 元
                                            </span>
                                            <span v-show="projectMaterial.skuDosageList[0].dosagePricingMode == 'demolitionProjectTotal'">
                                                拆除工程总价占比 <br>
                                                {{totalAmount.comprehensivefee4.toFixed(2)}} 元
                                            </span>
                                            <span v-show="projectMaterial.skuDosageList[0].dosagePricingMode == 'fixedUnitPrice'">
                                                <input type="text" style="width:50px;height:100%;text-align: center;"
                                                       class="form-control"
                                                       v-model="projectMaterial.skuDosageList[0].lossDosage" readonly>
                                            </span>
                                        </td>
                                        <td width="20%">
                                            <span v-show="projectMaterial.skuDosageList[0].dosagePricingMode == 'fixedUnitPrice'">
                                                单 价：￥ {{projectMaterial.storeSalePrice}} /{{projectMaterial.materialUnit}}<br>
                                                合 计：<span class="text-red">￥ {{(projectMaterial.storeSalePrice * projectMaterial.skuDosageList[0].lossDosage).toFixed(2)}}</span>
                                            </span>
                                            <span v-show="projectMaterial.skuDosageList[0].dosagePricingMode == 'dismantleFoundationPile'">
                                                占 比：{{projectMaterial.skuDosageList[0].projectProportion}} % <br>
                                                合 计：<span class="text-red">
                                                        ￥ {{((projectMaterial.skuDosageList[0].projectProportion || 0)/100 * (totalAmount.baseloadrating3 || 0)).toFixed(2)}}
                                                      </span>
                                            </span>
                                            <span v-show="projectMaterial.skuDosageList[0].dosagePricingMode == 'demolitionProjectTotal'">
                                                占 比：{{projectMaterial.skuDosageList[0].projectProportion}} % <br>
                                                合 计：<span class="text-red">
                                                        ￥ {{((projectMaterial.skuDosageList[0].projectProportion || 0)/100 * (totalAmount.comprehensivefee4 || 0)).toFixed(2)}}
                                                      </span>
                                            </span>
                                        </td>
                                    </tr>
                                    <tr style="height: 25px;">
                                        <td colspan="4">
                                            <%--定额描述--%>
                                            {{projectMaterial.quotaDescribe}}
                                        </td>
                                    </tr>
                                    <tr style="height: 25px;">
                                        <td colspan="4">设计备注：<span style="color:#ff9900">{{projectMaterial.designRemark}}</span>
                                        </td>
                                    </tr>
                                    <tr style="height: 25px;">
                                        <td colspan="4">审计备注：<span
                                                style="color:#FF3030">{{projectMaterial.auditRemark}}</span></td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                        </thead>
                    </table>
                </div>

                <%--拆除基装增项综合服务--%>
                <div class="row panel panel-heading" style="padding: 10px">
                    <div class="col-sm-3" style="text-align: left">
                        拆除基装增项综合服务 ({{dismantleBaseinstallCompFeeList.length}})
                    </div>
                </div>
                <div class="row panel-body" v-show="dismantleBaseinstallCompFeeList.length > 0">
                    <table width="100%" class="table table-striped table-bordered table-hover
                    table-de panel-heading border-bottom-style" style="margin-top: 10px">
                        <thead>
                        <%--遍历拆除基装定额集合--%>
                        <tr align="center" v-for="projectMaterial in dismantleBaseinstallCompFeeList">
                            <td width="10%" style="vertical-align: middle; text-align: center">
                                <%--瓦工 分类--%>
                                {{projectMaterial.skuDosageList[0].domainName}} <br>
                            </td>
                            <td width="90%">
                                <table align="center" width="100%">
                                    <tr style="height: 25px;">
                                        <%--大类目明细--%>
                                        <td width="50%"><%--功能区名称--%>
                                            <strong> {{projectMaterial.productName}} </strong>
                                        </td>
                                        <td width="20%">
                                        <span v-show="projectMaterial.skuDosageList[0].dosagePricingMode == 'dismantleFoundationPile'">
                                            拆除基桩定额总价占比 <br>
                                            {{totalAmount.baseloadrating3.toFixed(2)}} 元
                                        </span>
                                            <span v-show="projectMaterial.skuDosageList[0].dosagePricingMode == 'demolitionProjectTotal'">
                                            拆除工程总价占比 <br>
                                            {{totalAmount.comprehensivefee4.toFixed(2)}} 元
                                        </span>
                                        </td>
                                        <td width="20%">
                                        <span v-show="projectMaterial.skuDosageList[0].dosagePricingMode == 'fixedUnitPrice'">
                                            单 价：￥ {{projectMaterial.storeSalePrice}} /{{projectMaterial.materialUnit}}<br>
                                            合 计：<span class="text-red">￥ {{projectMaterial.storeSalePrice * projectMaterial.skuDosageList[0].lossDosage}}</span>
                                        </span>
                                            <span v-show="projectMaterial.skuDosageList[0].dosagePricingMode == 'dismantleFoundationPile'">
                                            占 比：{{projectMaterial.skuDosageList[0].projectProportion}} % <br>
                                            合 计：<span class="text-red">
                                                    ￥ {{((projectMaterial.skuDosageList[0].projectProportion || 0)/100 * (totalAmount.baseloadrating3 || 0)).toFixed(2)}}
                                                  </span>
                                        </span>
                                            <span v-show="projectMaterial.skuDosageList[0].dosagePricingMode == 'demolitionProjectTotal'">
                                            占 比：{{projectMaterial.skuDosageList[0].projectProportion}} % <br>
                                            合 计：<span class="text-red">
                                                    ￥ {{((projectMaterial.skuDosageList[0].projectProportion || 0)/100 * (totalAmount.comprehensivefee4 || 0)).toFixed(2)}}
                                                  </span>
                                        </span>
                                        </td>
                                    </tr>
                                    <tr style="height: 25px;">
                                        <td colspan="4">
                                            <%--定额描述--%>
                                            {{projectMaterial.quotaDescribe}}
                                        </td>
                                    </tr>
                                    <tr style="height: 25px;">
                                        <td colspan="4">设计备注：<span style="color:#ff9900">{{projectMaterial.designRemark}}</span>
                                        </td>
                                    </tr>
                                    <tr style="height: 25px;">
                                        <td colspan="4">审计备注：<span
                                                style="color:#FF3030">{{projectMaterial.auditRemark}}</span></td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                        </thead>
                    </table>
                </div>

                <%--拆除其它综合服务--%>
                <div class="row panel panel-heading" style="padding: 10px">
                    <div class="col-sm-2" style="text-align: left">
                        拆除其它综合服务 ({{dismantleOtherCompFeeList.length}})
                    </div>
                </div>
                <div class="row panel-body" v-show="dismantleOtherCompFeeList.length > 0">
                    <table width="100%" class="table table-striped table-bordered table-hover
                    table-de panel-heading border-bottom-style" style="margin-top: 10px">
                        <thead>
                        <%--遍历拆除基装定额集合--%>
                        <tr align="center" v-for="projectMaterial in dismantleOtherCompFeeList">
                            <td width="10%" style="vertical-align: middle; text-align: center">
                                <%--瓦工 分类--%>
                                {{projectMaterial.skuDosageList[0].domainName}} <br>
                            </td>
                            <td width="90%">
                                <table align="center" width="100%">
                                    <tr style="height: 25px;">
                                        <%--大类目明细--%>
                                        <td width="50%"><%--功能区名称--%>
                                            <strong> {{projectMaterial.productName}} </strong>
                                        </td>
                                        <td width="20%">
                                        <span v-show="projectMaterial.skuDosageList[0].dosagePricingMode == 'dismantleFoundationPile'">
                                            拆除基桩定额总价占比 <br>
                                            {{totalAmount.baseloadrating3.toFixed(2)}} 元
                                        </span>
                                            <span v-show="projectMaterial.skuDosageList[0].dosagePricingMode == 'demolitionProjectTotal'">
                                            拆除工程总价占比 <br>
                                            {{totalAmount.comprehensivefee4.toFixed(2)}} 元
                                        </span>
                                        </td>
                                        <td width="20%">
                                        <span v-show="projectMaterial.skuDosageList[0].dosagePricingMode == 'fixedUnitPrice'">
                                            单 价：￥ {{projectMaterial.storeSalePrice}} /{{projectMaterial.materialUnit}}<br>
                                            合 计：<span class="text-red">￥ {{projectMaterial.storeSalePrice * projectMaterial.skuDosageList[0].lossDosage}}</span>
                                        </span>
                                            <span v-show="projectMaterial.skuDosageList[0].dosagePricingMode == 'dismantleFoundationPile'">
                                            占 比：{{projectMaterial.skuDosageList[0].projectProportion}} % <br>
                                            合 计：<span class="text-red">
                                                    ￥ {{((projectMaterial.skuDosageList[0].projectProportion || 0)/100 * (totalAmount.baseloadrating3 || 0)).toFixed(2)}}
                                                  </span>
                                        </span>
                                            <span v-show="projectMaterial.skuDosageList[0].dosagePricingMode == 'demolitionProjectTotal'">
                                            占 比：{{projectMaterial.skuDosageList[0].projectProportion}} % <br>
                                            合 计：<span class="text-red">
                                                    ￥ {{((projectMaterial.skuDosageList[0].projectProportion || 0)/100 * (totalAmount.comprehensivefee4 || 0)).toFixed(2)}}
                                                  </span>
                                        </span>
                                        </td>
                                    </tr>
                                    <tr style="height: 25px;">
                                        <td colspan="4">
                                            <%--定额描述--%>
                                            {{projectMaterial.quotaDescribe}}
                                        </td>
                                    </tr>
                                    <tr style="height: 25px;">
                                        <td colspan="4">设计备注：<span style="color:#ff9900">{{projectMaterial.designRemark}}</span>
                                        </td>
                                    </tr>
                                    <tr style="height: 25px;">
                                        <td colspan="4">审计备注：<span
                                                style="color:#FF3030">{{projectMaterial.auditRemark}}</span></td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                        </thead>
                    </table>
                </div>
                <div class="row panel-body">
                    <h4 align="right" style="color: red">
                        拆除增项合计：+ {{amount.oldhousedemolition.toFixed(2)}}</h4>
                    <h4 align="right" style="color: red">
                        拆除基础定额：+ {{amount.baseloadrating3.toFixed(2)}}</h4>
                    <h4 align="right" style="color: red">
                        拆除基装增项综合服务：+ {{amount.comprehensivefee3.toFixed(2)}}</h4>
                    <h4 align="right" style="color: red">
                        拆除其它综合服务：+ {{amount.othercomprehensivefee3.toFixed(2)}}</h4>
                </div>
            </div>
        </div>
        <%--合计--%>
        <div class="panel-group table-responsive">
            <div class="panel-group table-responsive panel panel-default" style="padding:0px 15px" v-cloak>
                <div class="row panel panel-heading" style="padding: 10px">
                    <div class="col-sm-2" style="text-align: left">
                        <h4>合计</h4>
                    </div>
                </div>
                <div class="row panel-body">
                    <h4 align="center" style="color: red">
                        装修工程合计：{{getMealPrice().toFixed(2)}} +
                        拆除工程合计：{{amount.oldhousedemolition.toFixed(2)}}={{getProjectAmount().toFixed(2)}}</h4>
                </div>
            </div>
        </div>
    </div>
    <div class="modal-footer">
        <button type="button" data-dismiss="modal" class="btn">关闭</button>
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
                                    <td>{{customerContract.houseAddr}}</td>
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
                变更减项商品信息 &nbsp;&nbsp;<span style="color: red;font-weight:bold">合计：{{subMoney}}元</span>
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
                        <th>减少量</th>
                        <th>价格</th>
                        <th>设计备注</th>
                        <th>材料状态</th>
                        <th>原用量</th>
                        <th>现用量</th>
                        <th>合计</th>
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
                            <td>{{item.num | absNum}}{{item.materialUnit | unitFilter}}</td>
                            <td>{{item.price}}</td>
                            <td>{{item.designRemark}}</td>
                            <td><select disabled type="text" v-model="item.materialsStatus">
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
                            <td>{{item.originalDosage}}</td>
                            <td>{{item.lossDosage}}</td>
                            <template v-if="item.categoryCode=='PACKAGESTANDARD'">
                                <td>0元</td>
                            </template>
                            <template v-else>
                                <td>{{item.num *item.price}}元</td>
                            </template>
                        </tr>
                    </template>
                </table>
            </div>
        </div>
        <div class="ibox">
            <div class="ibox-heading">
                变更增项商品信息 &nbsp;&nbsp;<span style="color: red;font-weight:bold">合计：{{addMoney}}元</span>
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
                        <th>增加量</th>
                        <th>价格</th>
                        <th>设计备注</th>
                        <th>原用量</th>
                        <th>现用量</th>
                        <th>合计</th>
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
                            <td>{{item.num}}{{item.materialUnit |unitFilter}}</td>
                            <td>{{item.price}}</td>
                            <td>{{item.designRemark}}</td>
                            <td>{{item.originalDosage}}</td>
                            <td>{{item.lossDosage}}</td>
                            <template v-if="item.categoryCode=='PACKAGESTANDARD'">
                                <td>0元</td>
                            </template>
                            <template v-else>
                                <td>{{item.num *item.price}}元</td>
                            </template>
                        </tr>
                    </template>
                </table>
            </div>
        </div>

        <div class="ibox">
            <div class="ibox-heading">
                其他金额增减信息 &nbsp;&nbsp;<span style="color: red;font-weight:bold">合计：{{otherMoney}}元</span>
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
        <div class="ibox">
            <div class="ibox-heading">
                <span style="color: red;font-weight:bold">&nbsp;&nbsp;合计：{{addMoney+subMoney+otherMoney}}元</span>
            </div>
            <div class="ibox-content">
            </div>
        </div>
        <div class="ibox">
            <div class="ibox-content">
                <div class="form-group">
                    <label class="control-label col-sm-1">备注：</label>
                    <div class="col-sm-12">
                        <p v-if="remark1!=null">材料部备注：{{remark1}}</p>
                        <p v-if="remark2!=null">设计总监备注： {{remark2}}</p>
                        <p v-if="remark3!=null">审计备注： {{remark3}}</p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<%@include file="/WEB-INF/views/business/component/contractinfo.jsp" %>
<script src="/static/business/component/contractInfo.js"></script>
<script src="/static/business/customercontract/city.js"></script>
<script src="/static/business/changemange/changemange.js"></script>