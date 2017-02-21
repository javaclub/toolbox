package com.github.javaclub.toolbox.core.pinyin;

import java.io.UnsupportedEncodingException;

import com.github.javaclub.toolbox.util.BCConvert;

/**
 * 汉字到拼音的转换
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: PinyinSpelling.java 56 2011-06-27 02:11:24Z gerald.chen.hz@gmail.com $
 */
public class PinyinSpelling {

	/**
	 * 如果输入字符串中有汉字，则将汉字转换为其全拼的首字母，最后一起返回
	 *
	 * @param hanzis 带有汉字的输入
	 * @return  汉字首字母的拼音
	 */
	public static String getPinyinHeadChar(String hanzis) {
		try {
			return Chinese2Spell.getFirstSpell(BCConvert.qj2bj(hanzis));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 取得汉字拼音
	 *
	 * @param hanzis 带有汉字的输入
	 * @return 汉字拼音结果
	 */
	public static String getPinyin(String hanzis) {
		try {
			return Chinese2Spell.getFullSpell(BCConvert.qj2bj(hanzis));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 取得输入内容的第一个字符的拼音首字母
	 *
	 * @param chinese 带有汉字的输入
	 * @return 如果第一个字符是数字或字母的话，直接返回；否则返回第一个汉字的拼音首字母
	 */
	public static String getFirstChar(String chinese) {
		char first = BCConvert.qj2bj(chinese).charAt(0);
		char charArray[] = { first };
		String r = new String(charArray);
		if (Character.isDigit(charArray[0])) {
			r = "" + charArray[0];
		} else if (first >= 'a' && first <= 'z' || first >= 'A' && first <= 'Z') {
			r = "" + charArray[0];
		} else {
			r = getPinyinHeadChar(r);
		}
		return r;
	}

	public static void main(String[] args) {
		
		String test = "汉字转拼音";
		long c1 = System.currentTimeMillis();
        System.out.println(PinyinSpelling.getPinyin(test));
        System.out.println(System.currentTimeMillis() - c1);
		
		System.out.println(getPinyinHeadChar("厥囧我啊"));
		System.out.println(getFirstChar("我是中国人"));
		
		System.out.println(BCConvert.qj2bj("……×（……９９８ＨＪ00plfＧＪＧｅｔｅｔｒ，"));
	}
}
