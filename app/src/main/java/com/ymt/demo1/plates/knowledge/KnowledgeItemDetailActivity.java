package com.ymt.demo1.plates.knowledge;

import android.app.DownloadManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ymt.demo1.R;
import com.ymt.demo1.baseClasses.BaseActivity;
import com.ymt.demo1.customViews.MyTitle;
import com.ymt.demo1.utils.AppContext;
import com.ymt.demo1.utils.BaseURLUtil;
import com.ymt.demo1.utils.PopActionListener;
import com.ymt.demo1.utils.PopActionUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Dan on 2015/4/29
 */
public class KnowledgeItemDetailActivity extends BaseActivity {
    private String knowId;
    private RequestQueue mQueue;
    private String type;
    private String pdfFileUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mQueue = Volley.newRequestQueue(this);
        knowId = getIntent().getStringExtra("knowId");
        setContentView(R.layout.activity_download_layout_pdf);
        initTitle();
        initView();
        if (TextUtils.isEmpty(knowId)) {
            findViewById(R.id.download_info).setVisibility(View.GONE);
            findViewById(R.id.download_btn).setVisibility(View.GONE);
        }
        mQueue.add(getKonDetail(knowId));
    }

    /**
     * 初始化title 和 action事件
     */
    protected void initTitle() {
        final MyTitle title = (MyTitle) findViewById(R.id.my_title);
        title.setTitleStyle(MyTitle.TitleStyle.RIGHT_ICON_L_R);

        title.setOnLeftActionClickListener(new MyTitle.OnLeftActionClickListener() {
            @Override
            public void onClick() {
                finish();
            }
        });

        title.setOnRightActionClickListener(new MyTitle.OnRightActionClickListener() {
            @Override
            public void onRightLClick() {
                mQueue.add(doCollect(1, knowId, AppContext.now_session_id));
            }

            @Override
            public void onRightRClick() {
                //弹出分享界面
                //todo 分享内容
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("*/*");
                intent.putExtra(Intent.EXTRA_SUBJECT, "消防咨询-" + getIntent().getStringExtra("title"));
                intent.putExtra(Intent.EXTRA_TEXT, getIntent().getStringExtra("content"));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(Intent.createChooser(intent, getTitle()));

            }
        });
    }

    private TextView titleView;
    private TextView timeView;
    private WebView contentView;
    //所需积分
    private TextView scoreNeed;

    Button downBtn;

    protected void initView() {
        titleView = (TextView) findViewById(R.id.title);
        timeView = (TextView) findViewById(R.id.create_time);
        contentView = (WebView) findViewById(R.id.content);
        //所需积分
        scoreNeed = (TextView) findViewById(R.id.download_file_score_needed);

        downBtn = (Button) findViewById(R.id.download_btn);

        WebSettings settings = contentView.getSettings();
        //WebView启用Javascript脚本执行
        settings.setJavaScriptEnabled(true);//是否允许javascript脚本
        settings.setJavaScriptCanOpenWindowsAutomatically(true);//是否允许页面弹窗

        settings.setBuiltInZoomControls(true);
        settings.setPluginState(WebSettings.PluginState.ON);

        //下载按钮监听
        downBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //设置背景颜色变暗
                final WindowManager.LayoutParams lp =
                        KnowledgeItemDetailActivity.this.getWindow().getAttributes();
                lp.alpha = 0.3f;
                KnowledgeItemDetailActivity.this.getWindow().setAttributes(lp);

                //todo 弹出下载提示框
                PopActionUtil popActionUtil = PopActionUtil.getInstance(KnowledgeItemDetailActivity.this);
                PopupWindow popupWindow = popActionUtil.getDownloadPopActionMenu();
                popupWindow.showAtLocation(downBtn.getRootView(), Gravity.CENTER, 0, 0);

                popActionUtil.setActionListener(new PopActionListener() {
                    @Override
                    public void onAction(String action) {
                        switch (action) {
                            case "确定":
                                Toast.makeText(KnowledgeItemDetailActivity.this, "确定", Toast.LENGTH_LONG).show();
                                downloadBZGFFile(titleView.getText().toString(), pdfFileUrl);
                                break;
                            case "取消":
                                Toast.makeText(KnowledgeItemDetailActivity.this, "取消", Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                break;
                        }
                        lp.alpha = 1f;
                        KnowledgeItemDetailActivity.this.getWindow().setAttributes(lp);
                    }

                    @Override
                    public void onDismiss() {

                    }
                });
            }
        });
    }

    protected void downloadBZGFFile(String name, String pdf_id) {
        StringBuilder fNameBuilder = new StringBuilder(name);
        switch (type) {
            case "3":
                fNameBuilder.append(".pdf");
                break;
            default:
                break;
        }
        DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        Uri uri;
        uri = Uri.parse(BaseURLUtil.getKnowledgeFileUrl(pdf_id));
//        Uri uri = Uri.parse("http://tingge.5nd.com/20060919/2015/2015-7-8/67322/1.Mp3");
        DownloadManager.Request request = new DownloadManager.Request(uri);

        //设置允许使用的网络类型，这里是移动网络和wifi都可以
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);

        //禁止发出通知，既后台下载，如果要使用这一句必须声明一个权限：android.permission.DOWNLOAD_WITHOUT_NOTIFICATION
        //request.setShowRunningNotification(false);

        //不显示下载界面
        //request.setVisibleInDownloadsUi(false);
        /*设置下载后文件存放的位置,如果sdcard不可用，那么设置这个将报错，因此最好不设置如果sdcard可用，下载后的文件        在/mnt/sdcard/Android/data/packageName/files目录下面，如果sdcard不可用,设置了下面这个将报错，不设置，下载后的文件在/cache这个  目录下面*/
        request.setDestinationInExternalFilesDir(this, null, fNameBuilder.toString());

        long id = downloadManager.enqueue(request);
        //TODO 把id保存好，在接收者里面要用，最好保存在Preferences里面
    }

    /**
     * todo 收藏
     */
    protected StringRequest doCollect(int type, String knowId, String sId) {
        return new StringRequest(BaseURLUtil.collectOrDecollectKno(type, knowId, sId), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getString("result").equals("Y")) {
                        Toast.makeText(KnowledgeItemDetailActivity.this, "收藏成功！", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(KnowledgeItemDetailActivity.this, volleyError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 详情
     */
    private StringRequest getKonDetail(String knowId) {
        return new StringRequest(BaseURLUtil.getKnowlegeDetail(knowId), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.optString("result").equals("Y")) {
                        JSONObject obj = jsonObject.getJSONObject("datas").getJSONObject("listData");
                        type = obj.optString("docType");
                        pdfFileUrl = obj.optString("docLoacl");
                        contentView.loadUrl(BaseURLUtil.getKnowledgeFileUrl(pdfFileUrl));
                        scoreNeed.setText(obj.optString("avrScor"));
                        timeView.setText(obj.optString("upDate").substring(0, 10) + "上传   " + obj.optString("downTimes") + "下载");
                        switch (type) {
                            case "3":
                                titleView.setText(obj.optString("fileName") + ".pdf");
                                break;
                            default:
                                break;
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
