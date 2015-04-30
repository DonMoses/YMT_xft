package com.ymt.demo1.main;

import android.widget.PopupWindow;

public interface PopActionListener extends PopupWindow.OnDismissListener {

    void onAction(String action);
}