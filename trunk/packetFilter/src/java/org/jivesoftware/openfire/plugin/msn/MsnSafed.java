package org.jivesoftware.openfire.plugin.msn;

import org.jivesoftware.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by IntelliJ IDEA.
 * User: songjiao
 * Date: 2010-8-7
 * Time: 2:38:13
 * To change this template use File | Settings | File Templates.
 */
public class MsnSafed extends Thread {
    @Override
    public void run() {
       while(true){
           try {
               Thread.sleep(10000l);


               if(!isMsnLive()){
                   Log.info("MSN offline,restart it.");
                   MsnRobot.getInstance().start();
               }else{
                   Log.info("check MSN online status ok");
               }

           } catch (InterruptedException e) {
               e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
           }
       }

    }

    private boolean isMsnLive(){
        boolean flag=true;
        Process p= null;
        try {
            p = Runtime.getRuntime().exec("netstat -na");
            p.getInputStream();
            InputStreamReader ipsr=new InputStreamReader(p.getInputStream());
            BufferedReader br=new BufferedReader(ipsr);
            String line=null;
            boolean find=false;
            while((line=br.readLine())!=null){
                  if(line.indexOf("1863")>0&&line.indexOf("ESTABLISHED")>0){
                      find=true;
                  }
            }
            if(find){
                flag=true;
            }else{
                flag=false;
            }
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            flag=false;
        }


        return flag;
    }

    public static void main(String[] args){
        MsnSafed msf=new MsnSafed();
        msf.start();
    }
}
