package com.ymt.demo1.plates.eduPlane.mockExams;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ymt.demo1.R;
import com.ymt.demo1.baseClasses.BaseActivity;
import com.ymt.demo1.beams.edu.exam.Exam;
import com.ymt.demo1.beams.edu.exam.Opt;
import com.ymt.demo1.beams.edu.exam.Topic;
import com.ymt.demo1.customViews.MyCheckView;
import com.ymt.demo1.customViews.MyTitle;
import com.ymt.demo1.main.AppContext;
import com.ymt.demo1.main.BaseURLUtil;
import com.ymt.demo1.main.PopActionListener;
import com.ymt.demo1.main.PopActionUtil;
import com.ymt.demo1.main.sign.SignInActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Dan on 2015/7/17
 */
public class DoPaperActivity extends BaseActivity {
    private String exam_id;
    private RequestQueue mQueue;
    private int recIndex;
    private List<Topic> singleList;
    private List<Topic> mutilList;
    private List<Topic> fillList;
    private List<Topic> judgmentList;
    private List<Topic> subjectiveList;
    private LinkedList<Topic> linkedList;
    private Exam exam;
    private MyHandler myHandler = new MyHandler(this);
    private MyTitle title;
    private boolean isShownOpts = false;
    private LayoutInflater inflater;

