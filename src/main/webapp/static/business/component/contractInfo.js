/**
 * Created by 巢帅 on 2017/8/3.
 */
var ContractInfo = Vue.extend({
    template: '#contractinfo',
    props: {
        contractCode: ''
    },
    data: function () {
        return {
            customerContract: '',
            //页面类型: 选材(select) 或者 变更(change) 或者 其他
            pageType: MdniUtils.parseQueryStringDecode()['pageType'] || '',
        };
    },
    created: function () {
        this.findCustomerContract();
    },
    filters: {
        goDate: function (el) {
            if (el) {
                return moment(el).format('YYYY-MM-DD');
            }
            else {
                return '-';
            }
        },
        goType: function (val) {
            if (val == '1') {
                return '有';
            } else {
                return '无';
            }
        },
        houseStatus: function (val) {
            if (val == '1') {
                return '新房';
            } else {
                return '旧房';
            }
        },
        houseType: function (val) {
            if (val == '1') {
                return '复式';
            } else if (val == '2') {
                return '别墅';
            }else{
                return '楼房平层';
            }
        }
    },
    methods: {
        //获取合同信息
        findCustomerContract: function () {
            var self = this;
            self.$http.get("/customercontract/contract/get/" + self.contractCode).then(function (res) {
                if (res.data.code == 1) {
                    self.customerContract = res.data.data;
                }
            })
        },
        modify:function () {
            var self = this;
            self.$http.get("/customercontract/contract/get/" + self.contractCode).then(function (res) {
                if (res.data.code == 1) {
                    var model = res.data.data;
                    modifyModal(model);
                }
            })
        }
    }
});
