package com.ymt.demo1.plates.exportConsult;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ymt.demo1.R;
import com.ymt.demo1.beams.consult_cato.ConsultCato;
import com.ymt.demo1.beams.expert_consult.PreExpert;
import com.ymt.demo1.customViews.MyTitle;
import com.ymt.demo1.plates.exportConsult.chat.ConsultChatActivity;
import com.ymt.demo1.utils.AppContext;
import com.ymt.demo1.baseClasses.BaseFloatActivity;
import com.ymt.demo1.utils.BaseURLUtil;
import com.ymt.demo1.main.file.SearchFileActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dan on 2015/5/12
 */
public class ConsultNowActivity extends BaseFloatActivity {

    private ConsultInputCallBack consultInputCallBack;
    private RequestQueue mQueue;
    private int theExpertId = 0;
    private int expertId;
    private EditText titleTxt;
    private int codeId; //类型
    private int cId;    //本次咨询条目的Id
    private List<PreExpert> experts;
    private ExpertAdapter expertAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consult_now);
        mQueue = Volley.newRequestQueue(this);
        expertId = getIntent().getIntExtra("expertId", 0);
        experts = new ArrayList<>();
        initTitle();
        initView();
        initTypeSpinner();
    }

    /**
     * 初始化title
     */
    protected void initTitle() {
        MyTitle myTitle = (MyTitle) findViewById(R.id.my_title);
        myTitle.setTitleStyle(MyTitle.TitleStyle.LEFT_ICON);
        myTitle.setOnLeftActionClickListener(new MyTitle.OnLeftActionClickListener() {
            @Override
            public void onClick() {
                finish();
            }
        });
    }

    protected void initView() {
        //标题输入框
        titleTxt = (EditText) findViewById(R.id.input_consult_title);
        //内容输入框
        final EditText contentTxt = (EditText) findViewById(R.id.input_consult_content);

        //输入监听，动态改变输入框内容
        consultInputCallBack = new ConsultInputCallBack() {
            @Override
            public void callBackTitle(String titleStr) {
//                Log.e("TAG", "title>>>>>>>>" + titleStr);
                titleTxt.setText(titleStr);
                titleTxt.setSelection(titleStr.length());
            }

            @Override
            public void callBackContent(String contentStr) {
                contentTxt.setText(contentStr);
                contentTxt.setSelection(contentStr.length());
            }
        };

        titleTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(final Editable s) {
                if (!TextUtils.isEmpty(s.toString())) {
                    titleTxt.removeTextChangedListener(this);
                    consultInputCallBack.callBackTitle("标题：" + s.toString());
                }
            }
        });

        contentTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString())) {
                    contentTxt.removeTextChangedListener(this);
                    consultInputCallBack.callBackContent("内容：" + s.toString());
                }
            }
        });

        //上传附件按钮
        final Button uploadBtn = (Button) findViewById(R.id.upload_btn);
        //提交按钮
        final Button submitBtn = (Button) findViewById(R.id.submit_btn);
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.upload_btn:
                        //todo 上传文件【文件管理器界面】
