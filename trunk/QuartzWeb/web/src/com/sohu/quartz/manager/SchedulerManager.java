package com.sohu.quartz.manager;

import com.sohu.quartz.pojo.TriggerVO;
import org.quartz.*;

import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: SongJiao
 * Date: 2009-12-23
 * Time: 0:15:41
 * To change this template use File | Settings | File Templates.
 */
public interface SchedulerManager {

    /**
     * 根据 Quartz Cron Expression 调试任务
     *
     * @param cronExpression Quartz Cron 表达式，如 "0/10 * * ? * * *"等
     */
    void schedule(String cronExpression, JobDetail jobDetail);

    /**
     * 根据 Quartz Cron Expression 调试任务
     *
     * @param name           Quartz CronTrigger名称
     * @param cronExpression Quartz Cron 表达式，如 "0/10 * * ? * * *"等
     */
    void schedule(String name, String cronExpression, JobDetail jobDetail);

    /**
     * 根据 Quartz Cron Expression 调试任务
     *
     * @param name           Quartz CronTrigger名称
     * @param cronExpression Quartz Cron 表达式，如 "0/10 * * ? * * *"等
     * @param group          Quartz CronExpression Group
     */
    void schedule(String name, String cronExpression, String group, JobDetail jobDetail);

    /**
     * 根据 Quartz Cron Expression 调试任务
     *
     * @param cronExpression Quartz CronExpression
     */
    void schedule(CronExpression cronExpression, JobDetail jobDetail);

    /**
     * 根据 Quartz Cron Expression 调试任务
     *
     * @param name           Quartz CronTrigger名称
     * @param cronExpression Quartz CronExpression
     */
    void schedule(String name, CronExpression cronExpression, JobDetail jobDetail);

    /**
     * 根据 Quartz Cron Expression 调试任务
     *
     * @param name           Quartz CronTrigger名称
     * @param cronExpression Quartz CronExpression
     * @param group          Quartz CronExpression Group
     */
    void schedule(String name, CronExpression cronExpression, String group, JobDetail jobDetail);

    /**
     * 在startTime时执行调试一次
     *
     * @param startTime 调度开始时间
     */
    void schedule(Date startTime, JobDetail jobDetail);

    /**
     * 在startTime时执行调试一次
     *
     * @param startTime 调度开始时间
     * @param group     Quartz SimpleTrigger Group
     */
    void schedule(Date startTime, String group, JobDetail jobDetail);

    /**
     * 在startTime时执行调试一次
     *
     * @param name      Quartz SimpleTrigger 名称
     * @param startTime 调度开始时间
     */
    void schedule(String name, Date startTime, JobDetail jobDetail);

    /**
     * 在startTime时执行调试一次
     *
     * @param name      Quartz SimpleTrigger 名称
     * @param startTime 调度开始时间
     * @param group     Quartz SimpleTrigger Group
     */
    void schedule(String name, Date startTime, String group, JobDetail jobDetail);

    /**
     * 在startTime时执行调试，endTime结束执行调度
     *
     * @param startTime 调度开始时间
     * @param endTime   调度结束时间
     */
    void schedule(Date startTime, Date endTime, JobDetail jobDetail);

    /**
     * 在startTime时执行调试，endTime结束执行调度
     *
     * @param startTime 调度开始时间
     * @param endTime   调度结束时间
     * @param group     Quartz SimpleTrigger Group
     */
    void schedule(Date startTime, Date endTime, String group, JobDetail jobDetail);

    /**
     * 在startTime时执行调试，endTime结束执行调度
     *
     * @param name      Quartz SimpleTrigger 名称
     * @param startTime 调度开始时间
     * @param endTime   调度结束时间
     */
    void schedule(String name, Date startTime, Date endTime, JobDetail jobDetail);

    /**
     * 在startTime时执行调试，endTime结束执行调度
     *
     * @param name      Quartz SimpleTrigger 名称
     * @param startTime 调度开始时间
     * @param endTime   调度结束时间
     * @param group     Quartz SimpleTrigger Group
     */
    void schedule(String name, Date startTime, Date endTime, String group, JobDetail jobDetail);

    /**
     * 在startTime时执行调试，endTime结束执行调度，重复执行repeatCount次
     *
     * @param startTime   调度开始时间
     * @param endTime     调度结束时间
     * @param repeatCount 重复执行次数
     */
    void schedule(Date startTime, Date endTime, int repeatCount, JobDetail jobDetail);

    /**
     * 在startTime时执行调试，endTime结束执行调度，重复执行repeatCount次
     *
     * @param startTime   调度开始时间
     * @param endTime     调度结束时间
     * @param repeatCount 重复执行次数
     * @param group       Quartz SimpleTrigger Group
     */
    void schedule(Date startTime, Date endTime, int repeatCount, String group, JobDetail jobDetail);

    /**
     * 在startTime时执行调试，endTime结束执行调度，重复执行repeatCount次
     *
     * @param name        Quartz SimpleTrigger 名称
     * @param startTime   调度开始时间
     * @param endTime     调度结束时间
     * @param repeatCount 重复执行次数
     */
    void schedule(String name, Date startTime, Date endTime, int repeatCount, JobDetail jobDetail);

