package org.quartz.standalone.server.startup;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PidFileHelper extends Thread {

	Log log = LogFactory.getLog(PidFileHelper.class);
	private File pidfile;// pid 文件
	private Server server = null;

	public PidFileHelper(Server server) {
		super();
		this.server = server;
	}

	public PidFileHelper() {
		super();
	}

	/**
	 * 检查进程是否存在
	 * 
	 * @return
	 */
	public boolean pidExists(String pid_file_path) {
		pidfile = new File(pid_file_path);
		if (!pidfile.exists()) {// 进程ID文件不存在，说明相应的进程也不存在
			return false;
		} else {
			List<String> pids = this.getPids();// 用jps命令获得进程ID
			String pid2 = this.getPidFromPidFile(pid_file_path);// 从PID文件里获取PID
			if (pids.contains(pid2)) {
				return true;
			} else {
				return false;
			}
		}
	}

	/**
	 * 创建PID file
	 */
	public void createPidFile(String pid_file_path) {
		pidfile = new File(pid_file_path);
		if (!pidfile.exists()) {
			try {
				pidfile.createNewFile();
				String pid = this.getPid(pid_file_path);
				FileUtils.writeStringToFile(pidfile, "PID[" + pid + "]");// 将进程ID
																			// 写到PID文件
			} catch (IOException e) {
				log.error("Error In create pid file\r\n" + e);
			}
		}
		log.info("pidfile:" + pidfile.getAbsolutePath());
	}

	public void run() {
		try {
			FileInputStream in = new FileInputStream(pidfile);
			FileChannel fc = in.getChannel();
			long position = 0;
			long size = fc.size();
			long len = size - position;
			byte[] chs = new byte[(int) len];
			in.read(chs);
			position = size;
			while (true) {
				len = size - position;
				if (position < size && len > 0) {
					chs = new byte[(int) len];
					in.read(chs);
					String line = new String(chs);
					if (line.indexOf("stop") != -1) {
						break;// 停止服务
					}
					chs = null;
					position = size;
				} else {
					Thread.sleep(200);
					size = fc.size();
				}
			}
			fc.close();
			in.close();
			pidfile.delete();// 删除pid文件
			server.destroy();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 获取PID 利用jdk里的jps命令获取
	 * 
	 * @return
	 */
	public String getPid(String pid_file_path) {
		String pid = "";
		try {
			Process p = Runtime.getRuntime().exec("jps -m");
			InputStream input = p.getInputStream();
			LineIterator itr = IOUtils.lineIterator(input, "utf-8");
			String line = null;
			while (itr.hasNext()) {
				line = itr.nextLine();
				if (line.indexOf(pid_file_path) != -1) {
					pid = line.split(" ")[0];
				}
			}
		} catch (IOException e) {
			log.error("Error in get PID\r\n" + e);
		}

		return pid;
	}
	
	
	
	/**
	 * 获取但前所有java进程的PID 利用jdk里的jps命令获取
	 * 
	 * @return
	 */
	public List<String> getPids() {
		List<String> pids=new ArrayList<String>();
		try {
			Process p = Runtime.getRuntime().exec("jps -q");
			InputStream input = p.getInputStream();
			LineIterator itr = IOUtils.lineIterator(input, "utf-8");
			String line = null;
			while (itr.hasNext()) {
				pids.add(itr.nextLine().trim());
			}
		} catch (IOException e) {
			log.error("Error in get PID\r\n" + e);
		}

		return pids;
	}
	

	/**
	 * 从 PID文件中读取PID
	 * 
	 * @param pid_file_path
	 * @return
	 */
	public String getPidFromPidFile(String pid_file_path) {
		String pid = "";
		pidfile = new File(pid_file_path);
		try {
			String content = FileUtils.readFileToString(pidfile);
			if (content.indexOf("PID[") != -1) {
				pid = content.substring(content.indexOf("PID[") + 4, content
						.indexOf("]"));
			}
		} catch (IOException e) {
			log.error("Error in reading PID file.\r\n" + e);
		}
		return pid;
	}

}
