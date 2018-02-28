<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<head>
    <title>套餐选材</title>
    <link rel="stylesheet" href="/static/core/css/tab.css">
    <link href="/static/core/css/layout.css" rel="stylesheet" type="text/css"/>
    <style>
        .change-back-color {
            background-color: #bbddf9;
        }

        .turn-back-color {
            background-color: #BEBEBE;
        }
    </style>
</head>
<body id="app" class="fixed-sidebar full-height-layout gray-bg" v-cloak>
<div id="indexContainer" class="wrapper wrapper-content animated fadeInRight">
    <div id="breadcrumb">
        <bread-crumb :crumbs="breadcrumbs"></bread-crumb>
    </div>
    <!-- ibox start -->
    <div class="ibox">
        <div class="ibox-content">
            <div>
                <Tabs type="card" @click="clickEventOldHouse(this)">
                    <Tab-pane label="选材单信息" v-ref:customer>
                        <jsp:include page="materialInfo.jsp"/>
                    </Tab-pane>
                    <Tab-pane label="装修工程">
                        <jsp:include page="materialDecorationInfo.jsp"/>
                    </Tab-pane>
                    <Tab-pane label="旧房拆改工程" v-ref:son>
                        <old-house-change></old-house-change>
                    </Tab-pane>
                </Tabs>
            </div>

            <%--金额统计--%>
            <!-- 固定测导航栏 -->
            <div class="fixed-tool" id="sideBar">
                <div class="tool-item">
                    <i class="icon icon-tool01">icon</i>
                    <div class="tool-txt">预算合计</div>
                    <a href="javascript:void(0)" class="icon icon-tool02" @click="showTotalAmount(true)"
                       @blur="hideTotalAmount">icon</a>
                </div>
                <ul class="tool-div">
                    <li>
                        <p class="item-title"><span class="font16">预算合计：</span><span class="font22 text-red">¥{{totalAmount.totalBudget}}</span>
                        </p>
                        <p>装修工程合计：{{totalAmount.totalRenovationWorks}}</p>
                        <p>旧房折改工程合计：{{totalAmount.oldhousedemolition}}</p>
                    </li>
                    <li>
                        <p class="item-title"><span>套餐标配：</span><span>¥{{totalAmount.packagestandardprice}}</span></p>
                        <p>{{totalAmount.area}} X {{totalAmount.price}}</p>
                    </li>
                    <li>
                        <p class="item-title"><span>升级项：</span><span>¥{{totalAmount.upgradeitemprice}}</span></p>
                    </li>
                    <li>
                        <p class="item-title"><span>增 项：</span><span>¥{{totalAmount.increment}}</span></p>
                        <p>主材：{{totalAmount.mainmaterial1}}</p>
                        <p>基装定额：{{totalAmount.baseloadrating1}}</p>
                        <p>基装增项综合费：{{totalAmount.comprehensivefee1}}</p>
                    </li>
                    <li>
                        <p class="item-title"><span>减 项：</span><span>¥{{totalAmount.subtraction}}</span></p>
                        <p>主材：{{totalAmount.mainmaterial2}}</p>
                        <p>基装定额：{{totalAmount.baseloadrating2}}</p>
                    </li>
                    <li>
                        <p class="item-title">
                            <span>其它金额增减：</span><span>¥ {{totalAmount.otheramountsincreaseordecrease}}</span></p>
                        <p>增：{{totalAmount.otherincrease}}</p>
                        <p>减：{{totalAmount.otherminus}}</p>
                    </li>
                    <li>
                        <p class="item-title"><span>其它综合费：</span><span>¥ {{totalAmount.othercomprehensivefee}}</span>
                        </p>
                        <p>其它综合费用：{{totalAmount.othercomprehensivefee}}</p>
                    </li>
                    <li>
                        <p class="item-title"><span>旧房拆改工程：</span><span>¥{{totalAmount.oldhousedemolition}}</span></p>
                        <p>拆除基桩定额：{{totalAmount.baseloadrating3}}</p>
                        <p>拆除基装增减综合费：{{totalAmount.comprehensivefee3}}</p>
                        <p>拆除其他综合费：{{totalAmount.othercomprehensivefee3}} </p>
                    </li>
                </ul>
            </div>
        </div>
    </div>
    <!-- ibox end -->
</div>

