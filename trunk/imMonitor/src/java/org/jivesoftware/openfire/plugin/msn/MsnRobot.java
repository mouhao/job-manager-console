package org.jivesoftware.openfire.plugin.msn;

import net.sf.jml.*;
import net.sf.jml.event.MsnAdapter;
import net.sf.jml.event.MsnContactListAdapter;
import net.sf.jml.impl.MsnContactImpl;
import net.sf.jml.impl.MsnMessengerFactory;
import net.sf.jml.message.MsnControlMessage;
import net.sf.jml.message.MsnDatacastMessage;
import net.sf.jml.message.MsnInstantMessage;
import net.sf.jml.message.MsnSystemMessage;
import net.sf.jml.message.MsnUnknownMessage;

import net.sf.jml.protocol.MsnOutgoingMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Roger Chen
 */
public class MsnRobot {

    private static final Log log = LogFactory.getLog(MsnRobot.class);
    private static final String EMAIL = "cyou_monitor@live.cn";
    private static final String PASSWORD = "7days1week";
    private static MsnRobot instance;
    private static boolean contactListInitCompleted = false;
    private static boolean  logincompleted=false;

    private MsnMessenger messenger;

    private MsnRobot() {

    }

    public static MsnRobot getInstance() {
        synchronized (EMAIL) {
            if (instance == null) {
                instance = new MsnRobot();
                instance.start();
                while (!contactListInitCompleted||!logincompleted) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                }
            }
        }
        return instance;
    }

    public void start() {
        messenger = MsnMessengerFactory.createMsnMessenger(EMAIL,
                PASSWORD);
        messenger
                .setSupportedProtocol(new MsnProtocol[]{MsnProtocol.MSNP11});
        messenger.getOwner().setInitStatus(MsnUserStatus.ONLINE);
        messenger.getOwner().setInitDisplayName("畅游监控助手");
        messenger.setLogIncoming(true);
        messenger.setLogOutgoing(true);
        messenger.addListener(new MsnListener());
        messenger.login();
        

      

        
    }


    public void sendMessage(Email email, String msg) {
        this.messenger.sendText(email, msg);

    }

    public boolean isOnlie(String email){
        boolean online=false;
        try{
             online=this.messenger.getContactList().getContactByEmail(Email.parseStr(email)).getStatus().equals(MsnUserStatus.OFFLINE);
        }catch(Exception e){
            online=false;
        }
        return online;
    }

    /**
     * 判断是不是好友
     * @param email
     * @return
     */
    public boolean isContect(String email){
        MsnContact contect=null;
        try{
            contect=this.messenger.getContactList().getContactByEmail(Email.parseStr(email));
        }catch(Exception e){
            contect=null;
        }
        return contect==null?false:true;
    }

    public void addFriend(String email){
        try{
        this.messenger.addFriend(Email.parseStr(email),"畅游监控助手");
        }catch(Exception e){
            
        }
    }

    public void sendMessage(String email, String msg) {
        this.messenger.sendText(Email.parseStr(email), msg);
        
    }


    private static class MsnListener extends MsnAdapter {

        public void exceptionCaught(MsnMessenger messenger, Throwable throwable) {
            log.error(messenger + throwable.toString(), throwable);
        }

        public void loginCompleted(MsnMessenger messenger) {
            MsnRobot.logincompleted=true;
        }

        public void logout(MsnMessenger messenger) {
            log.info(messenger + " logout");
        }

        public void instantMessageReceived(MsnSwitchboard switchboard,
                                           MsnInstantMessage message, MsnContact friend) {
            log.info(switchboard + " recv instant message " + message);
            switchboard.sendMessage(message, false);
        }

        public void systemMessageReceived(MsnMessenger messenger,
                                          MsnSystemMessage message) {
            log.info(messenger + " recv system message " + message);
        }

        public void controlMessageReceived(MsnSwitchboard switchboard,
                                           MsnControlMessage message, MsnContact contact) {
            log.info(switchboard + " recv control message from "
                    + contact.getEmail());
            switchboard.sendMessage(message, false);
        }

        public void datacastMessageReceived(MsnSwitchboard switchboard,
                                            MsnDatacastMessage message, MsnContact friend) {
            log.info(switchboard + " recv datacast message " + message);

            switchboard.sendMessage(message, false);
        }

        public void unknownMessageReceived(MsnSwitchboard switchboard,
                                           MsnUnknownMessage message, MsnContact friend) {
            log.info(switchboard + " recv unknown message " + message);
        }

        public void contactListInitCompleted(MsnMessenger messenger) {
            MsnRobot.contactListInitCompleted=true;
        }

        public void contactListSyncCompleted(MsnMessenger messenger) {
            log.info(messenger + " contact list sync completed");
        }

        public void contactStatusChanged(MsnMessenger messenger,
                                         MsnContact friend) {
            log.info(messenger + " friend " + friend.getEmail()
                    + " status changed from " + friend.getOldStatus() + " to "
                    + friend.getStatus());
        }

        public void ownerStatusChanged(MsnMessenger messenger) {
            log.info(messenger + " status changed from "
                    + messenger.getOwner().getOldStatus() + " to "
                    + messenger.getOwner().getStatus());
        }

        public void contactAddedMe(MsnMessenger messenger, MsnContact friend) {
            messenger.addFriend(friend.getEmail(), friend.getFriendlyName());
            log.info(friend.getEmail() + " add " + messenger);
        }

        public void contactRemovedMe(MsnMessenger messenger, MsnContact friend) {
            log.info(friend.getEmail() + " remove " + messenger);
        }

        public void switchboardClosed(MsnSwitchboard switchboard) {
            log.info(switchboard + " closed");
        }

        public void switchboardStarted(MsnSwitchboard switchboard) {
            log.info(switchboard + " started");
        }

        public void contactJoinSwitchboard(MsnSwitchboard switchboard,
                                           MsnContact friend) {
            log.info(friend.getEmail() + " join " + switchboard);
        }

        public void contactLeaveSwitchboard(MsnSwitchboard switchboard,
                                            MsnContact friend) {
            log.info(friend.getEmail() + " leave " + switchboard);
        }

    }

}