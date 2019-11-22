package com.example.notes;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class Encryption {

    private final String salt;
    private final String iv;
    private SecretKey secretKey;

    Encryption(String salt, String iv, String password) throws InvalidKeySpecException, NoSuchAlgorithmException {
        this.salt = salt;
        this.iv = iv;
        secretKey = generateKey(password);
    }

    private SecretKey generateKey(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] saltByte;
        saltByte = salt.getBytes(StandardCharsets.UTF_8);
        KeySpec keySpec = new PBEKeySpec(password.toCharArray(), saltByte, 1024, 256);
        SecretKey tmpSecretKey = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512").generateSecret(keySpec);
        return new SecretKeySpec(tmpSecretKey.getEncoded(), "AES");
    }
}