<%--添加商品model--%>
<div id="skuModel" class="modal modal-dialog fade" tabindex="-1">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal"
                aria-hidden="true">×
        </button>
    </div>
    <div class="modal-body">
        <prod-sku :project-material="projectMaterial" :price-type="priceType"
                  :date="date" :catalog-url1="catalogUrl1" :catalog-url2="catalogUrl2" :page-type="pageType">
        </prod-sku>
    </div>
</div>

<%--添加商品sku用量--%>
<div id="addUsageModel" class="modal modal-dialog fade" tabindex="-1">
    <div class="modal-body">
        <add-usage :sku-code="skuCode" :price-type="priceType" :price="price"
                   :date="date" :catalog-url1="catalogUrl1" :catalog-url2="catalogUrl2"
                   :spec="spec" :project-material-id="projectMaterialId" :page-type="pageType">
        </add-usage>
    </div>
</div>

<%--备注弹框--%>
<div id="remarkModel" class="modal modal-dialog fade" tabindex="-1" data-width="760" data-keyboard=false>
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
        <h3>{{name}}</h3>
    </div>
    <div class="modal-body" style="text-align: center;margin-top: 5px">
            <textarea v-model="remark" maxlength="100" rows="5" cols="50"
                      @keyup="validateSub" @mouseout="validateSub"></textarea>
    </div>
    <div class="modal-footer">
        <button :disabled="disabled" type="button" data-dismiss="modal" class="btn btn-primary"
                @click="submit">确定
        </button>
        <button type="button" data-dismiss="modal" class="btn">取消</button>
    </div>
</div>

<%--修改定额数量弹框--%>
<div id="dosageModel" class="modal modal-dialog fade" tabindex="-1" data-width="760">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
        <h3>{{name}}</h3>
    </div>
    <div class="modal-body" style="text-align: center;">
        数量: <input v-model="lossDosage" type="number" min="1" @mouseout="validateSub" @keyup="validateSub"/>
    </div>
    <div class="modal-footer">
        <button :disabled="disabled" type="button" data-dismiss="modal" class="btn btn-primary"
                @click="dosageSubmit">确定
        </button>
        <button type="button" data-dismiss="modal" class="btn">取消</button>
    </div>
</div>

