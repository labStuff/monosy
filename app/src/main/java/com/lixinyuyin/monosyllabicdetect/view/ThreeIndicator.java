package com.lixinyuyin.monosyllabicdetect.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.LinearLayout;

import com.lixinyuyin.monosyllabicdetect.R;

/**
 * Created by zqj on 2015/10/8 18:05.
 */
public class ThreeIndicator extends LinearLayout {

    public ThreeIndicator(Context context) {
        super(context);
        init(context);
    }

    public ThreeIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ThreeIndicator(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }


    public final static int STAGE_ONE = 0;
    public final static int STAGE_TWO = 1;
    public final static int STAGE_THREE = 2;
    public final static int STAGE_FOUR = 3;

    private Context mContext;
    View indicatorI;
    View indicatorII;
    View indicatorIII;
    private AlphaAnimation animation;

    private void init(Context context) {
        mContext = context;
        inflate(mContext, R.layout.indicator, this);
        initView();
        initData();
    }

    private void initView() {
        indicatorI = findViewById(R.id.view_indicator1);
        indicatorII = findViewById(R.id.view_indicator2);
        indicatorIII = findViewById(R.id.view_indicator3);
    }

    private void initData() {
        animation = new AlphaAnimation(1.0f, 0.0f);
        animation.setDuration(1000);
        animation.setRepeatCount(Animation.INFINITE);
        animation.setRepeatMode(Animation.REVERSE);
    }

    public void show(int index) {
        indicatorI.clearAnimation();
        indicatorII.clearAnimation();
        indicatorIII.clearAnimation();
        indicatorI.setBackgroundResource(R.drawable.circle_gray);
        indicatorII.setBackgroundResource(R.drawable.circle_gray);
        indicatorIII.setBackgroundResource(R.drawable.circle_gray);
        switch (index) {
            case STAGE_ONE:
                indicatorI.setBackgroundResource(R.drawable.circle_green);
                indicatorI.setAnimation(animation);
                break;
            case STAGE_TWO:
                indicatorI.setBackgroundResource(R.drawable.circle_green);
                indicatorII.setBackgroundResource(R.drawable.circle_green);
                indicatorII.setAnimation(animation);
                break;
            case STAGE_THREE:
                indicatorI.setBackgroundResource(R.drawable.circle_green);
                indicatorII.setBackgroundResource(R.drawable.circle_green);
                indicatorIII.setBackgroundResource(R.drawable.circle_green);
                indicatorIII.setAnimation(animation);
                break;
            default:
                break;
        }
    }
}
