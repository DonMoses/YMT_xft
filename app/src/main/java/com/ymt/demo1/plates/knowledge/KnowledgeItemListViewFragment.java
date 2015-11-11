/*
 * Copyright 2014 DonMoses
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ymt.demo1.plates.knowledge;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.ymt.demo1.R;
import com.ymt.demo1.adapter.knowledge.KnowledgeItemAdapter;
import com.ymt.demo1.baseClasses.BaseFragment;
import com.ymt.demo1.beams.knowledge.KnowledgeItem;
import com.ymt.demo1.utils.AppContext;
import com.ymt.demo1.utils.BaseURLUtil;
import com.ymt.demo1.utils.PopActionListener;
import com.ymt.demo1.utils.PopActionUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment for ViewPagerTabFragmentActivity.
 * ScrollView callbacks are handled by its parent fragment, not its parent activity.
 */
public class KnowledgeItemListViewFragment extends BaseFragment {
    public static final int KNOWLEDGE_BZGF = 1;    //标准规范
    public static final int KNOWLEDGE_KYWX = 2;    //科研文献
    public static final int KNOWLEDGE_SPZL = 3;    //视频资料
    public static final int KNOWLEDGE_SJK = 4;     //数据库
    private PullToRefreshListView listView;
    ListView theListView;
    private KnowledgeItemAdapter adapter;
    private PopActionListener actionListener;
    private int mKnowledgeType;
    private List<KnowledgeItem> knowledgeItemList;
    private int pageSize;
    private int startIndex;
    private RequestQueue mQueue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scrollview_ultra_list_view, container, false);
        initView(view);
        return view;
    }

    public static KnowledgeItemListViewFragment getNewInstance(int knowledgeType) {
        KnowledgeItemListViewFragment fragment = new KnowledgeItemListViewFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("knowledge_type", knowledgeType);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mKnowledgeType = getArguments().getInt("knowledge_type");
        pageSize = 15;
        startIndex = 1;
        knowledgeItemList = new ArrayList<>();

        if (AppContext.internetAvialable()) {
            mQueue.add(getListData(pageSize, startIndex, ""));
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mQueue = ((KnowledgeMainActivity) activity).mQueue;
    }

    protected void initView(View view) {
        /*
         *将下拉刷新中的listView 加入到滚动布局中
         */
        listView = (PullToRefreshListView) view.findViewById(R.id.pull_to_refresh_list_view);
        theListView = listView.getRefreshableView();

        switch (mKnowledgeType) {
            case KNOWLEDGE_BZGF:
            case KNOWLEDGE_KYWX:
            case KNOWLEDGE_SJK:
                //设置数据源
                adapter = new KnowledgeItemAdapter(getActivity());
                listView.setAdapter(adapter);
                adapter.setKnowledgeItemList(knowledgeItemList);
                new AsyncTask<Void, Void, List<KnowledgeItem>>() {
                    @Override
                    protected List<KnowledgeItem> doInBackground(Void... params) {
                        return DataSupport.where("type = ?", String.valueOf(mKnowledgeType)).find(KnowledgeItem.class);
                    }

                    @Override
                    protected void onPostExecute(List<KnowledgeItem> itemList) {
                        super.onPostExecute(itemList);
                        knowledgeItemList.addAll(itemList);
                        adapter.notifyDataSetChanged();
                    }
                }.execute();
                break;
            default:
                break;

        }

        /*
         *设置长按弹出菜单事件
         */
        theListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, final View view, final int position, long id) {

            /*
            *初始化弹出菜单
            */
                String[] actionList = new String[]{"分享", "收藏"};
                PopActionUtil popActionUtil = PopActionUtil.getInstance(getActivity());
                actionListener = new PopActionListener() {
                    @Override
                    public void onAction(String action) {
                        switch (action) {
                            case "分享":
                                Toast.makeText(getActivity(), "分享", Toast.LENGTH_SHORT).show();
                                break;
                            case "收藏":
                                Toast.makeText(getActivity(), "收藏", Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                break;
                        }
                    }

                    @Override
                    public void onDismiss() {

                    }
                };

                popActionUtil.setActionListener(actionListener);
                popActionUtil.setActions(actionList);
                final PopupWindow itemPopMenu = popActionUtil.getSimpleTxtPopActionMenu();

                itemPopMenu.showAsDropDown(view, view.getWidth() - 20, -view.getHeight());
                itemPopMenu.update();

                return true;
            }
        });

        /*
        listView 点击事件 。 跳转到详情界面
         */
        theListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //传入内容
                Intent intent = new Intent(getActivity(), KnowledgeItemDetailActivity.class);
                switch (mKnowledgeType) {
                    case KNOWLEDGE_BZGF:
                    case KNOWLEDGE_KYWX:
                    case KNOWLEDGE_SJK:
                        intent.putExtra("knowId", ((KnowledgeItem) parent.getAdapter().getItem(position)).getKnowId());
                        break;
                    default:
                        break;
                }
                startActivity(intent);
            }
        });

        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                startIndex = 1;
                switch (mKnowledgeType) {
                    case KNOWLEDGE_BZGF:
                    case KNOWLEDGE_KYWX:
                    case KNOWLEDGE_SJK:
                        if (AppContext.internetAvialable()) {
                            DataSupport.deleteAll(KnowledgeItem.class, "type = ?", String.valueOf(mKnowledgeType));
                            knowledgeItemList.clear();
                            adapter.setKnowledgeItemList(knowledgeItemList);
                            mQueue.add(getListData(pageSize, startIndex, ""));
                        } else {
                            listView.onRefreshComplete();
                        }
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                startIndex++;
                switch (mKnowledgeType) {
                    case KNOWLEDGE_BZGF:
                    case KNOWLEDGE_KYWX:
                    case KNOWLEDGE_SJK:
                        if (AppContext.internetAvialable()) {
                            mQueue.add(getListData(pageSize, startIndex, ""));
                        }
                        break;
                    default:
                        break;

                }
            }
        });
    }

    /**
     * 标准规范
     */
    protected StringRequest getListData(int pageSize, int startIndex, String searchWhat) {
        String url = BaseURLUtil.doGetKnowledgeAction(mKnowledgeType, pageSize, startIndex, searchWhat);
//        Log.e("TAG", ">>>: KnowledgeItemListViewFragment url:  " + url);
        return new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
//                Log.e("TAG", ">>>>>>>>>>>>>s>>>>>>>>>>" + s);
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

                            /*
                             * 判断数据库是否已经包含该item
                             */
                            int size = DataSupport.where(
                                    "knowId = ? and type = ?", id, String.valueOf(mKnowledgeType)
                            ).find(KnowledgeItem.class).size();
                            int len = DataSupport.count(KnowledgeItem.class);
                            if (size == 0) {
                                if (len > 50) {
                                    DataSupport.delete(KnowledgeItem.class, 0);
                                }
                                knowledgeItem.save();
                            } else {
                                knowledgeItem.updateAll(
                                        "knowId = ? and type = ?", id, String.valueOf(mKnowledgeType)
                                );
                            }
                        }
                        knowledgeItemList = DataSupport.where("type = ?", String.valueOf(mKnowledgeType)).find(KnowledgeItem.class);
                        adapter.setKnowledgeItemList(knowledgeItemList);

                    }
                } catch (JSONException e) {
                    AppContext.toastBadJson();
                }
                listView.onRefreshComplete();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                AppContext.toastBadInternet();
                listView.onRefreshComplete();
            }
        });

    }

}
