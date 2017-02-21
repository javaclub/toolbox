/*
 * @(#)Authenticator.java	2010-3-14
 *
 * Copyright (c) 2010 by gerald. All Rights Reserved.
 */

package com.github.javaclub.toolbox.core.mail;

import java.io.Serializable;

/**
 * Authenticator
 * 
 * @author <a href="mailto:gerald.chen@qq.com">Gerald Chen</a>
 * @version $Id: Authenticator.java 71 2011-06-28 07:31:56Z gerald.chen.hz@gmail.com $
 */
public class Authenticator implements Serializable {

	private static final long serialVersionUID = 8696368778435273274L;

	private String mailhost;
	private String mailuser;
	private String password;
	private String sysaddress;
	private String sysname;

	public Authenticator() {
		super();
	}

	public Authenticator(String mailhost, String mailuser, String password,
			String sysaddress, String sysname) {
		super();
		this.mailhost = mailhost;
		this.mailuser = mailuser;
		this.password = password;
		this.sysaddress = sysaddress;
		this.sysname = sysname;
	}

	public String getMailhost() {
		return mailhost;
	}

	public void setMailhost(String mailhost) {
		this.mailhost = mailhost;
	}

	public String getMailuser() {
		return mailuser;
	}

	public void setMailuser(String mailuser) {
		this.mailuser = mailuser;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSysaddress() {
		return sysaddress;
	}

	public void setSysaddress(String sysaddress) {
		this.sysaddress = sysaddress;
	}

	public String getSysname() {
		return sysname;
	}

	public void setSysname(String sysname) {
		this.sysname = sysname;
	}

}
