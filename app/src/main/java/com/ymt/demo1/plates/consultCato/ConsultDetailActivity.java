package com.ymt.demo1.plates.consultCato;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.ymt.demo1.R;
import com.ymt.demo1.adapter.SimpleTxtItemAdapter;
import com.ymt.demo1.baseClasses.BaseActivity;
import com.ymt.demo1.customViews.MyTitle;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Dan on 2015/5/4
 */
public class ConsultDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consult_detail);
        initTitle();
        initView();
    }

    /**
     * 初始化title 和 action事件
     */
    protected void initTitle() {
        final MyTitle title = (MyTitle) findViewById(R.id.my_title);
        title.setTitleStyle(MyTitle.TitleStyle.RIGHT_ICON_L);

        title.updateCenterTitle(getSearchedKeyWord());     //设置title
        title.setOnLeftActionClickListener(new MyTitle.OnLeftActionClickListener() {
            @Override
            public void onClick() {
                finish();
            }
        });

        title.setOnRightActionClickListener(new MyTitle.OnRightActionClickListener() {
            @Override
            public void onRightLClick() {
                //弹出分享界面
                //todo 分享内容
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("*/*");
                intent.putExtra(Intent.EXTRA_SUBJECT, "消防咨询-" + getIntent().getStringExtra("title"));
                intent.putExtra(Intent.EXTRA_TEXT, getIntent().getStringExtra("content"));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(Intent.createChooser(intent, getTitle()));
            }

            @Override
            public void onRightRClick() {
                //此视图，右边只包含L按钮

            }
        });
    }

    /**
     * 获取搜索关键字
     */
    protected String getSearchedKeyWord() {
        Intent intent = getIntent();
        return intent.getStringExtra("search_key_word");
    }

    /**
     * 获得内容,初始化控件
     */
    protected void initView() {
        //title 和内容
        TextView title = (TextView) findViewById(R.id.subject);
        TextView content = (TextView) findViewById(R.id.content);
//        GridView hotConsults = (GridView) findViewById(R.id.hot_consult_grid_view);
        Intent intent = getIntent();
        title.setText(intent.getStringExtra("title"));
        content.setText(Html.fromHtml(intent.getStringExtra("content")));

//        //热门话题
//        GridView hotGrid = (GridView) findViewById(R.id.hot_consult_grid_view);
//        ArrayList<String> list = new ArrayList<>();
//        String[] hotArray = new String[]{"消防部门", "规范组", "建委",
//                "科研机构", "设计院", "开发商", "设备商", "服务商"};
//        Collections.addAll(list, hotArray);
//        SimpleTxtItemAdapter adapter = new SimpleTxtItemAdapter(this);
//        hotGrid.setAdapter(adapter);
//        adapter.setColor(Color.WHITE, getResources().getColor(R.color.bg_view_blue));
//        adapter.setList(list);
//
//        hotGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String str = parent.getAdapter().getItem(position).toString();
//                //todo
//                Toast.makeText(ConsultDetailActivity.this, str, Toast.LENGTH_SHORT).show();
//            }
//        });
    }
}
