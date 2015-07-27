package com.ymt.demo1.plates.hub;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ymt.demo1.R;
import com.ymt.demo1.beams.hub.HubPlate;
import com.ymt.demo1.customViews.MyTitle;
import com.ymt.demo1.main.AppContext;
import com.ymt.demo1.main.BaseFloatActivity;
import com.ymt.demo1.main.BaseURLUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dan on 2015/7/27
 */
public class DoPostActivity extends BaseFloatActivity {
    private Spinner spinner1;
    private Spinner spinner2;
    private EditText titleTxt;
    private EditText contentTxt;
    private List<HubPlate> parentList = new ArrayList<>();
    private List<HubPlate> childList = new ArrayList<>();
    private RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mQueue = Volley.newRequestQueue(this);
        setContentView(R.layout.activity_do_post);
        parentList = getIntent().getParcelableArrayListExtra("parent");
        childList = getIntent().getParcelableArrayListExtra("child");
        initTitle();
        initView();
    }

    /**
     * 初始化title 和 action事件
     */
    protected void initTitle() {
        final MyTitle title = (MyTitle) findViewById(R.id.my_title);
        title.setTitleStyle(MyTitle.TitleStyle.RIGHT_ICON_L);
        title.updateCenterTitle("发帖");     //设置title
        title.updateLeftLIcon2Txt("发表");
        title.setOnLeftActionClickListener(new MyTitle.OnLeftActionClickListener() {
            @Override
            public void onClick() {
                finish();
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });

        title.setOnRightActionClickListener(new MyTitle.OnRightActionClickListener() {
            @Override
            public void onRightLClick() {
                // 发表
                String title = titleTxt.getText().toString();
                String content = contentTxt.getText().toString();
                if ((!TextUtils.isEmpty(title)) && (!TextUtils.isEmpty(content))) {
                    for (HubPlate plate : childList) {
                        if (spinner2.getSelectedItem().equals(plate.getName())) {
                            mQueue.add(doPost(AppContext.now_user_name, plate.getFid(), title, content, 1));
                        }
                    }
                } else {
                    Toast.makeText(DoPostActivity.this, "输入有误，请重新输入!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onRightRClick() {

            }
        });

    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    protected void initView() {
        spinner1 = (Spinner) findViewById(R.id.spinner_1);
        spinner2 = (Spinner) findViewById(R.id.spinner_2);
        titleTxt = (EditText) findViewById(R.id.post_title);
        contentTxt = (EditText) findViewById(R.id.post_content);

        List<String> pPlates = new ArrayList<>();
        for (HubPlate plate : parentList) {
            pPlates.add(plate.getName());
        }
        final ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, android.R.id.text1, pPlates);
        spinner1.setAdapter(adapter1);

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                List<String> cPlates = new ArrayList<>();
                for (HubPlate plate : childList) {
                    if (plate.getFup() == parentList.get(position).getFid()) {
                        cPlates.add(plate.getName());
                    }
                }
                final ArrayAdapter adapter2 = new ArrayAdapter<>(DoPostActivity.this, R.layout.support_simple_spinner_dropdown_item, android.R.id.text1, cPlates);
                spinner2.setAdapter(adapter2);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    protected StringRequest doPost(String userName, int fid, String title, String content, int reqType) {
        return new StringRequest(BaseURLUtil.postHubSubject(userName, fid, title, content, reqType), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject object = new JSONObject(s);
                    if (object.getInt("retCode") == 0) {
                        Toast.makeText(DoPostActivity.this, "发帖成功!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(DoPostActivity.this, "网络问题，请稍后再试!", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(DoPostActivity.this, "网络问题，请稍后再试!", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
