<%@ page language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
</head>
<body>
<div id="msg" style="margin-bottom: 20px;padding:5px;background: #FFFFCC;">
    <p>使用说明：</p>

    <p>
        1.点击选择表文本框，稍等片刻:)，将会加载数据库的所有表。然后选择一张或多张表(Ctrl or Shift)，点击生成。
    </p>

    <p>2.直接输入表名后点击生成。输入表的优先级高于选择表。</p>
    <p>3.生成文件的下载地址为：http://10.27.103.227/code/文件名称。如果是单表，则文件名称是"项目名称.表名称.zip"；如果是多表，则文件名称是"项目名称.zip"</p>
</div>
<div class="starter-template">
    <form class="form-horizontal">
        <div class="form-group">
            <label for="selectTableName" class="col-sm-2 control-label">选择表</label>

            <div class="col-sm-10">
                <select id="selectTableName" name="selectTableName" multiple="multiple" class="form-control"
                        onclick="loadTable()">
                </select>
            </div>
        </div>
        <div class="form-group">
            <label for="inputTableName" class="col-sm-2 control-label">输入表</label>

            <div class="col-sm-10">
                <textarea class="form-control" id="inputTableName" name="inputTableName"
                          placeholder="多个表之间用逗号隔开">${param.inputTableName}</textarea>
            </div>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <input type="button" class="btn btn-default" onclick="generate(this)" value="生成"/>
            </div>
        </div>
    </form>
</div>
<script>

    function generate(btn) {
        var tableName = document.getElementById('inputTableName').value;
        if (!tableName) {
            var selectOpt = [];
            var select = document.getElementById('selectTableName');
            for (i = 0, len = select.length; i < len; i++) {
                if (select.options[i].selected) {
                    selectOpt.push(select.options[i].value);
                }
            }
            if (selectOpt.length == 0) {
                alert('选择表和输入表必须有一个不为空!');
                return;
            }
            tableName = selectOpt.join(',');
        }
        var xhr = window.XMLHttpRequest ? new XMLHttpRequest() : window.ActiveXObject ? new ActiveXObject("Microsoft.XMLHTTP") : null;
        if (xhr) {
            btn.disabled = 'disabled';
            btn.value = '代码生成中...';
            document.getElementById('msg').innerHTML = '正在生成代码，请稍等片刻:)';
            xhr.open('GET', '<%=request.getContextPath()%>/code/generate.do?id=${param.id}&tableName=' + tableName);
            xhr.onreadystatechange = function () {
                if (xhr.readyState == 4 && xhr.status == 200) {
                    document.getElementById('msg').innerHTML = xhr.responseText;
                    btn.disabled = false;
                    btn.value = '生成';
                    xhr = null;
                }
            };
            xhr.send();
        }
    }

    var tables = null;
    var loading = false;
    function loadTable() {
        if (tables) {
            return;
        }
        if (loading) {
            return;
        }
        var xhr = window.XMLHttpRequest ? new XMLHttpRequest() : window.ActiveXObject ? new ActiveXObject("Microsoft.XMLHTTP") : null;
        if (xhr) {
            loading = true;
            document.getElementById('msg').innerHTML = '正在加载表，请稍等片刻:)';
            xhr.open('GET', '<%=request.getContextPath()%>/code/loadTable.do?id=${param.id}');
            xhr.onreadystatechange = function () {
                if (xhr.readyState == 4 && xhr.status == 200) {
                    var responseText = xhr.responseText;
                    var json = window.eval('(' + responseText + ')');
                    if (json.error) {
                        alert(json.msg);
                    } else {
                        tables = json;
                        var select = document.getElementById('selectTableName');
                        select.innerHTML = '';
                        for (var i = 0 , len = tables.length; i < len; i++) {
                            var option = document.createElement('option');
                            option.text = option.value = tables[i];
                            select.appendChild(option);
                        }
                    }
                    xhr = null;
                    loading = false;
                    document.getElementById('msg').innerHTML = '表加载完成，请选择';
                }
            };
            xhr.send();
        }
    }

</script>
</body>
</html>