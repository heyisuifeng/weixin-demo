<%@page pageEncoding="utf-8" %>
<%@ page import="java.util.Date" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="time" value="<%=new Date().getTime()%>"/>
<c:set var="version" value="?v=${time}"/>
<%-- 项目路径 --%>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<script>
    var basePath = "${pageContext.request.contextPath}";
    var _ctx = "${ctx}";
    _ctx = _ctx == null || _ctx == "/" ? "" : _ctx;
    var _version = "${time}";
</script>
