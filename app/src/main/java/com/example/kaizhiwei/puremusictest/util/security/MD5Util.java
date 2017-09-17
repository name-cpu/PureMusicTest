package com.example.kaizhiwei.puremusictest.util.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by kaizhiwei on 17/9/16.
 */

public class MD5Util
{
    public static String toHexString(byte[] paramArrayOfByte, String paramString, boolean paramBoolean)
    {
        StringBuilder localStringBuilder = new StringBuilder();
        int j = paramArrayOfByte.length;
        int i = 0;
        while (i < j)
        {
            String str2 = Integer.toHexString(paramArrayOfByte[i] & 0xFF);
            String str1 = str2;
            if (paramBoolean)
                str1 = str2.toUpperCase();
            if (str1.length() == 1)
                localStringBuilder.append("0");
            localStringBuilder.append(str1).append(paramString);
            i += 1;
        }
        return localStringBuilder.toString();
    }

    public static String toMd5(byte[] paramArrayOfByte, boolean paramBoolean)
    {
        try
        {
            MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
            localMessageDigest.reset();
            localMessageDigest.update(paramArrayOfByte);
            String ret = toHexString(localMessageDigest.digest(), "", paramBoolean);
            return ret;
        }
        catch (NoSuchAlgorithmException e)
        {
        }

        return "";
    }
}