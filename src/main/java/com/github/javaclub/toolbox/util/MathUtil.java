/*
 * @(#)MathUtil.java	2011-6-27
 *
 * Copyright (c) 2011. All Rights Reserved.
 *
 */

package com.github.javaclub.toolbox.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * desc
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: MathUtil.java 131 2011-07-19 10:54:36Z gerald.chen.hz@gmail.com $
 */
public abstract class MathUtil {
	
	/** 默认除法运算精度,即结果保留的小数位数 */
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
	
	/**
	 * <p>
	 * Returns the minimum value in an array.
	 * </p>
	 * 
	 * @param array an array, must not be null or empty
	 * @return the minimum value in the array
	 * @throws IllegalArgumentException if <code>array</code> is <code>null</code>
	 * @throws IllegalArgumentException if <code>array</code> is empty
	 */
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

	/**
	 * <p>
	 * Returns the minimum value in an array.
	 * </p>
	 * 
	 * @param array an array, must not be null or empty
	 * @return the minimum value in the array
	 * @throws IllegalArgumentException if <code>array</code> is <code>null</code>
	 * @throws IllegalArgumentException if <code>array</code> is empty
	 */
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

	/**
	 * <p>
	 * Returns the minimum value in an array.
	 * </p>
	 * 
	 * @param array an array, must not be null or empty
	 * @return the minimum value in the array
	 * @throws IllegalArgumentException if <code>array</code> is <code>null</code>
	 * @throws IllegalArgumentException if <code>array</code> is empty
	 */
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

	/**
	 * <p>
	 * Returns the minimum value in an array.
	 * </p>
	 * 
	 * @param array an array, must not be null or empty
	 * @return the minimum value in the array
	 * @throws IllegalArgumentException if <code>array</code> is <code>null</code>
	 * @throws IllegalArgumentException if <code>array</code> is empty
	 */
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

	/**
	 * <p>
	 * Returns the minimum value in an array.
	 * </p>
	 * 
	 * @param array an array, must not be null or empty
	 * @return the minimum value in the array
	 * @throws IllegalArgumentException if <code>array</code> is <code>null</code>
	 * @throws IllegalArgumentException if <code>array</code> is empty
	 * @see IEEE754rUtils#min(double[]) IEEE754rUtils for a version of this method that handles NaN differently
	 */
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

	/**
	 * <p>
	 * Returns the minimum value in an array.
	 * </p>
	 * 
	 * @param array an array, must not be null or empty
	 * @return the minimum value in the array
	 * @throws IllegalArgumentException if <code>array</code> is <code>null</code>
	 * @throws IllegalArgumentException if <code>array</code> is empty
	 * @see IEEE754rUtils#min(float[]) IEEE754rUtils for a version of this method that handles NaN differently
	 */
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

	// Max in array
	// --------------------------------------------------------------------
	/**
	 * <p>
	 * Returns the maximum value in an array.
	 * </p>
	 * 
	 * @param array an array, must not be null or empty
	 * @return the minimum value in the array
	 * @throws IllegalArgumentException if <code>array</code> is <code>null</code>
	 * @throws IllegalArgumentException if <code>array</code> is empty
	 */
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

	/**
	 * <p>
	 * Returns the maximum value in an array.
	 * </p>
	 * 
	 * @param array an array, must not be null or empty
	 * @return the minimum value in the array
	 * @throws IllegalArgumentException if <code>array</code> is <code>null</code>
	 * @throws IllegalArgumentException if <code>array</code> is empty
	 */
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

	/**
	 * <p>
	 * Returns the maximum value in an array.
	 * </p>
	 * 
	 * @param array an array, must not be null or empty
	 * @return the minimum value in the array
	 * @throws IllegalArgumentException if <code>array</code> is <code>null</code>
	 * @throws IllegalArgumentException if <code>array</code> is empty
	 */
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

	/**
	 * <p>
	 * Returns the maximum value in an array.
	 * </p>
	 * 
	 * @param array an array, must not be null or empty
	 * @return the minimum value in the array
	 * @throws IllegalArgumentException if <code>array</code> is <code>null</code>
	 * @throws IllegalArgumentException if <code>array</code> is empty
	 */
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

