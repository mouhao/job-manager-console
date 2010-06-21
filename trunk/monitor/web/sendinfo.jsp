<%@ page import="org.jivesoftware.smack.XMPPConnection" %>
<%@ page import="org.jivesoftware.smack.ConnectionConfiguration" %>
<%@ page import="org.jivesoftware.smack.packet.Message" %>
<%--
  Created by IntelliJ IDEA.
  User: songjiao
  Date: 2010-6-16
  Time: 0:10:35
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String group = request.getParameter("group");
    String subject = request.getParameter("subject");
    String msg = request.getParameter("msg");

    if (group != null && !"".equals(group)) {
        if (subject == null) {
            subject = "null";
        }

        if (msg == null) {
            msg = "null";
        }

        try {
            ConnectionConfiguration conf = new ConnectionConfiguration("1.1.1.1", 1111);
            XMPPConnection connection = new XMPPConnection(conf);
            connection.connect();
            Message packet = new Message();
            packet.setPacketID("cyou_monitor_" + System.currentTimeMillis());
            packet.setTo(group);
            packet.setSubject(subject);
            packet.setBody(msg);
            connection.sendPacket(packet);
        } catch (Exception e) {
            e.printStackTrace();
            out.println("ERROR");

        }
        out.println("DONE");


    } else {
        out.println("NONE");
    }

%>