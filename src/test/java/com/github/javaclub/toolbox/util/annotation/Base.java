/*
 * @(#)Base.java	2011-7-10
 *
 * Copyright (c) 2011. All Rights Reserved.
 *
 */

package com.github.javaclub.toolbox.util.annotation;

/**
 * Base
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: Base.java 116 2011-07-13 09:09:34Z gerald.chen.hz@gmail.com $
 */
@Table("t_user")
public class Base {

	@Column("user_id")
	private int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	
}
