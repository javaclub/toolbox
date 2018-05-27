/*
 * @(#)ToolBox.java	2018年5月27日
 *
 * Copyright (c) 2018. All Rights Reserved.
 *
 */

package com.github.javaclub.toolbox;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.text.CollationKey;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.CRC32;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.github.javaclub.toolbox.crypt.CodecUtil;

/**
 * ToolBox 无须依赖任何第三方库的通用工具包，使用某些特定特性，可以看情况引入以下类库
 * 
 * <li> com.alibaba:fastjson:1.2.47
 * <li> javax.servlet:javax.servlet-api:3.1.0
 * 
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: ToolBox.java 2018年5月27日 20:05:25 Exp $
 */
public interface ToolBox {
	
	public static final String PREFERRED_ENCODING = "UTF-8";
	
	static final Logger logger = Logger.getLogger("ToolBox");
	
	public class TestCases {
		
	}

	/**
	 * 字符串处理
	 */
	public class Strings {
		
		public static final String EMPTY = "";
		public static final String UNKNOWN = "unknown";
		
		public static final int INDEX_NOT_FOUND = -1;
		
		public final static String format(final String pattern, final Object ... args) {
			return MessageFormatter.format(pattern, args);
		}
		
		public static String newStringUtf8(final byte[] bytes) {
	        return newString(bytes, Charset.forName("UTF-8"));
	    }
		
		private static String newString(final byte[] bytes, final Charset charset) {
	        return bytes == null ? null : new String(bytes, charset);
	    }
		
		public static String noneNull(String str) {
			return (null == str ? EMPTY : str.trim());
		}
		
		public static String noneNull(String str, String defaults) {
			return (null == str ? defaults : str.trim());
		}
		
		public static String anyNoneNull(String ... arrays) {
			if(null == arrays || 0 >= arrays.length) {
				return EMPTY;
			}
			for (String string : arrays) {
				if(isNotBlank(string)) {
					return string.trim();
				}
			}
			
			return EMPTY;
		}
		
		public static String noneBlank(String str, String defaults) {
			return (isBlank(str) ? defaults : str.trim());
		}
		
		public static String anyToString(Object o, boolean noneNull) {
			if(null == o && !noneNull) {
				return null;
			}
			
			return (null == o) ? EMPTY : o.toString();
		}
		
		public static boolean equals(String str1, String str2) {
			if(str1 == str2) {
				return true;
			}
			return str1 == null ? str2 == null : str1.equals(str2);
		}
		
		public static boolean equalsIgnoreCase(String str1, String str2) {
	        return str1 == null ? str2 == null : str1.equalsIgnoreCase(str2);
	    }
		
		public static boolean startsWithIgnoreCase(String str, String prefix) {
			if (str == null || prefix == null) {
				return false;
			}
			if (str.startsWith(prefix)) {
				return true;
			}
			if (str.length() < prefix.length()) {
				return false;
			}
			String lcStr = str.substring(0, prefix.length()).toLowerCase();
			String lcPrefix = prefix.toLowerCase();
			return lcStr.equals(lcPrefix);
		}
	    
	    public static boolean endsWithIgnoreCase(String str, String suffix) {
			if (str == null || suffix == null) {
				return false;
			}
			if (str.endsWith(suffix)) {
				return true;
			}
			if (str.length() < suffix.length()) {
				return false;
			}

			String lcStr = str.substring(str.length() - suffix.length()).toLowerCase();
			String lcSuffix = suffix.toLowerCase();
			return lcStr.equals(lcSuffix);
		}
	    
		public static boolean isEmpty(String input) {
			return null == input || 0 == input.length();
		}
		
		public static boolean isNotEmpty(String input) {
			return null != input && input.length() > 0;
		}
		
		public static boolean isBlank(String str) {
	        int strLen;
	        if (str == null || (strLen = str.length()) == 0) {
	            return true;
	        }
	        for (int i = 0; i < strLen; i++) {
	            if ((Character.isWhitespace(str.charAt(i)) == false)) {
	                return false;
	            }
	        }
	        return true;
	    }
		
		public static boolean isNotBlank(String str) {
	        return !isBlank(str);
	    }
	    
	    public static boolean isNotBlank(String ... arrays) {
			for (String string : arrays) {
				if(isBlank(string)) {
					return false;
				}
			}
			return true;
		}
	    
	    /**
	     * 把非空的字符串都连接起来返回
	     * 
	     * @param array 待连接的字符串
	     * @return
	     */
	    public static String concat(Object... array) {
		    	if(null == array || 0 >= array.length) {
		    		return "";
		    	}
		    	StringBuilder sbf = new StringBuilder();
		    	for (Object o : array) {
		    		sbf.append(null == o ? "" : o.toString());
			}
		    	
		    	return sbf.toString();
	    }
	    
	    /**
	     * <pre>
	     * StringUtils.substringBefore(null, *)      = null
	     * StringUtils.substringBefore("", *)        = ""
	     * StringUtils.substringBefore("abc", "a")   = ""
	     * StringUtils.substringBefore("abcba", "b") = "a"
	     * StringUtils.substringBefore("abc", "c")   = "ab"
	     * StringUtils.substringBefore("abc", "d")   = "abc"
	     * StringUtils.substringBefore("abc", "")    = ""
	     * StringUtils.substringBefore("abc", null)  = "abc"
	     * </pre>
	     *
	     * @param str  the String to get a substring from, may be null
	     * @param separator  the String to search for, may be null
	     * @return the substring before the first occurrence of the separator,
	     *  {@code null} if null String input
	     */
	    public static String substringBefore(final String str, final String separator) {
	        if (isEmpty(str) || separator == null) {
	            return str;
	        }
	        if (separator.isEmpty()) {
	            return EMPTY;
	        }
	        final int pos = str.indexOf(separator);
	        if (pos == INDEX_NOT_FOUND) {
	            return str;
	        }
	        return str.substring(0, pos);
	    }

	    /**
	     * <pre>
	     * StringUtils.substringAfter(null, *)      = null
	     * StringUtils.substringAfter("", *)        = ""
	     * StringUtils.substringAfter(*, null)      = ""
	     * StringUtils.substringAfter("abc", "a")   = "bc"
	     * StringUtils.substringAfter("abcba", "b") = "cba"
	     * StringUtils.substringAfter("abc", "c")   = ""
	     * StringUtils.substringAfter("abc", "d")   = ""
	     * StringUtils.substringAfter("abc", "")    = "abc"
	     * </pre>
	     *
	     * @param str  the String to get a substring from, may be null
	     * @param separator  the String to search for, may be null
	     * @return the substring after the first occurrence of the separator,
	     *  {@code null} if null String input
	     */
	    public static String substringAfter(final String str, final String separator) {
	        if (isEmpty(str)) {
	            return str;
	        }
	        if (separator == null) {
	            return EMPTY;
	        }
	        final int pos = str.indexOf(separator);
	        if (pos == INDEX_NOT_FOUND) {
	            return EMPTY;
	        }
	        return str.substring(pos + separator.length());
	    }
		
		public static boolean hasLength(CharSequence str) {
			return (str != null && str.length() > 0);
		}
		
		public static boolean hasLength(String str) {
			return hasLength((CharSequence) str);
		}
		
		public static boolean hasText(CharSequence str) {
			if (!hasLength(str)) {
				return false;
			}
			int strLen = str.length();
			for (int i = 0; i < strLen; i++) {
				if (!Character.isWhitespace(str.charAt(i))) {
					return true;
				}
			}
			return false;
		}

		public static boolean hasText(String str) {
			return hasText((CharSequence) str);
		}
		
		public static String capitalize(String str) {
			return changeFirstCharacterCase(str, true);
		}
		
		public static String uncapitalize(String str) {
			return changeFirstCharacterCase(str, false);
		}
		
		private static String changeFirstCharacterCase(String str, boolean capitalize) {
			if (str == null || str.length() == 0) {
				return str;
			}
			StringBuffer buf = new StringBuffer(str.length());
			if (capitalize) {
				buf.append(Character.toUpperCase(str.charAt(0)));
			} else {
				buf.append(Character.toLowerCase(str.charAt(0)));
			}
			buf.append(str.substring(1));
			return buf.toString();
		}
		
		/**
		 * <pre>
	     * Utils.split(null)       = null
	     * Utils.split("")         = []
	     * Utils.split("abc def")  = ["abc", "def"]
	     * Utils.split("abc  def") = ["abc", "def"]
	     * Utils.split(" abc ")    = ["abc"]
	     * </pre>
	     *
	     * @param str  the String to parse, may be null
	     * @return an array of parsed Strings, <code>null</code> if null String input
	     */
		public static String[] split(String str) {
	        return split(str, null, -1);
	    }
		
		/**
		 * <pre>
	     * StringUtils.split(null, *)         = null
	     * StringUtils.split("", *)           = []
	     * StringUtils.split("a.b.c", '.')    = ["a", "b", "c"]
	     * StringUtils.split("a..b.c", '.')   = ["a", "b", "c"]
	     * StringUtils.split("a:b:c", '.')    = ["a:b:c"]
	     * StringUtils.split("a b c", ' ')    = ["a", "b", "c"]
	     * </pre>
		 */
		public static String[] split(String str, char separatorChar) {
	        return splitWorker(str, separatorChar, false);
	    }
		
		/**
		 * <pre>
	     * StringUtils.split(null, *)         = null
	     * StringUtils.split("", *)           = []
	     * StringUtils.split("abc def", null) = ["abc", "def"]
	     * StringUtils.split("abc def", " ")  = ["abc", "def"]
	     * StringUtils.split("abc  def", " ") = ["abc", "def"]
	     * StringUtils.split("ab:cd:ef", ":") = ["ab", "cd", "ef"]
	     * </pre>
		 */
		public static String[] split(String str, String separatorChars) {
	        return splitWorker(str, separatorChars, -1, false);
	    }
		
		public static String[] split(String str, String separatorChars, int max) {
	        return splitWorker(str, separatorChars, max, false);
	    }
		
		public static String[] splitByFirstSeparator(String str, String sep) {
		    	String[] result = new String[2];
		    	result[0] = substringBefore(str, sep);
		    	result[1] = substringAfter(str, sep);
		    	return result;
		}
		
