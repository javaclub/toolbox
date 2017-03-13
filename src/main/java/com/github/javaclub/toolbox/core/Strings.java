/*
 * @(#)Strings.java	2011-7-9
 *
 * Copyright (c) 2011. All Rights Reserved.
 *
 */

package com.github.javaclub.toolbox.core;

import java.io.UnsupportedEncodingException;
import java.lang.Character.UnicodeBlock;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.javaclub.toolbox.util.ObjectUtil;

/**
 * Strings utility.
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: Strings.java 260 2011-08-26 07:37:31Z gerald.chen.hz $
 */
public abstract class Strings {

	private static final String CHARS = "AcdeopqrsSTUVWCD4abXYZ01IJKLlm2B3fghijk678EFGHnt5MNOPQR9uvwxyz";
	
	private static final String EMPTY_STRING = "";
	private static final Random RANDOM = new Random();
	private static final int INDEX_NOT_FOUND = -1;
	
	public static final String TOP_PATH = "..";
	public static final String CURRENT_PATH = ".";
	public static final char EXTENSION_SEPARATOR = '.';
	
	public static final String FOLDER_SEPARATOR = "/";
	public static final String WINDOWS_FOLDER_SEPARATOR = "\\";
	
	/** the char value of comma ',' */
	private static final char DEFAULT_DELIMITER_CHAR = ',';

	/** the char value of double quotation marks '"' */
	private static final char DOUBLE_QUOTE_CHAR = '"';

	/** the char value of single quotation marks ''' */
	private static final char SINGLE_QUOTE_CHAR = '\'';

	/**
	 * 字符串是否为<code>null</code>或为<code>""</code>
	 *
	 * @param input 输入
	 * @return true如果为空或null,否则false
	 */
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
	
	/**
	 * 产生固定长度的随机字符串
	 *
	 * @param length 长度
	 * @return 随机字符串
	 */
	public static String fixed(int length) {
		return random(length, CHARS);
	}
	
	/**
	 * 产生长度随机(不超过固定长度max,至少为1)的随机字符串
	 *
	 * @param max 最大长度
	 * @return 长度随机(不超过固定长度max)的随机字符串
	 */
	public static String random(int max) {
		return random(1, max);
	}
	
	/**
	 * 产生长度随机(不超过固定长度max,至少为min)的随机字符串
	 *
	 * @param min 最小长度
	 * @param max 最大长度
	 * @return 随机字符串
	 */
	public static String random(int min, int max) {
		int count = Numbers.random(min, max);
		return fixed(count);
	}
	
	public static String random(int count, String chars) {
		if (chars == null) {
			return random(count, 0, 0, false, false, null, RANDOM);
		}
		return random(count, 0, chars.length(), false, false, chars
				.toCharArray(), RANDOM);
	}
	
	public static String lowerCase(String str) {
        if (str == null) {
            return null;
        }
        return str.toLowerCase();
    }
    
    public static String upperCase(String str) {
        if (str == null) {
            return null;
        }
        return str.toUpperCase();
    }
    
    public static boolean equals(String str1, String str2) {
		if(str1 == str2) {
			return true;
		}
		return str1 == null ? str2 == null : str1.equals(str2);
	}
    
    /**
     * <p>Splits the provided text into an array, using whitespace as the
     * separator.
     * Whitespace is defined by {@link Character#isWhitespace(char)}.</p>
     *
     * <p>The separator is not included in the returned String array.
     * Adjacent separators are treated as one separator.
     * For more control over the split use the StrTokenizer class.</p>
     *
     * <p>A <code>null</code> input String returns <code>null</code>.</p>
     *
     * <pre>
     * StringUtils.split(null)       = null
     * StringUtils.split("")         = []
     * StringUtils.split("abc def")  = ["abc", "def"]
     * StringUtils.split("abc  def") = ["abc", "def"]
     * StringUtils.split(" abc ")    = ["abc"]
     * </pre>
     *
     * @param str  the String to parse, may be null
     * @return an array of parsed Strings, <code>null</code> if null String input
     */
    public static String[] split(String str) {
        return split(str, null, -1);
    }

