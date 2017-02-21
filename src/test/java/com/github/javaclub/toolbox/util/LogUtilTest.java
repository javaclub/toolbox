/*
 * @(#)LogUtilTest.java	2011-6-17
 *
 * Copyright (c) 2011. All Rights Reserved.
 *
 */

package com.github.javaclub.toolbox.util;

import com.github.javaclub.toolbox.core.Strings;
import com.github.javaclub.toolbox.util.LogUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * desc
 * 
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: LogUtilTest.java 59 2011-06-27 02:14:17Z
 *          gerald.chen.hz@gmail.com $
 */
public class LogUtilTest {

	ApplicationContext context = null;

	@Before
	public void init() throws Exception {
		context = new ClassPathXmlApplicationContext("applicationContext.xml");
	}

	@After
	public void destroy() throws Exception {
		if (context != null) {
			((AbstractApplicationContext) context).destroy();
		}
	}

	@Test
	public void testWriteString() {
		for (int i = 0; i < 10; i++) {
			LogUtil.write("哈哈你好我是中国" + i);
		}

	}

	@Test
	public void testWriteStringObjectArray() {
		for (int i = 0; i < 10; i++) {
			LogUtil.write("哈哈{} 你好我是中国人{}" + i, new String[] {
					Strings.random(6, "4234jhrewrfwef"),
					Strings.random(3, "4234jhrewrfwef") });
		}
	}

}
