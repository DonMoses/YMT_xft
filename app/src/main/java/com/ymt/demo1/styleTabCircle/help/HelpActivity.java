package com.ymt.demo1.styleTabCircle.help;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.ymt.demo1.R;
import com.ymt.demo1.adapter.LongClickItemsAdapter;

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
        initView();
    }

    protected void initView() {

        View mergeView = findViewById(R.id.merge_help_title);
        View helpTitle = mergeView.findViewById(R.id.merge_title_layout);
        final Button backBtn = (Button) helpTitle.findViewById(R.id.merge_title_back);
        TextView titleTxt = (TextView) mergeView.findViewById(R.id.merge_title_text);
        titleTxt.setText("帮助中心");
        helpTitle.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    backBtn.setBackgroundResource(R.drawable.back_normal);
                }

                return false;
            }
        });

        helpTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

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
