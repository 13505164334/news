package com.example.wangzixiong.news;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import base.BaseActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;
import util.CommonUtil;
import util.OkHttpUtil;
import util.UrlManage;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.iv)
    ImageView iv;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.sv_login)
    SurfaceView sv;
    @BindView(R.id.et1)
    EditText et1;
    @BindView(R.id.et2)
    EditText et2;
    @BindView(R.id.tv1)
    TextView tv1;
    @BindView(R.id.tv2)
    TextView tv2;
    @BindView(R.id.in)
    Button in;
    private SurfaceHolder svHolder;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mediaPlayer = new MediaPlayer();

        try {
            AssetFileDescriptor fd = getAssets().openFd("welcome.mp4");
            long offest = fd.getStartOffset();
            long len = fd.getLength();
            //加载资源
            mediaPlayer.setDataSource(fd.getFileDescriptor(), offest, len);
            //开始准备
            mediaPlayer.prepare();
            //循环播放
            mediaPlayer.setLooping(true);
            //获得SurfaceHolder对象
            svHolder = sv.getHolder();
            //准备工作完成监听
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {//准备完成是调用
                    //播放
                    mediaPlayer.start();
                }
            });

            svHolder.addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder holder) {
                    //添加SurfaceHolder对象.让SurfaceView播放视频
                    mediaPlayer.setDisplay(svHolder);
                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

                }

                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == 2) {
                Bundle bundle = data.getExtras();
                String name = bundle.getString("name");
                String password = bundle.getString("password");
                et1.setText(name);
                et2.setText(password);
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mediaPlayer.pause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mediaPlayer.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.reset();
    }

    @OnClick({R.id.tv1, R.id.in})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv1:
                Intent intent = new Intent(this, RegisterActivity.class);
                startActivityForResult(intent, 1);
                break;
            case R.id.in:
                getdata();
                break;
        }
    }

    private void getdata() {
        final String name = et1.getText().toString().trim();
        final String password = et2.getText().toString().trim();
        if (name.length() == 0) {
            et1.setError("用户名不能为空");
            return;
        }
        if (password.length() == 0) {
            et2.setError("密码不能为空");
            return;
        }
        if (!(password.length() >= 6 && password.length() <= 16)) {
            et2.setError("密码格式：6~16位数字和字母");
            return;
        }
        String url = UrlManage.NEWS_URL + "user_login?ver=1&uid=" + name + "&pwd=" + password + "&device=0";
        OkHttpUtil.getOkHttp(url, this, new OkHttpUtil.CallBack() {
            @Override
            public void onResponse(Call call, String string) {
                try {
                    Log.e("sdf", string);
                    JSONObject object = new JSONObject(string);
                    if (object.getString("message").equals("OK")) {
                        object = object.getJSONObject("data");
//                        Log.e("dfdfg",object.getString("explain"));
                        if (object.getString("explain").equals("登录成功")) {
                            String token = object.getString("token");
                            Intent data = new Intent();
                            Bundle bundle = new Bundle();
                            bundle.putString("token", token);
                            data.putExtras(bundle);
                            setResult(2, data);
                            LoginActivity.this.finish();
                        }
                    }
                }  catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(LoginActivity.this, "链接失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
