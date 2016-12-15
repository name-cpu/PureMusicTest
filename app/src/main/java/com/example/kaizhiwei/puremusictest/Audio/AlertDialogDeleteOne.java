package com.example.kaizhiwei.puremusictest.Audio;

import android.app.AlertDialog;
import android.content.ContentProvider;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import com.example.kaizhiwei.puremusictest.R;

/**
 * Created by 24820 on 2016/12/14.
 */
public class AlertDialogDeleteOne extends AlertDialog {
    private CheckBox ckDeleteFile;
    private Button btnDelete;
    private Button btnCancel;

    protected AlertDialogDeleteOne(Context context) {
        super(context);
    }

    protected AlertDialogDeleteOne(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public void show(){
        LayoutInflater inflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //View view = inflater.inflate(R.layout.delete_music);

    }
}
