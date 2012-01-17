package org.jivesoftware.openfire.plugin.packetFilter;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jivesoftware.util.Log;
import org.jivesoftware.openfire.plugin.sms.Sms;
import org.jivesoftware.openfire.plugin.DBManager;

public final class sms_002dedit_002dform_jsp extends org.apache.jasper.runtime.HttpJspBase
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

      out.write("\n\n\n\n\n\n\n\n\n\n");
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
    Sms sms = null;

    //Get Action
    boolean editSave = request.getParameter("editSave") != null;
    boolean edit = request.getParameter("edit") != null;
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
    if (edit) {
        String id = request.getParameter("edit");
        sms = dbManager.getSmsById(id);
    }
    if (editSave) {
        String input_jid=null;
        try {
            String input_id = request.getParameter("id");
            input_jid = request.getParameter("jid");
            String input_cellphone = request.getParameter("cellphone");
            String input_enable = request.getParameter("enable");
            sms = new Sms();
            sms.setId(Long.parseLong(input_id));
            
            sms.setJid(input_jid);
            if(input_cellphone==null||"".equals(input_cellphone)){
                errors.put("edit_msn_error","cellphone can not be null");
                response.sendRedirect("pf-main.jsp?user="+input_jid);
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
            response.sendRedirect("pf-main.jsp?user="+input_jid);
        }
    }

      out.write("\n<html>\n<head>\n    <title>\n        ");
      if (_jspx_meth_fmt_message_0(_jspx_page_context))
        return;
      out.write("\n    </title>\n    <meta name=\"pageID\" content=\"addSMS\"/>\n    <script language=\"JavaScript\" type=\"text/javascript\" src=\"scripts/packetfilter.js\"></script>\n</head>\n<body>\n\n\n");
 if (!errors.isEmpty()) { 
      out.write("\n\n<div class=\"jive-error\">\n    <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n        <tbody>\n        <tr>\n            <td class=\"jive-icon\"><img src=\"/images/error-16x16.gif\" width=\"16\" height=\"16\" border=\"0\"/></td>\n            <td class=\"jive-icon-label\">\n\n                ");
 if (errors.get("edit_msn_error") != null) {
                
      out.write("\n                ");
      out.print(errors.get("edit_msn_error"));
      out.write("\n\n                ");
}
      out.write("\n            </td>\n        </tr>\n        </tbody>\n    </table>\n</div>\n<br>\n");
 } 
      out.write("\n<form action=\"sms-edit-form.jsp?editSave\" method=\"GET\">\n    <input type=\"hidden\" name=\"id\" value=\"");
      out.print(sms.getId());
      out.write("\">\n    <input type=\"hidden\" name=\"jid\" value=\"");
      out.print(sms.getJid());
      out.write("\">\n\n\n    <div class=\"jive-table\">\n        <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"100%\">\n            <tbody>\n            <tr class=\"jive-even\">\n                <td>ID</td>\n                <td>\n                    ");
      out.print(sms.getId());
      out.write("\n                </td>\n            </tr>\n            <tr class=\"jive-odd\">\n                <td>JID</td>\n                <td>\n                    ");
      out.print(sms.getJid());
      out.write("\n                </td>\n\n            </tr>\n            <tr class=\"jive-even\">\n                <td>CellPhone</td>\n                <td>\n                    <input type=\"text\" name=\"cellphone\" value=\"");
      out.print(sms.getCellphone());
      out.write("\" size=\"40\"/>\n                </td>\n\n            </tr>\n\n            <tr class=\"jive-odd\">\n                <td>Enable</td>\n                <td><input type=\"checkbox\" name=\"enable\" value=\"true\"\n                    ");
if(sms.isEnable()) {
      out.write(" checked");
}
      out.write("></td>\n            </tr>\n            <tr>\n                <td>\n                    <input type=\"submit\" name=\"editSave\" value=\"editSave\">\n                    <input type=\"submit\" name=\"cancel\" value=\"cancel\">\n                </td>\n                <td>&nbsp;</td>\n            </tr>\n            </tbody>\n        </table>\n\n    </div>\n</form>\n\n</body>\n</html>\n\n");
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
    _jspx_th_fmt_message_0.setKey("pf.save.edit.sms");
    int _jspx_eval_fmt_message_0 = _jspx_th_fmt_message_0.doStartTag();
    if (_jspx_th_fmt_message_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_0);
      return true;
    }
    _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_0);
    return false;
  }
}
