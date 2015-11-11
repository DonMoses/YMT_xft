package com.ymt.demo1.plates.exportConsult;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ymt.demo1.R;
import com.ymt.demo1.baseClasses.BaseActivity;
import com.ymt.demo1.beams.consult_cato.ConsultItem;
import com.ymt.demo1.customViews.MyTitle;
import com.ymt.demo1.utils.AppContext;
import com.ymt.demo1.utils.BaseURLUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Dan on 2015/7/5
 */
public class ConsultDetailActivity extends BaseActivity {
    private ConsultItem catoItem;
    private RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hot_rec_consult_detail);
        int cid = getIntent().getIntExtra("cid", 0);
        mQueue = Volley.newRequestQueue(this);
        mQueue.add(getConsultDetail(cid));
        initTitle();
    }

    protected void initTitle() {
        MyTitle myTitle = (MyTitle) findViewById(R.id.my_title);
        myTitle.setTitleStyle(MyTitle.TitleStyle.RIGHT_ICON_L_R);
        myTitle.setOnLeftActionClickListener(new MyTitle.OnLeftActionClickListener() {
            @Override
            public void onClick() {
                finish();
            }
        });
        myTitle.setOnRightActionClickListener(new MyTitle.OnRightActionClickListener() {
            @Override
            public void onRightLClick() {
                //todo 收藏
                mQueue.add(doCollect(String.valueOf(catoItem.getCid()), AppContext.now_session_id));
            }

            @Override
            public void onRightRClick() {
                //弹出分享界面
                //todo 分享内容
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("*/*");
                intent.putExtra(Intent.EXTRA_SUBJECT, "消防咨询-" + catoItem.getTitle());
                intent.putExtra(Intent.EXTRA_TEXT, catoItem.getItContent());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(Intent.createChooser(intent, getTitle()));
            }
        });
        myTitle.updateCenterTitle("");
    }

    protected StringRequest doCollect(String consultId, String sId) {
        //todo 收藏
        return new StringRequest(BaseURLUtil.collectConsult(consultId, sId), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getString("result").equals("Y")) {
                        Toast.makeText(ConsultDetailActivity.this, "收藏成功！", Toast.LENGTH_SHORT).show();
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

    private StringRequest getConsultDetail(int cid) {
        //获取咨询内容
        return new StringRequest(BaseURLUtil.getConsultContent(cid), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject object = new JSONObject(s);
                    JSONObject obj = object.getJSONObject("datas")
                            .getJSONObject("listData");
                    ConsultItem item = new ConsultItem();
                    item.setTitle(obj.optString("title"));
                    item.setCreateTime(obj.optString("createTime"));
                    item.setExpertId(obj.optInt("expertId"));
                    item.setCid(obj.optInt("cid"));
                    item.setViews(obj.optInt("views"));
                    item.setItContent(obj.optString("itContent"));
                    item.setItTime(obj.optString("itTime"));
                    ((TextView) findViewById(R.id.consult_title)).setText(item.getTitle());
                    ((TextView) findViewById(R.id.consult_time)).setText(item.getCreateTime());
                    if (!TextUtils.isEmpty(obj.optString("itType"))) {
                        ((TextView) findViewById(R.id.consult_type)).setText("所属分类：" + obj.optString("itType"));
                    }
                    ((WebView) findViewById(R.id.consult_content))
                            .loadDataWithBaseURL(null, item.getItContent(), "text/html", "utf-8", null);
                    catoItem = item;

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