    //题目
    private TextView topicName;
    //（公选）答案
    private LinearLayout optsLayout;
    //（正确）答案
    private LinearLayout trueOptsLayout;
    //完成比例（如2/95，10/120）
    private TextView indexView;
    //已经提交的题目
    private ArrayList<String> doneList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflater = LayoutInflater.from(this);
        exam_id = getIntent().getStringExtra("exam_id");
        recIndex = 0;
        mQueue = Volley.newRequestQueue(this);
        mQueue.add(getPaper(exam_id));
        singleList = new ArrayList<>();
        mutilList = new ArrayList<>();
        fillList = new ArrayList<>();
        judgmentList = new ArrayList<>();
        subjectiveList = new ArrayList<>();
        setContentView(R.layout.activity_do_paper);
        initTitle();
        initView();

//        Log.e("TAG", ".........onCreate.........");

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {
                    isShownOpts = false;
                    optsLayout.removeAllViews();
                    trueOptsLayout.removeAllViews();
                    recIndex = data.getIntExtra("index", 1) - 1;
                    updateViewInfo();
                }
                break;
            default:
                break;
        }
    }

    protected void initTitle() {
        title = (MyTitle) findViewById(R.id.my_title);
        title.setTitleStyle(MyTitle.TitleStyle.RIGHT_ICON_L);
        title.updateLeftLIcon2Txt("收藏本题");
        title.setOnLeftActionClickListener(new MyTitle.OnLeftActionClickListener() {
            @Override
            public void onClick() {
                finish();
            }
        });

        title.setOnRightActionClickListener(new MyTitle.OnRightActionClickListener() {
            @Override
            public void onRightLClick() {
                Toast.makeText(DoPaperActivity.this, "收藏...", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRightRClick() {

            }
        });

    }

    protected void initView() {
        topicName = (TextView) findViewById(R.id.topic_title);
        optsLayout = (LinearLayout) findViewById(R.id.opts_parent);
        TextView viewOpts = (TextView) findViewById(R.id.view_true_opts);
        trueOptsLayout = (LinearLayout) findViewById(R.id.true_opts_parent);
        LinearLayout viewTopicsView = (LinearLayout) findViewById(R.id.view_all_topics);
        indexView = (TextView) findViewById(R.id.index_in_topics);
        LinearLayout subTopicLayoutView = (LinearLayout) findViewById(R.id.sub_topic);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (exam == null) {
                    return;
                }
                switch (v.getId()) {
                    case R.id.view_true_opts:
                        //显示参考答案

                        if (isShownOpts) {
                            trueOptsLayout.removeAllViews();
                        } else {
                            Topic topic = linkedList.get(recIndex);
                            List<Opt> opts = topic.getOps();
                            int size = opts.size();
                            for (int i = 0; i < size; i++) {
                                Opt opt = opts.get(i);
                                if (opt.is_corrent()) {
                                    View optView = inflater.inflate(R.layout.item_exam_opt, null);

                                    TextView optItem = (TextView) optView.findViewById(R.id.opt_content);
                                    MyCheckView optCheck = (MyCheckView) optView.findViewById(R.id.switch_opt);
                                    optCheck.setVisibility(View.GONE);
                                    optItem.setTextColor(Color.DKGRAY);
                                    optItem.setText(opts.get(i).getContent());
                                    trueOptsLayout.addView(optView);
                                }
                            }

                        }

                        isShownOpts = !isShownOpts;
                        break;
                    case R.id.view_all_topics:
                        //显示题目表
                        Intent intent = new Intent(DoPaperActivity.this, TopicIndexActivity.class);
                        ArrayList<String> allList = new ArrayList<>();
                        int size = linkedList.size();
                        for (int i = 0; i < size; i++) {
                            allList.add(String.valueOf(i + 1));
                        }

                        intent.putStringArrayListExtra("doneList", doneList);
                        intent.putStringArrayListExtra("allList", allList);
                        startActivityForResult(intent, 0);
                        break;
                    case R.id.sub_topic:
                        //提交题目。【如果是单选、多选、判断，则不能为空】
                        switch (linkedList.get(recIndex).getType()) {
                            case "c1":              //单选
                            case "c2":              //多选
                            case "c3":              //判断
                                int childCount = optsLayout.getChildCount();
                                int checkedCount = 0;
                                for (int i = 0; i < childCount; i++) {
                                    MyCheckView checkView = (MyCheckView) optsLayout.getChildAt(i).findViewById(R.id.switch_opt);
                                    if (checkView.isChecked()) {
                                        checkedCount++;
                                    }
                                }
                                if (checkedCount == 0) {
                                    Toast.makeText(DoPaperActivity.this, "请先完成当前题目!", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                break;
                            default:
                                break;
                        }

                        if (!doneList.contains(String.valueOf(recIndex + 1))) {
                            doneList.add(String.valueOf(recIndex + 1));
                        }

                        if (doneList.size() == linkedList.size()) {
                            recIndex = linkedList.size() - 1;
                            isShownOpts = false;
                            updateViewInfo();

                            //设置背景颜色变暗
                            final WindowManager.LayoutParams lp =
                                    DoPaperActivity.this.getWindow().getAttributes();
                            lp.alpha = 0.3f;
                            DoPaperActivity.this.getWindow().setAttributes(lp);

                            //todo 弹出下载提示框
                            PopActionUtil popActionUtil = PopActionUtil.getInstance(DoPaperActivity.this);
                            PopupWindow popupWindow = popActionUtil.getSubPaperPopActionMenu(exam);
                            popupWindow.showAtLocation(topicName.getRootView(), Gravity.CENTER, 0, 0);

                            popActionUtil.setActionListener(new PopActionListener() {
                                @Override
                                public void onAction(String action) {
                                    switch (action) {
                                        case "确定":
                                            mQueue.add(subPaper());
                                            //todo 提交
                                            break;
                                        case "取消":
                                            Toast.makeText(DoPaperActivity.this, "已取消！", Toast.LENGTH_SHORT).show();
                                            break;
                                        default:
                                            break;
                                    }
                                    lp.alpha = 1f;
                                    DoPaperActivity.this.getWindow().setAttributes(lp);
                                }

                                @Override
                                public void onDismiss() {

                                }
                            });

                        } else if (recIndex < linkedList.size() - 1) {
                            //提交题目
                            submitTopic();
                            trueOptsLayout.removeAllViews();
                            optsLayout.removeAllViews();
                            recIndex++;
                            isShownOpts = false;
                            updateViewInfo();
                        } else {
                            //显示题目表
                            Intent intent1 = new Intent(DoPaperActivity.this, TopicIndexActivity.class);
                            ArrayList<String> allList1 = new ArrayList<>();
                            int size1 = linkedList.size();
                            for (int i = 0; i < size1; i++) {
                                allList1.add(String.valueOf(i + 1));
                            }

                            intent1.putStringArrayListExtra("doneList", doneList);
                            intent1.putStringArrayListExtra("allList", allList1);
                            startActivityForResult(intent1, 0);
                        }
                        break;
                    default:
                        break;

                }
            }
        };

        viewOpts.setOnClickListener(onClickListener);
        viewTopicsView.setOnClickListener(onClickListener);
        subTopicLayoutView.setOnClickListener(onClickListener);
    }

    protected StringRequest getPaper(String exam_id) {
        if (TextUtils.isEmpty(AppContext.now_session_id)) {
            startActivity(new Intent(this, SignInActivity.class));
            return null;
        } else {
            return new StringRequest(BaseURLUtil.getPaperContent(exam_id), new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        if (jsonObject.getString("result").equals("Y")) {
                            //请求成功，返回试卷内容
                            JSONObject obj = jsonObject.getJSONObject("exam_info");
                            //试卷
                            exam = new Exam();

                            exam.setJudgmentCent(obj.optInt("judgmentCent"));
                            exam.setDoneUsers(obj.optInt("doneUsers"));
                            exam.setCurQueIndex(obj.optInt("curQueIndex"));
                            exam.setStatus(obj.optString("status"));
                            exam.setTotal_item(obj.optInt("total_item"));
                            exam.setFillCent(obj.optInt("fillCent"));
                            exam.setSubject(obj.optString("subject"));
                            exam.setTotal_score(obj.optInt("total_score"));
                            exam.setTop_score(obj.optInt("top_score"));
                            exam.setSingleCent(obj.optInt("singleCent"));
                            exam.setPassScore(obj.optInt("passScore"));
                            exam.setRemain_time(obj.optInt("remain_time"));
                            exam.setExam_title(obj.optString("exam_title"));
                            exam.setType(obj.optString("type"));

                            JSONArray singles = obj.getJSONArray("singles");
                            singleList = getCatoTopics(singles);
                            exam.setSingles(singleList);

                            JSONArray mutils = obj.getJSONArray("mutils");
                            mutilList = getCatoTopics(mutils);
                            exam.setMutils(mutilList);

                            exam.setThe_id(obj.optString("id"));
                            exam.setMutilCent(obj.optInt("mutilCent"));
                            exam.setEnd_time(obj.optString("end_time"));

                            JSONArray fills = obj.getJSONArray("fills");
                            fillList = getCatoTopics(fills);
                            exam.setFills(fillList);

                            exam.setSubjectiveCent(obj.optInt("subjectiveCent"));
                            exam.setCreate_time(obj.optString("create_time"));
                            exam.setStart_time(obj.optString("start_time"));
                            exam.setFk_create_user_id(obj.optString("fk_create_user_id"));
                            exam.setCor_score(obj.optInt("cor_score"));
                            exam.setFk_exam_id(obj.optString("fk_exam_id"));
                            exam.setExam_time(obj.optInt("exam_time"));

                            JSONArray judgments = obj.getJSONArray("judgments");
                            judgmentList = getCatoTopics(judgments);
                            exam.setJudgments(judgmentList);

                            JSONArray subjectives = obj.getJSONArray("subjectives");
                            subjectiveList = getCatoTopics(subjectives);
                            exam.setSubjectives(subjectiveList);

                            //todo 解析完毕以后，通过handler消息机制， 刷新界面（在这之前不操作与exam相关的主线程）

                            myHandler.sendEmptyMessage(0);
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

    protected List<Topic> getCatoTopics(JSONArray array) throws JSONException {
        int length1 = array.length();
        List<Topic> topicList = new ArrayList<>();
        for (int i = 0; i < length1; i++) {
            JSONObject object = array.getJSONObject(i);
            //题目
            Topic topic = new Topic();

            topic.setIs_fallibility(object.optString("is_fallibility"));
            topic.setDiff(object.optString("diff"));
            topic.setStatus(object.optString("status"));
            topic.setSubject(object.optString("subject"));
            topic.setAnswer(object.optString("answer"));
            topic.setFk_que_id(object.optString("fk_que_id"));
            topic.setUser_answer(object.optString("user_answer"));
            topic.setType(object.optString("type"));
            topic.setThe_id(object.optString("id"));
            topic.setContent(object.optString("content"));
            topic.setLevel(object.optString("level"));
            topic.setHot_ratio(object.optString("hot_ratio"));
            topic.setQue_score(object.optInt("que_score"));
            topic.setIs_hot(object.optString("is_hot"));
            topic.setPoints(object.optString("points"));
            topic.setErr_ratio(object.optString("err_ratio"));
            topic.setFk_exam_id(object.optString("fk_exam_id"));

            JSONArray opts = object.getJSONArray("ops");
            int length = opts.length();
            List<Opt> optList = new ArrayList<>();
            for (int j = 0; j < length; j++) {
                JSONObject optObj = opts.optJSONObject(j);
                //答案
                Opt opt = new Opt();
                opt.setContent(optObj.optString("content"));
                opt.setThe_id(optObj.optString("id"));
                opt.setSort(optObj.optInt("sort"));
                opt.setAnswer_analysis(optObj.optString("answer_analysis"));
                opt.setIs_correct(optObj.optBoolean("is_correct"));
                opt.setFk_que_id(optObj.optString("fk_que_id"));
                optList.add(opt);
            }

            topic.setOps(optList);

            topicList.add(topic);

        }

        return topicList;
    }

    /**
     * 显示题目信息
     */
    protected void setLinkedTopicData() {
        findViewById(R.id.pro_view).setVisibility(View.GONE);

        linkedList = new LinkedList<>();
        linkedList.addAll(exam.getSingles());
        linkedList.addAll(exam.getMutils());
        linkedList.addAll(exam.getJudgments());
        linkedList.addAll(exam.getFills());
        linkedList.addAll(exam.getSubjectives());

        updateViewInfo();
    }

    /**
     * 刷新题目、答案等信息
     */
    protected void updateViewInfo() {

        //显示参考答案
        final Topic topic = linkedList.get(recIndex);
        List<Opt> opts = topic.getOps();
        int size = opts.size();
        //供选答案
        if (topic.getType().equals("c5") || topic.getType().equals("c4")) {     //不显示问答题、填空题答案
            LinearLayout.LayoutParams params =
                    new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
            optsLayout.setLayoutParams(params);
        } else {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(3, 18, 3, 0);
            optsLayout.setLayoutParams(params);
            for (int i = 0; i < size; i++) {
                final View optView = inflater.inflate(R.layout.item_exam_opt, null);

                TextView optItem = (TextView) optView.findViewById(R.id.opt_content);
                final MyCheckView optCheck = (MyCheckView) optView.findViewById(R.id.switch_opt);

                optItem.setTextColor(Color.DKGRAY);
                optItem.setText(opts.get(i).getContent());

                optView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                          /*
                           设置单选、多选action
                           */
                        switch (topic.getType()) {
                            case "c1":              //单选
                            case "c3":              //判断
                                int pos = optsLayout.indexOfChild(optView);
                                int childCount = optsLayout.getChildCount();
                                if (optCheck.isChecked()) {
                                    optCheck.setIsChecked(false);
                                } else {
                                    optCheck.setIsChecked(true);
                                    for (int i = 0; i < childCount; i++) {
                                        if (i != pos) {
                                            MyCheckView check = (MyCheckView) optsLayout.getChildAt(i).findViewById(R.id.switch_opt);
                                            if (check.isChecked()) {
                                                check.setIsChecked(false);
                                            }
                                        }

                                    }
                                }

//                                Log.e("TAG", ">>>>>>>>>>>>>>>..c1.c3..>>>>>");
                                break;
                            case "c2":              //多选
                                if (optCheck.isChecked()) {
                                    optCheck.setIsChecked(false);
                                } else {
                                    optCheck.setIsChecked(true);
                                }
                                break;
                            default:
                                break;
                        }
                    }
                });

                optsLayout.addView(optView);

            }

        }

        //参考答案
        if (isShownOpts) {
            for (int i = 0; i < size; i++) {
                Opt opt = opts.get(i);
                if (opt.is_corrent()) {
                    View optView = inflater.inflate(R.layout.item_exam_opt, null);

                    TextView optItem = (TextView) optView.findViewById(R.id.opt_content);
                    MyCheckView optCheck = (MyCheckView) optView.findViewById(R.id.switch_opt);
                    optCheck.setVisibility(View.GONE);
                    optItem.setTextColor(Color.DKGRAY);
                    optItem.setText(opts.get(i).getContent());
                    trueOptsLayout.addView(optView);
                }
            }
        }

        //题目
        topicName.setText(String.valueOf(recIndex + 1) + ". " + topic.getContent());
        //title
        switch (topic.getType()) {
            case "c1":
                title.updateCenterTitle("单选题");
                break;
            case "c2":
                title.updateCenterTitle("多选题");
                break;
            case "c3":
                title.updateCenterTitle("判断题");
                break;
            case "c4":
                title.updateCenterTitle("简答题");
                break;
            case "c5":
                title.updateCenterTitle("问答题");
                break;
            default:
                break;
        }

        //下标
        indexView.setText(String.valueOf(doneList.size()) + "/" + String.valueOf(linkedList.size()));

    }

    /**
     * 提交题目答案
     */
    protected void submitTopic() {
        //加入到已提交列表中
        if (!doneList.contains(String.valueOf(recIndex + 1))) {
            doneList.add(String.valueOf(recIndex + 1));
        }

        /*
        ignoreConn : "Y",
        method:"doAnswer",
        examId : ,
        answer_id : qid,
        val:val
         */
        int size = optsLayout.getChildCount();
        ArrayList<String> ids = new ArrayList<>();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                MyCheckView checkView = (MyCheckView) optsLayout.getChildAt(i).findViewById(R.id.switch_opt);
                if (checkView.isChecked()) {
                    ids.add(linkedList.get(recIndex).getOps().get(i).getThe_id());
                }
            }
        }
        String examId = linkedList.get(recIndex).getFk_exam_id();
        String answerId = linkedList.get(recIndex).getFk_que_id();
        String[] optIds = new String[ids.size()];
        for (int i = 0; i < ids.size(); i++) {
            optIds[i] = ids.get(i);
        }

        mQueue.add(subAnswer(answerId, optIds));
    }

    /**
     * 提交答案网络请求
     */
    protected StringRequest subAnswer(final String answer_id, final String[] values) {

        return new StringRequest(BaseURLUtil.SUB_ANSWER, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(DoPaperActivity.this, "网络问题，请稍后重试!", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("ignoreConn", "Y");
                map.put("method", "doAnswer");
                map.put("examId", exam_id);
                map.put("answer_id", answer_id);
                for (String value : values) {
                    map.put("val[]", value);
                }
                return map;
            }
        };
    }

    /**
     * 交卷
     */
    protected StringRequest subPaper() {

        return new StringRequest(BaseURLUtil.SUB_ANSWER, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Toast.makeText(DoPaperActivity.this, "试卷已提交!", Toast.LENGTH_LONG).show();
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(DoPaperActivity.this, "网络问题，请稍后重试!", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("ignoreConn", "Y");
                map.put("method", "submitExam");
                map.put("exam_id", exam_id);
                return map;
            }
        };
    }

    static class MyHandler extends Handler {
        private WeakReference<DoPaperActivity> reference;

        public MyHandler(DoPaperActivity activity) {
            reference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            DoPaperActivity activity = reference.get();
            super.handleMessage(msg);
            if (activity != null) {
                switch (msg.what) {
                    case 0:
                        activity.setLinkedTopicData();
//                        Log.e("TAG", ".........handler.........");
                        break;
                    default:
                        break;

                }
            }
        }
    }
}
