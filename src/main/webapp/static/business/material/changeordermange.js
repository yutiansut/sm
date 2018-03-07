var vueIndex;
+(function () {
    $('#changeMange').addClass('active');
    $('#changeOrderMange').addClass('active');
    vueIndex = new Vue({
        el: '#container',
        data: {
            breadcrumbs: [{
                path: '/',
                name: '主页'
            }, {
                path: '',
                name: '变更管理',
            }, {
                path: '/',
                name: '变更单管理',
                active: true
            }],
            form: {
                keyword: '',
                currentStatus: '',
                changeStatus: '',
                designerName: '',
                auditorName: ''
            },
            designers: {},
            auditors: {},
            $dataTable: null,
            _$el: null,
            //所有的角色集合
            roleNameAllList: [
                "设计师", "材料部审核员", "设计总监", "审计经理", "审计员", "管理员"
            ],
            //个人角色集合
            roleNameList: [],
            //变更单状态集合
            contractStatuList: [
                {name: "材料部审核中", value: "MATERIALDEPARTMENTAUDIT"},
                {name: "设计总监审核中", value: "DESIGNDIRECTORINTHEAUDIT"},
                {name: "审计审核中", value: "AUDITREVIEW"},
                {name: "变更单撤回", value: "CHANGEORDERRECALL"},
                {name: "审计通过", value: "EXAMINATIONPASSED"},
                {name: "审计未通过", value: "AUDITFAILED"},
            ],
        },
        created: function () {
            this.dealRoleNameList();
        },
        ready: function () {
            this.drawTable();
        },
        methods: {
            //注意: DameiUser.roleNameList 是个字符串,需要转为数组!否则影响操作按钮角色的判断!
            dealRoleNameList: function () {
                var self = this;
                self.roleNameList = [];
                var str = DameiUser.roleNameList.substring(1, DameiUser.roleNameList.length - 1);
                var roleNameArr = str.split(",");
                roleNameArr.forEach(function (item) {
                    //去掉两端空格
                    self.roleNameList.push(item.trim());
                });
            },
            query: function () {
                this.$dataTable.bootstrapTable('selectPage', 1);
            },
            drawTable: function () {
                var self = this;
                self.$dataTable = $('#dataTable').bootstrapTable({
                    url: '/material/changedetail/findchangeorderlist',
                    method: 'get',
                    dataType: 'json',
                    cache: false,
                    pagination: true,
                    sidePagination: 'server',
                    queryParams: function (params) {
                        return _.extend({}, params, self.form);
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
                            title: '项目编号',
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
                            align: 'center'
                        }, {
                            field: 'mobile',
                            title: '联系方式',
                            orderable: false,
                            align: 'center'
                        },
                        {
                            field: 'designer',
                            title: '设计师',
                            orderable: false,
                            align: 'center'
                        },
                        {
                            field: 'changeCategoryName',
                            title: '所属类目',
                            orderable: false,
                            align: 'center',
                            formatter: function (val) {
                                var label = val;
                                if (val) {

                                } else {
                                    label = '其他金额增减';
                                }
                                return label;
                            }
                        },
                        {
                            field: 'currentStatus',
                            title: '审核状态',
                            orderable: false,
                            align: 'center',
                            formatter: function (val) {
                                switch (val) {
                                    case 'AUDITREVIEW':
                                        return '审计审核中';
                                    case 'EXAMINATIONPASSED':
                                        return '审核通过';
                                    case 'AUDITFAILED':
                                        return '<font color="red">审核未通过</font>';
                                    case 'MATERIALDEPARTMENTAUDIT':
                                        return '材料部审核中';
                                    case 'DESIGNDIRECTORINTHEAUDIT':
                                        return '设计总监审核中';
                                    case 'CHANGEORDERRECALL':
                                        return '变更单撤回';
                                }
                            }
                        },
                        {
                            field: 'createTime',
                            title: '变更完成时间',
                            orderable: false,
                            align: 'center'
                        },
                        {
                            field: 'downloadStatus',
                            title: '下载状态',
                            orderable: false,
                            align: 'center',
                            formatter: function (value, row) {
                                switch (value) {
                                    case '0':
                                        return '<font color="red">未下载</font>';
                                    case '1':
                                        return '已下载';
                                }
                            }
                        },
                        {
                            field: 'downloadTimes',
                            title: '下载次数',
                            orderable: false,
                            align: 'center'
                        },
                        {
                            field: 'id',
                            title: '操作',
                            orderable: false,
                            align: 'center',
                            formatter: function (value, row) {
                                var html = '';
                                if (row.currentStatus == self.contractStatuList[0].value && self.roleNameList) {
                                    //材料部审核中
                                    if (self.roleNameList.indexOf(self.roleNameAllList[0]) != -1) {
                                        //变更撤回 按钮
                                        html += '<button data-handle="data-recall" data-id="' + row.id + '" ' +
                                            'data-changelogid="' + row.changeLogId + '" ' +
                                            'data-contractcode="' + row.contractCode + '" ' +
                                            'data-changecategoryurl="' + row.changeCategoryUrl + '"' +
                                            'class="m-r-xs btn btn-xs btn-danger" type="button">撤回</button>';
                                    }
                                    if (self.roleNameList.indexOf(self.roleNameAllList[0]) != -1 ||
                                        self.roleNameList.indexOf(self.roleNameAllList[1]) != -1) {
                                        //查看 按钮
                                        html += '<button data-handle="data-detail" data-id="' + row.id + '" data-status="' + row.currentStatus + '"    data-url="'
                                            + row.changeCategoryUrl + '" data-changeno="' + row.changeNo + '" data-contract-code="' + row.contractCode
                                            + '" class="m-r-xs btn btn-xs btn-primary" type="button">查看</button>';
                                    }
                                    if (self.roleNameList.indexOf(self.roleNameAllList[1]) != -1) {
                                        //审计 按钮
                                        //html += '<button data-handle="data-detail" data-id="' + row.id + '" data-contractcode="' + row.contractCode + '" class="m-r-xs btn btn-xs btn-primary" type="button">变更详情</button>';
                                        //html += " 审计 ";
                                    }
                                } else if (row.currentStatus == self.contractStatuList[1].value && self.roleNameList) {
                                    //设计总监审核中
                                    if (self.roleNameList.indexOf(self.roleNameAllList[0]) != -1) {
                                        //变更撤回 按钮
                                        html += '<button data-handle="data-recall" data-id="' + row.id + '" ' +
                                            'data-changelogid="' + row.changeLogId + '" ' +
                                            'data-contractcode="' + row.contractCode + '" ' +
                                            'data-changecategoryurl="' + row.changeCategoryUrl + '"' +
                                            'class="m-r-xs btn btn-xs btn-danger" type="button">撤回</button>';
                                    }
                                    if (self.roleNameList.indexOf(self.roleNameAllList[0]) != -1 ||
                                        self.roleNameList.indexOf(self.roleNameAllList[1]) != -1 ||
                                        self.roleNameList.indexOf(self.roleNameAllList[2]) != -1) {
                                        //查看 按钮
                                        html += '<button data-handle="data-detail" data-id="' + row.id + '" data-status="' + row.currentStatus + '"    data-url="'
                                            + row.changeCategoryUrl + '" data-changeno="' + row.changeNo + '" data-contract-code="' + row.contractCode
                                            + '" class="m-r-xs btn btn-xs btn-primary" type="button">查看</button>';
                                    }
                                    if (self.roleNameList.indexOf(self.roleNameAllList[2]) != -1) {
                                        //审计 按钮
                                        //html += '<button data-handle="data-detail" data-id="' + row.id + '" data-contractcode="' + row.contractCode + '" class="m-r-xs btn btn-xs btn-primary" type="button">变更详情</button>';
                                        //html += " 审计 ";
                                    }

                                } else if (row.currentStatus == self.contractStatuList[2].value && self.roleNameList) {
                                    //审计审核中
                                    if (self.roleNameList.indexOf(self.roleNameAllList[0]) != -1) {
                                        //变更撤回 按钮
                                        html += '<button data-handle="data-recall" data-id="' + row.id + '" ' +
                                            'data-changelogid="' + row.changeLogId + '" ' +
                                            'data-contractcode="' + row.contractCode + '" ' +
                                            'data-changecategoryurl="' + row.changeCategoryUrl + '"' +
                                            'class="m-r-xs btn btn-xs btn-danger" type="button">撤回</button>';
                                    }
                                    if (self.roleNameList.indexOf(self.roleNameAllList[0]) != -1 ||
                                        self.roleNameList.indexOf(self.roleNameAllList[1]) != -1 ||
                                        self.roleNameList.indexOf(self.roleNameAllList[3]) != -1 ||
                                        self.roleNameList.indexOf(self.roleNameAllList[4]) != -1) {
                                        //查看 按钮
                                        html += '<button data-handle="data-detail" data-id="' + row.id + '" data-status="' + row.currentStatus + '"    data-url="'
                                            + row.changeCategoryUrl + '" data-changeno="' + row.changeNo + '" data-contract-code="' + row.contractCode
                                            + '" class="m-r-xs btn btn-xs btn-primary" type="button">查看</button>';
                                    }
                                    if (self.roleNameList.indexOf(self.roleNameAllList[3]) != -1
                                        || self.roleNameList.indexOf(self.roleNameAllList[4]) != -1) {
                                        //审计 按钮
                                        //html += '<button data-handle="data-detail" data-id="' + row.id + '" data-contractcode="' + row.contractCode + '" class="m-r-xs btn btn-xs btn-primary" type="button">变更详情</button>';
                                        //html += " 审计 ";
                                    }
                                } else if (row.currentStatus == self.contractStatuList[3].value && self.roleNameList) {
                                    //变更单撤回
                                } else if (row.currentStatus == self.contractStatuList[4].value && self.roleNameList) {
                                    //审计通过
                                    if (self.roleNameList.indexOf(self.roleNameAllList[0]) != -1 ||
                                        self.roleNameList.indexOf(self.roleNameAllList[1]) != -1 ||
                                        self.roleNameList.indexOf(self.roleNameAllList[3]) != -1 ||
                                        self.roleNameList.indexOf(self.roleNameAllList[4]) != -1) {
                                        //查看 按钮
                                        html += '<button data-handle="data-detail" data-id="' + row.id + '" data-status="' + row.currentStatus + '"    data-url="'
                                            + row.changeCategoryUrl + '" data-changeno="' + row.changeNo + '" data-contract-code="' + row.contractCode
                                            + '" class="m-r-xs btn btn-xs btn-primary" type="button">查看</button>';
                                    }
                                    if (self.roleNameList.indexOf(self.roleNameAllList[1]) != -1) {
                                        //下载 按钮
                                        html += '<button data-handle="data-download" data-contract-code="' + row.contractCode
                                            + '" data-change-no="' + row.changeNo + '" data-currentstatus="' + row.currentStatus + '" data-url="' + row.changeCategoryUrl + '" class="m-r-xs btn btn-xs btn-primary" type="button">下载</button>';
                                    }
                                } else if (row.currentStatus == self.contractStatuList[5].value && self.roleNameList) {
                                    //审计未通过
                                    if (self.roleNameList.indexOf(self.roleNameAllList[0]) != -1) {
                                        //变更撤回 按钮
                                        html += '<button data-handle="data-recall" data-id="' + row.id + '" ' +
                                            'data-changelogid="' + row.changeLogId + '" ' +
                                            'data-contractcode="' + row.contractCode + '" ' +
                                            'data-changecategoryurl="' + row.changeCategoryUrl + '"' +
                                            'class="m-r-xs btn btn-xs btn-danger" type="button">撤回</button>';
                                    }
                                    if (self.roleNameList.indexOf(self.roleNameAllList[0]) != -1) {
                                        //变更 按钮
                                        //row.changeCategoryUrl 为空时,视为其他金额增减被打回,传递other!!!
                                        html += '<button data-handle="data-change" data-contractcode="'
                                            + row.contractCode + '"data-categoryurl="'
                                            + row.changeCategoryUrl + '" class="m-r-xs btn btn-xs btn-warning">变更</button>';

                                        html += '<button data-handle="data-change-submit" data-id="' + row.id + '" ' +
                                            'data-changelogid="' + row.changeLogId + '" ' +
                                            'data-contractcode="' + row.contractCode + '" ' +
                                            'data-changecategoryurl="' + row.changeCategoryUrl + '"' +
                                            'class="m-r-xs btn btn-xs btn-success" type="button">变更提交</button>';
                                    }
                                    if (self.roleNameList.indexOf(self.roleNameAllList[0]) != -1 ||
                                        self.roleNameList.indexOf(self.roleNameAllList[1]) != -1 ||
                                        self.roleNameList.indexOf(self.roleNameAllList[3]) != -1 ||
                                        self.roleNameList.indexOf(self.roleNameAllList[4]) != -1) {
                                        //查看 按钮
                                        html += '<button data-handle="data-detail" data-id="' + row.id + '" data-status="' + row.currentStatus + '"    data-url="'
                                            + row.changeCategoryUrl + '" data-changeno="' + row.changeNo + '" data-contract-code="' + row.contractCode
                                            + '" class="m-r-xs btn btn-xs btn-primary" type="button">查看</button>';
                                    }
                                }
                                return html;
                            }
                        }]
                });

                //变更提交
                self.$dataTable.on('click', '[data-handle="data-change-submit"]',
                    function (e) {
                        var id = $(this).data().id;
                        var contractCode = $(this).data().contractcode;
                        var changeCategoryUrl = $(this).data().changecategoryurl;
                        swal({
                            title: '确认提交本次变更单？',
                            text: '',
                            type: 'info',
                            confirmButtonText: '确定',
                            cancelButtonText: '取消',
                            showCancelButton: true,
                            showConfirmButton: true,
                            showLoaderOnConfirm: true,
                            confirmButtonColor: '#ed5565',
                            closeOnConfirm: false
                        }, function () {
                            var data = {
                                id: id,
                                contractCode: contractCode,
                                changeCategoryUrl: changeCategoryUrl || null
                            }
                            self.$http.post('/material/changedetail/submit', data, {emulateJSON: true}).then(function (res) {
                                if (res.data.code == 1) {
                                    Vue.toastr.success("变更单提交成功！");
                                    vueIndex.$dataTable.bootstrapTable('refresh');
                                } else {
                                    Vue.toastr.error("变更单提交失败！");
                                }
                            }).catch(function () {

                            }).finally(function () {
                                swal.close();
                            });
                        });
                        e.stopPropagation();
                    }
                );
                //变更
                self.$dataTable.on('click', '[data-handle="data-change"]',
                    function (e) {
                        var model = $(this).data();
                        var changeCategoryUrl = model.categoryurl || 'other';
                        var params = {
                            contractCode: model.contractcode,
                            pageType: 'change',
                            catalogUrl: changeCategoryUrl
                        }
                        DameiUtils.locationHrefToClient('/material/materialIndex', params);
                    }
                );
                //下载
                self.$dataTable.on('click', '[data-handle="data-download"]',
                    function (e) {
                        var model = $(this).data();
                        //href="/exportexcel/changeorderexport?contractCode='+row.contractCode + '&changeNo='+ row.changeNo+'"
                        var contractCode = $(this).data().contractCode;
                        var changeNo = $(this).data().changeNo;
                        var changeCategoryUrl = $(this).data('url');
                        var currentStatus = $(this).data('currentstatus');
                        if (changeCategoryUrl == null || changeCategoryUrl == undefined || changeCategoryUrl == 'null') {
                            changeCategoryUrl = 'other';
                        }
                        window.location.href = '/material/exportexcel/changeorderexport?contractCode=' + contractCode
                            + '&changeNo=' + changeNo + '&currentStatus=' + currentStatus + '&changeCategoryUrl=' + changeCategoryUrl + '&downLoadFlag=' + true;
                        setTimeout(function () {
                            vueIndex.$dataTable.bootstrapTable('refresh');
                        }, 1500);
                        e.stopPropagation();
                    }
                );
                //查看
                self.$dataTable.on('click', '[data-handle="data-detail"]',
                    function (e) {
                        var model = $(this).data();
                        showDetailModal(model);
                        e.stopPropagation();
                    }
                );

                // 撤回
                self.$dataTable.on('click', '[data-handle="data-recall"]',
                    function (e) {
                        var id = $(this).data('id');
                        var changeLogId = $(this).data('changelogid');
                        var contractCode = $(this).data('contractcode');
                        var changeCategoryUrl = $(this).data('changecategoryurl');
                        var status = 'wait_transfer';
                        swal({
                            title: '确认撤回？',
                            text: '将修改变更单状态',
                            type: 'info',
                            confirmButtonText: '确定',
                            cancelButtonText: '取消',
                            showCancelButton: true,
                            showConfirmButton: true,
                            showLoaderOnConfirm: true,
                            confirmButtonColor: '#ed5565',
                            closeOnConfirm: false
                        }, function () {
                            self.$http.get('/material/changedetail/updatestatus?id=' + id + '&changeLogId=' + changeLogId + '&contractCode=' + contractCode + '&changeCategoryUrl=' + changeCategoryUrl).then(function (res) {
                                if (res.data.code == 1) {
                                    Vue.toastr.success("撤回变更单成功！");
                                    vueIndex.$dataTable.bootstrapTable('refresh');
                                } else {
                                    Vue.toastr.error("撤回变更单失败！");
                                }
                            }).catch(function () {

                            }).finally(function () {
                                swal.close();
                            });
                        });
                        e.stopPropagation();
                    });
                // 审核
                self.$dataTable.on('click', '[data-handle="data-audit"]',
                    function (e) {
                        var model = $(this).data();
                        auditModal(model);
                        e.stopPropagation();
                    });
            }
        }
    });
})();

