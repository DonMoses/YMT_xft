package com.ymt.demo1.main.sign;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
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
import com.ymt.demo1.customViews.MyCheckView;
import com.ymt.demo1.utils.AppContext;
import com.ymt.demo1.utils.BaseURLUtil;
import com.ymt.demo1.utils.PopActionListener;
import com.ymt.demo1.utils.PopActionUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

/**
 * Created by Dan on 2015/4/2
 */
public class SignInFragment extends Fragment {
    public static final String FLAG = "SignInFragment";
    private String account;
    private String psw;

    private EditText accountETxt;
    private EditText pswETxt;
    private SharedPreferences sharedPreferences;
    private RequestQueue queue;
    private boolean isFromConsult;
    private Button signInBtn;

    public static final String SIGN_IN_SETTING = "sign_in_setting";
    public static final String DO_REMEMBER_NAME = "0";
    public static final String DO_REMEMBER_PSW = "1";
    public static final String REMEMBERED_NAME = "name";
    public static final String REMEMBERED_PSW = "psw";
    private SharedPreferences preferences;

    /**
     * 记住昵称、密码
     */
    private MyCheckView rememberNameCheck;
    private MyCheckView rememberPswCheck;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_sign_in, container, false);
        isFromConsult = getActivity().getIntent().getBooleanExtra("isFromConsult", false);
        queue = Volley.newRequestQueue(getActivity());
        initView(view);
        return view;
    }

    protected void initView(View view) {
        final Button autoAccBtn = (Button) view.findViewById(R.id.auto_create_account_btn);
        accountETxt = (EditText) view.findViewById(R.id.sign_in_account_text);
        pswETxt = (EditText) view.findViewById(R.id.sign_in_psw_text);

        sharedPreferences = AppContext.getSaveAccountPreferences(getActivity());

        //登录按钮
        signInBtn = (Button) view.findViewById(R.id.sign_in_btn);
        final ImageButton wechatBtn = (ImageButton) view.findViewById(R.id.sign_in_wechat);
        final ImageButton qqBtn = (ImageButton) view.findViewById(R.id.sign_in_qq);
        final ImageButton sinaBtn = (ImageButton) view.findViewById(R.id.sign_in_sina);

        /*
        记住昵称、记住密码
         */
        rememberNameCheck = (MyCheckView) view.findViewById(R.id.remember_name);
        rememberPswCheck = (MyCheckView) view.findViewById(R.id.remember_psw);

        /*
        记住昵称、密码外部layout
         */
        LinearLayout nameCheckLayout = (LinearLayout) view.findViewById(R.id.remember_name_layout);
        LinearLayout pswCheckLayout = (LinearLayout) view.findViewById(R.id.remember_psw_layout);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo 获取网络账号密码信息
                switch (v.getId()) {
                    case R.id.auto_create_account_btn:
                        //自动分配账号
                        queue.add(getAutoAccount());
                        break;
                    case R.id.sign_in_btn:
                        /*获取用户名和密码，匹配则登录（跳转到个人界面），不匹配弹出提示框*/
                        account = accountETxt.getText().toString();
                        psw = pswETxt.getText().toString();
                         /*
                        如果更改账号登录，则删除前一账号的QQ信息
                         */
                        String savedUserAccount = sharedPreferences.getString("account", "");
                        if (!savedUserAccount.equals(account)) {
                            DataSupport.deleteAll(QQChatInfo.class);
                            DataSupport.deleteAll(QQMsg.class);
                            DataSupport.deleteAll(SearchedConsult.class);
                            DataSupport.deleteAll(SearchString.class);
                        }
                        queue.add(signInRequest(account, psw));
                        break;
                    case R.id.sign_in_wechat:
                        //todo 使用微信账号登录

                        break;
                    case R.id.sign_in_qq:
                        //todo 使用qq账号登录

                        break;
                    case R.id.sign_in_sina:
                        //todo 使用sina账号登录

                        break;
                    case R.id.remember_name_layout:
                        rememberNameCheck.callOnClick();            //记住昵称
                        if ((!rememberNameCheck.isChecked()) && rememberPswCheck.isChecked()) {
                            rememberPswCheck.callOnClick();       //必须记住昵称才能记住密码
                        }
                        break;
                    case R.id.remember_psw_layout:
                        rememberPswCheck.callOnClick();
                        if (!rememberNameCheck.isChecked()) {
                            rememberNameCheck.callOnClick();         //必须记住昵称才能记住密码
                        }
                        break;
                    default:
                        break;

                }
            }
        };

        nameCheckLayout.setOnClickListener(onClickListener);
        pswCheckLayout.setOnClickListener(onClickListener);
        autoAccBtn.setOnClickListener(onClickListener);
        signInBtn.setOnClickListener(onClickListener);
        wechatBtn.setOnClickListener(onClickListener);
        qqBtn.setOnClickListener(onClickListener);
        sinaBtn.setOnClickListener(onClickListener);

        if (preferences.getBoolean(DO_REMEMBER_NAME, false)) {
            accountETxt.setText(preferences.getString(REMEMBERED_NAME, ""));
            rememberNameCheck.setIsChecked(true);
        }
        if (preferences.getBoolean(DO_REMEMBER_PSW, false)) {
            pswETxt.setText(preferences.getString(REMEMBERED_PSW, ""));
            rememberPswCheck.setIsChecked(true);
        }


    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        preferences = getActivity().getSharedPreferences(SIGN_IN_SETTING, Context.MODE_PRIVATE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //保存设置
        SharedPreferences.Editor editor = preferences.edit();
        if (rememberNameCheck.isChecked()) {
            editor.putBoolean(DO_REMEMBER_NAME, true);
            editor.putString(REMEMBERED_NAME, accountETxt.getText().toString());
            editor.apply();
        } else {
            editor.clear();
            editor.apply();
        }
        if (rememberPswCheck.isChecked()) {
            editor.putBoolean(DO_REMEMBER_PSW, true);
            editor.putString(REMEMBERED_PSW, pswETxt.getText().toString());
            editor.apply();
        } else {
            editor.remove(REMEMBERED_PSW);
            editor.remove(DO_REMEMBER_PSW);
            editor.apply();
        }

    }

    protected StringRequest signInRequest(final String account, final String psw) {
        String url = BaseURLUtil.doSignIn(account, psw);

        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getString("result").equals("Y")) {
                        //登录成功
                        Toast.makeText(getActivity(), R.string.sign_in_ok, Toast.LENGTH_SHORT).show();
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

                        //保存设置
                        SharedPreferences.Editor editor1 = preferences.edit();
                        if (rememberNameCheck.isChecked()) {
                            editor1.putBoolean(DO_REMEMBER_NAME, true);
                            editor1.putString(REMEMBERED_NAME, accountETxt.getText().toString());
                            editor1.apply();
                        } else {
                            editor1.clear();
                            editor1.apply();
                        }
                        if (rememberPswCheck.isChecked()) {
                            editor1.putBoolean(DO_REMEMBER_PSW, true);
                            editor1.putString(REMEMBERED_PSW, pswETxt.getText().toString());
                            editor1.apply();
                        } else {
                            editor1.remove(REMEMBERED_PSW);
                            editor1.remove(DO_REMEMBER_PSW);
                            editor1.apply();
                        }
                        getActivity().finish();

                    } else {
                        Toast.makeText(getActivity(), jsonObject.getString("result"), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getActivity(), volleyError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        request.setRetryPolicy(new DefaultRetryPolicy(8 * 1000, 1, 1));
        return request;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //这里不能调用super的方法，如果调用则下面代码失效
        switch (requestCode) {
            case 0:
                if (resultCode == Activity.RESULT_OK) {      //从注册界面返回的账号和密码
                    accountETxt.setText(data.getStringExtra("account"));
                    pswETxt.setText(data.getStringExtra("password"));
//                    Log.e("TAG", "account>>>" + data.getStringExtra("account"));
//                    Log.e("TAG", "password>>>" + data.getStringExtra("password"));
                }
                break;
            case 128:
                if (resultCode == Activity.RESULT_OK) {      //从注册界面返回的账号和密码
                    accountETxt.setText(data.getStringExtra("loginName"));
                    pswETxt.setText(data.getStringExtra("psw"));
//                    Log.e("TAG", "account>>>" + data.getStringExtra("account"));
//                    Log.e("TAG", "password>>>" + data.getStringExtra("password"));
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onPause() {
        //保存成功的信息到本地sharedPreference
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("account", account);
        editor.putString("password", psw);
        editor.apply();
        super.onPause();
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
//                        String psw = obj.optString("pwd");
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
                getActivity().getWindow().getAttributes();
        lp.alpha = 0.3f;
        getActivity().getWindow().setAttributes(lp);
        PopActionUtil popActionUtil = PopActionUtil.getInstance(getActivity());
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
                getActivity().getWindow().setAttributes(lp);
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
