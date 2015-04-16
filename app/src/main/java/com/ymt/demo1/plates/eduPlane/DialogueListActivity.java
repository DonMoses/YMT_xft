/*
    历年真题界面。
 */
package com.ymt.demo1.plates.eduPlane;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ymt.demo1.R;
import com.ymt.demo1.beams.EduDialogueInfo;
import com.ymt.demo1.main.AppContext;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by Dan on 2015/4/9
 */
public class DialogueListActivity extends Activity implements View.OnClickListener {
    final int SIMPLE_TYPE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edu_dialogue_list);
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

        /*
        title 上back 按钮和搜索按钮的点击事件
         */
        View backView = findViewById(R.id.merge_title_layout);
        ImageButton searchBtn = (ImageButton) findViewById(R.id.merge_search_btn);
        backView.setOnClickListener(this);
        searchBtn.setOnClickListener(this);

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
        历年真题列表item 点击事件
            打开对应试题
         */
        dialogueList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(DialogueListActivity.this, ((EduDialogueInfo) parent.getItemAtPosition(position)).getQuestion(), Toast.LENGTH_SHORT).show();
            }
        });

        /*
        设置数据适配器
         */
        TestsItemAdapter adapter = new TestsItemAdapter(this);
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

    /**
     * View的点击事件。
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.merge_title_layout:

                finish();           //退出
                break;
            case R.id.merge_search_btn:         //搜索
                Toast.makeText(DialogueListActivity.this, "do search", Toast.LENGTH_SHORT).show();
                break;

            default:
                break;
        }
    }

    /*
    定义listView 中item 的适配器
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
