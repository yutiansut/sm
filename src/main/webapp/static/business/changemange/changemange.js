var contrCode;
+(function () {
    $('#changeMange').addClass('active');
    $('#changeManage').addClass('active');
    var vueIndex = new Vue({
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
                    path: '',
                    name: '变更管理',
                    active: true
                }],
            form: {
                keyword: '',
                contractStatus: '',
                changeTime: '',
                designerName: '',
                auditorName: ''
            },
            designers: {},
            auditors: {},
            $dataTable: null,
            _$el: null,
            _$dataTable: null,
            //所有的角色集合
            roleNameAllList: [
                "设计师", "审计经理", "审计员", "材料部审核员", "设计总监",
                "材料部经理", "工程结算员","管理员"
            ],
            //个人角色集合
            roleNameList: [],
            //选材单状态集合
            contractStatuList: [
                {name: "变更中", value: "CHANGING"},
                {name: "变更审计中", value: "CHANGE_AUDIT"},
                {name: "转单完成", value: "TRANSFER_COMPLETE"},
            ],
        },
        created: function () {
            this.dealRoleNameList();
        },
        ready: function () {
            this.fetchDesigner();
            this.fetchAuditor();
            this.activeDatepiker();
            this.drawTable();
        },
        methods: {
            //注意: DameiUser.roleNameList 是个字符串,需要转为数组!否则影响操作按钮角色的判断!
            dealRoleNameList: function () {
                var self = this;
                self.roleNameList = [];
                var str = DameiUser.roleNameList.substring(1, DameiUser.roleNameList.length -1);
                var roleNameArr = str.split(",");
                roleNameArr.forEach(function (item) {
                    //去掉两端空格
                    self.roleNameList.push(item.trim());
                });
            },
            fetchDesigner: function () {
                var self = this;
                self.$http.get('/customercontract/contract/findDesigner').then(function (res) {
                    if (res.data.code == 1) {
                        self.designers = res.data.data;
                    }
                }).catch(function () {

                }).finally(function () {
                    swal.close();
                });
            },
            fetchAuditor: function () {
                var self = this;
                self.$http.get('/customercontract/contract/findAuditor').then(function (res) {
                    if (res.data.code == 1) {
                        self.auditors = res.data.data;
                    }
                }).catch(function () {

                }).finally(function () {
                    swal.close();
                });
            },
            activeDatepiker: function () {
                var self = this;
                $(self.$els.endDate).datetimepicker('setStartDate', '');
                $(self.$els.startDate).datetimepicker('setStartDate', '');
            },
            //获取合同信息
            query: function () {
                this.$dataTable.bootstrapTable('selectPage', 1);
            },
            drawTable: function () {
                var self = this;
                self.$dataTable = $('#dataTable', self._$el).bootstrapTable({
                    url: '/contract/contractchange/findAll',
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
                            field: 'customerName',
                            title: '客户姓名',
                            orderable: false,
                            align: 'center'
                        }, {
                            field: 'customerMobile',
                            title: '联系方式',
                            orderable: false,
                            align: 'center'
                        }, {
                            field: 'layout',
                            title: '户型',
                            orderable: false,
                            align: 'center'
                        },
                        {
                            field: 'valuateArea',
                            title: '计价面积(m²)',
                            width: '10%',
                            orderable: false,
                            align: 'center'
                        },
                        {
                            field: 'mealName',
                            title: '所选套餐(￥)',
                            orderable: false,
                            align: 'center'
                        }, {
                            field: 'designer',
                            title: '设计师',
                            orderable: false,
                            align: 'center'
                        },
                        {
                            field: 'auditor',
                            title: '审计员',
                            orderable: false,
                            align: 'center'
                        }, {
                            field: 'singleOrderInfo',
                            title: '串单信息',
                            orderable: false,
                            align: 'center'
                        }, {
                            field: 'contractStatus',
                            title: '状态',
                            orderable: false,
                            align: 'center',
                            formatter: function (val) {
                                switch (val) {
                                    case 'CHANGING':
                                        return '变更中';
                                    case 'CHANGE_AUDIT':
                                        return '变更审计中';
                                    case 'AUDIT_FAIL':
                                        return '审计未通过';
                                    case 'MATERIAL_DEPARTMENT_AUDIT':
                                        return '材料部审核中';
                                    case 'WAIT_TRANSFER':
                                        return '等待转单';
                                    case 'TRANSFER_COMPLETE':
                                        return '转单完成';
                                    case 'DESIGN_DIRECTOR_AUDIT':
                                        return '设计总监审核中';
                                    case 'MATERIAL_AUDIT_FAIL':
                                        return '材料审核未通过';
                                }
                            }
                        },
                        {
                            field: 'downloadDate',
                            title: '提醒',
                            orderable: false,
                            align: 'center'
                        }, {
                            field: 'id',
                            title: '操作',
                            orderable: false,
                            align: 'center',
                            formatter: function (value, row) {
                                if (row) {
                                    var html = '';
                                    if (row.contractStatus == self.contractStatuList[0].value && self.roleNameList) {
                                        //变更中
                                        if (self.roleNameList.indexOf(self.roleNameAllList[0]) != -1 ||
                                            self.roleNameList.indexOf(self.roleNameAllList[4]) != -1) {
                                            //变更 按钮
                                            html += '<button data-handle="data-change" data-id="' + row.id + '" class="m-r-xs btn btn-xs btn-primary" type="button">变更</button>';
                                        }
                                        if (self.roleNameList.indexOf(self.roleNameAllList[0]) != -1 ||
                                            self.roleNameList.indexOf(self.roleNameAllList[1]) != -1 ||
                                            self.roleNameList.indexOf(self.roleNameAllList[3]) != -1 ||
                                            self.roleNameList.indexOf(self.roleNameAllList[4]) != -1 ||
                                            self.roleNameList.indexOf(self.roleNameAllList[5]) != -1 ||
                                            self.roleNameList.indexOf(self.roleNameAllList[6]) != -1 ) {
                                            //预览 按钮
                                            html += '<button data-handle="data-preview" data-id="' + row.id + '" class="m-r-xs btn btn-xs btn-primary" type="button">预览</button>';
                                        }
                                        if (self.roleNameList.indexOf(self.roleNameAllList[0]) != -1
                                            || self.roleNameList.indexOf(self.roleNameAllList[1]) != -1
                                            || self.roleNameList.indexOf(self.roleNameAllList[4]) != -1) {
                                            //变更详情 按钮
                                            html += '<button data-handle="data-detail" data-id="' + row.id + '" data-contractcode="' + row.contractCode + '" class="m-r-xs btn btn-xs btn-primary" type="button">变更详情</button>';
                                        }
                                        if (self.roleNameList.indexOf(self.roleNameAllList[0]) != -1 ||
                                            self.roleNameList.indexOf(self.roleNameAllList[1]) != -1 ||
                                            self.roleNameList.indexOf(self.roleNameAllList[3]) != -1 ||
                                            self.roleNameList.indexOf(self.roleNameAllList[4]) != -1 ||
                                            self.roleNameList.indexOf(self.roleNameAllList[5]) != -1 ) {
                                            //更多按钮
                                            html += '<button data-handle="data-more" data-id="' + row.id + '" data-contract-status="' + row.contractStatus + '"  class="m-r-xs btn btn-xs btn-primary" type="button">更多</button>';
                                        }
                                    } else if (row.contractStatus == self.contractStatuList[1].value && self.roleNameList) {
                                        //变更审计中
                                        //预览 按钮
                                        html += '<button data-handle="data-preview" data-id="' + row.id + '" class="m-r-xs btn btn-xs btn-primary" type="button">预览</button>';
                                        if (self.roleNameList.indexOf(self.roleNameAllList[0]) != -1
                                            || self.roleNameList.indexOf(self.roleNameAllList[1]) != -1
                                            || self.roleNameList.indexOf(self.roleNameAllList[2]) != -1
                                            || self.roleNameList.indexOf(self.roleNameAllList[4]) != -1) {
                                            //变更详情 按钮
                                            html += '<button data-handle="data-detail" data-id="' + row.id + '" data-contractcode="' + row.contractCode + '" class="m-r-xs btn btn-xs btn-primary" type="button">变更详情</button>';
                                        }
                                        if (self.roleNameList.indexOf(self.roleNameAllList[0]) != -1 ||
                                            self.roleNameList.indexOf(self.roleNameAllList[1]) != -1 ||
                                            self.roleNameList.indexOf(self.roleNameAllList[2]) != -1 ||
                                            self.roleNameList.indexOf(self.roleNameAllList[3]) != -1 ||
                                            self.roleNameList.indexOf(self.roleNameAllList[4]) != -1 ||
                                            self.roleNameList.indexOf(self.roleNameAllList[5]) != -1 ) {
                                            //更多按钮
                                            html += '<button data-handle="data-more" data-id="' + row.id + '" data-contract-status="' + row.contractStatus + '"  class="m-r-xs btn btn-xs btn-primary" type="button">更多</button>';
                                        }
                                    } else if (row.contractStatus == self.contractStatuList[2].value && self.roleNameList) {
                                        //转单完成
                                        //预览 按钮
                                        html += '<button data-handle="data-preview" data-id="' + row.id + '" class="m-r-xs btn btn-xs btn-primary" type="button">预览</button>';
                                        if (self.roleNameList.indexOf(self.roleNameAllList[0]) != -1
                                            || self.roleNameList.indexOf(self.roleNameAllList[1]) != -1
                                            || self.roleNameList.indexOf(self.roleNameAllList[2]) != -1
                                            || self.roleNameList.indexOf(self.roleNameAllList[4]) != -1) {
                                            //变更详情 按钮
                                            html += '<button data-handle="data-detail" data-id="' + row.id + '" data-contractcode="' + row.contractCode + '" class="m-r-xs btn btn-xs btn-primary" type="button">变更详情</button>';
                                        }
                                        if (self.roleNameList.indexOf(self.roleNameAllList[0]) != -1 ||
                                            self.roleNameList.indexOf(self.roleNameAllList[1]) != -1 ||
                                            self.roleNameList.indexOf(self.roleNameAllList[2]) != -1 ||
                                            self.roleNameList.indexOf(self.roleNameAllList[3]) != -1 ||
                                            self.roleNameList.indexOf(self.roleNameAllList[4]) != -1 ||
                                            self.roleNameList.indexOf(self.roleNameAllList[5]) != -1 ) {
                                            //更多按钮
                                            html += '<button data-handle="data-more" data-id="' + row.id + '" data-contract-status="' + row.contractStatus + '"  class="m-r-xs btn btn-xs btn-primary" type="button">更多</button>';
                                        }
                                    }
                                    return html;
                                }
                            }
                        }]
                });
                // 变更
                self.$dataTable.on('click', '[data-handle="data-change"]',
                    function (e) {
                        var id = $(this).data('id');
                        self.$http.get('/customercontract/contract/getById?id=' + id).then(function (res) {
                            if (res.data.code == 1) {
                                var model = res.data.data;
                                //window.location.href = '/material/materialIndex?contractCode=' + model.contractCode + '&pageType=change';
                                var params = {
                                    contractCode: model.contractCode,
                                    pageType: 'change'
                                }
                                DameiUtils.locationHrefToClient('/material/materialIndex', params);
                            }
                        }).catch(function () {
                        }).finally(function () {
                        });
                        e.stopPropagation();
                    });
                //详情
                self.$dataTable.on('click', '[data-handle="data-detail"]',
                    function (e) {
                        var id = $(this).data('id');
                        var contractCode = $(this).data('contractcode');
                        contrCode = contractCode;
                        detailModal(contractCode);
                        e.stopPropagation();
                    });
                // 更多
                //注意: 原始选材单预览 0 选材单下载 1 老房拆改项下载 2 提交变更审核 3
                self.$dataTable.on('click', '[data-handle="data-more"]',
                    function (e) {
                        var id = $(this).data('id');
                        var contractStatus = $(this).data('contractStatus');
                        //判断当前角色所对应的 能查看的数据
                        if (!self.roleNameList) {
                            self.$toastr.error("您没有该权限!");
                            return;
                        }
                        var stindex = "";

                        if (contractStatus == self.contractStatuList[0].value) {
                            //变更中 236 没有
                            if (self.roleNameList.indexOf(self.roleNameAllList[0]) != -1) {
                                //设计师
                                stindex += "1,2,3,";
                            }
                            if (self.roleNameList.indexOf(self.roleNameAllList[1]) != -1) {
                                //审计经理
                                stindex += "0,1,2,";
                            }
                            if (self.roleNameList.indexOf(self.roleNameAllList[4]) != -1) {
                                //设计总监
                                stindex += "1,2,3,";
                            }
                            if (self.roleNameList.indexOf(self.roleNameAllList[5]) != -1 ||
                                self.roleNameList.indexOf(self.roleNameAllList[3]) != -1) {
                                //材料部经理  材料部审核员
                                stindex += "1,";
                            }
                        } else if (contractStatus == self.contractStatuList[1].value) {
                            //变更审计中 36 没有
                            if (self.roleNameList.indexOf(self.roleNameAllList[0]) != -1) {
                                //设计师
                                stindex += "1,2,";
                            }
                            if (self.roleNameList.indexOf(self.roleNameAllList[1]) != -1) {
                                //审计经理
                                stindex += "0,1,2,";
                            }
                            if (self.roleNameList.indexOf(self.roleNameAllList[2]) != -1) {
                                //审计员
                                stindex += "0,1,";
                            }
                            if (self.roleNameList.indexOf(self.roleNameAllList[4]) != -1) {
                                //设计总监
                                stindex += "1,2,";
                            }
                            if (self.roleNameList.indexOf(self.roleNameAllList[5]) != -1 ||
                                self.roleNameList.indexOf(self.roleNameAllList[3]) != -1) {
                                //材料部经理  材料部审核员
                                stindex += "1,";
                            }
                        } else if (contractStatus == self.contractStatuList[2].value) {
                            //转单完成 36 没有
                            if (self.roleNameList.indexOf(self.roleNameAllList[0]) != -1) {
                                //设计师
                                stindex += "1,2,";
                            }
                            if (self.roleNameList.indexOf(self.roleNameAllList[1]) != -1) {
                                //审计经理
                                stindex += "0,1,2,";
                            }
                            if (self.roleNameList.indexOf(self.roleNameAllList[2]) != -1) {
                                //审计员
                                stindex += "0,1,";
                            }
                            if (self.roleNameList.indexOf(self.roleNameAllList[4]) != -1) {
                                //设计总监
                                stindex += "1,2,";
                            }
                            if (self.roleNameList.indexOf(self.roleNameAllList[5]) != -1 ||
                                self.roleNameList.indexOf(self.roleNameAllList[3]) != -1) {
                                //材料部经理  材料部审核员
                                stindex += "1,";
                            }
                        }
                        var displayObj = [];
                        for (var i = 0; i < 4; i++) {
                            if (stindex.indexOf(i) != -1) {
                                displayObj.push(true);
                            } else {
                                displayObj.push(false);
                            }
                        }
                        self.$http.get('/customercontract/contract/getById?id=' + id).then(function (res) {
                            if (res.data.code == 1) {
                                var model = res.data.data;
                                var installInfo = model.otherInstallInfo;
                                var hangCeiling = '';
                                var plasterLine = '';
                                var telWall = '';
                                var info = [];
                                if (installInfo != null) {
                                    info = installInfo.split(",");
                                }
                                if (info != null && info.length > 0) {
                                    if(info.indexOf('吊顶') != -1){
                                        hangCeiling = '吊顶';
                                    }
                                    if(info.indexOf('石膏线') != -1){
                                        plasterLine = '石膏线';
                                    }
                                    if(info.indexOf('电视背景墙') != -1){
                                        telWall = '电视背景墙';
                                    }
                                }
                                moreModal(res.data.data, displayObj, hangCeiling, plasterLine, telWall);
                            }
                        }).catch(function () {
                        }).finally(function () {
                        });
                        e.stopPropagation();
                    });
                // 审计
                self.$dataTable.on('click', '[data-handle="data-audit"]',
                    function (e) {
                        var id = $(this).data('id');
                        self.$http.get('/customercontract/contract/getById?id=' + id).then(function (res) {
                            if (res.data.code == 1) {
                                var model = res.data.data;

                                window.location.href = '/material/materialIndex?contractCode=' + model.contractCode
                                    + '&pageType=change';
                            }
                        }).catch(function () {
                        }).finally(function () {
                        });
                        e.stopPropagation();
                    });
                // 审计员
                self.$dataTable.on('click', '[data-handle="data-auditor"]',
                    function (e) {
                        var id = $(this).data('id');
                        self.$http.get('/customercontract/contract/getById?id=' + id).then(function (res) {
                            if (res.data.code == 1) {
                                var model = res.data.data;
                                auditorModal(model);
                            }
                        }).catch(function () {
                        }).finally(function () {
                        });
                        e.stopPropagation();
                    });
                // 预览
                self.$dataTable.on('click', '[data-handle="data-preview"]',
                    function (e) {
                        var id = $(this).data('id');
                        self.$http.get('/customercontract/contract/getById?id=' + id).then(function (res) {
                            if (res.data.code == 1) {
                                var model = res.data.data;
                                var installInfo = model.otherInstallInfo;
                                var hangCeiling = '';
                                var plasterLine = '';
                                var telWall = '';
                                var info = [];
                                if (installInfo != null) {
                                    info = installInfo.split(",");
                                }
                                if (info != null && info.length > 0) {
                                    if(info.indexOf('吊顶') != -1){
                                        hangCeiling = '吊顶';
                                    }
                                    if(info.indexOf('石膏线') != -1){
                                        plasterLine = '石膏线';
                                    }
                                    if(info.indexOf('电视背景墙') != -1){
                                        telWall = '电视背景墙';
                                    }
                                }
                                materialPreviewModal(model, hangCeiling, plasterLine, telWall);
                            }
                        }).catch(function () {
                        }).finally(function () {
                        });
                        e.stopPropagation();
                    });
            }
        }
    });

    function detailModal(contractCode) {
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
                var vueModal = new Vue({
                    el: el,
                    http: {
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded'
                        }
                    },
                    // 模式窗体必须引用 ModalMixin
                    mixins: [DameiVueMixins.ModalMixin],
                    components: {
                        'web-uploader': DameiVueComponents.WebUploaderComponent
                    },
                    $modal: $el,
                    //模式窗体 jQuery 对象
                    created: function () {
                    },
                    ready: function () {
                        this.drawTable();
                        this.getChangeVersionNo(contractCode);
                    },
                    data: {
                        form: {
                            changeVersionNo: '',
                            contractCode: contractCode
                        },
                        changeContract: {},
                        changeVersionNoList: '',

                    },

                    methods: {
                        //查询变更版本号
                        getChangeVersionNo: function (contractCode) {
                            var self = this;
                            self.$http.get('/material/changelog/findchangversionno?contractCode='+contractCode).then(function (res) {
                                if (res.data.code == 1) {
                                    self.changeVersionNoList = res.data.data;
                                }
                            })
                        },
                        query: function () {
                            this.$dataTable.bootstrapTable('selectPage', 1);
                            this.$dataTable.bootstrapTable('refresh');
                        },
                        drawTable: function () {
                            var self = this;
                            self.$dataTable = $('#dataTable2', self._$el).bootstrapTable({
                                url: '/material/changedetail/findchangedetail',
                                method: 'get',
                                dataType: 'json',
                                cache: false,
                                pagination: true,
                                sidePagination: 'client',
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
                                    }, {
                                        field: 'changeNo',
                                        title: '变更单号',
                                        width: '10%',
                                        align: 'center'
                                    }, {
                                        field: 'changeCategoryName',
                                        title: '变更类目',
                                        width: '10%',
                                        align: 'center',
                                        formatter: function (value) {
                                            if(value){
                                            }else{
                                                value='其他金额增减'
                                            }
                                            return value;
                                        }
                                    },{
                                        field: 'createTime',
                                        title: '提交时间',
                                        width: '10%',
                                        align: 'center',
                                        formatter: function (value) {
                                            return moment(value).format('YYYY-MM-DD HH:mm:ss')
                                        }
                                    }, {
                                        field: 'createUser',
                                        title: '操作人',
                                        width: '10%',
                                        align: 'center'
                                    }, {
                                        field: 'changeCategoryUrl',
                                        title: '类别',
                                        width: '10%',
                                        align: 'center',
                                        visible: false
                                    }, {
                                        field: 'currentStatus',
                                        title: '审计状态',
                                        width: '10%',
                                        align: 'center',
                                        formatter: function (value) {
                                            switch (value) {
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
                                                case 'CHANGEORDERRECALL':
                                                    return '变更单撤回';
                                            }
                                        }
                                    }, {
                                        field: 'id',
                                        title: '操作',
                                        width: '10%',
                                        align: 'center',
                                        formatter: function (value, row) {
                                            if (row.currentStatus != 'CHANGEORDERRECALL') {
                                                var html = '';
                                                // html += '<button data-handle="data-detail" data-id="' + row.id + '" data-status="' + row.currentStatus + '"    data-url="' + row.changeCategoryUrl + '" data-changeno="' + row.changeNo + '" class="m-r-xs btn btn-xs btn-primary" type="button">查看</button>';
                                                html += '<button data-handle="data-export" data-id="' + row.id + '" data-currentstatus="' + row.currentStatus + '" data-changeno="' + row.changeNo + '"  data-url="' + row.changeCategoryUrl + '" class="m-r-xs btn btn-xs btn-primary" type="button">主材下载</button>';
                                                return html;
                                            }
                                        }
                                    }]
                            });
                            //查看
                            self.$dataTable.on('click', '[data-handle="data-detail"]',
                                function (e) {
                                    var model = $(this).data();
                                    showDetailModal(model);
                                    e.stopPropagation();
                            });

                            //下载
                            self.$dataTable.on('click', '[data-handle="data-export"]',
                                function (e) {
                                    var changeNo = $(this).data('changeno');
                                    var currentStatus = $(this).data('currentstatus');
                                    var changeCategoryUrl = $(this).data('url');
                                    if (changeCategoryUrl == null || changeCategoryUrl == undefined || changeCategoryUrl == 'null') {
                                        changeCategoryUrl = 'other';
                                    }
                                    window.location.href = '/material/exportexcel/changeorderexport?contractCode=' + contractCode + '&changeNo=' + changeNo+
                                        '&currentStatus=' + currentStatus+ '&changeCategoryUrl=' + changeCategoryUrl + '&downLoadFlag=' + false;
                                }
                            );
                        }
                    }
                });

                // 创建的Vue对象应该被返回
                return vueModal;
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
                    },
                    // 模式窗体必须引用 ModalMixin
                    mixins: [DameiVueMixins.ModalMixin],
                    $modal: $el,
                    //模式窗体 jQuery 对象
                    created: function () {
                    },
                    ready: function () {
                        this.findCustomerContract();
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
                            if(value){
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
                            self.$http.get("/customercontract/contract/get/" + contrCode).then(function (res) {
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
                                            self.findOtherAddReduceAmount(0);
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
                                            } else {
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
                                            self.findOtherAddReduceAmount(1);
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
                                            } else {
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
                        findOtherAddReduceAmount: function (flg) {
                            var self = this;
                            //芒果
                            if (flg == 0) {
                                self.$http.get("/material/otheraddreduceamount/findotheraddreduceamount?contractCode=" + self.customerContract.contractCode + '&changeNo=' + model.changeno).then(function (res) {
                                    if (res.data.code == 1) {
                                        self.otherList = res.data.data;
                                        var list = self.otherList;
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
                                }).catch(function () {
                                }).finally(function () {
                                });
                            } else {
                                self.$http.get('/material/otheraddreduceamount/getChangeByContractCode/' + self.customerContract.contractCode).then(function (res) {
                                    if (res.data.code == 1) {
                                        self.otherList = res.data.data;
                                        var list = self.otherList;
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
                                }).catch(function () {
                                }).finally(function () {
                                });
                            }
                        },
                        demolitionHouses: function () {

                        },
                        totalProfitMargin: function () {

                        },
                        schedule: function () {

                        }
                    }
                });

                // 创建的Vue对象应该被返回
                return vueModal;
            });
    }

    function moreModal(model, displayObj, hangCeiling, plasterLine, telWall) {
        var _modal = $('#moreModal').clone();
        var $el = _modal.modal({
            height: 100,
            maxHeight: 500
        });
        $el.on('shown.bs.modal',
            function () {
                // 获取 node
                var el = $el.get(0);
                // 创建 Vue 对象编译节点
                var vueModal = new Vue({
                    el: el,
                    http: {
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded'
                        }
                    },
                    // 模式窗体必须引用 ModalMixin
                    mixins: [DameiVueMixins.ModalMixin],
                    components: {
                        'web-uploader': DameiVueComponents.WebUploaderComponent
                    },
                    $modal: $el,
                    //模式窗体 jQuery 对象
                    data: {
                        displayObj: displayObj
                    },
                    created: function () {
                    },
                    ready: function () {
                    },

                    methods: {
                        materialDownload: function () {
                            window.location.href = '/material/exportexcel/metarialexport?contractCode=' + model.contractCode + '&type=' + '1'
                        },
                        demolitionHouses: function () {
                            window.location.href = '/material/exportexcel/oldhouseexport?contractCode=' + model.contractCode + '&contractStatus='+model.contractStatus
                        },
                        totalProfitMargin: function () {

                        },
                        schedule: function () {

                        },
                        submitAudit: function () {
                            var self = this;
                            var status = 'CHANGE_AUDIT';
                            swal({
                                title: '确认提交审核？',
                                text: '将修改状态',
                                type: 'info',
                                confirmButtonText: '确定',
                                cancelButtonText: '取消',
                                showCancelButton: true,
                                showConfirmButton: true,
                                showLoaderOnConfirm: true,
                                confirmButtonColor: '#ed5565',
                                closeOnConfirm: false
                            }, function () {
                                self.$http.get('/contract/contractchange/' + model.id + '/updateStatus?status=' + status).then(function (res) {
                                    if (res.data.code == 1) {
                                        Vue.toastr.success(res.data.message);
                                        $el.modal('hide');
                                        vueIndex.$dataTable.bootstrapTable('refresh');
                                    }
                                }).catch(function () {

                                }).finally(function () {
                                    swal.close();
                                });
                            })
                        },
                        originalMaterialPreview: function () {
                            var params = {
                                contractCode: model.contractCode,
                                hangCeiling:hangCeiling,
                                plasterLine:plasterLine,
                                telWall: telWall
                            };
                            DameiUtils.locationHrefToClient('/business/changemange/originalmaterialpreview', params, true);
                        }
                    }
                });

                // 创建的Vue对象应该被返回
                return vueModal;
            });
    }

    function auditorModal(model) {
        var _modal = $('#auditorModal').clone();
        var $el = _modal.modal({
            height: 300,
            maxHeight: 500
        });
        $el.on('shown.bs.modal',
            function () {
                // 获取 node
                var el = $el.get(0);
                // 创建 Vue 对象编译节点
                var vueModal = new Vue({
                    el: el,
                    http: {
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded'
                        }
                    },
                    // 模式窗体必须引用 ModalMixin
                    mixins: [DameiVueMixins.ModalMixin],
                    components: {
                        'web-uploader': DameiVueComponents.WebUploaderComponent
                    },
                    $modal: $el,
                    data: {
                        auditorName: model.auditor,
                        auditors: {}
                    },
                    //模式窗体 jQuery 对象
                    created: function () {
                    },
                    ready: function () {
                        this.fetchAuditor();
                    },

                    methods: {
                        fetchAuditor: function () {
                            var self = this;
                            self.$http.get('/customercontract/contract/findAuditor').then(function (res) {
                                if (res.data.code == 1) {
                                    self.auditors = res.data.data;
                                }
                            }).catch(function () {

                            }).finally(function () {
                                swal.close();
                            });
                        },
                        saveAuditor: function () {
                            var self = this;
                            var auditor = self.auditorName;
                            self.$http.get('/customercontract/contract/' + model.id + '/updateAuditor?auditor=' + auditor).then(function (res) {
                                if (res.data.code == 1) {
                                    Vue.toastr.success(res.data.message);
                                    $el.modal('hide');
                                    vueIndex.$dataTable.bootstrapTable('refresh');
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

    //预览
    function materialPreviewModal(model, hangCeiling, plasterLine, telWall) {
        var _modal = $('#materialPreviewModal').clone();
        var $el = _modal.modal({
            height: 1000,
            maxHeight: 500
        });
        $el.on('shown.bs.modal',
            function () {
                // 获取 node
                var el = $el.get(0);
                // 创建 Vue 对象编译节点
                var vueModalh = new Vue({
                    el: el,
                    data: {
                        catalogUrl: '',
                        customerContract: model,
                        prodCatalogList: [],
                        topCatalogList: [],
                        //套餐标配
                        lowerCatalogList: [],
                        //升级项
                        upgradeCatalogList: [],
                        upgradeTopCatalogList: [],
                        upgradeLowerCatalogList: [],
                        //增项
                        addCatalogList: [],
                        addTopCatalogList: [],
                        addLowerCatalogList: [],
                        //减项
                        reduceCatalogList: [],
                        reduceTopCatalogList: [],
                        reduceLowerCatalogList: [],
                        otherSum: 0,
                        addProjectMaterialList: [],
                        //增项定额
                        addBaseInstallQuotaList: [],
                        //基装增项综合费 或者 拆除基装增项综合服务
                        addBaseInstallComfeeList: [],
                        //其他综合费 或者 拆除其它综合服务
                        addOtherComprehensiveFeeList: [],
                        //减项主材
                        reduceProjectMaterialList: [],
                        //基装定额 或者 拆除基装定额
                        reduceBaseInstallQuotaList: [],
                        //活动、优惠及其它金额增减项
                        otherMoneyList: [],
                        //老房拆改
                        oldHouseProjectMaterialList: [],
                        //老房拆改 基装定额 或者 拆除基装定额
                        dismantleBaseinstallquotaList: [],
                        //老房拆改 基装增项综合费 或者 拆除基装增项综合服务
                        dismantleBaseinstallCompFeeList: [],
                        //老房拆改 其他综合费 或者 拆除其它综合服务
                        dismantleOtherCompFeeList: [],
                        //其他综合 基装定额 或者 拆除基装定额
                        otherComprehensiveFeeList: [],
                        //其他综合
                        otherComprehensiveFeeProjectList: [],
                        amount: '',
                        totalAmount: {
                            totalBudget: 0,
                            totalRenovationWorks: 0,
                            oldhousedemolition: 0,
                            packagestandardprice: 0,
                            upgradeitemprice: 0,
                            increment: 0,
                            mainmaterial1: 0,
                            comprehensivefee1: 0,
                            subtraction: 0,
                            mainmaterial2: 0,
                            baseloadrating2: 0,
                            otheramountsincreaseordecrease: 0,
                            otherincrease: 0,
                            otherminus: 0,
                            othercomprehensivefee: 0,
                            comprehensivefee3: 0,
                            othercomprehensivefee3: 0,
                            //工程总价
                            renovationAmount: 0,
                            //基装增项总价
                            baseloadrating1: 0,
                            //拆除基装定额总价
                            baseloadrating3: 0,
                            //拆除工程总价
                            comprehensivefee4: 0,
                            //面积
                            area: 0,
                            //价格
                            price: 0,
                        },
                        hangCeiling: hangCeiling,
                        plasterLine: plasterLine,
                        telWall: telWall
                    },
                    // 模式窗体必须引用 ModalMixin
                    mixins: [DameiVueMixins.ModalMixin],
                    $modal: $el,
                    //模式窗体 jQuery 对象
                    created: function () {
                    },
                    ready: function () {
                        this.fetchMealStandard();
                    },
                    filters: {
                        goDate: function (el) {
                            if (el) {
                                return moment(el).format('YYYY-MM-DD');
                            }
                            else {
                                return '-';
                            }
                        },
                        goType: function (val) {
                            if (val == '1') {
                                return '有';
                            } else {
                                return '无';
                            }
                        },
                        houseStatus: function (val) {
                            if (val == '1') {
                                return '新房';
                            } else {
                                return '旧房';
                            }
                        },
                        houseType: function (val) {
                            if (val == '1') {
                                return '复式';
                            } else if (val == '2') {
                                return '别墅';
                            } else {
                                return '楼房平层';
                            }
                        }
                    },
                    methods: {
                        //##############价格操作开始##############
                        //获取价格
                        findAmount: function () {
                            var self = this;
                            self.$http.get('/material/smskudosage/changepreview?contractCode=' + model.contractCode + "&contractStatus=" + model.contractStatus).then(function (res) {
                                if (res.data.code == '1') {
                                    self.amount = res.data.data;
                                }
                            }).catch(function (e) {
                            });
                        },
                        //当前总价格
                        getMealPrice: function () {
                            var self = this;
                            var mealPrice = self.amount.othercomprehensivefee + self.amount.otheramountsincreaseordecrease - self.amount.subtraction + self.amount.increment + self.amount.upgradeitemprice + self.amount.packagestandardprice;
                            return mealPrice;
                        },
                        //装修工程合计
                        getProjectAmount: function () {
                            var self = this;
                            return self.getMealPrice() + self.amount.oldhousedemolition;
                        },
                        //计算总金额
                        getTotalAmount: function () {
                            var self = this;
                            //查询金额
                            var url = '/material/proportionmoney/getbycontractcode/' + model.contractCode;
                            self.$http.get(url).then(function (res) {
                                if (res.data.code == '1') {
                                    self.totalAmount = res.data.data;
                                } else {
                                    self.$toastr.error("计算总金额失败!");
                                }
                            })
                        },
                        getOtherFee:function () {
                            var self = this;
                            var otherFee = self.otherComprehensiveFeeList;
                            var sum = 0;
                            otherFee.forEach(function (fee) {
                                if(fee.skuDosageList[0].dosagePricingMode == 'fixedUnitPrice'){
                                    sum += fee.storeSalePrice * fee.skuDosageList[0].lossDosage;
                                }else if(fee.skuDosageList[0].dosagePricingMode == 'foundationPileTotal'){
                                    sum += ((fee.skuDosageList[0].projectProportion || 0)/100 * (self.totalAmount.baseloadrating1 || 0));
                                }else {
                                    sum += ((fee.skuDosageList[0].projectProportion || 0)/100 * (self.totalAmount.renovationAmount || 0));
                                }
                            });
                            return sum;
                        },
                        //##############价格操作结束##############

                        //##############数量操作开始##############
                        //套餐标配数量
                        getProjectMaterialAmount: function () {
                            var self = this;
                            var sum = 0;
                            self.lowerCatalogList.forEach(function (cat) {
                                sum += cat.projectMaterialList.length;
                            });
                            return sum;
                        },
                        //升级数量
                        getUpgradeAmount: function () {
                            var self = this;
                            var sum = 0;
                            self.upgradeLowerCatalogList.forEach(function (cat) {
                                sum += cat.projectMaterialList.length;
                            });
                            return sum;
                        },
                        //减项数量
                        getReduceAmount: function () {
                            var self = this;
                            var sum = 0;
                            self.reduceLowerCatalogList.forEach(function (cat) {
                                sum += cat.projectMaterialList.length;
                            });
                            return sum + self.reduceBaseInstallQuotaList.length;
                        },
                        //减项主材数量
                        getReduceMaterialAmount: function () {
                            var self = this;
                            var sum = 0;
                            self.reduceLowerCatalogList.forEach(function (cat) {
                                sum += cat.projectMaterialList.length;
                            });
                            return sum;
                        },
                        //增项数量
                        getAddAmount: function () {
                            var self = this;
                            var sum = 0;
                            self.addLowerCatalogList.forEach(function (cat) {
                                sum += cat.projectMaterialList.length;
                            });
                            return sum + self.addBaseInstallQuotaList.length +
                                self.addBaseInstallComfeeList.length;
                        },
                        //增项主材
                        getAddMaterialAmount: function () {
                            var self = this;
                            var sum = 0;
                            self.addLowerCatalogList.forEach(function (cat) {
                                sum += cat.projectMaterialList.length;
                            });
                            return sum;
                        },
                        //##############数量操作结束##############

                        //###################查询操作开始#################
                        //通过 contractCode 查询所有商品分类及对应主材和用量
                        findCatalogWithMaterialByContractCode: function () {
                            var self = this;
                            var url = '/material/prodcatalog/findtwostagewithpromaterial?contractCode='
                                + model.contractCode +'&pageType=';
                            if(model.contractStatus == 'TRANSFER_COMPLETE'){
                                url += 'select';
                            }else{
                                url += 'change';
                            }
                            self.$http.get(url).then(function (response) {
                                var res = response.data;
                                if (res.code == '1') {
                                    //所有数据
                                    var allCatalogList = res.data;
                                    if(allCatalogList && allCatalogList.length > 0){
                                        //遍历,存放到 套餐标配,升级项,增项,减项中的主材中
                                        allCatalogList.forEach(function (catalog) {
                                            var projectMaterialList = catalog.projectMaterialList;

                                            if(projectMaterialList && projectMaterialList.length > 0){
                                                var packCatalogTemp = $.extend(true,{}, catalog);
                                                var upgradeCatalogTemp = $.extend(true,{}, catalog);
                                                var addCatalogTemp = $.extend(true,{}, catalog);
                                                var reduceCatalogTemp = $.extend(true,{}, catalog);

                                                var packageMaterialTempList = [];
                                                var upgradeMaterialTempList = [];
                                                var addMaterialTempList = [];
                                                var reduceMaterialTempList = [];

                                                projectMaterialList.forEach(function (material) {
                                                    if(material.categoryCode == "PACKAGESTANDARD"){
                                                        packageMaterialTempList.push(material);
                                                    }else if(material.categoryCode == "UPGRADEITEM"){
                                                        upgradeMaterialTempList.push(material);
                                                    }else if(material.categoryCode == "ADDITEM" && material.categoryDetailCode == "MAINMATERIAL"){
                                                        //增项 主材
                                                        addMaterialTempList.push(material);
                                                    }else if(material.categoryCode == "REDUCEITEM" && material.categoryDetailCode == "MAINMATERIAL"){
                                                        //减项 主材
                                                        reduceMaterialTempList.push(material);
                                                    }
                                                });

                                                packCatalogTemp.projectMaterialList = packageMaterialTempList;
                                                self.prodCatalogList.push(packCatalogTemp);

                                                upgradeCatalogTemp.projectMaterialList = upgradeMaterialTempList;
                                                self.upgradeCatalogList.push(upgradeCatalogTemp);

                                                addCatalogTemp.projectMaterialList = addMaterialTempList;
                                                self.addCatalogList.push(addCatalogTemp);

                                                reduceCatalogTemp.projectMaterialList = reduceMaterialTempList;
                                                self.reduceCatalogList.push(reduceCatalogTemp);

                                            }

                                        });
                                        //往二级分类中填充主材
                                        self.dealCatalogAndMaterial(self.prodCatalogList, self.topCatalogList, self.lowerCatalogList);
                                        //往二级分类中填充主材
                                        self.dealCatalogAndMaterial(self.upgradeCatalogList, self.upgradeTopCatalogList, self.upgradeLowerCatalogList);
                                        //往二级分类中填充主材
                                        self.dealCatalogAndMaterial(self.addCatalogList, self.addTopCatalogList, self.addLowerCatalogList);
                                        //往二级分类中填充主材
                                        self.dealCatalogAndMaterial(self.reduceCatalogList, self.reduceTopCatalogList, self.reduceLowerCatalogList);
                                    }
                                }else{
                                    self.$toastr.error("获取主材数据失败!")
                                }
                            });
                        },
                        //通过 contractCode 查询所有 定额 主材和用量!
                        findMaterialWithSkuByContractCode: function () {
                            var self = this;
                            var url = '/material/projectmaterial/findmateriallist?contractCode='
                                + model.contractCode +'&pageType=';
                            if(model.contractStatus == 'TRANSFER_COMPLETE'){
                                url += 'select';
                            }else{
                                url += 'change';
                            }
                            self.$http.get(url).then(function(response) {
                                var res = response.data;
                                var materialQuataList = res.data;
                                if (res.code == '1') {
                                    if(materialQuataList && materialQuataList.length > 0){
                                        materialQuataList.forEach(function (material) {
                                            if(material.categoryCode == "ADDITEM"){
                                                if(material.categoryDetailCode == "BASEINSTALLQUOTA"){
                                                    //增项 基装定额
                                                    self.addBaseInstallQuotaList.push(material);
                                                }else if(material.categoryDetailCode == "BASEINSTALLCOMPREHENSIVEFEE"){
                                                    //增项 基装增项综合费
                                                    self.addBaseInstallComfeeList.push(material);
                                                }
                                            }else if(material.categoryCode == "REDUCEITEM" && material.categoryDetailCode == "BASEINSTALLQUOTA"){
                                                //减项 基装定额
                                                self.reduceBaseInstallQuotaList.push(material);
                                            }else if(material.categoryCode == "OTHERCOMPREHENSIVEFEE" && material.categoryDetailCode == "OTHERCATEGORIESOFSMALLFEES"){
                                                //其他综合费 其他综合费
                                                self.otherComprehensiveFeeList.push(material);
                                            }else if(material.categoryCode == "OLDHOUSEDEMOLITION"){
                                                if(material.categoryDetailCode == "DISMANTLEBASEINSTALLQUOTA"){
                                                    //旧房拆改 拆除基装定额
                                                    self.dismantleBaseinstallquotaList.push(material);
                                                }else if(material.categoryDetailCode == "DISMANTLEBASEINSTALLCOMPFEE"){
                                                    //旧房拆改 拆除基装增项综合服务
                                                    self.dismantleBaseinstallCompFeeList.push(material);
                                                }else if(material.categoryDetailCode == "DISMANTLEOTHERCOMPFEE"){
                                                    //旧房拆改 拆除其它综合服务
                                                    self.dismantleOtherCompFeeList.push(material);
                                                }
                                            }
                                        });
                                    }
                                }
                            });

                        },
                        //处理分类和主材数据关系
                        dealCatalogAndMaterial: function (catalogList, topCatalogList, lowerCatalogList) {
                            catalogList.forEach(function (cat) {
                                if (cat && cat.subCatalogList && cat.subCatalogList.length > 0) {
                                    topCatalogList.push(cat);
                                } else {
                                    if (cat.projectMaterialList != null && cat.projectMaterialList.length != 0) {
                                        lowerCatalogList.push(cat);
                                    }
                                }
                            });
                            this.dealSkuAmount(lowerCatalogList);
                        },
                        //活动、优惠及其它金额增减项
                        findOtherMoney: function () {
                            var self = this;
                            var pageType = 'change';
                            var url = "/material/otheraddreduceamount/findlistbycontractcode?contractCode=" + model.contractCode;
                            //拼接页面类型
                            if (model.currentChangeVersion != null && model.contractStatus == 'TRANSFER_COMPLETE') {
                                url += "&pageType=" + 'select';
                            }else {
                                url += "&pageType=" + 'change';
                            }
                            self.$http.get(url).then(function (response) {
                                var res = response.data;
                                if (res.code == '1') {
                                    self.otherMoneyList = res.data;
                                    //处理加减号
                                    self.otherMoneyList.forEach(function (otherMoney) {
                                        if (otherMoney.addReduceType == '1') {
                                            otherMoney.addReduceType = "+ ";
                                        } else if (otherMoney.addReduceType == '0') {
                                            otherMoney.addReduceType = "- ";
                                        }
                                    });
                                    //计算数量
                                    self.otherSum = self.otherMoneyList.length;
                                } else {
                                    self.$toastr.error("查询其他金额增减失败!")
                                }
                            });
                        },

                        //计算sku预算用量合计/含损耗用量合计/总价--页面刷新时加载一次 参数3:定额类型,如果有,就不再直接计算总数
                        dealSkuAmount: function (lowerCatalogList, activeIndex, quotaType) {
                            var self = this;
                            //定义变量
                            var budgetDosageAmount = 0;
                            var lossDosageAmount = 0;
                            var skuSumMoney = 0;
                            var skuSum = 0;
                            lowerCatalogList.forEach(function (catalog) {
                                if (catalog && catalog.projectMaterialList && catalog.projectMaterialList.length > 0) {
                                    catalog.projectMaterialList.forEach(function (projectMaterial) {
                                        //计算sku总数
                                        skuSum++;
                                        //清空
                                        budgetDosageAmount = 0;
                                        lossDosageAmount = 0;
                                        skuSumMoney = 0;
                                        if (projectMaterial && projectMaterial.skuDosageList && projectMaterial.skuDosageList.length > 0) {
                                            projectMaterial.skuDosageList.forEach(function (skuDosage) {
                                                if (skuDosage) {
                                                    //计算
                                                    budgetDosageAmount += skuDosage.budgetDosage || 0;
                                                    lossDosageAmount += skuDosage.lossDosage || 0;
                                                }
                                            });
                                            //计算合计
                                            skuSumMoney = (projectMaterial.storeSalePrice || 0) * lossDosageAmount;
                                        }
                                        //添加到属性中
                                        Vue.set(projectMaterial, "budgetDosageAmount", budgetDosageAmount);
                                        Vue.set(projectMaterial, "lossDosageAmount", lossDosageAmount);
                                        Vue.set(projectMaterial, "skuSumMoney", skuSumMoney);

                                    });
                                }
                            });
                        },
                        //##############查询操作结束#########################
                        fetchMealStandard: function () {
                            this.findAmount();
                            this.findCatalogWithMaterialByContractCode();
                            this.findMaterialWithSkuByContractCode();
                            this.findOtherMoney();
                            this.getTotalAmount();
                        }
                    }
                });

                // 创建的Vue对象应该被返回
                return vueModalh;
            });
    }
})();