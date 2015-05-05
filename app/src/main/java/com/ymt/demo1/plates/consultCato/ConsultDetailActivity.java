package com.ymt.demo1.plates.consultCato;

import android.content.Intent;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.ymt.demo1.R;
import com.ymt.demo1.baseClasses.BaseActivity;
import com.ymt.demo1.customViews.MyTitle;

/**
 * Created by Dan on 2015/5/4
 */
public class ConsultDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consult_detail);
        initTitle();
        initView();
    }

    /**
     * ��ʼ��title �� action�¼�
     */
    protected void initTitle() {
        final MyTitle title = (MyTitle) findViewById(R.id.my_title);
        title.setTitleStyle(MyTitle.TitleStyle.RIGHT_ICON_L);

        title.updateCenterTitle(getSearchedKeyWord());     //����title
        title.setOnLeftActionClickListener(new MyTitle.OnLeftActionClickListener() {
            @Override
            public void onClick() {
                finish();
            }
        });

        title.setOnRightActionClickListener(new MyTitle.OnRightActionClickListener() {
            @Override
            public void onRightLClick() {
                //�����������
                Toast.makeText(ConsultDetailActivity.this, "do share...", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRightRClick() {
                //����ͼ���ұ�ֻ����L��ť

            }
        });
    }

    /**
     * ��ȡ�����ؼ���
     */
    protected String getSearchedKeyWord() {
        Intent intent = getIntent();
        return intent.getStringExtra("search_key_word");
    }

    /**
     * �������,��ʼ���ؼ�
     */
    protected void initView() {
        TextView title = (TextView) findViewById(R.id.title);
        TextView content = (TextView) findViewById(R.id.content);
//        GridView hotConsults = (GridView) findViewById(R.id.hot_consult_grid_view);
        Intent intent = getIntent();
        title.setText(intent.getStringExtra("title"));
        content.setText(intent.getStringExtra("content"));
    }
}
