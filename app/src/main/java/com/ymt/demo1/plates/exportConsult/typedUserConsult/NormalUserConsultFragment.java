package com.ymt.demo1.plates.exportConsult.typedUserConsult;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ymt.demo1.R;
import com.ymt.demo1.plates.exportConsult.ExportChatListFragment;
import com.ymt.demo1.plates.exportConsult.ExportFollowListFragment;

/**
 * Created by DonMoses on 2015/11/12
 */
public class NormalUserConsultFragment extends Fragment {
    private TextView chatTxt;
    private TextView followTxt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_normal_user_consult, container, false);
        initTab(view);
        return view;
    }

    protected void initTab(View view) {
        chatTxt = (TextView) view.findViewById(R.id.tab_chat);
        followTxt = (TextView) view.findViewById(R.id.tab_follow);
        setTabSelection(0);
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.tab_chat:
                        //todo 切换到 “会话”
                        setTabSelection(0);
                        break;
                    case R.id.tab_follow:
                        //todo 切换到 “关注”
                        setTabSelection(1);
                        break;
                    default:
                        break;
                }
            }
        };
        chatTxt.setOnClickListener(onClickListener);
        followTxt.setOnClickListener(onClickListener);


    }

    /**
     * 设置tab选中状态
     */
    protected void setTabSelection(int tabIndex) {
        FragmentManager fm = getActivity().getSupportFragmentManager();

        switch (tabIndex) {
            case 0:
                chatTxt.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.icon_chat_on), null, null);
                followTxt.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.icon_follow_off), null, null);
                chatTxt.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));
                followTxt.setTextColor(getResources().getColor(R.color.material_blue_grey_800));

                /*
                会话fragment
                 */
                FragmentTransaction ft1 = fm.beginTransaction();
                if (fm.findFragmentByTag(ExportFollowListFragment.FRAGMENT_TAG) != null) {
                    ft1.hide(fm.findFragmentByTag(ExportFollowListFragment.FRAGMENT_TAG));
                }

                if (fm.findFragmentByTag(ExportChatListFragment.FRAGMENT_TAG) == null) {
                    ft1.add(R.id.my_consult_content, ExportChatListFragment.newInstance(""),
                            ExportChatListFragment.FRAGMENT_TAG);
                    ft1.commit();
//                    fm.executePendingTransactions();
                } else {
                    ft1.show(fm.findFragmentByTag(ExportChatListFragment.FRAGMENT_TAG));
                    ft1.commit();
                }
                break;
            case 1:
                chatTxt.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.icon_chat_off), null, null);
                followTxt.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.icon_follow_on), null, null);
                chatTxt.setTextColor(getResources().getColor(R.color.material_blue_grey_800));
                followTxt.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));
                 /*
                关注fragment
                 */
                FragmentTransaction ft2 = fm.beginTransaction();
                if (fm.findFragmentByTag(ExportChatListFragment.FRAGMENT_TAG) != null) {
                    ft2.hide(fm.findFragmentByTag(ExportChatListFragment.FRAGMENT_TAG));
                }
                if (fm.findFragmentByTag(ExportFollowListFragment.FRAGMENT_TAG) == null) {
                    ft2.add(R.id.my_consult_content, ExportFollowListFragment.newInstance(""),
                            ExportFollowListFragment.FRAGMENT_TAG);
                    ft2.commit();
//                    fm.executePendingTransactions();
                } else {
                    ft2.show(fm.findFragmentByTag(ExportFollowListFragment.FRAGMENT_TAG));
                    ft2.commit();
                }
                break;
            default:
                break;
        }
    }

}
