<%--sku 添加用量组件--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<template id="addUsage">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal"
                aria-hidden="true">×
        </button>
        <h3 class="modal-title" >添加用量</h3>
    </div>
    <div style="padding: 20px 0  20px 0">
        <validator name="validation">
            <form novalidate  class="form-horizontal" role="form">
                <div class="form-group"
                     :class="{'has-error':$validation.domainName.invalid && $validation.touched}">
                    <label class="col-sm-2 control-label">功能区：</label>
                    <div class="col-sm-6" style="margin-top:3px ">
                        <span v-for=" item in domainList">
                        <input v-model="submitData.domainName"
                               v-validate:domain-name="{required:{rule:true,message:'请选择功能区'}}"
                               type="checkbox" name="domainName"
                               :value="item.name"/>
                         {{item.name}}
                        </span>
                        <span v-cloak
                              v-if="$validation.domainName.invalid && $validation.touched"
                              class="help-absolute">
                                                            <span v-for="error in $validation.domainName.errors">
                                                                {{error.message}} {{($index !== ($validation.domainName.errors.length -1)) ? ',':''}}
                                                            </span>
                                                        </span>
                    </div>
                </div>
                <div v-if="flg" class="form-group"
                     :class="{'has-error':$validation.budgetDosage.invalid && $validation.touched}">
                    <label class="col-sm-2 control-label">用量：</label>
                    <div class="col-sm-6 ">
                        <input v-model="submitData.budgetDosage"
                               v-validate:budget-dosage="['positive2']"
                               type="text"
                               class="form-control"/>
                        <span v-cloak
                              v-if="$validation.budgetDosage.invalid && $validation.touched"
                              class="help-absolute">
                            <span v-for="error in $validation.budgetDosage.errors">
                                {{error.message}} {{($index !== ($validation.budgetDosage.errors.length -1)) ? ',':''}}
                            </span>
                        </span>
                    </div>
                </div>

                <div v-else class="form-group"
                     :class="{'has-error':$validation.budgetDosage.invalid && $validation.touched}">
                    <label class="col-sm-2 control-label">用量：</label>
                    <div class="col-sm-6 ">
                        <input v-model="submitData.budgetDosage"
                               v-validate:budget-dosage="['positive1']"
                               type="text"
                               class="form-control"/>
                        <span v-cloak
                              v-if="$validation.budgetDosage.invalid && $validation.touched"
                              class="help-absolute">
                            <span v-for="error in $validation.budgetDosage.errors">
                                {{error.message}} {{($index !== ($validation.budgetDosage.errors.length -1)) ? ',':''}}
                            </span>
                        </span>
                    </div>
                </div>


                <div class="form-group" v-if="convertUnitList!=null">
                    <label class="col-sm-2 control-label">计量单位转换：</label>
                    <div class="col-sm-6 ">
                        <select disabled="disabled" v-model="submitData.convertUnit"
                                class="form-control  ">
                            <option v-for="item in convertUnitList" :value="item.id">{{item.name}}</option>
                        </select>
                    </div>
                </div>

                <div class="form-group " hidden>
                    <label class="col-sm-2 control-label">损耗系数：</label>
                    <div class="col-sm-6 ">
                        <input readonly="true"
                               v-model="submitData.lossFactor"
                               type="text"
                               class="form-control "/>
                    </div>
                </div>
                <div class="form-group ">
                    <label class="col-sm-2 control-label">不含损耗用量：</label>
                    <div class="col-sm-6 ">
                        <input readonly="true" v-model="submitData.noLossDosage"
                               type="text"
                               class="form-control"/>
                    </div>
                </div>
                <div class="form-group ">
                    <label class="col-sm-2 control-label">含损耗用量：</label>
                    <div class="col-sm-6 ">
                        <input readonly="true" v-model="submitData.lossDosage"
                               type="text"
                               class="form-control"/>
                    </div>
                </div>
                <div class="form-group ">
                    <label class="col-sm-2 control-label">备注：</label>
                    <div class="col-sm-6 ">
                        <input v-model="submitData.dosageRemark"
                               type="text"
                               class="form-control"
                               placeholder="限输入10字"/>
                    </div>
                </div>

            </form>
        </validator>
    </div>
    <div class="modal-footer" style="text-align: center">
        <div class="form-group ">
            <button type="button" :disabled="submitting" @click="insert" class="btn btn-primary">确定
            </button>
            &nbsp;
            <button type="button" data-dismiss="modal" class="btn">取消</button>
        </div>
    </div>
</template>