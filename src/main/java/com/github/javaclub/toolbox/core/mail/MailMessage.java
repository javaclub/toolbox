/*
 * @(#)MailMessage.java	2011-6-28
 *
 * Copyright (c) 2011. All Rights Reserved.
 *
 */

package com.github.javaclub.toolbox.core.mail;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.github.javaclub.toolbox.core.Message;

import org.springframework.core.io.Resource;
import org.springframework.mail.SimpleMailMessage;

/**
 * MailMessage
 * 
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: MailMessage.java 173 2011-07-23 10:43:20Z gerald.chen.hz@gmail.com $
 */
public class MailMessage extends SimpleMailMessage implements Message {

	/** desc */
	private static final long serialVersionUID = 4914384264585736863L;

	private Map<String, Resource> inline = new HashMap<String, Resource>();
	private Map<String, Resource> attach = new HashMap<String, Resource>();

	public MailMessage() {
		super();
	}

	public MailMessage(SimpleMailMessage original) {
		super(original);
	}

	public MailMessage(String from, String[] to, String subject, String content) {
		super.setFrom(from);
		super.setTo(to);
		super.setSubject(subject);
		this.setContent(content);
	}

	public String getContent() {
		return getText();
	}

	public void setContent(Serializable content) {
		super.setText((String) content);
	}

	public MailMessage addInline(String inlineId, Resource resource) {
		if (!this.getInline().containsKey(inlineId)) {
			this.getInline().put(inlineId, resource);
		}
		return this;
	}

	public MailMessage addAttach(String attachId, Resource resource) {
		if (!this.getAttach().containsKey(attachId)) {
			this.getAttach().put(attachId, resource);
		}
		return this;
	}

	public Map<String, Resource> getInline() {
		return inline;
	}

	public Map<String, Resource> getAttach() {
		return attach;
	}

	public boolean hasInline() {
		return this.getInline().size() > 0;
	}

	public boolean hasAttach() {
		return this.getAttach().size() > 0;
	}
}
