/*
 * Copyright 2014 moses
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

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.ymt.demo1.R;
import com.ymt.demo1.baseClasses.BaseActivity;
import com.ymt.demo1.customViews.MyTitle;
import com.ymt.demo1.main.search.FullSearchActivity;

/**
 * This activity just provides a toolbar.
 * Toolbar is manipulated by ViewPagerTabFragmentParentFragment.
 */
public class FireHubMainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hub);

        FragmentManager fm = getSupportFragmentManager();
        if (fm.findFragmentByTag(FireHubMainFragment.FRAGMENT_TAG) == null) {
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.fragment, new FireHubMainFragment(),
                    FireHubMainFragment.FRAGMENT_TAG);
            ft.commit();
            fm.executePendingTransactions();
        }

        initTitle();
        initView();

    }

    protected void initTitle() {
        final MyTitle title = (MyTitle) findViewById(R.id.my_title);
        title.setTitleStyle(MyTitle.TitleStyle.RIGHT_ICON_L);
//        title.setTitleStyle(MyTitle.TitleStyle.RIGHT_ICON_L_R);
//
//        final PopActionListener actionListener = new PopActionListener() {
//            @Override
//            public void onAction(String action) {
//                switch (action) {
//                    case "我的收藏":
//                        Toast.makeText(FireHubMainActivity.this, action, Toast.LENGTH_SHORT).show();
//                        break;
//                    case "最近浏览":
//                        Toast.makeText(FireHubMainActivity.this, action, Toast.LENGTH_SHORT).show();
//                        break;
//                    case "问题申诉":
//                        Toast.makeText(FireHubMainActivity.this, action, Toast.LENGTH_SHORT).show();
//                        break;
//                    default:
//                        break;
//
//                }
//            }
//
//            @Override
//            public void onDismiss() {
//
//            }
//        };
        title.setOnLeftActionClickListener(new MyTitle.OnLeftActionClickListener() {
            @Override
            public void onClick() {
                finish();
            }
        });

        title.setOnRightActionClickListener(new MyTitle.OnRightActionClickListener() {
            @Override
            public void onRightLClick() {
                startActivity(new Intent(FireHubMainActivity.this, FullSearchActivity.class));
            }

            @Override
            public void onRightRClick() {
//                // 设置按钮Action
//                PopActionUtil popActionUtil = PopActionUtil.getInstance(FireHubMainActivity.this);
//                popActionUtil.setActions(new String[]{"我的收藏", "最近浏览", "问题申诉"});
//                popActionUtil.setActionListener(actionListener);
//                PopupWindow popupWindow = popActionUtil.getSimpleTxtPopActionMenu();
//                popupWindow.showAtLocation(title.getRootView(), Gravity.END | Gravity.TOP, 10, 100);

            }
        });
    }

    protected void initView() {


    }

}
