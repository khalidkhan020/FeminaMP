package com.linchpin.periodtracker.utlity;

import java.io.UnsupportedEncodingException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import android.util.Base64;

public class EncryptDecrypt {
	private static final byte[] iv = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };

	private SecretKeySpec secretKey;

	private static EncryptDecrypt encryptDecrypt;

	public static EncryptDecrypt getInstance(String secretKey) {
		if (null == encryptDecrypt)
			encryptDecrypt = new EncryptDecrypt(secretKey);

		return encryptDecrypt;
	}

	private EncryptDecrypt(String key) {
		try {
			secretKey = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("AES Crypto is not supported by System. " + e.getMessage());
		}
	}

	/**
	 * Encrypt the data
	 * 
	 * @param tobeEncrypted
	 * @return
	 */

	public String newEncrypt(String stringToEncrypt) {
	String base64 = null;
		try {
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(iv));
			byte[] result = cipher.doFinal(stringToEncrypt.getBytes("UTF-8"));
			base64 = Base64.encodeToString(result, Base64.DEFAULT);
		} catch (Exception exception) {

		}
		return base64;
	}

	public String newDcrypt(String tobeDecoded) {
		String string = null;
		try{
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		byte[] decoded = Base64.decode(tobeDecoded, Base64.DEFAULT);
		cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));
		byte[] decrypted = cipher.doFinal(decoded);
		 string = new String(decrypted);
		}catch(Exception exception){
			exception.printStackTrace();
		}
		return string;
	}

/*	public String symmetricEncrypt(String text, String secretKey) {
		byte[] raw;
		String encryptedString;
		SecretKeySpec skeySpec;
		byte[] encryptText = text.getBytes();
		Cipher cipher;
		try {
			raw = Base64.decodeBase64(secretKey);
			skeySpec = new SecretKeySpec(raw, "AES");
			cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
			encryptedString = Base64.encodeBase64String(cipher.doFinal(encryptText));
		} catch (Exception e) {
			e.printStackTrace();
			return "Error";
		}
		return encryptedString;
	}

	public String symmetricDecrypt(String text, String secretKey) {
		Cipher cipher;
		String encryptedString;
		byte[] encryptText = null;
		byte[] raw;
		SecretKeySpec skeySpec;
		try {
			raw = Base64.decodeBase64(secretKey);
			skeySpec = new SecretKeySpec(raw, "AES");
			encryptText = Base64.decodeBase64(text);
			cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, skeySpec);
			encryptedString = new String(cipher.doFinal(encryptText));
		} catch (Exception e) {
			e.printStackTrace();
			return "Error";
		}
		return encryptedString;
	}

	public String encrypt(String tobeEncrypted) {
		try {
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(iv));
			byte[] result = cipher.doFinal(tobeEncrypted.getBytes());
			 encodeBase64String 
			String encrypted = Base64.encodeBase64String(result);

			return encrypted;
		} catch (Exception e) {
			throw new RuntimeException("Encrypt using AES failed. " + e.getMessage());
		}
	}

	*//**
	 * Decrypt the data
	 * 
	 * @param encrypted
	 * @return
	 *//*
	public String decrypt(String encrypted) {
		try {

			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));
			byte[] decodes = Base64.decodeBase64(encrypted.getBytes());
			return new String(cipher.doFinal(encrypted.getBytes()));
		} catch (Exception e) {
			throw new RuntimeException("Deecrypt using AES failed. " + e.getMessage());
		}
	}*/

}