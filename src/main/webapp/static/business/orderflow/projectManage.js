var tt = null;
+(function () {
    $('#orderMenu').addClass('active');
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
                    name: '串单项目管理',
                    active: true
                }],
            form: {
                keyword: '',
            },
            projectManageList: null,
            projectManage: null
        },
        created: function () {
            this.findProjectManage();
        },
        ready: function () {
        },
        filters: {
            goType: function (val) {
                if (val == "STAY_TURN_DETERMINE") {
                    return "待转大定";
                } else if(val == "SUPERVISOR_STAY_ASSIGNED"){
                    return '督导组长待分配';
                } else if(val == "DESIGN_STAY_ASSIGNED"){
                    return '设计待分配';
                } else if(val == "APPLY_REFUND"){
                    return '申请退回';
                } else if(val == "STAY_DESIGN"){
                    return '待设计';
                } else if(val == "STAY_SIGN"){
                    return '待签约';
                } else if(val == "STAY_SEND_SINGLE_AGAIN"){
                    return '待重新派单';
                } else if(val == "STAY_CONSTRUCTION"){
                    return '待施工';
                } else if(val == "ON_CONSTRUCTION"){
                    return '施工中';
                } else if(val == "PROJECT_COMPLETE"){
                    return '竣工';
                } else if(val == "ORDER_CLOSE"){
                    return '订单关闭    ';
                }
            },
            goDate: function (el) {
                if (el == null) {
                    return '-';
                } else {
                    return moment(el).format('YYYY-MM-DD');
                }
            }
        },
        methods: {
            //根据id查询
            findProjectManage: function () {
                var self = this;
                var id = this.$parseQueryString()['id'];
                //var contractCode = self.form.keyword;
                self.$http.get('/material/singletag/getprojectmanagebyid?id=' + id).then(function (res) {
                    if (res.data.code == 1) {
                        self.projectManageList = res.data.data;
                    }
                });
            },
            //根据项目编号查询
            getProjectManage: function () {
                var self = this;
                var contractCode = self.form.keyword;
                //var id = this.$parseQueryString()['id'];
                self.$http.get('/material/singletag/getprojectmanagebycode?contractCode=' + contractCode).then(function (res) {
                    if (res.data.code == 1) {
                        self.projectManage = res.data.data;
                    }
                });
            },
            //移除
            remove: function (contractCode) {
                var self = this;
                self.$http.get('/material/singletag/remove?contractCode=' + contractCode).then(function (res) {
                    if (res.data.code == 1) {
                        this.$toastr.success(res.data.data);
                        this.findProjectManage();
                    }else{
                        this.$toastr.error(res.data.data);
                    }
                });
            },
            //串单
            singleString: function (contractCode) {
                var self = this;
                var id = this.$parseQueryString()['id'];
                self.$http.get('/material/singletag/singlestring?contractCode=' + contractCode +'&id=' + id).then(function (res) {
                    if (res.data.code == 1) {
                        this.$toastr.success(res.data.data);
                        //this.findProjectManage();
                        history.go(0);
                    }else{
                        this.$toastr.error(res.data.data);
                    }
                });
            }
        }
    });
})();