<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<title>功能区管理</title>
<div id="container" class="wrapper wrapper-content">
    <!-- 面包屑 -->
    <div id="breadcrumb">
        <bread-crumb :crumbs="breadcrumbs"></bread-crumb>
    </div>
    <!-- ibox start -->
    <div class="ibox">
        <div class="ibox-content">

            <div class="row">
                <form id="searchForm" @submit.prevent="query">
                    <div class="col-md-2">
                        <div class="form-group">
                            <label class="sr-only" for="keyword">名称</label>
                            <input
                                    v-model="form.keyword"
                                    id="keyword"
                                    name="keyword"
                                    type="text"
                                    placeholder="名称/类型" class="form-control"/>
                        </div>
                    </div>

                    <div class="col-md-1">
                        <div class="form-group">
                            <button id="searchBtn" type="submit" class="btn btn-block btn-outline btn-default" alt="搜索"
                                    title="搜索">
                                <i class="fa fa-search"></i>
                            </button>
                        </div>
                    </div>
                </form>

                <div class="col-md-1">
                    <div class="form-group">
                        <button @click="createBtnClickHandler" id="createBtn" type="button"
                                class="btn btn-block btn-outline btn-primary">添加
                        </button>
                    </div>
                </div>
            </div>

            <table v-el:data-table id="dataTable" width="100%"
                   class="table table-striped table-bordered table-hover">

            </table>
        </div>
    </div>
    <!-- ibox end -->
</div>

<div id="creatOrEditModel" class="modal fade" tabindex="-1" data-width="600">
    <validator name="validation">
    <form name="banner" novalidate class="form-horizontal" role="form">
        <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
            <h3 align="center">功能区信息</h3>
        </div>

        <div class="modal-body">

            <div class="form-group" :class="{'has-error':$validation.includeDomainType.invalid && submitBtnClick}">
                <label for="includeDomainType" class="control-label col-sm-3">功能区类型：</label>
                <div class="col-sm-9">
                    <select id="includeDomainType" v-model="domaininfo.includeDomainType"
                            v-validate:include-domain-type="{
                                 required:{rule:true,message:'请选择功能区类型'}
                                 }"
                            v-el:include-domain-type
                            name="includeDomainType"
                            placeholder="请选择功能区类型"
                            class="form-control">
                        <option value="">请选择功能区类型</option>
                        <option value="储物间">储物间</option>
                        <option value="阳台">阳台</option>
                        <option value="过道">过道</option>
                        <option value="厨卫">厨卫</option>
                        <option value="厅室">厅室</option>
                        <option value="居室">居室</option>
                    </select>
                    <span v-cloak v-if="$validation.includeDomainType.invalid && submitBtnClick" class="help-absolute">
                      <span v-for="error in $validation.includeDomainType.errors">
                        {{error.message}} {{($index !== ($validation.includeDomainType.errors.length -1)) ? ',':''}}
                      </span>
                    </span>
                </div>
            </div>


            <div class="form-group" :class="{'has-error':$validation.domainName.invalid && submitBtnClick}">
                 <label for="domainName" class="control-label col-sm-3">功能区名称：</label>
                <div class="col-sm-9">
                    <input
                            id="domainName"
                            v-model="domaininfo.domainName"
                            v-validate:domain-name="{
                                 required:{rule:true,message:'请输入功能区名称'}
                                 }"
                            v-el:domain-name
                            name="domainName"
                            data-tabindex="1"
                            type="text" placeholder="请输入功能区名称" class="form-control">
                    <span v-cloak v-if="$validation.domainName.invalid && submitBtnClick" class="help-absolute">
                      <span v-for="error in $validation.domainName.errors">
                        {{error.message}} {{($index !== ($validation.domainName.errors.length -1)) ? ',':''}}
                      </span>
                    </span>
                </div>
            </div>

        </div>

        <div class="modal-footer">
            <button type="button" data-dismiss="modal" class="btn">关闭</button>
            <button :disabled="submitting" type="button" @click="saveOrUpdate" class="btn btn-primary">保存</button>
        </div>
    </form>
    </validator>
</div>
<!-- container end-->
<script src="${ctx}/static/business/domaininfo/list.js"></script>
