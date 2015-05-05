package com.ymt.demo1.plates.knowledge;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.ymt.demo1.R;
import com.ymt.demo1.baseClasses.BaseActivity;
import com.ymt.demo1.customViews.MyTitle;
import com.ymt.demo1.main.PopActionListener;
import com.ymt.demo1.main.PopActionUtil;

/**
 * Created by Dan on 2015/4/29
 */
public class KnowledgeDownloadDetailActivity extends BaseActivity {
    private PopActionListener actionListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_knowledge_content_detail);
        initTitle();
        initView();
    }

    protected void initTitle() {
        final MyTitle title = (MyTitle) findViewById(R.id.my_title);
        title.setTitleStyle(MyTitle.TitleStyle.RIGHT_ICON_L);
        title.setOnLeftActionClickListener(new MyTitle.OnLeftActionClickListener() {
            @Override
            public void onClick() {
                finish();
            }
        });

        actionListener = new PopActionListener() {
            @Override
            public void onAction(String action) {
                switch (action) {
                    case "action1":
                        Toast.makeText(KnowledgeDownloadDetailActivity.this, "action1", Toast.LENGTH_SHORT).show();
                        break;
                    case "action2":
                        Toast.makeText(KnowledgeDownloadDetailActivity.this, "action2", Toast.LENGTH_SHORT).show();
                        break;
                    case "action3":
                        Toast.makeText(KnowledgeDownloadDetailActivity.this, "action3", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onDismiss() {

            }
        };

        title.setOnRightActionClickListener(new MyTitle.OnRightActionClickListener() {
            @Override
            public void onRightLClick() {
                PopActionUtil popActionUtil = PopActionUtil.getInstance(KnowledgeDownloadDetailActivity.this);
                popActionUtil.setActions(new String[]{"action1", "action2", "action3"});
                PopupWindow popupWindow = popActionUtil.getSimpleTxtPopActionMenu();
                popupWindow.showAtLocation(title.getRootView(),
                        Gravity.TOP | Gravity.END, 10, 100);

                popActionUtil.setActionListener(actionListener);

            }

            @Override
            public void onRightRClick() {
                //此视图，右边只包含L按钮

            }
        });
    }

    protected void initView() {
        //评论输入
        final EditText commentTxt = (EditText) findViewById(R.id.submit_your_comment);
        //总共可输入字符
        int totalCanIntput = 200;
        //页显示ViewPager
        final ViewPager pagePager = (ViewPager) findViewById(R.id.content_pager);
        pagePager.setOffscreenPageLimit(3);
        //当前显示页
        final TextView currPage = (TextView) findViewById(R.id.curr_page);
        //总共需要显示页
        final TextView totalPage = (TextView) findViewById(R.id.total_page);
        //文档大小
        final TextView fileSize = (TextView) findViewById(R.id.download_file_size);
        //所需积分
        final TextView scoreNeed = (TextView) findViewById(R.id.download_file_score_needed);
        //下载文档按钮
        final Button downBtn = (Button) findViewById(R.id.download_btn);
        //热门话题GridView
        final GridView hotCommentGrid = (GridView) findViewById(R.id.hot_comment_grid_view);
        //评论输入监听
        commentTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (commentTxt.length() == 200 && count > 0) {
                    //todo 弹出pop，提示已经达到最长输入
                    Toast.makeText(KnowledgeDownloadDetailActivity.this, "请输入少于200个字", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //页显示Pager滑动监听
        pagePager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //todo 滑动文档显示页面
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //下载按钮监听
        downBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //设置背景颜色变暗
                final WindowManager.LayoutParams lp =
                        KnowledgeDownloadDetailActivity.this.getWindow().getAttributes();
                lp.alpha = 0.3f;
                KnowledgeDownloadDetailActivity.this.getWindow().setAttributes(lp);

                //todo 弹出下载提示框
                PopActionUtil popActionUtil = PopActionUtil.getInstance(KnowledgeDownloadDetailActivity.this);
                PopupWindow popupWindow = popActionUtil.getDownloadPopActionMenu();
                popupWindow.showAtLocation(commentTxt.getRootView(), Gravity.CENTER, 0, 0);

                popActionUtil.setActionListener(new PopActionListener() {
                    @Override
                    public void onAction(String action) {
                        switch (action) {
                            case "确定":
                                Toast.makeText(KnowledgeDownloadDetailActivity.this, "确定", Toast.LENGTH_LONG).show();
                                break;
                            case "取消":
                                Toast.makeText(KnowledgeDownloadDetailActivity.this, "取消", Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                break;
                        }
                        lp.alpha = 1f;
                        KnowledgeDownloadDetailActivity.this.getWindow().setAttributes(lp);
                    }

                    @Override
                    public void onDismiss() {

                    }
                });
            }
        });

    }

}
