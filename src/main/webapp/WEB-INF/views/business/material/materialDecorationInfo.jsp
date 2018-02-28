<%--装修工程jsp--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<head>
    <link rel="stylesheet" href="/static/core/css/tab.css">
</head>
    <div id="materialDecorationInfoContainer" class="wrapper wrapper-content animated fadeInRight">
        <!-- ibox start -->
        <div class="ibox">
            <div class="ibox-content">
                <div>
                    <Tabs id="tab" v-ref:tabs type="card" @click="clickEvent(this)">
                        <Tab-pane v-for="($index,item) in tabList" :label="item.label">
                            <package-standard-component v-if="$index == 0" :msg="loadSuccFlag" ></package-standard-component>
                            <upgrade-item-component v-if="$index == 1" ></upgrade-item-component>
                            <add-item-component v-if="$index == 2" ></add-item-component>
                            <reduce-item-component v-if="$index == 3" ></reduce-item-component>
                            <other-money-component v-if="$index == 4" ></other-money-component>
                            <other-comp-fee-component v-if="$index == 5" ></other-comp-fee-component>
                        </Tab-pane>
                    </Tabs>
                </div>
            </div>
        </div>
        <!-- ibox end -->
    </div>

    <script src="${ctx}/static/core/js/components/tab.js"></script>

