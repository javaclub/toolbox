/*
 * @(#)FileMonitorTest.java	2011-7-13
 *
 * Copyright (c) 2011. All Rights Reserved.
 *
 */

package com.github.javaclub.toolbox.core.monitor;

import com.github.javaclub.toolbox.core.monitor.FileMonitor;

import org.junit.Test;

/**
 * FileMonitorTest
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: FileMonitorTest.java 114 2011-07-13 09:08:48Z gerald.chen.hz@gmail.com $
 */
public class FileMonitorTest {

	@Test
	public void test() throws Exception {
		String filename = "C:/Documents and Settings/Administrator/桌面/txt.txt";
		String file = "C:/Documents and Settings/Administrator/桌面/file.txt";
		FileMonitor monitor = FileMonitor.getInstance();
		TxtFileListener listener = new TxtFileListener();
		
		monitor.addFileChangeListener(listener, filename, 10 * 1000L);
		monitor.addFileChangeListener(listener, file, 10 * 1000L);
		Thread.sleep(24 * 60 * 60 * 1000L);
	}
}
