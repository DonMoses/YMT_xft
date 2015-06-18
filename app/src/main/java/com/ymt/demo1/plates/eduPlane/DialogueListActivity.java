/*
    历年真题界面。
 */
package com.ymt.demo1.plates.eduPlane;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ymt.demo1.R;
import com.ymt.demo1.adapter.DialogueAdapter;
import com.ymt.demo1.beams.edu.EduDialogueInfo;
import com.ymt.demo1.customViews.MyTitle;
import com.ymt.demo1.main.BaseFloatActivity;
import com.ymt.demo1.main.SearchViewUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by Dan on 2015/4/9
 * 答疑界面
 */
public class DialogueListActivity extends BaseFloatActivity {
    private SearchViewUtil searchViewUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edu_dialogue_list);
        searchViewUtil = new SearchViewUtil();
        initTitle();
        initView();
    }

    protected void initTitle() {
        MyTitle title = (MyTitle) findViewById(R.id.my_title);
        title.setTitleStyle(MyTitle.TitleStyle.LEFT_ICON);
        title.setOnLeftActionClickListener(new MyTitle.OnLeftActionClickListener() {
            @Override
            public void onClick() {
                finish();
            }
        });

//        title.setOnRightActionClickListener(new MyTitle.OnRightActionClickListener() {
//            @Override
//            public void onRightLClick() {
//                startActivity(new Intent(DialogueListActivity.this, SearchActivity.class));
//            }
//
//            @Override
//            public void onRightRClick() {
//                //todo 设置按钮Action
//            }
//        });
    }

    protected void initView() {
        searchViewUtil.initSearchView(this);

        /*
        问答列表
         */
        ListView dialogueList = (ListView) findViewById(R.id.dialogue_list_view);
        dialogueList.setEmptyView(findViewById(R.id.empty_pro_bar));           //无数据时显示progress bar

         /*
        列表滑动监听
         */
        dialogueList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case SCROLL_STATE_IDLE:                 //停止时

                        break;
                    case SCROLL_STATE_FLING:                //滚动中
                    case SCROLL_STATE_TOUCH_SCROLL:         //触摸中
                        searchViewUtil.clearInputFocus();
                        break;
                    default:
                        break;

                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

         /*
        todo 历年真题列表item 点击事件。打开对应试题
         */
        dialogueList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EduDialogueInfo eduDialogueInfo = ((EduDialogueInfo) parent.getItemAtPosition(position));
                Intent intent = new Intent(DialogueListActivity.this, DialogueDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("eduDialogueInfo", eduDialogueInfo);
                intent.putExtra("eduDialogueInfo", bundle);
                startActivity(intent);
            }
        });

        /*
        设置数据适配器
         */
        DialogueAdapter adapter = new DialogueAdapter(this);
        dialogueList.setAdapter(adapter);

        //todo 这里加入网络获取问答数据
        ArrayList<EduDialogueInfo> mData = new ArrayList<>();
        //模拟加入历年真题数据
        for (int i = 0; i < 50; i++) {
            EduDialogueInfo test = new EduDialogueInfo();
            test.setQuestion(String.valueOf(i + 1) + "消防工程包括哪些？");
            test.setAnswer("答：火灾自动报警系统、消火栓系统、自动喷淋系统、气体灭火系统、防排烟系统、" + "" +
                    "消防应急疏散系统、消防广播通讯系统、消防材料系统。");
            test.setWatchedCount(i * (20 + i));
            test.setCollectedCount(i * (5 + i));
            mData.add(test);
        }
        adapter.setList(mData);

    }

    static class ShowTabHandler extends Handler {
        private WeakReference<DialogueListActivity> overYearsTestActivityWeakReference;

        public ShowTabHandler(DialogueListActivity overYearsTestActivity) {
            overYearsTestActivityWeakReference = new WeakReference<>(overYearsTestActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            DialogueListActivity overYearsTestActivity = overYearsTestActivityWeakReference.get();
            if (overYearsTestActivity != null) {
                //todo 通过外部类的引用，操操作外部类的成员和方法
                switch (msg.what) {
                    case 0:

                        break;
                    default:
                        break;

                }
            }
        }
    }

}
