<%@ page import="org.jivesoftware.openfire.XMPPServer,
                 org.jivesoftware.openfire.component.InternalComponentManager,
                 org.jivesoftware.openfire.group.Group,
                 org.jivesoftware.openfire.plugin.DBManager"
        %>
<%@ page import="org.jivesoftware.openfire.plugin.rules.*" %>
<%@ page import="org.jivesoftware.openfire.user.UserManager" %>
<%@ page import="org.jivesoftware.util.ParamUtils" %>
<%@ page import="org.xmpp.component.Component" %>
<%@ page import="org.xmpp.packet.JID" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="org.jivesoftware.util.Log" %>
<%@ page import="org.jivesoftware.openfire.plugin.sms.Sms" %>

<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>

<jsp:useBean id="webManager" class="org.jivesoftware.util.WebManager"/>
<%
    webManager.init(request, response, session, application, out);

    DBManager dbManager = DBManager.getInstance();
    Sms sms = null;

    //Get Action
    boolean editSave = request.getParameter("editSave") != null;
    boolean edit = request.getParameter("edit") != null;
    boolean cancel = request.getParameter("cancel") != null;


    //Get data

    Map<String, String> errors = new HashMap<String, String>();

    if (cancel) {
        response.sendRedirect("pf-main.jsp");
        return;
    }
    if (edit) {
        String id = request.getParameter("edit");
        sms = dbManager.getSmsById(id);
    }
    if (editSave) {
        try {
            String input_id = request.getParameter("id");
            String input_jid = request.getParameter("jid");
            String input_cellphone = request.getParameter("cellphone");
            String input_enable = request.getParameter("enable");
            sms = new Sms();
            sms.setId(Long.parseLong(input_id));
            
            sms.setJid(input_jid);
            if(input_cellphone==null||"".equals(input_cellphone)){
                errors.put("edit_msn_error","cellphone can not be null");
                response.sendRedirect("pf-main.jsp");
            }
            sms.setCellphone(input_cellphone);
            if (input_enable == null) {
                sms.setEnable(false);
            } else {
                sms.setEnable(true);
            }

            dbManager.updateSms(sms);
        } catch (Exception e) {
            Log.error(e);
            errors.put("edit_msn_error", e.getLocalizedMessage());
        }
        if (errors.isEmpty()) {
            response.sendRedirect("pf-main.jsp");
        }
    }
%>
<html>
<head>
    <title>
        <fmt:message key="pf.save.edit.sms"/>
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

                <% if (errors.get("edit_msn_error") != null) {
                %>
                <%=errors.get("edit_msn_error")%>

                <%}%>
            </td>
        </tr>
        </tbody>
    </table>
</div>
<br>
<% } %>
<form action="sms-edit-form.jsp?editSave" method="GET">
    <input type="hidden" name="id" value="<%=sms.getId()%>">
    <input type="hidden" name="jid" value="<%=sms.getJid()%>">


    <div class="jive-table">
        <table cellpadding="0" cellspacing="0" border="0" width="100%">
            <tbody>
            <tr class="jive-even">
                <td>ID</td>
                <td>
                    <%=sms.getId()%>
                </td>
            </tr>
            <tr class="jive-odd">
                <td>JID</td>
                <td>
                    <%=sms.getJid()%>
                </td>

            </tr>
            <tr class="jive-even">
                <td>CellPhone</td>
                <td>
                    <input type="text" name="cellphone" value="<%=sms.getCellphone()%>" size="40"/>
                </td>

            </tr>

            <tr class="jive-odd">
                <td>Enable</td>
                <td><input type="checkbox" name="enable" value="true"
                    <%if(sms.isEnable()) {%> checked<%}%>></td>
            </tr>
            <tr>
                <td>
                    <input type="submit" name="editSave" value="editSave">
                    <input type="submit" name="cancel" value="cancel">
                </td>
                <td>&nbsp;</td>
            </tr>
            </tbody>
        </table>

    </div>
</form>

</body>
</html>

