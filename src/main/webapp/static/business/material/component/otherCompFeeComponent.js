//其他综合费组件js
var otherCompFeeComponent = Vue.extend({
    template: '#otherCompFeeTmpl',
    data: function () {
        return {
            //项目主材sku
            projectMaterial: {
                contractCode: MdniUtils.parseQueryStringDecode()['contractCode'],
                categoryCode: 'OTHERCOMPREHENSIVEFEE',//其他综合费--大类
                categoryDetailCode: 'OTHERCATEGORIESOFSMALLFEES',//其他综合费--子类
            },
            //sku总数(标题后面的数字)
            skuSum: 0,
            //定额相关--大集合
            projectMaterialList: [],
            //其他综合费集合
            otherComprehensiveFeeList: [],
            //sku总数--主材总数
            skuSum: 0,
            //定额部分总数
            quotaSum: 0,
            //总数--标题显示
            titleSum: 0,
            //统计金额对象
            totalAmount:{
                //工程总价
                renovationAmount: 0,
                //基装增项总价
                baseloadrating1: 0
            },
            //页面类型: 选材(select) 或者 变更(change) 或者 其他
            pageType: MdniUtils.parseQueryStringDecode()['pageType'] || '',
        };

    },
    ready: function () {
    },
    created: function () {
    },
    methods: {
        //添加定额model
        addQuotaModel: function () {
            var self = this;
            showAddProjectIntem(self.projectMaterial, MdniUtils.formatDate(new Date(), 'yyyy-MM-dd hh:mm:ss'), 5);
        },
        //修改定额用量
        updateDosage: function (skuDosage) {
            showDosageAmountModel("修改商品数量", skuDosage, "/material/smskudosage/save");
        },
        //修改备注
        updateRemark: function (name, updateRemark) {
            var url = '';
            if(this.pageType == 'select' || this.pageType == 'audit'){
                url = '/material/projectmaterial/save';
            }else if(this.pageType == 'change'){
                url = '/material/projectchangematerial/save';
            }
            showremarkModel(name, updateRemark, url);
        },
        //移除projectMaterial对象
        removeSku: function (projectMaterialList, projectMaterial) {
            var self = this;
            //调用主组件移除商品sku方法
            materialIndex.removeSku(projectMaterialList, projectMaterial, 5);
        },
    },
    watch: {
        'otherComprehensiveFeeList': {
            handler: function (newVal, oldVal) {
                var self = this;
                if(newVal && oldVal && newVal.length != oldVal.length){
                    //每次定额集合变更时,增加变化值
                    self.titleSum = self.titleSum + newVal.length - oldVal.length;
                }
                //页面集合数据发生变化,去重新计算总价钱
                materialIndex.showTotalAmount(false);
            },
            deep: true//对象内部的属性监听，也叫深度监听
        },
        'skuSum': function (newVal,oldVal) {
            var self = this;
            //每次定额集合变更时,增加变化值
            self.titleSum = self.titleSum + newVal - oldVal;
        },
        'titleSum': function (newVal,oldVal) {
            var self = this;
            //更改主组件的skuSum值的显示
            materialIndex.replaceTitleName(5, self.titleSum);
        },
    }
});