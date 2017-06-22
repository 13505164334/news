package com.example.wangzixiong.news;

import android.content.SharedPreferences;
import android.support.annotation.IdRes;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import adapter.ViewpagerAdapter;
import base.BaseActivity;

public class LeadeActivity extends BaseActivity {

    private ViewPager vp;
    private RadioGroup rg;
    private RadioButton rb1, rb2, rb3;
    private SharedPreferences sp;
    private SharedPreferences.Editor edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = getSharedPreferences("leade", MODE_PRIVATE);
        edit = sp.edit();
        boolean isfirst = sp.getBoolean("isfirst", true);
        if (isfirst) {
            setContentView(R.layout.activity_leade);
            find();
            ViewpagerAdapter adapter = new ViewpagerAdapter(this);
            vp.setAdapter(adapter);
            jianting();
        } else {
            startactivityFinish(LogoActivity.class);
        }

    }

    private void jianting() {
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        rb1.setChecked(true);
                        rb2.setChecked(false);
                        rb3.setChecked(false);
                        break;
                    case 1:
                        rb1.setChecked(false);
                        rb2.setChecked(true);
                        rb3.setChecked(false);
                        break;
                    case 2:
                        rb1.setChecked(false);
                        rb2.setChecked(false);
                        rb3.setChecked(true);
                        edit.putBoolean("isfirst", false);
                        edit.apply();
                        startactivityFinish(LogoActivity.class);
                        break;
                    default:
                        break;
                }


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.radioButton1:
                        vp.setCurrentItem(0);
                        break;
                    case R.id.radioButton2:
                        vp.setCurrentItem(1);
                        break;
                    case R.id.radioButton3:
                        vp.setCurrentItem(2);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void find() {
        vp = (ViewPager) findViewById(R.id.vp);
        rg = (RadioGroup) findViewById(R.id.rg);
        rb1 = (RadioButton) findViewById(R.id.radioButton1);
        rb2 = (RadioButton) findViewById(R.id.radioButton2);
        rb3 = (RadioButton) findViewById(R.id.radioButton3);
    }
}
