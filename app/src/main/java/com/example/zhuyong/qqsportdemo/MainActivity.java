package com.example.zhuyong.qqsportdemo;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.SeekBar;
import android.widget.TextView;

import cn.qqtheme.framework.picker.ColorPicker;

public class MainActivity extends AppCompatActivity {

    private TextView mTvPaintWidth;
    private TextView mTvCurrentNum;
    private SeekBar mSbPaintWidth;
    private SeekBar mSbCurrentNum;
    private QQSportView qqsport_view;
    private int mCurrentNum = 8555;//当前步数,默认8555，最高10000

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        qqsport_view = (QQSportView) findViewById(R.id.qqsport_view);
        mSbPaintWidth = (SeekBar) findViewById(R.id.sb);
        mSbCurrentNum = (SeekBar) findViewById(R.id.sb_num);
        mTvPaintWidth = (TextView) findViewById(R.id.tv_paint_width);
        mTvCurrentNum = (TextView) findViewById(R.id.tv_current_num);

        qqsport_view.setMaxNum(10000);//mCurrentNum不能超过此数值

        final ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, mCurrentNum);
        valueAnimator.setDuration(2000);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float current = (float) animation.getAnimatedValue();
                qqsport_view.setCurrent((int) current);
            }
        });
        valueAnimator.start();

        /**
         * 修改画笔宽度
         */
        mSbPaintWidth.setProgress(Util.dip2px(MainActivity.this, 10));
        mSbPaintWidth.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mTvPaintWidth.setText("画笔宽度" + Util.px2dip(MainActivity.this, progress) + "dp");
                qqsport_view.setWidth(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                valueAnimator.start();
            }
        });
        /**
         * 修改步数
         */
        mSbCurrentNum.setProgress(mCurrentNum);
        mTvCurrentNum.setText("当前步数" + mCurrentNum);
        mSbCurrentNum.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mTvCurrentNum.setText("当前步数" + progress);
                mCurrentNum = progress;
                qqsport_view.setCurrent(mCurrentNum);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                valueAnimator.setFloatValues(mCurrentNum);
                valueAnimator.start();
            }
        });
        /**
         * 修改颜色
         */
        findViewById(R.id.btn_big_rad).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorPicker picker = new ColorPicker(MainActivity.this);
                picker.setOnColorPickListener(new ColorPicker.OnColorPickListener() {
                    @Override
                    public void onColorPicked(int pickedColor) {
                        qqsport_view.setOutPaintColor(pickedColor);
                        valueAnimator.start();
                    }
                });
                picker.show();
            }
        });

        /**
         * 修改颜色
         */
        findViewById(R.id.btn_small_rad).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorPicker picker = new ColorPicker(MainActivity.this);
                picker.setOnColorPickListener(new ColorPicker.OnColorPickListener() {
                    @Override
                    public void onColorPicked(int pickedColor) {
                        qqsport_view.setInPaintColor(pickedColor);
                        valueAnimator.start();
                    }
                });
                picker.show();
            }
        });

    }
}
