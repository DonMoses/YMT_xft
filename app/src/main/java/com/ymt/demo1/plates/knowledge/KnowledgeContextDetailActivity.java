package com.ymt.demo1.plates.knowledge;

import android.os.Bundle;
import android.view.Gravity;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.ymt.demo1.R;
import com.ymt.demo1.baseClasses.BaseActivity;
import com.ymt.demo1.customViews.MyTitle;
import com.ymt.demo1.main.PopActionListener;
import com.ymt.demo1.plates.PopActionUtil;

/**
 * Created by Dan on 2015/4/29
 */
public class KnowledgeContextDetailActivity extends BaseActivity {
    private PopActionListener actionListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_knowledge_content_detail);
        initTitle();
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

        actionListener = new PopActionListener() {
            @Override
            public void onAction(String action) {
                switch (action) {
                    case "action1":
                        Toast.makeText(KnowledgeContextDetailActivity.this, "action1", Toast.LENGTH_SHORT).show();
                        break;
                    case "action2":
                        Toast.makeText(KnowledgeContextDetailActivity.this, "action2", Toast.LENGTH_SHORT).show();
                        break;
                    case "action3":
                        Toast.makeText(KnowledgeContextDetailActivity.this, "action3", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onDismiss() {

            }
        };

        title.setOnRightActionClickListener(new MyTitle.OnRightActionClickListener() {
            @Override
            public void onRightLClick() {
                PopActionUtil popActionUtil = PopActionUtil.getInstance(KnowledgeContextDetailActivity.this);
                popActionUtil.setActions(new String[]{"action1", "action2", "action3"});
                PopupWindow popupWindow = popActionUtil.getPopActionMenu();
                popupWindow.showAtLocation(title.getRootView(),
                        Gravity.TOP | Gravity.END, 10, 100);

                popActionUtil.setActionListener(actionListener);

            }

            @Override
            public void onRightRClick() {
                //此视图，右边只包含L按钮

            }
        });
    }

    protected void initView() {


    }


}
