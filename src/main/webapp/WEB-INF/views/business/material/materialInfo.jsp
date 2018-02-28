<%--选材单信息jsp--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div id="materialInfoContainer" class="wrapper wrapper-content animated fadeInRight">
        <div id="contractInfoId">
            <%--客户信息--%>
            <contract-info :contract-code="customerContract.contractCode"></contract-info>
        </div>
    <div class="ibox">
        <div class="ibox-title">
            <div class="col-md-1">
                <h4>备注</h4>
            </div>
            <div class="col-md-11" style="text-align: right;padding-bottom:5px">
                <button type="button" class="btn btn-primary" v-if="pageType == 'select' || pageType == 'change'" @click="updateRemark('设计备注', customerContract)">
                    备注
                </button>
                <button type="button" class="btn btn-primary" v-if="pageType == 'audit'" @click="updateRemark('审计备注', customerContract)">
                    备注
                </button>
            </div>
        </div>
        <div class="ibox-content">
            <div class="row">
                    <div class="col-md-1">
                        设计备注:
                    </div>
                    <div class="col-md-11" style="color:#ff9900">
                        {{customerContract.designRemark}}
                    </div>
            </div>
        </div>
        <div class="ibox-content">
            <div class="row">
                <div class="col-md-1">
                    审计备注:
                </div>
                <div class="col-md-11" style="color:#FF3030">
                    {{customerContract.auditRemark}}
                </div>
            </div>
        </div>
    </div>
</div>