//                                "todo 打开文件管理器 - 选择文件", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ConsultNowActivity.this, SearchFileActivity.class));
                        break;
                    case R.id.submit_btn:
                        try {
                            String title = new String(titleTxt.getText().toString().getBytes(), "UTF-8");
                            String content = new String(contentTxt.getText().toString().getBytes(), "UTF-8");

                            //todo 提交咨询
                            if (TextUtils.isEmpty(title) || TextUtils.isEmpty(content)) {
                                Toast.makeText(ConsultNowActivity.this,
                                        "标题和内容不能为空，请重新填写后提交！", Toast.LENGTH_SHORT).show();
                            } else {
                                //todo 弹出popup，提示提交成功【游客返回系统随机生成的账号和密码】
                                if (!(TextUtils.isEmpty(AppContext.now_session_id))) {           //注册用户
                                    //发起一条QQ会话
                                    mQueue.add(requestQQChatByVolley(title, content, codeId, theExpertId, AppContext.now_session_id));

//                                    Log.e("TAG", ">>>>>>>>>title :" + title);
//                                    Log.e("TAG", ">>>>>>>>>content :" + content);
                                }
                            }
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                        break;
                    default:
                        break;
                }
            }
        };

        uploadBtn.setOnClickListener(onClickListener);
        submitBtn.setOnClickListener(onClickListener);
    }

    /**
     * title 和content 内容改变监听
     */
    protected interface ConsultInputCallBack {
        void callBackTitle(String titleStr);

        void callBackContent(String contentStr);
    }

    /**
     * 提交问题（发起一个QQ会话）
     */
    protected StringRequest requestQQChatByVolley(final String title, final String content, final int type, final int expertId, final String sId) {

        return new StringRequest(BaseURLUtil.startQQChatUrlGET(URLEncoder.encode(title), URLEncoder.encode(content), type, expertId, sId), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int id = jsonObject.getJSONObject("datas").optInt("listData");
                    if (id > 0) {
                        cId = id;
                        Intent intent = new Intent(ConsultNowActivity.this, ConsultChatActivity.class);
                        intent.putExtra("cId", cId);
                        startActivity(intent);
                        ConsultNowActivity.this.finish();
                    } else {
                        //todo 回到会话列表界面
                        startActivity(new Intent(ConsultNowActivity.this, MyConsultActivity.class));
                        finish();
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

    private Spinner spinnerII;
    private ArrayAdapter<String> typeAdapter;

    private void initTypeSpinner() {
        /*
      类型选择器
     */
        Spinner spinnerI = (Spinner) findViewById(R.id.spinner_1);
        spinnerII = (Spinner) findViewById(R.id.spinner_2);
        String[] codeTypes = new String[]{"建筑", "专业", "关键词"};
        ArrayAdapter<String> codeTypeAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_dropdown_item, android.R.id.text1, codeTypes);
        spinnerI.setAdapter(codeTypeAdapter);

        List<ConsultCato> cato6 = DataSupport.where("codeType = ?", String.valueOf(6)).find(ConsultCato.class);
        List<ConsultCato> cato7 = DataSupport.where("codeType = ?", String.valueOf(7)).find(ConsultCato.class);
        List<ConsultCato> cato8 = DataSupport.where("codeType = ?", String.valueOf(8)).find(ConsultCato.class);
        final String[] type1 = new String[cato6.size()];
        final String[] type2 = new String[cato7.size()];
        final String[] type3 = new String[cato8.size()];
        for (int i = 0; i < cato6.size(); i++) {
            type1[i] = cato6.get(i).getCodeValue();
        }
        for (int i = 0; i < cato7.size(); i++) {
            type2[i] = cato7.get(i).getCodeValue();
        }
        for (int i = 0; i < cato8.size(); i++) {
            type3[i] = cato8.get(i).getCodeValue();
        }

        spinnerI.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (typeAdapter != null) {
                    typeAdapter = null;
                }
                switch (position) {
                    case 0: //建筑
                        typeAdapter = new ArrayAdapter<>(
                                ConsultNowActivity.this, android.R.layout.simple_spinner_dropdown_item, android.R.id.text1, type1);
                        break;
                    case 1: //专业
                        typeAdapter = new ArrayAdapter<>(
                                ConsultNowActivity.this, android.R.layout.simple_spinner_dropdown_item, android.R.id.text1, type2);
                        break;
                    case 2: //关键词s
                        typeAdapter = new ArrayAdapter<>(
                                ConsultNowActivity.this, android.R.layout.simple_spinner_dropdown_item, android.R.id.text1, type3);
                        break;
                    default:
                        break;

                }
                spinnerII.setAdapter(typeAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerII.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                codeId = DataSupport.where("codeValue = ?", parent.getAdapter().getItem(position).toString())
                        .find(ConsultCato.class).get(0).getCodeId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner expertSpinner = (Spinner) findViewById(R.id.spinner_3);
        expertAdapter = new ExpertAdapter(this);
        expertAdapter.setExperts(experts);
        expertSpinner.setAdapter(expertAdapter);
        expertSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                theExpertId = ((PreExpert) parent.getAdapter().getItem(position)).getFkUserId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                theExpertId = expertId;
            }
        });

        mQueue.add(getExperts(50, 1, ""));

    }

    class ExpertAdapter extends BaseAdapter {
        private List<PreExpert> experts;
        private LayoutInflater inflater;
        private Context context;

        public ExpertAdapter(Context context) {
            this.context = context;
            this.inflater = LayoutInflater.from(context);
        }

        public void setExperts(List<PreExpert> experts) {
            this.experts = experts;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return experts.size();
        }

        @Override
        public Object getItem(int position) {
            return experts.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            CEHolder holder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.layout_expert_want, null);
                holder = new CEHolder();
                holder.name = (TextView) convertView.findViewById(R.id.expert_name);
                holder.type = (TextView) convertView.findViewById(R.id.expert_type);
                holder.con_count = (TextView) convertView.findViewById(R.id.consult_count);
                holder.waiting_count = (TextView) convertView.findViewById(R.id.consult_waiting_count);
                convertView.setTag(holder);
            } else {
                holder = (CEHolder) convertView.getTag();
            }
            holder.name.setText(experts.get(position).getUsername());
            holder.type.setText(experts.get(position).getLevel());
            holder.con_count.setText("咨询人数 " + experts.get(position).getCount() + "人");
            holder.waiting_count.setText("等待人数 " + experts.get(position).getWaitCount() + "人");
            return convertView;
        }

        class CEHolder {
            private TextView name;
            private TextView type;
            private TextView con_count;
            private TextView waiting_count;

        }
    }

    protected StringRequest getExperts(int pageSize, int start, String searchWho) {
        return new StringRequest(BaseURLUtil.doGetExpertList(pageSize, start, searchWho), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getString("result").equals("Y")) {
                        JSONArray jsonArray = jsonObject.getJSONObject("datas").getJSONArray("listData");
                        int length = jsonArray.length();
                        for (int i = 0; i < length; i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            PreExpert preExpert = new PreExpert();
                            preExpert.setCount(obj.optInt("count"));
                            preExpert.setUsername(obj.optString("username"));
                            preExpert.setLevel(obj.optString("level"));
                            preExpert.setFkUserId(obj.optInt("fkUserId"));
                            preExpert.setWaitCount(obj.optInt("waitCount"));
                            preExpert.setHeadImage(obj.optString("headImage"));
                            int size = DataSupport.where("fkUserId = ?", String.valueOf(preExpert.getFkUserId())).find(PreExpert.class).size();
                            if (size == 0) {
                                preExpert.save();
                            } else {
                                preExpert.updateAll("fkUserId = ?", String.valueOf(preExpert.getFkUserId()));
                            }
                        }

                        experts.clear();
                        experts.addAll(DataSupport.findAll(PreExpert.class));
                        expertAdapter.notifyDataSetChanged();
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
