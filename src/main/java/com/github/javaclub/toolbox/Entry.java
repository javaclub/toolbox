/*
 * @(#)Entry.java	2017年5月18日
 *
 * Copyright (c) 2017. All Rights Reserved.
 *
 */

package com.github.javaclub.toolbox;

import java.io.Serializable;

/**
 * Entry 一个节点数据
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: Entry.java 2017年5月18日 10:55:18 Exp $
 */
public class Entry<K, V> implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private K key;
	private V value;
	
	public Entry(K key, V value) {
		this.key = key;
		this.value = value;
	}

	public K getKey() {
		return key;
	}

	public void setKey(K key) {
		this.key = key;
	}

	public V getValue() {
		return value;
	}

	public void setValue(V value) {
		this.value = value;
	}
	
	public static <K, V> Entry<K, V> create(K key, V value) {
		return new Entry<K, V>(key, value);
	}

}