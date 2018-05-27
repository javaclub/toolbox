/*
 * @(#)PasswordEncryptor.java	2009-11-15
 *
 * Copyright (c) 2009 by gerald. All Rights Reserved.
 */

package com.github.javaclub.toolbox.crypt;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;

import com.github.javaclub.toolbox.core.JRuntimeException;

/**
 * 用于密码加密和验证,同一个明文在不同时刻加密后的暗文不同.
 * </p>
 * 
 * @author <a href="mailto:gerald.chen@qq.com">Gerald Chen</a>
 * @version $Id: PasswordEncryptor.java 56 2011-06-27 02:11:24Z gerald.chen.hz@gmail.com $
 */
public class PasswordEncryptor {
	
	/**
     * constructor: forbid to call
     */
    private PasswordEncryptor() {

    }

    private static final SecureRandom secureRandom = new SecureRandom();
    
    /**
     * Encodes the specified password.
     * 
     * @param password the original password.
     * @return the encoded result.
     * @throws JRuntimeException if failed.
     */
    public final static String encrypt(String password) {
        return encrypt(password, null);
    }

	/**
	 * Encodes the specified password.
	 * 
	 * @param password the original password.
	 * @param salt the salt data to encrypt
	 * @return the encrypted result
	 * @throws JRuntimeException if failed.
	 */
	public final static String encrypt(String password, byte[] salt) {
		if (salt == null) {
			salt = new byte[12];
			secureRandom.nextBytes(salt);
		}

		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(salt);
			md.update(password.getBytes("UTF-8"));
			byte[] digest = md.digest();
			byte[] storedPassword = new byte[digest.length + 12];

			System.arraycopy(salt, 0, storedPassword, 0, 12);
			System.arraycopy(digest, 0, storedPassword, 12, digest.length);

			return new String(Base64Encryptor.encode(storedPassword));
		} catch (Exception ex) {
			throw new JRuntimeException("password=" + password + ", salt=" + salt, ex);
		}
	}

	/**
	 * Validates a password.
	 * 
	 * @param encrypted the encrypted password.
	 * @param password the password to validate.
	 * @return <code>true</code> if the specified password matches with the encoded data.
	 * @throws JRuntimeException if failed to validate.
	 */
	public final static boolean validate(final String encrypted, final String password) {
		String storedPassword = new String(encrypted);
		byte[] storedPasswordBytesWithSalt = Base64Encryptor.decode(storedPassword.getBytes());

		if (storedPasswordBytesWithSalt.length < 12) {
			throw new JRuntimeException("encrypted password data: [" + encrypted + "]");
		}

		try {
			byte[] salt = new byte[12];
			System.arraycopy(storedPasswordBytesWithSalt, 0, salt, 0, 12);

			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(salt);
			md.update(password.getBytes("UTF-8"));

			byte[] digestForGivenPassword = md.digest();
			byte[] digestForExistingPassword = new byte[storedPasswordBytesWithSalt.length - 12];
			System.arraycopy(storedPasswordBytesWithSalt, 12, digestForExistingPassword, 0, storedPasswordBytesWithSalt.length - 12);

			return Arrays.equals(digestForGivenPassword, digestForExistingPassword);
		} catch (Exception ex) {
			throw new JRuntimeException("encrypted=[" + encrypted + "], password=[" + password + "]", ex);
		}
	}
	
	public static void main(String[] args) {
		String password = "上无天";
		String encrypted = PasswordEncryptor.encrypt(password);
		// Ccf73KkuWy3gx2CRuMLxgNL8MeAu4B/jhPck2w==
		// Zsdfkjr0vDEj5UIk1SRDOTbz1mEgq+6ZgwqJkA==
		// bxbQS8+/piWAwFcse1op7ZFWNMQdq88mmUc/Cw==
		// lV2g7Db3A0IsOhlno4pwGb8yEcTm6cZxZ5Vzdg==
		// sS+IxdSPvu1/64Y1N+R3BefgavTCyAIlNnnyeA==
		System.out.println(encrypted.length() + "\t" + encrypted);
		
		System.out.println(PasswordEncryptor.validate(encrypted, password));
	}
}