<%--修改项目弹框--%>
<div id="modifyModal" class="modal fade" tabindex="-1" data-width="1000">
    <validator name="validation" v-model="validation">
        <form name="banner" novalidate class="form-horizontal" role="form">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-hidden="true">×
                </button>
                <h3 align="center">添加装修选材单</h3>
            </div>
            <div class="modal-body">
                <div class="form-group">
                    <div class="col-sm-12">
                        <label for="contractCode" class="control-label col-sm-2"><span
                                style="color: red">*</span>项目编号</label>
                        <div class="col-sm-4">
                            <input v-model="contractCode" id="contractCode" type="text" placeholder="订单号"
                                   class="form-control" readonly>
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-sm-12">
                        <label for="customerName" class="control-label col-sm-2"><span
                                style="color: red">*</span>客户姓名</label>
                        <div class="col-sm-4">
                            <input v-model="customerName" v-el="customerName" id="customerName" type="text"
                                   placeholder="客户姓名"
                                   class="form-control" readonly>
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-sm-12">
                        <label for="customerMobile" class="control-label col-sm-2"><span
                                style="color: red">*</span>联系方式</label>
                        <div class="col-sm-4">
                            <input v-model="customerMobile" id="customerMobile" type="text" placeholder="联系方式"
                                   class="form-control" readonly>
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-sm-12">
                        <label for="secondContact" class="control-label col-sm-2">第二联系人</label>
                        <div class="col-sm-4">
                            <input v-model="secondContact" id="secondContact" type="text" placeholder="第二联系人"
                                   class="form-control">
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-sm-12"
                         :class="{'has-error':$validation.secondContactMobile.invalid && $validation.touched}">
                        <label for="secondContactMobile" class="control-label col-sm-2">第二联系人电话</label>
                        <div class="col-sm-4">
                            <input v-model="secondContactMobile" id="secondContactMobile" type="text"
                                   v-validate:second-contact-mobile="{mobile:{rule:true,message:'请输入合法的手机号'}}"
                                   placeholder="第二联系人电话"
                                   class="form-control">
                            <span v-cloak v-if="$validation.secondContactMobile.invalid && $validation.touched"
                                  class="help-absolute">
                            <span v-for="error in $validation.secondContactMobile.errors">
                                {{error.message}} {{($index !== ($validation.secondContactMobile.errors.length -1)) ? ',':''}}
                            </span>
                        </span>
                        </div>
                    </div>
                </div>

                <div class="form-group">
                    <div class="col-sm-12" :class="{'has-error':($validation.mealName.invalid && $validation.touched)}">
                        <label for="mealId" class="control-label col-sm-2">所选套餐：</label>
                        <div class="col-sm-4">
                            <select v-model="mealId" id="mealId" :disabled="pageType != 'select'"
                                    class="form-control" v-validate:meal-name="{required:true}">
                                <option value="">请选择套餐</option>
                                <option v-for="meal in meals" :value="meal.id">
                                    {{ meal.mealName }}
                                </option>
                            </select>
                            <div v-if="$validation.mealName.invalid && $validation.touched"
                                 class="help-absolute">
                                <span v-if="$validation.mealName.invalid">请选择套餐</span>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-sm-12" :class="{'has-error':($validation.layout.invalid && $validation.touched)}">
                        <label for="layout" class="control-label col-sm-2"><span style="color: red">*</span>户型</label>
                        <div class="col-sm-4">
                            <input v-model="layout" id="layout" type="text" placeholder="户型"
                                   class="form-control" v-validate:layout="{required:true}">
                            <div v-if="$validation.layout.invalid && $validation.touched"
                                 class="help-absolute">
                                <span v-if="$validation.layout.invalid">请填写户型</span>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-sm-12"
                         :class="{'has-error':($validation.buildArea.invalid && $validation.touched)}">
                        <label for="buildArea" class="control-label col-sm-2"><span
                                style="color: red">*</span>房屋面积</label>
                        <div class="col-sm-4">
                            <input v-model="buildArea" id="buildArea" type="text" placeholder="房屋面积"
                                   class="form-control" v-validate:build-area="{required:true}">
                            <div v-if="$validation.buildArea.invalid && $validation.touched"
                                 class="help-absolute">
                                <span v-if="$validation.buildArea.invalid">请填写房屋面积</span>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-sm-12"
                         :class="{'has-error':($validation.valuateArea.invalid && $validation.touched)}">
                        <label for="valuateArea" class="control-label col-sm-2"><span
                                style="color: red">*</span>计价面积</label>
                        <div class="col-sm-4">
                            <input v-model="valuateArea" id="valuateArea" type="text" placeholder="计价面积"
                                   class="form-control" v-validate:valuate-area="{required:true}"
                                   :readonly="pageType != 'select'">
                            <div v-if="$validation.valuateArea.invalid && $validation.touched"
                                 class="help-absolute">
                                <span v-if="$validation.valuateArea.invalid">请填写计价面积</span>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-sm-12">
                        <label class="col-sm-2 control-label">房屋状况</label>
                        <div class="col-sm-5">
                            <div class="col-sm-3">
                                <input type="radio" :value="1"
                                       v-model="houseCondition">
                                <label for="houseCondition">新房</label>
                            </div>
                            <div class="col-sm-3">
                                <input type="radio" id="houseCondition" :value="0"
                                       v-model="houseCondition">
                                <label for="houseCondition">旧房</label>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-sm-12">
                        <label class="col-sm-2 control-label">有无电梯</label>
                        <div class="col-sm-4">
                            <div class="col-sm-3">
                                <input type="radio" :value="1"
                                       v-model="elevator">
                                <label for="elevator">有</label>
                            </div>
                            <div class="col-sm-3">
                                <input type="radio" id="elevator" :value="0"
                                       v-model="elevator">
                                <label for="elevator">无</label>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-sm-12">
                        <label class="col-sm-2 control-label">房屋类型</label>
                        <div class="col-sm-5">
                            <div class="col-sm-3">
                                <input type="radio" name="houseType" :value="1"
                                       v-model="houseType">
                                <label for="houseType">复式</label>
                            </div>
                            <div class="col-sm-3">
                                <input type="radio" name="houseType" :value="2"
                                       v-model="houseType">
                                <label for="houseType">别墅</label>
                            </div>
                            <div class="col-sm-4">
                                <input type="radio" name="houseType" id="houseType" :value="3"
                                       v-model="houseType">
                                <label for="houseType">楼房平层</label>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-sm-12" :class="{'has-error':($validation.touched && $validation.addressProvince.invalid ||
							$validation.addressCity.invalid || $validation.addressArea.invalid)}">
                        <label for="provinceCode" class="col-sm-2 control-label">
                            <font color="red">*</font>房屋地址:
                        </label>
                        <div class="col-sm-3">
                            <select @change="selectChange" class="form-control provinceCode" id="provinceCode"
                                    name="provinceCode"
                                    v-model="provinceCode" required v-validate:address-province="{required:true}">
                                <option value="">省份</option>
                                <option :value="item.ProID" v-for="item of province" :key="$index">
                                    {{item.addressProvince}}
                                </option>
                            </select>
                            <div v-if="$validation.addressProvince.invalid && $validation.touched"
                                 class="help-absolute">
                                <span v-if="$validation.addressProvince.invalid">请选择省份</span>
                            </div>
                        </div>
                        <div class="col-sm-3">
                            <select name="cityCode" class="form-control cityCode"
                                    v-model="cityCode" v-validate:address-city="{required:true}">
                                <option value="">市</option>
                                <option :value="item.CityID" v-for="item of city" :key="$index"
                                        v-if="item.ProID == provinceCode">{{item.addressCity}}
                                </option>
                            </select>
                            <div v-if="$validation.addressCity.invalid && $validation.touched"
                                 class="help-absolute">
                                <span v-if="$validation.addressCity.invalid">请选择城市</span>
                            </div>
                        </div>
                        <div class="col-sm-3">
                            <select name="areaCode" class="form-control areaCode"
                                    v-model="areaCode" required v-validate:address-area="{required:true}">
                                <option value="">区</option>
                                <option :value="item.Id" v-for="item of district" :key="$index"
                                        v-if="item.CityID == cityCode">{{item.addressArea}}
                                </option>
                            </select>
                            <div v-if="$validation.addressArea.invalid && $validation.touched"
                                 class="help-absolute">
                                <span v-if="$validation.addressArea.invalid">请选择区县</span>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-sm-12"
                         :class="{'has-error':($validation.houseAddr.invalid && $validation.touched)}">
                        <label for="houseAddr" class="control-label col-sm-2"><span
                                style="color: red">*</span>详细地址</label>
                        <div class="col-sm-4">
                            <input v-model="houseAddr" id="houseAddr" type="text" placeholder="详细地址"
                                   class="form-control" v-validate:house-addr="{required:true}">
                            <div v-if="$validation.houseAddr.invalid && $validation.touched"
                                 class="help-absolute">
                                <span v-if="$validation.houseAddr.invalid">请填写详细地址</span>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-sm-12">
                        <label for="activityName" class="control-label col-sm-2">活动名称</label>
                        <div class="col-sm-4">
                            <input v-model="activityName" id="activityName" type="text" placeholder="活动名称"
                                   class="form-control">
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" @click="save" class="btn btn-primary">保存</button>
            </div>
        </form>
    </validator>
