/*
 * @(#)Decorator.java	2017年11月13日
 *
 * Copyright (c) 2017. All Rights Reserved.
 *
 */

package com.github.javaclub.toolbox;

/**
 * Decorator 修饰器接口
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: Decorator.java 2017年11月13日 19:45:03 Exp $
 */
public interface Decorator<T, P, R> {
	
	/**
	 * 修饰目标对象
	 *
	 * @param element 被修饰的对象
	 * @param param   上下文参数
	 * @return
	 */
	R execute(T element, P param);
}
