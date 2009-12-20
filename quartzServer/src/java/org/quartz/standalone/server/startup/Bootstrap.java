package org.quartz.standalone.server.startup;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Bootstrap {
	Log log = LogFactory.getLog(Bootstrap.class);

	/**
	 * daemon object use by the main
	 */
	private static Bootstrap daemon = null;
	/**
	 * Daemon reference.
	 */
	private Object serverDaemon = null;
	PidFileHelper pidFileHelper = new PidFileHelper();

	private static Map<String, CommandType> COMMAND_TYPES = new HashMap<String, CommandType>();// 命令参数
	/**
	 * 初始化命令参数
	 */
	static {
		COMMAND_TYPES.put("start", CommandType.start);
		COMMAND_TYPES.put("stop", CommandType.stop);
		COMMAND_TYPES.put("restart", CommandType.restart);
		COMMAND_TYPES.put("help", CommandType.help);
		COMMAND_TYPES.put("pause", CommandType.pause);
		COMMAND_TYPES.put("resume", CommandType.resume);
	}

	public static void main(String[] args) throws Exception {

		if (daemon == null) {
			daemon = new Bootstrap();
			try {
				daemon.init();
			} catch (Throwable t) {
				t.printStackTrace();
				return;
			}
		}

		if (args.length > 1) {
			if (checkCmdArg(args[1])) {
				CommandType cmdType = COMMAND_TYPES.get(args[1].toLowerCase());
				if (cmdType == CommandType.start) {
					daemon.start(args[0]);
				} else if (cmdType == CommandType.stop) {
					daemon.stop(args[0]);
				} else if (cmdType == CommandType.restart) {
					daemon.restart(args[0]);
				} else if (cmdType == CommandType.pause) {
					daemon.pause(args[0]);
				} else if (cmdType == CommandType.resume) {
					daemon.resume(args[0]);
				} else if (cmdType == CommandType.help) {
					showHelpInfo();
				}

			} else {
				System.out.println("args error!");
				showHelpInfo();
			}
		} else {
			showHelpInfo();
			System.exit(0);
		}

	}

	/**
	 * 检查命令行参数
	 * 
	 * @param arg
	 * @return
	 */
	private static boolean checkCmdArg(String arg) {
		return COMMAND_TYPES.containsKey(arg) ? true : false;
	}

	/**
	 * 打印帮助信息
	 * 
	 */
	private static void showHelpInfo() {
		String helpfile = System.getProperty("user.dir") + File.separator
				+ "conf" + File.separator + "help.info";
		File f = new File(helpfile);
		if (!f.exists()) {
			System.out.println("help.info not exists");
		} else {
			try {
				LineIterator itr = FileUtils.lineIterator(f, "UTF-8");
				while (itr.hasNext()) {
					System.out.println(itr.nextLine());
				}
				itr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Initialize daemon.
	 */
	public void init() throws Exception {
		Class<?> startupClass = Class
				.forName("org.quartz.standalone.server.startup.Server");
		Object startupInstance = startupClass.newInstance();
		serverDaemon = startupInstance;
	}

	/**
	 * start the QuartzServerDemo
	 * 
	 * @param args
	 */
	public void start(String pid_file_path) throws Exception {
		File pidfile = new File(pid_file_path);
		if (pidFileHelper.pidExists(pidfile)) {// 检查进程ID是否存在
			log.info("QuartzServer already started.");
		} else {

			if (serverDaemon == null) {
				init();
			}
			Method method = serverDaemon.getClass().getMethod("start",
					new Class[] { String.class });
			method.invoke(serverDaemon, new Object[] { pid_file_path });
		}
	}

	/**
	 * stop the QuartzServerDemo
	 * 
	 * @param pidfile
	 *            PID文件路径
	 * 
	 * @throws Exception
	 */
	public void stop(String pidfile) throws Exception {
		File pid = new File(pidfile);
		if (!pidFileHelper.pidExists(pid)) {
			log.info("no service started");
		} else {
			this.sendCommandToServer(pid, "stop");
		}
	}
	
	/**
	 * 往PID文件里写命令，QuqrtzServer 有个线程监听PID文件的输入，如果有新的指令写入则执行相应的指令
	 * @param pidfile
	 * @param cmd
	 * @throws Exception
	 */
	private void sendCommandToServer(File pidfile,String cmd) throws Exception{
		FileOutputStream ops=new FileOutputStream(pidfile,true);
		IOUtils.write(cmd, ops);
		IOUtils.closeQuietly(ops);
	}

	/**
	 * 重启QuartzServerDemo
	 * @param args
	 * @throws Exception
	 */
	public void restart(String pidfile) throws Exception {
		File pid = new File(pidfile);
		this.sendCommandToServer(pid, "restart");
	}

	/**
	 * pause the QuartzServerDemo
	 * 
	 * @param args
	 * 
	 * @throws Exception
	 */
	public void pause(String pidfile) throws Exception {
		File pid = new File(pidfile);
		this.sendCommandToServer(pid, "pause");
	}

	/**
	 * resume the QuartzServerDemo
	 * 
	 * @param args
	 * 
	 * @throws Exception
	 */
	public void resume(String pidfile) throws Exception {
		File pid = new File(pidfile);
		this.sendCommandToServer(pid, "resume");
	}
}
