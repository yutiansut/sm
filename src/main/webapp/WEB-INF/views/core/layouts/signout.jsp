<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="sitemesh" uri="http://www.opensymphony.com/sitemesh/decorator" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%--<%@ taglib uri="http://shiro.apache.org/tags" prefix="shiro"%>--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
  <meta charset="UTF-8">
  <meta name="keywords" content="美得你,平台,后台管理,登录">
  <meta name="description" content="美得你平台管理平台登录">
  <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=0">
  <meta name="renderer" content="webkit">
  <meta http-equiv="Cache-Control" content="no-siteapp" />
  <title><sitemesh:title/></title>

  <!-- style -->
  <link rel="stylesheet" href="${ctx}/static/core/css/lib.css">
  <link rel="stylesheet" href="${ctx}/static/core/css/style.css">




  <script src="${ctx}/static/core/js/lib.js"></script>
  <%@include file="/WEB-INF/views/core/shims/polyfill.jsp" %>
  <%@include file="/WEB-INF/views/core/shims/config.jsp" %>

  <!-- 页面公用 -->
  <script>
  var ctx = '${ctx}';
  </script>

  <!-- 每页特殊样式特殊js-->
  <sitemesh:head/>
</head>
<body>
  <!-- 添加组件模板到这里 -->
  <!--右侧部分开始-->
  <sitemesh:body/>
  <!--右侧部分结束-->
</body>
</html>