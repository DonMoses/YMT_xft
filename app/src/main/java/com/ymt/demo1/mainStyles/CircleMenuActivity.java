package com.ymt.demo1.mainStyles;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ymt.demo1.R;

import com.ymt.demo1.customKeyBoard.ConsultActivity;
import com.ymt.demo1.main.CollectActivity;
import com.ymt.demo1.main.ShareActivity;
import com.ymt.demo1.main.setting.ManageAppearanceActivity;
import com.ymt.demo1.main.sign.SignInUpActivity;
import com.ymt.demo1.plates.consultCato.ConsultCatoMainActivity;
import com.ymt.demo1.plates.eduPlane.EduMainActivity;
import com.ymt.demo1.plates.exportConsult.ExportConsultMainActivity;
import com.ymt.demo1.plates.hub.FireHubMainActivity;
import com.ymt.demo1.plates.MoreCatoActivity;
import com.ymt.demo1.main.advice.AdviceActivity;
import com.ymt.demo1.main.help.HelpActivity;
import com.ymt.demo1.main.setting.SettingActivity;
import com.ymt.demo1.plates.knowledge.KnowledgeMainActivity;
import com.ymt.demo1.plates.news.NewsTabActivity;
import com.ymt.demo1.plates.personal.PersonalPagerTabActivity;
import com.zhy.view.CircleMenuLayout;

import org.litepal.tablemanager.Connector;

/**
 * Created by Moses on 2015
 */
public class CircleMenuActivity extends Activity implements View.OnTouchListener, View.OnClickListener, ManageAppearanceActivity.StyleChangeListener {

    private ImageView signIcon, adviceIcon, helpIcon, settingIcon, collectionIcon;
    private TextView signText, adviceText, helpText, settingText, collectionText;
    public static ManageAppearanceActivity.StyleChangeListener styleChangeListener;

