package com.ymt.demo1.plates.exportConsult;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import com.ymt.demo1.R;
import com.ymt.demo1.baseClasses.BaseActivity;
import com.ymt.demo1.beams.expert_consult.Expert;
import com.ymt.demo1.customViews.CircleImageView;
import com.ymt.demo1.customViews.MyTitle;
import com.ymt.demo1.utils.AppContext;
import com.ymt.demo1.utils.BaseURLUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Dan on 2015/5/13
 */
public class ExpertInfoActivity extends BaseActivity {
    private Expert expert;

    private boolean isFollowed;
    private TextView follow;
    private RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export_info);
        mQueue = Volley.newRequestQueue(this);
        isFollowed = false;
        initTitle();
        initView();
    }

    /**
     * 设置顶部title
     */
    protected void initTitle() {
        /*
         * 获取从上一activity 传过来的专家信息
         */
        expert = getIntent().getParcelableExtra("expert_info");
        /*
        todo 补充专家信息
         */

        //todo 最新问题

        final MyTitle title = (MyTitle) findViewById(R.id.my_title);
        title.setTitleStyle(MyTitle.TitleStyle.LEFT_ICON);
        title.updateCenterTitle(expert.getUser_name());          //根据专家名修改title
        title.invalidate();
        title.setOnLeftActionClickListener(new MyTitle.OnLeftActionClickListener() {
            @Override
            public void onClick() {
                finish();
            }
        });

    }

    protected void initView() {

        //立即咨询
        View consultLayout = findViewById(R.id.consult_action_layout);
        final ImageButton consultImg = (ImageButton) findViewById(R.id.consult_img);

        //专家头像
        CircleImageView exportHeader = (CircleImageView) findViewById(R.id.export_header);
        Picasso.with(this).load(expert.getHead_pic()).into(exportHeader);
        //专家名
        TextView exportName = (TextView) findViewById(R.id.export_name);
        exportName.setText(expert.getUser_name());
        //专家职位
        TextView exportPos = (TextView) findViewById(R.id.export_position_title);
        exportPos.setText(expert.getPosition_title());
        //关注按钮
        follow = (TextView) findViewById(R.id.follow_export);

        //todo 关注


        //主要经历
        WebView experienceView = (WebView) findViewById(R.id.experience);
        experienceView.loadDataWithBaseURL(null, expert.getExperience(), "text/html", "utf-8", null);
        //工作经验
        WebView majorWorkView = (WebView) findViewById(R.id.major_works);
        majorWorkView.loadDataWithBaseURL(null, expert.getMajor_works(), "text/html", "utf-8", null);
        //项目经验
        WebView workExpeView = (WebView) findViewById(R.id.work_experience);
        workExpeView.loadDataWithBaseURL(null, expert.getWork_experience(), "text/html", "utf-8", null);
        //基本信息
        ((TextView) findViewById(R.id.politic)).setText(expert.getPolitics());
        ((TextView) findViewById(R.id.addr)).setText(expert.getAddr());
        ((TextView) findViewById(R.id.position_title)).setText(expert.getPolitics());
        ((TextView) findViewById(R.id.pro_life)).setText(expert.getPro_life());
        ((TextView) findViewById(R.id.note)).setText(expert.getNote());
        ((TextView) findViewById(R.id.education)).setText(expert.getEducation());
        ((TextView) findViewById(R.id.degree)).setText(expert.getDegree());
        ((TextView) findViewById(R.id.qualification)).setText(expert.getQualification());
        ((TextView) findViewById(R.id.industry)).setText(expert.getIndustry());
        ((TextView) findViewById(R.id.reporting_methods)).setText(expert.getReporting_methods());
        ((TextView) findViewById(R.id.tel)).setText(expert.getTel());
        ((TextView) findViewById(R.id.work_addr)).setText(expert.getWork_addr());
        ((TextView) findViewById(R.id.work_zip_code)).setText(expert.getWork_zip_code());
        ((TextView) findViewById(R.id.school)).setText(expert.getSchool());
        ((TextView) findViewById(R.id.work_name)).setText(expert.getWork_name());

        /*
        点击事件监听
         */
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.follow_export:

                        if (!isFollowed) {
                            //todo 关注
                            mQueue.add(follow(AppContext.now_session_id, expert.getFk_user_id()));
                        } else {
                            //todo 取消关注
                            mQueue.add(unFollow(AppContext.now_session_id, expert.getFk_user_id(), AppContext.now_user_id));
                        }

                        isFollowed = !isFollowed;
                        break;
                    case R.id.consult_action_layout:
                        //todo 咨询会话界面
                        Intent intent = new Intent(ExpertInfoActivity.this, ExportConsultNowActivity.class);
                        intent.putExtra("expert_info", expert);
                        startActivity(intent);
                        finish();
                        break;
                    default:
                        break;

                }
            }
        };

        follow.setOnClickListener(onClickListener);
        consultLayout.setOnClickListener(onClickListener);

        View.OnTouchListener onTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (v.getId()) {
                    case R.id.consult_action_layout:
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            consultImg.setImageBitmap(
                                    BitmapFactory.decodeResource(getResources(), R.drawable.icon_consult_pressed));
                        } else if (event.getAction() == MotionEvent.ACTION_UP) {
                            consultImg.setImageBitmap(
                                    BitmapFactory.decodeResource(getResources(), R.drawable.icon_consult_normal));
                        }
                        break;
                    default:
                        break;
                }
                return false;
            }
        };
        consultLayout.setOnTouchListener(onTouchListener);
    }


    /**
     * 取消关注
     */
    private StringRequest unFollow(String sId, String expertId, String userId) {
        return new StringRequest(BaseURLUtil.unfollowedExpert(sId, expertId, userId), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getString("result").equals("Y")) {
                        follow.setText("+关注");
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

    /**
     * 关注
     */
    private StringRequest follow(String sId, String expertId) {
        return new StringRequest(BaseURLUtil.followExpert(sId, expertId), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getString("result").equals("Y")) {
                        follow.setText("已关注");
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
