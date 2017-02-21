/*
 * @(#)TxtFileListener.java	2011-7-13
 *
 * Copyright (c) 2011. All Rights Reserved.
 *
 */

package com.github.javaclub.toolbox.core.monitor;

import com.github.javaclub.toolbox.core.monitor.FileChangeListener;

/**
 * TxtFileListener
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: TxtFileListener.java 172 2011-07-23 10:42:48Z gerald.chen.hz@gmail.com $
 */
public class TxtFileListener implements FileChangeListener {

	public void fileChanged(String filename) {
		System.out.println("[" + filename + "] is changed ...");
	}

}
