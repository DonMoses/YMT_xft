package com.ymt.demo1.customViews;

import android.animation.Animator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PointF;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ymt.demo1.R;

/**
 * Created by DonMoses on 2015/8/27
 */
public class SignSwitchView extends LinearLayout {
    private Context context;
    private TextView leftBtn;
    private TextView rightBtn;
    private boolean isSignInMode = true;
    private boolean isSwitching = false;
    private SignSwitchListener switchListener;

    public void setSwitchListener(SignSwitchListener switchListener) {
        this.switchListener = switchListener;
    }

    public SignSwitchView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public SignSwitchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void init() {
        setOrientation(LinearLayout.HORIZONTAL);
        leftBtn = new TextView(context);
        rightBtn = new TextView(context);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                0, LayoutParams.MATCH_PARENT);
        params.weight = 1;

        leftBtn.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.bg_corner_3_white));
        leftBtn.setText("登录");
        leftBtn.setTextColor(context.getResources().getColor(android.R.color.holo_blue_dark));
        leftBtn.setGravity(Gravity.CENTER);

        rightBtn.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.bg_corner_3_dark_blue));
        rightBtn.setText("注册");
        rightBtn.setTextColor(context.getResources().getColor(android.R.color.white));
        rightBtn.setGravity(Gravity.CENTER);

        leftBtn.setLayoutParams(params);
        leftBtn.setId(R.id.sign_in);
        leftBtn.setFocusable(false);
        addView(leftBtn);
        rightBtn.setLayoutParams(params);
        rightBtn.setId(R.id.sign_up);
        rightBtn.setFocusable(false);
        addView(rightBtn);

        final Animator.AnimatorListener listener = new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                isSwitching = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isSwitching = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        };

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSwitching) {
                    return;
                }
                switchListener.isSignIn(!isSignInMode);
                if (!isSignInMode) {
                    //左边向右移动
                    float x1 = leftBtn.getX();
                    float x2 = rightBtn.getX();
                    float y1 = leftBtn.getY();
                    float y2 = rightBtn.getY();
                    SignInSwitchEvaluator evaluator1 = new SignInSwitchEvaluator();
                    ValueAnimator animatorI = ValueAnimator.ofObject(
                            evaluator1,
                            new PointF(x1, y1),
                            new PointF(x2, y2));
                    animatorI.addListener(listener);
                    SignSwitchAnimatorListener listener1 = new SignSwitchAnimatorListener(leftBtn);
                    animatorI.setDuration(128);
                    animatorI.setTarget(leftBtn);
                    animatorI.addUpdateListener(listener1);
                    animatorI.start();
                    //右边向左移动
                    SignUpSwitchEvaluator evaluator2 = new SignUpSwitchEvaluator();
                    ValueAnimator animatorII = ValueAnimator.ofObject(
                            evaluator2,
                            new PointF(x2, y2),
                            new PointF(x1, y1));
                    animatorII.addListener(listener);
                    SignSwitchAnimatorListener listener2 = new SignSwitchAnimatorListener(rightBtn);
                    animatorII.setDuration(128);
                    animatorII.setTarget(rightBtn);
                    animatorII.addUpdateListener(listener2);
                    animatorII.start();
                } else {
                    //右边向左边移动
                    float x11 = rightBtn.getX();
                    float x22 = leftBtn.getX();
                    float y11 = rightBtn.getY();
                    float y22 = leftBtn.getY();
                    SignUpSwitchEvaluator evaluator2 = new SignUpSwitchEvaluator();
                    ValueAnimator animatorII = ValueAnimator.ofObject(
                            evaluator2,
                            new PointF(x11, y11),
                            new PointF(x22, y22));
                    animatorII.addListener(listener);
                    SignSwitchAnimatorListener listener2 = new SignSwitchAnimatorListener(rightBtn);
                    animatorII.setDuration(128);
                    animatorII.setTarget(rightBtn);
                    animatorII.addUpdateListener(listener2);
                    animatorII.start();
                    //左边向右边移动
                    SignInSwitchEvaluator evaluator1 = new SignInSwitchEvaluator();
                    ValueAnimator animatorI = ValueAnimator.ofObject(
                            evaluator1,
                            new PointF(x22, y22),
                            new PointF(x11, y11));
                    animatorI.addListener(listener);
                    SignSwitchAnimatorListener listener1 = new SignSwitchAnimatorListener(leftBtn);
                    animatorI.setDuration(128);
                    animatorI.setTarget(leftBtn);
                    animatorI.addUpdateListener(listener1);
                    animatorI.start();
                }
                isSignInMode = !isSignInMode;
            }
        });

    }

    public class SignInSwitchEvaluator implements TypeEvaluator<PointF> {

        @Override
        public PointF evaluate(float fraction, PointF startValue, PointF endValue) {
            PointF pointF = new PointF();
            pointF.x = (endValue.x - startValue.x) * fraction + startValue.x;
            pointF.y = endValue.y;
            return pointF;
        }
    }

    public class SignUpSwitchEvaluator implements TypeEvaluator<PointF> {

        @Override
        public PointF evaluate(float fraction, PointF startValue, PointF endValue) {
            PointF pointF = new PointF();
            pointF.x = startValue.x - (startValue.x - endValue.x) * fraction;
            pointF.y = endValue.y;
            return pointF;
        }
    }

    public class SignSwitchAnimatorListener implements ValueAnimator.AnimatorUpdateListener {
        private View target;

        public SignSwitchAnimatorListener(View target) {
            this.target = target;
        }

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            PointF pointF = (PointF) animation.getAnimatedValue();
            float fraction = animation.getAnimatedFraction();
            if (fraction > 0f && fraction < 0.5f) {
                target.setAlpha(1 - 2 * fraction);
            } else if (fraction >= 0.5f) {
                if (leftBtn.getText().equals("注册")) {
                    leftBtn.setText("登录");
                } else {
                    leftBtn.setText("注册");
                }
                if (rightBtn.getText().equals("登录")) {
                    rightBtn.setText("注册");
                } else {
                    rightBtn.setText("登录");
                }
                target.setAlpha(2 * fraction - 1);
            }
            target.setX(pointF.x);
            target.setY(pointF.y);
        }
    }

    public interface SignSwitchListener {
        void isSignIn(boolean isSignIn);
    }

}
