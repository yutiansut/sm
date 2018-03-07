var detail;
+(function () {
    $('#financeManageMenu').addClass('active');
    $('#paymentMenu').addClass('active');
    detail = new Vue({
        el: '#container',
        components: {
            'payment-info': PaymentInfo,
            'payment-plan': PaymentPlan,
            'change-record': ChangeRecord,
            'claim-record': ClaimRecord,
            'refund-record': RefundRecord,
            'back-record': BackRecord,
            'operation-record': OperationRecord
        },
        data: {
            //判断按钮显示标示
            flag:'',
            //当前活动中的tab页索引
            activeIndex: 0,
            index: 0,
            contractCode: '',
            contractUuid: '',
            // 面包屑
            breadcrumbs: [{
                path: '/',
                name: '主页'
            },{
                path: '/',
                name: '列表详情',
                active: true //激活面包屑的
            }],
            contractPayment: {
                // 项目编号
                contractCode: '',
                // 客户姓名
                customerName: '',
                // 客户电话
                customerMobile: '',
                // 客户来源
                incomeSource: '',
                // 户型
                layout: '',
                // 面积
                buildArea: '',
                // 客服姓名
                serviceName: '',
                // 签单时间
                createTime: '',
                // 参加活动
                activityName: '',
                // 套餐类型
                mealName: '',
                // 特殊优惠
                discountName: '',
                //客户地址
                houseAddr: '',
                //合同状态
                contractStatus: '',
                //可以结束收款的payid
                ableFinishPayid:'',
                //可以红冲的payid
                ableRcwPayid:''
            },
            finaProjectAccount:{
                //预定合同实收总金额
                depositTotalPayed: 0,
                //预定合同已抵扣金额
                depositTotalDeduct: 0,
                //拆改合同原始总金额
                modifyExpectAmount: 0,
                //拆改合同实收总金额
                modifyTotalPayed: 0,
                //施工合同原始总金额
                constructExpectAmount: 0,
                //施工合同实收总金额
                constructTotalPayed: 0,
                //变更总金额
                changeAmount: 0,
                //赔款总金额
                reparationAmount: 0
            },
            tabList: []
        },
        created: function () {
            this.contractUuid = DameiUtils.parseQueryString()['id'];
            this.flag = DameiUtils.parseQueryString()['flag'];
            if(!this.contractUuid){
                this.showModel();
            }else{
                this.fetchData();
                this.fetchFinaProjectAccount();
            }
            this.tabList.push({label: '交款信息(实际交款)'});
            this.tabList.push({label: '交款计划(明细)'});
            this.tabList.push({label: '变更记录'});
            this.tabList.push({label: '赔款记录'});
            this.tabList.push({label: '退款记录'});
            this.tabList.push({label: '退单申请记录'});
            this.tabList.push({label: '操作记录'});
        },
        ready: function () {
        },
        filters: {
            goDate: function (el) {
                if (el == null || el == '') {
                    return '-';
                } else {
                    return moment(el).format('YYYY-MM-DD');
                }
            },
            goOrigin: function (val) {
                if (val == 'A01') {
                    return '搜索引擎';
                } else if (val == 'A02') {
                    return '朋友圈'
                } else if (val == 'A05') {
                    return '报价器';
                } else if (val == 'A06') {
                    return '极有家'
                } else if (val == 'A07') {
                    return '大美官网';
                } else if (val == 'A11') {
                    return '新媒体1'
                } else if (val == 'A12') {
                    return '新媒体2';
                } else if (val == 'B01') {
                    return '天猫';
                } else if (val == 'C01') {
                    return '广播'
                } else if (val == 'B01') {
                    return '天猫';
                } else if (val == 'B03') {
                    return '京东'
                } else if (val == 'B04') {
                    return '百度糯米'
                } else if (val == 'B05') {
                    return '大众点评';
                } else if (val == 'B11') {
                    return '电商渠道1'
                } else if (val == 'B12') {
                    return '电商渠道2'
                } else if (val == 'C01') {
                    return '广播'
                } else if (val == 'C02') {
                    return '电视'
                } else if (val == 'C03') {
                    return '户外';
                } else if (val == 'C07') {
                    return '今日头条'
                } else if (val == 'C08') {
                    return '报纸';
                } else if (val == 'D01') {
                    return '市场地推';
                } else if (val == 'D05') {
                    return '市场电邀'
                } else if (val == 'E01') {
                    return '转介绍'
                } else if (val == 'E04') {
                    return '小美返单';
                } else if (val == 'F01') {
                    return '自然进店';
                } else {
                    return '其他';
                }
            }
        },
        methods: {
            //项目编号跳转详情
            queryProjectDetail: function (contractCode) {
                showDetailModal(contractCode);
            },
            //选择项目跳转
            selectProject: function () {
                showDameiOrder();
            },
            //项目弹框
            showModel: function () {
                showDameiOrder();
            },
            refresh: function (val) {
                var self = this;
                self.$refs.tabs.$children[val].$children[0].fetchData(self.contractUuid);
            },
            clickEvent: function (val) {
                var self = this;
                //当前点击的tab索引
                var activeIndex = val.key;
                this.index = val.key;
                if(activeIndex == self.activeIndex){
                    //表示还是当前页点击, 不去重新处理数据
                    return;
                }
                self.$refs.tabs.$children[this.index].$children[0].fetchData(self.contractUuid);
                //记住本次活动页
                self.activeIndex = activeIndex;
            },
            fetchFinaProjectAccount: function () {
                var self = this;
                self.$http.get("/finance/project/getfinaprojectaccountbyuuid", {
                    params: {
                        contractUuid: self.contractUuid
                    }
                }).then(function (res) {
                    if (res.data.code == 1 && res.data.data != null) {
                        self.finaProjectAccount = res.data.data;
                    }
                });
            },
            fetchData: function () {
                var self = this;
                self.$http.get("/finance/project/projectinformation", {
                    params: {
                        contractUuid: self.contractUuid
                    }
                }).then(function (res) {
                    if (res.data.code == 1 && res.data.data != null) {
                        self.contractPayment = res.data.data;
                        detail.$refs.tabs.$children[0].$children[0].fetchData(detail.contractUuid);
                    }
                });
            }
        },
        computed: {
            orderFlowStatus: function () {
                if (this.contractPayment.orderFlowStatus == "STAY_TURN_DETERMINE") {
                    return "待转大定";
                } else if(this.contractPayment.orderFlowStatus == "SUPERVISOR_STAY_ASSIGNED"){
                    return '督导组长待分配';
                } else if(this.contractPayment.orderFlowStatus == "DESIGN_STAY_ASSIGNED"){
                    return '设计待分配';
                } else if(this.contractPayment.orderFlowStatus == "APPLY_REFUND"){
                    return '申请退回';
                } else if(this.contractPayment.orderFlowStatus == "STAY_DESIGN"){
                    return '待设计';
                } else if(this.contractPayment.orderFlowStatus == "STAY_SIGN"){
                    return '待签约';
                } else if(this.contractPayment.orderFlowStatus == "STAY_SEND_SINGLE_AGAIN"){
                    return '待重新派单';
                } else if(this.contractPayment.orderFlowStatus == "STAY_CONSTRUCTION"){
                    return '待施工';
                } else if(this.contractPayment.orderFlowStatus == "ON_CONSTRUCTION"){
                    return '施工中';
                } else if(this.contractPayment.orderFlowStatus == "PROJECT_COMPLETE"){
                    return '竣工';
                } else if(this.contractPayment.orderFlowStatus == "ORDER_CLOSE"){
                    return '订单关闭    ';
                }
            }
        }
    });

    // 显示项目列表
    function showDameiOrder() {
        var _modal = $('#mdnOrder').clone();
        var $el = _modal.modal({
            height: 480,
            backdrop: 'static',

        });

        var el = $el.get(0);
        var vueModal = new Vue({
            el: el,
            mixins: [DameiVueMixins.ModalMixin],
            data: {
                flag: true,//确定按钮是否显示的标识符
                form: {
                    keyword: '',
                    isFlag: true
                },
                $dataTable: null
            },
            methods: {
                query: function () {
                    var self = this;
                    /*if(self.form.keyword == "" || self.form.keyword.length == 0){
                        self.$toastr.error("请输入关键字查询!");
                        return false;
                    }*/
                    self.form.isFlag = false;
                    self.$dataTable.bootstrapTable('selectPage', 1);
                    self.$dataTable.bootstrapTable('refresh');
                },
                drawTable: function () {
                    var self = this;
                    self.$dataTable = $('#dataTable', self._$el).bootstrapTable({
                        url: '/finance/project/search',
                        method: 'get',
                        dataType: 'json',
                        cache: false,
                        pagination: true,
                        singleSelect: true,// 单选checkbox
                        clickToSelect: true, // 单击行即可以选中
                        sidePagination: 'server',
                        queryParams: function (params) {
                            return _.extend({}, params, self.form);
                        },
                        mobileResponsive: true,
                        undefinedText: '-',
                        striped: true,
                        maintainSelected: true,
                        sortOrder: 'desc',
                        columns: [{
                            checkbox: true,
                            align: 'center',
                            radio: true,
                            formatter: function (value, row, index) {
                                var label = false;
                                if(index == 0){
                                    label = true;
                                    var order = {
                                        contractCode: row.contractCode,
                                        contractUuid: row.contractUuid,
                                    }
                                    detail.contractCode = order.contractCode;
                                    detail.contractUuid = order.contractUuid;
                                }
                                return label;
                            }
                        }, {
                            field: 'contractCode',
                            title: '项目编号',
                            align: 'center',
                        }, {
                            field: '',
                            title: '客户姓名/电话',
                            align: 'center',
                            formatter: function (value, row) {
                                var fragment = '';
                                var customerName = row.customerName;
                                var customerMobile = row.customerMobile;
                                if(customerName){
                                    fragment += customerName + "/";
                                }
                                if(customerMobile){
                                    fragment += customerMobile;
                                }
                                return fragment;
                            }
                        }, {
                            field: '',
                            title: '第二联系人姓名/电话',
                            align: 'center',
                            formatter: function (value, row) {
                                var fragment = '';
                                var secondContact = row.secondContact;
                                var secondContactMobile = row.secondContactMobile;
                                if(secondContact){
                                    fragment += secondContact + "/";
                                }
                                if(secondContactMobile){
                                    fragment += secondContactMobile;
                                }
                                return fragment;
                            }
                        }, {
                            field: 'mobile',
                            title: '客服姓名/电话',
                            align: 'center',
                            formatter: function (value, row) {
                                var fragment = '';
                                var serviceName = row.serviceName;
                                var serviceMobile = row.serviceMobile;
                                if(serviceName){
                                    fragment += serviceName + "/";
                                }
                                if(serviceMobile){
                                    fragment += serviceMobile;
                                }
                                return fragment;
                            }
                        }, {
                            field: 'address',
                            title: '设计师姓名/电话',
                            align: 'center',
                            formatter: function (value, row) {
                                var fragment = '';
                                var designer = row.designer;
                                var designerMobile = row.designerMobile;
                                if(designer){
                                    fragment += designer + "/";
                                }
                                if(designerMobile){
                                    fragment += designerMobile;
                                }
                                return fragment;
                            }
                        }, {
                            field: 'itemName',
                            title: '财务阶段',
                            align: 'center',
                        }, {
                            field: 'orderFlowStatus',
                            title: '项目状态',
                            align: 'center',
                            formatter: function (value) {
                                switch (value) {
                                    case 'STAY_TURN_DETERMINE':
                                        return '待转大定';
                                        break;
                                    case 'SUPERVISOR_STAY_ASSIGNED' :
                                        return '督导组长待分配';
                                        break;
                                    case 'DESIGN_STAY_ASSIGNED' :
                                        return '设计待分配';
                                        break;
                                    case 'APPLY_REFUND' :
                                        return '申请退回';
                                        break;
                                    case 'STAY_DESIGN' :
                                        return '待设计';
                                        break;
                                    case 'STAY_SIGN' :
                                        return '待签约';
                                        break;
                                    case 'STAY_SEND_SINGLE_AGAIN' :
                                        return '待重新派单';
                                        break;
                                    case 'STAY_CONSTRUCTION' :
                                        return '待施工';
                                        break;
                                    case 'ON_CONSTRUCTION' :
                                        return '施工中';
                                        break;
                                    case 'PROJECT_COMPLETE' :
                                        return '竣工';
                                        break;
                                    case 'ORDER_CLOSE' :
                                        return '订单关闭';
                                        break;
                                }
                            }
                        }]
                    });
                    self.$dataTable.on('check.bs.table', function (row, data) {
                        self.flag = data[0];
                        var order = {
                            contractCode: data.contractCode,
                            contractUuid: data.contractUuid,
                        }
                        detail.contractCode = order.contractCode;
                        detail.contractUuid = order.contractUuid;
                    });
                },
                commitUsers: function () {
                    $el.modal('hide');
                    this.$destroy();
                    detail.fetchData();
                    detail.fetchFinaProjectAccount();
                }

            },
            created: function () {
            },
            ready: function () {
                this.drawTable();
            }
        });

    }

    //查看详情
    function showDetailModal(contractCode) {
        var _modal = $('#detailModal').clone();
        var $el = _modal.modal({
            height: 350,
            maxHeight: 500
        });
        $el.on('shown.bs.modal',
            function () {
                // 获取 node
                var el = $el.get(0);
                // 创建 Vue 对象编译节点
                var vueDetailModal = new Vue({
                    el: el,
                    // 模式窗体必须引用 ModalMixin
                    mixins: [DameiVueMixins.ModalMixin],
                    $modal: $el,
                    data: {
                        customerContract: null
                    },
                    //模式窗体 jQuery 对象
                    created: function () {

                    },
                    ready: function () {
                        this.getProjectDetail();
                    },
                    filters: {
                        goForwardDeliveryHousing: function (val) {
                            if (val == 1) {
                                return '是';
                            } else if (val == 0) {
                                return '否';
                            } else {
                                return '-';
                            }
                        },
                        goElevator: function (val) {
                            if (val == 1) {
                                return '有';
                            } else if (val == 0) {
                                return '无';
                            } else {
                                return '-';
                            }
                        },
                        goHoseType: function (val) {
                            if (val == '0') {
                                return '旧房';
                            } else if (val == '1') {
                                return '新房';
                            } else {
                                return '-';
                            }
                        },
                    },
                    methods: {
                        //查询项目当前的项目信息和客户信息
                        getProjectDetail: function () {
                            var self = this;
                            this.$http.get('/order/getbycode?contractCode=' + contractCode).then(function (res) {
                                if (res.data.code == 1) {
                                    self.customerContract = res.data.data;
                                }
                            })
                        }
                    }
                });
                // 创建的Vue对象应该被返回
                return vueDetailModal;
            });
    }
})();

