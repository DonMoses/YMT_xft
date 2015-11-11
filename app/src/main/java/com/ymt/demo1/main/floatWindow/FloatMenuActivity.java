package com.ymt.demo1.main.floatWindow;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.ymt.demo1.R;
import com.ymt.demo1.utils.AppContext;
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
    /**
     * 记录小窗的位置
     */
    private WindowManager.LayoutParams params;
    /**
     * 是否拖动的开关
     */
    private boolean isMoved;

    int screenWidth = 0;
    int screenHeight = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.float_window_big);
        initView();
//
        Window window = getWindow();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
        window.setLayout(screenWidth, screenHeight);
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

        final CircleMenuLayout circleMenuLayout =
                (CircleMenuLayout) findViewById(R.id.float_circle_menu_layout);

        circleMenuLayout.setMenuItemIconsAndTexts(titleIcons, titles);
        ViewTreeObserver treeObserver = circleMenuLayout.getViewTreeObserver();
        treeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                circleMenuLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                viewHeight = circleMenuLayout.getWidth();
                viewWidth = circleMenuLayout.getHeight();
            }
        });

        /*
        圆形菜单item 单击事件， 分别跳转到相应界面
         */
        circleMenuLayout.setOnMenuItemClickListener(new CircleMenuLayout.OnMenuItemClickListener() {
            @Override
            public void itemClick(View view, int pos) {
                switch (pos) {
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
                        startActivity(new Intent(FloatMenuActivity.this, NewsTabActivity.class)); //资讯平台;
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

        //todo 浮动窗口拖动、释放效果
        //中心的view
        final ImageView centerView = (ImageView) findViewById(R.id.float_center_view);
        params = MyWindowManager.getSmallWindowParams();
        if (params == null) {
            params = new WindowManager.LayoutParams();
        }
        centerView.setOnTouchListener(new View.OnTouchListener() {
            float rawX1 = 0;
            float rawY1 = 0;
            float rawX2 = 0;
            float rawY2 = 0;
            float vX = 0;
            float vY = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        isMoved = false;
                        rawX1 = event.getRawX();
                        rawY1 = event.getRawY();
                        vX = circleMenuLayout.getX();
                        vY = circleMenuLayout.getY();
                        if (!circleMenuLayout.isNowTouchMove()) {
                            circleMenuLayout.setIsNowTouchMove(true);
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        isMoved = true;
                        rawX2 = event.getRawX();
                        rawY2 = event.getRawY();
                        circleMenuLayout.setX(vX + (rawX2 - rawX1));
                        circleMenuLayout.setY(vY + (rawY2 - rawY1));
                        //设置小悬浮窗的位置跟随大悬浮窗位置变动
                        params.x = (int) (rawX2);
                        params.y = (int) (rawY2);
                        MyWindowManager.setSmallWindowParams(params);
                        break;
                    case MotionEvent.ACTION_UP:
                        if ((circleMenuLayout.getX() < -0.4 * viewWidth)
                                || (circleMenuLayout.getX() > screenWidth - 0.6 * viewWidth)
                                || (circleMenuLayout.getY() < -0.4 * viewHeight)
                                || (circleMenuLayout.getY() > screenHeight - getStatusBarHeight() - 0.6 * viewHeight)) {
                            MyWindowManager.removeBigWindow();
                            MyWindowManager.removeSmallWindow(AppContext.getContext());
                            MyWindowManager.createSmallWindow(AppContext.getContext());
                        }
                        if (circleMenuLayout.isNowTouchMove()) {
                            circleMenuLayout.setIsNowTouchMove(false);
                        }
                        break;
                    default:
                        break;
                }
                return isMoved;
            }
        });

        centerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyWindowManager.removeBigWindow();
                MyWindowManager.removeSmallWindow(AppContext.getContext());
                MyWindowManager.createSmallWindow(AppContext.getContext());
            }
        });

    }

    private int getStatusBarHeight() {
        Resources resources = this.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        return resources.getDimensionPixelSize(resourceId);
    }
}
