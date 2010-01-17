package com.sohu.quartz.controller;

import com.sohu.Constant;
import com.sohu.quartz.manager.SchedulerManager;
import com.sohu.quartz.pojo.FileBean;
import com.sohu.quartz.pojo.TriggerVO;
import com.sohu.quartz.utils.FileUploadHelper;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.quartz.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.WebUtils;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: SongJiao
 * Date: 2009-12-22
 * Time: 23:51:36
 * To change this template use File | Settings | File Templates.
 */

@Controller
public class JobProcessController {
    @Resource
    private FileUploadHelper fileUploadHelper;

    public FileUploadHelper getFileUploadHelper() {
        return fileUploadHelper;
    }

    public void setFileUploadHelper(FileUploadHelper fileUploadHelper) {
        this.fileUploadHelper = fileUploadHelper;
    }


    public SchedulerManager getSchedulerManager() {
        return schedulerManager;
    }

    public void setSchedulerManager(SchedulerManager schedulerManager) {
        this.schedulerManager = schedulerManager;
    }

    @Resource
    private SchedulerManager schedulerManager;


    /**
     * 添加Simple Trigger
     *
     * @param request
     * @param response
     */
    @RequestMapping("/addSimpleTrigger.do")
    private void addSimpleTrigger(HttpServletRequest request, HttpServletResponse response) throws IOException {

        // 获取界面以p_参数
        Map<String, String> filterMap = WebUtils.getParametersStartingWith(request, "p_");
        if (StringUtils.isEmpty(filterMap.get(Constant.STARTTIME))) {
            response.getWriter().println(1);
        }
        JobDetail jobDetail = new JobDetail();

        // 添加任务调试
        schedulerManager.schedule(filterMap, jobDetail);

        // response.setContentType("text/xml;charset=utf-8");
        response.getWriter().println(0);

    }

    /**
     * 根据Cron表达式添加Cron Trigger，
     *
     * @param request
     * @param response
     */
    @RequestMapping("/addCronTriggerByExpression.do")
    private void addCronTriggerByExpression(HttpServletRequest request, HttpServletResponse response) throws IOException {

        // 获取界面以参数
        String triggerName = request.getParameter("triggerName");
        String cronExpression = request.getParameter("cronExpression");
        if (StringUtils.isEmpty(triggerName) || StringUtils.isEmpty(cronExpression)) {
            response.getWriter().println(1);
        }
        JobDetail jobDetail = new JobDetail();

        // 添加任务调试
        schedulerManager.schedule(triggerName, cronExpression, jobDetail);

        // response.setContentType("text/xml;charset=utf-8");
        response.getWriter().println(0);

    }

    /**
     * 根据添加Cron Trigger，
     *
     * @param request
     * @param response
     */
    @RequestMapping("/addCronTrigger.do")
    private void addCronTriggerBy(HttpServletRequest request, HttpServletResponse response) throws IOException {

        // 获取界面以参数
        String triggerName = request.getParameter("triggerName");
        String val = request.getParameter("val");
        String selType = request.getParameter("selType");
        if (StringUtils.isEmpty(triggerName) || StringUtils.isEmpty(val) || NumberUtils.toLong(val) < 0 || NumberUtils.toLong(val) > 59) {
            response.getWriter().println(1);
        }

        String expression = null;
        if (StringUtils.equals(selType, "second")) {
            // 每多秒执行一次
            expression = "0/" + val + " * * ? * * *";
        } else if (StringUtils.equals(selType, "minute")) {
            // 每多少分执行一次
            expression = "0 0/" + val + " * ? * * *";
        }

        JobDetail jobDetail = new JobDetail();
        // 添加任务调试
        schedulerManager.schedule(triggerName, expression, jobDetail);

        // response.setContentType("text/xml;charset=utf-8");
        response.getWriter().println(0);

    }


