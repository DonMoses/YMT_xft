package com.ymt.demo1.main.sign;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ymt.demo1.R;
import com.ymt.demo1.customViews.MyCheckView;
import com.ymt.demo1.utils.BaseURLUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.tablemanager.Connector;

/**
 * Created by Moses on 2015
 */
public class SignUpFragment extends Fragment {
    public static final String NORMAL_USER = "001";
    //    public static final String EXPORT_USER = "002";
//    public static final String MEMBER_USER = "003";
//    public static final String MANAGER_USER = "004";
    private RequestQueue queue;
    private MyCheckView myCheckView;
    //&loginname=moses&pwd=123&phone=333&t=001(t表示用户类型，包括001，002，003，004)

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_sign_up, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Connector.getDatabase();
        queue = Volley.newRequestQueue(getActivity());
    }

    /**
     * 初始化账号信息
     * 如果已经账号或手机号码已经注册，则提示已经注册，请登录或注册其他账号。
     */
    protected void initView(View view) {
        /*
        本地模拟
         */
        //初始化控件
        final EditText phoneTxt = (EditText) view.findViewById(R.id.input_phone_num);
        final EditText accountTxt = (EditText) view.findViewById(R.id.input_account);
        final EditText pswTxt = (EditText) view.findViewById(R.id.input_psw);
        final EditText rePswTxt = (EditText) view.findViewById(R.id.re_input_psw);
        final TextView licenseTxt = (TextView) view.findViewById(R.id.licence_txt);
        /*获取验证码、 输入验证码
         */
        final TextView getCheckNums = (TextView) view.findViewById(R.id.get_check_nums);
        final EditText inputCheckNums = (EditText) view.findViewById(R.id.input_check_nums);
        getCheckNums.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo 通过手机号获取验证码

            }
        });


        /**
         *  todo 使用spannableString 设置富文本
         *   点击打开免责申明
         */
        SpannableString spannableString = new SpannableString(getString(R.string.license));
        ForegroundColorSpan span = new ForegroundColorSpan(getResources().getColor(R.color.material_blue_grey_800));
        spannableString.setSpan(span, 0, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        licenseTxt.setText(spannableString);
        //switch(消防协议)
        myCheckView = (MyCheckView) view.findViewById(R.id.switch_liscense);
        //注册按钮事件
        Button signUpBtn = (Button) view.findViewById(R.id.do_sign_btn);
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //四个输入组件的内容
                String phoneNum = phoneTxt.getText().toString();
                String account = accountTxt.getText().toString();
                String psw = pswTxt.getText().toString();
                String rePsw = rePswTxt.getText().toString();
                //验证码
                String checkNums = inputCheckNums.getText().toString();
                //todo 一下的if(){}else{} 逻辑中加入对验证码的判断

                if (!TextUtils.isEmpty(phoneNum) && !TextUtils.isEmpty(account) && !TextUtils.isEmpty(psw) && psw.equals(rePsw)) {
                    //已经阅读注册说明
                    if (myCheckView.isChecked()) {
                        queue.add(signUpRequest(account, psw, phoneNum, NORMAL_USER));
                    } else {
                        Toast.makeText(getActivity(), "请先阅读并同意《新消防安全协议》！", Toast.LENGTH_SHORT).show();
                    }

                } else if (TextUtils.isEmpty(phoneNum)) {
                    //提示手机号错误
                    Toast.makeText(getActivity(), "手机号不能为空！", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(account)) {
                    //提示账号错误
                    Toast.makeText(getActivity(), "账号不能为空！", Toast.LENGTH_SHORT).show();
                } else {
                    //提示密码错误
                    Toast.makeText(getActivity(), "密码输入错误，请重新输入！", Toast.LENGTH_SHORT).show();
                }
            }
        });

        LinearLayout licence = (LinearLayout) view.findViewById(R.id.liscense_layout);
        licence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myCheckView.callOnClick();
            }
        });
    }

    protected void jumpToSignIn(String account, String psw) {
        Toast.makeText(getActivity(), "注册成功！", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        intent.putExtra("account", account);
        intent.putExtra("password", psw);
        getActivity().setResult(Activity.RESULT_OK, intent);

        getActivity().finish();
    }

    /**
     * 发起请求 注册
     *
     * @param account   ：sign account
     * @param psw       : sign password
     * @param phone     : sign phone_num
     * @param user_type : sign type (normal_user,export_user,manager_user...)
     * @return : VOLLEY Request
     */
    protected StringRequest signUpRequest(final String account, final String psw, final String phone, final String user_type) {
        String loginBaseUrl = BaseURLUtil.BASE_URL + "/fw?controller=com.xfsm.action.RegAction";
        String url = loginBaseUrl + "&loginname=" + account + "&pwd=" + psw + "&phone=" + phone + "&t=" + user_type;

        return new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getString("result").equals("Y")) {
                        //sign successfully
                        jumpToSignIn(account, psw);

                    } else {
                        //sign unsuccessfully
                        Toast.makeText(getActivity(), jsonObject.getString("result"), Toast.LENGTH_SHORT).show();
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


}
