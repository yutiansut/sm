//升级项组件js
var upgradeItemComponent = Vue.extend({
    template: '#upgradeItemTmpl',
    data: function () {
        return {
            //一级分类集合
            topCatalogList: [],
            //二级分类集合
            lowerCatalogList: [],
            //商品类型
            priceType: 'UPGRADE',
            //项目主材sku
            projectMaterial: {
                contractCode: DameiUtils.parseQueryStringDecode()['contractCode'],
                categoryCode: 'UPGRADEITEM',//升级项
                categoryDetailCode: 'UPGRADEITEM',//升级项
            },
            //sku总数(标题后面的数字)
            skuSum: 0,
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
    },
    methods: {
        // 处理一级/二级分类
        dealCatalogList: function (firstLoadCatalog, catalogList) {
            var self = this;
            if(!catalogList){
                self.$toastr.error("获取商品分类信息失败!");
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
            materialIndex.dealSkuAmount(self.lowerCatalogList, 1);
        },
        //添加商品model
        addProdModel: function () {
            var self = this;
            //如果是被打回,那么只能添加该一级分类的url,锁定一级分类
            if(self.catalogUrl){
                var catalog ={
                    parent:{
                        url: self.catalogUrl,
                    },
                    url: ''
                };
                showAddProdModel(self.projectMaterial, self.priceType, catalog,
                    DameiUtils.formatDate(new Date(), 'yyyy-MM-dd hh:mm:ss'), 1);
            }else{
                showAddProdModel(self.projectMaterial, self.priceType, null,
                    DameiUtils.formatDate(new Date(), 'yyyy-MM-dd hh:mm:ss'), 1);
            }
        },
        //移除projectMaterial对象
        removeSku: function (projectMaterialList, projectMaterial) {
            var self = this;
            //调用主组件移除商品sku方法
            materialIndex.removeSku(projectMaterialList, projectMaterial, 1);
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
    },
    watch: {
        //监听二级分类的变化:如果是变更:
        // 1.那么就去统计其下所有sku的原预算用量和现用量(含损耗用量)总和,
        //  如果不相等,就视为发生了变更,给页面添加背景色
        //  2.如果其没有用量,也视为发生了变更
        //  3.如果二级分类url包含页面带过来的一级url,表示该一级分类被打回;其他表示没被打回,背景灰色,按钮不可操作
        lowerCatalogList: function (newVal, oldval) {
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

    },
});