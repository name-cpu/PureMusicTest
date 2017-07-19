package com.example.kaizhiwei.puremusictest.contract;

import com.example.kaizhiwei.puremusictest.download.ProgressUIListener;

/**
 * Created by kaizhiwei on 17/7/19.
 */

public interface DownloadContract {
    interface Presenter extends BaseContract.Presenter{
        void downloadFile(String fileUrl, String localFilePath, ProgressUIListener listener);
    }

}