//收款(弹框)
function showDepositModal (){
    var $modal = $('#depositModal').clone();
    $modal.modal({
        height: 800,
        width: 800,
        backdrop: 'static'
    });
    $modal.on('shown.bs.modal',
        function () {
            var depositVue = new Vue({
                el: $modal.get(0),
                data: {
                    depositEnableReturnBack: '',//有无可退字样
                    ableDeductAmount:'',//校验实际金额
                    returnWord: null,
                    flag:false,
                    isShow:false,
                    receNum: '',//收据号
                    depositList: [],//抵定金集合
                    paymoneyRecordDto: null,
                    deductMoney: null,//抵定金
                    bankObj: null,//银行对象
                    branchObj: null,//支行对象
                    paymethodObj: null,//支付方式对象
                    depMethodList: [],//定金支付方式集合
                    curMethodList: [],//其他阶段支付方式集合
                    attrBankList: [],//银行集合
                    attrBranchList: [],//支行集合
                    payType: '',//支付方式
                    customerName: '',//客户姓名
                    customerMobile: '',//客户手机号
                    paymoneyRecord: {
                        payerName:'',//交款人姓名
                        contractCode: '',//项目编号
                        contractUuid: '',//项目唯一uuid
                        stageCode: '',//交款阶段uuid编码
                        payerMobile: '',//交款人手机号
                        payTime: '',//交款时间
                        expectReceived: 0,//应收
                        actualReceived: 0,//实收
                        receiptNum: '',//收据号
                        remark: '',//备注
                        paymethodId: '',
                        paymethodCode: '',//支付方式id
                        paymethodName: '',//支付方式名称
                        paymethodAttrId: '',//支付方式最子级的属性id
                        paymethodAttrFullname: '',//支付方式所有级别属性名称
                        costfeeRate: '',//交易费率
                        costfeeAmount: 0,//交易手续费金额
                        itemName: '',//交款阶段
                        payManualFlag: '',//人工指定的款项类型
                        usedDeductMoneyId: ''//使用的抵扣金id
                    }
                },
                created: function () {
                    this.paymoneyRecord.payTime = getNowTime();//默认上个月时间
                },
                ready: function () {
                    this.findBuildPaymentSituation();
                    this.activeDatepicker();
                },
                validators: {
                    num: {
                        message: '请输入正确的金额格式，例（10或10.00）',
                        check: function (val) {
                            return /(\-?)^([1-9]\d{0,9}|0)([.]?|(\.\d{1,2})?)$/.test(val);
                        }
                    },
                    checkNum: function (val) {
                        return /(\-?)^([1-9]\d{0,9}|0)([.]?|(\.\d{1,2})?)$/.test(val);
                    },
                    actualReceivedMax: function (val,ableDeductAmount) {

                        return val <= ableDeductAmount;

                    },
                    mobile: {
                        message: '请输入正确的手机号',
                            check: function (val) {
                            return /^[1][3,4,5,7,8][0-9]{9}$/.test(val);
                        }
                    }
                },
                methods: {
                    //查询金额
                    fetchFinaProjectAccount: function () {
                        var self = this;
                        self.$http.get("/finance/project/getfinaprojectaccountbyuuid", {
                            params: {
                                contractUuid: detail.contractUuid
                            }
                        }).then(function (res) {
                            if (res.data.code == 1 && res.data.data != null) {
                                detail.finaProjectAccount = res.data.data;
                            }
                        });
                    },
                    //查询项目信息
                    fetchData: function () {
                        var self = this;
                        self.$http.get("/finance/project/projectinformation", {
                            params: {
                                contractUuid: detail.contractUuid
                            }
                        }).then(function (res) {
                            if (res.data.code == 1 && res.data.data != null) {
                                detail.contractPayment = res.data.data;
                                detail.refresh(0);
                            }
                        });
                    },
                    //勾选预订合同
                    changeContract: function (signedDepositContract) {
                        var self = this;
                        if(!signedDepositContract){
                            self.flag = true;
                        }else{
                            self.flag = false;
                        }
                    },
                    //修改有预订合同
                    checkContract: function () {
                        var self = this;
                        self.paymoneyRecordDto.signedDepositContract = false;
                        self.$http.get('/finance/project/checkcontract?contractUuid=' + self.paymoneyRecordDto.contractUuid).then(function (res) {
                            if (res.data.code == 1) {
                                Vue.toastr.success(res.data.message);
                            }
                        });
                    },
                    //修改有无可退字样
                    checkRefundable: function () {
                        var self = this;
                        if(self.paymoneyRecordDto.depositEnableReturnBack){
                            self.returnWord = 1;
                        }else{
                            self.returnWord = 0;
                        }
                        self.$http.get('/finance/project/checkrefundable?contractUuid=' + self.paymoneyRecordDto.contractUuid + '&returnWord=' + self.returnWord).then(function (res) {
                            if (res.data.code == 1) {
                                Vue.toastr.success(res.data.message);
                            }else{
                                Vue.toastr.error(res.data.message);
                            }
                        });
                    },
                    //修改客户信息
                    updateCustomerInfo: function () {
                        var self = this;
                        var data = {
                            name : self.paymoneyRecordDto.customerName,
                            mobile : self.paymoneyRecordDto.customerMobile,
                            contractUuid : self.paymoneyRecordDto.contractUuid
                        };
                        this.$http.post('/finance/project/altcusinfo', data).then(function (res) {
                            if (res.data.code == 1) {
                                Vue.toastr.success(res.data.message);
                            }
                        });
                    },
                    //收据号改变
                    chanReceiptNo: function (item) {
                        var self = this;
                        self.ableDeductAmount = item.maxDeductAmount - item.deductedAmount;
                        if(item){
                            self.deductMoney = item;
                            self.paymoneyRecordDto.receiptNum = "D" + self.deductMoney.receiptNo + self.deductMoney.deductTimes;
                            self.paymoneyRecord.usedDeductMoneyId = self.deductMoney.id;
                            self.paymoneyRecord.actualReceived = self.deductMoney.maxDeductAmount - self.deductMoney.deductedAmount;
                        }
                    },
                    //实收金额事件
                    caluCharge: function () {
                        var self = this;
                        var tar;
                        if(self.paymethodObj && self.bankObj && self.branchObj){
                            tar = self.branchObj;
                        }else if (self.paymethodObj && self.bankObj && self.branchObj == "") {
                            tar = self.bankObj;
                        }else if (self.paymethodObj && self.bankObj == "" && self.branchObj == "") {
                            tar = self.paymethodObj;
                        }
                        this.caluCostRate(tar);
                    },
                    //实际金额触发手续费变动
                    caluCostRate: function (tar) {
                        var self = this;
                        var obj;
                        var costRate;
                        if(tar){
                            obj = JSON.parse(tar);
                            costRate = obj.costRate;
                            if (obj.maxCostfee == -1){
                                self.paymoneyRecord.costfeeAmount = (self.paymoneyRecord.actualReceived * costRate).toFixed(2);
                            }else if(self.paymoneyRecord.actualReceived * costRate >= obj.maxCostfee){
                                self.paymoneyRecord.costfeeAmount = obj.maxCostfee.toFixed(2);
                            }else if (self.paymoneyRecord.actualReceived * costRate <= obj.minCostfee) {
                                self.paymoneyRecord.costfeeAmount = obj.minCostfee.toFixed(2);
                            }else{
                                self.paymoneyRecord.costfeeAmount = (self.paymoneyRecord.actualReceived * costRate).toFixed(2);
                            }
                        }
                    },
                    toggle: function () {
                        this.isShow = !this.isShow;
                    },
                    activeDatepicker: function () {
                        $(this.$els.payTime).datetimepicker({
                            format: 'yyyy-mm-dd hh:ii:ss',
                        })
                    },
                    //查询支付方式
                    changePayType: function () {
                        var self = this;
                        var str = self.paymethodObj;
                        if(str){
                            var obj = JSON.parse(str);
                            self.payType = obj.methodType;
                            if (self.payType == 'DEDUCT'){
                                this.$http.get('/finance/payment/querydepositdeduct?contratUuid=' + self.paymoneyRecordDto.contractUuid).then(function (res) {
                                    if (res.data.code == 1) {
                                        self.depositList = res.data.data;
                                    }
                                });
                            };
                            self.paymoneyRecordDto.receiptNum = self.receNum;
                            if (self.payType == 'POS') {
                                this.$http.get('/finance/paymethod/querymethodattr?methoId=' + obj.id).then(function (res) {
                                    if (res.data.code == 1) {
                                        var bankList = res.data.data;
                                        $.each(bankList, function (i, val) {
                                            var obj = {};
                                            obj.attrName = val.attrName;
                                            obj.values = JSON.stringify(val)
                                            self.attrBankList.push(obj);
                                        });

                                    }
                                });
                            }
                            self.bankObj = '';
                            self.attrBankList = [];
                            self.branchObj = '';
                            self.attrBranchList = [];
                            this.caluCharge();
                        }

                    },
                    //联动支行
                    changeBank: function () {
                        var self = this;
                        var str = self.bankObj;
                        if(str){
                            var obj = JSON.parse(str);
                            var attrPid = obj.id;
                            var methodId = obj.methodId;
                            if(methodId){
                                self.$http.get('/finance/paymethod/querymethodattr?methoId='+methodId+'&attrPid='+attrPid).then(function (res) {
                                    if (res.data.code == 1) {
                                        var branchList = res.data.data;
                                        $.each(branchList, function(i,val){
                                            var obj = {};
                                            obj.attrName = val.attrName;
                                            obj.values = JSON.stringify(val)
                                            self.attrBranchList.push(obj);
                                        });
                                    }
                                })
                            }
                            self.branchObj = '';
                            self.attrBranchList = [];
                            this.caluCharge();
                        }
                    },
                    //点击支行触发事件
                    changeBranch: function () {
                        var self = this;
                        var str = self.branchObj;
                        if(str){
                            var obj = JSON.parse(str);
                            this.caluCharge(obj);
                        }
                    },
                    //查询项目当前的交款阶段,用于展示财务收款
                    findBuildPaymentSituation: function () {
                        var self = this;
                        this.$http.get('/finance/payment/buildpaymentsituation?contractUuid=' + detail.contractUuid).then(function (res) {
                            if (res.data.code == 1) {
                                self.customerName = self.paymoneyRecord.payerName=res.data.data.customerName;
                                self.customerMobile = self.paymoneyRecord.payerMobile=res.data.data.customerMobile;
                                self.paymoneyRecordDto = res.data.data;
                                self.depositEnableReturnBack = self.paymoneyRecordDto.depositEnableReturnBack;
                                self.receNum = self.paymoneyRecordDto.receiptNum;
                                var depPaymethodList = self.paymoneyRecordDto.depositStageMethodList;
                                $.each(depPaymethodList, function(i,val){
                                    var obj = {};
                                    obj.methodName = val.methodName;
                                    obj.values = JSON.stringify(val)
                                    self.depMethodList.push(obj);
                                });
                                if(self.paymoneyRecordDto.payStage.stageName != '定金'){
                                    var curPaymethodList = self.paymoneyRecordDto.currentStageMethodList;
                                    $.each(curPaymethodList, function(i,val){
                                        var obj = {};
                                        obj.methodName = val.methodName;
                                        obj.values = JSON.stringify(val)
                                        self.curMethodList.push(obj);
                                    });
                                }
                            }
                        }).finally(function () {
                        });
                    },
                    insert: function () {
                        var self = this;
                        self.$validate(true,
                            function () {
                                if (self.$validation.valid) {
                                    self.submitting = true;
                                    var obj = null;
                                    var bankObj = null;
                                    var branchObj = null;
                                    if(self.paymethodObj){
                                        obj = JSON.parse(self.paymethodObj);
                                    }
                                    if(self.bankObj){
                                        bankObj = JSON.parse(self.bankObj);
                                    }
                                    if(self.branchObj){
                                        branchObj = JSON.parse(self.branchObj);
                                    }

                                    self.paymoneyRecord.contractUuid = self.paymoneyRecordDto.contractUuid;
                                    self.paymoneyRecord.contractCode = self.paymoneyRecordDto.contractNo;
                                    self.paymoneyRecord.receiptNum = self.paymoneyRecordDto.receiptNum;


                                    self.paymoneyRecord.paymethodId = obj.id;
                                    self.paymoneyRecord.paymethodCode = obj.methodCode;
                                    self.paymoneyRecord.paymethodName = obj.methodName;
                                    if(bankObj && branchObj){
                                        self.paymoneyRecord.paymethodAttrId = branchObj.id;//支付方式最子级的属性id
                                        self.paymoneyRecord.paymethodAttrFullname = bankObj.attrName + ',' + branchObj.attrName ;//支付方式所有级别属性名称
                                    }else if (self.bankObj && self.branchObj == "") {
                                        self.paymoneyRecord.paymethodAttrId = bankObj.id;//支付方式最子级的属性id
                                        self.paymoneyRecord.paymethodAttrFullname = bankObj.attrName;//支付方式所有级别属性名称
                                    }
                                    if(self.paymoneyRecord.itemName == '定金' || self.paymoneyRecordDto.payStage.stageName == '定金'){
                                        self.paymoneyRecord.stageCode = self.paymoneyRecordDto.depositStageCode;
                                        self.paymoneyRecord.expectReceived = self.paymoneyRecordDto.depositExpectPay;
                                    } else{
                                        self.paymoneyRecord.stageCode = self.paymoneyRecordDto.payStage.stageCode;
                                        self.paymoneyRecord.expectReceived = self.paymoneyRecordDto.stageExpectedPay;
                                    }

                                    //修改预订合同
                                    if(self.paymoneyRecordDto.signedDepositContract && self.flag){
                                        self.checkContract();
                                    }

                                    //修改有无可退字样
                                    if(self.paymoneyRecord.itemName == '定金' && self.paymoneyRecordDto.depositEnableReturnBack != self.depositEnableReturnBack){
                                        self.checkRefundable();
                                    }

                                    //修改客户姓名
                                    if(self.paymoneyRecordDto.customerName != self.customerName || self.paymoneyRecordDto.customerMobile != self.customerMobile){
                                        self.updateCustomerInfo();
                                    }

                                    self.$http.post('/finance/payment/savepaymentrecord', self.paymoneyRecord).then(function (res) {
                                            if (res.data.code === '1') {
                                                Vue.toastr.success(res.data.message);
                                                $modal.modal('hide');
                                                self.$destroy();
                                                this.fetchData();
                                                this.fetchFinaProjectAccount();
                                            }
                                            else{
                                                Vue.toastr.error(res.data.message);
                                            }
                                        },
                                        function (error) {
                                            Vue.toastr.error(error.responseText);
                                            window.location.href="/";
                                            self.submitting = false;
                                        });
                                }
                            })
                    }
                },
                watch: {
                    'paymoneyRecordDto.customerName': function (val){
                        this.paymoneyRecord.payerName = val;
                    },
                    'paymoneyRecordDto.customerMobile': function (val){
                        this.paymoneyRecord.payerMobile = val;
                    }
                }
            })
            /**
             * 获取当前时间
             * @returns
             */
            function getNowTime() {
                var date = new Date();
                var seperator1 = "-";
                var seperator2 = ":";
                var month = date.getMonth() + 1;
                var strDate = date.getDate();
                var strHours = date.getHours();
                var strMinutes = date.getMinutes();
                var strSeconds = date.getSeconds();
                if (month >= 1 && month <= 9) {
                    month = "0" + month;
                }
                if (strDate >= 0 && strDate <= 9) {
                    strDate = "0" + strDate;
                }
                if (strHours >= 1 && strHours <= 9) {
                    strHours = "0" + strHours;
                }
                if (strMinutes >= 0 && strMinutes <= 9) {
                    strMinutes = "0" + strMinutes;
                }
                if (strSeconds >= 0 && strSeconds <= 9) {
                    strSeconds = "0" + strSeconds;
                }
                var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate
                    + " " + strHours + seperator2 + strMinutes
                    + seperator2 + strSeconds;
                return currentdate;
            }
        });
}

