/*
 * @(#)AbstractMailSender.java	2011-6-28
 *
 * Copyright (c) 2011. All Rights Reserved.
 *
 */

package com.github.javaclub.toolbox.core.mail;

import java.io.Serializable;

import com.github.javaclub.toolbox.core.Message;
import com.github.javaclub.toolbox.core.MessageSender;

/**
 * AbstractMailSender
 * 
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: AbstractMailSender.java 173 2011-07-23 10:43:20Z gerald.chen.hz@gmail.com $
 */
public abstract class AbstractMailSender implements MessageSender {

	public boolean send(String from, String to, String subject, String content) {
		MailMessage message = new MailMessage(from, new String[] { to },
				subject, content);
		return send(message);
	}

	public boolean sendTemplateMail(String tpl, Message message) {
		MailMessage mailMsg = (MailMessage) message;
		String tplText = (String) processTemplate(tpl);
		if(tplText != null && tplText.length() > 0) {
			mailMsg.setContent(tplText);
		}
		return send(mailMsg);
	}

	public abstract Serializable processTemplate(String tpl);

}
