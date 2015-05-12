package com.ymt.demo1.plates.consultCato;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.ymt.demo1.R;
import com.ymt.demo1.adapter.SearchedConsultAdapter;
import com.ymt.demo1.baseClasses.BaseActivity;
import com.ymt.demo1.beams.SearchedConsultInfo;
import com.ymt.demo1.customViews.MyTitle;
import com.ymt.demo1.main.SearchActivity;

import java.util.ArrayList;

/**
 * Created by Dan on 2015/5/4
 */
public class CatoConsultListActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cato_consult_list);
        initTitle();
        initView();
    }

    /**
     * 获取搜索关键字
     */
    protected String getSearchedKeyWord() {
        Intent intent = getIntent();
        return intent.getStringExtra("search_key_word");
    }

    /**
     * 初始化title 和 action事件
     */
    protected void initTitle() {
        final MyTitle title = (MyTitle) findViewById(R.id.my_title);
        title.setTitleStyle(MyTitle.TitleStyle.RIGHT_ICON_L);

        title.updateCenterTitle(getSearchedKeyWord());     //设置title
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
                startActivity(new Intent(CatoConsultListActivity.this, SearchActivity.class));
            }

            @Override
            public void onRightRClick() {
                //此视图，右边只包含L按钮

            }
        });
    }

    /**
     * 初始化控件（ListView）【关键字搜索结果列表】
     */
    protected void initView() {
        PullToRefreshListView listView = (PullToRefreshListView) findViewById(R.id.searched_consult_list_view);

        //延时进度条
        ProgressBar progressBar = new ProgressBar(this);
        progressBar.setIndeterminate(false);
        listView.setEmptyView(progressBar);
        SearchedConsultAdapter adapter = new SearchedConsultAdapter(this);
        listView.setAdapter(adapter);

        //todo 模拟咨询搜索数据
        ArrayList<SearchedConsultInfo> list = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            SearchedConsultInfo info = new SearchedConsultInfo();
            info.setConsultTitle(i + "（test）" + getSearchedKeyWord() + "消防安全知识");
            info.setConsultAnswer("答: 消防安全20条。1．父母、师长要教育儿童养成不玩火的好习惯。任何单位不得组织未成年人扑救火灾。\n" +
                    "\n" +
                    "2．切莫乱扔烟头和火种。\n" +
                    "\n" +
                    "3．室内装修装饰不宜采用易燃可燃材料。\n" +
                    "\n" +
                    "4．消火栓关系公共安全，切勿损坏、圈占或埋压。\n" +
                    "\n" +
                    "5．爱护消防器材，掌握常用消防器材的使用方法。\n" +
                    "\n" +
                    "6．切勿携带易燃易爆物品进入公共场所、乘坐公共交通工具。\n" +
                    "\n" +
                    "7．进入公共场所要注意观察消防标志，记住疏散方向。\n" +
                    "\n" +
                    "8．在任何情况下都要保持疏散通道畅通。\n" +
                    "\n" +
                    "9．任何人发现危及公共消防安全的行为，都可向公安消防部门或值勤公安人员举报。\n" +
                    "\n" +
                    "10．生活用火要特别小心，火源附近不要放置可燃、易燃物品。\n" +
                    "\n" +
                    "l1．发现煤气泄漏，速关阀门，打开门窗，切勿触动电器开关和使用明火。\n" +
                    "\n" +
                    "12．电器线路破旧老化要及时修理更换。\n" +
                    "\n" +
                    "13．电路保险丝(片)熔断，切勿用铜线铁线代替。\n" +
                    "\n" +
                    "14．不能超负荷用电。\n" +
                    "\n" +
                    "15．发现火灾速打报警电话 l19，消防队救火不收费。\n" +
                    "\n" +
                    "16．了解火场情况的人，应及时将火场内被围人员及易燃易爆物品情况告诉消防人员。\n" +
                    "\n" +
                    "17．火灾袭来时要迅速疏散逃生，不要贪恋财物。\n" +
                    "\n" +
                    "18．必须穿过浓烟逃生时，应尽量用浸湿的衣物被裹身体，捂住口鼻，贴近地面。\n" +
                    "\n" +
                    "19．身上着火，可就地打滚，或用厚重衣物覆盖压灭火苗。\n" +
                    "\n" +
                    "20．大火封门无法逃生时，可用浸湿的被褥、衣物等堵塞门缝、泼水降温，呼救待援。");
            info.setCollectedCount((i + 10) % 5 * 1024 - i);
            info.setCommentCount((i + 5) % 3 * 1024 + i);
            if (i % 4 == 0) {
                info.setHasCollected(true);
            } else {
                info.setHasCollected(false);
            }
            if (i % 5 == 0) {
                info.setHasCommented(true);
            } else {
                info.setHasCommented(false);
            }
            list.add(info);
        }

        adapter.setList(list);

        //设置listView点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Log.e("TAG", ">>>>>>>>>>>>>>search_item>>>>" + position);
                //todo 跳转到具体内容界面
                Intent intent = new Intent(CatoConsultListActivity.this, ConsultDetailActivity.class);
                intent.putExtra("search_key_word", getSearchedKeyWord());
                intent.putExtra("title", ((SearchedConsultInfo) parent.getAdapter().getItem(position)).getConsultTitle());
                intent.putExtra("content", ((SearchedConsultInfo) parent.getAdapter().getItem(position)).getConsultAnswer());
                startActivity(intent);
            }
        });
    }
}
