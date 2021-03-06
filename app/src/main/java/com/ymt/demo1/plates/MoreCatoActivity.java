package com.ymt.demo1.plates;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.ymt.demo1.R;
import com.ymt.demo1.customViews.MyTitle;
import com.ymt.demo1.baseClasses.BaseFloatActivity;

import java.util.ArrayList;

/**
 * Created by Dan on 2015/4/7
 */
public class MoreCatoActivity extends BaseFloatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);
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

    protected void initView() {

        /*
        更多条目： gridView
         */
        GridView moreCatoView = (GridView) findViewById(R.id.more_cato_gridView);
        final ArrayList<String> list = new ArrayList<>();
        list.add("合作平台");
        list.add("跟踪服务");
        list.add("商城");
        list.add("游戏");
        CatoAdapter adapter = new CatoAdapter(this);
        moreCatoView.setAdapter(adapter);
        adapter.setmList(list);

        moreCatoView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(MoreCatoActivity.this,
                        parent.getItemAtPosition(position).toString(),
                        Toast.LENGTH_SHORT).show();
            }
        });

    }

    class CatoAdapter extends BaseAdapter {
        ArrayList<String> mList = new ArrayList<>();
        LayoutInflater inflater;
        Context context;

        public CatoAdapter(Context context) {
            inflater = LayoutInflater.from(context);
            this.context = context;
        }

        public void setmList(ArrayList<String> mList) {
            this.mList = mList;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView catoTxt;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.layout_cato_item, null);
                catoTxt = (TextView) convertView.findViewById(R.id.cato_text);
                convertView.setTag(catoTxt);
            } else {
                catoTxt = (TextView) convertView.getTag();
            }
            catoTxt.setText(getItem(position).toString());

            return convertView;
        }
    }
}
