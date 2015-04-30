package com.ymt.demo1.customKeyBoard;

import android.content.res.Resources;
import android.util.Log;

import com.ymt.demo1.main.AppContext;

/**
 * Created by Dan on 2015/4/29
 */
public class ScreenSizeUtil {

    public static int getStatusBarHeight() {
        int result = 0;
        int resourceId = AppContext.getContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = AppContext.getContext().getResources().getDimensionPixelSize(resourceId);
        }
        Log.e("TAG", "status_bar_height>>>>>>>>>>" + result);
        return result;
    }

    public static int getNavigationBarHeight() {
        Resources resources = AppContext.getContext().getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }
}
