package com.ymt.demo1.plates.eduPlane.mockExams;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ymt.demo1.R;
import com.ymt.demo1.beams.edu.MockExamItem;
import com.ymt.demo1.customViews.MyTitle;
import com.ymt.demo1.baseClasses.BaseFloatActivity;
import com.ymt.demo1.utils.AppContext;
import com.ymt.demo1.utils.BaseURLUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Dan on 2015/7/17
 */
public class ReadyActivity extends BaseFloatActivity {
    private MockExamItem examItem;
    private String userMockExamId;
    public static final String TYPE_NEW_MOCK = "newMock";
    public static final String TYPE_MY_MOCK = "myMock";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RequestQueue mQueue = Volley.newRequestQueue(this);

        setContentView(R.layout.activity_ready);
        initTitle();
        initView();
        String mockType = getIntent().getStringExtra("mockFrom");
        switch (mockType) {
            case TYPE_NEW_MOCK:
                examItem = getIntent().getParcelableExtra("item");
                mQueue.add(getOnTimeExamInfo(examItem.getExaId(), AppContext.now_session_id, examItem.getExaName()));
                break;
            case TYPE_MY_MOCK:
                userMockExamId = getIntent().getStringExtra("userMockId");
                break;
            default:
                break;
        }
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

    private View ansView;

    protected void initView() {
        ansView = findViewById(R.id.go_exam);
        ansView.setClickable(false);
        ansView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(userMockExamId)) {
                    Intent intent = new Intent(ReadyActivity.this, AnswerActivity.class);
                    intent.putExtra("item", examItem);
                    intent.putExtra("userMockId", userMockExamId);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);

                    finish();
                } else {
                    Toast.makeText(ReadyActivity.this, "当前网络加载较慢,请稍后重试!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private StringRequest getOnTimeExamInfo(String exaId, String sId, String exaName) {

        return new StringRequest(BaseURLUtil.getMockTTTInfo(exaId, sId, exaName), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.optString("result").equals("Y")) {
                        userMockExamId = jsonObject.getJSONObject("datas").getJSONObject("listData")
                                .optString("id");
                        ansView.setClickable(true);
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
