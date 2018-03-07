var tt = null;
+(function () {
    //$('#orderMenu').addClass('active');
    $('#singleTag').addClass('active');
    tt = new Vue({
        el: '#container',
        data: {
            breadcrumbs: [{
                path: '/',
                name: '主页'
            },
                {
                    path: '/',
                    name: '串单管理',
                    active: true
                }],
            form: {
                keyword: '',
            },
            singleTag: {
                tagName:'',
                operator:'',
                operateTime:'',
                describtion:'',
                itemCount:''
            },
            $dataTable: null,
            modalModel: null,
            _$el: null,
            _$dataTable: null,
            id: null

        },
        created: function () {
        },
        ready: function () {
            this.drawTable();
        },
        methods: {
            query: function () {
                this.$dataTable.bootstrapTable('selectPage', 1);
            },
            drawTable: function () {
                var self = this;
                self.$dataTable = $('#dataTable', self._$el).bootstrapTable({
                    url: '/material/singletag/list',
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
                        /*{
                            title: '序号',//标题  可不加 
                            align: 'center',
                            formatter: function (value, row, index) {
                                return index+1;
                            }
                        },*/
                        {
                            field: 'tagName',
                            title: '串单名称',
                            align: 'center'
                        },
                        {
                            field: 'itemCount',
                            title: '项目数量',
                            align: 'center'
                        },
                        {
                            field: 'operator',
                            title: '最近操作人',
                            align: 'center'
                        },
                        {
                            field: 'operateTime',
                            title: '最近操作时间',
                            align: 'center'
                        },
                        {
                            field: 'describtion',
                            title: '描述',
                            align: 'center'
                        },
                        {
                            field: 'id',
                            title: '操作',
                            align: 'center',
                            formatter: function (value, row) {
                                var fragment = '';

                                fragment += ('<button   data-handle="edit" data-id="' + row.id + '" type="button" class="btn btn-xs btn-primary">编辑</button>&nbsp');

                                fragment += ('<button   data-handle="close" data-id="' + row.id + '" type="button" class="btn btn-xs btn-danger">项目管理</button>&nbsp');

                                return fragment;
                            }
                        }
                    ]
                });
                //编辑
                self.$dataTable.on('click', '[data-handle="edit"]',
                    function (e) {
                        var id = $(this).data("id");
                        createOrEditModal(id,true);
                        e.stopPropagation();
                    }
                );

                // 项目管理
                self.$dataTable.on('click', '[data-handle="close"]', function (e) {
                    var id = $(this).data('id');
                    window.location.href = ctx + '/singletag/projectManage?id=' + id;
                    e.stopPropagation();
                });
            },
            createBtnClickHandler: function (e) {
                //新增
                createOrEditModal(null, false);
            }

        }
    });

    function createOrEditModal(id, isEdit) {

        var _modal = $('#contractModal').clone();
        var $el = _modal.modal({
            height: 200,
            width: 600,
        });
        // 获取 node
        var el = $el.get(0);
        isEdit = !!isEdit;
        var userVue = new Vue({
            el: el,
            $modal: $el,
            created: function () {

            },
            ready: function () {
                this.findSingleTag();
            },
            data: {
                singleTag: {
                    tagName:'',
                    describtion:''
                }
            },
            methods: {
                //回显
                findSingleTag: function () {
                    if(id==undefined || id=='undefined'){
                    }else {
                        var self = this;
                        self.$http.get('/material/singletag/'+id+'/getsingletagbyid').then(function (res) {
                            if (res.data.code == 1) {
                                self.singleTag = res.data.data;
                            }
                        }).catch(function () {

                        }).finally(function () {

                        });
                    }
                },
                insert: function () {
                    var self = this;
                    self.$validate(true,
                        function () {
                            if (self.$validation.valid) {
                                self.submitting = true;

                                self.$http.post('/material/singletag/saveorupdate', self.singleTag).then(function (res) {
                                        if (res.data.code === '1') {
                                            Vue.toastr.success(res.data.message);
                                            $el.modal('hide');
                                            tt.$dataTable.bootstrapTable('refresh');
                                            self.$destroy();
                                        }
                                    },
                                    function (error) {
                                        Vue.toastr.error(error.responseText);
                                    }).catch(function () {
                                }).finally(function () {
                                    self.submitting = false;
                                });
                            }
                        })
                }
            },
        });
        // 创建的Vue对象应该被返回
        return userVue;
    }
})();