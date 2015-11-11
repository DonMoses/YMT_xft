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
import com.ymt.demo1.baseClasses.BaseFloatActivity;
import com.ymt.demo1.utils.AppContext;
import com.ymt.demo1.utils.BaseURLUtil;
import com.ymt.demo1.plates.eduPlane.ExamsOrderYearActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

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
        setContentView(R.layout.layout_past_exam_main);
        initTitle();
        initView();

        mQueue.add(getExamInfo(1, 5, "1001", null, null));
        mQueue.add(getExamInfo(1, 5, "1002", null, null));
        mQueue.add(getExamInfo(1, 5, "1003", null, null));
        mQueue.add(getExamInfo(1, 5, "1004", null, null));
    }

    protected void initTitle() {
        MyTitle title = (MyTitle) findViewById(R.id.my_title);
        title.setTitleStyle(MyTitle.TitleStyle.RIGHT_ICON_L);
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
                startActivityForResult(intent, 1024);
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
                        Intent intent1 = new Intent(PastExamsMainActivity.this, PastExamsListActivity.class);
                        intent1.putExtra("level", "1001");
                        startActivity(intent1);
                        break;
                    case R.id.view_all_level002:
                        Intent intent2 = new Intent(PastExamsMainActivity.this, PastExamsListActivity.class);
                        intent2.putExtra("level", "1002");
                        startActivity(intent2);
                        break;
                    case R.id.view_all_level003:
                        Intent intent3 = new Intent(PastExamsMainActivity.this, PastExamsListActivity.class);
                        intent3.putExtra("level", "1003");
                        startActivity(intent3);
                        break;
                    case R.id.view_all_level004:
                        Intent intent4 = new Intent(PastExamsMainActivity.this, PastExamsListActivity.class);
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

    protected StringRequest getExamInfo(int index, int pageNum, final String level, String opTime, String searchWhat) {
        return new StringRequest(BaseURLUtil.getPastExamListByLevel(index, pageNum, level, opTime, searchWhat), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject object = new JSONObject(s);
                    if (object.getString("result").equals("Y")) {
                        JSONArray array = object.getJSONObject("datas").getJSONObject("listData")
                                .getJSONArray("edu");
                        int length = array.length();
                        for (int i = 0; i < length; i++) {
                            JSONObject obj = array.getJSONObject(i);
                            final PastExamItem exam = new PastExamItem();
                            exam.setTitle(obj.optString("title"));
                            exam.setLevel(obj.optString("level"));
                            exam.setSubjects(obj.optString("subjects"));
                            exam.setLevelId(obj.optInt("levelId"));
                            exam.setHistoryId(obj.optString("historyId"));
                            exam.setViews(obj.optInt("views"));
                            exam.setDATE(obj.optString("DATE"));
                            exam.setYuer(obj.optString("yuer"));
                            exam.setDescs(obj.optString("descs"));

                            View itemView = inflater.inflate(R.layout.item_past_exams_content_s, null);
                            TextView examName = (TextView) itemView.findViewById(R.id.content);
                            TextView downloadCount = (TextView) itemView.findViewById(R.id.download_count);
                            examName.setText(exam.getTitle());
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

                            if (level.equals("1001")) {
                                level001Layout.addView(view1);
                                level001Layout.addView(itemView);
                            } else if (level.equals("1002")) {
                                level002Layout.addView(view1);
                                level002Layout.addView(itemView);
                            } else if (level.equals("1003")) {
                                level003Layout.addView(view1);
                                level003Layout.addView(itemView);
                            } else if (level.equals("1004")) {
                                level004Layout.addView(view1);
                                level004Layout.addView(itemView);
                            }

                            if (DataSupport.where("historyId = ?", exam.getHistoryId()).find(PastExamItem.class)
                                    .size() == 0) {
                                exam.save();
                            } else {
                                exam.updateAll("historyId = ?", exam.getHistoryId());
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

}



