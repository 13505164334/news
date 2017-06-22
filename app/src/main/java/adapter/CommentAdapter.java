package adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.wangzixiong.news.R;

import java.util.List;

import base.BaseRCAdapter;
import bean.CommentItem;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by wangzixiong on 2017/5/25.
 */

public class CommentAdapter extends BaseRCAdapter<CommentItem> {
    public CommentAdapter(Context context, int layoutID) {
        super(context, layoutID);
    }

    @Override
    public void getitemViewHolder(ViewHolder holder, int position) {
        List<CommentItem> list = getList();
        CommentItem info = list.get(position);
        TextView tv1 = holder.getView(R.id.tv1);
        tv1.setText(info.getUid());
        TextView tv2 = holder.getView(R.id.tv2);
        tv2.setText(info.getStamp());
        TextView tv3 = holder.getView(R.id.tv3);
        tv3.setText(info.getContent());
        Glide.with(mContext).load(info.getPortrait()).into((CircleImageView) holder.getView(R.id.cv));
    }
}
