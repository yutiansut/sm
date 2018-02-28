<%@ page contentType="text/html;charset=UTF-8" %>
<div id="container" class="wrapper wrapper-content animated fadeInRight">
    <div id="breadcrumb">
        <bread-crumb :crumbs="breadcrumbs"></bread-crumb>
    </div>
    <!-- ibox start -->
    <div class="ibox">
        <div class="ibox-content">
            <div class="row">
                <form id="searchForm">
                    <div class="col-md-3 form-group">
                        <input v-model="form.keyword" type="text"
                               placeholder="订单单号，变更号" class="form-control"/>
                    </div>
                    <div class="col-md-3 form-group">
                        <label class="sr-only" for="status">状态：</label>
                        <select v-model="form.status"
                                id="status"
                                name="status"
                                class="form-control">
                            <option value="">请选择状态</option>
                            <option v-for="item in statusList" :value="item.value">{{item.name}}</option>
                        </select>
                    </div>
                    <div class="col-md-3 form-group">
                        <label class="sr-only" for="catalogUrl">类目</label>
                        <select v-model="form.catalogUrl"
                                id="catalogUrl"
                                name="catalogUrl"
                                class="form-control">
                            <option value="">请选择类目</option>
                            <option v-for="item in allCatalog" :value="item.url">{{item.name}}</option>
                        </select>
                    </div>
                    <div class="col-md-3 form-group">
                        <button id="searchBtn" type="button" @click.prevent="query"
                                class="btn btn-block btn-outline btn-default" alt="搜索"
                                title="搜索">
                            <i class="fa fa-search"></i>
                        </button>
                    </div>
                </form>
            </div>
            <table id="dataTable" width="100%"
                   class="table table-striped table-bordered table-hover">
            </table>
        </div>
    </div>
    <!-- ibox end -->
</div>

