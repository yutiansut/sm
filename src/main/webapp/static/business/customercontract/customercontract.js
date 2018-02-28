var vueIndex;
+(function () {
    $('#selectMaterialManage').addClass('active');
    Vue.validator('telphone', function (tel) {
        return /^1[3|4|5|7|8]\d{9}$/.test(tel);
    });
    vueIndex = new Vue({
        el: '#container',
        data: {
            breadcrumbs: [{
                path: '/',
                name: '主页'
            },
                {
                    path: '/',
                    name: '选材管理',
                    active: true
                }],
            form: {
                keyword: '',
                contractStatus: '',
                operateType: '',
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
                "材料部经理", "工程结算员", "供应链经理"
            ],
            //个人角色集合
            roleNameList: [],
            //选材单状态集合 9个
            contractStatuList: [
                {name: "未选材", value: "NOT_SELECT_MATERIAL"},
                {name: "选材中", value: "MATERIAL_SELECTION"},
                {name: "指派审计", value: "ASSIGN_AUDIT"},
                {name: "审核中", value: "UNDER_AUDIT"},
                {name: "等待转单", value: "WAIT_TRANSFER"},
                {name: "转单完成", value: "TRANSFER_COMPLETE"},
                {name: "审计撤回", value: "AUDIT_RETRACT"},
                {name: "待审计撤回", value: "PEND_AUDIT_RETRACT"},
                {name: "未通过", value: "NOT_PASS"},
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
            //注意: MdniUser.roleNameList 是个字符串,需要转为数组!否则影响操作按钮角色的判断!
            dealRoleNameList: function () {
                var self = this;
                self.roleNameList = [];
                var str = MdniUser.roleNameList.substring(1, MdniUser.roleNameList.length -1);
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
                    url: '/customercontract/contract/findAll',
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
                            align: 'center'
                        },
                        {
                            field: 'customerName',
                            title: '客户姓名',
                            align: 'center'
                        }, {
                            field: 'customerMobile',
                            title: '联系方式',
                            align: 'center'
                        }, {
                            field: 'layout',
                            title: '户型',
                            align: 'center'
                        },
                        {
                            field: 'valuateArea',
                            title: '计价面积(m²)',
                            width: '10%',
                            align: 'center'
                        },
                        {
                            field: 'mealName',
                            title: '所选套餐(￥)',
                            align: 'center'
                        }, {
                            field: 'designer',
                            title: '设计师',
                            align: 'center'
                        },
                        {
                            field: 'auditor',
                            title: '审计员',
                            align: 'center'
                        }, {
                            field: 'singleOrderInfo',
                            title: '串单信息',
                            align: 'center'
                        }, {
                            field: 'contractStatus',
                            title: '状态',
                            align: 'center',
                            formatter: function (val) {
                                switch (val) {
                                    case 'NOT_SELECT_MATERIAL':
                                        return '未选材';
                                    case 'MATERIAL_SELECTION':
                                        return '选材中';
                                    case 'ASSIGN_AUDIT':
                                        return '指派审计';
                                    case 'UNDER_AUDIT':
                                        return '审核中';
                                    case 'WAIT_TRANSFER':
                                        return '等待转单';
                                    case 'TRANSFER_COMPLETE':
                                        return '转单完成';
                                    case 'AUDIT_RETRACT':
                                        return '审计撤回';
                                    case 'NOT_PASS':
                                        return '未通过';
                                    case 'PEND_AUDIT_RETRACT':
                                        return '待审计撤回';
                                }
                            }
                        },
                        {
                            field: 'downloadDate',
                            title: '提醒',
                            align: 'center'
                        }, {
                            field: 'id',
                            title: '操作',
                            align: 'center',
                            formatter: function (value, row) {
                                if (row) {
                                    var html = '';
                                    /*html += '<button data-handle="data-change" data-id="' + row.id + '" class="m-r-xs btn btn-xs btn-primary" type="button">选材</button>';
                                     html += '<button data-handle="data-audit" data-id="' + row.id + '" class="m-r-xs btn btn-xs btn-primary" type="button">审计</button>';
                                     html += '<button data-handle="data-auditor" data-id="' + row.id + '" data-contract-code="' + row.contractCode + '" class="m-r-xs btn btn-xs btn-primary" type="button">审计员</button>';
                                     html += '<button data-handle="data-adopt" data-id="' + row.id + '" class="m-r-xs btn btn-xs btn-primary" type="button">通过</button>';
                                     html += '<button data-handle="data-failed" data-id="' + row.id + '" class="m-r-xs btn btn-xs btn-primary" type="button">未通过</button>';
                                     html += '<button data-handle="data-preview" data-id="' + row.id + '" class="m-r-xs btn btn-xs btn-primary" type="button">预览</button>';
                                     html += '<button data-handle="data-more" data-id="' + row.id + '" class="m-r-xs btn btn-xs btn-primary" type="button">更多</button>';*/
                                    if (row.contractStatus == self.contractStatuList[0].value) {
                                        //未选材
                                        if (self.roleNameList && (self.roleNameList.indexOf(self.roleNameAllList[0]) != -1
                                            || self.roleNameList.indexOf(self.roleNameAllList[4]) != -1)) {
                                            html += '<button data-handle="data-change" data-id="' + row.id + '" class="m-r-xs btn btn-xs btn-primary" type="button">选材</button>';
                                        }
                                    } else if (row.contractStatus == self.contractStatuList[1].value && self.roleNameList) {
                                        //选材中
                                        if (self.roleNameList.indexOf(self.roleNameAllList[0]) != -1
                                            || self.roleNameList.indexOf(self.roleNameAllList[4]) != -1) {
                                            //选材按钮
                                            html += '<button data-handle="data-change" data-id="' + row.id + '" class="m-r-xs btn btn-xs btn-primary" type="button">选材</button>';
                                        }
                                        if (self.roleNameList.indexOf(self.roleNameAllList[2]) == -1) {
                                            //预览按钮
                                            html += '<button data-handle="data-preview" data-id="' + row.id + '" class="m-r-xs btn btn-xs btn-primary" type="button">预览</button>';
                                        }
                                        if (self.roleNameList.indexOf(self.roleNameAllList[0]) != -1 ||
                                            self.roleNameList.indexOf(self.roleNameAllList[1]) != -1 ||
                                            self.roleNameList.indexOf(self.roleNameAllList[4]) != -1 ||
                                            self.roleNameList.indexOf(self.roleNameAllList[5]) != -1) {
                                            //更多按钮
                                            html += '<button data-handle="data-more" data-id="' + row.id + '" data-contract-status="'
                                                + row.contractStatus + '" class="m-r-xs btn btn-xs btn-primary" type="button">更多</button>';
                                        }
                                    } else if (row.contractStatus == self.contractStatuList[2].value && self.roleNameList) {
                                        //指派审计
                                        if (self.roleNameList.indexOf(self.roleNameAllList[1]) != -1) {
                                            //审计员按钮
                                            html += '<button data-handle="data-auditor" data-id="' + row.id + '" data-contract-code="' + row.contractCode + '" class="m-r-xs btn btn-xs btn-primary" type="button">审计员</button>';
                                        }
                                        if (self.roleNameList.indexOf(self.roleNameAllList[2]) == -1) {
                                            //预览按钮
                                            html += '<button data-handle="data-preview" data-id="' + row.id + '" class="m-r-xs btn btn-xs btn-primary" type="button">预览</button>';
                                        }
                                        if (self.roleNameList.indexOf(self.roleNameAllList[0]) != -1 ||
                                            self.roleNameList.indexOf(self.roleNameAllList[1]) != -1 ||
                                            self.roleNameList.indexOf(self.roleNameAllList[4]) != -1 ||
                                            self.roleNameList.indexOf(self.roleNameAllList[5]) != -1) {
                                            //更多按钮
                                            html += '<button data-handle="data-more" data-id="' + row.id + '" data-contract-status="' + row.contractStatus + '" class="m-r-xs btn btn-xs btn-primary" type="button">更多</button>';
                                        }
                                    } else if (row.contractStatus == self.contractStatuList[3].value && self.roleNameList) {
                                        //审核中
                                        if (self.roleNameList.indexOf(self.roleNameAllList[1]) != -1 ||
                                            self.roleNameList.indexOf(self.roleNameAllList[2]) != -1) {
                                            //审计按钮
                                            html += '<button data-handle="data-audit" data-id="' + row.id + '" class="m-r-xs btn btn-xs btn-primary" type="button">审计</button>';
                                        }
                                        if (self.roleNameList.indexOf(self.roleNameAllList[1]) != -1) {
                                            //审计员按钮
                                            html += '<button data-handle="data-auditor" data-id="' + row.id + '" data-contract-code="' + row.contractCode + '" class="m-r-xs btn btn-xs btn-primary" type="button">审计员</button>';
                                        }
                                        if (self.roleNameList.indexOf(self.roleNameAllList[1]) != -1 ||
                                            self.roleNameList.indexOf(self.roleNameAllList[2]) != -1) {
                                            //通过按钮
                                            html += '<button data-handle="data-adopt" data-id="' + row.id + '" class="m-r-xs btn btn-xs btn-primary" type="button">通过</button>';
                                        }
                                        if (self.roleNameList.indexOf(self.roleNameAllList[1]) != -1 ||
                                            self.roleNameList.indexOf(self.roleNameAllList[2]) != -1) {
                                            //未通过按钮
                                            html += '<button data-handle="data-failed" data-id="' + row.id + '" class="m-r-xs btn btn-xs btn-primary" type="button">未通过</button>';
                                        }
                                        //预览按钮
                                        html += '<button data-handle="data-preview" data-id="' + row.id + '" class="m-r-xs btn btn-xs btn-primary" type="button">预览</button>';

                                        if (self.roleNameList.indexOf(self.roleNameAllList[0]) != -1 ||
                                            self.roleNameList.indexOf(self.roleNameAllList[1]) != -1 ||
                                            self.roleNameList.indexOf(self.roleNameAllList[2]) != -1 ||
                                            self.roleNameList.indexOf(self.roleNameAllList[4]) != -1 ||
                                            self.roleNameList.indexOf(self.roleNameAllList[5]) != -1) {
                                            //更多按钮
                                            html += '<button data-handle="data-more" data-id="' + row.id + '" data-contract-status="' + row.contractStatus + '" class="m-r-xs btn btn-xs btn-primary" type="button">更多</button>';
                                        }
                                    } else if (row.contractStatus == self.contractStatuList[4].value && self.roleNameList) {
                                        //等待转单
                                        //预览按钮
                                        html += '<button data-handle="data-preview" data-id="' + row.id + '" class="m-r-xs btn btn-xs btn-primary" type="button">预览</button>';
                                        if (self.roleNameList.indexOf(self.roleNameAllList[0]) != -1 ||
                                            self.roleNameList.indexOf(self.roleNameAllList[1]) != -1 ||
                                            self.roleNameList.indexOf(self.roleNameAllList[2]) != -1 ||
                                            self.roleNameList.indexOf(self.roleNameAllList[4]) != -1 ||
                                            self.roleNameList.indexOf(self.roleNameAllList[5]) != -1) {
                                            //更多按钮
                                            html += '<button data-handle="data-more" data-id="' + row.id + '" data-contract-status="' + row.contractStatus + '" class="m-r-xs btn btn-xs btn-primary" type="button">更多</button>';
                                        }
                                    } else if (row.contractStatus == self.contractStatuList[5].value && self.roleNameList) {
                                        //转单完成
                                        //预览按钮
                                        html += '<button data-handle="data-preview" data-id="' + row.id + '" class="m-r-xs btn btn-xs btn-primary" type="button">预览</button>';
                                        if (self.roleNameList.indexOf(self.roleNameAllList[0]) != -1 ||
                                            self.roleNameList.indexOf(self.roleNameAllList[1]) != -1 ||
                                            self.roleNameList.indexOf(self.roleNameAllList[2]) != -1 ||
                                            self.roleNameList.indexOf(self.roleNameAllList[3]) != -1 ||
                                            self.roleNameList.indexOf(self.roleNameAllList[4]) != -1 ||
                                            self.roleNameList.indexOf(self.roleNameAllList[5]) != -1) {
                                            //更多按钮
                                            html += '<button data-handle="data-more" data-id="' + row.id + '" data-contract-status="' + row.contractStatus + '" class="m-r-xs btn btn-xs btn-primary" type="button">更多</button>';
                                        }
                                    } else if (row.contractStatus == self.contractStatuList[6].value && self.roleNameList) {
                                        //审计撤回
                                        if (self.roleNameList.indexOf(self.roleNameAllList[0]) != -1
                                            || self.roleNameList.indexOf(self.roleNameAllList[4]) != -1) {
                                            //选材按钮
                                            html += '<button data-handle="data-change" data-id="' + row.id + '" class="m-r-xs btn btn-xs btn-primary" type="button">选材</button>';
                                        }
                                        if (self.roleNameList.indexOf(self.roleNameAllList[1]) == -1) {
                                            //预览按钮
                                            html += '<button data-handle="data-preview" data-id="' + row.id + '" class="m-r-xs btn btn-xs btn-primary" type="button">预览</button>';
                                        }
                                        if (self.roleNameList.indexOf(self.roleNameAllList[0]) != -1 ||
                                            self.roleNameList.indexOf(self.roleNameAllList[2]) != -1 ||
                                            self.roleNameList.indexOf(self.roleNameAllList[4]) != -1 ||
                                            self.roleNameList.indexOf(self.roleNameAllList[5]) != -1) {
                                            //更多按钮
                                            html += '<button data-handle="data-more" data-id="' + row.id + '" data-contract-status="' + row.contractStatus + '" class="m-r-xs btn btn-xs btn-primary" type="button">更多</button>';
                                        }
                                    } else if (row.contractStatus == self.contractStatuList[7].value && self.roleNameList) {
                                        //待审计撤回
                                        if (self.roleNameList.indexOf(self.roleNameAllList[1]) != -1) {
                                            //审计撤回按钮

                                        }
                                        //预览按钮
                                        html += '<button data-handle="data-preview" data-id="' + row.id + '" class="m-r-xs btn btn-xs btn-primary" type="button">预览</button>';

                                        if (self.roleNameList.indexOf(self.roleNameAllList[0]) != -1 ||
                                            self.roleNameList.indexOf(self.roleNameAllList[1]) != -1 ||
                                            self.roleNameList.indexOf(self.roleNameAllList[2]) != -1 ||
                                            self.roleNameList.indexOf(self.roleNameAllList[4]) != -1 ||
                                            self.roleNameList.indexOf(self.roleNameAllList[5]) != -1) {
                                            //更多按钮
                                            html += '<button data-handle="data-more" data-id="' + row.id + '" data-contract-status="' + row.contractStatus + '" class="m-r-xs btn btn-xs btn-primary" type="button">更多</button>';
                                        }
                                    } else if (row.contractStatus == self.contractStatuList[8].value && self.roleNameList) {
                                        //未通过
                                        if (self.roleNameList.indexOf(self.roleNameAllList[0]) != -1
                                            || self.roleNameList.indexOf(self.roleNameAllList[4]) != -1) {
                                            //选材按钮
                                            html += '<button data-handle="data-change" data-id="' + row.id + '" class="m-r-xs btn btn-xs btn-primary" type="button">选材</button>';
                                        }
                                        //预览按钮
                                        html += '<button data-handle="data-preview" data-id="' + row.id + '" class="m-r-xs btn btn-xs btn-primary" type="button">预览</button>';

                                        if (self.roleNameList.indexOf(self.roleNameAllList[0]) != -1 ||
                                            self.roleNameList.indexOf(self.roleNameAllList[1]) != -1 ||
                                            self.roleNameList.indexOf(self.roleNameAllList[2]) != -1 ||
                                            self.roleNameList.indexOf(self.roleNameAllList[4]) != -1 ||
                                            self.roleNameList.indexOf(self.roleNameAllList[5]) != -1) {
                                            //更多按钮
                                            html += '<button data-handle="data-more" data-id="' + row.id + '" data-contract-status="' + row.contractStatus + '" class="m-r-xs btn btn-xs btn-primary" type="button">更多</button>';
                                        }
                                    }
                                    return html;
                                }
                            }
                        }]
                });
                // 选材
                self.$dataTable.on('click', '[data-handle="data-change"]',
                    function (e) {
                        var id = $(this).data('id');
                        self.$http.get('/customercontract/contract/getById?id=' + id).then(function (res) {
                            if (res.data.code == 1) {
                                if (res.data.data.contractStatus != 'NOT_SELECT_MATERIAL') {
                                    var params = {
                                        contractCode: res.data.data.contractCode,
                                        pageType: 'select'
                                    }
                                    MdniUtils.locationHrefToClient('/material/materialIndex', params);
                                    /*window.location.href = '/material/materialIndex?contractCode=' + res.data.data.contractCode
                                        + '&pageType=select';*/
                                } else {
                                    modifyModal(res.data.data);
                                }
                            }
                        }).catch(function () {
                        }).finally(function () {
                        });
                        e.stopPropagation();
                    });
                // 更多
                //注意:要对应索引! 选材单下载 0 老房拆改项下载 1 提交审计 2 申请审计退回  3，审计退回 4
                //      页面上按照这些顺序排列!!!
                self.$dataTable.on('click', '[data-handle="data-more"]',
                    function (e) {
                        var id = $(this).data('id');
                        var contractStatus = $(this).data('contractStatus');
                        console.log(contractStatus);
                        //判断当前角色所对应的 能查看的数据
                        if (!self.roleNameList) {
                            self.$toastr.error("您没有该权限!");
                            return;
                        }
                        var stindex = "";
                        if (contractStatus == self.contractStatuList[1].value) {
                            //选材中 2367 没有
                            if (self.roleNameList.indexOf(self.roleNameAllList[0]) != -1) {
                                //设计师
                                stindex += "0,1,2,";
                            }
                            if (self.roleNameList.indexOf(self.roleNameAllList[1]) != -1) {
                                //审计经理
                                stindex += "0,1,";
                            }
                            if (self.roleNameList.indexOf(self.roleNameAllList[4]) != -1) {
                                //设计总监
                                stindex += "0,1,";
                            }
                            if (self.roleNameList.indexOf(self.roleNameAllList[5]) != -1) {
                                //材料部经理
                                stindex += "0,";
                            }
                        } else if (contractStatus == self.contractStatuList[2].value) {
                            //指派审计 2367 没有
                            if (self.roleNameList.indexOf(self.roleNameAllList[0]) != -1) {
                                //设计师
                                stindex += "0,1,";
                            }
                            if (self.roleNameList.indexOf(self.roleNameAllList[1]) != -1) {
                                //审计经理
                                stindex += "0,1,";
                            }
                            if (self.roleNameList.indexOf(self.roleNameAllList[4]) != -1) {
                                //设计总监
                                stindex += "0,1,";
                            }
                            if (self.roleNameList.indexOf(self.roleNameAllList[5]) != -1) {
                                //材料部经理
                                stindex += "0,";
                            }
                        } else if (contractStatus == self.contractStatuList[3].value) {
                            //审核中 01245 有
                            if (self.roleNameList.indexOf(self.roleNameAllList[0]) != -1) {
                                //设计师
                                stindex += "0,1,";
                            }
                            if (self.roleNameList.indexOf(self.roleNameAllList[1]) != -1) {
                                //审计经理
                                stindex += "0,1,";
                            }
                            if (self.roleNameList.indexOf(self.roleNameAllList[2]) != -1) {
                                //审计员
                                stindex += "0,";
                            }
                            if (self.roleNameList.indexOf(self.roleNameAllList[4]) != -1) {
                                //设计总监
                                stindex += "0,1,";
                            }
                            if (self.roleNameList.indexOf(self.roleNameAllList[5]) != -1) {
                                //材料部经理
                                stindex += "0,";
                            }
                        } else if (contractStatus == self.contractStatuList[4].value) {
                            //等待转单 01245 有
                            if (self.roleNameList.indexOf(self.roleNameAllList[0]) != -1) {
                                //设计师
                                stindex += "0,1,3,";
                            }
                            if (self.roleNameList.indexOf(self.roleNameAllList[1]) != -1) {
                                //审计经理
                                stindex += "0,1,";
                            }
                            if (self.roleNameList.indexOf(self.roleNameAllList[2]) != -1) {
                                //审计员
                                stindex += "0,";
                            }
                            if (self.roleNameList.indexOf(self.roleNameAllList[4]) != -1) {
                                //设计总监
                                stindex += "0,1,";
                            }
                            if (self.roleNameList.indexOf(self.roleNameAllList[5]) != -1) {
                                //材料部经理
                                stindex += "0,";
                            }
                        } else if (contractStatus == self.contractStatuList[5].value) {
                            //转单完成 01245 有
                            if (self.roleNameList.indexOf(self.roleNameAllList[0]) != -1) {
                                //设计师
                                stindex += "0,1,";
                            }
                            if (self.roleNameList.indexOf(self.roleNameAllList[1]) != -1) {
                                //审计经理
                                stindex += "0,1,";
                            }
                            if (self.roleNameList.indexOf(self.roleNameAllList[2]) != -1) {
                                //审计员
                                stindex += "0,";
                            }
                            if (self.roleNameList.indexOf(self.roleNameAllList[4]) != -1) {
                                //设计总监
                                stindex += "0,1,";
                            }
                            if (self.roleNameList.indexOf(self.roleNameAllList[5]) != -1 ||
                                self.roleNameList.indexOf(self.roleNameAllList[3]) != -1) {
                                //材料部经理  材料部审核员
                                stindex += "0,";
                            }
                        } else if (contractStatus == self.contractStatuList[6].value) {
                            //审计撤回 0245 有
                            if (self.roleNameList.indexOf(self.roleNameAllList[0]) != -1) {
                                //设计师
                                stindex += "0,1,2,";
                            }
                            if (self.roleNameList.indexOf(self.roleNameAllList[2]) != -1) {
                                //审计员
                                stindex += "0,";
                            }
                            if (self.roleNameList.indexOf(self.roleNameAllList[4]) != -1) {
                                //设计总监
                                stindex += "0,1,";
                            }
                            if (self.roleNameList.indexOf(self.roleNameAllList[5]) != -1) {
                                //材料部经理
                                stindex += "0,";
                            }
                        } else if (contractStatus == self.contractStatuList[7].value) {
                            //待审计撤回 01245 有
                            if (self.roleNameList.indexOf(self.roleNameAllList[0]) != -1) {
                                //设计师
                                stindex += "0,1,";
                            }
                            if (self.roleNameList.indexOf(self.roleNameAllList[1]) != -1) {
                                //审计经理
                                stindex += "0,1,4,";
                            }
                            if (self.roleNameList.indexOf(self.roleNameAllList[2]) != -1) {
                                //审计员
                                stindex += "0,";
                            }
                            if (self.roleNameList.indexOf(self.roleNameAllList[4]) != -1) {
                                //设计总监
                                stindex += "0,1,";
                            }
                            if (self.roleNameList.indexOf(self.roleNameAllList[5]) != -1) {
                                //材料部经理
                                stindex += "0,";
                            }
                        } else if (contractStatus == self.contractStatuList[8].value) {
                            //未通过 01245 有
                            if (self.roleNameList.indexOf(self.roleNameAllList[0]) != -1) {
                                //设计师
                                stindex += "0,1,2,";
                            }
                            if (self.roleNameList.indexOf(self.roleNameAllList[1]) != -1) {
                                //审计经理
                                stindex += "0,1,";
                            }
                            if (self.roleNameList.indexOf(self.roleNameAllList[2]) != -1) {
                                //审计员
                                stindex += "0,";
                            }
                            if (self.roleNameList.indexOf(self.roleNameAllList[4]) != -1) {
                                //设计总监
                                stindex += "0,1,";
                            }
                            if (self.roleNameList.indexOf(self.roleNameAllList[5]) != -1) {
                                //材料部经理
                                stindex += "0,";
                            }
                        }
                        console.log(stindex);
                        var displayObj = [];
                        for (var i = 0; i < 5; i++) {
                            if (stindex.indexOf(i) != -1) {
                                displayObj.push(true);
                            } else {
                                displayObj.push(false);
                            }
                        }
                        console.log(displayObj)
                        self.$http.get('/customercontract/contract/getById?id=' + id).then(function (res) {
                            if (res.data.code == 1) {
                                moreModal(res.data.data, displayObj);
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
                                var params = {
                                    contractCode: model.contractCode,
                                    pageType: 'audit'
                                }
                                MdniUtils.locationHrefToClient('/material/materialIndex', params);
                                /*window.location.href = '/material/materialIndex?contractCode=' + model.contractCode
                                    + '&pageType=audit';*/
                            }
                        }).catch(function () {
                        }).finally(function () {
                        });
                        e.stopPropagation();
                    });
                // 审计员
                self.$dataTable.on('click', '[data-handle="data-auditor"]',
                    function (e) {
                        auditorModal($(this).data('id'), $(this).data('contractCode'));
                        e.stopPropagation();
                    });
                // 通过
                self.$dataTable.on('click', '[data-handle="data-adopt"]',
                    function (e) {
                        var id = $(this).data('id');
                        var status = 'WAIT_TRANSFER';
                        swal({
                            title: '确认通过？',
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
                            self.$http.get('/customercontract/contract/' + id + '/updateStatus?status=' + status).then(function (res) {
                                if (res.data.code == 1) {
                                    Vue.toastr.success("操作成功");
                                    vueIndex.$dataTable.bootstrapTable('refresh');
                                }
                            }).catch(function () {

                            }).finally(function () {
                                swal.close();
                            });
                        });
                        e.stopPropagation();
                    });
                // 未通过
                self.$dataTable.on('click', '[data-handle="data-failed"]',
                    function (e) {
                        var id = $(this).data('id');
                        var status = 'NOT_PASS';
                        swal({
                            title: '确认不通过？',
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
                            self.$http.get('/customercontract/contract/' + id + '/updateStatus?status=' + status).then(function (res) {
                                if (res.data.code == 1) {
                                    Vue.toastr.success("操作成功");
                                    vueIndex.$dataTable.bootstrapTable('refresh');
                                }
                            }).catch(function () {

                            }).finally(function () {
                                swal.close();
                            });

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
                                    if (info.indexOf('吊顶') != -1) {
                                        hangCeiling = '吊顶';
                                    }
                                    if (info.indexOf('石膏线') != -1) {
                                        plasterLine = '石膏线';
                                    }
                                    if (info.indexOf('电视背景墙') != -1) {
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

    function modifyModal(model) {
        var _modal = $('#modifyModal').clone();
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
                    validators: {
                        mobile: function (val) {
                            return /^1(3|4|5|7|8)\d{9}$/.test(val) || (val === '');//手机号必须为11位数字
                        }
                    },
                    http: {
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded'
                        }
                    },
                    // 模式窗体必须引用 ModalMixin
                    mixins: [MdniVueMixins.ModalMixin],
                    components: {
                        'web-uploader': MdniVueComponents.WebUploaderComponent
                    },
                    $modal: $el,
                    //模式窗体 jQuery 对象
                    created: function () {
                    },
                    ready: function () {
                        this.fetchMeal();
                    },
                    data: {
                        provinceCode: model.provinceCode,
                        cityCode: model.cityCode,
                        areaCode: model.areaCode,
                        addressProvince: '',
                        addressCity: '',
                        addressArea: '',

                        province: province,
                        city: city,
                        district: district,

                        id: model.id,
                        meals: null,
                        contractCode: model.contractCode,
                        houseAddr: model.houseAddr,
                        customerName: model.customerName,
                        customerMobile: model.customerMobile,
                        secondContact: model.secondContact,
                        secondContactMobile: model.secondContactMobile,
                        elevator: model.elevator,
                        houseCondition: model.houseCondition,
                        houseType: 3,
                        layout: model.layout,
                        buildArea: model.buildArea,
                        valuateArea: model.valuateArea,
                        mealId: null,
                        contractStatus: 'MATERIAL_SELECTION',
                        activityName: model.activityName,
                        storeCode: model.storeCode
                    },

                    methods: {
                        fetchMeal: function () {
                            var self = this;
                            self.$http.get('/material/mealinfo/findmealbystorecode').then(function (res) {
                                if (res.data.code == 1) {
                                    self.meals = res.data.data;
                                    self.mealId = model.mealId == null ? '' : model.mealId;
                                }
                            }).catch(function () {
                            }).finally(function () {
                            })
                        },
                        selectChange: function () {
                            this.cityCode = '';
                            this.areaCode = '';
                        },
                        //开始设计
                        save: function () {
                            var self = this;
                            self.addressProvince = $("#" + self.provinceCode).text().trim();
                            self.addressCity = $("#" + self.provinceCode + '-' + self.cityCode).text().trim();
                            self.addressArea = $("#" + self.cityCode + '-' + self.areaCode).text().trim();
                            var source = self._data;
                            delete source.province;
                            delete source.city;
                            delete source.district;
                            delete source.meals;
                            self.$validate(true, function () {
                                if (self.$validation.valid) {
                                    self.submitting = true;
                                    self.$http.post('/customercontract/contract/startdesign', $.param(self._data)).then(function (res) {
                                            if (res.data.code == '1') {
                                                Vue.toastr.success(res.data.message);
                                                $el.modal('hide');
                                                vueIndex.$dataTable.bootstrapTable('refresh');
                                                var params = {
                                                    contractCode: model.contractCode,
                                                    pageType: 'select'
                                                }
                                                MdniUtils.locationHrefToClient('/material/materialIndex', params);
                                                //window.location.href = '/material/materialIndex?contractCode=' + model.contractCode + '&pageType=select';
                                                self.$destroy();
                                            } else {
                                                Vue.toastr.error(res.data.message);
                                            }
                                        },
                                        function (error) {
                                            Vue.toastr.error(error.responseText);
                                        }).catch(function () {
                                    }).finally(function () {
                                        self.submitting = false;
                                    });
                                }
                            });
                        }
                    }
                });

                // 创建的Vue对象应该被返回
                return vueModal;
            });
    }

    function moreModal(model, displayObj) {
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
                    mixins: [MdniVueMixins.ModalMixin],
                    components: {
                        'web-uploader': MdniVueComponents.WebUploaderComponent
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
                            window.location.href = '/material/exportexcel/metarialexport?contractCode=' + model.contractCode + '&type=' + '2'
                        },
                        demolitionHouses: function () {
                            window.location.href = '/material/exportexcel/oldhouseexport?contractCode=' + model.contractCode + '&contractStatus=' + model.contractStatus
                        },
                        submitAudit: function () {
                            var self = this;
                            var status = 'ASSIGN_AUDIT';
                            self.$http.get('/material/smskudosage/getSkuDoSageByContractCode?contractCode=' + model.contractCode).then(function (res) {
                                if (res.data.code == 1) {
                                    var doSage = res.data.data;
                                    var lossDosage = true;
                                    if (doSage.length != 0) {
                                        doSage.forEach(function (sage) {
                                            if (sage.categoryDetailCode == 'PACKAGESTANDARD' || sage.categoryDetailCode == 'UPGRADEITEM' || sage.categoryDetailCode == 'MAINMATERIAL') {
                                                if (sage.lossDosage == null) {
                                                    lossDosage = false;
                                                    return;
                                                }
                                            }
                                        });
                                        if (lossDosage == false) {
                                            swal({
                                                title: '有用量未填写',
                                                type: 'info',
                                                confirmButtonText: '确定',
                                                cancelButtonText: '取消',
                                                showCancelButton: true,
                                                showConfirmButton: true,
                                                showLoaderOnConfirm: true,
                                                confirmButtonColor: '#ed5565',
                                                closeOnConfirm: false
                                            })

                                        } else {
                                            swal({
                                                title: '确认提交审计？',
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
                                                self.$http.get('/customercontract/contract/' + model.id + '/updateStatus?status=' + status).then(function (res) {
                                                    if (res.data.code == 1) {
                                                        Vue.toastr.success("操作成功");
                                                        $el.modal('hide');
                                                        vueIndex.$dataTable.bootstrapTable('refresh');
                                                    }
                                                }).catch(function () {

                                                }).finally(function () {
                                                    swal.close();
                                                });
                                            })
                                        }
                                    } else {
                                        swal({
                                            title: '未选材',
                                            type: 'info',
                                            confirmButtonText: '确定',
                                            cancelButtonText: '取消',
                                            showCancelButton: true,
                                            showConfirmButton: true,
                                            showLoaderOnConfirm: true,
                                            confirmButtonColor: '#ed5565',
                                            closeOnConfirm: false
                                        })
                                    }
                                }
                            });
                        },
                        auditApplyReturn: function () {
                            var self = this;
                            var status = 'PEND_AUDIT_RETRACT';
                            if (model.contractStatus == 'WAIT_TRANSFER') {
                                if (model.orderFlowStatus == 'STAY_SIGN') {
                                    swal({
                                        title: '确认申请审计撤回？',
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
                                        self.$http.get('/customercontract/contract/' + model.id + '/updateStatus?status=' + status).then(function (res) {
                                            if (res.data.code == 1) {
                                                Vue.toastr.success("操作成功");
                                                $el.modal('hide');
                                                vueIndex.$dataTable.bootstrapTable('refresh');
                                            }
                                        }).catch(function () {

                                        }).finally(function () {
                                            swal.close();
                                        });
                                    })
                                } else {
                                    swal({
                                        title: '订单状态不是待签约',
                                        type: 'info',
                                        confirmButtonText: '确定',
                                        cancelButtonText: '取消',
                                        showCancelButton: true,
                                        showConfirmButton: true,
                                        showLoaderOnConfirm: true,
                                        confirmButtonColor: '#ed5565',
                                        closeOnConfirm: false
                                    })
                                }
                            } else {
                                swal({
                                    title: '选材状态不是等待转单',
                                    type: 'info',
                                    confirmButtonText: '确定',
                                    cancelButtonText: '取消',
                                    showCancelButton: true,
                                    showConfirmButton: true,
                                    showLoaderOnConfirm: true,
                                    confirmButtonColor: '#ed5565',
                                    closeOnConfirm: false
                                })
                            }
                        },

                        auditReturn: function () {
                            var self = this;
                            var status = 'AUDIT_RETRACT';
                            swal({
                                title: '确认审计撤回？',
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
                                self.$http.get('/customercontract/contract/' + model.id + '/updateStatus?status=' + status).then(function (res) {
                                    if (res.data.code == 1) {
                                        Vue.toastr.success("操作成功");
                                        $el.modal('hide');
                                        vueIndex.$dataTable.bootstrapTable('refresh');
                                    }
                                }).catch(function () {
                                }).finally(function () {
                                    swal.close();
                                });
                            })
                        }
                    }
                });

                // 创建的Vue对象应该被返回
                return vueModal;
            });
    }

    function auditorModal(contractId, contractCode) {
        var _modal = $('#auditorModal').clone();
        var $el = _modal.modal({
            width: 700
        });
        $el.on('shown.bs.modal',
            function () {
                // 获取 node
                var el = $el.get(0);
                // 创建 Vue 对象编译节点
                var vueModal = new Vue({
                    el: el,
                    // 模式窗体必须引用 ModalMixin
                    mixins: [MdniVueMixins.ModalMixin],
                    $modal: $el,
                    data: {
                        contractId: contractId,
                        contractCode: contractCode,
                        auditor: '',
                        auditors: [],
                        historyList: []
                    },
                    //模式窗体 jQuery 对象
                    created: function () {
                    },
                    ready: function () {
                        this.findHistoryAuditor();
                        this.fetchAuditor();
                    },

                    methods: {
                        findHistoryAuditor: function () {
                            var self = this;
                            self.$http.get('/customercontract/contract/findHistoryAuditor?contractCode=' + self.contractCode).then(function (res) {
                                if (res.data.code == 1) {
                                    self.historyList = res.data.data;
                                }
                            }).catch(function () {
                            }).finally(function () {
                            });
                        },
                        fetchAuditor: function () {
                            var self = this;
                            self.$http.get('/order/findAllAudit').then(function (res) {
                                if (res.data.code == 1) {
                                    self.auditors = res.data.data;
                                }
                            }).catch(function () {
                            }).finally(function () {
                            });
                        },
                        saveAuditor: function () {
                            var self = this;
                            self.$validate(true,function () {
                                if (self.$validation.valid) {
                                    self.submitting = true;
                                    self.$http.get('/customercontract/contract/updateAuditor?id=' + self.contractId
                                        + '&auditorOrgCode=' + self.auditor.orgCode
                                        + '&auditorName=' + self.auditor.name
                                        + '&auditorPhone=' + self.auditor.mobile
                                    ).then(function (res) {
                                        if (res.data.code == 1) {
                                            Vue.toastr.success("操作成功");
                                            $el.modal('hide');
                                            vueIndex.$dataTable.bootstrapTable('refresh');
                                        }
                                    }).catch(function () {

                                    }).finally(function () {
                                        self.submitting = false;
                                    });
                                }
                            });
                        }
                    },
                    filters: {
                        dateFormat: function (val) {
                            var newDate = new Date();
                            newDate.setTime(val);
                            return MdniUtils.formatDate(newDate, 'yyyy-MM-dd hh:mm:ss');
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
                    mixins: [MdniVueMixins.ModalMixin],
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
                        //###############价格相关#################
                        //获取价格
                        findAmount: function () {
                            var self = this;
                            self.$http.get('/material/smskudosage/statisticsamount/' + model.contractCode + '/' + 'select').then(function (res) {
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
                            var url = "/material/smskudosage/statisticsamount/" + model.contractCode + "/";
                            url += 'select';
                            self.$http.get(url).then(function (res) {
                                if (res.data.code == '1') {
                                    self.totalAmount = res.data.data;
                                } else {
                                    self.$toastr.error("计算总金额失败!");
                                }
                            })
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
                                + model.contractCode +'&pageType=select';
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
                                + model.contractCode +'&pageType=select';
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
                            var url = "/material/otheraddreduceamount/findlistbycontractcode?contractCode="
                                + model.contractCode + "&pageType=select";
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