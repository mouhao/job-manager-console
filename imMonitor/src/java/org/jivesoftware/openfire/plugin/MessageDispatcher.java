package org.jivesoftware.openfire.plugin;

import org.jivesoftware.openfire.SessionManager;
import org.jivesoftware.openfire.XMPPServer;
import org.jivesoftware.openfire.plugin.msn.MsnRobot;
import org.jivesoftware.openfire.plugin.msn.Msn;
import org.jivesoftware.openfire.plugin.sms.Sms;
import org.jivesoftware.openfire.plugin.sms.SmsRobot;
import org.jivesoftware.openfire.session.ClientSession;
import org.jivesoftware.util.Log;
import org.xmpp.packet.JID;
import org.xmpp.packet.Message;
import org.xmpp.packet.Packet;

/**
 * Created by IntelliJ IDEA.
 * User: changyou
 * Date: 2010-6-12
 * Time: 14:12:58
 * To change this template use File | Settings | File Templates.
 */
public class MessageDispatcher extends Thread {
    private Packet packet;
    private String user;

    /**
     * Causes this thread to begin execution; the Java Virtual Machine
     * calls the <code>run</code> method of this thread.
     * <p/>
     * The result is that two threads are running concurrently: the
     * current thread (which returns from the call to the
     * <code>start</code> method) and the other thread (which executes its
     * <code>run</code> method).
     * <p/>
     * It is never legal to start a thread more than once.
     * In particular, a thread may not be restarted once it has completed
     * execution.
     *
     * @throws IllegalThreadStateException if the thread was already
     *                                     started.
     * @see #run()
     * @see #stop()
     */
    @Override
    public void start() {
        DBManager drm = DBManager.getInstance();
        MsnRobot msnRobot = MsnRobot.getInstance();
        SmsRobot smsRobot = SmsRobot.getInstance();
        String domain = XMPPServer.getInstance().getServerInfo().getXMPPDomain();
        String jid = this.user + "@" + domain;
        SessionManager sessionManager = SessionManager.getInstance();
        ClientSession clientSession = sessionManager.getSession(new JID(jid));
        if (clientSession != null) {
            if (clientSession.getPresence().isAvailable()) {//判断openfire是否在线
                packet.setTo(jid);
                clientSession.process(packet);
            }
        }
        String msg = "[" + ((Message) packet).getSubject() + "]:" + ((Message) packet).getBody();
        Msn msn = drm.getMsn(this.user);

        boolean msnMessageSend = false;
        if (msn != null) {
            if (msn.isEnable()) {
                try {
                    //先判断是不是联系人
                    if (!msnRobot.isContect(msn.getMsn())) {
                        Log.info("Add " + msn.getMsn() + " To Msn Contect List");
                        msnRobot.addFriend(msn.getMsn());
                    }
                    //if (msnRobot.isOnlie(msn.getMsn())) {
                    Log.info("Send Message To " + this.user + "'s Msn:" + msn.getMsn());
                    msnRobot.sendMessage(msn.getMsn(), msg);
                    msnMessageSend = true;
                    //}
                } catch (Exception e) {
                    Log.error(e);
                    msnMessageSend = false;
                }
            }

        }
        if (!msnMessageSend) {
            Sms sms = drm.getSmsByJid(this.user);
            if (sms != null) {
                if (sms.isEnable()) {
                    Log.info("Send Sms Message To " + this.user + "'s CellPhone:" + sms.getCellphone());
                    smsRobot.sendSmsMsg(sms.getCellphone(), msg);
                }
            }

        }


    }

    /**
     * Allocates a new <code>Thread</code> object. This constructor has
     * the same effect as <code>Thread(null, null,</code>
     * <i>gname</i><code>)</code>, where <b><i>gname</i></b> is
     * a newly generated name. Automatically generated names are of the
     * form <code>"Thread-"+</code><i>n</i>, where <i>n</i> is an integer.
     *
     * @see #Thread(ThreadGroup, Runnable, String)
     */
    public MessageDispatcher(Packet packet, String user) {
        this.packet = packet;
        this.user = user;
    }
}
