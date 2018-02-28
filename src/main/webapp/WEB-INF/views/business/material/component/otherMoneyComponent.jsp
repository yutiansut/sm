<%--其他金额增减组件--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<style>
    .pointSty{
        cursor: pointer;
    }
</style>
<template id="otherMoneyItemTmpl">
    <div class="panel-group table-responsive">
        <div class="panel-group table-responsive panel panel-default" style="padding:0px 15px">
            <div class="row panel panel-heading" style="padding: 10px">
                <div class="col-sm-5" style="text-align: left">
                    <h4>活动、优惠及其它金额增减 ({{otherMoneyList.length}})</h4><p style="color: red">(金额增减项不支持商品相关的金额增减，如商品缺失，请联系材料部添加)</p>
                </div>
                <div class="col-sm-7" style="text-align: right" v-if="pageType == 'select' || (pageType == 'change' && catalogUrl == 'other') || (pageType == 'change' && !catalogUrl)">
                    <button type="button" class="btn btn-primary" @click="addOtherMoneyModel()">
                        添加
                    </button>
                </div>
            </div>
            <div class="panel-body">
                <div v-for="otherMoney in otherMoneyList" style="padding-bottom: 5px">

                    <%--变更发生后,显示--%>
                    <div class="row"  v-if="pageType == 'change' && otherMoney.changeFlag == '1'" :class="backColorObj.changeBackColor">
                        <div class="col-sm-2">
                            {{otherMoney.itemName}}
                        </div>
                        <div class="col-sm-3">
                            {{otherMoney.addReduceReason}}
                        </div>
                        <div class="col-sm-1">
                            {{otherMoney.addReduceType}}{{otherMoney.quota}}
                        </div>
                        <div class="col-sm-2">
                            <span v-if="otherMoney.taxedAmount == 1">税后减额</span>
                        </div>
                        <div class="col-sm-2">
                            批准人：{{otherMoney.approver}}
                        </div>
                    </div>

                    <div class="row"  v-else>
                        <div class="col-sm-2">
                            {{otherMoney.itemName}}
                        </div>
                        <div class="col-sm-3">
                            {{otherMoney.addReduceReason}}
                        </div>
                        <div class="col-sm-1">
                            {{otherMoney.addReduceType}}{{otherMoney.quota}}
                        </div>
                        <div class="col-sm-2">
                            <span v-if="otherMoney.taxedAmount == 1" style="color: red">税后减额</span>
                        </div>
                        <div class="col-sm-2">
                            批准人：{{otherMoney.approver}}
                        </div>
                        <div class="col-sm-2" v-if="pageType == 'select'">
                            <a href="#" class="links" @click="removeOtherMoney(otherMoney)">移除</a>
                        </div>
                    </div>
                </div>
            </div>

        </div>
    </div>

</template>