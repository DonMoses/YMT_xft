package com.ymt.demo1.plates.consultCato;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.ymt.demo1.R;
import com.ymt.demo1.adapter.SearchedConsultAdapter;
import com.ymt.demo1.baseClasses.BaseActivity;
import com.ymt.demo1.beams.consult_cato.ConsultItem;
import com.ymt.demo1.customViews.MyTitle;
import com.ymt.demo1.main.search.FullSearchActivity;
import com.ymt.demo1.utils.AppContext;
import com.ymt.demo1.utils.BaseURLUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by Dan on 2015/5/4
 */
public class CatoConsultListActivity extends BaseActivity {
    private int codeId;
    private int startIndex;
    private SearchedConsultAdapter adapter;
    private RequestQueue mQueue;
    private String codeValue;
    private PullToRefreshListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        mQueue = Volley.newRequestQueue(this);
        codeId = intent.getIntExtra("codeId", 0);
        codeValue = intent.getStringExtra("codeValue");
        startIndex = 1;
        mQueue.add(getConsultRequest(startIndex, codeId));
        setContentView(R.layout.activity_cato_consult_list);
        initTitle();
        initView();
    }

    /**
     * 初始化title 和 action事件
     */
    protected void initTitle() {
        final MyTitle title = (MyTitle) findViewById(R.id.my_title);
        title.setTitleStyle(MyTitle.TitleStyle.RIGHT_ICON_L);

        title.updateCenterTitle(codeValue);     //设置title
        title.setOnLeftActionClickListener(new MyTitle.OnLeftActionClickListener() {
            @Override
            public void onClick() {
                finish();
            }
        });

        title.setOnRightActionClickListener(new MyTitle.OnRightActionClickListener() {
            @Override
            public void onRightLClick() {
                //打开搜索界面
                startActivity(new Intent(CatoConsultListActivity.this, FullSearchActivity.class));
            }

            @Override
            public void onRightRClick() {
                //此视图，右边只包含L按钮

            }
        });
    }

    /**
     * 初始化控件（ListView）【关键字搜索结果列表】
     */
    protected void initView() {
        listView = (PullToRefreshListView) findViewById(R.id.searched_consult_list_view);

        //延时进度条
        ProgressBar progressBar = new ProgressBar(this);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(AppContext.screenWidth / 8, AppContext.screenWidth / 8);
        progressBar.setLayoutParams(layoutParams);
        progressBar.setIndeterminate(false);
        listView.setEmptyView(progressBar);
        adapter = new SearchedConsultAdapter(this);
        listView.setAdapter(adapter);

        //todo 从数据库获取已保存的咨询
        List<ConsultItem> mConsultList = DataSupport.where("codeId = ?", String.valueOf(codeId)).find(ConsultItem.class);
        adapter.setList(mConsultList);

        //设置listView点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //todo 跳转到具体内容界面
                Intent intent = new Intent(CatoConsultListActivity.this, ConsultDetailActivity.class);
                intent.putExtra("item", ((ConsultItem) parent.getAdapter().getItem(position)));
                startActivity(intent);
            }
        });

        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                //下拉刷新
                if (AppContext.internetAvialable()) {
                    startIndex = 1;
                    DataSupport.deleteAll(ConsultItem.class);
                    mQueue.add(getConsultRequest(startIndex, codeId));
                }

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                //上拉加载更多
                if (AppContext.internetAvialable()) {
                    startIndex++;
                    mQueue.add(getConsultRequest(startIndex, codeId));
                }

            }
        });
    }

    /**
     * 根据关键字从网络获取咨询、并保存到数据库
     */
    protected StringRequest getConsultRequest(int start, int... typeCode) {
        return new StringRequest(BaseURLUtil.getTypedCatoList(start, typeCode), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getString("result").equals("Y")) {
                        JSONArray jsonArray = jsonObject.getJSONObject("datas").getJSONArray("listData");
                        int length = jsonArray.length();

                        for (int i = 0; i < length; i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            ConsultItem consult = new ConsultItem();
                            consult.setCid(object.optInt("cid"));
                            consult.setCreateTime(object.optString("createTime"));
                            consult.setExpertId(object.optInt("expertId"));
                            consult.setItContent(object.optString("itContent"));
                            consult.setItTime(object.optString("itTime"));
                            consult.setTitle(object.optString("title"));
                            consult.setViews(object.optInt("views"));
                            consult.setCodeId(codeId);
                            int savedSize = DataSupport.where("cid = ?", String.valueOf(consult.getCid())).find(ConsultItem.class).size();
                            if (savedSize == 0) {
                                consult.save();
                            } else {
                                consult.updateAll("cid = ?", String.valueOf(consult.getCid()));
                            }
                        }
                        //todo 从数据库获取已保存的咨询
                        List<ConsultItem> mConsultList = DataSupport.where("codeId = ?", String.valueOf(codeId)).find(ConsultItem.class);
                        adapter.setList(mConsultList);
                        listView.onRefreshComplete();
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
