package com.ymt.demo1.main.setting;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.ymt.demo1.R;
import com.ymt.demo1.adapter.MyStyleAdapter;
import com.ymt.demo1.beams.XFTStyle;
import com.ymt.demo1.customViews.MyTitle;
import com.ymt.demo1.launchpages.MainActivity;
import com.ymt.demo1.mainStyles.CircleMenuActivity;
import com.ymt.demo1.mainStyles.NavigationMenuActivity;

import java.util.ArrayList;

/**
 * Created by Dan on 2015/4/3
 */
public class ManageAppearanceActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_appearance);
        initTitle();
        initView();

    }

    protected void initView() {
        SharedPreferences mSharedPreferences =
                getSharedPreferences(MainActivity.SETTING_PREFERENCES, MODE_PRIVATE);
        final SharedPreferences.Editor editor = mSharedPreferences.edit();

        //风格选择gridView
        GridView styleGird = (GridView) findViewById(R.id.style_setting_grid);
        MyStyleAdapter styleAdapter = new MyStyleAdapter(this);
        styleGird.setAdapter(styleAdapter);
        //style数据源
        ArrayList<XFTStyle> mStyles = new ArrayList<>();
        //转盘风格
        XFTStyle circleStyle = new XFTStyle();
        circleStyle.setStyleImg(BitmapFactory.decodeResource(getResources(), R.drawable.bg_eg_style_circle));
        circleStyle.setStyleTxt(getString(R.string.style_circle));
        //侧滑风格
        XFTStyle slideStyle = new XFTStyle();
        slideStyle.setStyleImg(BitmapFactory.decodeResource(getResources(), R.drawable.bg_eg_style_slide));
        slideStyle.setStyleTxt(getString(R.string.style_slide));
        //tab风格
        XFTStyle tabStyle = new XFTStyle();
        tabStyle.setStyleImg(BitmapFactory.decodeResource(getResources(), R.drawable.bg_eg_style_tab));
        tabStyle.setStyleTxt(getString(R.string.style_tab));
        mStyles.add(circleStyle);
        mStyles.add(slideStyle);
        mStyles.add(tabStyle);
        styleAdapter.setStyles(mStyles);

        //设置风格
        styleGird.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyStyleAdapter adapter = (MyStyleAdapter) parent.getAdapter();
                String style = ((XFTStyle) adapter.getItem(position)).getStyleTxt();
                switch (style) {
                    case "转盘风格":
                        Intent intent2 = new Intent();
                        intent2.setClass(ManageAppearanceActivity.this, CircleMenuActivity.class);
                        intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent2);
                        editor.putInt(MainActivity.LAUNCH_STYLE_KEY, MainActivity.LAUNCH_STYLE_CIRCLE_MODE);     //保存风格设置
                        editor.apply();
                        finish();
                        if (NavigationMenuActivity.styleChangeListener != null) {
                            NavigationMenuActivity.styleChangeListener.onStyleChanged();
                        }
                        break;
                    case "侧滑风格":
                        Intent intent1 = new Intent();
                        intent1.setClass(ManageAppearanceActivity.this, NavigationMenuActivity.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent1);
                        editor.putInt(MainActivity.LAUNCH_STYLE_KEY, MainActivity.LAUNCH_STYLE_SLIDE_MODE);      //保存风格设置
                        editor.apply();
                        finish();
                        if (CircleMenuActivity.styleChangeListener != null) {
                            CircleMenuActivity.styleChangeListener.onStyleChanged();
                        }
                        break;
                    case "选项卡风格":
                        Toast.makeText(ManageAppearanceActivity.this, "选项卡风格暂时不可用...", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;

                }
            }
        });

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

    public interface StyleChangeListener {
        void onStyleChanged();
    }


}
