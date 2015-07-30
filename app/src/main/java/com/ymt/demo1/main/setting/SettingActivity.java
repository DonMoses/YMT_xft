package com.ymt.demo1.main.setting;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ymt.demo1.beams.expert_consult.QQChatInfo;
import com.ymt.demo1.beams.expert_consult.QQMsg;
import com.ymt.demo1.customViews.MyTitle;
import com.ymt.demo1.main.AppContext;
import com.ymt.demo1.main.BaseURLUtil;
import com.ymt.demo1.main.sign.ChangePswActivity;
import com.ymt.demo1.R;
import com.ymt.demo1.adapter.LongClickItemsAdapter;
import com.ymt.demo1.main.sign.SignInActivity;
import com.ymt.demo1.main.sign.SignUpActivity;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Dan on 2015/4/2
 */
public class SettingActivity extends Activity {
    private RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mQueue = Volley.newRequestQueue(this);
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
                    case 1:            //注册
                        startActivity(new Intent(SettingActivity.this, SignUpActivity.class));
                        finish();
                        break;
                    case 2:            //修改登录密码
                        Intent intent1 = new Intent(SettingActivity.this, ChangePswActivity.class);
                        intent1.putExtra("loginName", AppContext.now_user_name);
                        startActivityForResult(intent1, 128);
                        finish();
                        break;
                    case 3:            //自定义皮肤
                        startActivity(new Intent(SettingActivity.this, ManageAppearanceActivity.class));
                        finish();
                        break;
                    case 4:            //存储设置
                        startActivity(new Intent(SettingActivity.this, ManageStoreActivity.class));
                        finish();
                        break;
                    case 5:            //推荐给好友
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("*/*");
                        intent.putExtra(Intent.EXTRA_SUBJECT, "好友推荐");
                        intent.putExtra(Intent.EXTRA_TEXT, "嗨，我正在新消防，你也来试试！");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(Intent.createChooser(intent, getTitle()));
                        finish();
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
                //跳转到登录界面
                getSharedPreferences("saved_account", MODE_PRIVATE).edit().clear().apply();
                AppContext.headerPic = null;
                AppContext.now_user_name = "";
                AppContext.now_user_id = "";
                AppContext.now_session_id = "";
                DataSupport.deleteAll(QQMsg.class);
                DataSupport.deleteAll(QQChatInfo.class);
                mQueue.add(signOutAction(AppContext.now_session_id));
                Intent intent = new Intent(SettingActivity.this, SignInActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);                //清楚前面所有Activity
                startActivity(intent);
                //结束本活动
                finish();

            }
        });
    }

    /**
     * 退出账号操作
     */
    protected StringRequest signOutAction(String sId) {
        return new StringRequest(BaseURLUtil.signOutAct(sId), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("TAG", ">>>>>>>>>..s>>." + s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
    }
}
