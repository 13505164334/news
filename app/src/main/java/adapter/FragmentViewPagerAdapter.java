package adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import bean.NewsType;
import fragment.NewsListFragment;

/**
 * Created by Administrator on 2017/5/11.
 */

public class FragmentViewPagerAdapter extends FragmentPagerAdapter {
    List<NewsListFragment> list ;
    List<NewsType> listTitle;

    public FragmentViewPagerAdapter(FragmentManager fm) {
        super(fm);
        list = new ArrayList<>();
        listTitle = new ArrayList<>();
    }

    public void add(NewsListFragment fragment){
         list.add(fragment);
    }

    public void addTitle(NewsType s){
        listTitle.add(s);
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return listTitle.get(position).getSubgroup();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
    }
}
