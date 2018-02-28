var BackRecord = Vue.extend({
    template: '#backRecordTmpl',
    data: function () {
        return {
            backRecord: null,
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
        fetchData: function (contractUuid) {
            var self = this;
            self.$http.get('/finance/project/backrecord?contractUuid=' + contractUuid).then(function (res) {
                if (res.data.code == 1) {
                    self.backRecord = res.data.data;
                }
            }, function (res) {

            })
        },
        chargeback: function () {
            showChargebackModal();
        }
    }
});