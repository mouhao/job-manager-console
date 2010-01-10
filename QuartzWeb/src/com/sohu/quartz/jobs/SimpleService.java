package com.sohu.quartz.jobs;

import java.io.Serializable;


import org.apache.log4j.Logger;


public class SimpleService implements Serializable{
	
	private static final long serialVersionUID = 122323233244334343L;
	private static final Logger logger = Logger.getLogger(SimpleService.class);

	
	public void testMethod(String triggerName, String group){
		//这里执行定时调度业务
		logger.info("AAAA:"+triggerName+"=="+group);
		
	}
	
	public void testMethod2( String triggerName,String group){
		//这里执行定时调度业务
		logger.info("BBBB:"+triggerName+"=="+group);
	}


	
	
}