    private String[] titles = new String[]{"", "", "", "", "", "", "", ""};
    private int[] titleIcons = new int[]{R.drawable.icon_per_center,
            R.drawable.icon_more, R.drawable.icon_fire_hub,
            R.drawable.icon_fire_learning, R.drawable.icon_consult_cato,
            R.drawable.icon_edu_plane, R.drawable.icon_fire_expert,
            R.drawable.icon_fire_news};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Connector.getDatabase();
        styleChangeListener = this;
        setContentView(R.layout.activity_circle_menu);
        initViews();
    }

    /**
     * 初始化原型菜单组件
     */
    protected void initViews() {
        final CircleMenuLayout circleMenuLayout = (CircleMenuLayout) findViewById(R.id.id_menulayout);

        circleMenuLayout.setMenuItemIconsAndTexts(titleIcons, titles);

        /*
        圆形菜单item 单击事件， 分别跳转到相应界面
         */
        circleMenuLayout.setOnMenuItemClickListener(new CircleMenuLayout.OnMenuItemClickListener() {
            @Override
            public void itemClick(View view, int pos) {
                switch (pos) {
                    case 0:
                        //如果新用户，则跳转到注册。如果旧用户，则跳转个人界面。
                        startActivity(new Intent(CircleMenuActivity.this, PersonalPagerTabActivity.class));  //个人界面
                        break;
                    case 1:
                        startActivity(new Intent(CircleMenuActivity.this, MoreCatoActivity.class));          //更多
                        break;
                    case 2:
                        startActivity(new Intent(CircleMenuActivity.this, FireHubMainActivity.class));   //论坛
                        break;
                    case 3:
                        startActivity(new Intent(CircleMenuActivity.this, KnowledgeMainActivity.class));      //知识平台
                        break;
                    case 4:
                        startActivity(new Intent(CircleMenuActivity.this, ConsultCatoMainActivity.class));     //咨询分类
                        break;
                    case 5:
                        startActivity(new Intent(CircleMenuActivity.this, EduMainActivity.class));          //教育平台
                        break;
                    case 6:
                        startActivity(new Intent(CircleMenuActivity.this, ExportConsultMainActivity.class));     //专家咨询
                        break;
                    case 7:
                        startActivity(new Intent(CircleMenuActivity.this, NewsTabActivity.class));          //资讯平台
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

        View menuShare = findViewById(R.id.share_layout);
        View menuAdvice = findViewById(R.id.advice_layout);
        View menuHelp = findViewById(R.id.help_layout);
        View menuSetting = findViewById(R.id.setting_layout);
        View menuCollect = findViewById(R.id.collect_layout);

        signIcon = (ImageView) menuShare.findViewById(R.id.share_icon);
        signText = (TextView) menuShare.findViewById(R.id.share_text);
        adviceIcon = (ImageView) menuAdvice.findViewById(R.id.advice_icon);
        adviceText = (TextView) menuAdvice.findViewById(R.id.advice_text);
        helpIcon = (ImageView) menuHelp.findViewById(R.id.help_icon);
        helpText = (TextView) menuHelp.findViewById(R.id.help_text);
        settingIcon = (ImageView) menuSetting.findViewById(R.id.setting_icon);
        settingText = (TextView) menuSetting.findViewById(R.id.setting_text);
        collectionIcon = (ImageView) findViewById(R.id.collect_icon);
        collectionText = (TextView) findViewById(R.id.collect_text);

        menuShare.setOnTouchListener(this);
        menuAdvice.setOnTouchListener(this);
        menuHelp.setOnTouchListener(this);
        menuSetting.setOnTouchListener(this);
        menuCollect.setOnTouchListener(this);
        menuShare.setOnClickListener(this);
        menuAdvice.setOnClickListener(this);
        menuHelp.setOnClickListener(this);
        menuSetting.setOnClickListener(this);
        menuCollect.setOnClickListener(this);

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        /*
        触摸时改变tab 背景 和字体颜色
         */
        if (event.getAction() == MotionEvent.ACTION_UP) {
            switch (v.getId()) {
                case R.id.share_layout:
                    signIcon.setImageResource(R.drawable.icon_share_grey);
                    signText.setTextColor(getResources().getColor(android.R.color.darker_gray));
                    break;
                case R.id.advice_layout:
                    adviceIcon.setImageResource(R.drawable.icon_suggest);
                    adviceText.setTextColor(getResources().getColor(android.R.color.darker_gray));
                    break;
                case R.id.help_layout:
                    helpIcon.setImageResource(R.drawable.icon_help);
                    helpText.setTextColor(getResources().getColor(android.R.color.darker_gray));
                    break;
                case R.id.setting_layout:
                    settingIcon.setImageResource(R.drawable.icon_setup);
                    settingText.setTextColor(getResources().getColor(android.R.color.darker_gray));
                    break;
                case R.id.collect_layout:
                    collectionIcon.setImageResource(R.drawable.icon_collect);
                    collectionText.setTextColor(getResources().getColor(android.R.color.darker_gray));
                default:
                    break;
            }

        }

        /*
        触摸释放后改变 tab背景和 字体颜色
         */
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            switch (v.getId()) {
                case R.id.share_layout:
                    signIcon.setImageResource(R.drawable.icon_share_click);
                    signText.setTextColor(getResources().getColor(android.R.color.holo_blue_bright));
                    break;
                case R.id.advice_layout:
                    adviceIcon.setImageResource(R.drawable.icon_suggest_click);
                    adviceText.setTextColor(getResources().getColor(android.R.color.holo_blue_bright));
                    break;
                case R.id.help_layout:
                    helpIcon.setImageResource(R.drawable.icon_help_cilck);
                    helpText.setTextColor(getResources().getColor(android.R.color.holo_blue_bright));
                    break;
                case R.id.setting_layout:
                    settingIcon.setImageResource(R.drawable.icon_setup_click);
                    settingText.setTextColor(getResources().getColor(android.R.color.holo_blue_bright));
                    break;
                case R.id.collect_layout:
                    collectionIcon.setImageResource(R.drawable.icon_collect_click);
                    collectionText.setTextColor(getResources().getColor(android.R.color.holo_blue_bright));
                default:
                    break;
            }

        }

        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.share_layout:
                //跳转到注册界面
                startActivity(new Intent(this, ShareActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                break;
            case R.id.advice_layout:
                //跳转到意见反馈
                startActivity(new Intent(this, AdviceActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                break;
            case R.id.help_layout:
                //跳转到帮助中心
                startActivity(new Intent(this, HelpActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                break;
            case R.id.setting_layout:
                //跳转到设置
                startActivity(new Intent(this, SettingActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                break;
            case R.id.collect_layout:
                //跳转到收藏
                startActivity(new Intent(this, CollectActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                //todo 这里放入一个测试界面，方便开发中测验。 最后修改为收藏界面

                break;
            default:
                break;
        }
    }

    @Override
    public void onStyleChanged() {
        finish();
    }
}