    /**
     * 在startTime时执行调试，endTime结束执行调度，重复执行repeatCount次
     *
     * @param name        Quartz SimpleTrigger 名称
     * @param startTime   调度开始时间
     * @param endTime     调度结束时间
     * @param repeatCount 重复执行次数
     * @param group       Quartz SimpleTrigger Group
     */
    void schedule(String name, Date startTime, Date endTime, int repeatCount, String group, JobDetail jobDetail);

    /**
     * 在startTime时执行调试，endTime结束执行调度，重复执行repeatCount次，每隔repeatInterval秒执行一次
     *
     * @param startTime      调度开始时间
     * @param endTime        调度结束时间
     * @param repeatCount    重复执行次数
     * @param repeatInterval 执行时间隔间
     */
    void schedule(Date startTime, Date endTime, int repeatCount, long repeatInterval, JobDetail jobDetail);

    /**
     * 在startTime时执行调试，endTime结束执行调度，重复执行repeatCount次，每隔repeatInterval秒执行一次
     *
     * @param startTime      调度开始时间
     * @param endTime        调度结束时间
     * @param repeatCount    重复执行次数
     * @param repeatInterval 执行时间隔间
     * @param group          Quartz SimpleTrigger Group
     */
    void schedule(Date startTime, Date endTime, int repeatCount, long repeatInterval, String group, JobDetail jobDetail);

    /**
     * 在startTime时执行调试，endTime结束执行调度，重复执行repeatCount次，每隔repeatInterval秒执行一次
     *
     * @param name           Quartz SimpleTrigger 名称
     * @param startTime      调度开始时间
     * @param endTime        调度结束时间
     * @param repeatCount    重复执行次数
     * @param repeatInterval 执行时间隔间
     */
    void schedule(String name, Date startTime, Date endTime, int repeatCount, long repeatInterval, JobDetail jobDetail);

    /**
     * 在startTime时执行调试，endTime结束执行调度，重复执行repeatCount次，每隔repeatInterval秒执行一次
     *
     * @param name           Quartz SimpleTrigger 名称
     * @param startTime      调度开始时间
     * @param endTime        调度结束时间
     * @param repeatCount    重复执行次数
     * @param repeatInterval 执行时间隔间
     * @param group          Quartz SimpleTrigger Group
     */
    void schedule(String name, Date startTime, Date endTime, int repeatCount, long repeatInterval, String group, JobDetail jobDetail);

    /**
     * Trigger 参数,以com.sundoctor.example.Constant常量为键封装的Map
     *
     * @param map
     */
    void schedule(Map<String, String> map, JobDetail jobDetail);


    /**
     * 调度SimpleJob
     */
    void scheduleSimpleJob(String triggerName, String appPath, String mainClass, String args, String cronExpression, String group) throws ParseException;

    /**
     * 安装一个JobDetail
     *
     * @param appPath
     * @param mainClass
     * @param main_args
     * @param jvm_args
     * @param group
     */
    void addJobDetail(String appPath, String mainClass, String main_args, String jvm_args, String group, String name, String desc);

    /**
     * 取得所有调度Triggers
     *
     * @return
     */
    List<Trigger> getQrtzTriggers();

    /**
     * 根据名称和组别暂停Tigger
     *
     * @param triggerName
     * @param group
     */
    boolean pauseTrigger(String triggerName, String group);

    /**
     * 恢复Trigger
     *
     * @param triggerName
     * @param group
     */
    boolean resumeTrigger(String triggerName, String group);

    /**
     * 删除Trigger
     *
     * @param triggerName
     * @param group
     */
    boolean removeTrigger(String triggerName, String group);


    public void schedule(JobDetail jobDetail, CronTrigger cronTrigger);


    /**
     * @return 取得所有的JobDetail
     */
    public List<JobDetail> getAllJobDetails();

    /**
     * 根据jobDetail的名字和组号查找该jobDetail有哪些Trigger在调用
     *
     * @param jobName   job名
     * @param groupName 组名
     * @return
     */
    public List<TriggerVO> getTriggersByJobName(String jobName, String groupName);

    /**
     * 根据jobName和groupName查找JobDetail
     *
     * @param jobName
     * @param groupName
     * @return
     */
    public JobDetail getJobDetailByNameAndGroup(String jobName, String groupName);


    /**
     * 给Job添加CronTrigger
     *
     * @param cronTrigger
     */
    public void schedule(CronTrigger cronTrigger);

    /**
     * 根据triggerName 和groupName 判断Trigger的状态
     *
     * @param triggerName
     * @param groupName
     * @return
     */
    public String getTriggerState(String triggerName, String groupName);

    /**
     * 根据安装目录查找jobDetail
     *
     * @param app_path
     * @return
     */
    public List<JobDetail> getInstalledJobDetails(String app_path);

    public Trigger[] getTrigersOfJob(String jobName, String groupName) throws SchedulerException;

    /**
     * 删除Job
     *
     * @param jobName
     * @param jobGroup
     */
    public void deleteJobDetail(String jobName, String jobGroup);
}
