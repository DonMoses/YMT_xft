/*
    历年真题界面。
 */
package com.ymt.demo1.plates.eduPlane;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ymt.demo1.R;
import com.ymt.demo1.beams.EduDialogueInfo;
import com.ymt.demo1.customViews.MyTitle;
import com.ymt.demo1.dbBeams.SearchString;
import com.ymt.demo1.main.BaseFloatActivity;
import com.ymt.demo1.main.SearchActivity;

import org.litepal.crud.DataSupport;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dan on 2015/4/9
 * 答疑界面
 */
public class DialogueListActivity extends BaseFloatActivity {
    final int SIMPLE_TYPE = 0;
    private EditText inputView;
    private int updateIndex;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edu_dialogue_list);
        sharedPreferences = getSharedPreferences(SearchActivity.SEARCH_PREFERENCES, MODE_PRIVATE);
        updateIndex = sharedPreferences.getInt(SearchActivity.UPDATE_SEARCH_INDEX, 0);
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
        initSearchView();

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
                        inputView.clearFocus();
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
     * 数据库大小
     */
    private int size;

    /**
     * 搜索栏
     */
    protected void initSearchView() {
        //输入框
        inputView = (EditText) findViewById(R.id.search_edit_text);
        //搜索按钮
        final ImageView searchBtn = (ImageView) findViewById(R.id.search_btn);
        /*
        * 初始化适配器控件
        * */

        final GridView historyView = (GridView) findViewById(R.id.search_history_gridView);

        //从数据库获得已搜索的关键字
        final List<SearchString> searchedStrs = DataSupport.findAll(SearchString.class);
        size = searchedStrs.size();
        final ArrayList<String> searched = new ArrayList<>();
        for (int i = 0; i < searchedStrs.size(); i++) {
            searched.add(searchedStrs.get(i).getSearchedString());
        }

        //todo 为热门话题创建数据
        final ArrayAdapter<String> historyAdapter = new ArrayAdapter<>(this, R.layout.item_view_common_quest_low, searched);
        historyView.setAdapter(historyAdapter);

        inputView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    historyView.setVisibility(View.VISIBLE);
                } else {
                    historyView.setVisibility(View.GONE);
                }
            }
        });

        /*
        searchBtn 事件
         */
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //更新数据
                String str = inputView.getText().toString();
                if (TextUtils.isEmpty(str)) {
                    Toast.makeText(DialogueListActivity.this, "请输入关键字...", Toast.LENGTH_SHORT).show();
                } else if (!searched.contains(inputView.getText().toString())) {
                    //获取输入框内容，搜索内容，加入搜索数据库表. 只保存之多20条历史记录
                    //获取输入框内容，搜索内容，加入搜索数据库表
                    if (size >= 10) {
                        ContentValues values = new ContentValues();
                        values.put("searchedstring", inputView.getText().toString());

                        //更新index，则下次输入后更新到上一次的下一个坐标
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        DataSupport.update(SearchString.class, values, updateIndex + 1);
                        updateIndex++;
                        if (updateIndex > 10) {
                            updateIndex = 1;
                        }
                        editor.putInt(SearchActivity.UPDATE_SEARCH_INDEX, updateIndex);
                        editor.apply();
                    } else {
                        saveString(inputView.getText().toString());
                    }

                    searched.add(inputView.getText().toString());
                    //刷新适配器
                    historyAdapter.notifyDataSetChanged();
                    Toast.makeText(DialogueListActivity.this, "搜索：" + inputView.getText().toString(), Toast.LENGTH_SHORT).show();
                }

                //清空输入内容， 输入框改变为不聚焦
                inputView.setText("");
                inputView.clearFocus();
            }
        });

        /*
        最近搜索gridView 单击事件
         */
        historyView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String str = parent.getAdapter().getItem(position).toString();
                inputView.setText(str);
                inputView.setSelection(inputView.getText().toString().length());        //移动光标到最后
            }
        });

    }

    /**
     * 保存搜索记录到数据库
     */
    public void saveString(String str) {
        SearchString searchString = new SearchString();
        searchString.setSearchedString(str);
        searchString.save();            //加入数据库
        size++;
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
