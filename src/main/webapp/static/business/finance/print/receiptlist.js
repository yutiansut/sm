+(function () {
    $('#financePrintMenu').addClass('active');
    $('#receiptPrintMenu').addClass('active');
    var vueIndex = new Vue({
        el: '#container',
        data: {
            breadcrumbs: [{
                path: '/',
                name: '主页'
            }, {
                path: '',
                name: '财务打印',
            }, {
                path: '/',
                name: '收据打印',
                active: true
            }],
            stageType:{},
            form: {
                keyword: '',
                paystartTime:'',
                payendTime:'',
                contractCode:'',
                customerName:'',
                customerMobile:'',
                creator:''
            },
            $dataTable: null,
            _$el: null,
            pringIds: [],
            btnClass: 'btn-default',
            btnDisabled: true,
            //打印的门店
            printStoreCode: ''
        },
        created: function () {
        },
        ready: function () {
            this.activeDatepiker();
            this.drawTable();
            this.findPrintStoreAbled();
        },
        methods: {
            activeDatepiker: function () {
                var self = this;
                $(this.$els.paystartTime).datetimepicker({
                    format: 'yyyy-mm-dd',
                    autoSize: true,
                    clearBtn:true
                });
                $(this.$els.payendTime).datetimepicker({
                    format: 'yyyy-mm-dd',
                    autoSize: true,
                    clearBtn:true
                });
            },
            query: function () {
                if(this.form.paystartTime > this.form.payendTime){
                    this.$toastr.error('您选择的开始时间不能大于结束时间！');
                }else {
                    this.pringIds = [];
                    this.$dataTable.bootstrapTable('selectPage', 1);
                }
            },
            //查询打印的门店
            findPrintStoreAbled: function () {
                var self = this;
                self.$http.get("/finance/print/getstorecode/" + MdniUser.storeCode, {
                     }).then(function (res) {
                    if (res.data.code == 1) {
                        self.printStoreCode = res.data.data.storeCode;
                    }
                });
            },
            drawTable: function () {
                var self = this;
                self.$dataTable = $('#dataTable', self._$el).bootstrapTable({
                    url: '/finance/print/findreceiptall',
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
                    columns: [{
                        checkbox: true,
                        align: 'center',
                    }, {
                        field: 'id',
                        visible: false
                    }, {
                        field: 'contractCode',
                        title: '项目编号',
                        align: 'center'
                    }, {
                        field: 'secondContact',
                        title: '第二联系人姓名',
                        align: 'center'
                    }, {
                        field: 'secondContactMobile',
                        title: '第二联系人电话',
                        align: 'center'
                    }, {
                        field: 'itemName',
                        title: '收款阶段',
                        align: 'center'
                    }, {
                        field: 'payManualFlag',
                        title: '款项类别',
                        align: 'center'
                    }, {
                        field: 'payerName',
                        title: '交款人姓名',
                        align: 'center'
                    }, {
                        field: 'payerMobile',
                        title: '交款人电话',
                        align: 'center'
                    }, {
                        field: 'payTime',
                        title: '收款日期',
                        align: 'center',
                        formatter: function (value, row) {
                            if(value){
                                var div = "<div style='width:100px;'>"+value+"</div>";
                                return div;
                            }
                            return "-";
                        }
                    }, {
                        field: 'paymethodName',
                        title: '交款方式',
                        align: 'center',
                        formatter: function (value, row) {
                            if(value){
                                var div = "<div style='width:100px;'>"+value+"</div>";
                                return div;
                            }
                            return "-";
                        }
                    }, {
                        field: 'receiptNum',
                        title: '收据号',
                        align: 'center'
                    }, {
                        field: 'expectReceived',
                        title: '应收总金额',
                        align: 'center'
                    }, {
                        field: 'actualReceived',
                        title: '实收金额',
                        align: 'center'
                    }, {
                        field: 'balance',
                        title: '应/实收差额',
                        align: 'center'
                    }, {
                        field: 'creator',
                        title: '操作人',
                        align: 'center'
                    }, {
                        field: '',
                        title: '操作',
                        align: 'center',
                        formatter: function (value, row, index) {
                            var fragment = '';
                            fragment += ('<button data-handle="print" data-id="' + row.id + '" type="button" class="m-r-xs btn btn-xs btn-warning">打印</button>');
                            return fragment;
                        }
                    }]
                });

                // 打印
                self.$dataTable.on('click', '[data-handle="print"]', function (e) {
                    //查看当前登录人所在门店是否存在
                    var self = this;
                    if(vueIndex.printStoreCode == '' || vueIndex.printStoreCode == null){
                        vueIndex.$toastr.error('未对该门店指定打印配置，无法打印！');
                    }else {
                        var id = $(this).data("id");
                        var params = {
                            id: id
                        }
                        var url = '/finance/print/singleprint';
                        MdniUtils.locationHrefToServer(url, params, true);
                        vueIndex.$dataTable.bootstrapTable('refresh');
                        /*var newWindow = window.open('/finance/print/singleprint/' + id );
                         newWindow.window.print();*/
                    }
                });

                //复选框 --- 选中一行
                self.$dataTable.on('check.bs.table', function (row, data) {
                    self.pringIds.push(data.id);
                });

                //复选框 --- 取消选中一行
                self.$dataTable.on('uncheck.bs.table', function (row, data) {
                    self.pringIds.remove(data.id);
                });

                //复选框 --- 选中全部
                self.$dataTable.on('check-all.bs.table', function (row, data) {
                    self.pringIds = [];
                    if(data && data.length > 0){
                        data.forEach(function (item) {
                            self.pringIds.push(item.id);
                        });
                    }
                });
                //复选框 --- 取消选中全部
                self.$dataTable.on('uncheck-all.bs.table', function (row, data) {
                    self.pringIds = [];
                });
            },
            printMany:function(){
                var self = this;
                if(vueIndex.printStoreCode == '' || vueIndex.printStoreCode == null){
                    self.$toastr.error('打印失败，请联系管理员！');
                }else {
                    var ids = self.pringIds;
                    var params = {
                        ids: ids
                    }
                    var url = '/finance/print/multiprint';
                    MdniUtils.locationHrefToServer(url, params ,true);
                    setTimeout(function () {
                        self.$dataTable.bootstrapTable('refresh');
                        self.pringIds = [];
                    },2000);
                }
            }
        },
        watch: {
            pringIds: {
                //当有值时, 显示批量打印
                handler: function (newVal, oldVal) {
                    var self = this;
                    if(newVal){
                        if(newVal.length > 0){
                            self.btnClass = 'btn-danger';
                            self.btnDisabled = false;
                        }else{
                            self.btnClass = 'btn-default';
                            self.btnDisabled = true;
                        }
                    }else{
                        self.btnClass = 'btn-default';
                        self.btnDisabled = true;
                    }
                },
                deep: true
            },




        }
    });
})();