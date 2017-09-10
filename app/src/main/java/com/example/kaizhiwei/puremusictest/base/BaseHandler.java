package com.example.kaizhiwei.puremusictest.base;


import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class BaseHandler extends Handler {
    private AtomicBoolean isCancle = new AtomicBoolean(false);

    public BaseHandler(){
        super(Looper.getMainLooper());
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if (!isCancle.get()) {
            handleBusiness(msg);
        }
    }

    /**
     * 回调信息
     * 
     * @param msg
     */
    public abstract void handleBusiness(Message msg);

    /**
     * 取消数据回调
     */
    public void cancle() {
        isCancle.set(true);
    }

    /**
     * 是否继续运行
     * @return
     */
    public boolean canRun() {
        return !(isCancle.get());
    }
}
