package com.example.kaizhiwei.puremusictest.bean;

import java.util.List;

/**
 * Created by kaizhiwei on 17/7/31.
 */

public class MvCategoryBean {
    /**
     * error_code : 22000
     * result : ["全部","内地","港台","欧美","日本","韩国","现场"]
     */

    private int error_code;
    private List<String> result;

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public List<String> getResult() {
        return result;
    }

    public void setResult(List<String> result) {
        this.result = result;
    }
}
