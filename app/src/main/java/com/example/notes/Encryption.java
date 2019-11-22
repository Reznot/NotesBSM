package com.example.notes;

import java.nio.charset.StandardCharsets;
import java.security.AlgorithmParameters;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.Cipher;
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
    byte[] iv;

    Encryption(String salt, String password) throws InvalidKeySpecException, NoSuchAlgorithmException {
        this.salt = salt;
        secretKey = generateKey(password);
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
        AlgorithmParameters params = cipher.getParameters();
        iv = params.getParameterSpec(IvParameterSpec.class).getIV();
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(iv));
        byte[] toEncryptBytes = toEncrypt.getBytes(StandardCharsets.UTF_8);
        byte[] cipherText = cipher.doFinal(toEncryptBytes);
        return Base64.getEncoder().encodeToString(cipherText);
    }
}
