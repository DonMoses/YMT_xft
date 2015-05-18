package com.ymt.demo1.launchpages;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;

import com.ymt.demo1.customViews.obsScrollview.CacheFragmentStatePagerAdapter;
import com.ymt.demo1.customViews.widget.GuideTabStrip;
import com.ymt.demo1.R;

/**
 * Created by Moses on 2015
 */
public class GuideActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edu_guide);
        initViews();
        SharedPreferences mSharedPreferences = getSharedPreferences(MainActivity.SETTING_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(MainActivity.FIRST_LAUNCH_KEY, false);
        editor.apply();
    }

    protected void initViews() {
        ViewPager guideVPager = (ViewPager) findViewById(R.id.cf_guide_view_pager);

        FragmentManager manager = getSupportFragmentManager();
        GuidePageAdapter pageAdapter = new GuidePageAdapter(manager);

        guideVPager.setAdapter(pageAdapter);

        GuideTabStrip tabStrip = (GuideTabStrip) findViewById(R.id.guide_tab);
        tabStrip.setIndicatorColor(getResources().getColor(android.R.color.holo_red_light));
        tabStrip.setViewPager(guideVPager);

    }

    /**
     * This adapter provides two types of fragments as an example.
     * {@linkplain #createItem(int)} should be modified if you use this example for your app.
     */
    private class GuidePageAdapter extends CacheFragmentStatePagerAdapter {

        public GuidePageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        protected Fragment createItem(int position) {
            Fragment pageFragment = null;
            switch (position) {
                case 0:
                    pageFragment = GuidePageFragment.newInstance(R.drawable.guide1);
                    break;
                case 1:
                    pageFragment = GuidePageFragment.newInstance(R.drawable.guide2);
                    break;
                case 2:
                    pageFragment = GuidePageFragment3.newInstance(R.drawable.guide3);
                    break;
                default:
                    break;

            }
            return pageFragment;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "";
        }
    }

}
