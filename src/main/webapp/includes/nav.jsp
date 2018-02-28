<%@ page language="java" pageEncoding="UTF-8" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!--左侧导航开始-->
<nav id="nav" class="navbar-default navbar-static-side"
     role="navigation">
    <div class="nav-close">
        <i class="fa fa-times-circle"></i>
    </div>
    <div id="navUser" class="sidebar-collapse">
        <div class="nav-header">
            <div>
                <div class="dropdown profile-element">
                    <span>
                        <img id="headimgurl" src='/static/business/image/timg.jpg' class="img-circle"
                             width="60px" height="60px" alt="个人微信图像"/>
					</span>
                    <a data-toggle="dropdown" class="dropdown-toggle" href="#"> <span
                            class="clear"> <span class="block m-t-xs"> <strong
                            class="font-bold"><shiro:principal property="name"/></strong>
						</span>
					</span>
                    </a>
                </div>
            </div>
        </div>
        <!-- 左侧菜单 start-->
        <ul class="nav metismenu" id="sideMenu">

            <li id="homeMenu">
                <a href="/index">
                    <i class="fa fa-home"></i>
                    <span class="nav-label">主页</span>
                </a>
            </li>

            <%--<shiro:hasPermission name="employee:menu">--%>
            <%--<li id="regulationMenu">
                <a href="#"> <i class="fa fa-edit"></i>
                    <span class="nav-label">用户管理</span>
                    <span class="fa arrow"></span>
                </a>
                <ul class="nav nav-second-level sidebar-nav">
                    <li id="commonList">
                        <a href="/admin/employee/list?source=commonList">
                            <i class="fa fa-edit"></i> <span class="nav-label">用户列表</span>
                        </a>
                    </li>
                </ul>
            </li>--%>
            <%--</shiro:hasPermission>--%>

            <%--选材管理--%>
            <shiro:hasPermission name="materialSelection:menu">
                <li id="selectMaterialManage">
                    <a href="/business/customercontract/customercontract">
                        <i class="fa fa fa-bar-chart-o"></i>
                        <span class="nav-label">选材管理</span>
                    </a>
                </li>
            </shiro:hasPermission>

            <shiro:hasPermission name="materialChange:menu">
                <li id="changeMange">
                    <a href="#"> <i class="fa fa-edit"></i>
                        <i class="nav-label"></i>
                        <span class="nav-label">变更管理</span>
                        <span class="fa arrow"></span>
                    </a>
                    <ul class="nav nav-second-level sidebar-nav">
                        <shiro:hasPermission name="materialChange:management">
                            <li id="changeManage">
                                <a href="/business/changemange/changemange">
                                    <i class="fa fa fa-bar-chart-o"></i>
                                    <span class="nav-label">变更管理</span>
                                </a>
                            </li>
                        </shiro:hasPermission>
                    </ul>

                    <ul class="nav nav-second-level sidebar-nav">
                        <shiro:hasPermission name="materialChange:singlemanage">
                            <li id="changeOrderMange">
                                <a href="/material/changeordermange">
                                    <i class="fa fa fa-bar-chart-o"></i>
                                    <span class="nav-label">变更单管理</span>
                                </a>
                            </li>
                        </shiro:hasPermission>
                    </ul>


                    <ul class="nav nav-second-level sidebar-nav">
                        <shiro:hasPermission name="changeAudit:menu">
                            <li id="changeAudit">
                                <a href="/material/changeaudit">
                                    <i class="fa fa fa-bar-chart-o"></i>
                                    <span class="nav-label">变更审核</span>
                                </a>
                            </li>
                        </shiro:hasPermission>
                    </ul>

                    <ul class="nav nav-second-level sidebar-nav">
                        <shiro:hasPermission name="changeAuditCollect:menu">
                            <li id="changeAuditCollect">
                                <a href="/business/material/auditCollect">
                                    <i class="fa fa fa-bar-chart-o"></i>
                                    <span class="nav-label">变更汇总</span>
                                </a>
                            </li>
                        </shiro:hasPermission>
                    </ul>
                </li>
            </shiro:hasPermission>

            <shiro:hasPermission name="mealInfoMenu:menu">
                <li id="mealInfoMenu">
                    <a href="#"> <i class="fa fa-edit"></i>
                        <span class="nav-label">套餐管理</span>
                        <span class="fa arrow"></span>
                    </a>
                    <ul class="nav nav-second-level sidebar-nav">
                        <shiro:hasPermission name="mealInfoMenu:list">
                            <li id="mealInfo">
                                <a href="/mealinfo/meallist">
                                    <i class="fa fa-edit"></i> <span class="nav-label">套餐列表</span>
                                </a>
                            </li>
                        </shiro:hasPermission>
                    </ul>
                </li>
            </shiro:hasPermission>

            <shiro:hasPermission name="regulationMenu:menu">
                <li id="regulationMenu">
                    <a href="#"> <i class="fa fa-edit"></i>
                        <span class="nav-label">功能区管理</span>
                        <span class="fa arrow"></span>
                    </a>
                    <ul class="nav nav-second-level sidebar-nav">
                        <shiro:hasPermission name="regulationMenu:list">
                            <li id="commonList">
                                <a href="/domaininfo/list">
                                    <i class="fa fa-edit"></i> <span class="nav-label">功能区列表</span>
                                </a>
                            </li>
                        </shiro:hasPermission>
                    </ul>
                </li>
            </shiro:hasPermission>

            <shiro:hasPermission name="product:menu">
                <li id="commodityMenu">
                    <a href="#"> <i class="fa fa-edit"></i>
                        <span class="nav-label">装修商品</span>
                        <span class="fa arrow"></span>
                    </a>
                    <ul class="nav nav-second-level sidebar-nav">
                        <shiro:hasPermission name="product:price">
                            <li id="proSkuPrice">
                                <a href="/material/prodSkuPriceList">
                                    <i class="fa fa-edit"></i> <span class="nav-label">价格管理</span>
                                </a>
                            </li>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="product:label">
                            <li id="label">
                                <a href="/material/labelList">
                                    <i class="fa fa-edit"></i> <span class="nav-label">标签管理</span>
                                </a>
                            </li>
                        </shiro:hasPermission>
                    </ul>
                </li>
            </shiro:hasPermission>
            <shiro:hasPermission name="order:menu">
                <li id="orderMenu">
                    <a href="#">
                        <i class="fa fa fa-bar-chart-o"></i>
                        <span class="nav-label">订单管理</span>
                        <span class="fa arrow"></span>
                    </a>
                    <ul class="nav nav-second-level sidebar-nav">
                        <shiro:hasPermission name="order:list">
                            <li id="singleMenu">
                                <a href="/orderflow/order/list">
                                    <i class="fa fa-edit"></i> <span class="nav-label">订单列表</span>
                                </a>
                            </li>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="order:stringsingle">
                            <li id="singleTag">
                                <a href="/singletag/singletaglist">
                                    <i class="fa fa-edit"></i> <span class="nav-label">串单管理</span>
                                </a>
                            </li>
                        </shiro:hasPermission>
                    </ul>
                </li>
            </shiro:hasPermission>
            <shiro:hasPermission name="finance:manage">
            <li id="financeManageMenu">
                <a href="#">
                    <i class="fa fa fa-bar-chart-o"></i>
                    <span class="nav-label">财务管理</span>
                    <span class="fa arrow"></span>
                </a>
                <ul class="nav nav-second-level sidebar-nav">
                    <shiro:hasPermission name="money:manage">
                    <li id="paymentMenu">
                        <a href="/finance/payment/index">
                            <i class="fa fa-edit"></i> <span class="nav-label">收/退款管理</span>
                        </a>
                    </li>
                    </shiro:hasPermission>
                    <shiro:hasPermission name="finance:config">
                    <li id="queryfinapayme">
                        <a href="/finance/finapaymethod/queryfinapaymethodlist">
                            <i class="fa fa-edit"></i> <span class="nav-label">财务配置</span>
                        </a>
                    </li>
                    </shiro:hasPermission>
                </ul>
            </li>
            </shiro:hasPermission>

            <shiro:hasPermission name="finance:query">
            <li id="financeQueryMenu">
                <a href="#">
                    <i class="fa fa fa-bar-chart-o"></i>
                    <span class="nav-label">财务查询</span>
                    <span class="fa arrow"></span>
                </a>
                <ul class="nav nav-second-level sidebar-nav">
                    <shiro:hasPermission name="finadetail:list">
                    <li id="projectsummarizMenu">
                        <a href="/finance/analyze/projectsummarizlist">
                            <i class="fa fa-edit"></i> <span class="nav-label">项目汇总查询</span>
                        </a>
                    </li>
                    </shiro:hasPermission>
                    <shiro:hasPermission name="finaoutline:list">
                    <li id="compositeMenu">
                        <a href="/finance/analyze/projectcompositelist">
                            <i class="fa fa-edit"></i> <span class="nav-label">综合查询</span>
                        </a>
                    </li>
                    </shiro:hasPermission>
                    <shiro:hasPermission name="payrecord:list">
                    <li id="paymentrecordMenu">
                        <a href="/finance/analyze/paymentrecordlist">
                            <i class="fa fa-edit"></i> <span class="nav-label">交款记录查询</span>
                        </a>
                    </li>
                    </shiro:hasPermission>
                </ul>
            </li>
            </shiro:hasPermission>

            <shiro:hasPermission name="finance:print">
            <li id="financePrintMenu">
                <a href="#">
                    <i class="fa fa fa-bar-chart-o"></i>
                    <span class="nav-label">财务打印</span>
                    <span class="fa arrow"></span>
                </a>
                <ul class="nav nav-second-level sidebar-nav">
                    <shiro:hasPermission name="majorchange:print">
                    <li id="materialChangeMenu">
                        <a href="/finance/print/changelist">
                            <i class="fa fa-edit"></i> <span class="nav-label">主材变更单打印</span>
                        </a>
                    </li>
                    </shiro:hasPermission>
                    <shiro:hasPermission name="basechange:print">
                    <li id="baseMaterialChangeConfigMenu">
                        <a href="/finance/print/baselist">
                            <i class="fa fa-edit"></i> <span class="nav-label">基装变更单打印</span>
                        </a>
                    </li>
                    </shiro:hasPermission>
                    <shiro:hasPermission name="receipt:print">
                    <li id="receiptPrintMenu">
                        <a href="/finance/print/receiptlist">
                            <i class="fa fa-edit"></i> <span class="nav-label">收据打印</span>
                        </a>
                    </li>
                    </shiro:hasPermission>
                </ul>
            </li>
            </shiro:hasPermission>
            <li id="updatePassword">
                <a href="/modifyPassword">
                    <i class="fa fa-edit"></i>
                    <span class="nav-label">修改密码</span>
                </a>
            </li>

        </ul>
        <!-- 左侧菜单 end-->
    </div>
</nav>
<!--左侧导航结束-->

<%--处理图片!--%>
<script>
    if('<shiro:principal property="headimgurl"/>' != 'null'){
        $("#headimgurl").attr('src', '<shiro:principal property="headimgurl"/>');
    }
</script>