<%@ page import="org.jivesoftware.openfire.component.InternalComponentManager,
                 org.jivesoftware.openfire.plugin.msn.Msn,
                 org.jivesoftware.openfire.plugin.sms.Sms"
        %>


<%@ page import="org.xmpp.packet.JID" %>
<%@ page import="java.util.List" %>
<%@ page import="org.jivesoftware.openfire.plugin.DBManager" %>
<%@ page import="org.jivesoftware.openfire.group.GroupManager" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="org.jivesoftware.openfire.group.Group" %>
<%@ page import="org.jivesoftware.openfire.plugin.ImMonitorUtil" %>

<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>

<jsp:useBean id="webManager" class="org.jivesoftware.util.WebManager"/>
<%
    webManager.init(request, response, session, application, out);
    Iterator<String> groups = GroupManager.getInstance().getProvider().getGroupNames().iterator();

    boolean showGroupUser = request.getParameter("group") == null ? false : true;
    boolean showUser = request.getParameter("user") == null ? false : true;

%>

<html>
<head>
    <style type="text/css">
        <!--
        @import url("style/packetfilter.css");
        -->
    </style>
    <title>
        <fmt:message key="pf.summary.title"/>
    </title>
    <meta name="pageID" content="packetFilter"/>
    <meta name="helpPage" content=""/>
    <script language="JavaScript" type="text/javascript" src="scripts/packetfilter.js"></script>


</head>
<body>


<div class="jive-table">
    <h3>消息接收组</h3>
    <table cellpadding="0" cellspacing="0" border="0" width="100%">
        <thead>
        <tr>
            <th nowrap>Group</th>
            <th nowrap>Member</th>
            <th nowrap>Desc</th>
            <th nowrap>&nbsp</th>
        </tr>
        </thead>

        <tbody>
        <%
            while (groups.hasNext()) {
                String groupname = groups.next();
                Group group = GroupManager.getInstance().getProvider().getGroup(groupname);


        %>

        <tr class="jive-even">

            <td><%=group.getName()%>
            </td>
            <td><%=group.getMembers().size()%>
            </td>
            <td><%=group.getDescription()%>
            </td>
            <td><a href="pf-main.jsp?group=<%=group.getName()%>">
                <img src="/images/edit-16x16.gif" width="16" height="16" border="0">
            </a>
            </td>
        </tr>
        <%} %>
        </tbody>
    </table>
</div>



<%if (showGroupUser) {String groupName = request.getParameter("group");%>
<h3>Member Of Group <%=groupName%></h3>
<div class="jive-table">
    <table cellpadding="0" cellspacing="0" border="0" width="100%">
        <thead>
        <tr>
            <th nowrap>UserName</th>
            <th nowrap>Domain</th>
            <th nowrap>&nbsp</th>
        </tr>
        </thead>

        <tbody>
        <%

            Group cur_group = GroupManager.getInstance().getProvider().getGroup(groupName);
            Iterator<JID> members = cur_group.getMembers().iterator();
            while (members.hasNext()) {
                JID jid = members.next();
        %>

        <tr class="jive-even">

            <td><%=ImMonitorUtil.getJIDName(jid)%>
            </td>
            <td><%=jid.getDomain()%>
            </td>
            <td>
            <a href="pf-main.jsp?user=<%=ImMonitorUtil.getJIDName(jid)%>&group=<%=groupName%>"><img
                    src="/images/edit-16x16.gif" width="16"
                    height="16" border="0"></a>
            </td>
        </tr>
        <%} %>
        </tbody>
    </table>
</div>
<%}%>


<%if (showUser) {%>
<h3>User Detail</h3>
<div class="jive-table">
    <table cellpadding="0" cellspacing="0" border="0" width="100%">
        <thead>
        <tr>
            <th nowrap>JID</th>
            <th nowrap>MSN</th>
            <th nowrap>Enable</th>
            <th nowrap>&nbsp</th>
        </tr>
        </thead>

        <tbody>
        <%
            String user = request.getParameter("user");
            DBManager dbmanager = DBManager.getInstance();
            Msn msn = dbmanager.getMsn(user);
            if(msn!=null){
        %>
        <tr class="jive-<%= (msn.isEnable() ? "even" : "odd") %>">
            <%if (msn.isEnable()) {%>
            <td><%=msn.getJid()%>
            </td>
            <td><%=msn.getMsn()%>
            </td>
            <td><%=msn.isEnable()%>
            </td>
            <%} else {%>
            <td><strike><%=msn.getJid()%>
            </strike></td>
            <td><strike><%=msn.getMsn()%>
            </strike></td>
            <td><strike><%=msn.isEnable()%>
            </strike></td>
            <%}%>
            <td><a href="delete-msn.jsp?id=<%=msn.getId()%>&user=<%=user%>"><img src="/images/delete-16x16.gif"
                                                                  width="16" height="16"
                                                                  border="0"
                                                                  alt="<fmt:message key="global.click_delete" />"></a>
                <a href="msn-edit-form.jsp?edit=<%=msn.getId()%>&user=<%=user%>"><img src="/images/edit-16x16.gif" width="16"
                                                                       height="16" border="0"
                                                                       alt="<fmt:message key="pf.click.edit"/>"></a>
            </td>
        </tr>
        <%} %>
        </tbody>
    </table>
</div>
<input type="button" ONCLICK="window.location.href='msn-form.jsp?user=<%=user%>'" name="create"
       value="<fmt:message key="pf.create.new.msn"/>">


<br>

<div class="jive-table">
    <table cellpadding="0" cellspacing="0" border="0" width="100%">
        <thead>
        <tr>
            <th nowrap>JID</th>
            <th nowrap>CellPhone</th>
            <th nowrap>Enable</th>
            <th nowrap>&nbsp</th>
        </tr>
        </thead>

        <tbody>
        <%

            Sms sms = dbmanager.getSmsByJid(user);
            if(sms!=null){


        %>

        <tr class="jive-<%= (sms.isEnable() ? "even" : "odd") %>">

            <% if (sms.isEnable()) {%>
            <td><%=sms.getJid()%>
            </td>
            <td><%=sms.getCellphone()%>
            </td>
            <td><%=sms.isEnable()%>
            </td>
            <%} else {%>
            <td><strike><%=sms.getJid()%>
            </strike></td>
            <td><strike><%=sms.getCellphone()%>
            </strike></td>
            <td><strike><%=sms.isEnable()%>
            </strike></td>
            <%}%>
            <td><a href="delete-sms.jsp?id=<%=sms.getId()%>&user=<%=user%>"><img src="/images/delete-16x16.gif"
                                                                  width="16" height="16"
                                                                  border="0"
                                                                  alt="<fmt:message key="global.click_delete" />"></a>
                <a href="sms-edit-form.jsp?edit=<%=sms.getId()%>&user=<%=user%>"><img src="/images/edit-16x16.gif" width="16"
                                                                       height="16" border="0"
                                                                       alt="<fmt:message key="pf.click.edit"/>"></a>
            </td>
        </tr>
        <%} %>
        </tbody>
    </table>
</div>
<input type="button" ONCLICK="window.location.href='sms-form.jsp?user=<%=user%>'" name="create"
       value="<fmt:message key="pf.create.new.sms"/>">


<%}%>


</body>
</html>
