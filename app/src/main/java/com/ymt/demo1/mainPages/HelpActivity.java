package com.ymt.demo1.mainPages;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.ymt.demo1.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
                    backBtn.setBackgroundResource(R.drawable.btn_back_detail_pressed);
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    backBtn.setBackgroundResource(R.drawable.btn_back_detail_normal);
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
        final CommonQuestAdapter questAdapter = new CommonQuestAdapter(this);
        questListView.setAdapter(questAdapter);
        ArrayList<String> mQuests = new ArrayList<>();
        Collections.addAll(mQuests, quests);
        questAdapter.setmList(mQuests);

        questListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                questAdapter.setSelectedItemPosition(position);
                questAdapter.notifyDataSetInvalidated();
            }
        });

        questListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                questAdapter.setSelectedItemPosition(position);
                questAdapter.notifyDataSetInvalidated();
                return true;
            }
        });

    }

    protected class CommonQuestAdapter extends BaseAdapter {
        List<String> mList = new ArrayList<>();
        LayoutInflater inflater;
        private int selectedPosition = -1;

        public CommonQuestAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        public void setmList(List<String> mList) {
            this.mList = mList;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        public void setSelectedItemPosition(int position) {
            selectedPosition = position;
        }

        @Override
        public Object getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView textView;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_view_common_quest, null);
                textView = (TextView) convertView.findViewById(R.id.common_quest_item_view);
                convertView.setTag(textView);
            } else {
                textView = (TextView) convertView.getTag();
            }

            textView.setText(mList.get(position));
            if (selectedPosition != position) {
                convertView.setBackgroundColor(Color.WHITE);
                textView.setTextColor(Color.DKGRAY);
            } else {
                convertView.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_dark));
                textView.setTextColor(Color.WHITE);
            }

            return convertView;
        }
    }

}
