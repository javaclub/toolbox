/*
 * @(#)PropertySystemTest.java	2011-6-17
 *
 * Copyright (c) 2011. All Rights Reserved.
 *
 */

package com.github.javaclub.toolbox.core;

import java.io.IOException;

import com.github.javaclub.toolbox.core.PropertySystem;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * desc
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: PropertySystemApplicationContextTest.java 172 2011-07-23 10:42:48Z gerald.chen.hz@gmail.com $
 */
public class PropertySystemApplicationContextTest {

	public static void main(String[] args) throws IOException {
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml", PropertySystemApplicationContextTest.class);
		System.out.println(context);
		System.out.println(PropertySystem.boolValue("is.test.environment"));
		
		PropertySystem.setProperty("hi", "中国人民");
		System.out.println(PropertySystem.getProperty("hi"));
	}
	
	@Test
	public void testStartup() throws IOException {
		PropertySystem.getProperties();
	}
	
	@Test
	public void testConcurency() throws Exception {
		for(int i = 0; i < 18; i++) {
			final int j = i;
			new Thread() {

				public void run() {
					try {
						if(j % 2 == 0) {
							PropertySystem.setProperty("k", "" + j);
						} else {
							PropertySystem.setProperty("k", "" + (j + 1));
						}
					} catch(Exception e) {
						e.printStackTrace();
					}
					
					
				}
				
			}.start();
		}
		Thread.sleep(10 * 1000L);
	}

}

