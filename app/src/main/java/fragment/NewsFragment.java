package fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.wangzixiong.news.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import adapter.FragmentViewPagerAdapter;
import base.BaseFragment;
import bean.NewsType;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/5/16.
 */

public class NewsFragment extends BaseFragment implements View.OnClickListener{

    @BindView(R.id.tl_home)
    TabLayout tlHome;
    @BindView(R.id.vp_home)
    ViewPager vpHome;
    @BindView(R.id.fab_home)
    FloatingActionButton fabHome;
    Unbinder unbinder;
    FragmentViewPagerAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fargment_news, container, false);
        unbinder = ButterKnife.bind(this, view);
        fabHome = (FloatingActionButton) view.findViewById(R.id.fab_home);
        fabHome.setOnClickListener(this);
        intiEvent();
        return view;
    }

    private void intiEvent() {
        adapter = new FragmentViewPagerAdapter(getActivity().getSupportFragmentManager());
        getData();
        vpHome.setAdapter(this.adapter);

        //给ViewPager设置指示器
        tlHome.setupWithViewPager(vpHome);
        tlHome.setSelectedTabIndicatorColor(Color.parseColor("#990000"));
        tlHome.setTabTextColors(Color.parseColor("#000000"),Color.parseColor("#ff0000"));


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void getData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("http://118.244.212.82:9092/newsClient/news_sort?ver=1&imei=1")
                        .build();
                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Toast.makeText(getActivity(), "连接失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String strJson = response.body().string();
                        try {
                            JSONObject jsonObject = new JSONObject(strJson);
                            if (jsonObject.getString("message").equals("OK")){
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i <jsonArray.length() ; i++) {
                                    jsonObject = jsonArray.getJSONObject(i);
                                    JSONArray subArray= jsonObject.getJSONArray("subgrp");
                                    for (int j = 0; j < subArray.length(); j++) {
                                        jsonObject = subArray.getJSONObject(j);
                                        final NewsType newsType = new NewsType(jsonObject.getString("subgroup"), jsonObject.getInt("subid"));
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                adapter.addTitle(newsType);
                                                addNewsListData(newsType.getSubid());
                                                adapter.notifyDataSetChanged();
                                            }
                                        });
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }).start();
    }

    private void addNewsListData(int subid) {
        adapter.add(NewsListFragment.getNewsFragment(subid));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_home:
                fabHome.setImageResource(R.drawable.floatfill);
                Snackbar.make(fabHome, "悬浮", 0)
                        .show();
                break;
        }
    }
}
