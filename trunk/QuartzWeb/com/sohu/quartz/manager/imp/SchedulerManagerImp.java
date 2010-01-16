package com.sohu.quartz.manager.imp;

import com.sohu.quartz.dao.QuartzDao;
import com.sohu.quartz.jobs.SimpleJob;
import com.sohu.quartz.manager.SchedulerManager;
import com.sohu.Constant;

import java.text.ParseException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import com.sohu.quartz.pojo.TriggerVO;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.quartz.*;


/**
 * Created by IntelliJ IDEA.
 * User: SongJiao
 * Date: 2009-12-23
 * Time: 22:18:35
 * To change this template use File | Settings | File Templates.
 */
public class SchedulerManagerImp implements SchedulerManager {

    private QuartzDao quartzDao;

    public QuartzDao getQuartzDao() {
        return quartzDao;
    }

    public void setQuartzDao(QuartzDao quartzDao) {
        this.quartzDao = quartzDao;
    }

    private Scheduler scheduler;


    public Scheduler getScheduler() {
        return scheduler;
    }

    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }


    private static final Logger logger = Logger.getLogger(SchedulerManagerImp.class);


    @Override
    public void schedule(String cronExpression, JobDetail jobDetail) {
        schedule("", cronExpression, jobDetail);
    }

    @Override
    public void schedule(String name, String cronExpression, JobDetail jobDetail) {
        schedule(name, cronExpression, Scheduler.DEFAULT_GROUP, jobDetail);
    }

    @Override
    public void schedule(String name, String cronExpression, String group, JobDetail jobDetail) {
        try {
            schedule(name, new CronExpression(cronExpression), group, jobDetail);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void schedule(CronExpression cronExpression, JobDetail jobDetail) {
        schedule(null, cronExpression, jobDetail);
    }

    @Override
    public void schedule(String name, CronExpression cronExpression, JobDetail jobDetail) {
        schedule(name, cronExpression, Scheduler.DEFAULT_GROUP, jobDetail);
    }

    @Override
    public void schedule(String name, CronExpression cronExpression, String group, JobDetail jobDetail) {
        try {
            scheduler.addJob(jobDetail, true);

            CronTrigger cronTrigger = new CronTrigger(name, group, jobDetail.getName(),
                    Scheduler.DEFAULT_GROUP);
            cronTrigger.setCronExpression(cronExpression);
            scheduler.scheduleJob(cronTrigger);
            scheduler.rescheduleJob(cronTrigger.getName(), cronTrigger.getGroup(), cronTrigger);

        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    public void schedule(JobDetail jobDetail, CronTrigger cronTrigger) {
        try {
            scheduler.addJob(jobDetail, true);
            scheduler.scheduleJob(cronTrigger);
            scheduler.rescheduleJob(cronTrigger.getName(), cronTrigger.getGroup(), cronTrigger);
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<JobDetail> getAllJobDetails() {
        List<JobDetail> jobDetails = new ArrayList<JobDetail>();
        try {


            String[] groupNames = scheduler.getJobGroupNames();
            for (String groupName : groupNames) {
                String[] jobNames = scheduler.getJobNames(groupName);
                for (String jobName : jobNames) {
                    jobDetails.add(scheduler.getJobDetail(jobName, groupName));
                }
            }

        } catch (SchedulerException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return jobDetails;
    }


    /**
     * @param jobName
     * @param groupName
     * @return
     * @throws SchedulerException
     */
    @Override
    public Trigger[] getTrigersOfJob(String jobName, String groupName) throws SchedulerException {
        return scheduler.getTriggersOfJob(jobName, groupName);
    }

    /**
     * 删除Job
     *
     * @param jobName
     * @param jobGroup
     */
    @Override
    public void deleteJobDetail(String jobName, String jobGroup) {
        try {
            scheduler.deleteJob(jobName, jobGroup);
            quartzDao.unInstallJob(jobName, jobGroup);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据jobDetail的名字查找该jobDetail有哪些Trigger在调用
     *
     * @param jobName
     * @param groupName
     * @return
     */
    @Override
    public List<TriggerVO> getTriggersByJobName(String jobName, String groupName) {

        List<TriggerVO> triggers = new ArrayList<TriggerVO>();
        try {
            Trigger[] trgs = scheduler.getTriggersOfJob(jobName, groupName);
            for (Trigger trigger : trgs) {
                TriggerVO vo = new TriggerVO();
                vo.setTriggerGroup(trigger.getGroup());
                vo.setDescription(trigger.getDescription());
                vo.setEndTime(trigger.getEndTime());
                vo.setJobGroup(trigger.getJobGroup());
                vo.setJobData(trigger.getJobDataMap());
                vo.setJobName(trigger.getJobName());
                vo.setName(trigger.getName());
                vo.setNextFireTime(trigger.getNextFireTime());
                vo.setStartTime(trigger.getStartTime());
                vo.setPrevFireTime(trigger.getPreviousFireTime());
                vo.setPriority(trigger.getPriority());
                vo.setMisfireInstruction(trigger.getMisfireInstruction());
                vo.setState(this.getTriggerState(trigger.getName(), trigger.getGroup()));
                vo.setLinkedTriggerListeners(trigger.getTriggerListenerNames());
                triggers.add(vo);
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return triggers;
    }

    /**
     * 根据jobName和groupName查找JobDetail
     *
     * @param jobName
     * @param groupName
     * @return
     */
    @Override
    public JobDetail getJobDetailByNameAndGroup(String jobName, String groupName) {
        JobDetail jobDetail = null;
        try {
            jobDetail = scheduler.getJobDetail(jobName, groupName);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return jobDetail;
    }

    /**
     * 给Job添加CronTrigger
     *
     * @param cronTrigger
     */
    @Override
    public void schedule(CronTrigger cronTrigger) {
        try {
            scheduler.scheduleJob(cronTrigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据triggerName 和groupName 判断Trigger的状态
     *
     * @param triggerName
     * @param groupName
     * @return
     */
    @Override
    public String getTriggerState(String triggerName, String groupName) {
        String rs = "UNKNOWN";
        try {
            int state = scheduler.getTriggerState(triggerName, groupName);
            switch (state) {
                case Trigger.STATE_BLOCKED:
                    rs = "STATE_BLOCKED";
                    break;
                case Trigger.STATE_COMPLETE:
                    rs = "STATE_COMPLETE";
                    break;
                case Trigger.STATE_ERROR:
                    rs = "STATE_ERROR";
                    break;
                case Trigger.STATE_NONE:
                    rs = "STATE_NONE";
                    break;
                case Trigger.STATE_NORMAL:
                    rs = "STATE_NORMAL";
                    break;
                case Trigger.STATE_PAUSED:
                    rs = "STATE_PAUSED";
                    break;
                default:
                    break;

            }

        } catch (SchedulerException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


        return rs;
    }

    /**
     * 根据安装目录查找jobDetail
     *
     * @param app_path
     * @return
     */
    @Override
    public List<JobDetail> getInstalledJobDetails(String app_path) {
        List<JobDetail> jobDetails = new ArrayList<JobDetail>();
        List<Map<String, String>> installedJobs = quartzDao.getInstalledJobs(app_path);
        for (Map<String, String> row : installedJobs) {
            String job_name = row.get("JOB_NAME");
            String job_group = row.get("JOB_GROUP");
            JobDetail jdetail = this.getJobDetailByNameAndGroup(job_name, job_group);
            if (jdetail != null) {
                jobDetails.add(jdetail);
            }
        }
        return jobDetails;
    }

    @Override
    public void schedule(Date startTime, JobDetail jobDetail) {
        schedule(startTime, Scheduler.DEFAULT_GROUP, jobDetail);
    }

    @Override
    public void schedule(Date startTime, String group, JobDetail jobDetail) {
        schedule(startTime, null, group, jobDetail);
    }

    @Override
    public void schedule(String name, Date startTime, JobDetail jobDetail) {
        schedule(name, startTime, Scheduler.DEFAULT_GROUP, jobDetail);
    }

    @Override
    public void schedule(String name, Date startTime, String group, JobDetail jobDetail) {
        schedule(name, startTime, null, group, jobDetail);
    }

    @Override
    public void schedule(Date startTime, Date endTime, JobDetail jobDetail) {
        schedule(startTime, endTime, Scheduler.DEFAULT_GROUP, jobDetail);
    }

    @Override
    public void schedule(Date startTime, Date endTime, String group, JobDetail jobDetail) {
        schedule(startTime, endTime, 0, group, jobDetail);
    }

    @Override
    public void schedule(String name, Date startTime, Date endTime, JobDetail jobDetail) {
        schedule(name, startTime, endTime, Scheduler.DEFAULT_GROUP, jobDetail);
    }

    @Override
    public void schedule(String name, Date startTime, Date endTime, String group, JobDetail jobDetail) {
        schedule(name, startTime, endTime, 0, group, jobDetail);
    }

    @Override
    public void schedule(Date startTime, Date endTime, int repeatCount, JobDetail jobDetail) {
        schedule(startTime, endTime, 0, Scheduler.DEFAULT_GROUP, jobDetail);
    }

    @Override
    public void schedule(Date startTime, Date endTime, int repeatCount, String group, JobDetail jobDetail) {
        schedule(null, startTime, endTime, 0, group, jobDetail);
    }

    @Override
    public void schedule(String name, Date startTime, Date endTime, int repeatCount, JobDetail jobDetail) {
        schedule(name, startTime, endTime, 0, Scheduler.DEFAULT_GROUP, jobDetail);
    }

    @Override
    public void schedule(String name, Date startTime, Date endTime, int repeatCount, String group, JobDetail jobDetail) {
        schedule(name, startTime, endTime, 0, 1L, group, jobDetail);
    }

    @Override
    public void schedule(Date startTime, Date endTime, int repeatCount, long repeatInterval, JobDetail jobDetail) {
        schedule(startTime, endTime, repeatCount, repeatInterval, Scheduler.DEFAULT_GROUP, jobDetail);
    }

    @Override
    public void schedule(Date startTime, Date endTime, int repeatCount, long repeatInterval, String group, JobDetail jobDetail) {
        schedule(null, startTime, endTime, repeatCount, repeatInterval, group, jobDetail);
    }

    @Override
    public void schedule(String name, Date startTime, Date endTime, int repeatCount, long repeatInterval, JobDetail jobDetail) {
        this.schedule(name, startTime, endTime, repeatCount, repeatInterval, Scheduler.DEFAULT_GROUP, jobDetail);
    }

    @Override
    public void schedule(String name, Date startTime, Date endTime, int repeatCount, long repeatInterval, String group, JobDetail jobDetail) {
        if (name == null || name.trim().equals("")) {
            name = UUID.randomUUID().toString();
        } else {
            //在名称后添加UUID，保证名称的唯一性
            name += "&" + UUID.randomUUID().toString();
        }

        try {
            scheduler.addJob(jobDetail, true);

            SimpleTrigger SimpleTrigger = new SimpleTrigger(name, group, jobDetail.getName(),
                    Scheduler.DEFAULT_GROUP, startTime, endTime, repeatCount, repeatInterval);
            scheduler.scheduleJob(SimpleTrigger);
            scheduler.rescheduleJob(SimpleTrigger.getName(), SimpleTrigger.getGroup(), SimpleTrigger);
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void schedule(Map<String, String> map, JobDetail jobDetail) {

        String temp = null;
        //实例化SimpleTrigger
        SimpleTrigger SimpleTrigger = new SimpleTrigger();

        //这些值的设置也可以从外面传入，这里采用默放值
        SimpleTrigger.setJobName(jobDetail.getName());
        SimpleTrigger.setJobGroup(Scheduler.DEFAULT_GROUP);
        SimpleTrigger.setRepeatInterval(1000L);

        //设置名称
        temp = map.get(Constant.TRIGGERNAME);
        if (StringUtils.isEmpty(StringUtils.trim(temp))) {
            temp = UUID.randomUUID().toString();
        } else {
            //在名称后添加UUID，保证名称的唯一性
            temp += "&" + UUID.randomUUID().toString();
        }
        SimpleTrigger.setName(temp);

        //设置Trigger分组
        temp = map.get(Constant.TRIGGERGROUP);
        if (StringUtils.isEmpty(temp)) {
            temp = Scheduler.DEFAULT_GROUP;
        }
        SimpleTrigger.setGroup(temp);

        //设置开始时间
        temp = map.get(Constant.STARTTIME);
        if (StringUtils.isNotEmpty(temp)) {
            SimpleTrigger.setStartTime(this.parseDate(temp));
        }

        //设置结束时间
        temp = map.get(Constant.ENDTIME);
        if (StringUtils.isNotEmpty(temp)) {
            SimpleTrigger.setEndTime(this.parseDate(temp));
        }

        //设置执行次数
        temp = map.get(Constant.REPEATCOUNT);
        if (StringUtils.isNotEmpty(temp) && NumberUtils.toInt(temp) > 0) {
            SimpleTrigger.setRepeatCount(NumberUtils.toInt(temp));
        }

        //设置执行时间间隔
        temp = map.get(Constant.REPEATINTERVEL);
        if (StringUtils.isNotEmpty(temp) && NumberUtils.toLong(temp) > 0) {
            SimpleTrigger.setRepeatInterval(NumberUtils.toLong(temp) * 1000);
        }

        try {
            scheduler.addJob(jobDetail, true);

            scheduler.scheduleJob(SimpleTrigger);
            scheduler.rescheduleJob(SimpleTrigger.getName(), SimpleTrigger.getGroup(), SimpleTrigger);
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void scheduleSimpleJob(String triggerName, String appPath, String mainClass, String args, String cronExpression, String group) throws ParseException {
        String uuname = triggerName + "_" + UUID.randomUUID().toString();

        JobDetail jobDetail = new JobDetail(uuname, group, SimpleJob.class);
        JobDataMap datamap = new JobDataMap();
        datamap.put("appPath", appPath);
        datamap.put("mainClass", mainClass);
        datamap.put("args", args);
        jobDetail.setJobDataMap(datamap);


        CronTrigger cronTrigger = new CronTrigger(uuname, group, jobDetail.getName(),
                group);
        cronTrigger.setCronExpression(cronExpression);
        this.schedule(jobDetail, cronTrigger);
    }

    /**
     * 安装一个JobDetail
     *
     * @param appPath
     * @param mainClass
     * @param main_args
     * @param jvm_args
     * @param group
     */
    @Override
    public void addJobDetail(String appPath, String mainClass, String main_args, String jvm_args, String group, String name, String desc) {
        JobDetail jobDetail = new JobDetail(name, group, SimpleJob.class);
        jobDetail.setDescription(desc);
        JobDataMap datamap = new JobDataMap();
        datamap.put("appPath", appPath);
        datamap.put("mainClass", mainClass);
        datamap.put("main_args", main_args);
        datamap.put("jvm_args", jvm_args);
        jobDetail.setJobDataMap(datamap);
        try {
            scheduler.addJob(jobDetail, true);
            quartzDao.installJob(name, group, appPath);
        } catch (SchedulerException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Override
    public List<Trigger> getQrtzTriggers() {
        List<Trigger> triggers = new ArrayList<Trigger>();
        try {
            String[] triggerGroupNames = scheduler.getTriggerGroupNames();
            for (String triggerGroupName : triggerGroupNames) {
                String[] triggerNames = scheduler.getTriggerNames(triggerGroupName);
                for (String triggerName : triggerNames) {
                    triggers.add(scheduler.getTrigger(triggerName, triggerGroupName));
                }
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return triggers;
    }

    @Override
    public boolean pauseTrigger(String triggerName, String group) {
        boolean flag = false;
        try {
            scheduler.pauseTrigger(triggerName, group);//停止触发器
            flag = true;
        } catch (SchedulerException e) {
            flag = false;
            throw new RuntimeException(e);
        }
        return flag;
    }

    @Override
    public boolean resumeTrigger(String triggerName, String group) {
        boolean flag = false;
        try {
            scheduler.resumeTrigger(triggerName, group);//重启触发器
            flag = true;
        } catch (SchedulerException e) {
            flag = false;
            throw new RuntimeException(e);
        }
        return flag;
    }

    @Override
    public boolean removeTrigger(String triggerName, String group) {
        try {
            scheduler.pauseTrigger(triggerName, group);//停止触发器
            return scheduler.unscheduleJob(triggerName, group);//移除触发器
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }


    private Date parseDate(String time) {
        try {
            return DateUtils.parseDate(time, new String[]{"yyyy-MM-dd HH:mm"});
        } catch (ParseException e) {
            logger.error("日期格式错误{}，正确格式为：yyyy-MM-dd HH:mm" + time);
            throw new RuntimeException(e);
        }
    }


}
