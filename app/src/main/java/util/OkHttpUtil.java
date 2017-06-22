package util;

import android.app.Activity;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by wangzixiong on 2017/5/22.
 */

public class OkHttpUtil {
    public static void getOkHttp(String url, final Activity activity, final CallBack callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        callback.onFailure(call, e);
                    }
                });
            }

            @Override
            public void onResponse(final Call call, final Response response) throws IOException {
                final String string = response.body().string();
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        callback.onResponse(call, string);
                    }
                });
            }
        });
    }

    public interface CallBack {
        void onResponse(Call call, String string);

        void onFailure(Call call, IOException e);
    }
}
