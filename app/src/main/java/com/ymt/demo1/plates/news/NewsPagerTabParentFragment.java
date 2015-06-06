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

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.ymt.demo1.R;
import com.ymt.demo1.baseClasses.BaseFragment;
import com.ymt.demo1.baseClasses.ViewHelper;
import com.ymt.demo1.customKeyBoard.ScreenSizeUtil;
import com.ymt.demo1.customViews.obsScrollview.CacheFragmentStatePagerAdapter;
import com.ymt.demo1.customViews.obsScrollview.ObservableScrollViewCallbacks;
import com.ymt.demo1.customViews.obsScrollview.ScrollState;
import com.ymt.demo1.customViews.obsScrollview.ScrollUtils;
import com.ymt.demo1.customViews.obsScrollview.Scrollable;
import com.ymt.demo1.customViews.obsScrollview.TouchInterceptionFrameLayout;
import com.ymt.demo1.customViews.widget.PagerSlidingTabStrip;
import com.ymt.demo1.main.SearchViewUtil;

/**
 * This fragment manages ViewPager and its child Fragments.
 * Scrolling techniques are basically the same as ViewPagerTab2Activity.
 */
public class NewsPagerTabParentFragment extends BaseFragment implements ObservableScrollViewCallbacks {
    public static final String FRAGMENT_TAG = "fragment";

