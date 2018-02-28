var PaymentPlan = Vue.extend({
    template: '#paymentPlanTmpl',
    data: function () {
        return {
            contractCode: null,
            paymentPlan: null
        }

    },
    ready: function () {
    },
    created: function () {
    },
    filters: {
        goDate: function (el) {
            if (el == null) {
                return '-';
            } else {
                return moment(el).format('YYYY-MM-DD HH:mm:ss');
            }
        },
        goType: function (val) {
            switch (val) {
                case 1 :
                    return '已完成';
                    break;
                case 0 :
                    return '未完成';
                    break;
            }
        }
    },
    methods: {
        fetchData: function (contractUuid) {
            var self = this;
            self.$http.get('/finance/project/paymentplandetail?contractUuid=' + contractUuid).then(function (res) {
                if (res.data.code == 1) {
                    self.paymentPlan = res.data.data;
                }
            })
        }
    }
});