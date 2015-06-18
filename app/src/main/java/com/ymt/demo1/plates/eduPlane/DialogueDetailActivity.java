package com.ymt.demo1.plates.eduPlane;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.ymt.demo1.R;
import com.ymt.demo1.beams.edu.EduDialogueInfo;
import com.ymt.demo1.customViews.MyTitle;
import com.ymt.demo1.main.BaseFloatActivity;

/**
 * Created by Dan on 2015/5/20
 */
public class DialogueDetailActivity extends BaseFloatActivity {
    private EduDialogueInfo eduDialogueInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialogue_detail);
        eduDialogueInfo = getIntent().getBundleExtra("eduDialogueInfo").getParcelable("eduDialogueInfo");
        initTitle();
        initView();
    }

    protected void initTitle() {
        final MyTitle title = (MyTitle) findViewById(R.id.my_title);
        title.setTitleStyle(MyTitle.TitleStyle.RIGHT_ICON_L);
        title.setOnLeftActionClickListener(new MyTitle.OnLeftActionClickListener() {
            @Override
            public void onClick() {
                finish();
            }
        });

        title.setOnRightActionClickListener(new MyTitle.OnRightActionClickListener() {
            @Override
            public void onRightLClick() {
                //弹出分享界面
                //todo 分享内容
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("*/*");
                intent.putExtra(Intent.EXTRA_SUBJECT, "消防答疑-" + eduDialogueInfo.getQuestion());
                intent.putExtra(Intent.EXTRA_TEXT, eduDialogueInfo.getAnswer());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(Intent.createChooser(intent, getTitle()));
            }

            @Override
            public void onRightRClick() {
                //此视图，右边只包含L按钮

            }
        });
    }

    protected void initView() {
        TextView questContent = (TextView) findViewById(R.id.quest_content);
        TextView answerContent = (TextView) findViewById(R.id.answer_content);
        questContent.setText(eduDialogueInfo.getQuestion());
        answerContent.setText(eduDialogueInfo.getAnswer());

    }
}
