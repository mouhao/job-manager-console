package org.jivesoftware.openfire.plugin;

import org.jivesoftware.openfire.SessionManager;
import org.jivesoftware.openfire.group.Group;
import org.jivesoftware.openfire.group.GroupManager;
import org.jivesoftware.openfire.group.GroupNotFoundException;
import org.jivesoftware.openfire.session.ClientSession;
import org.jivesoftware.util.Log;
import org.xmpp.packet.*;

import java.util.Iterator;

public class IMMonitor {

    private static IMMonitor imMonitor = new IMMonitor();


    private IMMonitor() {

    }

    public static IMMonitor getInstance() {
        return imMonitor;
    }



    /**
     * 拦截所有packet类型为Message的，并且packetID为cyou_monitor_*的包
     *
     * @param packet
     */
    public void doFilter(Packet packet) {
        if (packet instanceof Message) {
            Message msg = (Message) packet;
            String packetID = packet.getID();
            if (packetID.startsWith("cyou_monitor")) {
                Log.info("==================CYOU_MONITOR_PACKET===============\nSubject:" + ((Message) packet).getSubject() + "\nTo:" + packet.getTo() + "\nFrom:" + packet.getFrom() + "\n" + ((Message) packet).getBody());
                SessionManager sessionManager = SessionManager.getInstance();
                ClientSession clientSession = sessionManager.getSession(packet.getFrom());
                String toGroup = ImMonitorUtil.getJIDName(msg.getTo());

                Group group = null;
                try {
                    group = GroupManager.getInstance().getProvider().getGroup(toGroup);
                } catch (GroupNotFoundException e) {
                      
                }
                if (group == null) {
                    Log.info("Group:"+toGroup+" not exists");
                    //FeedBack(packet, "group:" + toGroup + " not exists!",clientSession);
                }else{
                    Log.info("send message to group:"+group.getName());
                    Iterator<JID> members = group.getMembers().iterator();
                    while(members.hasNext()){
                        String jidName= ImMonitorUtil.getJIDName(members.next());
                        Log.info("send Message to:"+jidName);
                        MessageDispatcher md= new MessageDispatcher(packet,jidName);
                        md.start();
                    }
                }

            }
        }
    }

    /**
     * 给客户端反馈消息
     *
     * @param packet
     * @param msg
     */
    private void FeedBack(Packet packet, String msg,ClientSession clientSession) {
        Message in = (Message) packet.createCopy();
        if (clientSession != null && in.getBody() != null) {

            in.setFrom(new JID("system"));
            in.setBody(msg);
            in.setType(Message.Type.error);
            in.setTo(packet.getFrom());

            in.setSubject("Group not exists");
            clientSession.process(in);

        }

    }
}
