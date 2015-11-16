package com.lixinyuyin.monosyllabicdetect.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.lixinyuyin.monosyllabicdetect.R;

public class PureToneGraph extends View {

    private String[] xAxisText = {"125", "250", "500", "750", "1k", "1.5k", "2k", "3k", "4k", "5k", "8k"};
    private String[] yAxisText = {"0", "10", "20", "30", "40", "50", "60", "70", "80", "90", "100"};

    private float[] xAxis = new float[11];
    private float[] yAxis = new float[101];

    private float[] mMinLineYAxis = new float[11];
    private int[] mMinLineYAxisIndex = new int[11];

    private float[] mMediumLineYAxis = new float[11];
    private int[] mMediumLineYAxisIndex = new int[11];

    private float[] mMaxLineYAxis = new float[11];
    private int[] mMaxLineYAxisIndex = new int[11];

    private float mPaddingLeft = 100;
    private float mPaddingRight = 50;
    private float mPaddingTop = 50;
    private float mPaddingDown = 50;

    private float mBorderWidth = 5.0f;
    private float mGridLineWidth = 2.0f;
    private float mTargetLineWidth = 5.0f;

    private float mShortXLineOffset = 35;

    private float mDotRadius;

    private int mGridColor;
    private int mTextColor;
    private int mTargetLineColor;

    private int mTextSize;

    private Paint mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private TextPaint mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);

    private boolean mNeedRefresh = true;
    private boolean mNeedMapYAxis = false;

    public PureToneGraph(Context context) {
        super(context);
    }

    public PureToneGraph(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PureToneGraph(Context context, AttributeSet attrs, int defStyleAttr) {
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
        if (mNeedMapYAxis) {
            mapYAxis();
            mNeedMapYAxis = false;
        }
        plotBorder(canvas);
        plotGridLines(canvas);
        drawAxisText(canvas);
        drawMinLine(canvas);
        drawMediumLine(canvas);
        drawMaxLine(canvas);

    }


    public void setMinPoint(int xIndex, int yIndex) {
        if (mMinLineYAxis[xIndex] != yAxis[yIndex]) {
            mMinLineYAxis[xIndex] = yAxis[yIndex];
            mMinLineYAxisIndex[xIndex] = yIndex;
            invalidate();
        }
    }

    public void setMediumPoint(int xIndex, int yIndex) {
        if (mMediumLineYAxis[xIndex] != yAxis[yIndex]) {
            mMediumLineYAxis[xIndex] = yAxis[yIndex];
            mMediumLineYAxisIndex[xIndex] = yIndex;
            invalidate();
        }
    }

    public void setMaxPoint(int xIndex, int yIndex) {
        if (mMaxLineYAxis[xIndex] != yAxis[yIndex]) {
            mMaxLineYAxis[xIndex] = yAxis[yIndex];
            mMaxLineYAxisIndex[xIndex] = yIndex;
            invalidate();
        }
    }

    public void setGraph(GraphData data) {
        mMinLineYAxisIndex = data.getMinData();
        mMediumLineYAxisIndex = data.getMediumData();
        mMaxLineYAxisIndex = data.getMaxData();
        mNeedMapYAxis = true;
        invalidate();
    }

    public GraphData getLineData() {
        return new GraphData(mMinLineYAxisIndex, mMediumLineYAxisIndex, mMaxLineYAxisIndex,
                System.currentTimeMillis());
    }

    private void mapIndexToCoordinate(float[] coordinate, int[] index) {
        for (int i = 0; i < index.length; i++) {
            coordinate[i] = yAxis[index[i]];
        }
    }

    private void mapYAxis() {
        mapIndexToCoordinate(mMinLineYAxis, mMinLineYAxisIndex);
        mapIndexToCoordinate(mMediumLineYAxis, mMediumLineYAxisIndex);
        mapIndexToCoordinate(mMaxLineYAxis, mMaxLineYAxisIndex);
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
            if (i % 10 == 0) {
                canvas.drawLine(xAxis[0], yAxis[i], xAxis[xAxis.length - 1], yAxis[i], mLinePaint);
            }
            if (i % 5 == 0) {
                canvas.drawLine(xAxis[0], yAxis[i], xAxis[0] + mShortXLineOffset, yAxis[i],
                        mLinePaint);
                canvas.drawLine(xAxis[xAxis.length - 1], yAxis[i], xAxis[xAxis.length - 1]
                        - mShortXLineOffset, yAxis[i], mLinePaint);
            }
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
        mTextPaint.setTextAlign(Align.CENTER);
        for (int i = 0; i < xAxisText.length; i++) {
            canvas.drawText(xAxisText[i], xAxis[i], yAxis[0] - 10, mTextPaint);
        }
        mTextPaint.setTextAlign(Align.RIGHT);
        for (int i = 0; i < yAxisText.length; i++) {
            canvas.drawText(yAxisText[i], xAxis[0] - 5, yAxis[10 * i] + 20, mTextPaint);
        }
    }

    /**
     * @param canvas
     * @title: drawTargetLine
     * @description: 绘制目标线
     * @author: Zqj
     * @date: 2015年7月24日 下午4:34:06
     */
    private void drawMinLine(Canvas canvas) {
        mLinePaint.setColor(Color.parseColor("#0000ff"));//blue
        mLinePaint.setStrokeWidth(mTargetLineWidth);
        canvas.drawCircle(xAxis[0],mMinLineYAxis[0],mDotRadius,mLinePaint);
        for (int i = 1; i < xAxis.length; i++) {
            canvas.drawLine(xAxis[i - 1], mMinLineYAxis[i - 1], xAxis[i], mMinLineYAxis[i], mLinePaint);
            canvas.drawCircle(xAxis[i], mMinLineYAxis[i], mDotRadius, mLinePaint);
        }
    }

    private void drawMediumLine(Canvas canvas) {
        mLinePaint.setColor(Color.parseColor("#00ff00"));//green
        mLinePaint.setStrokeWidth(mTargetLineWidth);
        canvas.drawCircle(xAxis[0], mMediumLineYAxis[0], mDotRadius, mLinePaint);
        for (int i = 1; i < xAxis.length; i++) {
            canvas.drawLine(xAxis[i - 1], mMediumLineYAxis[i - 1], xAxis[i], mMediumLineYAxis[i], mLinePaint);
            canvas.drawCircle(xAxis[i], mMediumLineYAxis[i], mDotRadius, mLinePaint);
        }
    }

    private void drawMaxLine(Canvas canvas) {
        mLinePaint.setColor(Color.parseColor("#ff0000"));//red
        mLinePaint.setStrokeWidth(mTargetLineWidth);
        canvas.drawCircle(xAxis[0], mMaxLineYAxis[0], mDotRadius, mLinePaint);
        for (int i = 1; i < xAxis.length; i++) {
            canvas.drawLine(xAxis[i - 1], mMaxLineYAxis[i - 1], xAxis[i], mMaxLineYAxis[i], mLinePaint);
            canvas.drawCircle(xAxis[i], mMaxLineYAxis[i], mDotRadius, mLinePaint);
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
        mDotRadius = yStep;
        for (int i = 0; i < xAxis.length; i++) {
            xAxis[i] = mPaddingLeft + xStep * i;
        }
        for (int i = 0; i < yAxis.length; i++) {
            yAxis[i] = mPaddingTop + i * yStep;
        }
        for (int i = 0; i < xAxis.length; i++) {
            mMinLineYAxis[i] = yAxis[0];
            mMediumLineYAxis[i] = yAxis[0];
            mMaxLineYAxis[i] = yAxis[0];
        }
    }
}