    private TouchInterceptionFrameLayout mInterceptionLayout;
    private ViewPager mPager;
    private NavigationAdapter mPagerAdapter;
    private int mSlop;
    private boolean mScrolled;
    private ScrollState mLastScrollState;
    private SearchViewUtil searchViewUtil;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_knowledge_pagertabfragment_parent, container, false);
        //初始化搜索界面
        searchViewUtil = new SearchViewUtil();
        ActionBarActivity parentActivity = (ActionBarActivity) getActivity();
        mPagerAdapter = new NavigationAdapter(getChildFragmentManager());
        mPager = (ViewPager) view.findViewById(R.id.pager);
        mPager.setAdapter(mPagerAdapter);

        PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip) view.findViewById(R.id.sliding_tabs);
        tabStrip.setIndicatorColor(getResources().getColor(android.R.color.holo_blue_light));
        tabStrip.setIndicatorHeight(5);
        tabStrip.setDividerColor(getResources().getColor(android.R.color.holo_blue_bright));

        mPager.setAdapter(mPagerAdapter);
        mPager.setOffscreenPageLimit(4);
        tabStrip.setViewPager(mPager);         //////////////////

        ViewConfiguration vc = ViewConfiguration.get(parentActivity);
        mSlop = vc.getScaledTouchSlop();
        mInterceptionLayout = (TouchInterceptionFrameLayout) view.findViewById(R.id.container);
        mInterceptionLayout.setScrollInterceptionListener(mInterceptionListener);

        /*
        将pager 视图移到导航栏上方（解决被导航栏遮挡底部的问题）
         */
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(0, 0, 0, ScreenSizeUtil.getNavigationBarHeight());
        mPager.setLayoutParams(layoutParams);
        return view;
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        searchViewUtil.clearInputFocus();
    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //初始化搜索界面
        searchViewUtil.initSearchView(getActivity());
    }

    /**
     * @param view：被测量的view
     */
    public static boolean checkDownPointerInView(View view, float x, float y) {
        int[] location2 = new int[2];
        view.getLocationOnScreen(location2);
        return x >= location2[0] && x <= location2[0] + view.getWidth() && y >= location2[1] && y <= location2[1] + view.getHeight();
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
        if (!mScrolled) {
            // This event can be used only when TouchInterceptionFrameLayout
            // doesn't handle the consecutive events.
            adjustToolbar(scrollState);
        }
    }

    private TouchInterceptionFrameLayout.TouchInterceptionListener mInterceptionListener = new TouchInterceptionFrameLayout.TouchInterceptionListener() {
        @Override
        public boolean shouldInterceptTouchEvent(MotionEvent ev, boolean moving, float diffX, float diffY) {
            if (!mScrolled && mSlop < Math.abs(diffX) && Math.abs(diffY) < Math.abs(diffX)) {
                // Horizontal scroll is maybe handled by ViewPager
                return false;
            }

            Scrollable scrollable = getCurrentScrollable();
            if (scrollable == null) {
                mScrolled = false;
                return false;
            }

            // If interceptionLayout can move, it should intercept.
            // And once it begins to move, horizontal scroll shouldn't work any longer.
            View infoView = getActivity().findViewById(R.id.knowledge_search_layout);
            int infoViewHeight = infoView.getHeight();
            int translationY = (int) ViewHelper.getTranslationY(mInterceptionLayout);
            boolean scrollingUp = 0 < diffY;
            boolean scrollingDown = diffY < 0;
            if (scrollingUp) {
                if (translationY < 0) {
                    mScrolled = true;
                    mLastScrollState = ScrollState.UP;
                    return true;
                }
            } else if (scrollingDown) {
                if (-infoViewHeight < translationY) {
                    mScrolled = true;
                    mLastScrollState = ScrollState.DOWN;
                    return true;
                }
            }
            mScrolled = false;
            return false;
        }

        @Override
        public void onDownMotionEvent(MotionEvent ev) {
            int action = ev.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    float x = ev.getRawX();
                    float y = ev.getRawY();
                    if (!checkDownPointerInView(getActivity().findViewById(R.id.search_edit_text), x, y)) {
                        searchViewUtil.clearInputFocus();
                    }
            }
        }

        @Override
        public void onMoveMotionEvent(MotionEvent ev, float diffX, float diffY) {
            View infoView = getActivity().findViewById(R.id.knowledge_search_layout);
            float translationY = ScrollUtils.getFloat(
                    ViewHelper.getTranslationY(mInterceptionLayout) + diffY, -infoView.getHeight(), 0);
            ViewHelper.setTranslationY(mInterceptionLayout, translationY);
            ViewHelper.setTranslationY(infoView, translationY);
            if (translationY < 0) {
                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mInterceptionLayout.getLayoutParams();
                lp.height = (int) (-translationY + getScreenHeight());
                mInterceptionLayout.requestLayout();
            }
        }

        @Override
        public void onUpOrCancelMotionEvent(MotionEvent ev) {
            mScrolled = false;
            adjustToolbar(mLastScrollState);
        }
    };

    private Scrollable getCurrentScrollable() {
        Fragment fragment = getCurrentFragment();
        if (fragment == null) {
            return null;
        }
        View view = fragment.getView();
        if (view == null) {
            return null;
        }
        return (Scrollable) view.findViewById(R.id.scroll);
    }

    private void adjustToolbar(ScrollState scrollState) {
        View infoView = getActivity().findViewById(R.id.knowledge_search_layout);
        int toolbarHeight = infoView.getHeight();
        final Scrollable scrollable = getCurrentScrollable();
        if (scrollable == null) {
            return;
        }
        int scrollY = scrollable.getCurrentScrollY();
        if (scrollState == ScrollState.DOWN) {
            showToolbar();
        } else if (scrollState == ScrollState.UP) {
            if (toolbarHeight <= scrollY) {
                hideToolbar();
            } else {
                showToolbar();
            }
        } else if (!toolbarIsShown() && !toolbarIsHidden()) {
            // Toolbar is moving but doesn't know which to move:
            // you can change this to hideToolbar()
            showToolbar();
        }
    }

    private Fragment getCurrentFragment() {
        return mPagerAdapter.getItemAt(mPager.getCurrentItem());
    }

    private boolean toolbarIsShown() {
        return ViewHelper.getTranslationY(mInterceptionLayout) == 0;
    }

    private boolean toolbarIsHidden() {
        View view = getView();
        if (view == null) {
            return false;
        }
        View infoView = getActivity().findViewById(R.id.knowledge_search_layout);
        return ViewHelper.getTranslationY(mInterceptionLayout) == -infoView.getHeight();
    }

    private void showToolbar() {
        animateToolbar(0);
    }

    private void hideToolbar() {
        View infoView = getActivity().findViewById(R.id.knowledge_search_layout);
        animateToolbar(-infoView.getHeight());
    }

    private void animateToolbar(final float toY) {
        float layoutTranslationY = ViewHelper.getTranslationY(mInterceptionLayout);
        if (layoutTranslationY != toY) {
            ValueAnimator animator = ValueAnimator.ofFloat(ViewHelper.getTranslationY(mInterceptionLayout), toY).setDuration(200);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float translationY = (float) animation.getAnimatedValue();
                    View infoView = getActivity().findViewById(R.id.knowledge_search_layout);
                    ViewHelper.setTranslationY(mInterceptionLayout, translationY);
                    ViewHelper.setTranslationY(infoView, translationY);
                    if (translationY < 0) {
                        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mInterceptionLayout.getLayoutParams();
                        lp.height = (int) (-translationY + getScreenHeight());
                        mInterceptionLayout.requestLayout();
                    }
                }
            });
            animator.start();
        }
    }

    /**
     * This adapter provides two types of fragments as an example.
     * {@linkplain #createItem(int)} should be modified if you use this example for your app.
     */
    private static class NavigationAdapter extends CacheFragmentStatePagerAdapter {

        private static final String[] TITLES = new String[]{"消防新闻", "消防公告", "教育资讯"};

        public NavigationAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        protected Fragment createItem(int position) {
            Fragment fragment = null;
            //todo 根据类型返回不同接口的内容。 这里使用KnowledgeTabScrollUltraListViewFragment演示
            switch (getPageTitle(position).toString()) {
                case "消防新闻":
                    fragment = NewsFragment.newInstance("xf_article_h_news");
                    break;
                case "消防公告":
                    fragment = NewsFragment.newInstance("xf_article_h_notice");
                    break;
                case "教育资讯":
                    fragment = NewsFragment.newInstance("xf_article_h_edu");
                    break;
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
