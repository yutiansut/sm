+(function () {
    $('#financeQueryMenu').addClass('active');
    $('#compositeMenu').addClass('active');
    var vueIndex = new Vue({
        el: '#container',
        data: {
            breadcrumbs: [{
                path: '/',
                name: '主页'
            }, {
                path: '',
                name: '财务查询'
            }, {
                path: '/',
                name: '综合查询',
                active: true
            }],
            stageType:{},
            form: {
                keyword: '',
                paystartTime:'',
                payendTime:'',
                contractCode:'',
                customerName:'',
                secondContact:'',
                designer:'',
                serviceName:'',
                serviceMobile:'',
                financeStatus:'',
                orderFlowStatus:'',
                stageFinished:'',
                paymethodName:'',
                templateStageId:''
            },
            $dataTable: null,
            _$el: null
        },
        created: function () {
        },
        ready: function () {
            this.activeDatepiker();
            this.fetchStageType();
            this.drawTable();
        },
        methods: {
            //查询交款阶段类型
            fetchStageType: function () {
                var self = this;
                self.$http.get("/finance/analyze/getstagetype?storeCode="+MdniUser.storeCode).then(function (res) {
                    if (res.data.code == 1) {
                        self.stageType = res.data.data;
                        console.log(self.stageType)
                    }
                })
            },
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
                    this.$dataTable.bootstrapTable('selectPage', 1);
                }
            },
            drawTable: function () {
                var self = this;
                self.$dataTable = $('#dataTable', self._$el).bootstrapTable({
                    url: '/finance/analyze/projectcompositefindall',
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
                        field: '',
                        title: '客户/项目编号',
                        align: 'center',
                        formatter: function (value,row) {
                            if (row.customerName != null || row.contractCode != null || row.customeMobile != null) {
                                return '<span>'+row.customerName+'</span>&nbsp;&nbsp;<span>'+row.customeMobile+'</span><br/><span>'+row.contractCode+'</span>'
                            }else{
                                return '-';
                            }
                        }
                    }, {
                        field: '',
                        title: '工程地址/面积(m²)',
                        align: 'center',
                        formatter: function (value, row) {
                            if (row.addressProvince != null || row.addressCity != null ||row.addressArea != null || row.buildArea != null) {
                                if(row.addressProvince == row.addressCity){
                                    return '<span>'+row.addressCity+row.addressArea+row.houseAddr+'</span><br/><span>'+row.buildArea+'</span>'
                                }else{
                                    return '<span>'+row.addressProvince+row.addressCity+row.addressArea+row.houseAddr+'</span><br/><span>'+row.buildArea+'</span>'
                                }
                            }
                            return '-';
                        }
                    }, {
                        field: 'orderFlowStatus',
                        title: '订单状态',
                        align: 'center',
                        formatter: function (value) {
                            switch (value) {
                                case 'STAY_TURN_DETERMINE':
                                    return '待转大定';
                                case 'SUPERVISOR_STAY_ASSIGNED':
                                    return '督导组长待分配';
                                case 'DESIGN_STAY_ASSIGNED':
                                    return '设计待分配';
                                case 'APPLY_REFUND':
                                    return '申请退回';
                                case 'STAY_DESIGN':
                                    return '待设计';
                                case 'STAY_SIGN':
                                    return '待签约';
                                case 'STAY_SEND_SINGLE_AGAIN':
                                    return '待重新派单';
                                case 'STAY_CONSTRUCTION':
                                    return '待施工';
                                case 'ON_CONSTRUCTION':
                                    return '施工中';
                                case 'PROJECT_COMPLETE':
                                    return '已竣工';
                                case 'ORDER_CLOSE':
                                    return '订单关闭';
                            }
                        }
                    }, {
                        field: 'itemName',
                        title: '财务阶段',
                        align: 'center',
                    }, {
                        field: 'stageFinished',
                        title: '已签定金合同',
                        align: 'center',
                        formatter: function(value){
                            if(value == 0 || value == null){
                                return '否';
                            }else{
                                return '是';
                            }
                        }
                    }, {
                        field: 'actualReceived',
                        title: '收款金额',
                        align: 'center',
                        formatter: function (value, row) {
                            if(value != null){
                                return parseFloat(value).toFixed(2);
                            }else {
                                return '0.00';
                            }
                        }
                    }, {
                        field: 'payTime',
                        title: '收款日期',
                        align: 'center',
                        formatter: function(value,row){
                          if(row.payTime != null){
                              return moment(row.payTime).format('YYYY-MM-DD HH:mm:SS');
                          }else{
                              return '-';
                          }
                        }
                    }, {
                        field: 'paymethodName',
                        title: '收款方式',
                        align: 'center',
                    },{
                        field: '', //将id作为排序时会和将id作为操作field内容冲突，导致无法排序
                        title: "操作",
                        align: 'center',
                        formatter: function (value, row, index) {
                            var fragment = '';
                            fragment += ('<button data-handle="detail" data-contract-uuid="' + row.contractUuid + '" type="button" class="m-r-xs btn btn-xs btn-primary">查看</button>');
                            return fragment;
                        }
                    }]
                });

                //查看
                self.$dataTable.on('click', '[data-handle="detail"]',
                    function (e) {
                        var contractUuid = $(this).data("contractUuid");
                        window.location.href ='/finance/payment/index?id=' + contractUuid + '&flag='+ 1;
                    }
                );
            },
            //导出
            exportData: function () {
                var self = this;
                window.location.href ='/finance/analyze/exportprojectcomposite?keyword=' + self.form.keyword
                    + '&contractCode=' + self.form.contractCode
                    + '&paystartTime=' + self.form.paystartTime
                    + '&payendTime=' + self.form.payendTime
                    + '&customerName=' + self.form.customerName
                    + '&secondContact=' + self.form.secondContact
                    + '&serviceName=' + self.form.serviceName
                    + '&serviceMobile=' + self.form.serviceMobile
                    + '&financeStatus=' + self.form.financeStatus
                    + '&orderFlowStatus=' + self.form.orderFlowStatus
                    + '&stageFinished=' + self.form.stageFinished
                    + '&paymethodName=' + self.form.paymethodName
                    + '&templateStageId=' + self.form.templateStageId
                    + '&designer=' + self.form.designer;

            }
        }
    });
})();