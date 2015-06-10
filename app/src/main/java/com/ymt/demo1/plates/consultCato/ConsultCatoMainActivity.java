package com.ymt.demo1.plates.consultCato;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;

import com.ymt.demo1.R;
import com.ymt.demo1.adapter.SimpleExpandListAdapter;
import com.ymt.demo1.baseClasses.BaseActivity;
import com.ymt.demo1.customViews.MyTitle;
import com.ymt.demo1.main.SearchActivity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Dan on 2015/4/30
 */
public class ConsultCatoMainActivity extends BaseActivity {
    private MyHandler myHandler;
    private SimpleExpandListAdapter simpleExpandListAdapter;
    private List<String> parentList;
    private List<List<String>> childList;
    private ExpandableListView expandableListView;
    private int expandIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consult_cato_main);

        expandIndex = getIntent().getIntExtra("expand_index", 0);
        myHandler = new MyHandler(this);
        initTitle();
        initView();

    }

    /**
     * 初始化title 和 action事件
     */
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
                //打开搜索界面
                startActivity(new Intent(ConsultCatoMainActivity.this, SearchActivity.class));
            }

            @Override
            public void onRightRClick() {
                //此视图，右边只包含L按钮

            }
        });
    }

    protected void initView() {
        //todo 从网络接口获取列表信息
        //一级列表
        parentList = new ArrayList<>();
        parentList.add("建筑");
        parentList.add("专业");
        parentList.add("关键词");
        //二级列表
        childList = new ArrayList<>();
        List<String> constList = new ArrayList<>();
        List<String> profList = new ArrayList<>();
        List<String> keyWordList = new ArrayList<>();
        String[] constArray = new String[]{
                "高层", "超高层", "商业综合", "文体", "交通枢纽", "轨道交通",
                "地铁", "仓储厂房", "化工", "隧道", "古建筑", "区域类", "结构类", "其他"};
        String[] profArray = new String[]{
                "建筑定性", "防火分区", "平面布置", "疏散避难", "建筑构造",
                "救援", "电梯", "停机坪", "灭火", "报警", "防排烟", "暖通",
                "电气", "其他"};
        String[] keyWordArray = new String[]{
                "防火分隔", "防烟分区", "疏散距离", "疏散宽度"};

        Collections.addAll(constList, constArray);
        Collections.addAll(profList, profArray);
        Collections.addAll(keyWordList, keyWordArray);
        childList.add(constList);
        childList.add(profList);
        childList.add(keyWordList);
        //todo 模拟取数据，设置0.2s 延时
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                myHandler.sendEmptyMessage(0);
            }
        }).start();

        //一级列表控件
        expandableListView =
                (ExpandableListView) findViewById(R.id.consult_cato_expandable_list_view);
        //加载时显示圆形进度条
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.loading_progress_bar);
        expandableListView.setEmptyView(progressBar);

        simpleExpandListAdapter = new SimpleExpandListAdapter(this);
        expandableListView.setAdapter(simpleExpandListAdapter);

        //列表item点击事件
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                //todo 根据关键字搜索，跳转到咨询搜索界面
                SimpleExpandListAdapter adapter = (SimpleExpandListAdapter) parent.getExpandableListAdapter();
                String txt = adapter.getChild(groupPosition, childPosition).toString();
                Intent intent = new Intent(ConsultCatoMainActivity.this, CatoConsultListActivity.class);
                intent.putExtra("search_key_word", txt);
                startActivity(intent);
//                Toast.makeText(ConsultCatoMainActivity.this, txt, Toast.LENGTH_SHORT).show();
                return true;
            }
        });

    }

    protected void updateList() {
        simpleExpandListAdapter.setList(parentList, childList);
        //默认选中第一栏
        expandableListView.expandGroup(expandIndex);

    }

    static class MyHandler extends Handler {
        private WeakReference<ConsultCatoMainActivity> activityWeakReference;

        public MyHandler(ConsultCatoMainActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ConsultCatoMainActivity activity = activityWeakReference.get();
            if (activity != null) {
                switch (msg.what) {
                    case 0:
                        activity.updateList();
                        break;
                    default:
                        break;
                }
            }
        }
    }


}
