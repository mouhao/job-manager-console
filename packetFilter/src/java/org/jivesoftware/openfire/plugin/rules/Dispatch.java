package org.jivesoftware.openfire.plugin.rules;

import org.jivesoftware.openfire.SessionManager;
import org.jivesoftware.openfire.group.Group;
import org.jivesoftware.openfire.group.GroupManager;
import org.jivesoftware.openfire.group.GroupNotFoundException;
import org.jivesoftware.openfire.interceptor.PacketRejectedException;
import org.jivesoftware.openfire.plugin.msn.MsnRobot;
import org.jivesoftware.openfire.plugin.sms.SmsRobot;
import org.jivesoftware.openfire.session.ClientSession;
import org.jivesoftware.util.JiveGlobals;
import org.jivesoftware.util.Log;
import org.xmpp.packet.*;

import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: changyou
 * Date: 2010-6-9
 * Time: 15:21:28
 * To change this template use File | Settings | File Templates.
 */
public class Dispatch extends AbstractRule implements Rule {
    public String getDisplayName() {
        return "Dispatch";
    }

    public Packet doAction(Packet packet) throws PacketRejectedException {
        Log.info("===========================Dispatch packet================================");
        SessionManager sessionManager = SessionManager.getInstance();
        ClientSession clientSession = sessionManager.getSession(packet.getFrom());
        Log.info("Message?:" + (packet instanceof Message));
        Log.info("Presence?:" + (packet instanceof Presence));
        Log.info("IQ?:" + (packet instanceof IQ));
        if (packet instanceof Message) {
            try {
                Group group = GroupManager.getInstance().getProvider().getGroup(this.getDestination());
                Log.info("Group:" + group.getName());
                Iterator<JID> members = group.getMembers().iterator();

                DbRuleManager drm = DbRuleManager.getInstance();
                MsnRobot msnRobot = MsnRobot.getInstance();
                SmsRobot smsRobot = SmsRobot.getInstance();
                while (members.hasNext()) {
                    JID member = members.next();
                    Log.info("member:" + member.toBareJID());
                    Msn msn = drm.getMsn(member.toBareJID());
                    if (msn != null) {
                        String msg = "[" + ((Message) packet).getSubject() + "]:" + ((Message) packet).getBody();
                        if (msnRobot.isOnlie(msn.getMsn())) {
                            msnRobot.sendMessage(msn.getMsn(), msg);
                        } else {
                            Sms sms = drm.getSms(member.toBareJID());
                            if (sms != null) {
                                smsRobot.sendSmsMsg(sms.getCellphone(), msg);
                            }
                        }
                    } else{
                        Log.error("user:"+member+" has no MSN");
                    }
                }
            } catch (GroupNotFoundException e) {
                Log.error(e);
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }

        return null;
    }
}