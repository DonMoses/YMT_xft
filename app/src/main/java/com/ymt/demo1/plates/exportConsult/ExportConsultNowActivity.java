package com.ymt.demo1.plates.exportConsult;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.ymt.demo1.R;
import com.ymt.demo1.adapter.SimpleTxtItemAdapter;
import com.ymt.demo1.customViews.MyTitle;
import com.ymt.demo1.main.BaseFloatActivity;
import com.ymt.demo1.main.PopActionListener;
import com.ymt.demo1.main.PopActionUtil;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Dan on 2015/5/12
 */
public class ExportConsultNowActivity extends BaseFloatActivity {

    protected boolean isSigned = false;     //
    private ConsultInputCallBack consultInputCallBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consult_now);
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
        final EditText titleTxt = (EditText) findViewById(R.id.input_consult_title);
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
                        Toast.makeText(ExportConsultNowActivity.this,
                                "todo 打开文件管理器 - 选择文件", Toast.LENGTH_SHORT).show();
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
                            if (isSigned) {           //注册用户
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
//                                                logInfo(title, content);
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

                            } else {            //非注册用户
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
//                                                logInfo(title, content);
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
                                //todo 获取服务器返回的随机账号和密码
                                //假设账号： moses2015   密码： sob123
                                PopupWindow popupWindow =
                                        popActionUtil.getSubmitConsultUnsignedPop("moses2015", "sob123");
                                int width = (int) (titleTxt.getRootView().getWidth() * 0.8);
                                popupWindow.setWidth(width);
                                popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
                                popupWindow.showAtLocation(titleTxt.getRootView(), Gravity.CENTER, 0, 0);

                            }
                            //测试用boolean 值
                            isSigned = !isSigned;
                            Toast.makeText(ExportConsultNowActivity.this,
                                    "todo 提交成功pop", Toast.LENGTH_SHORT).show();
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
     * 提交的title 和content
     */
    protected void logInfo(String title, String content) {
        Log.e("TAG", title);
        Log.e("TAG", content);
    }

    /**
     * title 和content 内容改变监听
     */
    protected interface ConsultInputCallBack {
        void callBackTitle(String titleStr);

        void callBackContent(String contentStr);
    }


}
