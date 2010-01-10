package com.sohu.quartz.pojo;

/**
 * Created by IntelliJ IDEA.
 * User: SongJiao
 * Date: 2010-1-10
 * Time: 16:59:01
 * To change this template use File | Settings | File Templates.
 */
public class FileBean {
    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getLastmodified() {
        return lastmodified;
    }

    public void setLastmodified(String lastmodified) {
        this.lastmodified = lastmodified;
    }

    private String filename;
    private String lastmodified;
}