    /**
     * <p>Splits the provided text into an array, separator specified.
     * This is an alternative to using StringTokenizer.</p>
     *
     * <p>The separator is not included in the returned String array.
     * Adjacent separators are treated as one separator.
     * For more control over the split use the StrTokenizer class.</p>
     *
     * <p>A <code>null</code> input String returns <code>null</code>.</p>
     *
     * <pre>
     * StringUtils.split(null, *)         = null
     * StringUtils.split("", *)           = []
     * StringUtils.split("a.b.c", '.')    = ["a", "b", "c"]
     * StringUtils.split("a..b.c", '.')   = ["a", "b", "c"]
     * StringUtils.split("a:b:c", '.')    = ["a:b:c"]
     * StringUtils.split("a b c", ' ')    = ["a", "b", "c"]
     * </pre>
     *
     * @param str  the String to parse, may be null
     * @param separatorChar  the character used as the delimiter
     * @return an array of parsed Strings, <code>null</code> if null String input
     * @since 2.0
     */
    public static String[] split(String str, char separatorChar) {
        return splitWorker(str, separatorChar, false);
    }

    /**
     * <p>Splits the provided text into an array, separators specified.
     * This is an alternative to using StringTokenizer.</p>
     *
     * <p>The separator is not included in the returned String array.
     * Adjacent separators are treated as one separator.
     * For more control over the split use the StrTokenizer class.</p>
     *
     * <p>A <code>null</code> input String returns <code>null</code>.
     * A <code>null</code> separatorChars splits on whitespace.</p>
     *
     * <pre>
     * StringUtils.split(null, *)         = null
     * StringUtils.split("", *)           = []
     * StringUtils.split("abc def", null) = ["abc", "def"]
     * StringUtils.split("abc def", " ")  = ["abc", "def"]
     * StringUtils.split("abc  def", " ") = ["abc", "def"]
     * StringUtils.split("ab:cd:ef", ":") = ["ab", "cd", "ef"]
     * </pre>
     *
     * @param str  the String to parse, may be null
     * @param separatorChars  the characters used as the delimiters,
     *  <code>null</code> splits on whitespace
     * @return an array of parsed Strings, <code>null</code> if null String input
     */
    public static String[] split(String str, String separatorChars) {
        return splitWorker(str, separatorChars, -1, false);
    }

