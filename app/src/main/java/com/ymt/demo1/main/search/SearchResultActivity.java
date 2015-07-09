package com.ymt.demo1.main.search;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.ymt.demo1.R;
import com.ymt.demo1.adapter.ExpertListAdapter;
import com.ymt.demo1.adapter.KnowledgeItemAdapter;
import com.ymt.demo1.adapter.SearchedConsultAdapter;
import com.ymt.demo1.adapter.VideoListAdapter;
import com.ymt.demo1.beams.SearchString;
import com.ymt.demo1.beams.consult_cato.SearchedConsult;
import com.ymt.demo1.beams.expert_consult.Expert;
import com.ymt.demo1.beams.knowledge.KnowledgeItemBZGF;
import com.ymt.demo1.beams.knowledge.KnowledgeItemKYWX;
import com.ymt.demo1.beams.knowledge.KnowledgeVideo;
import com.ymt.demo1.main.AppContext;
import com.ymt.demo1.main.BaseFloatActivity;
import com.ymt.demo1.main.BaseURLUtil;
import com.ymt.demo1.plates.exportConsult.ExpertInfoActivity;
import com.ymt.demo1.plates.knowledge.KnowledgeItemListViewFragment;
import com.ymt.demo1.plates.knowledge.KnowledgeVideoFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dan on 2015/7/8
 */
public class SearchResultActivity extends BaseFloatActivity {
    private int updateIndex;
    SharedPreferences sharedPreferences;
    private int start;
    private int pageSize;
    private ExpertListAdapter expertListAdapter;
    private PullToRefreshGridView expertGirdView;
    private PullToRefreshListView normalListView;
    private RequestQueue mQueue;
    private int typePos;
    private String keyW;
    private Spinner spinner;
    private EditText searchTxt;
    private ImageButton searchBtn;
    private List<Expert> experts;
    private LinearLayout rootLayout;

