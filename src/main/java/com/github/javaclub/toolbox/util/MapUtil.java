/*
 * @(#)MapUtil.java	2011-7-9
 *
 * Copyright (c) 2011. All Rights Reserved.
 *
 */

package com.github.javaclub.toolbox.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

/**
 * MapUtil
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: MapUtil.java 92 2011-07-09 16:24:52Z gerald.chen.hz@gmail.com $
 */
public abstract class MapUtil {

	/**
	 * 将Map转换为元素为List类型的List，得到的List只有两个List类型元素
	 * <p>
	 * 第一个是该Map的key列表，第二个是该Map的value列表
	 * 
	 * @param map
	 * @return size为2的List
	 */
	@SuppressWarnings("unchecked")
	public static <K, V> List<List> mapToList(Map<K, V> map) {
		List<K> fields = new ArrayList<K>();
		List<V> values = new ArrayList<V>();

		Set<Entry<K, V>> set = map.entrySet();
		Iterator<Entry<K, V>> iter = set.iterator();
		while (iter.hasNext()) {
			Entry<K, V> entry = (Entry<K, V>) iter.next();
			fields.add((K) entry.getKey());
			values.add(entry.getValue());
		}
		List<List> result = new ArrayList<List>();
		result.add(fields);
		result.add(values);

		return result;
	}
}
