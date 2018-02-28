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
                               placeholder="名称" class="form-control"/>
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
                <h3 align="center">套餐信息</h3>
            </div>

            <div class="modal-body">

                <div class="form-group" :class="{'has-error':$validation.mealName.invalid && $validation.touched}">
                    <label class="control-label col-sm-3"><span style="color: red">*</span>套餐名称：</label>
                    <div class="col-sm-9">
                        <input v-model="mealInfo.mealName" v-validate:meal-name="{required:{rule:true,message:'请输入姓名'},maxlength:{rule:10,message:'姓名最长不能超过10个字符'}}"
                               data-tabindex="1" type="text" placeholder="套餐名称" class="form-control">
                        <span v-cloak v-if="$validation.mealName.invalid && $validation.touched" class="help-absolute">
                            <span v-for="error in $validation.mealName.errors">
                                {{error.message}} {{($index !== ($validation.name.errors.length -1)) ? ',':''}}
                            </span>
                        </span>
                    </div>
                </div>

                <div v-if="isFlag=='1'" class="form-group" :class="{'has-error':$validation.mealSalePrice.invalid && $validation.touched}">
                    <label class="control-label col-sm-3"><span style="color: red">*</span>套餐售价：</label>
                    <div class="col-sm-9">
                        <input v-model="mealInfo.mealSalePrice"
                               v-validate:meal-sale-price="['num']" readonly
                               name="mealSalePrice" type="number" placeholder="套餐售价" class="form-control" onkeypress="return (/[\d.]/.test(String.fromCharCode(event.keyCode)))">
                        <span v-cloak v-if="$validation.mealSalePrice.invalid && $validation.touched" class="help-absolute" />
                        <span v-for="error in $validation.mealSalePrice.errors">
									{{error.message}} {{($index !== ($validation.mealSalePrice.errors.length -1)) ? ',':''}}
								  </span>
                        </span>
                    </div>
                </div>
                <div v-else class="form-group" :class="{'has-error':$validation.mealSalePrice.invalid && $validation.touched}">
                    <label class="control-label col-sm-3"><span style="color: red">*</span>套餐售价：</label>
                    <div class="col-sm-9">
                        <input v-model="mealInfo.mealSalePrice"
                               v-validate:meal-sale-price="['num']"
                               name="mealSalePrice" type="number" placeholder="套餐售价" class="form-control" onkeypress="return (/[\d.]/.test(String.fromCharCode(event.keyCode)))">
                        <span v-cloak v-if="$validation.mealSalePrice.invalid && $validation.touched" class="help-absolute" />
                        <span v-for="error in $validation.mealSalePrice.errors">
									{{error.message}} {{($index !== ($validation.mealSalePrice.errors.length -1)) ? ',':''}}
								  </span>
                        </span>
                    </div>
                </div>

                <div class="form-group" :class="{'has-error':$validation.validityDate.invalid && $validation.touched}">
                    <label for="validityDate" class="col-sm-3 control-label"><span style="color: red">*</span>有效期：</label>
                    <div class="col-sm-4" :class="{'has-error':$validation.validityDate.invalid && $validation.touched}">
                        <input v-model="mealInfo.validityDate"
                               v-validate:validity-date="{
                                    required:{rule:true,message:'请输入有效期起始时间'}
                                }"
                               v-el:validity-date
                               maxlength="50"
                               tabindex="4"
                               id="validityDate" name="validityDate" type="text" class="form-control datepicker"  placeholder="请输入有效期起始时间" readonly>
                        <span v-cloak v-if="$validation.validityDate.invalid && $validation.touched" class="help-absolute">
                          <span v-for="error in $validation.validityDate.errors">
                            {{error.message}} {{($index !== ($validation.validityDate.errors.length -1)) ? ',':''}}
                          </span>
                        </span>
                    </div>
                    <label for="expirationDate" class="col-sm-1 control-label">至</label>
                    <div class="col-sm-4" :class="{'has-error':$validation.expirationDate.invalid && $validation.touched}">
                        <input v-model="mealInfo.expirationDate"
                               v-validate:expiration-date="{
                                    required:{rule:true,message:'请输入有效期截止时间'}
                                }"
                               v-el:expiration-date
                               maxlength="50"
                               tabindex="5"
                               id="expirationDate" name="expirationDate" type="text" class="form-control datepicker" placeholder="请输入有效期截止时间" readonly>
                        <span v-cloak v-if="$validation.expirationDate.invalid && $validation.touched" class="help-absolute">
                          <span v-for="error in $validation.expirationDate.errors">
                            {{error.message}} {{($index !== ($validation.expirationDate.errors.length -1)) ? ',':''}}
                          </span>
                        </span>
                    </div>
                </div>
                <div v-if="isFlag=='1'" class="form-group" :class="{'has-error':$validation.storeCode.invalid && $validation.touched}">
                    <label class="control-label col-sm-3"><span style="color: red">*</span>门店：</label>
                    <div class="col-sm-9">
                        <select class="form-control" v-validate:store-code="{
                                    required:{rule:true,message:'请选择门店'}
                               }" v-model="mealInfo.storeCode" disabled>
                            <option value="">请选择门店</option>
                            <option :value="item.code" v-for="item in storeList">{{item.name}}</option>
                        </select>

                        <span v-cloak v-if="$validation.storeCode.invalid && $validation.touched"
                              class="help-absolute">
                        <span v-for="error in $validation.storeCode.errors">
							{{error.message}} {{($index !==
							($validation.storeCode.errors.length -1)) ? ',':''}}
                        </span>
					</span>
                    </div>
                </div>
                <div v-else class="form-group" :class="{'has-error':$validation.storeCode.invalid && $validation.touched}">
                    <label class="control-label col-sm-3"><span style="color: red">*</span>门店：</label>
                    <div class="col-sm-9">
                        <select class="form-control" v-validate:store-code="{
                                    required:{rule:true,message:'请选择门店'}
                               }" v-model="mealInfo.storeCode">
                            <option value="">请选择门店</option>
                            <option :value="item.code" v-for="item in storeList">{{item.name}}</option>
                        </select>

                        <span v-cloak v-if="$validation.storeCode.invalid && $validation.touched"
                              class="help-absolute">
                        <span v-for="error in $validation.storeCode.errors">
							{{error.message}} {{($index !==
							($validation.storeCode.errors.length -1)) ? ',':''}}
                        </span>
					</span>
                    </div>
                </div>
                <div class="form-group">
                    <label  class="control-label col-sm-3">备注：</label>
                    <div class="col-sm-9">
                        <input v-model="mealInfo.remark" type="text" placeholder="套餐备注" class="form-control"/>
                        <span v-cloak
                              v-if="$validation.remark.invalid && $validation.touched"
                              class="help-absolute">
                            <span v-for="error in $validation.remark.errors">
                                {{error.message}} {{($index !== ($validation.remark.errors.length -1)) ? ',':''}}
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
<script src="/static/business/material/mealInfoList.js"></script>