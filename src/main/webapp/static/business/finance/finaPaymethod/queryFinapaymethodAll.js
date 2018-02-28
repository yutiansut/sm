var tt = null;
+(function () {
    $('#financeManageMenu').addClass('active');
    $('#queryfinapayme').addClass('active');
    tt = new Vue({
        el: '#container',
        data: {
            breadcrumbs: [{
                path: '/',
                name: '主页'
            },
                {
                    path: '/finance/paymethod/list',
                    name: '财务配置',
                    active: true
                }],
            form: {
                storeCode: '',
            },
            finaPaymethod: {
                tagName:'',
                operator:'',
                operateTime:'',
                describtion:'',
                itemCount:''
            },
            $dataTable: null,
            modalModel: null,
            _$el: null,
            _$dataTable: null,
            id: null,
            allStores:null,
            options:null
        },
        created: function () {
        },
        ready: function () {
            this.drawTable();
            this.fetchAllStores();

        },
        methods: {
            //门店
            fetchAllStores: function () {
                var self = this;
                self.$http.get('/material/prodsku/storelist').then(function (res) {
                    if (res.data.code == 1) {
                        self.allStores = res.data.data;
                    }
                }).catch(function () {
                }).finally(function () {
                })
            },
            //这个方法不知道怎么用的,当前页面不知道在哪使用的就没有删除(注意)
            query: function () {
                this.$dataTable.bootstrapTable('selectPage', 1);
            },
            //列表中的数据
            drawTable: function () {
                var self = this;
                self.$dataTable = $('#dataTable', self._$el).bootstrapTable({
                    url: '/finance/paymethod/list',
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
                        /*{
                         title: '序号',//标题  注释了这个列表不需要序号暂时不需要
                         align: 'center',
                         formatter: function (value, row, index) {
                         return index+1;
                         }
                         },*/
                        {
                            field: 'storeName',
                            title: '门店',
                            align: 'center'
                        },
                        {
                            field: 'methodName',
                            title: '付款方式',
                            align: 'center'
                        },
                        {
                            field: 'methodType',
                            title: '类型',
                            align: 'center'
                        },
                        {
                            field: 'ifCustome',
                            title: '有特殊配置',
                            align: 'center',

                            formatter: function (value, row) {
                                if(value==1){
                                    return "是";
                                }else if (value==0){
                                    return "否";
                                }
                            }

                        },
                        {
                            field: 'methodStatus',
                            title: '状态',
                            align: 'center'
                        },
                        {
                            field: 'id',
                            title: '操作',
                            align: 'center',
                            formatter: function (value, row) {
                                var fragment = '';

                                fragment += ('<button   data-handle="edit" data-id="' + row.id + '" type="button" class="btn btn-xs btn-primary">编辑</button>&nbsp');
                                if(row.ifCustome==1)      {
                                    fragment += ('<button   data-handle="close" data-id="' + row.id + '" type="button" class="btn btn-xs btn-danger">特殊配置</button>&nbsp');
                                }
                                return fragment;
                            }
                        }
                    ]
                });
                //编辑
                self.$dataTable.on('click', '[data-handle="edit"]',
                    function (e) {
                        var id = $(this).data("id");
                        createOrEditModal(id,true);
                        e.stopPropagation();
                    }
                );

                // 特殊配置
                self.$dataTable.on('click', '[data-handle="close"]', function (e) {
                    var id = $(this).data('id');
                    window.location.href = ctx + '/finance/finaPaymethod/particularConfig?methodId=' + id;
                    e.stopPropagation();
                });
            },
            createBtnClickHandler: function (e) {
                //新增

               var storeCode= $("#storeCode").val();
               var xinzhengHandlerId= $("#xinzhengHandlerId").val();
                   createOrEditModal(xinzhengHandlerId, false);
            }
        }
    });

    function createOrEditModal(id, isEdit) {
        var _modal = $('#contractModal').clone();
        var $el = _modal.modal({
            height: 600,
            width: 650,
        });
        // 获取 node
        var el = $el.get(0);
        isEdit = !!isEdit;
        var userVue = new Vue({
            el: el,

            //mixins: [RocoVueMixins.ModalMixin],
            $modal: $el,
            created: function () {

            },
            ready: function () {
                this.findSingleTag(id);
                this.fetchMethodType(id);

            },
            data: {
                finaPaymethod: {
                    methodName:'',//支付方式
                    costRate:'',//手续费率
                    checkedNames: [],//
                    methodCode:'',//支付方式编号
                    methodStatus:'',//状态
                    storeIds: [],//门店id集合
                    minCostfee:'',//最低手续费
                    methodType:'',//类型
                    ifCustome:'',//是否进行了定制
                    maxCostfee:'',//最高手续费
                    ablestageTemplateCode:''//适用阶段
                },
                methodStatus: null,
                methodType: null,
                storeinfo: null,
                xinzeng: id  //新增的标志
            },
            //校验的属性
            validators: {
                num: {
                    message: '请输入正确的金额格式，例（10或10.00..）',
                    check: function (val) {
                        // return /^[+]{0,1}(\d+)$|^[+]{0,1}(\d+\.\d+)$/.test(val);
                        // return /^0|[0-9]+(.[0-9]{1,2})?$/.test(val);
                        return /^([1-9]\d{0,9}|0)([.]?|(\.\d{1,9})?)$/.test(val);
                    }
                }
            },
            methods: {
                //获取类型和状态的ajax
                fetchMethodType: function () {
                    var self = this;
                   //  if(id==undefined || id=='undefined'||id==null||id==''||id==9999999999) {
                        self.$http.get('/finance/paymethod/fetchmethodtype').then(function (res) {
                            if (res.data.code == 1) {
                                self.methodType = res.data.data;
                            }
                        }).catch(function () {
                        }).finally(function () {
                        })

                    self.$http.get('/finance/paymethod/fetchmethodstatus').then(function (res) {
                        if (res.data.code == 1) {
                            self.methodStatus = res.data.data;
                        }
                    }).catch(function () {
                    }).finally(function () {
                    })
                    //获取所有门店
                    self.$http.get('/finance/paymethod/querystoreall').then(function (res) {
                        if (res.data.code == 1) {
                            self.storeinfo = res.data.data;
                        }
                    }).catch(function () {
                    }).finally(function () {
                    })

                  //  }
                },


                //插入通用表的方法
                insert: function () {
                    var self = this;


                    var checkboxvalue=  $('#chackbox1 input[type=checkbox]:checked').length
                    if(checkboxvalue==0) {
                        alert("请选择门店");
                        return false;
                    }

                    self.$validate(true,
                        function () {
                            if (self.$validation.valid) {
                                self.submitting = true;

                                self.$http.post('/finance/paymethod/inputpaymethod', self.finaPaymethod).then(function (res) {
                                        if (res.data.code === '1') {
                                            Vue.toastr.success(res.data.message);
                                            $el.modal('hide');
                                            tt.$dataTable.bootstrapTable('refresh');
                                            self.$destroy();
                                        }else{
                                            self.$toastr.error(res.data.message);
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
                },
                //回显
                findSingleTag: function (id) {
                    var self = this;
                    if(id==undefined || id=='undefined'||id==null||id==''||id==9999999999){
                    }else {
                        self.finaPaymethod.id=id;
                        self.$http.post('/finance/paymethod/querybyid', self.finaPaymethod ).then(function (res) {
                            if (res.data.code == 1) {
                                self.finaPaymethod = res.data.data;
                            }
                        }).catch(function () {

                        }).finally(function () {

                        });
                    }
                }

            },
        });
        // 创建的Vue对象应该被返回
        return userVue;
    }
})();