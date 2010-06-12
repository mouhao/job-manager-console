package org.jivesoftware.openfire.plugin;

import org.jivesoftware.openfire.SessionManager;
import org.jivesoftware.openfire.group.Group;
import org.jivesoftware.openfire.group.GroupManager;
import org.jivesoftware.openfire.group.GroupNotFoundException;
import org.jivesoftware.openfire.session.ClientSession;
import org.jivesoftware.util.Log;
import org.xmpp.packet.*;

import java.util.Iterator;

public class PacketFilter {

    private static PacketFilter packetFilter = new PacketFilter();
//    RuleManager ruleManager;

    private PacketFilter() {

    }

    public static PacketFilter getInstance() {
        return packetFilter;
    }

//    public void setRuleManager(RuleManager ruleManager) {
//        this.ruleManager = ruleManager;
//    }
//
//    public Rule findMatch(Packet packet) {
//        if (packet.getTo() == null || packet.getFrom() == null) return null;
//        List<Rule> rules = ruleManager.getRules();
//        for (Rule rule : rules) {
//            if (!rule.isDisabled() &&
//                    typeMatch(rule.getPackeType(), packet) &&
//                    sourceDestMatch(rule.getDestType(), rule.getDestination(), packet.getTo()) &&
//                    sourceDestMatch(rule.getSourceType(), rule.getSource(), packet.getFrom())) {
//
//                return rule;
//            }
//        }
//        return null;
//    }
//
//    private boolean typeMatch(Rule.PacketType rulePacketType, Packet packet) {
//        //Simple case. Any.
//        if (rulePacketType == Rule.PacketType.Any) return true;
//
//        else if (packet instanceof Message) {
//            Message message = (Message) packet;
//            if (rulePacketType == Rule.PacketType.Message) {
//                return true;
//            }
//            //Try some types.
//            else if (rulePacketType == Rule.PacketType.MessageChat
//                    && message.getType() == Message.Type.chat) {
//                return true;
//            } else if (rulePacketType == Rule.PacketType.MessageGroupChat
//                    && message.getType() == Message.Type.groupchat) {
//                return true;
//            }
//            return false;
//        } else if (packet instanceof Presence) {
//            if (rulePacketType == Rule.PacketType.Presence) {
//                return true;
//            } else return false;
//        } else if (packet instanceof IQ) {
//            if (rulePacketType == Rule.PacketType.Iq) {
//                return true;
//            } else return false;
//        }
//
//        return false;
//    }
//
//    /**
//     * 2010.06.10 15:49:28 sourceDestMatch:(Group,test,test001@127.0.0.1/spark)false
//     * 2010.06.10 15:49:28 sourceDestMatch:(User,test001@127.0.0.1,test001@127.0.0.1/spark)true
//     */
//    private boolean sourceDestMatch(Rule.SourceDestType type, String ruleToFrom, JID packetToFrom) {
//        if (type == Rule.SourceDestType.Any) return true;
//        if (type == Rule.SourceDestType.User) {
//            if (ruleToFrom.equals(packetToFrom.toBareJID())) {
//                return true;
//            }
//        } else if (type == Rule.SourceDestType.Group) {
//            return packetToFromGroup(ruleToFrom, packetToFrom);
//        } else if (type == Rule.SourceDestType.Component) {
//            if (ruleToFrom.toLowerCase().equals(packetToFrom.getDomain().toLowerCase())) {
//                return true;
//            }
//        } else if (type == Rule.SourceDestType.Other) {
//            if (matchUser(ruleToFrom, packetToFrom)) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    private boolean matchUser(String ruleToFrom, JID packetToFrom) {
//        boolean match = false;
//        //Escape the text so I get a rule to packet match.
//        // String escapedPacketToFrom = JID.unescapeNode(packetToFrom.toBareJID().toString());
//        if (ruleToFrom.indexOf("*") == 0 && ruleToFrom.indexOf("@") == 1) {
//            if (PacketFilterUtil.getDomain(ruleToFrom).equals(packetToFrom.getDomain().toString())) {
//                match = true;
//            }
//        } else {
//            if (ruleToFrom.equals(packetToFrom.toBareJID())) {
//                match = true;
//            }
//        }
//        return match;
//    }
//
//    //sourceDestMatch:(Group,test,test001@127.0.0.1/spark)false
//
//    private boolean packetToFromGroup(String rulegroup, JID packetToFrom) {
//        Group group = null;
//        try {
//            group = GroupManager.getInstance().getProvider().getGroup(rulegroup);
//
//            Iterator<JID> itr = group.getMembers().iterator();
//
//            while (itr.hasNext()) {
//                JID jid = itr.next();
//
//            }
//
//
//        } catch (GroupNotFoundException e) {
//            e.printStackTrace();
//        }
//        if (group == null) {
//            return false;
//        } else {
//            if (group.isUser(packetToFrom)) {
//                return true;
//            }
//        }
//        return false;
//    }

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
                String toGroup = PacketFilterUtil.getJIDName(msg.getTo());

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
                        String jidName=PacketFilterUtil.getJIDName(members.next());
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
