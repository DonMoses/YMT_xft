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
import com.ymt.demo1.beams.consult_cato.ConsultItem;
import com.ymt.demo1.beams.expert_consult.PreExpert;
import com.ymt.demo1.beams.knowledge.KnowledgeItem;
import com.ymt.demo1.utils.AppContext;
import com.ymt.demo1.baseClasses.BaseFloatActivity;
import com.ymt.demo1.utils.BaseURLUtil;
import com.ymt.demo1.plates.consultCato.ConsultDetailActivity;
import com.ymt.demo1.plates.exportConsult.ExpertInfoActivity;
import com.ymt.demo1.plates.knowledge.KnowledgeItemDetailActivity;
import com.ymt.demo1.plates.knowledge.KnowledgeItemListViewFragment;
import com.ymt.demo1.plates.knowledge.WebVideoActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
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
    private List<PreExpert> experts = new ArrayList<>();
    private List<KnowledgeItem> knowledgeItemList = new ArrayList<>();
    private List<ConsultItem> consultList = new ArrayList<>();
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
                if (AppContext.internetAvialable()) {
                    start = 1;
                    experts.clear();
                    expertListAdapter.setExperts(experts);
                    String sKw = searchTxt.getText().toString();
                    mQueue.add(getExperts(pageSize, start, sKw));
                }
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
                if (AppContext.internetAvialable()) {
                    start++;
                    String sKw = searchTxt.getText().toString();
                    mQueue.add(getExperts(pageSize, start, sKw));
                }
            }
        });

        expertGirdView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PreExpert expert = (PreExpert) expertGirdView.getRefreshableView().getAdapter().getItem(position);
                Intent intent = new Intent(SearchResultActivity.this, ExpertInfoActivity.class);
                intent.putExtra("id", expert.getFkUserId());
                startActivity(intent);
            }
        });

    }

    protected void initListView() {
        knowledgeItemList.clear();
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
            case 2:                 //知识平台-标准
            case 3:                 //知识平台-科研
                knowledgeNormalAdapter = new KnowledgeItemAdapter(SearchResultActivity.this);
                normalListView.setAdapter(knowledgeNormalAdapter);
                knowledgeNormalAdapter.setKnowledgeItemList(knowledgeItemList);
                mQueue.add(getKnowledgePdfList(pageSize, start, kword));
                break;
            case 4:                 //知识平台-视频
                videoListAdapter = new VideoListAdapter(SearchResultActivity.this, AppContext.screenWidth);
                normalListView.setAdapter(videoListAdapter);
                videoListAdapter.setVideos(knowledgeItemList);
                mQueue.add(getKnowledgeVideoList(pageSize, start, kword));
                break;
            case 5:                 //咨询分类
                searchedConsultAdapter = new SearchedConsultAdapter(SearchResultActivity.this);
                normalListView.setAdapter(searchedConsultAdapter);
                searchedConsultAdapter.setList(consultList);
                mQueue.add(getConsultRequest(start, 761, 762, 763));
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
                    case 3:                 //知识平台-标准
                        knowledgeItemList.clear();
                        knowledgeNormalAdapter.setKnowledgeItemList(knowledgeItemList);
                        mQueue.add(getKnowledgePdfList(pageSize, start, inKw));
                        break;
                    case 4:                 //知识平台-视频
                        knowledgeItemList.clear();
                        videoListAdapter.setVideos(knowledgeItemList);
                        mQueue.add(getKnowledgeVideoList(pageSize, start, inKw));
                        break;
                    case 5:                 //咨询分类
                        consultList.clear();
                        searchedConsultAdapter.setList(consultList);
                        mQueue.add(getConsultRequest(start, 761, 762, 763));
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
                    case 3:                 //知识平台-标准
                        mQueue.add(getKnowledgePdfList(pageSize, start, inKw));
                        break;
                    case 4:                 //知识平台-视频
                        mQueue.add(getKnowledgeVideoList(pageSize, start, inKw));
                        break;
                    case 5:                 //咨询分类
                        mQueue.add(getConsultRequest(start, 761, 762, 763));
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
                    case 0:                 //全文检索
                        //todo
//                        AllSearchItem item = (AllSearchItem) parent.getAdapter().getItem(position);
//                        switch (item.getDoc_type_name()) {
//                            case "标准规范":
//                                KnowledgeItem itemBZGF = new KnowledgeItem();
//                                itemBZGF.setCreate_time(item.getCreate_date());
//                                itemBZGF.setArticle_title(item.getDoc_title());
//                                itemBZGF.setPdf_id(item.getFile_path());
//                                itemBZGF.setContent(item.getFile_name());
//                                Intent intent0 = new Intent(SearchResultActivity.this, KnowledgeItemDetailActivity.class);
//                                intent0.putExtra("bzgf", itemBZGF);
//                                startActivity(intent0);
//                                break;
//                            case "问答咨询":
//                                KnowledgeItem itemWd = new KnowledgeItem();
//                                itemWd.setCreate_time(item.getCreate_date());
//                                itemWd.setArticle_title(item.getDoc_title());
//                                itemWd.setPdf_id(item.getFile_path());
//                                itemWd.setContent(item.getDoc_content());
//                                Intent intent1 = new Intent(SearchResultActivity.this, KnowledgeItemDetailActivity.class);
//                                intent1.putExtra("bzgf", itemWd);
//                                startActivity(intent1);
//                                break;
//                            case "论坛":
//                            case "教育":
//                            case "商品":
//                            case "咨询":
//                            default:
//                                break;
//                        }
                        break;
                    case 2:
                    case 3:
                        Intent intent2 = new Intent(SearchResultActivity.this, KnowledgeItemDetailActivity.class);
                        intent2.putExtra("item", ((KnowledgeItem) parent.getAdapter().getItem(position)));
                        startActivity(intent2);
                        break;
                    case 4:         //todo 视频
                        Intent intent = new Intent(SearchResultActivity.this, WebVideoActivity.class);
                        intent.putExtra("mp4_url", BaseURLUtil.getKnowledgeFileUrl(((KnowledgeItem) parent.getAdapter().getItem(position)).getKnowId()));
                        startActivity(intent);
                        break;
                    case 5:         //咨询分类
                        Intent intent5 = new Intent(SearchResultActivity.this, ConsultDetailActivity.class);
                        intent5.putExtra("title", ((ConsultItem) parent.getAdapter().getItem(position)).getTitle());
                        intent5.putExtra("content", ((ConsultItem) parent.getAdapter().getItem(position)).getItContent());
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
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getString("result").equals("Y")) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("datas");
                        JSONArray jsonArray = jsonObject1.getJSONArray("listData");
                        int length = jsonArray.length();
                        for (int i = 0; i < length; i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            PreExpert preExpert = new PreExpert();
                            preExpert.setCount(obj.optInt("count"));
                            preExpert.setUsername(obj.optString("username"));
                            preExpert.setLevel(obj.optString("level"));
                            preExpert.setFkUserId(obj.optInt("fkUserId"));
                            preExpert.setWaitCount(obj.optInt("waitCount"));
                            preExpert.setHeadImage(obj.optString("headImage"));
                            experts.add(preExpert);
                        }
                        expertListAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    AppContext.toastBadJson();
                }
                expertGirdView.onRefreshComplete();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                AppContext.toastBadInternet();
                expertGirdView.onRefreshComplete();
            }
        });
    }


    /**
     * 标准规范
     */
    protected StringRequest getKnowledgePdfList(int pageSize, int startIndex, String searchWhat) {
        return new StringRequest(BaseURLUtil.doGetKnowledgeAction(KnowledgeItemListViewFragment.KNOWLEDGE_BZGF, pageSize, startIndex, searchWhat), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                try {
                    JSONObject object = new JSONObject(s);

                    if (object.getString("result").equals("Y")) {
                        JSONObject jsonObject = object.getJSONObject("datas");
                        JSONArray jsonArray = jsonObject.getJSONArray("listData");
                        int length = jsonArray.length();
                        for (int i = 0; i < length; i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            KnowledgeItem knowledgeItem = new KnowledgeItem();
                            knowledgeItem.setAuditorId(obj.optString("auditorId"));
                            knowledgeItem.setAuthor(obj.optString("author"));
                            knowledgeItem.setAvrScor(obj.optInt("avrScor"));
                            knowledgeItem.setDocBrief(obj.optString("docBrief"));
                            knowledgeItem.setDocLoacl(obj.optString("docLoacl"));
                            knowledgeItem.setDocTitle(obj.optString("docTitle"));
                            knowledgeItem.setDocType(obj.optString("docType"));
                            knowledgeItem.setDownTimes(obj.optInt("downTimes"));
                            knowledgeItem.setDownVal(obj.optInt("downVal"));
                            knowledgeItem.setEditor(obj.optString("editor"));
                            knowledgeItem.setFileName(obj.optString("fileName"));
                            knowledgeItem.setKeyWord(obj.optString("keyWord"));
                            knowledgeItem.setKind(obj.optString("kind"));
                            String id = obj.optString("knowId");
                            knowledgeItem.setKnowId(id);
                            knowledgeItem.setNetType(obj.optString("netType"));
                            knowledgeItem.setPassTime(obj.optString("passTime"));
                            knowledgeItem.setPrtKind(obj.optString("prtKind"));
                            knowledgeItem.setReadTimes(obj.optInt("readTimes"));
                            knowledgeItem.setReason(obj.optString("reason"));
                            knowledgeItem.setScorTimes(obj.optInt("scorTimes"));
                            knowledgeItem.setSource(obj.optString("source"));
                            knowledgeItem.setStat(obj.optString("stat"));
                            knowledgeItem.setType(obj.optString("type"));
                            knowledgeItem.setUpDateTime(obj.optString("upDate"));
                            knowledgeItem.setUserid(obj.optString("userid"));
                            knowledgeItemList.add(knowledgeItem);
                            knowledgeNormalAdapter.setKnowledgeItemList(knowledgeItemList);
                        }
                        normalListView.onRefreshComplete();

                    }
                } catch (JSONException e) {
                    AppContext.toastBadJson();
                    normalListView.onRefreshComplete();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                AppContext.toastBadInternet();
                normalListView.onRefreshComplete();
            }
        });

    }

    /**
     * 咨询分类
     */
    protected StringRequest getConsultRequest(int start, int... typeCode) {
        return new StringRequest(BaseURLUtil.getTypedCatoList(start, typeCode), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getString("result").equals("Y")) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("datas");
                        JSONArray jsonArray = jsonObject1.getJSONArray("listData");
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
                            consultList.add(consult);
                            searchedConsultAdapter.setList(consultList);
                        }
                        normalListView.onRefreshComplete();

                    }
                } catch (JSONException e) {
                    AppContext.toastBadJson();
                    normalListView.onRefreshComplete();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                AppContext.toastBadInternet();
                normalListView.onRefreshComplete();
            }
        });
    }

    /**
     * 知识平台-视频
     */
    protected StringRequest getKnowledgeVideoList(int pageSize, int start, String searchWhat) {
        return new StringRequest(BaseURLUtil.doGetKnowledgeAction(KnowledgeItemListViewFragment.KNOWLEDGE_SPZL, pageSize, start, searchWhat), new Response.Listener<String>() {
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
                            KnowledgeItem knowledgeItem = new KnowledgeItem();
                            knowledgeItem.setAuditorId(obj.optString("auditorId"));
                            knowledgeItem.setAuthor(obj.optString("author"));
                            knowledgeItem.setAvrScor(obj.optInt("avrScor"));
                            knowledgeItem.setDocBrief(obj.optString("docBrief"));
                            knowledgeItem.setDocLoacl(obj.optString("docLoacl"));
                            knowledgeItem.setDocTitle(obj.optString("docTitle"));
                            knowledgeItem.setDocType(obj.optString("docType"));
                            knowledgeItem.setDownTimes(obj.optInt("downTimes"));
                            knowledgeItem.setDownVal(obj.optInt("downVal"));
                            knowledgeItem.setEditor(obj.optString("editor"));
                            knowledgeItem.setFileName(obj.optString("fileName"));
                            knowledgeItem.setKeyWord(obj.optString("keyWord"));
                            knowledgeItem.setKind(obj.optString("kind"));
                            String id = obj.optString("knowId");
                            knowledgeItem.setKnowId(id);
                            knowledgeItem.setNetType(obj.optString("netType"));
                            knowledgeItem.setPassTime(obj.optString("passTime"));
                            knowledgeItem.setPrtKind(obj.optString("prtKind"));
                            knowledgeItem.setReadTimes(obj.optInt("readTimes"));
                            knowledgeItem.setReason(obj.optString("reason"));
                            knowledgeItem.setScorTimes(obj.optInt("scorTimes"));
                            knowledgeItem.setSource(obj.optString("source"));
                            knowledgeItem.setStat(obj.optString("stat"));
                            knowledgeItem.setType(obj.optString("type"));
                            knowledgeItem.setUpDateTime(obj.optString("upDate"));
                            knowledgeItem.setUserid(obj.optString("userid"));
                            knowledgeItemList.add(knowledgeItem);
                            videoListAdapter.setVideos(knowledgeItemList);
                        }
                        normalListView.onRefreshComplete();

                    }
                } catch (JSONException e) {
                    AppContext.toastBadJson();
                    normalListView.onRefreshComplete();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                AppContext.toastBadInternet();
                normalListView.onRefreshComplete();
            }
        });
    }

    /**
     * 全部
     */
    protected StringRequest getAllSearch(int user_id, int start, int limit, final String queryInfo) {
        return new StringRequest("http://101.204.236.5/webintf/search/getFullQueryForKN?userId=" + user_id + "&queryWay=app&start=" + String.valueOf(start) + "&limit=" + String.valueOf(limit) + "&queryInfo=" + URLEncoder.encode(queryInfo), new Response.Listener<String>() {
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
                    AppContext.toastBadJson();
                    normalListView.onRefreshComplete();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                AppContext.toastBadInternet();
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
