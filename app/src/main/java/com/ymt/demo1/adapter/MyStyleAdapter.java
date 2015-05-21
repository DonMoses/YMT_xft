package com.ymt.demo1.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ymt.demo1.R;
import com.ymt.demo1.beams.XFTStyle;

import java.util.ArrayList;

/**
 * 风格adapter
 */
public class MyStyleAdapter extends BaseAdapter {
    ArrayList<XFTStyle> styles = new ArrayList<>();
    LayoutInflater inflater;
    Context context;

    public MyStyleAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    public void setStyles(ArrayList<XFTStyle> styles) {
        this.styles = styles;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return styles.size();
    }

    @Override
    public Object getItem(int position) {
        return styles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_style_item, null);
            viewHolder = new ViewHolder();
            viewHolder.styleImg = (ImageView) convertView.findViewById(R.id.style_img);
            viewHolder.styleTxt = (TextView) convertView.findViewById(R.id.style_txt);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.styleImg.setImageBitmap(styles.get(position).getStyleImg());
        viewHolder.styleTxt.setText(styles.get(position).getStyleTxt());
        return convertView;
    }

    class ViewHolder {
        private ImageView styleImg;
        private TextView styleTxt;
    }
}