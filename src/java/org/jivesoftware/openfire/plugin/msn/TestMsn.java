package org.jivesoftware.openfire.plugin.msn;

import net.sf.jml.Email;

/**
 * Created by IntelliJ IDEA.
 * User: songjiao
 * Date: 2010-5-23
 * Time: 14:35:02
 * To change this template use File | Settings | File Templates.
 */
public class TestMsn {
    public static void main(String[] args) throws InterruptedException {
        MsnRobot smn = MsnRobot.getInstance();
        smn.sendMessage(Email.parseStr("songancha@hotmail.com"), "测试消息");


    }
}


