package adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.wangzixiong.news.R;
import com.example.wangzixiong.news.WebActivity;
import java.util.ArrayList;
import java.util.List;

import bean.NewsInfo;

/**
 * Created by Administrator on 2017/5/16.
 */

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.ViewHolder> {
    private LayoutInflater inflater;
    private List<NewsInfo> list;
    private Context mContext;

    public NewsListAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        list = new ArrayList<>();
        this.mContext = context;
    }

    public void add(NewsInfo info) {
        list.add(info);
    }

    public void addNews(List<NewsInfo> mLst){
        list.addAll(mLst);
    }

    public void clear(){
        list.clear();
    }

    public List<NewsInfo> getList(){
        return list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_recycnews, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final NewsInfo info = list.get(position);
        Glide.with(mContext).load(info.getIcon()).into(holder.iv);
        holder.tv_title.setText(info.getTitle());
        holder.tv_summary.setText(info.getSummary());
        holder.tv_stamp.setText(info.getStamp());
        holder.itemVIew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, WebActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("info",info);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
        holder.itemVIew.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onItemLongCilckLinisener!=null){
                    onItemLongCilckLinisener.itemLongCilckLinisener(position);
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView iv;
        TextView tv_title, tv_summary, tv_stamp;
        View itemVIew;

        public ViewHolder(View itemView) {
            super(itemView);
            iv = (ImageView) itemView.findViewById(R.id.iv);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_summary = (TextView) itemView.findViewById(R.id.tv_summary);
            tv_stamp = (TextView) itemView.findViewById(R.id.tv_stamp);
            this.itemVIew = itemView;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    private OnItemLongCilckLinisener onItemLongCilckLinisener;

    public OnItemLongCilckLinisener getOnItemLongCilckLinisener() {
        return onItemLongCilckLinisener;
    }

    public void setOnItemLongCilckLinisener(OnItemLongCilckLinisener onItemLongCilckLinisener) {
        this.onItemLongCilckLinisener = onItemLongCilckLinisener;
    }

    public interface OnItemLongCilckLinisener{
        void itemLongCilckLinisener(int position);
    }
}
