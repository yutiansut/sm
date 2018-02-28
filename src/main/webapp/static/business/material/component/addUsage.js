//添加用量组件
var addUsage = Vue.extend({
    template: '#addUsage',
    data:function() {
        return {
            //功能区
            domainList: null,
            //计量单位转化
            convertUnitList: null,
            submitData:{
                projectMaterialId:this.projectMaterialId,
                storeUpgradeDifferencePrice:null,
                storeReducePrice:null,
                storeIncreasePrice:null,
                storeSalePrice:null,
                domainName:[],
                noLossDosage:null,
                convertUnit:null,
                lossFactor:null,
                lossDosage:null,
                dosageRemark:null,
                budgetDosage:null,
                skuCode:this.skuCode,
                date:this.date,
                price:this.price,
                priceType:this.priceType
            },
            selectedRows: {},
            // 选中列
            modalModel: null,
            // 模式窗体模型
            _$el: null,
            // 自己的 el $对象
            _$dataTable: null, // datatable $对象
            submitting:false,
            sp:1,
            flg:false,
            pageType: this.pageType
        }
    },
    validators:{
        positive1:{
            message:'请输入正确的用量',
            check:function(val){
                //整正数
               return  /^\+?[1-9]\d*$/.test(val);
            }
        },
        positive2:{
            message:'请输入正确的用量',
            check:function(val){
                //正数
                    return /^([1-9]\d*(\.\d*[1-9])?)|(0\.\d*[1-9])$/.test(val);
            }
        }
    },
    props:{
        projectMaterialId:{
            required: true
        },
        skuCode:{
            type: String,
            required: true
        },
        catalogUrl1:{
            type: String,
            required: true
        },
        catalogUrl2:{
            type: String,
            required: true
        },
        date:{
            required: true
        },
        price:{
            type:Number,
            required: true
        },
        priceType:{
            type:String,
            required:true
        },
        spec:{
            type:String,
            required:true
        },
        pageType:{
            type: String,
            required:true
        },
    },
    created: function () {
        this.findDomainList();
        this.findConvertUnitList();
        this.getLossFactor();
        this.getflg();
    },
    ready: function () {
    },
    methods: {
        // 获取功能区列表
        findDomainList:function () {
            var self = this;
            var ss=self.catalogUrl2;
            self.$http.get('/material/smskudosage/domainlist/'+ss).then(function (res) {
                if (res.data.code == 1) {
                    var data = res.data.data;
                    if (data.length>0){
                        self.domainList = data;
                    }else{
                        self.$toastr.error('功能区不可为空,请联系管理员');
                        self.submitting = true;
                    }
                }
            }).catch(function () {
            }).finally(function () {
            });
        },
        //转换单位列表
        findConvertUnitList: function () {
            var self = this;
            self.$http.get('/material/smskudosage/convertunit/'+self.catalogUrl2).then(function (res) {
                if (res.data.code == 1&& res.data.data!=null&&res.data.data.length>0) {
                    self.submitData.convertUnit = res.data.data[0].id;
                    self.convertUnitList = res.data.data;
                    //
                    if(this.convertUnitList) {
                        //规格处理
                        var spec = this.spec;
                        if(spec){
                            //去掉空格
                            spec = spec.replace(/\s+/g,"");
                            if (spec.indexOf("X") > 0 || spec.indexOf("x") > 0) {
                                spec = spec.replace("X", "*");
                                spec = spec.replace("x", "*");
                            }
                            var split = spec.split("*");
                            var result = 1;
                            if(split && split.length > 0){
                                split.forEach(function (item) {
                                    result *= parseInt(item);
                                });
                                this.sp = result / 1000000;
                            }
                            if(this.sp==NaN){
                                self.$toastr.error('规格不合法,应为300*300或者300X300，请联系管理员');
                                self.submitting = true;
                            }
                        }
                    }
                }
            })
        },
        //获取耗损系数
        getLossFactor: function () {
            var self = this;
            self.$http.get('/material/smskudosage/getlossfactor/'+self.catalogUrl2).then(function (res) {
                if (res.data.code == 1) {
                    self.submitData.lossFactor = res.data.data==null?1:res.data.data;
                }
            })
        },
        //用量是否 可以输入小数
        getflg: function () {
            var self = this;
            self.$http.get('/material/smskudosage/getflg/'+self.catalogUrl2).then(function (res) {
                if (res.data.code == 1) {
                    self.flg = res.data.data;
                }
            })
        },
        //添加
        insert: function(){
            var self = this;
            self.$validate(true,
                function () {
                    self.submitting = true;
                    if (self.$validation.valid) {
                        self.submitData.domainName = self.submitData.domainName.toString();
                        self.submitData.pageType = self.pageType;

                        self.$http.post('/material/smskudosage/save',self.submitData).then(function (res) {
                            if (res.data.code == 1) {
                                self.$dispatch('saveSkuDosage',res.data.data);
                                self.$toastr.success('添加sku用量成功!');
                                materialIndex.totalAmountFlag=true;
                            }else{
                                self.$toastr.error('添加sku用量失败');
                            }
                        })
                    }else{
                        self.submitting = false;
                    }
                })

        }
        // end of 渲染datatable
    },
    watch: {
        'submitData.budgetDosage':function(val,oldval){
            if(this.convertUnitList){
             this.submitData.noLossDosage=Math.ceil(val/this.sp);
            this.submitData.lossDosage=Math.ceil(val*(this.submitData.lossFactor*1000)/(this.sp*1000));
            }else{
                this.submitData.noLossDosage=val;
                this.submitData.lossDosage=val*this.submitData.lossFactor;
            }
        }

    }
});