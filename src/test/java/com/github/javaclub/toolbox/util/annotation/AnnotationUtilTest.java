/*
 * @(#)AnnotationUtilTest.java	2011-7-10
 *
 * Copyright (c) 2011. All Rights Reserved.
 *
 */

package com.github.javaclub.toolbox.util.annotation;

import java.lang.annotation.Annotation;

import com.github.javaclub.toolbox.util.AnnotationUtil;

import org.junit.Test;

/**
 * AnnotationUtilTest
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: AnnotationUtilTest.java 116 2011-07-13 09:09:34Z gerald.chen.hz@gmail.com $
 */
public class AnnotationUtilTest {

	@Test
	public void test1() {
		Annotation ann = AnnotationUtil.findAnnotation(User.class, Column.class);
		System.out.println(ann);
		
		ann = AnnotationUtil.findAnnotation(User.class, Table.class);
		System.out.println(ann);
		
		System.out.println(AnnotationUtil.getValue(ann));
	}
	
	@Test
	public void test2() {
		System.out.println(getTableName(User.class));
	}
	
	protected static String getTableName(Class<?> clazz) {
		Annotation ann = AnnotationUtil.findAnnotation(clazz, Table.class);
		return ann == null ? null : ((Table) ann).value();
	}
}
