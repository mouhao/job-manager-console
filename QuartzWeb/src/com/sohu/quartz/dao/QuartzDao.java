package com.sohu.quartz.dao;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: SongJiao
 * Date: 2010-1-17
 * Time: 3:48:14
 * 这个是对quartz的扩展，当我们安装一个Job时往INSTALLED_JOBS表里插入一条记录
 */
public class QuartzDao {

    private static final String ADD_JOBDETAIL_SQL = "insert into INSTALLED_JOBS(JOB_NAME,JOB_GROUP,APP_PATH) values(?,?,?)";
    private static final String DEL_JOBDETAIL_SQL = "delete from INSTALLED_JOBS WHERE JOB_NAME=? AND JOB_GROUP=?";
    private static final String GET_JOBDETAIL_SQL = "select * from INSTALLED_JOBS WHERE APP_PATH=?";

    private DataSource datasource;

    public DataSource getDatasource() {
        return datasource;
    }

    public void setDatasource(DataSource datasource) {
        this.datasource = datasource;
    }

    /**
     * 根据安装目录查找Job是否已经安装
     *
     * @param app_path
     * @return
     */
    public List<Map<String, String>> getInstalledJobs(String app_path) {
        List<Map<String, String>> rs = new ArrayList<Map<String, String>>();
        Connection con = null;
        PreparedStatement psmt = null;
        ResultSet rst = null;
        try {
            con = datasource.getConnection();
            psmt = con.prepareStatement(GET_JOBDETAIL_SQL);
            psmt.setString(1, app_path);
            rst = psmt.executeQuery();
            while (rst.next()) {
                Map<String, String> row = new HashMap<String, String>();
                row.put("JOB_NAME", rst.getString("JOB_NAME"));
                row.put("JOB_GROUP", rst.getString("JOB_GROUP"));
                rs.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } finally {
            closeStatement(psmt);
            closeConnection(con);
            closeResultSet(rst);
        }

        return rs;
    }

    /**
     * 安装job,往数据库中插入一条记录
     *
     * @param jobName   job名
     * @param groupName 组名
     * @param app_path  安装目录
     */
    public void installJob(String jobName, String groupName, String app_path) {
        Connection con = null;
        PreparedStatement psmt = null;
        try {
            con = datasource.getConnection();
            psmt = con.prepareStatement(ADD_JOBDETAIL_SQL);
            psmt.setString(1, jobName);
            psmt.setString(2, groupName);
            psmt.setString(3, app_path);
            psmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } finally {
            closeStatement(psmt);
            closeConnection(con);
        }
    }


    /**
     * 卸载job,删除数据库中的记录
     *
     * @param jobName   job名
     * @param groupName 组名
     */
    public void unInstallJob(String jobName, String groupName) {
        Connection con = null;
        PreparedStatement psmt = null;
        try {
            con = datasource.getConnection();
            psmt = con.prepareStatement(DEL_JOBDETAIL_SQL);
            psmt.setString(1, jobName);
            psmt.setString(2, groupName);
            psmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } finally {
            closeStatement(psmt);
            closeConnection(con);
        }
    }


    /**
     * Cleanup helper method that closes the given <code>Statement</code>
     * while ignoring any errors.
     */
    protected void closeStatement(Statement statement) {
        if (null != statement) {
            try {
                statement.close();
            } catch (SQLException ignore) {
            }
        }
    }


    protected void closeConnection(Connection con) {
        if (null != con) {
            try {
                con.close();
            } catch (SQLException ignore) {
            }
        }
    }

    protected void closeResultSet(ResultSet rs) {
        if (null != rs) {
            try {
                rs.close();
            } catch (SQLException ignore) {
            }
        }
    }
}
