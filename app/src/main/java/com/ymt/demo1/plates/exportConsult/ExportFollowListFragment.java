package com.ymt.demo1.plates.exportConsult;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.ymt.demo1.R;
import com.ymt.demo1.adapter.ExportFollowAdapter;
import com.ymt.demo1.beams.Export;

import java.util.ArrayList;

/**
 * Created by Dan on 2015/5/13
 */
public class ExportFollowListFragment extends Fragment {

    public static final String FRAGMENT_TAG = "ExportFollowFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_export_follow, container, false);
        initFollowList(view);
        return view;
    }

    public static ExportFollowListFragment newInstance(String emptyInfo) {
        ExportFollowListFragment exportFollowFragment = new ExportFollowListFragment();
        Bundle args = new Bundle();
        args.putString("empty_info", emptyInfo);
        exportFollowFragment.setArguments(args);
        return exportFollowFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Bundle bundle = getArguments();
//        String emptyInfo = bundle.getString("empty_info");
    }

    /**
     * 初始化关注界面
     */
    protected void initFollowList(View view) {
        ListView chatListView = (ListView) view.findViewById(R.id.export_follow_list_view);
        ExportFollowAdapter followAdapter = new ExportFollowAdapter(getActivity());
        chatListView.setAdapter(followAdapter);
        ArrayList<Export> exports = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            Export export = new Export();
            export.setIcon(BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.moses));
            export.setToDefault("export No." + String.valueOf(i));
            exports.add(export);
        }
        followAdapter.setList(exports);

        /*
        todo 关注列表 点击事件
         */
        chatListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //进入关注详情界面
                Toast.makeText(getActivity(), "export " + String.valueOf(position), Toast.LENGTH_SHORT).show();
            }
        });


    }
}
