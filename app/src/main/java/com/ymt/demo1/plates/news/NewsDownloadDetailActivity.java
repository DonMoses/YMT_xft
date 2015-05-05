package com.ymt.demo1.plates.news;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.ymt.demo1.R;
import com.ymt.demo1.adapter.SimpleTxtItemAdapter;
import com.ymt.demo1.baseClasses.BaseActivity;
import com.ymt.demo1.customViews.MyTitle;
import com.ymt.demo1.main.PopActionListener;
import com.ymt.demo1.main.PopActionUtil;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Dan on 2015/4/29
 */
public class NewsDownloadDetailActivity extends BaseActivity {
    private PopActionListener actionListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_knowledge_content_detail);
        initTitle();
        initView();
    }

    /**
     * 初始化title 和 action事件
     */
    protected void initTitle() {
        final MyTitle title = (MyTitle) findViewById(R.id.my_title);
        title.setTitleStyle(MyTitle.TitleStyle.RIGHT_ICON_L);

        title.updateCenterTitle(getIntent().getStringExtra("title"));     //设置title
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

    protected void initView() {
        //文档大小
        final TextView fileSize = (TextView) findViewById(R.id.download_file_size);
        //所需积分
        final TextView scoreNeed = (TextView) findViewById(R.id.download_file_score_needed);
        //下载文档按钮
        final Button downBtn = (Button) findViewById(R.id.download_btn);
        //热门话题GridView
        final GridView hotCommentGrid = (GridView) findViewById(R.id.hot_comment_grid_view);

        //下载按钮监听
        downBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //设置背景颜色变暗
                final WindowManager.LayoutParams lp =
                        com.ymt.demo1.plates.knowledge.KnowledgeDownloadDetailActivity.this.getWindow().getAttributes();
                lp.alpha = 0.3f;
                com.ymt.demo1.plates.knowledge.KnowledgeDownloadDetailActivity.this.getWindow().setAttributes(lp);

                //todo 弹出下载提示框
                PopActionUtil popActionUtil = PopActionUtil.getInstance(com.ymt.demo1.plates.knowledge.KnowledgeDownloadDetailActivity.this);
                PopupWindow popupWindow = popActionUtil.getDownloadPopActionMenu();
                popupWindow.showAtLocation(downBtn.getRootView(), Gravity.CENTER, 0, 0);

                popActionUtil.setActionListener(new PopActionListener() {
                    @Override
                    public void onAction(String action) {
                        switch (action) {
                            case "确定":
                                Toast.makeText(com.ymt.demo1.plates.knowledge.KnowledgeDownloadDetailActivity.this, "确定", Toast.LENGTH_LONG).show();
                                break;
                            case "取消":
                                Toast.makeText(com.ymt.demo1.plates.knowledge.KnowledgeDownloadDetailActivity.this, "取消", Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                break;
                        }
                        lp.alpha = 1f;
                        com.ymt.demo1.plates.knowledge.KnowledgeDownloadDetailActivity.this.getWindow().setAttributes(lp);
                    }

                    @Override
                    public void onDismiss() {

                    }
                });
            }
        });

        /*
        内容textView
         */
        final TextView contentView = (TextView) findViewById(R.id.content);
        contentView.setText(getIntent().getStringExtra("content"));

        ArrayList<String> list = new ArrayList<>();
        String[] hotArray = new String[]{"消防部门", "规范组", "建委",
                "科研机构", "设计院", "开发商", "设备商", "服务商"};
        Collections.addAll(list, hotArray);
        SimpleTxtItemAdapter adapter = new SimpleTxtItemAdapter(this);
        hotCommentGrid.setAdapter(adapter);
        adapter.setColor(Color.WHITE, getResources().getColor(R.color.bg_view_blue));
        adapter.setList(list);

        hotCommentGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String str = parent.getAdapter().getItem(position).toString();
                //todo
                Toast.makeText(com.ymt.demo1.plates.knowledge.KnowledgeDownloadDetailActivity.this, str, Toast.LENGTH_SHORT).show();
            }
        });

        /*
        点击 “写点评”
         */
        View view = findViewById(R.id.write_comment_layout);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo 写点评
                Toast.makeText(com.ymt.demo1.plates.knowledge.KnowledgeDownloadDetailActivity.this, "写点评...", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
