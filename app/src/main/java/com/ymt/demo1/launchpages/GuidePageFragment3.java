package com.ymt.demo1.launchpages;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.ymt.demo1.R;
import com.ymt.demo1.main.sign.SignInFragment;
import com.ymt.demo1.main.sign.SignUpFragment;

/**
 * Created by Moses on 2015
 */
public class GuidePageFragment3 extends Fragment {

    private int imageId;

    public static GuidePageFragment3 newInstance(int image_id) {
        GuidePageFragment3 guidePageFragment = new GuidePageFragment3();
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
        View view = inflater.inflate(R.layout.fragment_guide_page3, container, false);
        initView(view);
        return view;

    }

    protected void initView(View view) {
        ImageView imageView = (ImageView) view.findViewById(R.id.simple_image_view);
        imageView.setBackgroundResource(imageId);

        Button signIn = (Button) view.findViewById(R.id.guide_sign_in);
        Button signUp = (Button) view.findViewById(R.id.guide_sign_up);
        Button tryNow = (Button) view.findViewById(R.id.guide_try_now);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.guide_sign_in:
                        startActivity(new Intent(getActivity(), SignInFragment.class));
                        getActivity().finish();
                        break;
                    case R.id.guide_sign_up:
                        startActivity(new Intent(getActivity(), SignUpFragment.class));
                        getActivity().finish();
                        break;
                    case R.id.guide_try_now:
                        startActivity(new Intent(getActivity(), LoadingPageActivity.class));
                        getActivity().finish();
                        break;
                    default:
                        break;
                }
            }
        };
        signIn.setOnClickListener(listener);
        signUp.setOnClickListener(listener);
        tryNow.setOnClickListener(listener);

    }
}
