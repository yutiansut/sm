+(function () {
    $('#financeQueryMenu').addClass('active');
    $('#paymentrecordMenu').addClass('active');
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
                name: '交款记录查询',
                active: true
            }],
            stageType:{},
            form: {
                keyword: '',
                paystartTime:'',
                payendTime:'',
                contractCode:'',
                payerName:'',
                payerMobile:'',
                templateStageId:'',
                ifRcw:'',
                paymethodName:''
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
                }else{
                    this.$dataTable.bootstrapTable('selectPage', 1);
                }
            },
            drawTable: function () {
                var self = this;
                self.$dataTable = $('#dataTable', self._$el).bootstrapTable({
                    url: '/finance/analyze/paymoneyrecordfindall',
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
                        field: 'createTime',
                        title: '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;日期&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;',
                        align: 'center',
                        formatter: function(value,row){
                            if(value != null){
                                if( row.payType == 'RETURN_CONSTRUCT' ||
                                    row.payType == 'RETURN_MODIFY' || row.payType == 'RETURN_DEPOSIT'){
                                    return '<span style="color: #5c2699;">'+row.createTime+'</span>';
                                }else if(row.ifRcw == 1){
                                    return '<span style="color: tomato;">'+row.createTime+'</span>';
                                }else{
                                    return row.createTime;
                                }
                            }else{
                                return '-';
                            }
                        }
                    }, {
                        field: 'contractCode',
                        title: '项目编号',
                        align: 'center',
                        formatter: function(value,row){
                            if(value != null) {
                                if (row.payType == 'RETURN_CONSTRUCT' || row.payType == 'RETURN_MODIFY' || row.payType == 'RETURN_DEPOSIT') {
                                    return '<span style="color: #5c2699 ">' + row.contractCode + '</span>';
                                } else if (row.ifRcw == 1) {
                                    return '<span style="color: tomato">' + row.contractCode + '</span>';
                                } else {
                                    return row.contractCode;
                                }
                            }else{
                                return '-';
                            }
                        }
                    }, {
                        field: 'payerName',
                        title: '客户姓名',
                        align: 'center',
                        formatter: function(value,row){
                            if(value != null) {
                                if (row.payType == 'RETURN_CONSTRUCT' || row.payType == 'RETURN_MODIFY' || row.payType == 'RETURN_DEPOSIT') {
                                    return '<span style="color: #5c2699 ">' + row.payerName + '</span>';
                                } else if (row.ifRcw == 1) {
                                    return '<span style="color: tomato">' + row.payerName + '</span>';
                                } else {
                                    return row.payerName;
                                }
                            }else{
                                return '-';
                            }
                        }
                    }, {
                        field: 'remark',
                        title: '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;摘要&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;',
                        align: 'center',
                        formatter: function(value,row){
                            if(value != null) {
                                if ( row.payType == 'RETURN_CONSTRUCT' || row.payType == 'RETURN_MODIFY' || row.payType == 'RETURN_DEPOSIT') {
                                    return '<span style="color: #5c2699 ">' + row.remark + '</span>';
                                } else if (row.ifRcw == 1) {
                                    return '<span style="color: tomato">' + row.remark + '</span>';
                                } else {
                                    return row.remark;
                                }
                            }else{
                                return '-';
                            }
                        }
                    }, {
                        field: 'actualReceived',
                        title: '收',
                        align: 'center',
                        formatter: function(value,row){
                            if(value != null) {
                                if (row.payType == 'RETURN_CONSTRUCT' || row.payType == 'RETURN_MODIFY' || row.payType == 'RETURN_DEPOSIT') {
                                    return '-';
                                } else if (row.ifRcw == 1) {
                                    return '<span style="color: tomato">' + parseFloat(row.actualReceived).toFixed(2) + '</span>';
                                } else {
                                    return parseFloat(row.actualReceived).toFixed(2);
                                }
                            }else{
                                return '-';
                            }
                        }
                    }, {
                        field: 'costfeeAmount',
                        title: '收款手续费',
                        align: 'center',
                        formatter: function(value,row){
                            if(value != null){
                                if(row.payType == 'RETURN_CONSTRUCT' || row.payType == 'RETURN_MODIFY' || row.payType == 'RETURN_DEPOSIT'){
                                    return '<span style="color: #5c2699 ">'+parseFloat(row.costfeeAmount).toFixed(2)+'</span>';
                                }else if(row.ifRcw == 1){
                                    return '<span style="color: tomato">'+parseFloat(row.costfeeAmount).toFixed(2)+'</span>';
                                }else{
                                    return parseFloat(row.costfeeAmount).toFixed(2);
                                }
                            }else{
                                return '-';
                            }

                        }
                    }, {
                        field: 'actualReceived',
                        title: '支',
                        align: 'center',
                        formatter: function(value,row){
                            if (row.payType == 'RETURN_CONSTRUCT' || row.payType == 'RETURN_MODIFY' || row.payType == 'RETURN_DEPOSIT') {
                                return '<span style="color: #5c2699 ">' + parseFloat(row.actualReceived * -1).toFixed(2) + '</span>';
                            } else {
                                return '-';
                            }
                             if (row.ifRcw == 1) {
                                 if (row.payType == 'RETURN_CONSTRUCT' || row.payType == 'RETURN_MODIFY' || row.payType == 'RETURN_DEPOSIT') {
                                     return '<span style="color: tomato">' + parseFloat(row.actualReceived * -1).toFixed(2) + '</span>';
                                 }else{
                                     return '<span style="color: #5c2699 ">' + parseFloat(row.actualReceived * -1).toFixed(2) + '</span>';
                                 }
                             }

                        }
                    }, {
                        field: 'receiptNum',
                        title: '收据号',
                        align: 'center',
                        formatter: function(value,row){
                            if(value != null) {
                                if (row.payType == 'RETURN_CONSTRUCT' || row.payType == 'RETURN_MODIFY' || row.payType == 'RETURN_DEPOSIT') {
                                    return '<span style="color: #5c2699 ">' + row.receiptNum + '</span>';
                                } else if (row.ifRcw == 1) {
                                    return '<span style="color: tomato">' + row.receiptNum + '</span>';
                                } else {
                                    return row.receiptNum;
                                }
                            }else{
                                return '-';
                            }
                        }
                    }, {
                        field: 'paymethodName',
                        title: '收付款方式',
                        align: 'center',
                        formatter: function(value,row){
                            if(value != null) {
                                if (row.payType == 'RETURN_CONSTRUCT' || row.payType == 'RETURN_MODIFY' || row.payType == 'RETURN_DEPOSIT') {
                                    return '<span style="color: #5c2699 ">' + row.paymethodName + '</span>';
                                } else if (row.ifRcw == 1) {
                                    return '<span style="color: tomato">' + row.paymethodName + '</span>';
                                } else {
                                    return row.paymethodName;
                                }
                            }else{
                                return '-';
                            }
                        }
                    }, {
                        field: 'paymethodAttrFullname',
                        title: '银行名称',
                        align: 'center',
                        formatter: function(value,row){
                            if(value != null) {
                                if (row.payType == 'RETURN_CONSTRUCT' || row.payType == 'RETURN_MODIFY' || row.payType == 'RETURN_DEPOSIT') {
                                    return '<span style="color: #5c2699 ">' + row.paymethodAttrFullname + '</span>';
                                } else if (row.ifRcw == 1) {
                                    return '<span style="color: tomato">' + row.paymethodAttrFullname + '</span>';
                                } else {
                                    return row.paymethodAttrFullname;
                                }
                            }else{
                                return '-';
                            }
                        }
                    }, {
                        field: 'orderFlowStatus',
                        title: '订单状态',
                        align: 'center',
                        formatter: function (value,row) {
                            if(value != null) {
                                if ( row.payType == 'RETURN_CONSTRUCT' || row.payType == 'RETURN_MODIFY' || row.payType == 'RETURN_DEPOSIT') {
                                    switch (value) {
                                        case 'STAY_TURN_DETERMINE':
                                            return '<span style="color: #5c2699 ">待转大定</span>';
                                        case 'SUPERVISOR_STAY_ASSIGNED':
                                            return '<span style="color: #5c2699 ">督导组长待分配</span>';
                                        case 'DESIGN_STAY_ASSIGNED':
                                            return '<span style="color: #5c2699 ">设计待分配</span>';
                                        case 'APPLY_REFUND':
                                            return '<span style="color: #5c2699 ">申请退回</span>';
                                        case 'STAY_DESIGN':
                                            return '<span style="color: #5c2699 ">待设计</span>';
                                        case 'STAY_SIGN':
                                            return '<span style="color: #5c2699 ">待签约</span>';
                                        case 'STAY_SEND_SINGLE_AGAIN':
                                            return '<span style="color: #5c2699 ">待重新派单</span>';
                                        case 'STAY_CONSTRUCTION':
                                            return '<div style="width:100px;">待施工</div>';
                                        case 'ON_CONSTRUCTION':
                                            return '<div style="width:100px;">施工中</div>';
                                        case 'PROJECT_COMPLETE':
                                            return '<span style="color: #5c2699 ">已竣工</span>';
                                        case 'ORDER_CLOSE':
                                            return '<span style="color: #5c2699 ">订单关闭</span>';
                                    }
                                } else if (row.ifRcw == 1) {
                                    switch (value) {
                                        case 'STAY_TURN_DETERMINE':
                                            return '<span style="color: tomato ">待转大定</span>';
                                        case 'SUPERVISOR_STAY_ASSIGNED':
                                            return '<span style="color: tomato">督导组长待分配</span>';
                                        case 'DESIGN_STAY_ASSIGNED':
                                            return '<span style="color: tomato ">设计待分配</span>';
                                        case 'APPLY_REFUND':
                                            return '<span style="color: tomato ">申请退回</span>';
                                        case 'STAY_DESIGN':
                                            return '<span style="color: tomato ">待设计</span>';
                                        case 'STAY_SIGN':
                                            return '<span style="color: tomato ">待签约</span>';
                                        case 'STAY_SEND_SINGLE_AGAIN':
                                            return '<span style="color: tomato ">待重新派单</span>';
                                        case 'STAY_CONSTRUCTION':
                                            return '<span style="color: tomato ">待施工</span>';
                                        case 'ON_CONSTRUCTION':
                                            return '<span style="color: tomato ">施工中</span>';
                                        case 'PROJECT_COMPLETE':
                                            return '<span style="color: tomato ">已竣工</span>';
                                        case 'ORDER_CLOSE':
                                            return '<span style="color: tomato ">订单关闭</span>';
                                    }
                                } else {
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
                            }else{
                                return '-';
                            }
                        }
                    }, {
                        field: '',
                        title: '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;会计科目&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;',
                        align: 'center',
                        formatter: function(value,row){
                            if( row.payType == 'RETURN_CONSTRUCT' || row.payType == 'RETURN_MODIFY' || row.payType == 'RETURN_DEPOSIT') {
                                if(row.payManualFlag == '首期款' || row.payManualFlag == '中期款' || row.payManualFlag == '尾款'
                                    || row.payManualFlag == '补交首期款' || row.payManualFlag == '首期款补交'
                                    || row.payManualFlag == '中期款补交' || row.payManualFlag == '补交中期款'
                                    || row.payManualFlag == '拆改费'){
                                    return '<span style="color: #5c2699 ">预收账款-住宅装修</span>';
                                }else if(row.payManualFlag == '小定'){
                                    return '<span style="color: #5c2699 ">其他应付款-客户定金-小订</span>';
                                }else if(row.payManualFlag == '大定'){
                                    return '<span style="color: #5c2699 ">其他应付款-客户定金-大定</span>';
                                }
                            }else if(row.ifRcw == 1){
                                if(row.payManualFlag == '首期款' || row.payManualFlag == '中期款' || row.payManualFlag == '尾款'
                                        || row.payManualFlag == '补交首期款' || row.payManualFlag == '首期款补交'
                                        || row.payManualFlag == '中期款补交' || row.payManualFlag == '补交中期款'
                                        || row.payManualFlag == '拆改费'){
                                    return '<span style="color: tomato ">预收账款-住宅装修</span>';
                                }else if(row.payManualFlag == '小定'){
                                    return '<span style="color: tomato ">其他应付款-客户定金-小订</span>';
                                }else if(row.payManualFlag == '大定'){
                                    return '<span style="color: tomato ">其他应付款-客户定金-大定</span>';
                                }
                            }else{
                                if(row.payManualFlag == '首期款' || row.payManualFlag == '中期款' || row.payManualFlag == '尾款'
                                    || row.payManualFlag == '补交首期款' || row.payManualFlag == '首期款补交'
                                    || row.payManualFlag == '中期款补交' || row.payManualFlag == '补交中期款'
                                    || row.payManualFlag == '拆改费'){
                                    return '预收账款-住宅装修';
                                }else if(row.payManualFlag == '小定'){
                                    return '其他应付款-客户定金-小订';
                                }else if(row.payManualFlag == '大定'){
                                    return '其他应付款-客户定金-大定';
                                }
                            }
                        }
                    }, {
                        field: 'payManualFlag',
                        title: '&nbsp;&nbsp;&nbsp;&nbsp;款项类别&nbsp;&nbsp;&nbsp;&nbsp;',
                        align: 'center',
                        formatter: function(value,row){
                            if(value != null) {
                                if ( row.payType == 'RETURN_CONSTRUCT' || row.payType == 'RETURN_MODIFY' || row.payType == 'RETURN_DEPOSIT') {
                                    return '<span style="color: #5c2699 ">' + row.payManualFlag + '</span>';
                                } else if (row.ifRcw == 1) {
                                    return '<span style="color: tomato">' + row.payManualFlag + '</span>';
                                } else {
                                    return row.payManualFlag;
                                }
                            }else{
                                return '-';
                            }
                        }
                    }, {
                        field: 'buildArea',
                        title: '面积(m²)',
                        align: 'center',
                        formatter: function(value,row){
                            if(value != null) {
                                if (row.payType == 'RETURN_CONSTRUCT' || row.payType == 'RETURN_MODIFY' || row.payType == 'RETURN_DEPOSIT') {
                                    return '<span style="color: #5c2699 ">' + row.buildArea + '</span>';
                                } else if (row.ifRcw == 1) {
                                    return '<span style="color: tomato">' + row.buildArea + '</span>';
                                } else {
                                    return row.buildArea;
                                }
                            }else{
                                return '-';
                            }
                        }
                    }, {
                        field: 'contractStartTime',
                        title: '合同开工时间',
                        align: 'center',
                        formatter: function(value,row){
                            if(value != null) {
                                if (row.payType == 'RETURN_CONSTRUCT' || row.payType == 'RETURN_MODIFY' || row.payType == 'RETURN_DEPOSIT') {
                                    return '<span style="color: #5c2699 ">' + row.contractStartTime + '</span>';
                                } else if (row.ifRcw == 1) {
                                    return '<span style="color: tomato">' + row.contractStartTime + '</span>';
                                } else {
                                    return row.contractStartTime;
                                }
                            }else{
                                return '-';
                            }
                        }
                    }, {
                        field: 'ifRcw',
                        title: '是否红冲',
                        align: 'center',
                        sortable: true,
                        formatter: function (value,row) {
                            if( row.payType == 'RETURN_CONSTRUCT' || row.payType == 'RETURN_MODIFY' || row.payType == 'RETURN_DEPOSIT'){
                                if (value == 1) {
                                    return '<span style="color: #5c2699">是</span>'
                                } else {
                                    return '<span style="color: #5c2699">否</span>'
                                }
                            }else if(row.ifRcw == 1) {
                                if (value == 1) {
                                    return '<span style="color: tomato">是</span>'
                                } else {
                                    return '否'
                                }
                            }else{
                                if (value == 1) {
                                    return '是'
                                } else {
                                    return '否'
                                }
                            }
                        }
                    }]
                });

            },

            //导出
            exportData: function () {
                var self = this;
                window.location.href ='/finance/analyze/exportpaymoneyrecord?keyword=' + self.form.keyword
                    + '&paystartTime=' + self.form.paystartTime
                    + '&payendTime=' + self.form.payendTime
                    + '&contractCode=' + self.form.contractCode
                    + '&payerName=' + self.form.payerName
                    + '&payerMobile=' + self.form.payerMobile
                    + '&ifRcw=' + self.form.ifRcw
                    + '&paymethodName=' + self.form.paymethodName
                    + '&templateStageId=' + self.form.templateStageId;
            }
        }
    });
})();