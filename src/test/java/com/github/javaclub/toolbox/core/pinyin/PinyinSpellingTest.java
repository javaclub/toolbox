package com.github.javaclub.toolbox.core.pinyin;

import com.github.javaclub.toolbox.core.pinyin.PinyinSpelling;

import org.junit.BeforeClass;
import org.junit.Test;

public class PinyinSpellingTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Test
	public void testGetPinyinHeadChar() {
		String in = "为什么啊这样的世界人们";
		String out = PinyinSpelling.getPinyinHeadChar(in);
		System.out.println(out);
	}

	@Test
	public void testGetPinyin() {
		String me = "我是中国人，呵呵囧愧疚！";
		String output = PinyinSpelling.getPinyin(me);
		System.out.println(output);
	}

	@Test
	public void testGetFirstChar() {
		String s = "囧态呵呵咔嚓";
		String out = PinyinSpelling.getFirstChar(s);
		System.out.println(out);
	}

}
