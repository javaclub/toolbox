/*
 * @(#)FileChangeListener.java	2010-1-26
 *
 * Copyright (c) 2010 by gerald. All Rights Reserved.
 */

package com.github.javaclub.toolbox.core.monitor;

/**
 * desc
 *
 * @author <a href="mailto:gerald.chen@qq.com">Gerald Chen</a>
 * @version $Id: FileChangeListener.java 56 2011-06-27 02:11:24Z gerald.chen.hz@gmail.com $
 */
public interface FileChangeListener {

	/**
	 * Invoked when a file changes
	 * 
	 * @param filename Name of the changed file
	 */
	public void fileChanged(String filename);
}
