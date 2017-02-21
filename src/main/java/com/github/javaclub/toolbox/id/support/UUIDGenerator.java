/*
 * @(#)UUIDGenerator.java	2010-2-6
 *
 * Copyright (c) 2010 by gerald. All Rights Reserved.
 */

package com.github.javaclub.toolbox.id.support;

import java.io.Serializable;
import java.net.InetAddress;

import com.github.javaclub.toolbox.id.IdGenerator;

/**
 * The UUID Generator.
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: UUIDGenerator.java 56 2011-06-27 02:11:24Z gerald.chen.hz@gmail.com $
 */
public class UUIDGenerator implements IdGenerator {
	
	private String separator = "";

	public UUIDGenerator() {
		super();
	}

	public String getSeparator() {
		return separator;
	}

	public void setSeparator(String separator) {
		this.separator = separator;
	}

	private static int toInt(byte[] bytes) {
        int result = 0;
        for (int i = 0; i < 4; i++) {
            result = (result << 8) - Byte.MIN_VALUE + (int) bytes[i];
        }
        return result;
    }

    private static final int IP;
    static {
        int ipadd;
        try {
            ipadd = toInt(InetAddress.getLocalHost().getAddress());
        } catch (Exception e) {
            ipadd = 0;
        }
        IP = ipadd;
    }

    private static short counter = (short) 0;

    private static final int JVM = (int) (System.currentTimeMillis() >>> 8);

    /**
     * Unique across JVMs on this machine (unless they load this class in the
     * same quater second - very unlikely)
     */
    protected int getJVM() {
        return JVM;
    }

    /**
     * Unique in a millisecond for this JVM instance (unless there are >
     * Short.MAX_VALUE instances created in a millisecond)
     */
    protected short getCount() {
        synchronized (UUIDGenerator.class) {
            if (counter < 0)
                counter = 0;
            return counter++;
        }
    }

    /**
     * Unique in a local network
     */
    protected int getIP() {
        return IP;
    }

    /**
     * Unique down to millisecond
     */
    protected short getHiTime() {
        return (short) (System.currentTimeMillis() >>> 32);
    }

    protected int getLoTime() {
        return (int) System.currentTimeMillis();
    }

    protected String format(int intval) {
        String formatted = Integer.toHexString(intval);
        StringBuffer buf = new StringBuffer("00000000");
        buf.replace(8 - formatted.length(), 8, formatted);
        return buf.toString();
    }

    protected String format(short shortval) {
        String formatted = Integer.toHexString(shortval);
        StringBuffer buf = new StringBuffer("0000");
        buf.replace(4 - formatted.length(), 4, formatted);
        return buf.toString();
    }

    public Serializable generate() {
        return new StringBuffer(36).append(format(getIP())).append(separator).append(format(getJVM())).append(separator).append(
                format(getHiTime())).append(separator).append(format(getLoTime())).append(separator).append(format(getCount()))
                .toString();
    }
    
    public static void main(String[] args) {
    	UUIDGenerator generator = new UUIDGenerator();
    	
    	long start = System.currentTimeMillis();
		for (int i = 0; i < 200000; i++) {
			System.out.println(generator.generate());
		}
		// 4028e38126f990740126f990748c0000
		System.out.println("cost time ---> " + (System.currentTimeMillis() - start));
    }

}
