/*
 * @(#)IPUtil.java	2014-10-14
 *
 * Copyright (c) 2014. All Rights Reserved.
 *
 */

package com.github.javaclub.toolbox.util;

import java.util.regex.Pattern;

/**
 * IPUtil
 * 
 * @author <a href="mailto:hongyuan.czq@alibaba-inc.com">Gerald Chen</a>
 * @version $Id: IPUtil.java 2014-10-14 Exp $
 */
public abstract class IPUtil {

	private static final Pattern IPV4_PATTERN = Pattern.compile("^(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}$");

	private static final Pattern IPV6_STD_PATTERN = Pattern.compile("^(?:[0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$");

	private static final Pattern IPV6_HEX_COMPRESSED_PATTERN = Pattern.compile("^((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)::((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)$");
	
	/**
     * 将IP地址(220.189.213.3)转变成Long
     *
     * @param ip
     * @return
     */
    public static long ip2Long(String ip) {
        long ret = 0;
        if (ip == null) {
            return ret;
        }
        String[] segs = ip.split("\\.");

        for (int i = 0; i < segs.length; i++) {
            long seg = Long.parseLong(segs[i]);
            ret += (seg << ((3 - i) * 8));
        }

        return ret;
    }

    /**
     * 将数据库中表示IP的Long型，转变成标准形式（220.189.213.3）
     *
     * @param ipLong
     * @return
     */
    public static String long2Ip(long ipLong) {
        StringBuffer ip = new StringBuffer(String.valueOf(ipLong >> 24) + ".");

        ip.append(String.valueOf((ipLong & 16711680) >> 16) + ".");
        ip.append(String.valueOf((ipLong & 65280) >> 8) + ".");
        ip.append(String.valueOf(ipLong & 255));

        return ip.toString();
    }
	
	public static boolean isIp(final String input) {
		if (null == input || "".equals(input)) {
			return false;
		}
		String ip = input.trim();

		return (isIPv4Address(ip) || isIPv6Address(ip));
	}

	public static boolean isIPv4Address(final String input) {
		return IPV4_PATTERN.matcher(input).matches();
	}
	
	public static boolean isIPv6Address(final String input) {
		return isIPv6StdAddress(input) || isIPv6HexCompressedAddress(input);
	}

	public static boolean isIPv6StdAddress(final String input) {
		return IPV6_STD_PATTERN.matcher(input).matches();
	}

	public static boolean isIPv6HexCompressedAddress(final String input) {
		return IPV6_HEX_COMPRESSED_PATTERN.matcher(input).matches();
	}

	public static void main(String[] args) {
		String ip = "123.58.180.8";
		System.out.println(IPUtil.isIp(ip));
	}
}
