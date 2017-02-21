/*
 * @(#)XPathTest.java	2011-8-17
 *
 * Copyright (c) 2011. All Rights Reserved.
 *
 */

package com.github.javaclub.toolbox.core.xml;

import com.github.javaclub.toolbox.core.xml.XPath;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * XPathTest
 * 
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: XPathTest.java 2011-8-17 下午04:03:01 Exp $
 */
public class XPathTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void shouldSelectText() {
		String text = "<a> <b>this is only text</b> </a>";
		Assert.assertEquals("this is only text", XPath.selectText("/a/b[1]", text));
	}

	@Test
	public void shouldCountElements() {
		String xml = "<a> <b>text 1</b> <b>text 2</b> <b>text 3</b></a>";
		Assert.assertEquals(3, XPath.count("//b", xml));
	}

	@Test
	public void shouldFindAttribute() {
		String xml = "<a> <b id=\"y\"></b></a>";
		Assert.assertEquals("y", XPath.attributeValue(xml, "/a/b[1]/@id"));
	}

	@Test
	public void shouldSelectTextNodesAsStringList() {
		String xml = "<people>" + "  <person>" + "   <name>John</name>"
				+ "  </person>" + "  <person>" + "   <name>Jane</name>"
				+ "  </person>" + "</people>\n";
		
		Assert.assertEquals("John", XPath.selectStrings(xml, "//name/text()").get(0));
		Assert.assertEquals("Jane", XPath.selectStrings(xml, "//name/text()").get(1));
	}

	@Test(expected = RuntimeException.class)
	public void shouldFailSelectStringsIfXPathDoesNotPointToTextNodes() {
		String xml = "<people>" + "  <person>" + "   <name>John</name>"
				+ "  </person>" + "  <person>" + "   <name>Jane</name>"
				+ "  </person>" + "</people>\n";

		XPath.selectStrings(xml, "//name");
	}

}
