/*
 * @(#)Consts.java	2011-6-17
 *
 * Copyright (c) 2011. All Rights Reserved.
 *
 */

package com.github.javaclub.toolbox;

import java.io.File;
import java.io.Serializable;

import com.github.javaclub.toolbox.util.PropUtil;

/**
 * Collected constants of general utility.
 * 
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: Consts.java 56 2011-06-27 02:11:24Z gerald.chen.hz@gmail.com $
 */
public final class Consts implements Serializable {

	/** id */
	private static final long serialVersionUID = 5594079818259577805L;

	/** the newline character，it is "\r\n" in windows，and "\n" in linux */
	public static final String LINE_SEPARATER = PropUtil.getSystemProperty("line.separator", "\r\n");

	/** File.separator */
	public static final String FILE_SEPARATER = File.separator;
	
	/** System property - <tt>path.separator</tt> */
	public static final String PATH_SEPARATOR = System.getProperty("path.separator");

	/** the temp file directory in os */
	public static final String SYS_TMP_DIR = System.getProperty("java.io.tmpdir");

	/** the user home directory */
	public static final String USER_HOME_DIR = System.getProperty("user.home");

	/** os name */
	public static final String OS_NAME = System.getProperty("os.name");
	
	/** the username of os */
	public static final String USER_NAME = System.getProperty("user.name");
	
	/** jdk varsion */
	public static final String JAVA_VERSION = System.getProperty("java.version");

	/** is linux os */
	public static final boolean LINUX = OS_NAME.startsWith("Linux");

	/** is windows os */
	public static final boolean WINDOWS = OS_NAME.startsWith("Windows");

	/** True if running on SunOS. */
	public static final boolean SUN_OS = OS_NAME.startsWith("SunOS");

	/** True iff this is Java version 1.3. */
	public static final boolean JAVA_1_3 = JAVA_VERSION.startsWith("1.3.");

	/** True iff this is Java version 1.4. */
	public static final boolean JAVA_1_4 = JAVA_VERSION.startsWith("1.4.");

	/** True iff this is Java version 1.5. */
	public static final boolean JAVA_1_5 = JAVA_VERSION.startsWith("1.5.");

	/** True iff this is Java version 1.6. */
	public static final boolean JAVA_1_6 = JAVA_VERSION.startsWith("1.6.");

	/** Opposite of {@link #FAIL}. */
	public static final boolean PASS = true;
	/** Opposite of {@link #PASS}. */
	public static final boolean FAIL = false;

	/** Opposite of {@link #FAILURE}. */
	public static final boolean SUCCESS = true;
	/** Opposite of {@link #SUCCESS}. */
	public static final boolean FAILURE = false;

	/**
	 * Useful for {@link String} operations, which return an index of
	 * <tt>-1</tt> when an item is not found.
	 */
	public static final int NOT_FOUND = -1;

	public static final String EMPTY_STRING = "";
	public static final String SPACE = " ";
	public static final String TAB = "\t";
	public static final String SINGLE_QUOTE = "'";
	public static final String PERIOD = ".";
	public static final String DOUBLE_QUOTE = "\"";

	/* =============== Encoding ==================== */

	/** the prefered encoding */
	public static final String PREFERED_ENCODING = "UTF-8";

	/** encoding: UTF-8 */
	public static final String ENCODING_UTF8 = "UTF-8";

	/** encoding: ISO-8859-1 */
	public static final String ENCODING_ISO8859_1 = "ISO-8859-1";

	/** encoding: GBK */
	public static final String ENCODING_GBK = "GBK";

	/** 服务器端用于存放验证码的key值 */
	public static final String KEY_RANDOM_CODE = "_random_code";

	/** 客户端表单中验证码的name值 */
	public static final String KEY_CLIENT_RANDOM_NUM = "rnum";

}
