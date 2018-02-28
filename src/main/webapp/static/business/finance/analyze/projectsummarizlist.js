var vueIndex;
var manyModelVue;
+(function () {
    $('#financeQueryMenu').addClass('active');
    $('#projectsummarizMenu').addClass('active');
    vueIndex = new Vue({
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
                name: '项目汇总查询',
                active: true
            }],
            form: {
                keyword: '',
                contractCode:'',
                customerName:'',
                customerMobile:'',
                orderFlowStatus:'',
                stratDate:'',
                endDate:'',
                contractCodeMany: '',
            },
            $dataTable: null,
            _$el: null,
            projectPayplanStageList:[],
            payplanStageList:[],
            btnDisabled: true,
            columnShowObj: {
                firstExpectShowFlag: false,
                firstActualTotalShowFlag: false,
                oweFirstShowFlag: false,
                mediumExpectShowFlag: false,
                mediumActualTotalShowFlag: false,
                oweMediumShowFlag: false,
                finalExpectShowFlag: false,
                finalActualTotalShowFlag: false,
                oweFinalShowFlag: false
            },
            //列隐藏数据 加载完成标记
            loadAbledStoreFlag: true
        },
        created: function () {
        },
        ready: function () {
            this.activeDatepiker();
            this.findAbleColumn();
        },
        methods: {
            findAbleColumn: function () {
                var self = this;
                self.$http.get("/finance/analyze/getstagetemplatecode?storeCode="+MdniUser.storeCode, {
                }).then(function (res) {
                    if (res.data.code == 1) {
                        self.payplanStageList = res.data.data;
                        for(i = 0; i < self.payplanStageList.length; i++){
                            if(self.payplanStageList[i]){
                                if(self.payplanStageList[i].stageTemplateCode == 'NODE_FIRST' ){
                                    self.columnShowObj.firstExpectShowFlag = true;
                                    self.columnShowObj.firstActualTotalShowFlag = true;
                                    self.columnShowObj.oweFirstShowFlag = true;
                                }else if(self.payplanStageList[i].stageTemplateCode == 'NODE_MEDIUM'){
                                    self.columnShowObj.mediumExpectShowFlag = true;
                                    self.columnShowObj.mediumActualTotalShowFlag = true;
                                    self.columnShowObj.oweMediumShowFlag = true;
                                }else if(self.payplanStageList[i].stageTemplateCode == 'NODE_FINAL'){
                                    self.columnShowObj.finalExpectShowFlag = true;
                                    self.columnShowObj.finalActualTotalShowFlag = true;
                                    self.columnShowObj.oweFinalShowFlag = true;
                                }
                            }
                        }
                        self.loadAbledStoreFlag = false;
                    }
                });
            },
            activeDatepiker: function () {
                var self = this;
                $(self.$els.stratDate).datetimepicker({
                    startView: 2,//启始视图显示年视图
                    clearBtn:true
                })
                $(self.$els.endDate).datetimepicker({
                    startView: 2,//启始视图显示年视图
                    clearBtn:true
                })
            },
            query: function () {
                if(this.form.stratDate > this.form.endDate){
                    this.$toastr.error('您选择的开始时间不能大于结束时间！');
                }else{
                    this.btnDisabled = false;
                    if(!this.$dataTable){
                        this.drawTable();
                    }else{
                        this.$dataTable.bootstrapTable('selectPage', 1);
                    }
                }
                if(manyModelVue){
                    manyModelVue.contractCodeMany = "";
                }
            },
            drawTable: function () {
                var self = this;
                self.$dataTable = $('#dataTable', self._$el).bootstrapTable({
                    url: '/finance/analyze/projectsummarizfindall',
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
                        field: 'contractCode',
                        title: '项目编号',
                        align: 'center'
                    }, {
                        field: 'customerName',
                        title: '客户',
                        align: 'center',
                        formatter: function (value, row) {
                            if(value){
                                var div = "<div style='width:100px;'>"+value+"</div>";
                                return div;
                            }
                            return "-";
                        }
                    }, {
                        field: 'constructExpectAmount',
                        title: '合同金额',
                        align: 'center',
                        formatter: function (value, row) {
                            if(value != null){
                                return parseFloat(value).toFixed(2);
                            }else {
                                return parseFloat(0).toFixed(2);
                            }
                        }
                    }, {
                        field: 'modifyAmount',
                        title: '拆改费',
                        align: 'center',
                        formatter: function (value, row) {
                            if(value != null){
                                return parseFloat(value).toFixed(2);
                            }else {
                                return parseFloat(0).toFixed(2);
                            }
                        }
                    }, {
                        field: 'addChangeAmount',
                        title: '增项',
                        align: 'center',
                        formatter: function (value, row) {
                            if(value != null){
                                return parseFloat(value).toFixed(2);
                            }else {
                                return parseFloat(0).toFixed(2);
                            }
                        }
                    }, {
                        field: 'fewChangeAmount',
                        title: '减项',
                        align: 'center',
                        formatter: function (value, row) {
                            if(value != null){
                                return parseFloat(value).toFixed(2);
                            }else {
                                return parseFloat(0).toFixed(2);
                            }
                        }
                    }, {
                        field: 'reparationAmount',
                        title: '赔款',
                        align: 'center',
                        formatter: function (value, row) {
                            if(value != null){
                                return parseFloat(value).toFixed(2);
                            }else {
                                return parseFloat(0).toFixed(2);
                            }
                        }
                    }, {
                        field: '',
                        title: '应收款合计',
                        align: 'center',
                        formatter: function (value,row) {
                            var modifyAmounts = row.modifyAmount ? row.modifyAmount : 0;
                            var accountsTotal = row.constructExpectAmount + modifyAmounts + row.addChangeAmount - (row.fewChangeAmount * -1);
                            return parseFloat(accountsTotal).toFixed(2);
                        }
                    }, {
                        field: 'accumulativeTotal',
                        title: '累计收款金额',
                        align: 'center',
                        formatter: function (value, row) {
                            if(value != null){
                                return parseFloat(value).toFixed(2);
                            }else {
                                return parseFloat(0).toFixed(2);
                            }
                        }
                    }, {
                        field: '',
                        title: '未收款金额',
                        align: 'center',
                        formatter: function (value,row) {
                            var modifyAmounts = row.modifyAmount ? row.modifyAmount : 0;
                            var accountsTotal = row.constructExpectAmount + modifyAmounts + row.addChangeAmount - (row.fewChangeAmount * -1);
                            var notAmount = accountsTotal - row.accumulativeTotal;
                            return parseFloat(notAmount).toFixed(2);
                        }
                    }, {
                        field: 'orderFlowStatus',
                        title: '订单状态',
                        align: 'center',
                        formatter: function (value) {
                            switch (value) {
                                case 'STAY_TURN_DETERMINE':
                                    return '<div style="width:100px;">待转大定</div>';
                                case 'SUPERVISOR_STAY_ASSIGNED':
                                    return '<div style="width:100px;">督导组长待分配</div>';
                                case 'DESIGN_STAY_ASSIGNED':
                                    return '<div style="width:100px;">设计待分配</div>';
                                case 'APPLY_REFUND':
                                    return '<div style="width:100px;">申请退回</div>';
                                case 'STAY_DESIGN':
                                    return '<div style="width:100px;">待设计</div>';
                                case 'STAY_SIGN':
                                    return '<div style="width:100px;">待签约</div>';
                                case 'STAY_SEND_SINGLE_AGAIN':
                                    return '<div style="width:100px;">待重新派单</div>';
                                case 'STAY_CONSTRUCTION':
                                    return '<div style="width:100px;">待施工</div>';
                                case 'ON_CONSTRUCTION':
                                    return '<div style="width:100px;">施工中</div>';
                                case 'PROJECT_COMPLETE':
                                    return '<div style="width:100px;">已竣工</div>';
                                case 'ORDER_CLOSE':
                                    return '<div style="width:100px;">订单关闭</div>';
                                default:
                                    return '-';
                            }
                        }
                    }, {
                        field: 'depositActualTotalReceived',
                        title: '定金',
                        align: 'center',
                        formatter: function (value, row) {
                            if(value != null){
                                return parseFloat(value).toFixed(2);
                            }else {
                                return parseFloat(0).toFixed(2);
                            }
                        }
                    }, {
                        field: 'firstExpectReceived',
                        title: '应收首期',
                        visible: self.columnShowObj.firstExpectShowFlag,
                        align: 'center',
                        formatter: function (value, row) {
                            if(value != null){
                                return parseFloat(value).toFixed(2);
                            }else {
                                return parseFloat(0).toFixed(2);
                            }
                        }
                    }, {
                        field: 'firstActualTotalReceived',
                        title: '实收首期',
                        visible: self.columnShowObj.firstActualTotalShowFlag,
                        align: 'center',
                        formatter: function (value, row) {
                            if(value != null){
                                return parseFloat(value).toFixed(2);
                            }else {
                                return parseFloat(0).toFixed(2);
                            }
                        }
                    }, {
                        field: 'oweFirst',
                        title: '欠首期款',
                        visible: self.columnShowObj.oweFirstShowFlag,
                        align: 'center',
                        formatter: function (value, row) {
                            if(value != null){
                                return parseFloat(value).toFixed(2);
                            }else {
                                return parseFloat(0).toFixed(2);
                            }
                        }
                    }, {
                        field: 'mediumExpectReceived',
                        title: '应收中期',
                        visible: self.columnShowObj.mediumExpectShowFlag,
                        align: 'center',
                        formatter: function (value, row) {
                            if(value != null){
                                return parseFloat(value).toFixed(2);
                            }else {
                                return parseFloat(0).toFixed(2);
                            }
                        }
                    }, {
                        field: 'mediumActualTotalReceived',
                        title: '实收中期',
                        visible: self.columnShowObj.mediumActualTotalShowFlag,
                        align: 'center',
                        formatter: function (value, row) {
                            if(value != null){
                                return parseFloat(value).toFixed(2);
                            }else {
                                return parseFloat(0).toFixed(2);
                            }
                        }
                    }, {
                        field: 'oweMedium',
                        title: '欠中期款',
                        visible: self.columnShowObj.oweMediumShowFlag,
                        align: 'center',
                        formatter: function (value, row) {
                            if(value != null){
                                return parseFloat(value).toFixed(2);
                            }else {
                                return parseFloat(0).toFixed(2);
                            }
                        }
                    }, {
                        field: 'finalExpectReceived',
                        title: '应收尾期',
                        visible: self.columnShowObj.finalExpectShowFlag,
                        align: 'center',
                        formatter: function (value, row) {
                            if(value != null){
                                return parseFloat(value).toFixed(2);
                            }else {
                                return parseFloat(0).toFixed(2);
                            }
                        }
                    }, {
                        field: 'finalActualTotalReceived',
                        title: '实收尾期',
                        visible: self.columnShowObj.finalActualTotalShowFlag,
                        align: 'center',
                        formatter: function (value, row) {
                            if(value != null){
                                return parseFloat(value).toFixed(2);
                            }else {
                                return parseFloat(0).toFixed(2);
                            }
                        }
                    }, {
                        field: 'oweFinal',
                        title: '欠尾期款',
                        visible: self.columnShowObj.oweFinalShowFlag,
                        align: 'center',
                        formatter: function (value, row) {
                            if(value != null){
                                return parseFloat(value).toFixed(2);
                            }else {
                                return parseFloat(0).toFixed(2);
                            }
                        }
                    },{
                        field: 'chargebackAmount',
                        title: '退单扣除费用',
                        align: 'center',
                        formatter: function (value, row) {
                            if(value != null){
                                return parseFloat(value).toFixed(2);
                            }else {
                                return parseFloat(0).toFixed(2);
                            }
                        }
                    },{
                        field: 'buildArea',
                        title: '面积',
                        align: 'center',
                    },{
                        field: 'serviceName',
                        title: '客服姓名',
                        align: 'center'
                    },{
                        field: 'designer',
                        title: '设计师姓名',
                        align: 'center'
                    },{
                        field: 'projectManager',
                        title: '项目经理姓名',
                        align: 'center'
                    },{
                        field: 'contractStartTime',
                        title: '合同开工日期',
                        align: 'center',
                        formatter: function(value,row){
                            if(row.contractStartTime != null){
                                return moment(row.contractStartTime).format('YYYY-MM-DD');
                            }else{
                                return '-';
                            }
                        }
                    },{
                        field: 'contractCompleteTime',
                        title: '合同竣工日期',
                        align: 'center',
                        formatter: function(value,row){
                            if(row.contractCompleteTime != null){
                                return moment(row.contractCompleteTime).format('YYYY-MM-DD');
                            }else{
                                return '-';
                            }
                        }
                    },{
                        field: 'startConstructionTime',
                        title: '实际开工时间',
                        align: 'center',
                        formatter: function(value,row){
                            if(row.startConstructionTime != null){
                                return moment(row.startConstructionTime).format('YYYY-MM-DD');
                            }else{
                                return '-';
                            }
                        }
                    },{
                        field: 'completeConstructionTime',
                        title: '实际竣工日期',
                        align: 'center',
                        formatter: function(value,row){
                            if(row.completeConstructionTime != null){
                                return moment(row.completeConstructionTime).format('YYYY-MM-DD');
                            }else{
                                return '-';
                            }
                        }
                    },{
                        field: 'mealName',
                        title: '套餐',
                        align: 'center',
                        formatter: function (value, row) {
                            if(value){
                                var div = "<div style='width:130px;'>"+value+"</div>";
                                return div;
                            }
                            return "-";
                        }
                    }]
                });
            },

            //精准查询
            searchmanyBtn:function (e){
                searchmanyModel ({},true);
                e.stopPropagation();
            },

            //导出
            exportData: function () {
                var self = this;
                if(manyModelVue){
                    var newCodeMany = "";
                    var contractCodeMany = manyModelVue.contractCodeMany ? manyModelVue.contractCodeMany : '';
                    if( contractCodeMany || (contractCodeMany && contractCodeMany.indexOf("\n") != -1)){
                        var contractCodeArr = contractCodeMany.split("\n");
                        for(var i = 0; i < contractCodeArr.length; i++){
                            newCodeMany += contractCodeArr[i] + ",";
                        }
                    }
                    window.location.href ='/finance/analyze/exportprojectsummariz?keyword=' + self.form.keyword
                        + '&contractCode=' + self.form.contractCode
                        + '&customerName=' + self.form.customerName
                        + '&customerMobile=' + self.form.customerMobile
                        + '&orderFlowStatus=' + self.form.orderFlowStatus
                        + '&contractCodeMany=' + newCodeMany
                        + '&startDate=' + self.form.startDate
                        + '&endDate=' + self.form.endDate;
                }else{
                    window.location.href ='/finance/analyze/exportprojectsummariz?keyword=' + self.form.keyword
                        + '&contractCode=' + self.form.contractCode
                        + '&customerName=' + self.form.customerName
                        + '&customerMobile=' + self.form.customerMobile
                        + '&orderFlowStatus=' + self.form.orderFlowStatus
                        + '&startDate=' + self.form.stratDate
                        + '&endDate=' + self.form.endDate;
                }

            }
        },

    });

    function searchmanyModel (model,isEdit){
        var _modal = $('#searchmanyModel').clone();
        var $el = _modal.modal({
            height: 300,
            width: 600
        });
        $el.on('shown.bs.modal', function () {
            // 获取 node
            var el = $el.get(0);
            isEdit = !!isEdit;
            // 创建 Vue 对象编译节点
            manyModelVue = new Vue({
                el: el,
                // 模式窗体必须引用 ModalMixin
                mixins: [MdniVueMixins.ModalMixin],
                $modal: $el, // 模式窗体 jQuery 对象
                data: {
                    contractCodeMany: '',
                    submitBtnClick:false,
                    disabled :false,
                    _$el: null
                },

                methods: {
                    saveOrUpdate: function () {
                        var self = this;
                        self.submitBtnClick = true;
                        self.disabled = true;
                        vueIndex.form.contractCodeMany = self.contractCodeMany;
                        vueIndex.$dataTable.bootstrapTable('selectPage', 1);
                        vueIndex.form.contractCodeMany = "";
                        $el.modal('hide');
                    }
                },
                created: function () {
                },
                ready: function () {
                }
            });
            // 创建的Vue对象应该被返回
            return manyModelVue ;
        });
    }
})();