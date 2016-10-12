<%@ page import="java.io.ByteArrayOutputStream" %>
<%@ page import="java.io.PrintStream" %>
<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 15-6-14
  Time: 上午9:47
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>SSF Mock平台</title>
    <link rel="shortcut icon" type="image/ico" href="<%=request.getContextPath()%>/favicon.ico">
</head>
<body>
系统异常，<a href="<%=request.getContextPath()%>/">返回首页</a><br/>
<%
    Throwable throwable = (Throwable) request.getAttribute("javax.servlet.error.exception");
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    throwable.printStackTrace(new PrintStream(baos));
    out.println(baos.toString().replaceAll("\r\n", "<br/>"));
    baos.close();
%>
</body>
</html>