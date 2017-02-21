/*
 * @(#)DBTddlUtil.java	2013-10-30
 *
 * Copyright (c) 2013. All Rights Reserved.
 *
 */

package com.github.javaclub.toolbox;

import java.net.URLDecoder;

import org.junit.Test;

/**
 * DBTddlUtil
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: DBTddlUtil.java 2013-10-30 13:36:01 Exp $
 */
public class DBTddlUtil {

	@Test
	public void eticket() {
		String code = "35910223";
		System.out.println("code_eticket:" + Math.abs(code.hashCode()) % 128);

		long orderId = 394380334621726L;
		System.out.println("order_eticket:" + orderId % 128);
		System.out.println("order_log:" + orderId % 256);

	}
	
	@Test
	public void poi_shanghu_store() {
		long itemId = 36000291632L;
		System.out.println("item_dump:" + itemId % 64);
		System.out.println("item_pos:" + itemId % 64);

		String localstoreId = "6db64705ce044a77976f4e36e50311c8";
		System.out.println("localstore:" + Math.abs(localstoreId.hashCode()) % 256);
		System.out.println("localstore_dump:" + Math.abs(localstoreId.hashCode()) % 256);

	}
	
	@Test
	public void new_poi_shanghu_platform() {
		long store_id = 123456789L;
		long relation_id = 112001L;
		String outer_id = "10187498";
		
		System.out.println("store:" + store_id % 256);
		System.out.println("store_relation:" + store_id % 256);
		System.out.println("store_extends:" + store_id % 256);
		System.out.println("store_relation_dump:" + relation_id % 256);
		System.out.println("ot_store_relation:" + Math.abs(outer_id.hashCode()) % 256);
	}
	
	@Test
	public void test_get_union_all() {
		StringBuilder sbf = new StringBuilder();
		for (int i = 0; i < 256; i++) {
			String sql = "SELECT * from store_relation_dump_" + mod(i);
			sbf.append(sql);
			if(i < 255) {
				sbf.append(" ").append("union all").append(" ");
			}
		}
		System.out.println(sbf.toString());
	}
	
	@Test
	public void test_get_union_all_2() {
		StringBuilder sbf = new StringBuilder();
		int MIN_INDEX = 0;
		int MAX_INDEX = 30;
		for (int i = MIN_INDEX; i < MAX_INDEX; i++) {
			String sql = "select count(*) from ma_biz_" + mod(i) + " where gmt_create>'2016-06-03 00:00:00' and biz_type=3001";
			sbf.append(sql);
			if(i < MAX_INDEX - 1) {
				sbf.append(" ").append("union all").append(" ");
			}
		}
		System.out.println(sbf.toString());
	}
	
	@Test
	public void testDecode() {
		String txt = "%25E7%2583%25A4%25E9%25B1%25BC";
		
		try {
			
			String keyword = URLDecoder.decode(txt, "utf-8");
			System.out.println(keyword);
			
		} catch (Exception ex) {
		}
	}
	
	@Test
	public void testSqlDelete() {
		for (int i = 0; i < 1024; i++) {
			String sql = "DELETE FROM eticket_record_" + mod(i) + ";";
			System.out.println(sql);
		}
		System.out.println("DELETE FROM history_transfer_mapping;");
	}
	
	String mod(int i) {
		if(i < 10) {
			return "000" + i;
		} else if(i >= 10 && i < 100) {
			return "00" + i;
		} else if(i >= 100 && i < 1000) {
			return "0" + i;
		} else if(i >= 1000) {
			return String.valueOf(i);
		}
		return "";
	}
	
	public static void main(String[] args) {
		long store_id = 123456789L;
		long relation_id = 112001L;
		String outer_id = "2100506653123";
		
		System.out.println("store:" + store_id % 256);
		System.out.println("store_relation:" + store_id % 256);
		System.out.println("store_extends:" + store_id % 256);
		System.out.println("store_relation_dump:" + relation_id % 256);
		System.out.println("ot_store_relation:" + Math.abs(outer_id.hashCode()) % 256);
		
		Integer a = null;
		Integer b = 8;
		if(null != b && b >0 || a.intValue() > 0) {
			System.out.println("OKOK");
		}
	}
}
