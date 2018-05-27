/*
 * @(#)Matchable.java	2017年11月13日
 *
 * Copyright (c) 2017. All Rights Reserved.
 *
 */

package com.github.javaclub.toolbox;

/**
 * Matchable
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: Matchable.java 2017年11月13日 19:45:03 Exp $
 */
public interface Matchable<T> {
	
	boolean match(T element);
}
