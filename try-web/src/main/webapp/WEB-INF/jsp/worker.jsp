<%@ page language="java" isELIgnored="false" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
    <meta charset="utf-8">
    <title>worker管理</title>
    <link rel="stylesheet" href="../../css/index-worker.css"/>
    <script src="../../js/lib/jquery/jquery-1.6.min.js"></script>
</head>
<body>
    <div id="main">
        <div id="workerHeader">
            <span id="title">◤通用全量索引Worker</span>
        </div>
        <div id="allindex">
            <table>
                <tr>
                    <th>任务名称</th>
                    <th>任务描述</th>
                    <th>任务参数</th>
                    <th>操作</th>
                </tr>
                <tr>
                    <td>GatherWorker</td>
                    <td>通用全量索引任务</td>
                    <td>worker类型：
                        <select id="gatherType" class="selectType">
                            <option value="0">请选择...</option>
                            <c:forEach items="${gatherList}" var="gather">
                                <option value="<c:out value="${gather.id}"></c:out>"><c:out
                                        value="${gather.instruction}"></c:out></option>
                            </c:forEach>

                        </select>

                        库：
                        <select id="gatherDb" class="selectType">
                        </select>

                        时间从: <input type="text" id="fromText"/>
                        到：<input type="text" id="toText"/>
                    </td>
                    <td><input type="button" id="gatherWorkerButton" value="开启WOKER"/></td>
                </tr>
                <tr>
                    <td>重新索引记录</td>
                    <td>重新索引记录</td>
                    <td>
                        <div>
                            Index:
                            <select id="reindexType" class="selectType">
                                <option value="0">请选择...</option>
                                <c:forEach items="${gatherList}" var="gather">
                                    <option value="<c:out value="${gather.id}"></c:out>"><c:out
                                            value="${gather.instruction}"></c:out></option>
                                </c:forEach>
                            </select>
                            DB:
                            <select id="reindexDb" class="selectType">
                                <option value="0">请选择...</option>
                            </select>
                            tableName: <input type="text" id="tbName"/> <br/>
                            IDs: <input type="text" id="reindexIdsText" class="idsText"/>
                        </div>
                    </td>
                    <td><input type="button" id="reindexButton" value="重新索引"/></td>
                </tr>
                <tr>
                    <td>删除索引记录</td>
                    <td>删除索引记录</td>
                    <td>index类型：
                        <select id="deleteType" class="selectType">
                            <option value="0">请选择...</option>
                            <c:forEach items="${gatherList}" var="gather">
                                <option value="<c:out value="${gather.id}"></c:out>"><c:out
                                        value="${gather.instruction}"></c:out></option>
                            </c:forEach>
                        </select>
                        IDs: <input type="text" id="deleteIdsText" class="idsText"/>
                    </td>
                    <td><input type="button" id="deleteButton" value="删除索引"/></td>
                </tr>
            </table>
        </div>
        <div id="prompt"></div>
    </div>
