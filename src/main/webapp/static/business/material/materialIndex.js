/**
 * @Description: 选材首页js
 *      1.引入其它子组件
 *      2.子组件公用方法,弹窗方法都放在此处
 *      3.合同信息的加载放于此处,并初始化加载其它子组件数据
 *      4.画外音: 注意各组件的数据加载时机!
 * @Company: 美得你智装科技有限公司
 * @Author: Paul
 */
var materialIndex;
+(function() {
        //$('#selectMaterialManage').addClass('active');
        //$('#material').addClass('active');
        materialIndex = new Vue({
            el: '#indexContainer',
            components: {
                'contract-info': ContractInfo,
                'package-standard-component': packageStandardComponent,
                'upgrade-item-component': upgradeItemComponent,
                'add-item-component': addItemComponent,
                'reduce-item-component': reduceItemComponent,
                'other-money-component': otherMoneyComponent,
                'other-comp-fee-component': otherCompFeeComponent,
                /*旧房拆改工程组件~~~注意:该组件不再是tabs下面,而是在 son 下面!!!!*/
                'old-house-change': oldHouseChangeComponent,
            },
            data: {
                // 面包屑
                breadcrumbs: [{
                    path: '/',
                    name: '主页'
                }, {
                    path: '/',
                    name: '选材管理',
                    active: true//激活面包屑的
                }],
                //引入的tab集合
                tabList:[],
                //合同加载完成标记
                loadSuccFlag: false,
                //客户合同对象
                customerContract: {
                    contractCode: '',
                },
                //商品分类集合
                prodCatalogList: null,
                //备注弹出框相关
                remarkModel:{
                    //title标题
                    name: '',
                },
                //6个tab页 选材类型 枚举
                selectMaterialTypeEnmu:
                    [{'name': '套餐标配', 'value': 'PACKAGESTANDARD'},
                        {'name': '升级项', 'value': 'UPGRADEITEM'},
                        {'name': '增项', 'value': 'ADDITEM'},
                        {'name': '减项', 'value': 'REDUCEITEM'},
                        {'name': '其它金额增减', 'value': 'OTHERMONEYADDORREDUCE'},
                        {'name': '其它综合费', 'value': 'OTHERCOMPREHENSIVEFEE'},
                        {'name': '旧房拆改', 'value': 'OLDHOUSEDEMOLITION'}
                    ],
                //当前活动中的tab页索引
                activeIndex: 0,
                //统计金额对象
                totalAmount:{
                    totalBudget:0,
                    totalRenovationWorks:0,
                    oldhousedemolition:0,
                    packagestandardprice:0,
                    upgradeitemprice:0,
                    increment:0,
                    mainmaterial1:0,
                    comprehensivefee1:0,
                    subtraction:0,
                    mainmaterial2:0,
                    baseloadrating2:0,
                    otheramountsincreaseordecrease:0,
                    otherincrease:0,
                    otherminus:0,
                    othercomprehensivefee:0,
                    comprehensivefee3:0,
                    othercomprehensivefee3:0,
                    //工程总价
                    renovationAmount: 0,
                    //基装增项总价
                    baseloadrating1:0,
                    //拆除基装定额总价
                    baseloadrating3:0,
                    //拆除工程总价
                    comprehensivefee4: 0,
                    //面积
                    area:0,
                    //价格
                    price:0,
                },
                totalAmountFlag:false,
                oldHouseactiveIndex: 0,
                //页面类型: 选材(select) 或者 变更(change) 或者 其它
                pageType: MdniUtils.parseQueryStringDecode()['pageType'] || '',
                //背景色对象
                backColorObj:{
                    //变更
                    changeBackColor: 'change-back-color',
                    //不可操作背景色
                    turnBackColor: 'turn-back-color'
                },
                //被打回的一级分类url
                //注意: 其他金额增减被打回时,传递 &catalogUrl=other !!!!
                catalogUrl: MdniUtils.parseQueryStringDecode()['catalogUrl'] || '',
                //装修工程tab页,加载标记,只有第一次点击时,全部加载
                decorationNeedLoadFlag: true,

            },
            created: function() {
                //获取合同
                if(!this.loadContract()){
                    return;
                }
                //页面类型变化后,改变页面中相应的变化
                this.changePage(this.pageType);
                this.loadTabs();
                //第一次 进入时 设置参数为可以加载
                this.totalAmountFlag = true;
            },
            ready: function() {
            },
            methods: {
                //页面类型变化后,改变页面中相应的变化
                changePage: function (pageType) {
                    var self = this;
                    if(!pageType){
                        self.$toastr.error("参数丢失,请重新刷新尝试!");
                        return ;
                    }
                    if(pageType == 'select'){
                        //选材时,页面显示
                        $('#selectMaterialManage').addClass('active');

                        self.breadcrumbs = [{
                            path: '/',
                            name: '主页'
                        },{
                            path: '',
                            name: '选材管理'
                        }, {
                            path: '/',
                            name: '选材',
                            active: true//激活面包屑的
                        }];
                    }else if(pageType == 'change'){
                        //变更时,页面显示
                        $('#changeMange').addClass('active');
                        $('#changeManage').addClass('active');

                        self.breadcrumbs = [{
                            path: '/',
                            name: '主页'
                        }, {
                            path: '',
                            name: '变更管理',
                        }, {
                            path: '/',
                            name: '变更',
                            active: true//激活面包屑的
                        }];
                    }else if(pageType == 'audit'){
                        //审计时,页面显示
                        $('#selectMaterialManage').addClass('active');

                        self.breadcrumbs = [{
                            path: '/',
                            name: '主页'
                        },{
                            path: '',
                            name: '选材管理',
                        },{
                            path: '/',
                            name: '审计',
                            active: true//激活面包屑的
                        }];
                    }
                },
                //展示金额统计
                showTotalAmount:function(isShow){
                    var self=this;
                    var contractCode = self.customerContract.contractCode;
                    //查询金额
                    var item = self.totalAmountFlag;
                    if(item){
                        var url = "/material/smskudosage/statisticsamount/"+contractCode+"/";

                        if(self.pageType == 'select' || self.pageType == 'audit'){
                            url += 'select';
                        }else if(self.pageType == 'change'){
                            url += 'change';
                        }
                        self.$http.get(url).then(function(res) {
                            if (res.data.code == '1') {
                                self.totalAmount = res.data.data;
                                self.totalAmountFlag = false;
                            }else{
                                self.$toastr.error("计算总金额失败!");
                            }
                        })
                    }
                    //是否展开
                    if(isShow){
                        //展示
                        $('.tool-div').addClass('_in');
                        $('.tool-div').fadeIn('slow');
                    }
                },
                //变更时,基装增项总价/工程总价 从这里读取
                countTotalMoneyForOtherCompFee: function () {
                    var self = this;
                    var contractCode = self.customerContract.contractCode;

                    //其它综合费,变更时,仍然展示选材时的金额
                    var url = "/material/proportionmoney/getbycontractcode/" + contractCode;
                    self.$http.get(url).then(function(res) {
                        if (res.data.code == '1') {
                            var data = res.data.data;
                            var totalAmount = {
                                //基装增项总价
                                baseloadrating1: data.baseloadrating1,
                                //工程总价
                                renovationAmount: data.renovationAmount,
                                //拆除基装定额总价
                                baseloadrating3: data.baseloadrating3,
                                //拆除工程总价
                                comprehensivefee4: data.comprehensivefee4
                            };
                            self.$refs.tabs.$children[2].$children[0].totalAmount = totalAmount;
                            self.$refs.tabs.$children[5].$children[0].totalAmount = totalAmount;
                            //旧房拆改组件
                            self.$refs.son.$children[0].totalAmount = totalAmount;
                            self.totalAmountFlag = false;
                        }else{
                            self.$toastr.error("计算其它综合费用总价失败!");
                        }
                    });
                },
                //隐藏金额统计
                hideTotalAmount:function () {
                    $('.tool-div').removeClass('_in');
                    $('.tool-div').fadeOut('slow');
                },
                loadContract: function(){
                    var self = this;
                    self.customerContract.contractCode = MdniUtils.parseQueryStringDecode()['contractCode'];
                    if(!self.customerContract.contractCode){
                        self.$toastr.error("参数丢失,请重新尝试!");
                        return false;
                    }
                    //通过code查询合同信息
                    this.getContractByCode(self.customerContract.contractCode);
                    return true;
                },
                //加载6个tab页
                loadTabs: function () {
                    var self = this;
                    self.tabList.push({label: '套餐标配(0)'});
                    self.tabList.push({label: '升级项(0)'});
                    self.tabList.push({label: '增项(0)'});
                    self.tabList.push({label: '减项(0)'});
                    self.tabList.push({label: '其它金额增减(0)'});
                    self.tabList.push({label: '其它综合费(0)'});
                },
                //通过code查询合同信息
                getContractByCode: function (contractCode) {
                    var self = this;
                    self.$http.get("/customercontract/contract/get/" + contractCode).then(function(response) {
                        var res = response.data;
                        if (res.code == '1') {
                            self.customerContract = res.data;
                            //告诉子组件,合同对象已经加载完成了
                            self.loadSuccFlag = true;

                        }else{
                            self.$toastr.error("查询合同失败!")
                        }
                    }).catch(function () {
                        self.$toastr.error("查询合同失败!")
                    });
                },
                //***获取所有商品分类集合--并加载sku数据***
                // 参数: firstLoadCatalog:第一次加载标记 index:第几个组件
                // 数据筛选条件: catalogUrl1(一级分类url) 和 categoryCode:选材类型大类
                findAllCatalogs: function (firstLoadCatalog, index, catalogUrl1, categoryCode) {
                    var self = this;
                    var url = "/material/prodcatalog/findtwostagewithpromaterial?contractCode="
                        + self.customerContract.contractCode;
                    //拼接页面类型
                    if(self.pageType == 'select' || self.pageType == 'audit'){
                        url += "&pageType=select";
                    }else if(self.pageType == 'change'){
                        url += "&pageType=change";
                    }
                    if(catalogUrl1){
                        url += "&url=" + catalogUrl1;
                    }
                    if(categoryCode){
                        url += "&categoryCode=" + categoryCode;
                    }
                    self.$http.get(url).then(function(response) {
                        var res = response.data;
                        if (res.code == '1') {
                            self.prodCatalogList = res.data;
                            //直接给子组件中属性赋值
                            //调用套餐标配组件中方法--查询分类及对应sku集合 -- 并且不再加载一级分类
                            self.$refs.tabs.$children[index].$children[0].dealCatalogList(firstLoadCatalog, self.prodCatalogList);

                        }else{
                            self.$toastr.error("获取分类信息失败!")
                        }
                    }).catch(function (e) {
                        console.log(e);
                        self.$toastr.error("获取分类信息失败!")
                    });
                },
                //修改备注
                updateRemark: function (name, customerContract) {
                    showremarkModel (name, customerContract, "/customercontract/contract/save");
                },
                //切换tab页 方法
                clickEvent: function (val) {
                    var self = this;
                    //当前点击的tab索引
                    var activeIndex = val.$refs.tabs.activeKey;
                    if(activeIndex == self.activeIndex){
                        //表示还是当前页点击, 不去重新处理数据
                        return;
                    }
                    var scope = self.selectMaterialTypeEnmu[activeIndex].value;

                    //每次切换tab页时,重新刷新数据--其它金额增减和其它综合费单独&&处理
                    if(activeIndex == 2 || activeIndex == 3){
                        //增减项 需要在查询定额部分
                        self.findMaterialByCategoryCode(activeIndex, this.selectMaterialTypeEnmu[activeIndex].value, this.customerContract.contractCode);
                        //查询主材信息
                        self.findAllCatalogs(true, activeIndex, null, scope);
                    }else if(activeIndex == 4 ){
                        //查询其它金额增减
                        self.$refs.tabs.$children[activeIndex].$children[0].findOtherMoney();
                    }else if(activeIndex == 5 ){
                        //其它综合费的定额部分
                        self.findMaterialByCategoryCode(activeIndex, this.selectMaterialTypeEnmu[activeIndex].value, this.customerContract.contractCode);
                        //计算下总数--只有这个和旧房拆改需要
                        self.showTotalAmount(false);
                    }else{
                        self.findAllCatalogs(true, activeIndex, null, scope);
                    }

                    if(self.catalogUrl && activeIndex == 0){
                        setTimeout(function () {
                            //被打回回显
                            $("#" + self.catalogUrl + "Id").addClass('trun-back-back-color');
                        },200);
                    }

                    //记住本次活动页
                    self.activeIndex = activeIndex;
                },
                //点击切换旧房拆改工程tab, 每次点击时,只有当点击到旧房拆改时,才加载,
                // 一直点击当前页时,不触发查询
                clickEventOldHouse: function (val) {
                    var self = this;
                    //当前点击的tab索引
                    var activeIndex = val.$children[1].activeKey;

                    if(activeIndex == 1 && self.decorationNeedLoadFlag){
                        //点击了装修工程, 去初始化6个字组件
                        //获取所有分类信息 第一个参数为初次加载(每个tab页只在此加载一次,以便统计表头数量)
                        self.findAllCatalogs(true, 1, null, self.selectMaterialTypeEnmu[1].value);
                        self.findAllCatalogs(true, 2, null, self.selectMaterialTypeEnmu[2].value);
                        self.findAllCatalogs(true, 3, null, self.selectMaterialTypeEnmu[3].value);
                        //初始化加载 增项中的定额
                        self.findMaterialByCategoryCode(2, self.selectMaterialTypeEnmu[2].value, self.customerContract.contractCode);
                        //初始化加载 减项中的定额
                        self.findMaterialByCategoryCode(3, self.selectMaterialTypeEnmu[3].value, self.customerContract.contractCode);
                        //初始化加载 其它综合费中的定额
                        self.findMaterialByCategoryCode(5, self.selectMaterialTypeEnmu[5].value, self.customerContract.contractCode);
                        //其它金额增减--初始化加载
                        self.$refs.tabs.$children[4].$children[0].findOtherMoney();
                        //当页面类型是 change 且有被打回一级分类时,加载一次! 默认选中当前被打回来得一级分类
                        if(self.pageType == 'change' && self.catalogUrl != 'other'){
                            //加载类目一级分类,并且回显打回的一级分类
                            self.loadFirstCatalogs();
                        }else if(self.pageType == 'select' || self.pageType == 'change' || self.pageType == 'audit'){
                            //加载所有一二级分类带主材sku及用量
                            self.findAllCatalogs(true, 0, null, self.selectMaterialTypeEnmu[0].value);
                        }
                        //将装修标记变为false, 以后的点击不再重新加载
                        self.decorationNeedLoadFlag = false;
                    }

                    if(activeIndex != 2 || self.oldHouseactiveIndex == activeIndex){
                        //记住本次活动页
                        self.oldHouseactiveIndex = activeIndex;
                        //表示还是当前页点击, 不去重新处理数据
                        return;
                    }

                    var scope = self.selectMaterialTypeEnmu[6].value;

                    //每次切换tab页时,重新刷新数据
                    //旧房拆改工程
                    self.findMaterialByCategoryCode(0, this.selectMaterialTypeEnmu[6].value, this.customerContract.contractCode, "son");

                    //计算下总数
                    self.showTotalAmount(false);

                    //记住本次活动页
                    self.oldHouseactiveIndex = activeIndex;
                },
                //加载类目一级分类
                //参数: 用于
                loadFirstCatalogs: function () {
                    var self = this;
                    self.$http.get("/material/prodcatalog/findfirsthaschildren").then(function(response) {
                        var res = response.data;
                        if (res.code == '1') {
                            //直接给子组件中属性赋值
                            self.$refs.tabs.$children[0].$children[0].topCatalogList = res.data;
                            self.$refs.tabs.$children[0].$children[0].chooseCatalog(self.catalogUrl, true);
                        } else {
                            self.$toastr.error("获取一级分类信息失败!")
                        }
                    });
                },
                //计算sku预算用量合计/含损耗用量合计/总价--页面刷新时加载一次
                // 参数3:quotaType定额类型,如果有,就不再直接计算tab页标题总数,(即: 有定额的不用计算!!!)
                dealSkuAmount: function (lowerCatalogList, activeIndex, quotaType) {
                    var self = this;
                    //定义变量
                    var budgetDosageAmount = 0;
                    var lossDosageAmount = 0;
                    var skuSumMoney = 0;
                    var skuSum = 0;
                    lowerCatalogList.forEach(function (catalog) {
                        if(catalog && catalog.projectMaterialList && catalog.projectMaterialList.length > 0){
                            catalog.projectMaterialList.forEach(function(projectMaterial){
                                //计算sku总数
                                skuSum ++;
                                //清空
                                budgetDosageAmount = 0;
                                lossDosageAmount = 0;
                                skuSumMoney = 0;
                                if(projectMaterial && projectMaterial.skuDosageList && projectMaterial.skuDosageList.length > 0){
                                    projectMaterial.skuDosageList.forEach(function (skuDosage) {
                                        if(skuDosage){
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
                    //给组件中skuSum赋值
                    self.$refs.tabs.$children[activeIndex].$children[0].skuSum = skuSum;
                    if(!quotaType){
                        //更改主组件的skuSum值的显示
                        self.replaceTitleName(activeIndex, skuSum);
                    }
                },
                //替换tip页名称
                replaceTitleName: function (activeIndex, amount) {
                    var self = this;
                    var name = self.selectMaterialTypeEnmu[activeIndex].name;
                    for(var i = 0; i <= self.tabList.length; i++){
                        if(self.tabList[i].label && self.tabList[i].label.indexOf(name) != -1){
                            self.tabList[i].label = name + "(" + amount + ")";
                            break;
                        }
                    }
                },
                //计算当前 projectMaterial 对象下的预算用量合计/含损耗用量合计/总价; --每次增删sku用量时,执行
                //type:加 true/减 false
                dealSkuAmountByOperation: function (projectMaterial, skuDosage, type) {
                    if(type){
                        projectMaterial.budgetDosageAmount += skuDosage.budgetDosage || 0;
                        projectMaterial.lossDosageAmount += skuDosage.lossDosage || 0;
                    }else{
                        projectMaterial.budgetDosageAmount -= skuDosage.budgetDosage || 0;
                        projectMaterial.lossDosageAmount -= skuDosage.lossDosage || 0;
                    }
                    projectMaterial.skuSumMoney = (projectMaterial.storeSalePrice || 0) * projectMaterial.lossDosageAmount;
                },
                //根据商品分类url 去查询对应的catalog对象
                getCatalogByUrl: function (lowerCatalogList, url) {
                    if(lowerCatalogList && url){
                        for(var i = 0; i <= lowerCatalogList.length; i++){
                            if(lowerCatalogList[i] && url.indexOf(lowerCatalogList[i].url) != -1){
                                //找到了一级分类
                                return lowerCatalogList[i];
                                break;
                            }
                        }
                    }
                    return null;
                },
                //移除商品sku方法
                //参数: projectMaterialList: sku集合, projectMaterial: sku对象, index:组件的索引
                //参数4: quotaType:true表示需要考虑定额部分
                removeSku: function (projectMaterialList, projectMaterial, index, quotaType) {
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
                        //1.去后台删除该项目主材sku
                        self.$http.get("/material/projectmaterial/delwithdosage?id="+ projectMaterial.id ).then(function(res) {
                            if (res.data.code == '1') {
                                //2.页面移除数据
                                projectMaterialList.remove(projectMaterial);

                                self.totalAmountFlag=true;

                                //旧房拆改时,索引不存在--不需要显示标题总数,故不需要处理
                                if(index != undefined){
                                    if(quotaType){
                                        //3.修改titleSum值的显示 -1
                                        self.$refs.tabs.$children[index].$children[0].titleSum --;
                                    }else{
                                        //3.更改主组件的skuSum值的显示 -1
                                        self.$refs.tabs.$children[index].$children[0].skuSum --;
                                        self.replaceTitleName(index, materialIndex.$refs.tabs.$children[index].$children[0].skuSum);
                                    }
                                }
                                self.$toastr.success("移除商品成功!");
                            }else{
                                self.$toastr.error("移除商品失败!")
                            }
                        }).catch(function () {
                            self.$toastr.error('移除商品失败!');
                        }).finally(function () {
                            swal.close();
                        });
                    });
                },
                //删除sku用量
                removeSkuDosage: function (projectMaterial, skuDosageList, skuDosage) {
                    var self = this;
                    swal({
                        title: '确定删除该商品用量?',
                        text: '',
                        type: 'info',
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        showCancelButton: true,
                        showConfirmButton: true,
                        showLoaderOnConfirm: true,
                        confirmButtonColor: '#ed5565',
                        closeOnConfirm: false
                    }, function () {
                        var url;
                        //拼接页面类型
                        if(self.pageType == 'select'){
                            url = "/material/smskudosage/" + skuDosage.id + "/del";
                        }else if(self.pageType == 'change'){
                            url = '/material/smskuchangedosage/delete/' + skuDosage.id;
                        }
                        //1.去后台删除该项目主材sku
                        self.$http.get(url).then(function(res) {
                            if (res.data.code == '1') {
                                self.totalAmountFlag=true;
                                //2.重新计算sku数量/价钱
                                self.dealSkuAmountByOperation(projectMaterial, skuDosage, false);

                                if(self.pageType == 'select'){
                                    //3.选材时, 页面上删除sku用量
                                    skuDosageList.remove(skuDosage);
                                }else if(self.pageType == 'change'){
                                    //3.变更时, 页面上不删除sku用量,而是将sku的 lossDosage 变为0,页面不显示该sku用量
                                    skuDosage.lossDosage = 0;
                                    skuDosage.noLossDosage = 0;
                                    skuDosage.budgetDosage = 0;
                                    //4.如果是变更,删除用量后,判断是否要改变背景色
                                    self.dealSkuBackColorBySkuDosageList(projectMaterial, skuDosageList);
                                }
                                self.$toastr.success("移除商品用量成功!");
                            }else{
                                self.$toastr.error("移除商品用量失败!")
                            }
                        }).catch(function () {
                            self.$toastr.error('移除商品用量失败!');
                        }).finally(function () {
                            swal.close();
                        });
                    });
                },
                //通过选材类型大类 查找 projectMaterial对象--查询定额相关!!!
                //参数: type: 表示子组件的父类不再是tabs,又该参数指定
                findMaterialByCategoryCode: function (index, categoryCode, contractCode, type) {
                    var self = this;
                    var url = "/material/projectmaterial/findmateriallist?categoryCode="
                        + categoryCode + "&contractCode=" + contractCode;
                    //拼接页面类型
                    if(self.pageType == 'select' || self.pageType == 'audit'){
                        url += "&pageType=select";
                    }else if(self.pageType == 'change'){
                        url += "&pageType=change";
                    }
                    self.$http.get(url).then(function(response) {
                        var res = response.data;
                        if (res.code == '1') {
                            if(!type){
                                //直接给子组件中属性赋值
                                self.$refs.tabs.$children[index].$children[0].projectMaterialList = res.data;

                                //先清空基装和综合费集合
                                self.$refs.tabs.$children[index].$children[0].baseInstallQuotaList = [];
                                self.$refs.tabs.$children[index].$children[0].baseInstallComfeeList = [];
                                if(index == 5){
                                    self.$refs.tabs.$children[index].$children[0].otherComprehensiveFeeList = [];
                                    //将查询到的其它综合费 ,处理成其它综合费子类,并存储到集合中
                                    self.dealBaseInstallList(self.$refs.tabs.$children[index].$children[0].projectMaterialList,
                                        null, null ,self.$refs.tabs.$children[index].$children[0].otherComprehensiveFeeList);
                                }else{
                                    //将查询到的增项,处理成基装和综合费,并存储到集合中
                                    self.dealBaseInstallList( self.$refs.tabs.$children[index].$children[0].projectMaterialList,
                                        self.$refs.tabs.$children[index].$children[0].baseInstallQuotaList, self.$refs.tabs.$children[index].$children[0].baseInstallComfeeList);
                                }
                            }else{
                                //son组件下!!!
                                //直接给子组件中属性赋值
                                self.$refs.son.$children[index].projectMaterialList = res.data;

                                //先清空基装和综合费集合
                                self.$refs.son.$children[index].dismantleBaseinstallquotaList = [];
                                self.$refs.son.$children[index].dismantleBaseinstallCompFeeList = [];
                                self.$refs.son.$children[index].dismantleOtherCompFeeList = [];
                                //将查询到的增项,处理成基装和综合费,并存储到集合中
                                self.dealBaseInstallList(self.$refs.son.$children[index].projectMaterialList,
                                            self.$refs.son.$children[index].dismantleBaseinstallquotaList,
                                            self.$refs.son.$children[index].dismantleBaseinstallCompFeeList,
                                            self.$refs.son.$children[index].dismantleOtherCompFeeList);
                            }


                        }else{
                            self.$toastr.error("查询项目主材列表失败!")
                        }
                    }).catch(function (e) {
                        console.log(e);
                        self.$toastr.error("查询项目主材列表失败!")
                    });
                },
                //将查询到的增项,处理成基装和综合费,并存储到集合中
                dealBaseInstallList: function (projectMaterialList, baseInstallQuotaList, baseInstallComfeeList,
                                               otherComprehensiveFeeList) {
                    var self = this;
                    if(projectMaterialList && projectMaterialList.length > 0){
                        projectMaterialList.forEach(function (projectMaterial) {
                            if(projectMaterial){
                                if(projectMaterial.categoryDetailCode == "BASEINSTALLQUOTA" || projectMaterial.categoryDetailCode == "DISMANTLEBASEINSTALLQUOTA"){
                                    //基装定额 或者 拆除基装定额
                                    if(baseInstallQuotaList){
                                        baseInstallQuotaList.push(projectMaterial);
                                    }
                                }else if(projectMaterial.categoryDetailCode == "BASEINSTALLCOMPREHENSIVEFEE" || projectMaterial.categoryDetailCode == "DISMANTLEBASEINSTALLCOMPFEE"){
                                    //基装增项综合费 或者 拆除基装增项综合服务
                                    if(baseInstallComfeeList){
                                        baseInstallComfeeList.push(projectMaterial);
                                    }
                                }else if(projectMaterial.categoryDetailCode == "OTHERCATEGORIESOFSMALLFEES" || projectMaterial.categoryDetailCode == "DISMANTLEOTHERCOMPFEE"){
                                    //其它综合费 或者 拆除其它综合服务
                                    if(otherComprehensiveFeeList){
                                        otherComprehensiveFeeList.push(projectMaterial);
                                    }
                                }
                            }
                        });
                    }
                },
                //计算主材中 选材/变更/被打回,背景颜色变化
                //参数: catalog 分类
                dealSkuBackColorByCatalog: function (catalog) {
                    catalog.projectMaterialList.forEach(function (projectMaterial) {
                        var orgBudgetSum = 0;
                        var newBudgetSum = 0;
                        if(projectMaterial.skuDosageList && projectMaterial.skuDosageList.length > 0){
                            projectMaterial.skuDosageList.forEach(function (skuDosage) {
                                newBudgetSum += skuDosage.lossDosage || 0;
                                orgBudgetSum += skuDosage.originalDosage || 0;
                            });

                            if(orgBudgetSum != newBudgetSum){
                                //发生了变更
                                Vue.set(projectMaterial, "backColorFlag", 'change');
                            }else{
                                Vue.set(projectMaterial, "backColorFlag", null);
                            }
                        }else{
                            //该主材下没有用量
                            Vue.set(projectMaterial, "backColorFlag", 'change');
                        }

                    });
                },
                //计算主材中 选材/变更/被打回,背景颜色变化
                //参数: skuDosageList
                dealSkuBackColorBySkuDosageList: function (projectMaterial, skuDosageList) {
                    var orgBudgetSum = 0;
                    var newBudgetSum = 0;
                    if(skuDosageList && skuDosageList.length > 0){
                        skuDosageList.forEach(function (skuDosage) {
                            newBudgetSum += skuDosage.lossDosage || 0;
                            orgBudgetSum += skuDosage.originalDosage || 0;
                        });
                        if(orgBudgetSum != newBudgetSum){
                            //发生了变更
                            Vue.set(projectMaterial, "backColorFlag", 'change');
                        }else{
                            Vue.set(projectMaterial, "backColorFlag", null);
                        }
                    }else{
                        //该主材下没有用量
                        Vue.set(projectMaterial, "backColorFlag", 'change');
                    }
                },

            },
            events: {

            },
            watch: {
                'totalAmount': function (newVal,oldVal) {
                    var self = this;
                    //总数变化后,给子组件中需要的统计数量 赋值
                    var totalAmount = {
                        //基装增项总价
                        baseloadrating1: self.totalAmount.baseloadrating1,
                        //工程总价
                        renovationAmount: self.totalAmount.renovationAmount,
                        //拆除基装定额总价
                        baseloadrating3: self.totalAmount.baseloadrating3,
                        //拆除工程总价
                        comprehensivefee4: self.totalAmount.comprehensivefee4
                    };
                    if(self.pageType == 'select' || self.pageType == 'audit'){
                        self.$refs.tabs.$children[2].$children[0].totalAmount = totalAmount;
                        self.$refs.tabs.$children[5].$children[0].totalAmount = totalAmount;
                        //旧房拆改组件
                        self.$refs.son.$children[0].totalAmount = totalAmount;
                    }else if(self.pageType == 'change'){
                        //变更时,增项和其它综合费的 基装增项总价 和 工程总价 要读取的是选材时的
                        //      旧房拆改工程的 拆除基装定额总价 和 拆除工程总价  要读取的是选材时的!
                        self.countTotalMoneyForOtherCompFee();
                    }
                }
            }
        });
    }
)();

//显示添加商品model
//参数: index:第几个组件(必要)
function showAddProdModel (projectMaterial, priceType, catalog, paymentTime, index){
    var $modal = $('#skuModel').clone();
    $modal.modal({
        height: 600,
        width: 1200
    });
    $modal.on('shown.bs.modal',
        function () {
            vueModal4 = new Vue({
                el: $modal.get(0),
                components: {
                    'prod-sku': prodSku
                },
                data: {
                    projectMaterial: projectMaterial,
                    priceType: priceType,
                    catalogUrl1: catalog != null ? catalog.parent.url : '',
                    catalogUrl2: catalog != null ? catalog.url : '',
                    date: paymentTime,
                    pageType: materialIndex.pageType
                },
                created: function () {
                },
                ready: function () {
                },
                methods: {
                },
                events:{
                    'sku': function (model) {
                        var self = this;
                        //本次选中的sku,填充到 项目主材中
                        var projectMaterial = {
                            //新增时,返回的主键
                            id: model.projectMaterialId,
                            productName: model.name || '',
                            skuImagePath: model.path || '',
                            storeSalePrice: model.price || '',
                            attribute1: model.attribute1 || '',
                            attribute2: model.attribute2 || '',
                            attribute3: model.attribute3 || '',
                            materialUnit: model.unit,
                            skuCode: model.code,
                            //提前定义三个计算字段
                            budgetDosageAmount: 0,
                            lossDosageAmount: 0,
                            skuSumMoney: 0,
                            skuSqec: model.spec,
                        }
                        if (index != 0){
                            //其它组件需要根据商品分类url 在二级分类中去查询对应的catalog对象
                            catalog = materialIndex.getCatalogByUrl(
                                materialIndex.$refs.tabs.$children[index].$children[0].lowerCatalogList, model.url);
                        }
                        //项目主材sku集合
                        var projectMaterialList ;
                        if(!catalog.projectMaterialList){
                            //不存在 就新增
                            projectMaterialList = [];
                        }else{
                            //继续追加
                            projectMaterialList = catalog.projectMaterialList
                        }
                        projectMaterialList.push(projectMaterial);

                        //2.更改主组件的skuSum值的显示 +1
                        materialIndex.$refs.tabs.$children[index].$children[0].skuSum ++;
                        materialIndex.replaceTitleName(index,
                            materialIndex.$refs.tabs.$children[index].$children[0].skuSum);

                        //将当前 projectMaterialList 对象,填充到catalog中 (每次都是填充新的)
                        Vue.set(catalog, 'projectMaterialList', projectMaterialList);

                        //3.如果是变更,删除用量后,判断是否要改变背景色
                        if(materialIndex.pageType == 'change'){
                            materialIndex.dealSkuBackColorByCatalog(catalog);
                        }
                        $modal.modal('hide');
                    }
                }
            })
        });
}

//显示 备注 弹框modal
function showremarkModel (name, obj, url){
    var $modal = $('#remarkModel').clone();
    $modal.modal({
        height: 150,
        width: 470,
        backdrop: 'static',
    });
    var remark = '';
    if(materialIndex.pageType == 'select' || materialIndex.pageType == 'change'){
        remark = obj.designRemark;
    }else if (materialIndex.pageType == 'audit'){
        remark = obj.auditRemark;
    }
    $modal.on('shown.bs.modal',
        function () {
            var vueModal = new Vue({
                el: $modal.get(0),
                data: {
                    name: name,
                    id: obj.id,
                    remark: remark,
                    disabled: true
                },
                created: function () {
                },
                ready: function () {
                },
                methods: {
                    validateSub: function () {
                        var self = this;
                        if(materialIndex.pageType == 'select' || materialIndex.pageType == 'change'){
                            if(self.remark != obj.designRemark){
                                self.disabled = false;
                            }else{
                                self.disabled = true;
                            }
                        }else if (materialIndex.pageType == 'audit'){
                            if(self.remark != obj.auditRemark){
                                self.disabled = false;
                            }else{
                                self.disabled = true;
                            }
                        }
                    },
                    submit: function () {
                        var self = this;
                        self.disabled = true;
                        var data = {
                            id: self.id,
                        }
                        if(materialIndex.pageType == 'select' || materialIndex.pageType == 'change'){
                            data.designRemark = self.remark;
                        }else if (materialIndex.pageType == 'audit'){
                            data.auditRemark = self.remark;
                        }
                        self.$http.post(url, data, {emulateJSON: true}).then(function(res) {
                            if (res.data.code == '1') {
                                if(materialIndex.pageType == 'select' || materialIndex.pageType == 'change'){
                                    Vue.set(obj, 'designRemark', self.remark);
                                }else if (materialIndex.pageType == 'audit'){
                                    Vue.set(obj, 'auditRemark', self.remark);
                                }
                                self.$toastr.success("添加备注成功!");
                                $modal.modal('hide');
                            }else{
                                self.$toastr.success("添加备注失败!");
                            }
                        })
                    }
                }
            })
        });
}

//显示 修改数量 弹框modal
function showDosageAmountModel (name, skuDosage, url){
    var $modal = $('#dosageModel').clone();
    $modal.modal({
        height: 100,
        width: 450,
        backdrop: 'static'
    });
    $modal.on('shown.bs.modal',
        function () {
            var vueModal = new Vue({
                el: $modal.get(0),
                data: {
                    name: name,
                    id: skuDosage.id,
                    lossDosage: skuDosage.lossDosage || 0,
                    disabled: true
                },
                created: function () {
                },
                ready: function () {
                },
                methods: {
                    validateSub: function () {
                        var self = this;
                        if(self.lossDosage != skuDosage.lossDosage){
                            self.disabled = false;
                        }else{
                            self.disabled = true;
                        }
                    },
                    dosageSubmit: function () {
                        var self = this;
                        self.disabled = true;
                        //加入数字正则
                        if(!self.lossDosage || !/^[0-9]*$/.test(self.lossDosage)){
                            self.lossDosage = 1;
                            self.$toastr.error("请输入大于0的数字");
                            return;
                        }
                        var data = {
                            id: self.id,
                            lossDosage: self.lossDosage
                        }
                        //拼接页面参数
                        if(materialIndex.pageType){
                            data.pageType = materialIndex.pageType;
                        }
                        self.$http.post(url, data).then(function(res) {
                            if (res.data.code == '1') {
                                Vue.set(skuDosage, 'lossDosage', self.lossDosage);
                                materialIndex.totalAmountFlag=true;
                                self.$toastr.success("修改数量成功!");
                                $modal.modal('hide');
                            }else{
                                self.$toastr.error("修改数量失败!");
                            }
                        })
                    }
                }
            })
        });
}

/**显示添加 sku 用量model*/
function showAddSkuDosageModel (priceType, catalog, projectMaterial, paymentTime){
    var $modal = $('#addUsageModel').clone();
    $modal.modal({
        height: 600,
        width: 800,
        keyboard: false,
        backdrop: 'static'
    });
    $modal.on('shown.bs.modal',
        function () {
            addUsageVue = new Vue({
                el: $modal.get(0),
                components: {
                    'add-usage': addUsage
                },
                data: {
                    projectMaterialId: projectMaterial.id,
                    skuCode: projectMaterial.skuCode,
                    priceType: priceType,
                    price: projectMaterial.storeSalePrice,
                    catalogUrl1: catalog.parent.url,
                    catalogUrl2: catalog.url,
                    date: paymentTime,
                    spec: projectMaterial.skuSqec,
                    pageType: materialIndex.pageType
                },
                created: function () {
                },
                ready: function () {
                },
                methods: {
                },
                events:{
                    'saveSkuDosage': function (model) {
                        //本次选中的sku用量,填充到 projectMaterial 对象
                        var skuDosage = {
                            id: model.id,
                            domainName: model.domainName,
                            budgetDosage: model.budgetDosage,
                            lossFactor: model.lossFactor,
                            lossDosage: model.lossDosage,
                            dosageRemark: model.dosageRemark,
                            convertUnit: model.convertUnit
                        };
                        //sku用量集合
                        var skuDosageList ;
                        if(!projectMaterial.skuDosageList){
                            //不存在 就新增
                            skuDosageList = [];
                        }else{
                            //继续追加
                            skuDosageList = projectMaterial.skuDosageList
                        }
                        //1.重新计算sku数量/价钱
                        materialIndex.dealSkuAmountByOperation(projectMaterial, skuDosage, true);
                        skuDosageList.push(skuDosage);
                        //将当前 projectMaterialList 对象,填充到catalog中 (每次都是填充新的)
                        Vue.set(projectMaterial, 'skuDosageList', skuDosageList);

                        //2.如果是变更,删除用量后,判断是否要改变背景色
                        if(materialIndex.pageType == 'change'){
                            materialIndex.dealSkuBackColorBySkuDosageList(projectMaterial, skuDosageList);
                        }
                        $modal.modal('hide');
                    }
                }
            })
        });
}

/**修改客户合同*/
function modifyModal(model) {
    var _modal = $('#modifyModal').clone();
    var $el = _modal.modal({
        height: 500,
        maxHeight: 500,
        keyboard: false,
        backdrop: 'static'
    });
    $el.on('shown.bs.modal',
        function () {
            // 获取 node
            var el = $el.get(0);
            // 创建 Vue 对象编译节点
            vueModal = new Vue({
                el: el,
                validators: {
                    mobile: function (val) {
                        return /^1(3|4|5|7|8)\d{9}$/.test(val) || (val === '');//手机号必须为11位数字
                    }
                },
                http: {
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    }
                },
                // 模式窗体必须引用 ModalMixin
                mixins: [MdniVueMixins.ModalMixin],
                $modal: $el,
                //模式窗体 jQuery 对象
                created: function () {
                },
                ready: function () {
                    this.fetchMeal();
                },
                data: {
                    provinceCode: model.provinceCode,
                    cityCode: model.cityCode,
                    areaCode: model.areaCode,
                    addressProvince: '',
                    addressCity: '',
                    addressArea: '',

                    province: province,
                    city: city,
                    district: district,

                    id: model.id,
                    meals: null,
                    contractCode: model.contractCode,
                    houseAddr: model.houseAddr,
                    customerName: model.customerName,
                    customerMobile: model.customerMobile,
                    secondContact: model.secondContact,
                    secondContactMobile: model.secondContactMobile,
                    elevator: model.elevator,
                    houseCondition: model.houseCondition,
                    houseType: model.houseType,
                    layout: model.layout,
                    buildArea: model.buildArea,
                    valuateArea: model.valuateArea,
                    mealId: null,
                    mealName:model.mealName,
                    pageType: materialIndex.pageType,
                    activityName: model.activityName
                },

                methods: {
                    fetchMeal: function () {
                        var self = this;
                        self.$http.get('/material/mealinfo/findmealbystorecode').then(function (res) {
                            if (res.data.code == 1) {
                                self.meals = res.data.data;
                                self.mealId = model.mealId;
                            }
                        }).catch(function () {
                        }).finally(function () {
                        })
                    },
                    selectChange: function () {
                        this.cityCode = '';
                        this.areaCode = '';
                    },
                    save: function () {
                        var self = this;
                        var provinceCode = document.querySelector(".provinceCode");
                        var cityCode = document.querySelector('.cityCode');
                        var areaCode = document.querySelector('.areaCode');
                        self.addressProvince = provinceCode.options[provinceCode.selectedIndex].text;
                        self.addressCity = cityCode.options[cityCode.selectedIndex].text;
                        self.addressArea = areaCode.options[areaCode.selectedIndex].text;
                        var source = self._data;
                        delete source.province;
                        delete source.city;
                        delete source.district;
                        delete source.meals;
                        self.$validate(true, function () {
                            if (self.$validation.valid) {
                                self.submitting = true;
                                self.$http.post('/customercontract/contract/update', $.param(self._data)).then(function (res) {
                                        if (res.data.code == '1') {
                                            Vue.toastr.success(res.data.message);
                                            materialIndex.$refs.customer.$children[0].customerContract = source;
                                            $el.modal('hide');
                                            self.$destroy();
                                        } else {
                                            Vue.toastr.error(res.data.message);
                                        }
                                    },
                                    function (error) {
                                        Vue.toastr.error(error.responseText);
                                    }).catch(function () {
                                }).finally(function () {
                                    self.submitting = false;
                                });
                            }
                        });
                    }
                },
                watch: {
                    //通过id,修改套餐名称
                    mealId: function (newVal,oldVal) {
                        var self = this;
                        if(self.meals && self.meals.length > 0){
                            for (var i = 0; i < self.meals.length; i++){
                                if(newVal == self.meals[i].id){
                                    self.mealName = self.meals[i].mealName;
                                    break;
                                }
                            }
                        }
                        materialIndex.totalAmountFlag=true;
                    },
                    valuateArea:function (newVal,oldVal) {
                        materialIndex.totalAmountFlag=true;
                    }
                }
            });

            // 创建的Vue对象应该被返回
            return vueModal;
        });
}

/**显示添加定额modal 参数type:表示切换了子组件父类名称!*/
function showAddProjectIntem(projectMaterial, paymentTime, index, type) {
    var $modal = $('#projectIntemModel').clone();
    $modal.modal({
        height: 600,
        width: 1200
    });
    $modal.on('shown.bs.modal',
        function () {
            projectIntemVue = new Vue({
                el: $modal.get(0),
                components: {
                    'project-intem': projectIntem
                },
                data: {
                    contractCode: projectMaterial.contractCode,
                    projectIntemMold: projectMaterial.categoryCode,
                    paymentTime: paymentTime,

                },
                created: function () {
                },
                ready: function () {
                },
                methods: {},
                events: {
                    'otherIntem': function (model) {
                        //本次选中的sku,填充到 项目主材中
                        //给项目主材sku集合添加当前对象
                        if(model.categoryDetailCode == 'BASEINSTALLQUOTA'){
                            materialIndex.$refs.tabs.$children[index].$children[0].baseInstallQuotaList.push(model);
                        }else if(model.categoryDetailCode == 'BASEINSTALLCOMPREHENSIVEFEE'){
                            materialIndex.$refs.tabs.$children[index].$children[0].baseInstallComfeeList.push(model);
                        }else if(model.categoryDetailCode == 'OTHERCATEGORIESOFSMALLFEES'){
                            materialIndex.$refs.tabs.$children[index].$children[0].otherComprehensiveFeeList.push(model);
                        }else if(model.categoryDetailCode == 'DISMANTLEBASEINSTALLQUOTA' && type){
                            materialIndex.$refs.son.$children[index].dismantleBaseinstallquotaList.push(model);
                        }else if(model.categoryDetailCode == 'DISMANTLEBASEINSTALLCOMPFEE'  && type){
                            materialIndex.$refs.son.$children[index].dismantleBaseinstallCompFeeList.push(model);
                        }else if(model.categoryDetailCode == 'DISMANTLEOTHERCOMPFEE'  && type){
                            materialIndex.$refs.son.$children[index].dismantleOtherCompFeeList.push(model);
                        }


                        if(!type){
                            //2.更改主组件的 titleSum 值的显示 +1
                            materialIndex.$refs.tabs.$children[index].$children[0].titleSum ++;
                        }else{
                            //2.更改主组件的 titleSum 值的显示 +1
                            materialIndex.$refs.son.$children[index].titleSum ++;
                        }

                        $modal.modal('hide');
                    }
                }
            })
        });
}

/**显示 添加其它金额增减model*/
function showOtherFeeModel(otherMoneyList, contractCode) {
    var $modal = $('#otherFeeModel').clone();
    var $el = $modal.modal({
        height: 400,
        width: 600,
        keyboard: false,
        backdrop: 'static'
    });
    $modal.on('shown.bs.modal',
        function () {
            otherFeeVue = new Vue({
                el: $modal.get(0),
                data: {
                    types: [{'id': 1, 'name': "优惠税金", 'type': 'DISCOUNTS_TAXES'},
                        {'id': 2, 'name': "优惠管理费", 'type': 'DISCOUNTS_ADMINISTRATIVE_FEE'},
                        {'id': 3, 'name': "优惠设计费", 'type': 'DISCOUNTS_DESIGN_FEE'},
                        {'id': 4, 'name': "其它", 'type': 'OTHER'}],
                    contractCode: contractCode,
                    itemName:'',
                    addReduceReason:'',
                    addReduceType:'1',
                    taxedAmount: [],
                    quota:'',
                    approver:'',
                    showCheckbox:false,
                    pageType: materialIndex.pageType,
                },
                watch:{
                    'addReduceType':function (val) {
                        var self = this;
                        if (val == 0 ) {
                            self.showCheckbox = true;
                        }else{
                            self.showCheckbox = false;
                        }
                    }
                },
                validators: {
                    num: {
                        message: '请输入正确的金额格式，例（10或10.00）',
                        check: function (val) {
                            // /^([0-9]+|[0-9]{1,3}(,[0-9]{3})*)(.[0-9]{1,2})?$/
                            return /^([1-9]\d{0,9}|0)([.]?|(\.\d{1,2})?)$/.test(val);
                        }
                    }
                },
                created: function () {
                },
                ready: function () {
                },
                methods: {
                    saveOtherMoney: function(){
                        var self = this;
                        self.$validate(true,
                            function () {
                                if (self.$validation.valid) {
                                    if (self.taxedAmount.length > 0) {
                                        self.taxedAmount = self.taxedAmount[0];
                                    }else{
                                        self.taxedAmount = '0';
                                    }
                                    self.$http.post('/material/otheraddreduceamount/save',self._data,{emulateJSON: true}).then(function (res) {
                                        if (res.data.code == 1) {
                                            materialIndex.totalAmountFlag=true;
                                            self.$toastr.success('添加其它金额增加成功!');
                                            //将增加完的数据 添加到页面中
                                            var otherMoney = res.data.data;
                                            if(otherMoney.addReduceType == '1'){
                                                otherMoney.addReduceType = "+ ";
                                            }else if(otherMoney.addReduceType == '0'){
                                                otherMoney.addReduceType = "- ";
                                            }
                                            otherMoneyList.push(otherMoney);
                                            //给skuSum +1
                                            materialIndex.$refs.tabs.$children[4].$children[0].skuSum ++;
                                            materialIndex.replaceTitleName(4, materialIndex.$refs.tabs.$children[4].$children[0].skuSum);
                                            $el.modal('hide');
                                        }else{
                                            self.$toastr.error('添加其它金额增加失败!');
                                        }
                                    })
                                }else{
                                    self.submitting = false;
                                }
                            })
                    }
                },
            })
        });
}