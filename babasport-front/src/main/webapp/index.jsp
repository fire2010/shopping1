<%@page import="java.net.URLEncoder"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!-- 重定向到检索页面 -->
<%response.sendRedirect("/product/display/list.shtml?keyword=" + URLEncoder.encode("瑜伽服", "UTF-8")); %>
