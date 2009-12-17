package org.quartz.ui.web.action.schedule;

import java.util.ArrayList;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;

public class ListJobs extends ScheduleBase {

	String jobName="";
	String jobGroup=""; 
	String triggerGroup = "";
		
	private ArrayList jobList = new ArrayList();
	
	/**
	 * Returns the jobs.
	 * @return ArrayList
	 */
	public java.util.List getJobs() {
		return  jobList;
	}
	
	public void setJobs(java.util.List jobs) {
			jobList=(ArrayList) jobs;
		}
	
	
				
	public String execute() throws Exception  {

	  	   if (hasFieldErrors()) {
			   LOG.info("this thing has errors");
			return ERROR;
			}
			
			Scheduler scheduler = ScheduleBase.getCurrentScheduler();
			this.jobList = new ArrayList();		

				try {

					String[] jobGroups = scheduler.getJobGroupNames();

					ArrayList addedJobs = new ArrayList(jobGroups.length);
					//
					// have had some problems multiple jobs showing
					for (int i = 0; i < jobGroups.length; i++) {
						String groupName = jobGroups[i];
						String[] jobs = scheduler.getJobNames(groupName);
						for (int j = 0; j < jobs.length; j++) {
							String job = jobs[j];
							JobDetail jobDetail =scheduler.getJobDetail(job, groupName);
							String key = job + groupName;
							if (!addedJobs.contains(key)) {
							/*	JobDetailForm jForm = new JobDetailForm();
								jForm.setDescription(jobDetail.getDescription());
								jForm.setGroupName(jobDetail.getGroup());
								jForm.setJobClass(jobDetail.getJobClass().getName());
								jForm.setName (jobDetail.getName());
								jForm.setRecoveryRequesting(jobDetail.requestsRecovery());
								jForm.setVolatile(jobDetail.isVolatile()); */
								
								this.jobList.add(jobDetail);
								addedJobs.add(key);
							}
						}
					}
				} catch (SchedulerException e) {
					throw new Exception("When reading the jobs", e);
				}
			
		return SUCCESS;

		}



}
