package com.sains.common.util;

import java.security.MessageDigest;

public class Encriptor {
	public static final String MD5 = "MD5";

	public static final String SHA_1 = "SHA-1";

	private static String hexDigit(byte x) {
		StringBuffer result = new StringBuffer();

		char c = (char) ((x >> 4) & 0xf);

		if (c > 9) {
			c = (char) ((c - 10) + 'a');
		} else {
			c = (char) (c + '0');
		}

		result.append(c);

		c = (char) (x & 0xf);

		if (c > 9) {
			c = (char) ((c - 10) + 'a');
		} else {
			c = (char) (c + '0');
		}

		result.append(c);

		return result.toString();
	}

	public static String encode(String text) {
		return encode(text, MD5);
	}

	public static String encode(String text, String algorithm) {
		String result = text;
		byte[] content = text.getBytes();

		if (content != null) {
			try {
				MessageDigest messageDigest = MessageDigest.getInstance(algorithm);

				messageDigest.reset();
				messageDigest.update(content);

				StringBuffer buffer = new StringBuffer();

				byte digest[] = messageDigest.digest();

				for (int i = 0; i < digest.length; i++) {
					buffer.append(hexDigit(digest[i]));
					buffer.append(" ");
				}

				result = buffer.toString();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return result;
	}
}