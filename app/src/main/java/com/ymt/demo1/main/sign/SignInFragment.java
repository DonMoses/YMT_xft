package com.ymt.demo1.main.sign;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import com.ymt.demo1.beams.consult_cato.ConsultItem;
import com.ymt.demo1.beams.expert_consult.QQChatInfo;
import com.ymt.demo1.beams.expert_consult.QQMsg;
import com.ymt.demo1.customViews.MyCheckView;
import com.ymt.demo1.utils.AppContext;
import com.ymt.demo1.utils.BaseURLUtil;
import com.ymt.demo1.utils.PopActionListener;
import com.ymt.demo1.utils.PopActionUtil;

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
                        //todo 自动分配账号
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
                            DataSupport.deleteAll(ConsultItem.class);
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
        Log.e("TAG", " login in url:  " + url);

        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    SignInUpActivity activity = (SignInUpActivity) getActivity();

                    if (jsonObject.getString("result").equals("N")) {

                        Toast.makeText(getActivity(), "登陆失败！", Toast.LENGTH_SHORT).show();
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("account", account);
                        editor.putString("password", psw);
                        editor.putString("now_user_id", "");
                        editor.putString("now_session_id", "");
                        editor.apply();

                        AppContext.now_session_id = "";
                        AppContext.now_user_id = 0;
                        AppContext.now_user_name = "";

                        activity.isSigned = false;

                    } else if (jsonObject.getString("result").equals("Y")) {

                        //登录成功
                        Toast.makeText(getActivity(), R.string.sign_in_ok, Toast.LENGTH_SHORT).show();
                        JSONObject listData = jsonObject.getJSONObject("datas").getJSONObject("listData");
                        int userId = listData.optInt("uid");
                        String userSId = listData.optString("sId");

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("account", account);
                        editor.putString("password", psw);
                        editor.putInt("now_user_id", userId);
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

                        activity.isSigned = true;
                        activity.finish();
                    }

                } catch (JSONException e) {
                    AppContext.toastBadJson();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                AppContext.toastBadInternet();
            }
        });

        request.setRetryPolicy(new DefaultRetryPolicy(8 * 1000, 1, 1));
        return request;
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
    //默认密码
    private final static String DEFAULT_PSW = "000000";

    protected StringRequest getAutoAccount() {
        return new StringRequest(BaseURLUtil.AUTO_CREATE_ACCOUNT, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    accountETxt.setText(jsonObject.getJSONObject("datas").optString("listData"));
                    pswETxt.setText(DEFAULT_PSW);
                    popAutoAccountInfo(accountETxt.getText().toString(), DEFAULT_PSW);
                } catch (JSONException e) {
                    AppContext.toastBadJson();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                AppContext.toastBadInternet();
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
                Activity activity = getActivity();
                if (activity != null) {
                    activity.getWindow().setAttributes(lp);
                }
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
