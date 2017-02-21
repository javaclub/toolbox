/*
 * @(#)Environment.java	2011-7-21
 *
 * Copyright (c) 2011. All Rights Reserved.
 *
 */

package com.github.javaclub.toolbox.core;

/**
 * Java Environment
 * 
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: Environment.java 2011-7-21 下午07:59:39 Exp $
 */
public class Environment {

	public static boolean support(String className) {
		boolean flag = false;
		try {
			Class.forName(className);
			flag = true;
		} catch (ClassNotFoundException e) {
		}
		return flag;
	}
}
