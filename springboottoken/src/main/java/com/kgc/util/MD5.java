package com.kgc.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class MD5 {

	/**
	 * 生成加密的密文
	 * @param plainText 明文
	 * @param length
	 * @return
	 */
	public static String getMd5(String plainText,int length) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes());
			byte b[] = md.digest();

			int i;

			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			// 32位
			// return buf.toString();
			// 16位
			// return buf.toString().substring(0, 16);
			
			return buf.toString().substring(0, length);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}

	}

	public static int getRandomCode(){		
		int max=9999;
        int min=1111;
        Random random = new Random();
        return random.nextInt(max)%(max-min+1) + min;		
	}
	public static void main(String[] args) {
		System.out.println(MD5.getMd5("ni好",16));
		//System.out.println(getRandomCode());
	}

}
