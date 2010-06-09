package org.jivesoftware.openfire.plugin.rules;

import org.jivesoftware.openfire.SessionManager;
import org.jivesoftware.openfire.interceptor.PacketRejectedException;
import org.jivesoftware.openfire.session.ClientSession;
import org.jivesoftware.util.JiveGlobals;
import org.jivesoftware.util.Log;
import org.xmpp.packet.JID;
import org.xmpp.packet.Message;
import org.xmpp.packet.Packet;

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
        String pfFrom = JiveGlobals.getProperty("pf.From", "packetfilter");
        if (packet instanceof Message) {
            Message in = (Message) packet.createCopy();
            if (clientSession != null && in.getBody() != null) {
                in.setFrom(new JID(pfFrom));
                String rejectMessage = JiveGlobals.getProperty("pf.rejectMessage", "Your message was rejected by the packet filter");
                in.setBody(rejectMessage);
                in.setType(Message.Type.error);
                in.setTo(packet.getFrom());
                String rejectSubject = JiveGlobals.getProperty("pf.rejectSubject", "Rejected");
                in.setSubject(rejectSubject);
                clientSession.process(in);

            }

        }

        return null;
    }
}
