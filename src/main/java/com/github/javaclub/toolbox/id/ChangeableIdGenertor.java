/*
 * @(#)ChangeableIdGenertor.java	2010-4-6
 *
 * Copyright (c) 2010 by gerald. All Rights Reserved.
 */

package com.github.javaclub.toolbox.id;

import java.io.Serializable;

/**
 * This class is used to create changeable id.
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: ChangeableIdGenertor.java 56 2011-06-27 02:11:24Z gerald.chen.hz@gmail.com $
 */
public interface ChangeableIdGenertor extends NumberIdGenerator {

	Serializable generate(int length);
}
