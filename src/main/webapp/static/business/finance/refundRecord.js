var RefundRecord = Vue.extend({
    template: '#refundRecordTmpl',
    data: function () {
        return {
            contractCode: null,
            refundRecord: null,
            flag: ''
        }

    },
    ready: function () {
    },
    created: function () {
        this.flag = DameiUtils.parseQueryString()['flag'];
    },
    filters: {
        goDate: function (el) {
            if (el == null) {
                return '-';
            } else {
                return moment(el).format('YYYY-MM-DD');
            }
        },
        goType: function (val) {
            switch (val) {
                case 'COMMONREFUND' :
                    return '普通退款';
                    break;
                case 'ORDERCLOSEREFUND' :
                    return '退单退款';
                    break;
            }
        }
    },
    methods: {
        fetchData: function (contractUuid) {
            var self = this;
            self.$http.get('/finance/project/refundrecord?contractUuid=' + contractUuid).then(function (res) {
                if (res.data.code == 1) {
                    self.refundRecord = res.data.data;
                }
            }, function (res) {

            })
        },
        refundment: function () {
            showRefundmentModal();
        }
    }
});