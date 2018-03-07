+(function () {
    $('#changeMange').addClass('active');
    $('#changeAudit').addClass('active');
    var aa = new Vue({
        el: '#container',
        data: {
            breadcrumbs: [{
                path: '/',
                name: '主页'
            },
                {
                    path: '',
                    name: '变更管理',
                }, {
                    path: '/',
                    name: '变更审核',
                    active: true
                }],
            form: {
                keyword: '',
                status: '',
                catalogUrl: ''
            },
            allCatalog: '',
            statusList: '',
            $dataTable: null,
            _$el: null,
            _$dataTable: null,
            roleFlg: null
        },
        created: function () {
            this.getRole();
        },
        ready: function () {
            this.fetchCategory1();
            this.drawTable();
        },
        methods: {
            //类目1级
            fetchCategory1: function () {
                var self = this;
                self.$http.get('/material/prodsku/findcatalogfirstlist').then(function (res) {
                    if (res.data.code == 1) {
                        self.allCatalog = res.data.data;
                        if (self.roleFlg == '2' || self.roleFlg == '3') {
                            self.allCatalog.push({name: '其他金额增减', url: 'other'});
                        }
                    }
                })
            },
            query: function () {
                this.$dataTable.bootstrapTable('selectPage', 1);
            },
            drawTable: function () {
                var self = this;
                self.$dataTable = $('#dataTable', self._$el).bootstrapTable({
                    url: '/material/changedetail/list',
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
                            field: 'contractCode',
                            title: '订单编号',
                            orderable: true,
                            align: 'center'
                        },
                        {
                            field: 'changeNo',
                            title: '变更号',
                            orderable: false,
                            align: 'center'
                        },
                        {
                            field: 'name',
                            title: '客户姓名',
                            orderable: false,
                            align: 'center',
                        }, {
                            field: 'mobile',
                            title: '联系方式',
                            orderable: false,
                            align: 'center',
                            formatter: function (val) {
                                if(val){
                                    return val.substring(0, 3) + "****" + val.substring(8, 11);
                                }else{
                                    return val;
                                }
                            }
                        }, {
                            field: 'designer',
                            title: '设计师',
                            orderable: false,
                            align: 'center'
                        },
                        {
                            field: 'changeCategoryName',
                            title: '所属类目',
                            width: '10%',
                            orderable: false,
                            align: 'center',
                            formatter: function (val) {
                                var label = val
                                if (label) {
                                } else {
                                    label = '其他金额增减';
                                }
                                return label;
                            }

                        },
                        {
                            field: 'changeCategoryUrl',
                            title: '类目',
                            width: '10%',
                            orderable: false,
                            align: 'center',
                            visible: false
                        },
                        {
                            field: 'createTime',
                            title: '提交时间',
                            orderable: false,
                            align: 'center'
                        },
                        {
                            field: 'addressProvince',
                            title: '省',
                            orderable: false,
                            align: 'center',
                            visible: false
                        },
                        {
                            field: 'addressCity',
                            title: '市',
                            orderable: false,
                            align: 'center',
                            visible: false
                        },
                        {
                            field: 'addressArea',
                            title: '区县',
                            orderable: false,
                            align: 'center',
                            visible: false
                        },
                        {
                            field: 'houseAddr',
                            title: '房屋地址',
                            orderable: false,
                            align: 'center',
                            visible: false
                        },
                        {
                            field: 'designerMobile',
                            title: '设计师电话',
                            orderable: false,
                            align: 'center',
                            visible: false
                        },
                        {
                            field: 'valuateArea',
                            title: '计价面积',
                            orderable: false,
                            align: 'center',
                            visible: false
                        },
                        {
                            field: 'materialRemarks',
                            title: '材料审核备注',
                            orderable: false,
                            align: 'center',
                            visible: false
                        },
                        {
                            field: 'designDirectorRemarks',
                            title: '设计总监备注',
                            orderable: false,
                            align: 'center',
                            visible: false
                        },
                        {
                            field: 'currentStatus',
                            title: '审核状态',
                            orderable: false,
                            align: 'center',
                            formatter: function (val) {
                                switch (val) {
                                    case 'MATERIALDEPARTMENTAUDIT':
                                        return '材料部审核中';
                                    case 'DESIGNDIRECTORINTHEAUDIT':
                                        return '设计总监审核中';
                                    case 'AUDITREVIEW':
                                        return '审计审核中';
                                    case 'EXAMINATIONPASSED':
                                        return '审核通过';
                                    case 'AUDITFAILED':
                                        return '审核未通过';
                                }
                            }
                        }, {
                            field: 'id',
                            title: '操作',
                            orderable: false,
                            align: 'center',
                            formatter: function (value, row) {
                                var html = '';
                                if (row) {
                                    var ss = null;
                                    switch (self.roleFlg) {
                                        case '1':
                                            ss = 'MATERIALDEPARTMENTAUDIT';
                                            break;
                                        case '2':
                                            ss = 'DESIGNDIRECTORINTHEAUDIT';
                                            break;
                                        case '3':
                                            ss = 'AUDITREVIEW';
                                            break;
                                    }
                                    if (row.currentStatus == ss) {
                                        html += '<button data-handle="data-audit"' +
                                            ' data-id="' + row.id + '" ' +
                                            ' data-code="' + row.contractCode + '" ' +
                                            ' data-log-id="' + row.changeLogId + '" ' +
                                            ' data-no="' + row.changeNo + '" ' +
                                            ' data-name="' + row.name + '" ' +
                                            ' data-mobile="' + row.mobile + '" ' +
                                            ' data-url="' + row.changeCategoryUrl + '" ' +
                                            ' data-designer="' + row.designer + '" ' +
                                            ' data-designer-mobile="' + row.designerMobile + '" ' +
                                            ' data-valuate-area="' + row.valuateArea + '" ' +
                                            ' data-house-addr="' + row.houseAddr + '" ' +
                                            ' data-address-province="' + row.addressProvince + '" ' +
                                            ' data-address-city="' + row.addressCity + '" ' +
                                            ' data-address-area="' + row.addressArea + '" ' +
                                            ' data-remark1="' + row.materialRemarks + '" ' +
                                            ' data-remark2="' + row.designDirectorRemarks + '" ' +
                                            ' data-status="' + row.currentStatus + '" ' +
                                            'class="m-r-xs btn btn-xs btn-primary" type="button">审核</button>';
                                    }
                                    html += '<button data-handle="data-see"' +
                                        ' data-id="' + row.id + '" ' +
                                        ' data-code="' + row.contractCode + '" ' +
                                        ' data-no="' + row.changeNo + '" ' +
                                        ' data-name="' + row.name + '" ' +
                                        ' data-url="' + row.changeCategoryUrl + '" ' +
                                        ' data-mobile="' + row.mobile + '" ' +
                                        ' data-designer="' + row.designer + '" ' +
                                        ' data-designer-mobile="' + row.designerMobile + '" ' +
                                        ' data-valuate-area="' + row.valuateArea + '" ' +
                                        ' data-house-addr="' + row.houseAddr + '" ' +
                                        ' data-address-province="' + row.addressProvince + '" ' +
                                        ' data-address-city="' + row.addressCity + '" ' +
                                        ' data-address-area="' + row.addressArea + '" ' +
                                        ' data-remark1="' + row.materialRemarks + '" ' +
                                        ' data-remark2="' + row.designDirectorRemarks + '" ' +
                                        ' data-status="' + row.currentStatus + '" ' +
                                        'class="m-r-xs btn btn-xs btn-primary" type="button">查看</button>';
                                }
                                return html;
                            }
                        }]
                });
                // 审核
                self.$dataTable.on('click', '[data-handle="data-audit"]',
                    function (e) {
                        var model = $(this).data();
                        detailModal(model, true, self.roleFlg);
                        e.stopPropagation();
                    });
                //查看
                self.$dataTable.on('click', '[data-handle="data-see"]',
                    function (e) {
                        var model = $(this).data();
                        detailModal(model, false, self.roleFlg);
                        e.stopPropagation();
                    });
            },
            //获取角色和下拉
            getRole: function () {
                var roleNameList = DameiUser.roleNameList.toString();
                if (roleNameList.indexOf('材料部审核员') >= 0) {
                    this.roleFlg = '1';
                    this.statusList = [
                        {
                            name: '审计审核中',
                            value: 'AUDITREVIEW'
                        },
                        {
                            name: '审核通过',
                            value: 'EXAMINATIONPASSED'
                        },
                        {
                            name: '设计总监审核中',
                            value: 'DESIGNDIRECTORINTHEAUDIT'
                        },
                        {
                            name: '材料部审核中',
                            value: 'MATERIALDEPARTMENTAUDIT'
                        }
                    ];
                } else if (roleNameList.indexOf('设计总监') >= 0) {
                    this.roleFlg = '2';
                    this.statusList = [{
                        name: '设计总监审核中',
                        value: 'DESIGNDIRECTORINTHEAUDIT'
                    }]
                } else if (roleNameList.indexOf('审计员') >= 0) {
                    this.roleFlg = '3';
                    this.statusList = [{
                        name: '审计审核中',
                        value: 'AUDITREVIEW'
                    }]
                } else {
                    this.roleFlg = '4';
                }
            }
        }
    });

    function detailModal(model, edit, roleFlg) {
        var _modal = $('#detailModal').clone();
        var $el = _modal.modal({
            height: 480,
            maxHeight: 500
        });
        $el.on('shown.bs.modal',
            function () {
                // 获取 node
                var el = $el.get(0);
                // 创建 Vue 对象编译节点
                 vueModal = new Vue({
                    el: el,
                    // 模式窗体必须引用 ModalMixin
                    mixins: [DameiVueMixins.ModalMixin],
                    $modal: $el,
                    //模式窗体 jQuery 对象
                    created: function () {
                        this.view();
                    },
                    ready: function () {
                        this.getAuditList();
                    },
                    data: {
                        edit1:(roleFlg=='3'||roleFlg=='2')?false:edit,
                        roleFlg:roleFlg,
                        edit: edit,
                        customer: model,
                        skuList: {},
                        otherList: {},
                        remark: '',
                        ids: [],
                        status: [],
                        id: model.id,
                        //确定的按钮
                        submitting: true,
                        //通过驳回的按钮
                        submitting1: false,
                        //材料部备注
                        remarkFlg1: false,
                        //设计总监备注和材料部备注
                        remarkFlg2: false,
                        size1: 0,
                        remark1: model.remark1,
                        remark2: model.remark2,
                        addMoney:0,
                        subMoney:0,
                        otherMoney:0,
                        addFlg:false,
                        num_zero:false,
                    },
                    filters: {
                        categoryFilter: function (value) {
                            var label = '';
                            if (value == 'PACKAGESTANDARD') {
                                label = '套餐标配';
                            } else if (value == 'UPGRADEITEM') {
                                label = '升级项';
                            } else if (value == 'ADDITEM') {
                                label = '增项';
                            } else if (value == 'REDUCEITEM') {
                                label = '减项';
                            }
                            return label;
                        },
                        absNum: function (value) {
                            return -value;
                        },
                        addReduceType: function (value) {
                            return value == '1' ? '增加金额' : '减少金额';
                        },
                        unitFilter: function (value) {
                            //去空格
                            value = value.replace(/\s+/g,"");
                            //去掉 元/
                            if(value.indexOf("元/") >= 0){
                               value= value.replace("元/","");
                            }
                            return value;
                        }
                    },
                    methods: {
                        view: function () {
                            var self = this;
                            //查看
                            if (!edit) {
                                switch (roleFlg) {
                                    case '1':
                                        self.remarkFlg1 = true;
                                        break;
                                    case '2':
                                        self.remarkFlg1 = true;
                                        break;
                                    default:
                                        break;
                                }
                                //编辑
                            } else {
                                switch (roleFlg) {
                                    case '2':
                                        self.submitting1 = true
                                        break;
                                    case '3':
                                        self.submitting1 = true
                                        break;
                                    default:
                                        break;
                                }

                            }
                        },
                        getAuditList: function () {
                            var self = this;
                            self.$http.get('/material/projectchangematerial/getauditlist?categoryUrl=' + model.url + "&contractCode=" + model.code).then(function (res) {
                                if (res.data.code == 1) {
                                    self.skuList = res.data.data;
                                    if (self.skuList.length == 0) {
                                        self.addFlg=true;
                                        self.getOrtherList();
                                    }
                                    //材料部
                                    if(self.skuList.length != 0&&(roleFlg=='1'||roleFlg=='3')){
                                        self.getOrtherList();
                                    }

                                    var j = 0;
                                    for (var i = 0; i < self.skuList.length; i++) {
                                        if (self.skuList[i].num < 0 && self.skuList[i].categoryCode!='REDUCEITEM' ) {
                                            j++;
                                        }else if(self.skuList[i].num == 0){
                                             self.num_zero=true;
                                        }
                                    }
                                    self.size1 = j;
                                    //计算金额
                                    if(roleFlg=='3') {
                                        var list = self.skuList;
                                        for (var i = 0; i < list.length; i++) {
                                            if(list[i].num>0){
                                                switch (list[i].categoryCode) {
                                                    case 'REDUCEITEM':
                                                        self.addMoney-=list[i].num*(list[i].price*100)/100;
                                                        break;
                                                    case 'PACKAGESTANDARD' :
                                                        break;
                                                    default :
                                                        self.addMoney+=list[i].num*(list[i].price*100)/100;
                                                        break;
                                                }
                                            }else if(list[i].num<0){
                                                switch (list[i].categoryCode) {
                                                    case 'REDUCEITEM':
                                                        self.subMoney-=list[i].num*(list[i].price*100)/100;
                                                        break;
                                                    case 'PACKAGESTANDARD' :
                                                        break;
                                                    default :
                                                        self.subMoney+=list[i].num*(list[i].price*100)/100;
                                                        break;
                                                }
                                            }


                                        }
                                    }
                                }
                            }).catch(function () {
                            }).finally(function () {
                            });
                        },
                        getOrtherList: function () {
                            var self = this;
                            self.$http.get('/material/otheraddreduceamount/getChangeByContractCode/' + model.code).then(function (res) {
                                if (res.data.code == 1) {
                                    self.otherList = res.data.data;
                                    if(roleFlg=='3' && self.addFlg) {
                                        var list= self.otherList;
                                        for (var i = 0; i < list.length; i++) {
                                                switch (list[i].addReduceType) {
                                                    case '1' :
                                                        self.otherMoney += (list[i].quota * 100) / 100;
                                                        break;
                                                    default :
                                                        self.otherMoney -= (list[i].quota * 100) / 100;
                                                        break;
                                                }
                                        }
                                    }
                                }
                            }).catch(function () {
                            }).finally(function () {
                            });
                        },
                        submit: function (result) {
                            var self = this;
                            //材料部审核提交
                            if (roleFlg == '1') {
                                //修改 detail 状态 并添加备注
                                var stat = 1;
                                // 0 拒绝  1 通过  2 驳回
                                for (var i = 0; i < self.status.length; i++) {
                                    var sta = self.status[i];
                                    if (sta == 'MEASURED' || sta == 'ALREADY_ORDERED' || sta == 'SHIPPED' || sta == 'INSTALLED') {
                                        var stat = 2;
                                        break;
                                    }
                                    if (sta == 'UNFILLED_ORDER_CONDITIONS') {
                                        var stat = 0;
                                        break;
                                    }
                                }
                                var data = {
                                    id: self.id,
                                    remark: self.remark,
                                    status: stat,
                                    current: model.status,
                                    ids: self.ids,
                                    changeNo: model.no,
                                    statusList: self.status,
                                };
                                self.$http.post('/material/projectchangematerial/detail', data, {emulateJSON: true}).then(function (res) {
                                    if (res.data.code = 1) {
                                        Vue.toastr.success("操作成功");
                                        aa.$dataTable.bootstrapTable('refresh');
                                    }
                                }).catch(function () {
                                }).finally(function () {
                                });
                                //设计总监
                            } else if (roleFlg == '2') {
                                var data = {
                                    id: self.id,
                                    remark: self.remark,
                                    status: result,
                                    changeNo: model.no,
                                    current: model.status,
                                };
                                self.$http.post('/material/projectchangematerial/detail', data, {emulateJSON: true}).then(function (res) {
                                    if (res.data.code = 1) {
                                        Vue.toastr.success("操作成功");
                                        aa.$dataTable.bootstrapTable('refresh');
                                    }
                                }).catch(function () {
                                }).finally(function () {
                                });
                                //审计
                            } else if (roleFlg == '3') {
                                if (result == '2') {
                                    result = '0';
                                } else {
                                    result = '1';
                                }
                                var data = {
                                    id: self.id,
                                    remark: self.remark,
                                    type: result,
                                    changeNo: model.no,
                                    changeLogId: model.logId,
                                    contractCode: model.code,
                                    changeCategoryUrl: model.url,
                                };
                                self.$http.post('/material/smmaterialchangeauditrecord/updatestatus', data, {emulateJSON: true}).then(function (res) {
                                    if (res.data.code = 1) {
                                        Vue.toastr.success("操作成功");
                                        aa.$dataTable.bootstrapTable('refresh');
                                    }
                                }).catch(function () {
                                }).finally(function () {
                                });
                            }
                        }
                    },
                    watch: {
                        'status': {
                            handler: function (newValue, oldValue) {
                                var num = 0;
                                for (var i = 0; i < newValue.length; i++) {
                                    if (newValue[i]) {
                                        num = num + 1;
                                    }
                                }
                                if (num == this.size1) {
                                    this.submitting = false;
                                }
                            },
                            deep: true
                        }
                    }
                });

                // 创建的Vue对象应该被返回
                return vueModal;
            });
    }

})();