/*
 * Copyright 2014 DonMoses
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

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
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
import com.ymt.demo1.adapter.KnowledgeItemAdapter;
import com.ymt.demo1.baseClasses.BaseFragment;
import com.ymt.demo1.beams.KnowledgeItem;
import com.ymt.demo1.customViews.obsScrollview.ObservableScrollView;
import com.ymt.demo1.customViews.obsScrollview.ObservableScrollViewCallbacks;
import com.ymt.demo1.main.PopActionListener;
import com.ymt.demo1.main.PopActionUtil;

import java.util.ArrayList;


/**
 * Fragment for ViewPagerTabFragmentActivity.
 * ScrollView callbacks are handled by its parent fragment, not its parent activity.
 */
public class KnowledgeTabScrollUltraListViewFragment extends BaseFragment {

    private ObservableScrollView scrollView;
    PullToRefreshListView listView;
    ListView theListView;
    ArrayList<KnowledgeItem> testArray;
    KnowledgeItemAdapter adapter;
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
            KnowledgeItem item = new KnowledgeItem();
            item.setTitle(i + 1 + "深圳市出台《消防产品监督管理工作考核办法》");
            item.setContentTxt("笔者近日获悉，为切实消除因产品质量问题造成的火灾隐患，整治和规范消防产品市场秩序，全面推进全市消防产品监督执法工作规范化建设，提高执法质量，广东省深圳市公安局消防监督管理局今天出台《深圳市消防产品监督管理工作考核办法》。\n" +
                    "\n" +
                    "考核内容包括消防产品使用领域监督、部门联合执法整治、消防产品地方监督抽查、开展消防产品监督宣传报道、消防产品身份证管理制度实施、公共场所阻燃制品标识管理制度实施、消防产品监督管理工作创新举措等七种不同情况。\n" +
                    "\n" +
                    "考核办法主要对消防产品使用领域监督及处罚情况、部门联合执法查处制售假冒伪劣消防产品案件、消防产品地方监督抽查情况、消防产品监督宣传报道情况、消防产品身份证管理制度实施情况、生产企业申请阻燃制品标识情况、工作落实及创新情况七种情况进行确认后实行量化考核。");
            switch (i / 4) {
                case 0:
                    item.setKnowledgeItemType(KnowledgeItemType.TXT);
                    break;
                case 1:
                    item.setKnowledgeItemType(KnowledgeItemType.PPT);
                    break;
                case 2:
                    item.setKnowledgeItemType(KnowledgeItemType.PDF);
                    break;
                case 3:
                    item.setKnowledgeItemType(KnowledgeItemType.MP3);
                    break;
                default:
                    break;
            }
            item.setCollectedCount((i + 1) * 258 + i * 2 - 119);      //todo 测试，随意输入的数字
            item.setCommentedCount(i * 1024 - 520);
            //设置是否被收藏(每3个一次)
            if (i % 3 == 0) {
                item.setIsCollected(true);
            }
            testArray.add(item);
        }

        /*
        设置数据源
         */
        adapter = new KnowledgeItemAdapter(getActivity());
        listView.setAdapter(adapter);
        adapter.setList(testArray);
        scrollView.setListView(listView);            //测量、监听listView的变化

        /*
         *设置长按弹出菜单事件
         */
        theListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, final View view, final int position, long id) {

            /*
            *初始化弹出菜单
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
//                              //属性动画
                                ObjectAnimator animator = ObjectAnimator
                                        .ofFloat(view, "rotationX", 0.0F, 360.0F)//
                                        .setDuration(1000);
                                animator.start();
                                animator.addListener(new Animator.AnimatorListener() {
                                    @Override
                                    public void onAnimationStart(Animator animation) {

                                    }

                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        testArray.remove(position - 1);     //因为此listView包含header，所以 -1
                                        adapter.setList(testArray);
                                    }

                                    @Override
                                    public void onAnimationCancel(Animator animation) {

                                    }

                                    @Override
                                    public void onAnimationRepeat(Animator animation) {

                                    }
                                });
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
                final PopupWindow itemPopMenu = popActionUtil.getSimpleTxtPopActionMenu();

                itemPopMenu.showAsDropDown(view, view.getWidth() - 20, -view.getHeight());
                itemPopMenu.update();

                return true;
            }
        });

        /*
        listView 点击事件 。 跳转到详情界面
         */
        theListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //传入内容
                Intent intent = new Intent(getActivity(), KnowledgeDownloadDetailActivity.class);
                intent.putExtra("title", ((KnowledgeItem) parent.getAdapter().getItem(position)).getTitle());
                intent.putExtra("content", ((KnowledgeItem) parent.getAdapter().getItem(position)).getContentTxt());
                startActivity(intent);
            }
        });

    }
}
