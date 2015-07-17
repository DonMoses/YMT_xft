package com.ymt.demo1.plates.eduPlane.mockExams;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.ymt.demo1.R;
import com.ymt.demo1.adapter.SimpleTextDragGridViewAdapter;
import com.ymt.demo1.adapter.TopicIndexGridAdapter;
import com.ymt.demo1.customViews.DragGridView;
import com.ymt.demo1.plates.eduPlane.pastExams.PastExamsListActivity;

import java.util.ArrayList;

/**
 * Created by Dan on 2015/4/10
 */
public class TopicIndexActivity extends Activity {
    private ArrayList<String> doneList;
    private ArrayList<String> allList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        doneList = getIntent().getStringArrayListExtra("doneList");
        allList = getIntent().getStringArrayListExtra("allList");
        setContentView(R.layout.activity_edu_order_grid);
        initView();

    }

    protected void initView() {
        DragGridView gridView = (DragGridView) findViewById(R.id.list_tests_gridView);
        TopicIndexGridAdapter adapter = new TopicIndexGridAdapter(this, 6, 2);
        gridView.setAdapter(adapter);

        adapter.setList(allList, doneList);

        /*
        gridView点击事件。 点击item ，进入对应年份的试题
            ②如果点击“全部”，则退出dialog 界面
         */
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String text = parent.getAdapter().getItem(position).toString();
                Intent intent = new Intent(TopicIndexActivity.this, PastExamsListActivity.class);
                intent.putExtra("index", Integer.valueOf(text));
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }
}