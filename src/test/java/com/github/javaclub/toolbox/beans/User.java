/*
 * @(#)User.java	2011-7-18
 *
 * Copyright (c) 2011. All Rights Reserved.
 *
 */

package com.github.javaclub.toolbox.beans;

import java.io.Serializable;

/**
 * User
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: User.java 172 2011-07-23 10:42:48Z gerald.chen.hz@gmail.com $
 */
public class User implements Serializable {

	/** desc */
	private static final long serialVersionUID = 4492079704572871503L;
	
	private Integer id;
	
	private String username;
	
	private String paaaword;
	
	private String job;
	
	public User() {
		super();
	}

	public User(String username) {
		super();
		this.username = username;
	}

	public User(String username, String paaaword, String job) {
		super();
		this.username = username;
		this.paaaword = paaaword;
		this.job = job;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPaaaword() {
		return paaaword;
	}

	public void setPaaaword(String paaaword) {
		this.paaaword = paaaword;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public String toString() {
		return "User [id=" + id + ", job=" + job + ", paaaword=" + paaaword
				+ ", username=" + username + "]";
	}

}
