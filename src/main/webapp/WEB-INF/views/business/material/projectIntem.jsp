<%--sku 定额分类列表组件--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<template id="projectIntem">
    <!-- ibox start -->
    <div class="ibox">
        <div class="ibox-content">
            <div class="row">
                <form id="searchForm" @submit.prevent="query" class="form-horizontal">
                    <div class="col-md-3 form-group">
                        <label  class="control-label col-sm-3">关键字</label>
                        <div class="col-sm-8">
                            <input v-model="form.keyword" type="text" placeholder="定额名称" class="form-control"/>
                        </div>
                    </div>
                    <div class="col-md-3 form-group">
                        <label  class="control-label col-sm-3">定额分类</label>
                        <div class="col-sm-8">
                            <select v-model="form.projectIntemTypeId"  class="form-control">
                                <option value="">请选择定额分类</option>
                                <option v-for="projectIntemType in projectIntemTypes" :value="projectIntemType.id">{{{projectIntemType.projectIntemTypeName}}}</option>
                            </select>
                        </div>
                    </div>
                    <div class="col-md-3 form-group">
                        <label  class="control-label col-sm-3"></label>
                        <div class="col-sm-8">
                            <button id="searchBtn" type="submit" class="btn btn-block btn-outline btn-default" alt="搜索"
                                    title="搜索">
                                <i class="fa fa-search"></i>
                            </button>
                        </div>
                    </div>

                </form>
            </div>
            <!-- <div class="columns columns-right btn-group pull-right"></div> -->
            <table id="dataTable" width="100%" class="table table-striped table-bordered table-hover">
            </table>
        </div>
    </div>
    <!-- ibox end -->
</template>