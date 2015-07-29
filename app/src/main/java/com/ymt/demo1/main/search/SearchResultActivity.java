package com.ymt.demo1.main.search;

import android.content.Intent;
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
import com.ymt.demo1.adapter.AllSearchListAdapter;
import com.ymt.demo1.adapter.expertConsult.ExpertListAdapter;
import com.ymt.demo1.adapter.knowledge.KnowledgeItemAdapter;
import com.ymt.demo1.adapter.SearchedConsultAdapter;
import com.ymt.demo1.adapter.knowledge.VideoListAdapter;
import com.ymt.demo1.beams.AllSearchItem;
import com.ymt.demo1.beams.consult_cato.SearchedConsult;
import com.ymt.demo1.beams.expert_consult.Expert;
import com.ymt.demo1.beams.knowledge.KnowledgeItemBZGF;
import com.ymt.demo1.beams.knowledge.KnowledgeItemKYWX;
import com.ymt.demo1.beams.knowledge.KnowledgeVideo;
import com.ymt.demo1.main.AppContext;
import com.ymt.demo1.main.BaseFloatActivity;
import com.ymt.demo1.main.BaseURLUtil;
import com.ymt.demo1.plates.consultCato.ConsultDetailActivity;
import com.ymt.demo1.plates.exportConsult.ExpertInfoActivity;
import com.ymt.demo1.plates.knowledge.KnowledgeItemDetailActivity;
import com.ymt.demo1.plates.knowledge.KnowledgeItemListViewFragment;
import com.ymt.demo1.plates.knowledge.KnowledgeVideoFragment;
import com.ymt.demo1.plates.knowledge.WebVideoActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dan on 2015/7/8
 */
public class SearchResultActivity extends BaseFloatActivity {
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
    private LinearLayout rootLayout;
    private SearchedConsultAdapter searchedConsultAdapter;

    private KnowledgeItemAdapter knowledgeNormalAdapter;
    private VideoListAdapter videoListAdapter;
    private AllSearchListAdapter allSearchListAdapter;
    private List<Expert> experts = new ArrayList<>();
    private List<KnowledgeItemBZGF> bzgfList = new ArrayList<>();
    private List<KnowledgeItemKYWX> kywxList = new ArrayList<>();
    private List<KnowledgeVideo> spzlList = new ArrayList<>();
    private List<SearchedConsult> consultList = new ArrayList<>();
    private List<AllSearchItem> allSearchList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    protected void initBaseView() {
        rootLayout = (LinearLayout) findViewById(R.id.root_layout);
        spinner = (Spinner) findViewById(R.id.search_spinner);
        ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new String[]{"全部", "专家", "科研", "规范", "视频", "咨询"});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setVisibility(View.VISIBLE);//设置默认显示

        searchTxt = (EditText) findViewById(R.id.search_edit_text);
        ImageButton searchBtn = (ImageButton) findViewById(R.id.search_btn);

