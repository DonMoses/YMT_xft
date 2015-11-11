package com.ymt.demo1.plates.exportConsult;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.ymt.demo1.beams.expert_consult.FollowedExpert;
import com.ymt.demo1.customViews.CircleImageView;
import com.ymt.demo1.customViews.MyTitle;
import com.ymt.demo1.main.sign.SignInUpActivity;
import com.ymt.demo1.utils.AppContext;
import com.ymt.demo1.utils.BaseURLUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

/**
 * Created by Dan on 2015/5/13
 */
public class ExpertInfoActivity extends BaseActivity {
    private Expert expert;

    private int expertId;
    private TextView follow;
    private RequestQueue mQueue;
    private MyTitle title;
    private boolean isFollowed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export_info);
        mQueue = Volley.newRequestQueue(this);
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
        expertId = getIntent().getIntExtra("id", 0);
        mQueue.add(getExpertInfo(expertId));
        mQueue.add(getFollowedExpert(AppContext.now_session_id));

        title = (MyTitle) findViewById(R.id.my_title);
        title.setTitleStyle(MyTitle.TitleStyle.LEFT_ICON);

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

        //关注按钮
        follow = (TextView) findViewById(R.id.follow_export);
        setFollowBtnDefault();

        /*
        点击事件监听
         */
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.follow_export:

                        if (TextUtils.isEmpty(AppContext.now_session_id)) {
                            startActivity(new Intent(ExpertInfoActivity.this, SignInUpActivity.class));
                            return;
                        }
                        if (expert == null) {
                            return;
                        }
                        if (isFollowed) {
                            //todo 取消关注
                            mQueue.add(unFollow(AppContext.now_session_id, expert.getFkUserId()));
                        } else {
                            //todo 关注
                            mQueue.add(follow(AppContext.now_session_id, expert.getFkUserId()));
                        }
                        break;
                    case R.id.consult_action_layout:
                        //todo 咨询会话界面
                        if (!(TextUtils.isEmpty(AppContext.now_session_id))) {
                            Intent intent = new Intent(ExpertInfoActivity.this, ConsultNowActivity.class);
                            intent.putExtra("expertId", expertId);
                            startActivity(intent);
                            finish();
                        } else {
                            Intent intent = new Intent(ExpertInfoActivity.this, SignInUpActivity.class);
                            intent.putExtra("isFromConsult", true);
                            startActivity(intent);
                        }
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

    private void setInfoToView() {
        title.updateCenterTitle(expert.getUsername());          //根据专家名修改title
        title.invalidate();

        CircleImageView header = (CircleImageView) findViewById(R.id.export_header);
        Picasso.with(this).load(expert.getHeadPic()).into(header);
        ((TextView) findViewById(R.id.export_name)).setText(expert.getUsername());
        ((TextView) findViewById(R.id.export_position_title)).setText(expert.getPositionTitle());
        //主要经历
        WebView experienceView = (WebView) findViewById(R.id.experience);
        experienceView.loadDataWithBaseURL(null, expert.getExperience(), "text/html", "utf-8", null);
        //工作经验
        WebView majorWorkView = (WebView) findViewById(R.id.major_works);
        majorWorkView.loadDataWithBaseURL(null, expert.getMajorWorks(), "text/html", "utf-8", null);
        //项目经验
        WebView workExpeView = (WebView) findViewById(R.id.work_experience);
        workExpeView.loadDataWithBaseURL(null, expert.getWorkExperience(), "text/html", "utf-8", null);
        //基本信息
        ((TextView) findViewById(R.id.politic)).setText(expert.getPolitics());
        ((TextView) findViewById(R.id.addr)).setText(expert.getAddr());
        ((TextView) findViewById(R.id.position_title)).setText(expert.getPositionTitle());
        ((TextView) findViewById(R.id.pro_life)).setText(expert.getProLife());
        ((TextView) findViewById(R.id.education)).setText(expert.getEducation());
        ((TextView) findViewById(R.id.degree)).setText(expert.getDegree());
        ((TextView) findViewById(R.id.qualification)).setText(expert.getQualification());
        int industryType = expert.getIndustry();
        String industry = null;
        switch (industryType) {
            case 1:
                industry = "消防工程";
                break;
            case 2:
                industry = "电器工程及其自动化";
                break;
            case 3:
                industry = "电子信息工程";
                break;
            case 4:
                industry = "通信工程";
                break;
            case 5:
                industry = "建筑学";
                break;
            case 6:
                industry = "建筑环境与设备工程";
                break;
            case 7:
                industry = "给水排水工程";
                break;
            case 8:
                industry = "土木工程";
                break;
            case 9:
                industry = "安全工程";
                break;
            case 10:
                industry = "计算机科学与技术";
                break;
            case 11:
                industry = "工业工程";
                break;
            case 12:
                industry = "城市规划";
                break;
            case 13:
                industry = "工程管理";
                break;
            case 14:
                industry = "化学工程与工艺";
                break;
            case 15:
                industry = "管理科学";
                break;
            case 16:
                industry = "其他";
                break;
            default:
                break;
        }
        ((TextView) findViewById(R.id.industry)).setText(industry);
        ((TextView) findViewById(R.id.reporting_methods)).setText(expert.getReportingMethods());
        ((TextView) findViewById(R.id.tel)).setText(expert.getTel());
        ((TextView) findViewById(R.id.school)).setText(expert.getSchool());
        ((TextView) findViewById(R.id.work_name)).setText(expert.getWorkName());
        ((TextView) findViewById(R.id.work_addr)).setText(expert.getWorkAddr());
    }

    /**
     * 取消关注
     */
    private StringRequest unFollow(final String sId, final int expertId) {
        return new StringRequest(BaseURLUtil.unfollowExpert(sId, expertId), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getString("result").equals("Y")
                            && jsonObject.getJSONObject("datas").optInt("listData") > 0) {
                        setFollowBtnDefault();
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

    /**
     * 关注
     */
    private StringRequest follow(final String sId, final int expertId) {
        return new StringRequest(BaseURLUtil.followExpert(sId, expertId), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getString("result").equals("Y")
                            && jsonObject.getJSONObject("datas").optInt("listData") > 0) {
                        setFollowBtnYes();
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

    protected StringRequest getExpertInfo(int expertId) {
        return new StringRequest(BaseURLUtil.getExpertInfo(expertId), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getString("result").equals("Y")) {
                        JSONObject obj = jsonObject.getJSONObject("datas").getJSONObject("listData");
                        expert = new Expert();
                        expert.setReportingMethods(obj.optString("reportingMethods"));
                        expert.setWorkAddr(obj.optString("workAddr"));
                        expert.setMajorWorks(obj.optString("majorWorks"));
                        expert.setCount(obj.optInt("count"));
                        expert.setTel(obj.optString("tel"));
                        expert.setGoods(obj.optString("goods"));
                        expert.setAddr(obj.optString("addr"));
                        expert.setEducation(obj.optString("education"));
                        expert.setUserType(obj.optString("userType"));
                        expert.setPolitics(obj.optString("politics"));
                        expert.setQualification(obj.optString("qualification"));
                        expert.setUsername(obj.optString("username"));
                        expert.setLevel(obj.optString("level"));
                        expert.setFkUserId(obj.optInt("fkUserId"));
                        expert.setCodeValue(obj.optString("codeValue"));
                        expert.setWorkName(obj.optString("workName"));
                        expert.setCapacity(obj.optString("capacity"));
                        expert.setExperience(obj.optString("experience"));
                        expert.setIndustry(obj.optInt("industry"));
                        expert.setProLife(obj.optString("proLife"));
                        expert.setHonoraryTitle(obj.optString("honoraryTitle"));
                        expert.setWaitCount(obj.optInt("waitCount"));
                        expert.setHeadPic(obj.optString("headPic"));
                        expert.setCertificateNumber(obj.optString("certificateNumber"));
                        expert.setDegree(obj.optString("degree"));
                        expert.setSchool(obj.optString("school"));
                        expert.setWorkExperience(obj.optString("workExperience"));
                        expert.setPositionTitle(obj.optString("positionTitle"));

                        setInfoToView();

                        int size = DataSupport.where("fkUserId = ?", String.valueOf(expert.getFkUserId())).find(Expert.class).size();
                        if (size == 0) {
                            expert.save();
                        } else {
                            expert.updateAll("fkUserId = ?", String.valueOf(expert.getFkUserId()));
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

    public void setFollowBtnDefault() {
        isFollowed = false;
        follow.setText("+关注");
    }

    public void setFollowBtnYes() {
        isFollowed = true;
        follow.setText("已关注");
    }

    /**
     * 获取关注列表
     */
    public StringRequest getFollowedExpert(String sId) {
        return new StringRequest(BaseURLUtil.getFollowedExpertList(sId), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getString("result").equals("Y")) {
                        JSONArray array = jsonObject.getJSONObject("datas").getJSONArray("listData");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            FollowedExpert followedExpert = new FollowedExpert();
                            followedExpert.setFkExpertId(object.optInt("fkExpertId"));
                            followedExpert.setHeadPic(object.optString("headPic"));
                            followedExpert.setMajorWorks(object.optString("majorWorks"));
                            followedExpert.setUserName(object.optString("userName"));
                            if (expertId == followedExpert.getFkExpertId()) {
                                setFollowBtnYes();
                                return;
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
