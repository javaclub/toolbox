/*
 * @(#)DefaultIdGenerator.java	2010-4-6
 *
 * Copyright (c) 2010 by gerald. All Rights Reserved.
 */

package com.github.javaclub.toolbox.id.support;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

import com.github.javaclub.toolbox.core.Strings;
import com.github.javaclub.toolbox.id.AbstractIdGenerator;
import com.github.javaclub.toolbox.id.ChangeableIdGenertor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * The default id generator.
 * 
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: DefaultIdGenerator.java 131 2011-07-19 10:54:36Z gerald.chen.hz@gmail.com $
 */
public class DefaultIdGenerator extends AbstractIdGenerator {

	/**
	 * The log stream
	 */
	protected static final Log LOG = LogFactory.getLog(DefaultIdGenerator.class);

	/**
	 * The default message digest algorithm to use if we cannot use the
	 * requested one.
	 */
	protected static final String DEFAULT_ALGORITHM = "MD5";

	/**
	 * The message digest algorithm to be used when generating session
	 * identifiers. This must be an algorithm supported by the
	 * <code>java.security.MessageDigest</code> class on your platform.
	 */
	protected String algorithm = DEFAULT_ALGORITHM;

	/**
	 * A random number generator to use when generating session identifiers.
	 */
	protected Random random = new SecureRandom();

	/**
	 * Return the MessageDigest implementation to be used when creating session
	 * identifiers.
	 */
	protected MessageDigest digest = null;

	/**
	 * Seed the random number
	 */
	public DefaultIdGenerator() {
		// Start with the current system time as a seed
		long seed = System.currentTimeMillis();

		// Also throw in the system identifier for 'this' from toString
		char[] entropy = this.toString().toCharArray();
		for (int i = 0; i < entropy.length; i++) {
			long update = ((byte) entropy[i]) << ((i % 8) * 8);
			seed ^= update;
		}
		random.setSeed(seed);
	}

	/**
	 * Generate and return a new session identifier.
	 * 
	 * @param length The number of bytes to generate
	 * @return A new page id string
	 */
	public synchronized Serializable generate(int length) {
		byte buffer[] = new byte[16];

		// Render the result as a String of hexadecimal digits
		StringBuffer reply = new StringBuffer();

		int resultLength = reply.length();
		while (resultLength < length) {
			random.nextBytes(buffer);
			buffer = getDigest().digest(buffer);

			for (int j = 0; j < buffer.length && resultLength < length; j++) {
				byte b1 = (byte) ((buffer[j] & 0xf0) >> 4);
				byte b2 = (byte) (buffer[j] & 0x0f);
				boolean flag = (j % 2 == 0);
				if(flag) {
					if (b1 < 10) {
						reply.append((char) ('0' + b1));
					} else {
						reply.append((char) ('A' + (b1 - 10)));
					}
				} else {
					if (b2 < 10) {
						reply.append((char) ('0' + b2));
					} else {
						reply.append((char) ('A' + (b2 - 10)));
					}
				}
				
				resultLength++;
			}
		}
		return reply.toString().toLowerCase();
	}

	public Serializable number(int length) {
		return Strings.random(length, NUMBER_CHARS);
	}

	/**
	 * @return the algorithm
	 */
	public synchronized String getAlgorithm() {
		return algorithm;
	}

	/**
	 * @param algorithm the algorithm to set
	 */
	public synchronized void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
		digest = null;
	}

	/**
	 * Return the MessageDigest object to be used for calculating session
	 * identifiers. If none has been created yet, initialize one the first time
	 * this method is called.
	 * 
	 * @return The hashing algorithm
	 */
	private MessageDigest getDigest() {
		if (digest == null) {
			try {
				digest = MessageDigest.getInstance(algorithm);
			} catch (NoSuchAlgorithmException ex) {
				try {
					digest = MessageDigest.getInstance(DEFAULT_ALGORITHM);
				} catch (NoSuchAlgorithmException ex2) {
					digest = null;
					throw new IllegalStateException(
							"No algorithms for IdGenerator");
				}
			}

			if(LOG.isDebugEnabled()) {
				LOG.debug("Using MessageDigest: " + digest.getAlgorithm());
			}
		}

		return digest;
	}

	public String toString() {
		return super.toString();
	}
	
	public static void main(String[] args) {
		
		ChangeableIdGenertor generator = new DefaultIdGenerator();
		/*for (int i = 0; i < 1000000; i++) {
			System.out.println(generator.generate(16));
		}
		
		System.out.println(generator.generate(500));*/
		for (int i = 0; i < 1000000; i++) {
			System.out.println(generator.number(6));
		}
	}

}
