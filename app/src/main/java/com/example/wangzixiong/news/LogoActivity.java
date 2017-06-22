package com.example.wangzixiong.news;

import android.os.Bundle;
import android.widget.RelativeLayout;

import base.BaseActivity;

public class LogoActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.rl);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            startactivityFinish(MainActivity.class);
                            overridePendingTransition(R.anim.main, R.anim.logo);

                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
