package com.sohu.quartz.jobs;

import com.sohu.quartz.utils.JarFileFilter;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.io.*;

/**
 * Created by IntelliJ IDEA.
 * User: SongJiao
 * Date: 2010-1-10
 * Time: 23:03:36
 * 这个job开启一个新的java进程，利用Runtime.getRuntime.exe()执行java -classpath
 */
public class SimpleJob implements Job {
    private static final Logger logger = Logger.getLogger(SimpleJob.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("**********************************************");
        logger.info("job:" + context.getJobDetail().getName() + " start");
        JobDataMap datamap = context.getJobDetail().getJobDataMap();
        String appPath = datamap.get("appPath").toString();
        String mainClass = datamap.get("mainClass").toString();
        String main_args = datamap.get("main_args").toString();
        String jvm_args = datamap.get("jvm_args").toString();
        String[] margs = null;
        if (!"".equals(main_args)) {
            margs = main_args.split(" ");
        }

        String[] jargs = null;
        if (!"".equals(jvm_args)) {
            jargs = jvm_args.split(" ");
        }

        String classPath = appPath + File.separator + "lib";
        File[] jarFiles = new File(classPath).listFiles(new JarFileFilter());
        File[] jarFiles2 = new File(appPath).listFiles(new JarFileFilter());

        StringBuffer exe_str = new StringBuffer(" java ");

        for (String jvmarg : jargs) {
            exe_str.append(jvmarg).append(" ");
        }

        exe_str.append(" -classpath ");

        for (File jarfile : jarFiles2) {
            exe_str.append(jarfile.getAbsolutePath()).append(File.pathSeparator);
        }

        for (int i = 0; i < jarFiles.length - 1; i++) {
            exe_str.append(jarFiles[i].getAbsolutePath()).append(File.pathSeparator);
        }

        exe_str.append(jarFiles[jarFiles.length - 1].getAbsolutePath()).append(" ");

        exe_str.append(mainClass).append(" ");

        for (String arg : margs) {
            exe_str.append(arg).append(" ");
        }

        try {

            Process p = Runtime.getRuntime().exec(exe_str.toString());
            InputStream in = p.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line = br.readLine();
            while (line != null) {
                logger.info(line);
                line = br.readLine();
            }
            int flag = p.waitFor();
            if (flag == 0) {
                logger.info("job:" + context.getJobDetail().getName() + " executed ok");
            } else {
                logger.info("job:" + context.getJobDetail().getName() + " executed failed");
            }
            logger.info("**********************************************");
        } catch (IOException e) {
            throw new JobExecutionException(e);
        } catch (InterruptedException e) {
            throw new JobExecutionException(e);
        }


    }
}
