package com.ymt.demo1.guidepages;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;

import com.ymt.demo1.customViews.obsScrollview.CacheFragmentStatePagerAdapter;
import com.ymt.demo1.customViews.widget.GuideTabStrip;
import com.ymt.demo1.styleTabCircle.CircleMenuActivity;
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
            GuidePageFragment pageFragment = null;
            switch (position) {
                case 0:
                    pageFragment = GuidePageFragment.newInstance(R.drawable.guide_1);
                    break;
                case 1:
                    pageFragment = GuidePageFragment.newInstance(R.drawable.guide_2);
                    break;
                case 2:
                    pageFragment = GuidePageFragment.newInstance(R.drawable.guide_3);
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

        @Override
        public CharSequence getPageTitle(int position) {
            return "";
        }
    }

}
