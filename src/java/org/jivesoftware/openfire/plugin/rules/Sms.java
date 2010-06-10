package org.jivesoftware.openfire.plugin.rules;

/**
 * Created by IntelliJ IDEA.
 * User: songjiao
 * Date: 2010-6-9
 * Time: 21:53:46
 * To change this template use File | Settings | File Templates.
 */

public class Sms {

    private long id;
    private String jid;
    private String cellphone;
    private boolean enable;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getJid() {
        return jid;
    }

    public void setJid(String jid) {
        this.jid = jid;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}
