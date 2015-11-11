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
import android.graphics.Bitmap;
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
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import com.ymt.demo1.R;
import com.ymt.demo1.baseClasses.BaseFragment;
import com.ymt.demo1.beams.news.ImageNewsSummary;
import com.ymt.demo1.beams.news.NewsSummary;
import com.ymt.demo1.utils.AppContext;
import com.ymt.demo1.utils.BaseURLUtil;
import com.ymt.demo1.utils.bitmap.BitmapCutUtil;
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

    private ImageNewsSummary imgSummary1;
    private ImageNewsSummary imgSummary2;
    private ImageNewsSummary imgSummary3;
    private RequestQueue mQueue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fire_news, container, false);

        initView(view);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mQueue = Volley.newRequestQueue(getActivity());
        mQueue.add(getImageNews()); // TODO: 2015/10/23
        mQueue.add(newsRequest("001", 1, 1, "hitnum"));
        mQueue.add(newsRequest("001", 1, 1, "time"));
    }

    protected void initView(View view) {
//        View picsGate = view.findViewById(R.id.pics_gate);
        View fireNewsGate = view.findViewById(R.id.fire_news_hot_btn);
        View fireNoticeGate = view.findViewById(R.id.fire_news_rec_btn);

        imgNewI = (ImageView) view.findViewById(R.id.img_news1);
        imgNewII = (ImageView) view.findViewById(R.id.img_news2);
        imgNewIII = (ImageView) view.findViewById(R.id.img_news3);
        imgSummary1 = new ImageNewsSummary();
        imgSummary2 = new ImageNewsSummary();
        imgSummary3 = new ImageNewsSummary();

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
//                    case R.id.pics_gate:
//                        startActivity(new Intent(getActivity(), FirePicActivity.class));
//                        break;
                    case R.id.fire_news_hot_btn:
                        Intent intentH = new Intent(getActivity(), FireHRActivity.class);
                        intentH.putExtra("type", "hitnum");
                        startActivity(intentH);
                        break;
                    case R.id.fire_news_rec_btn:
                        Intent intentR = new Intent(getActivity(), FireHRActivity.class);
                        intentR.putExtra("type", "time");
                        startActivity(intentR);
                        break;
                    case R.id.fire_news_hot_layout:
                        Intent intent1 = new Intent(getActivity(), NewsDetailActivity.class);
                        intent1.putExtra("summary", hotNew);
                        intent1.putExtra("type", "hitnum");
                        startActivity(intent1);
                        break;
                    case R.id.fire_news_rec_layout:
                        Intent intent2 = new Intent(getActivity(), NewsDetailActivity.class);
                        intent2.putExtra("summary", recNew);
                        intent2.putExtra("type", "time");
                        startActivity(intent2);
                        break;
                    case R.id.img_news1:
                        Intent intent3 = new Intent(getActivity(), NewsDetailActivity.class);
                        intent3.putExtra("summary", imgSummary1);//todo 图片1
                        startActivity(intent3);
                        break;
                    case R.id.img_news2:
                        Intent intent4 = new Intent(getActivity(), NewsDetailActivity.class);
                        intent4.putExtra("summary", imgSummary2);//todo 图片2
                        startActivity(intent4);
                        break;
                    case R.id.img_news3:
                        Intent intent5 = new Intent(getActivity(), NewsDetailActivity.class);
                        intent5.putExtra("summary", imgSummary3);//todo 图片3
                        startActivity(intent5);
                        break;
                    default:
                        break;
                }
            }
        };
