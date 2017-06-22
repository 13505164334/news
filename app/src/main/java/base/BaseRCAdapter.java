package base;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangzixiong on 2017/5/24.
 */

public abstract class BaseRCAdapter<T> extends RecyclerView.Adapter<BaseRCAdapter.ViewHolder> {
    public Context mContext;
    private final LayoutInflater inflater;
    private final List<T> data;
    private int mLayoutID;


    public BaseRCAdapter(Context context, int layoutID) {
        this.mContext = context;
        inflater = LayoutInflater.from(context);
        data = new ArrayList<>();
        this.mLayoutID = layoutID;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(mLayoutID, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        getitemViewHolder(holder, position);
    }

    public void add(List<T> list) {
        data.addAll(list);
    }

    public List<T> getList() {
        return data;
    }

    //获得itemViewHolder和Position
    public abstract void getitemViewHolder(ViewHolder holder, int position);

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void clear() {
        data.clear();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        //数组用来保存View
        private final SparseArray<View> viewArray;

        public ViewHolder(View itemView) {
            super(itemView);
            viewArray = new SparseArray<>();
        }

        public <T extends View> T getView(int viewID) {
            View view = viewArray.get(viewID);
            if (view == null) {//如果数组中没有对应的view
                view = itemView.findViewById(viewID);
                //保存到数组中
                viewArray.put(viewID, view);
            }
            return (T) view;
        }
    }
}
