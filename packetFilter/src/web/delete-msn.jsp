
<%@ page import="org.jivesoftware.util.*"%>
<%@ page import="org.jivesoftware.openfire.plugin.rules.*" %>

<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>

<jsp:useBean id="webManager" class="org.jivesoftware.util.WebManager" />
<% webManager.init(request, response, session, application, out ); %>

<% // Get parameters //
    boolean cancel = request.getParameter("cancel") != null;
    boolean delete = request.getParameter("delete") != null;
    String id = ParamUtils.getParameter(request, "id");

    DbRuleManager dbRuleManager = DbRuleManager.getInstance();
    Msn msn = dbRuleManager.getMsnById(id);

    // Handle a cancel
    if (cancel) {
        response.sendRedirect("pf-main.jsp");
        return;
    }
    if (delete) {
       dbRuleManager.deleteMsnById(id);
        response.sendRedirect("pf-main.jsp");
    }
%>

<html>
    <head>
        <title><fmt:message key="pf.delete.msn.title"/></title>
        <meta name="pageID" content="packetFilter"/>
    </head>
    <body>

    You have choosen to delete <%=msn.getJid()%>'s  Msn Are you sure?

    <br>
    <br>

<form action="delete-msn.jsp">
<input type="hidden" name="id" value="<%=msn.getId()%>">
<input type="submit" name="delete" value="<fmt:message key="pf.delete.delete.msn" />">
<input type="submit" name="cancel" value="<fmt:message key="pf.global.cancel" />">
</form>
</body>
</html>
