package com.ymt.demo1.main.collect;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.ymt.demo1.R;

/**
 * Created by DonMoses on 2015/11/6
 */
public class CollectListFragment extends Fragment {
    private static CollectListFragment collectListFragment;
    public static String TAG = "CollectListFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_collect_wrong_train_type_list, container, false);
        initView(view);
        return view;
    }

    public static CollectListFragment getInstance() {
        if (collectListFragment == null) {
            collectListFragment = new CollectListFragment();
        }
        return collectListFragment;
    }

    private void initView(View view) {
        ListView listView = (ListView) view.findViewById(R.id.collect_list_view);
        final String[] array = getResources().getStringArray(R.array.collect);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.item_collect, R.id.collect, array);
        listView.setAdapter(adapter);
        final FragmentManager fm = getFragmentManager();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FragmentTransaction ft = fm.beginTransaction();
                switch (position) {
                    case 0:         //咨询收藏
                        ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                        ft.replace(R.id.content, CollectContentFragment.getInstance("1"));
                        ft.commit();
                        break;
                    case 1:         //知识收藏
                        ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                        ft.replace(R.id.content, CollectContentFragment.getInstance("2"));
                        ft.commit();
                        break;
                    case 2:         //教育收藏
                        ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                        ft.replace(R.id.content, CollectContentFragment.getInstance("3"));
                        ft.commit();
                        break;
                    case 3:         //商品收藏
                        Toast.makeText(getActivity(), "对不起，该板块还未开通！", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;

                }
            }
        });
    }
}
