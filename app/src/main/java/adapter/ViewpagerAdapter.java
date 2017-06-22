package adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.example.wangzixiong.news.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/10.
 */

public class ViewpagerAdapter extends PagerAdapter {
    int[] saveimages = {R.drawable.bd, R.drawable.wy, R.drawable.welcome};
    List<ImageView> list;
    Context context;

    public ViewpagerAdapter(Context context) {
        this.context = context;
        list = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return saveimages.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(list.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setImageResource(saveimages[position]);
        container.addView(imageView);
        list.add(imageView);
        return imageView;
    }
}
