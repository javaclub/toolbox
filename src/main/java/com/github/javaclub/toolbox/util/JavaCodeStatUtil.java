/*
 * @(#)JavaCodeStatUtil.java	2010-2-24
 *
 * Copyright (c) 2010 by gerald. All Rights Reserved.
 */

package com.github.javaclub.toolbox.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;


import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * 统计java代码行数工具
 * 
 * @author <a href="mailto:gerald.chen@qq.com">Gerald Chen</a>
 * @version $Id: JavaCodeStatUtil.java 56 2011-06-27 02:11:24Z gerald.chen.hz@gmail.com $
 */
public class JavaCodeStatUtil {

	/**
	 * normalLines, commentLines, whiteLines
	 * 
	 * @param javaFile a java file
	 * @return
	 */
	public static long[] stat(File javaFile) {
		Assert.isTrue(javaFile.isFile()
				&& StringUtils.endsWithIgnoreCase(javaFile.getName(), ".java"),
				"the file must be a java file.");
		long normalLines = 0;
		long commentLines = 0;
		long whiteLines = 0;
		BufferedReader br = null;
		boolean comment = false;
		try {
			br = new BufferedReader(new FileReader(javaFile));
			String line = "";
			while ((line = br.readLine()) != null) {
				line = line.trim();
				if (line.matches("^[\\s&&[^\\n]]*$")) {
					whiteLines++;
				} else if (line.startsWith("/*") && !line.endsWith("*/")) {
					commentLines++;
					comment = true;
				} else if (line.startsWith("/*") && line.endsWith("*/")) {
					commentLines++;
				} else if (true == comment) {
					commentLines++;
					if (line.endsWith("*/")) {
						comment = false;
					}
				} else if (line.startsWith("//")) {
					commentLines++;
				} else {
					normalLines++;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			IOUtil.close(br);
		}

		return new long[] { normalLines, commentLines, whiteLines };
	}

	public static long[] stat(String javasrc) {
		Assert.isTrue(StringUtils.hasLength(javasrc));
		File file = new File(javasrc);
		Assert.isTrue(file.exists() && file.isDirectory(),
				"the parameter [javaPath] must be a directory's path.");
		long normalLines = 0;
		long commentLines = 0;
		long whiteLines = 0;
		File[] javaFiles = FileUtil.listTree(file, new FileFilter() {
			public boolean accept(File pathname) {
				if (pathname.isFile() && pathname.getName().endsWith(".java")) {
					return true;
				}
				return false;
			}
		});
		for (File javaFile : javaFiles) {
			long[] counts = stat(javaFile);
			normalLines += counts[0];
			commentLines += counts[1];
			whiteLines += counts[2];
		}
		return new long[] { normalLines, commentLines, whiteLines };
	}

	public static void main(String[] args) {
		String path = "E:/workspace/java/seek/gerald-develop/src";
		long[] a = stat(path);
		System.out.println(a[0] + a[1] + a[2]);
		System.out.println(MessageUtil.format("normalLines:{}, commentLines:{}, whiteLines:{}", new Object[] {a[0] , a[1] , a[2]}));
	}

}
