//套餐标配组件js
var packageStandardComponent = Vue.extend({
    template: '#packageStandardTmpl',
    data: function () {
        return {
            //是否有吊顶,石膏线,电视背景墙,
            otherInstallInfo: '',
            //基础装修
            chooseAll: false,
            hangCeiling: false,
            plasterLine: false,
            telWall: false,
            baseBtnClass: 'btn-default',
            basebtnDisable: true,
            //基装数据集合
            otherInstallArr: ["吊顶", "石膏线", "电视背景墙"],
            //一级分类集合
            topCatalogList: [],
            //二级分类集合
            lowerCatalogList: [],
            //商品类型
            priceType: 'SALE',
            //项目主材sku
            projectMaterial: {
                contractCode: MdniUtils.parseQueryStringDecode()['contractCode'],
                categoryCode: 'PACKAGESTANDARD',//套餐标配
                categoryDetailCode: 'PACKAGESTANDARD',//套餐标配
            },
            //sku总数(标题后面的数字)
            skuSum: 0,
            //上次点击的一级分类id
            activeNvgId: 'allId',
            //页面类型: 选材(select) 或者 变更(change) 或者 其他
            pageType: MdniUtils.parseQueryStringDecode()['pageType'] || '',
            //被打回的一级分类url
            catalogUrl: MdniUtils.parseQueryStringDecode()['catalogUrl'] || '',
            //背景色对象
            backColorObj:{
                //变更
                changeBackColor: 'change-back-color',
                //不可操作背景色
                turnBackColor: 'turn-back-color'
            },
        };

    },
    ready: function () {
        //当被打回页面时,默认不给选中全部
        if(!this.catalogUrl){
            //给选择所有添加样式
            $("#" + this.activeNvgId).addClass('active-nvg-color');
        }
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
            materialIndex.dealSkuAmount(self.lowerCatalogList, 0);
        },
        //回显 参数
        loadPackageStandard: function () {
            var self = this;
            //一. 回显 基础装修
            if(materialIndex && materialIndex.customerContract &&
                    materialIndex.customerContract.otherInstallInfo){
                var baseInstall = materialIndex.customerContract.otherInstallInfo;
                if(baseInstall.indexOf('吊顶') != -1){
                    self.hangCeiling = true;
                }
                if(baseInstall.indexOf('石膏线') != -1){
                    self.plasterLine = true;
                }
                if(baseInstall.indexOf('电视背景墙') != -1){
                    self.telWall = true;
                }
            }
        },
        //全选 反选 基础装修(水电基材及人工) 选择
        chooseOrNot: function () {
            var self = this;
            self.chooseAll = !self.chooseAll;
            if(self.chooseAll){
                //全选
                self.hangCeiling = true;
                self.plasterLine = true;
                self.telWall = true;
            }else{
                self.hangCeiling = false;
                self.plasterLine = false;
                self.telWall = false;
            }

        },
        //保存基础装修
        saveBase: function () {
            var self = this;
            var contract = {
                id: materialIndex.customerContract.id,
                contractCode: materialIndex.customerContract.contractCode,
                otherInstallInfo: self.otherInstallInfo,
            }
            self.saveOrUpdateContract(contract);

            //给页面基装赋值,记住
            materialIndex.customerContract.otherInstallInfo = self.otherInstallInfo;
            //保存按钮不可用
            self.basebtnDisable = true;
            //按钮变色
            self.baseBtnClass = 'btn-default';
        },
        //保存/更新方法--操作客户合同
        saveOrUpdateContract: function (data) {
            var self = this;
            self.$http.post("/customercontract/contract/save", data, {emulateJSON: true}
                ).then(function(response) {
                    var res = response.data;
                    if (res.code == '1') {
                        self.$toastr.success("操作成功!")
                    }else{
                        self.$toastr.error("操作失败!")
                    }
                }).catch(function () {
                    self.$toastr.error("操作失败!")
                });
        },
        //修改 otherInstallInfo 字段
        updOtherInstallInfo: function () {
            var self = this;
            if(materialIndex.decorationNeedLoadFlag){
                //首次需要延迟加载
                setTimeout(function () {
                    self.doUpdOtherInstallInfo();
                },200);
            }else{
                self.doUpdOtherInstallInfo();
            }

        },
        //处理基础装修数据
        doUpdOtherInstallInfo: function () {
            var self = this;
            self.otherInstallInfo = materialIndex.customerContract.otherInstallInfo;
            //获取基装的那些input框
            var inputArr = $(".pointSty input");
            var otherInstallInfo = '';
            for(var i = 0; i < inputArr.length - 1; i++){
                if(inputArr[i].checked){
                    otherInstallInfo += self.otherInstallArr[i] + ",";
                }
            }
            //去掉最后一个逗号
            if(otherInstallInfo && otherInstallInfo.lastIndexOf(",") == (otherInstallInfo.length - 1)){
                otherInstallInfo = otherInstallInfo.substring(0, otherInstallInfo.length -1)
            }
            //本次更新的赋值给原来的
            self.otherInstallInfo = otherInstallInfo;
            //触发全选和反选
            if(self.hangCeiling && self.plasterLine && self.telWall ){
                self.chooseAll = true;
            }else{
                self.chooseAll = false;
            }
            //改变保存按钮颜色
            if(self.otherInstallInfo != materialIndex.customerContract.otherInstallInfo){
                self.baseBtnClass = 'btn-primary';
                self.basebtnDisable = false;
            }else{
                self.baseBtnClass = 'btn-default';
                self.basebtnDisable = true;
            }
        },
        //添加商品model
        addProdModel: function (catalog) {
            var self = this;
            showAddProdModel(self.projectMaterial, self.priceType, catalog,
                        MdniUtils.formatDate(new Date(), 'yyyy-MM-dd hh:mm:ss'), 0);
        },
        //移除商品sku
        removeSku: function (projectMaterialList, projectMaterial) {
            var self = this;
            //调用主组件移除商品sku方法
            materialIndex.removeSku(projectMaterialList, projectMaterial, 0);
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
            showAddSkuDosageModel(self.priceType, catalog, projectMaterial,
                    MdniUtils.formatDate(new Date(), 'yyyy-MM-dd hh:mm:ss'));
        },
        //一级分类 导航,
        chooseCatalog: function (type, turnBack) {
            var self = this;
            if(type == 'all'){
                materialIndex.findAllCatalogs(false, 0, null, 'PACKAGESTANDARD');
                //给当前id添加样式
                $("#allId").addClass('active-nvg-color');
                //去掉上次点击添加的样式
                $("#" + self.activeNvgId).removeClass('active-nvg-color');
                //记录本次点击的id
                self.activeNvgId = 'allId';

            }else{
                materialIndex.findAllCatalogs(false, 0, type, 'PACKAGESTANDARD');
                //给当前id添加样式
                //打回,一直给添加红色背景标记
                //延迟 等页面渲染完后 操作
                if(self.catalogUrl && turnBack) {
                    setTimeout(function () {
                        $("#" + self.catalogUrl + "Id").addClass('trun-back-back-color');
                        $("#" + self.catalogUrl + "Id").addClass('active-nvg-color');
                        //去掉上次点击添加的样式
                        $("#" + self.activeNvgId).removeClass('active-nvg-color');
                        //记录本次点击的
                        self.activeNvgId = self.catalogUrl + "Id";
                    }, 200);
                }else{
                    $("#" + type + "Id").addClass('active-nvg-color');
                    //去掉上次点击添加的样式
                    $("#" + self.activeNvgId).removeClass('active-nvg-color');
                    //记录本次点击的
                    self.activeNvgId = type + "Id";
                }
            }
        },
        //删除sku用量
        removeSkuDosage: function (projectMaterial, skuDosageList, skuDosage) {
            materialIndex.removeSkuDosage(projectMaterial, skuDosageList, skuDosage);
        },
    },
    props: ['msg','contractCode'],
    watch: {
        'hangCeiling': function () {
            this.updOtherInstallInfo();
        },
        'plasterLine': function () {
            this.updOtherInstallInfo();
        },
        'telWall': function () {
            this.updOtherInstallInfo();
        },
        //子组件  监听 customerContract 的查询完成, 才能渲染页面
        //合同信息加载完成信号
        'msg': {
            handler: function (newVal, oldval) {
                var self = this;
                if(newVal){
                    //回显
                    this.loadPackageStandard();
                }
            },
            deep: true//对象内部的属性监听，也叫深度监听
        },
        //监听二级分类的变化:
        // 如果是变更:
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