package com.ymt.demo1.plates.knowledge;

import android.app.DownloadManager;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
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

import com.android.volley.toolbox.Volley;
import com.ymt.demo1.R;
import com.ymt.demo1.adapter.SimpleTxtItemAdapter;
import com.ymt.demo1.baseClasses.BaseActivity;
import com.ymt.demo1.beams.knowledge.KnowledgeItemBZGF;
import com.ymt.demo1.beams.knowledge.KnowledgeItemKYWX;
import com.ymt.demo1.customViews.MyTitle;
import com.ymt.demo1.main.BaseURLUtil;
import com.ymt.demo1.main.PopActionListener;
import com.ymt.demo1.main.PopActionUtil;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Dan on 2015/4/29
 */
public class KnowledgeItemDetailActivity extends BaseActivity {
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
                                if (isBZGF) {
//                                    downloadBZGFFile(111 + ".mp3", itemBZGF.getPdf_id());
                                    downloadBZGFFile(itemBZGF.getArticle_title() + ".pdf", itemBZGF.getPdf_id());
                                } else {
                                    downloadBZGFFile(itemKYWX.getArticle_title() + ".pdf", itemKYWX.getPdf_id());
                                }
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

    protected void downloadBZGFFile(String name, String pdf_id) {
        DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(BaseURLUtil.PDF_BASE + pdf_id);
//        Uri uri = Uri.parse("http://tingge.5nd.com/20060919/2015/2015-7-8/67322/1.Mp3");
        DownloadManager.Request request = new DownloadManager.Request(uri);

        //设置允许使用的网络类型，这里是移动网络和wifi都可以
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);

        //禁止发出通知，既后台下载，如果要使用这一句必须声明一个权限：android.permission.DOWNLOAD_WITHOUT_NOTIFICATION
        //request.setShowRunningNotification(false);

        //不显示下载界面
        //request.setVisibleInDownloadsUi(false);
        /*设置下载后文件存放的位置,如果sdcard不可用，那么设置这个将报错，因此最好不设置如果sdcard可用，下载后的文件        在/mnt/sdcard/Android/data/packageName/files目录下面，如果sdcard不可用,设置了下面这个将报错，不设置，下载后的文件在/cache这个  目录下面*/
        request.setDestinationInExternalFilesDir(this, null, name);

        long id = downloadManager.enqueue(request);
        //TODO 把id保存好，在接收者里面要用，最好保存在Preferences里面
    }

}