<div id="detailModal" class="modal fade" tabindex="-1" data-width="1000">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal"
                aria-hidden="true">×
        </button>
        <h3 align="center">变更审核列表</h3>
    </div>
    <div class="modal-body">
        <div class="ibox">
            <div class="ibox-heading">
                客户信息
            </div>
            <div class="ibox-content">
                <table class="table table-striped table-bordered table-hover">
                    <tr>
                        <td>订单编号：</td>
                        <td>{{customer.code}}</td>
                        <td>变更编号：</td>
                        <td>{{customer.no}}</td>
                        <td></td>
                        <td></td>
                    </tr>
                    <tr>
                        <td>客户姓名：</td>
                        <td>{{customer.name}}</td>
                        <td>联系电话：</td>
                        <td>{{customer.mobile}}</td>
                        <td>房屋地址：</td>
                        <td>{{customer.addressProvince}} &nbsp;{{customer.addressCity}}&nbsp;{{customer.addressArea}}&nbsp;{{customer.houseAddr}}</td>
                    </tr>
                    <tr>
                        <td>设计师：</td>
                        <td>{{customer.designer}}</td>
                        <td>设计师电话：</td>
                        <td>{{customer.designerMobile}}</td>
                        <td>计价面积：</td>
                        <td>{{customer.valuateArea}}m²</td>
                    </tr>
                </table>
            </div>
        </div>
        <div class="ibox">
            <div class="ibox-heading">
                变更减项商品信息 &nbsp;&nbsp;<span v-if="roleFlg=='3'"  style="color: red;font-weight:bold" >合计：{{subMoney}}元</span>
            </div>
            <div class="ibox-content">
                <table class="table table-striped table-bordered table-hover">
                    <tr>
                        <td hidden>id</td>
                        <th>类别</th>
                        <th>商品类目</th>
                        <th>品牌</th>
                        <th>名称</th>
                        <th>型号</th>
                        <th>属性</th>
                        <th>位置</th>
                        <th v-if="roleFlg=='3'">原用量</th>
                        <th v-if="roleFlg=='3'">现用量</th>
                        <th>减少量</th>
                        <th>价格</th>
                        <th>设计备注</th>
                        <th>材料状态</th>

                        <th v-if="roleFlg=='3'">合计</th>
                    </tr>
                    <template v-if="roleFlg!='3'">
                        <template v-for=" item in skuList" v-if="item.num<0 && item.categoryCode!='REDUCEITEM' ">
                            <tr>
                                <td hidden><input type="text" v-model="ids[$index]" :value="item.id"></td>
                                <td>{{item.categoryCode | categoryFilter}}</td>
                                <td>{{item.cataLogName}}</td>
                                <td>{{item.brand}}</td>
                                <td>{{item.productName}}</td>
                                <td>{{item.skuModel}}</td>
                                <td>{{item.attribute1}}-{{item.attribute2}}-{{item.attribute3}}</td>
                                <td>{{item.domainName}}</td>
                                <td v-if="roleFlg=='3'">{{item.originalDosage}}</td>
                                <td v-if="roleFlg=='3'">{{item.lossDosage}}</td>
                                <td>{{item.num | absNum}}{{item.materialUnit | unitFilter}}</td>
                                <td>{{item.price}}</td>
                                <td>{{item.designRemark}}</td>
                                <td><select v-if="!edit1" disabled type="text" v-model="item.materialsStatus">
                                    <option value="">请选择</option>
                                    <option value="NOT_MEASURED">未测量</option>
                                    <option value="MEASURED">已测量</option>
                                    <option value="NO_ORDERS">未下单</option>
                                    <option value="ALREADY_ORDERED">已下单</option>
                                    <option value="NOT_SHIPPED">未发货</option>
                                    <option value="SHIPPED">已发货</option>
                                    <option value="NOT_INSTALLED">未安装</option>
                                    <option value="INSTALLED">已安装</option>
                                    <option value="UNFILLED_ORDER_CONDITIONS">未满足下单条件</option>
                                </select>
                                    <select v-else type="text" v-model="status[$index]">
                                        <option value="" selected>请选择</option>
                                        <option value="NOT_MEASURED">未测量</option>
                                        <option value="MEASURED">已测量</option>
                                        <option value="NO_ORDERS">未下单</option>
                                        <option value="ALREADY_ORDERED">已下单</option>
                                        <option value="NOT_SHIPPED">未发货</option>
                                        <option value="SHIPPED">已发货</option>
                                        <option value="NOT_INSTALLED">未安装</option>
                                        <option value="INSTALLED">已安装</option>
                                        <option value="UNFILLED_ORDER_CONDITIONS">未满足下单条件</option>
                                    </select>
                                </td>

                                <template v-if="item.categoryCode=='PACKAGESTANDARD'">
                                    <td v-if="roleFlg=='3'">0元</td>
                                </template>
                                <template v-else >
                                    <td v-if="roleFlg=='3'">{{item.num *item.price}}元</td>
                                </template>
                            </tr>
                        </template>
                    </template>
                    <template v-else>
                        <template v-for=" item in skuList" v-if="item.num<0 ">
                            <tr>
                                <td hidden><input type="text" v-model="ids[$index]" :value="item.id"></td>
                                <td>{{item.categoryCode | categoryFilter}}</td>
                                <td>{{item.cataLogName}}</td>
                                <td>{{item.brand}}</td>
                                <td>{{item.productName}}</td>
                                <td>{{item.skuModel}}</td>
                                <td>{{item.attribute1}}-{{item.attribute2}}-{{item.attribute3}}</td>
                                <td>{{item.domainName}}</td>
                                <td v-if="roleFlg=='3'">{{item.originalDosage}}</td>
                                <td v-if="roleFlg=='3'">{{item.lossDosage}}</td>
                                <td>{{item.num | absNum}}{{item.materialUnit |unitFilter}}</td>
                                <td>{{item.price}}</td>
                                <td>{{item.designRemark}}</td>
                                <td><select v-if="!edit1" disabled type="text" v-model="item.materialsStatus">
                                    <option value="">请选择</option>
                                    <option value="NOT_MEASURED">未测量</option>
                                    <option value="MEASURED">已测量</option>
                                    <option value="NO_ORDERS">未下单</option>
                                    <option value="ALREADY_ORDERED">已下单</option>
                                    <option value="NOT_SHIPPED">未发货</option>
                                    <option value="SHIPPED">已发货</option>
                                    <option value="NOT_INSTALLED">未安装</option>
                                    <option value="INSTALLED">已安装</option>
                                    <option value="UNFILLED_ORDER_CONDITIONS">未满足下单条件</option>
                                </select>
                                    <select v-else type="text" v-model="status[$index]">
                                        <option value="" selected>请选择</option>
                                        <option value="NOT_MEASURED">未测量</option>
                                        <option value="MEASURED">已测量</option>
                                        <option value="NO_ORDERS">未下单</option>
                                        <option value="ALREADY_ORDERED">已下单</option>
                                        <option value="NOT_SHIPPED">未发货</option>
                                        <option value="SHIPPED">已发货</option>
                                        <option value="NOT_INSTALLED">未安装</option>
                                        <option value="INSTALLED">已安装</option>
                                        <option value="UNFILLED_ORDER_CONDITIONS">未满足下单条件</option>
                                    </select>
                                </td>
                                <template v-if="item.categoryCode=='PACKAGESTANDARD'">
                                    <td v-if="roleFlg=='3'">0元</td>
                                </template>
                                <template v-else >
                                    <td v-if="roleFlg=='3'">{{item.num *item.price}}元</td>
                                </template>
                            </tr>
                        </template>
                    </template>
                </table>
            </div>
        </div>
        <div class="ibox">
            <div class="ibox-heading">
                变更增项商品信息 &nbsp;&nbsp;<span v-if="roleFlg=='3'" style="color: red;font-weight:bold">合计：{{addMoney}}元</span>
            </div>
            <div class="ibox-content">
                <table class="table table-striped table-bordered table-hover">
                    <tr>
                        <th>类别</th>
                        <th>商品类目</th>
                        <th>品牌</th>
                        <th>名称</th>
                        <th>型号</th>
                        <th>属性</th>
                        <th>位置</th>
                        <th v-if="roleFlg=='3'">原用量</th>
                        <th v-if="roleFlg=='3'">现用量</th>
                        <th>增加量</th>
                        <th>价格</th>
                        <th>设计备注</th>
                        <th v-if="roleFlg=='3'">合计</th>
                    </tr>
                    <template v-for=" item in skuList" v-if="item.num>0 && roleFlg=='3'">
                        <tr>
                            <td>{{item.categoryCode |categoryFilter}}</td>
                            <td>{{item.cataLogName}}</td>
                            <td>{{item.brand}}</td>
                            <td>{{item.productName}}</td>
                            <td>{{item.skuModel}}</td>
                            <td>{{item.attribute1}}-{{item.attribute2}}-{{item.attribute3}}</td>
                            <td>{{item.domainName}}</td>
                            <td v-if="roleFlg=='3'">{{item.originalDosage}}</td>
                            <td v-if="roleFlg=='3'">{{item.lossDosage}}</td>
                            <td>{{item.num}}{{item.materialUnit |unitFilter}}</td>
                            <td>{{item.price}}</td>
                            <td>{{item.designRemark}}</td>
                            <template v-if="item.categoryCode=='PACKAGESTANDARD'">
                                <td v-if="roleFlg=='3'">0元</td>
                            </template>
                            <template v-else >
                                <td v-if="roleFlg=='3'">{{item.num *item.price}}元</td>
                            </template>
                        </tr>
                    </template>
                </table>
            </div>
        </div>
        <div class="ibox" v-if="num_zero" >
            <div class="ibox-heading">
                未变更商品信息 &nbsp;&nbsp;<span style="color: red;font-weight:bold">合计：0元</span>
            </div>
            <div class="ibox-content">
                <table class="table table-striped table-bordered table-hover">
                    <tr>
                        <th>类别</th>
                        <th>商品类目</th>
                        <th>品牌</th>
                        <th>名称</th>
                        <th>型号</th>
                        <th>属性</th>
                        <th>位置</th>
                        <th>数量</th>
                        <th>价格</th>
                        <th>设计备注</th>
                        <th>合计</th>
                    </tr>
                    <template v-for=" item in skuList" v-if="item.num == 0  && roleFlg == '3'">
                        <tr>
                            <td>{{item.categoryCode |categoryFilter}}</td>
                            <td>{{item.cataLogName}}</td>
                            <td>{{item.brand}}</td>
                            <td>{{item.productName}}</td>
                            <td>{{item.skuModel}}</td>
                            <td>{{item.attribute1}}-{{item.attribute2}}-{{item.attribute3}}</td>
                            <td>{{item.domainName}}</td>
                            <td>{{item.lossDosage}}{{item.materialUnit |unitFilter}}</td>
                            <td>{{item.price}}</td>
                            <td>{{item.designRemark}}</td>
                             <td >0元</td>
                        </tr>
                    </template>
                </table>
            </div>
        </div>
        <div class="ibox">
            <div class="ibox-heading">
                其他金额增减信息 &nbsp;&nbsp;<span v-if="roleFlg=='3'" style="color: red;font-weight:bold" >合计：{{otherMoney}}元</span>
            </div>
            <div class="ibox-content">
                <table class="table table-striped table-bordered table-hover">
                    <tr>
                        <td>增减金额类型</td>
                        <td>增减金额原因</td>
                        <td>增减金额</td>
                    </tr>
                    <template v-for=" item in otherList" v-if="(roleFlg=='1')||(item.addReduceType==0 &&roleFlg=='2')||roleFlg=='3' ">
                        <tr>
                            <td>{{item.addReduceType | addReduceType}}</td>
                            <td>{{item.addReduceReason}}</td>
                            <td>{{item.quota}}元</td>
                        </tr>
                    </template>
                </table>
            </div>
        </div>
        <div class="ibox" v-if="roleFlg=='3'">
            <div class="ibox-heading">
                <span v-if="roleFlg=='3'" style="color: red;font-weight:bold"  >&nbsp;&nbsp;合计：{{addMoney+subMoney+otherMoney}}元</span>
            </div>
            <div class="ibox-content">
            </div>
        </div>
        <div class="ibox">
            <div class="ibox-content">
                <div class="form-group" v-if="remarkFlg1">
                    <div class="col-sm-12">
                        <p v-if="remark1!=null">材料部备注：{{remark1}}</p>
                    </div>
                </div>
                <div class="form-group" v-if="remarkFlg2">
                    <label class="control-label col-sm-1">备注：</label>
                    <div class="col-sm-12">
                        <p v-if="remark1!=null">材料部备注：{{remark1}}</p>
                        <p v-if="remark2!=null">设计总监备注： {{remark2}}</p>
                    </div>
                </div>
                <div class="form-group" v-if="edit">
                    <label class="control-label col-sm-1">备注：</label>
                    <div class="col-sm-12">
                        <textarea maxlength="100" rows="3" cols="50" v-model="remark"></textarea>
                    </div>
                </div>


            </div>
        </div>
    </div>
    <div class="modal-footer">
        <button type="button" data-dismiss="modal" class="btn btn-primary"
                @click.prevent="submit(1)" v-if="submitting1">通过
        </button>
        <button type="button" data-dismiss="modal" class="btn btn-danger"
                @click.prevent="submit(2)" v-if="submitting1">驳回
        </button>
        <button type="button" data-dismiss="modal" v-else class="btn btn-primary"
                @click.prevent="submit" :disabled="submitting">确定
        </button>
        <button type="button" data-dismiss="modal" class="btn">取消</button>
    </div>
</div>

<script src="/static/business/material/changeaudit.js"></script>
