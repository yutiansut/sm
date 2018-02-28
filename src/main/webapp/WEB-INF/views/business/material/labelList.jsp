<%@ page contentType="text/html;charset=UTF-8" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<div id="container" class="wrapper wrapper-content animated fadeInRight">
    <div id="breadcrumb">
        <bread-crumb :crumbs="breadcrumbs"></bread-crumb>
    </div>
    <!-- ibox start -->
    <div class="ibox">
        <div class="ibox-content">
            <div class="row">
                <form id="searchForm" @submit.prevent="query" class="form-horizontal">


                    <div class="col-md-3 form-group">
                        <label for="allSuppliers" class="control-label col-sm-3">关键字</label>
                        <div class="col-sm-8">
                            <input v-model="form.keyword" type="text" placeholder="sku名称/型号 " class="form-control"/>
                        </div>
                    </div>
                    <div class="col-md-3 form-group">
                        <label for="allStores" class="control-label col-sm-3">门店</label>
                        <div class="col-sm-8">
                            <select name="allStores" id="allStores" @change="fetchRegionSuppliers" class="form-control" v-model="form.allStoreCode">
                                <option value="{{item.code}}" v-for="item of allStores">{{item.name}}</option>
                            </select>
                        </div>
                    </div>
                    <div class="col-md-3 form-group">
                        <label for="allSuppliers" class="control-label col-sm-3">区域供应商</label>
                        <div class="col-sm-8">
                            <select name="allSuppliers" id="allSuppliers" @change="fetchSuppliers" class="form-control" v-model="form.allSupplierId">
                                <option value="">请选择</option>
                                <option value="{{item.id}}" v-for="item of allSuppliers">{{item.name}}</option>
                            </select>
                        </div>
                    </div>

                    <div class="col-md-3 form-group">
                        <label for="" class="control-label col-sm-3">商品供应商</label>
                        <div class="col-sm-8">
                            <select name="" id="" class="form-control" v-model="form.supplierId">
                                <option value="">请选择</option>
                                <option value="{{item.id}}" v-for="item of suppliers">{{item.name}}</option>
                            </select>
                        </div>
                    </div>

                    <div class="col-md-3 form-group">
                        <label for="allBrand" class="control-label col-sm-3">商品品牌</label>
                        <div class="col-sm-8">
                            <select v-model="form.brandId" class="form-control" id="allBrand">
                                <option value="">请选择商品品牌</option>
                                <option v-for="item in allBrand" :value="item.id">{{{item.name}}}</option>
                            </select>
                        </div>
                    </div>
                    <div class="col-md-3 form-group">
                        <label for="allStores" class="control-label col-sm-3">商品一级类目</label>
                        <div class="col-sm-8">
                            <select v-model="form.catalogUrl1" class="form-control" @change="fetchCategory2">
                                <option value="">请选择商品一级类目</option>
                                <option v-for="category in allCatalog1" :value="category.url">{{{category.name}}}</option>
                            </select>
                        </div>
                    </div>

                    <div class="col-md-3 form-group">
                        <label for="allStores" class="control-label col-sm-3">商品二级类目</label>
                        <div class="col-sm-8">
                            <select v-model="form.catalogUrl2" class="form-control">
                                <option value="">请选择商品二级类目</option>
                                <option v-for="category in allCatalog2" :value="category.url">{{{category.name}}}</option>
                            </select>
                        </div>
                    </div>

                    <div class="col-md-3 form-group">
                        <label for="allStores" class="control-label col-sm-3"></label>
                        <div class="col-sm-8">
                            <button id="searchBtn" type="submit" class="btn btn-block btn-outline btn-default" alt="搜索"
                                    title="搜索">
                                <i class="fa fa-search"></i>
                            </button>
                        </div>
                    </div>

                    <div class="col-md-3 form-group">
                    </div>
                    <div class="col-md-3 form-group">
                    </div>
                    <div class="col-md-3 form-group">
                    </div>
                    <div class="col-md-3 form-group">
                        <label for="download1" class="control-label col-sm-3"></label>
                        <div >
                            <button id="download1" type="button" class="btn  btn-danger" @click.prevent="downloadByType('STORE')">
                                批量下载标配标签
                            </button>
                        </div>
                    </div>
                    <div class="col-md-3 form-group">
                        <label for="download2" class="control-label col-sm-3"></label>
                        <div >
                            <button id="download2" type="button" class="btn  btn-primary" @click.prevent="downloadByType('UPGRADE')">
                                批量下载升级标签
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
</div>
<div id="priceModal" class="modal fade" tabindex="-1" data-width="800" v-cloak>
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal"
                aria-hidden="true">×
        </button>
        <h3 align="center">设置价格</h3>
    </div>
    <div class="modal-body">
        <div class="row">
            <div class="col-md-12">
                <div class="col-md-4 tab" @click="tab('aConfig')" :class="{active:current == 'aConfig' ? true:false}"
                     v-if="aShow == 1">套餐价
                </div>
                <div class="col-md-4 tab" @click="tab('bConfig')" :class="{active:current == 'bConfig' ? true:false}"
                     v-if="bShow == 1">升级价
                </div>
                <div class="col-md-4 tab" @click="tab('cConfig')" :class="{active:current == 'cConfig' ? true:false}"
                     v-if="cShow == 1">减项价
                </div>
            </div>

        </div>
        <div v-show="current == 'aConfig'" class="col-md-12 ">
            <div class="search">
                <div class="btn btn-primary fr" @click="createBtnClickHandler('UPGRADE')" style="margin:10px 0"
                     v-if="aControl == 1">新增
                </div>
            </div>
            <table id="aTable" width="100%" class="table table-striped table-bordered table-hover">
            </table>
        </div>

        <div v-show="current == 'bConfig'" class="col-md-12">
            <div class="search">
                <div class="btn btn-primary fr" @click="createBtnClickHandler('INCREASED')" style="margin:10px 0"
                     v-if="bControl == 1">新增
                </div>
            </div>
            <table id="bTable" width="100%" class="table table-striped table-bordered table-hover">
            </table>
        </div>

        <div v-show="current == 'cConfig'" class="col-md-12">
            <div class="search">
                <div class="btn btn-primary fr" @click="createBtnClickHandler('MINUS')" style="margin:10px 0"
                     v-if="cControl == 1">新增
                </div>
            </div>
            <table id="cTable" width="100%" class="table table-striped table-bordered table-hover">
            </table>
        </div>

    </div>
    <div class="modal-footer">
        <button type="button" @click="closeFrame" class="btn btn-primary">确定</button>
        <button type="button" data-dismiss="modal" class="btn">取消</button>
    </div>
</div>
<style>
    .border {
        border: 1px solid #ccc;
        padding: 10px;
    }

    .margin20 {
        margin-bottom: 20px;
    }

    .margin10 {
        margin-bottom: 10px;
    }

    .sku input,
    .sku label {
        padding: 6px 12px;
    }

    .tab {
        border: 1px solid #f3f3f4;
        text-align: center;
        font-size: 14px;
        line-height: 40px;
        cursor: pointer;
    }

    .tab:hover {
        background-color: #1ab394;
    }

    .tab.active {
        background-color: #1ab394;
    }
</style>
<script src="${ctx}/static/core/js/components/jquery.form.min.js"></script>
<script src="${ctx}/static/business/material/labelList.js"></script>