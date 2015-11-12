package com.ymt.demo1.plates.eduPlane.mockExams;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ymt.demo1.R;
import com.ymt.demo1.baseClasses.BaseFloatActivity;
import com.ymt.demo1.beams.edu.MockExamItem;
import com.ymt.demo1.customViews.MyTitle;
import com.ymt.demo1.main.search.FullSearchActivity;
import com.ymt.demo1.utils.AppContext;
import com.ymt.demo1.utils.BaseURLUtil;
import com.ymt.demo1.utils.PopActionListener;
import com.ymt.demo1.utils.PopActionUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by DonMoses on 2015/11/5
 */
public class AnswerActivity extends BaseFloatActivity {
    private static final int SINGLE_ONE = 1;
    private MockExamItem examItem;
    private String userMockId;
    private RequestQueue mQueue;

    //存放已经完成的题目序列
    private ArrayList<Integer> doneList;
    //所有题目序列
    private ArrayList<Integer> allList;
    //当前所在的题目位置
    private int index;
    //当前题目试题
    private TTT ttt;
    //试题的答案
    private StringBuilder ansBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mQueue = Volley.newRequestQueue(this);
        examItem = getIntent().getParcelableExtra("item");
        userMockId = getIntent().getStringExtra("userMockId");
        init();
        setContentView(R.layout.activity_answer);
        initView();
        mQueue.add(getQuestionInfo(examItem.getExaId(), userMockId, AppContext.now_session_id, index, SINGLE_ONE));
    }

    private TextView tttTitle;//题目
    private LinearLayout tttAnsLayout; //参考答案容器

    View tttIndex;  //题目完成情况1
    private TextView tttIndexTxt;  //题目完成情况Txt
    private TextView tttUp; //上一题
    private TextView tttDown; //下一题
    View tttSubmit;   //提交试卷

    private void init() {
        doneList = new ArrayList<>();
        allList = new ArrayList<>();
        for (int i = 1; i < examItem.getBank_num() + 1; i++) {
            allList.add(i);
        }
        index = 1;
        ansBuilder = new StringBuilder();
    }

    private void initView() {
        MyTitle title = (MyTitle) findViewById(R.id.my_title);
        title.setTitleStyle(MyTitle.TitleStyle.RIGHT_ICON_L);
        title.updateCenterTitle(examItem.getExaName());
        title.setOnLeftActionClickListener(new MyTitle.OnLeftActionClickListener() {
            @Override
            public void onClick() {
                finish();
            }
        });
        title.setOnRightActionClickListener(new MyTitle.OnRightActionClickListener() {
            @Override
            public void onRightLClick() {
                startActivity(new Intent(AnswerActivity.this, FullSearchActivity.class));
            }

            @Override
            public void onRightRClick() {

            }
        });

        tttTitle = (TextView) findViewById(R.id.ttt_title);
        tttAnsLayout = (LinearLayout) findViewById(R.id.ttt_ans_layout);

        tttIndex = findViewById(R.id.ttt_view_ind);
        tttIndexTxt = (TextView) findViewById(R.id.ttt_index);
        tttUp = (TextView) findViewById(R.id.ttt_up);
        if (index == 1) {
            tttUp.setVisibility(View.INVISIBLE);
        }
        tttDown = (TextView) findViewById(R.id.ttt_down);
        //提交题目
        tttSubmit = findViewById(R.id.ttt_sub);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2015/11/5
                switch (v.getId()) {
                    case R.id.ttt_view_ind:   //完成情况Grid
                        jumpToViewPager();
                        break;
                    case R.id.ttt_up:       //上一题
                        if (index > 1) {
                            setChoosedAns(tttAnsLayout);
                            mQueue.add(submitQuestion(ansBuilder.toString(), index, ttt.getCordId()));
                            tttUp.setVisibility(View.VISIBLE);
                            tttDown.setVisibility(View.VISIBLE);
                            //获取上一题
                            index--;
                            mQueue.add(getQuestionInfo(examItem.getExaId(), userMockId, AppContext.now_session_id, index, SINGLE_ONE));
                        } else {
                            tttUp.setVisibility(View.INVISIBLE);
                        }
                        break;
                    case R.id.ttt_down:     //下一题
                        //提交本题
                        setChoosedAns(tttAnsLayout);
                        mQueue.add(submitQuestion(ansBuilder.toString(), index, ttt.getCordId()));
                        if (index < examItem.getBank_num()) {
                            tttUp.setVisibility(View.VISIBLE);
                            tttDown.setVisibility(View.VISIBLE);
                            //获取下一题
                            index++;
                            mQueue.add(getQuestionInfo(examItem.getExaId(), userMockId, AppContext.now_session_id, index, SINGLE_ONE));
                        } else {
                            tttDown.setVisibility(View.INVISIBLE);
                        }
                        break;
                    case R.id.ttt_sub:
                        //pop提交试卷
                        popSubmitPager();
                        break;
                    default:
                        break;
                }
            }
        };

        tttUp.setOnClickListener(onClickListener);
        tttDown.setOnClickListener(onClickListener);
        tttSubmit.setOnClickListener(onClickListener);
        tttIndex.setOnClickListener(onClickListener);
    }

    //设置选中的答案
    private void setChoosedAns(LinearLayout tttAnsLayout) {
        //提交本题
        if (ansBuilder != null) {
            ansBuilder = null;
            ansBuilder = new StringBuilder();
        } else {
            ansBuilder = new StringBuilder();
        }
        if (tttAnsLayout.getChildAt(0) instanceof RadioGroup) {     //单选、判断题
            RadioGroup ansGroup = (RadioGroup) tttAnsLayout.getChildAt(0);
            int ansIndex = 1024;
            for (int i = 0; i < ansGroup.getChildCount(); i++) {
                RadioButton ansBtn = (RadioButton) ansGroup.getChildAt(i);
                if (ansBtn.isChecked()) {
                    ansIndex = i;
                    break;
                }
            }
            switch (ansIndex) {
                case 0:
                    ansBuilder.append("A");
                    break;
                case 1:
                    ansBuilder.append("B");
                    break;
                case 2:
                    ansBuilder.append("C");
                    break;
                case 3:
                    ansBuilder.append("D");
                    break;
                default:
                    break;
            }

        } else if (tttAnsLayout.getChildAt(0) instanceof CheckBox) {   //多选
            for (int i = 0; i < tttAnsLayout.getChildCount(); i++) {
                CheckBox ansBtn = (CheckBox) tttAnsLayout.getChildAt(i);
                if (ansBtn.isChecked()) {
                    switch (i) {
                        case 0:
                            ansBuilder.append("A");
                            break;
                        case 1:
                            ansBuilder.append("B");
                            break;
                        case 2:
                            ansBuilder.append("C");
                            break;
                        case 3:
                            ansBuilder.append("D");
                            break;
                        case 4:
                            ansBuilder.append("E");
                            break;
                        case 5:
                            ansBuilder.append("F");
                            break;
                        case 6:
                            ansBuilder.append("G");
                            break;
                        default:
                            break;
                    }
                }
            }
        } else if (tttAnsLayout.getChildAt(0) instanceof EditText) {   //问答
            EditText ansTxt = (EditText) tttAnsLayout.getChildAt(0);
            ansBuilder.append(ansTxt.getText().toString());
        }
    }

    //获得题目（逐条）
    private StringRequest getQuestionInfo(String examId, String userMockId, String sId, int index, int pageNum) {
        // TODO: 2015/11/5
        return new StringRequest(BaseURLUtil.getMockTTT(examId, userMockId, sId, index, pageNum), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.optString("result").equals("Y")) {
                        JSONArray jsonArray = jsonObject.getJSONObject("datas").getJSONObject("listData")
                                .getJSONArray("topic");
                        if (jsonArray.length() > 0) {
                            JSONObject obj = jsonArray.getJSONObject(0);
                            TTT ttt = new TTT();
                            ttt.setTestId(obj.optString("testId"));
                            ttt.setTopicType(obj.optInt("topicType"));
                            ttt.setProblem(obj.optString("problem"));
                            ttt.setCordId(obj.optInt("cordId"));
                            ttt.setTopicNo(obj.optInt("topicNo"));
                            JSONArray optArray = obj.optJSONArray("options");
                            String[] opts = new String[optArray.length()];
                            for (int i = 0; i < optArray.length(); i++) {
                                opts[i] = optArray.getString(i);
                            }
                            ttt.setOptions(opts);
                            AnswerActivity.this.ttt = ttt;
                            //显示供选答案或者输入框
                            setAnsOpts(ttt.getTopicType(), ttt.getOptions(), tttAnsLayout);
                            //显示题目信息
                            showQuestionInfo(ttt);
                            //更新题目Index
                            setIndexState(tttIndexTxt);
                        }

                    }

                } catch (JSONException e) {
                    AppContext.toastBadJson();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                AppContext.toastBadJson();
            }
        });
    }

    //显示上面获得的题目信息
    private void showQuestionInfo(TTT ttt) {
        String type = null;
        switch (ttt.getTopicType()) {
            case 1:
                type = "单选题";
                break;
            case 2:
                type = "多选题";
                break;
            case 3:
                type = "判断题";
                break;
            case 5:
                type = "问答题";
                break;
            default:
                break;
        }
        tttTitle.setText(String.valueOf(ttt.getTopicNo()) + ".（" + type + "） " + ttt.getProblem());
        if (index == 1) {
            tttUp.setVisibility(View.INVISIBLE);
        }
        if (index == examItem.getBank_num()) {
            tttDown.setVisibility(View.INVISIBLE);
        }
    }

    //提交题目（逐条）
    private StringRequest submitQuestion(String ans, final int topicNo, int cordId) {
//        Log.e("TAG", ">>>>>>>submit ans : " + BaseURLUtil.submitMockTTT(ans, topicNo, cordId));
        return new StringRequest(BaseURLUtil.submitMockTTT(ans, topicNo, cordId), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //todo 成功提交，将坐标加入容器
                addDoneIndexToList(topicNo);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
    }

    //提交试卷确认框
    private void popSubmitPager() {
        mQueue.add(submitQuestion(ansBuilder.toString(), examItem.getBank_num(), ttt.getCordId()));

        //设置背景颜色变暗
        final WindowManager.LayoutParams lp =
                AnswerActivity.this.getWindow().getAttributes();
        lp.alpha = 0.3f;
        AnswerActivity.this.getWindow().setAttributes(lp);
        final PopActionUtil popActionUtil = PopActionUtil.getInstance(this);
        PopupWindow popupWindow = popActionUtil.getSubPaperPopActionMenu(examItem);
        popupWindow.showAtLocation(tttAnsLayout.getRootView(), Gravity.CENTER, 0, 0);
        popActionUtil.setActionListener(new PopActionListener() {
            @Override
            public void onAction(String action) {
                switch (action) {
                    case "确定":
                        mQueue.add(submitExamPager(examItem.getExaId(), userMockId, AppContext.now_session_id));
                        break;
                    case "取消":
                        Toast.makeText(AnswerActivity.this, "试卷未提交！", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
                lp.alpha = 1f;
                AnswerActivity.this.getWindow().setAttributes(lp);
            }

            @Override
            public void onDismiss() {

            }
        });
    }

    //提交试卷
    private StringRequest submitExamPager(String exaId, String userMockId, String sId) {
        return new StringRequest(BaseURLUtil.submitMockPaper(exaId, userMockId, sId), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.optString("result").equals("Y")) {
                        if (jsonObject.getJSONObject("datas").optInt("listData") >= 0) {
                            Toast.makeText(AnswerActivity.this, "试卷已经提交！", Toast.LENGTH_SHORT).show();
                            //// TODO: 2015/11/10 这里应该返回考试的成绩
                            new Timer().schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    startActivity(new Intent(AnswerActivity.this, MockExamsMainActivity.class));
                                    AnswerActivity.this.finish();
                                }
                            }, 1200);
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

    //更新题目完成缩略图
    private void setIndexState(TextView tttIndexTxt) {
        tttIndexTxt.setText(String.valueOf(doneList.size()) + "/" + String.valueOf(examItem.getBank_num()));
    }

    //查看题目完成情况
    private void jumpToViewPager() {
        Intent intent = new Intent(this, TopicIndexActivity.class);
        intent.putIntegerArrayListExtra("doneList", doneList);
        intent.putIntegerArrayListExtra("allList", allList);
        startActivityForResult(intent, 1024);
    }

    //题目对象
    class TTT {
        private String testId;
        private int topicType;
        private String problem;
        private int cordId;
        private int topicNo;
        private String[] options;

        public void setTestId(String testId) {
            this.testId = testId;
        }

        public int getTopicType() {
            return topicType;
        }

        public void setTopicType(int topicType) {
            this.topicType = topicType;
        }

        public String getProblem() {
            return problem;
        }

        public void setProblem(String problem) {
            this.problem = problem;
        }

        public int getCordId() {
            return cordId;
        }

        public void setCordId(int cordId) {
            this.cordId = cordId;
        }

        public int getTopicNo() {
            return topicNo;
        }

        public void setTopicNo(int topicNo) {
            this.topicNo = topicNo;
        }

        public String[] getOptions() {
            return options;
        }

        public void setOptions(String[] options) {
            this.options = options;
        }
    }

    //切换显示、隐藏参考答案状态
    private void setAnsOpts(int topicType, String[] opts, LinearLayout answersLayout) {
        if (answersLayout.getChildCount() > 0) {
            answersLayout.removeAllViews();
        }
        switch (topicType) {
            case 1:     //单选
            case 3:     //判断
                RadioGroup radioGroup = new RadioGroup(this);
                for (String opt : opts) {
                    RadioButton radioButton = new RadioButton(this);
                    radioButton.setText(opt);
                    radioButton.setTextColor(Color.BLACK);
                    radioGroup.addView(radioButton, RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT);
                }
                answersLayout.addView(radioGroup, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                break;
            case 2:     //多选
                for (String opt : opts) {
                    CheckBox checkBox = new CheckBox(this);
                    checkBox.setText(opt);
                    checkBox.setTextColor(Color.BLACK);
                    answersLayout.addView(checkBox, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
                }
                break;
            case 5:     //问答
                EditText ansEditView = new EditText(this);
                ansEditView.setHint("输入答案：");
                ansEditView.setHintTextColor(Color.DKGRAY);
                ansEditView.setTextColor(Color.BLACK);
                answersLayout.addView(ansEditView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                break;
            default:
                break;
        }


    }

    //将已经完成的题目index加入容器
    private void addDoneIndexToList(int index) {
        boolean submitAlready = false;
        for (int i = 0; i < doneList.size(); i++) {
            if (index == doneList.get(i)) {
                submitAlready = true;
            }
        }
        if (!submitAlready) {
            doneList.add(index);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1024) {
            if (resultCode == RESULT_OK) {
                index = data.getIntExtra("index", index);
                if (index > 1 && index < examItem.getBank_num()) {
                    tttUp.setVisibility(View.VISIBLE);
                    tttDown.setVisibility(View.VISIBLE);
                } else if (index == 1) {
                    tttUp.setVisibility(View.INVISIBLE);
                } else if (index == examItem.getBank_num()) {
                    tttDown.setVisibility(View.INVISIBLE);
                }
                mQueue.add(getQuestionInfo(examItem.getExaId(), userMockId, AppContext.now_session_id, index, SINGLE_ONE));
            }
        }
    }
}
