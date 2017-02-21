/*
 * @(#)TplMail.java	2011-6-29
 *
 * Copyright (c) 2011. All Rights Reserved.
 *
 */

package com.github.javaclub.toolbox.core.mail;

import java.io.Serializable;

import com.github.javaclub.toolbox.core.mail.DefaultMailSender;
import com.github.javaclub.toolbox.core.net.DownloadUtil;

/**
 * TplMail
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: TplMail.java 172 2011-07-23 10:42:48Z gerald.chen.hz@gmail.com $
 */
public class TplMail extends DefaultMailSender {
	
	public TplMail() {
		super();
	}

	public Serializable processTemplate(String tpl) {
		String mailHtml = DownloadUtil.download(tpl).getText();
		return mailHtml;
	}
	
}