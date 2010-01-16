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
        function doCmd(state, triggerName, group, triggerState, jobName, jobGroup) {

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
                            window.location.href = "triggerJob.do?jobName=" + jobName + "&groupName=" + jobGroup;
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
                            window.location.href = "triggerJob.do?jobName=" + jobName + "&groupName=" + jobGroup;
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
                            window.location.href = "triggerJob.do?jobName=" + jobName + "&groupName=" + jobGroup;
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
    <table class="hor-minimalist-b" summary="Employee Pay Sheet">
        <thead>
        <tr>
            <th>JOB_NAME</th>
            <th>JOB_GROUP</th>
            <th>DESCRIPTION</th>
            <th>JOB_CLASS_NAME</th>
            <th>IS_DURABLE</th>
            <th>IS_VOLATILE</th>
            <th>REQUESTS_RECOVERY</th>
            <th>JOB_DATA</th>
        </tr>
        </thead>
        <tbody>

        <tr>
            <td>${jobDetail.name}</td>
            <td>${jobDetail.group}</td>
            <td>${jobDetail.description}</td>
            <td>${jobDetail.jobClass}</td>
            <td>
                <c:choose>
                    <c:when test="${jobDetail.durability}">Yes</c:when><c:otherwise>No</c:otherwise>
                </c:choose>
            </td>
            <td>
                <c:choose>
                    <c:when test="${jobDetail.volatility}">Yes</c:when><c:otherwise>No</c:otherwise>
                </c:choose>
            </td>
            <td>
                <c:choose>
                    <c:when test="${jobDetail.shouldRecover}">Yes</c:when><c:otherwise>No</c:otherwise>
                </c:choose>
            </td>
            <td>
                <c:forEach items="${jobDetail.jobDataMap}" var="map">
                    [${map.key}=${map.value}]
                </c:forEach>

            </td>

        </tr>
        </tbody>
    </table>
</div>


<div>
    <c:if test="${empty triggers}">
        <lable class="underline-lable">
            目前还没有Trigger调用该Job
        </lable>
    </c:if>
    <c:if test="${not empty triggers}">
        <lable class="underline-lable">
            已有如下Trigger调用该Job
        </lable>
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
                               onclick="doCmd('pause','${trigger.name}','${trigger.triggerGroup}','${trigger.state}','${trigger.jobName}','${trigger.jobgroup}')">
                        <input type="button" id="resume" value="恢复"
                               onclick="doCmd('resume','${trigger.name}','${trigger.triggerGroup}','${trigger.state}','${trigger.jobName}','${trigger.jobgroup}')">
                        <input type="button" id="remove" value="删除"
                               onclick="doCmd('remove','${trigger.name}','${trigger.triggerGroup}','${trigger.state}','${trigger.jobName}','${trigger.jobgroup}')">
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

<div style="margin:15px;">
    <br>

    <form action="addTriggerToJob.do" method="post">
        <table class="hor-minimalist-b" summary="Employee Pay Sheet">
            <tr>
                <td>
                    <lable class="underline-lable">
                        新增Trigger
                    </lable>
                </td>
                <td>
                    <input type="hidden" name="jobName" value="${jobDetail.name}">
                    <input type="hidden" name="groupName" value="${jobDetail.group}">
                </td>
            </tr>
            <tr>
                <td>Trigger名称:</td>
                <td><input name="triggerName" value="" maxlength="100" type="text"></td>
            </tr>
            <tr>
                <td>所属组:</td>
                <td><input name="triggerGroup" value="" maxlength="100" type="text"></td>
            </tr>
            <tr>
                <td>Cron表达式：</td>
                <td><input name="cronExpression" value="0/10 * * ? * * *" maxlength="100" type="text">（必填，Cron表达式(如"0/10
                    * * ? * * *"，每10秒中执行调试一次)
                </td>
            </tr>
            <tr>
                <td>描述：</td>
                <td><input name="desc" type="text" value="" maxlength="100"></td>
            </tr>

            <tr>
                <td><input name="提交" type="submit" value="提交"></td>
            </tr>
        </table>

    </form>
</div>

</body>
</html>