package org.jivesoftware.openfire.plugin;

import org.jivesoftware.database.DbConnectionManager;

import org.jivesoftware.openfire.plugin.msn.Msn;
import org.jivesoftware.openfire.plugin.sms.Sms;
import org.jivesoftware.util.Log;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class DBManager {


    private static final String INSERT_MSN = "insert into ofMsn(jid,msn,enable) values(?,?,?)";
    private static final String UPDATE_MSN = "update ofMsn set jid=?,msn=?,enable=? where id=?";
    private static final String GET_MSN_BY_JID = "select id,jid,msn,enable from ofMsn where jid=?";
    private static final String GET_MSN_BY_ID = "select id,jid,msn,enable from ofMsn where id=?";
    private static final String GET_ALL_MSN = "select id,jid,msn,enable from ofMsn order by id desc";
    private static final String DELETE_MSN_BY_ID = "delete from ofMsn where id=?";


    private static final String INSERT_SMS = "insert into ofSms(jid,cellphone,enable) values(?,?,?)";
    private static final String UPDATE_SMS = "update ofSms set jid=?,cellphone=?,enable=? where id=?";
    private static final String GET_SMS_BY_ID = "select id,jid,cellphone,enable from ofSms where id=?";
    private static final String GET_ALL_SMS = "select id,jid,cellphone,enable from ofSms order by id desc";
    private static final String DELETE_SMS_BY_ID = "delete from ofSms where id=?";
    private static final String GET_SMS_BY_JID = "select id,jid,cellphone,enable from ofSms where jid=?";

    public List<Msn> getAllMsn() {
        List<Msn> msns = new ArrayList<Msn>();

        synchronized (GET_ALL_MSN) {

            Connection con = null;
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            try {
                con = DbConnectionManager.getConnection();
                pstmt = con.prepareStatement(GET_ALL_MSN);
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    Msn msn = new Msn();
                    msn.setId(rs.getLong(1));
                    msn.setJid(rs.getString(2));
                    msn.setMsn(rs.getString(3));
                    msn.setEnable(rs.getBoolean(4));
                    msns.add(msn);
                }


            } catch (SQLException sqle) {
                Log.error(sqle);
            }
            finally {
                DbConnectionManager.closeConnection(pstmt, con);
            }
            return msns;
        }
    }

    public List<Sms> getAllSms() {
        List<Sms> smss = new ArrayList<Sms>();

        synchronized (GET_ALL_SMS) {

            Connection con = null;
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            try {
                con = DbConnectionManager.getConnection();
                pstmt = con.prepareStatement(GET_ALL_SMS);
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    Sms sms = new Sms();
                    sms.setId(rs.getLong(1));
                    sms.setJid(rs.getString(2));
                    sms.setCellphone(rs.getString(3));
                    sms.setEnable(rs.getBoolean(4));
                    smss.add(sms);
                }


            } catch (SQLException sqle) {
                Log.error(sqle);
            }
            finally {
                DbConnectionManager.closeConnection(pstmt, con);
            }
            return smss;
        }
    }

    public Sms getSmsById(String id) {
        Sms sms = null;

        synchronized (GET_SMS_BY_ID) {

            Connection con = null;
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            try {
                con = DbConnectionManager.getConnection();
                pstmt = con.prepareStatement(GET_SMS_BY_ID);
                pstmt.setString(1, id);
                rs = pstmt.executeQuery();

                while (rs.next()) {
                    sms = new Sms();
                    sms.setId(rs.getLong(1));
                    sms.setJid(rs.getString(2));
                    sms.setCellphone(rs.getString(3));
                    sms.setEnable(rs.getBoolean(4));

                }


            } catch (SQLException sqle) {
                Log.error(sqle);
            }
            finally {
                DbConnectionManager.closeConnection(pstmt, con);
            }

        }


        return sms;
    }


    public Msn getMsnById(String id) {
        Msn msn = null;

        synchronized (GET_MSN_BY_JID) {
            Connection con = null;
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            try {
                con = DbConnectionManager.getConnection();
                pstmt = con.prepareStatement(GET_MSN_BY_ID);
                pstmt.setString(1, id);
                rs = pstmt.executeQuery();

                while (rs.next()) {
                    msn = new Msn();
                    msn.setId(rs.getLong(1));
                    msn.setJid(rs.getString(2));
                    msn.setMsn(rs.getString(3));
                    msn.setEnable(rs.getBoolean(4));
                }


            } catch (SQLException sqle) {
                Log.error(sqle);
            }
            finally {
                DbConnectionManager.closeConnection(pstmt, con);
            }
        }
        return msn;
    }

    public Msn getMsn(String jid) {
        Msn msn = null;

        synchronized (GET_MSN_BY_JID) {
            Connection con = null;
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            try {
                con = DbConnectionManager.getConnection();
                pstmt = con.prepareStatement(GET_MSN_BY_JID);
                pstmt.setString(1, jid);
                rs = pstmt.executeQuery();

                while (rs.next()) {
                    msn = new Msn();
                    msn.setId(rs.getLong(1));
                    msn.setJid(rs.getString(2));
                    msn.setMsn(rs.getString(3));
                    msn.setEnable(rs.getBoolean(4));
                }


            } catch (SQLException sqle) {
                Log.error(sqle);
            }
            finally {
                DbConnectionManager.closeConnection(pstmt, con);
            }
        }
        return msn;
    }


    public boolean addSMS(String jid, String cellphone, int enable) {
        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = DbConnectionManager.getConnection();
            pstmt = con.prepareStatement(INSERT_SMS);

            pstmt.setString(1, jid);
            pstmt.setString(2, cellphone);
            pstmt.setInt(3, enable);
            pstmt.execute();
        } catch (SQLException sqle) {
            Log.error(sqle);
            return false;
        }
        finally {
            DbConnectionManager.closeConnection(pstmt, con);
        }
        return true;
    }

    public boolean updateSms(Sms sms) {
        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = DbConnectionManager.getConnection();
            pstmt = con.prepareStatement(UPDATE_SMS);
            pstmt.setString(1, sms.getJid());
            pstmt.setString(2, sms.getCellphone());
            pstmt.setBoolean(3, sms.isEnable());
            pstmt.setLong(4, sms.getId());
            pstmt.execute();
        } catch (SQLException sqle) {
            Log.error(sqle);
            return false;
        }
        finally {
            DbConnectionManager.closeConnection(pstmt, con);
        }
        return true;
    }

    public boolean deleteSmsById(String id) {
        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = DbConnectionManager.getConnection();
            pstmt = con.prepareStatement(DELETE_SMS_BY_ID);
            pstmt.setString(1, id);
            pstmt.execute();
        } catch (SQLException sqle) {
            Log.error(sqle);
            return false;
        }
        finally {
            DbConnectionManager.closeConnection(pstmt, con);
        }
        return true;
    }

    public boolean deleteMsnById(String id) {
        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = DbConnectionManager.getConnection();
            pstmt = con.prepareStatement(DELETE_MSN_BY_ID);
            pstmt.setString(1, id);
            pstmt.execute();
        } catch (SQLException sqle) {
            Log.error(sqle);
            return false;
        }
        finally {
            DbConnectionManager.closeConnection(pstmt, con);
        }
        return true;
    }

    public boolean updateSms(String jid, String cellphone) {
        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = DbConnectionManager.getConnection();
            pstmt = con.prepareStatement(UPDATE_SMS);
            pstmt.setString(1, cellphone);
            pstmt.setString(2, jid);
            pstmt.execute();
        } catch (SQLException sqle) {
            Log.error(sqle);
            return false;
        }
        finally {
            DbConnectionManager.closeConnection(pstmt, con);
        }
        return true;
    }

    public boolean updateMsn(Msn msn) {
        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = DbConnectionManager.getConnection();
            pstmt = con.prepareStatement(UPDATE_MSN);


            pstmt.setString(1, msn.getJid());
            pstmt.setString(2, msn.getMsn());
            pstmt.setBoolean(3, msn.isEnable());
            pstmt.setLong(4, msn.getId());
            pstmt.execute();
        } catch (SQLException sqle) {
            Log.error(sqle);
            return false;
        }
        finally {
            DbConnectionManager.closeConnection(pstmt, con);
        }
        return true;
    }

    public boolean updateMsn(String jid, int enable) {
        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = DbConnectionManager.getConnection();
            pstmt = con.prepareStatement(DBManager.UPDATE_MSN);

            pstmt.setByte(1, new Byte("+enable+"));
            pstmt.setString(2, jid);
            pstmt.execute();
        } catch (SQLException sqle) {
            Log.error(sqle);
            return false;
        }
        finally {
            DbConnectionManager.closeConnection(pstmt, con);
        }
        return true;
    }


    public boolean addMsn(String jid, String msn, int enable) {
        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = DbConnectionManager.getConnection();
            pstmt = con.prepareStatement(INSERT_MSN);

            pstmt.setString(1, jid);
            pstmt.setString(2, msn);
            pstmt.setInt(3, enable);
            pstmt.execute();
        } catch (SQLException sqle) {
            Log.error(sqle);
            return false;
        }
        finally {
            DbConnectionManager.closeConnection(pstmt, con);
        }
        return true;
    }


    private static final DBManager DB_MANAGER = new DBManager();


    private DBManager() {

    }

    public static DBManager getInstance() {
        return DB_MANAGER;
    }


    public Sms getSmsByJid(String jid) {
        Sms sms = null;

        synchronized (GET_SMS_BY_JID) {

            Connection con = null;
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            try {
                con = DbConnectionManager.getConnection();
                pstmt = con.prepareStatement(GET_SMS_BY_JID);
                pstmt.setString(1, jid);
                rs = pstmt.executeQuery();

                while (rs.next()) {
                    sms = new Sms();
                    sms.setId(rs.getLong(1));
                    sms.setJid(rs.getString(2));
                    sms.setCellphone(rs.getString(3));
                    sms.setEnable(rs.getBoolean(4));

                }


            } catch (SQLException sqle) {
                Log.error(sqle);
            }
            finally {
                DbConnectionManager.closeConnection(pstmt, con);
            }

        }


        return sms;
    }
}
