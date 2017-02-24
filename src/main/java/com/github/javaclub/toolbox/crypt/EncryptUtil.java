/*
 * @(#)EncryptUtil.java  2009-2-19
 *
 * Copyright (c) 2009 by gerald. All Rights Reserved.
 */

package com.github.javaclub.toolbox.crypt;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.github.javaclub.toolbox.Constants;
import com.github.javaclub.toolbox.crypt.impl.BlowfishCipher;

import org.springframework.util.StringUtils;

/**
 * A utilities class to encypt or decrypt string, file etc.
 * 
 * @author <a href="mailto:gerald.chen@qq.com">Gerald Chen</a>
 */
public class EncryptUtil {

	/** 默认密钥，当提供的密钥为null时使用 */
	private static final String DEFAULT_KEY = "=jhe%$@^#6(l&*)="; // 随意设置，但不能超过16位
	
	private static ICipher cipher = new BlowfishCipher();

	/**
	 * 加密串source
	 * 
	 * @param source 要加密的内容
	 * @return 加密后的内容
	 */
	public static String encrypt(String src) {
		return encrypt(src, null);
	}

	/**
	 * 解密串source
	 * 
	 * @param source 要解密的内容
	 * @return 解密后的内容
	 */
	public static String decrypt(String src) {
		return decrypt(src, null);
	}
	
	/**
	 * 加密串src,使用的密钥为key
	 *
	 * @param src 要加密的内容
	 * @param key 加密所使用的密钥
	 * @return
	 */
	public static String encrypt(String src, String key) {
		byte[] srcBytes = CodecUtil.toBytes(src);
		byte[] keyBytes = (key == null ? CodecUtil.toBytes(DEFAULT_KEY) : CodecUtil.toBytes(key.hashCode() + ""));
		byte[] encryptedBytes = cipher.encrypt(srcBytes, keyBytes);
		return Base64Encryptor.encodeToString(encryptedBytes);
	}
	
	/**
	 * 解密串src,使用的密钥为key
	 *
	 * @param src 要解密的内容
	 * @param key 解密时所使用的密钥
	 * @return
	 */
	public static String decrypt(String src, String key) {
		byte[] srcBytes = Base64Encryptor.decode(src);
		byte[] keyBytes = (key == null ? CodecUtil.toBytes(DEFAULT_KEY) : CodecUtil.toBytes(key.hashCode() + ""));
		byte[] bytes = cipher.decrypt(srcBytes, keyBytes);
		return CodecUtil.toString(bytes);
	}
	
	/**
	 * 加密文本文件
	 *
	 * @param src 源文件
	 * @param dst 加密后的文件，若不存在此文件将会自动生成
	 * @throws IOException
	 */
	public static void encrypt(File src, File dst) throws IOException {
		encrypt(src, dst, null, null);
	}
	
	public static void encrypt(File src, File dst, String encoding) throws IOException {
		encrypt(src, dst, null, encoding);
	}
	
	/**
	 * 解密文本文件
	 *
	 * @param src 待解密的文件
	 * @param dst 解密后的文件，若不存在此文件将会自动生成
	 * @throws IOException
	 */
	public static void decrypt(File src, File dst) throws IOException {
		decrypt(src, dst, null, null);
	}
	
	public static void decrypt(File src, File dst, String encoding) throws IOException {
		decrypt(src, dst, null, encoding);
	}
	
	/**
	 * 加密文本文件
	 *
	 * @param src 源文件
	 * @param dst 加密后的文件，若不存在此文件将会自动生成
	 * @param key 加密密钥
	 * @throws IOException
	 */
	public static void encrypt(File src, File dst, String key, String encoding) throws IOException {
		String charset = Constants.PREFERED_ENCODING;
		charset = (StringUtils.hasLength(encoding) ? encoding : Constants.PREFERED_ENCODING);
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(src), charset));
		StringBuilder content = new StringBuilder();
		String line = null;
		while((line = reader.readLine()) != null) {
			content.append(line + Constants.LINE_SEPARATER);
		}
		if(null != reader) {
			reader.close();
		}
		
		String encrypt = encrypt(content.toString(), key);
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dst), charset));
		writer.write(encrypt);
		writer.flush();
		if(null != writer) {
			writer.close();
		}
	}
	
	/**
	 * 解密文本文件
	 *
	 * @param src 待解密的文件
	 * @param dst 解密后的文件，若不存在此文件将会自动生成
	 * @param key 解密密钥
	 * @throws IOException
	 */
	public static void decrypt(File src, File dst, String key, String encoding) throws IOException {
		String charset = Constants.PREFERED_ENCODING;
		charset = (StringUtils.hasLength(encoding) ? encoding : Constants.PREFERED_ENCODING);
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(src), charset));
		StringBuilder content = new StringBuilder();
		String line = null;
		while((line = reader.readLine()) != null) {
			content.append(line + Constants.LINE_SEPARATER);
		}
		if(null != reader) {
			reader.close();
		}
		
		String decrypt = decrypt(content.toString(), key);
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dst), charset));
		writer.write(decrypt);
		writer.flush();
		if(null != writer) {
			writer.close();
		}
	}
	
    /**
     * Make MD5 diaguest. The same as <code>toMD5(text.getBytes())</code>
     */
    public static String md5(String text) {
        return md5(text.getBytes());
    }

    /**
     * Make MD5 diaguest.
     */
    public static String md5(byte[] data) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] buf = md.digest(data);
            return toHexString(buf);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
    
    private static String toHexString(byte[] bytes) {
        int length = bytes.length;
        StringBuffer sb = new StringBuffer(length * 2);
        int x = 0;
        int n1 = 0, n2 = 0;
        for(int i=0; i<length; i++) {
            if(bytes[i]>=0)
                x = bytes[i];
            else
                x= 256 + bytes[i];
            n1 = x >> 4;
            n2 = x & 0x0f;
            sb = sb.append(HEX[n1]);
            sb = sb.append(HEX[n2]);
        }
        return sb.toString();
    }

    private static final char[] HEX = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    
    public static void main(String[] args) throws IOException {
		String orginal = "囧，我是中国人！！！";
		String encrypted = EncryptUtil.encrypt(orginal);
		System.out.println(encrypted);
		String decrypted = EncryptUtil.decrypt(encrypted);
		System.out.println(decrypted + "  (decrypted==orginal)=" + orginal.equalsIgnoreCase(decrypted));
		
		File oringnalFile = new File("C:/Documents and Settings/Administrator/桌面/txt.txt");
		File encryptedFile = new File("C:/Documents and Settings/Administrator/桌面/encrypted.txt");
		EncryptUtil.encrypt(oringnalFile, encryptedFile, "UTF-8");
		
		File decryptedFile = new File("C:/Documents and Settings/Administrator/桌面/decrypted.txt");
		EncryptUtil.decrypt(encryptedFile, decryptedFile, "UTF-8");
		
		// 4d72e32e0b63f0763fca3a9b6c489a57
		System.out.println(EncryptUtil.md5(orginal));
	}

}
