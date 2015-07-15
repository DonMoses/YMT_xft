package com.ymt.demo1.main.sign;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ymt.demo1.R;
import com.ymt.demo1.beams.SearchString;
import com.ymt.demo1.beams.consult_cato.SearchedConsult;
import com.ymt.demo1.beams.expert_consult.QQChatInfo;
import com.ymt.demo1.beams.expert_consult.QQMsg;
import com.ymt.demo1.customViews.MyTitle;
import com.ymt.demo1.launchpages.MainActivity;
import com.ymt.demo1.main.AppContext;
import com.ymt.demo1.main.BaseURLUtil;
import com.ymt.demo1.main.PopActionListener;
import com.ymt.demo1.main.PopActionUtil;
import com.ymt.demo1.mainStyles.CircleMenuActivity;
import com.ymt.demo1.mainStyles.NavigationMenuActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

/**
 * Created by Dan on 2015/4/2
 */
public class SignInActivity extends Activity {
    String account;
    String psw;

    private EditText accountETxt;
    private EditText pswETxt;
    private SharedPreferences sharedPreferences;
    private RequestQueue queue;
    private boolean isFromConsult;
    private Button signInBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isFromConsult = getIntent().getBooleanExtra("isFromConsult", false);
        queue = Volley.newRequestQueue(this);
        setContentView(R.layout.activity_sign_in);
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
        final Button signUpBtn = (Button) findViewById(R.id.jump_sign_up);
//        final Button foundPswBtn = (Button) findViewById(R.id.jump_found_psw);
        final Button autoAccBtn = (Button) findViewById(R.id.auto_create_account_btn);
        accountETxt = (EditText) findViewById(R.id.sign_in_account_text);
        pswETxt = (EditText) findViewById(R.id.sign_in_psw_text);

        sharedPreferences = AppContext.getSaveAccountPrefecences(this);

              /*
        从sharedPreference获取保存到本地的账号信息
         */

        String savedAccount = sharedPreferences.getString("account", "");
        String savedPsw = sharedPreferences.getString("password", "");
        accountETxt.setText(savedAccount);
        pswETxt.setText(savedPsw);

        //登录按钮
        signInBtn = (Button) findViewById(R.id.sign_in_btn);
        final ImageButton wechatBtn = (ImageButton) findViewById(R.id.sign_in_wechat);
        final ImageButton qqBtn = (ImageButton) findViewById(R.id.sign_in_qq);
        final ImageButton sinaBtn = (ImageButton) findViewById(R.id.sign_in_sina);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo 获取网络账号密码信息
                switch (v.getId()) {
                    case R.id.jump_sign_up:
                        //跳转到注册界面
                        startActivityForResult(new Intent(SignInActivity.this, SignUpActivity.class), 0);
                        break;
                    case R.id.auto_create_account_btn:
                        //自动分配账号
                        queue.add(getAutoAccount());
                        break;
//                    case R.id.jump_found_psw:
//                        //找回密码逻辑
////                        foundPswBtn.setBackgroundColor(Color.BLUE);
////                        foundPswBtn.setTextColor(Color.WHITE);
//                        Intent intent = new Intent(SignInActivity.this, ChangePswActivity.class);
//                        intent.putExtra("loginName", accountETxt.getText().toString());
//                        startActivityForResult(intent, 128);
//                        break;
                    case R.id.sign_in_btn:
                        /*获取用户名和密码，匹配则登录（跳转到个人界面），不匹配弹出提示框*/
                        account = accountETxt.getText().toString();
                        psw = pswETxt.getText().toString();
                         /*
                        如果更改账号登录，则删除前一账号的QQ信息
                         */
                        String savedUserAccount = sharedPreferences.getString("account", "");
                        assert savedUserAccount != null;
                        if (!savedUserAccount.equals(account)) {
                            DataSupport.deleteAll(QQChatInfo.class);
                            DataSupport.deleteAll(QQMsg.class);
                            DataSupport.deleteAll(SearchedConsult.class);
                            DataSupport.deleteAll(SearchString.class);
                        }
                        queue.add(signInRequest(account, psw));
                        break;
                    case R.id.sign_in_wechat:
                        //使用微信账号登录

                        break;
                    case R.id.sign_in_qq:
                        //使用qq账号登录

                        break;
                    case R.id.sign_in_sina:
                        //使用sina账号登录

                        break;
                    default:
                        break;

                }
            }
        };

        signUpBtn.setOnClickListener(onClickListener);
        autoAccBtn.setOnClickListener(onClickListener);
