var tt = null;
+(function () {
    $('#orderMenu').addClass('active');
    $('#singleMenu').addClass('active');
    tt = new Vue({
        el: '#container',
        data: {
            breadcrumbs: [{
                path: '/',
                name: '主页'
            }, {
                path: '',
                name: '订单管理'
            }, {
                path: '/',
                name: '订单列表',
                active: true
            }],
            form: {
                operateType: '',
                keyword: '',
                startTime: '',
                endTime: '',
                orderFlowStatus: ''
            },
            _$el: null,
            _$dataTable: null,
            id: null,
            //所有的角色集合
            roleNameAllList: [
                "设计师", "设计总监", "设计组长", "督导组长", "督导", "管理员"
            ],
            //个人角色集合
            roleNameList: [],
            //订单状态集合
            contractStatuList: [
                {name: "待设计", value: "STAY_DESIGN"},
                {name: "待签约", value: "STAY_SIGN"},
                {name: "待重新派单", value: "STAY_SEND_SINGLE_AGAIN"},
                {name: "待施工", value: "STAY_CONSTRUCTION"},
                {name: "施工中", value: "ON_CONSTRUCTION"},
                {name: "已竣工", value: "PROJECT_COMPLETE"},
                {name: "待转大定", value: "STAY_TURN_DETERMINE"},
                {name: "督导组长待分配", value: "SUPERVISOR_STAY_ASSIGNED"},
                {name: "设计待分配", value: "DESIGN_STAY_ASSIGNED"},
                {name: "申请分配退回", value: "APPLY_REFUND"},
                {name: "订单关闭", value: "ORDER_CLOSE"},
            ],
        },
        created: function () {
            this.dealRoleNameList();
        },
        ready: function () {
            this.drawTable();
            this.activeDatepiker();

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
            activeDatepiker: function () {
                var self = this;
                $(self.$els.startTime).datetimepicker('setStartDate', '');
                $(self.$els.endTime).datetimepicker('setStartDate', '');
            },
            drawTable: function () {
                var self = this;
                self.$dataTable = $('#dataTable', self._$el).bootstrapTable({
                    url: '/order/findAll',
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
                        }, {
                            field: '',
                            title: '客户/订单',
                            align: 'center',
                            formatter: function (row, val) {
                                return val.customerName + '</br>' + "/" + val.customerMobile + '</br>' + "/" + val.contractCode
                            }
                        }, {
                            field: 'customerTag',
                            title: '客户标签',
                            align: 'center'
                        }, {
                            field: 'stageName',
                            title: '付款阶段',
                            align: 'center'
                        }, {
                            field: '',
                            title: '第二联系人/电话',
                            align: 'center',
                            formatter: function (row, val) {
                                if (val.secondContact != null) {
                                    return val.secondContact + '</br>' + "/" + val.secondContactMobile;
                                } else {
                                    return '-';
                                }
                            }
                        }, {
                            field: '',
                            title: '工程地址/面积',
                            align: 'center',
                            //督导组长看不到该列
                            visible: self.roleNameList.indexOf(self.roleNameAllList[3]) == -1,
                            formatter: function (row, val) {
                                if (val.buildArea != null && val.houseAddr) {
                                    return val.houseAddr + '</br>' + "/" + val.buildArea + "m²";
                                } else {
                                    return '-';
                                }
                            }
                        }, {
                            field: '',
                            title: '客服/下单时间',
                            align: 'center',
                            formatter: function (row, val) {
                                if (val.serviceName == null) {
                                    return "-";
                                } else if (val.createTime == null) {
                                    return val.serviceName + "/" + '-';
                                } else {
                                    return val.serviceName + '</br>' + "/" + val.createTime;
                                }
                            }
                        }, {
                            field: '',
                            title: '设计师/签约时间',
                            align: 'center',
                            formatter: function (row, val) {
                                if (val.designerDepName != null && val.designer != null && val.designerMobile != null && val.completeTime != null) {
                                    return val.designerDepName + "/" + val.designer + '</br>' + "/" + val.designerMobile + '</br>' + "/" + val.completeTime;
                                } else if (val.designerDepName == null && val.designer != null && val.designerMobile != null && val.completeTime != null) {
                                    return "-" + "/" + val.designer + '</br>' + "/" + val.designerMobile + '</br>' + "/" + val.completeTime;
                                } else if (val.designerDepName != null && val.designer != null && val.designerMobile == null && val.completeTime != null) {
                                    return val.designerDepName + "/" + val.designer + '</br>' + "/" + "-" + '</br>' + "/" + val.completeTime;
                                } else if (val.designerDepName != null && val.designer != null && val.designerMobile != null && val.completeTime == null) {
                                    return val.designerDepName + "/" + val.designer + '</br>' + "/" + val.designerMobile + '</br>' + "/" + "-";
                                } else if (val.designerDepName != null && val.designer == null && val.designerMobile == null && val.completeTime == null) {
                                    return val.designerDepName + "/" + "-";
                                } else {
                                    return "-"
                                }
                            }
                        }, {
                            field: '',
                            title: '量房时间',
                            width: '7%',
                            align: 'center',
                            formatter: function (row, val) {
                                if (val.bookHouseTime != null) {
                                    return "预约:" + '</br>' + moment(val.bookHouseTime).format('YYYY-MM-DD');//预约
                                } else if (val.planHouseTime != null) {
                                    return "计划:" + '</br>' + moment(val.planHouseTime).format('YYYY-MM-DD');//计划
                                }
                            }
                        }, {
                            field: 'orderFlowStatus',
                            title: '订单状态',
                            align: 'center',
                            formatter: function (val) {
                                switch (val) {
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
                                        return '竣工';
                                    case 'ORDER_CLOSE':
                                        return '订单关闭';
                                }
                            }
                        }, {
                            field: '',
                            title: '操作',
                            align: 'center',
                            formatter: function (value, row) {
                                var fragment = '';
                                if (row.orderFlowStatus == self.contractStatuList[0].value && self.roleNameList) {
                                    //待设计
                                    if (self.roleNameList.indexOf(self.roleNameAllList[0]) != -1 ||
                                        self.roleNameList.indexOf(self.roleNameAllList[1]) != -1 ||
                                        self.roleNameList.indexOf(self.roleNameAllList[2]) != -1) {
                                        //查看项目信息 按钮
                                        fragment += ('<button data-handle="data-detail" data-id="' + row.id + '" data-contractcode="' + row.contractCode + '" type="button" class="btn btn-xs btn-primary">查看项目信息</button>&nbsp');
                                    }
                                    if (self.roleNameList.indexOf(self.roleNameAllList[2]) != -1) {
                                        //预约量房
                                        fragment += ('<button   data-handle="appoint-measure" data-id="' + row.id + '" type="button" class="btn btn-xs btn-primary">预约量房</button>&nbsp');
                                    }
                                    if (self.roleNameList.indexOf(self.roleNameAllList[2]) != -1 ||
                                        self.roleNameList.indexOf(self.roleNameAllList[3]) != -1) {
                                        //分配 按钮
                                        fragment += ('<button data-handle="send" data-id="' + row.id + '" type="button" class="btn btn-xs btn-primary">分配</button>&nbsp');
                                    }
                                    if (self.roleNameList.indexOf(self.roleNameAllList[0]) != -1
                                        || self.roleNameList.indexOf(self.roleNameAllList[2]) != -1) {
                                        //退回 按钮
                                        fragment += ('<button data-handle="retreat" data-id="' + row.id + '" data-contractcode="' + row.contractCode + '"  type="button" class="btn btn-xs btn-primary">退回</button>&nbsp');
                                    }
                                    if (self.roleNameList.indexOf(self.roleNameAllList[0]) != -1
                                        || self.roleNameList.indexOf(self.roleNameAllList[2]) != -1) {
                                        //量房 按钮
                                        fragment += ('<button   data-handle="design-send" data-id="' + row.id + '" type="button" class="btn btn-xs btn-primary">量房</button>&nbsp');
                                    }
                                    if (self.roleNameList.indexOf(self.roleNameAllList[0]) != -1 ||
                                        self.roleNameList.indexOf(self.roleNameAllList[2]) != -1) {
                                        //出图
                                        fragment += ('<button   data-handle="design" data-id="' + row.id + '" type="button" class="btn btn-xs btn-primary">出图</button>&nbsp');
                                    }
                                    if (self.roleNameList.indexOf(self.roleNameAllList[0]) != -1 ||
                                        self.roleNameList.indexOf(self.roleNameAllList[2]) != -1 ||
                                        self.roleNameList.indexOf(self.roleNameAllList[4]) != -1) {
                                        //签约
                                        fragment += ('<button data-handle="sign" data-contractcode="' + row.contractCode + '" type="button" class="btn btn-xs btn-primary">签约</button>&nbsp');
                                    }
                                    if (self.roleNameList.indexOf(self.roleNameAllList[0]) != -1 ||
                                        self.roleNameList.indexOf(self.roleNameAllList[2]) != -1) {
                                        //发起变更
                                        fragment += ('<button   data-handle="change" data-id="' + row.id + '" data-contractcode="' + row.contractCode + '" data-currentchangeversion="' + row.currentChangeVersion + '" data-contractstatus="' + row.contractStatus + '" type="button" class="btn btn-xs btn-primary">发起变更</button>&nbsp');
                                    }
                                    if (self.roleNameList.indexOf(self.roleNameAllList[2]) != -1 ||
                                        self.roleNameList.indexOf(self.roleNameAllList[3]) != -1) {
                                        //查看串单 按钮
                                        fragment += ('<button data-handle="single-preview" data-id="' + row.id + '" data-contractcode="' + row.contractCode + '" type="button" class="btn btn-xs btn-primary">查看串单</button>&nbsp');
                                    }
                                } else if (row.orderFlowStatus == self.contractStatuList[1].value && self.roleNameList) {
                                    //待签约
                                    if (self.roleNameList.indexOf(self.roleNameAllList[0]) != -1 ||
                                        self.roleNameList.indexOf(self.roleNameAllList[1]) != -1 ||
                                        self.roleNameList.indexOf(self.roleNameAllList[2]) != -1) {
                                        //查看项目信息 按钮
                                        fragment += ('<button data-handle="data-detail" data-id="' + row.id + '" data-contractcode="' + row.contractCode + '" type="button" class="btn btn-xs btn-primary">查看项目信息</button>&nbsp');
                                    }
                                    if (self.roleNameList.indexOf(self.roleNameAllList[0]) != -1
                                        || self.roleNameList.indexOf(self.roleNameAllList[2]) != -1) {
                                        //量房 按钮
                                        fragment += ('<button data-handle="design-send" data-id="' + row.id + '" type="button" class="btn btn-xs btn-primary">量房</button>&nbsp');
                                    }
                                    if (self.roleNameList.indexOf(self.roleNameAllList[0]) != -1 ||
                                        self.roleNameList.indexOf(self.roleNameAllList[2]) != -1) {
                                        //出图
                                        fragment += ('<button   data-handle="design" data-id="' + row.id + '" type="button" class="btn btn-xs btn-primary">出图</button>&nbsp');
                                    }
                                    if (self.roleNameList.indexOf(self.roleNameAllList[0]) != -1 ||
                                        self.roleNameList.indexOf(self.roleNameAllList[2]) != -1 ||
                                        self.roleNameList.indexOf(self.roleNameAllList[4]) != -1) {
                                        //签约
                                        fragment += ('<button   data-handle="sign" data-contractcode="' + row.contractCode + '" type="button" class="btn btn-xs btn-primary">签约</button>&nbsp');
                                    }
                                    if (self.roleNameList.indexOf(self.roleNameAllList[0]) != -1 ||
                                        self.roleNameList.indexOf(self.roleNameAllList[2]) != -1) {
                                        //选材变更
                                        fragment += ('<button   data-handle="change" data-id="' + row.id + '" data-contractcode="' + row.contractCode + '" data-currentchangeversion="' + row.currentChangeVersion + '" data-contractstatus="' + row.contractStatus + '" type="button" class="btn btn-xs btn-primary">发起变更</button>&nbsp');
                                    }
                                } else if (row.orderFlowStatus == self.contractStatuList[2].value && self.roleNameList) {
                                    //待重新派单
                                    if (self.roleNameList.indexOf(self.roleNameAllList[0]) != -1 ||
                                        self.roleNameList.indexOf(self.roleNameAllList[1]) != -1 ||
                                        self.roleNameList.indexOf(self.roleNameAllList[2]) != -1) {
                                        //查看项目信息 按钮
                                        fragment += ('<button data-handle="data-detail" data-id="' + row.id + '" data-contractcode="' + row.contractCode + '" type="button" class="btn btn-xs btn-primary">查看项目信息</button>&nbsp');
                                    }
                                    if (self.roleNameList.indexOf(self.roleNameAllList[0]) != -1
                                        || self.roleNameList.indexOf(self.roleNameAllList[2]) != -1) {
                                        //量房 按钮
                                        fragment += ('<button data-handle="design-send" data-id="' + row.id + '" type="button" class="btn btn-xs btn-primary">量房</button>&nbsp');
                                    }
                                    if (self.roleNameList.indexOf(self.roleNameAllList[0]) != -1 ||
                                        self.roleNameList.indexOf(self.roleNameAllList[2]) != -1) {
                                        //出图
                                        fragment += ('<button   data-handle="design" data-id="' + row.id + '" type="button" class="btn btn-xs btn-primary">出图</button>&nbsp');
                                    }
                                    if (self.roleNameList.indexOf(self.roleNameAllList[0]) != -1 ||
                                        self.roleNameList.indexOf(self.roleNameAllList[2]) != -1 ||
                                        self.roleNameList.indexOf(self.roleNameAllList[4]) != -1) {
                                        //签约
                                        fragment += ('<button   data-handle="sign" data-contractcode="' + row.contractCode + '" type="button" class="btn btn-xs btn-primary">签约</button>&nbsp');
                                    }
                                    if (self.roleNameList.indexOf(self.roleNameAllList[0]) != -1 ||
                                        self.roleNameList.indexOf(self.roleNameAllList[2]) != -1) {
                                        //选材变更
                                        fragment += ('<button   data-handle="change" data-id="' + row.id + '" data-contractcode="' + row.contractCode + '" data-currentchangeversion="' + row.currentChangeVersion + '" data-contractstatus="' + row.contractStatus + '" type="button" class="btn btn-xs btn-primary">发起变更</button>&nbsp');
                                    }
                                } else if (row.orderFlowStatus == self.contractStatuList[3].value && self.roleNameList) {
                                    //待施工
                                    if (self.roleNameList.indexOf(self.roleNameAllList[0]) != -1 ||
                                        self.roleNameList.indexOf(self.roleNameAllList[1]) != -1 ||
                                        self.roleNameList.indexOf(self.roleNameAllList[2]) != -1) {
                                        //查看项目信息 按钮
                                        fragment += ('<button data-handle="data-detail" data-id="' + row.id + '" data-contractcode="' + row.contractCode + '" type="button" class="btn btn-xs btn-primary">查看项目信息</button>&nbsp');
                                    }
                                    if (self.roleNameList.indexOf(self.roleNameAllList[0]) != -1
                                        || self.roleNameList.indexOf(self.roleNameAllList[2]) != -1) {
                                        //量房 按钮
                                        fragment += ('<button   data-handle="design-send" data-id="' + row.id + '" type="button" class="btn btn-xs btn-primary">量房</button>&nbsp');
                                    }
                                    if (self.roleNameList.indexOf(self.roleNameAllList[0]) != -1 ||
                                        self.roleNameList.indexOf(self.roleNameAllList[2]) != -1) {
                                        //出图
                                        fragment += ('<button   data-handle="design" data-id="' + row.id + '" type="button" class="btn btn-xs btn-primary">出图</button>&nbsp');
                                    }
                                    if (self.roleNameList.indexOf(self.roleNameAllList[0]) != -1 ||
                                        self.roleNameList.indexOf(self.roleNameAllList[2]) != -1 ||
                                        self.roleNameList.indexOf(self.roleNameAllList[4]) != -1) {
                                        //签约
                                        fragment += ('<button   data-handle="sign" data-contractcode="' + row.contractCode + '" type="button" class="btn btn-xs btn-primary">签约</button>&nbsp');
                                    }
                                    if (self.roleNameList.indexOf(self.roleNameAllList[0]) != -1 ||
                                        self.roleNameList.indexOf(self.roleNameAllList[2]) != -1) {
                                        //选材变更
                                        fragment += ('<button   data-handle="change" data-id="' + row.id + '" data-contractcode="' + row.contractCode + '" data-currentchangeversion="' + row.currentChangeVersion + '" data-contractstatus="' + row.contractStatus + '" type="button" class="btn btn-xs btn-primary">发起变更</button>&nbsp');
                                    }
                                } else if (row.orderFlowStatus == self.contractStatuList[4].value && self.roleNameList) {
                                    //施工中
                                    if (self.roleNameList.indexOf(self.roleNameAllList[0]) != -1 ||
                                        self.roleNameList.indexOf(self.roleNameAllList[1]) != -1 ||
                                        self.roleNameList.indexOf(self.roleNameAllList[2]) != -1) {
                                        //查看项目信息 按钮
                                        fragment += ('<button data-handle="data-detail" data-id="' + row.id + '" data-contractcode="' + row.contractCode + '" type="button" class="btn btn-xs btn-primary">查看项目信息</button>&nbsp');
                                    }
                                    if (self.roleNameList.indexOf(self.roleNameAllList[0]) != -1) {
                                        //量房 按钮
                                        fragment += ('<button   data-handle="design-send" data-id="' + row.id + '" type="button" class="btn btn-xs btn-primary">量房</button>&nbsp');
                                    }
                                    if (self.roleNameList.indexOf(self.roleNameAllList[0]) != -1) {
                                        //出图
                                        fragment += ('<button   data-handle="design" data-id="' + row.id + '" type="button" class="btn btn-xs btn-primary">出图</button>&nbsp');
                                    }
                                    if (self.roleNameList.indexOf(self.roleNameAllList[0]) != -1 ||
                                        self.roleNameList.indexOf(self.roleNameAllList[4]) != -1) {
                                        //签约
                                        fragment += ('<button   data-handle="sign" data-contractcode="' + row.contractCode + '" type="button" class="btn btn-xs btn-primary">签约</button>&nbsp');
                                    }
                                    if (self.roleNameList.indexOf(self.roleNameAllList[0]) != -1) {
                                        //选材变更
                                        fragment += ('<button   data-handle="change" data-id="' + row.id + '" data-contractcode="' + row.contractCode + '" data-currentchangeversion="' + row.currentChangeVersion + '" data-contractstatus="' + row.contractStatus + '" type="button" class="btn btn-xs btn-primary">发起变更</button>&nbsp');
                                    }
                                } else if (row.orderFlowStatus == self.contractStatuList[5].value && self.roleNameList) {
                                    //已竣工
                                    if (self.roleNameList.indexOf(self.roleNameAllList[0]) != -1 ||
                                        self.roleNameList.indexOf(self.roleNameAllList[1]) != -1 ||
                                        self.roleNameList.indexOf(self.roleNameAllList[2]) != -1) {
                                        //查看项目信息 按钮
                                        fragment += ('<button data-handle="data-detail" data-id="' + row.id + '" data-contractcode="' + row.contractCode + '" type="button" class="btn btn-xs btn-primary">查看项目信息</button>&nbsp');
                                    }
                                    if (self.roleNameList.indexOf(self.roleNameAllList[0]) != -1) {
                                        //量房 按钮
                                        fragment += ('<button   data-handle="design-send" data-id="' + row.id + '" type="button" class="btn btn-xs btn-primary">量房</button>&nbsp');
                                    }
                                    if (self.roleNameList.indexOf(self.roleNameAllList[0]) != -1) {
                                        //出图
                                        fragment += ('<button   data-handle="design" data-id="' + row.id + '" type="button" class="btn btn-xs btn-primary">出图</button>&nbsp');
                                    }
                                    if (self.roleNameList.indexOf(self.roleNameAllList[0]) != -1 ||
                                        self.roleNameList.indexOf(self.roleNameAllList[4]) != -1) {
                                        //签约
                                        fragment += ('<button   data-handle="sign" data-contractcode="' + row.contractCode + '" type="button" class="btn btn-xs btn-primary">签约</button>&nbsp');
                                    }
                                    if (self.roleNameList.indexOf(self.roleNameAllList[0]) != -1) {
                                        //选材变更
                                        fragment += ('<button   data-handle="change" data-id="' + row.id + '" data-contractcode="' + row.contractCode + '" data-currentchangeversion="' + row.currentChangeVersion + '" data-contractstatus="' + row.contractStatus + '" type="button" class="btn btn-xs btn-primary">发起变更</button>&nbsp');
                                    }
                                } else if (row.orderFlowStatus == self.contractStatuList[6].value && self.roleNameList) {
                                    //待转大定
                                    if (self.roleNameList.indexOf(self.roleNameAllList[0]) != -1 ||
                                        self.roleNameList.indexOf(self.roleNameAllList[1]) != -1 ||
                                        self.roleNameList.indexOf(self.roleNameAllList[2]) != -1) {
                                        //查看项目信息 按钮
                                        fragment += ('<button data-handle="data-detail" data-id="' + row.id + '" data-contractcode="' + row.contractCode + '" type="button" class="btn btn-xs btn-primary">查看项目信息</button>&nbsp');
                                    }
                                } else if (row.orderFlowStatus == self.contractStatuList[7].value && self.roleNameList) {
                                    //督导组长待分配
                                    if (self.roleNameList.indexOf(self.roleNameAllList[0]) != -1 ||
                                        self.roleNameList.indexOf(self.roleNameAllList[1]) != -1 ||
                                        self.roleNameList.indexOf(self.roleNameAllList[2]) != -1) {
                                        //查看项目信息 按钮
                                        fragment += ('<button data-handle="data-detail" data-id="' + row.id + '" data-contractcode="' + row.contractCode + '" type="button" class="btn btn-xs btn-primary">查看项目信息</button>&nbsp');
                                    }
                                    if (self.roleNameList.indexOf(self.roleNameAllList[3]) != -1) {
                                        //分配 按钮
                                        fragment += ('<button data-handle="send" data-id="' + row.id + '" type="button" class="btn btn-xs btn-primary">分配</button>&nbsp');
                                    }
                                    if (self.roleNameList.indexOf(self.roleNameAllList[3]) != -1) {
                                        //查看串单 按钮
                                        fragment += ('<button data-handle="single-preview" data-id="' + row.id + '" data-contractcode="' + row.contractCode + '" type="button" class="btn btn-xs btn-primary">查看串单</button>&nbsp');
                                    }
                                } else if (row.orderFlowStatus == self.contractStatuList[8].value && self.roleNameList) {
                                    //设计待分配
                                    if (self.roleNameList.indexOf(self.roleNameAllList[0]) != -1 ||
                                        self.roleNameList.indexOf(self.roleNameAllList[1]) != -1 ||
                                        self.roleNameList.indexOf(self.roleNameAllList[2]) != -1) {
                                        //查看项目信息 按钮
                                        fragment += ('<button data-handle="data-detail" data-id="' + row.id + '" data-contractcode="' + row.contractCode + '" type="button" class="btn btn-xs btn-primary">查看项目信息</button>&nbsp');
                                    }
                                    if (self.roleNameList.indexOf(self.roleNameAllList[2]) != -1) {
                                        //预约量房
                                        fragment += ('<button data-handle="appoint-measure" data-id="' + row.id + '" type="button" class="btn btn-xs btn-primary">预约量房</button>&nbsp');
                                    }
                                    if (self.roleNameList.indexOf(self.roleNameAllList[2]) != -1 ||
                                        self.roleNameList.indexOf(self.roleNameAllList[3]) != -1) {
                                        //分配 按钮
                                        fragment += ('<button data-handle="send" data-id="' + row.id + '" type="button" class="btn btn-xs btn-primary">分配</button>&nbsp');
                                    }
                                    if (self.roleNameList.indexOf(self.roleNameAllList[2]) != -1) {
                                        //退回 按钮
                                        fragment += ('<button data-handle="retreat" data-id="' + row.id + '" data-contractcode="' + row.contractCode + '"  type="button" class="btn btn-xs btn-primary">退回</button>&nbsp');
                                    }
                                    if (self.roleNameList.indexOf(self.roleNameAllList[2]) != -1) {
                                        //量房 按钮
                                        fragment += ('<button   data-handle="design-send" data-id="' + row.id + '" type="button" class="btn btn-xs btn-primary">量房</button>&nbsp');
                                    }
                                    if (self.roleNameList.indexOf(self.roleNameAllList[2]) != -1) {
                                        //出图
                                        fragment += ('<button   data-handle="design" data-id="' + row.id + '" type="button" class="btn btn-xs btn-primary">出图</button>&nbsp');
                                    }
                                    if (self.roleNameList.indexOf(self.roleNameAllList[2]) != -1) {
                                        //签约
                                        fragment += ('<button   data-handle="sign" data-contractcode="' + row.contractCode + '" type="button" class="btn btn-xs btn-primary">签约</button>&nbsp');
                                    }
                                    if (self.roleNameList.indexOf(self.roleNameAllList[2]) != -1) {
                                        //选材变更
                                        fragment += ('<button   data-handle="change" data-id="' + row.id + '" data-contractcode="' + row.contractCode + '" data-currentchangeversion="' + row.currentChangeVersion + '" data-contractstatus="' + row.contractStatus + '" type="button" class="btn btn-xs btn-primary">发起变更</button>&nbsp');
                                    }
                                    if (self.roleNameList.indexOf(self.roleNameAllList[2]) != -1 ||
                                        self.roleNameList.indexOf(self.roleNameAllList[3]) != -1) {
                                        //查看串单 按钮
                                        fragment += ('<button data-handle="single-preview" data-id="' + row.id + '" data-contractcode="' + row.contractCode + '" type="button" class="btn btn-xs btn-primary">查看串单</button>&nbsp');
                                    }
                                } else if (row.orderFlowStatus == self.contractStatuList[9].value && self.roleNameList) {
                                    //申请分配退回
                                    if (self.roleNameList.indexOf(self.roleNameAllList[1]) != -1) {
                                        //查看项目信息 按钮
                                        fragment += ('<button data-handle="data-detail" data-id="' + row.id + '" data-contractcode="' + row.contractCode + '" type="button" class="btn btn-xs btn-primary">查看项目信息</button>&nbsp');
                                    }
                                    if (self.roleNameList.indexOf(self.roleNameAllList[3]) != -1) {
                                        //收回 按钮
                                        fragment += ('<button data-handle="recovery" data-id="' + row.id + '" data-contractcode="' + row.contractCode + '" type="button" class="btn btn-xs btn-primary">收回</button>&nbsp');
                                    }
                                } else if (row.orderFlowStatus == self.contractStatuList[10].value && self.roleNameList) {
                                    //订单关闭
                                    if (self.roleNameList.indexOf(self.roleNameAllList[0]) != -1 ||
                                        self.roleNameList.indexOf(self.roleNameAllList[2]) != -1) {
                                        //查看项目信息 按钮
                                        fragment += ('<button data-handle="data-detail" data-id="' + row.id + '" data-contractcode="' + row.contractCode + '" type="button" class="btn btn-xs btn-primary">查看项目信息</button>&nbsp');
                                    }
                                }
                                return fragment;
                            }
                        }
                    ]
                });
                //查看串单
                self.$dataTable.on('click', '[data-handle="single-preview"]',
                    function (e) {
                        var contractCode = $(this).data('contractcode');
                        singlePreviewModal(contractCode);
                        e.stopPropagation();
                    });
                //查看详情
                self.$dataTable.on('click', '[data-handle="data-detail"]',
                    function (e) {
                        var id = $(this).data('id');
                        var contractCode = $(this).data('contractcode');
                        self.$http.get('/order/getbycode?contractCode=' + contractCode).then(function (res) {
                            if (res.data.code == 1) {
                                detailModal(res.data.data);
                            }
                        }).catch(function () {
                        }).finally(function () {
                        });
                        e.stopPropagation();
                    });
                //分配
                self.$dataTable.on('click', '[data-handle="send"]',
                    function (e) {
                        var id = $(this).data('id');
                        self.$http.get('/customercontract/contract/getById?id=' + id).then(function (res) {
                            if (res.data.code == 1) {
                                if (DameiUser.roleNameList.toString().indexOf('督导') >= 0) {
                                    sendGroupModal(res.data.data);
                                } else if (DameiUser.roleNameList.toString().indexOf('设计组长') >= 0) {
                                    sendDesignerModal(res.data.data);
                                }
                            }
                        }).catch(function () {
                        }).finally(function () {
                        });
                        e.stopPropagation();
                    });
                //预约量房
                self.$dataTable.on('click', '[data-handle="appoint-measure"]',
                    function (e) {
                        var id = $(this).data('id');
                        self.$http.get('/order/getById?id=' + id).then(function (res) {
                            if (res.data.code == 1) {
                                appointMeasureHouseModal(res.data.data);
                            }
                        }).catch(function () {
                        }).finally(function () {
                        });
                        e.stopPropagation();
                    });
                //量房
                self.$dataTable.on('click', '[data-handle="design-send"]',
                    function (e) {
                        var id = $(this).data('id');
                        self.$http.get('/order/getById?id=' + id).then(function (res) {
                            if (res.data.code == 1) {
                                designSendModal(res.data.data);
                            }
                        }).catch(function () {
                        }).finally(function () {
                        });
                        e.stopPropagation();
                    });
                //出图
                self.$dataTable.on('click', '[data-handle="design"]',
                    function (e) {
                        var id = $(this).data('id');
                        self.$http.get('/order/getById?id=' + id).then(function (res) {
                            if (res.data.code == 1) {
                                designModal(res.data.data);
                            }
                        }).catch(function () {
                        }).finally(function () {
                        });
                        e.stopPropagation();
                    });
                //签约
                self.$dataTable.on('click', '[data-handle="sign"]',
                    function (e) {
                        var contractCode = $(this).data('contractcode');
                        self.$http.get('/projectSign/getByCode?contractCode=' + contractCode).then(function (res) {
                            if (res.data.code == 1) {
                                var projectSign = res.data.data;
                                self.$http.get('/order/getbycode?contractCode=' + contractCode).then(function (resp) {
                                    if (resp.data.code == 1) {
                                        var contract = resp.data.data;
                                        if (projectSign.completeTime != null) {
                                            signPreviewModal(projectSign);
                                        } else {
                                            if (contract.bookHouseCompleteTime != null) {
                                                if (contract.outMapCompleteTime != null) {
                                                    if (contract.contractStatus == 'WAIT_TRANSFER' && contract.orderFlowStatus == 'STAY_SIGN') {
                                                        signModal(res.data.data);
                                                    } else {
                                                        swal({
                                                            title: '未满足签约状态:选材或者订单流转状态不符',
                                                            type: 'info',
                                                            confirmButtonText: '确定',
                                                            cancelButtonText: '取消',
                                                            showCancelButton: true,
                                                            showConfirmButton: true,
                                                            showLoaderOnConfirm: true,
                                                            confirmButtonColor: '#ed5565',
                                                            closeOnConfirm: false
                                                        });
                                                    }
                                                } else {
                                                    swal({
                                                        title: '出图未完成',
                                                        type: 'info',
                                                        confirmButtonText: '确定',
                                                        cancelButtonText: '取消',
                                                        showCancelButton: true,
                                                        showConfirmButton: true,
                                                        showLoaderOnConfirm: true,
                                                        confirmButtonColor: '#ed5565',
                                                        closeOnConfirm: false
                                                    });
                                                }
                                            } else {
                                                swal({
                                                    title: '量房未完成',
                                                    type: 'info',
                                                    confirmButtonText: '确定',
                                                    cancelButtonText: '取消',
                                                    showCancelButton: true,
                                                    showConfirmButton: true,
                                                    showLoaderOnConfirm: true,
                                                    confirmButtonColor: '#ed5565',
                                                    closeOnConfirm: false
                                                });
                                            }
                                        }
                                    }
                                });
                            }
                        }).catch(function () {
                        }).finally(function () {
                        });
                        e.stopPropagation();
                    });
                // 发起变更
                self.$dataTable.on('click', '[data-handle="change"]',
                    function (e) {
                        var id = $(this).data('id');
                        var contractCode = $(this).data('contractcode');
                        var currentChangeVersion = $(this).data('currentchangeversion');
                        var contractStatus = $(this).data('contractstatus');
                        var sign = null;
                        var fina = null;
                        self.$http.get('/projectSign/getByCode?contractCode=' + contractCode).then(function (res) {
                            if (res.data.code == 1) {
                                sign = res.data.data;
                                self.$http.get('/projectSign/getFinaFinishTime?contractCode=' + contractCode).then(function (res) {
                                    fina = res.data.data;
                                    if (sign.completeTime != null && fina.finishTime != null) {
                                        if (currentChangeVersion != null) {
                                            if (contractStatus == 'TRANSFER_COMPLETE') {
                                                swal({
                                                    title: '确认发起变更？',
                                                    text: '将发起变更',
                                                    type: 'info',
                                                    confirmButtonText: '确定',
                                                    cancelButtonText: '取消',
                                                    showCancelButton: true,
                                                    showConfirmButton: true,
                                                    showLoaderOnConfirm: true,
                                                    confirmButtonColor: '#ed5565',
                                                    closeOnConfirm: false
                                                }, function () {
                                                    self.$http.get('/order/startChange?id=' + id).then(function (res) {
                                                        if (res.data.code == 1) {
                                                            Vue.toastr.success("操作成功");
                                                            tt.$dataTable.bootstrapTable('refresh');
                                                        }
                                                    }).catch(function () {
                                                    }).finally(function () {
                                                        swal.close();
                                                    });
                                                });
                                            } else {
                                                swal({
                                                    title: '上次变更未完成',
                                                    type: 'info',
                                                    confirmButtonText: '确定',
                                                    cancelButtonText: '取消',
                                                    showCancelButton: true,
                                                    showConfirmButton: true,
                                                    showLoaderOnConfirm: true,
                                                    confirmButtonColor: '#ed5565',
                                                    closeOnConfirm: false
                                                });
                                            }
                                        } else {
                                            if (contractStatus == 'TRANSFER_COMPLETE') {
                                                swal({
                                                    title: '确认发起变更？',
                                                    text: '将发起变更',
                                                    type: 'info',
                                                    confirmButtonText: '确定',
                                                    cancelButtonText: '取消',
                                                    showCancelButton: true,
                                                    showConfirmButton: true,
                                                    showLoaderOnConfirm: true,
                                                    confirmButtonColor: '#ed5565',
                                                    closeOnConfirm: false
                                                }, function () {
                                                    self.$http.get('/order/startChange?id=' + id).then(function (res) {
                                                        if (res.data.code == 1) {
                                                            Vue.toastr.success("操作成功");
                                                            tt.$dataTable.bootstrapTable('refresh');
                                                        }
                                                    }).catch(function () {
                                                    }).finally(function () {
                                                        swal.close();
                                                    });
                                                });
                                            } else {
                                                swal({
                                                    title: '项目状态不是转单完成',
                                                    type: 'info',
                                                    confirmButtonText: '确定',
                                                    cancelButtonText: '取消',
                                                    showCancelButton: true,
                                                    showConfirmButton: true,
                                                    showLoaderOnConfirm: true,
                                                    confirmButtonColor: '#ed5565',
                                                    closeOnConfirm: false
                                                });
                                            }
                                        }
                                    } else {
                                        swal({
                                            title: '未签约或者首期款未完成',
                                            type: 'info',
                                            confirmButtonText: '确定',
                                            cancelButtonText: '取消',
                                            showCancelButton: true,
                                            showConfirmButton: true,
                                            showLoaderOnConfirm: true,
                                            confirmButtonColor: '#ed5565',
                                            closeOnConfirm: false
                                        });
                                    }
                                });

                            }
                        }).catch(function () {
                        }).finally(function () {
                        });
                        // self.$http.get('/projectSign/getByCode?contractCode=' + contractCode).then(function (res) {
                        //     if (res.data.code == 1) {
                        //         fina = res.data.data;
                        //     }
                        // }).catch(function () {
                        // }).finally(function () {
                        // });

                        e.stopPropagation();
                    });
                //跳转选材
                self.$dataTable.on('click', '[data-handle="turn-material"]',
                    function () {
                        var contractCode = $(this).data('contractcode');
                        var params = {
                            contractCode: contractCode,
                            pageType: 'select'
                        }
                        DameiUtils.locationHrefToClient('/material/materialIndex', params);
                        //window.location.href = '/material/materialIndex?contractCode=' + contractCode + '&pageType=select';
                    });
                //退回
                self.$dataTable.on('click', '[data-handle="retreat"]',
                    function () {
                        var contractCode = $(this).data('contractcode');
                        self.$http.get('/order/getbycode?contractCode=' + contractCode).then(function (res) {
                            if (res.data.code == 1) {
                                var model = res.data.data;
                                if (model.orderFlowStatus == 'DESIGN_STAY_ASSIGNED' || model.orderFlowStatus == 'STAY_DESIGN') {
                                    if (model.contractStatus == 'NOT_SELECT_MATERIAL') {
                                        retreatModal(model);
                                    } else {
                                        swal({
                                            title: '已选材不能退回',
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
                                        title: '此时不能退回',
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
                    });
                //收回
                self.$dataTable.on('click', '[data-handle="recovery"]',
                    function () {
                        var contractCode = $(this).data('contractcode');
                        self.$http.get('/order/getbycode?contractCode=' + contractCode).then(function (res) {
                            if (res.data.code == 1) {
                                recoveryModal(res.data.data);
                            }
                        });
                    });
            }
        }
    });
    //查看串单
    function singlePreviewModal(contractCode) {
        var _modal = $('#singlePreviewModal').clone();
        var $el = _modal.modal({
            height: 500
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
                    $modal: $el,
                    data:{
                        _$dataTable: null
                    },
                    //模式窗体 jQuery 对象
                    created: function () {
                    },
                    ready: function () {
                        this.drawTable();
                    },
                    methods: {
                        drawTable: function () {
                            var self = this;
                            self.$dataTable = $('#singlePreviewTable', self._$el).bootstrapTable({
                                url: '/order/singledetail?contractCode=' + contractCode,
                                method: 'get',
                                dataType: 'json',
                                cache: false,
                                pagination: false,
                                sidePagination: 'server',
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
                                        field: 'contractCode',
                                        title: '项目编号',
                                        align: 'center'
                                    }, {
                                        field: '',
                                        title: '客户信息',
                                        align: 'center',
                                        formatter: function (row,val) {
                                            if (val.customerName != null && val.customerMobile == null) {
                                                return val.customerName + "/" + "-";
                                            } else {
                                                return val.customerName + "/" + val.customerMobile;
                                            }
                                        }
                                    }, {
                                        field: 'customerTag',
                                        title: '客户级别',
                                        align: 'center'
                                    }, {
                                        field: '',
                                        title: '客服信息',
                                        align: 'center',
                                        formatter: function (row,val) {
                                            if (val.serviceCode != null && val.serviceName != null && val.serviceMobile != null) {
                                                return val.serviceCode + "/" + val.serviceName + "/" + '<br/>' + val.serviceMobile;
                                            } else if (val.serviceCode != null && val.serviceName != null && val.serviceMobile == null) {
                                                return val.serviceCode + "/" + val.serviceName + "/" + '<br/>' + "-";
                                            } else if (val.serviceCode != null && val.serviceName == null && val.serviceMobile != null) {
                                                return val.serviceCode + "/" + "-" + "/" + '<br/>' + val.serviceMobile;
                                            } else if (val.serviceCode == null && val.serviceName != null && val.serviceMobile != null) {
                                                return "-" + "/" + val.serviceName + "/" + '<br/>' + val.serviceMobile;
                                            }
                                        }
                                    }, {
                                        field: '',
                                        title: '设计师信息',
                                        align: 'center',
                                        formatter: function (row, val) {
                                            if (val.designerDepName != null && val.designer != null && val.designerMobile != null) {
                                                return val.designerDepName + "/" + val.designer + '</br>' + "/" + val.designerMobile;
                                            } else if (val.designerDepName == null && val.designer != null && val.designerMobile != null) {
                                                return "-" + "/" + val.designer + '</br>' + "/" + val.designerMobile;
                                            } else if (val.designerDepName != null && val.designer != null && val.designerMobile == null) {
                                                return val.designerDepName + "/" + val.designer + '</br>' + "/" + "-";
                                            } else if (val.designerDepName != null && val.designer != null && val.designerMobile != null) {
                                                return val.designerDepName + "/" + val.designer + '</br>' + "/" + val.designerMobile;
                                            } else {
                                                return "-"
                                            }
                                        }
                                    }, {
                                        field: 'orderFlowStatus',
                                        title: '订单状态',
                                        align: 'center',
                                        formatter: function (val) {
                                            switch (val) {
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
                                                    return '竣工';
                                                case 'ORDER_CLOSE':
                                                    return '订单关闭';
                                            }
                                        }
                                    }]
                            });
                        }
                    }
                });
                // 创建的Vue对象应该被返回
                return vueModal;
            });
    }

    //查看详情
    function detailModal(model) {
        var _modal = $('#detailModal').clone();
        var $el = _modal.modal({
            height: 700
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
                    $modal: $el,
                    data: {
                        customerContract: model
                    },
                    //模式窗体 jQuery 对象
                    created: function () {
                    },
                    ready: function () {
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
                    methods: {}
                });
                // 创建的Vue对象应该被返回
                return vueModal;
            });
    }

    //督导组长分配设计组
    function sendGroupModal(model) {
        var _modal = $('#sendGroupModal').clone();
        var $el = _modal.modal({
            height: 150,
            maxHeight: 500
        });
        $el.on('shown.bs.modal',
            function () {
                // 获取 node
                var el = $el.get(0);
                // 创建 Vue 对象编译节点
                var vueModal = new Vue({
                    el: el,
                    // http: {
                    //     headers: {
                    //         'Content-Type': 'application/x-www-form-urlencoded'
                    //     }
                    // },
                    // 模式窗体必须引用 ModalMixin
                    mixins: [DameiVueMixins.ModalMixin],
                    $modal: $el,
                    data: {
                        designerGroup: '',
                        orgCode: model.designerDepCode == null ? '' : model.designerDepCode
                    },
                    //模式窗体 jQuery 对象
                    created: function () {
                    },
                    ready: function () {
                        this.fetchDesignerGroup();
                    },
                    methods: {
                        fetchDesignerGroup: function () {
                            var self = this;
                            self.$http.get('/order/findAllDesignerGroup').then(function (res) {
                                if (res.data.code == 1) {
                                    self.designerGroup = res.data.data;
                                }
                            }).catch(function () {

                            }).finally(function () {
                            });
                        },
                        save: function () {
                            var self = this;
                            var param = model;
                            param.designerDepCode = self.orgCode;
                            var arr = self.designerGroup;
                            for (var i = 0; i < arr.length; i++) {
                                if (self.orgCode == arr[i].orgCode) {
                                    param.designerDepName = arr[i].orgName;
                                }
                            }
                            self.$validate(true, function () {
                                if (self.$validation.valid) {
                                    self.submitting = true;
                                    self.$http.post('/order/updateDesignerGroup', param).then(function (res) {
                                        if (res.data.code == 1) {
                                            Vue.toastr.success("操作成功");
                                            $el.modal('hide');
                                            tt.$dataTable.bootstrapTable('refresh');
                                        }
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

    //设计组长分配设计师
    function sendDesignerModal(model) {
        var _modal = $('#sendDesignerModal').clone();
        var $el = _modal.modal({
            height: 150,
            maxHeight: 500
        });
        $el.on('shown.bs.modal',
            function () {
                // 获取 node
                var el = $el.get(0);
                // 创建 Vue 对象编译节点
                var vueModal = new Vue({
                    el: el,
                    // 模式窗体必须引用 ModalMixin
                    mixins: [DameiVueMixins.ModalMixin],
                    $modal: $el,
                    data: {
                        designers: '',
                        orgCode: model.designerCode == null ? '' : model.designerCode
                    },
                    //模式窗体 jQuery 对象
                    created: function () {
                    },
                    ready: function () {
                        this.fetchDesigner();
                    },
                    methods: {
                        fetchDesigner: function () {
                            var self = this;
                            self.$http.get('/order/findDesignByGroup').then(function (res) {
                                if (res.data.code == 1) {
                                    self.designers = res.data.data;
                                }
                            }).catch(function () {

                            }).finally(function () {
                            });
                        },
                        save: function () {
                            var self = this;
                            var param = model;
                            var arr = self.designers;
                            for (var i = 0; i < arr.length; i++) {
                                if (self.orgCode == arr[i].orgCode) {
                                    param.designer = arr[i].name;
                                    param.designerMobile = arr[i].mobile;
                                }
                            }
                            param.designerCode = self.orgCode;
                            self.$validate(true, function () {
                                if (self.$validation.valid) {
                                    self.submitting = true;
                                    self.$http.post('/order/updateDesigner', param).then(function (res) {
                                        if (res.data.code == 1) {
                                            Vue.toastr.success("操作成功");
                                            $el.modal('hide');
                                            tt.$dataTable.bootstrapTable('refresh');
                                        }
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

    //预约量房
    function appointMeasureHouseModal(model) {
        var _modal = $('#appointMeasureHouseModal').clone();
        var $el = _modal.modal({
            height: 450,
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
                        id: model.id,
                        bookHouseTime: model.bookHouseTime
                    },
                    //模式窗体 jQuery 对象
                    created: function () {
                    },
                    ready: function () {
                        this.activeDatepiker();
                    },
                    methods: {
                        activeDatepiker: function () {
                            var self = this;
                            $(self.$els.bookHouseTime).datetimepicker('setBookHouseTime', '');
                        },
                        saveAppointMeasureHouseTime: function () {
                            var self = this;
                            self.$http.post('/order/updateBookHouse', $.param(self._data)).then(function (res) {
                                if (res.data.code == 1) {
                                    Vue.toastr.success(res.data.message);
                                    $el.modal('hide');
                                    tt.$dataTable.bootstrapTable('refresh');
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

    //量房
    function designSendModal(model) {
        var _modal = $('#designSendModal').clone();
        var $el = _modal.modal({
            height: 450,
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
                        webUploaderAccessories: {   // 上传附件
                            type: 'accessories',
                            formData: {},
                            accept: {
                                title: '文件',
                                extensions: 'txt,doc,docx,xls,xlsx,pdf,rar,zip,dwg'
                            },
                            server: ctx + '/order/upload',
                            // 上传路径
                            fileNumLimit: 8,
                            fileSizeLimit: 10000 * 1024 * 100,  //总大小
                            fileSingleSizeLimit: 1000 * 1024 * 20  //单文件大小
                        },
                        accessories: [],  // 附件集合
                        accessories_percentage: 0,//上传进度
                        id: model.id,
                        planHouseTime: model.planHouseTime,
                        bookHouseComplete: 1,
                        bookHouseExecutor: model.bookHouseExecutor,
                        bookHouseCompleteTime: model.bookHouseCompleteTime,
                        houseAddr: model.houseAddr,
                        engineerArea: model.addressProvince + model.addressCity + model.addressArea
                    },
                    events: {
                        'webupload-upload-success-accessories': function (file, res) {
                            if (res.code == '1') {
                                var from = this.from;
                                this.$toastr.success('上传成功');
                                this.accessories.push(res.data);
                                // from.photoNums = (from.photoNums * 1) + 1;
                            } else {
                                this.$toastr.error(res.message);
                            }
                        },
                        'webupload-upload-progress-accessories': function (file, percentage) {
                            var self = this;
                            //进度
                            self.accessories_percentage = Math.round(percentage * 100);
                        },
                        'webupload-upload-error-accessories': function (file, percentage) {
                            var self = this;
                            this.$toastr.error("服务器异常，请联系管理员");
                        }
                    },
                    //模式窗体 jQuery 对象
                    created: function () {
                    },
                    ready: function () {
                        this.activeDatepiker();
                    },
                    methods: {
                        activeDatepiker: function () {
                            var self = this;
                            $(self.$els.bookHouseCompleteTime).datetimepicker('setBookHouseCompleteTime', '');
                        },
                        saveDesignSend: function () {
                            var self = this;
                            self.$http.post('/order/updateBookHouse', $.param(self._data)).then(function (res) {
                                if (res.data.code == 1) {
                                    Vue.toastr.success(res.data.message);
                                    $el.modal('hide');
                                    tt.$dataTable.bootstrapTable('refresh');
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

    //出图
    function designModal(model) {
        var _modal = $('#designModal').clone();
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
                    $modal: $el,
                    components: {
                        'web-uploader': DameiVueComponents.WebUploaderComponent
                    },
                    data: {
                        webUploaderAccessories: {   // 上传附件
                            type: 'accessories',
                            formData: {},
                            accept: {
                                title: '文件',
                                extensions: 'txt,doc,docx,xls,xlsx,pdf,rar,zip,dwg'
                            },
                            server: ctx + '/order/upload',
                            // 上传路径
                            fileNumLimit: 8,
                            fileSizeLimit: 10000 * 1024 * 100,  //总大小
                            fileSingleSizeLimit: 1000 * 1024 * 20  //单文件大小
                        },
                        accessories: [],  // 附件集合
                        accessories_percentage: 0,//上传进度
                        id: model.id,
                        outMapComplete: 1,
                        outMapCompleteTime: model.outMapCompleteTime,
                        outMapExecutor: model.outMapExecutor
                    },
                    events: {
                        'webupload-upload-success-accessories': function (file, res) {
                            if (res.code == '1') {
                                var from = this.from;
                                this.$toastr.success('上传成功');
                                this.accessories.push(res.data);
                                // from.photoNums = (from.photoNums * 1) + 1;
                            } else {
                                this.$toastr.error(res.message);
                            }
                        },
                        'webupload-upload-progress-accessories': function (file, percentage) {
                            var self = this;
                            //进度
                            self.accessories_percentage = Math.round(percentage * 100);
                        },
                        'webupload-upload-error-accessories': function (file, percentage) {
                            var self = this;
                            this.$toastr.error("服务器异常，请联系管理员");
                        }
                    },
                    //模式窗体 jQuery 对象
                    created: function () {
                    },
                    ready: function () {
                        this.activeDatepiker();
                    },
                    methods: {
                        activeDatepiker: function () {
                            var self = this;
                            $(self.$els.outMapCompleteTime).datetimepicker('setOutMapCompleteTime', '');
                        },
                        saveDesign: function () {
                            var self = this;
                            self.$http.post('/order/updateOutMap', $.param(self._data)).then(function (res) {
                                if (res.data.code == 1) {
                                    Vue.toastr.success(res.data.message);
                                    $el.modal('hide');
                                    tt.$dataTable.bootstrapTable('refresh');
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

    //签约
    function signModal(model) {
        var _modal = $('#signModal').clone();
        var $el = _modal.modal({
            height: 350,
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
                    $modal: $el,
                    data: {
                        id: model.id == null || model.id === 'undefined' ? '' : model.id,
                        contractCode: model.contractCode == null || model.contractCode === 'undefined' ? '' : model.contractCode,
                        complete: model.complete == null || model.complete === 'undefined' ? 1 : model.complete,
                        completeTime: model.completeTime == null || model.completeTime === 'undefined' ? '' : model.completeTime,
                        signExecutor: model.signExecutor == null || model.signExecutor === 'undefined' ? '' : model.signExecutor,
                        contractSignTrem: model.contractSignTrem === null || model.contractSignTrem === 'undefined' ? '' : model.contractSignTrem,
                        contractStartTime: model.contractStartTime === null || model.contractStartTime === 'undefined' ? '' : model.contractStartTime,
                        contractCompleteTime: model.contractCompleteTime === null || model.contractCompleteTime === 'undefined' ? '' : model.contractCompleteTime,
                        contractAmount: model.contractAmount === null || model.contractAmount === 'undefined' ? '' : model.contractAmount,
                        modifyAmount: model.modifyAmount === null || model.modifyAmount === 'undefined' ? '' : model.modifyAmount,
                        aa: true
                    },
                    //模式窗体 jQuery 对象
                    created: function () {
                    },
                    ready: function () {
                        this.activeDatepiker();
                    },
                    methods: {
                        activeDatepiker: function () {
                            var self = this;
                            $(self.$els.completeTime).datetimepicker('setCompleteTime', '');
                            $(self.$els.contractStartTime).datetimepicker('setContractStartTime', '')
                                .on('changeDate', function (ev) {
                                    self.contractCompleteTime = moment(ev.date).add(self.contractSignTrem, 'days')
                                        .format('L')
                                });
                            $(self.$els.contractCompleteTime).datetimepicker('setContractCompleteTime', '');
                        },
                        saveSign: function () {
                            var self = this;
                            self.$validate(true, function () {
                                if (self.$validation.valid) {
                                    self.submitting = true;
                                    self.$http.post('/projectSign/update', $.param(self._data)).then(function (res) {
                                        if (res.data.code == 1) {
                                            Vue.toastr.success(res.data.message);
                                            $el.modal('hide');
                                            tt.$dataTable.bootstrapTable('refresh');
                                        } else {
                                            Vue.toastr.error(res.data.message);
                                        }
                                    }).catch(function () {
                                    }).finally(function () {
                                        self.submitting = false;
                                    });
                                }
                            })
                        }
                    },
                    watch: {
                        'contractSignTrem': function (val) {
                            var self = this;
                            if (val != null && val > 0) {
                                vueModal.aa = false;
                                if (self.contractStartTime) {
                                    self.contractCompleteTime = moment(self.contractStartTime).add(self.contractSignTrem, 'days')
                                        .format('L')
                                }
                            }
                        }
                    }
                });
                // 创建的Vue对象应该被返回
                return vueModal;
            });
    }

    //签约查看
    function signPreviewModal(model) {
        var _modal = $('#signPreviewModal').clone();
        var $el = _modal.modal({
            height: 350,
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
                    $modal: $el,
                    data: {
                        id: model.id == null || model.id === 'undefined' ? '' : model.id,
                        contractCode: model.contractCode == null || model.contractCode === 'undefined' ? '' : model.contractCode,
                        complete: model.complete == null || model.complete === 'undefined' ? 1 : model.complete,
                        completeTime: model.completeTime == null || model.completeTime === 'undefined' ? '' : model.completeTime,
                        signExecutor: model.signExecutor == null || model.signExecutor === 'undefined' ? '' : model.signExecutor,
                        contractSignTrem: model.contractSignTrem === null || model.contractSignTrem === 'undefined' ? '' : model.contractSignTrem,
                        contractStartTime: model.contractStartTime === null || model.contractStartTime === 'undefined' ? '' : model.contractStartTime,
                        contractCompleteTime: model.contractCompleteTime === null || model.contractCompleteTime === 'undefined' ? '' : model.contractCompleteTime,
                        contractAmount: model.contractAmount === null || model.contractAmount === 'undefined' ? '' : model.contractAmount,
                        modifyAmount: model.modifyAmount === null || model.modifyAmount === 'undefined' ? '' : model.modifyAmount
                    },
                    //模式窗体 jQuery 对象
                    created: function () {
                    },
                    ready: function () {
                    },
                    methods: {}
                });
                // 创建的Vue对象应该被返回
                return vueModal;
            });
    }

    //退回
    function retreatModal(model) {
        var _modal = $('#retreatModal').clone();
        var $el = _modal.modal({
            height: 250,
            maxHeight: 300
        });
        $el.on('shown.bs.modal',
            function () {
                // 获取 node
                var el = $el.get(0);
                // 创建 Vue 对象编译节点
                var vueModal = new Vue({
                    el: el,
                    // 模式窗体必须引用 ModalMixin
                    mixins: [DameiVueMixins.ModalMixin],
                    $modal: $el,
                    data: {
                        returnReason: model.returnReason == null || model.returnReason === 'undefined' ? '' : model.returnReason,
                        returnReasonDescribe: model.returnReasonDescribe == null || model.returnReasonDescribe === 'undefined' ? '' : model.returnReasonDescribe
                    },
                    //模式窗体 jQuery 对象
                    created: function () {
                    },
                    ready: function () {
                    },
                    methods: {
                        saveRetreat: function () {
                            var self = this;
                            var param = model;
                            param.returnReason = self.returnReason;
                            param.returnReasonDescribe = self.returnReasonDescribe;
                            self.$validate(true, function () {
                                if (self.$validation.valid) {
                                    self.submitting = true;
                                    self.$http.post('/order/retreat', param).then(function (res) {
                                        if (res.data.code == 1) {
                                            Vue.toastr.success(res.data.message);
                                            $el.modal('hide');
                                            tt.$dataTable.bootstrapTable('refresh');
                                        }
                                    }).catch(function () {
                                    }).finally(function () {
                                        self.submitting = false;
                                    });
                                }
                            })
                        }
                    }
                });
                // 创建的Vue对象应该被返回
                return vueModal;
            });
    }

    //收回
    function recoveryModal(model) {
        var _modal = $('#recoveryModal').clone();
        var $el = _modal.modal({
            height: 250,
            maxHeight: 300
        });
        $el.on('shown.bs.modal',
            function () {
                // 获取 node
                var el = $el.get(0);
                // 创建 Vue 对象编译节点
                var vueModal = new Vue({
                    el: el,
                    // 模式窗体必须引用 ModalMixin
                    mixins: [DameiVueMixins.ModalMixin],
                    $modal: $el,
                    data: {
                        returnReason: model.returnReason == null || model.returnReason === 'undefined' ? '' : model.returnReason,
                        returnReasonDescribe: model.returnReasonDescribe == null || model.returnReasonDescribe === 'undefined' ? '' : model.returnReasonDescribe
                    },
                    //模式窗体 jQuery 对象
                    created: function () {
                    },
                    ready: function () {
                    },
                    methods: {
                        saveRecovery: function () {
                            var self = this;
                            var param = model;
                            param.returnReason = self.returnReason;
                            param.returnReasonDescribe = self.returnReasonDescribe;
                            if (model.orderFlowStatus == 'APPLY_REFUND') {
                                self.$http.post('/order/recovery', param).then(function (res) {
                                    if (res.data.code == 1) {
                                        Vue.toastr.success(res.data.message);
                                        $el.modal('hide');
                                        tt.$dataTable.bootstrapTable('refresh');
                                    }
                                }).catch(function () {
                                }).finally(function () {
                                });
                            } else {
                                swal({
                                    title: '此时不能收回',
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
                    }
                });
                // 创建的Vue对象应该被返回
                return vueModal;
            });
    }
})();