		/**
	     * 将目标字符串以指定字符分隔后，并每个分隔的元素去除首尾空格(去除空内容)
	     *
	     * @param str 被处理字符串
	     * @param separatorChars 分隔字符
	     * @return
	     */
	    public static String[] splitAndTrim(String str, String separatorChars) {
		    	List<String> list = new ArrayList<String>();
		    	String[] strArray = splitWorker(str, separatorChars, -1, false);
		    	if(null != strArray && strArray.length > 0) {
		    		for (String e : strArray) {
						if(isBlank(e)) continue;
						list.add(e.trim());
					}
		    	}
		    	return list.toArray(new String[list.size()]);
	    }
	    
	    static String[] splitWorker(String str, char separatorChar, boolean preserveAllTokens) {
	        // Performance tuned for 2.0 (JDK1.4)

	        if (str == null) {
	            return null;
	        }
	        int len = str.length();
	        if (len == 0) {
	            return new String[0];
	        }
	        List<String> list = new ArrayList<String>();
	        int i = 0, start = 0;
	        boolean match = false;
	        boolean lastMatch = false;
	        while (i < len) {
	            if (str.charAt(i) == separatorChar) {
	                if (match || preserveAllTokens) {
	                    list.add(str.substring(start, i));
	                    match = false;
	                    lastMatch = true;
	                }
	                start = ++i;
	                continue;
	            }
	            lastMatch = false;
	            match = true;
	            i++;
	        }
	        if (match || (preserveAllTokens && lastMatch)) {
	            list.add(str.substring(start, i));
	        }
	        return (String[]) list.toArray(new String[list.size()]);
	    }
		
		static String[] splitWorker(String str, String separatorChars, int max, boolean preserveAllTokens) {
	        // Performance tuned for 2.0 (JDK1.4)
	        // Direct code is quicker than StringTokenizer.
	        // Also, StringTokenizer uses isSpace() not isWhitespace()

	        if (str == null) {
	            return null;
	        }
	        int len = str.length();
	        if (len == 0) {
	            return new String[0];
	        }
	        List<String> list = new ArrayList<String>();
	        int sizePlus1 = 1;
	        int i = 0, start = 0;
	        boolean match = false;
	        boolean lastMatch = false;
	        if (separatorChars == null) {
	            // Null separator means use whitespace
	            while (i < len) {
	                if (Character.isWhitespace(str.charAt(i))) {
	                    if (match || preserveAllTokens) {
	                        lastMatch = true;
	                        if (sizePlus1++ == max) {
	                            i = len;
	                            lastMatch = false;
	                        }
	                        list.add(str.substring(start, i));
	                        match = false;
	                    }
	                    start = ++i;
	                    continue;
	                }
	                lastMatch = false;
	                match = true;
	                i++;
	            }
	        } else if (separatorChars.length() == 1) {
	            // Optimise 1 character case
	            char sep = separatorChars.charAt(0);
	            while (i < len) {
	                if (str.charAt(i) == sep) {
	                    if (match || preserveAllTokens) {
	                        lastMatch = true;
	                        if (sizePlus1++ == max) {
	                            i = len;
	                            lastMatch = false;
	                        }
	                        list.add(str.substring(start, i));
	                        match = false;
	                    }
	                    start = ++i;
	                    continue;
	                }
	                lastMatch = false;
	                match = true;
	                i++;
	            }
	        } else {
	            // standard case
	            while (i < len) {
	                if (separatorChars.indexOf(str.charAt(i)) >= 0) {
	                    if (match || preserveAllTokens) {
	                        lastMatch = true;
	                        if (sizePlus1++ == max) {
	                            i = len;
	                            lastMatch = false;
	                        }
	                        list.add(str.substring(start, i));
	                        match = false;
	                    }
	                    start = ++i;
	                    continue;
	                }
	                lastMatch = false;
	                match = true;
	                i++;
	            }
	        }
	        if (match || (preserveAllTokens && lastMatch)) {
	            list.add(str.substring(start, i));
	        }
	        return (String[]) list.toArray(new String[list.size()]);
	    }
	}
	
	public class Objects {
		
		/**
		 * 必须非null
		 */
		public static <T> T requireNotNull(T obj, MessageCode mc) {
	        if (null == obj) {
	            throw new BizException(mc.getCode(), mc.getMessage());
	        }
	        return obj;
	    }
		
		/**
		 * 字符串必须非空
		 */
		public static String requireNotEmpty(String str, MessageCode mc) {
			if(null == str || 0 >= str.trim().length()) {
				throw new BizException(mc.getCode(), mc.getMessage());
			}
			return str;
		}
		
		/**
		 * 数值类型非空且大于0
		 */
		public static Number requireNotNullGtZero(Number number, MessageCode mc) {
	        if (null == number || 0 >= number.intValue()) {
	            throw new BizException(mc.getCode(), mc.getMessage());
	        }
	        return number;
	    }
		
		/**
		 * 必须为true
		 */
		public static void requireTrue(Boolean flag, MessageCode mc) {
	        if (null == flag || !flag) {
	            throw new BizException(mc.getCode(), mc.getMessage());
	        }
	    }
		
		/**
		 * 若null了，说点啥？
		 */
		public static void ifNull(Object obj, MessageCode mc) {
			if(null == obj) {
				throw new BizException(mc.getCode(), mc.getMessage());
			}
		}
		
		/**
		 * 若false了，说点啥？
		 */
		public static void ifFalse(Boolean bool, MessageCode mc) {
			if(!bool) {
				throw new BizException(mc.getCode(), mc.getMessage());
			}
		}
		
		public static void requireEquals(Object a, Object b, MessageCode mc) {
			requireTrue((a == b) || (a != null && a.equals(b)), mc);
		}
		
		public static void requireEquals(Object a, Object b, String message) {
			requireTrue((a == b) || (a != null && a.equals(b)), message);
		}
		
		/**
		 * 必须非null
		 */
		public static <T> T requireNotNull(T obj, String message) {
	        if (null == obj) {
	            throw new BizException(BasicMessage.PARAMS_OBJ_IS_NULL.getCode(), Strings.isBlank(message) ? BasicMessage.PARAMS_OBJ_IS_NULL.getMessage() : message);
	        }
	        return obj;
	    }
		
		/**
		 * 字符串必须非空
		 */
		public static String requireNotEmpty(String str, String message) {
			if(null == str || 0 >= str.trim().length()) {
				throw new BizException(BasicMessage.PARAMS_STRING_SHOULD_NOT_EMPTY.getCode(), Strings.isBlank(message) ? BasicMessage.PARAMS_STRING_SHOULD_NOT_EMPTY.getMessage() : message);
			}
			return str;
		}
		
		/**
		 * 数值类型非空且大于0
		 */
		public static Number requireNotNullGtZero(Number number, String message) {
	        if (null == number || new BigDecimal(0).compareTo(new BigDecimal(number.toString())) >= 0) {
	            throw new BizException(BasicMessage.PARAMS_NUMBER_SHOULD_POSITIVE.getCode(), Strings.isBlank(message) ? number + " : " + BasicMessage.PARAMS_NUMBER_SHOULD_POSITIVE.getMessage() : message);
	        }
	        return number;
	    }
		
		/**
		 * 数值number必须在指定的区间范围[min, max]
		 */
		public static Number requireRange(Number number, Number min, Number max, String message) {
			if(null == min || null == max) {
				throw new IllegalStateException(Strings.format("Invalid Data range => [{},{}]", min, max));
			}
	        if (null == number || new BigDecimal(number.toString()).compareTo(new BigDecimal(min.toString())) < 0 
	        		|| new BigDecimal(number.toString()).compareTo(new BigDecimal(max.toString())) > 0) {
	            throw new BizException(BasicMessage.PARAMS_NUMBER_SHOULD_IN_RANGE.getCode(), Strings.isBlank(message) ? Strings.format("{}@[{},{}]? : {}", number, min, max, BasicMessage.PARAMS_NUMBER_SHOULD_IN_RANGE.getMessage()) : message);
	        }
	        return number;
	    }
		
		/**
		 * 必须为true
		 */
		public static void requireTrue(Boolean flag, String message) {
	        if (null == flag || !flag) {
	            throw new BizException(BasicMessage.PARAMS_BOOL_SHOULD_TRUE.getCode(), message);
	        }
	    }
		
		public static <T> T matchAndReturn(boolean flag, T value) {
			if(flag) {
				return value;
			}
			
			return null;
		}
		
		public static <T> T matchOne(List<T> list, Matchable<T> matchable) {
			if(0 >= length(list)) {
				return null;
			}
			for (T t : list) {
				if(matchable.match(t)) {
					return t;
				}
			}
			
			return null;
		}
		
		public static <T> List<T> matchList(List<T> list, Matchable<T> matchable) {
			List<T> result = new ArrayList<T>();
			if(0 >= length(list)) {
				return result;
			}
			for (T t : list) {
				if(matchable.match(t)) {
					result.add(t);
				}
			}
			
			return result;
		}
		
		public static <T, P, R> List<R> decorate(List<T> list, P param, Decorator<T, P, R> decorator) {
			if(0 >= length(list)) {
				return new ArrayList<R>();
			}
			List<R> result = new ArrayList<R>();
			for (T t : list) {
				R r = decorator.execute(t, param);
				if(null != r) {
					result.add(r);
				}
			}
			
			return result;
		}
		
		/**
		 * Get the length of object, which accepts:
		 * <p>
		 * <ul>
		 * <li>null         : 0
		 * <li>Array        : Array.length
		 * <li>Collection   : Collection.size()
		 * <li>Map          : Map.size()
		 * <li>Pain Object  : 1
		 * </ul>
		 * 
		 * @param obj the target object
		 * @return the length of the object.
		 */
		public static int length(Object obj) {
			if (null == obj)
				return 0;
			if (obj.getClass().isArray()) {
				return Array.getLength(obj);
			} else if (obj instanceof Collection<?>) {
				return ((Collection<?>) obj).size();
			} else if (obj instanceof Map<?, ?>) {
				return ((Map<?, ?>) obj).size();
			}
			return 1;
		}
		
