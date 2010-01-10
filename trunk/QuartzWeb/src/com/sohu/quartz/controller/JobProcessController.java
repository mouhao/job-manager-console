package com.sohu.quartz.controller;

import com.sohu.Constant;
import com.sohu.quartz.jobs.SimpleJob;
import com.sohu.quartz.manager.SchedulerManager;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.quartz.CronTrigger;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.WebUtils;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by IntelliJ IDEA.
 * User: SongJiao
 * Date: 2009-12-22
 * Time: 23:51:36
 * To change this template use File | Settings | File Templates.
 */

@Controller
public class JobProcessController {

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

        // 添加任务调试
        schedulerManager.schedule(filterMap);

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

        // 添加任务调试
        schedulerManager.schedule(triggerName, cronExpression);

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

        // 添加任务调试
        schedulerManager.schedule(triggerName, expression);

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
        String group=request.getParameter("group");
        String cronExpression = request.getParameter("cronExpression");
        String uuname=triggerName+"_"+UUID.randomUUID().toString();

        JobDetail jobDetail =new JobDetail(uuname,group, SimpleJob.class);
        JobDataMap datamap=new JobDataMap();
        datamap.put("appPath",appPath);
        datamap.put("mainClass",mainClass);
        datamap.put("args",args);
        jobDetail.setJobDataMap(datamap);

        CronTrigger cronTrigger = new CronTrigger(uuname, group, jobDetail.getName(),
                    group);
            cronTrigger.setCronExpression(cronExpression);

        // 添加任务调试
        schedulerManager.schedule(jobDetail, cronTrigger);

        // response.setContentType("text/xml;charset=utf-8");
        response.getWriter().println(0);
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
        List<Map<String, Object>> results = this.schedulerManager.getQrtzTriggers();
        request.setAttribute("list", results);
        request.getRequestDispatcher("/list.jsp").forward(request, response);
    }

    /**
     * 根据名称和组别暂停Tigger
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("/pauseTrigger.do")
    private void pauseTrigger(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // request.setCharacterEncoding("UTF-8");
        String triggerName = URLDecoder.decode(request.getParameter("triggerName"), "utf-8");
        String group = URLDecoder.decode(request.getParameter("group"), "utf-8");

        schedulerManager.pauseTrigger(triggerName, group);
        response.getWriter().println(0);
    }

    /**
     * 根据名称和组别暂停Tigger
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("/resumeTrigger.do")
    private void resumeTrigger(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // request.setCharacterEncoding("UTF-8");
        String triggerName = URLDecoder.decode(request.getParameter("triggerName"), "utf-8");
        String group = URLDecoder.decode(request.getParameter("group"), "utf-8");

        schedulerManager.resumeTrigger(triggerName, group);
        response.getWriter().println(0);
    }

    /**
     * 根据名称和组别暂停Tigger
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("/removeTrigger.do")
    private void removeTrigdger(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // request.setCharacterEncoding("UTF-8");
        String triggerName = URLDecoder.decode(request.getParameter("triggerName"), "utf-8");
        String group = URLDecoder.decode(request.getParameter("group"), "utf-8");

        boolean rs = schedulerManager.removeTrigdger(triggerName, group);
        if (rs) {
            response.getWriter().println(0);
        } else {
            response.getWriter().println(1);
        }
    }


    @RequestMapping("/list.do")
    public String list(ModelMap model) {
        List<Map<String, Object>> list = schedulerManager.getQrtzTriggers();
        model.addAttribute("list", list);
        return "list";
    }

    @RequestMapping("/index.do")
    public String index() {
        return "index";
    }

}
