
var PaymentInfo = Vue.extend({
    template: '#paymentInfoTmpl',
    data: function () {
        return {
            contractUuid: null,
            paymentInfo: null,
            ableFinishPayid: '',//this.ableFinishPayid
            ableRcwPayid: '',
            flag: ''
        }

    },
    ready: function () {
    },
    created: function () {
        this.flag = MdniUtils.parseQueryString()['flag'];
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
        //切换刷新项目信息
        findContractPaymentData: function () {
            var self = this;
            self.$http.get("/finance/project/projectinformation", {
                params: {
                    contractUuid: detail.contractUuid
                }
            }).then(function (res) {
                if (res.data.code == 1 && res.data.data != null) {
                    detail.contractPayment = res.data.data;
                    this.ableFinishPayid = detail.contractPayment.ableFinishPayid;
                    this.ableRcwPayid = detail.contractPayment.ableRcwPayid;
                }
            });
        },
        //刷新项目信息
        queryContractPaymentData: function () {
            var self = this;
            self.$http.get("/finance/project/projectinformation", {
                params: {
                    contractUuid: detail.contractUuid
                }
            }).then(function (res) {
                if (res.data.code == 1 && res.data.data != null) {
                    detail.contractPayment = res.data.data;
                    this.ableFinishPayid = detail.contractPayment.ableFinishPayid;
                    this.ableRcwPayid = detail.contractPayment.ableRcwPayid;
                    detail.refresh(0);
                }
            });
        },
        //查询金额
        fetchFinaProjectAccount: function () {
            var self = this;
            self.$http.get("/finance/project/getfinaprojectaccountbyuuid", {
                params: {
                    contractUuid: self.contractUuid
                }
            }).then(function (res) {
                if (res.data.code == 1 && res.data.data != null) {
                    detail.finaProjectAccount = res.data.data;
                }
            });
        },
        fetchData: function (contractUuid) {
            var self = this;
            self.contractUuid = contractUuid;
            self.$http.get('/finance/project/paymentinformationinquiry?contractUuid=' + contractUuid).then(function (res) {
                if (res.data.code == 1) {
                    this.findContractPaymentData();
                    /*this.ableFinishPayid = detail.contractPayment.ableFinishPayid;
                    this.ableRcwPayid = detail.contractPayment.ableRcwPayid;*/
                    self.paymentInfo = res.data.data;
                }
            })
        },
        receivables: function () {
            showDepositModal();
        },
        redPunch: function (payId) {
            var self = this;
            swal({
                    title: '红冲数据',
                    text: '确定红冲吗？',
                    type: 'info',
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    showCancelButton: true,
                    showConfirmButton: true,
                    showLoaderOnConfirm: true,
                    confirmButtonColor: '#ed5565',
                    closeOnConfirm: false
                },
                function () {
                    self.$http.get('/finance/payment/redpunch?payId=' + payId).then(function (res) {
                        if (res.data.code == 1) {
                            Vue.toastr.success(res.data.message);
                            this.queryContractPaymentData(self.contractUuid);
                            this.fetchFinaProjectAccount();
                        } else {
                            Vue.toastr.error(res.data.message);
                        }

                    }).catch(function () {

                    }).finally(function () {
                        swal.close();
                    });
                });
        },
        endReceipt: function (payId) {
            var self = this;
            self.$http.get('/finance/payment/endreceipt?payId=' + payId).then(function (res) {
                if (res.data.code == 1) {
                    Vue.toastr.success(res.data.message);
                    this.queryContractPaymentData(self.contractUuid);
                    this.fetchFinaProjectAccount();
                } else {
                    Vue.toastr.error(res.data.message);
                }
            })
        },
        printed: function (id) {
            var params = {
                id: id
            };
            var url = '/finance/print/singleprint';
            MdniUtils.locationHrefToServer(url, params, true);
        }
    }
});

