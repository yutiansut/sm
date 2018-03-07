var tt = null;
+(function () {
    $('#financeManageMenu').addClass('active');
    $('#queryfinapayme').addClass('active');
    tt = new Vue({
        el: '#container',
        data: {
            // 面包屑
            breadcrumbs: [{
                path: '/',
                name: '主页'
            }, {
                path: '',
                name: '财务配置'
            }, {
                path: '/',
                name: '特殊配置',
                active: true // 激活面包屑的
            }],
            $dataTable: null,
            form: {
                keyword: '',
                status: '',
                parents: '',
                selectedItem: ''
            },
            //是否显示 新增/编辑/删除
            showBtn: true,
            num: 0
        },

        methods: {
            auto: function () {
                this.$dataTable.bootstrapTable('selectPage', 1);
            },
            fetchJsTree: function () {
                var self = this;
                var _$jstree = $('#jstree');
                var methodId = $("#methodId").val();
                this.$http.get('/finance/paymethod/querytree?id=' + methodId).then(function (res) {
                    if (res.data.code == 1) {
                        $.jstree.defaults.sort = function (obj, deep) {
                            return 1;
                        }
                        _$jstree.jstree({
                            core: {
                                multiple: false,
                                // 不加此项无法动态删除节点
                                check_callback: true,
                                data: res.data.data
                            },
                            types: {
                                default: {
                                    icon: 'glyphicon glyphicon-stop'
                                }
                            },
                            sort: function () {
                                var aaa = this.get_node(arguments[0]);
                                var bbb = this.get_node(arguments[1]);
                                return aaa.original.sort > bbb.original.sort ? 1 : -1;
                            },
                            plugins: ['sort', 'types', 'wholerow', 'changed']
                        });

                        //单击事件
                        _$jstree.bind('click.jstree', function (event) {
                            var ref = _$jstree.jstree(true);
                            var sel = ref.get_selected(true);
                            if (!sel.length) {
                                return;
                            }
                            var deleted = sel[0].original.deleted;
                            if (deleted == '1') {
                                //不显示新增/编辑/删除按钮
                                tt.showBtn = false;
                            } else if (deleted == '0') {
                                tt.showBtn = true;
                            }
                        });
                    }
                }).catch(function () {

                }).finally(function () {

                });
            },
            //新增自定义属性
            createBtnClickHandler: function (showBtn) {
                if (showBtn) {
                    //不打开弹出页面窗口
                    return;
                }
                var self = this;
                var _$jstree = $('#jstree');
                var dict = {
                    id: '',
                    code: '',          //编号
                    name: '',          //名称
                    methodId: '',      //支付方式ID
                    deleted: null,     //删除标示
                    sort: '',           //排序标示(暂时没用到)
                    attrAath: null,    //父节点集合
                    parentId: '',       //父节点
                    parentName: null,  //父节点名称
                    attrStatus: '',     //是否启用
                    costRate: '',       //手续费率
                    minCostfee: '',     //最低手续费金额
                    remark: null,       //备注
                    maxCostFee: ''      //最高手续费金额
                };

                var ref = _$jstree.jstree(true),
                    sel = ref.get_selected(true);
                // 未选择分类创建一级分类
                if (!sel.length) {
                    toastr.error("请选择节点");
                    return;
                } else {

                    //新增时,其父id就是当前的id
                    dict.parentId = sel[0].id;
                    //当时 顶级父类时,其sel[0].parent是 #
                    if (sel[0].parent != '#') {
                        //此时要显示父类名称
                        dict.parentName = sel[0].text;

                    } else {
                        //默认父id为1, 在根目录下增加
                        dict.parentId = 1;
                    }
                    var methodId = $("#methodId");
                    //var methodId = request.getParameter("methodId")
                    dict.methodId = methodId.val()
                    this.showModel(dict, true);
                }


            },
            //编辑
            editBtn: function (showBtn) {

                if (showBtn) {
                    //不打开弹出页面窗口
                    return;
                }
                var self = this;
                var _$jstree = $('#jstree');
                var ref = _$jstree.jstree(true),
                    sel = ref.get_selected(true);
                if (!sel.length) {
                    toastr.error("请选择节点");
                    return;
                } else {
                    self.$http.get('/finance/paymethod/getbyidtree?id=' + sel[0].id).then(function (res) {
                        if (res.data.code == 1) {
                            var dict = res.data.data;
                            this.showModel(dict, false);
                            //ref.rename_node(sel,data.text);
                        }
                    });
                }
            },
            //删除
            deleteBtn: function () {
                var self = this;
                var _$jstree = $('#jstree');
                var ref = _$jstree.jstree(true),
                    sel = ref.get_selected(true);
                if (!sel.length) {
                    toastr.error("请选择节点");
                    return;
                } else {
                    swal({
                        title: "你确定删除该条数据吗?",
                        text: "警告:删除后不可恢复！",
                        type: "warning",
                        showCancelButton: true,
                        confirmButtonColor: "#DD6B55",
                        confirmButtonText: "确定",
                        cancelButtonText: "取消",
                        closeOnConfirm: false
                    }, function (isConfirm) {
                        if (isConfirm) {
                            self.$http.get('/finance/paymethod/deletebyid?id=' + sel[0].id + "&name=" + sel[0].text).then(function (res) {
                                if (res.data.code == 1) {
                                    swal("操作成功!", "", "success");
                                    //注释的部分是删除css样式 效果编辑和新增不能点击
                                    /* ref.rename_node(sel, "<font color='red'>" + sel[0].original.text + "(已删除)</font>");
                                     self.showBtn = false;
                                     /sel[0].original.deleted = '1';*/
                                    window.location.href = window.location.href;
                                    self.$toastr.success('删除自定义属性成功!');

                                } else {
                                    self.$toastr.error(res.data.message);
                                }
                            }).catch(function () {
                                swal("操作失败！", "", "error");
                            }).finally(function () {
                                swal.close();
                            });
                        }
                    });
                }
            },

            showModel: function (dict, isEdit) {

                var self = this;
                var _$modal = $('#modal').clone();
                var $modal = _$modal.modal({
                    height: 600,
                    maxHeight: 450,
                    backdrop: 'static',
                    keyboard: false
                });
                modal($modal, dict, isEdit, function (data) {
                    var _$jstree = $('#jstree');

                    var ref = _$jstree.jstree(true),
                        sel = ref.get_selected(true);
                    if (dict.id != '') {
                        //ref.refresh() ;
                        sel[0].original.sort = dict.sort;
                        ref.rename_node(sel, data.text);
                    } else {
                        ref.create_node(dict.parentId, data);
                    }

                });
            }
        },
        created: function () {
            this.fUser = window.DameiUser;
        },
        ready: function () {
            this.fetchJsTree();
        }
    });

    // 实现弹窗方法
    function modal($el, model, isEdit, callback) {
        // 获取 node
        var el = $el.get(0);

        // 创建 Vue 对象编译节点
        var vueModal = new Vue({
            el: el,
            // 模式窗体必须引用 ModalMixin
            validators: {
                validAppName: function (val) {
                    p
                    if (_.trim(val) === '') {
                        return true;
                    }
                    return /^[A-Za-z0-9_-]+$/.test(val);
                }
            },
            $modal: $el, //模式窗体 jQuery 对象
            data: {
                //控制 按钮是否可点击
                disabled: false,
                //模型复制给对应的key
                parenId: '',
                parents: '',
                dict: model,
                isEdit: isEdit,
                submitBtnClick: false,
                methodStatus: null
            },
            created: function () {

            },
            ready: function () {
                this.fetchMethodStatus();
            },
            validators: {
                num: {
                    message: '请输入正确的金额格式，例（10或10.00..）',
                    check: function (val) {
                        // return /^[+]{0,1}(\d+)$|^[+]{0,1}(\d+\.\d+)$/.test(val);
                        // return /^0|[0-9]+(.[0-9]{1,2})?$/.test(val);
                        return /^([1-9]\d{0,9}|0)([.]?|(\.\d{1,9})?)$/.test(val);
                    }
                }
            },
            methods: {

                //获取类型和状态的ajax
                fetchMethodStatus: function () {
                    var self = this;

                    self.$http.get('/finance/paymethod/getattrstatus').then(function (res) {
                        if (res.data.code == 1) {
                            self.methodStatus = res.data.data;
                        }
                    }).catch(function () {
                    }).finally(function () {
                    })

                },
                //插入特殊配置方法
                submit: function () {
                    var self = this;
                    self.submitBtnClick = true;
                    self.$validate(true, function () {
                        if (self.$validation.valid) {
                            self.disabled = true;
                            self.$http.post('/finance/paymethod/saveorupdate', self.dict).then(function (res) {
                                if (res.data.code == 1) {
                                    $el.on('hidden.bs.modal', function () {
                                        if (isEdit) {
                                            self.$toastr.success('新增自定义名称成功!');
                                        } else {
                                            self.$toastr.success('编辑自定义名称成功!');
                                        }
                                        callback(res.data.data);
                                    });
                                    $el.modal('hide');
                                } else {
                                    self.$toastr.error(res.data.message);
                                }
                            }).catch(function () {
                                swal(res.data.message, "", "error");
                            }).finally(function () {
                                self.disabled = false;
                            });
                        }
                    });
                }
            }
        });
        // 创建的Vue对象应该被返回
        return vueModal;
    }
})();