		/**
		 * Gets the first object of the target object.
		 * 
		 * @param obj the target source object
		 * @return the first object
		 */
		public static Object first(Object obj) {
			if (null == obj) {
				return null;
			}
			if (obj.getClass().isArray()) {
				if(0 == Array.getLength(obj)) {
					return null;
				}
				return Array.get(obj, 0);
			} else if (obj instanceof Collection<?>) {
				if(((Collection<?>) obj).isEmpty()) {
					return null;
				}
				return ((Collection<?>) obj).iterator().next();
			} else if (obj instanceof Map<?, ?>) {
				if(((Map<?, ?>) obj).isEmpty()) {
					return null;
				}
				return ((Map<?, ?>) obj).entrySet().iterator().next();
			}
			return obj;
		}
		
		/**
		 * Check the target source object is empty.
		 *
		 * @param source the target source object
		 * @return true if the target source object is null or empty
		 */
		public static boolean isEmpty(Object source) {
			return 0 == length(source);
		}
		
		public static Object[] toObjectArray(Object source) {
			if (source instanceof Object[]) {
				return (Object[]) source;
			}
			if (source == null) {
				return new Object[0];
			}
			if (!source.getClass().isArray()) {
				throw new IllegalArgumentException("Source is not an array: " + source);
			}
			int length = Array.getLength(source);
			if (length == 0) {
				return new Object[0];
			}
			Class<?> wrapperType = Array.get(source, 0).getClass();
			Object[] newArray = (Object[]) Array.newInstance(wrapperType, length);
			for (int i = 0; i < length; i++) {
				newArray[i] = Array.get(source, i);
			}
			return newArray;
		}
		
		public static Object[] addToArray(Object[] array, Object obj) {
			Class<?> compType = Object.class;
			if (array != null) {
				compType = array.getClass().getComponentType();
			}
			else if (obj != null) {
				compType = obj.getClass();
			}
			int newArrLength = (array != null ? array.length + 1 : 1);
			Object[] newArr = (Object[]) Array.newInstance(compType, newArrLength);
			if (array != null) {
				System.arraycopy(array, 0, newArr, 0, array.length);
			}
			newArr[newArr.length - 1] = obj;
			return newArr;
		}
		
		public static <T> List<T> asList(T ... array) {
			if(null == array || 0 >= array.length) {
				return new ArrayList<T>();
			}
			List<T> list = new ArrayList<T>();
			for (T t : array) {
				list.add(t);
			}
			return list;
		}
		
		public static boolean contains(Object[] array, Object element) {
			if (array == null) {
				return false;
			}
			for (int i = 0; i < array.length; i++) {
				if (java.util.Objects.deepEquals(array[i], element)) {
					return true;
				}
			}
			return false;
		}
		
		public static ClassLoader getDefaultClassLoader(Class<?> callingClass) {
			ClassLoader cl = null;
			try {
				cl = Thread.currentThread().getContextClassLoader();
			} catch (Throwable ex) {
				// Cannot access thread context ClassLoader - falling back to system class loader...
			}
			if (cl == null) {
				// No thread context class loader -> use class loader of this class.
				try {
					cl = Objects.class.getClassLoader();
				} catch (Throwable e) {
				}
			}
			if(cl == null && null != callingClass) {
				cl = callingClass.getClassLoader();
			}
			return cl;
		}
		
		/**
		 * 取得实体对象按字段名排序后的kv对
		 *
		 * @param object Java数据对象
		 * @return
		 */
		public static Map<String, Object> getSortedFields(Object object) {
			Map<String, Object> map = Maps.keysortMap(true);
			
	        Class<?> clazz = object.getClass() ;  
	        for(; clazz != Object.class ; clazz = clazz.getSuperclass()) {  
	            try {  
	                for (Field field : clazz.getDeclaredFields()) {
	                	field.setAccessible(true);
	                	map.put(field.getName(), field.get(object));
					}
	            } catch (Exception e) {
	                // ignore                  
	            }   
	        }  
	        
	        return map;
		}
	}
	
	public class Maps {
		
		public static <K> Map<K, K> asMap(List<K> list) {
			if(Objects.isEmpty(list)) {
				return new HashMap<K, K>();
			}
			
			Map<K, K> map = new HashMap<K, K>();
			for (K k : list) {
				map.put(k, k);
			}
			
			return map;
		}
		
		public static <K, V> List<K> keyList(Map<K, V> map) {
			if(Objects.isEmpty(map)) {
				return new ArrayList<K>();
			}
			List<K> list = new ArrayList<K>();
			for (Map.Entry<K, V> e : map.entrySet()) {
				list.add(e.getKey());
			}
			
			return list;
		}
		
		public static <K, V> List<V> valueList(Map<K, V> map) {
			if(Objects.isEmpty(map)) {
				return new ArrayList<V>();
			}
			List<V> list = new ArrayList<V>();
			for (Map.Entry<K, V> e : map.entrySet()) {
				list.add(e.getValue());
			}
			
			return list;
		}
		
		public static <K, V> Map<K, V> create(List<Entry<K, V>> entries) {
			Map<K, V> map = new HashMap<K, V>();
			for (Entry<K, V> entry : entries) {
				map.put(entry.getKey(), entry.getValue());
			}
			return map;
		}
		
		public static <K, V> Map<K, V> keysortMap(final boolean asc) {
			
			return new TreeMap<K, V>(new Comparator<K>() {
				
				private Collator collator = Collator.getInstance();

				public int compare(Object o1, Object o2) {
	                if (o1 == null || o2 == null) {
	                    return 0; // if one param is empty, return 0
	                }
					CollationKey key1 = collator.getCollationKey(o1.toString().toLowerCase());
					CollationKey key2 = collator.getCollationKey(o2.toString().toLowerCase());
					return (asc ? key1.compareTo(key2) : -key1.compareTo(key2));
				}
				
			});
		}
		
		public static Map<String, String> toHashMap(Object ... objs) {
			Map<String, String> map = new HashMap<String, String>();
			if(null == objs) {
				return map;
			}
			Objects.requireTrue(objs.length % 2 == 0, "The parameter objs array length must be even number.");
			for (int i = 0; i < objs.length; i++) {
				if(i % 2 == 0) {
					continue;
				}
				if(null == objs[i-1] || null == objs[i]) {
					continue;
				}
				map.put(objs[i-1].toString(), objs[i].toString());
			}
			return map;
		}
		
		public static Map<String, String> toTreeMap(Properties p) {
			Map<String, String> map = new TreeMap<String, String>();
			
			for (Enumeration<?> e = p.propertyNames(); e.hasMoreElements(); ) {
			      String key = (String) e.nextElement();
			      map.put(key, p.getProperty(key));
			}
			
			return map;
		}
	}
	
	/**
	 * Base64 字符串处理
	 */
	public final class Base64 {
		
	}
	
	/**
	 * MD5 加密处理
	 */
	public final class MD5 {
		/**
		 * 计算md5
		 */
		public static String getMd5(final String text) {
	        return md5(text.getBytes());
	    }
		
		/**
	     * Make MD5 diaguest.
	     */
	    public static String md5(byte[] data) {
	        try {
	            MessageDigest md = MessageDigest.getInstance("MD5");
	            byte[] buf = md.digest(data);
	            return toHexString(buf);
	        } catch (NoSuchAlgorithmException e) {
	            throw new RuntimeException("MD5 Algorithm not supported", e);
	        }
	    }
	    
	    public static String toHexString(byte[] bytes) {
	        int length = bytes.length;
	        StringBuffer sb = new StringBuffer(length * 2);
	        int x = 0;
	        int n1 = 0, n2 = 0;
	        for(int i=0; i<length; i++) {
	            if(bytes[i]>=0)
	                x = bytes[i];
	            else
	                x= 256 + bytes[i];
	            n1 = x >> 4;
	            n2 = x & 0x0f;
	            sb = sb.append(HEX[n1]);
	            sb = sb.append(HEX[n2]);
	        }
	        return sb.toString();
	    }

	    private static final char[] HEX = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
	}
	
	/**
	 * Hash算法
	 */
	public enum Hash {

		/**
		 * Native hash (String.hashCode()).
		 */
		NATIVE_HASH,
		/**
		 * CRC32_HASH as used by the perl API. This will be more consistent both
		 * across multiple API users as well as java versions, but is mostly likely
		 * significantly slower.
		 */
		CRC32_HASH,
		/**
		 * FNV hashes are designed to be fast while maintaining a low collision
		 * rate. The FNV speed allows one to quickly hash lots of data while
		 * maintaining a reasonable collision rate.
		 *
		 * @see <a href="http://www.isthe.com/chongo/tech/comp/fnv/">fnv comparisons</a>
		 * @see <a href="http://en.wikipedia.org/wiki/Fowler_Noll_Vo_hash">fnv at wikipedia</a>
		 */
		FNV1_64_HASH,
		/**
		 * Variation of FNV.
		 */
		FNV1A_64_HASH,
		/**
		 * 32-bit FNV1.
		 */
		FNV1_32_HASH,
		/**
		 * 32-bit FNV1a.
		 */
		FNV1A_32_HASH,
		/**
		 * MD5-based hash algorithm used by ketama.
		 */
		KETAMA_HASH;

		private static final long FNV_64_INIT = 0xcbf29ce484222325L;
		private static final long FNV_64_PRIME = 0x100000001b3L;

		private static final long FNV_32_INIT = 2166136261L;
		private static final long FNV_32_PRIME = 16777619;

