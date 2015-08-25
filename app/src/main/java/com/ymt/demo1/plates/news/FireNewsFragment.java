/*
 * Copyright 2014 Don Moses
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

package com.ymt.demo1.plates.news;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import com.ymt.demo1.R;
import com.ymt.demo1.baseClasses.BaseFragment;
import com.ymt.demo1.beams.news.NewsSummary;
import com.ymt.demo1.utils.BaseURLUtil;
import com.ymt.demo1.utils.StringUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This fragment manages ViewPager and its child Fragments.
 * Scrolling techniques are basically the same as ViewPagerTab2Activity.
 */
public class FireNewsFragment extends BaseFragment {
    public static final String FRAGMENT_TAG = "fragment";
    private TextView hotTitleView;
    private TextView hotContentView;
    private TextView hotHitView;
    private ImageView hotPicView;

    private TextView recTitleView;
    private TextView recContentView;
    private TextView recHitView;
    private ImageView recPicView;

    private NewsSummary hotNew;
    private NewsSummary recNew;

    private ImageView imgNewI;
    private ImageView imgNewII;
    private ImageView imgNewIII;

    private NewsSummary imgSummary1;
    private NewsSummary imgSummary2;
    private NewsSummary imgSummary3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fire_news, container, false);

        initView(view);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RequestQueue mQueue = Volley.newRequestQueue(getActivity());
        mQueue.add(newsRequest(BaseURLUtil.BASE_URL+"/fw?controller=com.xfsm.action.ArticleAction&m=list&type=" + "xf_article_h_news_photo" + "&order=new&start=" + String.valueOf(1), 0));
        mQueue.add(newsRequest(BaseURLUtil.BASE_URL+"/fw?controller=com.xfsm.action.ArticleAction&m=list&type=" + "xf_article_h_news" + "&order=hot&start=" + String.valueOf(1), 1));
        mQueue.add(newsRequest(BaseURLUtil.BASE_URL+"/fw?controller=com.xfsm.action.ArticleAction&m=list&type=" + "xf_article_h_notice" + "&order=new&start=" + String.valueOf(1), 2));
    }

    protected void initView(View view) {
        View picsGate = view.findViewById(R.id.pics_gate);
        View fireNewsGate = view.findViewById(R.id.fire_news_hot_btn);
        View fireNoticeGate = view.findViewById(R.id.fire_news_rec_btn);

        imgNewI = (ImageView) view.findViewById(R.id.img_news1);
        imgNewII = (ImageView) view.findViewById(R.id.img_news2);
        imgNewIII = (ImageView) view.findViewById(R.id.img_news3);
        imgSummary1 = new NewsSummary();
        imgSummary2 = new NewsSummary();
        imgSummary3 = new NewsSummary();

        View hotView = view.findViewById(R.id.fire_news_hot_layout);
        View recView = view.findViewById(R.id.fire_news_rec_layout);
        hotTitleView = (TextView) hotView.findViewById(R.id.subject);
        hotContentView = (TextView) hotView.findViewById(R.id.content_text);
        hotHitView = (TextView) hotView.findViewById(R.id.hit_num);
        hotPicView = (ImageView) hotView.findViewById(R.id.pic);

        recTitleView = (TextView) recView.findViewById(R.id.subject);
        recContentView = (TextView) recView.findViewById(R.id.content_text);
        recHitView = (TextView) recView.findViewById(R.id.hit_num);
        recPicView = (ImageView) recView.findViewById(R.id.pic);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.pics_gate:
                        startActivity(new Intent(getActivity(), FirePicActivity.class));
                        break;
                    case R.id.fire_news_hot_btn:
                        Intent intentH = new Intent(getActivity(), FireHRActivity.class);
                        intentH.putExtra("type", "hot");
                        startActivity(intentH);
                        break;
                    case R.id.fire_news_rec_btn:
                        Intent intentR = new Intent(getActivity(), FireHRActivity.class);
                        intentR.putExtra("type", "new");
                        startActivity(intentR);
                        break;
                    case R.id.fire_news_hot_layout:
                        Intent intent1 = new Intent(getActivity(), NewsDetailActivity.class);
                        intent1.putExtra("summary", hotNew);
                        startActivity(intent1);
                        break;
                    case R.id.fire_news_rec_layout:
                        Intent intent2 = new Intent(getActivity(), NewsDetailActivity.class);
                        intent2.putExtra("summary", recNew);
                        startActivity(intent2);
                        break;
                    case R.id.img_news1:
                        Intent intent3 = new Intent(getActivity(), NewsDetailActivity.class);
                        intent3.putExtra("summary", imgSummary1);
                        startActivity(intent3);
                        break;
                    case R.id.img_news2:
                        Intent intent4 = new Intent(getActivity(), NewsDetailActivity.class);
                        intent4.putExtra("summary", imgSummary2);
                        startActivity(intent4);
                        break;
                    case R.id.img_news3:
                        Intent intent5 = new Intent(getActivity(), NewsDetailActivity.class);
                        intent5.putExtra("summary", imgSummary3);
                        startActivity(intent5);
                        break;

                    default:
                        break;
                }
            }
        };
        picsGate.setOnClickListener(onClickListener);
        fireNewsGate.setOnClickListener(onClickListener);
        fireNoticeGate.setOnClickListener(onClickListener);
        hotView.setOnClickListener(onClickListener);
        recView.setOnClickListener(onClickListener);
        imgNewI.setOnClickListener(onClickListener);
        imgNewII.setOnClickListener(onClickListener);
        imgNewIII.setOnClickListener(onClickListener);

    }

    private StringRequest newsRequest(String urlStr, final int type) {

        return new StringRequest(urlStr, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray summaryArray = jsonObject.getJSONObject("datas").getJSONArray("listData");

                    switch (type) {
                        case 0:         //图片
                            int length = summaryArray.length();
                            for (int i = 0; i < length; i++) {
                                JSONObject object = summaryArray.getJSONObject(i);
                                NewsSummary summary = new NewsSummary();
                                summary.setContent(object.optString("content"));
                                summary.setCreate_time(object.optString("create_time"));
                                summary.setArticle_title(object.optString("article_title"));
                                summary.setHitnum(object.optString("hitnum"));
                                summary.setThe_id(object.optString("id"));
                                summary.setFk_create_user_id(object.optString("fk_create_user_id"));
                                summary.setSource(object.optString("source"));
                                summary.setEditor(object.optString("editor"));
                                summary.setAuthor(object.optString("author"));
                                summary.setStatus(object.optString("status"));
                                summary.setPic(BaseURLUtil.BASE_URL + object.opt("pic"));

                                if (i == 0) {
                                    imgSummary1 = summary;
                                    Picasso.with(getActivity()).load(summary.getPic()).into(imgNewI);
                                } else if (i == 1) {
                                    imgSummary2 = summary;
                                    Picasso.with(getActivity()).load(summary.getPic()).into(imgNewII);
                                } else if (i == 2) {
                                    imgSummary3 = summary;
                                    Picasso.with(getActivity()).load(summary.getPic()).into(imgNewIII);
                                } else {
                                    return;
                                }

                            }
                            break;
                        case 1:         //hot
                            JSONObject object1 = summaryArray.getJSONObject(0);
                            hotNew = new NewsSummary();
                            hotNew.setContent(object1.optString("content"));
                            hotNew.setCreate_time(object1.optString("create_time"));
                            hotNew.setArticle_title(object1.optString("article_title"));
                            hotNew.setHitnum(object1.optString("hitnum"));
                            hotNew.setThe_id(object1.optString("id"));
                            hotNew.setFk_create_user_id(object1.optString("fk_create_user_id"));
                            hotNew.setSource(object1.optString("source"));
                            hotNew.setEditor(object1.optString("editor"));
                            hotNew.setAuthor(object1.optString("author"));
                            hotNew.setStatus(object1.optString("status"));
                            String pic1 = object1.optString("pic");
                            hotNew.setPic(BaseURLUtil.BASE_URL + pic1);
                            if (!TextUtils.isEmpty(pic1)) {
                                hotPicView.setVisibility(View.VISIBLE);
                                Picasso.with(getActivity()).load(hotNew.getPic()).into(hotPicView);
                            } else {
                                hotPicView.setVisibility(View.GONE);
                            }
                            hotTitleView.setText(hotNew.getArticle_title());
                            hotContentView.setText(StringUtils.replaceBlank(Html.fromHtml(hotNew.getContent()).toString()));
                            hotHitView.setVisibility(View.GONE);
                            break;
                        case 2:         //rec
                            JSONObject object2 = summaryArray.getJSONObject(0);
                            recNew = new NewsSummary();
                            recNew.setContent(object2.optString("content"));
                            recNew.setCreate_time(object2.optString("create_time"));
                            recNew.setArticle_title(object2.optString("article_title"));
                            recNew.setHitnum(object2.optString("hitnum"));
                            recNew.setThe_id(object2.optString("id"));
                            recNew.setFk_create_user_id(object2.optString("fk_create_user_id"));
                            recNew.setSource(object2.optString("source"));
                            recNew.setEditor(object2.optString("editor"));
                            recNew.setAuthor(object2.optString("author"));
                            recNew.setStatus(object2.optString("status"));
                            String pic2 = object2.optString("pic");
                            recNew.setPic(BaseURLUtil.BASE_URL + pic2);

                            if (!TextUtils.isEmpty(pic2)) {
                                recPicView.setVisibility(View.VISIBLE);
                                Picasso.with(getActivity()).load(recNew.getPic()).into(recPicView);
                            } else {
                                recPicView.setVisibility(View.GONE);
                            }
                            recTitleView.setText(recNew.getArticle_title());
                            recContentView.setText(StringUtils.replaceBlank(Html.fromHtml(recNew.getContent()).toString()));
                            recHitView.setVisibility(View.GONE);
                            break;
                        default:

                            break;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getActivity(), "网络错误，请稍后重试！", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
