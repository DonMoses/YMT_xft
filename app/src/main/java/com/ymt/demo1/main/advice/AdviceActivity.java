package com.ymt.demo1.main.advice;

import android.app.Activity;
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
                if ((!TextUtils.isEmpty(adviceTxt)) && (!TextUtils.isEmpty(adviceTitle))) {
                    mQueue.add(doAdvice(AppContext.now_session_id, adviceTitle, adviceTxt, ""));
                } else {
                    Toast.makeText(AdviceActivity.this, "输入有误，请重新输入!", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    protected StringRequest doAdvice(String sId, String title, String content, String phoneNum) {
        return new StringRequest(BaseURLUtil.doAdviceAction(sId, title, content, phoneNum), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getString("result").endsWith("Y")) {
                        Toast.makeText(AdviceActivity.this, "提交成功!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(AdviceActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(AdviceActivity.this, volleyError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
