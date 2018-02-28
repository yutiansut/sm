+(function () {
    var vueIndex = new Vue({
        el: '#container',
        data: {
            breadcrumbs: [{
                path: '/',
                name: '主页'
            },
                {
                    path: '/business/changemange/changemange',
                    name: '变更管理'
                }, {
                    path: '',
                    name: '原始选材预览',
                    active: true
                }],
            $dataTable: null,
            _$el: null,
            _$dataTable: null,
            changeVersionNo:'',
            contractCode:'',

            catalogUrl: '',
            customerContract: '',
            prodCatalogList: [],
            topCatalogList: [],
            //套餐标配
            lowerCatalogList: [],
            //升级项
            upgradeCatalogList: [],
            upgradeTopCatalogList: [],
            upgradeLowerCatalogList: [],
            //增项
            addCatalogList: [],
            addTopCatalogList: [],
            addLowerCatalogList: [],
            //减项
            reduceCatalogList: [],
            reduceTopCatalogList: [],
            reduceLowerCatalogList: [],
            otherSum: 0,
            //增项主材
            addProjectMaterialList: [],
            //增项定额
            addBaseInstallQuotaList: [],
            //基装增项综合费 或者 拆除基装增项综合服务
            addBaseInstallComfeeList: [],
            //其他综合费 或者 拆除其它综合服务
            addOtherComprehensiveFeeList: [],
            //减项主材
            reduceProjectMaterialList: [],
            //基装定额 或者 拆除基装定额
            reduceBaseInstallQuotaList: [],
            //活动、优惠及其它金额增减项
            otherMoneyList: [],
            //老房拆改
            oldHouseProjectMaterialList: [],
            //老房拆改 基装定额 或者 拆除基装定额
            dismantleBaseinstallquotaList: [],
            //老房拆改 基装增项综合费 或者 拆除基装增项综合服务
            dismantleBaseinstallCompFeeList: [],
            //老房拆改 其他综合费 或者 拆除其它综合服务
            dismantleOtherCompFeeList: [],
            //其他综合 基装定额 或者 拆除基装定额
            otherComprehensiveFeeList: [],
            //其他综合
            otherComprehensiveFeeProjectList: [],
            amount: '',
            totalAmount: {
                totalBudget: 0,
                totalRenovationWorks: 0,
                oldhousedemolition: 0,
                packagestandardprice: 0,
                upgradeitemprice: 0,
                increment: 0,
                mainmaterial1: 0,
                comprehensivefee1: 0,
                subtraction: 0,
                mainmaterial2: 0,
                baseloadrating2: 0,
                otheramountsincreaseordecrease: 0,
                otherincrease: 0,
                otherminus: 0,
                othercomprehensivefee: 0,
                comprehensivefee3: 0,
                othercomprehensivefee3: 0,
                //工程总价
                renovationAmount: 0,
                //基装增项总价
                baseloadrating1: 0,
                //拆除基装定额总价
                baseloadrating3: 0,
                //拆除工程总价
                comprehensivefee4: 0,
                //面积
                area: 0,
                //价格
                price: 0,
            },
            hangCeiling: '',
            plasterLine: '',
            telWall: ''
        },
        created: function () {
            this.changeVersionNo = MdniUtils.parseQueryStringDecode()['changeVersionNo'];
            this.contractCode = MdniUtils.parseQueryStringDecode()['contractCode'];
            this.hangCeiling = MdniUtils.parseQueryStringDecode()['hangCeiling'];
            this.plasterLine = MdniUtils.parseQueryStringDecode()['plasterLine'];
            this.telWall = MdniUtils.parseQueryStringDecode()['telWall'];
        },
        ready: function () {
            this.findCustomerContract();
            this.fetchMealStandard();
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
                } else {
                    return '楼房平层';
                }
            }
        },
        methods: {
            //###############价格相关#################
            //获取价格
            findAmount: function () {
                var self = this;
                self.$http.get('/material/smskudosage/statisticsamount/' + self.contractCode + '/' + 'select').then(function (res) {
                    if (res.data.code == '1') {
                        self.amount = res.data.data;
                    }
                }).catch(function (e) {
                });
            },
            //当前总价格
            getMealPrice: function () {
                var self = this;
                var mealPrice = self.amount.othercomprehensivefee + self.amount.otheramountsincreaseordecrease - self.amount.subtraction + self.amount.increment + self.amount.upgradeitemprice + self.amount.packagestandardprice;
                return mealPrice;
            },
            //装修工程合计
            getProjectAmount: function () {
                var self = this;
                return self.getMealPrice() + self.amount.oldhousedemolition;
            },
            //计算总金额
            getTotalAmount: function () {
                var self = this;
                //查询金额
                var url = "/order/originaltotalamount?contractCode=" + self.contractCode;
                self.$http.get(url).then(function (res) {
                    if (res.data.code == '1') {
                        self.totalAmount = res.data.data;
                    } else {
                        self.$toastr.error("计算总金额失败!");
                    }
                })
            },
            //#############价格相关结束################

            //##############数量操作开始##############
            //套餐标配数量
            getProjectMaterialAmount: function () {
                var self = this;
                var sum = 0;
                self.lowerCatalogList.forEach(function (cat) {
                    sum += cat.projectMaterialList.length;
                });
                return sum;
            },
            //升级数量
            getUpgradeAmount: function () {
                var self = this;
                var sum = 0;
                self.upgradeLowerCatalogList.forEach(function (cat) {
                    sum += cat.projectMaterialList.length;
                });
                return sum;
            },
            //减项数量
            getReduceAmount: function () {
                var self = this;
                var sum = 0;
                self.reduceLowerCatalogList.forEach(function (cat) {
                    sum += cat.projectMaterialList.length;
                });
                return sum + self.reduceBaseInstallQuotaList.length;
            },
            //减项主材数量
            getReduceMaterialAmount: function () {
                var self = this;
                var sum = 0;
                self.reduceLowerCatalogList.forEach(function (cat) {
                    sum += cat.projectMaterialList.length;
                });
                return sum;
            },
            //增项数量
            getAddAmount: function () {
                var self = this;
                var sum = 0;
                self.addLowerCatalogList.forEach(function (cat) {
                    sum += cat.projectMaterialList.length;
                });
                return sum + self.addBaseInstallQuotaList.length +
                    self.addBaseInstallComfeeList.length;
            },
            //增项主材
            getAddMaterialAmount: function () {
                var self = this;
                var sum = 0;
                self.addLowerCatalogList.forEach(function (cat) {
                    sum += cat.projectMaterialList.length;
                });
                return sum;
            },
            //##############数量操作结束##############

            //###################查询操作开始#################
            //获取合同信息
            findCustomerContract: function () {
                var self = this;
                self.$http.get("/customercontract/contract/get/" + self.contractCode).then(function (res) {
                    if (res.data.code == 1) {
                        self.customerContract = res.data.data;
                    }
                })
            },
            //通过 contractCode 查询所有商品分类及对应主材和用量
            findCatalogWithMaterialByContractCode: function () {
                var self = this;
                var url = '/order/findmaterialbycodeandflag?contractCode='
                    + self.contractCode;
                self.$http.get(url).then(function (response) {
                    var res = response.data;
                    if (res.code == '1') {
                        //所有数据
                        var allCatalogList = res.data;
                        if(allCatalogList && allCatalogList.length > 0){
                            //遍历,存放到 套餐标配,升级项,增项,减项中的主材中
                            allCatalogList.forEach(function (catalog) {
                                var projectMaterialList = catalog.projectMaterialList;

                                if(projectMaterialList && projectMaterialList.length > 0){
                                    var packCatalogTemp = $.extend(true,{}, catalog);
                                    var upgradeCatalogTemp = $.extend(true,{}, catalog);
                                    var addCatalogTemp = $.extend(true,{}, catalog);
                                    var reduceCatalogTemp = $.extend(true,{}, catalog);

                                    var packageMaterialTempList = [];
                                    var upgradeMaterialTempList = [];
                                    var addMaterialTempList = [];
                                    var reduceMaterialTempList = [];

                                    projectMaterialList.forEach(function (material) {
                                        if(material.categoryCode == "PACKAGESTANDARD"){
                                            packageMaterialTempList.push(material);
                                        }else if(material.categoryCode == "UPGRADEITEM"){
                                            upgradeMaterialTempList.push(material);
                                        }else if(material.categoryCode == "ADDITEM" && material.categoryDetailCode == "MAINMATERIAL"){
                                            //增项 主材
                                            addMaterialTempList.push(material);
                                        }else if(material.categoryCode == "REDUCEITEM" && material.categoryDetailCode == "MAINMATERIAL"){
                                            //减项 主材
                                            reduceMaterialTempList.push(material);
                                        }
                                    });

                                    packCatalogTemp.projectMaterialList = packageMaterialTempList;
                                    self.prodCatalogList.push(packCatalogTemp);

                                    upgradeCatalogTemp.projectMaterialList = upgradeMaterialTempList;
                                    self.upgradeCatalogList.push(upgradeCatalogTemp);

                                    addCatalogTemp.projectMaterialList = addMaterialTempList;
                                    self.addCatalogList.push(addCatalogTemp);

                                    reduceCatalogTemp.projectMaterialList = reduceMaterialTempList;
                                    self.reduceCatalogList.push(reduceCatalogTemp);

                                }

                            });
                            //往二级分类中填充主材
                            self.dealCatalogAndMaterial(self.prodCatalogList, self.topCatalogList, self.lowerCatalogList);
                            //往二级分类中填充主材
                            self.dealCatalogAndMaterial(self.upgradeCatalogList, self.upgradeTopCatalogList, self.upgradeLowerCatalogList);
                            //往二级分类中填充主材
                            self.dealCatalogAndMaterial(self.addCatalogList, self.addTopCatalogList, self.addLowerCatalogList);
                            //往二级分类中填充主材
                            self.dealCatalogAndMaterial(self.reduceCatalogList, self.reduceTopCatalogList, self.reduceLowerCatalogList);
                        }
                    }else{
                        self.$toastr.error("获取主材数据失败!")
                    }
                });
            },
            //通过 contractCode 查询所有 定额 主材和用量!
            findMaterialWithSkuByContractCode: function () {
                var self = this;
                var url = '/material/projectmaterial/findmateriallist?contractCode='
                    + self.contractCode +'&pageType=select';
                self.$http.get(url).then(function(response) {
                    var res = response.data;
                    var materialQuataList = res.data;
                    if (res.code == '1') {
                        if(materialQuataList && materialQuataList.length > 0){
                            materialQuataList.forEach(function (material) {
                                if(material.categoryCode == "ADDITEM"){
                                    if(material.categoryDetailCode == "BASEINSTALLQUOTA"){
                                        //增项 基装定额
                                        self.addBaseInstallQuotaList.push(material);
                                    }else if(material.categoryDetailCode == "BASEINSTALLCOMPREHENSIVEFEE"){
                                        //增项 基装增项综合费
                                        self.addBaseInstallComfeeList.push(material);
                                    }
                                }else if(material.categoryCode == "REDUCEITEM" && material.categoryDetailCode == "BASEINSTALLQUOTA"){
                                    //减项 基装定额
                                    self.reduceBaseInstallQuotaList.push(material);
                                }else if(material.categoryCode == "OTHERCOMPREHENSIVEFEE" && material.categoryDetailCode == "OTHERCATEGORIESOFSMALLFEES"){
                                    //其他综合费 其他综合费
                                    self.otherComprehensiveFeeList.push(material);
                                }else if(material.categoryCode == "OLDHOUSEDEMOLITION"){
                                    if(material.categoryDetailCode == "DISMANTLEBASEINSTALLQUOTA"){
                                        //旧房拆改 拆除基装定额
                                        self.dismantleBaseinstallquotaList.push(material);
                                    }else if(material.categoryDetailCode == "DISMANTLEBASEINSTALLCOMPFEE"){
                                        //旧房拆改 拆除基装增项综合服务
                                        self.dismantleBaseinstallCompFeeList.push(material);
                                    }else if(material.categoryDetailCode == "DISMANTLEOTHERCOMPFEE"){
                                        //旧房拆改 拆除其它综合服务
                                        self.dismantleOtherCompFeeList.push(material);
                                    }
                                }
                            });
                        }
                    }
                });

            },
            //处理分类和主材数据关系
            dealCatalogAndMaterial: function (catalogList, topCatalogList, lowerCatalogList) {
                catalogList.forEach(function (cat) {
                    if (cat && cat.subCatalogList && cat.subCatalogList.length > 0) {
                        topCatalogList.push(cat);
                    } else {
                        if (cat.projectMaterialList != null && cat.projectMaterialList.length != 0) {
                            lowerCatalogList.push(cat);
                        }
                    }
                });
                this.dealSkuAmount(lowerCatalogList);
            },
            //活动、优惠及其它金额增减项
            findOtherMoney: function () {
                var self = this;
                var url = "/order/originaladdreduceamount?contractCode="
                    + self.contractCode;
                self.$http.get(url).then(function (response) {
                    var res = response.data;
                    if (res.code == '1') {
                        self.otherMoneyList = res.data;
                        //处理加减号
                        self.otherMoneyList.forEach(function (otherMoney) {
                            if (otherMoney.addReduceType == '1') {
                                otherMoney.addReduceType = "+ ";
                            } else if (otherMoney.addReduceType == '0') {
                                otherMoney.addReduceType = "- ";
                            }
                        });
                        //计算数量
                        self.otherSum = self.otherMoneyList.length;
                    } else {
                        self.$toastr.error("查询其他金额增减失败!")
                    }
                });
            },

            //计算sku预算用量合计/含损耗用量合计/总价--页面刷新时加载一次 参数3:定额类型,如果有,就不再直接计算总数
            dealSkuAmount: function (lowerCatalogList, activeIndex, quotaType) {
                var self = this;
                //定义变量
                var budgetDosageAmount = 0;
                var lossDosageAmount = 0;
                var skuSumMoney = 0;
                var skuSum = 0;
                lowerCatalogList.forEach(function (catalog) {
                    if (catalog && catalog.projectMaterialList && catalog.projectMaterialList.length > 0) {
                        catalog.projectMaterialList.forEach(function (projectMaterial) {
                            //计算sku总数
                            skuSum++;
                            //清空
                            budgetDosageAmount = 0;
                            lossDosageAmount = 0;
                            skuSumMoney = 0;
                            if (projectMaterial && projectMaterial.skuDosageList && projectMaterial.skuDosageList.length > 0) {
                                projectMaterial.skuDosageList.forEach(function (skuDosage) {
                                    if (skuDosage) {
                                        //计算
                                        budgetDosageAmount += skuDosage.budgetDosage || 0;
                                        lossDosageAmount += skuDosage.lossDosage || 0;
                                    }
                                });
                                //计算合计
                                skuSumMoney = (projectMaterial.storeSalePrice || 0) * lossDosageAmount;
                            }
                            //添加到属性中
                            Vue.set(projectMaterial, "budgetDosageAmount", budgetDosageAmount);
                            Vue.set(projectMaterial, "lossDosageAmount", lossDosageAmount);
                            Vue.set(projectMaterial, "skuSumMoney", skuSumMoney);

                        });
                    }
                });
            },
            //##############查询操作结束#########################
            fetchMealStandard: function () {
                this.findAmount();
                this.findCatalogWithMaterialByContractCode();
                this.findMaterialWithSkuByContractCode();
                this.findOtherMoney();
                this.getTotalAmount();
            }

        }
    });
})();