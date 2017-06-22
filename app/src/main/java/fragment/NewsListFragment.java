package fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.wangzixiong.news.R;
import com.mugen.Mugen;
import com.mugen.MugenCallbacks;
import com.mugen.attachers.BaseAttacher;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import adapter.NewsListAdapter;
import base.BaseFragment;
import bean.NewsInfo;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import util.UrlManage;

/**
 * Created by Administrator on 2017/5/11.
 */

public class NewsListFragment extends BaseFragment {


    @BindView(R.id.rc)
    RecyclerView rc;
    Unbinder unbinder;
    @BindView(R.id.sl)
    SwipeRefreshLayout sl;
    private int subid;
    private NewsListAdapter adapter;
    //判断是否正在加载更多
    private boolean isLoading = false;

    public static NewsListFragment getNewsFragment(int subid) {
        NewsListFragment listFragment = new NewsListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("subid", subid);
        listFragment.setArguments(bundle);
        return listFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        subid = getArguments().getInt("subid");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_newslist, container, false);
        unbinder = ButterKnife.bind(this, view);
        adapter = new NewsListAdapter(getActivity());
        rc.setLayoutManager(new LinearLayoutManager(getActivity()));
        rc.setItemAnimator(new DefaultItemAnimator());
        rc.setAdapter(adapter);
        sl.setColorSchemeResources(R.color.blue);
        sl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.clear();
                adapter.notifyDataSetChanged();
                getNewsData(1);
            }
        });
        getNewsData(1);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        BaseAttacher attacher = Mugen.with(rc, new MugenCallbacks() {
            @Override
            public void onLoadMore() {
                Toast.makeText(getActivity(), "开始加载", Toast.LENGTH_SHORT).show();
                getNewsData(2);
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

    public void getNewsData(int dir) {
        int nid;
        String stamp;
        if (dir == 2) {
            //获得最后一条新闻ID
            nid = adapter.getList().get(adapter.getItemCount() - 1).getNid();
            stamp = adapter.getList().get(adapter.getItemCount() - 1).getStamp();
            isLoading = true;
        } else {
            nid = 0;
            stamp = "0";
        }

        OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(UrlManage.NEWS_URL + "news_list?ver=1&subid=" + subid + "&dir=" + dir + "&nid=" + nid + "&stamp=" + stamp + "&cnt=20")
                .build();
        Log.e("12345",UrlManage.NEWS_URL + "news_list?ver=1&subid=" + subid + "&dir=" + dir + "&nid=" + nid + "&stamp=" + stamp + "&cnt=20");
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        sl.setRefreshing(false);
                        isLoading = false;
                        Toast.makeText(getActivity(), "连接服务器失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String srtJson = response.body().string();
                try {
                    JSONObject object = new JSONObject(srtJson);
                    if (object.getString("message").equals("OK")) {
                        JSONArray array = object.getJSONArray("data");
                        for (int i = 0; i < array.length(); i++) {
                            NewsInfo info = new NewsInfo();
                            object = array.getJSONObject(i);
                            info.setIcon(object.getString("icon"));
                            info.setLink(object.getString("link"));
                            info.setNid(object.getInt("nid"));
                            info.setStamp(object.getString("stamp"));
                            info.setSummary(object.getString("summary"));
                            info.setTitle(object.getString("title"));
                            info.setType(object.getInt("type"));
                            adapter.add(info);
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                                //关闭刷新动画
                                sl.setRefreshing(false);
                                isLoading = false;
                            }
                        });
                    } else {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity(), "没了", Toast.LENGTH_SHORT).show();
                                isLoading = false;
                                sl.setRefreshing(false);
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
