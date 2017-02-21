/*
 * @(#)FileMonitor.java	2010-1-26
 *
 * Copyright (c) 2010 by gerald. All Rights Reserved.
 */

package com.github.javaclub.toolbox.core.monitor;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * desc
 *
 * @author <a href="mailto:gerald.chen@qq.com">Gerald Chen</a>
 * @version $Id: FileMonitor.java 56 2011-06-27 02:11:24Z gerald.chen.hz@gmail.com $
 */
public class FileMonitor {

	private static Log logger = LogFactory.getLog(FileMonitor.class);
	private static final FileMonitor instance = new FileMonitor();
	private Timer timer;
	private Map<String, FileMonitorTask> timerEntries;
	
	private FileMonitor() {
		this.timerEntries = new HashMap<String, FileMonitorTask>();
		this.timer = new Timer(true);
	}
	
	public static FileMonitor getInstance() {
		return instance;
	}
	
	/**
	 * Add a file to the monitor
	 * 
	 * @param listener The file listener
	 * @param filename The filename to watch
	 * @param period The watch interval.
	 */
	public void addFileChangeListener(FileChangeListener listener, 
		String filename, long period) {
		this.removeFileChangeListener(filename);
		
		if(logger.isInfoEnabled()) {
			logger.info("Watching " + filename);
		}
		
		FileMonitorTask task = new FileMonitorTask(listener, filename);
		
		this.timerEntries.put(filename, task);
		this.timer.schedule(task, period, period);
	}
	
	/**
	 * Stop watching a file
	 * 
	 * @param listener The file listener
	 * @param filename The filename to keep watch
	 */
	public void removeFileChangeListener(String filename) {
		FileMonitorTask task = (FileMonitorTask)this.timerEntries.remove(filename);
		
		if (task != null) {
			task.cancel();
		}
	}
	
	private static class FileMonitorTask extends TimerTask {
		private FileChangeListener listener;
		private String filename;
		private File monitoredFile;
		private long lastModified;
		
		public FileMonitorTask(FileChangeListener listener, String filename) {
			this.listener = listener;
			this.filename = filename;
			
			this.monitoredFile = new File(filename);
			if (!this.monitoredFile.exists()) {
				return;
			}
			
			this.lastModified = this.monitoredFile.lastModified();
		}
		
		public void run() {
			long latestChange = this.monitoredFile.lastModified();
			if (this.lastModified != latestChange) {
				this.lastModified = latestChange;
				
				this.listener.fileChanged(this.filename);
			}
		}
	}

}
