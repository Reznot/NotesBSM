package com.example.notes;

import java.nio.charset.StandardCharsets;
import java.security.AlgorithmParameters;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class Encryption {

    Cipher cipher;

    private static final String cypherInstance = "AES/CBC/PKCS5Padding";

    private final String salt;
    private SecretKey secretKey;
    String iv;

    Encryption(String salt, String password, String iv) throws InvalidKeySpecException, NoSuchAlgorithmException {
        this.salt = salt;
        secretKey = generateKey(password);
        this.iv = iv;
    }

    private SecretKey generateKey(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] saltByte;
        saltByte = salt.getBytes(StandardCharsets.UTF_8);
        KeySpec keySpec = new PBEKeySpec(password.toCharArray(), saltByte, 1024, 256);
        SecretKey tmpSecretKey = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512").generateSecret(keySpec);
        return new SecretKeySpec(tmpSecretKey.getEncoded(), "AES");
    }

    public String encrypt(String toEncrypt) throws Exception {
        cipher = Cipher.getInstance(cypherInstance);
        byte[] ivBytes = Arrays.copyOfRange(iv.getBytes(StandardCharsets.UTF_8), 0, 16);
        AlgorithmParameterSpec spec = new IvParameterSpec(ivBytes);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, spec);
        byte[] cipherText = cipher.doFinal(
                toEncrypt.getBytes(StandardCharsets.UTF_8)
        );
        return Base64.getEncoder().encodeToString(cipherText);
    }

    public String decrypt(String toDecrypt) throws Exception {
        cipher = Cipher.getInstance(cypherInstance);
        byte[] ivBytes = Arrays.copyOfRange(iv.getBytes(StandardCharsets.UTF_8), 0, 16);
        AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
        cipher.init(javax.crypto.Cipher.DECRYPT_MODE, secretKey, ivSpec);
        byte[] decryptedBytes = cipher.doFinal(
                Base64.getDecoder().decode(toDecrypt)
        );
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }
}
