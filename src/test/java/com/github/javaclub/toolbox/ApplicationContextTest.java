/*
 * @(#)SpringContextTest.java	2011-6-23
 *
 * Copyright (c) 2011. All Rights Reserved.
 *
 */

package com.github.javaclub.toolbox;

import java.io.IOException;
import java.util.Map;

import junit.framework.Assert;
import com.github.javaclub.toolbox.core.PropertySystem;
import com.github.javaclub.toolbox.core.ip.IPAddress;
import com.github.javaclub.toolbox.core.mail.DefaultMailSender;
import com.github.javaclub.toolbox.core.mail.MailMessage;
import com.github.javaclub.toolbox.id.AbstractIdGenerator;
import com.github.javaclub.toolbox.util.SpringContextUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.FileSystemResource;

/**
 * desc
 * 
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: ApplicationContextTest.java 1595 2012-03-06 06:06:36Z gerald.chen.hz $
 */
public class ApplicationContextTest {

	ApplicationContext context = null;
	private static IPAddress ipAddress = null;
	private static AbstractIdGenerator idGenerator = null;
	private static DefaultMailSender mailSender = null;

	@Before
	public void init() throws Exception {
		context = new ClassPathXmlApplicationContext("applicationContext.xml");
		ipAddress = SpringContextUtil.getBean("ipAddress");
		idGenerator = SpringContextUtil.getBean("idGenerator");
		mailSender = SpringContextUtil.getBean("mailSender");
	}

	@After
	public void destroy() {
		if (context != null) {
			((AbstractApplicationContext) context).destroy();
		}
	}

	@Test
	public void testSendMail2() {
		MailMessage message = new MailMessage("kkaa@126.com", new String[] {
				"etxp@qq.com", "756382030@qq.com", "115143140@qq.com"}, "Spring邮件发�?测试GBK",
				"下午好，Hello,您好");
		message.setCc("jsoft@126.com");
		message.addAttach("txt.txt", new FileSystemResource("C:/Documents and Settings/hongyuan.czq/桌面/txt.txt"))
			.addAttach("java.java", new FileSystemResource("C:/Documents and Settings/hongyuan.czq/桌面/java.java"))
			.addAttach("xml.xml", new FileSystemResource("C:/Documents and Settings/hongyuan.czq/桌面/xml.xml"));
		mailSender.send(message);
	}

	@Test
	public void testSendMail() {
		mailSender.send("jstudio@163.com", "etxp@qq.com", "问好", "hahah哈哈");
	}

	@Test
	public void testIpAddress() {
		System.out.println(ipAddress.getCountry("220.181.6.6"));
		System.out.println(ipAddress.getCountry("202.101.172.35") + " ---> "
				+ ipAddress.getArea("202.101.172.35"));
		System.out.println(ipAddress.getCountry("64.233.167.99") + " ---> "
				+ ipAddress.getArea("64.233.167.99"));
		System.out.println(ipAddress.getCountry("203.208.33.100") + " ---> "
				+ ipAddress.getArea("203.208.33.100"));
		System.out.println(ipAddress.getCountry("127.0.0.1"));
		System.out.println(ipAddress.getArea("220.181.28.50"));
		System.out.println(ipAddress.getCountry("10.9.7.11") + "--->"
				+ ipAddress.getArea("10.9.7.11"));
	}

	@Test
	public void testPropertySystem() throws IOException {
		Map<String, String> map = PropertySystem.getProperties();
		System.out.println(map);
		Assert.assertTrue(map.size() > 0);
	}

	@Test
	public void testGetSystemProperty() {
		for (int i = 0; i < 10; i++) {
			System.out.println(PropertySystem.getProperty("key1"));
		}
	}

	@Test
	public void testSetSystemProperty() throws IOException {
		PropertySystem.setProperty("ko", "ko-value");
	}

	@Test
	public void testIdGenerator() {
		for (int i = 0; i < 10; i++) {
			System.out.println(idGenerator.number(6));
		}

		for (int i = 0; i < 10; i++) {
			System.out.println(idGenerator.generate());
		}

		for (int i = 0; i < 10; i++) {
			System.out.println(idGenerator.generate(16));
		}
	}
}
