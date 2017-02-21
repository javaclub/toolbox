/*
 * @(#)AbstractIdGenerator.java	2010-4-6
 *
 * Copyright (c) 2010 by gerald. All Rights Reserved.
 */

package com.github.javaclub.toolbox.id;

import java.io.Serializable;

import com.github.javaclub.toolbox.util.UuidUtil;

/**
 * The base class of id generator.
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: AbstractIdGenerator.java 56 2011-06-27 02:11:24Z gerald.chen.hz@gmail.com $
 */
public abstract class AbstractIdGenerator implements ChangeableIdGenertor {
	
	public Serializable generate() {
		return UuidUtil.newUUID();
	}
	
}
