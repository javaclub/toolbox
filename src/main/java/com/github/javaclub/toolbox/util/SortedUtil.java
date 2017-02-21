/*
 * @(#)SortedUtil.java	2012-3-8
 *
 * Copyright (c) 2012. All Rights Reserved.
 *
 */

package com.github.javaclub.toolbox.util;

import java.text.CollationKey;
import java.text.Collator;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * SortedUtil
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: SortedUtil.java 2012-3-8 16:48:30 Exp $
 */
public class SortedUtil {
	
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

	public static void main(String[] args) {
		Map<String, Object> keysortMap = SortedUtil.keysortMap(false);
		
		for(int i = 0; i < 10; i++) {
			String s = "" + (int)(Math.random()*1000);
			keysortMap.put(s, s);
		}

		keysortMap.put("aacd","aacd");
		keysortMap.put("ckdg", "ckdg");
		keysortMap.put("crdg", "crdg");
		keysortMap.put("Abc", "Abc");
		keysortMap.put("111", "111");
		keysortMap.put("bab","bab");
		keysortMap.put("BBBB", "BBBB");
		keysortMap.put("北京","北京");
		keysortMap.put("北山","北山");
		keysortMap.put("中国","中国");
		keysortMap.put("上海", "上海");
		keysortMap.put("厦门", "厦门");
		keysortMap.put("厦人", "厦人");
		keysortMap.put("香口", "香口");
		keysortMap.put("香港", "香港");
		keysortMap.put("碑海", "碑海");
		keysortMap.put("111", "111");
		keysortMap.put("0", "ooo");
		Collection<?> col = keysortMap.values();
		Iterator<?> it = col.iterator();
		while(it.hasNext()) {
			 System.out.println(it.next());
		}
	}

}
