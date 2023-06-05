package com.spc.healthmaster.security;

import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;

//TODO IMPLMENTAR EN PASSWROD DE CADA UNO
@Service
public class PasswordEncryptorDecryptorImpl implements PasswordEncryptorDecryptor {
    private static final String AES_ALGORITHM = "AES";
    private static final String SECRET_KEY = "YourSecretKey"; // Clave secreta para encriptar/desencriptar (debe ser segura y secreta)

    @Override
    public String encrypt(String password) {
        try {
            Key key = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), AES_ALGORITHM);
            Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encryptedBytes = cipher.doFinal(password.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException("Error al encriptar la contraseña", e);
        }
    }

    @Override
    public String decrypt(String encryptedPassword) {
        try {
            Key key = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), AES_ALGORITHM);
            Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedPassword));
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Error al desencriptar la contraseña", e);
        }
    }
}
