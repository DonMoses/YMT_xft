package com.ymt.demo1.plates.eduPlane.pastExams;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ymt.demo1.R;
import com.ymt.demo1.beams.edu.PastExamItem;
import com.ymt.demo1.customViews.MyTitle;
import com.ymt.demo1.main.BaseFloatActivity;
import com.ymt.demo1.main.BaseURLUtil;
import com.ymt.demo1.main.search.SearchActivity;
import com.ymt.demo1.plates.eduPlane.ExamsOrderYearActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Dan on 2015/7/16
 */
public class PastExamsMainActivity extends BaseFloatActivity {
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

        mQueue.add(getExamInfo(1, "level001", ""));
        mQueue.add(getExamInfo(1, "level002", ""));
        mQueue.add(getExamInfo(1, "level003", ""));
        mQueue.add(getExamInfo(1, "level004", ""));
    }

    protected void initTitle() {
        MyTitle title = (MyTitle) findViewById(R.id.my_title);
        title.setTitleStyle(MyTitle.TitleStyle.RIGHT_ICON_L_R);
        title.updateCenterTitle("历年真题");
        title.setOnLeftActionClickListener(new MyTitle.OnLeftActionClickListener() {
            @Override
            public void onClick() {
                finish();
            }
        });

        title.setOnRightActionClickListener(new MyTitle.OnRightActionClickListener() {
            @Override
            public void onRightLClick() {
                Intent intent = new Intent(PastExamsMainActivity.this, ExamsOrderYearActivity.class);
                String[] array = PastExamsMainActivity.this.getResources().getStringArray(R.array.exam_years_array);
                int size = array.length;
                ArrayList<String> list = new ArrayList<>();
                list.addAll(Arrays.asList(array).subList(0, size));
                intent.putStringArrayListExtra("tests_years", list);
                intent.putExtra("type", "past");
                startActivity(intent);
            }

            @Override
            public void onRightRClick() {
                startActivity(new Intent(PastExamsMainActivity.this, SearchActivity.class));
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
                        Intent intent1 = new Intent(PastExamsMainActivity.this, PastExamsListActivity.class);
                        intent1.putExtra("level", 1);
                        startActivity(intent1);
                        break;
                    case R.id.view_all_level002:
                        Intent intent2 = new Intent(PastExamsMainActivity.this, PastExamsListActivity.class);
                        intent2.putExtra("level", 2);
                        startActivity(intent2);
                        break;
                    case R.id.view_all_level003:
                        Intent intent3 = new Intent(PastExamsMainActivity.this, PastExamsListActivity.class);
                        intent3.putExtra("level", 3);
                        startActivity(intent3);
                        break;
                    case R.id.view_all_level004:
                        Intent intent4 = new Intent(PastExamsMainActivity.this, PastExamsListActivity.class);
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


    protected StringRequest getExamInfo(int start, final String level, String searchWhat) {
        return new StringRequest(BaseURLUtil.getPastExamsByLevel(start, level, searchWhat), new Response.Listener<String>() {
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
                            final PastExamItem exam = new PastExamItem();
                            exam.setExam_year(obj.optString("exam_year"));
                            exam.setThe_id(obj.optString("id"));
                            exam.setFiles(obj.optString("files"));
                            exam.setArticle_title(obj.optString("article_title"));
                            exam.setLevel(obj.optString("level"));
                            exam.setStatus(obj.optString("status"));
                            exam.setSubject(obj.optString("subject"));
                            exam.setMeta_keys(obj.optString("meta_keys"));
                            exam.setCreate_time(obj.optString("create_time"));
                            exam.setHitnum(obj.optString("hitnum"));
                            exam.setFk_create_user_id(obj.optString("fk_create_user_id"));
                            exam.setPdf_id(obj.optString("pdf_id"));
                            exam.setDowncount(obj.optString("downcount"));

                            View itemView = inflater.inflate(R.layout.item_past_exams_content_s, null);
                            TextView examName = (TextView) itemView.findViewById(R.id.content);
                            TextView downloadCount = (TextView) itemView.findViewById(R.id.download_count);
                            examName.setText(exam.getArticle_title());
                            downloadCount.setVisibility(View.GONE);

                            LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT, 3);

                            View view1 = new View(PastExamsMainActivity.this);
                            view1.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                            view1.setLayoutParams(params1);

                            itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(PastExamsMainActivity.this, PastExamDetailActivity.class);
                                    intent.putExtra("exam", exam);
                                    startActivity(intent);
                                }
                            });

                            if (level.equals("level001")) {
                                level001Layout.addView(view1);
                                level001Layout.addView(itemView);

                            } else if (level.equals("level002")) {
                                level002Layout.addView(view1);
                                level002Layout.addView(itemView);
                            } else if (level.equals("level003")) {
                                level003Layout.addView(view1);
                                level003Layout.addView(itemView);
                            } else if (level.equals("level004")) {
                                level004Layout.addView(view1);
                                level004Layout.addView(itemView);
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
}



