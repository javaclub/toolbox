/*
 * @(#)Ref.java	2017年7月26日
 *
 * Copyright (c) 2017. All Rights Reserved.
 *
 */

package com.github.javaclub.toolbox;

import java.io.Serializable;

/**
 * Ref
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: Ref.java 2017年7月26日 3:27:11 Exp $
 */
public class Ref<T> implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private T object;

	public Ref() {
	}
	
	public static <T> Ref<T> create(T o) {
		Ref<T> ref = new Ref<T>();
		ref.setObject(o);
		return ref;
	}

	public T getObject() {
		return object;
	}

	public void setObject(T object) {
		this.object = object;
	}
	
	
}
