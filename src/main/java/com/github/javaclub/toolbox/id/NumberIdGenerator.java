/*
 * @(#)NumberIdGenerator.java	2011-6-16
 *
 * Copyright (c) 2011. All Rights Reserved.
 *
 */

package com.github.javaclub.toolbox.id;

import java.io.Serializable;

/**
 * This interface is used to generate numeric Id.
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: NumberIdGenerator.java 56 2011-06-27 02:11:24Z gerald.chen.hz@gmail.com $
 */
public interface NumberIdGenerator extends IdGenerator {
	
	final String NUMBER_CHARS = "4916037852";

	/**
	 * Generates a numeric Id with the specified length
	 *
	 * @param length the length of the Id
	 * @return numeric Id
	 */
	Serializable number(int length);
}
