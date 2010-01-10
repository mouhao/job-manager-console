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
 * To change this template use File | Settings | File Templates.
 */
public class SimpleJob implements Job {
    private static final Logger logger = Logger.getLogger(SimpleJob.class);
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("**********************************************");
        logger.info("job:"+context.getJobDetail().getName()+" start");
        JobDataMap datamap = context.getJobDetail().getJobDataMap();
        String appPath = datamap.get("appPath").toString();
        String mainClass = datamap.get("mainClass").toString();
        String argss = datamap.get("args").toString();
        String[] args = null;
        if (!"".equals(argss)) {
            args = argss.split(" ");
        }
        String classPath = appPath + File.separator + "lib";
        File[] jarFiles = new File(classPath).listFiles(new JarFileFilter());
        File[] jarFiles2=new File(appPath).listFiles(new JarFileFilter());

        StringBuffer exe_str = new StringBuffer();
        exe_str.append("java -classpath ");

        for(File jarfile:jarFiles2){
            exe_str.append(jarfile.getAbsolutePath()).append(File.pathSeparator);
        }

        for (int i = 0; i < jarFiles.length - 1; i++) {
            exe_str.append(jarFiles[i].getAbsolutePath()).append(File.pathSeparator);
        }

        exe_str.append(jarFiles[jarFiles.length-1].getAbsolutePath()).append(" ");

        exe_str.append(mainClass).append(" ");

        for (String arg : args) {
            exe_str.append(arg).append(" ");
        }

        try {

            Process p = Runtime.getRuntime().exec(exe_str.toString());
            InputStream in=p.getInputStream();
            BufferedReader br=new BufferedReader(new InputStreamReader(in));
            String line=br.readLine();
            while(line!=null){
                logger.info(line);
                line=br.readLine();
            }
            int flag = p.waitFor();
            if(flag==0){
                logger.info("job:"+context.getJobDetail().getName()+" executed ok");
            }else{
                logger.info("job:"+context.getJobDetail().getName()+" executed failed");
            }
            logger.info("**********************************************");
        } catch (IOException e) {
            throw new JobExecutionException(e);
        } catch (InterruptedException e) {
            throw new JobExecutionException(e);
        }


    }
}
