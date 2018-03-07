//其它金额增减组件js
var otherMoneyComponent = Vue.extend({
    template: '#otherMoneyItemTmpl',
    data: function () {
        return {
            contractCode: DameiUtils.parseQueryStringDecode()['contractCode'],
            //项目主材集合
            otherMoneyList: [],
            //sku总数(标题后面的数字)
            skuSum: 0,
            //页面类型: 选材(select) 或者 变更(change) 或者 其它
            pageType: DameiUtils.parseQueryStringDecode()['pageType'] || '',
            //被打回的一级分类url
            catalogUrl: DameiUtils.parseQueryStringDecode()['catalogUrl'] || '',
            //背景色对象
            backColorObj:{
                //变更
                changeBackColor: 'change-back-color',
                //不可操作背景色
                turnBackColor: 'turn-back-color'
            }
        };

    },
    ready: function () {
    },
    created: function () {
    },
    methods: {
        //获取其它金额增减
        findOtherMoney: function () {
            var self = this;
            self.$http.get("/material/otheraddreduceamount/findlistbycontractcode?contractCode=" + self.contractCode + '&pageType=' + self.pageType).then(function(response) {
                var res = response.data;
                if (res.code == '1') {
                    self.otherMoneyList = res.data;
                    //处理加减号
                    self.otherMoneyList.forEach(function (otherMoney) {
                        if(otherMoney.addReduceType == '1'){
                            otherMoney.addReduceType = "+ ";
                        }else if(otherMoney.addReduceType == '0'){
                            otherMoney.addReduceType = "- ";
                        }
                    });
                    //计算数量
                    self.skuSum = self.otherMoneyList.length;
                    materialIndex.replaceTitleName(4, self.skuSum);
                }else{
                    self.$toastr.error("查询其它金额增减失败!")
                }
            });
        },
        //移除--其它金额增减表
        removeOtherMoney: function (otherMoney) {
            var self = this;
            swal({
                title: '确定移除该商品?',
                text: '移除该商品,并将移除该商品下所有用量!',
                type: 'info',
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                showCancelButton: true,
                showConfirmButton: true,
                showLoaderOnConfirm: true,
                confirmButtonColor: '#ed5565',
                closeOnConfirm: false
            }, function () {
                self.$http.get("/material/otheraddreduceamount/" + otherMoney.id + "/del").then(function (response) {
                    var res = response.data;
                    if (res.code == '1') {
                        //页面移除
                        self.otherMoneyList.remove(otherMoney);
                        //重新计算数量
                        self.skuSum --;
                        materialIndex.replaceTitleName(4, self.skuSum);
                        materialIndex.totalAmountFlag=true;
                        self.$toastr.success("移除其它金额增减成功!")
                    } else {
                        self.$toastr.error("移除其它金额增减失败!")
                    }
                }).catch(function () {
                    self.$toastr.error('移除其它金额增减失败!');
                }).finally(function () {
                    swal.close();
                });
            });
        },
        //添加--其它金额增减表
        addOtherMoneyModel: function () {
            var self = this;
            showOtherFeeModel( self.otherMoneyList,self.contractCode);
        }
    }
});