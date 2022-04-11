package com.github.imoliwer.nesqueue.shared.crypto;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import static java.util.Base64.getEncoder;
import static javax.crypto.Cipher.*;

/**
 * This class handles cryptography encryption and decryption logic by algorithm, private and public key.
 */
public final class CryptoHandle {
    /** {@link String} the algorithm to use during encryption and decryption. **/
    private final String algorithm;

    /** {@link PrivateKey} the private key to use for decryption. **/
    private final PrivateKey privateKey;

    /** {@link PublicKey} the public key to use for encryption. **/
    private final PublicKey publicKey;

    /**
     * @param algorithm {@link String} used for encryption and decryption.
     * @param privateKey {@link PrivateKey} the decryption key.
     * @param publicKey {@link PublicKey} the encryption key.
     */
    public CryptoHandle(String algorithm, PrivateKey privateKey, PublicKey publicKey) {
        this.algorithm = algorithm;
        if (privateKey == null && publicKey == null) {
            try {
                final var generator = KeyPairGenerator.getInstance(algorithm);
                generator.initialize(4096);
                final var pair = generator.generateKeyPair();

                this.privateKey = pair.getPrivate();
                this.publicKey = pair.getPublic();
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException("Failed during creation of private/public key pair.");
            }
        } else {
            this.privateKey = privateKey;
            this.publicKey = publicKey;
        }
    }

    /**
     * Encrypt a byte array of value.
     *
     * @param value {@link Byte} array of bytes to be encrypted.
     * @return {@link String} the finished encryption of said value.
     */
    public String encrypt(byte[] value) {
        return this.handle(ENCRYPT_MODE, value);
    }

    /**
     * Decrypt a byte array of value.
     *
     * @param value {@link Byte} array of bytes to be decrypted.
     * @return {@link String} the finished decryption of said value.
     */
    public String decrypt(byte[] value) {
        return this.handle(DECRYPT_MODE, value);
    }

    /**
     * Encrypt or decrypt a value.
     *
     * @param mode {@link Integer} the mode to use (ENCRYPT_MODE=1 or DECRYPT_MODE=2).
     * @param value {@link Byte} byte array of said value to be encrypted / decrypted.
     * @return {@link String} the handled value in string.
     */
    private String handle(int mode, byte[] value) {
        if (mode != 1 && mode != 2) {
            throw new IllegalArgumentException("Mode must be either 1 (encryption) or 2 (decryption).");
        }

        try {
            final Cipher cipher = getInstance(this.algorithm);
            cipher.init(mode, mode == 1 ? this.publicKey : this.privateKey);

            final byte[] bytes = cipher.doFinal(value);
            return mode == 1 ? getEncoder().encodeToString(bytes) : new String(bytes);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}