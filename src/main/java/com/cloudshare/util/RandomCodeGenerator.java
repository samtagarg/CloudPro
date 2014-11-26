package com.cloudshare.util;

import java.util.Random;

public class RandomCodeGenerator {

	static Random rnd = new Random();

	public static String randomString(int len) {
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++)
			sb.append("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".charAt(rnd
					.nextInt("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".length())));
		return sb.toString();
	}

}
