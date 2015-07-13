package com.ymt.demo1.plates.knowledge;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.ymt.demo1.R;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Dan on 2015/7/13
 */
public class WebVideoActivity extends Activity {
    private WebView holder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_video);
        initView();
    }

    protected void initView() {
        holder = (WebView) findViewById(R.id.video_holder);

        WebSettings settings = holder.getSettings();
        //WebView启用Javascript脚本执行
        settings.setJavaScriptEnabled(true);//是否允许javascript脚本
        settings.setJavaScriptCanOpenWindowsAutomatically(true);//是否允许页面弹窗

        settings.setBuiltInZoomControls(true);
        settings.setPluginState(WebSettings.PluginState.ON);

        String html = getIntent().getStringExtra("mp4_url");
        holder.loadUrl(html);

    }

    private void callHiddenWebViewMethod(String name) {
        if (holder != null) {
            try {
                Method method = WebView.class.getMethod(name);
                method.invoke(holder);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        holder.pauseTimers();

        callHiddenWebViewMethod("onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        holder.resumeTimers();

        callHiddenWebViewMethod("onResume");
    }
}
