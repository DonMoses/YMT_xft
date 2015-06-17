package com.ymt.demo1.plates.exportConsult;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ymt.demo1.R;
import com.ymt.demo1.adapter.QQChatListAdapter;
import com.ymt.demo1.adapter.SimpleTxtItemAdapter;
import com.ymt.demo1.beams.expert_consult.Expert;
import com.ymt.demo1.beams.expert_consult.QQChatInfo;
import com.ymt.demo1.customViews.MyTitle;
import com.ymt.demo1.main.AppContext;
import com.ymt.demo1.main.BaseFloatActivity;
import com.ymt.demo1.main.BaseURLUtil;
import com.ymt.demo1.main.PopActionListener;
import com.ymt.demo1.main.PopActionUtil;
import com.ymt.demo1.main.file.SearchFileActivity;
import com.ymt.demo1.main.sign.SignInActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Dan on 2015/5/12
 */
public class ExportConsultNowActivity extends BaseFloatActivity {

    //    protected boolean isSigned = false;
    private ConsultInputCallBack consultInputCallBack;
    private RequestQueue mQueue;
    private Expert expert;
    private EditText titleTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consult_now);
        mQueue = Volley.newRequestQueue(this);
        expert = getIntent().getParcelableExtra("expert_info");

        initTitle();
        initView();
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
//                        Toast.makeText(ExportConsultNowActivity.this,
//                                "todo 打开文件管理器 - 选择文件", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ExportConsultNowActivity.this, SearchFileActivity.class));
                        break;
                    case R.id.submit_btn:
                        final String title = titleTxt.getText().toString();
                        final String content = contentTxt.getText().toString();
                        //todo 提交咨询
                        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(content)) {
                            Toast.makeText(ExportConsultNowActivity.this,
                                    "标题和内容不能为空，请重新填写后提交！", Toast.LENGTH_SHORT).show();
                        } else {
                            //todo 弹出popup，提示提交成功【游客返回系统随机生成的账号和密码】
                            if (!(TextUtils.isEmpty(AppContext.now_session_id))) {           //注册用户

                                /*
                                 发起一条QQ会话
                                 */
                                mQueue.add(requestQQChat(AppContext.now_session_id, title, content, expert.getThe_id()));


                            } else {            //非注册用户
//                                final WindowManager.LayoutParams lp =
//                                        ExportConsultNowActivity.this.getWindow().getAttributes();
//                                lp.alpha = 0.3f;
//                                ExportConsultNowActivity.this.getWindow().setAttributes(lp);
//                                PopActionUtil popActionUtil = PopActionUtil.getInstance(ExportConsultNowActivity.this);
//                                popActionUtil.setActionListener(new PopActionListener() {
//                                    @Override
//                                    public void onAction(String action) {
//                                        switch (action) {
//                                            case "确定":
////                                                logInfo(title, content);
//                                                break;
//                                            default:
//                                                break;
//                                        }
//                                    }
//
//                                    @Override
//                                    public void onDismiss() {
//                                        lp.alpha = 1f;
//                                        ExportConsultNowActivity.this.getWindow().setAttributes(lp);
//                                    }
//                                });
//                                //todo 获取服务器返回的随机账号和密码
//                                //假设账号： moses2015   密码： sob123
//                                PopupWindow popupWindow =
//                                        popActionUtil.getSubmitConsultUnsignedPop("moses2015", "sob123");
//                                int width = (int) (titleTxt.getRootView().getWidth() * 0.8);
//                                popupWindow.setWidth(width);
//                                popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
//                                popupWindow.showAtLocation(titleTxt.getRootView(), Gravity.CENTER, 0, 0);
                                Toast.makeText(ExportConsultNowActivity.this,
                                        "请先登录...", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(ExportConsultNowActivity.this, SignInActivity.class));
                            }

                        }
                        break;
                    default:
                        break;
                }
            }
        };

        uploadBtn.setOnClickListener(onClickListener);
        submitBtn.setOnClickListener(onClickListener);
        //底部热点话题
        final GridView hotGrid = (GridView) findViewById(R.id.hot_comment_grid_view);
        setDataToHotGird(hotGrid);
    }

    protected void popMyWindow(final String qq_id) {
         /*
         弹出对话框
         */
        final WindowManager.LayoutParams lp =
                ExportConsultNowActivity.this.getWindow().getAttributes();
        lp.alpha = 0.3f;
        ExportConsultNowActivity.this.getWindow().setAttributes(lp);
        PopActionUtil popActionUtil = PopActionUtil.getInstance(ExportConsultNowActivity.this);
        popActionUtil.setActionListener(new PopActionListener() {
            @Override
            public void onAction(String action) {
                switch (action) {
                    case "确定":
                        Intent intent = new Intent(ExportConsultNowActivity.this, ConsultChatActivity.class);
                        intent.putExtra("session_id", AppContext.now_session_id);
                        intent.putExtra("qq_id", qq_id);
                        startActivity(intent);
                        onDismiss();
                        finish();
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onDismiss() {
                lp.alpha = 1f;
                ExportConsultNowActivity.this.getWindow().setAttributes(lp);
            }
        });
        PopupWindow popupWindow = popActionUtil.getSubmitConsultSignedPop();
        int width = (int) (titleTxt.getRootView().getWidth() * 0.618);
        popupWindow.setWidth(width);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.showAtLocation(titleTxt.getRootView(), Gravity.CENTER, 0, 0);
    }

    /**
     * 为底部热点话题设置数据
     */
    protected void setDataToHotGird(GridView gridView) {
        ArrayList<String> list = new ArrayList<>();
        String[] hotArray = new String[]{"消防部门", "规范组", "建委",
                "科研机构", "设计院", "开发商", "设备商", "服务商"};
        Collections.addAll(list, hotArray);
        SimpleTxtItemAdapter adapter = new SimpleTxtItemAdapter(this);
        gridView.setAdapter(adapter);
        adapter.setColor(getResources().getColor(R.color.bg_like_white),
                getResources().getColor(R.color.bg_view_blue));
        adapter.setList(list);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String str = parent.getAdapter().getItem(position).toString();
                //todo
                Toast.makeText(ExportConsultNowActivity.this, str, Toast.LENGTH_SHORT).show();
            }
        });
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
    protected StringRequest requestQQChat(String sessionId, String title, String content, String exportId) {
        return new StringRequest(BaseURLUtil.startQQChatUrlGET(sessionId, title, content, exportId), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String result = jsonObject.getString("result");
                    if (result.equals("Y")) {
                        String qq_id = jsonObject.getString("qq_id");

                        /*
                        todo 加入到QQ会话列表*（更新数据库）
                         */
                        mQueue.add(getMyQQMsgs());
                        popMyWindow(qq_id);

                    } else {
                        //todo 调到会话列表界面
                        startActivity(new Intent(ExportConsultNowActivity.this, MyConsultActivity.class));
                        finish();
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

    private ChatListUpdateHolder chatListUpdateHolder;

    /**
     * 获取QQ会话列表
     */
    protected StringRequest getMyQQMsgs() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        chatListUpdateHolder = new ChatListUpdateHolder((ExportChatListFragment) fragmentManager.findFragmentByTag(ExportChatListFragment.FRAGMENT_TAG));

        String saveSID = AppContext.now_session_id;

        StringRequest stringRequest = null;
        if (!TextUtils.isEmpty(saveSID)) {
            stringRequest = new StringRequest(BaseURLUtil.getMyQQMsgs(saveSID), new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    try {
                        JSONObject object = new JSONObject(s);
                        JSONObject object1 = object.getJSONObject("datas");
                        JSONArray jsonArray = object1.getJSONArray("listData");
                        int length = jsonArray.length();

                        /*
                        如果数据库中没有对应QQ会话，则加入对应QQ会话
                         */
                        for (int i = 0; i < length; i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            QQChatInfo qqChatInfo = new QQChatInfo();
                            String qq_id = obj.optString("id");
                            qqChatInfo.setQq_id(qq_id);
                            qqChatInfo.setMsg_title(obj.optString("msg_title"));
                            qqChatInfo.setMsg_time(obj.optString("msg_time"));
                            qqChatInfo.setFk_user_id(obj.optString("fk_user_id"));
                            qqChatInfo.setCreate_time(obj.optString("create_time"));
                            qqChatInfo.setStatus(obj.optString("status"));
                            qqChatInfo.setFk_company_id(obj.optString("fk_company_id"));
                            qqChatInfo.setElec_price(obj.optString("elec_price"));
                            qqChatInfo.setFk_pro_id(obj.optString("fk_pro_id"));
                            qqChatInfo.setMsg_num(obj.optInt("msg_num"));
                            qqChatInfo.setFk_contract_id(obj.optString("fk_contract_id"));
                            int size = DataSupport.where("qq_id = ?", qq_id).find(QQChatInfo.class).size();
                            if (size == 0) {
                                qqChatInfo.save();
                            } else {
                                qqChatInfo.updateAll("qq_id = ?", qq_id);
                            }
                        }

                        chatListUpdateHolder.onUpdate();

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
        return stringRequest;

    }

    /**
     * 更新会话列表
     */
    static class ChatListUpdateHolder {
        static WeakReference<ExportChatListFragment> listFragmentWeakReference;

        public ChatListUpdateHolder(ExportChatListFragment exportChatListFragment) {
            listFragmentWeakReference = new WeakReference<>(exportChatListFragment);
        }

        public void onUpdate() {
            ExportChatListFragment exportChatListFragment = listFragmentWeakReference.get();
            if (exportChatListFragment != null) {
                QQChatListAdapter chatListAdapter = exportChatListFragment.chatAdapter;
                List<QQChatInfo> chatInfos = DataSupport.findAll(QQChatInfo.class);
                chatListAdapter.setList(chatInfos);
            }
        }

    }


}
