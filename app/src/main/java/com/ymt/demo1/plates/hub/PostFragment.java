package com.ymt.demo1.plates.hub;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.ymt.demo1.R;

/**
 * Created by Dan on 2015/4/8
 */
public class PostFragment extends Fragment {

    public static PostFragment newInstance() {

        return new PostFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post, container, false);
        initView(view);
        return view;
    }

    protected void initView(View view) {
        ImageButton postTxt = (ImageButton) view.findViewById(R.id.post_text);
        ImageButton postImg = (ImageButton) view.findViewById(R.id.post_img);
        ImageButton postVideo = (ImageButton) view.findViewById(R.id.post_video);
        ImageButton postCamera = (ImageButton) view.findViewById(R.id.post_camera);
        ImageButton postExpose = (ImageButton) view.findViewById(R.id.post_expose);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                switch (v.getId()) {
//                    case R.id.post_text:            //发表文字
//
//                        break;
//                    case R.id.post_img:             //发表图片
//
//                        break;
//                    case R.id.post_video:           //发表视频
//
//                        break;
//                    case R.id.post_camera:          //使用相机
//
//                        break;
//                    case R.id.post_expose:          //发表评论
//
//                        break;
//                    default:
//                        break;
//
//                }

                startActivity(new Intent(getActivity(), PostActivity.class));
            }
        };

        postTxt.setOnClickListener(onClickListener);
        postImg.setOnClickListener(onClickListener);
        postVideo.setOnClickListener(onClickListener);
        postCamera.setOnClickListener(onClickListener);
        postExpose.setOnClickListener(onClickListener);
    }
}
