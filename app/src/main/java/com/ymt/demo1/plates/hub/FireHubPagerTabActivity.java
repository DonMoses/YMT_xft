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

package com.ymt.demo1.plates.hub;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ymt.demo1.R;
import com.ymt.demo1.baseClasses.BaseActivity;
import com.ymt.demo1.baseClasses.FireHubPagerTabParentFragment;
import com.ymt.demo1.main.AppContext;

/**
 * This activity just provides a toolbar.
 * Toolbar is manipulated by ViewPagerTabFragmentParentFragment.
 */
public class FireHubPagerTabActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpagertabfragment);

        FragmentManager fm = getSupportFragmentManager();
        if (fm.findFragmentByTag(FireHubPagerTabParentFragment.FRAGMENT_TAG) == null) {
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.fragment, new FireHubPagerTabParentFragment(),
                    FireHubPagerTabParentFragment.FRAGMENT_TAG);
            ft.commit();
            fm.executePendingTransactions();
        }

        initView();

    }
    @Override
    protected void onResume() {
        AppContext.addToAppContext(this);
        super.onResume();
    }

    @Override
    protected void onPause() {
        AppContext.removeFromAppContext(this);
        super.onPause();
    }

    protected void initView() {

        // 设置title 及back 键
        View mergeView = findViewById(R.id.merge_title);
        View adviceTitle = mergeView.findViewById(R.id.merge_title_layout);
        final ImageButton backBtn = (ImageButton) adviceTitle.findViewById(R.id.merge_title_back);
        TextView titleTxt = (TextView) mergeView.findViewById(R.id.merge_title_text);
        titleTxt.setText("消防论坛");
        adviceTitle.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    backBtn.setBackgroundResource(R.drawable.back_normal);
                }

                return false;
            }
        });

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.merge_title_layout:       //back 键
                        finish();
                        break;
                    case R.id.merge_search_btn:               //search 键  。点击调到搜索界面
                        Toast.makeText(FireHubPagerTabActivity.this, "do search", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.merge_action_btn:               //action 键  。点击弹出菜单
                        Toast.makeText(FireHubPagerTabActivity.this, "do action", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;

                }
            }
        };

        adviceTitle.setOnClickListener(onClickListener);

        //设置搜索button和action 键
        ImageButton searchBtn = (ImageButton) mergeView.findViewById(R.id.merge_search_btn);
        ImageButton actionBtn = (ImageButton) mergeView.findViewById(R.id.merge_action_btn);
        searchBtn.setOnClickListener(onClickListener);
        actionBtn.setOnClickListener(onClickListener);


    }

}
