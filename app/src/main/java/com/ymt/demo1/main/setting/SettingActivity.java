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
import com.ymt.demo1.main.ShareActivity;
import com.ymt.demo1.main.sign.SignInUpActivity;
import com.ymt.demo1.utils.AppContext;
import com.ymt.demo1.utils.BaseURLUtil;
import com.ymt.demo1.main.sign.ChangePswActivity;
import com.ymt.demo1.R;
import com.ymt.demo1.adapter.LongClickItemsAdapter;
import com.ymt.demo1.main.sign.SignInFragment;
import com.ymt.demo1.zxing.activity.CaptureActivity;

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
                    case 0:            //登录、注册
                        startActivity(new Intent(SettingActivity.this, SignInUpActivity.class));
                        finish();
                        break;
                    case 1:
                        //todo          //修改信息
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
                    case 4:            //消息声音
                        startActivity(new Intent(SettingActivity.this, VoiceSettingActivity.class));
                        break;
                    case 5:            //存储设置
                        startActivity(new Intent(SettingActivity.this, ManageStoreActivity.class));
                        finish();
                        break;
                    case 6:            //分享
                        startActivity(new Intent(SettingActivity.this, ShareActivity.class));
                        finish();
                        break;
                    case 7:
                        //todo          //扫一扫
                        //todo 二维码扫描
                        Intent scanIntent = new Intent(SettingActivity.this, CaptureActivity.class);
                        startActivityForResult(scanIntent, 12345);
                        break;
                    case 8:             //关于我们
                        startActivity(new Intent(SettingActivity.this, AboutUsActivity.class));
                        finish();
                        break;
                    case 9:
                        //todo          //软件更新
                        break;
                    case 10:             //卸载
                        Uri packageURI = Uri.parse("package:com.ymt.demo1");//通过程序的包名创建URI           //卸载
                        Intent deleteIntent = new Intent(Intent.ACTION_DELETE, packageURI);
                        startActivity(deleteIntent); //执行卸载程序
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
                AppContext.now_user_id = 0;
                AppContext.now_session_id = "";
                DataSupport.deleteAll(QQMsg.class);
                DataSupport.deleteAll(QQChatInfo.class);
                mQueue.add(signOutAction(AppContext.now_session_id));
                Intent intent = new Intent(SettingActivity.this, SignInUpActivity.class);
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
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 12345 && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("result");
            if (scanResult == null)
                return;
            String url = scanResult; // web address
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        }
    }
}
