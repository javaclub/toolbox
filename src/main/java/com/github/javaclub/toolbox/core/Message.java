/*
 * @(#)Message.java	2011-6-28
 *
 * Copyright (c) 2011. All Rights Reserved.
 *
 */

package com.github.javaclub.toolbox.core;

import java.io.Serializable;

/**
 * desc
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: Message.java 71 2011-06-28 07:31:56Z gerald.chen.hz@gmail.com $
 */
public interface Message {

	public void setFrom(String fromId);
	public String getFrom();
	
	public void setTo(String[] toIds);
	public String[] getTo();
	
	public void setContent(Serializable content);
	public Serializable getContent();
	
	public void setSubject(String subject);
	public String getSubject();
}