    @RequestMapping("/addSimpleJob.do")
    private void addSimpleJob(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException {
        // 获取界面以参数
        String triggerName = request.getParameter("triggerName");
        String appPath = request.getParameter("appPath");
        String mainClass = request.getParameter("mainClass");
        String args = request.getParameter("args");
        String group = request.getParameter("group");
        String cronExpression = request.getParameter("cronExpression");

        // 添加任务调试
        try {
            schedulerManager.scheduleSimpleJob(triggerName, appPath, mainClass, args, cronExpression, group);
            response.getWriter().println(0);
        } catch (Exception e) {
            response.getWriter().println(-1);
        }


    }

    /**
     * 取得所有Trigger
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @RequestMapping("/getQrtzTriggers.do")
    private void getQrtzTriggers(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Trigger> results = this.schedulerManager.getQrtzTriggers();
        request.setAttribute("list", results);
        request.getRequestDispatcher("/list.jsp").forward(request, response);
    }

    /**
     * 根据名称和组别暂停Tigger
     *
     * @param response
     * @throws IOException
     */
    @RequestMapping("/pauseTrigger.do")
    private void pauseTrigger(@RequestParam("triggerName") String triggerName,
                              @RequestParam("group") String group,
                              HttpServletResponse response) throws IOException {
        if (schedulerManager.pauseTrigger(triggerName, group)) {
            response.getWriter().println(0);
        } else {
            response.getWriter().println(1);
        }

    }


    /**
     * 根据名称和组别唤醒Tigger
     *
     * @param response
     * @throws IOException
     */
    @RequestMapping("/resumeTrigger.do")
    private void resumeTrigger(@RequestParam("triggerName") String triggerName, @RequestParam("group") String group, HttpServletResponse response) throws IOException {
        if (schedulerManager.resumeTrigger(triggerName, group)) {
            response.getWriter().println(0);
        } else {
            response.getWriter().println(1);
        }
    }


    /**
     * 根据名称和组别删除Tigger
     *
     * @param response
     * @throws IOException
     */
    @RequestMapping("/removeTrigger.do")
    private void removeTrigdger(@RequestParam("triggerName") String triggerName,
                                @RequestParam("group") String group,
                                HttpServletResponse response) throws IOException {
        boolean rs = schedulerManager.removeTrigger(triggerName, group);
        if (rs) {
            response.getWriter().println(0);
        } else {
            response.getWriter().println(1);
        }
    }


    @RequestMapping("/listAllTrigger.do")
    public String listTriggers(ModelMap model) {
        List<Trigger> triggers = schedulerManager.getQrtzTriggers();
        model.addAttribute("triggers", triggers);
        return "listTriggers";
    }

    @RequestMapping("/index.do")
    public String index() {
        return "index";
    }

    @RequestMapping("/addJob.do")
    public String addJob() {
        schedulerManager.getAllJobDetails();
        return "listJobs";
    }


    /**
     * 列出所有的JobDetail
     *
     * @param model
     * @return
     */
    @RequestMapping("/listJobDetails.do")
    public String listJobDetails(ModelMap model) {

        model.addAttribute("jobDetails", schedulerManager.getAllJobDetails());

        return "listJobDetails";
    }


    /**
     * 显示所选的Job有哪些Trigger在调度它
     *
     * @param model
     * @param jobName
     * @param groupName
     * @return
     * @throws Exception
     */
    @RequestMapping("/triggerJob.do")
    public String triggerJob(ModelMap model, @RequestParam("jobName") String jobName, @RequestParam("groupName") String groupName) throws Exception {
        List<TriggerVO> triggers = schedulerManager.getTriggersByJobName(jobName, groupName);
        JobDetail jobDetail = schedulerManager.getJobDetailByNameAndGroup(jobName, groupName);
        if (jobDetail == null) {
            throw new Exception("Job dose not exists in group:" + groupName + " named:" + jobName);
        }
        model.addAttribute("triggers", triggers);
        model.addAttribute("jobDetail", jobDetail);
        return "triggerJob";
    }


    /**
     * 给Job添加Trigger
     *
     * @param model
     * @param jobName
     * @param groupName
     * @param triggerName
     * @param triggerGroup
     * @param cronExpression
     * @return
     * @throws Exception
     */
    @RequestMapping("/addTriggerToJob.do")
    public String addTriggerToJob(ModelMap model,
                                  @RequestParam("jobName") String jobName,
                                  @RequestParam("groupName") String groupName,
                                  @RequestParam("triggerName") String triggerName,
                                  @RequestParam("triggerGroup") String triggerGroup,
                                  @RequestParam("cronExpression") String cronExpression,
                                  @RequestParam("desc") String desc) throws Exception {
        if (triggerName == null || triggerName.trim().equals("")) {
            throw new Exception("TriggerName can not be null");
        }

        CronTrigger cronTrigger = new CronTrigger();
        cronTrigger.setCronExpression(cronExpression);
        cronTrigger.setGroup(triggerGroup);
        cronTrigger.setName(triggerName);
        cronTrigger.setJobGroup(groupName);
        cronTrigger.setJobName(jobName);
        cronTrigger.setDescription(desc);

        JobDetail jobDetail = schedulerManager.getJobDetailByNameAndGroup(jobName, groupName);

        schedulerManager.schedule(cronTrigger);
        List<TriggerVO> triggers = schedulerManager.getTriggersByJobName(jobName, groupName);
        model.addAttribute("triggers", triggers);
        model.addAttribute("jobDetail", jobDetail);
        return "triggerJob";
    }

    @RequestMapping("/showInstallJob.do")
    public String showInstallJob(ModelMap model, @RequestParam("app_path") String app_path) {
        List<JobDetail> jobDetails = schedulerManager.getInstalledJobDetails(app_path);
        model.addAttribute("jobDetails", jobDetails);
        model.addAttribute("app_path", app_path);
        return "listJobDetails";
    }

    @RequestMapping("/installJob.do")
    public String installJob(ModelMap model,
                             @RequestParam("app_path") String app_path,
                             @RequestParam("jobName") String jobName,
                             @RequestParam("jobGroup") String jobGroup,
                             @RequestParam("mainClass") String mainClass,
                             @RequestParam("main_args") String main_args,
                             @RequestParam("jvm_args") String jvm_args,
                             @RequestParam("desc") String desc) {

        schedulerManager.addJobDetail(app_path, mainClass, main_args, jvm_args, jobGroup, jobName, desc);
        model.addAttribute("jobDetails", schedulerManager.getAllJobDetails());
        return "listJobDetails";
    }


    /**
     * 卸载Job
     * 先找到安装目录下的所有Job然后找到所有job的Trigger,把所有的Trigger和JobDetail依次删除，最后把目录删除
     *
     * @return
     */
    @RequestMapping("/unInstallJob.do")
    public String unInstallJob(Model model, @RequestParam("app_path") String app_path) {
        List<JobDetail> jobDetails = schedulerManager.getInstalledJobDetails(app_path);
        for (JobDetail jobDetail : jobDetails) {
            try {
                Trigger[] ts = schedulerManager.getTrigersOfJob(jobDetail.getName(), jobDetail.getGroup());
                for (Trigger t : ts) {
                    schedulerManager.removeTrigger(t.getName(), t.getGroup());
                }
            } catch (SchedulerException e) {
                e.printStackTrace();
            }
            schedulerManager.deleteJobDetail(jobDetail.getName(), jobDetail.getGroup());
        }
        try {
            this.fileUploadHelper.rmDir(app_path);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


        List<File> dirs = this.fileUploadHelper.getSubDirs();
        List<FileBean> filebeans = new ArrayList<FileBean>();
        for (File f : dirs) {
            FileBean fb = new FileBean();
            fb.setFilename(f.getAbsolutePath());
            fb.setLastmodified(new Date(f.lastModified()));
            filebeans.add(fb);
        }


        model.addAttribute("dirs", filebeans);
        return "upload";
    }
}
