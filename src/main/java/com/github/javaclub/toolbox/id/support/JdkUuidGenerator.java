/*
 * @(#)JdkUuidGenerator.java	2010-2-26
 *
 * Copyright (c) 2010 by gerald. All Rights Reserved.
 */

package com.github.javaclub.toolbox.id.support;

import java.io.Serializable;

import com.github.javaclub.toolbox.id.IdGenerator;
import com.github.javaclub.toolbox.util.UuidUtil;

/**
 * The UUID generator of JDK.
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: JdkUuidGenerator.java 56 2011-06-27 02:11:24Z gerald.chen.hz@gmail.com $
 */
public class JdkUuidGenerator implements IdGenerator {
	
	public JdkUuidGenerator() {
		super();
	}

	public Serializable generate() {
		return UuidUtil.newUUID();
	}
	
	public static void main(String[] args) {
		IdGenerator generator = new JdkUuidGenerator();
		long start = System.currentTimeMillis();
		for (int i = 0; i < 20000; i++) {
			System.out.println(generator.generate());
		}
		// 4028e38126f990740126f990748c0000
		System.out.println("cost time ---> " + (System.currentTimeMillis() - start));
	}
}
