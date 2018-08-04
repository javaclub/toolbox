/*
 * @(#)_TestCases.java	2018年5月27日
 *
 * Copyright (c) 2018. All Rights Reserved.
 *
 */

package com.github.javaclub.toolbox;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Test;

import com.github.javaclub.toolbox.ToolBox.Base64;
import com.github.javaclub.toolbox.ToolBox.BonusAlgorithm;

/**
 * 测试用例
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: _TestCases.java 2018年5月27日 23:17:50 Exp $
 */
public class _TestCases {
	
	@Test
	public void testBonusSplit() {
		List<BigDecimal> list = BonusAlgorithm.split(new BigDecimal("30"), 8);
		for (BigDecimal bigDecimal : list) {
			System.out.println(bigDecimal);
		}
	}

	@Test
	public void testUrlSafeBase64() {
		String text = "I love the 43645===6jnhjsfds%%*^54767+-_==4Hfdefd395``~`* Word 亲爱的世界";
		String safeUrlBase64 = Base64.encodeSafeUrlBase64(text);
		System.out.println("safeUrlBase64 = " + safeUrlBase64);
		
		String decodeBase64 = Base64.decodeSafeUrlBase64(safeUrlBase64);
		System.out.println("decodeBase64 = " + decodeBase64);
		
		System.out.println(text.equals(decodeBase64));
	}
	
}