        spinner.setSelection(typePos);
        searchTxt.setText(keyW);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if ((typePos != position)) {
                    //类型改变,自动搜索
                    switch (position) {
                        case 1:                 //搜索专家
                            initGridView();
                            break;
                        case 0:
                        case 2:                 //知识平台-科研
                        case 3:                 //知识平台-标准
                        case 4:                 //知识平台-视频
                        case 5:                 //咨询分类
                            initListView();
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

         /*
        searchBtn 事件
         */
        searchBtn.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             String inKw = searchTxt.getText().toString();
                                             if (!TextUtils.isEmpty(inKw)) {

                                                 int position = spinner.getSelectedItemPosition();
                                                 if ((typePos != position) || (!keyW.equals(inKw))) {
                                                     //类型改变,自动搜索
                                                     switch (position) {
                                                         case 1:                 //搜索专家
                                                             initGridView();
                                                             break;
                                                         case 0:
                                                         case 2:                 //知识平台-科研
                                                         case 3:                 //知识平台-标准
                                                         case 4:                 //知识平台-视频
                                                         case 5:                 //咨询分类

                                                             initListView();
                                                             break;
                                                         default:
                                                             break;
                                                     }

                                                     typePos = position;
                                                     keyW = inKw;
                                                 }

                                             } else {
                                                 Toast.makeText(SearchResultActivity.this, "请输入搜索关键词...", Toast.LENGTH_SHORT).show();
                                             }

                                             //清空输入内容， 输入框改变为不聚焦
                                             //searchTxt.setText(null);
                                             searchTxt.clearFocus();
                                         }
                                     }

        );
        searchBtn.callOnClick();
        ProgressBar progressBar = new ProgressBar(SearchResultActivity.this);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        progressBar.setLayoutParams(params);
        if (rootLayout.getChildAt(1) instanceof PullToRefreshGridView)

        {
            ((PullToRefreshGridView) rootLayout.getChildAt(1)).setEmptyView(progressBar);
        } else if (rootLayout.getChildAt(1) instanceof PullToRefreshListView)

        {
            ((PullToRefreshListView) rootLayout.getChildAt(1)).setEmptyView(progressBar);
        }

    }

    protected void initGridView() {
        experts.clear();
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
        expertListAdapter.setExperts(experts);
        expertGirdView.onRefreshComplete();

        //进入时，根据关键字自动搜索
        String kword = searchTxt.getText().toString();
        mQueue.add(getExperts(pageSize, start, kword));

        expertGirdView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
                start = 1;
                experts.clear();
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

    }

    protected void initListView() {
        bzgfList.clear();
        kywxList.clear();
        consultList.clear();
        spzlList.clear();
        allSearchList.clear();
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

        int pos = spinner.getSelectedItemPosition();
        String kword = searchTxt.getText().toString();
        switch (pos) {
            case 0:                 //全部
                start = 0;
                allSearchListAdapter = new AllSearchListAdapter(SearchResultActivity.this);
                normalListView.setAdapter(allSearchListAdapter);
                allSearchListAdapter.setList(allSearchList, kword);
                mQueue.add(getAllSearch(AppContext.now_user_id, start, pageSize, kword));
                break;
            case 2:                 //知识平台-科研
                knowledgeNormalAdapter = new KnowledgeItemAdapter(SearchResultActivity.this, KnowledgeItemListViewFragment.KNOWLEDGE_KYWX);
                normalListView.setAdapter(knowledgeNormalAdapter);
                knowledgeNormalAdapter.setKYWXList(kywxList);
                mQueue.add(getKywxList(pageSize, start, kword));
                break;
            case 3:                 //知识平台-标准
                knowledgeNormalAdapter = new KnowledgeItemAdapter(SearchResultActivity.this, KnowledgeItemListViewFragment.KNOWLEDGE_BZGF);
                normalListView.setAdapter(knowledgeNormalAdapter);
                knowledgeNormalAdapter.setBZGFList(bzgfList);
                mQueue.add(getBzgfList(pageSize, start, kword));
                break;
            case 4:                 //知识平台-视频
                videoListAdapter = new VideoListAdapter(SearchResultActivity.this, AppContext.screenWidth);
                normalListView.setAdapter(videoListAdapter);
                videoListAdapter.setVideos(spzlList);
                mQueue.add(getKnowledgeVideo(pageSize, start, kword));
                break;
            case 5:                 //咨询分类
                searchedConsultAdapter = new SearchedConsultAdapter(SearchResultActivity.this);
                normalListView.setAdapter(searchedConsultAdapter);
                searchedConsultAdapter.setList(consultList);
                mQueue.add(getConsultRequest(pageSize, start, "", kword));
                break;
            default:
                break;
        }
        normalListView.onRefreshComplete();

        normalListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                start = 1;
                int pos = spinner.getSelectedItemPosition();
                String inKw = searchTxt.getText().toString();
                switch (pos) {
                    case 0:                 //全部
                        start = 0;
                        allSearchList.clear();
                        allSearchListAdapter.setList(allSearchList, inKw);
                        mQueue.add(getAllSearch(AppContext.now_user_id, start, pageSize, inKw));
                        break;
                    case 2:                 //知识平台-科研
                        kywxList.clear();
                        knowledgeNormalAdapter.setKYWXList(kywxList);
                        mQueue.add(getKywxList(pageSize, start, inKw));
                        break;
                    case 3:                 //知识平台-标准
                        bzgfList.clear();
                        knowledgeNormalAdapter.setBZGFList(bzgfList);
                        mQueue.add(getBzgfList(pageSize, start, inKw));
                        break;
                    case 4:                 //知识平台-视频
                        spzlList.clear();
                        videoListAdapter.setVideos(spzlList);
                        mQueue.add(getKnowledgeVideo(pageSize, start, inKw));
                        break;
                    case 5:                 //咨询分类
                        consultList.clear();
                        searchedConsultAdapter.setList(consultList);
                        mQueue.add(getConsultRequest(pageSize, start, "", inKw));
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                start++;
                int pos = spinner.getSelectedItemPosition();
                String inKw = searchTxt.getText().toString();
                switch (pos) {
                    case 0:                 //全部
                        mQueue.add(getAllSearch(AppContext.now_user_id, start, pageSize, inKw));
                        break;
                    case 2:                 //知识平台-科研
                        mQueue.add(getKywxList(pageSize, start, inKw));
                        break;
                    case 3:                 //知识平台-标准
                        mQueue.add(getBzgfList(pageSize, start, inKw));
                        break;
                    case 4:                 //知识平台-视频
                        mQueue.add(getKnowledgeVideo(pageSize, start, inKw));
                        break;
                    case 5:                 //咨询分类
                        mQueue.add(getConsultRequest(pageSize, start, "", inKw));
                        break;
                    default:
                        break;
                }
            }
        });

         /*
        listView 点击事件 。 跳转到详情界面
         */
        normalListView.getRefreshableView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //传入内容

                int pos = spinner.getSelectedItemPosition();
                switch (pos) {
                    case 0:                 //全部
                        //todo
                        AllSearchItem item = (AllSearchItem) parent.getAdapter().getItem(position);
                        switch (item.getDoc_type_name()) {
                            case "标准规范":
                                KnowledgeItemBZGF itemBZGF = new KnowledgeItemBZGF();
                                itemBZGF.setCreate_time(item.getCreate_date());
                                itemBZGF.setArticle_title(item.getDoc_title());
                                itemBZGF.setPdf_id(item.getFile_path());
                                itemBZGF.setContent(item.getFile_name());
                                Intent intent0 = new Intent(SearchResultActivity.this, KnowledgeItemDetailActivity.class);
                                intent0.putExtra("bzgf", itemBZGF);
                                startActivity(intent0);
                                break;
                            case "论坛":
                            case "教育":
                            case "商品":
                            case "咨询":
                            default:
                                break;
                        }
                        break;
                    case 2:
                        Intent intent2 = new Intent(SearchResultActivity.this, KnowledgeItemDetailActivity.class);
                        intent2.putExtra("kywx", ((KnowledgeItemKYWX) parent.getAdapter().getItem(position)));
                        startActivity(intent2);
                        break;
                    case 3:
                        Intent intent3 = new Intent(SearchResultActivity.this, KnowledgeItemDetailActivity.class);
                        intent3.putExtra("bzgf", ((KnowledgeItemBZGF) parent.getAdapter().getItem(position)));
                        startActivity(intent3);
                        break;
                    case 4:         //视频
                        Intent intent = new Intent(SearchResultActivity.this, WebVideoActivity.class);
                        intent.putExtra("mp4_url", ((KnowledgeVideo) parent.getAdapter().getItem(position)).getAttachment());
                        startActivity(intent);
                        break;
                    case 5:         //咨询分类
                        Intent intent5 = new Intent(SearchResultActivity.this, ConsultDetailActivity.class);
                        intent5.putExtra("title", ((SearchedConsult) parent.getAdapter().getItem(position)).getArticle_title());
                        intent5.putExtra("content", ((SearchedConsult) parent.getAdapter().getItem(position)).getArticle_content());
                        startActivity(intent5);
                        break;
                    default:

                        break;

                }
            }
        });


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
                            expert.setCount(obj.optString("count"));
                            expert.setPro_life(obj.optString("pro_life"));
                            expert.setHead_pic(BaseURLUtil.BASE_URL + obj.optString("head_pic"));
                            expert.setTel(obj.optString("tel"));
                            expert.setFk_user_id(obj.optString("fk_user_id"));
                            expert.setAddr(obj.optString("addr"));
                            expert.setEducation(obj.optString("education"));
                            expert.setReporting_methods(obj.optString("reporting_methods"));
                            expert.setHome_zip_code(obj.optString("home_zip_code"));
                            expert.setPolitics(obj.optString("politics"));
                            expert.setQualification(obj.optString("qualification"));
                            expert.setLevel(obj.optString("level"));
                            expert.setCapacity(obj.optString("capacity"));
                            expert.setExperience(obj.optString("experience"));
                            expert.setIndustry(obj.optString("industry"));
                            expert.setNote(obj.optString("note"));
                            expert.setWork_addr(obj.optString("work_addr"));
                            expert.setOthers(obj.optString("others"));
                            expert.setId_number(obj.optString("id_number"));
                            expert.setHome_addr(obj.optString("home_addr"));
                            expert.setUser_name(obj.optString("user_name"));
                            expert.setSchool(obj.optString("school"));
                            expert.setDegree(obj.optString("degree"));
                            expert.setMajor_works(obj.optString("major_works"));
                            expert.setWork_zip_code(obj.optString("work_zip_code"));
                            expert.setCreate_time(obj.optString("create_time"));
                            expert.setPosition_title(obj.optString("position_title"));
                            expert.setWork_experience(obj.optString("work_experience"));
                            expert.setWork_name(obj.optString("work_name"));
                            experts.add(expert);
                        }
                        expertListAdapter.setExperts(experts);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }
                expertGirdView.onRefreshComplete();
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
                            knowledgeItemBZGF.setHxxf(obj.optString("hxxf"));
                            knowledgeItemBZGF.setDqxf(obj.optString("dqxf"));
                            knowledgeItemBZGF.setPdf_id(obj.optString("pdf_id"));
                            bzgfList.add(knowledgeItemBZGF);
                            knowledgeNormalAdapter.setBZGFList(bzgfList);
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
                            kywxList.add(knowledgeItemKYWX);
                            knowledgeNormalAdapter.setKYWXList(kywxList);
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
                            consultList.add(consult);
                            searchedConsultAdapter.setList(consultList);
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

                        for (int i = 0; i < length; i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            KnowledgeVideo video = new KnowledgeVideo();
                            String id = obj.optString("id");
                            video.setCreate_time(obj.optString("create_time"));
                            video.setArticle_title(obj.optString("article_title"));
                            video.setContent(obj.optString("content"));
                            video.setHitnum(obj.optString("hitnum"));
                            video.setFk_create_user_id(obj.optString("fk_create_user_id"));
                            video.setStatus(obj.optString("status"));
                            video.setThe_id(id);
                            video.setAttachment(obj.optString("attachment"));
                            video.setClassify(obj.optString("classify"));
                            video.setCover(obj.optString("cover"));
                            spzlList.add(video);
                            videoListAdapter.setVideos(spzlList);
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
     * 全部
     */
    protected StringRequest getAllSearch(String user_id, int start, int limit, final String queryInfo) {
        return new StringRequest("http://120.24.172.105/webintf/search/getFullQueryForKN?userId=" + user_id + "&queryWay=app&queryType=&start=" + String.valueOf(start) + "&limit=" + String.valueOf(limit) + "&queryInfo=" + queryInfo, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONObject jsonObject1 = jsonObject.getJSONObject("response");
                    JSONArray array = jsonObject1.getJSONArray("docs");
                    int length = array.length();
                    for (int i = 0; i < length; i++) {
                        JSONObject obj = array.getJSONObject(i);
                        AllSearchItem item = new AllSearchItem();
                        item.setThe_id(obj.optString("id"));
                        item.setDoc_title(obj.optString("doc_title"));
                        item.setDoc_url(obj.optString("doc_url"));
                        item.setDoc_type(obj.optString("doc_type"));
                        item.setCreate_date(obj.optString("create_date"));
                        item.setDoc_type_name(obj.optString("doc_type_name"));
                        item.setDoc_keyword(obj.optString("doc_keyword"));
                        item.setFile_name(obj.optString("file_name"));
                        item.setFile_type(obj.optString("file_type"));
                        item.setFile_path(obj.optString("file_path"));
                        item.setDoc_content(obj.optString("doc_content"));
                        allSearchList.add(item);
                        allSearchListAdapter.setList(allSearchList, queryInfo);
                    }
                    normalListView.onRefreshComplete();
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


    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }
}
