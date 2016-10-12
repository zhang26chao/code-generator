<%@ page language="java" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix='fmt' uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="/tags/mock" prefix="mk" %>
<!DOCTYPE html>
<html lang="en">
<head>
</head>
<body>
<nav class="navbar navbar-default">
    <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
        <ul class="nav navbar-nav">
            <li><a href="<%=request.getContextPath()%>/code/list.do"><span
                    class="glyphicon glyphicon-home">列表</span></a>
            </li>
        </ul>
        <ul class="nav navbar-nav">
            <li><a href="<%=request.getContextPath()%>/code/create.do"><span
                    class="glyphicon glyphicon-plus">新增</span></a></li>
        </ul>
        <form class="navbar-form navbar-right" role="search">
            <div class="input-group">
                <input type="text" class="form-control" name="ip" value="${param.ip}" placeholder="ip"/>
                <span class="input-group-btn">
                    <button type="submit" class="btn btn-default">查询</button>
                </span>
            </div>
        </form>
    </div>
</nav>
<div id="msg" style="margin-bottom: 20px;padding:5px;background: #FFFFCC;display: none;"></div>
<table class="table table-condensed table-bordered table-striped" style="margin-bottom: 0px;">
    <thead>
    <tr>
        <th>类型</th>
        <th>URL</th>
        <th>用户名</th>
        <th>生成代码</th>
        <th>操作</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${list}" var="database">
        <tr>
            <td>${database.type}</td>
            <td>${database.url}</td>
            <td>${database.username}</td>
            <td>
                <input type="button" class="btn btn-success btn-sm" value="部分生成" onclick="generate('${database.id}')"/>
                <input type="button" class="btn btn-primary btn-sm" value="全部生成" onclick="generateAll(this,'${database.id}')"/>
            </td>
            <td>
                <input type="button" class="btn btn-warning btn-sm" value="编辑" onclick="edit('${database.id}')"/>
                <input type="button" class="btn btn-danger btn-sm" value="删除" onclick="del('${database.id}')"/>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<script>

    function generate(id) {
        window.location = '<%=request.getContextPath()%>/code/toGenerate.do?id=' + id;
    }

    function generateAll(btn,id) {
        if (!window.confirm('确认要生成数据库所有表的代码吗?')) {
             return;
        }
        var xhr = window.XMLHttpRequest ? new XMLHttpRequest() : window.ActiveXObject ? new ActiveXObject("Microsoft.XMLHTTP") : null;
        if (xhr) {
            btn.disabled = 'disabled';
            btn.value = '代码生成中...';
            document.getElementById('msg').innerHTML = '正在生成代码，请稍等片刻:)';
            document.getElementById('msg').style.display = 'block';
            xhr.open('GET', '<%=request.getContextPath()%>/code/generate.do?id=' + id+'&tableName=all');
            xhr.onreadystatechange = function () {
                if (xhr.readyState == 4 && xhr.status == 200) {
                    document.getElementById('msg').innerHTML = xhr.responseText;
                    btn.disabled = false;
                    btn.value = '全部生成';
                    xhr = null;
                }
            };
            xhr.send();
        }
    }

    function del(id) {
        if (window.confirm('确认要删除吗?')) {
            window.location = '<%=request.getContextPath()%>/code/delete.do?id=' + id;
        }
    }

    function edit(id) {
        window.location = '<%=request.getContextPath()%>/code/edit.do?id=' + id;
    }

</script>
<mk:paginator/>
</body>
</html>