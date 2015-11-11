package com.ymt.demo1.plates.eduPlane.pastExams;

import android.app.DownloadManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import com.ymt.demo1.beams.edu.PastExamItem;
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
public class PastExamDetailActivity extends BaseActivity {
    private String historyId;
    private RequestQueue mQueue;
    private String examPdfId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        historyId = getIntent().getStringExtra("historyId");
        mQueue = Volley.newRequestQueue(this);
        setContentView(R.layout.activity_download_layout_pdf);
        initTitle();
        initView();
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
                //收藏
                mQueue.add(doCollect(historyId, AppContext.now_session_id));
            }

            @Override
            public void onRightRClick() {
                //弹出分享界面
                //todo 分享内容
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("*/*");
                intent.putExtra(Intent.EXTRA_SUBJECT, "消防历年真题-" + getIntent().getStringExtra("title"));
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

    protected void initView() {
        final Button downBtn = (Button) findViewById(R.id.download_btn);
        titleView = (TextView) findViewById(R.id.title);
        timeView = (TextView) findViewById(R.id.create_time);
        contentView = (WebView) findViewById(R.id.content);

        scoreNeed = (TextView) findViewById(R.id.download_file_score_needed);

        downBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //下载事件
                //设置背景颜色变暗
                final WindowManager.LayoutParams lp =
                        PastExamDetailActivity.this.getWindow().getAttributes();
                lp.alpha = 0.3f;
                PastExamDetailActivity.this.getWindow().setAttributes(lp);

                //todo 弹出下载提示框
                PopActionUtil popActionUtil = PopActionUtil.getInstance(PastExamDetailActivity.this);
                PopupWindow popupWindow = popActionUtil.getDownloadPopActionMenu();
                popupWindow.showAtLocation(downBtn.getRootView(), Gravity.CENTER, 0, 0);

                popActionUtil.setActionListener(new PopActionListener() {
                    @Override
                    public void onAction(String action) {
                        switch (action) {
                            case "确定":
                                Toast.makeText(PastExamDetailActivity.this, "确定", Toast.LENGTH_LONG).show();
                                if (AppContext.internetAvialable() && examPdfId != null) {
                                    downloadBZGFFile(titleView.getText().toString() + ".pdf", examPdfId);
                                }
                                break;
                            case "取消":
                                Toast.makeText(PastExamDetailActivity.this, "取消", Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                break;
                        }
                        lp.alpha = 1f;
                        PastExamDetailActivity.this.getWindow().setAttributes(lp);
                    }

                    @Override
                    public void onDismiss() {

                    }
                });
            }
        });

        mQueue.add(getExamDetail(historyId));

    }

    protected void downloadBZGFFile(String name, String pdf_id) {
        //下载
        DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(BaseURLUtil.getEduPdf(pdf_id));
//        Uri uri = Uri.parse("http://tingge.5nd.com/20060919/2015/2015-7-8/67322/1.Mp3");
        DownloadManager.Request request = new DownloadManager.Request(uri);

        //设置允许使用的网络类型，这里是移动网络和wifi都可以
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);

        //禁止发出通知，既后台下载，如果要使用这一句必须声明一个权限：android.permission.DOWNLOAD_WITHOUT_NOTIFICATION
        //request.setShowRunningNotification(false);

        //不显示下载界面
        //request.setVisibleInDownloadsUi(false);
        /*设置下载后文件存放的位置,如果sdcard不可用，那么设置这个将报错，因此最好不设置如果sdcard可用，下载后的文件        在/mnt/sdcard/Android/data/packageName/files目录下面，如果sdcard不可用,设置了下面这个将报错，不设置，下载后的文件在/cache这个  目录下面*/
        request.setDestinationInExternalFilesDir(this, null, name);

        long id = downloadManager.enqueue(request);
        //TODO 把id保存好，在接收者里面要用，最好保存在Preferences里面
    }

    protected StringRequest doCollect(String examId, String sId) {
        //todo 收藏
        return new StringRequest(BaseURLUtil.collectPastExam(examId, sId), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getString("result").equals("Y")) {
                        Toast.makeText(PastExamDetailActivity.this, "收藏成功！", Toast.LENGTH_SHORT).show();
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

    private StringRequest getExamDetail(final String examId) {
        //获取试题详情
        return new StringRequest(BaseURLUtil.getPastExamDetailById(examId), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONObject object = jsonObject.getJSONObject("datas").getJSONObject("listData");
                    ExamInfo examInfo = new ExamInfo();
                    examInfo.setColumnType(object.optString("columnType"));
                    examInfo.setDescs(object.optString("descs"));
                    examInfo.setDownNum(object.optInt("downNum"));
                    examInfo.setHistoryId(object.optString("historyId"));
                    examInfo.setModel(object.optInt("model"));
                    examInfo.setOpTime(object.optString("opTime"));
                    examInfo.setReplays(object.optInt("replays"));
                    examInfo.setScore(object.optInt("score"));
                    examInfo.setSubId(object.optInt("subId"));
                    examInfo.setTitle(object.optString("title"));
                    examInfo.setUrl(object.optString("url"));
                    examInfo.setViews(object.optInt("views"));
                    examInfo.setYuer(object.optString("yuer"));
                    //// TODO: 2015/11/5 更新试卷信息
                    titleView.setText(examInfo.getTitle());
                    timeView.setText(examInfo.getOpTime() + "上传");
                    scoreNeed.setText(examInfo.getScore() + "分");
                    loadPDF2(contentView, BaseURLUtil.getEduPdf(examInfo.getUrl()));
                    examPdfId = examInfo.getUrl();
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

    class ExamInfo {
        private String columnType;
        private String descs;
        private int downNum;
        private String historyId;
        private int model;
        private String opTime;
        private int replays;
        private int score;
        private int subId;
        private String title;
        private String url;
        private int views;
        private String yuer;

        public String getColumnType() {
            return columnType;
        }

        public void setColumnType(String columnType) {
            this.columnType = columnType;
        }

        public String getDescs() {
            return descs;
        }

        public void setDescs(String descs) {
            this.descs = descs;
        }

        public int getDownNum() {
            return downNum;
        }

        public void setDownNum(int downNum) {
            this.downNum = downNum;
        }

        public String getHistoryId() {
            return historyId;
        }

        public void setHistoryId(String historyId) {
            this.historyId = historyId;
        }

        public int getModel() {
            return model;
        }

        public void setModel(int model) {
            this.model = model;
        }

        public String getOpTime() {
            return opTime;
        }

        public void setOpTime(String opTime) {
            this.opTime = opTime;
        }

        public int getReplays() {
            return replays;
        }

        public void setReplays(int replays) {
            this.replays = replays;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public int getSubId() {
            return subId;
        }

        public void setSubId(int subId) {
            this.subId = subId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getViews() {
            return views;
        }

        public void setViews(int views) {
            this.views = views;
        }

        public String getYuer() {
            return yuer;
        }

        public void setYuer(String yuer) {
            this.yuer = yuer;
        }
    }

    //方法1:利用设备自带浏览器打开pdf
    private void loadPDF1(WebView mWebView, String pdfUrl) {
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.requestFocus();
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebView.loadUrl("http://docs.google.com/gview?embedded=true&url=" + pdfUrl);

    }

    //方法2:利用Google服务解析后再在mWebView中打开pdf
    private void loadPDF2(WebView mWebView, String pdfUrl) {
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.requestFocus();
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        String data = "<iframe src='http://docs.google.com/gview?embedded=true&url=" + pdfUrl + "'" + " width='100%' height='100%' style='border: none;'></iframe>";
        mWebView.loadData(data, "text/html", "UTF-8");

    }

}
