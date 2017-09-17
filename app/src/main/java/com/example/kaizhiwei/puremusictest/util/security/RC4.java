package com.example.kaizhiwei.puremusictest.util.security;

/**
 * Created by kaizhiwei on 17/9/16.
 */

public class RC4
{
    private static final int STATE_LENGTH = 256;
    private byte[] engineState = null;
    private byte[] workingKey = null;
    private int x = 0;
    private int y = 0;

    public RC4(String paramString)
    {
        this.workingKey = paramString.getBytes();
    }

    private void processBytes(byte[] paramArrayOfByte1, int paramInt1, int paramInt2, byte[] paramArrayOfByte2, int paramInt3)
    {
        if (paramInt1 + paramInt2 > paramArrayOfByte1.length)
            throw new RuntimeException("input buffer too short");
        if (paramInt3 + paramInt2 > paramArrayOfByte2.length)
            throw new RuntimeException("output buffer too short");
        int j = 0;
        while (j < paramInt2)
        {
            this.x = (this.x + 1 & 0xFF);
            this.y = (this.engineState[this.x] + this.y & 0xFF);
            byte i = this.engineState[this.x];
            this.engineState[this.x] = this.engineState[this.y];
            this.engineState[this.y] = i;
            paramArrayOfByte2[(j + paramInt3)] = ((byte)(paramArrayOfByte1[(j + paramInt1)] ^ this.engineState[(this.engineState[this.x] + this.engineState[this.y] & 0xFF)]));
            j += 1;
        }
    }

    private void reset()
    {
        setKey(this.workingKey);
    }

    private void setKey(byte[] paramArrayOfByte)
    {
        int k = 0;
        this.x = 0;
        this.y = 0;
        if (this.engineState == null)
            this.engineState = new byte[256];
        int j = 0;
        while (j < 256)
        {
            this.engineState[j] = ((byte)j);
            j += 1;
        }
        int m = 0;
        j = 0;
        while (k < 256)
        {
            m = m + ((paramArrayOfByte[j] & 0xFF) + this.engineState[k]) & 0xFF;
            byte i = this.engineState[k];
            this.engineState[k] = this.engineState[m];
            this.engineState[m] = i;
            j = (j + 1) % paramArrayOfByte.length;
            k += 1;
        }
    }

    public byte[] decrypt(byte[] paramArrayOfByte)
    {
        reset();
        byte[] arrayOfByte = new byte[paramArrayOfByte.length];
        processBytes(paramArrayOfByte, 0, paramArrayOfByte.length, arrayOfByte, 0);
        return arrayOfByte;
    }

    public byte[] encrypt(byte[] paramArrayOfByte)
    {
        reset();
        byte[] arrayOfByte = new byte[paramArrayOfByte.length];
        processBytes(paramArrayOfByte, 0, paramArrayOfByte.length, arrayOfByte, 0);
        return arrayOfByte;
    }
}