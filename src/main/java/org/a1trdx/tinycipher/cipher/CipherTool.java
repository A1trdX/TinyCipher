package org.a1trdx.tinycipher.cipher;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

public class CipherTool {

    private static final SecureRandom RANDOM = new SecureRandom();

    private static final int INT_LENGTH = Integer.BYTES;
    private static final int SALT_LENGTH = 16;
    private static final int GCM_IV_LENGTH = 12;
    private static final int METADATA_LENGTH = INT_LENGTH * 2 + SALT_LENGTH + GCM_IV_LENGTH;

    private static final int FORMAT_VERSION = 0;
    private static final int GCM_TAG_LENGTH = 16;
    private static final int KEY_LENGTH = 256;
    private static final int ITERATIONS = 600000;
    private static final String CIPHER_TRANSFORMATION = "AES/GCM/NoPadding";
    private static final String SECRET_KEY_FACTORY_ALGORITHM = "PBKDF2WithHmacSHA256";
    private static final String SECRET_KEY_ALGORITHM = "AES";

    public static byte[] encrypt(byte[] input, byte[] password) {
        try {
            byte[] salt = generateSalt();
            byte[] iv = generateInitVector();

            var cipher = initCipher(Cipher.ENCRYPT_MODE, salt, iv, password, ITERATIONS);
            byte[] encryptedData = cipher.doFinal(input);

            var buffer = ByteBuffer.allocate(METADATA_LENGTH + encryptedData.length);
            buffer.putInt(FORMAT_VERSION);
            buffer.putInt(ITERATIONS);
            buffer.put(salt);
            buffer.put(iv);
            buffer.put(encryptedData);
            return buffer.array();
        } catch (Exception ex) {
            throw new CipherToolException(ex);
        }
    }

    public static byte[] decrypt(byte[] input, byte[] password) {
        try {
            var buffer = ByteBuffer.wrap(input);

            int formatVersion = buffer.getInt();
            if (formatVersion != FORMAT_VERSION) {
                throw new RuntimeException("Unsupported format version: " + FORMAT_VERSION);
            }

            int iterations = buffer.getInt();
            byte[] salt = new byte[SALT_LENGTH];
            buffer.get(salt);
            byte[] iv = new byte[GCM_IV_LENGTH];
            buffer.get(iv);
            byte[] encryptedData = new byte[input.length - METADATA_LENGTH];
            buffer.get(encryptedData);

            var cipher = initCipher(Cipher.DECRYPT_MODE, salt, iv, password, iterations);
            return cipher.doFinal(encryptedData);
        } catch (Exception ex) {
            throw new CipherToolException(ex);
        }
    }

    private static Cipher initCipher(int mode, byte[] salt, byte[] iv, byte[] password, int iterations) throws Exception {
        var cipher = Cipher.getInstance(CIPHER_TRANSFORMATION);
        var parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv);
        var secretKeySpec = generateSecretKeySpec(password, salt, iterations);
        cipher.init(mode, secretKeySpec, parameterSpec);
        return cipher;
    }

    private static SecretKeySpec generateSecretKeySpec(byte[] password, byte[] salt, int iterations) throws Exception {
        char[] passwordCharArray = new String(password, StandardCharsets.UTF_8).toCharArray();
        var keyFactory = SecretKeyFactory.getInstance(SECRET_KEY_FACTORY_ALGORITHM);
        var keySpec = new PBEKeySpec(passwordCharArray, salt, iterations, KEY_LENGTH);
        byte[] secretKey = keyFactory.generateSecret(keySpec).getEncoded();
        return new SecretKeySpec(secretKey, SECRET_KEY_ALGORITHM);
    }

    private static byte[] generateSalt() {
        byte[] salt = new byte[SALT_LENGTH];
        RANDOM.nextBytes(salt);
        return salt;
    }

    private static byte[] generateInitVector() {
        byte[] iv = new byte[GCM_IV_LENGTH];
        RANDOM.nextBytes(iv);
        return iv;
    }
}
