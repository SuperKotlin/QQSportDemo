package com.example.zhuyong.qqsportdemo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by zhuyong on 2017/6/14.
 * 自定义View
 */
public class QQSportView extends View {
    private Context mContext;
    private int mOutBgColor = Color.BLUE;
    private int mInBgColor = Color.RED;
    private float mPaintOutWidth;
    private int mTextColor = Color.RED;
    private float mTextSize;

    private Paint mPaintOut;//大圆弧画笔
    private Paint mPaintIn;//当前步数的画笔
    private Paint mPaintTextLabel;
    private Paint mPaintText;
    private static final String mTextLabel = "今日步数";

    private int mNumMax = 10000;
    private int mNumCurrent = 0;

    public QQSportView(Context context) {
        this(context, null);
    }

    public QQSportView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QQSportView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.QQSportView);
        mOutBgColor = array.getColor(R.styleable.QQSportView_out_bg_color, mOutBgColor);
        mInBgColor = array.getColor(R.styleable.QQSportView_in_bg_color, mInBgColor);
        mPaintOutWidth = array.getDimension(R.styleable.QQSportView_circle_width, Util.dip2px(mContext, 10));//默认10dp
        mTextColor = array.getColor(R.styleable.QQSportView_text_color, mTextColor);
        mTextSize = array.getDimension(R.styleable.QQSportView_text_size, Util.dip2px(mContext, 40));//默认40sp
        array.recycle();
        initPaint();
    }


    private void initPaint() {
        //画背景圆弧
        mPaintOut = new Paint();
        mPaintOut.setAntiAlias(true);
        mPaintOut.setStrokeWidth(mPaintOutWidth);
        mPaintOut.setColor(mOutBgColor);
        mPaintOut.setStyle(Paint.Style.STROKE);
        mPaintOut.setStrokeCap(Paint.Cap.ROUND);
        //画当前步数
        mPaintIn = new Paint();
        mPaintIn.setAntiAlias(true);
        mPaintIn.setStrokeWidth(mPaintOutWidth);
        mPaintIn.setColor(mInBgColor);
        mPaintIn.setStyle(Paint.Style.STROKE);
        mPaintIn.setStrokeCap(Paint.Cap.ROUND);
        //画“今日步数”
        mPaintTextLabel = new Paint();
        mPaintTextLabel.setAntiAlias(true);
        mPaintTextLabel.setColor(Color.GRAY);
        mPaintTextLabel.setStyle(Paint.Style.STROKE);
        mPaintTextLabel.setTextSize(Util.dip2px(mContext, 16));
        //画数字
        mPaintText = new Paint();
        mPaintText.setAntiAlias(true);
        mPaintText.setColor(mTextColor);
        mPaintText.setStyle(Paint.Style.STROKE);
        mPaintText.setTextSize(mTextSize);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int length = MeasureSpec.getSize(widthMeasureSpec) > MeasureSpec.getSize(heightMeasureSpec) ?
                MeasureSpec.getSize(heightMeasureSpec) : MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(length, length);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //外圆弧,因为画笔有一定的宽度，所有画圆弧的范围要比View本身的大小稍微小一些，不然画笔画出来的东西会显示不完整
        RectF rectF = new RectF(mPaintOutWidth / 2, mPaintOutWidth / 2, getWidth() - mPaintOutWidth / 2, getHeight() - mPaintOutWidth / 2);
        canvas.drawArc(rectF, 135, 270, false, mPaintOut);

        //内圆弧
        if (mNumMax <= 0) return;
        float sweepAngle = (float) mNumCurrent / mNumMax;
        canvas.drawArc(rectF, 135, sweepAngle * 270, false, mPaintIn);

        //字（今日步数）
        Rect textBoundsLabel = new Rect();
        mPaintTextLabel.getTextBounds(mTextLabel, 0, mTextLabel.length(), textBoundsLabel);

        int dexLabel = getWidth() / 2 - textBoundsLabel.width() / 2;
        Paint.FontMetricsInt fontMetrics1 = mPaintTextLabel.getFontMetricsInt();
        int linrLabel = (fontMetrics1.bottom - fontMetrics1.top) / 2 - fontMetrics1.bottom;
        int dyLabel = getHeight() / 3 + linrLabel;
        canvas.drawText(mTextLabel, dexLabel, dyLabel, mPaintTextLabel);

        //字(多少步)
        String mText = mNumCurrent + "";
        Rect textBounds = new Rect();
        mPaintText.getTextBounds(mText, 0, mText.length(), textBounds);

        int dex = getWidth() / 2 - textBounds.width() / 2;
        Paint.FontMetricsInt fontMetrics = mPaintText.getFontMetricsInt();
        int line = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom;
        int dy = (int) (getHeight() / 1.5 + line);
        canvas.drawText(mText, dex, dy, mPaintText);
    }

    public void setMaxNum(int max) {
        this.mNumMax = max;
    }

    /**
     * 设置当前步数并进行重绘
     *
     * @param current
     */
    public void setCurrent(int current) {
        this.mNumCurrent = current;
        invalidate();
    }

    /**
     * 设置弧度的宽度
     *
     * @param width
     */
    public void setWidth(int width) {
        this.mPaintOutWidth = width;
        initPaint();
        invalidate();
    }

    /**
     * 设置当大圆弧的颜色
     *
     * @param outPaintColor
     */
    public void setOutPaintColor(int outPaintColor) {
        this.mOutBgColor = outPaintColor;
        initPaint();
        invalidate();
    }

    /**
     * 设置当小圆弧的颜色
     *
     * @param inPaintColor
     */
    public void setInPaintColor(int inPaintColor) {
        this.mInBgColor = inPaintColor;
        initPaint();
        invalidate();
    }
}
