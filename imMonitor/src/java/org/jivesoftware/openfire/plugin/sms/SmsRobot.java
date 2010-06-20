package org.jivesoftware.openfire.plugin.sms;

import com.changyou.client.Clinet;
import com.changyou.sms.manager.smsInterface;

/**
 * Created by IntelliJ IDEA.
 * User: changyou
 * Date: 2010-6-10
 * Time: 11:42:36
 * To change this template use File | Settings | File Templates.
 */
public class SmsRobot {

    private static SmsRobot instance=null;
    private static final int CNT=1;
    private static final String KEY="DFSDFS";
    private  smsInterface clinet=null;
    private SmsRobot() {
    }

    public static SmsRobot getInstance(){
        synchronized(KEY){
            if(instance==null){
                instance=new SmsRobot();
                instance.clinet=Clinet.getClient();
            }
            return instance;            
        }
    }


    public void sendSmsMsg(String phone, String msg) {
        this.clinet.send(phone, msg, CNT);
    }
}
