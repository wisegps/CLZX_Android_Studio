package com.wisegps.clzx.utils;

import android.os.CountDownTimer;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/9/3.
 */
public class CountDownTimerUtils extends CountDownTimer {

    private TextView mTextView;
    private int normalBgColor;
    private int countDownBgColor;
    private int normalTextColor;
    private int countDownTextColor;

    /**
     * @param textView  倒计时控件
     * @param millisInFuture  倒计时时间  毫秒
     * @param countDownInterval 倒计时间隔 毫秒
     * @param normalBgColor 控件背景颜色（未倒计时）
     * @param normalTextColor 控件字体颜色（未倒计时）
     * @param countDownBgColor 控件倒计时背景颜色
     * @param countDownTextColor 控件倒计时 字体 颜色
     */
    public CountDownTimerUtils(TextView textView, long millisInFuture, long countDownInterval,
                               int normalBgColor,int normalTextColor,int countDownBgColor,int countDownTextColor) {
        super(millisInFuture, countDownInterval);
        this.mTextView = textView;
        this.normalBgColor = normalBgColor;
        this.countDownBgColor = countDownBgColor;
        this.normalTextColor = normalTextColor;
        this.countDownTextColor = countDownTextColor;
    }

    @Override
    public void onTick(long l) {
        mTextView.setClickable(false); //设置不可点击
        mTextView.setText(l / 1000 + "秒后重新获取");  //设置倒计时时间
        mTextView.setBackgroundColor(countDownBgColor); //设置按钮为灰色，这时是不能点击的
        /**
         * 超链接 URLSpan
         * 文字背景颜色 BackgroundColorSpan
         * 文字颜色 ForegroundColorSpan
         * 字体大小 AbsoluteSizeSpan
         * 粗体、斜体 StyleSpan
         * 删除线 StrikethroughSpan
         * 下划线 UnderlineSpan
         * 图片 ImageSpan
         * http://blog.csdn.net/ah200614435/article/details/7914459
         */
        SpannableString spannableString = new SpannableString(mTextView.getText().toString());  //获取按钮上的文字
        ForegroundColorSpan span = new ForegroundColorSpan(countDownTextColor);
        /**
         * public void setSpan(Object what, int start, int end, int flags) {
         * 主要是start跟end，start是起始位置,无论中英文，都算一个。
         * 从0开始计算起。end是结束位置，所以处理的文字，包含开始位置，但不包含结束位置。
         */
        spannableString.setSpan(span, 0, mTextView.getText().length(),Spannable.SPAN_INCLUSIVE_EXCLUSIVE);//将倒计时的时间设置为红色
        mTextView.setText(spannableString);
    }

    @Override
    public void onFinish() {
        mTextView.setClickable(true); //设置不可点击
        mTextView.setText("获取验证码");  //设置倒计时时间
        mTextView.setTextColor(normalTextColor);
        mTextView.setBackgroundColor(normalBgColor); //设置按钮为灰色，这时是不能点击的
    }
}
