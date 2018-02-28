<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<title>特殊配置</title>
<link rel="stylesheet" href="/static/core/vendor/jstree/themes/default/style.css" />
<style>
    .help-absolute {
        margin-top: 0px;
    }
</style>
<!-- 面包屑 -->
<div id="container" class="wrapper wrapper-content">
    <div id="breadcrumb">
        <bread-crumb :crumbs="breadcrumbs"></bread-crumb>
    </div>
    <!-- ibox start -->
    <div class="ibox">
        <div class="ibox-content">
            <div class="row">
                <form id="searchForm">
                    <div class="col-md-12 text-left">
                        <div class="form-group" id="buttons">
                            <%-- <shiro:hasPermission name="organization:add"> --%>
                            <a id="createBtn" @click="createBtnClickHandler(!showBtn)"
                               class="btn btn-outline btn-primary"
                               v-cloak :disabled="!showBtn">新增特殊配置</a>
                            <%-- </shiro:hasPermission> --%>

                            <%-- <shiro:hasPermission name="organization:edit"> --%>
                            <a id="editBtn" @click="editBtn(!showBtn)"
                               class="btn btn-outline btn-primary" v-cloak :disabled="!showBtn">编辑</a>
                            <%-- </shiro:hasPermission> --%>

                            <%-- <shiro:hasPermission name="organization:delete"> --%>
                            <a id="deleteBtn" @click="deleteBtn"
                               class="btn btn-outline btn-danger" v-cloak v-show="showBtn">删除</a>
                            <%--  </shiro:hasPermission> --%>

                        </div>
                    </div>
                </form>
            </div>
            <div class="ibox-content">
                <div class="row">
                    <div class="col-md-5">
                        <div id="jstree"></div>
                    </div>

                </div>
            </div>
        </div>
    </div>
    <!-- ibox end -->
