package com.ymt.demo1.main.setting;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ymt.demo1.R;
import com.ymt.demo1.baseClasses.BaseFloatActivity;
import com.ymt.demo1.customViews.IOSSwitchView;
import com.ymt.demo1.customViews.MyTitle;
import com.ymt.demo1.utils.AppContext;

/**
 * Created by DonMoses on 2015/9/6
 */
public class VoiceSettingActivity extends BaseFloatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
         * 启动程序后【到AppContext中去获取设置】， 检测本活动保存的消息设置
         *  AppContext 注册了VoiceSettingActivity类中的设置改变监听
         */
        setVoiceSettingListener((AppContext) super.getApplication());
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
        VoiceSettingAdapter adapter = new VoiceSettingAdapter(this);
        adapter.setArray(array);
        settingListView.setAdapter(adapter);

        settingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //todo 消息声音设置里的各个开关
                IOSSwitchView switchView = (IOSSwitchView) view.findViewById(R.id.setting_switch_view);
                switchView.callOnClick();
            }
        });

    }

    private class VoiceSettingAdapter extends BaseAdapter {
        private String[] array;
        private LayoutInflater inflater;

        public VoiceSettingAdapter(Context context) {
            this.inflater = LayoutInflater.from(context);
        }

        public void setArray(String[] array) {
            this.array = array;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return array.length;
        }

        @Override
        public Object getItem(int position) {
            return array[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_voice_setting, null);
                holder = new ViewHolder();
                holder.settingName = (TextView) convertView.findViewById(R.id.setting_text_view);
                holder.settingSwitch = (IOSSwitchView) convertView.findViewById(R.id.setting_switch_view);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.settingName.setText(array[position]);
            holder.settingSwitch.setCheckedKey(array[position]);
            return convertView;
        }

        class ViewHolder {
            TextView settingName;
            IOSSwitchView settingSwitch;
        }
    }

    private VoiceSettingListener voiceSettingListener;

    public void setVoiceSettingListener(VoiceSettingListener voiceSettingListener) {
        this.voiceSettingListener = voiceSettingListener;
    }

    public interface VoiceSettingListener {
        void onSettingDone();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //退出设置界面后，使用回调将设置变更事件通知注册了监听的对象。【这里是AppContext也就是全局使用的Application】
        voiceSettingListener.onSettingDone();
    }
}
