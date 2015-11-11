/*
 * Copyright 2014 Soichiro Kashima
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ymt.demo1.plates.news;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ymt.demo1.R;
import com.ymt.demo1.baseClasses.BaseFragment;
import com.ymt.demo1.customViews.obsScrollview.CacheFragmentStatePagerAdapter;
import com.ymt.demo1.customViews.widget.PagerSlidingTabStrip;

/**
 * This fragment manages ViewPager and its child Fragments.
 * Scrolling techniques are basically the same as ViewPagerTab2Activity.
 */
public class PagerTabParentFragment extends BaseFragment {
    public static final String FRAGMENT_TAG = "fragment";
    private int tabPosition;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_knowledge_pagertabfragment_parent, container, false);
        NavigationAdapter mPagerAdapter = new NavigationAdapter(getChildFragmentManager());
        ViewPager mPager = (ViewPager) view.findViewById(R.id.pager);
        mPager.setAdapter(mPagerAdapter);

        PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip) view.findViewById(R.id.sliding_tabs);
        tabStrip.setIndicatorColor(getResources().getColor(android.R.color.holo_blue_light));
        tabStrip.setIndicatorHeight(5);
        tabStrip.setDividerColor(getResources().getColor(android.R.color.holo_blue_bright));

        mPager.setAdapter(mPagerAdapter);
        mPager.setOffscreenPageLimit(4);
        tabStrip.setViewPager(mPager);         //////////////////

        mPager.setCurrentItem(tabPosition);
        return view;
    }

    public static PagerTabParentFragment newInstanceWithPosition(int position) {
        PagerTabParentFragment pagerTabParentFragment = new PagerTabParentFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        pagerTabParentFragment.setArguments(bundle);
        return pagerTabParentFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tabPosition = getArguments().getInt("position");
    }

    /**
     * This adapter provides two types of fragments as an example.
     * {@linkplain #createItem(int)} should be modified if you use this example for your app.
     */
    private static class NavigationAdapter extends CacheFragmentStatePagerAdapter {

        private static final String[] TITLES = new String[]{"消防新闻"
                , "消防公告"
//                , "教育资讯"  todo 教育咨询没有内容，暂时屏蔽
        };

        public NavigationAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        protected Fragment createItem(int position) {
            Fragment fragment = null;
            //todo 根据类型返回不同接口的内容。 这里使用KnowledgeTabScrollUltraListViewFragment演示
            switch (getPageTitle(position).toString()) {
                case "消防新闻":
                    fragment = new FireNewsFragment();
                    break;
                case "消防公告":
                    fragment = FileNoticeFragment.newInstance("xf_article_h_notice");
                    break;
//                case "教育资讯":
//                    fragment = EduNewsFragment.newInstance("xf_article_h_edu");
//                    break;
                //// TODO: 2015/10/23  教育资讯
                default:
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }
    }
}