		/**
		 * Compute the hash for the given key.
		 *
		 * @return a positive integer hash
		 */
		public long hash(final String k) {
			long rv = 0;
			switch (this) {
				case NATIVE_HASH:
					rv = k.hashCode();
					break;
				case CRC32_HASH:
					// return (crc32(shift) >> 16) & 0x7fff;
					CRC32 crc32 = new CRC32();
					crc32.update(Bytes.getBytes(k, "UTF-8"));
					rv = (crc32.getValue() >> 16) & 0x7fff;
					break;
				case FNV1_64_HASH: {
						// Thanks to pierre@demartines.com for the pointer
						rv = FNV_64_INIT;
						int len = k.length();
						for (int i = 0; i < len; i++) {
							rv *= FNV_64_PRIME;
							rv ^= k.charAt(i);
						}
					}
					break;
				case FNV1A_64_HASH: {
						rv = FNV_64_INIT;
						int len = k.length();
						for (int i = 0; i < len; i++) {
							rv ^= k.charAt(i);
							rv *= FNV_64_PRIME;
						}
					}
					break;
				case FNV1_32_HASH: {
						rv = FNV_32_INIT;
						int len = k.length();
						for (int i = 0; i < len; i++) {
							rv *= FNV_32_PRIME;
							rv ^= k.charAt(i);
						}
					}
					break;
				case FNV1A_32_HASH: {
						rv = FNV_32_INIT;
						int len = k.length();
						for (int i = 0; i < len; i++) {
							rv ^= k.charAt(i);
							rv *= FNV_32_PRIME;
						}
					}
					break;
				case KETAMA_HASH:
					byte[] bKey=computeMd5(k);
					rv = ((long) (bKey[3] & 0xFF) << 24)
							| ((long) (bKey[2] & 0xFF) << 16)
							| ((long) (bKey[1] & 0xFF) << 8)
							| (bKey[0] & 0xFF);
					break;
				default:
					assert false;
			}
			return rv & 0xffffffffL; /* Truncate to 32-bits */
		}

		/**
		 * Get the md5 of the given key.
		 */
		public static byte[] computeMd5(String k) {
			MessageDigest md5;
			try {
				md5 = MessageDigest.getInstance("MD5");
			} catch (NoSuchAlgorithmException e) {
				throw new RuntimeException("MD5 Algorithm not supported", e);
			}
			md5.reset();
			md5.update(Bytes.getBytes(k, "UTF-8"));
			return md5.digest();
		}
	}
	
	/**
	 * 数字处理
	 */
	public class Numbers {
		
		public static Integer parseInt(String value) {
			BigDecimal big = null;
			try {
				big = new BigDecimal(value);
				int val = big.intValue();
				if(new BigDecimal(val).compareTo(big) == 0) {
					return val;
				}
			} catch (Exception e) {
			}
			return null;
		}
		
		public static Long parseLong(String value) {
			BigDecimal big = null;
			try {
				big = new BigDecimal(value);
				long val = big.longValue();
				if(new BigDecimal(val).compareTo(big) == 0) {
					return val;
				}
			} catch (Exception e) {
			}
			return null;
		}
		
	}
	
	/**
	 * 数学计算
	 */
	public class Maths {
		/**
		 * 默认除法运算精度,即结果保留的小数位数 
		 */
		private static final int DEF_DIV_SCALE = 10;

		/**
		 * 提供精确的加法运算。
		 * 
		 * @param v1 被加数
		 * @param v2 加数
		 * @return 两个参数的和
		 */
		public static double add(double v1, double v2) {
			BigDecimal b1 = new BigDecimal(Double.toString(v1));
			BigDecimal b2 = new BigDecimal(Double.toString(v2));
			return b1.add(b2).doubleValue();
		}

		/**
		 * 提供精确的减法运算。
		 * 
		 * @param v1 被减数
		 * @param v2 减数
		 * @return 两个参数的差
		 */
		public static double sub(double v1, double v2) {
			BigDecimal b1 = new BigDecimal(Double.toString(v1));
			BigDecimal b2 = new BigDecimal(Double.toString(v2));
			return b1.subtract(b2).doubleValue();
		}

		/**
		 * 提供精确的乘法运算。
		 * 
		 * @param v1 被乘数
		 * @param v2 乘数
		 * @return 两个参数的积
		 */
		public static double mul(double v1, double v2) {
			BigDecimal b1 = new BigDecimal(Double.toString(v1));
			BigDecimal b2 = new BigDecimal(Double.toString(v2));
			return b1.multiply(b2).doubleValue();
		}

		/**
		 * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到 小数点以后10位，以后的数字四舍五入。
		 * 
		 * @param v1 被除数
		 * @param v2 除数
		 * @return 两个参数的商
		 */
		public static double div(double v1, double v2) {
			return div(v1, v2, DEF_DIV_SCALE);
		}

		/**
		 * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入。
		 * 
		 * @param v1 被除数
		 * @param v2 除数
		 * @param scale 表示表示需要精确到小数点以后几位。
		 * @return 两个参数的商
		 */
		public static double div(double v1, double v2, int scale) {
			if (scale < 0) {
				throw new IllegalArgumentException(
						"The scale must be a positive integer or zero");
			}
			BigDecimal b1 = new BigDecimal(Double.toString(v1));
			BigDecimal b2 = new BigDecimal(Double.toString(v2));
			return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
		}

		/**
		 * 提供精确的小数位四舍五入处理。
		 * 
		 * @param v 需要四舍五入的数字
		 * @param scale 小数点后保留几位
		 * @return 四舍五入后的结果
		 */
		public static double round(double v, int scale) {
			if (scale < 0) {
				scale = 0;
			}
			BigDecimal bd = new BigDecimal(Double.toString(v));
			BigDecimal one = new BigDecimal("1");
			return bd.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
		}
		
		/**
		 * 不使用科学计数法的返回格式
		 *
		 * @param value
		 * @param scale
		 * @return
		 */
		public static String roundNum(double value, int scale) {
			if (scale < 0) {
				scale = 0;
			}
			BigDecimal bd = new BigDecimal(Double.toString(value));
			BigDecimal one = new BigDecimal("1");
			double d = bd.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
			
			Double dd = new Double(d); 
			java.text.NumberFormat nf = java.text.NumberFormat.getInstance(); 
			nf.setGroupingUsed(false); // 位数较大时，不使用科学计数法

			return nf.format(dd);
		}

		/**
		 * 提供精确的类型转换(Float)
		 * 
		 * @param v 需要被转换的数字
		 * @return 返回转换结果
		 */
		public static float toFloat(double v) {
			BigDecimal b = new BigDecimal(v);
			return b.floatValue();
		}

		/**
		 * 提供精确的类型转换(Int)不进行四舍五入
		 * 
		 * @param v 需要被转换的数字
		 * @return 返回转换结果
		 */
		public static int toInt(double v) {
			BigDecimal b = new BigDecimal(v);
			return b.intValue();
		}

		/**
		 * 提供精确的类型转换(Long)
		 * 
		 * @param v 需要被转换的数字
		 * @return 返回转换结果
		 */
		public static long toLong(double v) {
			BigDecimal b = new BigDecimal(v);
			return b.longValue();
		}

		/**
		 * 返回两个数中大的一个值
		 * 
		 * @param v1 需要被对比的第一个数
		 * @param v2 需要被对比的第二个数
		 * @return 返回两个数中大的一个值
		 */
		public static double max(double v1, double v2) {
			BigDecimal b1 = new BigDecimal(v1);
			BigDecimal b2 = new BigDecimal(v2);
			return b1.max(b2).doubleValue();
		}

		/**
		 * 返回两个数中小的一个值
		 * 
		 * @param v1 需要被对比的第一个数
		 * @param v2 需要被对比的第二个数
		 * @return 返回两个数中小的一个值
		 */
		public static double min(double v1, double v2) {
			BigDecimal b1 = new BigDecimal(v1);
			BigDecimal b2 = new BigDecimal(v2);
			return b1.min(b2).doubleValue();
		}
		
		public static long min(long[] array) {
			// Validates input
			if (array == null) {
				throw new IllegalArgumentException("The Array must not be null");
			} else if (array.length == 0) {
				throw new IllegalArgumentException("Array cannot be empty.");
			}

			// Finds and returns min
			long min = array[0];
			for (int i = 1; i < array.length; i++) {
				if (array[i] < min) {
					min = array[i];
				}
			}

			return min;
		}

		public static int min(int[] array) {
			// Validates input
			if (array == null) {
				throw new IllegalArgumentException("The Array must not be null");
			} else if (array.length == 0) {
				throw new IllegalArgumentException("Array cannot be empty.");
			}

			// Finds and returns min
			int min = array[0];
			for (int j = 1; j < array.length; j++) {
				if (array[j] < min) {
					min = array[j];
				}
			}

			return min;
		}

		public static short min(short[] array) {
			// Validates input
			if (array == null) {
				throw new IllegalArgumentException("The Array must not be null");
			} else if (array.length == 0) {
				throw new IllegalArgumentException("Array cannot be empty.");
			}

			// Finds and returns min
			short min = array[0];
			for (int i = 1; i < array.length; i++) {
				if (array[i] < min) {
					min = array[i];
				}
			}

			return min;
		}

		public static byte min(byte[] array) {
			// Validates input
			if (array == null) {
				throw new IllegalArgumentException("The Array must not be null");
			} else if (array.length == 0) {
				throw new IllegalArgumentException("Array cannot be empty.");
			}

			// Finds and returns min
			byte min = array[0];
			for (int i = 1; i < array.length; i++) {
				if (array[i] < min) {
					min = array[i];
				}
			}

			return min;
		}

		public static double min(double[] array) {
			// Validates input
			if (array == null) {
				throw new IllegalArgumentException("The Array must not be null");
			} else if (array.length == 0) {
				throw new IllegalArgumentException("Array cannot be empty.");
			}

			// Finds and returns min
			double min = array[0];
			for (int i = 1; i < array.length; i++) {
				if (Double.isNaN(array[i])) {
					return Double.NaN;
				}
				if (array[i] < min) {
					min = array[i];
				}
			}

			return min;
		}

		public static float min(float[] array) {
			// Validates input
			if (array == null) {
				throw new IllegalArgumentException("The Array must not be null");
			} else if (array.length == 0) {
				throw new IllegalArgumentException("Array cannot be empty.");
			}

			// Finds and returns min
			float min = array[0];
			for (int i = 1; i < array.length; i++) {
				if (Float.isNaN(array[i])) {
					return Float.NaN;
				}
				if (array[i] < min) {
					min = array[i];
				}
			}

			return min;
		}

