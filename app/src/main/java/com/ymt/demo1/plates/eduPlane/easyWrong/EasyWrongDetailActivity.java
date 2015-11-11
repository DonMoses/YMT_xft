package com.ymt.demo1.plates.eduPlane.easyWrong;

import android.os.Bundle;
import android.webkit.WebView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ymt.demo1.R;
import com.ymt.demo1.baseClasses.BaseFloatActivity;
import com.ymt.demo1.utils.BaseURLUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by DonMoses on 2015/11/11
 */
public class EasyWrongDetailActivity extends BaseFloatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RequestQueue mQueue = Volley.newRequestQueue(this);
        int bankId = getIntent().getIntExtra("bankId", 0);
        setContentView(R.layout.activity_easy_wrong_detail);
        mQueue.add(getWrongTopicDetail(bankId));
    }

    private StringRequest getWrongTopicDetail(int bankId) {
        return new StringRequest(BaseURLUtil.getEasyWrongDetail(bankId), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONObject eduInfo = jsonObject.getJSONObject("datas").getJSONObject("listData")
                            .getJSONObject("eduInfo");
                    switch (eduInfo.optInt("topicType")) {
                        case 1:
                            ((TextView) findViewById(R.id.topic_type)).setText("单选题");
                            break;
                        case 2:
                            ((TextView) findViewById(R.id.topic_type)).setText("多选题");
                            break;
                        case 3:
                            ((TextView) findViewById(R.id.topic_type)).setText("判断题");
                            break;
                        case 5:
                            ((TextView) findViewById(R.id.topic_type)).setText("问答题");
                            break;
                        default:
                            break;
                    }
                    ((TextView) findViewById(R.id.topic_subject)).setText("知识点：" + eduInfo.optString("subjects"));
                    ((TextView) findViewById(R.id.topic_problem)).setText("题目：" + eduInfo.optString("problem"));
                    ((WebView) findViewById(R.id.topic_opts)).loadDataWithBaseURL(null, eduInfo.optString("options"), "text/html", "utf-8", null);
                    ((TextView) findViewById(R.id.topic_ans)).setText("正确答案：" + eduInfo.optString("answer"));
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
