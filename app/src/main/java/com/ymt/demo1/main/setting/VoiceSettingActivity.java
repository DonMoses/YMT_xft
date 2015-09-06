package com.ymt.demo1.main.setting;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.ymt.demo1.R;
import com.ymt.demo1.baseClasses.BaseFloatActivity;
import com.ymt.demo1.customViews.MyTitle;

/**
 * Created by DonMoses on 2015/9/6
 */
public class VoiceSettingActivity extends BaseFloatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_setting);
        initTitle();
        initView();
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
    
    private void initView() {
        ListView settingListView = (ListView) findViewById(R.id.voice_setting_list_view);
        String[] array = getResources().getStringArray(R.array.voice_settings);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.item_voice_setting, R.id.setting_text_view, array);
        settingListView.setAdapter(adapter);

        settingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //todo 消息声音设置里的各个开关


            }
        });

    }
}