//        picsGate.setOnClickListener(onClickListener);
        fireNewsGate.setOnClickListener(onClickListener);
        fireNoticeGate.setOnClickListener(onClickListener);
        hotView.setOnClickListener(onClickListener);
        recView.setOnClickListener(onClickListener);
        imgNewI.setOnClickListener(onClickListener);
        imgNewII.setOnClickListener(onClickListener);
        imgNewIII.setOnClickListener(onClickListener);

    }

    private StringRequest newsRequest(String state, int start, int pagesize, final String type) {

        return new StringRequest(BaseURLUtil.getNews(state, start, pagesize, type), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray summaryArray = jsonObject.getJSONObject("datas").getJSONArray("listData");

                    switch (type) {
                        case "hitnum":         //hot
                            JSONObject object1 = summaryArray.getJSONObject(0);
                            hotNew = new NewsSummary();

                            hotNew.setContent(object1.optString("content"));
                            hotNew.setThe_id(object1.optString("id"));
                            hotNew.setAuthor(object1.optString("author"));
                            hotNew.setCreateTime(object1.optString("createTime"));
                            hotNew.setEditor(object1.optString("editor"));
                            hotNew.setSource(object1.optString("source"));
                            hotNew.setArticleTitle(object1.optString("articleTitle"));
                            hotNew.setHitnum(object1.optString("hitnum"));
                            hotNew.setType(object1.optString("type"));
                            String pic1 = object1.optString("pic");
                            hotNew.setPic(pic1);
                            hotNew.setName1(object1.optString("name1"));
                            hotNew.setName2(object1.optString("name2"));

                            hotNew.setPic(BaseURLUtil.BASE_URL + pic1);
                            if (!TextUtils.isEmpty(pic1)) {
                                hotPicView.setVisibility(View.VISIBLE);
                                Picasso.with(getActivity()).load(hotNew.getPic()).into(hotPicView);
                            } else {
                                hotPicView.setVisibility(View.GONE);
                            }
                            hotTitleView.setText(hotNew.getArticleTitle());
                            hotContentView.setText(StringUtils.replaceBlank(Html.fromHtml(hotNew.getContent()).toString()));
                            hotHitView.setVisibility(View.GONE);
                            break;
                        case "time":         //rec
                            JSONObject object2 = summaryArray.getJSONObject(0);
                            recNew = new NewsSummary();

                            recNew.setContent(object2.optString("content"));
                            recNew.setThe_id(object2.optString("id"));
                            recNew.setAuthor(object2.optString("author"));
                            recNew.setCreateTime(object2.optString("createTime"));
                            recNew.setEditor(object2.optString("editor"));
                            recNew.setSource(object2.optString("source"));
                            recNew.setArticleTitle(object2.optString("articleTitle"));
                            recNew.setHitnum(object2.optString("hitnum"));
                            recNew.setType(object2.optString("type"));
                            String pic2 = object2.optString("pic");
                            recNew.setPic(pic2);
                            recNew.setName1(object2.optString("name1"));
                            recNew.setName2(object2.optString("name2"));
                            recNew.setPic(BaseURLUtil.BASE_URL + pic2);

                            if (!TextUtils.isEmpty(pic2)) {
                                recPicView.setVisibility(View.VISIBLE);
                                Picasso.with(getActivity()).load(recNew.getPic()).into(recPicView);
                            } else {
                                recPicView.setVisibility(View.GONE);
                            }
                            recTitleView.setText(recNew.getArticleTitle());
                            recContentView.setText(StringUtils.replaceBlank(Html.fromHtml(recNew.getContent()).toString()));
                            recHitView.setVisibility(View.GONE);
                            break;
                        default:

                            break;
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


    private StringRequest getImageNews() {
        return new StringRequest(BaseURLUtil.getImgNews(), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray summaryArray = jsonObject.getJSONObject("datas").getJSONArray("listData");
                    int length = summaryArray.length();
                    for (int i = 0; i < length; i++) {
                        JSONObject object = summaryArray.getJSONObject(i);
                        ImageNewsSummary summary = new ImageNewsSummary();
                        summary.setContent(object.optString("content"));
                        summary.setThe_id(object.optString("id"));
                        summary.setCreateTime(object.optString("createTime"));
                        summary.setCover(object.optString("cover"));
                        summary.setArticleTitle(object.getString("newsTitle"));

                        //// TODO: 2015/10/23 图片
                        if (i == 0) {
                            imgSummary1 = summary;
                            imgUrls[0] = summary.getCover();
                        } else if (i == 1) {
                            imgSummary2 = summary;
                            imgUrls[1] = summary.getCover();
                        } else if (i == 2) {
                            imgSummary3 = summary;
                            imgUrls[2] = summary.getCover();
                        } else {
                            break;
                        }

                    }
                    //读取图片
                    getImageNewsPics();
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

    private String[] imgUrls = new String[3];

    //读取、截图
    private void getImageNewsPics() {
        //todo 根据控件、图片的尺寸进行剪裁。   【目前的方式值适用于控件宽:控件高 近似于 图片宽:图片高】
        for (int i = 0; i < 3; i++) {
            String urls = imgUrls[i];
            if (i == 0) {
                ImageRequest request = new ImageRequest(urls, new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        Bitmap bitmap1 = BitmapCutUtil.getBitmapCutByViewSize(imgNewI, bitmap);
                        imgNewI.setImageBitmap(bitmap1);
                    }
                }, imgNewI.getWidth(), imgNewI.getHeight(), Bitmap.Config.RGB_565, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        AppContext.toastBadInternet();
                    }
                });
                mQueue.add(request);
            } else if (i == 1) {
                ImageRequest request = new ImageRequest(urls, new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        Bitmap bitmap1 = BitmapCutUtil.getBitmapCutByViewSize(imgNewII, bitmap);
                        imgNewII.setImageBitmap(bitmap1);
                    }
                }, imgNewII.getWidth(), imgNewII.getHeight(), Bitmap.Config.RGB_565, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        AppContext.toastBadInternet();
                    }
                });
                mQueue.add(request);
            } else if (i == 2) {
                ImageRequest request = new ImageRequest(urls, new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        Bitmap bitmap1 = BitmapCutUtil.getBitmapCutByViewSize(imgNewIII, bitmap);
                        imgNewIII.setImageBitmap(bitmap1);
                    }
                }, imgNewIII.getWidth(), imgNewIII.getHeight(), Bitmap.Config.RGB_565, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        AppContext.toastBadInternet();
                    }
                });
                mQueue.add(request);
            }

        }
    }


}
