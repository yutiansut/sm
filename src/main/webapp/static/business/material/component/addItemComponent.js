//增项组件js
var addItemComponent = Vue.extend({
    template: '#addItemTmpl',
    data: function () {
        return {
            //一级分类集合
            topCatalogList: [],
            //二级分类集合
            lowerCatalogList: [],
            //商品类型
            priceType: 'INCREASED',
            //项目主材sku
            projectMaterial: [
                    //主材对象
                    {
                        contractCode: '',
                        categoryCode: 'ADDITEM',//增项
                        categoryDetailCode: 'MAINMATERIAL',//主材
                    },
                    //基装定额对象
                    {
                        contractCode: '',
                        categoryCode: 'ADDITEM',//增项
                        categoryDetailCode: 'BASEINSTALLQUOTA',//基装定额
                    },
                    //基装定额对象
                    {
                        contractCode: '',
                        categoryCode: 'ADDITEM',//增项
                        categoryDetailCode: 'BASEINSTALLCOMPREHENSIVEFEE',//基装增项综合费
                    }
                ],
            //定额相关--大集合
            projectMaterialList: [],
            //基装定额集合
            baseInstallQuotaList: [],
            //基装增项综合费集合
            baseInstallComfeeList: [],
            //sku总数--主材总数
            skuSum: 0,
            //定额部分总数
            quotaSum: 0,
            //总数--标题显示
            titleSum: 0,
            //统计金额对象
            totalAmount:{
                /*//工程总价
                renovationAmount: 0,*/
                //基装增项总价
                baseloadrating1: 0
            },
            //页面类型: 选材(select) 或者 变更(change) 或者 其他
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
        //合同code赋值
        var contractCode = DameiUtils.parseQueryStringDecode()['contractCode'];
        this.projectMaterial[0].contractCode = contractCode;
        this.projectMaterial[1].contractCode = contractCode;
        this.projectMaterial[2].contractCode = contractCode;
    },
    methods: {
        // 处理一级/二级分类--主材
        dealCatalogList: function (firstLoadCatalog, catalogList) {
            var self = this;
            if(!catalogList){
                self.$toastr.error("获取商品分类信息失败!")
                return;
            }
            //每次处理时,都将二级分类清空
            self.lowerCatalogList = [];
            //只有第一次加载时, 才重新组装一级分类,清空
            if(firstLoadCatalog){
                self.topCatalogList = [];
            }
            catalogList.forEach(function (cat) {
                if(cat && cat.subCatalogList && cat.subCatalogList.length > 0){
                    //是一级分类
                    if(firstLoadCatalog){
                        //只有第一次加载活着点击全部时,才重新组装一级分类
                        self.topCatalogList.push(cat);
                    }
                }else{
                    //是二级分类
                    self.lowerCatalogList.push(cat);
                }
            });
            //计算sku用量
            materialIndex.dealSkuAmount(self.lowerCatalogList, 2, true);
        },
        //添加商品model
        addProdModel: function () {
            var self = this;
            //如果是被打回,那么只能添加该一级分类的url,锁定一级分类
            if(self.catalogUrl) {
                var catalog ={
                    parent:{
                        url: self.catalogUrl,
                    },
                    url: ''
                };
                showAddProdModel(self.projectMaterial[0], self.priceType, catalog,
                    DameiUtils.formatDate(new Date(), 'yyyy-MM-dd hh:mm:ss'), 2);
            }else{
                showAddProdModel(self.projectMaterial[0], self.priceType, null, DameiUtils.formatDate(new Date(), 'yyyy-MM-dd hh:mm:ss'), 2);
            }
        },
        //移除projectMaterial对象
        removeSku: function (projectMaterialList, projectMaterial, isQuota) {
            var self = this;
            //调用主组件移除商品sku方法
            materialIndex.removeSku(projectMaterialList, projectMaterial, 2, isQuota);
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
        //添加sku用量
        addSkuDosageModel: function (catalog, projectMaterial) {
            var self = this;
            showAddSkuDosageModel(self.priceType, catalog, projectMaterial, DameiUtils.formatDate(new Date(), 'yyyy-MM-dd hh:mm:ss'));
        },
        //删除sku用量
        removeSkuDosage: function (projectMaterial, skuDosageList, skuDosage) {
            materialIndex.removeSkuDosage(projectMaterial, skuDosageList, skuDosage);
        },
        //添加定额model
        addQuotaModel: function () {
            var self = this;
            showAddProjectIntem(self.projectMaterial[1], DameiUtils.formatDate(new Date(), 'yyyy-MM-dd hh:mm:ss'), 2);
        },
        //修改定额用量
        updateDosage: function (skuDosage) {
            showDosageAmountModel("修改商品数量", skuDosage, "/material/smskudosage/save");
        }
    },
    watch: {
        'baseInstallQuotaList': {
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
        'baseInstallComfeeList': function (newVal,oldVal) {
            var self = this;
            if(newVal && oldVal && newVal.length != oldVal.length){
                //每次定额集合变更时,增加变化值
                self.titleSum = self.titleSum + newVal.length - oldVal.length;
            }
        },
        'skuSum': function (newVal,oldVal) {
            var self = this;
            //每次定额集合变更时,增加变化值
            self.titleSum = self.titleSum + newVal - oldVal;
        },
        'titleSum': function (newVal,oldVal) {
            var self = this;
            //更改主组件的skuSum值的显示
            materialIndex.replaceTitleName(2, self.titleSum);
        },
        //监听二级分类的变化:如果是变更:
        // 1.那么就去统计其下所有sku的原预算用量和现用量(含损耗用量)总和,
        //  如果不相等,就视为发生了变更,给页面添加背景色
        //  2.如果其没有用量,也视为发生了变更
        //  3.如果二级分类url包含页面带过来的一级url,表示该一级分类被打回;其他表示没被打回,背景灰色,按钮不可操作
        'lowerCatalogList': function (newVal, oldval) {
            var self = this;
            if(self.pageType == 'change'){
                if(newVal && newVal.length > 0){
                    newVal.forEach(function (catalog) {
                        if(catalog.projectMaterialList && catalog.projectMaterialList.length > 0){
                            if(self.catalogUrl){
                                //处理被打回情况:
                                if(catalog.parent && catalog.parent.url == self.catalogUrl){
                                    //该一级类目被打回--跟变更一样,不做特殊处理
                                    materialIndex.dealSkuBackColorByCatalog(catalog);
                                }else{
                                    //没被打回,
                                    catalog.projectMaterialList.forEach(function (projectMaterial) {
                                        //该主材下没有用量
                                        Vue.set(projectMaterial, "backColorFlag", 'turnBack');
                                    });
                                }
                            }else{
                                //处理变更情况
                                materialIndex.dealSkuBackColorByCatalog(catalog);
                            }
                        }
                    });
                }
            }
        }

    }
});