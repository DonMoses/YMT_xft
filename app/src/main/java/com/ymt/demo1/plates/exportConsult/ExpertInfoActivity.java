package com.ymt.demo1.plates.exportConsult;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.ymt.demo1.R;
import com.ymt.demo1.baseClasses.BaseActivity;
import com.ymt.demo1.beams.expert_consult.Expert;
import com.ymt.demo1.customViews.CircleImageView;
import com.ymt.demo1.customViews.MyTitle;
import com.ymt.demo1.main.BaseURLUtil;

/**
 * Created by Dan on 2015/5/13
 */
public class ExpertInfoActivity extends BaseActivity {
    private Expert expert;

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
        /*
         * 获取从上一activity 传过来的专家信息
         */
        expert = getIntent().getParcelableExtra("expert_info");
        /*
        todo 补充专家信息
         */

        //todo 最新问题

        final MyTitle title = (MyTitle) findViewById(R.id.my_title);
        title.setTitleStyle(MyTitle.TitleStyle.LEFT_ICON);
        title.updateCenterTitle(expert.getUser_name());          //根据专家名修改title
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
        Picasso.with(this).load(expert.getHead_pic()).into(exportHeader);
        //专家名
        TextView exportName = (TextView) findViewById(R.id.export_name);
        exportName.setText(expert.getUser_name());
        //专家生日
        TextView exportAge = (TextView) findViewById(R.id.export_age);
//        exportAge.setText(expert.getBio());
        //专家职业
        TextView exportMajor = (TextView) findViewById(R.id.export_major);
        exportMajor.setText(expert.getMajor_works());
        //关注按钮
        final TextView follow = (TextView) findViewById(R.id.follow_export);

        //todo 关注

        //专家简介
        TextView selfResume = (TextView) findViewById(R.id.self_resume_content);
//        selfResume.setText(expert.getResume());
        //团队简介
        TextView groupResume = (TextView) findViewById(R.id.group_resume_content);
        groupResume.setText(expert.getCapacity());
        //最近问题1，2，3，4
        TextView questA = ((TextView) findViewById(R.id.quest_a));
        TextView questB = ((TextView) findViewById(R.id.quest_b));
        TextView questC = ((TextView) findViewById(R.id.quest_c));
        TextView questD = ((TextView) findViewById(R.id.quest_d));

        //todo 最近咨询的问题
//        ArrayList<String> quests = expert.getRecentQuests();
//        int questsSize = quests.size();
//        switch (questsSize) {
//            case 0:
//                questA.setText("该专家目前没有处理中的问题。");
//                break;
//            case 1:
//                questA.setText(quests.get(0));
//                break;
//            case 2:
//                questA.setText(quests.get(0));
//                questB.setText(quests.get(1));
//                break;
//            case 3:
//                questA.setText(quests.get(0));
//                questB.setText(quests.get(1));
//                questC.setText(quests.get(2));
//                break;
//            case 4:
//                questA.setText(quests.get(0));
//                questB.setText(quests.get(1));
//                questC.setText(quests.get(2));
//                questD.setText(quests.get(3));
//                break;
//            default:
//
//                break;
//        }

//        //成功案例1，2，3，4
//        TextView caseA = (TextView) findViewById(R.id.case_a);
//        TextView caseB = (TextView) findViewById(R.id.case_b);
//        TextView caseC = (TextView) findViewById(R.id.case_c);
//        TextView caseD = (TextView) findViewById(R.id.case_d);
        //立即咨询layout、icon
        View consultLayout = findViewById(R.id.consult_action_layout);
        final ImageButton consultImg = (ImageButton) findViewById(R.id.consult_img);
        /*
        点击事件监听
         */
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.follow_export:

                        //todo 关注

                        break;
                    case R.id.consult_action_layout:
                        //todo 咨询会话界面
                        Intent intent = new Intent(ExpertInfoActivity.this, ExportConsultNowActivity.class);
                        intent.putExtra("expert_info", expert);
                        startActivity(intent);
                        finish();
                        break;
                    default:
                        break;

                }
            }
        };

        follow.setOnClickListener(onClickListener);
        consultLayout.setOnClickListener(onClickListener);

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
                    default:
                        break;
                }
                return false;
            }
        };
        consultLayout.setOnTouchListener(onTouchListener);
    }


}
