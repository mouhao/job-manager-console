package com.sohu.quartz.utils;

import java.io.File;
import java.io.FileFilter;

/**
 * Created by IntelliJ IDEA.
 * User: SongJiao
 * Date: 2010-1-10
 * Time: 14:35:48
 * 筛选出jar包文件
 */
public class JarFileFilter implements FileFilter {
    @Override
    public boolean accept(File file) {
        String fileName = file.getName().toLowerCase();
        if (fileName.endsWith(".jar")) {
            return true;
        } else {
            return false;
        }
    }
}