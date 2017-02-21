/*
 * @(#)DefaultMailSender.java	2011-6-28
 *
 * Copyright (c) 2011. All Rights Reserved.
 *
 */

package com.github.javaclub.toolbox.core.mail;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.mail.internet.MimeMessage;

import com.github.javaclub.toolbox.Consts;
import com.github.javaclub.toolbox.core.JRuntimeException;
import com.github.javaclub.toolbox.core.Message;
import com.github.javaclub.toolbox.core.PropertySystem;
import com.github.javaclub.toolbox.core.base64.Base64;
import com.github.javaclub.toolbox.util.NumberUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

/**
 * DefaultMailSender
 * 
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: DefaultMailSender.java 71 2011-06-28 07:31:56Z gerald.chen.hz@gmail.com $
 */
public class DefaultMailSender extends AbstractMailSender implements InitializingBean {

	private String mailEncoding;
	private JavaMailSenderImpl javaMailSender;
	
	private static int count = 0;
	private static List<Authenticator> authenticators = new ArrayList<Authenticator>();

	protected static final Log LOG = LogFactory.getLog(DefaultMailSender.class);

	public boolean send(Message message) {
		MailMessage sendMessage = (MailMessage) message;
		Authenticator authenticator = this.getAuthenticator();
		javaMailSender.setHost(authenticator.getMailhost());
		javaMailSender.setUsername(authenticator.getMailuser());
		javaMailSender.setPassword(Base64.decode(authenticator.getPassword()));
		try {
			MimeMessage mailMessage = javaMailSender.createMimeMessage();
			MimeMessageHelper messageHelper = new MimeMessageHelper(
					mailMessage, true,
					getMailEncoding() == null ? Consts.PREFERED_ENCODING
							: getMailEncoding());

			messageHelper.setTo(sendMessage.getTo());
			if (sendMessage.getCc() != null && sendMessage.getCc().length > 0) {
				messageHelper.setCc(sendMessage.getCc());
			}
			messageHelper.setFrom(authenticator.getSysaddress(), authenticator
					.getSysname());
			messageHelper.setSubject(sendMessage.getSubject());
			messageHelper.setText(sendMessage.getContent(), true);
			if (sendMessage.hasInline()) {
				for (Map.Entry<String, Resource> entry : sendMessage
						.getInline().entrySet()) {
					messageHelper.addInline(entry.getKey(), entry.getValue());
				}
			}
			if (sendMessage.hasAttach()) {
				for (Map.Entry<String, Resource> entry : sendMessage
						.getAttach().entrySet()) {
					messageHelper.addAttachment(entry.getKey(), entry
							.getValue());
				}
			}
			javaMailSender.send(mailMessage);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public Serializable processTemplate(String tpl) {
		return null;
	}
	
	public void setJavaMailSender(JavaMailSenderImpl javaMailSender) {
		this.javaMailSender = javaMailSender;
	}

	public void setMailEncoding(String mailEncoding) {
		this.mailEncoding = mailEncoding;
	}

	public String getMailEncoding() {
		return mailEncoding;
	}

	protected Authenticator getAuthenticator() {
		Authenticator authenticator = null;
		if (count <= 0) {
			throw new JRuntimeException(
					"There are no Authenticators in config file.");
		} else if (count == 1) {
			authenticator = authenticators.get(0);
		} else {
			// 在多个邮件配置中随即产生一个
			authenticator = authenticators.get(NumberUtil.random(count));
			if (LOG.isInfoEnabled()) {
				LOG.info("Send mail using " + authenticator.getMailuser() + "@"
						+ authenticator.getMailhost());
			}
		}
		return authenticator;
	}

	public void afterPropertiesSet() throws Exception {
		if (javaMailSender == null) {
			javaMailSender = new JavaMailSenderImpl();
		}
		init();
	}

	private void init() {
		Map<String, String> properties = new HashMap<String, String>();
		try {
			properties = PropertySystem.getProperties();
		} catch (Exception e) {
			throw new JRuntimeException(
					"The PropertySystem hasn't been initialized", e);
		}
		Set<Map.Entry<String, String>> entrys = properties.entrySet();
		String key = null;
		Authenticator authenticator = null;
		for (Map.Entry<String, String> entry : entrys) {
			key = entry.getKey();
			if (key.startsWith("mail.host.")) {
				String idx = key.substring(key.lastIndexOf(".") + 1);
				authenticator = new Authenticator(PropertySystem
						.getProperty("mail.host." + idx), PropertySystem
						.getProperty("mail.user." + idx), PropertySystem
						.getProperty("mail.password." + idx), PropertySystem
						.getProperty("mail.system.user." + idx), PropertySystem
						.getProperty("mail.system.user.name." + idx));
				authenticators.add(authenticator);
				count++;
			}
		}
		if (LOG.isInfoEnabled()) {
			LOG.info("There are " + count + " Authenticators in config file.");
		}
	}

}