</div>

<%--添加其他金额model--%>
<div id="otherFeeModel" class="modal modal-dialog fade" tabindex="-1" data-width="760">
    <validator name="validation">
        <form name="user" novalidate class="form-horizontal" role="form">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                <h3>活动、优惠及其它金额增减</h3>
            </div>
            <div class="modal-body">

                <div class="form-group" :class="{'has-error':$validation.itemName.invalid &&  $validation.touched}">
                    <label for="CatalogType" class="control-label col-sm-3">增减类型：</label>
                    <div class="col-sm-5">
                        <select v-model="itemName" class="form-control"
                                v-validate:item-name="{required:{rule:true,message:'请选择增减类型'}}">
                            <option value="">请选择增减类型</option>
                            <option v-for="type in types" v-bind:value="type.name">{{type.name}}</option>
                            <span v-cloak v-if="$validation.itemName.invalid &&  $validation.touched"
                                  class="help-absolute">
                    <span v-for="error in $validation.itemName.errors">
                    {{error.message}} {{($index !== ($validation.itemName.errors.length -1)) ? ',':''}}
                    </span>
                    </span>
                        </select>
                    </div>
                </div>

                <div class="form-group" :class="{'has-error':$validation.addCause.invalid && $validation.touched}">
                    <label class="control-label col-sm-3">增加原因：</label>
                    <div class="col-sm-6">
                    <textarea
                            v-validate:add-cause="{required:{rule:true,message:'请输入增加原因'}}"
                            v-model="addReduceReason" type="text" placeholder="请输入增加原因" class="form-control"></textarea>
                        <span v-cloak v-if="$validation.addCause.invalid && $validation.touched" class="help-absolute">
                    <span v-for="error in $validation.addCause.errors">
                    {{error.message}} {{($index !== ($validation.addCause.errors.length -1)) ? ',':''}}
                    </span>
                    </span>
                    </div>
                </div>
                <div class="form-group" :class="{'has-error':$validation.addReduceType.invalid && $validation.touched}">
                    <label class="control-label col-sm-3">类型：</label>
                    <input type="radio" v-model="addReduceType" :value="1">增加金额<br/>
                    <input type="radio" v-model="addReduceType" :value="0">减少金额
                </div>
                <div class="form-group" v-if="showCheckbox">
                    <label class="control-label col-sm-3">类型：</label>
                    <input type="checkbox" v-model="taxedAmount" value="1">是否税后减额
                </div>
                <div class="form-group" :class="{'has-error':$validation.quota.invalid && $validation.touched}">
                    <label class="control-label col-sm-3">额度：</label>
                    <div class="col-sm-6">
                        <input
                                v-validate:quota="['num']"
                                v-model="quota" type="number" placeholder="请输入额度" class="form-control">
                        <span v-cloak v-if="$validation.quota.invalid && $validation.touched" class="help-absolute">
                    <span v-for="error in $validation.quota.errors">
                        {{error.message}} {{($index !== ($validation.quota.errors.length -1)) ? ',':''}}
                    </span>
                </span>
                    </div>
                </div>
                <div class="form-group">
                    <label class="control-label col-sm-3">批准人：</label>
                    <div class="col-sm-6">
                        <input v-model="approver" type="text" placeholder="请输入批准人" class="form-control">
                        </span>
                    </div>
                </div>
                <div style="text-align: center">
                    <span style="color: red">(金额增减项不支持商品相关的金额增减，如商品缺失，请联系材料部添加)</span>
                </div>
            </div>
            <div class="modal-footer">
                <button :disabled="disabled" type="button" data-dismiss="modal" class="btn">关闭</button>
                <button :disabled="disabled" type="button" @click="saveOtherMoney" class="btn btn-primary">保存</button>
            </div>
        </form>
    </validator>
