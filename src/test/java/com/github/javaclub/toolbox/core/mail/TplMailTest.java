/*
 * @(#)TplMailTest.java	2011-6-29
 *
 * Copyright (c) 2011. All Rights Reserved.
 *
 */

package com.github.javaclub.toolbox.core.mail;

import com.github.javaclub.toolbox.core.mail.MailMessage;
import com.github.javaclub.toolbox.util.SpringContextUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


/**
 * TplMailTest
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: TplMailTest.java 76 2011-06-29 02:22:24Z gerald.chen.hz@gmail.com $
 */
public class TplMailTest {

	ApplicationContext context = null;
	private static TplMail mailSender = null;
	String htmlUrl = "http://javaclub.sourceforge.net";
	
	@Before
	public void init() throws Exception {
		context = new ClassPathXmlApplicationContext("applicationContext.xml", TplMailTest.class);
		mailSender = SpringContextUtil.getBean("mailSender");
	}
	
	@After
	public void destroy() {
		if (context != null) {
			((AbstractApplicationContext) context).destroy();
		}
	}
	
	@Test
	public void testSendTplMail() {
		MailMessage message = new MailMessage();
		message.setSubject("明天你好！");
		message.setFrom("adminx@aliyun.com");
		message.setTo(new String[] {
				"jsoft@126.com",
				"gerald.chen.hz@gmail.com",
				//"hongyuan.czq@taobao.com"
		});
		mailSender.sendTemplateMail(htmlUrl, message);
	}
}