//        foundPswBtn.setOnClickListener(onClickListener);
        signInBtn.setOnClickListener(onClickListener);
        wechatBtn.setOnClickListener(onClickListener);
        qqBtn.setOnClickListener(onClickListener);
        sinaBtn.setOnClickListener(onClickListener);

    }

    protected StringRequest signInRequest(final String account, final String psw) {
        String url = BaseURLUtil.doSignIn(account, psw);

        return new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getString("result").equals("Y")) {
                        //登录成功
                        Toast.makeText(SignInActivity.this, R.string.sign_in_ok, Toast.LENGTH_SHORT).show();
                        String userId = jsonObject.optString("id");
                        String userSId = jsonObject.optString("sId");

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("account", account);
                        editor.putString("password", psw);
                        editor.putString("now_user_id", userId);
                        editor.putString("now_session_id", userSId);
                        editor.apply();

                        AppContext.now_session_id = userSId;
                        AppContext.now_user_id = userId;
                        AppContext.now_user_name = account;

                        queue.add(AppContext.getHeader(jsonObject.optString("headPic")));

                        chooseLaunchStyle();
                    } else {
                        Toast.makeText(SignInActivity.this, jsonObject.getString("result"), Toast.LENGTH_SHORT).show();
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("account", account);
                        editor.putString("password", psw);
                        editor.putString("now_user_id", "");
                        editor.putString("now_session_id", "");
                        editor.apply();

                        AppContext.now_session_id = "";
                        AppContext.now_user_id = "";
                        AppContext.now_user_name = "";
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //这里不能调用super的方法，如果调用则下面代码失效
        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {      //从注册界面返回的账号和密码
                    accountETxt.setText(data.getStringExtra("account"));
                    pswETxt.setText(data.getStringExtra("password"));
                    Log.e("TAG", "account>>>" + data.getStringExtra("account"));
                    Log.e("TAG", "password>>>" + data.getStringExtra("password"));
                }
                break;
            case 128:
                if (resultCode == RESULT_OK) {      //从注册界面返回的账号和密码
                    accountETxt.setText(data.getStringExtra("loginName"));
                    pswETxt.setText(data.getStringExtra("psw"));
                    Log.e("TAG", "account>>>" + data.getStringExtra("account"));
                    Log.e("TAG", "password>>>" + data.getStringExtra("password"));
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onPause() {
        //保存成功的信息到本地sharedPreference
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("account", account);
        editor.putString("password", psw);
        editor.apply();
        super.onPause();
    }

    protected void chooseLaunchStyle() {
        SharedPreferences sharedPreferences =
                getSharedPreferences(MainActivity.SETTING_PREFERENCES, MODE_PRIVATE);
        int style = sharedPreferences.getInt(MainActivity.LAUNCH_STYLE_KEY, 0);
        switch (style) {
            case MainActivity.LAUNCH_STYLE_CIRCLE_MODE:
                startActivity(new Intent(this, CircleMenuActivity.class));
                finish();
                break;
            case MainActivity.LAUNCH_STYLE_SLIDE_MODE:
                startActivity(new Intent(this, NavigationMenuActivity.class));
                finish();
                break;
            default:
                break;
        }
    }

    /**
     * 自动生成账号
     */
    protected StringRequest getAutoAccount() {
        return new StringRequest(BaseURLUtil.AUTO_CREATE_ACCOUNT, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getString("result").equals("Y")) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        JSONArray jsonArray = jsonObject1.getJSONArray("listData");
                        JSONObject obj = jsonArray.getJSONObject(0);
                        String account = obj.optString("login_name");
                        String psw = obj.optString("pwd");
                        accountETxt.setText(account);
//                        pswETxt.setText(psw);
//                        popAutoAccountInfo(account, psw);
                        pswETxt.setText("123456");
                        popAutoAccountInfo(account, "123456");

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
    }

    /**
     * 弹出自动配置的账号信息
     */
    protected void popAutoAccountInfo(String login_name, String pwd) {
        final WindowManager.LayoutParams lp =
                SignInActivity.this.getWindow().getAttributes();
        lp.alpha = 0.3f;
        SignInActivity.this.getWindow().setAttributes(lp);
        PopActionUtil popActionUtil = PopActionUtil.getInstance(SignInActivity.this);
        popActionUtil.setActionListener(new PopActionListener() {
            @Override
            public void onAction(String action) {
                switch (action) {
                    case "确定":


                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onDismiss() {
                lp.alpha = 1f;
                SignInActivity.this.getWindow().setAttributes(lp);
            }
        });
        //todo 获取服务器返回的随机账号和密码
        PopupWindow popupWindow =
                popActionUtil.getSubmitConsultUnsignedPop(login_name, pwd, isFromConsult);
        int width = (int) (signInBtn.getRootView().getWidth() * 0.8);
        popupWindow.setWidth(width);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.showAtLocation(signInBtn.getRootView(), Gravity.CENTER, 0, 0);
    }

}
