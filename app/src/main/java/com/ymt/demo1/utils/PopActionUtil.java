package com.ymt.demo1.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ymt.demo1.R;
import com.ymt.demo1.beams.edu.exam.Exam;
import com.ymt.demo1.main.sign.ChangePswActivity;

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

    /**
     * 显示普通列表选项的POP
     */
    public PopupWindow getSimpleTxtPopActionMenu() {
        inflater = LayoutInflater.from(context);
        View popContent = inflater.inflate(R.layout.layout_simple_text_pop_action, null);
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

    /**
     * 显示下载提示的POP
     */
    public PopupWindow getDownloadPopActionMenu() {
        inflater = LayoutInflater.from(context);
        View popContent = inflater.inflate(R.layout.layout_download_pop_action, null);
        final Button yBtn = (Button) popContent.findViewById(R.id.do_download_btn);
        final Button nBtn = (Button) popContent.findViewById(R.id.not_download_btn);

        final PopupWindow popupWindow = new PopupWindow(popContent,
                FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x90000000));
        //设置弹出菜单的动画
        popupWindow.setAnimationStyle(R.style.MyDownloadPopAnimation);
        popupWindow.setOnDismissListener(actionListener);

        View.OnClickListener downloadListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo 下载菜单监听
                switch (v.getId()) {
                    case R.id.do_download_btn:
                        //todo 下载
                        actionListener.onAction("确定");
                        popupWindow.dismiss();
                        break;
                    case R.id.not_download_btn:
                        //todo 关闭
                        actionListener.onAction("取消");
                        popupWindow.dismiss();
                        //关闭pop
                        break;
                    default:
                        break;
                }

            }
        };
        yBtn.setOnClickListener(downloadListener);
        nBtn.setOnClickListener(downloadListener);

        return popupWindow;

    }

    /**
     * 显示成功提交咨询的注册用户POP
     */
    public PopupWindow getSubmitConsultSignedPop() {
        inflater = LayoutInflater.from(context);
        View popContent = inflater.inflate(R.layout.layout_submit_consult_signed_pop, null);
        final Button confirmBtn = (Button) popContent.findViewById(R.id.confirm_btn);

        final PopupWindow popupWindow = new PopupWindow(popContent,
                WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x90000000));
        //设置弹出菜单的动画
        popupWindow.setAnimationStyle(R.style.MyDownloadPopAnimation);
        popupWindow.setOnDismissListener(actionListener);

        View.OnClickListener downloadListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo 下载菜单监听
                switch (v.getId()) {
                    case R.id.confirm_btn:
                        //todo 下载
                        actionListener.onAction("确定");
                        popupWindow.dismiss();
                        break;
                    default:
                        break;
                }

            }
        };
        confirmBtn.setOnClickListener(downloadListener);

        return popupWindow;

    }

    /**
     * 显示成功提交咨询的游客用户POP
     */
    public PopupWindow getSubmitConsultUnsignedPop(final String accountTxt, String pswTxt, boolean isFromConsult) {
        inflater = LayoutInflater.from(context);
        View popContent = inflater.inflate(R.layout.layout_submit_consult_unsigned_pop, null);
        final TextView consultSent = (TextView) popContent.findViewById(R.id.consult_sent);
        if (!isFromConsult) {
            consultSent.setVisibility(View.INVISIBLE);
        }
        final TextView account = (TextView) popContent.findViewById(R.id.account_txt);
        final TextView psw = (TextView) popContent.findViewById(R.id.psw_txt);
        final TextView editPsw = (TextView) popContent.findViewById(R.id.edit_psw_txt);
        final Button confirmBtn = (Button) popContent.findViewById(R.id.confirm_btn);
        account.setText(account.getText().toString() + accountTxt);
        psw.setText(psw.getText().toString() + pswTxt);

        //对修改账号密码的view 使用富文本，点击“修改“则跳转到密码修改界面
        StringBuilder builder = new StringBuilder(editPsw.getText());   // "建议您修改账号密码"
        SpannableString spannableString = new SpannableString(builder);
        ForegroundColorSpan colorSpan =
                new ForegroundColorSpan(context.getResources().getColor(android.R.color.holo_blue_dark));
        spannableString.setSpan(colorSpan, 3, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent = new Intent(context, ChangePswActivity.class);
                intent.putExtra("loginName", accountTxt);
                context.startActivity(intent);
            }
        }, 3, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //这只spannableString
        editPsw.setText(spannableString);
        //凡是要有点击事件的textView ，都必须设置下面属性
        editPsw.setMovementMethod(LinkMovementMethod.getInstance());

        //弹出窗背景焦点等逻辑
        final PopupWindow popupWindow = new PopupWindow(popContent,
                FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x90000000));
        //设置弹出菜单的动画
        popupWindow.setAnimationStyle(R.style.MyDownloadPopAnimation);
        popupWindow.setOnDismissListener(actionListener);

        View.OnClickListener downloadListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo 下载菜单监听
                switch (v.getId()) {
                    case R.id.confirm_btn:
                        //todo 下载
                        actionListener.onAction("确定");
                        popupWindow.dismiss();
                        break;
                    default:
                        break;
                }

            }
        };
        confirmBtn.setOnClickListener(downloadListener);

        return popupWindow;

    }

    /**
     * pop事件点选监听
     */
    public void setActionListener(PopActionListener actionListener) {
        this.actionListener = actionListener;
    }

    /**
     * 显示提交试卷POP
     */
    public PopupWindow getSubPaperPopActionMenu(Exam exam) {
        inflater = LayoutInflater.from(context);
        View popContent = inflater.inflate(R.layout.layout_sub_paper_pop_action, null);
        final Button yBtn = (Button) popContent.findViewById(R.id.do_download_btn);
        final Button nBtn = (Button) popContent.findViewById(R.id.not_download_btn);

        final TextView examName = (TextView) popContent.findViewById(R.id.exam_content);
        final TextView totalItem = (TextView) popContent.findViewById(R.id.total_item);
        final TextView totalScore = (TextView) popContent.findViewById(R.id.total_score);
        examName.setText(exam.getExam_title());
        totalItem.setText("题目：" + exam.getTotal_item() + "题");
        totalScore.setText("总分：" + exam.getTotal_score() + "分");

        final PopupWindow popupWindow = new PopupWindow(popContent,
                FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x90000000));
        //设置弹出菜单的动画
        popupWindow.setAnimationStyle(R.style.MyDownloadPopAnimation);
        popupWindow.setOnDismissListener(actionListener);

        View.OnClickListener downloadListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo 下载菜单监听
                switch (v.getId()) {
                    case R.id.do_download_btn:
                        //todo 下载
                        actionListener.onAction("确定");
                        popupWindow.dismiss();
                        break;
                    case R.id.not_download_btn:
                        //todo 关闭
                        actionListener.onAction("取消");
                        popupWindow.dismiss();
                        //关闭pop
                        break;
                    default:
                        break;
                }

            }
        };
        yBtn.setOnClickListener(downloadListener);
        nBtn.setOnClickListener(downloadListener);

        return popupWindow;

    }
}
