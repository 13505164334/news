package fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;


import com.example.wangzixiong.news.R;

import java.util.List;
import adapter.NewsListAdapter;
import base.BaseFragment;
import bean.NewsInfo;
import db.CollectNewsdb;

/**
 * Created by Administrator on 2017/5/16.
 */

public class CollectFragment extends BaseFragment {
    private NewsInfo info;
    private NewsListAdapter adapter;
    private LayoutInflater minflater;
    private TextView all;
    private TextView one;
    private RecyclerView rc;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.minflater = inflater;
        View view = inflater.inflate(R.layout.fargment_favorite, container, false);
        rc = (RecyclerView) view.findViewById(R.id.rc);
        adapter = new NewsListAdapter(getActivity());
        rc.setLayoutManager(new LinearLayoutManager(getActivity()));
        rc.setItemAnimator(new DefaultItemAnimator());
        getSaveNews();
        rc.setAdapter(adapter);
        adapter.setOnItemLongCilckLinisener(new NewsListAdapter.OnItemLongCilckLinisener() {
            @Override
            public void itemLongCilckLinisener(int position) {
                info = adapter.getList().get(position);
                showpop(position);
            }
        });
        return view;
    }


    public void getSaveNews() {
        adapter.clear();
        new Thread(new Runnable() {
            @Override
            public void run() {
                CollectNewsdb collectNewsdb = new CollectNewsdb();
                final List<NewsInfo> quarry = collectNewsdb.quary(getActivity());
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.addNews(quarry);
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }).start();
    }

    //弹出Popupwindow
    PopupWindow popupWindow;
    View popview;
    private void showpop(int position) {
        popupWindow = new PopupWindow(getActivity());
        popview = minflater.inflate(R.layout.pop_collect, null);
        //设置宽高
        popupWindow.setWidth(rc.getWidth());
        popupWindow.setHeight(150);
        //设置显示内容
        popupWindow.setContentView(popview);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(getActivity().getResources().getDrawable(R.color.black));
        popupWindow.showAtLocation(rc, Gravity.BOTTOM, 0, 0);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {

            }
        });
        intiView(position);

    }

    private void intiView(final int position) {
        one = (TextView) popview.findViewById(R.id.tv_deleteOne);
        all = (TextView) popview.findViewById(R.id.tv_deleteAll);
        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CollectNewsdb.delete(getActivity(), info);
                //参数一：当前删除的位置，参数二：后面要刷新的位置
                adapter.notifyDataSetChanged();
               // adapter.notifyItemRangeChanged(position,);
                getSaveNews();
                popupWindow.dismiss();
            }
        });

        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CollectNewsdb.deleteall(getActivity());
                adapter.notifyDataSetChanged();
                getSaveNews();
                popupWindow.dismiss();
            }
        });
    }
}
