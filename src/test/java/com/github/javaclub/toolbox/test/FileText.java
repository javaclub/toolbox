/*
 * @(#)FileText.java	2016年11月24日
 *
 * Copyright (c) 2016. All Rights Reserved.
 *
 */

package com.github.javaclub.toolbox.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.javaclub.toolbox.core.Strings;
import com.github.javaclub.toolbox.util.IOUtil;

/**
 * FileText
 *
 * @author <a href="mailto:hongyuan.czq@alibaba-inc.com">Gerald Chen</a>
 * @version $Id: FileText.java 2016年11月24日 Exp $
 */
public class FileText {
	

	public static void main(String[] args) throws Exception {
		String path = "D:/temp/data/package_inner_accounts/gbk_vv2.txt";
		CountAccountMap countMap = new CountAccountMap();
		String acc = "测试111";
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path), "GBK"));
		try {
			String line = null;
			while( null != (line = reader.readLine()) ) {
				String[] arr = line.split(",");
				List<String> accountArr = parseArr(arr[3]);
				for (String aa : accountArr) {
					if(!acc.equals(aa)) {
						continue;
					}
					Account account = new Account(arr[0], arr[1], arr[2], aa);
					countMap.add(aa, account);
				}
			}
		} catch (Exception e) {
			System.err.println(e);
		} finally {
			IOUtil.close(reader);
		}
		
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new FileWriter(new File("D:/temp/data/package_inner_accounts/acounts.txt"), true), true);
			Map<String, Map<String, Account>> data = countMap.getData();
			for (Map.Entry<String, Map<String, Account>> entry : data.entrySet()) {
				if(entry.getValue().size() > 0) {
					for (Map.Entry<String, Account> ee : entry.getValue().entrySet()) {
						pw.println(ee.getValue().toString());
						pw.flush();
					}
				}
			}
		} catch (Exception e) {
			System.err.println(e);
		} finally {
			IOUtil.close(pw);
		}
		
	}
	
	private static List<String> parseArr(String string) {
		String[] arr = string.split(";");;
		List<String> result = new ArrayList<String>();
		if(null != arr) {
			for (String string2 : arr) {
				if(Strings.isNotEmpty(string2)) {
					result.add(string2);
				}
			}
		}
		return result;
	}

	static class CountAccountMap {
		private Map<String, Map<String, Account>> data = new HashMap<String, Map<String,Account>>();
		
		public Map<String, Map<String, Account>> getData() {
			return data;
		}
		
		public void add(String account, Account accObj) {
			if(data.containsKey(account)) {
				Map<String, Account> map = data.get(account);
				if(null == map) {
					map = new HashMap<String, FileText.Account>();
					data.put(account, map);
				}
				if(!data.get(account).containsKey(accObj.toString())) {
					data.get(account).put(accObj.toString(), accObj);
				}
				
			}
		}
	}
	
	static class Account {
		private String sellerId;
		private String packageId;
		private String storeId;
		private String account;
		
		public Account(String sellerId, String packageId, String storeId,String account) {
			super();
			this.sellerId = sellerId;
			this.packageId = packageId;
			this.storeId = storeId;
			this.account = account;
		}
		public String getSellerId() {
			return sellerId;
		}
		public void setSellerId(String sellerId) {
			this.sellerId = sellerId;
		}
		public String getPackageId() {
			return packageId;
		}
		public void setPackageId(String packageId) {
			this.packageId = packageId;
		}
		public String getStoreId() {
			return storeId;
		}
		public void setStoreId(String storeId) {
			this.storeId = storeId;
		}
		public String getAccount() {
			return account;
		}
		public void setAccount(String account) {
			this.account = account;
		}
		
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((account == null) ? 0 : account.hashCode());
			result = prime * result
					+ ((packageId == null) ? 0 : packageId.hashCode());
			result = prime * result
					+ ((sellerId == null) ? 0 : sellerId.hashCode());
			result = prime * result
					+ ((storeId == null) ? 0 : storeId.hashCode());
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Account other = (Account) obj;
			if (account == null) {
				if (other.account != null)
					return false;
			} else if (!account.equals(other.account))
				return false;
			if (packageId == null) {
				if (other.packageId != null)
					return false;
			} else if (!packageId.equals(other.packageId))
				return false;
			if (sellerId == null) {
				if (other.sellerId != null)
					return false;
			} else if (!sellerId.equals(other.sellerId))
				return false;
			if (storeId == null) {
				if (other.storeId != null)
					return false;
			} else if (!storeId.equals(other.storeId))
				return false;
			return true;
		}
		@Override
		public String toString() {
			return sellerId + "," + packageId + "," + storeId + "," + account;
		}
		
	}
}
