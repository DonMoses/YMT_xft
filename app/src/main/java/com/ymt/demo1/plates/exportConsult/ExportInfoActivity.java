package com.ymt.demo1.plates.exportConsult;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ymt.demo1.R;
import com.ymt.demo1.baseClasses.BaseActivity;
import com.ymt.demo1.beams.Export;
import com.ymt.demo1.customViews.CircleImageView;
import com.ymt.demo1.customViews.MyTitle;


import java.util.ArrayList;

/**
 * Created by Dan on 2015/5/13
 */
public class ExportInfoActivity extends BaseActivity {
    private Export export;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export_info);
        initTitle();
        initView();
    }

    /**
     * 设置顶部title
     */
    protected void initTitle() {
        //todo 根据专家信息，设置title 标题【专家姓名】
        /*
         * 获取从上一activity 传过来的专家信息
         */
        Bundle bundle = getIntent().getBundleExtra("export_info");
        export = bundle.getParcelable("export_info");
        /*
        todo 模拟补充专家信息
         */
        export.setSelfResume("self resume：朱良辉，男，汉族，1942-05-29生于大连市，1960-08毕业于大连市第一高中，" +
                "1965-07毕业于大连工学院（现为：大连理工大学）化学工程系燃料化学工学专业，大学5年本科毕业学历。");
        export.setTeamResume("group resume：在全国建筑给水排水委员会的任职和兼职（标委会常委和分会理事长），主要是为全国的设计单位服务，" +
                "并在建筑给水排水两委会的领导下为全国的设计单位和消防部门搭建跨省的学术/技术交流平台。作为消防部门的代表，" +
                "应当尽心尽力、尽职尽责、切实地起到密切联络、有机协调的桥梁作用。");
        //todo 模拟最新问题
        ArrayList<String> quests = new ArrayList<>();
        quests.add("quests example for test 1");
        quests.add("quests example for test 2");
        export.setRecentQuests(quests);

        final MyTitle title = (MyTitle) findViewById(R.id.my_title);
        title.setTitleStyle(MyTitle.TitleStyle.LEFT_ICON);
        title.updateCenterTitle(export.getName());          //根据专家名修改title
        title.invalidate();
        title.setOnLeftActionClickListener(new MyTitle.OnLeftActionClickListener() {
            @Override
            public void onClick() {
                finish();
            }
        });


    }

    protected void initView() {
        //专家头像
        CircleImageView exportHeader = (CircleImageView) findViewById(R.id.export_header);
        exportHeader.setImageBitmap(export.getIcon());
        //专家名
        TextView exportName = (TextView) findViewById(R.id.export_name);
        exportName.setText(export.getName());
        //专家生日
        TextView exportAge = (TextView) findViewById(R.id.export_age);
        exportAge.setText(export.getBirthDay());
        //专家职业
        TextView exportMajor = (TextView) findViewById(R.id.export_major);
        exportMajor.setText(export.getMajor());
        //关注按钮
        final TextView follow = (TextView) findViewById(R.id.follow_export);
        if (export.isFollowed()) {
            follow.setText("已关注");
        } else {
            follow.setText("+关注");
        }
        //专家简介
        TextView selfResume = (TextView) findViewById(R.id.self_resume_content);
        selfResume.setText(export.getSelfResume());
        //团队简介
        TextView groupResume = (TextView) findViewById(R.id.group_resume_content);
        groupResume.setText(export.getTeamResume());
        //最近问题1，2，3，4
        TextView questA = ((TextView) findViewById(R.id.quest_a));
        TextView questB = ((TextView) findViewById(R.id.quest_b));
        TextView questC = ((TextView) findViewById(R.id.quest_c));
        TextView questD = ((TextView) findViewById(R.id.quest_d));
        ArrayList<String> quests = export.getRecentQuests();
        int questsSize = quests.size();
        switch (questsSize) {
            case 0:
                questA.setText("该专家目前没有处理中的问题。");
                break;
            case 1:
                questA.setText(quests.get(0));
                break;
            case 2:
                questA.setText(quests.get(0));
                questB.setText(quests.get(1));
                break;
            case 3:
                questA.setText(quests.get(0));
                questB.setText(quests.get(1));
                questC.setText(quests.get(2));
                break;
            case 4:
                questA.setText(quests.get(0));
                questB.setText(quests.get(1));
                questC.setText(quests.get(2));
                questD.setText(quests.get(3));
                break;
            default:

                break;
        }

//        //成功案例1，2，3，4
//        TextView caseA = (TextView) findViewById(R.id.case_a);
//        TextView caseB = (TextView) findViewById(R.id.case_b);
//        TextView caseC = (TextView) findViewById(R.id.case_c);
//        TextView caseD = (TextView) findViewById(R.id.case_d);
        //立即咨询layout、icon
        View consultLayout = findViewById(R.id.consult_action_layout);
        final ImageButton consultImg = (ImageButton) findViewById(R.id.consult_img);
        //预约layout、icon
        View bookingLayout = findViewById(R.id.booking_action_layout);
        final ImageButton bookingImg = (ImageButton) findViewById(R.id.booking_img);
        /*
        点击事件监听
         */
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.follow_export:

                        //考虑使用 MyExport extends Export
                        if (export.isFollowed()) {
                            follow.setText("+关注");
                        } else {
                            follow.setText("已关注");
                        }
                        export.setIsFollowed(!export.isFollowed());

                        //todo 同步关注状态

                        break;
                    case R.id.consult_action_layout:
                        //todo 咨询会话界面
                        Intent intent = new Intent(ExportInfoActivity.this, ConsultChatActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("export_info", export);
                        intent.putExtra("export_info", bundle);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.booking_action_layout:
                        //todo 立即预约界面 【事件选择 。。。等】
                        Toast.makeText(ExportInfoActivity.this, "booking now...", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;

                }
            }
        };

        follow.setOnClickListener(onClickListener);
        consultLayout.setOnClickListener(onClickListener);
        bookingLayout.setOnClickListener(onClickListener);

        View.OnTouchListener onTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (v.getId()) {
                    case R.id.consult_action_layout:
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            consultImg.setImageBitmap(
                                    BitmapFactory.decodeResource(getResources(), R.drawable.icon_consult_pressed));
                        } else if (event.getAction() == MotionEvent.ACTION_UP) {
                            consultImg.setImageBitmap(
                                    BitmapFactory.decodeResource(getResources(), R.drawable.icon_consult_normal));
                        }
                        break;
                    case R.id.booking_action_layout:
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            bookingImg.setImageBitmap(
                                    BitmapFactory.decodeResource(getResources(), R.drawable.icon_booking_on));
                        } else if (event.getAction() == MotionEvent.ACTION_UP) {
                            bookingImg.setImageBitmap(
                                    BitmapFactory.decodeResource(getResources(), R.drawable.icon_booking_off));
                        }
                        break;
                    default:
                        break;
                }
                return false;
            }
        };
        consultLayout.setOnTouchListener(onTouchListener);
        bookingLayout.setOnTouchListener(onTouchListener);
    }


}
