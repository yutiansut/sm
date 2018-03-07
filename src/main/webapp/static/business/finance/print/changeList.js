+(function () {
    $('#financePrintMenu').addClass('active');
    $('#materialChangeMenu').addClass('active');
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
                name: '主材变更单打印',
                active: true
            }],
            stageType:{},
            form: {
                keyWord: '',
                startDate:'',
                endDate:'',
                printCount: ''
            },
            $dataTable: null,
            _$el: null,
            pringIds: [],
            btnClass: 'btn-default',
            btnDisabled: true,

        },
        created: function () {
        },
        ready: function () {
            this.activeDatepiker();
            this.drawTable();
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
                if(this.form.startDate > this.form.endDate){
                    this.$toastr.error('您选择的开始时间不能大于结束时间！');
                }else {
                    this.pringIds = [];
                    this.$dataTable.bootstrapTable('selectPage', 1);
                }
            },
            drawTable: function () {
                var self = this;
                self.$dataTable = $('#dataTable', self._$el).bootstrapTable({
                    url: '/finance/changematerialprint/changemateriallist',
                    method: 'get',
                    dataType: 'json',
                    cache: false,
                    pagination: true,
                    sidePagination: 'server',
                    queryParams: function (params) {
                        return _.extend({},params, self.form);
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
                        field: 'changeNo',
                        title: '变更单号',
                        align: 'center'
                    }, {
                        field: 'contractCode',
                        title: '项目编号',
                        align: 'center'
                    }, {
                        field: 'name',
                        title: '客户姓名',
                        align: 'center'
                    }, {
                        field: 'mobile',
                        title: '客户电话',
                        align: 'center'
                    }, {
                        field: 'secondContact',
                        title: '第二联系人',
                        align: 'center'
                    }, {
                        field: 'secondContactMobile',
                        title: '第二联系人电话',
                        align: 'center'
                    }, {
                        field: 'createTime',
                        title: '变更时间',
                        align: 'center'
                    }, {
                        field: 'changeAmount',
                        title: '变更金额',
                        align: 'center'
                    }, {
                        field: 'printCount',
                        title: '打印次数',
                        align: 'center'
                    }, {
                        field: '',
                        title: '操作',
                        align: 'center',
                        formatter: function (value, row, index) {
                            var fragment = '';
                            fragment += '<button data-handle="print" data-id="' + row.id + '"  type="button" class="m-r-xs btn btn-xs btn-warning">打印</button>';
                            fragment += '<button data-handle="check" data-id="' + row.id + '"  type="button" class="m-r-xs btn btn-xs btn-primary">查看</button>';
                            return fragment;
                        }
                    }]
                });

                // 打印
                self.$dataTable.on('click', '[data-handle="print"]', function (e) {
                    var id = $(this).data("id");
                    var params = {
                        ids: id,
                        isPrint: true
                    }
                    var url = '/finance/changematerialprint/materialchangeprint';
                    DameiUtils.locationHrefToServer(url, params, true);
                    vueIndex.$dataTable.bootstrapTable('refresh');

                });
                // 查看
                self.$dataTable.on('click', '[data-handle="check"]', function (e) {
                    var id = $(this).data("id");
                    var params = {
                        ids: id,
                        isPrint: false
                    }
                    var url = '/finance/changematerialprint/materialchangeprint';
                    DameiUtils.locationHrefToServer(url, params, true);
                    vueIndex.$dataTable.bootstrapTable('refresh');

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
                var ids = self.pringIds;
                var params = {
                    ids: ids,
                    isPrint: true
                }
                console.log(params)
                var url = '/finance/changematerialprint/materialchangeprint';
                DameiUtils.locationHrefToServer(url, params, true);
                /*setTimeout(function () {
                    self.$dataTable.bootstrapTable('refresh');
                    self.pringIds = [];
                },2000);*/
            },
        }
    });





})();