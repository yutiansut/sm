
+(function () {
    $('#regulationMenu').addClass('active');
    $('#commonList').addClass('active');
    var vueIndex = new Vue({
        el: '#container',
        data: {
            domainStatus:null,
            breadcrumbs: [{
                path: '/',
                name: '功能区管理'
            }, {
                path: '/',
                name: '功能区添加 / 编辑',
                active: true
            }],
            // 查询表单
            form: {
                keyword:"",
                domainName:"",
                includeDomainType:""
            },
            $dataTable: null,
            showDomainInfoTree: false
        },
        methods: {
            query: function () {
                this.$dataTable.bootstrapTable('selectPage', 1);
            },

            //查询
            drawTable: function () {
                var self = this;
                self.$dataTable = $(this.$els.dataTable).bootstrapTable({
                    url: '/domaininfo/findAll',
                    method: 'get',
                    dataType: 'json',
                    cache: false, //去缓存
                    pagination: true, //是否分页
                    sidePagination: 'server', //服务端分页
                    queryParams: function (params) {
                        return _.extend({}, params,self.form);
                    },
                    mobileResponsive: true,
                    undefinedText: '-', //空数据的默认显示字符
                    striped: true, //斑马线
                    maintainSelected: true, //维护checkbox选项
                    sortName: 'id', //默认排序列名
                    sortOrder: 'desc', //默认排序方式
                    columns: [{
                        field: 'id',
                        visible: false
                    },{
                        field: 'domainName',
                        title: '功能区名称',
                        align: 'center'
                    }, {
                        field: 'includeDomainType',
                        title: '包涵功能区类型',
                        align: 'center'
                    },{
                        field: 'domainStatus',
                        title: '状态',
                        align: 'center',
                        formatter: function(value) {
                             if(value == 1){
                                return '开启'
                             }else{
                                return '关闭'
                             }
                         }
                    },{
                        field: '', //将id作为排序时会和将id作为操作field内容冲突，导致无法排序
                        title: "操作",
                        align: 'center',
                        formatter: function (value, row, index) {
                            var fragment = '';
                            if( row.domainStatus == 1 ){
                                fragment += ('<button data-handle="operate-edit" data-id="' + row.id + '" type="button" class="m-r-xs btn btn-xs btn-default">修改</button>');
                            }else {
                                fragment += ('<button data-handle="operate-open" data-id="' + row.id + '" type="button" class="m-r-xs btn btn-xs btn-default">开启</button>');
                            }
                            if(row.domainStatus != 1){
                                fragment += ('<button data-handle="operate-delete" data-index="' + index + '" data-id="' + row.id + '" type="button" class="m-r-xs btn btn-xs btn-danger">删除</button>');
                            }else {
                                fragment += ('<button data-handle="operate-off" data-index="' + index + '" data-id="' + row.id + '" type="button" class="m-r-xs btn btn-xs btn-danger">关闭</button>');
                            }
                            return fragment;
                        }
                    }]
                });

              //  根据id获取单条信息
              self.$dataTable.on('click', '[data-handle="operate-edit"]',
                    function (e) {
                        var id = $(this).data("id");
                        self.$http.get('/domaininfo/getById?id=' + id).then(function (res) {
                            createOrUpdateModel(res.data.data, true);
                            e.stopPropagation();
                        });
                    }
              );

              // 删除
              self.$dataTable.on('click', '[data-handle="operate-delete"]', function (e) {
                    var id = $(this).data('id');
                    swal({
                        title: '删除',
                        text: '确定删除吗？',
                        type: 'info',
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        showCancelButton: true,
                        showConfirmButton: true,
                        showLoaderOnConfirm: true,
                        confirmButtonColor: '#ed5565',
                        closeOnConfirm: false
                    }, function () {
                        self.$http.get('/domaininfo/deleteById?id=' + id).then(function (res) {
                            if (res.data.code == 1) {
                                self.$dataTable.bootstrapTable('refresh');
                            }
                        }).catch(function () {

                        }).finally(function () {
                            swal.close();
                        });
                    });
                    e.stopPropagation();
                });

              //状态开启
                self.$dataTable.on('click', '[data-handle="operate-open"]',function (e) {
                    var id = $(this).data("id");
                    swal({
                        title: '开启',
                        text: '确定开启状态吗？',
                        type: 'info',
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        showCancelButton: true,
                        showConfirmButton: true,
                        showLoaderOnConfirm: true,
                        confirmButtonColor: '#ed5565',
                        closeOnConfirm: false
                    }, function () {
                        self.$http.get('/domaininfo/openById?id=' + id).then(function (res) {
                            if (res.data.code == 1) {
                                self.$dataTable.bootstrapTable('refresh');
                            }
                        }).catch(function () {

                        }).finally(function () {
                            swal.close();
                        });
                    });
                    e.stopPropagation();
                });

              // 状态关闭
              self.$dataTable.on('click', '[data-handle="operate-off"]',function (e) {
                  var id = $(this).data("id");
                  swal({
                      title: '关闭',
                      text: '确定关闭状态吗？',
                      type: 'info',
                      confirmButtonText: '确定',
                      cancelButtonText: '取消',
                      showCancelButton: true,
                      showConfirmButton: true,
                      showLoaderOnConfirm: true,
                      confirmButtonColor: '#ed5565',
                      closeOnConfirm: false
                  }, function () {
                      self.$http.get('/domaininfo/offById?id=' + id).then(function (res) {
                          if (res.data.code == 1) {
                              self.$dataTable.bootstrapTable('refresh');
                          }
                      }).catch(function () {

                      }).finally(function () {
                          swal.close();
                      });
                  });
                  e.stopPropagation();
              });

            },
            createBtnClickHandler:function (e) {
                createOrUpdateModel({},false);
                e.stopPropagation();
            }
        },
        created: function () {

        },
        ready: function () {
            this.drawTable();
        }
    });
    // --- 添加 --- 编辑
    function createOrUpdateModel(model, isEdit) {
        var _modal = $('#creatOrEditModel').clone();
        var $el = _modal.modal({
            height: 150,
            width: 500
        });
        $el.on('shown.bs.modal', function () {
            // 获取 node
            var el = $el.get(0);
            isEdit = !!isEdit;
            // 创建 Vue 对象编译节点
            var showDomainInfoVue = new Vue({
                el: el,
                // 模式窗体必须引用 ModalMixin
                mixins: [DameiVueMixins.ModalMixin],
                $modal: $el, // 模式窗体 jQuery 对象
                data: {
                    domaininfo:{
                        id:model.id,
                        domainName:model.domainName,
                        includeDomainType:model.includeDomainType || "",
                        domainStatus:model.domainStatus,
                        createTime:model.createTime,
                        createUser:model.createUser,
                        updateTime:model.updateTime,
                        updateUser:model.updateUser
                    },
                    submitBtnClick:false,
                    disabled :false,
                    _$el: null
                },
                methods: {
                    saveOrUpdate: function () {
                        var self = this;
                        self.submitBtnClick = true;
                        self.$validate(true, function () {
                            if (self.$validation.valid) {
                                self.disabled = true;
                                self.$http.post('/domaininfo/insertOrUpdate', self.domaininfo).then(function (res) {
                                    if (res.data.code == 1) {
                                        vueIndex.$dataTable.bootstrapTable('refresh');
                                        self.$toastr.success('保存成功');
                                        $el.modal('hide');
                                    } else {
                                        self.$toastr.error('您输入的类型已存在，请重新输入！');
                                    }
                                });
                            }else{
                                self.disabled = false;
                            }
                        });
                    }
                },
                created: function () {
                },
                ready: function () {
                }
            });
            // 创建的Vue对象应该被返回
            return showDomainInfoVue;
        });
    }
})();



