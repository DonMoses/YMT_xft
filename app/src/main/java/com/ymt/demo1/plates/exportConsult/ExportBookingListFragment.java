package com.ymt.demo1.plates.exportConsult;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.ymt.demo1.R;
import com.ymt.demo1.adapter.ExportBookingListAdapter;
import com.ymt.demo1.beams.expert_consult.Expert;
import com.ymt.demo1.beams.expert_consult.ExportBookingPast;
import com.ymt.demo1.beams.expert_consult.ExportBookingRecent;

import java.util.ArrayList;

/**
 * Created by Dan on 2015/5/13
 */
public class ExportBookingListFragment extends Fragment {
    public static final String FRAGMENT_TAG = "ExportBookingFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_export_booking, container, false);
        initBookingList(view);
        return view;
    }

    public static ExportBookingListFragment newInstance(String emptyInfo) {
        ExportBookingListFragment exportBookingFragment = new ExportBookingListFragment();
        Bundle args = new Bundle();
        args.putString("empty_info", emptyInfo);
        exportBookingFragment.setArguments(args);
        return exportBookingFragment;
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
    protected void initBookingList(View view) {
        //二级列表【目前预约、预约记录】
        ExpandableListView bookingListView =
                (ExpandableListView) view.findViewById(R.id.export_booking_list_view);

        ExportBookingListAdapter bookingListAdapter = new ExportBookingListAdapter(getActivity());
        bookingListView.setAdapter(bookingListAdapter);
        //一级目录
        ArrayList<String> parentList = new ArrayList<>();
        parentList.add("预约记录");
        parentList.add("目前预约");
        //二级目录
        ArrayList<ArrayList<ExportBookingPast>> childList = new ArrayList<>();
        ArrayList<ExportBookingPast> childA = new ArrayList<>();
        ArrayList<ExportBookingPast> childB = new ArrayList<>();

        for (int i = 0; i < 15; i++) {
            ExportBookingPast exportBookingPast = new ExportBookingPast();
            Expert expert = new Expert();
            expert.setUser_name("export " + i);
            expert.setMajor_works("消防队指导员");
            exportBookingPast.setExpert(expert);
            exportBookingPast.setDate("2015-01-23");
            childA.add(exportBookingPast);
        }
        for (int i = 0; i < 9; i++) {
            ExportBookingRecent exportBookingRecent = new ExportBookingRecent();
            Expert exportTest = new Expert();
            exportTest.setUser_name("export " + i);
            exportTest.setMajor_works("消防队指导员");
            exportBookingRecent.setExpert(exportTest);
            exportBookingRecent.setDate("2015-01-23");
            exportBookingRecent.setDateHour("上午10:00");
            childB.add(exportBookingRecent);
        }

        childList.add(childA);
        childList.add(childB);

        bookingListAdapter.setList(parentList, childList);
    }
}
