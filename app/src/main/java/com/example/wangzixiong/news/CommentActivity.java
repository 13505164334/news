package com.example.wangzixiong.news;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.bean.BuildBean;
import com.google.gson.Gson;
import com.mugen.Mugen;
import com.mugen.MugenCallbacks;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import adapter.CommentAdapter;
import base.BaseActivity;
import bean.CommentInfo;
import bean.NewsInfo;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import util.OkHttpUtil;
import util.UrlManage;

public class CommentActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout toolbarLayout;
    @BindView(R.id.app_bar)
    AppBarLayout appBar;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.iv)
    ImageView iv;
    @BindView(R.id.rc)
    RecyclerView rc;
    @BindView(R.id.srl_comment)
    SwipeRefreshLayout srlComment;
    private NewsInfo info;
    private CommentAdapter adapter;
    private SharedPreferences sp;
    private SharedPreferences.Editor edit;
    private boolean isLogin;
    private boolean isLoading = false;
    private CircleImageView cv;
    private EditText et;
    private BuildBean buildBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        sp = getSharedPreferences("login", MODE_PRIVATE);
        edit = sp.edit();
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        info = (NewsInfo) bundle.getSerializable("info");
        settitle();
        Glide.with(this).load(info.getIcon()).into(iv);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                isLogin = sp.getBoolean("isLogin", false);
                if (isLogin) {
                    String token = sp.getString("token", null);
                    if (token != null) {
                        sendDialog(token);
                    }
                } else {
                    Snackbar.make(view, "请登录", Snackbar.LENGTH_LONG)
                            .setAction("点击登录", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(CommentActivity.this, LoginActivity.class);
                                    startActivityForResult(intent, 1);
                                }
                            }).show();
                }
            }


        });
        rc.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CommentAdapter(this, R.layout.comment_rc);
        rc.setAdapter(adapter);
        getdata(1);
        down();
        up();
    }

    private void up() {
        Mugen.with(rc, new MugenCallbacks() {
            @Override
            public void onLoadMore() {
                Toast.makeText(CommentActivity.this, "开始加载", Toast.LENGTH_SHORT).show();
                getdata(2);
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }

            @Override
            public boolean hasLoadedAllItems() {
                return false;
            }
        }).start();
    }

    private void down() {
        srlComment.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getdata(1);
                srlComment.setRefreshing(false);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == 2) {//登录界面返回数据
                Bundle bundle = data.getExtras();
                String token = bundle.getString("token");
                sendDialog(token);
            }
        }
    }

    //发送评论
    private void sendDialog(final String token) {
        View send = LayoutInflater.from(CommentActivity.this).inflate(R.layout.dialog_send, null);
        buildBean = DialogUIUtils.showCustomBottomAlert(CommentActivity.this, send);
        buildBean.show();
        et = (EditText) send.findViewById(R.id.et);
        cv = (CircleImageView) send.findViewById(R.id.cv);
        cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = et.getText().toString().trim();
                if (comment.length() <= 0) {
                    et.setError("评论不能为空");
                    return;
                } else {
                    sendcomment(comment, token);
                }
            }
        });
    }

    private void sendcomment(String comment, final String token) {
        String url = UrlManage.NEWS_URL + "cmt_commit?ver=1&nid=" + info.getNid() + "&token=" + token + "&imei=1&ctx=" + comment;
        OkHttpUtil.getOkHttp(url, this, new OkHttpUtil.CallBack() {
            @Override
            public void onResponse(Call call, String string) {
                try {
                    JSONObject object = new JSONObject(string);
                    object = object.getJSONObject("data");
                    Log.e("dfgh", object.toString());
                    if (object.getString("explain").equals("发布成功！")) {
                        buildBean.dialog.dismiss();
                        getdata(1);
                        Toast.makeText(CommentActivity.this, "发送成功", Toast.LENGTH_SHORT).show();
                        edit.putBoolean("isLogin", true);
                        edit.putString("token", token);
                        Log.e("token", token.toString());
                        edit.commit();
                    } else {
                        Toast.makeText(CommentActivity.this, "发送失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(CommentActivity.this, "连接失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void settitle() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(info.getTitle());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            setResult(2);
            finish();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void getdata(int dir) {
        int cid;
        String stamp;
        if (dir == 1) {
            cid = 0;
            stamp = "0";
            adapter.clear();
            adapter.notifyDataSetChanged();
        } else {
            cid = adapter.getList().get(adapter.getItemCount() - 1).getCid();
            stamp = adapter.getList().get(adapter.getItemCount() - 1).getStamp();
            isLoading = true;
        }
        String url = UrlManage.NEWS_URL + "cmt_list?ver=1&nid=" + info.getNid() + "&type=1&stamp=" + stamp + "&cid=" + cid + "&dir=" + dir + "&cnt=20";
        Log.e("url", url);
        OkHttpUtil.getOkHttp(url, this, new OkHttpUtil.CallBack() {
            @Override
            public void onResponse(Call call, String string) {
                Log.e("swdefg", string);
                Gson gson = new Gson();
                CommentInfo info = gson.fromJson(string, CommentInfo.class);
                adapter.add(info.getData());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(CommentActivity.this, "连接失败", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