//赔款(弹框)
function showReparationModal (){
    var $modal = $('#reparationModal').clone();
    $modal.modal({
        height: 400,
        width: 600,
        backdrop: 'static'
    });
    $modal.on('shown.bs.modal',
        function () {
            var reparationVue = new Vue({
                el: $modal.get(0),
                data: {
                    projectStage: null,
                    reparationMoney: {
                        reparationNo: '',
                        reparationAmount: '',
                        createTime: '',
                        reparationReason: '',
                        contractUuid: '',
                        contractCode: '',
                        createStageId: '',
                        effectStageId: '',
                        effectStageName: ''
                    }
                },
                created: function () {
                },
                ready: function () {
                    this.findProjectStage();
                    this.activeDatepicker();
                },
                validators: {
                    num: {
                        message: '请输入正确的金额格式，例（10或10.00）',
                        check: function (val) {
                            return /(\-?)^([1-9]\d{0,9}|0)([.]?|(\.\d{1,2})?)$/.test(val);
                        }
                    }
                },
                methods: {
                    //查询金额
                    fetchFinaProjectAccount: function () {
                        var self = this;
                        self.$http.get("/finance/project/getfinaprojectaccountbyuuid", {
                            params: {
                                contractUuid: detail.contractUuid
                            }
                        }).then(function (res) {
                            if (res.data.code == 1 && res.data.data != null) {
                                detail.finaProjectAccount = res.data.data;
                            }
                        });
                    },
                    activeDatepicker: function () {
                        var self = this;
                        $(self.$els.createTime).datetimepicker({
                            format: 'yyyy-mm-dd hh:ii:ss',
                            //clearBtn:true
                        })
                    },
                    //查询项目当前的阶段信息和客户信息,用于展示赔款
                    findProjectStage: function () {
                        var self = this;
                        this.$http.get('/finance/reparationmoney/findprojectstage?contractUuid=' + detail.contractUuid).then(function (res) {
                            if (res.data.code == 1) {
                                self.projectStage = res.data.data;
                            }
                        }).finally(function () {
                        });
                    },
                    insert: function () {
                        var self = this;
                        self.reparationMoney.effectStageId = self.projectStage.stage_code;
                        self.reparationMoney.createStageId = self.projectStage.stage_code;
                        self.reparationMoney.effectStageName = self.projectStage.stage_name;
                        self.reparationMoney.contractCode = self.projectStage.contract_code;
                        self.reparationMoney.contractUuid = self.projectStage.contract_uuid;
                        self.reparationMoney.reparationNo = self.projectStage.reparationNo;

                        self.$validate(true,
                            function () {
                                if (self.$validation.valid) {
                                    self.submitting = true;
                                    self.$http.post('/finance/reparationmoney/save', self.reparationMoney).then(function (res) {
                                            if (res.data.code === '1') {
                                                Vue.toastr.success(res.data.message);
                                                $modal.modal('hide');
                                                self.$destroy();
                                                detail.refresh(3);
                                                this.fetchFinaProjectAccount();
                                            }else{
                                                Vue.toastr.error(res.data.message);
                                            }
                                        },
                                        function (error) {
                                            Vue.toastr.error(error.responseText);
                                            self.submitting = false;
                                        });
                                }
                            })
                    }
                }
            })
        });
}

