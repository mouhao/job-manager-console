<%--
  Created by IntelliJ IDEA.
  User: SongJiao
  Date: 2010-1-9
  Time: 22:34:10
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head><title>上传</title></head>
  <body>
  <form action="upload.do" method="post" name="uploadform" enctype="multipart/form-data">
      <label>选择要上传的文件：</label> <input name="file" type="file">
      <input name="submit" type="submit" value="上传">
  </form>

  <c:if test="${not empty dirs}">
      <div>
          <table>
              <thead>
                <tr>
                    <td>name</td>
                    <td>last modify</td>
                </tr>
              </thead>
              <tbody>
              　<c:forEach items="${dirs}" var="file" >
                  <tr>
                      <td>${file.filename}</td>
                      <td>${file.lastmodified}</td>
                  </tr>

               </c:forEach>
              </tbody>
          </table>
      </div>

  </c:if>


  </body>
</html>