		public static long max(long[] array) {
			// Validates input
			if (array == null) {
				throw new IllegalArgumentException("The Array must not be null");
			} else if (array.length == 0) {
				throw new IllegalArgumentException("Array cannot be empty.");
			}

			// Finds and returns max
			long max = array[0];
			for (int j = 1; j < array.length; j++) {
				if (array[j] > max) {
					max = array[j];
				}
			}

			return max;
		}

		public static int max(int[] array) {
			// Validates input
			if (array == null) {
				throw new IllegalArgumentException("The Array must not be null");
			} else if (array.length == 0) {
				throw new IllegalArgumentException("Array cannot be empty.");
			}

			// Finds and returns max
			int max = array[0];
			for (int j = 1; j < array.length; j++) {
				if (array[j] > max) {
					max = array[j];
				}
			}

			return max;
		}

		public static short max(short[] array) {
			// Validates input
			if (array == null) {
				throw new IllegalArgumentException("The Array must not be null");
			} else if (array.length == 0) {
				throw new IllegalArgumentException("Array cannot be empty.");
			}

			// Finds and returns max
			short max = array[0];
			for (int i = 1; i < array.length; i++) {
				if (array[i] > max) {
					max = array[i];
				}
			}

			return max;
		}

		public static byte max(byte[] array) {
			// Validates input
			if (array == null) {
				throw new IllegalArgumentException("The Array must not be null");
			} else if (array.length == 0) {
				throw new IllegalArgumentException("Array cannot be empty.");
			}

			// Finds and returns max
			byte max = array[0];
			for (int i = 1; i < array.length; i++) {
				if (array[i] > max) {
					max = array[i];
				}
			}

			return max;
		}

		public static double max(double[] array) {
			// Validates input
			if (array == null) {
				throw new IllegalArgumentException("The Array must not be null");
			} else if (array.length == 0) {
				throw new IllegalArgumentException("Array cannot be empty.");
			}

			// Finds and returns max
			double max = array[0];
			for (int j = 1; j < array.length; j++) {
				if (Double.isNaN(array[j])) {
					return Double.NaN;
				}
				if (array[j] > max) {
					max = array[j];
				}
			}

			return max;
		}

		public static float max(float[] array) {
			// Validates input
			if (array == null) {
				throw new IllegalArgumentException("The Array must not be null");
			} else if (array.length == 0) {
				throw new IllegalArgumentException("Array cannot be empty.");
			}

			// Finds and returns max
			float max = array[0];
			for (int j = 1; j < array.length; j++) {
				if (Float.isNaN(array[j])) {
					return Float.NaN;
				}
				if (array[j] > max) {
					max = array[j];
				}
			}

			return max;
		}

		public static long min(long a, long b, long c) {
			if (b < a) {
				a = b;
			}
			if (c < a) {
				a = c;
			}
			return a;
		}

		public static int min(int a, int b, int c) {
			if (b < a) {
				a = b;
			}
			if (c < a) {
				a = c;
			}
			return a;
		}

		public static short min(short a, short b, short c) {
			if (b < a) {
				a = b;
			}
			if (c < a) {
				a = c;
			}
			return a;
		}

		public static byte min(byte a, byte b, byte c) {
			if (b < a) {
				a = b;
			}
			if (c < a) {
				a = c;
			}
			return a;
		}

		public static double min(double a, double b, double c) {
			return Maths.min(Maths.min(a, b), c);
		}

		public static float min(float a, float b, float c) {
			return Math.min(Math.min(a, b), c);
		}

		public static long max(long a, long b, long c) {
			if (b > a) {
				a = b;
			}
			if (c > a) {
				a = c;
			}
			return a;
		}

		public static int max(int a, int b, int c) {
			if (b > a) {
				a = b;
			}
			if (c > a) {
				a = c;
			}
			return a;
		}

		public static short max(short a, short b, short c) {
			if (b > a) {
				a = b;
			}
			if (c > a) {
				a = c;
			}
			return a;
		}

		public static byte max(byte a, byte b, byte c) {
			if (b > a) {
				a = b;
			}
			if (c > a) {
				a = c;
			}
			return a;
		}

		public static double max(double a, double b, double c) {
			return Maths.max(Maths.max(a, b), c);
		}

		public static float max(float a, float b, float c) {
			return Math.max(Math.max(a, b), c);
		}

		public static int compare(double lhs, double rhs) {
			if (lhs < rhs) {
				return -1;
			}
			if (lhs > rhs) {
				return +1;
			}
			// Need to compare bits to handle 0.0 == -0.0 being true
			// compare should put -0.0 < +0.0
			// Two NaNs are also == for compare purposes
			// where NaN == NaN is false
			long lhsBits = Double.doubleToLongBits(lhs);
			long rhsBits = Double.doubleToLongBits(rhs);
			if (lhsBits == rhsBits) {
				return 0;
			}
			// Something exotic! A comparison to NaN or 0.0 vs -0.0
			// Fortunately NaN's long is > than everything else
			// Also negzeros bits < poszero
			// NAN: 9221120237041090560
			// MAX: 9218868437227405311
			// NEGZERO: -9223372036854775808
			if (lhsBits < rhsBits) {
				return -1;
			} else {
				return +1;
			}
		}

		public static int compare(float lhs, float rhs) {
			if (lhs < rhs) {
				return -1;
			}
			if (lhs > rhs) {
				return +1;
			}
			// Need to compare bits to handle 0.0 == -0.0 being true
			// compare should put -0.0 < +0.0
			// Two NaNs are also == for compare purposes
			// where NaN == NaN is false
			int lhsBits = Float.floatToIntBits(lhs);
			int rhsBits = Float.floatToIntBits(rhs);
			if (lhsBits == rhsBits) {
				return 0;
			}
			// Something exotic! A comparison to NaN or 0.0 vs -0.0
			// Fortunately NaN's int is > than everything else
			// Also negzeros bits < poszero
			// NAN: 2143289344
			// MAX: 2139095039
			// NEGZERO: -2147483648
			if (lhsBits < rhsBits) {
				return -1;
			} else {
				return +1;
			}
		}
		
		public static int[] sort(int[] unsorted, boolean asc) {
	    		int temp = 0; boolean flag = false;
	        for (int i = 0; i < unsorted.length; i++) {
	            for (int j = i; j < unsorted.length; j++) {
	            	flag = asc ? (unsorted[i] > unsorted[j]) : (unsorted[i] < unsorted[j]);
	                if (flag) {
	                    temp = unsorted[i];
	                    unsorted[i] = unsorted[j];
	                    unsorted[j] = temp;
	                }
	            }
	        }
	        
	        return unsorted;
	    }
		
		/**
		 * 将十进制数转为2的N次相加
		 *
		 * @param val 十进制数
		 * @return
		 */
		public static List<Long> decompose(long val) {
			long chu = val;
			long yu = 0;
			int count = -1;
			List<Long> list = new ArrayList<Long>();
			while(chu > 0) {
				yu = chu % 2;
				chu = chu / 2;
				count++;
				if(yu == 1) {
					list.add(((Double)Math.pow(2, count)).longValue());
				}
			}
			
			return list;
		}
		
		/**
		 * 计算n以内的奇数和
		 *
		 * @param n n值
		 */
		public static long sumodd(int n) {
			if(n <= 0) return 0;
			long sum = 0; 
			int max = (n % 2 == 0) ? n - 1 : n;
			for (int i = 1; i <= max; i += 2) {
				sum += i;
			}
			return sum;
		}
		
		/**
		 * 计算n以内的偶数和
		 *
		 * @param n n值
		 */
		public static long sumeven(int n) {
			if(n <= 0) return 0;
			long sum = 0; 
			int max = (n % 2 == 0) ? n : n - 1;
			for (int i = 2; i <= max; i += 2) {
				sum += i;
			}
			return sum;
		}
		
		public static void main(String[] args) {
			
			Map<String, Integer> map = new TreeMap<String, Integer>();

			map.put("j2se", 20);
			map.put("j2ee", 10);
			map.put("j2me", 30);

			List<Map.Entry<String, Integer>> infoIds = new ArrayList<Map.Entry<String, Integer>>(map.entrySet());
			// 排序前
			for (int i = 0; i < infoIds.size(); i++) {
				String id = infoIds.get(i).toString();
				System.out.println(id);
			}
			System.out.println("\n===================\n");
			// 排序
			Collections.sort(infoIds, new Comparator<Map.Entry<String, Integer>>() {
				public int compare(Map.Entry<String, Integer> o1,
						Map.Entry<String, Integer> o2) {
					return (o2.getValue() - o1.getValue());
				}
			});
			// 排序后
			for (int i = 0; i < infoIds.size(); i++) {
				String id = infoIds.get(i).toString();
				System.out.println(id);
			}
			
			System.out.println(decompose(82));
			
			double d = 3.6;
			System.out.println(round(d, 0));
		}
	}
	
	/**
	 * 字节处理
	 */
	public class Bytes {

		private static final int MAX_LENGTH = 1024;

		public static byte[] object2ByteArray(Serializable obj) {
			ByteArrayOutputStream bos = null;
			ObjectOutputStream out = null;

			try {
				bos = new ByteArrayOutputStream();
				out = new ObjectOutputStream(bos);
				out.writeObject(obj);
				out.flush();

				byte[] results = bos.toByteArray();
				int srcLength = results.length;
				int length = srcLength;
				byte flags = 0x00;
				if (srcLength > MAX_LENGTH) {
					results = compress(results);
					length = results.length;
					flags = 0x01;
				}
				byte[] body = new byte[length + 5];
				body[0] = flags;
				System.arraycopy(toBytes(srcLength), 0, body, 1, 4);
				System.arraycopy(results, 0, body, 5, length);
				return body;
			} catch (IOException e) {
				throw new RuntimeException("serialize object failed.");
			} finally {
				IO.closeQuietly(out);
				IO.closeQuietly(bos);
			}
		}