	/**
	 * <p>
	 * Returns the maximum value in an array.
	 * </p>
	 * 
	 * @param array an array, must not be null or empty
	 * @return the minimum value in the array
	 * @throws IllegalArgumentException if <code>array</code> is <code>null</code>
	 * @throws IllegalArgumentException if <code>array</code> is empty
	 * @see IEEE754rUtils#max(double[]) IEEE754rUtils for a version of this method that handles NaN differently
	 */
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

	/**
	 * <p>
	 * Returns the maximum value in an array.
	 * </p>
	 * 
	 * @param array an array, must not be null or empty
	 * @return the minimum value in the array
	 * @throws IllegalArgumentException if <code>array</code> is <code>null</code>
	 * @throws IllegalArgumentException if <code>array</code> is empty
	 * @see IEEE754rUtils#max(float[]) IEEE754rUtils for a version of this method that handles NaN differently
	 */
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

	// 3 param min
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Gets the minimum of three <code>long</code> values.
	 * </p>
	 * 
	 * @param a value 1
	 * @param b value 2
	 * @param c value 3
	 * @return the smallest of the values
	 */
	public static long min(long a, long b, long c) {
		if (b < a) {
			a = b;
		}
		if (c < a) {
			a = c;
		}
		return a;
	}

	/**
	 * <p>
	 * Gets the minimum of three <code>int</code> values.
	 * </p>
	 * 
	 * @param a value 1
	 * @param b value 2
	 * @param c value 3
	 * @return the smallest of the values
	 */
	public static int min(int a, int b, int c) {
		if (b < a) {
			a = b;
		}
		if (c < a) {
			a = c;
		}
		return a;
	}

	/**
	 * <p>
	 * Gets the minimum of three <code>short</code> values.
	 * </p>
	 * 
	 * @param a value 1
	 * @param b value 2
	 * @param c value 3
	 * @return the smallest of the values
	 */
	public static short min(short a, short b, short c) {
		if (b < a) {
			a = b;
		}
		if (c < a) {
			a = c;
		}
		return a;
	}

	/**
	 * <p>
	 * Gets the minimum of three <code>byte</code> values.
	 * </p>
	 * 
	 * @param a value 1
	 * @param b value 2
	 * @param c value 3
	 * @return the smallest of the values
	 */
	public static byte min(byte a, byte b, byte c) {
		if (b < a) {
			a = b;
		}
		if (c < a) {
			a = c;
		}
		return a;
	}

	/**
	 * <p>
	 * Gets the minimum of three <code>double</code> values.
	 * </p>
	 * 
	 * <p>
	 * If any value is <code>NaN</code>, <code>NaN</code> is returned. Infinity is handled.
	 * </p>
	 * 
	 * @param a value 1
	 * @param b value 2
	 * @param c value 3
	 * @return the smallest of the values
	 * @see IEEE754rUtils#min(double, double, double) for a version of this method that handles NaN differently
	 */
	public static double min(double a, double b, double c) {
		return Math.min(Math.min(a, b), c);
	}

	/**
	 * <p>
	 * Gets the minimum of three <code>float</code> values.
	 * </p>
	 * 
	 * <p>
	 * If any value is <code>NaN</code>, <code>NaN</code> is returned. Infinity is handled.
	 * </p>
	 * 
	 * @param a value 1
	 * @param b value 2
	 * @param c value 3
	 * @return the smallest of the values
	 * @see IEEE754rUtils#min(float, float, float) for a version of this method that handles NaN differently
	 */
	public static float min(float a, float b, float c) {
		return Math.min(Math.min(a, b), c);
	}

	// 3 param max
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Gets the maximum of three <code>long</code> values.
	 * </p>
	 * 
	 * @param a value 1
	 * @param b value 2
	 * @param c value 3
	 * @return the largest of the values
	 */
	public static long max(long a, long b, long c) {
		if (b > a) {
			a = b;
		}
		if (c > a) {
			a = c;
		}
		return a;
	}

	/**
	 * <p>
	 * Gets the maximum of three <code>int</code> values.
	 * </p>
	 * 
	 * @param a value 1
	 * @param b value 2
	 * @param c value 3
	 * @return the largest of the values
	 */
	public static int max(int a, int b, int c) {
		if (b > a) {
			a = b;
		}
		if (c > a) {
			a = c;
		}
		return a;
	}

