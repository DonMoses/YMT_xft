package com.ymt.demo1.plates.consultCato;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
import com.ymt.demo1.beams.consult_cato.SearchedConsult;
import com.ymt.demo1.customViews.MyTitle;
import com.ymt.demo1.main.BaseURLUtil;
import com.ymt.demo1.main.search.SearchActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by Dan on 2015/5/4
 */
public class CatoConsultListActivity extends BaseActivity {
    private String consult_key_word;
    private int startIndex;
    private SearchedConsultAdapter adapter;
    private RequestQueue mQueue;
    private String typeCode;
    private PullToRefreshListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        mQueue = Volley.newRequestQueue(this);
        consult_key_word = intent.getStringExtra("search_key_word");
        typeCode = intent.getStringExtra("code");
        mQueue.add(getConsultRequest(5, startIndex, typeCode, ""));
//        Log.e("TAG", ">>>>>>>>>>>>>>>>>" + typeCode);
        startIndex = 1;
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

        title.updateCenterTitle(consult_key_word);     //设置title
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
                startActivity(new Intent(CatoConsultListActivity.this, SearchActivity.class));
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
        progressBar.setIndeterminate(false);
        listView.setEmptyView(progressBar);
        adapter = new SearchedConsultAdapter(this);
        listView.setAdapter(adapter);

        //todo 从数据库获取已保存的咨询
        List<SearchedConsult> mConsultList = DataSupport.where("consult_key_word = ?", consult_key_word).find(SearchedConsult.class);
        adapter.setList(mConsultList);

        //设置listView点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Log.e("TAG", ">>>>>>>>>>>>>>search_item>>>>" + position);
                //todo 跳转到具体内容界面
                Intent intent = new Intent(CatoConsultListActivity.this, ConsultDetailActivity.class);
                intent.putExtra("search_key_word", consult_key_word);
                intent.putExtra("title", ((SearchedConsult) parent.getAdapter().getItem(position)).getArticle_title());
                intent.putExtra("content", ((SearchedConsult) parent.getAdapter().getItem(position)).getArticle_content());
                startActivity(intent);
            }
        });

        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                //下拉刷新
                startIndex = 1;
                DataSupport.deleteAll(SearchedConsult.class);
                mQueue.add(getConsultRequest(5, startIndex, typeCode, ""));
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                //上拉加载更多
                startIndex++;
                mQueue.add(getConsultRequest(5, startIndex, typeCode, ""));
            }
        });
    }

    /**
     * 根据关键字从网络获取咨询、并保存到数据库
     */
    protected StringRequest getConsultRequest(int pageSize, int start, String typeCode, String searchWhat) {
//        Log.e("TAG", ">>>>>>>>url>>>>>>>>>" + BaseURLUtil.doTypeContentListAction(pageSize, start, typeCode, searchWhat));
        return new StringRequest(BaseURLUtil.doTypeContentListAction(pageSize, start, typeCode, searchWhat), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
//                Log.e("TAG", ">>>>>>>>>>>>>>>>>" + s);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getString("result").equals("Y")) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("datas");
                        JSONArray jsonArray = jsonObject1.getJSONArray("listData");
                        int length = jsonArray.length();

                        for (int i = 0; i < length; i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            SearchedConsult consult = new SearchedConsult();
                            String id = object.getString("id");
                            consult.setArticle_id(id);
                            consult.setCreate_time(object.optString("create_time"));
                            consult.setArticle_content(object.optString("content"));
                            consult.setArticle_title(object.optString("article_title"));
                            consult.setConsult_key_word(consult_key_word);
                            consult.setFk_expert_id(object.optString("fk_expert_id"));
                            consult.setHitnum(object.optString("hitnum"));
                            int savedSize = DataSupport.where("article_id = ?", id).find(SearchedConsult.class).size();
//                            Log.e("TAG", ">>>>>>>>>savedSize>>>>>>>" + savedSize);
                            if (savedSize == 0) {
                                consult.save();
                            } else {
                                consult.updateAll("article_id = ?", id);
                            }
                        }
                        //todo 从数据库获取已保存的咨询
                        List<SearchedConsult> mConsultList = DataSupport.where("consult_key_word = ?", consult_key_word).find(SearchedConsult.class);
                        adapter.setList(mConsultList);
                        listView.onRefreshComplete();
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
