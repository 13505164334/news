package com.example.wangzixiong.news;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import base.BaseActivity;
import bean.NewsInfo;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import db.CollectNewsdb;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import util.UrlManage;

import static android.R.attr.data;

public class WebActivity extends BaseActivity {

    @BindView(R.id.wv)
    WebView wv;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.pb)
    ProgressBar pb;
    private NewsInfo info;
    private Menu menu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        ButterKnife.bind(this);
        info = (NewsInfo) getIntent().getExtras().getSerializable("info");
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            for (int i = 0; i <= 100; i += 10) {
                                pb.setProgress(i);
                                if (i == 100) {
                                    pb.setVisibility(View.INVISIBLE);
                                    wv.setWebViewClient(new WebViewClient());
                                    wv.loadUrl(info.getLink());
                                    title.setText(info.getTitle());
                                }
                            }

                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        ShareSDK.initSDK(this,"1e366215c648a ");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == 2) {//评论界面返回数据
                getCommentNum(menu);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu=menu;
       getMenuInflater().inflate(R.menu.webset, menu);
        getCommentNum(menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                this.finish();
                break;
            case R.id.collect:
                collectnews();
                break;
            case R.id.comment:
                Intent intent = new Intent(this, CommentActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("info",info);
                intent.putExtras(bundle);
                startActivityForResult(intent,1);
                break;
            case R.id.share:
                showShare();
                break;
        }
        return true;
    }

    //    评论

    public void getCommentNum(final Menu menu) {
        OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(UrlManage.NEWS_URL + "cmt_num?ver=1& nid="+info.getNid())
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WebActivity.this, "连接服务器失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String strJosn = response.body().string();
                try {
                    JSONObject object = new JSONObject(strJosn);
                    if (object.getString("message").equals("OK")) {
                        final int commentNum = object.getInt("data");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                menu.getItem(1).setTitle("评论  (" + commentNum + ")");
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //    收藏
    private void collectnews() {
        if (CollectNewsdb.insertNews(this, info)) {
            Toast.makeText(this, "收藏成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "已收藏", Toast.LENGTH_SHORT).show();
        }

    }
    private void showShare() {

        ShareSDK.initSDK(this);

        OnekeyShare oks = new OnekeyShare();

        //关闭sso授权

        oks.disableSSOWhenAuthorize();



        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间等使用

        oks.setTitle("标题");

        // titleUrl是标题的网络链接，QQ和QQ空间等使用

        oks.setTitleUrl("http://sharesdk.cn");

        // text是分享文本，所有平台都需要这个字段

        oks.setText("我是分享文本");

        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数

        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片

        // url仅在微信（包括好友和朋友圈）中使用

        oks.setUrl("http://sharesdk.cn");

        // comment是我对这条分享的评论，仅在人人网和QQ空间使用

        oks.setComment("我是测试评论文本");

        // site是分享此内容的网站名称，仅在QQ空间使用

        oks.setSite(getString(R.string.app_name));

        // siteUrl是分享此内容的网站地址，仅在QQ空间使用

        oks.setSiteUrl("http://sharesdk.cn");



// 启动分享GUI

        oks.show(this);

    }
}
