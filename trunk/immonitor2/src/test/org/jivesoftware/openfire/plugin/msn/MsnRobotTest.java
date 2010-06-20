package org.jivesoftware.openfire.plugin.msn;

import junit.framework.TestCase;
import net.sf.jml.Email;

/**
 * Created by IntelliJ IDEA.
 * User: songjiao
 * Date: 2010-5-23
 * Time: 14:35:02
 * To change this template use File | Settings | File Templates.
 */
public class MsnRobotTest extends TestCase {
    private MsnRobot msnRobot=null;
    @Override
    protected void setUp() throws Exception {
        msnRobot=MsnRobot.getInstance();
    }



    @Override
    protected void tearDown() throws Exception {
        
    }
}