function auditModal(model) {
    var $modal = $('#auditModal').clone();
    var $el = $modal.modal({
        height: 100,
        width: 400
    });
    $modal.on('shown.bs.modal',
        function () {
            otherFeeVue = new Vue({
                el: $modal.get(0),
                data: {
                    type: '',
                    remark: '',
                    id: model.id,
                    changeNo: model.changeno,
                    changeLogId: model.changelogid,
                    contractCode: model.contractcode,
                    changeCategoryUrl: model.changecategoryurl
                },
                created: function () {
                },
                ready: function () {
                },
                methods: {
                    pass: function () {
                        var self = this;
                        self._data.type = 1;
                        self.$http.post('/material/smmaterialchangeauditrecord/updatestatus', self._data, {emulateJSON: true}).then(function (res) {
                            if (res.data.code == 1) {
                                Vue.toastr.success("变更单审核已通过！");
                                vueIndex.$dataTable.bootstrapTable('refresh');
                                $el.modal('hide');
                            }
                        })
                    },
                    nopass: function () {
                        var self = this;
                        self._data.type = 0;
                        self.$http.post('/material/smmaterialchangeauditrecord/updatestatus', self._data, {emulateJSON: true}).then(function (res) {
                            if (res.data.code == 1) {
                                Vue.toastr.success("变更单审核未通过！");
                                $el.modal('hide');
                            }
                        })
                    }
                },
            })
        });


}

