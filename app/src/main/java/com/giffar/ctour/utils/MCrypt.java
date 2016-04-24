package com.giffar.ctour.utils;

/**
 * Created by wafdamufti on 11/23/15.
 */

import org.spongycastle.util.encoders.Base64;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import de.ailis.pherialize.Pherialize;


public class MCrypt {

    static char[] HEX_CHARS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    //private static String iv = "fedcba9876543210";//Dummy iv (CHANGE IT!)
    public IvParameterSpec ivspec;
    private SecretKeySpec keyspec;
    private Cipher cipher;
    byte iv[];

    private String SecretKey = "3f5117fed21ce4a5e385635415a187e2";//Dummy secretKey (CHANGE IT!)

    public MCrypt() {
        SecureRandom random = new SecureRandom();

        iv = new byte[16];

        random.nextBytes(iv);

        ivspec = new IvParameterSpec(iv);

        keyspec = new SecretKeySpec(SecretKey.getBytes(), "AES");

        try {
            cipher = Cipher.getInstance("AES/CBC/NoPadding");
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public byte[] encrypt(String text) throws Exception {
        if (text == null || text.length() == 0)
            throw new Exception("Empty string");

        byte[] encrypted = null;

        try {
            String newText = Pherialize.serialize(text);
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");

            SecretKeySpec secret_key = new SecretKeySpec(SecretKey.getBytes(), "HmacSHA256");
            sha256_HMAC.init(secret_key);

            String hashMac = new String(bin2hex((sha256_HMAC.doFinal(newText.getBytes()))));

            cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);

            encrypted = cipher.doFinal(padString(newText + hashMac).getBytes());
        } catch (Exception e) {
            throw new Exception("[encrypt] " + e.getMessage());
        }

        return encrypted;
    }

    public byte[] decrypt(String code) throws Exception {
        if (code == null || code.length() == 0)
            throw new Exception("Empty string");

        byte[] decrypted = null;

        try {

            String[] newCipher = code.split("\\|");

            byte[] decodedCode = Base64.decode(newCipher[0].trim().getBytes());

            byte[] newIv = Base64.decode(newCipher[1]);

            if(newIv.length != 16){ throw new Exception(); }

            IvParameterSpec ivSpec = new IvParameterSpec(newIv);

            cipher.init(Cipher.DECRYPT_MODE, keyspec, ivSpec);

            decrypted = cipher.doFinal(decodedCode);

            System.out.println(Pherialize.unserialize(new String(decrypted)));

            //Remove trailing zeroes
            if (decrypted.length > 0) {
                int trim = 0;
                for (int i = decrypted.length - 1; i >= 0; i--) if (decrypted[i] == 0) trim++;

                if (trim > 0) {
                    byte[] newArray = new byte[decrypted.length - trim];
                    System.arraycopy(decrypted, 0, newArray, 0, decrypted.length - trim);
                    decrypted = newArray;
                }
            }
        } catch (Exception e) {
            throw new Exception("[decrypt] " + e.getMessage());
        }
        return decrypted;
    }


    public String bytesToHex(byte[] buf) {
        return new String(Base64.encode(buf)) + "|" + new String(Base64.encode(iv));
    }


    public static byte[] hexToBytes(String str) {
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

    public String bin2hex(byte[] data) {
        return String.format("%0" + (data.length * 2) + 'x', new BigInteger(1, data));
    }


    private static String padString(String source) {
        char paddingChar = 0;
        int size = 16;
        int x = source.length() % size;
        int padLength = size - x;

        for (int i = 0; i < padLength; i++) {
            source += paddingChar;
        }

        return source;
    }
}
