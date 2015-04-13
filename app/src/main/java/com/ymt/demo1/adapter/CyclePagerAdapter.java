package com.ymt.demo1.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import java.util.ArrayList;

/**
 * 实现了循环的PagerAdapter
 */
public class CyclePagerAdapter extends PagerAdapter {

    private ArrayList<View> viewList = new ArrayList<>();

    public void setViews(ArrayList<View> viewList) {
        this.viewList = viewList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        //设置成最大，使用户看不到边界
//        return Integer.MAX_VALUE;
        return viewList.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public void destroyItem(ViewGroup container, int position,
                            Object object) {
        //Warning：不要在这里调用removeView
        container.removeView(viewList.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        //对ViewPager页号求模取出View列表中要显示的项
//        position %= viewList.size();
//        if (position < 0) {
//            position = viewList.size() + position;
//        }
//        View view = viewList.get(position);
//        //如果View已经在之前添加到了一个父组件，则必须先remove，否则会抛出IllegalStateException。
//        ViewParent vp = view.getParent();
//        if (vp != null) {
//            ViewGroup parent = (ViewGroup) vp;
//            parent.removeView(view);
//        }
//        container.addView(view);
//        //add listeners here if necessary
//        return view;

        container.addView(viewList.get(position));
        return viewList.get(position);
    }
}