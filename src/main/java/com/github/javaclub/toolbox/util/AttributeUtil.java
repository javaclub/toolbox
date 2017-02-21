/*
 * @(#)AttributeUtil.java	2013-7-3
 *
 * Copyright (c) 2013. All Rights Reserved.
 *
 */

package com.github.javaclub.toolbox.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * AttributeUtil 对数据库存储扩展字段的一个处理方法
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: AttributeUtil.java 2013-7-3 14:38:36 Exp $
 */
public class AttributeUtil {

	private static final String SP = ";";
	private static final String SSP = ":";
	private static final String COMMA = ",";
	
	private static final String R_SP = "#3A";
	private static final String R_SSP = "#3B";
	
	/**
	 * 通过Map转换成String
	 * @param attrs
	 * @return
	 */
	public static final String toString(Map<String, String> attrs) {
		StringBuilder sb = new StringBuilder();
		if (null != attrs && !attrs.isEmpty()) {
			sb.append(SP);
			for (String key : attrs.keySet()) {
				String val = attrs.get(key);
				if (isNotEmpty(val)) {
					sb.append(encode(key)).append(SSP).append(encode(val)).append(SP);
				}
			}
		}
		return sb.toString();
	}
	
	
	/**
	 * 通过字符串解析成attributes
	 * @param str
	 * @return
	 */
	public static final Map<String, String> fromString(String str) {
		Map<String, String> attrs = new HashMap<String, String>();
		if (isNotBlank(str)) {
			String[] arr = str.split(SP);
			if (null != arr) {
				for (String kv : arr) {
					if (isNotBlank(kv)) {
						String[] ar = kv.split(SSP);
						if (null != ar && ar.length == 2) {
							String key = decode(ar[0]);
							String val = decode(ar[1]);
							if (isNotEmpty(val)) {
								attrs.put(key, val);
							}
						}
					}
				}
			}
		}
		return attrs;
	}
	
	/**
	 * 字符串变成set
	 * @param str
	 * @return
	 */
	public static Set<String> stringToSet(String str) {
		Set<String> set = new HashSet<String>();
		if(isNotBlank(str)) {
			String[] arr = str.split(COMMA);
			if(null != arr) {
				for(String value : arr) {
					if(isNotEmpty(value.trim())) {
						set.add(value.trim());
					}
				}
			}
		}
		return set;
	}
	
	/**
	 * set变成字符串
	 * @param val
	 * @return
	 */
	public static String setToString(Set<String> set) {
		String str = null;
		if( null != set && !set.isEmpty() ) {
			str = set.toString().replace("[", COMMA).replace("]", COMMA);
			
		}
		return str;
	}
	
    public static boolean isNotEmpty(String str) {
        return ((str != null) && (str.length() > 0));
    }
    
    public static boolean isNotBlank(String str) {
        int length;

        if ((str == null) || ((length = str.length()) == 0)) {
            return false;
        }

        for (int i = 0; i < length; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }

        return false;
    }
    /**
     * @param text 要扫描的字符串
     * @param repl 要搜索的子串
     * @param with 替换字符串
     *
     * @return 被替换后的字符串，如果原始字符串为<code>null</code>，则返回<code>null</code>
     */
    public static String replace(String text, String repl, String with) {
        return replace(text, repl, with, -1);
    }
    
    /**
     * @param text 要扫描的字符串
     * @param repl 要搜索的子串
     * @param with 替换字符串
     * @param max maximum number of values to replace, or <code>-1</code> if no maximum
     *
     * @return 被替换后的字符串，如果原始字符串为<code>null</code>，则返回<code>null</code>
     */
    public static String replace(String text, String repl, String with, int max) {
        if ((text == null) || (repl == null) || (with == null) 
        		|| (repl.length() == 0) || (max == 0)) {
            return text;
        }

        StringBuffer buf   = new StringBuffer(text.length());
        int          start = 0;
        int          end   = 0;

        while ((end = text.indexOf(repl, start)) != -1) {
            buf.append(text.substring(start, end)).append(with);
            start = end + repl.length();

            if (--max == 0) {
                break;
            }
        }

        buf.append(text.substring(start));
        return buf.toString();
    }
    
    private static String encode(String val) {
		return replace(replace(val, SP, R_SP), SSP, R_SSP);
	}
	
	private static String decode(String val) {
		return replace(replace(val, R_SP, SP), R_SSP, SSP);
	}
}
