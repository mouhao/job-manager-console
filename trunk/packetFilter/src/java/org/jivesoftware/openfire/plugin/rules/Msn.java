package org.jivesoftware.openfire.plugin.rules;

/**
 * Created by IntelliJ IDEA.
 * User: songjiao
 * Date: 2010-6-9
 * Time: 21:53:40
 * To change this template use File | Settings | File Templates.
 */
public class Msn {
    private long id;

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

    public String getMsn() {
        return msn;
    }

    public void setMsn(String msn) {
        this.msn = msn;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    private String jid;
   private String msn;
   private boolean enable;
}
