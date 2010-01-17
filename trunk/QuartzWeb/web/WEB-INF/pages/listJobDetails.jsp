<%--
  Created by IntelliJ IDEA.
  User: SongJiao
  Date: 2010-1-15
  Time: 21:28:28
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head><title>JobDetails</title>
    <style type="text/css">
        @import url('<c:url value="/styles/table/style.css"/>');
    </style>
</head>
<jsp:include page="head.jsp"/>


<body>
<div>
    <table class="hor-minimalist-b" summary="Employee Pay Sheet">
        <thead>
        <tr>
            <th nowrap>任务名</th>
            <th nowrap>任务组</th>
            <th nowrap>描述</th>
            <th nowrap>JOB_CLASS_NAME</th>
            <th nowrap>IS_DURABLE</th>
            <th nowrap>IS_VOLATILE</th>
            <th nowrap>REQUESTS_RECOVERY</th>
            <th nowrap>JOB_DATA</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="jobDetail" items="${jobDetails}">
            <tr>
                <td><a href="triggerJob.do?jobName=${jobDetail.name}&groupName=${jobDetail.group}">${jobDetail.name}</a>
                </td>
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
        </c:forEach>
        </tbody>

    </table>
</div>

<c:if test="${not empty app_path}">

    <div style="margin:15px;">
        <br>

        <form action="installJob.do" method="post">
            <table class="hor-minimalist-b" summary="Employee Pay Sheet">
                <tr>
                    <td>
                        <lable class="underline-lable">
                            安装Job
                        </lable>
                    </td>
                    <td>

                        <input type="hidden" name="app_path" value="${app_path}">
                    </td>
                </tr>
                <tr>
                    <td>任务名称:</td>
                    <td><input name="jobName" value="" maxlength="100" type="text"></td>
                </tr>
                <tr>
                    <td>所属组:</td>
                    <td><input name="jobGroup" value="" maxlength="100" type="text"></td>
                </tr>
                <tr>
                    <td>Main Class路径</td>
                    <td><input name="mainClass" value="" maxlength="100" type="text">
                    </td>
                </tr>
                <tr>
                    <td>Main 参数：</td>
                    <td><input name="main_args" type="text" value="" maxlength="100">(*多个参数用空格隔开*)</td>
                </tr>
                <tr>
                    <td>JVM参数：</td>
                    <td><input name="jvm_args" type="text" value="" maxlength="100"></td>
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
</c:if>
</body>
</html>