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

package com.ymt.demo1.baseClasses;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.ymt.demo1.R;
import com.ymt.demo1.adapter.SimpleTxtItemAdapter;
import com.ymt.demo1.customViews.obsScrollview.ObservableScrollView;
import com.ymt.demo1.customViews.obsScrollview.ObservableScrollViewCallbacks;
import com.ymt.demo1.main.PopActionListener;
import com.ymt.demo1.plates.PopActionUtil;

import java.util.ArrayList;


/**
 * Fragment for ViewPagerTabFragmentActivity.
 * ScrollView callbacks are handled by its parent fragment, not its parent activity.
 */
public class SimpleTestTabFragmentScrollUltraListViewFragment extends BaseFragment {

    private ObservableScrollView scrollView;
    PullToRefreshListView listView;
    ListView theListView;
    ArrayList<String> testArray;
    SimpleTxtItemAdapter adapter;
    private int atPosition;
    private PopActionListener actionListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scrollview_ultra_list_view, container, false);

        scrollView = (ObservableScrollView) view.findViewById(R.id.scroll);
        Fragment parentFragment = getParentFragment();
        ViewGroup viewGroup = (ViewGroup) parentFragment.getView();
        if (viewGroup != null) {
            scrollView.setTouchInterceptionViewGroup((ViewGroup) viewGroup.findViewById(R.id.container));
            if (parentFragment instanceof ObservableScrollViewCallbacks) {
                scrollView.setScrollViewCallbacks((ObservableScrollViewCallbacks) parentFragment);
            }
        }

        initView(view);
        return view;
    }

    protected void initView(View view) {

        /*
         *将下拉刷新中的listView 加入到滚动布局中
         */
        listView = (PullToRefreshListView) view.findViewById(R.id.pull_to_refresh_list_view);
        theListView = listView.getRefreshableView();
        testArray = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            testArray.add(String.valueOf(i));
        }

        /*
        设置数据源
         */
        adapter = new SimpleTxtItemAdapter(getActivity());
        listView.setAdapter(adapter);
        adapter.setList(testArray);
        scrollView.setListView(listView);            //测量、监听listView的变化

        /*
         *设置长按弹出菜单事件
         */
        theListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                 /*
        初始化弹出菜单
         */
                String[] actionList = new String[]{"分享", "收藏", "删除"};
                PopActionUtil popActionUtil = PopActionUtil.getInstance(getActivity());
                actionListener = new PopActionListener() {
                    @Override
                    public void onAction(String action) {
                        switch (action) {
                            case "分享":
                                Toast.makeText(getActivity(), "分享", Toast.LENGTH_SHORT).show();
                                break;
                            case "收藏":
                                Toast.makeText(getActivity(), "收藏", Toast.LENGTH_SHORT).show();
                                break;
                            case "删除":
                                Toast.makeText(getActivity(), "删除", Toast.LENGTH_SHORT).show();
                                testArray.remove(atPosition);
                                adapter.setList(testArray);
                                break;
                            default:
                                break;
                        }
                    }

                    @Override
                    public void onDismiss() {

                    }
                };

                popActionUtil.setActionListener(actionListener);
                popActionUtil.setActions(actionList);
                final PopupWindow itemPopMenu = popActionUtil.getPopActionMenu();

                itemPopMenu.showAsDropDown(view, view.getWidth() - 20, -view.getHeight());
                itemPopMenu.update();
                atPosition = position - 1;

                return false;
            }
        });
    }



}