//退款(弹框)
function showRefundmentModal (){
    var $modal = $('#refundmentModal').clone();
    $modal.modal({
        height: 400,
        width: 600,
        backdrop: 'static'
    });
    $modal.on('shown.bs.modal',
        function () {
            var refundmentVue = new Vue({
                el: $modal.get(0),
                data: {
                    refundRecord: {
                        refundNo: '',//退款编号
                        contractCode: '',//合同单号
                        contractUuid: '',//合同唯一uuid
                        createStageId: '',//在哪个阶段发生的赔款
                        refundType: '',//退款类型
                        refundExpectAmount: 0,//总应退金额
                        refundTime: '',//退款时间
                        refundDepositAmount: 0,//定金应退
                        refundModifyAmount: 0,//拆改费应退
                        refundConstructAmount: 0,//施工款应退
                        deductDesignAmount: 0,//扣除设计费
                        deductOtherAmount: 0,//扣除其他费
                        refundReceiverName: '',//退款收钱人名称
                        refundReceiverMobile: '',//退款收钱人姓名
                        refundReceiverAccount: '',//退款收钱人给的银行卡号
                        refundReson: '',//退款原因
                        constructAbleBackAmount: 0,//施工款可退
                        modifyAbleBackAmount: 0,//拆改费可退
                        depositAbleBackAmount: 0,//可退定金
                        refundActualAmount: 0,//总实退金额
                        depositRefundDetailStr: ''//id与金额字符串
                    },

                    constructAmount: 0,//施工款可退
                    modifyAmount: 0,//拆改费可退
                    depositAmount: 0,//可退定金
                    checkedOne: false,
                    checkedTwo: false,
                    checkedThree: false,
                    depositList: [],//定金列表
                    depositTotalAmount: 0//定金总实收金额
                },
                created: function () {
                },
                ready: function () {
                    this.findRefundRecord();
                    this.activeDatepicker();
                },
                validators: {
                    card: {
                        message: '请输入正确的银行卡号，银行卡号长度为16位或19位',
                        check: function (val) {
                            return (/^([1-9]{1})(\d{15}|\d{18})$/).test(val);
                        }
                    },
                    checkNum: function (val) {
                        return /(\-?)^([1-9]\d{0,9}|0)([.]?|(\.\d{1,2})?)$/.test(val);
                    },
                    refundDepositAmountMax: function (val, depositAbleBackAmount) {
                        return val <= depositAbleBackAmount;
                    },
                    refundModifyAmountMax: function (val) {
                        return val <= refundmentVue.refundRecord.modifyAbleBackAmount;
                    },
                    refundConstructAmountMax: function (val) {
                        return val <= refundmentVue.refundRecord.constructAbleBackAmount;
                    }
                },
                watch:{
                    depositList: {
                        handler: function (val, oldVal) {
                            var self = this;
                            if(oldVal.length == 0){
                                return ;
                            }
                            self.depositTotalAmount = 0;
                            if(val && val.length > 0){
                                var obj = {};
                                val.forEach(function (item) {
                                    if(!item.itemCheck && typeof(item.itemCheck) != "undefined"){
                                        item.refundDepositAmount = 0;
                                        self.depositTotalAmount += parseFloat(item.refundDepositAmount);
                                        self.refundRecord.refundActualAmount = (self.depositTotalAmount+ parseFloat(self.refundRecord.refundModifyAmount || 0) +parseFloat(self.refundRecord.refundConstructAmount || 0)).toFixed(2);
                                    }
                                    if(item.refundDepositAmount){
                                        obj[item.id] = item.refundDepositAmount;
                                        self.depositTotalAmount += parseFloat(item.refundDepositAmount);
                                        self.refundRecord.refundActualAmount = (self.depositTotalAmount+ parseFloat(self.refundRecord.refundModifyAmount || 0) +parseFloat(self.refundRecord.refundConstructAmount || 0)).toFixed(2);
                                    }
                                })
                                self.refundRecord.refundDepositAmount = self.depositTotalAmount;
                                self.refundRecord.depositRefundDetailStr =  JSON.stringify(obj);//转换为json对象
                            }
                        },
                        deep: true
                    }
                },
                methods: {
                    activeDatepicker: function () {
                        $(this.$els.refundTime).datetimepicker({
                            format: 'yyyy-mm-dd',
                        })
                    },
                    //查询金额
                    fetchFinaProjectAccount: function () {
                        var self = this;
                        self.$http.get("/finance/project/getfinaprojectaccountbyuuid", {
                            params: {
                                contractUuid: detail.contractUuid
                            }
                        }).then(function (res) {
                            if (res.data.code == 1 && res.data.data != null) {
                                detail.finaProjectAccount = res.data.data;
                            }
                        });
                    },
                    //输入金额改变实际收款
                    changeMoney: function () {
                        var self =this;
                        //总实退金额
                        self.refundRecord.refundActualAmount = (parseFloat(self.depositTotalAmount || 0) + parseFloat(self.refundRecord.refundModifyAmount || 0) + parseFloat(self.refundRecord.refundConstructAmount || 0)).toFixed(2);
                        //总应退金额
                        self.refundRecord.refundExpectAmount = ((self.depositAmount || 0) + (self.modifyAmount || 0) + (self.constructAmount || 0)).toFixed(2);
                    },
                    //计算实收金额
                    sum: function (val) {
                        var self =this;
                        if(val == 'checkedOne'){
                            self.checkedOne = !self.checkedOne;
                            if (self.checkedOne) {
                                self.depositAmount = self.refundRecord.depositAbleBackAmount;
                                this.$http.get('/finance/payment/querydepositdeduct?contratUuid=' + self.refundRecord.contractUuid).then(function (res) {
                                    if (res.data.code == 1) {
                                        self.depositList = res.data.data;
                                        self.depositList.forEach(function (item) {
                                            item.brandField = 'brandField' + item.id;
                                            item.brandValidate = {
                                                checkNum:{rule:item.refundDepositAmount,message:'请输入正确金额'},
                                                refundDepositAmountMax:{rule:(item.refundDepositAmount, item.maxDeductAmount - item.deductedAmount),message:'输入金额不能大于定金'}
                                            };
                                        });
                                    }
                                });
                            }else{
                                self.depositTotalAmount = 0;
                                self.depositAmount = 0;
                            }
                            self.checkedOne = !self.checkedOne;
                        }else if(val == 'checkedTwo'){
                            self.checkedTwo = !self.checkedTwo;
                            if (self.checkedTwo) {
                                self.modifyAmount = self.refundRecord.modifyAbleBackAmount;
                            }else{
                                self.refundRecord.refundModifyAmount = 0;
                                self.modifyAmount = 0;
                            }
                            self.checkedTwo = !self.checkedTwo;
                        }else if(val == 'checkedThree'){
                            self.checkedThree = !self.checkedThree;
                            if (self.checkedThree) {
                                self.constructAmount = self.refundRecord.constructAbleBackAmount;
                            }else{
                                self.refundRecord.refundConstructAmount = 0;
                                self.constructAmount = 0;
                            }
                            self.checkedThree = !self.checkedThree;
                        }
                        //总实退金额
                        self.refundRecord.refundActualAmount = (self.depositTotalAmount + parseFloat(self.refundRecord.refundModifyAmount || 0) + parseFloat(self.refundRecord.refundConstructAmount || 0)).toFixed(2);
                        //总应退金额
                        self.refundRecord.refundExpectAmount = ((self.depositAmount  || 0) + (self.modifyAmount || 0) + (self.constructAmount || 0)).toFixed(2);
                    },
                    //查询项目当前的阶段退款信息
                    findRefundRecord: function () {
                        var self = this;
                        this.$http.get('/finance/payment/buildrefundsituation?contractUuid=' + detail.contractUuid).then(function (res) {
                            if (res.data.code == 1) {
                                self.refundRecord = res.data.data;
                            }
                        }).finally(function () {
                        });
                    },
                    insert: function () {
                        var self = this;
                        self.$validate(true,
                            function () {
                                if (self.$validation.valid) {
                                    self.submitting = true;
                                    self.$http.post('/finance/payment/saveprojectrefundrecord', self.refundRecord).then(function (res) {
                                            if (res.data.code === '1') {
                                                Vue.toastr.success(res.data.message);
                                                $modal.modal('hide');
                                                self.$destroy();
                                                detail.refresh(4);
                                                this.fetchFinaProjectAccount();
                                            }else{
                                                Vue.toastr.error(res.data.message);
                                            }
                                        },
                                        function (error) {
                                            Vue.toastr.error(error.responseText);
                                            self.submitting = false;
                                        });
                                }
                            })
                    }
                }
            })
        });
}

