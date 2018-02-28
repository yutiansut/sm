var tt = null;
+(function () {
    $('#mealInfoMenu').addClass('active');
    $('#mealInfo').addClass('active');
    tt = new Vue({
        el: '#container',
        //mixins: [RocoVueMixins.DataTableMixin],
        data: {
            breadcrumbs: [{
                path: '/',
                name: '主页'
            },
                {
                    path: '/',
                    name: '套餐管理',
                    active: true
                }],
            form: {
                keyword: '',
            },
            mealInfo: {
                storeName:'',
                storeCode:'',
                mealName:'',
                mealSalePrice:'',
                validityDate:'',
                expirationDate:'',
                remark:''
            },
            $dataTable: null,
            modalModel: null,
            _$el: null,
            _$dataTable: null,
            id: null,
            flag: ''
        },
        created: function () {
        },
        ready: function () {
            this.drawTable();
        },
        methods: {
            query: function () {
                this.$dataTable.bootstrapTable('selectPage', 1);
            },
            drawTable: function () {
                var self = this;
                self.$dataTable = $('#dataTable', self._$el).bootstrapTable({
                    url: '/material/mealinfo/list',
                    method: 'get',
                    dataType: 'json',
                    cache: false,
                    pagination: true,
                    sidePagination: 'server',
                    queryParams: function (params) {
                        return _.extend({},
                            params, self.form);
                    },
                    mobileResponsive: true,
                    undefinedText: '-',
                    striped: true,
                    maintainSelected: true,
                    sortOrder: 'desc',
                    columns: [
                        {
                            field: "id",
                            visible: false
                        },
                        {
                            field: 'mealName',
                            title: '套餐名称',
                            align: 'center'
                        },
                        {
                            field: 'mealSalePrice',
                            title: '套餐售价',
                            align: 'center'
                        },
                        {
                            field: 'storeName',
                            title: '门店',
                            align: 'center'
                        },
                        {
                            field: 'noDeadline',
                            title: '有效期',
                            align: 'center',
                            formatter: function (val, row, index) {
                                var s = '';
                                if (val == '1') {
                                    s = '无期';
                                    return s;
                                }else{
                                    s = row.validityDate + '至' + row.expirationDate;
                                    return s;
                                }
                            }
                        },
                        {
                            field: 'mealStatus',
                            title: '状态',
                            align: 'center',
                            formatter: function (val, row, index) {
                                var s = '';
                                if (val == '1') {
                                    s = '开启';
                                    return s;
                                }else{
                                    s = '关闭';
                                    return s;
                                }
                            }
                        },
                        {
                            field: 'id',
                            title: '操作',
                            align: 'center',
                            formatter: function (value, row) {
                                var fragment = '';
                                if(row.mealStatus == '1'){

                                    fragment += ('<button   data-handle="edit" data-id="' + row.id + '" type="button" class="btn btn-xs btn-primary">编辑</button>&nbsp');

                                    fragment += ('<button   data-handle="close" data-id="' + row.id + '" data-mealstatus="' + row.mealStatus + '" type="button" class="btn btn-xs btn-danger">关闭</button>&nbsp');

                                    return fragment;
                                }else{
                                    fragment += ('<button   data-handle="close" data-id="' + row.id + '" data-mealstatus="' + row.mealStatus + '" type="button" class="btn btn-xs btn-danger">开启</button>&nbsp');
                                    return fragment;
                                }

                            }
                        }
                    ]
                });

                //编辑
                self.$dataTable.on('click', '[data-handle="edit"]',
                    function (e) {
                        var id = $(this).data("id");
                        self.flag = 1;
                        createOrEditModal(id,true);
                        e.stopPropagation();
                    }
                );
                //关闭-开启
                self.$dataTable.on('click', '[data-handle="close"]', function (e) {
                    var id = $(this).data('id');
                    var mealStatus = $(this).data("mealstatus");
                    var title='';
                    var text='';
                    if(mealStatus == 1){
                        title='套餐关闭'
                        text='确定要将该套餐状态置为关闭吗？'
                    }
                    if(mealStatus == 0){
                        title='套餐开启'
                        text='确定要将该套餐状态置为开启吗？'
                    }

                    swal({
                        title: title,
                        text: text,
                        type: 'info',
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        showCancelButton: true,
                        showConfirmButton: true,
                        showLoaderOnConfirm: true,
                        confirmButtonColor: '#ed5565',
                        closeOnConfirm: false
                    }, function () {
                        self.$http.get('/material/mealinfo/'+ id +'/changestatus').then(function (res) {
                            if (res.data.code == 1) {
                                self.$toastr.success('操作成功');
                                self.$dataTable.bootstrapTable('refresh');
                            }
                        }).catch(function () {
                            self.$toastr.error('系统异常');
                        }).finally(function () {
                            swal.close();
                        });
                    });
                    e.stopPropagation();
                });

            },
            createBtnClickHandler: function () {
                this.flag = '';
                //新增
                createOrEditModal(null, false);
            }

        }
    });

    function createOrEditModal(id, isEdit) {

        var _modal = $('#contractModal').clone();
        var $el = _modal.modal({
            height: 300,
            width: 500,
            backdrop: 'static',
        });
        // 获取 node
        var el = $el.get(0);
        var isEdit = !!isEdit;
        var userVue = new Vue({
            el: el,
            //mixins: [RocoVueMixins.ModalMixin],
            $modal: $el,
            created: function () {
                this.findStoreList();
                this.isFlag = tt.flag;
            },
            ready: function () {
                this.activeDatepicker();
                this.findMealInfo();
            },
            data: {
                isFlag: '',
                storeList:[],
                mealInfo: {
                    storeName:'',
                    storeCode:'',
                    mealName:'',
                    mealSalePrice:'',
                    validityDate:'',
                    expirationDate:'',
                    remark:'',
                    mealStatus:1
                },
            },
            //正整数校验
            validators: {
                num: {
                    message: '请输入正确的金额格式，例（10或10.00）',
                    check: function (val) {
                        return /^([1-9]\d{0,9}|0)([.]?|(\.\d{1,2})?)$/.test(val);
                    }
                }
            },
            methods: {
                activeDatepicker: function () {
                    var self = this;
                    $(self.$els.validityDate).datetimepicker({
                        minView: "month", //选择日期后，不会再跳转去选择时分秒
                        format: "yyyy-mm-dd", //选择日期后，文本框显示的日期格式
                        language: 'zh-CN', //汉化
                        autoClose:true //选择日期后自动关闭
                    });
                    $(self.$els.expirationDate).datetimepicker({
                        minView: "month", //选择日期后，不会再跳转去选择时分秒
                        format: "yyyy-mm-dd", //选择日期后，文本框显示的日期格式
                        language: 'zh-CN', //汉化
                        autoClose:true //选择日期后自动关闭
                    })
                },

                //查询门店
                findStoreList: function () {
                    var self = this;
                    this.$http.get('/material/mealinfo/getstorelist').then(function (res) {
                        if (res.data.code == 1) {
                            self.storeList = res.data.data
                        }
                    }).finally(function () {
                    });
                },
                findMealInfo: function () {
                    if(id==undefined || id=='undefined'){
                    }else {
                        var self = this;
                        self.$http.get('/material/mealinfo/'+id+'/getmealinfobyid').then(function (res) {
                            if (res.data.code == 1) {
                                self.mealInfo = res.data.data;
                            }
                        }).catch(function () {

                        }).finally(function () {

                        });
                    }
                },
                insert: function () {
                    var self = this;
                    self.$validate(true,
                        function () {
                            if (self.$validation.valid) {
                                self.submitting = true;

                                self.$http.post('/material/mealinfo/saveorupdate', self.mealInfo).then(function (res) {
                                        if (res.data.code === '1') {
                                            Vue.toastr.success(res.data.message);
                                            $el.modal('hide');
                                            tt.$dataTable.bootstrapTable('refresh');
                                            self.$destroy();
                                        }
                                    },
                                    function (error) {
                                        Vue.toastr.error(error.responseText);
                                    }).catch(function () {
                                }).finally(function () {
                                    self.submitting = false;
                                });
                            }
                        })
                }
            },
        });
        // 创建的Vue对象应该被返回
        return userVue;
    }
})();