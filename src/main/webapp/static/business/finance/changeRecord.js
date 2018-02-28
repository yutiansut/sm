var ChangeRecord = Vue.extend({
    template: '#changeRecordTmpl',
    data: function () {
        return {
            contractCode: null,
            changeRecord: null,
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
        },
        goChangeType: function (val) {
            switch (val) {
                case 'SYSTEM' :
                    return '系统';
                    break;
                case 'MANUAL' :
                    return '手动';
                    break;
            }
        }
    },
    methods: {
        fetchData: function (contractUuid) {
            var self = this;
            self.$http.get('/finance/project/changelog?contractUuid=' + contractUuid).then(function (res) {
                if (res.data.code == 1) {
                    self.changeRecord = res.data.data;
                }
            }, function (res) {

            })
        },
        viewDetail: function (id,changeType) {
            var params = {
                ids: id,
                isPrint: false
            };
            if(changeType == 'BASIC'){
                var url = '/material/contractchange/vieworprint';
                MdniUtils.locationHrefToServer(url, params, true);
            }else{
                var url = '/finance/changematerialprint/materialchangeprint';
                MdniUtils.locationHrefToServer(url, params, true);
            }
        }
    }
});