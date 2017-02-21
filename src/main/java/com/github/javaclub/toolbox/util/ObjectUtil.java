/*
 * @(#)ObjectUtil.java	2011-6-16
 *
 * Copyright (c) 2011. All Rights Reserved.
 *
 */

package com.github.javaclub.toolbox.util;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import com.github.javaclub.toolbox.Consts;

/**
 * Miscellaneous object utility methods.
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: ObjectUtil.java 174 2011-07-23 10:43:37Z gerald.chen.hz@gmail.com $
 */
public abstract class ObjectUtil {

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
			return ((Collection<?>) obj).iterator().next();
		} else if (obj instanceof Map<?, ?>) {
			return ((Map<?, ?>) obj).entrySet().iterator().next();
		}
		return obj;
	}
	
	/**
	 * Convert the given array (which may be a primitive array) to an
	 * object array (if necessary of primitive wrapper objects).
	 * <p>A <code>null</code> source value will be converted to an
	 * empty Object array.
	 * @param source the (potentially primitive) array
	 * @return the corresponding object array (never <code>null</code>)
	 * @throws IllegalArgumentException if the parameter is not an array
	 */
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
	
	/**
	 * Check the target source object is empty.
	 *
	 * @param source the target source object
	 * @return true if the target source object is null or empty
	 */
	public static boolean isEmpty(Object source) {
		return 0 == length(source);
	}
	
	/**
	 * Find the first element's index in the array.
	 *
	 * @param array
	 * @param element
	 * @return
	 */
	public static int search(Object[] array, Object element) {
		final int noidx = -1;
		if(isEmpty(array)) {
			return noidx;
		}
		for (int i = 0; i < array.length; i++) {
			if (nullSafeEquals(array[i], element)) {
				return i;
			}
		}
		return noidx;
	}
	
	/**
	 * Check whether the given array contains the given element.
	 * @param array the array to check (may be <code>null</code>,
	 * in which case the return value will always be <code>false</code>)
	 * @param element the element to check for
	 * @return whether the element has been found in the given array
	 */
	public static boolean contains(Object[] array, Object element) {
		if (array == null) {
			return false;
		}
		for (int i = 0; i < array.length; i++) {
			if (nullSafeEquals(array[i], element)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Append the given Object to the given array, returning a new array
	 * consisting of the input array contents plus the given Object.
	 * @param array the array to append to (can be <code>null</code>)
	 * @param obj the Object to append
	 * @return the new array (of the same component type; never <code>null</code>)
	 */
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
	
	/**
	 * Determine if the given objects are equal, returning <code>true</code>
	 * if both are <code>null</code> or <code>false</code> if only one is
	 * <code>null</code>.
	 * <p>Compares arrays with <code>Arrays.equals</code>, performing an equality
	 * check based on the array elements rather than the array reference.
	 * @param o1 first Object to compare
	 * @param o2 second Object to compare
	 * @return whether the given objects are equal
	 * @see java.util.Arrays#equals
	 */
	public static boolean nullSafeEquals(Object o1, Object o2) {
		if (o1 == o2) {
			return true;
		}
		if (o1 == null || o2 == null) {
			return false;
		}
		if (o1.equals(o2)) {
			return true;
		}
		if (o1.getClass().isArray() && o2.getClass().isArray()) {
			if (o1 instanceof Object[] && o2 instanceof Object[]) {
				return Arrays.equals((Object[]) o1, (Object[]) o2);
			}
			if (o1 instanceof boolean[] && o2 instanceof boolean[]) {
				return Arrays.equals((boolean[]) o1, (boolean[]) o2);
			}
			if (o1 instanceof byte[] && o2 instanceof byte[]) {
				return Arrays.equals((byte[]) o1, (byte[]) o2);
			}
			if (o1 instanceof char[] && o2 instanceof char[]) {
				return Arrays.equals((char[]) o1, (char[]) o2);
			}
			if (o1 instanceof double[] && o2 instanceof double[]) {
				return Arrays.equals((double[]) o1, (double[]) o2);
			}
			if (o1 instanceof float[] && o2 instanceof float[]) {
				return Arrays.equals((float[]) o1, (float[]) o2);
			}
			if (o1 instanceof int[] && o2 instanceof int[]) {
				return Arrays.equals((int[]) o1, (int[]) o2);
			}
			if (o1 instanceof long[] && o2 instanceof long[]) {
				return Arrays.equals((long[]) o1, (long[]) o2);
			}
			if (o1 instanceof short[] && o2 instanceof short[]) {
				return Arrays.equals((short[]) o1, (short[]) o2);
			}
		}
		return false;
	}
	
	public static String join(Object[] array, String separator) {
        if (array == null) {
            return null;
        }
        return join(array, separator, 0, array.length);
    }
	
	public static String join(Object[] array, String separator, int startIndex, int endIndex) {
        if (array == null) {
            return null;
        }
        if (separator == null) {
            separator = Consts.EMPTY_STRING;
        }

        // endIndex - startIndex > 0:   Len = NofStrings *(len(firstString) + len(separator))
        //           (Assuming that all Strings are roughly equally long)
        int bufSize = (endIndex - startIndex);
        if (bufSize <= 0) {
            return Consts.EMPTY_STRING;
        }
        bufSize *= ((array[startIndex] == null ? 16 : array[startIndex].toString().length())
                        + separator.length());

        StringBuffer buf = new StringBuffer(bufSize);

        for (int i = startIndex; i < endIndex; i++) {
            if (i > startIndex) {
                buf.append(separator);
            }
            if (array[i] != null) {
                buf.append(array[i]);
            }
        }
        return buf.toString();
    }
	
	public static ClassLoader getDefaultClassLoader() {
		ClassLoader cl = null;
		try {
			cl = Thread.currentThread().getContextClassLoader();
		}
		catch (Throwable ex) {
			// Cannot access thread context ClassLoader - falling back to system class loader...
		}
		if (cl == null) {
			// No thread context class loader -> use class loader of this class.
			cl = ObjectUtil.class.getClassLoader();
		}
		return cl;
	}
	
	public static void main(String[] args) throws InterruptedException {
		int[] array = new int[] { 20, 18, 68, 28 };
		Object[] arr = ObjectUtil.toObjectArray(array);
		System.out.println(Arrays.asList(arr));
		Thread.sleep(3000);
	}
}
