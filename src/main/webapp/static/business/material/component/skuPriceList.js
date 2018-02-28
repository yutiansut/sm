//sku列表组件
var prodSku = Vue.extend({
    template: '#prodSku',
    data: function () {
        return {
            //1级分类
            allCatalog1: null,
            //2级分类
            allCatalog2: null,
            //品牌
            allBrand: null,
            form: {
                keyword: '',
                //1级目录
                catalogUrl1: '',
                //2级目录
                catalogUrl2: '',
                //品牌
                brandId: '',
                //sku类型
                priceType: this.priceType,
                date: this.date
            },
            $dataTable: null,
            selectedRows: {},
            // 选中列
            modalModel: null,
            // 模式窗体模型
            _$el: null,
            // 自己的 el $对象
            _$dataTable: null, // datatable $对象
            titleName: '销售价',
            //分类可不可选的框
            flg1: false,
            flg2: false,
            //项目主材sku
            projectMaterial: {
                contractCode: this.projectMaterial.contractCode,
                categoryCode: this.projectMaterial.categoryCode,
                categoryDetailCode: this.projectMaterial.categoryDetailCode,
            },
            pageType: this.pageType
        }
    },
    props: {
        projectMaterial: {
            required: true
        },
        priceType: {
            type: String,
            required: true
        },
        catalogUrl1: {
            type: String,
            required: false
        },
        catalogUrl2: {
            type: String,
            required: false
        },
        date: {
            required: true
        },
        pageType: {
            type: String,
            required: true
        },

    },
    created: function () {
        //选中的分类
        if (this.catalogUrl1 != '') {
            this.form.catalogUrl1 = this.catalogUrl1;
            this.fetchCategory2();
            this.flg1 = true;
        }
        if (this.catalogUrl2 != '') {
            this.form.catalogUrl2 = this.catalogUrl2;
            this.flg2 = true;
        }

        this.findAllBrand();
        this.fetchCategory1();
    },
    ready: function () {
        this.drawTable();
    },
    methods: {
        // 商品品牌列表
        findAllBrand: function () {
            var self = this;
            self.$http.get('/material/prodsku/findAllBrand').then(function (res) {
                if (res.data.code == 1) {
                    self.allBrand = res.data.data;
                }
            }).catch(function () {
            }).finally(function () {
            });
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
            var url = self.form.catalogUrl1;
            if (url) {
                self.$http.get('/material/prodsku/findcataloglistbyurl/' + url).then(function (res) {
                    if (res.data.code == 1) {
                        self.allCatalog2 = res.data.data;
                        self.form.catalogUrl2 = '';
                        if (self.catalogUrl2) {
                            self.form.catalogUrl2 = self.catalogUrl2;
                        }
                    }
                })
            } else {
                self.form.catalogUrl2 = '';
            }
        },
        query: function () {
            this.$dataTable.bootstrapTable('selectPage', 1)
        },
        drawTable: function () {
            var self = this;
            self.$dataTable = $('#dataTable', self._$el).bootstrapTable({
                url: '/material/prodsku/pricetypelist',
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
                    if (self.form.priceType == 'INCREASED') {
                        self.titleName = '增项价';
                    } else if (self.form.priceType == 'MINUS') {
                        self.titleName = '减项价';
                    } else if (self.form.priceType == 'UPGRADE') {
                        self.titleName = '升级项价';
                    }
                    return _.extend({}, params, self.form)
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
                        field: 'unitName',
                        title: '计量单位',
                        align: 'center',
                        visible: false
                    },
                    {
                        field: 'imgPath',
                        title: '图片',
                        align: 'center',
                        orderable: true,
                        formatter: function (val) {
                            if (!val) {
                                return '<img src="/static/business/image/notimg.png"/>';
                            }
                            return val;
                        }
                    },
                    {
                        field: 'name',
                        title: 'sku名称',
                        align: 'center',
                        orderable: true
                    },
                    {
                        field: 'attribute1',
                        title: '属性1',
                        align: 'center',
                        orderable: true,
                        formatter: function (value, row) {
                            var label = '';
                            if (value) {
                                label = row.label1 + '：' + value;
                            }
                            return label;
                        }
                    },
                    {
                        field: 'attribute2',
                        title: '属性2',
                        align: 'center',
                        orderable: true,
                        formatter: function (value, row) {
                            var label = '';
                            if (value) {
                                label = row.label2 + '：' + value;
                            }
                            return label;
                        }
                    },
                    {
                        field: 'attribute3',
                        title: '属性3',
                        align: 'center',
                        orderable: true,
                        formatter: function (value, row) {
                            var label = '';
                            if (value) {
                                label = row.label3 + '：' + value;
                            }
                            return label;
                        }
                    },
                    {
                        field: 'stock',
                        title: '库存量',
                        align: 'center',
                        orderable: true
                    }, {
                        field: 'productBrandName',
                        title: '品牌',
                        align: 'center',
                        orderable: true,
                    },
                    {
                        field: 'productBrandId',
                        title: '品牌id',
                        align: 'center',
                        orderable: true,
                        visible:false
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
                        field: 'productCategoryUrl',
                        title: '类别',
                        align: 'center',
                        orderable: true,
                        visible: false
                    },
                    {
                        field: 'price',
                        title: '价格',
                        align: 'center',
                        orderable: true,
                        formatter: function (value, row) {
                            var label = value + '元';
                            return label;
                        }
                    },
                    {
                        field: 'id',
                        title: '操作',
                        width: '5%',
                        orderable: false,
                        align: 'center',
                        formatter: function (value, row) {
                            var url = row.productCategoryUrl
                            if (url) {
                                var split = url.split('-');
                                if (split.length > 2) {
                                    url = split[0] + '-' + split[1] + '-';
                                }
                            }
                            var html = '<button data-handle="set-price" data-id="' + value + '"' +
                                ' data-code="' + row.code + '"  ' +
                                ' data-label1="' + row.label1 + '"  ' +
                                ' data-label2="' + row.label2 + '"  ' +
                                ' data-label3="' + row.label3 + '"  ' +
                                ' data-attribute1="' + row.attribute1 + '"  ' +
                                ' data-attribute2="' + row.attribute2 + '"  ' +
                                ' data-attribute3="' + row.attribute3 + '"  ' +
                                '  data-model="' + row.productModel + '"' +
                                ' data-spec="' + row.productSpec + '"  ' +
                                'data-price="' + row.price + '"   ' +
                                'data-name="' + row.name + '"  ' +
                                'data-brand="' + row.productBrandName + '"  ' +
                                'data-product-brand-id="' + row.productBrandId + '"  ' +
                                'data-path="' + row.imgPath + '"' +
                                'data-unit="' + row.unitName + '"' +
                                'data-url="' + url + '"' +
                                ' data-price="' + row.price + '"  type="button" class="btn btn-xs btn-primary">添加</button>';
                            return html;
                        }
                    }
                ]
            });
            // 添加
            self.$dataTable.on('click', '[data-handle="set-price"]',
                function (e) {
                    var model = $(this).data();
                    //新增项目主材sku
                    if (model && model.path == "undefined") {
                        model.path = '/static/business/image/notimg.png';
                    }
                    var data = {
                        //商品类目id
                        productCategoryUrl: model.url,
                        contractCode: self.projectMaterial.contractCode,
                        categoryCode: self.projectMaterial.categoryCode,
                        categoryDetailCode: self.projectMaterial.categoryDetailCode,
                        skuCode: model.code,
                        skuSqec: model.spec || '',
                        skuModel: model.model || '',
                        productName: model.name || '',
                        skuImagePath: model.path || '',
                        storeSalePrice: model.price || '',
                        attribute1: model.attribute1 || '',
                        attribute2: model.attribute2 || '',
                        attribute3: model.attribute3 || '',
                        materialUnit: model.unit || '',
                        brand: model.brand || '',
                        pageType: self.pageType,
                        brandId: model.productBrandId
                    };
                    self.$http.post("/material/projectmaterial/saveandreturnid", data, {emulateJSON: true})
                        .then(function (response) {
                            var res = response.data;
                            if (res.code == '1') {
                                //拿到新增后商品主材sku的id,并存到model中
                                model.projectMaterialId = res.data;
                                self.$dispatch('sku', model);
                                self.$toastr.success("添加商品成功!")
                            } else {
                                self.$toastr.error("添加商品失败!")
                            }
                        }).catch(function (e) {
                        console.log(e)
                        self.$toastr.error("添加商品失败!")
                    });

                }
            );
        },
        // end of 渲染datatable
    }
});