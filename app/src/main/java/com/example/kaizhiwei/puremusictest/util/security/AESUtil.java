package com.example.kaizhiwei.puremusictest.util.security;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by kaizhiwei on 17/9/16.
 */


public final class AESUtil
{
    private static final String ALGORITHM_NAME = "AES";
    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";

    public static byte[] decrypt(String src, String key, byte[] paramArrayOfByte)
    {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "AES");
        try {
            Cipher localCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            localCipher.init(2, secretKeySpec, new IvParameterSpec(src.getBytes()));
            return localCipher.doFinal(paramArrayOfByte);
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static byte[] encrypt(String src, String key, byte[] paramArrayOfByte)
    {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "AES");
        try{
            Cipher localCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            localCipher.init(1, secretKeySpec, new IvParameterSpec(src.getBytes()));
            return localCipher.doFinal(paramArrayOfByte);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }
}