package com.ymt.demo1.customViews;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PointF;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.ymt.demo1.R;
import com.ymt.demo1.launchpages.MainActivity;

/**
 * Created by DonMoses on 2015/9/7
 */
public class IOSSwitchView extends FrameLayout implements View.OnClickListener {
    private SharedPreferences voiceSettingPreference;
    private Context context;
    private ImageView target;
    private int layoutWidth;
    private int targetWidth;
    private int l;
    private int r;
    private boolean isChecked;
    private boolean isSwitching;
    private String checkedKey;

    public IOSSwitchView(Context context) {
        super(context);
        this.context = context;
        init();
        voiceSettingPreference = context.getSharedPreferences(
                MainActivity.SETTING_PREFERENCES, Context.MODE_PRIVATE);
    }

    public IOSSwitchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
        voiceSettingPreference = context.getSharedPreferences(
                MainActivity.SETTING_PREFERENCES, Context.MODE_PRIVATE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        layoutWidth = getMeasuredWidth();
    }

    private void init() {
        isChecked = false;
        isSwitching = false;
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setLayoutParams(params);
        this.setBackgroundResource(R.drawable.switch_view_bg_grey);
        initLeftSwitchIcon();
        setOnClickListener(this);
    }

    /**
     * 加入图标icon
     */
    private void initLeftSwitchIcon() {
        target = new ImageView(context);
        LayoutParams params = new LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.setMargins(3, 3, 3, 3);
        l = params.leftMargin;
        r = params.rightMargin;
        params.gravity = Gravity.CENTER_VERTICAL;
        target.setLayoutParams(params);
        target.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.switch_view_icon_grey));
        addView(target);
        targetWidth = target.getDrawable().getIntrinsicWidth();
    }

    /**
     * 加入图标icon
     */
    private void initRightSwitchIcon() {
        isChecked = true;
        this.setBackgroundResource(R.drawable.switch_view_bg_blue);
        target = new ImageView(context);
        LayoutParams params = new LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.setMargins(3, 3, 3, 3);
        l = params.leftMargin;
        r = params.rightMargin;
        params.gravity = Gravity.CENTER_VERTICAL | Gravity.END;
        target.setLayoutParams(params);
        target.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.switch_view_icon_blue));
        addView(target);
        targetWidth = target.getDrawable().getIntrinsicWidth();
    }

    @Override
    public void onClick(View v) {
        //todo 点击时切换状态
        ValueAnimator animator;
        if (isSwitching) {
            return;
        }
        if (!isChecked) {
            SwitchEvaluatorI evaluator = new SwitchEvaluatorI();
            animator = ValueAnimator.ofObject(evaluator,
                    new PointF(target.getX(), target.getY()),
                    new PointF(target.getX() - l + layoutWidth - targetWidth - r, target.getY()));
        } else {
            SwitchEvaluatorII evaluator = new SwitchEvaluatorII();
            animator = ValueAnimator.ofObject(evaluator,
                    new PointF(target.getX(), target.getY()),
                    new PointF(target.getX() + targetWidth + r - layoutWidth + l, target.getY()));
        }

        SwitchUpdateListener listener = new SwitchUpdateListener(target);
        animator.addUpdateListener(listener);
        animator.setDuration(309);
        animator.setTarget(target);
        animator.start();

    }

    private class SwitchEvaluatorI implements TypeEvaluator<PointF> {
        @Override
        public PointF evaluate(float fraction, PointF startValue, PointF endValue) {
            PointF pointF = new PointF();
            //todo 点击移动
            float x = startValue.x + fraction * (endValue.x - startValue.x);
            float y = endValue.y;
            pointF.x = x;
            pointF.y = y;
            return pointF;
        }
    }

    private class SwitchEvaluatorII implements TypeEvaluator<PointF> {

        @Override
        public PointF evaluate(float fraction, PointF startValue, PointF endValue) {
            PointF pointF = new PointF();
            //todo 点击移动
            float x = startValue.x - fraction * (startValue.x - endValue.x);
            float y = endValue.y;
            pointF.x = x;
            pointF.y = y;
            return pointF;
        }
    }

    private class SwitchUpdateListener implements ValueAnimator.AnimatorUpdateListener {

        private View target;

        public SwitchUpdateListener(View target) {
            this.target = target;
        }

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            PointF pointF = (PointF) animation.getAnimatedValue();
            this.target.setX(pointF.x);
            this.target.setY(pointF.y);
            float fraction = animation.getAnimatedFraction();

            if (fraction < 0.5) {
                if (!isChecked) {
                    IOSSwitchView.this.setBackgroundResource(R.drawable.switch_view_bg_grey);
                    ((ImageView) target).setImageDrawable(
                            ContextCompat.getDrawable(context, R.drawable.switch_view_icon_grey));
                    target.setAlpha(1 - 2 * fraction);
                } else {
                    IOSSwitchView.this.setBackgroundResource(R.drawable.switch_view_bg_blue);
                    ((ImageView) target).setImageDrawable(
                            ContextCompat.getDrawable(context, R.drawable.switch_view_icon_blue));
                    target.setAlpha(1 - 2 * fraction);
                }
            } else {
                if (!isChecked) {
                    IOSSwitchView.this.setBackgroundResource(R.drawable.switch_view_bg_blue);
                    ((ImageView) target).setImageDrawable(
                            ContextCompat.getDrawable(context, R.drawable.switch_view_icon_blue));
                    target.setAlpha(2 * fraction - 1);
                } else {
                    IOSSwitchView.this.setBackgroundResource(R.drawable.switch_view_bg_grey);
                    ((ImageView) target).setImageDrawable(
                            ContextCompat.getDrawable(context, R.drawable.switch_view_icon_grey));
                    target.setAlpha(2 * fraction - 1);
                }
            }

            //切换完成后，保存状态
            isSwitching = fraction != 1;
            if (fraction == 1) {
                isChecked = !isChecked;
                SharedPreferences.Editor editor = voiceSettingPreference.edit();
                editor.remove(checkedKey);
                editor.putBoolean(checkedKey, isChecked);
                editor.apply();
            }
        }
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setCheckedKey(String checkedKey) {
        this.checkedKey = checkedKey;
        boolean checked = voiceSettingPreference.getBoolean(checkedKey, false);
        if (checked) {
            removeAllViews();
            initRightSwitchIcon();
        }
    }

}
