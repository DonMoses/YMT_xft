package com.ymt.demo1.plates.exportConsult;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.ymt.demo1.R;
import com.ymt.demo1.baseClasses.BaseActivity;
import com.ymt.demo1.beams.Export;
import com.ymt.demo1.customViews.MyTitle;


/**
 * Created by Dan on 2015/5/14
 */
public class ConsultChatActivity extends BaseActivity {

    Export export;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consult_chat);
        initTitle();

    }

    protected void initTitle() {
        //todo 调用网络接口，获取聊天记录
        export = getIntent().getBundleExtra("export_info").getParcelable("export_info");
        final MyTitle title = (MyTitle) findViewById(R.id.my_title);
        title.setTitleStyle(MyTitle.TitleStyle.RIGHT_ICON_L);
        title.updateLeftLIcon2Txt("预约");
        title.updateCenterTitle(export.getName());
        if (title.getChildCount() == 3) {
            title.getChildAt(2).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //todo 跳转到预约界面
                    Toast.makeText(ConsultChatActivity.this, "预约...", Toast.LENGTH_SHORT).show(); //打开预约界面
                }
            });
        }

        title.setOnLeftActionClickListener(new MyTitle.OnLeftActionClickListener() {
            @Override
            public void onClick() {
                finish();
            }
        });

        title.setOnRightActionClickListener(new MyTitle.OnRightActionClickListener() {
            @Override
            public void onRightLClick() {
                //上面已经使用textView 替换布局中的ImageView
            }

            @Override
            public void onRightRClick() {
                //此视图，右边只包含L按钮

            }
        });
    }
}
