<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<template id="contractinfo">
    <div class="ibox">
        <div class="ibox-title">
            <div class="col-sm-1"><h4>客户信息</h4></div>
            <div class="col-sm-11" style="text-align: right;padding-bottom:5px" v-if="pageType == 'select' || pageType == 'change'">
                <button type="button" @click="modify" class="btn btn-primary">修改</button>
            </div>
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
    </div>
</template>