package com.ymt.demo1.customKeyBoard;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;

import com.ymt.demo1.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by Dan on 2015/4/26
 */
public class EmoJFragment extends Fragment {
    private ArrayList<Bitmap> mImeList = new ArrayList<>();

    public static EmoJFragment newInstance() {
        return new EmoJFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_emoj, container, false);
        initImeList();
        initView(view);
        return view;
    }

    protected void initView(View view) {
        GridView gridView = (GridView) view.findViewById(R.id.ime_item_grid_view);
        MyImeAdapter adapter = new MyImeAdapter(getActivity());
        gridView.setAdapter(adapter);
        adapter.setList(mImeList);
        gridView.setOnItemClickListener(new MyImeInputListener((ConsultActivity) getActivity()));
    }

    /**
     * 初始化表情
     */
    protected void initImeList() {
        Bitmap bitmap = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.icon_input_emoj);
        for (int i = 0; i < 21; i++) {
            mImeList.add(bitmap);
        }
    }

    /**
     * 输入表情的CLick事件
     */
    static class MyImeInputListener implements AdapterView.OnItemClickListener {
        WeakReference<ConsultActivity> consultActivityWeakReference;

        public MyImeInputListener(ConsultActivity consultActivity) {
            consultActivityWeakReference = new WeakReference<>(consultActivity);
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            MyImeAdapter imeAdapter = (MyImeAdapter) parent.getAdapter();
            Bitmap imeBitmap = (Bitmap) imeAdapter.getItem(position);
            ConsultActivity consultActivity = consultActivityWeakReference.get();
            if (consultActivity != null) {
                EditText inputText = consultActivity.mInputText;
                SpannableString spannableString = new SpannableString("0");
                ImageSpan imageSpan = new ImageSpan(consultActivity, imeBitmap);
                spannableString.setSpan(imageSpan, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                inputText.append(spannableString);
            }
        }
    }

    /**
     * 显示表情的适配器
     */
    class MyImeAdapter extends BaseAdapter {

        ArrayList<Bitmap> mList = new ArrayList<>();
        LayoutInflater inflater;

        public MyImeAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        public void setList(ArrayList<Bitmap> mList) {
            this.mList = mList;
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

            ImageView imeItem;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.layout_ime_emoj_item, null);
                imeItem = (ImageView) convertView.findViewById(R.id.ime_item_imgView);
                convertView.setTag(imeItem);
            } else {
                imeItem = (ImageView) convertView.getTag();
            }
            imeItem.setImageBitmap(mList.get(position));

            return convertView;
        }
    }
}
