package com.ebizu.manis;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author kazao
 */
public abstract class Security {

    private static Security INSTANCE;
    private IvParameterSpec ivSpec;
    private SecretKeySpec keySpec;
    private Cipher cipher;

    public Security() {
    }

    public String encrypt(String plain) throws Exception {
        if (plain == null || plain.length() == 0) {
            return null;
        }
        try {
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
            byte[] encrypted = cipher.doFinal(padString(plain).getBytes());
            return bytesToHex(encrypted);
        } catch (Exception e) {
        }
        return null;
    }

    public String decrypt(String encrypted) throws Exception {
        if (encrypted == null || encrypted.length() == 0) {
            return null;
        }
        try {
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
            return new String(cipher.doFinal(hexToBytes(encrypted)));
        } catch (Exception e) {
        }
        return null;
    }

    public String bytesToHex(byte[] data) {
        if (data == null) {
            return null;
        }
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < data.length; i++) {
            if ((data[i] & 0xFF) < 16) {
                buffer.append("0" + Integer.toHexString(data[i] & 0xFF));
            } else {
                buffer.append(Integer.toHexString(data[i] & 0xFF));
            }
        }
        return buffer.toString();
    }

    public byte[] hexToBytes(String str) {
        if (str == null) {
            return null;
        } else if (str.length() < 2) {
            return null;
        } else {
            int len = str.length() / 2;
            byte[] buffer = new byte[len];
            for (int i = 0; i < len; i++) {
                buffer[i] = (byte) Integer.parseInt(str.substring(i * 2, i * 2 + 2), 16);
            }
            return buffer;
        }
    }

    private static String padString(String source) {
        char paddingChar = ' ';
        int size = 16;
        int x = source.length() % size;
        int padLength = size - x;
        for (int i = 0; i < padLength; i++) {
            source += paddingChar;
        }
        return source;
    }

    public void setIvKey(String iv, String key) {
        ivSpec = new IvParameterSpec(iv.getBytes());
        keySpec = new SecretKeySpec(key.getBytes(), "AES");
        try {
            cipher = Cipher.getInstance("AES/CBC/NoPadding");
        } catch (Exception e) {
        }

    }

    public boolean isReady() {
        return cipher != null;
    }

    public static Security getInstance() {
        if (INSTANCE == null) {
            class SecurityImpl extends Security {
            };
            INSTANCE = new SecurityImpl();
        }
        return INSTANCE;
    }

}
