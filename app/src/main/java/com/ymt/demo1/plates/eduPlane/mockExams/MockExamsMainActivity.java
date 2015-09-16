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
import com.ymt.demo1.main.sign.SignInUpActivity;
import com.ymt.demo1.utils.AppContext;
import com.ymt.demo1.baseClasses.BaseFloatActivity;
import com.ymt.demo1.utils.BaseURLUtil;
import com.ymt.demo1.main.search.SearchActivity;
import com.ymt.demo1.main.sign.SignInFragment;
import com.ymt.demo1.plates.eduPlane.ExamsOrderYearActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

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
        setContentView(R.layout.layout_past_mock_exam_main);
        initTitle();
        initView();

        mQueue.add(getExamInfo(1, 0, "001", ""));
        mQueue.add(getExamInfo(1, 0, "002", ""));
        mQueue.add(getExamInfo(1, 0, "003", ""));
        mQueue.add(getExamInfo(1, 0, "004", ""));
    }

    protected void initTitle() {
        MyTitle title = (MyTitle) findViewById(R.id.my_title);
        title.setTitleStyle(MyTitle.TitleStyle.RIGHT_ICON_L_R);
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
                Intent intent = new Intent(MockExamsMainActivity.this, ExamsOrderYearActivity.class);
                String[] array = MockExamsMainActivity.this.getResources().getStringArray(R.array.exam_years_array);
                int size = array.length;
                ArrayList<String> list = new ArrayList<>();
                list.addAll(Arrays.asList(array).subList(0, size));
                intent.putStringArrayListExtra("tests_years", list);
                intent.putExtra("type", "mock");
                startActivity(intent);
            }

            @Override
            public void onRightRClick() {
                startActivity(new Intent(MockExamsMainActivity.this, SearchActivity.class));
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
                        intent1.putExtra("level", 1);
                        startActivity(intent1);
                        break;
                    case R.id.view_all_level002:
                        Intent intent2 = new Intent(MockExamsMainActivity.this, MockExamsListActivity.class);
                        intent2.putExtra("level", 2);
                        startActivity(intent2);
                        break;
                    case R.id.view_all_level003:
                        Intent intent3 = new Intent(MockExamsMainActivity.this, MockExamsListActivity.class);
                        intent3.putExtra("level", 3);
                        startActivity(intent3);
                        break;
                    case R.id.view_all_level004:
                        Intent intent4 = new Intent(MockExamsMainActivity.this, MockExamsListActivity.class);
                        intent4.putExtra("level", 4);
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

    protected StringRequest getExamInfo(int start, int year, final String level, String searchWhat) {
        return new StringRequest(BaseURLUtil.getMockExams(start, level, year, searchWhat), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject object = new JSONObject(s);
                    if (object.getString("result").equals("Y")) {
                        JSONObject object1 = object.getJSONObject("datas");
                        JSONArray array = object1.getJSONArray("listData");
                        int length = array.length();
                        for (int i = 0; i < length; i++) {
                            JSONObject obj = array.getJSONObject(i);
                            final MockExamItem exam = new MockExamItem();
                            exam.setThe_id(obj.optString("id"));
                            exam.setStatus(obj.optString("status"));
                            exam.setTotal_item(obj.optString("total_item"));
                            exam.setSubject(obj.optString("subject"));
                            exam.setTotal_score(obj.optString("total_score"));
                            exam.setTop_score(obj.optString("top_score"));
                            exam.setCreate_time(obj.optString("create_time"));
                            exam.setFk_create_user_id(obj.optString("fk_create_user_id"));
                            exam.setExam_time(obj.optString("exam_time"));
                            exam.setType(obj.optString("type"));
                            exam.setExam_title(obj.optString("exam_title"));

                            final View itemView = inflater.inflate(R.layout.item_mock_exams_content, null);
                            TextView examName = (TextView) itemView.findViewById(R.id.content);
                            TextView totalItem = (TextView) itemView.findViewById(R.id.total_item);
                            TextView totalTime = (TextView) itemView.findViewById(R.id.total_time);
                            TextView totalScore = (TextView) itemView.findViewById(R.id.total_score);
                            examName.setText(exam.getExam_title());
                            totalItem.setText("总题:" + exam.getTotal_item() + "题");
                            totalTime.setText("考试时长:" + exam.getExam_time() + "分钟");
                            totalScore.setText("总分:" + exam.getTotal_score() + "分");

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
                                        intent.putExtra("exam_id", exam.getThe_id());
                                        startActivity(intent);
                                        //todo (试卷的其他信息)
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
                                case "001":
                                    level001Layout.addView(view1);
                                    level001Layout.addView(itemView);
                                    break;
                                case "002":
                                    level002Layout.addView(view1);
                                    level002Layout.addView(itemView);
                                    break;
                                case "003":
                                    level003Layout.addView(view1);
                                    level003Layout.addView(itemView);
                                    break;
                                case "004":
                                    level004Layout.addView(view1);
                                    level004Layout.addView(itemView);
                                    break;
                            }

                        }
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
        if(requestCode == 1024 && resultCode == RESULT_OK){
            target.callOnClick();
        }
    }
}