var ClaimRecord = Vue.extend({
    template: '#claimRecordTmpl',
    data: function () {
        return {
            contractCode: '',
            claimRecord: null,
            complete: '',
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
        getCompleted: function () {
            var self = this;
            self.$http.get("/projectSign/getByCode", {
                params: {
                    contractCode: detail.contractCode
                }
            }).then(function (res) {
                if (res.data.code == 1 && res.data.data != null) {
                    self.complete = res.data.data.complete;
                }
            });
        },
        fetchData: function (contractUuid) {
            var self = this;
            self.$http.get('/finance/project/claimrecord?contractUuid=' + contractUuid).then(function (res) {
                if (res.data.code == 1) {
                    self.claimRecord = res.data.data;
                    this.getCompleted();
                }
            }, function (res) {

            })
        },
        reparation: function () {
            showReparationModal();
        }
    }
});