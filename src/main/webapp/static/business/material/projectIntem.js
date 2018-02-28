//sku列表组件--定额
var projectIntem = Vue.extend({
    template: '#projectIntem',
    data: function () {
        return {
            projectIntemTypes: null,
            form: {
                keyword: '',
                projectIntemTypeId:'',
            },
            contractCode:this.contractCode,
            projectIntemMold:this.projectIntemMold,
            paymentTime:this.paymentTime,
            subordinateCategory:this.subordinateCategory,
            $dataTable: null,
            selectedRows: {},
            // 选中列
            modalModel: null,
            // 模式窗体模型
            _$el: null,
            // 自己的 el $对象
            _$dataTable: null, // datatable $对象
            titleName: '销售价',
        }
    },
    props: {
        contractCode: {
            type: String,
            required: true
        },
        projectIntemMold: {
            type: String,
            required: true
        },
        paymentTime: {
            required: true
        },
        subordinateCategory: {
            required: false
        }
    },
    created: function () {

    },
    ready: function () {
        this.projectintem();
        this.drawTable();
    },
    methods: {
        projectintem: function () {
            var self = this;
            self.$http.get('/material/projectintem/getbytype').then(function (res) {
                if (res.data.code == 1) {
                    self.projectIntemTypes = res.data.data;
                }
            })
        },
        query: function () {
            this.$dataTable.bootstrapTable('selectPage', 1);
        },
        drawTable: function () {
            var self = this;
            self.$dataTable = $('#dataTable', self._$el).bootstrapTable({
                url: '/material/projectintem/list',
                method: 'get',
                dataType: 'json',
                cache: false,
                // 去缓存
                pagination: true,
                // 是否分页
                sidePagination: 'server',
                pageSize: 10,
                pageList: [10, 50, 100, 200],
                // 服务端分页
                queryParams: function (params) {
                    // 将table 参数与搜索表单参数合并
                    return _.extend({'projectIntemMold':self.projectIntemMold,'paymentTime':self.paymentTime},
                        params, self.form)
                },
                mobileResponsive: true,
                undefinedText: '',
                // 空数据的默认显示字符
                striped: true,
                // 斑马线
                maintainSelected: true,
                // 维护checkbox选项
                sortOrder: 'desc',
                // 默认排序方式
                columns: [
                    {
                        field: 'projectIntemName',
                        title: '定额名称',
                        align: 'center',
                        orderable: true
                    },
                    {
                        field: 'projectIntemTypeName',
                        title: '所属分类',
                        align: 'center',
                        visible: true
                    },
                    {
                        field: 'subordinateCategory',
                        title: '类型',
                        align: 'center',
                        visible: true,
                        formatter:function (value) {
                            switch (value) {
                                case 'jizhuangdinge':
                                    return '基装定额';
                                    break;
                                case 'jizhuangzengxiang' :
                                    return '基桩增项综合费';
                                    break;
                                case 'qitazonmghefei' :
                                    return '其他综合费';
                                    break;
                                case 'laofangchaichu' :
                                    return '老房拆除基装定额';
                                    break;
                                case 'laofangzonghefei' :
                                    return '老房拆除基装增项综合费';
                                    break;
                                case 'laofangqitazonghefei' :
                                    return '老房拆除其他综合费';
                                    break;
                            }
                        }
                    },
                    {
                        field: 'projectIntemUnit',
                        title: '计量单位',
                        align: 'center',
                        orderable: true
                    },
                    {
                        field: 'valuationMethod',
                        title: '计价方式',
                        align: 'center',
                        orderable: true,
                        formatter:function (value) {
                            switch (value) {
                                case 'fixedUnitPrice':
                                    return '固定单价';
                                    break;
                                case 'foundationPileTotal' :
                                    return '基桩增项总价占比';
                                    break;
                                case 'renovationFoundationPile' :
                                    return '装修工程总价占比';
                                    break;
                                case 'dismantleFoundationPile' :
                                    return '拆除基桩定额总价占比';
                                    break;
                                case 'demolitionProjectTotal' :
                                    return '拆除工程总价占比';
                                    break;
                            }
                        }
                    },
                    {
                        field: 'projectIntemPrice',
                        title: '单价或比例',
                        align: 'center',
                        orderable: true,
                    },
                    {
                        field: 'projectIntemDetail',
                        title: '定额说明',
                        align: 'center',
                        orderable: true,
                    },
                    {
                        field: 'dosage',
                        title: '数量',
                        align: 'center',
                        orderable: true,
                        formatter: function (value, row) {
                            if(row && row.valuationMethod == 'fixedUnitPrice'){
                                return '<input type="number" onkeypress="return (/^[1-9]*[0-9][0-9]*$/.test(String.fromCharCode(event.keyCode)))" class="form-control"' +
                                    ' value="1" min="1" id="'  + row.id + '"  style="width: 80px;height: 90%"/>';
                            }else{
                                return "/";
                            }
                        }
                    },
                    {
                        field: 'id',
                        title: '操作',
                        width: '10%',
                        orderable: false,
                        align: 'center',
                        formatter: function (value, row) {
                            var html = '<button data-handle="set-projectintem" data-id="' + value + '"' +
                                ' data-projectintemcode="' + row.projectIntemCode + '"  ' +
                                ' data-subordinatecategory="' + row.subordinateCategory + '"  ' +
                                ' data-projectintemtypename="' + row.projectIntemTypeName + '"  ' +
                                ' data-projectintemunit="' + row.projectIntemUnit + '"  ' +
                                ' data-projectintemdetail="' + row.projectIntemDetail + '"  ' +
                                ' data-valuationmethod="' + row.valuationMethod + '"  ' +
                                ' data-projectintemname="' + row.projectIntemName + '"  ' +
                                ' data-projectintemprice="' + row.projectIntemPrice + '"  ' +
                                ' data-projectintemunit="' + row.projectIntemUnit + '"  ' +
                                ' data-projectintemcostprice="' + row.projectIntemCostPrice + '"  ' +
                                '  type="button" class="btn btn-xs btn-primary">添加</button>';
                            return html;
                        }
                    }
                ]
            });
            // 添加
            self.$dataTable.on('click', '[data-handle="set-projectintem"]',
                function (e) {
                    var model = $(this).data();
                    var projectMaterial = {};
                    projectMaterial.contractCode = self.contractCode;
                    if ('jizhuangdinge' == model.subordinatecategory) {
                        projectMaterial.categoryDetailCode = 'BASEINSTALLQUOTA';
                    }else if ('jizhuangzengxiang' == model.subordinatecategory) {
                        projectMaterial.categoryDetailCode = 'BASEINSTALLCOMPREHENSIVEFEE';
                    }else if ('qitazonmghefei' == model.subordinatecategory) {
                        projectMaterial.categoryDetailCode = 'OTHERCATEGORIESOFSMALLFEES';
                    }else if ('laofangchaichu' == model.subordinatecategory) {
                        projectMaterial.categoryDetailCode = 'DISMANTLEBASEINSTALLQUOTA';
                    }else if ('laofangzonghefei' == model.subordinatecategory) {
                        projectMaterial.categoryDetailCode = 'DISMANTLEBASEINSTALLCOMPFEE';
                    }else if ('laofangqitazonghefei' == model.subordinatecategory) {
                        projectMaterial.categoryDetailCode = 'DISMANTLEOTHERCOMPFEE';
                    }
                    projectMaterial.quotaDescribe = model.projectintemdetail;
                    projectMaterial.productName = model.projectintemname;
                    projectMaterial.categoryCode = self.projectIntemMold;//第一大类（增项，减项等）
                    projectMaterial.storeSalePrice = model.projectintemprice;
                    projectMaterial.materialUnit = model.projectintemunit;
                    var storeSalePrice = '';
                    var projectProportion = '';
                    if ('fixedUnitPrice' == model.valuationmethod) {
                        storeSalePrice = model.projectintemprice;
                    }else{
                        projectProportion = model.projectintemprice;
                    }
                    projectMaterial.skuDosageList = [{
                                 domainName : model.projectintemtypename,
                                 storeSalePrice: storeSalePrice,
                                 storeIncreasePrice: storeSalePrice,
                                 storeReducePrice: storeSalePrice,
                                 storePurchasePrice:model.projectintemcostprice,
                                 projectProportion: projectProportion,
                                 dosagePricingMode: model.valuationmethod,
                                 lossDosage:model["dosage"] = $("#" + model.id).val() || 1,
                                 noLossDosage:model["dosage"] = $("#" + model.id).val() || 1,
                                 lossFactor:1
                    }];
                    console.log(projectMaterial)
                    self.$http.post('/material/projectmaterial/saveprojectintem', projectMaterial).then(function (res) {
                        if (res.data.code == 1) {
                            materialIndex.totalAmountFlag=true;
                            projectMaterial.id = res.data.data.projectMaterialId;
                            projectMaterial.skuDosageList[0].id = res.data.data.skuDosageId;
                            self.$dispatch('otherIntem', projectMaterial);
                            self.$toastr.success('添加定额成功!');
                        }else{
                            self.$toastr.error("添加定额失败!")
                        }
                    })
                }
            );
        },
        // end of 渲染datatable
    }
});