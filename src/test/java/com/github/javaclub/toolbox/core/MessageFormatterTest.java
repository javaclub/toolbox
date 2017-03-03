/*
 * @(#)MessageFormatterTest.java	2017-3-4
 *
 * Copyright (c) 2017. All Rights Reserved.
 *
 */

package com.github.javaclub.toolbox.core;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * MessageFormatterTest
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: MessageFormatterTest.java 2017-3-4 12:53:44 Exp $
 */
public class MessageFormatterTest {

	@Test
	public void testFormat() {
		String target = MessageFormatter.format("Hi, {}, This is my mum, {}", "Tom", "CHINA");
		String expect = "Hi, Tom, This is my mum, CHINA";
		assertTrue(target.equals(expect));
	}

}
