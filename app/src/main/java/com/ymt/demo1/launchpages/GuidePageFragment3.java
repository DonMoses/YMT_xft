package com.ymt.demo1.launchpages;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.ymt.demo1.R;
import com.ymt.demo1.mainStyles.CircleMenuActivity;
import com.ymt.demo1.mainStyles.NavigationMenuActivity;
import com.ymt.demo1.mainStyles.TabMenuActivity;

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

        Button tryNow = (Button) view.findViewById(R.id.guide_try_now);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.guide_try_now:
                        chooseLaunchStyle();
                        break;
                    default:
                        break;
                }
            }
        };
        tryNow.setOnClickListener(listener);
    }

    protected void chooseLaunchStyle() {
        //屏幕尺寸这这里保存到AppContext供全局使用
        SharedPreferences sharedPreferences =
                getActivity().getSharedPreferences(MainActivity.SETTING_PREFERENCES, Activity.MODE_PRIVATE);
        int style = sharedPreferences.getInt(MainActivity.LAUNCH_STYLE_KEY, 0);
        switch (style) {
            case MainActivity.LAUNCH_STYLE_CIRCLE_MODE:
                startActivity(new Intent(getActivity(), CircleMenuActivity.class));
                getActivity().finish();
                break;
            case MainActivity.LAUNCH_STYLE_SLIDE_MODE:
                startActivity(new Intent(getActivity(), NavigationMenuActivity.class));
                getActivity().finish();
                break;
            case MainActivity.LAUNCH_STYLE_TAB:
                startActivity(new Intent(getActivity(), TabMenuActivity.class));
                getActivity().finish();
                break;
            default:
                break;
        }
    }
}
