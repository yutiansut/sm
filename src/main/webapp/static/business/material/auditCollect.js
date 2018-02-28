var tt = null;
+(function () {
    $('#changeMange').addClass('active');
    $('#changeAuditCollect').addClass('active');
    tt = new Vue({
        el: '#container',
        //mixins: [RocoVueMixins.DataTableMixin],
        data: {
            breadcrumbs: [{
                path: '/',
                name: '主页'
            },
                {
                    path: '/',
                    name: '审计汇总',
                    active: true
                }],
            form: {
                startDate: '',
                endDate:''
            },
            auditCollectInfo: {
                auditUser: '',
                auditCount: '',
                startDate: '',
                endDate:''
            },
            $dataTable: null,
            modalModel: null,
            _$el: null,
            _$dataTable: null,
            id: null,
            flag: ''
        },
        created: function () {
        },
        ready: function () {
            this.drawTable();
            this.activeDatepiker();
        },
        methods: {
            activeDatepiker: function () {
                var self = this;
                $(self.$els.startDate).datetimepicker('setStartDate', '');
                $(self.$els.endDate).datetimepicker('setStartDate', '');
            },
            query: function () {
                this.$dataTable.bootstrapTable('selectPage', 1);
                this.$dataTable.bootstrapTable('refresh');
            },
            drawTable: function () {
                var self = this;
                self.$dataTable = $('#dataTable', self._$el).bootstrapTable({
                    url: '/contract/contractchange/auditcollect',
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
                            field: 'auditUser',
                            title: '审计员',
                            align: 'center'
                        },
                        {
                            field: 'auditCount',
                            title: '审核单数',
                            align: 'center'
                        },
                        {
                            field: 'id',
                            title: '操作',
                            align: 'center',
                            formatter: function (value, row) {
                                var button = '';
                                button = ('<button   data-handle="auditCollectDetails" data-audituser="' + row.auditUser + '" data-startdate="' + row.startDate + '" data-enddate="' + row.endDate + '" type="button" class="btn btn-xs btn-primary">查看</button>&nbsp');
                                return button;
                            }
                        }
                    ]
                });


                //查看详情
                self.$dataTable.on('click', '[data-handle="auditCollectDetails"]',
                    function (e) {
                        var auditUser = $(this).data('audituser');
                        var startDate = $(this).data('startdate');
                        var endDate = $(this).data('enddate');
                        self.$http.get('/contract/contractchange/findauditchangeorderinfo?auditUser='
                            + auditUser + '&startDate=' + startDate + '&endDate=' + endDate).then(function (res) {
                            detailModal(res.data.data);
                        }).catch(function () {
                        }).finally(function () {
                        });
                        e.stopPropagation();
                    }
                );
            }
        }
    });


    function detailModal(auditData){
        var $modal = $('#detailModal').clone();
        $modal.modal({
            height: 600,
            width: 1000,
            backdrop: 'static',
        });

        $modal.on('shown.bs.modal',
            function () {
                var vueModal = new Vue({
                    el: $modal.get(0),
                    data: {
                        auditData : auditData
                    },
                    created: function () {
                    },
                    ready: function () {
                    },
                    filters: {
                        goDate: function (el) {
                            if (el == null) {
                                return '-';
                            } else {
                                return moment(el).format('YYYY-MM-DD HH:mm:ss');
                            }
                        }
                    },
                    methods: {

                    }
                })
            });
    }
})();