    private SearchedConsultAdapter searchedConsultAdapter;
    private KnowledgeItemAdapter knowledgeNormalAdapter;
    private VideoListAdapter videoListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences(SearchActivity.SEARCH_PREFERENCES, MODE_PRIVATE);
        updateIndex = sharedPreferences.getInt(SearchActivity.UPDATE_SEARCH_INDEX, 0);
        mQueue = Volley.newRequestQueue(this);
        //创建时搜索类型和关键字
        typePos = getIntent().getIntExtra("position", 0);
        keyW = getIntent().getStringExtra("keyword");
        setContentView(R.layout.activity_search_result);
        start = 1;
        pageSize = 10;
        initBaseView();
        if (typePos == 1) {
            initGridView();
        } else {
            initListView();
        }
    }

    private int size;


    protected void initBaseView() {
        rootLayout = (LinearLayout) findViewById(R.id.root_layout);
        spinner = (Spinner) findViewById(R.id.search_spinner);
        ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new String[]{"全部", "专家", "知识平台-科研", "知识平台-标准", "知识平台-视频", "咨询分类"});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setVisibility(View.VISIBLE);//设置默认显示

        searchTxt = (EditText) findViewById(R.id.search_edit_text);
        searchBtn = (ImageButton) findViewById(R.id.search_btn);

        spinner.setSelection(typePos);
        searchTxt.setText(keyW);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String inKw = searchTxt.getText().toString();
                if ((typePos != position) && (!TextUtils.isEmpty(inKw))) {
                    //类型改变,自动搜索
                    switch (position) {
                        case 0:
                            initListView();
                            break;
                        case 1:                 //搜索专家
                            initGridView();
                            mQueue.add(getExperts(pageSize, start, inKw));
                            break;
                        case 2:                 //知识平台-科研
                            initListView();
                            knowledgeNormalAdapter = new KnowledgeItemAdapter(SearchResultActivity.this, KnowledgeItemListViewFragment.KNOWLEDGE_KYWX);
                            normalListView.setAdapter(knowledgeNormalAdapter);
                            mQueue.add(getKywxList(pageSize, start, inKw));
                            break;
                        case 3:                 //知识平台-标准
                            initListView();
                            knowledgeNormalAdapter = new KnowledgeItemAdapter(SearchResultActivity.this, KnowledgeItemListViewFragment.KNOWLEDGE_BZGF);
                            normalListView.setAdapter(knowledgeNormalAdapter);
                            mQueue.add(getBzgfList(pageSize, start, inKw));
                            break;
                        case 4:                 //知识平台-视频
                            initListView();
                            videoListAdapter = new VideoListAdapter(SearchResultActivity.this, AppContext.screenWidth);
                            normalListView.setAdapter(videoListAdapter);
                            mQueue.add(getKnowledgeVideo(pageSize, start, inKw));
                            break;
                        case 5:                 //咨询分类
                            initListView();
                            searchedConsultAdapter = new SearchedConsultAdapter(SearchResultActivity.this);
                            normalListView.setAdapter(searchedConsultAdapter);
                            mQueue.add(getConsultRequest(pageSize, start, "", inKw));
                            break;
                        default:
                            break;
                    }

                }
                typePos = position;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final List<SearchString> searchedStrs = DataSupport.findAll(SearchString.class);
        size = searchedStrs.size();
        final ArrayList<String> searched = new ArrayList<>();
        for (int i = 0; i < searchedStrs.size(); i++) {
            searched.add(searchedStrs.get(i).getSearchedString());
        }

         /*
        searchBtn 事件
         */
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inKw = searchTxt.getText().toString();
                if (!TextUtils.isEmpty(inKw)) {
                    //更新数据
                    if (!searched.contains(inKw)) {
                        //获取输入框内容，搜索内容，加入搜索数据库表
                        if (size >= 10) {
                            ContentValues values = new ContentValues();
                            values.put("searchedstring", inKw);

                            //更新index，则下次输入后更新到上一次的下一个坐标
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            DataSupport.update(SearchString.class, values, updateIndex + 1);
                            updateIndex++;
                            if (updateIndex > 10) {
                                updateIndex = 1;
                            }
                            editor.putInt(SearchActivity.UPDATE_SEARCH_INDEX, updateIndex);
                            editor.apply();
                        } else {
                            saveString(inKw);
                        }

                        searched.add(inKw);

                    }

                    int pos = spinner.getSelectedItemPosition();
                    if (!keyW.equals(inKw)) {
                        switch (pos) {
                            case 0:
                                initListView();
                                break;
                            case 1:                 //搜索专家
                                initGridView();
                                mQueue.add(getExperts(pageSize, start, inKw));
                                break;
                            case 2:                 //知识平台-科研
                                initListView();
                                mQueue.add(getKywxList(pageSize, start, inKw));
                                break;
                            case 3:                 //知识平台-标准
                                initListView();
                                mQueue.add(getBzgfList(pageSize, start, inKw));
                                break;
                            case 4:                 //知识平台-视频
                                initListView();
                                mQueue.add(getKnowledgeVideo(pageSize, start, inKw));
                                break;
                            case 5:                 //咨询分类
                                initListView();
                                mQueue.add(getConsultRequest(pageSize, start, "", inKw));
                                break;
                            default:
                                break;
                        }
                    }

                } else {
                    Toast.makeText(SearchResultActivity.this, "请输入搜索关键词...", Toast.LENGTH_SHORT).show();
                }

                //清空输入内容， 输入框改变为不聚焦
