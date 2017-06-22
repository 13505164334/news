package com.example.wangzixiong.news;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;
import util.CommonUtil;
import util.OkHttpUtil;
import util.UrlManage;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.et1)
    EditText et1;
    @BindView(R.id.et2)
    EditText et2;
    @BindView(R.id.et3)
    EditText et3;
    @BindView(R.id.in)
    Button in;
    @BindView(R.id.iv)
    ImageView iv;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.cb)
    CheckBox cb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);


    }

    private void getdata() {
        final String name = et1.getText().toString().trim();
        final String email = et2.getText().toString().trim();
        final String password = et3.getText().toString().trim();
        if (name.length() == 0) {
            et1.setError("用户名不能为空");
            return;
        }
        if (email.length() == 0) {
            et2.setError("邮箱不能为空");
            return;
        }
        if (password.length() == 0) {
            et3.setError("密码不能为空");
            return;
        }
        if (!CommonUtil.verifyEmail(email)) {
            et2.setError("邮箱格式错误");
            return;
        }
        if (!(password.length() >= 6 && password.length() <= 16)) {
            et3.setError("密码格式：6~16位数字和字母");
            return;
        }
        String url = UrlManage.NEWS_URL + "user_register?ver=1 &uid=" + name + "&email=" + email + "&pwd=" + password;
        OkHttpUtil.getOkHttp(url, this, new OkHttpUtil.CallBack() {
            @Override
            public void onResponse(Call call, String string) {
                try {
                    JSONObject object = new JSONObject(string);
                    if (object.getString("message").equals("OK")) {
                        object = object.getJSONObject("data");
                        if (object.getString("explain").equals("注册成功")) {
//                            String token = object.getString("token");
                            Intent data = new Intent();
                            Bundle bundle = new Bundle();
                            bundle.putString("name", name);
                            bundle.putString("password", password);
                            data.putExtras(bundle);
                            setResult(2, data);
                            finish();
                        } else {
                            Toast.makeText(RegisterActivity.this, object.getString("explain"), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(RegisterActivity.this, string, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(RegisterActivity.this, "链接失败", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @OnClick({R.id.iv, R.id.in})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv:
                finish();
                break;
            case R.id.in:
                getdata();
                break;
        }
    }
}