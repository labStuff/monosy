package com.lixinyuyin.monosyllabicdetect.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.lixinyuyin.monosyllabicdetect.R;

/**
 * Created by zqj on 2015/8/19 16:26.
 */
public class ToneResolutionGraph extends View {
    private String[] xAxisText = {"125", "250", "500", "750", "1k", "1.5k", "2k", "3k", "4k", "5k", "8k"};
    private String[] yAxisText = {"1/2", "1/4", "1/8", "1/16", "1/32", "1/64", "1/128", "1/256", "1/512"};

    private float[] xAxis = new float[11];
    private float[] yAxis = new float[9];

    private int[] mDataIndex = new int[11];

    private float mPaddingLeft = 100;
    private float mPaddingRight = 50;
    private float mPaddingTop = 50;
    private float mPaddingDown = 50;

    private float mBorderWidth = 5.0f;
    private float mGridLineWidth = 2.0f;
    private float mTargetLineWidth = 5.0f;


    private float mDotRadius;

    private int mGridColor;
    private int mTextColor;
    private int mTargetLineColor;

    private int mTextSize;

    private Paint mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private TextPaint mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);

    private boolean mNeedRefresh = true;

    public ToneResolutionGraph(Context context) {
        super(context);
    }

    public ToneResolutionGraph(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ToneResolutionGraph(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.gridgraph);
        Resources res = getResources();

        mGridColor =
                attributes.getColor(R.styleable.gridgraph_grid_color,
                        res.getColor(R.color.gridgraph_gridcolor));
        mTextColor =
                attributes.getColor(R.styleable.gridgraph_axis_textcolor,
                        res.getColor(R.color.gridgraph_textcolor));
        mTargetLineColor =
                attributes.getColor(R.styleable.gridgraph_targetline_color,
                        res.getColor(R.color.gridgraph_linecolor));
        mTextSize =
                attributes.getDimensionPixelSize(R.styleable.gridgraph_axis_textsize,
                        res.getDimensionPixelSize(R.dimen.gridgraph_textsize));

        attributes.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mNeedRefresh) {
            initAxis();
            mNeedRefresh = false;
        }
        plotBorder(canvas);
        plotGridLines(canvas);
        drawAxisText(canvas);
        drawDataLine(canvas);

    }

    public void changeData(int xIndex, int yIndex) {
        mDataIndex[xIndex] = yIndex;
        invalidate();
    }


    /**
     * @param canvas
     * @title: plotBorder
     * @description: 绘制边界
     * @author: Zqj
     * @date: 2015年7月24日 下午4:31:40
     */
    private void plotBorder(Canvas canvas) {
        mLinePaint.setColor(mGridColor);
        mLinePaint.setStrokeWidth(mBorderWidth);
        canvas.drawLine(xAxis[0], yAxis[0], xAxis[0], yAxis[yAxis.length - 1], mLinePaint);
        canvas.drawLine(xAxis[xAxis.length - 1], yAxis[0], xAxis[xAxis.length - 1],
                yAxis[yAxis.length - 1], mLinePaint);
        canvas.drawLine(xAxis[0], yAxis[0], xAxis[xAxis.length - 1], yAxis[0], mLinePaint);
        canvas.drawLine(xAxis[0], yAxis[yAxis.length - 1], xAxis[xAxis.length - 1],
                yAxis[yAxis.length - 1], mLinePaint);
    }

    /**
     * @param canvas
     * @title: plotGridLines
     * @description: 绘制网格线
     * @author: Zqj
     * @date: 2015年7月24日 下午4:32:10
     */
    private void plotGridLines(Canvas canvas) {
        mLinePaint.setColor(mGridColor);
        mLinePaint.setStrokeWidth(mGridLineWidth);
        for (int i = 1; i < yAxis.length; i++) {
            canvas.drawLine(xAxis[0], yAxis[i], xAxis[xAxis.length - 1], yAxis[i], mLinePaint);
        }
        for (int i = 1; i < xAxis.length - 1; i++) {
            canvas.drawLine(xAxis[i], yAxis[0], xAxis[i], yAxis[yAxis.length - 1], mLinePaint);
        }
    }

    /**
     * @param canvas
     * @title: drawAxisText
     * @description: 绘制坐标轴labels
     * @author: Zqj
     * @date: 2015年7月24日 下午4:33:36
     */
    private void drawAxisText(Canvas canvas) {
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        for (int i = 0; i < xAxisText.length; i++) {
            canvas.drawText(xAxisText[i], xAxis[i], yAxis[0] - 10, mTextPaint);
        }
        mTextPaint.setTextAlign(Paint.Align.RIGHT);
        for (int i = 0; i < yAxisText.length; i++) {
            canvas.drawText(yAxisText[i], xAxis[0] - 5, yAxis[i] + 12, mTextPaint);
        }
    }

    /**
     * @param canvas
     * @title: drawTargetLine
     * @description: 绘制目标线
     * @author: Zqj
     * @date: 2015年7月24日 下午4:34:06
     */
    private void drawDataLine(Canvas canvas) {
        mLinePaint.setColor(mTargetLineColor);
        mLinePaint.setStrokeWidth(mTargetLineWidth);
        canvas.drawCircle(xAxis[0], yAxis[mDataIndex[0]], mDotRadius, mLinePaint);
        for (int i = 1; i < xAxis.length; i++) {
            canvas.drawLine(xAxis[i - 1], yAxis[mDataIndex[i - 1]], xAxis[i], yAxis[mDataIndex[i]], mLinePaint);
            canvas.drawCircle(xAxis[i], yAxis[mDataIndex[i]], mDotRadius, mLinePaint);
        }
    }

    /**
     * @title: calculateAxis
     * @description: 计算坐标
     * @author: Zqj
     * @date: 2015年7月24日 下午4:34:25
     */
    private void initAxis() {
        float width = getWidth();
        float height = getHeight();
        float xStep = (width - mPaddingLeft - mPaddingRight) / (xAxis.length - 1);
        float yStep = (height - mPaddingDown - mPaddingTop) / (yAxis.length - 1);
        mDotRadius = yStep / 10;
        for (int i = 0; i < xAxis.length; i++) {
            xAxis[i] = mPaddingLeft + xStep * i;
        }
        for (int i = 0; i < yAxis.length; i++) {
            yAxis[i] = mPaddingTop + i * yStep;
        }
    }
}
