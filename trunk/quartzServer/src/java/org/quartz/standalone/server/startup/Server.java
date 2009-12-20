package org.quartz.standalone.server.startup;

import java.io.File;

import org.apache.commons.daemon.Daemon;
import org.apache.commons.daemon.DaemonContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

public class Server implements Daemon {
	Log log = LogFactory.getLog(Server.class);

	private boolean init_flag = false;// 是否已经初始化

	private Scheduler sched = null;

	private String pidfile;// pid 文件

	/**
	 * 销毁后台程序
	 */
	public void destroy() {
		if (sched != null) {
			try {
				sched.shutdown(true);
			} catch (SchedulerException e) {
				e.printStackTrace();
				log.error(e);
			}
		} else {
			log.error("The Scheduler is null!");
			System.exit(-1);
		}
		log.info("------- Shutdown Complete -----------");
	}

	/**
	 * 初始化后台程序
	 */
	public void init(DaemonContext arg0) throws Exception {

		PidFileHelper t = new PidFileHelper(this);
		t.createPidFile(this.pidfile);
		t.start();// 监控pid文件,当文件里写入了stop时停止服务
		
		
		StdSchedulerFactory sf = new StdSchedulerFactory();
		String propertiefile = System.getProperty("user.dir") + File.separator
				+ "conf" + File.separator + "quartz.properties";
		sf.initialize(propertiefile);
		sched = sf.getScheduler();
		log.info("------- Initialization Complete -----------");
		init_flag = true;
	}

	/**
	 * 开启后台程序
	 */
	public void start(String pid_file_path) throws Exception {
		if (init_flag == false) {
			this.pidfile = pid_file_path;
			init(null);
		}
		if (sched != null) {
			sched.start();
		} else {
			log.error("The Scheduler is null!");
			System.exit(-1);
		}
	}

	/**
	 * 销毁后台程序
	 */
	public void stop() throws Exception {
		this.destroy();
	}

	/**
	 * 重启后台程序
	 * 
	 * @throws Exception
	 */
	public void restart() throws Exception {
		sched.shutdown(true);//等待所有任务执行完后关闭
		Thread.sleep(1000l);//等待1s
		init(null);//重新初始化
		sched.start();
	}

	/**
	 * 将后台定时程序全部挂起
	 * 
	 * @throws Exception
	 */
	public void pause() throws Exception {
		sched.pauseAll();
	}

	/**
	 * 将后台定时程序全部唤醒
	 * 
	 * @throws Exception
	 */
	public void resume() throws Exception {
		sched.resumeAll();
	}

	public void start() throws Exception {
		// TODO Auto-generated method stub
		
	}


}
