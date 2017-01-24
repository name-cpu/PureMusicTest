package com.example.kaizhiwei.puremusictest.Util;

import android.os.Handler;
import android.os.Message;

/**
 * Created by kaizhiwei on 17/1/14.
 */
public abstract class BaseHandler extends Handler {
    public abstract void handleBusiness(Message msg);

    public void handleMessage(Message msg) {
        handleBusiness(msg);
    }
}