		public static Serializable byteArray2Object(byte[] body) {
			if (null == body) {
				return null;
			}
			if (body.length < 5) {
				return null;
			}

			ByteArrayInputStream bis = null;
			ObjectInputStream in = null;
			try {
				int length = body.length - 5;
				byte flags = body[0];
				int c4 = body[4] & 0xff;
				int c3 = body[3] & 0xff;
				int c2 = body[2] & 0xff;
				int c1 = body[1] & 0xff;
				int size = ((c4 << 24) + (c3 << 16) + (c2 << 8) + (c1 << 0));
				byte[] results = new byte[length];
				System.arraycopy(body, 5, results, 0, length);
				if (flags == 0x01) {
					results = uncompress(results, size);
				}
				bis = new ByteArrayInputStream(results);
				in = new ObjectInputStream(bis);
				return (Serializable) in.readObject();
			} catch (IOException e) {
				throw new RuntimeException("deserialize object failed.");
			} catch (DataFormatException e) {
				throw new RuntimeException("serialize object failed.");
			} catch (ClassNotFoundException e) {
				throw new RuntimeException(
						"deserialize object failed. Class not found exception.");
			} finally {
				IO.closeQuietly(in);
				IO.closeQuietly(bis);
			}
		}
		
		public static int toInt( byte[] bytes ) {
			int result = 0;
			for (int i=0; i<4; i++) {
				result = ( result << 8 ) - Byte.MIN_VALUE + (int) bytes[i];
			}
			return result;
		}
		
		/**
		 * 将整型数据转换为字节数组
		 *
		 * @param i 整型数据
		 * @return
		 */
		public static byte[] toBytes(int i) {
			byte[] b = new byte[4];

			b[0] = (byte) (i & 0xff);
			b[1] = (byte) ((i & 0xff00) >> 8);
			b[2] = (byte) ((i & 0xff0000) >> 16);
			b[3] = (byte) ((i & 0xff000000) >> 24);
			return b;
		}
		
		public static byte[] getBytes(String str, String charset) {
			try {
				return str.getBytes(charset);
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException(e);
			}
		}

		private static byte[] compress(byte[] input) throws IOException {
			Deflater compressor = new Deflater();
			compressor.setLevel(Deflater.BEST_COMPRESSION);
			compressor.setInput(input);
			compressor.finish();
			ByteArrayOutputStream bos = new ByteArrayOutputStream(input.length);
			byte[] buf = new byte[8192];
			while (!compressor.finished()) {
				int count = compressor.deflate(buf);
				bos.write(buf, 0, count);
			}
			compressor.end();
			return bos.toByteArray();
		}

		private static byte[] uncompress(byte[] input, int uncompr_len)
				throws IOException, DataFormatException {
			Inflater decompressor = new Inflater();
			ByteArrayOutputStream bos = new ByteArrayOutputStream(uncompr_len);
			byte[] buf = new byte[uncompr_len];
			decompressor.setInput(input);
			while (!decompressor.finished()) {
				int count = decompressor.inflate(buf);
				if (count <= 0) {
					break;
				}
				bos.write(buf, 0, count);
			}
			decompressor.end();
			return bos.toByteArray();
		}

		public static byte[] toBytes(char[] chars) {
	        return toBytes(new String(chars), PREFERRED_ENCODING);
	    }

	    public static byte[] toBytes(char[] chars, String encoding) throws CodecException {
	        return toBytes(new String(chars), encoding);
	    }

	    /**
	     * Converts the specified source argument to a byte array with JSecurity's
	     * {@link CodecUtil#PREFERRED_ENCODING PREFERRED_ENCODING}.
	     *
	     * @param source the string to convert to a byte array.
	     * @return the bytes representing the specified string under JSecurity's {@link CodecUtil#PREFERRED_ENCODING PREFERRED_ENCODING}.
	     */
	    public static byte[] toBytes(String source) {
	        return toBytes(source, PREFERRED_ENCODING);
	    }

	    public static byte[] toBytes(String source, String encoding) throws CodecException {
	        try {
	            return source.getBytes(encoding);
	        } catch (UnsupportedEncodingException e) {
	            String msg = "Unable to convert source [" + source + "] to byte array using " +
	                    "encoding '" + encoding + "'";
	            throw new CodecException(msg, e);
	        }
	    }

	    public static String toString(byte[] bytes) {
	        return toString(bytes, PREFERRED_ENCODING);
	    }

	    public static String toString(byte[] bytes, String encoding) throws CodecException {
	        try {
	            return new String(bytes, encoding);
	        } catch (UnsupportedEncodingException e) {
	            String msg = "Unable to convert byte array to String with encoding '" + encoding + "'.";
	            throw new CodecException(msg, e);
	        }
	    }

	    public static char[] toChars(byte[] bytes) {
	        return toChars(bytes, PREFERRED_ENCODING);
	    }

	    public static char[] toChars(byte[] bytes, String encoding) throws CodecException {
	        return toString(bytes, encoding).toCharArray();
	    }

	    protected byte[] toBytes(Object o) {
	        if (o == null) {
	            String msg = "Argument for byte conversion cannot be null.";
	            throw new IllegalArgumentException(msg);
	        }
	        if (o instanceof byte[]) {
	            return (byte[]) o;
	        } else if (o instanceof char[]) {
	            return toBytes((char[]) o);
	        } else if (o instanceof String) {
	            return toBytes((String) o);
	        } else {
	            return objectToBytes(o);
	        }
	    }

	    protected String toString(Object o) {
	        if (o == null) {
	            String msg = "Argument for String conversion cannot be null.";
	            throw new IllegalArgumentException(msg);
	        }
	        if (o instanceof byte[]) {
	            return toString((byte[]) o);
	        } else if (o instanceof char[]) {
	            return new String((char[]) o);
	        } else if (o instanceof String) {
	            return (String) o;
	        } else {
	            return objectToString(o);
	        }
	    }

	    protected byte[] objectToBytes(Object o) {
	        String msg = "The " + getClass().getName() + " implementation only supports conversion to " +
	                "byte[] if the source is of type byte[], char[] or String.  The instance provided as a method " +
	                "argument is of type [" + o.getClass().getName() + "].  If you would like to convert " +
	                "this argument type to a byte[], you can 1) convert the argument to a byte[], char[] or String " +
	                "yourself and then use that as the method argument or 2) subclass " + getClass().getName() +
	                " and override the objectToBytes(Object o) method.";
	        throw new CodecException(msg);
	    }

	    protected String objectToString(Object o) {
	        return o.toString();
	    }

		public static void main(String[] args) {
			Map<String, String> params = new HashMap<String, String>();
			params.put("k1", "v1");
			params.put("k2", "v2");
			
			byte[] bytes = Bytes.object2ByteArray((Serializable) params);
			System.out.println(bytes.length);

			System.out.println(Bytes.byteArray2Object(bytes));
		}
	}
	
	/**
	 * 文件处理
	 */
	public class Files {
		
		public static File getClasspathFile(String classpath) {
			URL url = Objects.getDefaultClassLoader(Files.class).getResource(classpath);
			if (url == null) {
				throw new IllegalStateException(
							"Could not find classpath properties resource: "
									+ classpath);
			}
			if (url.getProtocol().equals("file") == false) {
				throw new IllegalArgumentException(
						"URL could not be converted to a File: " + url);
			}
			return toFile(url);
		}
		
		public static File toFile(URL url) {
			if (url == null || !url.getProtocol().equals("file")) {
				return null;
			} else {
				String filename = url.getFile().replace('/', File.separatorChar);
				int pos = 0;
				while ((pos = filename.indexOf('%', pos)) >= 0) {
					if (pos + 2 < filename.length()) {
						String hexStr = filename.substring(pos + 1, pos + 3);
						char ch = (char) Integer.parseInt(hexStr, 16);
						filename = filename.substring(0, pos) + ch
								+ filename.substring(pos + 3);
					}
				}
				return new File(filename);
			}
		}
	}
	
	/**
	 * IO/流处理
	 */
	public class IO {
		
		public static final int BUFFER_SIZE = 4096;
		
		public static byte[] copyToByteArray(InputStream in) throws IOException {
			ByteArrayOutputStream out = new ByteArrayOutputStream(BUFFER_SIZE);
			copy(in, out);
			return out.toByteArray();
		}
		
		public static int copy(InputStream in, OutputStream out) throws IOException {
			try {
				int byteCount = 0;
				byte[] buffer = new byte[BUFFER_SIZE];
				int bytesRead = -1;
				while ((bytesRead = in.read(buffer)) != -1) {
					out.write(buffer, 0, bytesRead);
					byteCount += bytesRead;
				}
				out.flush();
				return byteCount;
			} finally {
				closeQuietly(in);
				closeQuietly(out);
			}
		}
		
		public static void closeQuietly(InputStream in) {
			try {
				if(null != in) {
					in.close();
				}
			} catch (Exception e) {
			}
		}
		
		public static void closeQuietly(OutputStream out) {
			try {
				if(null != out) {
					out.close();
				}
			} catch (Exception e) {
			}
		}
		
		public static void closeQuietly(Connection con) {
			try {
				if(null != con) {
					con.close();
				}
			} catch (Exception e) {
			}
		}
		
		public static void closeQuietly(Reader reader) {
			try {
				if(null != reader) {
					reader.close();
				}
			} catch (Exception e) {
			}
		}
	}
	
	public class Web {
		
		/**
		 * URLEncoder 编码URL地址
		 */
		public static String encodeURL(String url, String charset) {
			if (url == null) {
				return null;
			}
			String retUrl = "";
			try {
				retUrl = URLEncoder.encode(url, charset);
			} catch (UnsupportedEncodingException e) {
				logger.severe("URLEncoder encodeURL error " + url);
			}
			return retUrl;
		}
		
		/**
		 * URLDecoder 解码URL地址
		 */
		public static String decodeURL(String url, String charset) {
			if (url == null) {
				return null;
			}
			String retUrl = "";
			
			try {
				retUrl = URLDecoder.decode(url, charset);
			} catch (UnsupportedEncodingException e) {
				logger.severe("URLDecoder decodeURL error " + url);
			}

			return retUrl;
		}
		
		/**
		 * 请求方法是否为某指定方法
		 */
		public static boolean isThisMethod(HttpServletRequest request, String method) {
			if (Strings.equalsIgnoreCase(request.getMethod(), method)) {
				return true;
			}
			return false;
		}

