package com.example.kaizhiwei.puremusictest;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.kaizhiwei.puremusictest.ScanSong.ScanSongActivity;
import com.example.kaizhiwei.puremusictest.Welcome.WelcomeActivity;

/**
 * Created by kaizhiwei on 16/10/29.
 */
public class LaunchActivity extends FragmentActivity {
    protected void onCreate( Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.launch_activity);

        Button btn = (Button)this.findViewById(R.id.button1);
        btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LaunchActivity.this, ScanSongActivity.class);
                startActivity(intent);
            }
        });
    }
}
