<%--其他综合费组件--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<style>
    .links{
        color: #4564ea;
        text-decoration: underline;
    }
    .links:hover {
        color: #ca1e30;
        text-decoration: underline;
    }
</style>
<template id="otherCompFeeTmpl">

    <%--定额--%>
    <div class="panel-group table-responsive">
        <div class="panel-group table-responsive panel panel-default" style="padding:0px 15px">
            <div class="row panel panel-heading" style="padding: 10px">
                <div class="col-sm-2" style="text-align: left">
                    <h4>其它综合费 ({{otherComprehensiveFeeList.length}})</h4>
                </div>
                <div class="col-sm-10" style="text-align: right" v-if="pageType == 'select'">
                    <button type="button" class="btn btn-primary" @click="addQuotaModel()">
                        添加
                    </button>
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
                                        <td width="20%" >
                                            <span v-show="projectMaterial.skuDosageList[0].dosagePricingMode == 'foundationPileTotal'">
                                                基装增项总价 <br>
                                                {{totalAmount.baseloadrating1}} 元
                                            </span>
                                            <span v-show="projectMaterial.skuDosageList[0].dosagePricingMode == 'renovationFoundationPile'">
                                                装修工程总价占比 <br>
                                                {{totalAmount.renovationAmount}} 元
                                            </span>
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
                                            <span v-show="projectMaterial.skuDosageList[0].dosagePricingMode == 'renovationFoundationPile'">
                                                占 比：{{projectMaterial.skuDosageList[0].projectProportion}} % <br>
                                                合 计：<span class="text-red">
                                                    ￥ {{((projectMaterial.skuDosageList[0].projectProportion || 0)/100 * (totalAmount.renovationAmount || 0)).toFixed(2)}}
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
                                               @click="removeSku(otherComprehensiveFeeList, projectMaterial)">
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