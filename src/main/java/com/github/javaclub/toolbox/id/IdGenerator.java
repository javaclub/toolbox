/*
 * @(#)IdentifierGenerator.java	2010-2-6
 *
 * Copyright (c) 2010 by gerald. All Rights Reserved.
 */

package com.github.javaclub.toolbox.id;

import java.io.Serializable;

/**
 * This interface is used to generate serializable id.
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: IdGenerator.java 56 2011-06-27 02:11:24Z gerald.chen.hz@gmail.com $
 */
public interface IdGenerator {

	/**
	 * Generates a serializable id
	 *
	 * @return Serializable id
	 */
	Serializable generate();
	
}
