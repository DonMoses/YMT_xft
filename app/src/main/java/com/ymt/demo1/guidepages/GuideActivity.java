package com.ymt.demo1.guidepages;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.ymt.demo1.styleTabCircle.CircleMenuActivity;
import com.ymt.demo1.R;

/**
 * Created by Moses on 2015
 */
public class GuideActivity extends FragmentActivity implements ViewPager.OnPageChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        initViews();
    }

    protected void initViews() {
        ViewPager guideVPager = (ViewPager) findViewById(R.id.cf_guide_view_pager);

        FragmentManager manager = getSupportFragmentManager();
        GuidePageAdapter pageAdapter = new GuidePageAdapter(manager);

        guideVPager.setAdapter(pageAdapter);
        guideVPager.setOnPageChangeListener(this);


    }

    @Override
    public void onPageScrolled(int i, float v, int i2) {

    }

    @Override
    public void onPageSelected(int i) {

    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }


    public class GuidePageAdapter extends FragmentStatePagerAdapter {

        public GuidePageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            GuidePageFragment pageFragment = null;
            switch (i) {
                case 0:
                    pageFragment = GuidePageFragment.newInstance(R.drawable.guide1);
                    break;
                case 1:
                    pageFragment = GuidePageFragment.newInstance(R.drawable.guide2);
                    break;
                case 2:
                    pageFragment = GuidePageFragment.newInstance(R.drawable.guide3);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(3000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            startActivity(new Intent(GuideActivity.this, CircleMenuActivity.class));
                            GuideActivity.this.finish();
                        }
                    }).start();
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

    }

}
