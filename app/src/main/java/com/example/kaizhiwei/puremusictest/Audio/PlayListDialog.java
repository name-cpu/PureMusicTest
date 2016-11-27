package com.example.kaizhiwei.puremusictest.Audio;

import android.app.Dialog;
import android.content.Context;

/**
 * Created by kaizhiwei on 16/11/27.
 */
public class PlayListDialog extends Dialog {
    public PlayListDialog(Context context) {
        super(context);
    }

    protected PlayListDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public PlayListDialog(Context context, int themeResId) {
        super(context, themeResId);
    }
}
