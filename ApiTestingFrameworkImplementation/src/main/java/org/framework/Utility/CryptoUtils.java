package org.framework.Utility;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class CryptoUtils {
    // AES algorithm; for AES-128 the key must be 16 bytes.
    private static final String ALGORITHM = "AES";
    // Example secret key. In real projects, load this securely (e.g., environment variable or secure vault).
    private static final String SECRET_KEY = "MySuperSecretKey"; // Must be 16 characters for AES-128

    /**
     * Encrypts the provided plain text using AES and returns a Base64-encoded cipher text.
     *
     * @param plainText The plain text to encrypt.
     * @return The encrypted text as Base64 encoded string.
     * @throws Exception If encryption fails.
     */
    public static String encrypt(String plainText) throws Exception {
        SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    /**
     * Decrypts the provided Base64-encoded cipher text using AES and returns the plain text.
     *
     * @param cipherText The Base64-encoded cipher text.
     * @return The decrypted plain text.
     * @throws Exception If decryption fails.
     */
    public static String decrypt(String cipherText) throws Exception {
        SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        byte[] decodedBytes = Base64.getDecoder().decode(cipherText);
        byte[] decryptedBytes = cipher.doFinal(decodedBytes);
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    // Optional: Convenience methods for emails if you wish to keep them separate.
    public static String encryptEmail(String email) throws Exception {
        return encrypt(email);
    }

    public static String decryptEmail(String cipherEmail) throws Exception {
        return decrypt(cipherEmail);
    }


}