	/**
	 * <p>
	 * Gets the maximum of three <code>short</code> values.
	 * </p>
	 * 
	 * @param a value 1
	 * @param b value 2
	 * @param c value 3
	 * @return the largest of the values
	 */
	public static short max(short a, short b, short c) {
		if (b > a) {
			a = b;
		}
		if (c > a) {
			a = c;
		}
		return a;
	}

	/**
	 * <p>
	 * Gets the maximum of three <code>byte</code> values.
	 * </p>
	 * 
	 * @param a value 1
	 * @param b value 2
	 * @param c value 3
	 * @return the largest of the values
	 */
	public static byte max(byte a, byte b, byte c) {
		if (b > a) {
			a = b;
		}
		if (c > a) {
			a = c;
		}
		return a;
	}

	/**
	 * <p>
	 * Gets the maximum of three <code>double</code> values.
	 * </p>
	 * 
	 * <p>
	 * If any value is <code>NaN</code>, <code>NaN</code> is returned. Infinity is handled.
	 * </p>
	 * 
	 * @param a value 1
	 * @param b value 2
	 * @param c value 3
	 * @return the largest of the values
	 * @see IEEE754rUtils#max(double, double, double) for a version of this method that handles NaN differently
	 */
	public static double max(double a, double b, double c) {
		return Math.max(Math.max(a, b), c);
	}

	/**
	 * <p>
	 * Gets the maximum of three <code>float</code> values.
	 * </p>
	 * 
	 * <p>
	 * If any value is <code>NaN</code>, <code>NaN</code> is returned. Infinity is handled.
	 * </p>
	 * 
	 * @param a value 1
	 * @param b value 2
	 * @param c value 3
	 * @return the largest of the values
	 * @see IEEE754rUtils#max(float, float, float) for a version of this method that handles NaN differently
	 */
	public static float max(float a, float b, float c) {
		return Math.max(Math.max(a, b), c);
	}

	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Compares two <code>doubles</code> for order.
	 * </p>
	 * 
	 * <p>
	 * This method is more comprehensive than the standard Java greater than, less than and equals operators.
	 * </p>
	 * <ul>
	 * <li>It returns <code>-1</code> if the first value is less than the second.</li>
	 * <li>It returns <code>+1</code> if the first value is greater than the second.</li>
	 * <li>It returns <code>0</code> if the values are equal.</li>
	 * </ul>
	 * 
	 * <p>
	 * The ordering is as follows, largest to smallest:
	 * <ul>
	 * <li>NaN
	 * <li>Positive infinity
	 * <li>Maximum double
	 * <li>Normal positive numbers
	 * <li>+0.0
	 * <li>-0.0
	 * <li>Normal negative numbers
	 * <li>Minimum double (<code>-Double.MAX_VALUE</code>)
	 * <li>Negative infinity
	 * </ul>
	 * </p>
	 * 
	 * <p>
	 * Comparing <code>NaN</code> with <code>NaN</code> will return <code>0</code>.
	 * </p>
	 * 
	 * @param lhs the first <code>double</code>
	 * @param rhs the second <code>double</code>
	 * @return <code>-1</code> if lhs is less, <code>+1</code> if greater, <code>0</code> if equal to rhs
	 */
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

	/**
	 * <p>
	 * Compares two floats for order.
	 * </p>
	 * 
	 * <p>
	 * This method is more comprehensive than the standard Java greater than, less than and equals operators.
	 * </p>
	 * <ul>
	 * <li>It returns <code>-1</code> if the first value is less than the second.
	 * <li>It returns <code>+1</code> if the first value is greater than the second.
	 * <li>It returns <code>0</code> if the values are equal.
	 * </ul>
	 * 
	 * <p>
	 * The ordering is as follows, largest to smallest:
	 * <ul>
	 * <li>NaN
	 * <li>Positive infinity
	 * <li>Maximum float
	 * <li>Normal positive numbers
	 * <li>+0.0
	 * <li>-0.0
	 * <li>Normal negative numbers
	 * <li>Minimum float (<code>-Float.MAX_VALUE</code>)
	 * <li>Negative infinity
	 * </ul>
	 * 
	 * <p>
	 * Comparing <code>NaN</code> with <code>NaN</code> will return <code>0</code>.
	 * </p>
	 * 
	 * @param lhs the first <code>float</code>
	 * @param rhs the second <code>float</code>
	 * @return <code>-1</code> if lhs is less, <code>+1</code> if greater, <code>0</code> if equal to rhs
	 */
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
