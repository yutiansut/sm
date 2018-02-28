var OperationRecord = Vue.extend({
    template: '#operationRecordTmpl',
    data: function () {
        return {
            contractCode: null,
            operationRecord: null
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
                case 'PAY_DEPOSIT' :
                    return '交定金';
                    break;
                case 'PAY_MODIFY' :
                    return '交拆改费';
                    break;
                case 'PAY_CONSTRUCT' :
                    return '交工程款';
                    break;
                case 'PAY_RCW' :
                    return '交款红冲';
                    break;
                case 'PAY_RETURN' :
                    return '退款';
                    break;
                case 'PAY_BACKRETURN' :
                    return '退单';
                    break;
                case 'STAGE_TRANSFORM' :
                    return '财务阶段流转';
                    break;
                case 'CREATE_CHANGE' :
                    return '新增变更';
                    break;
                case 'CREATE_REPARATION' :
                    return '新增赔款';
                    break;
                case 'CANCEL_REPARATION' :
                    return '撤销赔款';
                    break;
                case 'CANCEL_ORDER' :
                    return '退单退款';
                    break;
            }
        }
    },
    methods: {
        fetchData: function (contractUuid) {
            var self = this;
            self.$http.get('/finance/project/operationrecord?contractUuid=' + contractUuid).then(function (res) {
                if (res.data.code == 1) {
                    self.operationRecord = res.data.data;
                }
            }, function (res) {

            })
        }
    }
});