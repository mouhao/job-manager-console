<%--
  Created by IntelliJ IDEA.
  User: SongJiao
  Date: 2010-1-9
  Time: 22:34:10
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head><title>上传/安装/卸载Job</title>
    <style type="text/css">
        @import url('<c:url value="/styles/table/style.css"/>');
    </style>
</head>
<jsp:include page="head.jsp"/>
<body>
<div>
    <form action="upload.do" method="post" name="uploadform" enctype="multipart/form-data">
        <label class="underline-lable">选择要上传的文件：</label> <input name="file"
                                                                type="file">
        <input name="submit" type="submit" value="上传">
    </form>
</div>
<c:if test="${not empty dirs}">
    <label class="underline-lable">已上传文件列表</label>

    <div>
        <table class="hor-minimalist-b" summary="Employee Pay Sheet">
            <thead>
            <tr>
                <th nowrap>主目录名</th>
                <th nowrap>上传时间</th>
                <th nowrap>安装</th>
            </tr>
            </thead>
            <tbody>
            　<c:forEach items="${dirs}" var="file">
                <tr>
                    <td>${file.filename}</td>
                    <td>
                        <fmt:formatDate value="${file.lastmodified}" pattern="yyyy-MM-dd HH:mm:ss"/>
                    </td>
                    <td nowrap>
                        <a href="showInstallJob.do?app_path=${file.filename}">安装</a>
                        <a href="unInstallJob.do?app_path=${file.filename}">卸载</a>
                    </td>
                </tr>

            </c:forEach>
            </tbody>
        </table>
    </div>

</c:if>


</body>
</html>