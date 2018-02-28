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
    <div class="ibox">
        <div class="ibox-content">
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
                                    <td>
                                        {{customerContract.addressProvince}}{{customerContract.addressCity}}{{customerContract.addressArea}}{{customerContract.houseAddr}}
                                    </td>
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
                                                <p>合 计：<span class="text-red">￥ {{projectMaterial.skuSumMoney}}</span>
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
                                                <p>合 计：<span class="text-red">￥ {{projectMaterial.skuSumMoney}}</span>
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
                                                <p>合 计：<span class="text-red">￥ {{projectMaterial.skuSumMoney}}</span>
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
                                                    <img class="goods-media pull-left"
                                                         :src="projectMaterial.skuImagePath"
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
                        <h4 align="right" style="color: red">升级价：+{{totalAmount.upgradeitemprice}}</h4>
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
                                                                       v-model="projectMaterial.lossDosageAmount"
                                                                       readonly>
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
                                        合 计：<span class="text-red">￥ {{projectMaterial.storeSalePrice * projectMaterial.skuDosageList[0].lossDosage}}</span>
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
                            增项：+{{totalAmount.increment}}</h4>
                        <h4 align="right" style="color: red">
                            主材：+{{totalAmount.mainmaterial1}}</h4>
                        <h4 align="right" style="color: red">
                            基装定额：+{{totalAmount.baseloadrating1}}</h4>
                        <h4 align="right" style="color: red">
                            基装增项综合费：+{{totalAmount.comprehensivefee1}}</h4>
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
                                                                       v-model="projectMaterial.lossDosageAmount"
                                                                       readonly>
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
                            减项：-{{totalAmount.subtraction}}</h4>
                        <h4 align="right" style="color: red">
                            主材：-{{totalAmount.mainmaterial2}}</h4>
                        <h4 align="right" style="color: red">
                            基装定额：-{{totalAmount.baseloadrating2}}</h4>
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
                            增减合计：{{totalAmount.otheramountsincreaseordecrease}}</h4>
                        <h4 align="right" style="color: red">
                            增：+{{totalAmount.otherincrease}}</h4>
                        <h4 align="right" style="color: red">
                            减：-{{totalAmount.otherminus}}</h4>
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
                                                {{totalAmount.baseloadrating1}} 元
                                            </span>
                                                <span v-show="projectMaterial.skuDosageList[0].dosagePricingMode == 'renovationFoundationPile'">
                                                装修工程总价占比 <br>
                                                {{totalAmount.renovationAmount}} 元
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
                            其它综合费：+{{totalAmount.othercomprehensivefee.toFixed(2)}}</h4>
                    </div>
                </div>
            </div>
            <%--拆除工程增项--%>
            <div class="panel-group table-responsive">
                <div class="panel-group table-responsive panel panel-default" style="padding:0px 15px" v-cloak>
                    <div class="row panel panel-heading" style="padding: 10px">
                        <div class="col-sm-2" style="text-align: left;">
                            <h4>拆除工程增项 ({{dismantleBaseinstallquotaList.length + dismantleBaseinstallCompFeeList.length
                                +
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
                                                    {{totalAmount.baseloadrating3}} 元
                                                </span>
                                                <span v-show="projectMaterial.skuDosageList[0].dosagePricingMode == 'demolitionProjectTotal'">
                                                    拆除工程总价占比 <br>
                                                    {{totalAmount.comprehensivefee4}} 元
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
                                            {{totalAmount.baseloadrating3}} 元
                                        </span>
                                                <span v-show="projectMaterial.skuDosageList[0].dosagePricingMode == 'demolitionProjectTotal'">
                                            拆除工程总价占比 <br>
                                            {{totalAmount.comprehensivefee4}} 元
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
                                            {{totalAmount.baseloadrating3}} 元
                                        </span>
                                                <span v-show="projectMaterial.skuDosageList[0].dosagePricingMode == 'demolitionProjectTotal'">
                                            拆除工程总价占比 <br>
                                            {{totalAmount.comprehensivefee4}} 元
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
                            拆除增项合计：+ {{totalAmount.oldhousedemolition}}</h4>
                        <h4 align="right" style="color: red">
                            拆除基础定额：+ {{totalAmount.baseloadrating3}}</h4>
                        <h4 align="right" style="color: red">
                            拆除基装增项综合服务：+ {{totalAmount.comprehensivefee3}}</h4>
                        <h4 align="right" style="color: red">
                            拆除其它综合服务：+ {{totalAmount.othercomprehensivefee3}}</h4>
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
                            装修工程合计：{{totalAmount.totalRenovationWorks.toFixed(2)}} +
                            拆除工程合计：{{totalAmount.oldhousedemolition.toFixed(2)}}={{totalAmount.totalBudget.toFixed(2)}}</h4>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<%@include file="/WEB-INF/views/business/changemange/signin.jsp" %>
<%@include file="/WEB-INF/views/business/component/contractinfo.jsp" %>

<script src="/static/business/component/contractInfo.js"></script>
<script src="/static/business/changemange/originalmaterialpreview.js"></script>