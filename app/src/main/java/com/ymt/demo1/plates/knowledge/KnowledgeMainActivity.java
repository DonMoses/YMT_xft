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
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.widget.PopupWindow;

import com.ymt.demo1.R;
import com.ymt.demo1.baseClasses.BaseActivity;
import com.ymt.demo1.baseClasses.KnowledgePagerTabParentFragment;
import com.ymt.demo1.baseClasses.PersonalPagerTabParentFragment;
import com.ymt.demo1.customViews.MyTitle;
import com.ymt.demo1.plates.PopActionUtil;

/**
 * This activity just provides a toolbar.
 * Toolbar is manipulated by ViewPagerTabFragmentParentFragment.
 */
public class KnowledgeMainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_knowledge_main);

        FragmentManager fm = getSupportFragmentManager();
        if (fm.findFragmentByTag(KnowledgePagerTabParentFragment.FRAGMENT_TAG) == null) {
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.fragment, new KnowledgePagerTabParentFragment(),
                    KnowledgePagerTabParentFragment.FRAGMENT_TAG);
            ft.commit();
            fm.executePendingTransactions();
        }
        initTitle();
        initView();

    }

    protected void initTitle() {
        final MyTitle title = (MyTitle) findViewById(R.id.my_title);
        title.setTitleStyle(MyTitle.TitleStyle.RIGHT_ICON_L);
        title.setOnLeftActionClickListener(new MyTitle.OnLeftActionClickListener() {
            @Override
            public void onClick() {
                finish();
            }
        });

        title.setOnRightActionClickListener(new MyTitle.OnRightActionClickListener() {
            @Override
            public void onRightLClick() {
                PopupWindow popupWindow = PopActionUtil.getInstance(
                        KnowledgeMainActivity.this,
                        new String[]{"最近浏览", "知识讨论", "更多"}).getPopActionMenu();
                popupWindow.showAtLocation(title.getRootView(),
                        Gravity.TOP | Gravity.END, 10, 100);
            }

            @Override
            public void onRightRClick() {
                //此视图，右边只包含L按钮

            }
        });
    }

    protected void initView() {


    }

}
