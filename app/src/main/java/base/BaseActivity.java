package base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;

/**
 * Created by Administrator on 2017/5/10.
 */

public class BaseActivity extends AppCompatActivity {


    private String name;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        name = this.getClass().getCanonicalName();
        Log.e("onCreate",name);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    /*界面跳转，不结束当前界面*/
    protected void startactivity(Class<?> mclass) {
        Intent intent = new Intent(this, mclass);
        startActivity(intent);
    }

    /*界面跳转，不结束当前界面,并且传值*/
    protected void startactivity(Class<?> mclass, Bundle bundle) {
        Intent intent = new Intent(this, mclass);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /*界面跳转，结束当前界面*/
    protected void startactivityFinish(Class<?> mclass) {
        Intent intent = new Intent(this, mclass);
        startActivity(intent);
        this.finish();

    }
}
