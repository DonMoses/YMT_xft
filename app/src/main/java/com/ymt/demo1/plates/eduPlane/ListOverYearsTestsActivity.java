
package com.ymt.demo1.plates.eduPlane;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.ymt.demo1.R;
import com.ymt.demo1.adapter.SimpleTextGridViewAdapter;
import com.ymt.demo1.customViews.DragGridView;

import java.util.ArrayList;

/**
 * Created by Dan on 2015/4/10
 */
public class ListOverYearsTestsActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_over_years_tests);
        initView();
    }

    protected void initView() {
        DragGridView gridView = (DragGridView) findViewById(R.id.list_tests_gridView);
        SimpleTextGridViewAdapter adapter = new SimpleTextGridViewAdapter(this, 6, 2);
        gridView.setAdapter(adapter);

        Intent intent = getIntent();
        ArrayList<String> mTests = intent.getStringArrayListExtra("tests_years");
        adapter.setList(mTests);

        /*
        gridView点击事件。 点击item ，进入对应年份的试题
            ②如果点击“全部”，则退出dialog 界面
         */
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String text = parent.getAdapter().getItem(position).toString();
                switch (text) {
                    case "全部":
                        finish();
                        break;
                    default:
                        /*
                        进入该年份试题答题界面
                         */
                        Toast.makeText(ListOverYearsTestsActivity.this,
                                "open " + text, Toast.LENGTH_SHORT).show();
                        //todo 进入对应答题界面
                        finish();  //退出当前界面
                        break;

                }
            }
        });

    }
}
