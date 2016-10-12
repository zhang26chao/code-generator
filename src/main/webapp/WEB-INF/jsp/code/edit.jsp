<%@ page language="java" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
</head>
<body>
<c:if test="${result!= null && result != ''}">
    <div style="background: #FFFFCC;padding: 5px;">${result}</div>
</c:if>
<div class="starter-template">
    <form class="form-horizontal" action="<%=request.getContextPath()%>/code/update.do" method="post"
          role="form" onsubmit="return checkForm();">
        <input type="hidden" name="id" value="${database.id}">

        <div class="form-group">
            <label for="type" class="col-sm-2 control-label">数据库类型</label>

            <div class="col-sm-10">
                <select id="type" name="type" class="form-control">
                    <option value="DB2">DB2</option>
                    <option value="MySQL">MySQL</option>
                </select>
            </div>
        </div>
        <div class="form-group">
            <label for="url" class="col-sm-2 control-label">URL</label>

            <div class="col-sm-10">
                <input type="text" style="border: 1px solid red;" class="form-control" id="url" name="url"
                       value="${database.url}"
                       placeholder="url地址">
            </div>
        </div>
        <div class="form-group">
            <label for="username" class="col-sm-2 control-label">用户名</label>

            <div class="col-sm-10">
                <input type="text" class="form-control" style="border: 1px solid red;" id="username" name="username"
                       value="${database.username}"
                       placeholder="用户名">
            </div>
        </div>
        <div class="form-group">
            <label for="password" class="col-sm-2 control-label">密码</label>

            <div class="col-sm-10">
                <input type="password" style="border: 1px solid red;" class="form-control" id="password"
                       name="password" value="${database.password}"
                       placeholder="密码">
            </div>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <button type="submit" class="btn btn-default">保存</button>
            </div>
        </div>
    </form>
</div>
<script>
    function checkForm() {
        var url = document.getElementById('url');
        if (!url.value) {
            alert('url地址不允许为空!');
            url.focus();
            return false;
        }
        var username = document.getElementById('username');
        if (!username.value) {
            alert('用户名不允许为空!');
            username.focus();
            return false;
        }
        var password = document.getElementById('password');
        if (!password.value) {
            alert('密码不允许为空!');
            password.focus();
            return false;
        }
        return true;
    }

    window.onload = function () {
        var setFun = function (id, value) {
            var options = document.getElementById(id).options;
            for (var i = 0, len = options.length; i < len; i++) {
                if (options[i].value == value) {
                    options[i].selected = true;
                    break;
                }
            }
        };
        setFun('type', '${database.type}');
    };
</script>
</body>
</html>