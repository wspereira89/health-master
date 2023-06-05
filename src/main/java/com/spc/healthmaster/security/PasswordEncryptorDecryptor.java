package com.spc.healthmaster.security;

public interface PasswordEncryptorDecryptor {
    String encrypt(String password);
    String decrypt(String encryptedPassword);
}
