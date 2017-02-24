package com.github.javaclub.toolbox.core;

import static org.junit.Assert.*;

import org.junit.Test;

public class StringsTest {

	@Test
	public void testConcat() {
		String txt = Strings.concat("i", "am", "from", "china");
		assertTrue("iamfromchina".equals(txt));
	}

}
