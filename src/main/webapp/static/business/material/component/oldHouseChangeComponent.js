//其他综合费组件js
var oldHouseChangeComponent = Vue.extend({
    template: '#oldHouseChangeTmpl',
    data: function () {
        return {
            //项目主材sku
            projectMaterial: [
                {
                    contractCode: '',
                    categoryCode: 'OLDHOUSEDEMOLITION',//旧房拆改工程--大类
                    categoryDetailCode: 'DISMANTLEBASEINSTALLQUOTA',//拆除基装定额--子类
                },{
                    contractCode: '',
                    categoryCode: 'OLDHOUSEDEMOLITION',//旧房拆改工程--大类
                    categoryDetailCode: 'DISMANTLEBASEINSTALLCOMPFEE',//拆除基装增项综合服务--子类
                },{
                    contractCode: '',
                    categoryCode: 'OLDHOUSEDEMOLITION',//旧房拆改工程--大类
                    categoryDetailCode: 'DISMANTLEOTHERCOMPFEE',//拆除其它综合服务--子类
                },
            ],
            //定额相关--大集合
            projectMaterialList: [],
            //拆除基装定额集合
            dismantleBaseinstallquotaList: [],
            //拆除基装增项综合服务集合
            dismantleBaseinstallCompFeeList: [],
            //拆除其它综合服务集合
            dismantleOtherCompFeeList: [],
            totalAmount: {
                //拆除基装定额总价
                baseloadrating3: 0,
                //拆除工程总价
                comprehensivefee4: 0
            },
            //页面类型: 选材(select) 或者 变更(change) 或者 其他
            pageType: MdniUtils.parseQueryStringDecode()['pageType'] || '',
        };

    },
    ready: function () {
    },
    created: function () {
        //合同code赋值
        var contractCode = MdniUtils.parseQueryStringDecode()['contractCode'];
        this.projectMaterial[0].contractCode = contractCode;
        this.projectMaterial[1].contractCode = contractCode;
        this.projectMaterial[2].contractCode = contractCode;
    },
    methods: {
        //添加定额model
        addQuotaModel: function () {
            var self = this;
            //注意索引要传递0
            showAddProjectIntem(self.projectMaterial[0],
                MdniUtils.formatDate(new Date(), 'yyyy-MM-dd hh:mm:ss'), 0, 'son');
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
            materialIndex.removeSku(projectMaterialList, projectMaterial);
        },
    },
    watch: {
        'dismantleBaseinstallquotaList': {
            handler: function (newVal, oldVal) {
                var self = this;
                //页面集合数据发生变化,去重新计算总价钱
                materialIndex.showTotalAmount(false);
            },
            deep: true//对象内部的属性监听，也叫深度监听
        },
        'dismantleBaseinstallCompFeeList': {
            handler: function (newVal, oldVal) {
                var self = this;
                //页面集合数据发生变化,去重新计算总价钱
                materialIndex.showTotalAmount(false);
            },
            deep: true//对象内部的属性监听，也叫深度监听
        },
        'dismantleOtherCompFeeList': {
            handler: function (newVal, oldVal) {
                var self = this;
                //页面集合数据发生变化,去重新计算总价钱
                materialIndex.showTotalAmount(false);
            },
            deep: true//对象内部的属性监听，也叫深度监听
        },
    }
});