</body>
<script>
    $(document).ready(function() {
        var workerRunning = false;
        var workerButton = $('#gatherWorkerButton');
        var promptElement = $('#prompt');

        var getWorkerState = function() {
            $.ajax({
                url: '/worker/getWorkerState',
                type: "GET",
                timeout: 5000,
                success: function(data){
                    parseWorkerStateData(data);
                    updateWorkerButtonState();
                },
                error: function(xhr, status, err) {
                    prompt('获取worker状态失败！' + err);
                }
            });
        };

        var openWorker = function() {
            clearPrompt();

            var type = $('#gatherType').val();
            if (type === '0') {
                prompt('请先选择worker类型！');
                return;
            }

            var db = $('#gatherDb').val();
            var from = $('#fromText').val();
            var to = $('#toText').val();

            var data = {
                "type": type,
                "db": db,
                "from": from,
                "to": to
            };

            workerButton.attr('disabled', true);
            $.ajax({
                url: '/worker/start',
                type: 'GET',
                timeout: 5000,
                data: data,
                success: function(data){
                    if (data.taskCount > 0) {
                        prompt('Worker开启成功！');
                    } else {
                        prompt('Worker开启失败！');
                    }
                    workerButton.attr('disabled', false);
                    parseWorkerStateData(data);
                    updateWorkerButtonState();
                },
                error: function(xhr, status, err) {
                    prompt('开启Worker失败！' + err);
                    workerButton.attr('disabled', false);
                }
            });
        };

        var closeWorker = function() {
            clearPrompt();

            workerButton.attr('disabled', true);
            $.ajax({
                url: '/worker/stop',
                type: 'GET',
                timeout: 5000,
                success: function(data){
                    prompt('关闭Worker成功！')
                    parseWorkerStateData(data);
                    workerButton.attr('disabled', false);
                    updateWorkerButtonState();
                },
                error: function(xhr, status, err) {
                    prompt('关闭Worker失败！' + err);
                    workerButton.attr('disabled', false);
                }
            });
        };

        var reindex = function() {
            clearPrompt();

            var type = $('#reindexType').val();
            if (type === '0') {
                prompt('请先选择index类型！');
                return;
            }

            var ids = $('#reindexIdsText').val();
            if (!ids) {
                prompt('请输入ids！');
                return;
            }

            var dbName = $('#reindexDb').val();
            if (dbName === '0') {
                prompt('请先选择数据库！');
                return;
            }

            var tbName = $('#tbName').val();
            if (!tbName) {
                prompt('请输入表序号！');
                return;
            }

            var data = {
                'type': type,
                'dbName': dbName,
                'tbName': tbName,
                'ids': ids
            }

            $.ajax({
                url: '/worker/reindex',
                type: 'GET',
                data: data,
                timeout: 5000,
                success: function(data){
                    parseWorkerStateData(data);
                    prompt('重新索引成功！');
                },
                error: function(xhr, status, err) {
                    prompt('重新索引失败！' + err);
                }
            });
        };

        var deleteIndex = function() {
            clearPrompt();

            var type = $('#deleteType').val();
            if (type === '0') {
                prompt('请先选择index类型！');
                return;
            }

            var ids = $('#deleteIdsText').val();
            if (!ids) {
                prompt('请输入ids！');
                return;
            }

            var data = {
                'type': type,
                'ids': ids
            }

            $.ajax({
                url: '/worker/delete',
                type: 'GET',
                data: data,
                timeout: 5000,
                success: function(data){
                    parseWorkerStateData(data);
                    prompt('删除成功！');
                },
                error: function(xhr, status, err) {
                    prompt('删除失败！' + err);
                }
            });
        };

        var parseWorkerStateData = function(data) {
            workerRunning = (data.taskCount > 0);
        };

        var updateWorkerButtonState = function() {
            if (workerRunning) {
                workerButton.attr('value', '关闭Worker');
            } else {
                workerButton.attr('value', '开启Worker');
            }
        };

        var clearPrompt = function() {
            promptElement.html('');
        };

        var prompt = function(info) {
            promptElement.html(info);
            setTimeout(clearPrompt, 5000);
        };

        $('#gatherWorkerButton').click(function() {
            if (workerRunning) {
                closeWorker();
            } else {
                openWorker();
            }
        });

        $('#reindexButton').click(function() {
            reindex();
        });

        $('#deleteButton').click(function() {
            deleteIndex();
        });

        $('#gatherType').change(function(){
            var type = $(this).children('option:selected').val();

            if(type == 0){
                $("#gatherDb").empty();
            }else{
                $.ajax({
                    url: '/worker/getDbNameTableCountMap',
                    type: 'GET',
                    timeout: 5000,
                    data: {
                        'type': type
                    },
                    success: function(dbNameTableCountMap){
                        $("#gatherDb").empty();
                        $("#gatherDb").append("<option value=\"-1\" >all</option>");
                        $.each(dbNameTableCountMap,function(key,values){
                            $("#gatherDb").append("<option value="+key+">"+key+"</option>");
                        });
                    },
                    error: function(xhr, status, err) {
                        prompt('请求失败！' + err);
                    }
                });
            }
        });

        $('#reindexType').change(function(){
            var type = $(this).children('option:selected').val();
            if(type == 0){
                $("#reindexDb").empty();
                $("#reindexDb").append("<option value=\"0\" >请选择...</option>");
            }else{
                $.ajax({
                    url: '/worker/getDbNameTableCountMap',
                    type: 'GET',
                    timeout: 5000,
                    data: {
                        'type': type
                    },
                    success: function(dbNameTableCountMap){
                        $("#reindexDb").empty();
                        $("#reindexDb").append("<option value=\"0\" >请选择...</option>");
                        $.each(dbNameTableCountMap,function(key,values){
                            $("#reindexDb").append("<option value="+key+">"+key+"</option>");
                        });
                    },
                    error: function(xhr, status, err) {
                        prompt('请求失败！' + err);
                    }
                });
            }

        });

        getWorkerState();
        setInterval(getWorkerState, 60000);
    });
</script>
</html>