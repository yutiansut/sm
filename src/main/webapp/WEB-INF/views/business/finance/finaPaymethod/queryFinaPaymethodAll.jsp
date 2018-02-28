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
                    <input type="hidden"  id="xinzhengHandlerId"  value="9999999999">

                    <div class="col-md-4 form-group" style="width: 180px">
                        <label class="sr-only" for="storeCode"></label>
                        <select v-model="form.storeCode"
                                id="storeCode"
                                name="storeCode"
                                class="form-control">
                            <option value="">请选择门店</option>
                            <option v-for="designer in allStores" :value="designer.code">{{designer.name}}</option>
                        </select>
                    </div>


                    <div class="col-md-2 form-group"  style="width: 80px">
                        <button id="searchBtn" type="submit"
                                class="btn btn-block btn-outline btn-default" alt="搜索"
                                title="搜索">
                            <i class="fa fa-search"></i>
                        </button>
                    </div>
                </form>

                <div class="col-md-9 text-right">
                    <div class="form-group">
                        <button @click="createBtnClickHandler" id="createBtnClickHandler" type="button"
                                class="btn btn-outline btn-primary">新增
                        </button>
                    </div>
                </div>
            </div>
            <table id="dataTable" width="100%"
                   class="table table-striped table-bordered table-hover">
            </table>
        </div>
    </div>
</div>





