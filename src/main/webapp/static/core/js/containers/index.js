var vueIndex = null;
+(function (Vue, $, _, moment, DameiUtils) {
    $('#homeMenu').addClass('active');
    vueIndex = new Vue({
        el: '#container',
        mixins: [DameiVueMixins.DataTableMixin],
        data: {
            _$dataTable: null // datatable $对象
        },
        methods: {},
        created: function () {
        },
        ready: function () {
        }
    });


})(Vue, jQuery, _, moment, DameiUtils);

