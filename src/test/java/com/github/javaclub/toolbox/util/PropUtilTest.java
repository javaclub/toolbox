/*
 * @(#)PropUtilTest.java	2011-7-13
 *
 * Copyright (c) 2011. All Rights Reserved.
 *
 */

package com.github.javaclub.toolbox.util;

import java.io.IOException;
import java.util.Properties;

import com.github.javaclub.toolbox.util.PropUtil;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * PropUtilTest
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: PropUtilTest.java 117 2011-07-13 09:52:00Z gerald.chen.hz@gmail.com $
 */
public class PropUtilTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Test
	public void testLoadFromClassPath() {
		Properties p = PropUtil.loadFromClassPath("conf/props/test.properties", true);
		p.list(System.out);
	}
	
	@Test
	public void testSetValue() throws IOException {
		String filename = "C:/Documents and Settings/Administrator/桌面/test.properties";
		Properties p = PropUtil.setValue(filename, "+", "val");
		p.list(System.out);
		System.out.println(p.getProperty("user.home"));
	}

}
