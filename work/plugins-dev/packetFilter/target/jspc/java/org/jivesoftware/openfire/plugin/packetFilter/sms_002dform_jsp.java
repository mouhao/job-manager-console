package org.jivesoftware.openfire.plugin.packetFilter;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import java.util.HashMap;
import java.util.Map;
import org.jivesoftware.openfire.RoutingTable;
import org.jivesoftware.util.Log;
import org.jivesoftware.openfire.plugin.msn.Msn;
import org.jivesoftware.openfire.plugin.DBManager;
import org.jivesoftware.openfire.user.User;
import java.util.Iterator;

public final class sms_002dform_jsp extends org.apache.jasper.runtime.HttpJspBase
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

      out.write("\n\n\n\n\n\n\n\n\n\n\n");
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
    DBManager dbManager = DBManager.getInstance();
    Iterator<User> users = webManager.getUserManager().getUsers().iterator();
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

      out.write("\n<html>\n<head>\n    <title>\n        ");
      if (_jspx_meth_fmt_message_0(_jspx_page_context))
        return;
      out.write("\n\n    </title>\n    <meta name=\"pageID\" content=\"addSMS\"/>\n    <script language=\"JavaScript\" type=\"text/javascript\" src=\"scripts/packetfilter.js\"></script>\n</head>\n<body>\n\n");
 if (!errors.isEmpty()) { 
      out.write("\n\n<div class=\"jive-error\">\n    <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n        <tbody>\n        <tr>\n            <td class=\"jive-icon\"><img src=\"/images/error-16x16.gif\" width=\"16\" height=\"16\" border=\"0\"/></td>\n            <td class=\"jive-icon-label\">\n\n                ");
 if (errors.get("add_sms_error") != null) { 
      out.write("\n                ");
      out.print(errors.get("add_sms_error"));
      out.write("\n                ");
 } 
      out.write("\n            </td>\n        </tr>\n        </tbody>\n    </table>\n</div>\n<br>\n\n");
 } 
      out.write("\n\n<form action=\"sms-form.jsp\" method=\"get\">\n    <div class=\"jive-table\">\n        <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"100%\">\n            <tbody>\n            <tr class=\"jive-even\">\n                <td>JID</td>\n                <td>\n                                        <select name=\"jid\" id=\"jid\">\n                        ");

                         if(users!=null){
                             while(users.hasNext()){
                                 User user=users.next();
      out.write("\n                            <option value=\"");
      out.print(user.getUsername());
      out.write('"');
      out.write('>');
      out.print(user.getUsername());
      out.write("</option>\n                        ");

                             }
                         }

                        
      out.write("\n                    </select>\n\n\n                   ");
// <input type="text" name="jid" value="" size="40"/>
      out.write("\n                </td>\n            </tr>\n            <tr class=\"jive-odd\">\n                <td>CellPhone</td>\n                <td>\n                    <input type=\"text\" name=\"cellphone\" value=\"\" size=\"40\"/>\n                </td>\n\n            </tr>\n            <tr class=\"jive-even\">\n                <td>Enable</td>\n                <td>\n                    <input type=\"checkbox\" name=\"enable\" value=\"true\" checked>\n                </td>\n\n            </tr>\n\n\n            <tr>\n                <td>\n                    <input type=\"submit\" name=\"create\" value=\"");
      if (_jspx_meth_fmt_message_1(_jspx_page_context))
        return;
      out.write("\">\n                    <input type=\"submit\" name=\"cancel\" value=\"");
      if (_jspx_meth_fmt_message_2(_jspx_page_context))
        return;
      out.write("\">\n                </td>\n                <td>&nbsp;</td>\n            </tr>\n            </tbody>\n        </table>\n\n    </div>\n</form>\n\n</body>\n</html>\n\n");
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
    _jspx_th_fmt_message_0.setKey("pf.create.new.sms");
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
    _jspx_th_fmt_message_1.setKey("pf.create.new.sms");
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
    _jspx_th_fmt_message_2.setKey("pf.global.cancel");
    int _jspx_eval_fmt_message_2 = _jspx_th_fmt_message_2.doStartTag();
    if (_jspx_th_fmt_message_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_2);
      return true;
    }
    _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_2);
    return false;
  }
}