</div>

<%--添加定额model--%>
<div id="projectIntemModel" class="modal modal-dialog fade" tabindex="1">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal"
                aria-hidden="true">×
        </button>
    </div>
    <div class="modal-body">
        <project-intem :contract-code="contractCode" :project-intem-mold="projectIntemMold"
                       :payment-time="paymentTime">
        </project-intem>
    </div>
</div>


<%--子组件的jsp和js都放在首页,放在子tab页中,不可行!--%>
<%--引入子组件jsp--%>
<%@include file="component/packageStandardComponent.jsp" %>
<%@include file="component/upgradeItemComponent.jsp" %>
<%@include file="component/addItemComponent.jsp" %>
<%@include file="component/reduceItemComponent.jsp" %>
<%@include file="component/otherMoneyComponent.jsp" %>
<%@include file="component/otherCompFeeComponent.jsp" %>
<%@include file="component/oldHouseChangeComponent.jsp" %>

<%--引入 添加商品弹出页--%>
<%@include file="component/skuPriceList.jsp" %>
<%--引入 添加sku用量弹出页--%>
<%@include file="component/addUsage.jsp" %>
<%--引入 客户信息页--%>
<%@include file="../component/contractinfo.jsp" %>
<%--引入 添加定额页--%>
<%@include file="projectIntem.jsp" %>


<script src="${ctx}/static/core/js/components/tab.js"></script>
<%--引入6个组件js--%>
<script src="/static/business/material/component/packageStandardComponent.js"></script>
<script src="/static/business/material/component/upgradeItemComponent.js"></script>
<script src="/static/business/material/component/addItemComponent.js"></script>
<script src="/static/business/material/component/reduceItemComponent.js"></script>
<script src="/static/business/material/component/otherMoneyComponent.js"></script>
<script src="/static/business/material/component/otherCompFeeComponent.js"></script>
<script src="/static/business/material/component/oldHouseChangeComponent.js"></script>

<%--添加商品--%>
<script src="/static/business/material/component/skuPriceList.js"></script>
<%--添加sku用量--%>
<script src="/static/business/material/component/addUsage.js"></script>
<%--客户信息js--%>
<script src="/static/business/component/contractInfo.js"></script>
<%--省市区--%>
<script src="/static/business/customercontract/city.js"></script>
<%--引入 添加定额js--%>
<script src="/static/business/material/projectIntem.js"></script>


<%--选材首页js 放在最后!--%>
<script src="${ctx}/static/business/material/materialIndex.js"></script>
</body>