		/**
		 * 是否 GET 请求
		 */
		public static boolean isGet(HttpServletRequest request) {
			if ("GET".equalsIgnoreCase(request.getMethod())) {
				return true;
			}
			return false;
		}

		/**
		 * 是否 POST 请求
		 */
		public static boolean isPost(HttpServletRequest request) {
			if ("POST".equalsIgnoreCase(request.getMethod())) {
				return true;
			}
			return false;
		}
		
		/**
		 * 允许 JS 跨域设置
		 */
		public static void allowJsCrossDomain(HttpServletRequest request, HttpServletResponse response) {
			String origin = request.getHeader("Origin");
	        if (origin == null || origin.isEmpty()) {
	            origin = "*";
	        }
	        response.addHeader("Access-Control-Allow-Origin", origin);
			response.setHeader("Access-Control-Allow-Credentials", "true");
			response.setHeader("Access-Control-Allow-Methods", "GET, OPTIONS, POST, PUT, DELETE");
			response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
			response.setHeader("Access-Control-Max-Age", "3600");
		}
		
		/**
		 * 取请求客户端IP
		 */
		public static String getIpAddr(HttpServletRequest request) {
			String ip = request.getHeader("x-forwarded-for");
			if (Strings.isBlank(ip) || Strings.equalsIgnoreCase(Strings.UNKNOWN, ip)) {
				ip = request.getHeader("X-Forwarded-For");
			}
			if (Strings.isBlank(ip) || Strings.equalsIgnoreCase(Strings.UNKNOWN, ip)) {
				ip = request.getHeader("Proxy-Client-IP");
			}
			if (Strings.isBlank(ip) || Strings.equalsIgnoreCase(Strings.UNKNOWN, ip)) {
				ip = request.getHeader("WL-Proxy-Client-IP");
			}
			if (Strings.isBlank(ip) || Strings.equalsIgnoreCase(Strings.UNKNOWN, ip)) {
				ip = request.getRemoteAddr();
			}
			return ip;
		}
		
		public static boolean isAjaxRequest(HttpServletRequest request) {
			String header = request.getHeader("X-Requested-With");
			if(Strings.equalsIgnoreCase("XMLHttpRequest", header)) {
				return true;
			}
			header = request.getHeader("x-requested-with");
			if(Strings.equalsIgnoreCase("XMLHttpRequest", header)) {
				return true;
			}
			return false;
		}
		
		/**
		 * 取一级域名，如：.web.com | .baidu.com
		 */
		public static String getFirstLevelDomain(HttpServletRequest request) {
			String domain = request.getServerName();
	        int lastDot = domain.lastIndexOf('.');
	        if (lastDot == -1) {
	            return domain;
	        }
	        int secondDot = domain.lastIndexOf('.', (lastDot - 1));
	        if (secondDot == -1) {
	            return domain;
	        }
	        String firstDomain = domain.substring(secondDot);
	        return firstDomain;
	    }
		
		public static String getFullRequestURL(HttpServletRequest request) {
			StringBuilder sbf = new StringBuilder(request.getProtocol());
			sbf.append("://").append(request.getServerName()); // 服务器地址
			
			int port = request.getServerPort();
			if(port > 0 && port != 80) { // 80端口可省略
				sbf.append(":").append(port); // 端口号 
			}
			sbf.append(request.getContextPath()) // 项目名称
				.append(request.getServletPath()); // 请求页面或其他地址Path路径
			
			if(Strings.isNotBlank(request.getQueryString())) {
				sbf.append("?").append(request.getQueryString()); // // Query请求参数 
			}
			
			return sbf.toString();
		}

		/**
		 * 获取当前完整请求地址前缀，不包含path及后面的
		 * 
		 * 如：http://daily.idev.com:8081/msgcenter/timeout.html?zn=enlish#j2ee <br>
		 * 得到：http://daily.idev.com:8081
		 *
		 */
		public static String getRequestURLPre(HttpServletRequest request) {
			String urlTxt = "";
			try {
				URL url = new URL(request.getRequestURL().toString());
				urlTxt = url.getProtocol() + "://" + url.getAuthority();
			} catch (Exception e) {
				logger.severe("getRequestURLPre: " + e.getMessage());
			}
			return urlTxt;
		}
		
		/**
		 * 获取 Request Playload 内容
		 */
		public static String requestPlayload(HttpServletRequest request) throws IOException {
			StringBuilder stringBuilder = new StringBuilder();
			BufferedReader bufferedReader = null;
			try {
				InputStream inputStream = request.getInputStream();
				if (inputStream != null) {
					bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
					char[] charBuffer = new char[128];
					int bytesRead = -1;
					while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
						stringBuilder.append(charBuffer, 0, bytesRead);
					}
				} else {
					stringBuilder.append("");
				}
			} catch (IOException ex) {
				throw ex;
			} finally {
				IO.closeQuietly(bufferedReader);
			}
			return stringBuilder.toString();
		}

