/*
 * @(#)MessageSender.java	2011-6-28
 *
 * Copyright (c) 2011. All Rights Reserved.
 *
 */

package com.github.javaclub.toolbox.core;


/**
 * MessageSender
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: MessageSender.java 71 2011-06-28 07:31:56Z gerald.chen.hz@gmail.com $
 */
public interface MessageSender {

	boolean send(String from, String to, String subject, String content);
	
	boolean send(Message message);
	
}