<div id="contractModal" class="modal fade" tabindex="-1">
    <validator name="validation">
        <form name="banner" novalidate class="form-horizontal" role="form">

            <input type="hidden" v-model="id">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                <h3 align="center">交款通用配置</h3>
            </div>

            <div class="modal-body">

                <div class="form-group">
                    <label class="control-label col-sm-3"><span style="color: red">*</span>支付款方式名称</label>
                    <div class="col-sm-9">
                        <input v-model="finaPaymethod.methodName"    v-validate:tag-name="{required:{rule:true,message:'支付款方式名称'},maxlength:{rule:50,message:'姓名最长不能超过50个字符'}}" type="text" placeholder="支付款方式名称" class="form-control">
                        </input>
                        <span v-cloak v-if="$validation.tagName.invalid && $validation.touched" class="help-absolute">
                            <span v-for="error in $validation.tagName.errors">
                                <span style="color: red">{{error.message}} {{($index !== ($validation.tagName.errors.length -1)) ? ',':''}}</span>

                            </span>
                        </span>
                    </div>
                </div>
                <div class="form-group"  :class="{'has-error':$validation.costRate.invalid && submitBtnClick}">
                    <label  class="control-label col-sm-3">手续费率：</label>
                    <div class="col-sm-9"  >
                        <input v-model="finaPaymethod.costRate"  v-validate:cost-rate="['num']"  onkeypress="return (/[\d.]/.test(String.fromCharCode(event.keyCode)))"  type="text" placeholder="手续费率" class="form-control">
                        </input>
                        <span v-cloak
                              v-if="$validation.costRate.invalid && $validation.touched"
                              class="help-absolute">
                            <span style="color: red"  v-for="error in $validation.costRate.errors">
                                {{error.message}} {{($index !== ($validation.costRate.errors.length -1)) ? ',':''}}
                            </span>
                        </span>
                    </div>
                </div>

                <div class="form-group">
                    <label  class="control-label col-sm-3">编号：</label>
                    <div class="col-sm-9" v-show="xinzeng == 9999999999">
                        <input v-model="finaPaymethod.methodCode" type="text" placeholder="编号"   v-validate:method-code="{required:{rule:true,message:'支付款方式编号'},maxlength:{rule:50,message:'编号不能超过50个字符'}}" type="text" placeholder="编号"   class="form-control">
                        </input>

                        <span v-cloak
                              v-if="$validation.methodCode.invalid && $validation.touched"
                              class="help-absolute">
                            <span  style="color: red"  v-for="error in $validation.methodCode.errors">
                                {{error.message}} {{($index !== ($validation.describtion.errors.length -1)) ? ',':''}}
                            </span>
                        </span>
                    </div>

                    <div class="col-sm-9"  v-show="xinzeng!= 9999999999" >
                        <input v-model="finaPaymethod.methodCode"  style="border: 1px solid #DDD; background-color: #F5F5F5; color:#ACA899;"  readonly="readonly"   type="text" placeholder="编号"   v-validate:method-code="{required:{rule:true,message:'支付款方式编号'},maxlength:{rule:50,message:'编号不能超过50个字符'}}" type="text" placeholder="编号"   class="form-control">
                        </input>
                        <div>{{finaPaymethod.methodCode.commeNum}}</div>
                        <span v-cloak
                              v-if="$validation.methodCode.invalid && $validation.touched"
                              class="help-absolute">
                            <span  style="color: red"  v-for="error in $validation.methodCode.errors">
                                {{error.message}} {{($index !== ($validation.describtion.errors.length -1)) ? ',':''}}
                            </span>
                        </span>
                    </div>
                </div>
                <div class="form-group">
                    <label  class="control-label col-sm-3">状态：</label>
                    <div class="col-sm-9">

                        <select v-model="finaPaymethod.methodStatus" class="form-control">
                            <option value="">请选择</option>
                            <option v-for="option in methodStatus" v-bind:value="option.value">
                                {{ option.text }}
                            </option>
                        </select>

                        <span v-cloak
                              v-if="$validation.describtion.invalid && $validation.touched"
                              class="help-absolute">
                            <span v-for="error in $validation.describtion.errors">
                                {{error.message}} {{($index !== ($validation.describtion.errors.length -1)) ? ',':''}}
                            </span>
                        </span>
                    </div>
                </div>
                <div class="form-group" :class="{'has-error':$validation.minCostfee.invalid && submitBtnClick}">
                    <label   class="control-label col-sm-3">最低费用：</label>
                    <div class="col-sm-9">
                        <input v-model="finaPaymethod.minCostfee" v-validate:min-costfee="['num']"  onkeypress="return (/[\d.]/.test(String.fromCharCode(event.keyCode)))" type="text" placeholder="最低费用" class="form-control">
                        </input>

                        <span v-cloak
                              v-if="$validation.minCostfee.invalid && $validation.touched"
                              class="help-absolute">
                            <span style="color: red" v-for="error in $validation.minCostfee.errors">
                                {{error.message}} {{($index !== ($validation.minCostfee.errors.length -1)) ? ',':''}}
                            </span>
                        </span>
                    </div>
                </div>

                <div class="form-group">
                    <label  class="control-label col-sm-3">交易类型：</label>
                    <div class="col-sm-9">

                        <select v-model="finaPaymethod.methodType" class="form-control">
                            <option value="">请选择</option>
                            <option v-for="option in methodType" v-bind:value="option.value">
                                {{ option.text }}
                            </option>
                        </select>


                        <span v-cloak
                              v-if="$validation.describtion.invalid && $validation.touched"
                              class="help-absolute">
                            <span v-for="error in $validation.describtion.errors">
                                {{error.message}} {{($index !== ($validation.describtion.errors.length -1)) ? ',':''}}
                            </span>
                        </span>
                    </div>
                </div>
                <div class="form-group">
                    <label  class="control-label col-sm-3">有无特殊配置：</label>
                    <div class="col-sm-9">

                        <select v-model="finaPaymethod.ifCustome" class="form-control">
                            <option value="1"  >是</option>
                            <option value="0" selected="selected">否</option>
                        </select>
                        <span v-cloak
                              v-if="$validation.ifCustome.invalid && $validation.touched"
                              class="help-absolute">
                            <span v-for="error in $validation.ifCustome.errors">
                                {{error.message}} {{($index !== ($validation.ifCustome.errors.length -1)) ? ',':''}}
                            </span>
                        </span>
                    </div>
                </div>
                <div class="form-group" :class="{'has-error':$validation.maxCostfee.invalid && submitBtnClick}">
                    <label  class="control-label col-sm-3">封顶费用：</label>
                    <div class="col-sm-9">
                        <input v-model="finaPaymethod.maxCostfee" v-validate:max-costfee="['num']"  onkeypress="return (/[\d.]/.test(String.fromCharCode(event.keyCode)))"  type="text" placeholder="封顶费用" class="form-control">
                        </input>
                        <span v-cloak
                              v-if="$validation.maxCostfee.invalid && $validation.touched"
                              class="help-absolute">
                            <span  style="color: red"  v-for="error in $validation.maxCostfee.errors">
                                {{error.message}} {{($index !== ($validation.maxCostfee.errors.length -1)) ? ',':''}}
                            </span>
                        </span>
                    </div>
                </div>
                <div class="form-group">
                    <label  class="control-label col-sm-3">试用阶段：</label>
                    <div class="col-sm-9">
                        <input v-model="finaPaymethod.ablestageTemplateCode" type="text" placeholder="试用阶段数据格式(,xx,xx,)" class="form-control">
                        </input>
                        <span v-cloak
                              v-if="$validation.describtion.invalid && $validation.touched"
                              class="help-absolute">
                            <span v-for="error in $validation.describtion.errors">
                                {{error.message}} {{($index !== ($validation.describtion.errors.length -1)) ? ',':''}}
                            </span>
                        </span>
                    </div>
                </div>

                <div class="form-group">
                    <span style="color: red">*</span>
                    <label  class="control-label col-sm-3">支持的门店：</label>
                    <div id='example-3'   >
                        <div   id="chackbox1" style="width:110px; float:right;"  v-for="storeCheckbox in storeinfo"  >
                        <input type="checkbox" id ="{{storeCheckbox.value}}"   v-model="finaPaymethod.storeIds"    v-bind:value="storeCheckbox.value"   v-validate:store-ids="{required:{rule:true,message:'必选门店'},maxlength:{rule:50,message:'编号不能超过50个字符'}}" type="text" placeholder="必选"    name="name" >
                            <label for="{{storeCheckbox.value}}" >  {{ storeCheckbox.text }}</label>
                         </input>
                        </div>
                        <span v-cloak
                              v-if="$validation.describtion.storeIds && $validation.touched"
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
                <button type="button" @click="insert"    class="btn btn-primary">保存</button>
            </div>
        </form>
    </validator>
</div>

<script src="/static/business/finance/finaPaymethod/queryFinapaymethodAll.js"></script>