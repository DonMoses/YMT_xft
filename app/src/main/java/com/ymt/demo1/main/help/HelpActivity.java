package com.ymt.demo1.main.help;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ymt.demo1.R;
import com.ymt.demo1.adapter.LongClickItemsAdapter;
import com.ymt.demo1.customViews.MyTitle;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Moses on 2015
 */
public class HelpActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
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
    }

    protected void initView() {

        ListView questListView = (ListView) findViewById(R.id.common_quest_list_view);
        String[] quests = getResources().getStringArray(R.array.common_quest_array);
        final LongClickItemsAdapter questAdapter = new LongClickItemsAdapter(this);
        questListView.setAdapter(questAdapter);
        ArrayList<String> mQuests = new ArrayList<>();
        Collections.addAll(mQuests, quests);
        questAdapter.setmList(mQuests);

        questListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                questAdapter.setSelectedItemPosition(position);
                questAdapter.notifyDataSetInvalidated();

                //跳转到帮助详情页面


            }
        });

        questListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                questAdapter.setSelectedItemPosition(position);
                questAdapter.notifyDataSetInvalidated();

                //跳转到帮助详情页面


                return true;
            }
        });

    }


}
