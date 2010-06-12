package org.jivesoftware.openfire.plugin;

import org.jivesoftware.openfire.container.Plugin;
import org.jivesoftware.openfire.container.PluginManager;
import org.jivesoftware.openfire.interceptor.InterceptorManager;
import org.jivesoftware.openfire.interceptor.PacketInterceptor;
import org.jivesoftware.openfire.interceptor.PacketRejectedException;
import org.jivesoftware.openfire.plugin.msn.MsnRobot;
import org.jivesoftware.openfire.plugin.sms.SmsRobot;
import org.jivesoftware.openfire.session.Session;
import org.jivesoftware.util.Log;
import org.xmpp.packet.Packet;

import java.io.File;

public class PacketFilterPlugin implements Plugin, PacketInterceptor {

    private static PluginManager pluginManager;

    public PacketFilterPlugin() {
        interceptorManager = InterceptorManager.getInstance();
    }

    //Packet Filter
    private PacketFilter pf;

    //Hook for intercpetorn
    private InterceptorManager interceptorManager;


    public void initializePlugin(PluginManager manager, File pluginDirectory) {
        // register with interceptor manager
        Log.info("Packet Filter loaded...");
        interceptorManager.addInterceptor(this);
        this.pluginManager = manager;
        pf = PacketFilter.getInstance();
        MsnRobot.getInstance();
        SmsRobot.getInstance();

    }

    public void destroyPlugin() {
        // unregister with interceptor manager
        interceptorManager.removeInterceptor(this);

    }


    public String getName() {
        return "packetFilter";

    }

    public static PluginManager getPluginManager() {
        return pluginManager;
    }

    /**
     * @param packet    the packet to take action on.
     * @param session   the session that received or is sending the packet.
     * @param incoming  flag that indicates if the packet was read by the server or sent from
     *                  the server.
     * @param processed flag that indicates if the action (read/send) was performed. (PRE vs. POST).
     * @throws PacketRejectedException
     */
    public void interceptPacket(Packet packet, Session session, boolean incoming, boolean processed) throws PacketRejectedException {

        if (processed) {
            return;
        }

        if (incoming) {
            PacketFilter.getInstance().doFilter(packet);
        }
    }
}       
