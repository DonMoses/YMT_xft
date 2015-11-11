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

package com.ymt.demo1.plates.knowledge;

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
public class KnowledgePagerTabParentFragment extends BaseFragment {
    public static final String FRAGMENT_TAG = "fragment";

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

        return view;
    }

    /**
     * This adapter provides two types of fragments as an example.
     * {@linkplain #createItem(int)} should be modified if you use this example for your app.
     */
    private static class NavigationAdapter extends CacheFragmentStatePagerAdapter {

        private static final String[] TITLES = new String[]{
//                "机构",             // TODO: 2015/9/17 暂时屏蔽三个tab项
//                "行业",
//                "系统",
                "科研",
                "规范",
                "视频",
                "数据库"
        };

        public NavigationAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        protected Fragment createItem(int position) {
            //todo 根据类型返回不同接口的内容。 这里使用KnowledgeTabScrollUltraListViewFragment演示
            Fragment f;
            switch (TITLES[position]) {
                case "机构":
                    f = new KnowledgeJgFragment();
                    break;
                case "行业":
                    f = new KnowledgeHyFragment();
                    break;
                case "系统":
                    f = new KnowledgeHyFragment();
                    break;
                case "规范":
                    f = KnowledgeItemListViewFragment.getNewInstance(KnowledgeItemListViewFragment.KNOWLEDGE_BZGF);
                    break;
                case "科研":
                    f = KnowledgeItemListViewFragment.getNewInstance(KnowledgeItemListViewFragment.KNOWLEDGE_KYWX);
                    break;
                case "视频":
                    f = new KnowledgeVideoFragment();
                    break;
                case "数据库":
                    f = KnowledgeItemListViewFragment.getNewInstance(KnowledgeItemListViewFragment.KNOWLEDGE_SJK);
                    break;
                default:
                    f = new Fragment();
                    break;
            }
            return f;
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