</div>
<!-- container end-->
<div id="modal" class="modal fade" tabindex="-1" data-width="760">
    <validator name="validation">
        <form name="createMirror" novalidate class="form-horizontal" role="form">
            <input type="hidden" id="methodId" name="id" value=" <%= request.getParameter("methodId") %>">

            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-hidden="true">×</button>
                <h3>新增/编辑特殊配置数据</h3>
            </div>
            <div class="modal-body">

                <!-- 编辑时  才显示 -->
                <div class="form-group" v-if="dict.parentName != null">
                    <label for="parentName" class="col-sm-2 control-label">自定义父类名称:
                    </label>
                    <div class="col-sm-8">
                        <input v-model="dict.parentName" id="parentName" name="parentName"
                               type="text" class="form-control" placeholder="{{dict.parentName}}"
                               disabled="disabled">
                    </div>
                </div>

                <div class="form-group" :class="{'has-error':$validation.name.invalid && submitBtnClick}">
                    <label for="name" class="col-sm-2 control-label">自定义属性名称: </label>
                    <div class="col-sm-8">
                        <input v-model="dict.name"
                               v-validate:name="{
	                                    required:{rule:true,message:'请输入自定义属性名称'},
	                                    maxlength:{rule:50,message:'自定义属性名称最长不能超过50个字符'}
	                                }"
                               maxlength="50" data-tabindex="1" id="name" name="name" type="text"
                               class="form-control" placeholder="自定义属性名称">
                        <span v-cloak v-if="$validation.name.invalid && submitBtnClick"
                              class="help-absolute">
							<span v-for="error in $validation.name.errors"> {{error.message}}
								{{($index !== ($validation.name.errors.length -1)) ? ',':''}}
							</span>
						</span>
                    </div>
                </div>

                <div class="form-group"
                     :class="{'has-error':$validation.code.invalid && submitBtnClick}">
                    <label for="code" class="col-sm-2 control-label">自定义属性编码: </label>
                    <div class="col-sm-8">
                        <input v-model="dict.code"
                               v-validate:code="{
	                                    required:{rule:true,message:'请输入自定义属性编码'},
	                                    maxlength:{rule:20,message:'自定义属性编码最长不能超过20个字符'}
	                                }"
                               maxlength="20" data-tabindex="2" id="code" name="code"
                               type="text" class="form-control" placeholder="自定义属性编码" :disabled="!isEdit">
                        <span v-cloak v-if="$validation.code.invalid && submitBtnClick"
                              class="help-absolute" >
							<span  v-for="error in $validation.code.errors">
								{{error.message}}
								{{($index !== ($validation.code.errors.length -1)) ? ',':''}}
							</span>
						</span>
                    </div>
                </div>

                <div class="form-group" :class="{'has-error':$validation.attrStatus.invalid && submitBtnClick}">
                    <label  class="col-sm-2 control-label">状态: </label>
                    <div class="col-sm-8">


                        <select v-model="dict.attrStatus" class="form-control">
                            <option value="">请选择</option>
                            <option v-for="option in methodStatus" v-bind:value="option.value">
                                {{ option.text }}
                            </option>
                        </select>

                        <span v-cloak v-if="$validation.attrStatus.invalid && submitBtnClick"
                              class="help-absolute">
							<span v-for="error in $validation.attrStatus.errors"> {{error.message}}
								{{($index !== ($validation.name.errors.length -1)) ? ',':''}}
							</span>
						</span>
                    </div>
                </div>
                <div class="form-group" :class="{'has-error':$validation.costRate.invalid && submitBtnClick}">
                    <label class="col-sm-2 control-label">手续费率: </label>
                    <div class="col-sm-8"  >
                        <input v-model="dict.costRate"  v-validate:cost-rate="['num']"  onkeypress="return (/[\d.]/.test(String.fromCharCode(event.keyCode)))"  type="text" placeholder="手续费率" class="form-control">
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
                <div class="form-group" :class="{'has-error':$validation.minCostfee.invalid && submitBtnClick}">
                    <label  class="col-sm-2 control-label">最低费用: </label>
                    <div class="col-sm-8">
                        <input v-model="dict.minCostfee" v-validate:min-costfee="['num']"  onkeypress="return (/[\d.]/.test(String.fromCharCode(event.keyCode)))" type="text" placeholder="最低费用" class="form-control">
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

                <div class="form-group" :class="{'has-error':$validation.maxCostFee.invalid && submitBtnClick}">
                    <label  class="col-sm-2 control-label">封顶费用: </label>
                    <div class="col-sm-8">
                        <input v-model="dict.maxCostFee" v-validate:max-costFee="['num']"  onkeypress="return (/[\d.]/.test(String.fromCharCode(event.keyCode)))"  type="text" placeholder="封顶费用" class="form-control">
                        </input>
                        <span v-cloak
                              v-if="$validation.maxCostFee.invalid && $validation.touched"
                              class="help-absolute">
                            <span  style="color: red"  v-for="error in $validation.maxCostFee.errors">
                                {{error.message}} {{($index !== ($validation.maxCostfee.errors.length -1)) ? ',':''}}
                            </span>
                        </span>
                    </div>
                </div>


                <!-- <div class="form-group"
                    :class="{'has-error':$validation.sort.invalid && submitBtnClick}">
                    <label for="sort" class="col-sm-2 control-label">排序</label>
                    <div class="col-sm-8">
                        <input v-model="org.sort"
                            v-validate:sort="{
                                        required:{rule:true,message:'请输入数量'},
                                        min:0,
                                        max:99999999.99
                                    }"
                            data-tabindex="4" type="number" maxlength="20" id="sort"
                            name="sort" type="text" class="form-control" placeholder="请输入排序值">
                        <span v-cloak v-if="$validation.sort.invalid && submitBtnClick"
                            class="help-absolute"> <span
                            v-for="error in $validation.sort.errors"> {{error.message}}
                                {{($index !== ($validation.sort.errors.length -1)) ? ',':''}} </span>
                        </span>
                    </div>
                </div> -->

                <!-- <div v-if="isEdit" class="form-group"
                    :class="{'has-error':$validation.name.invalid && submitBtnClick}">
                    <label for="type" class="col-sm-2 control-label">组织机构类型</label>
                    <div class="col-sm-3">
                        <select v-validate:type="{required:true}" v-model="org.type"
                            id="type" name="type" data-tabindex="3" class="form-control">
                            <option v-if="org.parentType=='BASE'" value="COMPANY">公司</option>
                            <option v-if="org.parentType!='DEPARTMENT'" value="DEPARTMENT">部门</option>
                            <option v-if="org.parentType=='DEPARTMENT'" value="GROUP">组</option>
                        </select>
                    </div>
                </div> -->

            </div>
            <div class="modal-footer">
                <button :disabled="disabled" type="button" data-dismiss="modal"
                        class="btn">取消</button>
                <button @click="submit" :disabled="disabled" type="button"
                        class="btn btn-primary">提交</button>
            </div>
        </form>
    </validator>
</div>


<script src="/static/core/vendor/iCheck/icheck.min.js"></script>

<script src="/static/core/vendor/sweetalert/sweetalert.min.js"></script>
<script src="/static/core/vendor/jstree/jstree.js"></script>
<script src="${ctx}/static/business/finance/finaPaymethod/particularConfig.js"></script>
