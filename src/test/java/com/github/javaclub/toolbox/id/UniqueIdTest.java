/*
 * @(#)UniqueIdTest.java	2012-3-8
 *
 * Copyright (c) 2012. All Rights Reserved.
 *
 */

package com.github.javaclub.toolbox.id;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import com.github.javaclub.toolbox.id.UniqueId;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * UniqueIdTest
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: UniqueIdTest.java 2012-3-8 下午01:06:02 Exp $
 */
public class UniqueIdTest {
	
	private static Map<Long, String> db;
	
	@Before
	public void init() throws Exception {
		db = new HashMap<Long, String>();
	}

	@After
	public void destroy() {
		db.clear();
		db = null;
	}
	
	@Test
	public void customId() throws Exception {
		
		for (int i = 0; i < 20; i++) {
			new Thread(new GetCustomId(db)).start();
			Thread.sleep(2000);
		}
		System.out.println();
	}
	
	@Test
	public void uniqueId() {
		for (int i = 0; i < 20; i++) {
			new Thread(new GetUniqueId(db)).start();
		}
		System.out.println();
	}

}

class GetCustomId implements Runnable {
	
	private Map<Long, String> db;

	public GetCustomId() {
		super();
	}
	

	public GetCustomId(Map<Long, String> db) {
		super();
		this.db = db;
	}

	public void run() {
		for (int i = 0; i < 10000; i++) {
			Long k = System.currentTimeMillis();
			if(db.containsKey(k)) {
				Assert.fail("The key => [" + k + "] already exits.");
			}
			db.put(k, k.toString());
			System.out.println("Thread => [" + this + "], Put key => [" + k + "]");
		}
	}
	
}

class GetUniqueId implements Runnable {
	
	private Map<Long, String> db;

	public GetUniqueId() {
		super();
	}
	

	public GetUniqueId(Map<Long, String> db) {
		super();
		this.db = db;
	}

	public void run() {
		for (int i = 0; i < 10000; i++) {
			Long k = UniqueId.getInstance().getUniqTime();
			if(db.containsKey(k)) {
				Assert.fail("The key => [" + k + "] already exsits.");
			}
			db.put(k, k.toString());
			System.out.println("Thread => [" + this + "], Put key => [" + k + "]");
		}
	}
	
}