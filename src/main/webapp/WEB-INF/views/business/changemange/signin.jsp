<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
  <meta charset="UTF-8">
  <meta name="keywords" content="">
  <meta name="description" content="">
  <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=0">
  <meta name="renderer" content="webkit">
  <meta http-equiv="Cache-Control" content="no-siteapp"/>
  <!-- style -->
  <link rel="stylesheet" href="${ctx}/static/core/css/lib.css">
  <link rel="stylesheet" href="${ctx}/static/core/css/style.css">
  <style>
    #breadcrumb {
      height: 35px;
    }
  </style>

  <script src="${ctx}/static/core/js/lib.js"></script>
  <%--<%@include file="/WEB-INF/views/core/shims/polyfill.jsp" %>--%>
  <%--<%@include file="/WEB-INF/views/core/shims/config.jsp" %>--%>

  <!-- 页面公用 -->
  <script src="${ctx}/static/core/js/main.js"></script>
  <script src="${ctx}/static/core/js/mixins/mixins.js"></script>
  <script src="${ctx}/static/core/js/filters/filters.js"></script>
  <script src="${ctx}/static/core/js/components/breadcrumb.js"></script>
  <script src="${ctx}/static/core/js/components/header.js"></script>
  <script src="${ctx}/static/core/js/utils.js"></script>
  <script src="${ctx}/static/core/js/directives/clickoutside.js"></script>
  <script>
    var ctx = '${ctx}';
  </script>

  <!-- 每页特殊样式特殊js-->
</head>
<body id="app" class="fixed-sidebar full-height-layout gray-bg" style="overflow-y:scroll">
<!-- 添加组件模板到这里 -->
<%@include file="/WEB-INF/views/core/components/breadcrumb.jsp" %>
</body>
</html>