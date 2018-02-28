<%--sku 价格列表组件--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<template id="prodSku">
    <!-- ibox start -->
    <div class="ibox">
        <div class="ibox-title">
            <div class="row">
                <form id="searchForm" @submit.prevent="query" class="form-horizontal">
                    <div class="row">
                        <div class="col-md-3 form-group">
                            <label  class="control-label col-sm-4">关键字:</label>
                            <div class="col-sm-8">
                                <input v-model="form.keyword" type="text" placeholder="sku名称/型号 " class="form-control"/>
                            </div>
                        </div>
                        <div class="col-md-3 form-group">
                            <label  class="control-label col-sm-5">商品一级类目:</label>
                            <div class="col-sm-7">
                                <select v-model="form.catalogUrl1" :disabled="flg1"  class="form-control" @change="fetchCategory2">
                                    <option value="">请选择商品一级类目</option>
                                    <option v-for="category in allCatalog1" :value="category.url">{{{category.name}}}</option>
                                </select>
                            </div>
                        </div>
                        <div class="col-md-3 form-group">
                            <label  class="control-label col-sm-5">商品二级类目:</label>
                            <div class="col-sm-7">
                                <select v-model="form.catalogUrl2" :disabled="flg2"  class="form-control">
                                    <option value="">请选择商品二级类目</option>
                                    <option v-for="category in allCatalog2" :value="category.url">{{{category.name}}}</option>
                                </select>
                            </div>
                        </div>
                        <div class="col-md-3 form-group">
                            <label for="allBrand" class="control-label col-sm-4">商品品牌:</label>
                            <div class="col-sm-8">
                                <select v-model="form.brandId" class="form-control" id="allBrand">
                                    <option value="">请选择商品品牌</option>
                                    <option v-for="item in allBrand" :value="item.id">{{{item.name}}}</option>
                                </select>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-md-10"></div>
                        <div class="col-md-1 form-group">
                            <button id="searchBtn" type="submit" class="btn btn-block btn-outline btn-default" alt="搜索"
                                    title="搜索">
                                <i class="fa fa-search"></i>
                            </button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
            <!-- <div class="columns columns-right btn-group pull-right"></div> -->
        <div class="ibox-content">
            <table id="dataTable" width="100%" class="table table-striped table-bordered table-hover"></table>
        </div>
    </div>
    <!-- ibox end -->
</template>