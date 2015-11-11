package com.ymt.demo1.plates.consultCato;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
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
 * Created by Dan on 2015/5/4
 */
public class ConsultDetailActivity extends BaseActivity {
    private ConsultItem catoItem;
    private RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consult_detail);
        mQueue = Volley.newRequestQueue(this);
        catoItem = getIntent().getParcelableExtra("item");
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
        if (catoItem == null) {
            return;
        }

        title.setOnRightActionClickListener(new MyTitle.OnRightActionClickListener() {
            @Override
            public void onRightLClick() {
                //// TODO: 2015/11/5 收藏
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
    }

    /**
     * 获得内容,初始化控件
     */
    protected void initView() {
        TextView title = (TextView) findViewById(R.id.subject);
        TextView content = (TextView) findViewById(R.id.content);
        if (catoItem != null) {
            title.setText(catoItem.getTitle());
            content.setText(Html.fromHtml(catoItem.getItContent()));
        }

    }

    protected StringRequest doCollect(String consultId, String sId) {
        //todo 收藏
        return new StringRequest(BaseURLUtil.collectConsult(consultId, sId), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getJSONObject("datas").optInt("listData") == 1) {
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
}
