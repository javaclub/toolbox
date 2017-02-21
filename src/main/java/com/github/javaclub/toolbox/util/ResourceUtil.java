/*
 * @(#)ResourceUtil.java	2010-1-22
 *
 * Copyright (c) 2010 by gerald. All Rights Reserved.
 */

package com.github.javaclub.toolbox.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Helper class for file resources
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: ResourceUtil.java 131 2011-07-19 10:54:36Z gerald.chen.hz@gmail.com $
 */
public class ResourceUtil {
	/**
	 * Logger for this class
	 */
	protected static final Log logger = LogFactory.getLog(ResourceUtil.class);

	/**
	 * @param resourceName
	 * @return resource (mainly file in file system or file in compressed package) as BufferedInputStream
	 */
	public static BufferedInputStream getResourceInputStream(String resourceName) {
		return new BufferedInputStream(ResourceUtil.class.getResourceAsStream(resourceName));
	}
	
	/**
	 * 返回一个文件读取对象
	 *
	 * @param resourceName 文件相对于包的全路径，如：/com/jsoft/plough/templates/test.properties
	 * @return
	 */
	public static Reader getAsStreamReader(String resourceName) {
		Reader reader = new InputStreamReader(ResourceUtil.class.getResourceAsStream(resourceName));
		return reader;
	}
	
	/**
	 * 读取本Jar包里的文本文件的内容
	 *
	 * @param resourceName 文件相对于包的全路径，如：/com/jsoft/plough/templates/test.properties
	 * @return
	 * @throws IOException
	 */
	public static String readAsString(String resourceName) throws IOException {
		InputStream input = getResourceInputStream(resourceName);
		String content = FileUtil.readAsString(input);
		return content;
	}
	
	/**
	 * 以指定文件编码读取本Jar包里的文本文件的内容
	 *
	 * @param resourceName 文件相对于包的全路径，如：/com/jsoft/plough/templates/test.properties
	 * @param encoding
	 * @return
	 * @throws IOException
	 */
	public static String readAsString(String resourceName, String encoding) throws IOException {
		InputStream input = getResourceInputStream(resourceName);
		String content = FileUtil.readAsString(input, encoding);
		return content;
	}
	
	/**
	 * 将一个输入流转换为字节数
	 *
	 * @param input
	 * @return
	 * @throws IOException
	 */
	public static byte[] toByteArray(InputStream input) throws IOException {
		int status = 0;
		int totalBytesRead = 0;
		int blockCount = 1;
		byte[] dynamicBuffer = new byte[5000 * blockCount];
		byte[] buffer = new byte[5000];

		boolean endOfStream = false;
		while (!endOfStream) {
			int bytesRead = 0;
			if (input.available() != 0) {
				status = input.read(buffer);
				endOfStream = status == -1;
				if (!endOfStream) {
					bytesRead = status;
				}

			} else {
				status = input.read();
				endOfStream = status == -1;
				buffer[0] = (byte) status;
				if (!endOfStream) {
					bytesRead = 1;
				}
			}

			if (!endOfStream) {
				if (totalBytesRead + bytesRead > 5000 * blockCount) {
					++blockCount;
					byte[] newBuffer = new byte[5000 * blockCount];
					System.arraycopy(dynamicBuffer, 0, newBuffer, 0, totalBytesRead);

					dynamicBuffer = newBuffer;
				}
				System.arraycopy(buffer, 0, dynamicBuffer, totalBytesRead, bytesRead);

				totalBytesRead += bytesRead;
			}

		}

		byte[] result = new byte[totalBytesRead];
		if (totalBytesRead != 0) {
			System.arraycopy(dynamicBuffer, 0, result, 0, totalBytesRead);
		}

		return result;
	}

}
