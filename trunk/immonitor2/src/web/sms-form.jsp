<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Map" %>
<%@ page import="org.jivesoftware.openfire.RoutingTable" %>
<%@ page import="org.jivesoftware.util.Log" %>
<%@ page import="org.jivesoftware.openfire.plugin.msn.Msn" %>
<%@ page import="org.jivesoftware.openfire.plugin.DBManager" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>

<jsp:useBean id="webManager" class="org.jivesoftware.util.WebManager"/>
<%
    webManager.init(request, response, session, application, out);
    DBManager dbManager = DBManager.getInstance();

    Msn msn = null;
    //Get Action
    boolean create = request.getParameter("create") != null;
    boolean cancel = request.getParameter("cancel") != null;

    //Get data

    Map<String, String> errors = new HashMap<String, String>();

    if (cancel) {
        String user = request.getParameter("user");
        if (user != null & !"".equals(user)) {
             response.sendRedirect("pf-main.jsp?user="+user);
        } else {
            response.sendRedirect("pf-main.jsp");
        }
        return;
    }
    if (create) {
        String input_jid =null;
       try {
        input_jid = request.getParameter("jid");
        String input_cellPhone = request.getParameter("cellphone");
        String input_enable = request.getParameter("enable");
        if (input_jid == null || "".equals(input_jid)) {
            errors.put("add_sms_error", "jid can not be null");
        }

        if (input_cellPhone == null || "".equals(input_cellPhone)) {
            errors.put("add_sms_error", "cellPhone can not be null");
        }


        int enable = 0;
        if (input_enable == null) {
            enable = 0;
        } else {
            enable = 1;
        }
        

            dbManager.addSMS(input_jid, input_cellPhone, enable);
        } catch (Exception e) {
            Log.error(e);
            errors.put("add_sms_error", e.getLocalizedMessage());
        }
        if (errors.isEmpty()) {
            response.sendRedirect("pf-main.jsp?user="+input_jid);
        }


    }
%>
<html>
<head>
    <title>
        <fmt:message key="pf.create.new.sms"/>

    </title>
    <meta name="pageID" content="packetFilter"/>
    <script language="JavaScript" type="text/javascript" src="scripts/packetfilter.js"></script>
</head>
<body>

<% if (!errors.isEmpty()) { %>

<div class="jive-error">
    <table cellpadding="0" cellspacing="0" border="0">
        <tbody>
        <tr>
            <td class="jive-icon"><img src="/images/error-16x16.gif" width="16" height="16" border="0"/></td>
            <td class="jive-icon-label">

                <% if (errors.get("add_sms_error") != null) { %>
                <%=errors.get("add_sms_error")%>
                <% } %>
            </td>
        </tr>
        </tbody>
    </table>
</div>
<br>

<% } %>

<form action="sms-form.jsp" method="get">
    <div class="jive-table">
        <table cellpadding="0" cellspacing="0" border="0" width="100%">
            <tbody>
            <tr class="jive-even">
                <td>JID</td>
                <td>
                    <input type="text" name="jid" value="" size="40"/>
                </td>
            </tr>
            <tr class="jive-odd">
                <td>CellPhone</td>
                <td>
                    <input type="text" name="cellphone" value="" size="40"/>
                </td>

            </tr>
            <tr class="jive-even">
                <td>Enable</td>
                <td>
                    <input type="checkbox" name="enable" value="true" checked>
                </td>

            </tr>


            <tr>
                <td>
                    <input type="submit" name="create" value="<fmt:message key="pf.create.new.sms" />">
                    <input type="submit" name="cancel" value="<fmt:message key="pf.global.cancel" />">
                </td>
                <td>&nbsp;</td>
            </tr>
            </tbody>
        </table>

    </div>
</form>

</body>
</html>

