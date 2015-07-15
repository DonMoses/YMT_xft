package com.ymt.demo1.main.floatWindow;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.ymt.demo1.R;
import com.ymt.demo1.main.AppContext;
import com.ymt.demo1.plates.MoreCatoActivity;
import com.ymt.demo1.plates.consultCato.ConsultCatoMainActivity;
import com.ymt.demo1.plates.eduPlane.EduMainActivity;
import com.ymt.demo1.plates.exportConsult.ExportConsultMainActivity;
import com.ymt.demo1.plates.hub.FireHubMainActivity;
import com.ymt.demo1.plates.knowledge.KnowledgeMainActivity;
import com.ymt.demo1.plates.news.NewsTabActivity;
import com.ymt.demo1.plates.personal.PersonalPagerTabActivity;
import com.zhy.view.CircleMenuLayout;

/**
 * Created by Dan on 2015/4/16
 */
public class FloatMenuActivity extends Activity {

    /**
     * 记录大悬浮窗的宽度
     */
    public static int viewWidth;

    /**
     * 记录大悬浮窗的高度
     */
    public static int viewHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.float_window_big);
        initView();

    }

    @Override
    protected void onResume() {
        AppContext.setFloatActivity(this);
        super.onResume();
    }

    @Override
    protected void onPause() {
        AppContext.setFloatActivity(null);
        super.onPause();
    }

    protected void initView() {
        final String[] titles = new String[]{"", "", "", "", "", "", "", ""};
        int[] titleIcons = new int[]{
                R.drawable.icon_float_more, R.drawable.icon_float_hub,
                R.drawable.icon_float_learning, R.drawable.icon_float_consult,
                R.drawable.icon_float_edu, R.drawable.icon_float_personal_cen,
                R.drawable.icon_float_news, R.drawable.icon_float_expert};

        CircleMenuLayout circleMenuLayout = (CircleMenuLayout) findViewById(R.id.float_circle_menu_layout);
        viewWidth = circleMenuLayout.getLayoutParams().width;
        viewHeight = circleMenuLayout.getLayoutParams().height;

        circleMenuLayout.setMenuItemIconsAndTexts(titleIcons, titles);

        /*
        圆形菜单item 单击事件， 分别跳转到相应界面
         */
        circleMenuLayout.setOnMenuItemClickListener(new CircleMenuLayout.OnMenuItemClickListener() {
            @Override
            public void itemClick(View view, int pos) {
                switch (pos) {
//                    case 0:
//                        //如果新用户，则跳转到注册。如果旧用户，则跳转个人界面。
//                        MyWindowManager.removeBigWindow();
//                        startActivity(
//                                new Intent(FloatMenuActivity.this, PersonalPagerTabActivity.class));  //个人界面
//                        break;
                    case 0:
                        MyWindowManager.removeBigWindow();
                        startActivity(
                                new Intent(FloatMenuActivity.this, MoreCatoActivity.class));          //更多
                        break;
                    case 1:
                        MyWindowManager.removeBigWindow();
                        startActivity(
                                new Intent(FloatMenuActivity.this, FireHubMainActivity.class));   //论坛
                        break;
                    case 2:
                        MyWindowManager.removeBigWindow();
                        startActivity(
                                new Intent(FloatMenuActivity.this, KnowledgeMainActivity.class));    //知识平台
                        break;
                    case 3:
                        MyWindowManager.removeBigWindow();
                        startActivity(
                                new Intent(FloatMenuActivity.this, ConsultCatoMainActivity.class));  //咨询分类
                        break;
                    case 4:
                        MyWindowManager.removeBigWindow();
                        startActivity(
                                new Intent(FloatMenuActivity.this, EduMainActivity.class));          //教育平台
                        break;
                    case 5:
                        MyWindowManager.removeBigWindow();
                        startActivity(
                                new Intent(FloatMenuActivity.this, PersonalPagerTabActivity.class)); //个人中心;
                        break;
                    case 6:
                        MyWindowManager.removeBigWindow();
                        startActivity( new Intent(FloatMenuActivity.this, NewsTabActivity.class)); //资讯平台;
                        break;
                    case 7:
                        MyWindowManager.removeBigWindow();
                        startActivity(
                                new Intent(FloatMenuActivity.this, ExportConsultMainActivity.class)); //专家咨询;
                        break;
                    default:
                        Toast.makeText(AppContext.getContext(), titles[pos], Toast.LENGTH_SHORT).show();
                        break;

                }

            }

            @Override
            public void itemCenterClick(View view) {
                // 点击中心的时候，移除大悬浮窗，创建小悬浮窗
                MyWindowManager.removeBigWindow();
                MyWindowManager.removeSmallWindow(AppContext.getContext());
                MyWindowManager.createSmallWindow(AppContext.getContext());
            }
        });

    }
}
