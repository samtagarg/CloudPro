package com.cloudshare.security;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class SaltTextEncryption {

	public static final String pbkdf2Algo = "PBKDF2WithHmacSHA1";
	public static final int saltByteSize = 16;
	public static final int hashByteSize = 16;
	public static final int pbkdf2Iterations = 1000;

	public static final int iterationIndex = 0;
	public static final int saltIndex = 1;
	public static final int pbkdf2Index = 2;

	public String createHash(String password)
			throws NoSuchAlgorithmException, InvalidKeySpecException {
		return createHash(password.toCharArray());
	}

	private static String createHash(char[] password)
			throws NoSuchAlgorithmException, InvalidKeySpecException {
		SecureRandom random = new SecureRandom();
		byte[] salt = new byte[saltByteSize];
		random.nextBytes(salt);
		byte[] hash = pbkdf2(password, salt, pbkdf2Iterations, hashByteSize);
		return pbkdf2Iterations + ":" + toHex(salt) + ":" + toHex(hash);
	}

	public boolean validatePassword(String password, String correctHash)
			throws NoSuchAlgorithmException, InvalidKeySpecException {
		return validatePassword(password.toCharArray(), correctHash);
	}

	private static boolean validatePassword(char[] password, String correctHash)
			throws NoSuchAlgorithmException, InvalidKeySpecException {
		String[] params = correctHash.split(":");
		int iterations = Integer.parseInt(params[iterationIndex]);
		byte[] salt = fromHex(params[saltIndex]);
		byte[] hash = fromHex(params[pbkdf2Index]);
		byte[] testHash = pbkdf2(password, salt, iterations, hash.length);
		return slowEquals(hash, testHash);
	}

	private static boolean slowEquals(byte[] a, byte[] b) {
		int diff = a.length ^ b.length;
		for (int i = 0; i < a.length && i < b.length; i++)
			diff |= a[i] ^ b[i];
		return diff == 0;
	}

	private static byte[] pbkdf2(char[] password, byte[] salt, int iterations,
			int bytes) throws NoSuchAlgorithmException, InvalidKeySpecException {
		PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, bytes * 8);
		SecretKeyFactory skf = SecretKeyFactory.getInstance(pbkdf2Algo);
		return skf.generateSecret(spec).getEncoded();
	}

	private static byte[] fromHex(String hex) {
		byte[] binary = new byte[hex.length() / 2];
		for (int i = 0; i < binary.length; i++) {
			binary[i] = (byte) Integer.parseInt(
					hex.substring(2 * i, 2 * i + 2), 16);
		}
		return binary;
	}

	private static String toHex(byte[] array) {
		BigInteger bi = new BigInteger(1, array);
		String hex = bi.toString(16);
		int paddingLength = (array.length * 2) - hex.length();
		if (paddingLength > 0)
			return String.format("%0" + paddingLength + "d", 0) + hex;
		else
			return hex;
	}

	public static void main(String[] args) {

		try {
			String hash = new SaltTextEncryption().createHash("apple!3401");
			System.out.println(hash);

			System.out.println(new SaltTextEncryption().validatePassword("apple!3401",
					hash));
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			e.printStackTrace();
		}
	}

}