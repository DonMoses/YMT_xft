package com.ymt.demo1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ymt.demo1.mainPages.AdviceActivity;
import com.ymt.demo1.mainPages.HelpActivity;
import com.ymt.demo1.mainPages.SignInActivity;
import com.ymt.demo1.mainPages.SignUpActivity;
import com.zhy.view.CircleMenuLayout;

/**
 * Created by Moses on 2015
 */
public class CircleMenuActivity extends Activity implements View.OnTouchListener, View.OnClickListener {

    private ImageView signIcon, adviceIcon, helpIcon, settingIcon;
    private TextView signText, adviceText, helpText, settingText;

    private String[] titles = new String[]{"个人中心", "更多内容", "消防论坛", "知识平台", "专家咨询", "资讯平台", "我的收藏"};
    private int[] titleIcons = new int[]{R.drawable.icon_personal_center,
            R.drawable.icon_search_more, R.drawable.icon_fire_hub,
            R.drawable.icon_fire_learning, R.drawable.icon_fire_expert,
            R.drawable.icon_fire_consult, R.drawable.icon_fire_consult};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_menu);
        initViews();
    }

    /**
     * 初始化原型菜单组件
     */
    protected void initViews() {
        CircleMenuLayout circleMenuLayout = (CircleMenuLayout) findViewById(R.id.id_menulayout);

        circleMenuLayout.setMenuItemIconsAndTexts(titleIcons, titles);
        circleMenuLayout.setOnMenuItemClickListener(new CircleMenuLayout.OnMenuItemClickListener() {
            @Override
            public void itemClick(View view, int pos) {
                switch (pos) {
                    case 0:
                        startActivity(new Intent(CircleMenuActivity.this, SignInActivity.class));
                        break;
                    case 1:

                        break;
                    case 2:

                        break;
                    case 3:
                        break;
                    case 4:

                        break;
                    case 5:

                        break;
                    case 6:

                        break;
                    default:
                        break;

                }
                Toast.makeText(CircleMenuActivity.this, titles[pos], Toast.LENGTH_SHORT).show();
            }

            @Override
            public void itemCenterClick(View view) {
                Toast.makeText(CircleMenuActivity.this, "欢迎来到消防云中心", Toast.LENGTH_SHORT).show();
            }
        });

        View menuSign = findViewById(R.id.sign_layout);
        View menuAdvice = findViewById(R.id.advice_layout);
        View menuHelp = findViewById(R.id.help_layout);
        View menuSetting = findViewById(R.id.setting_layout);

        signIcon = (ImageView) menuSign.findViewById(R.id.sign_icon);
        signText = (TextView) menuSign.findViewById(R.id.sign_text);
        adviceIcon = (ImageView) menuAdvice.findViewById(R.id.advice_icon);
        adviceText = (TextView) menuAdvice.findViewById(R.id.advice_text);
        helpIcon = (ImageView) menuHelp.findViewById(R.id.help_icon);
        helpText = (TextView) menuHelp.findViewById(R.id.help_text);
        settingIcon = (ImageView) menuSetting.findViewById(R.id.setting_icon);
        settingText = (TextView) menuSetting.findViewById(R.id.setting_text);

        menuSign.setOnTouchListener(this);
        menuAdvice.setOnTouchListener(this);
        menuHelp.setOnTouchListener(this);
        menuSetting.setOnTouchListener(this);
        menuSign.setOnClickListener(this);
        menuAdvice.setOnClickListener(this);
        menuHelp.setOnClickListener(this);
        menuSetting.setOnClickListener(this);

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            switch (v.getId()) {
                case R.id.sign_layout:
                    signIcon.setImageResource(R.drawable.icon_sign_pressed);
                    signText.setTextColor(getResources().getColor(android.R.color.holo_blue_bright));
                    break;
                case R.id.advice_layout:
                    adviceIcon.setImageResource(R.drawable.icon_advice_pressed);
                    adviceText.setTextColor(getResources().getColor(android.R.color.holo_blue_bright));
                    break;
                case R.id.help_layout:
                    helpIcon.setImageResource(R.drawable.icon_help_pressed);
                    helpText.setTextColor(getResources().getColor(android.R.color.holo_blue_bright));
                    break;
                case R.id.setting_layout:
                    settingIcon.setImageResource(R.drawable.icon_setting_pressed);
                    settingText.setTextColor(getResources().getColor(android.R.color.holo_blue_bright));
                    break;
                default:
                    break;
            }

        }

        if (event.getAction() == MotionEvent.ACTION_UP) {
            switch (v.getId()) {
                case R.id.sign_layout:
                    signIcon.setImageResource(R.drawable.icon_sign_normal);
                    signText.setTextColor(getResources().getColor(android.R.color.darker_gray));
                    break;
                case R.id.advice_layout:
                    adviceIcon.setImageResource(R.drawable.icon_advice_normal);
                    adviceText.setTextColor(getResources().getColor(android.R.color.darker_gray));
                    break;
                case R.id.help_layout:
                    helpIcon.setImageResource(R.drawable.icon_help_normal);
                    helpText.setTextColor(getResources().getColor(android.R.color.darker_gray));
                    break;
                case R.id.setting_layout:
                    settingIcon.setImageResource(R.drawable.icon_setting_normal);
                    settingText.setTextColor(getResources().getColor(android.R.color.darker_gray));
                    break;
                default:
                    break;
            }

        }

        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_layout:
                startActivity(new Intent(this, SignUpActivity.class));
                break;
            case R.id.advice_layout:
                startActivity(new Intent(this, AdviceActivity.class));
                break;
            case R.id.help_layout:
                startActivity(new Intent(this, HelpActivity.class));
                break;
            case R.id.setting_layout:

                break;
            default:
                break;
        }
    }
}
