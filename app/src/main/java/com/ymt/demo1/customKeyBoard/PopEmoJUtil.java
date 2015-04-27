package com.ymt.demo1.customKeyBoard;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.FrameLayout;

import com.ymt.demo1.R;
import com.ymt.demo1.customViews.IndicatorView;

/**
 * Created by Dan on 2015/4/25
 */
public class PopEmoJUtil {

    private static PopEmoJUtil popEmoJUtil;
    private FrameLayout imeTab1;
    private FrameLayout imeTab2;
    private FrameLayout imeTab3;
    private ViewPager mEmoJPager;
    private IndicatorView mIndicator;
    private EmoJPagerAdapter mEmoJAdapter;
    private ConsultActivity mActivity;

    private PopEmoJUtil(Context context) {
        this.mActivity = (ConsultActivity) context;
        this.mEmoJAdapter = new EmoJPagerAdapter(mActivity.getSupportFragmentManager());
    }

    public static PopEmoJUtil getInstance(Context context) {
        if (popEmoJUtil == null) {
            popEmoJUtil = new PopEmoJUtil(context);
        }
        return popEmoJUtil;
    }

    /**
     * 执行适配器操作
     */
    public void doAdapter(View contentView) {
        initContent(contentView);          //布局控件初始化
    }

    /**
     * 初始化popWindow布局
     */
    protected void initContent(View contentView) {

        //表情viewPager
        mEmoJPager = (ViewPager) contentView.findViewById(R.id.ime_viewPager);
        mEmoJPager.setAdapter(mEmoJAdapter);

        //指示器Indicator初始化
        mIndicator = (IndicatorView) contentView.findViewById(R.id.ime_pagerIndicator);
        mIndicator.updateTotal(6);   //设置指示器显示item个数（适配adapter中元素个数）
        mIndicator.setCurr(0);

        //底部tab
        imeTab1 = (FrameLayout) contentView.findViewById(R.id.ime_tab_1);
        imeTab2 = (FrameLayout) contentView.findViewById(R.id.ime_tab_2);
        imeTab3 = (FrameLayout) contentView.findViewById(R.id.ime_tab_3);

         /*
         * 根据表情tab切换设置viewpager滚动
         */
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.ime_tab_1:
                        setImeTab(0);
                        mEmoJPager.setCurrentItem(0);             //滚动到第一类的第一个
                        break;
                    case R.id.ime_tab_2:
                        setImeTab(1);
                        mEmoJPager.setCurrentItem(6);             //滚动到第二类的第一个
                        break;
                    case R.id.ime_tab_3:
                        setImeTab(2);
                        mEmoJPager.setCurrentItem(8);             //滚动到第三类的第一个
                        break;
                    default:
                        break;

                }
            }
        };

        imeTab1.setOnClickListener(clickListener);
        imeTab2.setOnClickListener(clickListener);
        imeTab3.setOnClickListener(clickListener);

        mEmoJPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position < 6) {
                    mIndicator.updateTotal(6);
                    mIndicator.setCurr(position);
                    setImeTab(0);
                } else if (position < 8) {
                    mIndicator.updateTotal(2);
                    mIndicator.setCurr(position % 6);
                    setImeTab(1);
                } else {
                    mIndicator.updateTotal(0);
                    setImeTab(2);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    /**
     * 根据viewpager滑动设置表情tab切换
     */
    protected void setImeTab(int tabID) {
        switch (tabID) {
            case 0:
                imeTab1.setBackgroundColor(mActivity.getResources().getColor(android.R.color.darker_gray));
                imeTab2.setBackgroundColor(mActivity.getResources().getColor(android.R.color.white));
                imeTab3.setBackgroundColor(mActivity.getResources().getColor(android.R.color.white));
                break;
            case 1:
                imeTab1.setBackgroundColor(mActivity.getResources().getColor(android.R.color.white));
                imeTab2.setBackgroundColor(mActivity.getResources().getColor(android.R.color.darker_gray));
                imeTab3.setBackgroundColor(mActivity.getResources().getColor(android.R.color.white));
                break;
            case 2:
                imeTab1.setBackgroundColor(mActivity.getResources().getColor(android.R.color.white));
                imeTab2.setBackgroundColor(mActivity.getResources().getColor(android.R.color.white));
                imeTab3.setBackgroundColor(mActivity.getResources().getColor(android.R.color.darker_gray));
                break;
            default:
                break;
        }

    }

    /**
     * 显示表情的Pager适配器
     */
    class EmoJPagerAdapter extends FragmentStatePagerAdapter {

        public EmoJPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return EmoJFragment.newInstance();
        }

        @Override
        public int getCount() {
            return 9;
        }
    }


}
