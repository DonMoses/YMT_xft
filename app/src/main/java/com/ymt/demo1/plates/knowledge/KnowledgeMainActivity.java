/*
 * Copyright 2015 DonMoses
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

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.ymt.demo1.R;
import com.ymt.demo1.baseClasses.BaseActivity;
import com.ymt.demo1.customViews.MyTitle;
import com.ymt.demo1.utils.PopActionListener;
import com.ymt.demo1.utils.PopActionUtil;
import com.ymt.demo1.main.search.SearchActivity;

/**
 * This activity just provides a toolbar.
 * Toolbar is manipulated by ViewPagerTabFragmentParentFragment.
 */
public class KnowledgeMainActivity extends BaseActivity {

    private PopActionListener actionListener;
    public RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mQueue = Volley.newRequestQueue(this);
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
        title.setTitleStyle(MyTitle.TitleStyle.RIGHT_ICON_L_R);
        title.setOnLeftActionClickListener(new MyTitle.OnLeftActionClickListener() {
            @Override
            public void onClick() {
                finish();
            }
        });

        actionListener = new PopActionListener() {
            @Override
            public void onAction(String action) {
                switch (action) {
                    case "最近浏览":
                        Toast.makeText(KnowledgeMainActivity.this, "最近浏览", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(KnowledgeMainActivity.this, RecentViewKnowledgeActivity.class));
                        break;
                    case "最新上传":
                        Toast.makeText(KnowledgeMainActivity.this, "最新上传", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(KnowledgeMainActivity.this, RecentUploadActivity.class));
                        break;
                    case "下载排行":
                        Toast.makeText(KnowledgeMainActivity.this, "下载排行", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(KnowledgeMainActivity.this, DownloadRankActivity.class));
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onDismiss() {

            }
        };

        title.setOnRightActionClickListener(new MyTitle.OnRightActionClickListener() {
            @Override
            public void onRightLClick() {
                startActivity(new Intent(KnowledgeMainActivity.this, SearchActivity.class));

            }

            @Override
            public void onRightRClick() {
                PopActionUtil popActionUtil = PopActionUtil.getInstance(KnowledgeMainActivity.this);
                popActionUtil.setActions(new String[]{"最近浏览", "最新上传", "下载排行"});
                PopupWindow popupWindow = popActionUtil.getSimpleTxtPopActionMenu();
                popupWindow.showAtLocation(title.getRootView(),
                        Gravity.TOP | Gravity.END, 10, 100);

                popActionUtil.setActionListener(actionListener);
            }
        });
    }

    protected void initView() {


    }

}
