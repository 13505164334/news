package adapter;

import android.content.Context;

import android.widget.TextView;

import com.example.wangzixiong.news.R;

import java.util.List;

import base.BaseRCAdapter;
import bean.UserInfo;

/**
 * Created by wangzixiong on 2017/5/24.
 */

public class UserAdapter extends BaseRCAdapter<UserInfo.Loginlog> {

    public UserAdapter(Context context, int layoutID) {
        super(context, layoutID);
    }

    @Override
    public void getitemViewHolder(ViewHolder holder, int position) {
        List<UserInfo.Loginlog> list = getList();
        TextView time = holder.getView(R.id.time);
        time.setText(list.get(position).getTime());
        TextView address = holder.getView(R.id.address);
        address.setText(list.get(position).getAddress());
        TextView device = holder.getView(R.id.device);
        device.setText(list.get(position).getDevice() == 0 ? "手机" : "PC");
    }
}
