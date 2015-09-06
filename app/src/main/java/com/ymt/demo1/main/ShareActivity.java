package com.ymt.demo1.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ymt.demo1.R;
import com.ymt.demo1.baseClasses.BaseFloatActivity;
import com.ymt.demo1.customViews.MyTitle;

/**
 * Created by DonMoses on 2015/9/6
 */
public class ShareActivity extends BaseFloatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
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

    private void initView() {
        Button shareBtn = (Button) findViewById(R.id.do_share_btn);
        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doShare();
            }
        });
    }

    private void doShare() {
        Intent intent = new Intent(Intent.ACTION_SEND);                                     //分享
        intent.setType("*/*");
//        intent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.recommend_info));
//        intent.putExtra(Intent.EXTRA_TEXT, R.string.recommend_info);
        intent.putExtra(Intent.EXTRA_TEXT,
                getResources().getString(R.string.recommend_info) + "\n" +
//                        getResources().getString(R.string.app_download_url)
                        "http://android.myapp.com/myapp/detail.htm?apkName=com.ymt.demo1"
        );

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(Intent.createChooser(intent, getTitle()));
    }
}
