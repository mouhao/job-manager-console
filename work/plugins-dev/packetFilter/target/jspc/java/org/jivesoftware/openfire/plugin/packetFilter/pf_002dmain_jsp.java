package org.jivesoftware.openfire.plugin.packetFilter;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.jivesoftware.openfire.component.InternalComponentManager;
import org.jivesoftware.openfire.plugin.msn.Msn;
import org.jivesoftware.openfire.plugin.sms.Sms;
import org.xmpp.packet.JID;
import java.util.List;
import org.jivesoftware.openfire.plugin.DBManager;
import org.jivesoftware.openfire.group.GroupManager;
import java.util.Iterator;
import org.jivesoftware.openfire.group.Group;
import org.jivesoftware.openfire.plugin.PacketFilterUtil;

public final class pf_002dmain_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static java.util.List _jspx_dependants;

  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_fmt_message_key_nobody;

  public Object getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _jspx_tagPool_fmt_message_key_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
  }

  public void _jspDestroy() {
    _jspx_tagPool_fmt_message_key_nobody.release();
  }

  public void _jspService(HttpServletRequest request, HttpServletResponse response)
        throws java.io.IOException, ServletException {

    JspFactory _jspxFactory = null;
    PageContext pageContext = null;
    HttpSession session = null;
    ServletContext application = null;
    ServletConfig config = null;
    JspWriter out = null;
    Object page = this;
    JspWriter _jspx_out = null;
    PageContext _jspx_page_context = null;


    try {
      _jspxFactory = JspFactory.getDefaultFactory();
      response.setContentType("text/html");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write("\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
      org.jivesoftware.util.WebManager webManager = null;
      synchronized (_jspx_page_context) {
        webManager = (org.jivesoftware.util.WebManager) _jspx_page_context.getAttribute("webManager", PageContext.PAGE_SCOPE);
        if (webManager == null){
          webManager = new org.jivesoftware.util.WebManager();
          _jspx_page_context.setAttribute("webManager", webManager, PageContext.PAGE_SCOPE);
        }
      }
      out.write('\n');

    webManager.init(request, response, session, application, out);
    Iterator<String> groups = GroupManager.getInstance().getProvider().getGroupNames().iterator();

    boolean showGroupUser = request.getParameter("group") == null ? false : true;
    boolean showUser = request.getParameter("user") == null ? false : true;


      out.write("\n\n<html>\n<head>\n    <style type=\"text/css\">\n        <!--\n        @import url(\"style/packetfilter.css\");\n        -->\n    </style>\n    <title>\n        ");
      if (_jspx_meth_fmt_message_0(_jspx_page_context))
        return;
      out.write("\n    </title>\n    <meta name=\"pageID\" content=\"groups\"/>\n    <meta name=\"helpPage\" content=\"\"/>\n    <script language=\"JavaScript\" type=\"text/javascript\" src=\"scripts/packetfilter.js\"></script>\n\n\n</head>\n<body>\n\n\n<div class=\"jive-table\">\n    <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"100%\">\n        <thead>\n        <tr>\n            <th nowrap>Group</th>\n            <th nowrap>Member</th>\n            <th nowrap>Desc</th>\n            <th nowrap>&nbsp</th>\n        </tr>\n        </thead>\n\n        <tbody>\n        ");

            while (groups.hasNext()) {
                String groupname = groups.next();
                Group group = GroupManager.getInstance().getProvider().getGroup(groupname);
                

        
      out.write("\n\n        <tr class=\"jive-even\">\n\n            <td>");
      out.print(group.getName());
      out.write("\n            </td>\n            <td>");
      out.print(group.getMembers().size());
      out.write("\n            </td>\n            <td>");
      out.print(group.getDescription());
      out.write("\n            </td>\n            <td><a href=\"pf-main.jsp?group=");
      out.print(group.getName());
      out.write("\">\n                <img src=\"/images/edit-16x16.gif\" width=\"16\" height=\"16\" border=\"0\">\n            </a>\n            </td>\n        </tr>\n        ");
} 
      out.write("\n        </tbody>\n    </table>\n</div>\n\n\n\n");
if (showGroupUser) {String groupName = request.getParameter("group");
      out.write("\n<h3>Member Of Group ");
      out.print(groupName);
      out.write("</h3>\n<div class=\"jive-table\">\n    <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"100%\">\n        <thead>\n        <tr>\n            <th nowrap>UserName</th>\n            <th nowrap>Domain</th>\n            <th nowrap>&nbsp</th>\n        </tr>\n        </thead>\n\n        <tbody>\n        ");


            Group cur_group = GroupManager.getInstance().getProvider().getGroup(groupName);
            Iterator<JID> members = cur_group.getMembers().iterator();
            while (members.hasNext()) {
                JID jid = members.next();
        
      out.write("\n\n        <tr class=\"jive-even\">\n\n            <td>");
      out.print(PacketFilterUtil.getJIDName(jid));
      out.write("\n            </td>\n            <td>");
      out.print(jid.getDomain());
      out.write("\n            </td>\n            <td>\n            <a href=\"pf-main.jsp?user=");
      out.print(PacketFilterUtil.getJIDName(jid));
      out.write("&group=");
      out.print(groupName);
      out.write("\"><img\n                    src=\"/images/edit-16x16.gif\" width=\"16\"\n                    height=\"16\" border=\"0\"></a>\n            </td>\n        </tr>\n        ");
} 
      out.write("\n        </tbody>\n    </table>\n</div>\n");
}
      out.write('\n');
      out.write('\n');
      out.write('\n');
if (showUser) {
      out.write("\n<h3>User Detail</h3>\n<div class=\"jive-table\">\n    <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"100%\">\n        <thead>\n        <tr>\n            <th nowrap>JID</th>\n            <th nowrap>MSN</th>\n            <th nowrap>Enable</th>\n            <th nowrap>&nbsp</th>\n        </tr>\n        </thead>\n\n        <tbody>\n        ");

            String user = request.getParameter("user");
            DBManager dbmanager = DBManager.getInstance();
            Msn msn = dbmanager.getMsn(user);
            if(msn!=null){
        
      out.write("\n        <tr class=\"jive-");
      out.print( (msn.isEnable() ? "even" : "odd") );
      out.write("\">\n            ");
if (msn.isEnable()) {
      out.write("\n            <td>");
      out.print(msn.getJid());
      out.write("\n            </td>\n            <td>");
      out.print(msn.getMsn());
      out.write("\n            </td>\n            <td>");
      out.print(msn.isEnable());
      out.write("\n            </td>\n            ");
} else {
      out.write("\n            <td><strike>");
      out.print(msn.getJid());
      out.write("\n            </strike></td>\n            <td><strike>");
      out.print(msn.getMsn());
      out.write("\n            </strike></td>\n            <td><strike>");
      out.print(msn.isEnable());
      out.write("\n            </strike></td>\n            ");
}
      out.write("\n            <td><a href=\"delete-msn.jsp?id=");
      out.print(msn.getId());
      out.write("&user=");
      out.print(user);
      out.write("\"><img src=\"/images/delete-16x16.gif\"\n                                                                  width=\"16\" height=\"16\"\n                                                                  border=\"0\"\n                                                                  alt=\"");
      if (_jspx_meth_fmt_message_1(_jspx_page_context))
        return;
      out.write("\"></a>\n                <a href=\"msn-edit-form.jsp?edit=");
      out.print(msn.getId());
      out.write("&user=");
      out.print(user);
      out.write("\"><img src=\"/images/edit-16x16.gif\" width=\"16\"\n                                                                       height=\"16\" border=\"0\"\n                                                                       alt=\"");
      if (_jspx_meth_fmt_message_2(_jspx_page_context))
        return;
      out.write("\"></a>\n            </td>\n        </tr>\n        ");
} 
      out.write("\n        </tbody>\n    </table>\n</div>\n\n\n\n<br>\n\n<div class=\"jive-table\">\n    <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"100%\">\n        <thead>\n        <tr>\n            <th nowrap>JID</th>\n            <th nowrap>CellPhone</th>\n            <th nowrap>Enable</th>\n            <th nowrap>&nbsp</th>\n        </tr>\n        </thead>\n\n        <tbody>\n        ");


            Sms sms = dbmanager.getSmsByJid(user);
            if(sms!=null){


        
      out.write("\n\n        <tr class=\"jive-");
      out.print( (sms.isEnable() ? "even" : "odd") );
      out.write("\">\n\n            ");
 if (sms.isEnable()) {
      out.write("\n            <td>");
      out.print(sms.getJid());
      out.write("\n            </td>\n            <td>");
      out.print(sms.getCellphone());
      out.write("\n            </td>\n            <td>");
      out.print(sms.isEnable());
      out.write("\n            </td>\n            ");
} else {
      out.write("\n            <td><strike>");
      out.print(sms.getJid());
      out.write("\n            </strike></td>\n            <td><strike>");
      out.print(sms.getCellphone());
      out.write("\n            </strike></td>\n            <td><strike>");
      out.print(sms.isEnable());
      out.write("\n            </strike></td>\n            ");
}
      out.write("\n            <td><a href=\"delete-sms.jsp?id=");
      out.print(sms.getId());
      out.write("&user=");
      out.print(user);
      out.write("\"><img src=\"/images/delete-16x16.gif\"\n                                                                  width=\"16\" height=\"16\"\n                                                                  border=\"0\"\n                                                                  alt=\"");
      if (_jspx_meth_fmt_message_3(_jspx_page_context))
        return;
      out.write("\"></a>\n                <a href=\"sms-edit-form.jsp?edit=");
      out.print(sms.getId());
      out.write("&user=");
      out.print(user);
      out.write("\"><img src=\"/images/edit-16x16.gif\" width=\"16\"\n                                                                       height=\"16\" border=\"0\"\n                                                                       alt=\"");
      if (_jspx_meth_fmt_message_4(_jspx_page_context))
        return;
      out.write("\"></a>\n            </td>\n        </tr>\n        ");
} 
      out.write("\n        </tbody>\n    </table>\n</div>\n\n\n\n");
}
      out.write("\n\n\n</body>\n</html>\n");
    } catch (Throwable t) {
      if (!(t instanceof SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          out.clearBuffer();
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
      }
    } finally {
      if (_jspxFactory != null) _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }

  private boolean _jspx_meth_fmt_message_0(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:message
    org.apache.taglibs.standard.tag.rt.fmt.MessageTag _jspx_th_fmt_message_0 = (org.apache.taglibs.standard.tag.rt.fmt.MessageTag) _jspx_tagPool_fmt_message_key_nobody.get(org.apache.taglibs.standard.tag.rt.fmt.MessageTag.class);
    _jspx_th_fmt_message_0.setPageContext(_jspx_page_context);
    _jspx_th_fmt_message_0.setParent(null);
    _jspx_th_fmt_message_0.setKey("pf.summary.title");
    int _jspx_eval_fmt_message_0 = _jspx_th_fmt_message_0.doStartTag();
    if (_jspx_th_fmt_message_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_0);
      return true;
    }
    _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_0);
    return false;
  }

  private boolean _jspx_meth_fmt_message_1(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:message
    org.apache.taglibs.standard.tag.rt.fmt.MessageTag _jspx_th_fmt_message_1 = (org.apache.taglibs.standard.tag.rt.fmt.MessageTag) _jspx_tagPool_fmt_message_key_nobody.get(org.apache.taglibs.standard.tag.rt.fmt.MessageTag.class);
    _jspx_th_fmt_message_1.setPageContext(_jspx_page_context);
    _jspx_th_fmt_message_1.setParent(null);
    _jspx_th_fmt_message_1.setKey("global.click_delete");
    int _jspx_eval_fmt_message_1 = _jspx_th_fmt_message_1.doStartTag();
    if (_jspx_th_fmt_message_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_1);
      return true;
    }
    _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_1);
    return false;
  }

  private boolean _jspx_meth_fmt_message_2(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:message
    org.apache.taglibs.standard.tag.rt.fmt.MessageTag _jspx_th_fmt_message_2 = (org.apache.taglibs.standard.tag.rt.fmt.MessageTag) _jspx_tagPool_fmt_message_key_nobody.get(org.apache.taglibs.standard.tag.rt.fmt.MessageTag.class);
    _jspx_th_fmt_message_2.setPageContext(_jspx_page_context);
    _jspx_th_fmt_message_2.setParent(null);
    _jspx_th_fmt_message_2.setKey("pf.click.edit");
    int _jspx_eval_fmt_message_2 = _jspx_th_fmt_message_2.doStartTag();
    if (_jspx_th_fmt_message_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_2);
      return true;
    }
    _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_2);
    return false;
  }

  private boolean _jspx_meth_fmt_message_3(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:message
    org.apache.taglibs.standard.tag.rt.fmt.MessageTag _jspx_th_fmt_message_3 = (org.apache.taglibs.standard.tag.rt.fmt.MessageTag) _jspx_tagPool_fmt_message_key_nobody.get(org.apache.taglibs.standard.tag.rt.fmt.MessageTag.class);
    _jspx_th_fmt_message_3.setPageContext(_jspx_page_context);
    _jspx_th_fmt_message_3.setParent(null);
    _jspx_th_fmt_message_3.setKey("global.click_delete");
    int _jspx_eval_fmt_message_3 = _jspx_th_fmt_message_3.doStartTag();
    if (_jspx_th_fmt_message_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_3);
      return true;
    }
    _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_3);
    return false;
  }

  private boolean _jspx_meth_fmt_message_4(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:message
    org.apache.taglibs.standard.tag.rt.fmt.MessageTag _jspx_th_fmt_message_4 = (org.apache.taglibs.standard.tag.rt.fmt.MessageTag) _jspx_tagPool_fmt_message_key_nobody.get(org.apache.taglibs.standard.tag.rt.fmt.MessageTag.class);
    _jspx_th_fmt_message_4.setPageContext(_jspx_page_context);
    _jspx_th_fmt_message_4.setParent(null);
    _jspx_th_fmt_message_4.setKey("pf.click.edit");
    int _jspx_eval_fmt_message_4 = _jspx_th_fmt_message_4.doStartTag();
    if (_jspx_th_fmt_message_4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_4);
      return true;
    }
    _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_4);
    return false;
  }
}