		static List<Pattern> xssPatterns = null;
	    static List<Object[]> getXssPatternList() {
	        List<Object[]> ret = new ArrayList<Object[]>();
	        ret.add(new Object[]{"<(no)?script[^>]*>.*?</(no)?script>", Pattern.CASE_INSENSITIVE});
	        ret.add(new Object[]{"eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL});
	        ret.add(new Object[]{"expression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL});
	        ret.add(new Object[]{"(javascript:|vbscript:|view-source:)*", Pattern.CASE_INSENSITIVE});
	        ret.add(new Object[]{"<(\"[^\"]*\"|\'[^\']*\'|[^\'\">])*>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL});
	        ret.add(new Object[]{"(window\\.location|window\\.|\\.location|document\\.cookie|document\\.|alert\\(.*?\\)|window\\.open\\()*", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL});
	        ret.add(new Object[]{"<+\\s*\\w*\\s*(oncontrolselect|oncopy|oncut|ondataavailable|ondatasetchanged|ondatasetcomplete|ondblclick|ondeactivate|ondrag|ondragend|ondragenter|ondragleave|ondragover|ondragstart|ondrop|onerror=|onerroupdate|onfilterchange|onfinish|onfocus|onfocusin|onfocusout|onhelp|onkeydown|onkeypress|onkeyup|onlayoutcomplete|onload|onlosecapture|onmousedown|onmouseenter|onmouseleave|onmousemove|onmousout|onmouseover|onmouseup|onmousewheel|onmove|onmoveend|onmovestart|onabort|onactivate|onafterprint|onafterupdate|onbefore|onbeforeactivate|onbeforecopy|onbeforecut|onbeforedeactivate|onbeforeeditocus|onbeforepaste|onbeforeprint|onbeforeunload|onbeforeupdate|onblur|onbounce|oncellchange|onchange|onclick|oncontextmenu|onpaste|onpropertychange|onreadystatechange|onreset|onresize|onresizend|onresizestart|onrowenter|onrowexit|onrowsdelete|onrowsinserted|onscroll|onselect|onselectionchange|onselectstart|onstart|onstop|onsubmit|onunload)+\\s*=+", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL});
	        return ret;
	    }
	    static List<Pattern> getPatterns() {

	        if (xssPatterns == null) {
	            List<Pattern> list = new ArrayList<Pattern>();

	            String regex = null;
	            Integer flag = null;
	            int arrLength = 0;

	            for(Object[] arr : getXssPatternList()) {
	                arrLength = arr.length;
	                for(int i = 0; i < arrLength; i++) {
	                    regex = (String)arr[0];
	                    flag = (Integer)arr[1];
	                    list.add(Pattern.compile(regex, flag));
	                }
	            }
	            xssPatterns = list;
	        }
	        return xssPatterns;
	    }

	    public static String stripXss(String value) {
	        if(Strings.isNotBlank(value)) {
	            Matcher matcher = null;
	            for(Pattern pattern : getPatterns()) {
	                matcher = pattern.matcher(value);
	                // 匹配
	                if(matcher.find()) {
	                    // 删除相关字符串
	                    value = matcher.replaceAll("");
	                }
	            }

	            value = value.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
	            value = value.replaceAll("\\(", "&#40;").replaceAll("\\)", "&#41;");
	            value = value.replaceAll("'", "&#39;");
	        }

	        return value;
	    }
	    
	    public static String stripSqlInjection(String value) {
	    		if(Strings.isNotBlank(value)) {
	    			return value.replaceAll("('.+--)|(--)|(\\|)|(%7C)", "");
	    		}
	    		return value;
	    }
		
		public static Map<String, String> getParamsFromUrl(String url) {
	        Map<String, String> map = null;
	        if (url != null && url.indexOf('?') != -1) {
	            map = splitUrlQuery(url.substring(url.indexOf('?') + 1));
	        }
	        if (map == null) {
	            map = new HashMap<String, String>();
	        }
	        return map;
	    }

	    /**
	     * 从URL中提取所有的参数
	     * 
	     * @param query URL地址
	     * @return 参数映射
	     */
	    public static Map<String, String> splitUrlQuery(String query) {
	        Map<String, String> result = new HashMap<String, String>();

	        String[] pairs = query.split("&");
	        if (pairs != null && pairs.length > 0) {
	            for (String pair : pairs) {
	                String[] param = pair.split("=", 2);
	                if (param != null && param.length == 2) {
	                    result.put(param[0], param[1]);
	                }
	            }
	        }

	        return result;
	    }
	    
	    public static void response(HttpServletResponse response, 
	    								Object data, 
	    								boolean json, 
	    								String callback, 
	    								String charset) throws IOException {
			if(null == data) {
				data = "{}";
			}
			response.setContentType("text/html;charset=" + (null == charset ? "UTF-8" : charset));
			if(json) {
				response.setContentType("application/json;charset=" + (null == charset ? "UTF-8" : charset));
			}
			String content = json ? JSON.toJSONString(data) : data.toString();
			if(Strings.isNotBlank(callback)) {
				content = "\n" + callback + "(" + content + ");";
			}
			PrintWriter out = response.getWriter();
			out.print(content);
			out.flush();
		}
		
	}
	
	final class MessageFormatter {

		static final char DELIM_START = '{';
		static final char DELIM_STOP = '}';
		static final String DELIM_STR = "{}";
		private static final char ESCAPE_CHAR = '\\';

		public static final String format(String messagePattern, Object arg) {
			return format(messagePattern, new Object[] { arg });
		}

		public static final String format(final String messagePattern, Object arg1, Object arg2) {
			return format(messagePattern, new Object[] { arg1, arg2 });
		}
		
		public final static String format(final String messagePattern, final Object ... argArray) {
			if (messagePattern == null) {
				return null;
			}
			if (argArray == null) {
				return messagePattern;
			}
			int i = 0;
			int j;
			StringBuffer sbuf = new StringBuffer(messagePattern.length() + 50);

			for (int L = 0; L < argArray.length; L++) {

				j = messagePattern.indexOf(DELIM_STR, i);

				if (j == -1) {
					// no more variables
					if (i == 0) { // this is a simple string
						return messagePattern;
					} else { // add the tail string which contains no variables and return
						// the result.
						sbuf.append(messagePattern.substring(i, messagePattern.length()));
						return sbuf.toString();
					}
				} else {
					if (isEscapedDelimeter(messagePattern, j)) {
						if (!isDoubleEscaped(messagePattern, j)) {
							L--; // DELIM_START was escaped, thus should not be incremented
							sbuf.append(messagePattern.substring(i, j - 1));
							sbuf.append(DELIM_START);
							i = j + 1;
						} else {
							// The escape character preceding the delimiter start is
							// itself escaped: "abc x:\\{}"
							// we have to consume one backward slash
							sbuf.append(messagePattern.substring(i, j - 1));
							deeplyAppendParameter(sbuf, argArray[L], new HashMap());
							i = j + 2;
						}
					} else {
						// normal case
						sbuf.append(messagePattern.substring(i, j));
						deeplyAppendParameter(sbuf, argArray[L], new HashMap());
						i = j + 2;
					}
				}
			}
			// append the characters following the last {} pair.
			sbuf.append(messagePattern.substring(i, messagePattern.length()));
			return sbuf.toString();
		}

		final static boolean isEscapedDelimeter(String messagePattern,
				int delimeterStartIndex) {

			if (delimeterStartIndex == 0) {
				return false;
			}
			char potentialEscape = messagePattern.charAt(delimeterStartIndex - 1);
			if (potentialEscape == ESCAPE_CHAR) {
				return true;
			} else {
				return false;
			}
		}

		final static boolean isDoubleEscaped(String messagePattern, int delimeterStartIndex) {
			if (delimeterStartIndex >= 2
					&& messagePattern.charAt(delimeterStartIndex - 2) == ESCAPE_CHAR) {
				return true;
			} else {
				return false;
			}
		}

		// special treatment of array values was suggested by 'lizongbo'
		private static void deeplyAppendParameter(StringBuffer sbuf, Object o, Map seenMap) {
			if (o == null) {
				sbuf.append("null");
				return;
			}
			if (!o.getClass().isArray()) {
				safeObjectAppend(sbuf, o);
			} else {
				// check for primitive array types because they
				// unfortunately cannot be cast to Object[]
				if (o instanceof boolean[]) {
					booleanArrayAppend(sbuf, (boolean[]) o);
				} else if (o instanceof byte[]) {
					byteArrayAppend(sbuf, (byte[]) o);
				} else if (o instanceof char[]) {
					charArrayAppend(sbuf, (char[]) o);
				} else if (o instanceof short[]) {
					shortArrayAppend(sbuf, (short[]) o);
				} else if (o instanceof int[]) {
					intArrayAppend(sbuf, (int[]) o);
				} else if (o instanceof long[]) {
					longArrayAppend(sbuf, (long[]) o);
				} else if (o instanceof float[]) {
					floatArrayAppend(sbuf, (float[]) o);
				} else if (o instanceof double[]) {
					doubleArrayAppend(sbuf, (double[]) o);
				} else {
					objectArrayAppend(sbuf, (Object[]) o, seenMap);
				}
			}
		}

		private static void safeObjectAppend(StringBuffer sbuf, Object o) {
			try {
				String oAsString = o.toString();
				sbuf.append(oAsString);
			} catch (Throwable t) {
				System.err
						.println("SLF4J: Failed toString() invocation on an object of type ["
								+ o.getClass().getName() + "]");
				t.printStackTrace();
				sbuf.append("[FAILED toString()]");
			}

		}

		private static void objectArrayAppend(StringBuffer sbuf, Object[] a, Map seenMap) {
			sbuf.append('[');
			if (!seenMap.containsKey(a)) {
				seenMap.put(a, null);
				final int len = a.length;
				for (int i = 0; i < len; i++) {
					deeplyAppendParameter(sbuf, a[i], seenMap);
					if (i != len - 1)
						sbuf.append(", ");
				}
				// allow repeats in siblings
				seenMap.remove(a);
			} else {
				sbuf.append("...");
			}
			sbuf.append(']');
		}

		private static void booleanArrayAppend(StringBuffer sbuf, boolean[] a) {
			sbuf.append('[');
			final int len = a.length;
			for (int i = 0; i < len; i++) {
				sbuf.append(a[i]);
				if (i != len - 1)
					sbuf.append(", ");
			}
			sbuf.append(']');
		}

		private static void byteArrayAppend(StringBuffer sbuf, byte[] a) {
			sbuf.append('[');
			final int len = a.length;
			for (int i = 0; i < len; i++) {
				sbuf.append(a[i]);
				if (i != len - 1)
					sbuf.append(", ");
			}
			sbuf.append(']');
		}

		private static void charArrayAppend(StringBuffer sbuf, char[] a) {
			sbuf.append('[');
			final int len = a.length;
			for (int i = 0; i < len; i++) {
				sbuf.append(a[i]);
				if (i != len - 1)
					sbuf.append(", ");
			}
			sbuf.append(']');
		}

		private static void shortArrayAppend(StringBuffer sbuf, short[] a) {
			sbuf.append('[');
			final int len = a.length;
			for (int i = 0; i < len; i++) {
				sbuf.append(a[i]);
				if (i != len - 1)
					sbuf.append(", ");
			}
			sbuf.append(']');
		}

		private static void intArrayAppend(StringBuffer sbuf, int[] a) {
			sbuf.append('[');
			final int len = a.length;
			for (int i = 0; i < len; i++) {
				sbuf.append(a[i]);
				if (i != len - 1)
					sbuf.append(", ");
			}
			sbuf.append(']');
		}

		private static void longArrayAppend(StringBuffer sbuf, long[] a) {
			sbuf.append('[');
			final int len = a.length;
			for (int i = 0; i < len; i++) {
				sbuf.append(a[i]);
				if (i != len - 1)
					sbuf.append(", ");
			}
			sbuf.append(']');
		}

		private static void floatArrayAppend(StringBuffer sbuf, float[] a) {
			sbuf.append('[');
			final int len = a.length;
			for (int i = 0; i < len; i++) {
				sbuf.append(a[i]);
				if (i != len - 1)
					sbuf.append(", ");
			}
			sbuf.append(']');
		}

		private static void doubleArrayAppend(StringBuffer sbuf, double[] a) {
			sbuf.append('[');
			final int len = a.length;
			for (int i = 0; i < len; i++) {
				sbuf.append(a[i]);
				if (i != len - 1)
					sbuf.append(", ");
			}
			sbuf.append(']');
		}

		public static void main(String[] args) {
			System.out.println(format("hello {}  jkfbds {}", new Object[] {"gerald", "中国"}));
		}

	}
	
	public interface MessageCode {
		
	    int getCode();
		String getMessage();
		
	}
	
	public enum BasicMessage implements MessageCode {
		
		/**
		 * 系统异常 
		 */
		FATAL(-999, "系统异常，请稍后再试"),
		
		/**
		 * 成功 
		 */
		DEFAULT_SUCCESS(1, "成功"),
		
		/**
		 * 入口参数数据有误
		 */
		PARAMS_ERROR(100, "入参数据格式错误"),
		
		/**
		 * 入参对象不可为空 
		 */
		PARAMS_OBJ_IS_NULL(101, "入参对象不可为空"),
		
		/**
		 * 数值对象不可为空且必须为正数
		 */
		PARAMS_NUMBER_SHOULD_POSITIVE(102, "数值对象不可为空且必须为正数"),
		
		/**
		 * 布尔参数数据必须为true 
		 */
		PARAMS_BOOL_SHOULD_TRUE(103, "布尔参数数据必须为true"),
		
		/**
		 * String参数数据不能为空
		 */
		PARAMS_STRING_SHOULD_NOT_EMPTY(104, "String参数数据不能为空"),
		
		/**
		 * 数值对象必须在指定区间范围内
		 */
		PARAMS_NUMBER_SHOULD_IN_RANGE(105, "数值对象必须在指定区间范围内"),
		
		/**
		 * 方法调用超出最大限制数 
		 */
		METHOD_CALL_IS_LIMITED(106, "方法调用超出最大限制数"),
		
		/**
		 * 数据不存在或已被删除
		 */
		NOT_FOUND(404, "数据不存在或已被删除"),
		
		// ----------- 会员系统 start -----------
		
		/**
		 * 未登录提示 
		 */
		UN_LOGINED(11011, "当前尚未登录或登录已失效，请重新登录！"),
		
		NO_USER_MATCHED(11012, "用户不存在！"),
		
		
		// ----------- 会员系统 end -----------
		
		
	    ;

		private static Map<Integer, String> mapValues;
		
		/**
		 * 唯一特征码标识
		 */
		private int 	code;
		
		/**
		 * 提示信息 
		 */
		private String 	message;
		
		private BasicMessage(int code, String message) {
			this.code = code;
			this.message = message;
		}

		public int getCode() {
			return code;
		}

		public String getMessage() {
			return message;
		}
		
		public static Map<Integer, String> map() {
			if(null == mapValues) {
				mapValues = new HashMap<Integer, String>();
				for (MessageCode mc : BasicMessage.values()) {
					mapValues.put(mc.getCode(), mc.getMessage());
				}
			}
			
			return mapValues;
		}
	}
	
}
