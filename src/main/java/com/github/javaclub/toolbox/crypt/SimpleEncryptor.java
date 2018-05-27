package com.github.javaclub.toolbox.crypt;

public class SimpleEncryptor {

    private SimpleEncryptor() {}

    /**
     * Encrypts the specified string.
     * 
     * @param input the string to encrypt.
     * @return the encrypted string.
     */
    public static String encrypt(String input) {
        String sResult = "";
        for (int i = 0; i < input.length(); i++) {
            int iAsc = input.charAt(i);

            if (iAsc >= 97 && iAsc <= 109) {
                iAsc = iAsc + 13; //a ... m inclusive become n ... z
            }
            else if (iAsc >= 110 && iAsc <= 122) {
                iAsc = iAsc - 13; //n ... z inclusive become a ... m
            }
            else if (iAsc >= 65 && iAsc <= 77) {
                iAsc = iAsc + 13; //A ... M inclusive become N ... Z
            }
            else if (iAsc >= 78 && iAsc <= 90) {
                iAsc = iAsc - 13; //N ... Z inclusive become A ... M
            }
            sResult += (char) iAsc;
        }
        return sResult;
    }

    /**
     * Decrypts the encrypted string.
     * 
     * @param encrypted a encrypted string.
     * @return the original string before encrypted.
     */
    public static String decrypt(String encrypted) {
        return encrypt(encrypted);
    }
    
    public static void main(String[] args) {
		String s = "haha我54/^6fre/wNJJ*+=-_Mggh";
		String encrypt = SimpleEncryptor.encrypt(s);
		System.out.println(encrypt);
		
		String deText = SimpleEncryptor.decrypt(encrypt);
		System.out.println(deText);
		
		System.out.println(s.equals(deText));
	}

}