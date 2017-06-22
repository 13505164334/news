package base;

import android.app.Application;

import cn.jpush.android.api.JPushInterface;
import cn.sharesdk.framework.ShareSDK;

/**
 * Created by wangzixiong on 2017/5/27.
 */

public class Baseapp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ShareSDK.initSDK(this,"1e366215c648a ");
        //初始化sdk
        JPushInterface.setDebugMode(true);//正式版的时候设置false，关闭调试
        //        初始化极光推送
        JPushInterface.init(this);
    }
}