//                searchTxt.setText(null);
                searchTxt.clearFocus();
                keyW = inKw;
            }
        });
        searchBtn.callOnClick();
        ProgressBar progressBar = new ProgressBar(SearchResultActivity.this);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        progressBar.setLayoutParams(params);
        if (rootLayout.getChildAt(1) instanceof PullToRefreshGridView) {
            ((PullToRefreshGridView) rootLayout.getChildAt(1)).setEmptyView(progressBar);
        } else if (rootLayout.getChildAt(1) instanceof PullToRefreshListView) {
            ((PullToRefreshListView) rootLayout.getChildAt(1)).setEmptyView(progressBar);
        }
    }

    protected void initGridView() {
        expertGirdView = (PullToRefreshGridView) LayoutInflater.from(this).inflate(R.layout.search_pull_grid_view, null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        expertGirdView.setLayoutParams(params);
        expertGirdView.setPadding(6, 6, 6, 6);
        if (rootLayout.getChildCount() == 2) {
            rootLayout.removeViewAt(1);
        }
        rootLayout.addView(expertGirdView);
        expertListAdapter = new ExpertListAdapter(this, AppContext.screenWidth);
        expertGirdView.setAdapter(expertListAdapter);
        experts = new ArrayList<>();
        expertListAdapter.setExperts(experts);
        expertGirdView.onRefreshComplete();

        expertGirdView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
                start = 1;
                experts.clear();
                DataSupport.deleteAll(Expert.class);
                expertListAdapter.setExperts(experts);
                String sKw = searchTxt.getText().toString();
                mQueue.add(getExperts(pageSize, start, sKw));
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
                start++;
                String sKw = searchTxt.getText().toString();
                mQueue.add(getExperts(pageSize, start, sKw));
            }
        });

        expertGirdView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Expert expert = (Expert) expertGirdView.getRefreshableView().getAdapter().getItem(position);
                Intent intent = new Intent(SearchResultActivity.this, ExpertInfoActivity.class);
                intent.putExtra("expert_info", expert);
                startActivity(intent);
            }
        });

        //进入时，根据关键字自动搜索
        if (!TextUtils.isEmpty(keyW)) {
            mQueue.add(getExperts(pageSize, start, keyW));
        }

    }

    protected void initListView() {
        normalListView = (PullToRefreshListView) findViewById(R.id.result_list_view);
        if (normalListView == null) {
            normalListView = (PullToRefreshListView) LayoutInflater.from(this).inflate(R.layout.search_pull_list_view, null);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            normalListView.setLayoutParams(params);
            normalListView.setMode(PullToRefreshBase.Mode.BOTH);
            normalListView.setPadding(6, 6, 6, 6);
            if (rootLayout.getChildCount() == 2) {
                rootLayout.removeViewAt(1);
            }
            rootLayout.addView(normalListView);
        }
        normalListView.setDividerPadding(1);

        normalListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                start = 1;
                int pos = spinner.getSelectedItemPosition();
                String inKw = searchTxt.getText().toString();
                if (!keyW.equals(inKw)) {
                    switch (pos) {
                        case 0:
                            initListView();
                            break;
                        case 1:                 //搜索专家
                            initGridView();
                            mQueue.add(getExperts(pageSize, start, inKw));
                            break;
                        case 2:                 //知识平台-科研
                            initListView();
                            mQueue.add(getKywxList(pageSize, start, inKw));
                            break;
                        case 3:                 //知识平台-标准
                            initListView();
                            mQueue.add(getBzgfList(pageSize, start, inKw));
                            break;
                        case 4:                 //知识平台-视频
                            initListView();
                            mQueue.add(getKnowledgeVideo(pageSize, start, inKw));
                            break;
                        case 5:                 //咨询分类
                            initListView();
                            mQueue.add(getConsultRequest(pageSize, start, "", inKw));
                            break;
                        default:
                            break;
                    }
                }
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                start++;
                int pos = spinner.getSelectedItemPosition();
                String inKw = searchTxt.getText().toString();
                if (!keyW.equals(inKw)) {
                    switch (pos) {
                        case 0:
                            initListView();
                            break;
                        case 1:                 //搜索专家
                            initGridView();
                            mQueue.add(getExperts(pageSize, start, inKw));
                            break;
                        case 2:                 //知识平台-科研
                            initListView();
                            mQueue.add(getKywxList(pageSize, start, inKw));
                            break;
                        case 3:                 //知识平台-标准
                            initListView();
                            mQueue.add(getBzgfList(pageSize, start, inKw));
                            break;
                        case 4:                 //知识平台-视频
                            initListView();
                            mQueue.add(getKnowledgeVideo(pageSize, start, inKw));
                            break;
                        case 5:                 //咨询分类
                            initListView();
                            mQueue.add(getConsultRequest(pageSize, start, "", inKw));
                            break;
                        default:
                            break;
                    }
                }
            }
        });

    }

    public void saveString(String str) {
        if (str != null && !str.equals("")) {
            SearchString searchString = new SearchString();
            searchString.setSearchedString(str);
            searchString.save();            //加入数据库
            size++;
        }

    }

    /**
     * 专家列表
     */
    protected StringRequest getExperts(int pageSize, int start, String searchWho) {
        return new StringRequest(BaseURLUtil.doGetExpertList(pageSize, start, searchWho), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
//                Log.e("TAG", ">>>>>>>>>>>expert>>>>" + s);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getString("result").equals("Y")) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("datas");
                        JSONArray jsonArray = jsonObject1.getJSONArray("listData");
                        int length = jsonArray.length();
                        for (int i = 0; i < length; i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            Expert expert = new Expert();
                            String id = obj.optString("id");
                            expert.setThe_id(id);
                            expert.setCreate_time(obj.optString("create_time"));
                            expert.setBio(obj.optString("bio"));
                            expert.setCapacity(obj.optString("capacity"));
                            expert.setCount(obj.optString("count"));
                            expert.setExperience(obj.optString("experience"));
                            expert.setFk_user_id(obj.optString("fk_user_id"));
                            expert.setGood(obj.optString("good"));
                            expert.setHead_pic(obj.optString("head_pic"));
                            expert.setLevel(obj.optString("level"));
                            expert.setMajor_works(obj.optString("major_works"));
                            expert.setNote(obj.optString("note"));
                            expert.setRemark(obj.optString("remark"));
                            expert.setResume(obj.optString("resume"));
                            expert.setSocial_part_time(obj.optString("social_part_time"));
                            expert.setUser_name(obj.optString("user_name"));
                            expert.setWork_time(obj.optString("work_time"));
                            experts.add(expert);
                            expertListAdapter.setExperts(experts);
                        }
                        expertGirdView.onRefreshComplete();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    expertGirdView.onRefreshComplete();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                expertGirdView.onRefreshComplete();
            }
        });
    }


    /**
     * 标准规范
     */
    protected StringRequest getBzgfList(int pageSize, int startIndex, String searchWhat) {
//        Log.e("TAG", ">>>>>>>>>>>>>url>>>>>>>>>>" + BaseURLUtil.doGetKnowledgeAction(KNOWLEDGE_BZGF, pageSize, startIndex, searchWhat));
        return new StringRequest(BaseURLUtil.doGetKnowledgeAction(KnowledgeItemListViewFragment.KNOWLEDGE_BZGF, pageSize, startIndex, searchWhat), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                try {
                    JSONObject object = new JSONObject(s);
//                    Log.e("TAG", ">>>>>>>>>>>>>s>>>>>>>>>>" + s);

                    if (object.getString("result").equals("Y")) {
                        JSONObject jsonObject = object.getJSONObject("datas");
                        JSONArray jsonArray = jsonObject.getJSONArray("listData");
                        int length = jsonArray.length();
                        List<KnowledgeItemBZGF> knowledgeItemBZGFList = new ArrayList<>();
                        for (int i = 0; i < length; i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            KnowledgeItemBZGF knowledgeItemBZGF = new KnowledgeItemBZGF();
                            knowledgeItemBZGF.setHitnum(obj.optString("hitnum"));
                            knowledgeItemBZGF.setCreate_time(obj.optString("create_time"));
                            knowledgeItemBZGF.setArticle_title(obj.optString("article_title"));
                            knowledgeItemBZGF.setContent(obj.optString("content"));
                            knowledgeItemBZGF.setDowncount(obj.optString("downcount"));
                            knowledgeItemBZGF.setFiles(obj.optString("files"));
                            knowledgeItemBZGF.setFk_create_user_id(obj.optString("fk_create_user_id"));
                            knowledgeItemBZGF.setJzxf(obj.optString("jzxf"));
                            knowledgeItemBZGF.setMeta_keys(obj.optString("meta_keys"));
                            knowledgeItemBZGF.setStatus(obj.optString("status"));
                            String id = obj.optString("id");
                            knowledgeItemBZGF.setThe_id(id);
                            knowledgeItemBZGF.setScore(obj.optString("score"));
                            knowledgeItemBZGFList.add(knowledgeItemBZGF);
                            knowledgeNormalAdapter.setBZGFList(knowledgeItemBZGFList);
                        }
                        normalListView.onRefreshComplete();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    normalListView.onRefreshComplete();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                normalListView.onRefreshComplete();
            }
        });

    }

    /**
     * 科研文献
     */
    protected StringRequest getKywxList(int pageSize, int startIndex, String searchWhat) {
        return new StringRequest(BaseURLUtil.doGetKnowledgeAction(KnowledgeItemListViewFragment.KNOWLEDGE_KYWX, pageSize, startIndex, searchWhat), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject object = new JSONObject(s);
//                    Log.e("TAG", ">>>>>>>>>>>>>s>>>>>>>>>>" + s);

                    if (object.getString("result").equals("Y")) {
                        JSONObject jsonObject = object.getJSONObject("datas");
                        JSONArray jsonArray = jsonObject.getJSONArray("listData");
                        int length = jsonArray.length();
                        List<KnowledgeItemKYWX> knowledgeItemKYWXList = new ArrayList<>();
                        for (int i = 0; i < length; i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            KnowledgeItemKYWX knowledgeItemKYWX = new KnowledgeItemKYWX();
                            knowledgeItemKYWX.setHitnum(obj.optString("hitnum"));
                            knowledgeItemKYWX.setCreate_time(obj.optString("create_time"));
                            knowledgeItemKYWX.setArticle_title(obj.optString("article_title"));
                            knowledgeItemKYWX.setContent(obj.optString("content"));
                            knowledgeItemKYWX.setDowncount(obj.optString("downcount"));
                            knowledgeItemKYWX.setFiles(obj.optString("files"));
                            knowledgeItemKYWX.setFk_create_user_id(obj.optString("fk_create_user_id"));
                            knowledgeItemKYWX.setJzxf(obj.optString("jzxf"));
                            knowledgeItemKYWX.setMeta_keys(obj.optString("meta_keys"));
                            knowledgeItemKYWX.setStatus(obj.optString("status"));
                            knowledgeItemKYWX.setAuthor(obj.optString("author"));
                            knowledgeItemKYWX.setPdf_id(obj.optString("pdf_id"));
                            knowledgeItemKYWX.setIsFile(obj.optString("isfile"));
                            knowledgeItemKYWX.setAttribute(obj.optString("attribute"));

                            String id = obj.optString("id");
                            knowledgeItemKYWX.setThe_id(id);
                            knowledgeItemKYWX.setScore(obj.optString("score"));
                            knowledgeItemKYWXList.add(knowledgeItemKYWX);
                            knowledgeNormalAdapter.setKYWXList(knowledgeItemKYWXList);
                        }
                        normalListView.onRefreshComplete();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    normalListView.onRefreshComplete();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                normalListView.onRefreshComplete();
            }
        });

    }

    /**
     * 咨询分类
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

                        List<SearchedConsult> mConsultList = new ArrayList<>();
                        for (int i = 0; i < length; i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            SearchedConsult consult = new SearchedConsult();
                            String id = object.getString("id");
                            consult.setArticle_id(id);
                            consult.setCreate_time(object.optString("create_time"));
                            consult.setArticle_content(object.optString("content"));
                            consult.setArticle_title(object.optString("article_title"));
//                            consult.setConsult_key_word(consult_key_word);
                            consult.setFk_expert_id(object.optString("fk_expert_id"));
                            consult.setHitnum(object.optString("hitnum"));
                            mConsultList.add(consult);
                            searchedConsultAdapter.setList(mConsultList);
                        }
                        normalListView.onRefreshComplete();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    normalListView.onRefreshComplete();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                normalListView.onRefreshComplete();
            }
        });
    }

    /**
     * 知识平台-视频
     */
    protected StringRequest getKnowledgeVideo(int pageSize, int start, String searchWhat) {
        return new StringRequest(BaseURLUtil.doGetKnowledgeAction(KnowledgeVideoFragment.KNOWLEDGE_SPZL, pageSize, start, searchWhat), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getString("result").equals("Y")) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("datas");
                        JSONArray jsonArray = jsonObject1.getJSONArray("listData");
                        int length = jsonArray.length();

                        List<KnowledgeVideo> videoList = new ArrayList<>();
                        for (int i = 0; i < length; i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            KnowledgeVideo video = new KnowledgeVideo();
                            String id = obj.optString("id");
                            video.setCreate_time(obj.optString("create_time"));
                            video.setArticle_title(obj.optString("article_title"));
                            video.setContent(obj.optString("content"));
                            video.setDowncount(obj.optString("downcount"));
                            video.setFiles(obj.optString("files"));
                            video.setFk_create_user_id(obj.optString("fk_create_user_id"));
                            video.setHitnum(obj.optString("hitnum"));
                            video.setMeta_keys(obj.optString("meta_keys"));
                            video.setScore(obj.optString("score"));
                            video.setStatus(obj.optString("status"));
                            video.setThe_id(id);
                            videoList.add(video);
                            videoListAdapter.setVideos(videoList);
                        }
                        normalListView.onRefreshComplete();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    normalListView.onRefreshComplete();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                normalListView.onRefreshComplete();
            }
        });
    }
}
