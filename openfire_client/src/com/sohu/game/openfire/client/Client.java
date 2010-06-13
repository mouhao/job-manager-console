package com.sohu.game.openfire.client;

import org.jivesoftware.smack.*;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: changyou
 * Date: 2010-6-11
 * Time: 16:08:06
 * To change this template use File | Settings | File Templates.
 */
public class Client {
    private static final String PACKETID = "cyou_monitor_";
    private XMPPConnection connection=null;
    public static void main(String[] args) throws XMPPException, IOException {
        String group = args[0];
        String subject = args[1];
        String message = args[2];
        if (args.length != 3) {
            System.out.println("Useage:java -jar group msg");
            System.exit(-1);
        }
        Client.getInstance().sendMessage(group,subject,message);

    }


    private static Client instance = new Client();

    private Client() {
        try{
        Properties p = new Properties();
        p.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("server.properties"));
        String host = p.getProperty("server.host");
        int port = Integer.parseInt(p.getProperty("server.port"));
        ConnectionConfiguration config = new ConnectionConfiguration(host, port);
        connection = new XMPPConnection(config);
        connection.connect();
        }catch(Exception e){
            e.printStackTrace();
            System.exit(-1);
        }

    }

    public void sendMessage(String group, String subject, String message) {
        Message msg = new Message();
        msg.setPacketID(PACKETID + System.currentTimeMillis());
        msg.setTo(group);
        msg.setSubject(subject);
        msg.setBody(message);
        connection.sendPacket(msg);
    }

    public static Client getInstance() {
        return instance;
    }
}
