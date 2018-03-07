var  vueModal2;
var  vueModal;
var proSkuPriceList;
+(function () {
    $('#commodityMenu').addClass('active')
    $('#proSkuPrice').addClass('active')
     proSkuPriceList = new Vue({
        el: '#container',
        mixins: [DameiVueMixins.DataTableMixin],
        data: {
            breadcrumbs: [{
                path: '/',
                name: '主页'
            },
                {
                    path: '/',
                    name: '装修商品',
                    active: false
                },
                {
                    path: '/',
                    name: '价格管理',
                    active: true
                }],
            //1级分类
            allCatalog1: null,
            //2级分类
            allCatalog2: null,
            //门店
            allOrganization: null,
            // 区域供应商
            allSuppliers: null,
            // 商品供应商
            suppliers: null,
            //品牌
            allBrand:null,
            //门店
            allStores: null,
            skuId:null,
            form: {
                keyword: '',
                //1级目录
                catalogUrl1: '',
                //2级目录
                catalogUrl2: '',
                //商品供货商
                supplierId: '',
                //区域供货商
                allSupplierId: '',
                //门店
                allStoreCode: '',
                //品牌
                brandId:''
            },
            $dataTable: null,
            selectedRows: {},
            // 选中列
            modalModel: null,
            // 模式窗体模型
            _$el: null,
            // 自己的 el $对象
            _$dataTable: null, // datatable $对象
        },
        created: function () {
        },
        ready: function () {
            this.fetchAllStores();
            this.findAllBrand();
            this.fetchCategory1();
        },
        methods: {
            // 商品品牌列表
            findAllBrand:function () {
                var self = this;
                    self.$http.get('/material/prodsku/findAllBrand').then(function (res) {
                        if (res.data.code == 1) {
                            self.allBrand = res.data.data;
                        }
                    }).catch(function () {
                    }).finally(function () {
                    });
            },
            // 商品供应商列表
            fetchSuppliers: function () {
                var self = this;
                var id=self.form.allSupplierId;
                if (id) {
                    self.$http.get('/material/prodsku/findsupplierbyregionid/' + id).then(function (res) {
                        if (res.data.code == 1) {
                            self.suppliers = res.data.data;
                            self.form.supplierId='';
                        }
                    }).catch(function () {
                    }).finally(function () {
                    });
                }else{
                    self.form.supplierId='';
                }
            },
            // 区域供应商
            fetchRegionSuppliers: function () {
                var self = this;
                var val=self.form.allStoreCode;
                if(val){
                    self.$http.get('/material/prodsku/findregionsupplierbystorecode/' + val).then(function (res) {
                        if (res.data.code == 1) {
                            self.allSuppliers = res.data.data;
                            self.form.supplierId='';
                            self.form.allSupplierId='';
                        }
                    }).catch(function () {
                    }).finally(function () {
                    })
                }else{
                    self.form.supplierId='';
                    self.form.allSupplierId='';
                }
            },
            //门店
            fetchAllStores: function () {
                var self = this;
                self.$http.get('/material/prodsku/storelist').then(function (res) {
                    if (res.data.code == 1) {
                        self.allStores = [];
                         var dataStore = res.data.data;
                         if(dataStore){
                             if(DameiUser.storeCode){
                             for (var i=0;i<dataStore.length;i++){
                                if( dataStore[i].code==DameiUser.storeCode){
                                    self.allStores.push(dataStore[i]);
                                }
                             }
                            self.form.allStoreCode=DameiUser.storeCode;
                            this.drawTable();
                            this.fetchSuppliers();
                            this.fetchRegionSuppliers();
                             }
                         }
                    }
                }).catch(function () {
                }).finally(function () {
                })
            },
            //类目1级
            fetchCategory1: function () {
                var self = this;
                self.$http.get('/material/prodsku/findcatalogfirstlist').then(function (res) {
                    if (res.data.code == 1) {
                        self.allCatalog1 = res.data.data;
                    }
                })
            },
            //类目2级
            fetchCategory2: function () {
                var self = this;
                var url=self.form.catalogUrl1;
                if(url){
                    self.$http.get('/material/prodsku/findcataloglistbyurl/'+url).then(function (res) {
                        if (res.data.code == 1) {
                            self.allCatalog2 = res.data.data;
                            self.form.catalogUrl2='';
                        }
                    })
                }else{
                    self.form.catalogUrl2='';
                }
             },
            query: function () {
                this.$dataTable.bootstrapTable('selectPage', 1)
            },
            drawTable: function () {
                var self = this;
                self.$dataTable = $('#dataTable', self._$el).bootstrapTable({
                    url: '/material/prodsku/list',
                    method: 'get',
                    dataType: 'json',
                    cache: false,
                    // 去缓存
                    pagination: true,
                    // 是否分页
                    sidePagination: 'server',
                    pageSize: 10,
                    pageList: [10, 50, 100, 200],
                    // 服务端分页
                    queryParams: function (params) {
                        // 将table 参数与搜索表单参数合并
                        return _.extend({},
                            params, self.form)
                    },
                    mobileResponsive: true,
                    undefinedText: '',
                    // 空数据的默认显示字符
                    striped: true,
                    // 斑马线
                    maintainSelected: true,
                    // 维护checkbox选项
                    sortOrder: 'desc',
                    // 默认排序方式
                    columns: [
                        {
                            field: 'code',
                            title: 'sku编码',
                            align: 'center',
                            orderable: true
                        },
                        {
                            field: 'name',
                            title: 'sku名称',
                            align: 'center',
                            orderable: true
                        },
                        {
                            field: 'attribute1',
                            title: '属性值1',
                            align: 'center',
                            orderable: true
                        },
                        {
                            field: 'attribute2',
                            title: '属性值2',
                            align: 'center',
                            orderable: true
                        },
                        {
                            field: 'attribute3',
                            title: '属性值3',
                            align: 'center',
                            orderable: true
                        },
                        {
                            field: 'stock',
                            title: '库存量',
                            align: 'center',
                            orderable: true
                        },{
                            field: 'productBrandName',
                            title: '品牌',
                            align: 'center',
                            orderable: true,
                        },
                        {
                            field: 'productModel',
                            title: '型号',
                            align: 'center',
                            orderable: true,
                        },
                        {
                            field: 'productSpec',
                            title: '规格',
                            align: 'center',
                            orderable: true,
                        },
                        {
                            field: 'priceFlag',
                            title: '价格标记',
                            align: 'center',
                            orderable: true,
                            visible: false
                        },
                        {
                            field: 'priceFlag',
                            title: '升级项',
                            align: 'center',
                            orderable: true,
                            formatter: function (value, row) {
                                var label = '';
                                if (value != undefined && value.indexOf("UPGRADE") >= 0) {
                                    label = '<font color="green">已填写</font>';
                                } else {
                                    label = '<font color="red">未填写</font>';
                                }
                                return label;
                            }

                        },
                        {
                            field: 'priceFlag',
                            title: '增项价',
                            align: 'center',
                            orderable: true,
                            formatter: function (value, row) {
                                var label = '';
                                if (value != undefined && value.indexOf("INCREASED") >= 0) {
                                    label = '<font color="green">已填写</font>';
                                } else {
                                    label = '<font color="red">未填写</font>';
                                }
                                return label;
                            }
                        },
                        {
                            field: 'priceFlag',
                            title: '减项价',
                            align: 'center',
                            orderable: true,
                            formatter: function (value, row) {
                                var label = '';
                                if (value != undefined && value.indexOf("MINUS") >= 0) {
                                    label = '<font color="green">已填写</font>';
                                } else {
                                    label = '<font color="red">未填写</font>';
                                }
                                return label;
                            }
                        },
                        {
                            field: 'id',
                            title: '操作',
                            width: '10%',
                            orderable: false,
                            align: 'center',
                            formatter: function (value, row) {
                                var html = '';
                                if(DameiUtils.hasPermission('product:setPriceBtn')){
                                    html += '<button data-handle="set-price" data-id="' + value + '" type="button" class="btn btn-xs btn-primary">设置价格</button>';
                                }
                                if(DameiUtils.hasPermission('product:viewBtn')){
                                    html += '<button data-handle="see-price" data-id="' + value + '" type="button" class="btn btn-xs btn-danger">查看</button>';
                                }
                                return html;
                            }
                        }
                    ]
                });
                // 设置价格
                self.$dataTable.on('click', '[data-handle="set-price"]',
                    function (e) {
                        var id = $(this).data('id');
                        self.skuId=id;
                        priceModal(id,true);
                        e.stopPropagation();
                    }
                );
                // 看价格
                self.$dataTable.on('click', '[data-handle="see-price"]',
                    function (e) {
                        var id = $(this).data('id');
                        self.skuId=id;
                        priceModal(id,false);
                        e.stopPropagation();
                    }
                );
            },
            // end of 渲染datatable
        }
    })
    // 设置价格
    function priceModal(id,isTrue) {
        var $modal = $('#priceModal').clone();
        $modal.modal({
            height: 400,
            maxHeight: 500,
            width: 800
        });
        $modal.on('shown.bs.modal',
            function () {
                vueModal2 = new Vue({
                    el: $modal.get(0),
                    mixins: [DameiVueMixins.DataTableMixin],
                    data: {
                        form: {
                            keyword: '',
                            declareCode: null,
                            orgId: null,
                            declareStartDate: null,
                            declareEndDate: null
                        },
                        id: id,
                        current: 'aConfig', // 当前tab
                        aKeyword: '', // a关键字
                        bKeyword: '', // b关键字
                        cKeyword: '', // c关键字
                        $aDataTable: null, // A表格
                        $bDataTable: null, // B表格
                        $cDataTable: null, // C表格
                        $dataTable: null,
                        selectedRows: {},
                        // 选中列
                        modalModel: null,
                        // 模式窗体模型
                        _$el: null,
                        aControl: 0, // 是否可以新增 0 不可以 1 可以
                        bControl: 0, // 是否可以新增 0 不可以 1 可以
                        cControl: 0, // 是否可以新增 0 不可以 1 可以
                        aShow: 0, // 是否可以显示tab 0 不可以 1 可以
                        bShow: 0, // 是否可以显示tab 0 不可以 1 可以
                        cShow: 0 // 是否可以显示tab 0 不可以 1 可以
                        // 自己的 el $对象
                    },
                    created: function () {
                    },
                    ready: function () {
                        if(isTrue){
                        this.aControl = 1;
                        this.bControl = 1;
                        this.cControl = 1;
                        this.aShow = 1;
                        this.bShow = 1;
                        this.cShow = 1;
                        this.aTable(1);
                        this.bTable(1);
                        this.cTable(1);
                        }else{
                            this.aControl = 0;
                            this.bControl = 0;
                            this.cControl = 0;
                            this.aShow = 1;
                            this.bShow = 1;
                            this.cShow = 1;
                            this.aTable(0);
                            this.bTable(0);
                            this.cTable(0);
                        }
                    },
                    methods: {
                        createBtnClickHandler: function (priceType) {
                            createOrEditModal({priceType: priceType}, false)
                        },
                        // tab切换
                        tab: function (config) {
                            this.current = config
                        },
                        // A表格
                        aTable: function (control) {
                            var self = this;
                            self.$aDataTable = $('#aTable', self._$el).bootstrapTable({
                                url: '/material/prodskuprice/listbytype',
                                method: 'get',
                                dataType: 'json',
                                cache: false,
                                pagination: true,
                                sidePagination: 'server',
                                queryParams: function (params) {
                                    return _.extend({}, params, {priceType: 'UPGRADE', skuId: id})
                                },
                                mobileResponsive: true,
                                undefinedText: '',
                                striped: true,
                                maintainSelected: true,
                                sortOrder: 'ASC',
                                columns: [
                                    {
                                        field: 'priceStartDate',
                                        title: '生效时间',
                                        align: 'center'
                                    },
                                    {
                                        field: 'price',
                                        title: '价格',
                                        align: 'center',
                                        formatter: function (value) {
                                            return '¥' + value
                                        }
                                    },
                                    {
                                        field: 'editorName',
                                        title: '操作人',
                                        align: 'center',
                                    },
                                    {
                                        field: 'editTime',
                                        title: '操作时间',
                                        align: 'center'
                                    },
                                    {
                                        field: 'id',
                                        title: '操作',
                                        className: 'td_center',
                                        orderable: false,
                                        align: 'center',
                                        formatter: function (value, row, index) {
                                            var html = '';
                                            if (control == '1' ) {
                                                html += '<button style="margin-left:10px;"';
                                                html += 'data-handle="aEdit"';
                                                html += 'data-priceStartDate="' + row.priceStartDate + '"';
                                                html += 'data-price="' + row.price + '"'
                                                html += 'data-priceType="' + row.priceType + '"';
                                                html += 'data-id="' + row.id + '"';
                                                html += 'class="m-r-xs btn btn-xs btn-primary" type="button">编辑</button>';
                                                html += '<button style="margin-left:10px;" data-handle="delete" data-id="' + value + '" class="m-r-xs btn btn-xs btn-primary" type="button">删除</button>';
                                            }
                                            return html
                                        }
                                    }]
                            })
                            // 编辑
                            self.$aDataTable.on('click', '[data-handle="aEdit"]',
                                function (e) {
                                    var model = $(this).data();
                                    model.priceStartDate = model.pricestartdate;
                                    model.priceType = model.pricetype;
                                    createOrEditModal(model, true);
                                    e.stopPropagation()
                                });

                            // 删除
                            self.$aDataTable.on('click', '[data-handle="delete"]',
                                function (e) {
                                    var id = $(this).data('id');
                                    swal({
                                            title: '',
                                            text: '确定删除吗？',
                                            type: 'info',
                                            confirmButtonText: '确定',
                                            cancelButtonText: '取消',
                                            showCancelButton: true,
                                            showConfirmButton: true,
                                            showLoaderOnConfirm: true,
                                            confirmButtonColor: '#ed5565',
                                            closeOnConfirm: false
                                        },
                                        function () {
                                            vueModal2.$http.post('/material/prodskuprice/' + id + '/del').then(function (res) {
                                                if (res.data.code == 1) {
                                                    Vue.toastr.success(res.data.message);
                                                    self.$aDataTable.bootstrapTable('refresh');
                                                }
                                            }).catch(function () {
                                            }).finally(function () {
                                                swal.close();
                                            })
                                        });
                                    e.stopPropagation();
                                })
                        },
                        // B表格
                        bTable: function (control) {
                            var self = this;
                            self.$bDataTable = $('#bTable', self._$el).bootstrapTable({
                                url: '/material/prodskuprice/listbytype',
                                method: 'get',
                                dataType: 'json',
                                cache: false,
                                pagination: true,
                                sidePagination: 'server',
                                queryParams: function (params) {
                                    return _.extend({}, params, {priceType: 'INCREASED', skuId: id})
                                },
                                mobileResponsive: true,
                                undefinedText: '',
                                striped: true,
                                maintainSelected: true,
                                sortOrder: 'ASC',
                                columns: [
                                    {
                                        field: 'priceStartDate',
                                        title: '生效时间',
                                        align: 'center'
                                    },
                                    {
                                        field: 'price',
                                        title: '价格',
                                        align: 'center',
                                        formatter: function (value) {
                                            return '¥' + value
                                        }
                                    },
                                    {
                                        field: 'editorName',
                                        title: '操作人',
                                        align: 'center',
                                    },
                                    {
                                        field: 'editTime',
                                        title: '操作时间',
                                        align: 'center'
                                    },
                                    {
                                        field: 'id',
                                        title: '操作',
                                        className: 'td_center',
                                        orderable: false,
                                        align: 'center',
                                        formatter: function (value, row, index) {
                                            var html = '';
                                            if (control == '1' ) {
                                                html += '<button style="margin-left:10px;"'
                                                html += 'data-handle="bEdit"'
                                                html += 'data-priceStartDate="' + row.priceStartDate + '"'
                                                html += 'data-price="' + row.price + '"'
                                                html += 'data-priceType="' + row.priceType + '"'
                                                html += 'data-id="' + row.id + '"';
                                                html += 'class="m-r-xs btn btn-xs btn-primary" type="button">编辑</button>';
                                                html += '<button style="margin-left:10px;" data-handle="delete" data-id="' + value + '" class="m-r-xs btn btn-xs btn-primary" type="button">删除</button>';
                                            }
                                            return html;
                                        }
                                    }]
                            });
                            // 编辑
                            self.$bDataTable.on('click', '[data-handle="bEdit"]',
                                function (e) {
                                    var model = $(this).data();
                                    model.priceStartDate = model.pricestartdate;
                                    model.priceType = model.pricetype;
                                    createOrEditModal(model, true);
                                    e.stopPropagation();
                                });

                            // 删除
                            self.$bDataTable.on('click', '[data-handle="delete"]',
                                function (e) {
                                    var id = $(this).data('id');
                                    swal({
                                            title: '',
                                            text: '确定删除吗？',
                                            type: 'info',
                                            confirmButtonText: '确定',
                                            cancelButtonText: '取消',
                                            showCancelButton: true,
                                            showConfirmButton: true,
                                            showLoaderOnConfirm: true,
                                            confirmButtonColor: '#ed5565',
                                            closeOnConfirm: false
                                        },
                                        function () {
                                            vueModal2.$http.post('/material/prodskuprice/' + id + '/del').then(function (res) {
                                                if (res.data.code == 1) {
                                                    Vue.toastr.success(res.data.message);
                                                    self.$bDataTable.bootstrapTable('selectPage', 1);
                                                }
                                            }).catch(function () {
                                            }).finally(function () {
                                                swal.close();
                                            })
                                        });
                                    e.stopPropagation();
                                })
                        },
                        // C表格
                        cTable: function (control) {
                            var self = this;
                            self.$cDataTable = $('#cTable', self._$el).bootstrapTable({
                                url: '/material/prodskuprice/listbytype',
                                method: 'get',
                                dataType: 'json',
                                cache: false,
                                pagination: true,
                                sidePagination: 'server',
                                queryParams: function (params) {
                                    return _.extend({}, params, {priceType: 'MINUS', skuId: id})
                                },
                                mobileResponsive: true,
                                undefinedText: '',
                                striped: true,
                                maintainSelected: true,
                                sortOrder: 'ASC',
                                columns: [
                                    {
                                        field: 'priceStartDate',
                                        title: '生效时间',
                                        align: 'center'
                                    },
                                    {
                                        field: 'price',
                                        title: '价格',
                                        align: 'center',
                                        formatter: function (value) {
                                            return '¥' + value
                                        }
                                    },
                                    {
                                        field: 'editorName',
                                        title: '操作人',
                                        align: 'center',
                                    },
                                    {
                                        field: 'editTime',
                                        title: '操作时间',
                                        align: 'center'
                                    },
                                    {
                                        field: 'id',
                                        title: '操作',
                                        className: 'td_center',
                                        orderable: false,
                                        align: 'center',
                                        formatter: function (value, row, index) {
                                            var html = '';
                                            if (control == '1') {
                                                html += '<button style="margin-left:10px;"';
                                                html += 'data-handle="cEdit"';
                                                html += 'data-priceStartDate="' + row.priceStartDate + '"';
                                                html += 'data-price="' + row.price + '"';
                                                html += 'data-priceType="' + row.priceType + '"';
                                                html += 'data-id="' + row.id + '"';
                                                html += 'class="m-r-xs btn btn-xs btn-primary" type="button">编辑</button>';
                                                html += '<button style="margin-left:10px;" data-handle="delete" data-id="' + value + '" class="m-r-xs btn btn-xs btn-primary" type="button">删除</button>';
                                            }
                                            return html;
                                        }
                                    }]
                            });
                            // 编辑
                            self.$cDataTable.on('click', '[data-handle="cEdit"]',
                                function (e) {
                                    var model = $(this).data();
                                    model.priceStartDate = model.pricestartdate;
                                    model.priceType = model.pricetype;
                                    createOrEditModal(model, true);
                                    e.stopPropagation();
                                });

                            // 删除
                            self.$cDataTable.on('click', '[data-handle="delete"]',
                                function (e) {
                                    var id = $(this).data('id');
                                    swal({
                                            title: '',
                                            text: '确定删除吗？',
                                            type: 'info',
                                            confirmButtonText: '确定',
                                            cancelButtonText: '取消',
                                            showCancelButton: true,
                                            showConfirmButton: true,
                                            showLoaderOnConfirm: true,
                                            confirmButtonColor: '#ed5565',
                                            closeOnConfirm: false
                                        },
                                        function () {
                                            vueModal2.$http.post('/material/prodskuprice/' + id + '/del').then(function (res) {
                                                if (res.data.code == 1) {
                                                    Vue.toastr.success(res.data.message);
                                                    self.$cDataTable.bootstrapTable('selectPage', 1);
                                                }
                                            }).catch(function () {
                                            }).finally(function () {
                                                swal.close();
                                            })
                                        });
                                    e.stopPropagation();
                                })
                        },
                        closeFrame: function () {
                            $modal.modal('hide');
                            proSkuPriceList.$dataTable.bootstrapTable('refresh');
                        }
                    }
                })
            })
    }

    //  新增／编辑价格
    function createOrEditModal(model, isEdit) {
        var _modal = $('#contractModal').clone();
        var atableData = vueModal2.$aDataTable.bootstrapTable('getData');
        var btableData = vueModal2.$bDataTable.bootstrapTable('getData');
        if(vueModal2.$cDataTable){
            var ctableData = vueModal2.$cDataTable.bootstrapTable('getData');
        }
        var $el = _modal.modal({
            maxHeight: 600,
            width: 400
        });
        $el.on('shown.bs.modal',
            function () {
                var el = $el.get(0)
                isEdit = !!isEdit
                 vueModal = new Vue({
                    el: el,
                    http: {
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded'
                        }
                    },
                    mixins: [DameiVueMixins.ModalMixin],
                    $modal: $el,
                    data: {
                        id: model.id,
                        priceStartDate: model.priceStartDate,
                        price: model.price,
                        skuId: proSkuPriceList.skuId,
                        priceType: model.priceType,
                        type: '价格',
                        blank:false
                    },
                    ready: function () {
                        $('#timepick', this._$el).datetimepicker({
                            startDate: new Date()
                        })
                    },
                    created: function () {
                        switch (this.priceType) {
                            case 'UPGRADE':
                                this.type = '升级项价';
                                break;
                            case 'INCREASED':
                                this.type = '增项价';
                                break;
                            case 'MINUS':
                                this.type = '减项价';
                                break;
                        }
                        if (atableData.length == 0) {
                            this.blank = true;
                            this.priceStartDate = moment().format('YYYY-MM-DD');
                        }
                        if (btableData.length == 0) {
                            this.blank = true;
                            this.priceStartDate = moment().format('YYYY-MM-DD');
                        }
                        if (vueModal2.$cDataTable && ctableData.length == 0) {
                            this.blank = true;
                            this.priceStartDate = moment().format('YYYY-MM-DD');
                        }
                    },
                    methods: {
                        savePrice: function () {
                            var self = this;
                            if (self.$validation.valid) {
                                self.submitting = true;
                                delete self._data.type;
                                self.$http.post('/material/prodskuprice/save', $.param(self._data)).then(function (res) {
                                        if (res.data.code === '1') {
                                            Vue.toastr.success(res.data.message);
                                            $el.modal('hide');
                                            if (vueModal2.current == 'aConfig') {
                                                vueModal2.$aDataTable.bootstrapTable('refresh');
                                            } else if (vueModal2.current == 'bConfig') {
                                                vueModal2.$bDataTable.bootstrapTable('refresh');
                                            } else {
                                                vueModal2.$cDataTable.bootstrapTable('refresh');
                                            }
                                            self.$destroy();
                                            proSkuPriceList.$dataTable.bootstrapTable('refresh');
                                        }else{
                                            Vue.toastr.error(res.data.message);
                                        }
                                    },
                                    function (error) {
                                        Vue.toastr.error(error.responseText);
                                    }).catch(function () {

                                }).finally(function () {
                                    self.submitting = false;
                                })
                            }
                        }

                    }

                });
                // 创建的Vue对象应该被返回
                return vueModal
            })
    }

})()
