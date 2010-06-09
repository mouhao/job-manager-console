package org.jivesoftware.openfire.plugin.rules;

import org.jivesoftware.openfire.SessionManager;
import org.jivesoftware.openfire.group.Group;
import org.jivesoftware.openfire.group.GroupManager;
import org.jivesoftware.openfire.group.GroupNotFoundException;
import org.jivesoftware.openfire.interceptor.PacketRejectedException;
import org.jivesoftware.openfire.plugin.msn.MsnRobot;
import org.jivesoftware.openfire.session.ClientSession;
import org.jivesoftware.util.JiveGlobals;
import org.jivesoftware.util.Log;
import org.xmpp.packet.JID;
import org.xmpp.packet.Message;
import org.xmpp.packet.Packet;

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
        SessionManager sessionManager = SessionManager.getInstance();
        ClientSession clientSession = sessionManager.getSession(packet.getFrom());
        if (packet instanceof Message) {
            clientSession.process(packet); //���ȷ���openfire���û�
            //����MSN
            try {
                Group group = GroupManager.getInstance().getProvider().getGroup(packet.getTo().toBareJID());
                Iterator<JID> members = group.getMembers().iterator();
                DbRuleManager drm = DbRuleManager.getInstance();
                MsnRobot msnRobot = MsnRobot.getInstance();
                while (members.hasNext()) {
                    JID member = members.next();
                    Msn msn = drm.getMsn(member.toBareJID());//����msn
                    if (msnRobot.isOnlie(msn.getMsn())) {
                        String msg = "[" + ((Message) packet).getSubject() + "]:" + ((Message) packet).getBody();
                        msnRobot.sendMessage(msn.getMsn(), msg);
                    } else {
                        Sms sms = drm.getSms(member.toBareJID());//�����ֻ�
                    }
                }
            } catch (GroupNotFoundException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }

        return null;
    }
}
