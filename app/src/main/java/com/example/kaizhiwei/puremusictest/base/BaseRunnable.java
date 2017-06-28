package com.example.kaizhiwei.puremusictest.base;

public abstract class BaseRunnable implements Runnable {

    private BaseHandler mHandle;

    public BaseRunnable(BaseHandler handle) {
        mHandle = handle;
        ThreadPool.submit(this);
    }

    @Override
    public void run() {
        try {
            doBusiness();
        } catch (Exception e) {
           e.printStackTrace();
        }
    }

    public abstract void doBusiness() throws Exception;
}
