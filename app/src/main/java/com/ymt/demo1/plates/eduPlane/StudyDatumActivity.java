/*
    历年真题界面。
 */
package com.ymt.demo1.plates.eduPlane;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ymt.demo1.R;
import com.ymt.demo1.beams.EduDialogueInfo;
import com.ymt.demo1.customViews.MyTitle;
import com.ymt.demo1.main.BaseFloatActivity;
import com.ymt.demo1.main.SearchActivity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by Dan on 2015/4/9
 */
public class StudyDatumActivity extends BaseFloatActivity {
    final int SIMPLE_TYPE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edu_study_datum);
        initTitle();
        initView();

    }

    protected void initTitle() {
        MyTitle title = (MyTitle) findViewById(R.id.my_title);
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
                startActivity(new Intent(StudyDatumActivity.this, SearchActivity.class));
            }

            @Override
            public void onRightRClick() {
                //todo

            }
        });
    }

    protected void initView() {
        /*
        资料列表
         */
        ListView hotList = (ListView) findViewById(R.id.hot_study_data);
        ListView otherList = (ListView) findViewById(R.id.other_study_data);
        hotList.setEmptyView(findViewById(R.id.empty_pro_bar_u));           //无数据时显示progress bar
        otherList.setEmptyView(findViewById(R.id.empty_pro_bar_d));

         /*
        资料列表点击事件
         */
        AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (view.getId()) {
                    case R.id.hot_study_data:
                        Toast.makeText(StudyDatumActivity.this, "hot " + position, Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.other_study_data:
                        Toast.makeText(StudyDatumActivity.this, "other " + position, Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;

                }
            }
        };
        hotList.setOnItemClickListener(onItemClickListener);
        otherList.setOnItemClickListener(onItemClickListener);

        /*
        设置数据适配器
         */
        TestsItemAdapter adapter = new TestsItemAdapter(this);
        hotList.setAdapter(adapter);
        otherList.setAdapter(adapter);

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

    /*
    定义listView 中item 的适配器
    todo 根据数据，修改此适配器。
     */
    class TestsItemAdapter extends BaseAdapter {

        ArrayList<EduDialogueInfo> mList = new ArrayList<>();
        Context context;
        LayoutInflater inflater;

        public TestsItemAdapter(Context context) {
            this.context = context;
            inflater = LayoutInflater.from(context);
        }

        public void setList(ArrayList<EduDialogueInfo> list) {
            this.mList = list;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int position) {
            return mList.get(position);
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public int getItemViewType(int position) {
            return SIMPLE_TYPE;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            int type = getItemViewType(position);
            if (convertView == null) {
                switch (type) {
                    case SIMPLE_TYPE:
                        convertView = inflater.inflate(R.layout.layout_dialogue_item, parent, false);
                        viewHolder = new ViewHolder();
                        viewHolder.question = (TextView) convertView.findViewById(R.id.dialogue_qust_txt);
                        viewHolder.answer = (TextView) convertView.findViewById(R.id.dialogue_answer_view);
                        viewHolder.watchedCount = (TextView) convertView.findViewById(R.id.dialogue_watched_count);
                        viewHolder.collectedCount = (TextView) convertView.findViewById(R.id.dialogue_collected_count);
                        convertView.setTag(viewHolder);
                        break;
                    default:
                        break;
                }

            } else {
                switch (type) {
                    case SIMPLE_TYPE:
                        viewHolder = (ViewHolder) convertView.getTag();
                        break;
                    default:
                        break;
                }
            }

            /*
            设置TestInfo字段信息到列表的item中
             */
            switch (type) {
                case SIMPLE_TYPE:
                    EduDialogueInfo dialogue = (EduDialogueInfo) getItem(position);
                    viewHolder.question.setText(dialogue.getQuestion());
                    viewHolder.answer.setText(dialogue.getAnswer());
                    viewHolder.watchedCount.setText(String.valueOf(dialogue.getWatchedCount()));
                    viewHolder.collectedCount.setText(String.valueOf(dialogue.getCollectedCount()));
                    break;

                default:
                    break;

            }
            return convertView;
        }

        /*
        包含EduDialogueInfo中内容字段的ViewHolder
         */
        class ViewHolder {
            TextView question;
            TextView answer;
            TextView watchedCount;
            TextView collectedCount;
        }
    }

    static class ShowTabHandler extends Handler {
        private WeakReference<StudyDatumActivity> overYearsTestActivityWeakReference;

        public ShowTabHandler(StudyDatumActivity overYearsTestActivity) {
            overYearsTestActivityWeakReference = new WeakReference<>(overYearsTestActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            StudyDatumActivity overYearsTestActivity = overYearsTestActivityWeakReference.get();
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
