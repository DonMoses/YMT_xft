package com.ymt.demo1.plates.news;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.WebView;
import android.widget.TextView;

import com.ymt.demo1.R;
import com.ymt.demo1.baseClasses.BaseActivity;
import com.ymt.demo1.beams.news.NewsSummary;
import com.ymt.demo1.customViews.MyTitle;

/**
 * Created by Dan on 2015/4/29
 */
public class NewsDetailActivity extends BaseActivity {
    private String title;
    private String time;
    private NewsSummary summary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        summary = getIntent().getParcelableExtra("summary");
        title = summary.getArticle_title();
        String fullTime = summary.getCreate_time();
        time = (String) fullTime.subSequence(0, fullTime.length() - 2);
        setContentView(R.layout.activity_news_detail);
        initTitle();
        initView();
    }

    /**
     * 初始化title 和 action事件
     */
    protected void initTitle() {
        final MyTitle title = (MyTitle) findViewById(R.id.my_title);
        title.setTitleStyle(MyTitle.TitleStyle.RIGHT_ICON_L);

        title.updateCenterTitle(getIntent().getStringExtra("title"));     //设置title
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
                intent.putExtra(Intent.EXTRA_SUBJECT, "消防咨询-" + getIntent().getStringExtra("title"));
                intent.putExtra(Intent.EXTRA_TEXT, getIntent().getStringExtra("content"));
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
        //热门话题GridView
//        final GridView hotCommentGrid = (GridView) findViewById(R.id.hot_comment_grid_view);
        final TextView newsTitle = (TextView) findViewById(R.id.news_title);
        final TextView newsTime = (TextView) findViewById(R.id.news_time);
        newsTitle.setText(title);
        String author = summary.getAuthor();
        String editor = summary.getEditor();
        String source = summary.getSource();
        if (!TextUtils.isEmpty(editor)) {
            newsTime.setText(source + "-" + author + "  编辑-" + editor + "  " + time);
        } else {
            newsTime.setText(source + "-" + author + "  " + time);
        }

        /*
        内容textView
         */
        WebView contentView = (WebView) findViewById(R.id.content);
        contentView.loadDataWithBaseURL(null, summary.getContent(), "text/html", "UTF-8", null);
        //设置底部热点话题内容
//        setDataToHotGird(hotCommentGrid);

//        /*
//        点击 “写点评”
//         */
//        View view = findViewById(R.id.write_comment_layout);
//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //todo 写点评
//                Toast.makeText(NewsDetailActivity.this, "写点评...", Toast.LENGTH_SHORT).show();
//            }
//        });
    }

//    /**
//     * 为底部热点话题设置数据
//     */
//    protected void setDataToHotGird(GridView gridView) {
//        ArrayList<String> list = new ArrayList<>();
//        String[] hotArray = new String[]{"消防部门", "规范组", "建委",
//                "科研机构", "设计院", "开发商", "设备商", "服务商"};
//        Collections.addAll(list, hotArray);
//        SimpleTxtItemAdapter adapter = new SimpleTxtItemAdapter(this);
//        gridView.setAdapter(adapter);
//        adapter.setColor(Color.WHITE, getResources().getColor(R.color.bg_view_blue));
//        adapter.setList(list);
//
//        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String str = parent.getAdapter().getItem(position).toString();
//                //todo
//                Toast.makeText(NewsDetailActivity.this, str, Toast.LENGTH_SHORT).show();
//            }
//        });
//    }


}
