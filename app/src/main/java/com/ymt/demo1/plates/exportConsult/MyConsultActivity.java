package com.ymt.demo1.plates.exportConsult;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;

import com.ymt.demo1.R;
import com.ymt.demo1.customViews.MyTitle;
import com.ymt.demo1.baseClasses.BaseFloatActivity;
import com.ymt.demo1.main.search.FullSearchActivity;
import com.ymt.demo1.plates.exportConsult.typedUserConsult.ExportUserConsultFragment;
import com.ymt.demo1.plates.exportConsult.typedUserConsult.NormalUserConsultFragment;
import com.ymt.demo1.utils.AppContext;

/**
 * Created by Dan on 2015/5/12
 */
public class MyConsultActivity extends BaseFloatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_consult);
        initTitle();

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        if (TextUtils.isEmpty(AppContext.user_type)) {
            ft.add(R.id.content, new NormalUserConsultFragment());
        } else {
            //// TODO: 2015/11/12
            switch (AppContext.user_type) {
                case "003":     //值守专家界面
                case "004":     //资深专家界面
                    ft.replace(R.id.content, ExportUserConsultFragment.newInstance(AppContext.user_type));
                    break;
                default:        //普通用户界面
                    ft.replace(R.id.content, new NormalUserConsultFragment());
                    break;
            }
        }

        ft.commit();
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

        title.setOnRightActionClickListener(new MyTitle.OnRightActionClickListener() {
            @Override
            public void onRightLClick() {
                startActivity(new Intent(MyConsultActivity.this, FullSearchActivity.class));  //打开搜索界面
            }

            @Override
            public void onRightRClick() {
                //此视图，右边只包含L按钮

            }
        });
    }

}


