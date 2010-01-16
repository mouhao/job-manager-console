<%--
  Created by IntelliJ IDEA.
  User: SongJiao
  Date: 2010-1-15
  Time: 23:24:10
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head><title>TriggerJob</title>
    <style type="text/css">
        @import url('<c:url value="/styles/table/style.css"/>');
    </style>
    <script type="text/javascript" src="${pageContext.request.contextPath}/components/jquery/jquery-1.3.2.min.js">
    </script>
    <script type="text/javascript">
        function doCmd(state, triggerName, group, triggerState) {

            if (state == 'pause' && triggerState == 'STATE_PAUSED') {
                alert("该Trigger己经暂停！");
                return;
            }

            if (state == 'resume' && triggerState != 'STATE_PAUSED') {
                alert("该Trigger正在运行中！");
                return;
            }

            //客户端两次编码，服务端再解码，否测中文乱码
            // triggerName = encodeURIComponent(encodeURIComponent(triggerName));
            //group = encodeURIComponent(encodeURIComponent(group));
            if (state == "pause") {
                $.ajax({
                    url: 'pauseTrigger.do?triggerName=' + triggerName + '&group=' + group,
                    type: 'post',
                    //dataType: 'xml',
                    // timeout: 3000,
                    error: function() {
                        alert("执行失败！");
                    },
                    success: function(xml) {
                        if (xml == 0) {
                            alert("执行成功！");
                            window.location.reload();
                        } else {
                            alert("执行失败！");
                        }
                    }
                });

            }

            if (state == "resume") {

                $.ajax({
                    url: 'resumeTrigger.do?triggerName=' + triggerName + '&group=' + group,
                    type: 'post',
                    //dataType: 'xml',
                    // timeout: 3000,
                    error: function() {
                        alert("执行失败！");
                    },
                    success: function(xml) {
                        if (xml == 0) {
                            alert("执行成功！");
                            window.location.reload();
                        } else {
                            alert("执行失败！");
                        }
                    }
                });


            }

            if (state == "remove") {
                $.ajax({
                    url: 'removeTrigger.do?triggerName=' + triggerName + '&group=' + group,
                    type: 'post',
                    //dataType: 'xml',
                    // timeout: 3000,
                    error: function() {
                        alert("执行失败！");
                    },
                    success: function(xml) {
                        if (xml == 0) {
                            alert("执行成功！");
                            window.location.reload();
                        } else {
                            alert("执行失败！");
                        }
                    }
                });
            }


        }
    </script>
</head>

<jsp:include page="head.jsp"/>
<body>

<div>

    <c:if test="${not empty triggers}">
        <table class="hor-minimalist-b" summary="Employee Pay Sheet">

            <thead>

            <tr>
                <th nowrap>触发器名</th>
                <th nowrap>触发器组</th>
                <th nowrap>任务名</th>
                <th nowrap>任务组</th>
                <th nowrap>描述</th>
                <th nowrap>开始时间</th>
                <th nowrap>结束时间</th>
                <th nowrap>下次触发时间</th>
                <th nowrap>上次触发时间</th>
                <th nowrap>状态</th>
                <td nowrap>操作</td>
                <th nowrap>优先级</th>
                <th nowrap>JOB_DATA</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="trigger" items="${triggers}">
                <tr>
                    <td>${trigger.name}</td>
                    <td>${trigger.triggerGroup}</td>
                    <td>${trigger.jobName}</td>
                    <td>${trigger.jobgroup}</td>
                    <td>${trigger.description}</td>
                    <td>
                        <fmt:formatDate value="${trigger.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                    </td>
                    <td>
                        <fmt:formatDate value="${trigger.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                    </td>

                    <td>
                        <fmt:formatDate value="${trigger.nextFireTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                    </td>
                    <td>
                        <fmt:formatDate value="${trigger.previousFireTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                    </td>
                    <td>
                            ${trigger.state}
                    </td>

                    <td nowrap>
                        <input type="button" id="pause" value="暂停"
                               onclick="doCmd('pause','${trigger.name}','${trigger.triggerGroup}','${trigger.state}')">
                        <input type="button" id="resume" value="恢复"
                               onclick="doCmd('resume','${trigger.name}','${trigger.triggerGroup}','${trigger.state}')">
                        <input type="button" id="remove" value="删除"
                               onclick="doCmd('remove','${trigger.name}','${trigger.triggerGroup}','${trigger.state}')">
                    </td>

                    <td>${trigger.priority}</td>
                    <td>
                        <c:forEach items="${trigger.jobDataMap}" var="map">
                            [${map.key}=${map.value}]
                        </c:forEach>

                    </td>

                </tr>
            </c:forEach>
            </tbody>
        </table>
    </c:if>
</div>


</body>
</html>