    /**
     * <p>Splits the provided text into an array with a maximum length,
     * separators specified.</p>
     *
     * <p>The separator is not included in the returned String array.
     * Adjacent separators are treated as one separator.</p>
     *
     * <p>A <code>null</code> input String returns <code>null</code>.
     * A <code>null</code> separatorChars splits on whitespace.</p>
     *
     * <p>If more than <code>max</code> delimited substrings are found, the last
     * returned string includes all characters after the first <code>max - 1</code>
     * returned strings (including separator characters).</p>
     *
     * <pre>
     * StringUtils.split(null, *, *)            = null
     * StringUtils.split("", *, *)              = []
     * StringUtils.split("ab de fg", null, 0)   = ["ab", "cd", "ef"]
     * StringUtils.split("ab   de fg", null, 0) = ["ab", "cd", "ef"]
     * StringUtils.split("ab:cd:ef", ":", 0)    = ["ab", "cd", "ef"]
     * StringUtils.split("ab:cd:ef", ":", 2)    = ["ab", "cd:ef"]
     * </pre>
     *
     * @param str  the String to parse, may be null
     * @param separatorChars  the characters used as the delimiters,
     *  <code>null</code> splits on whitespace
     * @param max  the maximum number of elements to include in the
     *  array. A zero or negative value implies no limit
     * @return an array of parsed Strings, <code>null</code> if null String input
     */
    public static String[] split(String str, String separatorChars, int max) {
        return splitWorker(str, separatorChars, max, false);
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
    
    public static boolean equalsIgnoreCase(String str1, String str2) {
        return str1 == null ? str2 == null : str1.equalsIgnoreCase(str2);
    }
    
	public static String substringBefore(String str, String separator) {
        if (isEmpty(str) || separator == null) {
            return str;
        }
        if (separator.length() == 0) {
            return EMPTY_STRING;
        }
        int pos = str.indexOf(separator);
        if (pos == INDEX_NOT_FOUND) {
            return str;
        }
        return str.substring(0, pos);
    }
	
	public static String substringAfter(String str, String separator) {
        if (isEmpty(str)) {
            return str;
        }
        if (separator == null) {
            return EMPTY_STRING;
        }
        int pos = str.indexOf(separator);
        if (pos == INDEX_NOT_FOUND) {
            return EMPTY_STRING;
        }
        return str.substring(pos + separator.length());
    }
	
	/**
	 * Count the occurrences of the substring in string s.
	 * @param str string to search in. Return 0 if this is null.
	 * @param sub string to search for. Return 0 if this is null.
	 */
	public static int count(String str, String sub) {
		if (str == null || sub == null || str.length() == 0 || sub.length() == 0) {
			return 0;
		}
		int count = 0, pos = 0, idx = 0;
		while ((idx = str.indexOf(sub, pos)) != -1) {
			++count;
			pos = idx + sub.length();
		}
		return count;
	}
	
	/**
     * 把非空的字符串都连接起来返回
     * 
     * @param array 待连接的字符串
     * @return
     */
    public static String concat(String... array) {
    	if(null == array || 0 >= array.length) {
    		return "";
    	}
    	StringBuilder sbf = new StringBuilder();
    	for (String string : array) {
    		sbf.append(null == string ? "" : string);
		}
    	
    	return sbf.toString();
    }
    
    /**
	 * if the target string endswith suffix,return target string;
	 * <p>
	 * otherwise append the suffix to the target string and return it.
	 * 
	 * @param target the target string
	 * @param suffix a suffixal string
	 * @return a string endswith suffix
	 */
	public static String addTail(String target, String suffix) {
		if (target.endsWith(suffix) || isEmpty(suffix)) {
			return target;
		}
		return concat(target, suffix);
	}
	
	/**
	 * 给字符串添加前缀
	 *
	 * @param target	目标字符串
	 * @param prefix	前缀
	 * @return
	 */
	public static String addHead(String target, String prefix) {
		if(target.startsWith(prefix) || isEmpty(prefix)) {
			return target;
		}
		return concat(prefix, target);
	}
	
	/**
	 * 给字符串添加前缀和后缀
	 *
	 * @param target 目标字符串
	 * @param prefix 前缀
	 * @param suffix 后缀
	 * @return
	 */
	public static String addHeadAndTail(String target, String prefix, String suffix) {
		String result = target;
		if(!isEmpty(prefix)) {
			result = prefix + result;
		}
		if(!isEmpty(suffix)) {
			result = result + suffix;
		}
		return result;
	}
    
    /**
	 * 取字符串text的前max字符
	 *
	 * @param text 要处理的文本内容
	 * @param max  最多显示的字符数
	 * @param fill 是否填充'...'
	 * @return
	 */
	public static String left(String text, int max, boolean fill) {
		if (text.length() <= max)
			return text;
		return fill ? text.substring(0, max - 3) + "..." : text.substring(0, max - 3);
	}
	
	/**
	 * 查找字符串数组中是否存在值val，并返回所在数组下标
	 * 
	 * @param 	array			字符串数组
	 * @param 	val				要查找的字符串
	 * @param 	ignoreCase 		是否忽略大小写
	 * @return 	存在时val的索引值，不存在为-1
	 */
	public static int indexOf(String array[], String val, boolean ignoreCase) {
		for (int i = 0; i < array.length; i++) {
			if (ignoreCase) {
				if (array[i].equalsIgnoreCase(val)) {
					return i;
				}
			} else {
				if (array[i].equals(val)) {
					return i;
				}
			}
		}
		
		return INDEX_NOT_FOUND;
	}
	
	/**
	 * Delete any character in a given String.
	 * 
	 * @param inString the original String
	 * @param charsToDelete a set of characters to delete. E.g. "az\n" will delete 'a's, 'z's and new lines.
	 * @return the resulting String
	 */
	public static String deleteAny(String inString, String charsToDelete) {
		if (!hasLength(inString) || !hasLength(charsToDelete)) {
			return inString;
		}
		StringBuffer out = new StringBuffer();
		for (int i = 0; i < inString.length(); i++) {
			char c = inString.charAt(i);
			if (charsToDelete.indexOf(c) == -1) {
				out.append(c);
			}
		}
		return out.toString();
	}
	
	/**
	 * Find the first matched string by the specified RegExp.
	 *
	 * @param source the input source string
	 * @param regexp a RegExp
	 * @return the first matched string, if not found <code>null</code>
	 */
	public static String find(String source,String regexp){
        if(source == null || regexp == null ){
            return null;
        }
        Matcher matcher = Pattern.compile(regexp).matcher(source);
        if(matcher.find()){
            return matcher.group();
        }
        return null;
    }
	
	/**
	 * Find all the matched string by the specified RegExp.
	 *
	 * @param source the input source string
	 * @param regexp a RegExp
	 * @return all matched string, if not found <code>null</code>
	 */
	public static String[] multiFind(String source, String regexp) {
		if (source == null || regexp == null) {
			return null;
		}
		Matcher matcher = Pattern.compile(regexp.trim()).matcher(source);
		StringBuilder sb = new StringBuilder();
		while (matcher.find()) {
			sb.append(matcher.group() + ",");
		}
		String result = sb.toString();
		if (!isEmpty(result)) {
			return split(result.substring(0, result.lastIndexOf(",")), ",");
		}
		return null;
	}
	
	/**
	 * Delete all occurrences of the given substring.
	 * 
	 * @param input the original String
	 * @param toDelStr the String to delete all occurrences of
	 * @return the resulting String
	 */
	public static String delete(String input, String toDelStr) {
		return replace(input, toDelStr, "");
	}
	
	/**
	 * Replace all occurences of a substring within a string with another string.
	 * 
	 * @param inString String to examine
	 * @param oldPattern String to replace
	 * @param newPattern String to insert
	 * @return a String with the replacements
	 */
	public static String replace(String inString, String oldPattern, String newPattern) {
		if (!hasLength(inString) || !hasLength(oldPattern) || newPattern == null) {
			return inString;
		}
		StringBuffer sbuf = new StringBuffer();
		// output StringBuffer we'll build up
		int pos = 0; // our position in the old string
		int index = inString.indexOf(oldPattern);
		// the index of an occurrence we've found, or -1
		int patLen = oldPattern.length();
		while (index >= 0) {
			sbuf.append(inString.substring(pos, index));
			sbuf.append(newPattern);
			pos = index + patLen;
			index = inString.indexOf(oldPattern, pos);
		}
		sbuf.append(inString.substring(pos));
		// remember to append any characters to the right of a match
		return sbuf.toString();
	}
	
	/**
	 * Replaces all instances of oldString with newString in line with the added feature that matches of newString in oldString ignore
	 * case.
	 * 
	 * @param line the String to search to perform replacements on
	 * @param oldString the String that should be replaced by newString
	 * @param newString the String that will replace all instances of oldString
	 * 
	 * @return a String will all instances of oldString replaced by newString
	 */
	public static final String replaceIgnoreCase(String line, String oldString,
			String newString) {
		if (line == null) {
			return null;
		}
		String lcLine = line.toLowerCase();
		String lcOldString = oldString.toLowerCase();
		int i = 0;
		if ((i = lcLine.indexOf(lcOldString, i)) >= 0) {
			char[] line2 = line.toCharArray();
			char[] newString2 = newString.toCharArray();
			int oLength = oldString.length();
			StringBuffer buf = new StringBuffer(line2.length);
			buf.append(line2, 0, i).append(newString2);
			i += oLength;
			int j = i;
			while ((i = lcLine.indexOf(lcOldString, i)) > 0) {
				buf.append(line2, j, i - j).append(newString2);
				i += oLength;
				j = i;
			}
			buf.append(line2, j, line2.length - j);
			return buf.toString();
		}
		return line;
	}
	
	/**
	 * Normalize the path by suppressing sequences like "path/.." and inner simple dots.
	 * <p>
	 * The result is convenient for path comparison. For other uses, notice that Windows separators ("\") are replaced by simple slashes.
	 * 
	 * @param path the original path
	 * @return the normalized path
	 */
	public static String cleanPath(String path) {
		if (path == null) {
			return null;
		}
		String pathToUse = replace(path, WINDOWS_FOLDER_SEPARATOR, FOLDER_SEPARATOR);

		// Strip prefix from path to analyze, to not treat it as part of the
		// first path element. This is necessary to correctly parse paths like
		// "file:core/../core/io/Resource.class", where the ".." should just
		// strip the first "core" directory while keeping the "file:" prefix.
		int prefixIndex = pathToUse.indexOf(":");
		String prefix = "";
		if (prefixIndex != -1) {
			prefix = pathToUse.substring(0, prefixIndex + 1);
			pathToUse = pathToUse.substring(prefixIndex + 1);
		}
		if (pathToUse.startsWith(FOLDER_SEPARATOR)) {
			prefix = prefix + FOLDER_SEPARATOR;
			pathToUse = pathToUse.substring(1);
		}

		String[] pathArray = delimitedListToStringArray(pathToUse, FOLDER_SEPARATOR);
		List pathElements = new LinkedList();
		int tops = 0;

		for (int i = pathArray.length - 1; i >= 0; i--) {
			String element = pathArray[i];
			if (CURRENT_PATH.equals(element)) {
				// Points to current directory - drop it.
			} else if (TOP_PATH.equals(element)) {
				// Registering top path found.
				tops++;
			} else {
				if (tops > 0) {
					// Merging path element with element corresponding to top path.
					tops--;
				} else {
					// Normal path element found.
					pathElements.add(0, element);
				}
			}
		}

		// Remaining top paths need to be retained.
		for (int i = 0; i < tops; i++) {
			pathElements.add(0, TOP_PATH);
		}

		return prefix + collectionToDelimitedString(pathElements, FOLDER_SEPARATOR);
	}
	
	/**
	 * Convenience method to return a Collection as a delimited (e.g. CSV) String. E.g. useful for <code>toString()</code>
	 * implementations.
	 * 
	 * @param coll the Collection to display
	 * @param delim the delimiter to use (probably a ",")
	 * @return the delimited String
	 */
	public static String collectionToDelimitedString(Collection coll, String delim) {
		return collectionToDelimitedString(coll, delim, "", "");
	}

	/**
	 * Convenience method to return a Collection as a delimited (e.g. CSV) String. E.g. useful for <code>toString()</code>
	 * implementations.
	 * 
	 * @param coll the Collection to display
	 * @param delim the delimiter to use (probably a ",")
	 * @param prefix the String to start each element with
	 * @param suffix the String to end each element with
	 * @return the delimited String
	 */
	public static String collectionToDelimitedString(Collection coll, String delim,
			String prefix, String suffix) {
		if (ObjectUtil.isEmpty(coll)) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		Iterator it = coll.iterator();
		while (it.hasNext()) {
			sb.append(prefix).append(it.next()).append(suffix);
			if (it.hasNext()) {
				sb.append(delim);
			}
		}
		return sb.toString();
	}

	/**
	 * Take a String which is a delimited list and convert it to a String array.
	 * <p>
	 * A single delimiter can consists of more than one character: It will still be considered as single delimiter string, rather than as
	 * bunch of potential delimiter characters - in contrast to <code>tokenizeToStringArray</code>.
	 * 
	 * @param str the input String
	 * @param delimiter the delimiter between elements (this is a single delimiter, rather than a bunch individual delimiter characters)
	 * @return an array of the tokens in the list
	 * @see #tokenizeToStringArray
	 */
	public static String[] delimitedListToStringArray(String str, String delimiter) {
		return delimitedListToStringArray(str, delimiter, null);
	}

	/**
	 * Take a String which is a delimited list and convert it to a String array.
	 * <p>
	 * A single delimiter can consists of more than one character: It will still be considered as single delimiter string, rather than as
	 * bunch of potential delimiter characters - in contrast to <code>tokenizeToStringArray</code>.
	 * 
	 * @param str the input String
	 * @param delimiter the delimiter between elements (this is a single delimiter, rather than a bunch individual delimiter characters)
	 * @param charsToDelete a set of characters to delete. Useful for deleting unwanted line breaks: e.g. "\r\n\f" will delete all new
	 *            lines and line feeds in a String.
	 * @return an array of the tokens in the list
	 * @see #tokenizeToStringArray
	 */
	public static String[] delimitedListToStringArray(String str, String delimiter,
			String charsToDelete) {
		if (str == null) {
			return new String[0];
		}
		if (delimiter == null) {
			return new String[] { str };
		}
		List result = new ArrayList();
		if ("".equals(delimiter)) {
			for (int i = 0; i < str.length(); i++) {
				result.add(deleteAny(str.substring(i, i + 1), charsToDelete));
			}
		} else {
			int pos = 0;
			int delPos = 0;
			while ((delPos = str.indexOf(delimiter, pos)) != -1) {
				result.add(deleteAny(str.substring(pos, delPos), charsToDelete));
				pos = delPos + delimiter.length();
			}
			if (str.length() > 0 && pos <= str.length()) {
				// Add rest of String, but not in case of empty input.
				result.add(deleteAny(str.substring(pos), charsToDelete));
			}
		}
		return toArray(result);
	}
	
	/**
	 * Remove duplicate Strings from the given array. Also sorts the array, as it uses a TreeSet.
	 * 
	 * @param array the String array
	 * @return an array without duplicates, in natural sort order
	 */
	public static String[] removeDuplicateStrings(String[] array) {
		if (ObjectUtil.isEmpty(array)) {
			return array;
		}
		Set set = new TreeSet();
		for (int i = 0; i < array.length; i++) {
			set.add(array[i]);
		}
		return toArray(set);
	}

	

	/**
	 * expand string to the specified length filling with the specified character
	 * 
	 * @param input string to expand
	 * @param length length of the string after expanded
	 * @param fillChar character to fill when expanding
	 * @param fillOnLeft true, to fill the characters on left side; false, on right side
	 * @return Returns the expanded string
	 */
	public static String expand(String input, int length, char fillChar, boolean fillOnLeft) {
		int nLen = input.length();
		if (length <= nLen)
			return input; // already exceed the length
		// required

		// else, to expand string
		String sRet = input;
		for (int i = 0; i < length - nLen; i++) {
			sRet = (fillOnLeft ? fillChar + sRet : sRet + fillChar); // fill
			// charachter
		}
		return sRet;
	}

	/**
	 * parse String like "&#33298;&#36866;&#26580;&#36719;&#27454;&#32431;&#26825;&#30701;&#34966;T&#24676; &#968;&#8594;" to a regular
	 * String
	 * 
	 * @param input string with the unicode format
	 * @return regular string
	 */
	public static String unicode2char(String input) {
		String result = input;
		if (result == null || result.length() == 0)
			return result;
		Pattern p = Pattern.compile("&#([0-9]+);");
		Matcher matcher = p.matcher(result);
		int end = 0;
		while (matcher.find(end)) {
			String sValue = matcher.group();
			String sNumber = sValue.substring(2, sValue.length() - 1);
			result = result.replace(sValue, "" + (char) Integer.parseInt(sNumber));
			end = matcher.end();
		}
		return result;
	}
	
	public static String iso2utf(String input) {
		return convert(input, "ISO-8859-1", "UTF-8");
	}
	
	public static String utf2iso(String input) {
		return convert(input, "UTF-8", "ISO-8859-1");
	}
	
	public static String convert(String input, String charsetFrom, String charsetTo) {
		String result = input;
		try {
			result = new String(input.getBytes(charsetFrom), charsetTo);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("errors occured while converting string[" + input + "] from "
					+ charsetFrom + " to " + charsetTo, e);
		}
		return result;
	}
	
	/**
	 * 将指定的数转为固定位数的十六进制串
	 *
	 * @param number 目标输入
	 * @param totalCount 固定位数
	 * @return
	 */
	public static String toRegularHex(long number, int totalCount) {
        return toRegularFormat(number, totalCount, '0', 16);
	}
	
	/**
	 * 将指定的数转为固定位数的十六进制串
	 *
	 * @param number 目标输入
	 * @param totalCount 固定位数
	 * @return
	 */
	public static String toRegularHex(int number, int totalCount) {
		long num = Long.valueOf(number + "").longValue();
		return toRegularFormat(num, totalCount, '0', 16);
	}
	
	public static String toRegularFormat(long number, int totalCount, char dupChar, int radix) {
		String formattedHex = Long.toString(number, radix);
		int formattedHexLenth = formattedHex.length();
		if(formattedHexLenth > totalCount){
			throw new RuntimeException("The converted format string's length is larger than the parameter count -> " + totalCount);
		}
		StringBuffer buf = new StringBuffer(dup(dupChar, totalCount));
        buf.replace(totalCount - formattedHex.length(), totalCount, formattedHex);
        return buf.toString();
	}
	
	/**
	 * 返回内容为num个字符c的字符串
	 * 
	 * @param c 字符
	 * @param num 数量
	 * @return 新字符串
	 */
	public static String dup(char c, int num) {
		if (c == 0) {
			return "";
		}
		StringBuilder sb = new StringBuilder(num);
		for (int i = 0; i < num; i++) {
			sb.append(c);
		}
		return sb.toString();
	}
	
	/**
	 * 将形如call_telephone_record的串转换为驼峰式的Java类名
	 *
	 * @param input  形如call_telephone_record的串
	 * @return
	 */
	public static String getJavaClassnameOrMethodname(String input) {
		if(isEmpty(input)) {
			throw new IllegalArgumentException("the parameter input mustn't be empty.");
		}
		input = input.toLowerCase();
		String[] arrFilename = input.split("_");
		input = "";
		for (int i = 0; i < arrFilename.length; i++) {
			input += arrFilename[i].substring(0, 1).toUpperCase() + arrFilename[i].substring(1, arrFilename[i].length());
		}
		return input;
	}
	
	/**
	 * native character 2 unicode ascill code
	 *
	 * @param text
	 * @return
	 */
	public static String native2ascii(String text){
		if(text == null) return null ;
		
		char[] buffer = text.toCharArray() ;
        StringBuffer sb = new StringBuffer() ;
        
        for (int i = 0; i < buffer.length; i++) {
        	char c = buffer[i] ;
            Character.UnicodeBlock ub = UnicodeBlock.of(c) ;
            
            if(ub == UnicodeBlock.BASIC_LATIN){
                //英文及数字等
                sb.append(c) ;
            }else{
                //汉字
                String hexS = Integer.toHexString(c & 0xffff) ;
                sb.append("\\u").append(hexS.toLowerCase()) ;
            }
        }
        return sb.toString() ;
	}
	
	/**
     * <p>Searches a String for substrings delimited by a start and end tag,
     * returning all matching substrings in an array.</p>
     *
     * <p>A <code>null</code> input String returns <code>null</code>.
     * A <code>null</code> open/close returns <code>null</code> (no match).
     * An empty ("") open/close returns <code>null</code> (no match).</p>
     *
     * <pre>
     * StringUtils.substringsBetween("[a][b][c]", "[", "]") = ["a","b","c"]
     * StringUtils.substringsBetween(null, *, *)            = null
     * StringUtils.substringsBetween(*, null, *)            = null
     * StringUtils.substringsBetween(*, *, null)            = null
     * StringUtils.substringsBetween("", "[", "]")          = []
     * </pre>
     *
     * @param str  the String containing the substrings, null returns null, empty returns empty
     * @param open  the String identifying the start of the substring, empty returns null
     * @param close  the String identifying the end of the substring, empty returns null
     * @return a String Array of substrings, or <code>null</code> if no match
     * @since 2.3
     */
    public static String[] substringsBetween(String str, String open, String close) {
        if (str == null || isEmpty(open) || isEmpty(close)) {
            return null;
        }
        int strLen = str.length();
        if (strLen == 0) {
            return new String[] {};
        }
        int closeLen = close.length();
        int openLen = open.length();
        List list = new ArrayList();
        int pos = 0;
        while (pos < (strLen - closeLen)) {
            int start = str.indexOf(open, pos);
            if (start < 0) {
                break;
            }
            start += openLen;
            int end = str.indexOf(close, start);
            if (end < 0) {
                break;
            }
            list.add(str.substring(start, end));
            pos = end + closeLen;
        }
        if (list.isEmpty()) {
            return null;
        } 
        return (String[]) list.toArray(new String [list.size()]);
    }
	
	/**
	 * Copy the given Collection into a String array. The Collection must contain String elements only.
	 * 
	 * @param collection the Collection to copy
	 * @return the String array (<code>null</code> if the passed-in Collection was <code>null</code>)
	 */
	public static String[] toArray(Collection<String> collection) {
		if (collection == null) {
			return null;
		}
		return (String[]) collection.toArray(new String[collection.size()]);
	}
	
	/**
	 * 用双引号引住字符串
	 *
	 * @param input 
	 * @return
	 */
	public static String dblQuote(String input) {
		if(isEmpty(input)) {
			return EMPTY_STRING;
		}
		return "\"" + input + "\"";
	}
	
	/**
	 * @param cs 字符串
	 * @param lc 左字符
	 * @param rc 右字符
	 * @return 字符串是被左字符和右字符包裹
	 */
	public static boolean isQuoteBy(CharSequence cs, char lc, char rc) {
		if (null == cs) return false;
		int length = cs.length();
		if (length < 2)
			return false;
		if (cs.charAt(0) != lc)
			return false;
		if (cs.charAt(length - 1) != rc)
			return false;
		return true;
	}
	
	/**
	 * 在字符串左侧填充一定数量的特殊字符
	 * 
	 * @param cs 字符串
	 * @param width 字符数量
	 * @param c 字符
	 * @return 新字符串
	 */
	public static String alignRight(CharSequence cs, int width, char c) {
		if (null == cs) return null;
		int len = cs.length();
		if (len >= width)
			return cs.toString();
		StringBuilder sb = new StringBuilder();
		sb.append(dup(c, width - len));
		sb.append(cs);
		return sb.toString();
	}

	/**
	 * 在字符串右侧填充一定数量的特殊字符
	 * 
	 * @param cs 字符串
	 * @param width 字符数量
	 * @param c 字符
	 * @return 新字符串
	 */
	public static String alignLeft(CharSequence cs, int width, char c) {
		if (null == cs) return null;
		int length = cs.length();
		if (length >= width)
			return cs.toString();
		StringBuilder sb = new StringBuilder();
		sb.append(cs);
		sb.append(dup(c, width - length));
		return sb.toString();
	}
    
	/**
	 * 过滤掉message中的HTML标记
	 * 
	 * @param text
	 * @return 过滤掉HTML标记后的字符串。如果text为null，返回空串。
	 */
	public static final String escapeHTML(String text) {
		if (text == null || text.length() == 0) {
			return EMPTY_STRING;
		}

		char[] content = new char[text.length()];
		text.getChars(0, text.length(), content, 0);
		StringBuffer result = new StringBuffer(content.length + 50);
		for (int i = 0; i < content.length; i++) {
			switch (content[i]) {
				case '<':
					result.append("&lt;");
					break;
				case '>':
					result.append("&gt;");
					break;
				case '&':
					result.append("&amp;");
					break;
				case '"':
					result.append("&quot;");
					break;
				default:
					result.append(content[i]);
			}
		}
		
		return (result.toString());
	}
	
    /**
     * Performs the logic for the <code>split</code> and 
     * <code>splitPreserveAllTokens</code> methods that return a maximum array 
     * length.
     *
     * @param str  the String to parse, may be <code>null</code>
     * @param separatorChars the separate character
     * @param max  the maximum number of elements to include in the
     *  array. A zero or negative value implies no limit.
     * @param preserveAllTokens if <code>true</code>, adjacent separators are
     * treated as empty token separators; if <code>false</code>, adjacent
     * separators are treated as one separator.
     * @return an array of parsed Strings, <code>null</code> if null String input
     */
	private static String[] splitWorker(String str, String separatorChars, int max, boolean preserveAllTokens) {
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
        List list = new ArrayList();
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
    
    /**
     * Performs the logic for the <code>split</code> and 
     * <code>splitPreserveAllTokens</code> methods that do not return a
     * maximum array length.
     *
     * @param str  the String to parse, may be <code>null</code>
     * @param separatorChar the separate character
     * @param preserveAllTokens if <code>true</code>, adjacent separators are
     * treated as empty token separators; if <code>false</code>, adjacent
     * separators are treated as one separator.
     * @return an array of parsed Strings, <code>null</code> if null String input
     */
	private static String[] splitWorker(String str, char separatorChar, boolean preserveAllTokens) {
        // Performance tuned for 2.0 (JDK1.4)

        if (str == null) {
            return null;
        }
        int len = str.length();
        if (len == 0) {
            return new String[0];
        }
        List list = new ArrayList();
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
	
	
	/**
     * <p>Creates a random string based on a variety of options, using
     * supplied source of randomness.</p>
     *
     * <p>If start and end are both <code>0</code>, start and end are set
     * to <code>' '</code> and <code>'z'</code>, the ASCII printable
     * characters, will be used, unless letters and numbers are both
     * <code>false</code>, in which case, start and end are set to
     * <code>0</code> and <code>Integer.MAX_VALUE</code>.
     *
     * <p>If set is not <code>null</code>, characters between start and
     * end are chosen.</p>
     *
     * <p>This method accepts a user-supplied {@link Random}
     * instance to use as a source of randomness. By seeding a single 
     * {@link Random} instance with a fixed seed and using it for each call,
     * the same random sequence of strings can be generated repeatedly
     * and predictably.</p>
     *
     * @param count  the length of random string to create
     * @param start  the position in set of chars to start at
     * @param end  the position in set of chars to end before
     * @param letters  only allow letters?
     * @param numbers  only allow numbers?
     * @param chars  the set of chars to choose randoms from.
     *  If <code>null</code>, then it will use the set of all chars.
     * @param random  a source of randomness.
     * @return the random string
     * @throws ArrayIndexOutOfBoundsException if there are not
     *  <code>(end - start) + 1</code> characters in the set array.
     * @throws IllegalArgumentException if <code>count</code> &lt; 0.
     * @since 2.0
     */
    private static String random(int count, int start, int end, boolean letters, boolean numbers,
                                char[] chars, Random random) {
        if (count == 0) {
            return "";
        } else if (count < 0) {
            throw new IllegalArgumentException("Requested random string length " + count + " is less than 0.");
        }
        if ((start == 0) && (end == 0)) {
            end = 'z' + 1;
            start = ' ';
            if (!letters && !numbers) {
                start = 0;
                end = Integer.MAX_VALUE;
            }
        }

        char[] buffer = new char[count];
        int gap = end - start;

        while (count-- != 0) {
            char ch;
            if (chars == null) {
                ch = (char) (random.nextInt(gap) + start);
            } else {
                ch = chars[random.nextInt(gap) + start];
            }
            if ((letters && Character.isLetter(ch))
                || (numbers && Character.isDigit(ch))
                || (!letters && !numbers)) 
            {
                if(ch >= 56320 && ch <= 57343) {
                    if(count == 0) {
                        count++;
                    } else {
                        // low surrogate, insert high surrogate after putting it in
                        buffer[count] = ch;
                        count--;
                        buffer[count] = (char) (55296 + random.nextInt(128));
                    }
                } else if(ch >= 55296 && ch <= 56191) {
                    if(count == 0) {
                        count++;
                    } else {
                        // high surrogate, insert low surrogate before putting it in
                        buffer[count] = (char) (56320 + random.nextInt(128));
                        count--;
                        buffer[count] = ch;
                    }
                } else if(ch >= 56192 && ch <= 56319) {
                    // private high surrogate, no effing clue, so skip it
                    count++;
                } else {
                    buffer[count] = ch;
                }
            } else {
                count++;
            }
        }
        return new String(buffer);
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
    
	public static void main(String[] args) {
		for(int i = 0; i < 20; i++) {
			System.out.println(fixed(8));
		}
		System.out.println("=========================");
		for(int i = 0; i < 20; i++) {
			System.out.println(random(10));
		}
		System.out.println("=========================");
		for(int i = 0; i < 20; i++) {
			System.out.println(random(3, 10));
		}
	}
}
