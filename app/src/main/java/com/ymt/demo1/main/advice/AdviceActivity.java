package com.ymt.demo1.main.advice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ymt.demo1.R;
import com.ymt.demo1.customViews.MyTitle;
import com.ymt.demo1.main.sign.SignInUpActivity;
import com.ymt.demo1.utils.AppContext;
import com.ymt.demo1.utils.BaseURLUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Moses on 2015
 */
public class AdviceActivity extends Activity {
    private RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mQueue = Volley.newRequestQueue(this);
        setContentView(R.layout.activity_advice);
        initView();
        initTitle();
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
        final Button subAdviceBtn = (Button) findViewById(R.id.do_sub_btn);
        final EditText editAdviceContent = (EditText) findViewById(R.id.advice_edit_content);
        final EditText editAdviceTitle = (EditText) findViewById(R.id.advice_edit_title);

        /*
        提交建议
         */
        subAdviceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String adviceTxt = editAdviceContent.getText().toString();
                String adviceTitle = editAdviceTitle.getText().toString();
                if (TextUtils.isEmpty(AppContext.now_session_id)) {
                    //先登录
                    Toast.makeText(AdviceActivity.this, "请先登录!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AdviceActivity.this, SignInUpActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    return;
                }
                if ((!TextUtils.isEmpty(adviceTxt)) && (!TextUtils.isEmpty(adviceTitle))) {
                    mQueue.add(doAdvice(adviceTitle, adviceTxt));
                } else {
                    Toast.makeText(AdviceActivity.this, "输入有误，请重新输入!", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    protected StringRequest doAdvice(String title, String content) {
        return new StringRequest(BaseURLUtil.doAdviceAction(title, content) + "&nickname=" + AppContext.now_user_name, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getJSONObject("datas").optBoolean("listData")) {
                        Toast.makeText(AdviceActivity.this, "您的建议已成功提交,谢谢！", Toast.LENGTH_SHORT).show();
                        finish();
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
    }

}
