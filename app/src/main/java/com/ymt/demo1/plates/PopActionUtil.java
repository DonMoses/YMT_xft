package com.ymt.demo1.plates;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.ymt.demo1.R;
import com.ymt.demo1.main.PopActionListener;

/**
 * Created by Dan on 2015/4/27
 */
public class PopActionUtil {
    private static PopActionUtil popActionUtil;
    private LayoutInflater inflater;
    private String[] actionTexts;
    private Context context;
    private PopActionListener actionListener;

    private PopActionUtil(Context context) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    public static PopActionUtil getInstance(Context context) {
        if (popActionUtil == null) {
            popActionUtil = new PopActionUtil(context);
        }
        return popActionUtil;

    }

    public void setActions(String[] actions) {
        this.actionTexts = actions;
    }

    public PopupWindow getPopActionMenu() {
        inflater = LayoutInflater.from(context);
        View popContent = inflater.inflate(R.layout.layout_hub_pop_action, null);
        final ListView actionList = (ListView) popContent.findViewById(R.id.pop_action_list_view);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(context,
                R.layout.item_text_pop_action, actionTexts);
        actionList.setAdapter(adapter);
        final PopupWindow popupWindow = new PopupWindow(popContent,
                FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable());
        //设置弹出菜单的动画
        popupWindow.setAnimationStyle(R.style.MyPopAnimation);
        popupWindow.setOnDismissListener(actionListener);

        actionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //关闭、回调action
                actionListener.onAction(adapter.getItem(position));
                popupWindow.dismiss();
            }
        });
        return popupWindow;

    }

    public void setActionListener(PopActionListener actionListener) {
        this.actionListener = actionListener;
    }
}
