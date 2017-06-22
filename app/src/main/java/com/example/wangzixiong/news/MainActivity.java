package com.example.wangzixiong.news;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import base.BaseActivity;
import de.hdodenhof.circleimageview.CircleImageView;
import fragment.CollectFragment;
import fragment.NewsFragment;
import okhttp3.Call;
import util.OkHttpUtil;
import util.UrlManage;

public class MainActivity extends BaseActivity {

    private DrawerLayout dl_home;
    private String title;
    private Toolbar tool_home;
    private NavigationView nav_home;
    private NewsFragment newsFragment;
    private CollectFragment collectFragment;
    private JSONObject data;
    private TextView tv_homelogin;
    private CircleImageView cv_homelogin;
    //用来保存登录状态
    private SharedPreferences sp;
    private boolean isLogin;
    private SharedPreferences.Editor edit;
    private boolean isphotochange;
    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sp = getSharedPreferences("login", MODE_PRIVATE);
        edit = sp.edit();
        isphotochange = sp.getBoolean("isphotochange", false);
        path = sp.getString("path", null);
        intiView();
        intiEvent();
        collectFragment = new CollectFragment();
        //消除NetworkMineThreadExtion
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

    }

    private void intiEvent() {
        title = "新闻";
        //将toolbar代替actionbar
        setSupportActionBar(tool_home);
        final ActionBar actionBar = getSupportActionBar();
        //显示home，返回箭头
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("新闻");
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, dl_home, tool_home, R.string.open, R.string.close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                actionBar.setTitle("菜单");
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                actionBar.setTitle(title);
            }
        };
        //不显示箭头，显示三条横线
        drawerToggle.syncState();
        //打开关闭的监听
        dl_home.addDrawerListener(drawerToggle);
        //侧滑菜单默认选中第一个
        nav_home.getMenu().getItem(0).setChecked(true);
        //侧滑菜单item选中监听
        nav_home.setNavigationItemSelectedListener(navigationItemSelectedListener);
        //显示NewsFragment
        if (newsFragment == null) {
            newsFragment = new NewsFragment();
        }
        showFragment(newsFragment);

        islogin();

    }

    /*
    判断是否登录
     */
    private void islogin() {
        isLogin = sp.getBoolean("isLogin", false);
        nav_home.getHeaderView(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isLogin = sp.getBoolean("isLogin", false);
                if (!isLogin) {
                    //进入登陆界面
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivityForResult(intent, 1);
                } else {
                    //进入用户界面
                    Intent intent = new Intent(MainActivity.this, UserActivity.class);
                    startActivityForResult(intent, 2);
                }
            }
        });
        if (isLogin) {
            String token = sp.getString("token", null);
            if (token != null) {
                getUser(token);
            }
        } else {
            tv_homelogin.setText("点击登录");
            cv_homelogin.setImageResource(R.drawable.biz_pc_main_info_profile_avatar_bg_dark);
        }
    }

    private Fragment targetFragment;

    private void showFragment(Fragment fragment) {
        //获得FragmentManager对象和事物
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fts = fm.beginTransaction();
        if (fragment == null) {//传入为空，直接返回
            Toast.makeText(this, "不能传入空的Fragment", Toast.LENGTH_SHORT).show();
            return;
        }
        if (targetFragment == null) {//如果正在显示的targetFragment为空
            targetFragment = fragment; //当前显示的为传入的
            fts.add(R.id.ll, fragment);
            fts.commit();
            return;
        }
        String simpleName = fragment.getClass().getSimpleName();
        if (simpleName.equals(targetFragment.getClass().getSimpleName())) {
            return;
        } else {
            fts.hide(targetFragment);
            if (!fragment.isAdded()) {
                fts.add(R.id.ll, fragment);
            } else {
                fts.show(fragment);
            }
            targetFragment = fragment;
            fts.commit();
        }
    }

    NavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.news:
                    title = "新闻";
                    showFragment(newsFragment);
                    dl_home.closeDrawer(Gravity.START);
                    break;
                case R.id.favorite:
                    title = "收藏";
                    if (collectFragment == null) {
                        collectFragment = new CollectFragment();
                    }
                    if (collectFragment.isAdded()) {
                        collectFragment.getSaveNews();
                    }
                    showFragment(collectFragment);
                    dl_home.closeDrawer(Gravity.START);
                    break;
                case R.id.local:
                    title = "本地";
                    dl_home.closeDrawer(Gravity.START);
                    break;
                case R.id.comment:
                    title = "跟帖";
                    dl_home.closeDrawer(Gravity.START);
                    break;
                case R.id.photo:
                    title = "图片";
                    dl_home.closeDrawer(Gravity.START);
                    break;
                case R.id.updata:
                    title = "版本更新";
                    updata();
                    dl_home.closeDrawer(Gravity.START);
                    break;
            }
            return true;
        }
    };

    private void updata() {
        final String url = UrlManage.NEWS_URL + "update?imei=1&pkg=" + this.getPackageName() + "&ver=1";
        OkHttpUtil.getOkHttp(url, this, new OkHttpUtil.CallBack() {
            @Override
            public void onResponse(Call call, String string) {
                try {
                    JSONObject object = new JSONObject(string);
                    if (object.getString("version").equals("1")) {
                        Toast.makeText(MainActivity.this, "已经是最新版本", Toast.LENGTH_SHORT).show();
                    } else {
                        Snackbar.make(nav_home, "发现新版本", Snackbar.LENGTH_SHORT).setAction("点击下载", null).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(MainActivity.this, "连接失败", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void intiView() {
        dl_home = (DrawerLayout) findViewById(R.id.dl);
        tool_home = (Toolbar) findViewById(R.id.toolbar);
        nav_home = (NavigationView) findViewById(R.id.nv);
        tv_homelogin = (TextView) nav_home.getHeaderView(0).findViewById(R.id.tv_homelogin);
        cv_homelogin = (CircleImageView) nav_home.getHeaderView(0).findViewById(R.id.cv);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1://主界面进入登录界面
                if (resultCode == 2) {//登录界面返回数据
                    Bundle bundle = data.getExtras();
                    String token = bundle.getString("token");
                    getUser(token);
                }
                break;
            case 2://用户中心
                if (resultCode == 2) {//用户中心返回数据
                    isphotochange = sp.getBoolean("isphotochange", false);
                    path = sp.getString("path", path);
//                    重新获取
                    islogin();
                }
        }
    }

    public void getUser(final String token) {
        String url = UrlManage.NEWS_URL + "user_home?ver=1&imei=1&token=" + token;
        OkHttpUtil.getOkHttp(url, this, new OkHttpUtil.CallBack() {
            @Override
            public void onResponse(Call call, String string) {
                try {
                    JSONObject object = new JSONObject(string);
                    if (object.getString("message").equals("OK")) {
                        data = object.getJSONObject("data");
                        String uid = data.getString("uid");
                        String portrait = data.getString("portrait");
                        tv_homelogin.setText(uid);
                        if (isphotochange) {
                            cv_homelogin.setImageBitmap(BitmapFactory.decodeFile(path));
                        } else {
                            Glide.with(MainActivity.this).load(portrait).into(cv_homelogin);
                        }
                        edit.putString("token", token);
                        edit.putBoolean("isLogin", true);
                        edit.putString("string", string);
                        edit.commit();
                    } else {
                        edit.clear();
                        edit.putBoolean("isLogin", false);
                        edit.commit();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                edit.clear();
                edit.putBoolean("isLogin", false);
                edit.commit();
                Toast.makeText(MainActivity.this, "连接失败", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