//退单(弹框)
function showChargebackModal (){
    var $modal = $('#chargebackModal').clone();
    $modal.modal({
        height: 400,
        width: 650,
        backdrop: 'static'
    });
    $modal.on('shown.bs.modal',
        function () {
            var chargebackVue = new Vue({
                el: $modal.get(0),
                data: {
                    refundRecord: {
                        refundNo: '',//退款编号
                        contractCode: '',//合同单号
                        contractUuid: '',//合同唯一uuid
                        createStageId: '',//在哪个阶段发生的赔款
                        refundType: '',//退款类型
                        refundExpectAmount: '',//总应退金额
                        refundTime: '',//退款时间
                        refundDepositAmount: 0,//定金应退
                        refundModifyAmount: 0,//拆改费应退
                        refundConstructAmount: 0,//施工款应退
                        deductDesignAmount: 0,//扣除设计费
                        deductOtherAmount: 0,//扣除其他费
                        refundReceiverName: '',//退款收钱人名称
                        refundReceiverMobile: '',//退款收钱人姓名
                        refundReceiverAccount: '',//退款收钱人给的银行卡号
                        refundReson: '',//退款原因
                        constructAbleBackAmount: 0,//施工款可退
                        modifyAbleBackAmount: 0,//拆改费可退
                        depositAbleBackAmount: 0,//可退定金
                        refundActualAmount: 0,//总实退金额
                        refundMemo: ''//退款所处阶段
                    },
                    depositList: [],//定金列表
                    checkedOne: false,
                    checkedTwo: false,
                    checkedThree: false,
                    depositTotalAmount: 0//定金总实收金额
                },
                created: function () {
                },
                ready: function () {
                    this.findRefundRecord();
                    this.activeDatepicker();
                },
                validators: {
                    card: {
                        message: '请输入正确的银行卡号，银行卡号长度为16位或19位',
                        check: function (val) {
                            return (/^([1-9]{1})(\d{15}|\d{18})$/).test(val);
                        }
                    },
                    checkNum: function (val) {
                        return /(\-?)^([1-9]\d{0,9}|0)([.]?|(\.\d{1,2})?)$/.test(val);
                    },
                    refundActualAmountCheck: function (val) {
                        return val >= 0;
                    }
                },
                watch:{
                    depositList: {
                        handler: function (val, oldVal) {
                            var self = this;
                            if(oldVal.length == 0){
                                return ;
                            }
                            self.depositTotalAmount = 0;
                            if(val && val.length > 0){
                                var obj = {};
                                val.forEach(function (item) {
                                    if(!item.itemCheck || typeof(item.itemCheck) == "undefined"){
                                        item.refundDepositAmount = 0;
                                    }else{
                                        item.refundDepositAmount = item.maxDeductAmount - item.deductedAmount;
                                        obj[item.id] = item.refundDepositAmount;
                                        self.depositTotalAmount += parseFloat(item.refundDepositAmount);
                                        self.refundRecord.refundActualAmount = (self.depositTotalAmount + parseFloat(self.refundRecord.refundModifyAmount || 0)
                                                                                + parseFloat(self.refundRecord.refundConstructAmount || 0) - self.refundRecord.deductDesignAmount * 1
                                                                                - self.refundRecord.deductOtherAmount * 1).toFixed(2);
                                        //应收总金额
                                        self.refundRecord.refundExpectAmount = ((self.depositTotalAmount || 0) + (self.refundRecord.refundModifyAmount || 0) + (self.refundRecord.refundConstructAmount || 0)).toFixed(2);
                                    }
                                })

                                self.refundRecord.refundDepositAmount = self.depositTotalAmount;
                                self.refundRecord.depositRefundDetailStr =  JSON.stringify(obj);//转换为json对象
                            }
                        },
                        deep: true
                    }
                },
                methods: {
                    activeDatepicker: function () {
                        $(this.$els.refundTime).datetimepicker({
                            format: 'yyyy-mm-dd',
                        })
                    },
                    //查询金额
                    fetchFinaProjectAccount: function () {
                        var self = this;
                        self.$http.get("/finance/project/getfinaprojectaccountbyuuid", {
                            params: {
                                contractUuid: detail.contractUuid
                            }
                        }).then(function (res) {
                            if (res.data.code == 1 && res.data.data != null) {
                                detail.finaProjectAccount = res.data.data;
                            }
                        });
                    },
                    //计算实收金额
                    sum: function (val) {
                        var self =this;
                        if(val == 'checkedOne'){
                            self.checkedOne = !self.checkedOne;
                            if (self.checkedOne) {
                                //self.depositTotalAmount = self.refundRecord.depositAbleBackAmount;
                                this.$http.get('/finance/payment/querydepositdeduct?contratUuid=' + self.refundRecord.contractUuid).then(function (res) {
                                    if (res.data.code == 1) {
                                        self.depositList = res.data.data;
                                    }
                                });
                            }else{
                                self.depositTotalAmount = 0;
                            }
                            self.checkedOne = !self.checkedOne;
                        }else if(val == 'checkedTwo'){
                            self.checkedTwo = !self.checkedTwo;
                            if (self.checkedTwo) {
                                self.refundRecord.refundModifyAmount = self.refundRecord.modifyAbleBackAmount;
                            }else{
                                self.refundRecord.refundModifyAmount = 0;
                            }
                            self.checkedTwo = !self.checkedTwo;
                        }else if(val == 'checkedThree'){
                            self.checkedThree = !self.checkedThree;
                            if (self.checkedThree) {
                                self.refundRecord.refundConstructAmount = self.refundRecord.constructAbleBackAmount;
                            }else{
                                self.refundRecord.refundConstructAmount = 0;
                            }
                            self.checkedThree = !self.checkedThree;
                        }
                        //实收金额
                        self.refundRecord.refundActualAmount = ((self.depositTotalAmount || 0) +
                                                                (self.refundRecord.refundModifyAmount || 0) +
                                                                (self.refundRecord.refundConstructAmount || 0) -
                                                                (self.refundRecord.deductDesignAmount || 0) -
                                                                (self.refundRecord.deductOtherAmount || 0)).toFixed(2);
                        //应收金额
                        self.refundRecord.refundExpectAmount = ((self.depositTotalAmount || 0) + (self.refundRecord.refundModifyAmount || 0) + (self.refundRecord.refundConstructAmount || 0)).toFixed(2);
                    },
                    //查询项目当前的阶段退款信息
                    findRefundRecord: function () {
                        var self = this;
                        this.$http.get('/finance/payment/buildrefundsituation?contractUuid=' + detail.contractUuid).then(function (res) {
                            if (res.data.code == 1) {
                                self.refundRecord = res.data.data;
                            }
                        }).finally(function () {
                        });
                    },
                    insert: function () {
                        var self = this;
                        self.$validate(true,
                            function () {
                                if (self.$validation.valid) {
                                    self.submitting = true;
                                    self.$http.post('/finance/project/closeproject', self.refundRecord).then(function (res) {
                                            if (res.data.code === '1') {
                                                Vue.toastr.success(res.data.message);
                                                $modal.modal('hide');
                                                self.$destroy();
                                                //detail.refresh(5);
                                                detail.fetchFinaProjectAccount();
                                            }
                                            else{
                                                Vue.toastr.error(res.data.message);
                                            }
                                        },
                                        function (error) {
                                            Vue.toastr.error(error.responseText);
                                            self.submitting = false;
                                        });
                                }
                            })
                    }
                }
            })
        });
}