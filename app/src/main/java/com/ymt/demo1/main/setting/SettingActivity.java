package com.ymt.demo1.main.setting;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.ymt.demo1.customViews.MyTitle;
import com.ymt.demo1.mainStyles.CircleMenuActivity;
import com.ymt.demo1.R;
import com.ymt.demo1.adapter.LongClickItemsAdapter;
import com.ymt.demo1.main.sign.SignInActivity;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Dan on 2015/4/2
 */
public class SettingActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_setting);
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

    protected void initView() {

        ListView settingListView = (ListView) findViewById(R.id.setting_list_view);
        String[] quests = getResources().getStringArray(R.array.setting_array);
        final LongClickItemsAdapter settingAdapter = new LongClickItemsAdapter(this);
        settingListView.setAdapter(settingAdapter);
        ArrayList<String> mQuests = new ArrayList<>();
        Collections.addAll(mQuests, quests);
        settingAdapter.setmList(mQuests);

        settingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //跳转到设置详细页面
                switch (position) {
                    case 0:            //账号登陆
                        startActivity(new Intent(SettingActivity.this, SignInActivity.class));
                        finish();
                        break;
                    case 1:            //修改登录密码
                        startActivity(new Intent(SettingActivity.this, ManagePswActivity.class));
                        finish();
                        break;
                    case 2:            //自定义皮肤
                        startActivity(new Intent(SettingActivity.this, ManageAppearanceActivity.class));
                        finish();
                        break;
                    case 3:            //存储设置
                        startActivity(new Intent(SettingActivity.this, ManageStoreActivity.class));
                        finish();
                        break;
                    case 4:            //推荐给好友
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("*/*");
                        intent.putExtra(Intent.EXTRA_SUBJECT, "好友推荐");
                        intent.putExtra(Intent.EXTRA_TEXT, "嗨，我正在消防通，你也来试试！");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(Intent.createChooser(intent, getTitle()));
                        finish();
                        break;
                    case 5:            //关于我们
                        startActivity(new Intent(SettingActivity.this, AboutUsActivity.class));
                        finish();
                        break;
                    case 6:            //软件卸载
                        Uri packageURI = Uri.parse("package:com.ymt.demo1");//通过程序的包名创建URI
                        Intent deleteIntent = new Intent(Intent.ACTION_DELETE, packageURI);
                        startActivity(deleteIntent); //执行卸载程序
                        break;
                    default:
                        break;
                }

            }
        });

        Button signOutBtn = (Button) findViewById(R.id.sign_out_btn);
        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //退出当前账号
                //跳转到主界面
                startActivity(new Intent(SettingActivity.this, CircleMenuActivity.class));
                //退出账号

                //结束本活动
                finish();

            }
        });
    }


}
