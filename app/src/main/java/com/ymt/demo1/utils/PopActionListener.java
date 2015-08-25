package com.ymt.demo1.utils;

import android.widget.PopupWindow;

public interface PopActionListener extends PopupWindow.OnDismissListener {

    void onAction(String action);
}