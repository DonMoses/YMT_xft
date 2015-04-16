package com.ymt.demo1.guidepages;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ymt.demo1.R;

/**
 * Created by Moses on 2015
 */
public class GuidePageFragment extends Fragment {

    private int imageId;

    public static GuidePageFragment newInstance(int image_id) {
        GuidePageFragment guidePageFragment = new GuidePageFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("image_id", image_id);
        guidePageFragment.setArguments(bundle);
        return guidePageFragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        imageId = bundle.getInt("image_id");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_simple_guide_page, container, false);
        initView(view);
        return view;

    }

    protected void initView(View view) {
        ImageView imageView = (ImageView) view.findViewById(R.id.simple_image_view);
        imageView.setBackgroundResource(imageId);

    }
}
