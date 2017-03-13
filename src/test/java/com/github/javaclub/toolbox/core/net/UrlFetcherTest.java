/*
 * @(#)UrlFetcherTest.java	2010-5-8
 *
 * Copyright (c) 2010 by gerald. All Rights Reserved.
 */

package com.github.javaclub.toolbox.core.net;

import java.io.File;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.javaclub.toolbox.core.Strings;

/**
 * desc
 *
 * @author <a href="mailto:gerald.chen@qq.com">Gerald Chen</a>
 * @version $Id: UrlFetcherTest.java,v 1.1 2010/06/24 16:51:00 gerald.chen Exp $
 */
public class UrlFetcherTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void testFetch() throws Exception {
		String dest = "D:/temp/digfile";
		String image = "http://www.apache.org/images/asf_logo_wide.gif";
		File file = UrlFetcher.fetch(image, dest, false);
		System.out.println(file.getAbsolutePath());
	}

	@Test
	public void testMultiFetch() {
		String[] urls = new String[] {
			"http://www.apache.org/images/asf_logo_wide.gif",
			"http://www.apache.org/images/asf_logo_wide.gif",
			"http://www.apache.org/images/asf_logo_wide.gif",
			"http://www.apache.org/images/asf_logo_wide.gif",
		};
		String dest = "D:/temp/digfile";
		UrlFetcher.multiFetch(urls, dest, false);
		// 设置断点，debug看效果
		System.out.println("kkk");
	}
	
	@Test
	public void testMultiFetch2() {
		String source = DownloadUtil.download("http://cms.17ext.com/extpic/2009/0504/ext-icon-300.html").getText();
		String regex = "/uploads/allimg/090504/.*gif";
		String[] links = Strings.multiFind(source, regex);
		for (int i = 0; i < links.length; i++) {
			links[i] = "http://cms.17ext.com" + links[i];
		}
		String dest = "D:/temp/digfile/ext";
		UrlFetcher.multiFetch(links, dest, false);
		// 设置断点，debug看效果
		System.out.println("kkk");
	}
	
	@Test
	public void testFetchBaiduHimage() {
		
		String from = "http://img.baidu.com/hi/";
		// ldw
		String dest = "D:/tmp/digfile/baidu/hi/ldw";
		/*String[] ldw = new String[100];
		for (int i = 0; i < 100; i++) {
			ldw[i] = from + "ldw/w_" + StringUtil.alignRight(String.valueOf(i), 4, '0') + ".gif";
		}
		UrlFetcher.multiFetch(ldw, dest, true);*/
		
		// tsj
		/*dest = "D:/tmp/digfile/baidu/hi/tsj";
		String[] tsj = new String[100];
		for (int i = 0; i < 100; i++) {
			tsj[i] = from + "tsj/t_" + StringUtil.alignRight(String.valueOf(i), 4, '0') + ".gif";
		}
		UrlFetcher.multiFetch(tsj, dest, true);*/
		
		// bobo
		/*dest = "D:/tmp/digfile/baidu/hi/bobo";
		String[] bobo = new String[100];
		for (int i = 0; i < 100; i++) {
			bobo[i] = from + "bobo/B_" + StringUtil.alignRight(String.valueOf(i), 4, '0') + ".gif";
		}
		UrlFetcher.multiFetch(bobo, dest, true);*/
		
		// babycat
		dest = "D:/tmp/digfile/baidu/hi/jx2";
		String[] babycat = new String[100];
		for (int i = 0; i < 100; i++) {
			babycat[i] = from + "jx2/j_" + Strings.alignRight(String.valueOf(i), 4, '0') + ".gif";
		}
		UrlFetcher.multiFetch(babycat, dest, true);
		
		// face
		/*dest = "D:/tmp/digfile/baidu/hi/face";
		String[] face = new String[99];
		for (int i = 0; i < 99; i++) {
			face[i] = from + "face/i_f" + StringUtil.alignRight(String.valueOf(i), 2, '0') + ".gif";
		}
		UrlFetcher.multiFetch(face, dest, true);
		
		// youa
		dest = "D:/tmp/digfile/baidu/hi/youa";
		String[] youa = new String[100];
		for (int i = 0; i < 99; i++) {
			youa[i] = from + "youa/y_" + StringUtil.alignRight(String.valueOf(i), 4, '0') + ".gif";
		}
		UrlFetcher.multiFetch(youa, dest, true);*/
		
		// 设置断点，debug看效果
		System.out.println("kkk");
	}


}
