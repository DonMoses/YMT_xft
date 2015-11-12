package com.ymt.demo1.plates.eduPlane.mockExams;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ymt.demo1.R;
import com.ymt.demo1.beams.edu.MockExamItem;
import com.ymt.demo1.customViews.MyTitle;
import com.ymt.demo1.main.search.FullSearchActivity;
import com.ymt.demo1.main.sign.SignInUpActivity;
import com.ymt.demo1.utils.AppContext;
import com.ymt.demo1.baseClasses.BaseFloatActivity;
import com.ymt.demo1.utils.BaseURLUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Dan on 2015/7/16
 */
public class MockExamsMainActivity extends BaseFloatActivity {
    private LinearLayout level001Layout;
    private LinearLayout level002Layout;
    private LinearLayout level003Layout;
    private LinearLayout level004Layout;
    private LayoutInflater inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RequestQueue mQueue = Volley.newRequestQueue(this);
        inflater = LayoutInflater.from(this);
        setContentView(R.layout.layout_mock_exam_main);
        initTitle();
        initView();

        mQueue.add(getExamInfo("1001"));
        mQueue.add(getExamInfo("1002"));
        mQueue.add(getExamInfo("1003"));
        mQueue.add(getExamInfo("1004"));
    }

    protected void initTitle() {
        MyTitle title = (MyTitle) findViewById(R.id.my_title);
        title.setTitleStyle(MyTitle.TitleStyle.RIGHT_ICON_L);
        title.updateCenterTitle("模拟试题");
        title.setOnLeftActionClickListener(new MyTitle.OnLeftActionClickListener() {
            @Override
            public void onClick() {
                finish();
            }
        });

        title.setOnRightActionClickListener(new MyTitle.OnRightActionClickListener() {
            @Override
            public void onRightLClick() {
                startActivity(new Intent(MockExamsMainActivity.this, FullSearchActivity.class));
            }

            @Override
            public void onRightRClick() {

            }
        });
    }

    protected void initView() {
        View all001View = findViewById(R.id.view_all_level001);
        View all002View = findViewById(R.id.view_all_level002);
        View all003View = findViewById(R.id.view_all_level003);
        View all004View = findViewById(R.id.view_all_level004);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.view_all_level001:
                        Intent intent1 = new Intent(MockExamsMainActivity.this, MockExamsListActivity.class);
                        intent1.putExtra("level", "1001");
                        startActivity(intent1);
                        break;
                    case R.id.view_all_level002:
                        Intent intent2 = new Intent(MockExamsMainActivity.this, MockExamsListActivity.class);
                        intent2.putExtra("level", "1002");
                        startActivity(intent2);
                        break;
                    case R.id.view_all_level003:
                        Intent intent3 = new Intent(MockExamsMainActivity.this, MockExamsListActivity.class);
                        intent3.putExtra("level", "1003");
                        startActivity(intent3);
                        break;
                    case R.id.view_all_level004:
                        Intent intent4 = new Intent(MockExamsMainActivity.this, MockExamsListActivity.class);
                        intent4.putExtra("level", "1004");
                        startActivity(intent4);
                        break;
                    default:
                        break;
                }
            }
        };

        all001View.setOnClickListener(onClickListener);
        all002View.setOnClickListener(onClickListener);
        all003View.setOnClickListener(onClickListener);
        all004View.setOnClickListener(onClickListener);

        level001Layout = (LinearLayout) findViewById(R.id.layout_level001);
        level003Layout = (LinearLayout) findViewById(R.id.layout_level003);
        level004Layout = (LinearLayout) findViewById(R.id.layout_level004);
        level002Layout = (LinearLayout) findViewById(R.id.layout_level002);
    }

    private View target;

    protected StringRequest getExamInfo(final String level) {
        return new StringRequest(BaseURLUtil.getMockExamList(level), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject object = new JSONObject(s);
                    if (object.getString("result").equals("Y")) {
                        JSONArray array = object.getJSONObject("datas").getJSONArray("listData");
                        int length = array.length();
                        for (int i = 0; i < length; i++) {
                            JSONObject obj = array.getJSONObject(i);
                            final MockExamItem exam = new MockExamItem();
                            exam.setBank_num(obj.optInt("bank_num"));
                            exam.setUids(obj.optInt("uids"));
                            exam.setScores(obj.optInt("scores"));
                            exam.setTimes(obj.optString("times"));
                            exam.setExaId(obj.optString("exaId"));
                            exam.setExaName(obj.optString("exaName"));

                            final View itemView = inflater.inflate(R.layout.item_mock_exams_content, null);
                            TextView examName = (TextView) itemView.findViewById(R.id.content);
                            TextView totalItem = (TextView) itemView.findViewById(R.id.total_item);
                            TextView totalScore = (TextView) itemView.findViewById(R.id.total_score);
                            examName.setText(exam.getExaName());
                            totalItem.setText(String.valueOf(exam.getBank_num()) + "题");
                            totalScore.setText("得分：" + String.valueOf(exam.getScores()) + "分");

                            LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT, 3);

                            View view1 = new View(MockExamsMainActivity.this);
                            view1.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                            view1.setLayoutParams(params1);

                            itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (!TextUtils.isEmpty(AppContext.now_session_id)) {
                                        Intent intent = new Intent(MockExamsMainActivity.this, ReadyActivity.class);
                                        intent.putExtra("item", exam);
                                        intent.putExtra("mockFrom", ReadyActivity.TYPE_NEW_MOCK);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(MockExamsMainActivity.this, "请登录！", Toast.LENGTH_SHORT).show();
                                        target = itemView;
                                        Intent intent = new Intent(MockExamsMainActivity.this, SignInUpActivity.class);
                                        intent.putExtra("is_back_to_main", false);
                                        startActivityForResult(intent, 1024);
                                    }

                                }
                            });

                            switch (level) {
                                case "1001":
                                    level001Layout.addView(view1);
                                    level001Layout.addView(itemView);
                                    break;
                                case "1002":
                                    level002Layout.addView(view1);
                                    level002Layout.addView(itemView);
                                    break;
                                case "1003":
                                    level003Layout.addView(view1);
                                    level003Layout.addView(itemView);
                                    break;
                                case "1004":
                                    level004Layout.addView(view1);
                                    level004Layout.addView(itemView);
                                    break;
                            }

                        }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1024 && resultCode == RESULT_OK) {
            target.callOnClick();
        }
    }
}