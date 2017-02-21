/*
 * @(#)ResourceTest.java	2011-7-9
 *
 * Copyright (c) 2011. All Rights Reserved.
 *
 */

package com.github.javaclub.toolbox;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * ResourceTest
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: ResourceTest.java 82 2011-07-09 06:38:12Z $
 */
public class ResourceTest {
	
	public static void main(String[] args) throws IOException {
		Resource resource = new ClassPathResource("conf/props/test.properties");
		Properties p = new Properties();
		p.load(resource.getInputStream());
		p.list(System.out);
		
		System.out.println(new Date().getTime());
	}
	
	@Test
	public void testYu() {
		int num = 0;
		
		num |= 1;
		System.out.println(num);
		
		num |= 2;
		System.out.println(num);
		
		System.out.println((8-10) % 10);
		
		long v1 = 100; int v2 = 100;
		float f = div(v1, v2, 2);
		System.out.println(f);
	}
	
	@Test
	public void testDatetimeToLong() throws Exception {
		String time = "2015-10-01 23:59:59";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long longValue = simpleDateFormat.parse(time).getTime();
        System.out.println(longValue);
	}
	
	public static float div(long v1, int v2, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).floatValue();
	}
}
