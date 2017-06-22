package com.example.wangzixiong.news;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.bean.BuildBean;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import adapter.UserAdapter;
import bean.UserInfo;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.coll)
    CollapsingToolbarLayout coll;
    @BindView(R.id.cm)
    CircleImageView cm;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.tv1)
    TextView tv1;
    @BindView(R.id.tv2)
    TextView tv2;
    @BindView(R.id.tv3)
    TextView tv3;
    @BindView(R.id.rc)
    RecyclerView rc;
    @BindView(R.id.out)
    Button out;
    private SharedPreferences sp;
    private UserAdapter adapter;
    private TextView camera, photo, back;
    private SharedPreferences.Editor edit;
    private String path;
    private boolean isphotochange;
    private BuildBean buildBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        sp = getSharedPreferences("login", MODE_PRIVATE);
        edit = sp.edit();
        path = sp.getString("path", null);
        isphotochange = sp.getBoolean("isphotochange", false);
        ButterKnife.bind(this);
        settitle();
        rcView();
        cm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = LayoutInflater.from(UserActivity.this).inflate(R.layout.dialog_user, null);
                buildBean = DialogUIUtils.showCustomBottomAlert(UserActivity.this, view);
                buildBean.show();
                camera = (TextView) view.findViewById(R.id.camera);
                photo = (TextView) view.findViewById(R.id.photo);
                back = (TextView) view.findViewById(R.id.back);
                camera.setOnClickListener(UserActivity.this);
                photo.setOnClickListener(UserActivity.this);
                back.setOnClickListener(UserActivity.this);

            }
        });

    }

    private void rcView() {
        rc.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UserAdapter(this, R.layout.user_rc);
        rc.setAdapter(adapter);
        initdata();
    }

    private void initdata() {
        String strj = sp.getString("string", null);
        Log.e("swergth", strj.toString());
        if (strj != null) {
            try {
                JSONObject object = new JSONObject(strj);
                object = object.getJSONObject("data");
                Gson gson = new Gson();
                UserInfo info = gson.fromJson(object.toString(), UserInfo.class);
                Log.e("sdf", info.toString());
                if (isphotochange) {
                    cm.setImageBitmap(BitmapFactory.decodeFile(path));
                } else {
                    Glide.with(this).load(info.getPortrait()).into(cm);
                }

                tv1.setText(info.getUid());
                tv2.setText("积分：" + info.getIntegration());
                tv3.setText("跟帖：" + info.getComnum());

                adapter.add(info.getLoginlog());
                adapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    private void settitle() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("我的帐号");
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            back();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.camera:
                takePhoto();
                break;
            case R.id.photo:
                selectPhoto();
                break;
            case R.id.back:
                break;
        }
        buildBean.dialog.dismiss();
    }

    /**
     * 跳转到系统的拍照功能
     */
    protected void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, OPCAMERA);
    }

    //相机拍照
    private static final int OPCAMERA = 1;
    //手机相册获取
    private static final int OPPHOTO = 2;

    /**
     * 从相册选择
     */
    private void selectPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setType("image/*");
        intent.putExtra("crop", "true");//设置裁剪功能
        intent.putExtra("aspectX", 1); //宽高比例
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 80); //宽高值
        intent.putExtra("outputY", 80);
        intent.putExtra("return-data", true); //返回裁剪结果
        startActivityForResult(intent, OPPHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == OPCAMERA) {
            if (resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                Bitmap bitmap = (Bitmap) bundle.get("data");
                savephoto(bitmap);
                cm.setImageBitmap(bitmap);
            }
        }
        if (requestCode == OPPHOTO) {
            if (resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                Bitmap bitmap = (Bitmap) bundle.get("data");
                savephoto(bitmap);
                cm.setImageBitmap(bitmap);
            }
        }
    }

    private void savephoto(Bitmap bitmap) {
//        显示一个路径，并创建文件夹
        File fileDir = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/photo");
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
//        创建文件
        File file = new File(fileDir, "userphoto.jpg");
        try {
            FileOutputStream fos = new FileOutputStream(file);
//            写入
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
            String path = file.getAbsolutePath();
            edit.putString("path", path);
            edit.putBoolean("isphotochange", true);
            edit.commit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        back();
    }

    //返回结果到上一界面
    private void back() {
        isphotochange = sp.getBoolean("isphotochange", false);
        if (isphotochange) {
            setResult(2);
            finish();
        } else {
            finish();
        }
    }

    @OnClick(R.id.out)
    public void onViewClicked() {
        edit.clear();
        edit.commit();
        setResult(2);
        back();

    }
}
