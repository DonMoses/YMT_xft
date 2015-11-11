package com.ymt.demo1.plates.news;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.squareup.picasso.Picasso;
import com.ymt.demo1.R;
import com.ymt.demo1.beams.news.NewsSummary;
import com.ymt.demo1.customViews.MyTitle;
import com.ymt.demo1.baseClasses.BaseFloatActivity;
import com.ymt.demo1.utils.AppContext;
import com.ymt.demo1.utils.BaseURLUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Dan on 2015/7/14
 */
public class FirePicActivity extends BaseFloatActivity {
    private PullToRefreshListView newsListView;
    private int start;
    private RequestQueue mQueue;
    private PicSummaryAdapter summaryAdapter;
    private ArrayList<NewsSummary> newsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mQueue = Volley.newRequestQueue(this);
        start = 1;
        setContentView(R.layout.activity_fire_pic);
        initTitle();
        initView();
        mQueue.add(summaryRequest(start));
    }

    protected void initTitle() {
        MyTitle title = (MyTitle) findViewById(R.id.my_title);
        title.setTitleStyle(MyTitle.TitleStyle.LEFT_ICON);

        title.setOnLeftActionClickListener(new MyTitle.OnLeftActionClickListener() {
            @Override
            public void onClick() {
                finish();
            }
        });
    }

    protected void initView() {
        newsListView = (PullToRefreshListView) findViewById(R.id.pull_to_refresh_list_view);
        summaryAdapter = new PicSummaryAdapter(this);
        newsList = new ArrayList<>();
        newsListView.setAdapter(summaryAdapter);
        newsListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                start = 1;
                newsList.clear();
                summaryAdapter.setList(newsList);
                mQueue.add(summaryRequest(start));
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                start++;
                mQueue.add(summaryRequest(start));
            }
        });

        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(FirePicActivity.this, NewsDetailActivity.class);
                NewsSummary summary = (NewsSummary) (parent.getAdapter()).getItem(position);
                intent.putExtra("summary", summary);
                startActivity(intent);
            }
        });
    }

    private StringRequest summaryRequest(int start) {
        return new StringRequest(BaseURLUtil.getPicNewsUrl(), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray summaryArray = jsonObject.getJSONObject("datas").getJSONArray("listData");
                    int length = summaryArray.length();
                    for (int i = 0; i < length; i++) {
                        JSONObject object = summaryArray.getJSONObject(i);
                        NewsSummary summary = new NewsSummary();
                        summary.setContent(object.optString("content"));
                        summary.setThe_id(object.optString("id"));
                        summary.setAuthor(object.optString("author"));
                        summary.setCreateTime(object.optString("createTime"));
                        summary.setEditor(object.optString("editor"));
                        summary.setSource(object.optString("source"));
                        summary.setArticleTitle(object.optString("articleTitle"));
                        summary.setHitnum(object.optString("hitnum"));
                        summary.setType(object.optString("type"));
                        summary.setPic(object.optString("pic"));
                        summary.setName1(object.optString("name1"));
                        summary.setName2(object.optString("name2"));
                        newsList.add(summary);

                    }
                    summaryAdapter.setList(newsList);
                } catch (JSONException e) {
                    AppContext.toastBadJson();
                }
                newsListView.onRefreshComplete();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                AppContext.toastBadInternet();
                newsListView.onRefreshComplete();
            }
        });

    }

    public class PicSummaryAdapter extends BaseAdapter {
        ArrayList<NewsSummary> list = new ArrayList<>();
        LayoutInflater inflater;
        Context context;

        public PicSummaryAdapter(Context context) {
            this.context = context;
            this.inflater = LayoutInflater.from(context);
        }

        public void setList(ArrayList<NewsSummary> list) {
            this.list = list;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_pic_new, null);
                viewHolder = new ViewHolder();
                viewHolder.titleView = (TextView) convertView.findViewById(R.id.pic_title);
                viewHolder.picView = (ImageView) convertView.findViewById(R.id.pic_view);
                viewHolder.hitView = (TextView) convertView.findViewById(R.id.hit_num);
                convertView.setTag(viewHolder);

            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            //todo

            viewHolder.titleView.setText(list.get(position).getArticleTitle());
            viewHolder.hitView.setText(list.get(position).getHitnum() + "跟贴");
            String pic = list.get(position).getPic();
            if (!TextUtils.isEmpty(pic)) {
                viewHolder.picView.setVisibility(View.VISIBLE);
                Picasso.with(context).load(pic).into(viewHolder.picView);
            } else {
                viewHolder.picView.setVisibility(View.GONE);
            }

            return convertView;
        }

        class ViewHolder {
            ImageView picView;
            TextView titleView;
            TextView hitView;

        }
    }
}