//查看变更详情
function showDetailModal(model) {
    var _modal = $('#showDetailModal').clone();
    var $el = _modal.modal({
        height: 700,
        maxHeight: 500
    });
    $el.on('shown.bs.modal',
        function () {
            // 获取 node
            var el = $el.get(0);
            // 创建 Vue 对象编译节点
            var vueModal = new Vue({
                el: el,
                data: {
                    customerContract: null,
                    addMoney: 0,
                    subMoney: 0,
                    otherMoney: 0,
                    skuList: null,
                    otherList: null,
                    remarks: null
                },
                // 模式窗体必须引用 ModalMixin
                mixins: [DameiVueMixins.ModalMixin],
                $modal: $el,
                //模式窗体 jQuery 对象
                created: function () {
                },
                ready: function () {
                    this.findCustomerContract();
                    this.findRemarks();
                },
                filters: {
                    goDate: function (el) {
                        if (el) {
                            return moment(el).format('YYYY-MM-DD');
                        }
                    },
                    goType: function (val) {
                        if (val = '1') {
                            return '有';
                        } else {
                            return '无';
                        }
                    },
                    houseStatus: function (val) {
                        if (val = '1') {
                            return '新房';
                        } else {
                            return '旧房';
                        }
                    },
                    houseType: function (val) {
                        if (val = '1') {
                            return '复式';
                        } else if (val = '2') {
                            return '别墅';
                        } else {
                            return '楼房平层';
                        }
                    },
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
                    addReduceType: function (value) {
                        return value == '1' ? '增加金额' : '减少金额';
                    },
                    unitFilter: function (value) {
                        if (value) {
                            //去空格
                            value = value.replace(/\s+/g, "");
                            //去掉 元/
                            if (value.indexOf("元/") >= 0) {
                                value = value.replace("元/", "");
                            }
                        }
                        return value;
                    }
                },
                methods: {
                    //获取合同信息
                    findCustomerContract: function () {
                        var self = this;
                        self.$http.get("/customercontract/contract/get/" + model.contractCode).then(function (res) {
                            if (res.data.code == 1) {
                                self.customerContract = res.data.data;
                                self.findProMatrlByContrCode();
                            }
                        })
                    },
                    //根据合同code和变更号查询历史
                    findProMatrlByContrCode: function () {
                        var self = this;
                        //通过 查芒果
                        if (model.status == 'EXAMINATIONPASSED') {
                            self.$http.get("/material/changedetail/findpromatrlbycontrcode?contractCode=" + self.customerContract.contractCode + '&changeNo=' + model.changeno).then(function (res) {
                                if (res.data.code == 1) {
                                    self.skuList = res.data.data;
                                    if (self.skuList.length == 0) {
                                        self.findOtherAddReduceAmount(true);
                                    } else {
                                        self.findOtherAddReduceAmount(false);
                                    }
                                    //计算金额
                                    var list = self.skuList;
                                    for (var i = 0; i < list.length; i++) {
                                        if (list[i].num > 0) {
                                            switch (list[i].categoryCode) {
                                                case 'REDUCEITEM':
                                                    self.addMoney -= list[i].num * (list[i].price * 100) / 100;
                                                    break;
                                                case 'PACKAGESTANDARD' :
                                                    break;
                                                default :
                                                    self.addMoney += list[i].num * (list[i].price * 100) / 100;
                                                    break;
                                            }
                                        } else if (list[i].num < 0) {
                                            switch (list[i].categoryCode) {
                                                case 'REDUCEITEM':
                                                    self.subMoney -= list[i].num * (list[i].price * 100) / 100;
                                                    break;
                                                case 'PACKAGESTANDARD' :
                                                    break;
                                                default :
                                                    self.subMoney += list[i].num * (list[i].price * 100) / 100;
                                                    break;
                                            }
                                        }
                                    }
                                }
                            }).catch(function () {
                            }).finally(function () {
                            });
                        } else {
                            self.$http.get('/material/projectchangematerial/getauditlist?categoryUrl=' + model.url + "&contractCode=" + self.customerContract.contractCode).then(function (res) {
                                if (res.data.code == 1) {
                                    self.skuList = res.data.data;
                                    if (self.skuList.length == 0) {
                                        self.findOtherAddReduceAmount(true);
                                    } else {
                                        self.findOtherAddReduceAmount(false);
                                    }
                                    //计算金额
                                    var list = self.skuList;
                                    for (var i = 0; i < list.length; i++) {
                                        if (list[i].num > 0) {
                                            switch (list[i].categoryCode) {
                                                case 'REDUCEITEM':
                                                    self.addMoney -= list[i].num * (list[i].price * 100) / 100;
                                                    break;
                                                case 'PACKAGESTANDARD' :
                                                    break;
                                                default :
                                                    self.addMoney += list[i].num * (list[i].price * 100) / 100;
                                                    break;
                                            }
                                        } else if (list[i].num < 0) {
                                            switch (list[i].categoryCode) {
                                                case 'REDUCEITEM':
                                                    self.subMoney -= list[i].num * (list[i].price * 100) / 100;
                                                    break;
                                                case 'PACKAGESTANDARD' :
                                                    break;
                                                default :
                                                    self.subMoney += list[i].num * (list[i].price * 100) / 100;
                                                    break;
                                            }
                                        }
                                    }
                                }
                            }).catch(function () {
                            }).finally(function () {
                            });
                        }
                    },
                    //查询其他金额增减
                    findOtherAddReduceAmount: function (addFlg) {
                        var self = this;
                        //芒果
                        self.$http.get("/material/otheraddreduceamount/findOtherAddReduceAmountOnlyByContrCode/" + model.changeno+"/"+self.customerContract.contractCode).then(function (res) {
                            if (res.data.code == 1) {
                                self.otherList = res.data.data;
                                var list = self.otherList;
                                if(list!=null && list.length>0){
                                    if (addFlg) {
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
                                }else{
                                    self.$http.get('/material/otheraddreduceamount/getChangeBychangeVison/' +model.changeno ).then(function (res) {
                                        if (res.data.code == 1) {
                                            self.otherList = res.data.data;
                                            var list = self.otherList;
                                            if (addFlg) {
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
                                }
                            }
                        }).catch(function () {
                        }).finally(function () {
                        });
                    },
                    //查询备注：
                    findRemarks: function () {
                        var self = this;
                        self.$http.get("/material/changedetail/getremark/" + model.changeno).then(function (res) {
                            if (res.data.code == 1) {
                                self.remarks = res.data.data;
                            }
                        }).catch(function () {
                        }).finally(function () {
                        });
                    }
                }
            });

            // 创建的Vue对象应该被返回
            return vueModal;
        });
}
