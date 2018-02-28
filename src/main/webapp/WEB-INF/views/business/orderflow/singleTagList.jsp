<%@ page contentType="text/html;charset=UTF-8" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<head>
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

                    <div class="col-md-2 form-group">
                        <input v-model="form.keyword" type="text"
                               placeholder="串单名称/操作人/描述" class="form-control"/>
                    </div>
                    <div class="col-md-2 form-group">
                        <button id="searchBtn" type="submit"
                                class="btn btn-block btn-outline btn-default" alt="搜索"
                                title="搜索">
                            <i class="fa fa-search"></i>
                        </button>
                    </div>
                </form>

                <div class="col-md-7 text-right">
                    <%--<shiro:hasPermission name="dict:edit">--%>
                        <div class="form-group">
                            <button @click="createBtnClickHandler" id="createBtnClickHandler" type="button"
                                    class="btn btn-outline btn-primary">新增
                            </button>
                        </div>
                    <%--</shiro:hasPermission>--%>
                </div>
            </div>
            <!-- <div class="columns columns-right btn-group pull-right"></div> -->
            <table id="dataTable" width="100%"
                   class="table table-striped table-bordered table-hover">
            </table>
        </div>
    </div>
    <!-- ibox end -->
</div>

<div id="contractModal" class="modal fade" tabindex="-1">
    <validator name="validation">
        <form name="banner" novalidate class="form-horizontal" role="form">
            <input type="hidden" v-model="id">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                <h3 align="center">串单信息</h3>
            </div>

            <div class="modal-body">

                <div class="form-group" :class="{'has-error':$validation.mealName.invalid && $validation.touched}">
                    <label class="control-label col-sm-3"><span style="color: red">*</span>标签名称：</label>
                    <div class="col-sm-9">
                        <input v-model="singleTag.tagName" v-validate:tag-name="{required:{rule:true,message:'请输入标签名称'},maxlength:{rule:50,message:'姓名最长不能超过50个字符'}}"
                               data-tabindex="1" type="text" placeholder="标签名称" class="form-control">
                        <span v-cloak v-if="$validation.tagName.invalid && $validation.touched" class="help-absolute">
                            <span v-for="error in $validation.tagName.errors">
                                {{error.message}} {{($index !== ($validation.tagName.errors.length -1)) ? ',':''}}
                            </span>
                        </span>
                    </div>
                </div>

                <div class="form-group">
                    <label  class="control-label col-sm-3">描述：</label>
                    <div class="col-sm-9">
                        <textarea v-model="singleTag.describtion" type="text" placeholder="描述" class="form-control">
                        </textarea>
                        <span v-cloak
                              v-if="$validation.describtion.invalid && $validation.touched"
                              class="help-absolute">
                            <span v-for="error in $validation.describtion.errors">
                                {{error.message}} {{($index !== ($validation.describtion.errors.length -1)) ? ',':''}}
                            </span>
                        </span>
                    </div>
                </div>


            </div>

            <div class="modal-footer" style="text-align: center;padding-top:30px;padding-bottom:30px;" >
                <button type="button" data-dismiss="modal" class="btn">关闭</button>
                <button type="button" @click="insert" class="btn btn-primary">保存</button>
            </div>
        </form>
    </validator>
</div>
<%--<%@include file="/WEB-INF/views/admin/components/select.jsp" %>--%>
<script src="/static/admin/vendor/webuploader/webuploader.js"></script>
<script src="/static/business/orderflow/singleTagList.js"></script>