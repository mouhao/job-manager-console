package com.sohu.quartz.utils;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: SongJiao
 * Date: 2010-1-10
 * Time: 0:43:51
 * 上传的文件都是zip包，这个类负责将zip包解压，删除zip包，显示目录信息
 */
public class FileUploadHelper {
    private static final Logger logger = Logger.getLogger(FileUploadHelper.class);

    private String uploadDir;

    public String getUploadDir() {
        return uploadDir;
    }

    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }

    public void rmDir(String app_path) throws IOException {
        File dir = new File(app_path);
        FileUtils.deleteDirectory(dir);

    }


    /**
     * 查看uploadDir 下有哪些目录
     *
     * @return
     */
    public List<File> getSubDirs() {
        File dir = new File(this.uploadDir);
        File[] files = dir.listFiles();
        List<File> dirs = new ArrayList<File>();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                dirs.add(files[i]);
            }
        }
        return dirs;
    }

    /**
     * 将uploadDir 目录下的 zip文件解压
     */
    public void unzip() throws IOException {
        File[] zipfiles = this.getZipFiles();
        if (zipfiles == null) {
            return;
        }

        for (File zipfile : zipfiles) {
            this.doDecompress(zipfile, new File(this.uploadDir));
            zipfile.delete();
        }
    }

    /**
     * 获得zip文件列表
     *
     * @return
     */
    public File[] getZipFiles() {
        File dir = new File(this.uploadDir);
        return dir.listFiles(new ZipFileFilter());
    }


    /**
     * 解压zip包
     *
     * @param srcFile
     * @param destDir
     * @throws IOException
     */
    protected void doDecompress(File srcFile, File destDir) throws IOException {


        ZipArchiveInputStream is = null;
        final int bufferLen = 1024;
        try {
            is = new ZipArchiveInputStream(new BufferedInputStream(new FileInputStream(srcFile), bufferLen));
            ZipArchiveEntry entry = null;
            while ((entry = is.getNextZipEntry()) != null) {
                if (entry.isDirectory()) {
                    File directory = new File(destDir, entry.getName());
                    directory.mkdirs();
                    logger.debug(directory.getAbsolutePath());

                } else {
                    OutputStream os = null;
                    try {
                        File dist_file = new File(destDir, entry.getName());
                        if (!dist_file.exists()) {
                            FileUtils.forceMkdir(dist_file.getParentFile());
                        }
                        os = new BufferedOutputStream(
                                new FileOutputStream(dist_file), bufferLen);

                        long cnt = IOUtils.copyLarge(is, os);
                        logger.debug(dist_file.getAbsolutePath() + ":" + cnt);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    finally {
                        IOUtils.closeQuietly(os);
                    }
                }
            }
        } finally {
            IOUtils.closeQuietly(is);
        }
    }

}
