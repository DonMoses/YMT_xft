package com.ymt.demo1.plates.knowledge;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.ymt.demo1.R;
import com.ymt.demo1.adapter.SimpleTxtItemAdapter;
import com.ymt.demo1.baseClasses.BaseActivity;
import com.ymt.demo1.beams.knowledge.KnowledgeItemBZGF;
import com.ymt.demo1.beams.knowledge.KnowledgeItemKYWX;
import com.ymt.demo1.customViews.MyTitle;
import com.ymt.demo1.main.PopActionListener;
import com.ymt.demo1.main.PopActionUtil;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Dan on 2015/4/29
 */
public class KnowledgeItemDetailActivity extends BaseActivity {
    private PopActionListener actionListener;
    private KnowledgeItemBZGF itemBZGF;
    private KnowledgeItemKYWX itemKYWX;
    private boolean isBZGF = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        itemBZGF = getIntent().getParcelableExtra("bzgf");
        itemKYWX = getIntent().getParcelableExtra("kywx");
        if (itemBZGF == null) {
            isBZGF = false;
        } else {
            isBZGF = true;
        }
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
        final TextView titleView = (TextView) findViewById(R.id.title);
        final TextView timeView = (TextView) findViewById(R.id.create_time);
        final TextView contentView = (TextView) findViewById(R.id.content);
        //所需积分
        final TextView scoreNeed = (TextView) findViewById(R.id.download_file_score_needed);

        final Button downBtn = (Button) findViewById(R.id.download_btn);


        if (isBZGF) {
            titleView.setText(itemBZGF.getArticle_title() + ".pdf");
            timeView.setText(itemBZGF.getCreate_time());
            scoreNeed.setText(itemBZGF.getScore());
        } else {
            titleView.setText(itemKYWX.getArticle_title());
            timeView.setText(itemKYWX.getCreate_time());
            scoreNeed.setText(itemKYWX.getScore());
            contentView.setText(Html.fromHtml(itemKYWX.getContent()));
        }


//        //热门话题GridView
//        final GridView hotCommentGrid = (GridView) findViewById(R.id.hot_comment_grid_view);

        //下载按钮监听
        downBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //设置背景颜色变暗
                final WindowManager.LayoutParams lp =
                        KnowledgeItemDetailActivity.this.getWindow().getAttributes();
                lp.alpha = 0.3f;
                KnowledgeItemDetailActivity.this.getWindow().setAttributes(lp);

                //todo 弹出下载提示框
                PopActionUtil popActionUtil = PopActionUtil.getInstance(KnowledgeItemDetailActivity.this);
                PopupWindow popupWindow = popActionUtil.getDownloadPopActionMenu();
                popupWindow.showAtLocation(downBtn.getRootView(), Gravity.CENTER, 0, 0);

                popActionUtil.setActionListener(new PopActionListener() {
                    @Override
                    public void onAction(String action) {
                        switch (action) {
                            case "确定":
                                Toast.makeText(KnowledgeItemDetailActivity.this, "确定", Toast.LENGTH_LONG).show();
                                break;
                            case "取消":
                                Toast.makeText(KnowledgeItemDetailActivity.this, "取消", Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                break;
                        }
                        lp.alpha = 1f;
                        KnowledgeItemDetailActivity.this.getWindow().setAttributes(lp);
                    }

                    @Override
                    public void onDismiss() {

                    }
                });
            }
        });

//        ArrayList<String> list = new ArrayList<>();
//        String[] hotArray = new String[]{"消防部门", "规范组", "建委",
//                "科研机构", "设计院", "开发商", "设备商", "服务商"};
//        Collections.addAll(list, hotArray);
//        SimpleTxtItemAdapter adapter = new SimpleTxtItemAdapter(this);
//        hotCommentGrid.setAdapter(adapter);
//        adapter.setColor(Color.WHITE, getResources().getColor(R.color.bg_view_blue));
//        adapter.setList(list);

//        hotCommentGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String str = parent.getAdapter().getItem(position).toString();
//                //todo
//                Toast.makeText(KnowledgeItemDetailActivity.this, str, Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        /*
//        点击 “写点评”
//         */
//        View view = findViewById(R.id.write_comment_layout);
//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //todo 写点评
//                Toast.makeText(KnowledgeItemDetailActivity.this, "写点评...", Toast.LENGTH_SHORT).show();
//            }
//        });
    }

}
