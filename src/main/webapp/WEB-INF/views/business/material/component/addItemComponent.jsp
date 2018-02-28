<%--增项组件--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<template id="addItemTmpl">
    <%--主材--%>
    <div class="panel-group table-responsive">
        <div class="panel-group table-responsive panel panel-default" style="padding:0px 15px">
            <div class="row panel panel-heading" style="padding: 10px">
                <div class="col-sm-2" style="text-align: left">
                    <h4>主材 ({{skuSum}})</h4>
                </div>
                <div class="col-sm-10" style="text-align: right"v-if="(pageType == 'select' || pageType == 'change') && catalogUrl != 'other'">
                    <button type="button" class="btn btn-primary" @click="addProdModel()">
                        添加
                    </button>
                </div>
            </div>
            <div class="row panel-body" >
                <table width="100%" class="table table-striped table-bordered table-hover
                    table-de panel-heading border-bottom-style" style="margin-top: 10px">
                    <thead>
                    <%--遍历二级分类 并且当二级分类下面有sku数据时,展示--%>
                        <tr align="center" v-for="catalog in lowerCatalogList"
                            v-show="catalog.projectMaterialList != null && catalog.projectMaterialList.length > 0">
                            <td width="10%" style="vertical-align: middle; text-align: center">
                                {{catalog.parent.name}} <br>
                                {{catalog.name}} <br>
                            </td>
                            <td width="90%">
                                <%--大类目明细--%>
                                <div class="table-bd" v-for="projectMaterial in catalog.projectMaterialList" >
                                    <%--选材!--%>
                                    <div v-if="!projectMaterial.backColorFlag" >
                                        <!--商品projectMaterial属性-->
                                        <div class="table-row clearfix" >
                                            <div class="item item-proname clearfix">
                                                <img class="goods-media pull-left" :src="projectMaterial.skuImagePath" alt="商品图" width="80" height="80" />
                                                <div class="item-inner pull-left">
                                                    <div class="goods-title _ellipsis" >
                                                        {{projectMaterial.productName}}
                                                    </div>
                                                    <div class="goods-item _ellipsis4">
                                                        属性: {{projectMaterial.attribute1}}<br>
                                                        属性: {{projectMaterial.attribute2}}<br>
                                                        属性: {{projectMaterial.attribute3}}<br>
                                                        规格: {{projectMaterial.skuSqec}}
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="item item-pay">
                                                <p>
                                                    <input type="text" class="input-num" v-model="projectMaterial.lossDosageAmount" readonly>
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
                                                <p>合 计：<span class="text-red">￥ {{projectMaterial.skuSumMoney.toFixed(2)}}</span></p>
                                            </div>
                                            <div class="item item-operation">
                                                <p v-if="pageType == 'select' || pageType == 'change'">
                                                    <a class="links" href="javascript:;" @click="addSkuDosageModel(catalog, projectMaterial)">添加用量</a>
                                                </p>
                                                <p>
                                                    <span v-if="pageType == 'select' || pageType == 'change'">
                                                        <a class="links" href="javascript:;" @click="updateRemark('设计备注', projectMaterial)">设计备注</a>
                                                    </span>
                                                    <span v-if="pageType == 'audit'">
                                                        <a class="links" href="javascript:;" @click="updateRemark('审计备注', projectMaterial)">审计备注</a>
                                                    </span>
                                                </p>
                                                <p v-if="pageType == 'select'">
                                                    <a class="links" href="javascript:;"
                                                       @click="removeSku(catalog.projectMaterialList, projectMaterial, false)">
                                                        移&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;除
                                                    </a>
                                                </p>
                                            </div>
                                        </div>
                                        <%--用量集合 dosageList--%>
                                        <!-- /.table-row -->
                                        <div class="table-row clearfix" v-for="skuDosage in projectMaterial.skuDosageList">
                                            <div class="item item-proname"  v-if="skuDosage.lossDosage != 0">
                                                <div class="item-inner">
                                                    <div class="goods-item clearfix">
                                                        <span>功能区：{{skuDosage.domainName}}</span>
                                                        <%--<span>损耗系数：{{skuDosage.lossFactor}}</span>--%>
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
                                                        <a href="#" class="pull-right" v-if="pageType == 'select' || pageType == 'change'"
                                                           @click="removeSkuDosage(projectMaterial, projectMaterial.skuDosageList, skuDosage)">删除
                                                        </a>
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
                                    <div v-if="projectMaterial.backColorFlag == 'change'" :class="backColorObj.changeBackColor">
                                        <!--商品projectMaterial属性-->
                                        <div class="table-row clearfix" >
                                            <div class="item item-proname clearfix">
                                                <img class="goods-media pull-left" :src="projectMaterial.skuImagePath" alt="商品图" width="80" height="80" />
                                                <div class="item-inner pull-left">
                                                    <div class="goods-title _ellipsis" >
                                                        {{projectMaterial.productName}}
                                                    </div>
                                                    <div class="goods-item _ellipsis4">
                                                        属性: {{projectMaterial.attribute1}}<br>
                                                        属性: {{projectMaterial.attribute2}}<br>
                                                        属性: {{projectMaterial.attribute3}}<br>
                                                        规格: {{projectMaterial.skuSqec}}
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="item item-pay">
                                                <p>
                                                    <input type="text" class="input-num" v-model="projectMaterial.lossDosageAmount" readonly>
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
                                                <p>合 计：<span class="text-red">￥ {{projectMaterial.skuSumMoney.toFixed(2)}}</span></p>
                                            </div>
                                            <div class="item item-operation">
                                                <p v-if="pageType == 'select' || pageType == 'change'">
                                                    <a class="links" href="javascript:;" @click="addSkuDosageModel(catalog, projectMaterial)">添加用量</a>
                                                </p>
                                                <p>
                                                    <span v-if="pageType == 'select' || pageType == 'change'">
                                                        <a class="links" href="javascript:;" @click="updateRemark('设计备注', projectMaterial)">设计备注</a>
                                                    </span>
                                                    <span v-if="pageType == 'audit'">
                                                        <a class="links" href="javascript:;" @click="updateRemark('审计备注', projectMaterial)">审计备注</a>
                                                    </span>
                                                </p>
                                                <p v-if="pageType == 'select'">
                                                    <a class="links" href="javascript:;"
                                                       @click="removeSku(catalog.projectMaterialList, projectMaterial, false)">
                                                        移&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;除
                                                    </a>
                                                </p>
                                            </div>
                                        </div>
                                        <%--用量集合 dosageList--%>
                                        <!-- /.table-row -->
                                        <div class="table-row clearfix" v-for="skuDosage in projectMaterial.skuDosageList">
                                            <div class="item item-proname" v-if="skuDosage.lossDosage != 0">
                                                <div class="item-inner">
                                                    <div class="goods-item clearfix">
                                                        <span>功能区：{{skuDosage.domainName}}</span>
                                                        <%--<span>损耗系数：{{skuDosage.lossFactor}}</span>--%>
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
                                                        <a href="#" class="pull-right" v-if="pageType == 'select' || pageType == 'change'"
                                                           @click="removeSkuDosage(projectMaterial, projectMaterial.skuDosageList, skuDosage)">删除
                                                        </a>
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

                                    <%--被打回!--%>
                                    <div v-if="projectMaterial.backColorFlag == 'turnBack'" :class="backColorObj.turnBackColor">
                                            <!--商品projectMaterial属性-->
                                            <div class="table-row clearfix" >
                                                <div class="item item-proname clearfix">
                                                    <img class="goods-media pull-left" :src="projectMaterial.skuImagePath" alt="商品图" width="80" height="80" />
                                                    <div class="item-inner pull-left">
                                                        <div class="goods-title _ellipsis" >
                                                            {{projectMaterial.productName}}
                                                        </div>
                                                        <div class="goods-item _ellipsis4">
                                                            属性: {{projectMaterial.attribute1}}<br>
                                                            属性: {{projectMaterial.attribute2}}<br>
                                                            属性: {{projectMaterial.attribute3}}<br>
                                                            规格: {{projectMaterial.skuSqec}}
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="item item-pay">
                                                    <p>
                                                        <input type="text" class="input-num" v-model="projectMaterial.lossDosageAmount" readonly>
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
                                                    <p>合 计：<span class="text-red">￥ {{projectMaterial.skuSumMoney.toFixed(2)}}</span></p>
                                                </div>
                                                <div class="item item-operation">
                                                    <p>
                                                    <span v-if="pageType == 'select' || pageType == 'change'">
                                                        <a class="links" href="javascript:;" @click="updateRemark('设计备注', projectMaterial)">设计备注</a>
                                                    </span>
                                                        <span v-if="pageType == 'audit'">
                                                        <a class="links" href="javascript:;" @click="updateRemark('审计备注', projectMaterial)">审计备注</a>
                                                    </span>
                                                    </p>
                                                </div>
                                            </div>
                                            <%--用量集合 dosageList--%>
                                            <!-- /.table-row -->
                                            <div class="table-row clearfix" v-for="skuDosage in projectMaterial.skuDosageList">
                                                <div class="item item-proname" v-if="skuDosage.lossDosage != 0">
                                                    <div class="item-inner">
                                                        <div class="goods-item clearfix">
                                                            <span>功能区：{{skuDosage.domainName}}</span>
                                                            <%--<span>损耗系数：{{skuDosage.lossFactor}}</span>--%>
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
                    <h4>定额 ({{baseInstallQuotaList.length + baseInstallComfeeList.length}})</h4>
                    基装定额 ({{baseInstallQuotaList.length}})
                </div>
                <div class="col-sm-10" style="text-align: right" v-if="pageType == 'select'">
                    <button type="button" class="btn btn-primary" @click="addQuotaModel()">
                        添加
                    </button>
                </div>
            </div>
            <div class="row panel-body"  v-show="baseInstallQuotaList.length > 0">
                <table width="100%" class="table table-striped table-bordered table-hover
                    table-de panel-heading border-bottom-style" style="margin-top: 10px">
                    <thead>
                    <%--遍历基装增项综合费--%>
                    <tr align="center" v-for="projectMaterial in baseInstallQuotaList">
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
                                    <td width="20%" >
                                        <input type="text" style="width:50px;height:100%;text-align: center;" class="form-control"
                                               v-model="projectMaterial.skuDosageList[0].lossDosage || 0" readonly>
                                        <a class="links" href="#" @click="updateDosage(projectMaterial.skuDosageList[0])"
                                           v-if="pageType == 'select'">
                                            修改数量
                                        </a>
                                    </td>
                                    <td width="20%">
                                    <span v-show="projectMaterial.skuDosageList[0].dosagePricingMode == 'fixedUnitPrice'">
                                        单 价：￥ {{projectMaterial.storeSalePrice}} /{{projectMaterial.materialUnit}}<br>
                                        合 计：<span class="text-red">￥ {{(projectMaterial.storeSalePrice * projectMaterial.skuDosageList[0].lossDosage).toFixed(2)}}</span>
                                    </span>
                                    </td>
                                    <td width="10%">
                                        <p>
                                            <span v-if="pageType == 'select' || pageType == 'change'">
                                                <a class="links" href="javascript:;" @click="updateRemark('设计备注', projectMaterial)">设计备注</a>
                                            </span>
                                            <span v-if="pageType == 'audit'">
                                                <a class="links" href="javascript:;" @click="updateRemark('审计备注', projectMaterial)">审计备注</a>
                                            </span>
                                        </p>
                                        <a class="links" href="javascript:;" v-if="pageType == 'select'"
                                           @click="removeSku(baseInstallQuotaList, projectMaterial, true)">
                                            移&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;除
                                        </a>
                                    </td>
                                </tr>
                                <tr style="height: 25px;">
                                    <td colspan="4">
                                        <%--定额描述--%>
                                        {{projectMaterial.quotaDescribe}}
                                    </td>
                                </tr>
                                <tr style="height: 25px;">
                                    <td colspan="4">设计备注：<span style="color:#ff9900">{{projectMaterial.designRemark}}</span></td>
                                </tr>
                                <tr style="height: 25px;">
                                    <td colspan="4">审计备注：<span style="color:#FF3030">{{projectMaterial.auditRemark}}</span></td>
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
                    基装增项综合费 ({{baseInstallComfeeList.length}})
                </div>
            </div>
            <div class="row panel-body" v-show="baseInstallComfeeList.length > 0">
                <table width="100%" class="table table-striped table-bordered table-hover
                    table-de panel-heading border-bottom-style" style="margin-top: 10px">
                    <thead>
                    <%--遍历基装增项综合费--%>
                    <tr align="center" v-for="projectMaterial in baseInstallComfeeList">
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
                                        <td width="20%" >
                                            <span v-show="projectMaterial.skuDosageList[0].dosagePricingMode == 'foundationPileTotal'">
                                                基装增项总价 <br>
                                                {{totalAmount.baseloadrating1}} 元
                                            </span>
                                            <%--<span v-show="projectMaterial.skuDosageList[0].dosagePricingMode == 'renovationFoundationPile'">
                                                装修工程总价占比 <br>
                                                {{totalAmount.renovationAmount}} 元
                                            </span>--%>
                                            <span v-show="projectMaterial.skuDosageList[0].dosagePricingMode == 'fixedUnitPrice'">
                                                <input type="text" style="width:50px;height:100%;text-align: center;" class="form-control"
                                                       v-model="projectMaterial.skuDosageList[0].lossDosage || 0" readonly>
                                                <a class="links" href="#" v-if="pageType == 'select'"
                                                   @click="updateDosage(projectMaterial.skuDosageList[0])">
                                                    修改数量
                                                </a>
                                            </span>
                                        </td>
                                        <td width="20%">
                                            <span v-show="projectMaterial.skuDosageList[0].dosagePricingMode == 'fixedUnitPrice'">
                                                单 价：￥ {{projectMaterial.storeSalePrice}} /{{projectMaterial.materialUnit}}<br>
                                                合 计：<span class="text-red">￥ {{(projectMaterial.storeSalePrice * projectMaterial.skuDosageList[0].lossDosage).toFixed(2)}}</span>
                                            </span>
                                            <%--基装增项总价--%>
                                            <span v-show="projectMaterial.skuDosageList[0].dosagePricingMode == 'foundationPileTotal'">
                                                占 比：{{projectMaterial.skuDosageList[0].projectProportion}} % <br>
                                                合 计：<span class="text-red">
                                                    ￥ {{((projectMaterial.skuDosageList[0].projectProportion || 0)/100 * (totalAmount.baseloadrating1 || 0)).toFixed(2)}}
                                                  </span>
                                            </span>
                                            <%--工程总价--%>
                                            <%--<span v-show="projectMaterial.skuDosageList[0].dosagePricingMode == 'renovationFoundationPile'">
                                                占 比：{{projectMaterial.skuDosageList[0].projectProportion}} % <br>
                                                合 计：<span class="text-red">
                                                    ￥ {{((projectMaterial.skuDosageList[0].projectProportion || 0)/100 * (totalAmount.renovationAmount || 0)).toFixed(2)}}
                                                  </span>
                                            </span>--%>
                                        </td>
                                        <td width="10%">
                                            <span v-if="pageType == 'select' || pageType == 'change'">
                                                <a class="links" href="javascript:;" @click="updateRemark('设计备注', projectMaterial)">设计备注</a>
                                            </span>
                                            <span v-if="pageType == 'audit'">
                                                <a class="links" href="javascript:;" @click="updateRemark('审计备注', projectMaterial)">审计备注</a>
                                            </span>
                                            <br>
                                            <a class="links" href="javascript:;" v-if="pageType == 'select'"
                                               @click="removeSku(baseInstallComfeeList, projectMaterial, true)">
                                                移&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;除
                                            </a>
                                        </td>
                                    </tr>
                                    <tr style="height: 25px;">
                                        <td colspan="4">
                                            <%--定额描述--%>
                                            {{projectMaterial.quotaDescribe}}
                                        </td>
                                    </tr>
                                    <tr style="height: 25px;">
                                        <td colspan="4">设计备注：<span style="color:#ff9900">{{projectMaterial.designRemark}}</span></td>
                                    </tr>
                                    <tr style="height: 25px;">
                                        <td colspan="4">审计备注：<span style="color:#FF3030">{{projectMaterial.auditRemark}}</span></td>
                                    </tr>
                                </table>
                        </td>
                    </tr>
                    </thead>
                </table>
            </div>
        </div>
    </div>
</template>