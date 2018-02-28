<%--旧房拆改工程组件--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<template id="oldHouseChangeTmpl">

    <div class="panel-group table-responsive">
        <div class="panel-group table-responsive panel panel-default" style="padding:0px 15px">
            <div class="row panel panel-heading" style="padding: 10px">
                <div class="col-sm-2" style="text-align: left;">
                    <h4>拆除工程增项 ({{dismantleBaseinstallquotaList.length + dismantleBaseinstallCompFeeList.length + dismantleOtherCompFeeList.length}})</h4>
                </div>
                <div class="col-sm-9" style="text-align: right" v-if="pageType == 'select'">
                    <button type="button" class="btn btn-primary" @click="addQuotaModel()">
                        添加
                    </button>
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
                                    <tr style="height: 25px;" >
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
                                                <input type="text" style="width:50px;height:100%;text-align: center;" class="form-control"
                                                       v-model="projectMaterial.skuDosageList[0].lossDosage" readonly>
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
                                        <td width="10%">
                                            <span v-if="pageType == 'select' || pageType == 'change'">
                                                <a class="links" href="javascript:;" @click="updateRemark('设计备注', projectMaterial)">设计备注</a>
                                            </span>
                                            <span v-if="pageType == 'audit'">
                                                <a class="links" href="javascript:;" @click="updateRemark('审计备注', projectMaterial)">审计备注</a>
                                            </span>
                                            <br>
                                            <a class="links" href="javascript:;" v-if="pageType == 'select'"
                                               @click="removeSku(dismantleBaseinstallquotaList, projectMaterial)">
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

            <%--拆除基装增项综合服务--%>
            <div class="row panel panel-heading" style="padding: 10px">
                <div class="col-sm-2" style="text-align: left">
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
                                        <span v-show="projectMaterial.skuDosageList[0].dosagePricingMode == 'fixedUnitPrice'">
                                            <input type="text" style="width:50px;height:100%;text-align: center;" class="form-control"
                                                   v-model="projectMaterial.skuDosageList[0].lossDosage" readonly>
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
                                    <td width="10%">
                                        <span v-if="pageType == 'select' || pageType == 'change'">
                                                <a class="links" href="javascript:;" @click="updateRemark('设计备注', projectMaterial)">设计备注</a>
                                            </span>
                                        <span v-if="pageType == 'audit'">
                                                <a class="links" href="javascript:;" @click="updateRemark('审计备注', projectMaterial)">审计备注</a>
                                            </span>
                                        <br>
                                        <a class="links" href="javascript:;" v-if="pageType == 'select'"
                                           @click="removeSku(dismantleBaseinstallCompFeeList, projectMaterial)">
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
                                    <td width="20%" >
                                        <span v-show="projectMaterial.skuDosageList[0].dosagePricingMode == 'dismantleFoundationPile'">
                                            拆除基桩定额总价占比 <br>
                                            {{totalAmount.baseloadrating3}} 元
                                        </span>
                                        <span v-show="projectMaterial.skuDosageList[0].dosagePricingMode == 'demolitionProjectTotal'">
                                            拆除工程总价占比 <br>
                                            {{totalAmount.comprehensivefee4}} 元
                                        </span>
                                        <span v-show="projectMaterial.skuDosageList[0].dosagePricingMode == 'fixedUnitPrice'">
                                            <input type="text" style="width:50px;height:100%;text-align: center;" class="form-control" v-model="projectMaterial.skuDosageList[0].lossDosage" readonly>
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
                                    <td width="10%">
                                        <span v-if="pageType == 'select' || pageType == 'change'">
                                                <a class="links" href="javascript:;" @click="updateRemark('设计备注', projectMaterial)">设计备注</a>
                                            </span>
                                        <span v-if="pageType == 'audit'">
                                                <a class="links" href="javascript:;" @click="updateRemark('审计备注', projectMaterial)">审计备注</a>
                                            </span>
                                        <br>
                                        <a class="links" href="javascript:;" v-if="pageType == 'select'"
                                           @click="removeSku(dismantleOtherCompFeeList, projectMaterial)">
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