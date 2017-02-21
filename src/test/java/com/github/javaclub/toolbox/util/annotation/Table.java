/*
 * @(#)Table.java	2010-4-21
 *
 * Copyright (c) 2010 by gerald. All Rights Reserved.
 */

package com.github.javaclub.toolbox.util.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 声明的一个 POJO 所对应的数据表名,比如：
 * 
 * <pre>
 * &#064;Table(&quot;t_tab&quot;)
 * public class MyPojo{
 * 	...
 * }
 * </pre>
 *
 * @author <a href="mailto:gerald.chen@qq.com">Gerald Chen</a>
 * @version $Id: Table.java 116 2011-07-13 09:09:34Z gerald.chen.hz@gmail.com $
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Table {

	String value();

}
