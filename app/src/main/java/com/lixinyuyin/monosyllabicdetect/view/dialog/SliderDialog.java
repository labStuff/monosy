package com.lixinyuyin.monosyllabicdetect.view.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lixinyuyin.monosyllabicdetect.R;
import com.lixinyuyin.monosyllabicdetect.view.buttonflat.ButtonFlat;
import com.lixinyuyin.monosyllabicdetect.view.slider.Slider;

/**
 * Created by zqj on 2015/11/20 16:59.
 */
public class SliderDialog extends Dialog {
    // a dialog includes black background and content
    private View backgroundView;
    private View contentView;

    private TextView titleTextView;
    private TextView hintTextView;
    private Slider slider;
    private ButtonFlat sureButton;
    private ButtonFlat cancelButton;

    private Context mContext;
    private boolean mCancelable = true;

    private int sliderMaxNum;


    public SliderDialog(Context context, int num) {
        super(context, android.R.style.Theme_Translucent);

        mContext = context;
        sliderMaxNum = num;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comm_slider_dialog);

        backgroundView = (RelativeLayout) findViewById(R.id.dialog_rootView);
        backgroundView.setOnTouchListener(new View.OnTouchListener() {

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mCancelable) {
                    if (event.getX() < contentView.getLeft()
                            || event.getX() > contentView.getRight()
                            || event.getY() > contentView.getBottom()
                            || event.getY() < contentView.getTop()) {
                        dismiss();
                    }
                }
                return false;
            }
        });
        contentView = (RelativeLayout) findViewById(R.id.contentDialog);

        titleTextView = (TextView) findViewById(R.id.title);
        slider = (Slider) findViewById(R.id.slider);
        slider.setMin(1);
        slider.setMax(sliderMaxNum);
        slider.setOnValueChangedListener(new Slider.OnValueChangedListener() {
            @Override
            public void onValueChanged(int value) {
                hintTextView.setText(String.format(mContext.getString(R.string.selected_num_hint), value));
            }
        });
        sureButton = (ButtonFlat) findViewById(R.id.button_accept);
        sureButton.setTextSize(16);
        sureButton.setRippleColor(mContext.getResources().getColor(R.color.black_300));
        hintTextView = (TextView) findViewById(R.id.textView_choiceHint);
    }

    @Override
    public void show() {
        super.show();
        contentView.startAnimation(AnimationUtils.loadAnimation(mContext,
                R.anim.material_dialog_main_show));
        backgroundView.startAnimation(AnimationUtils.loadAnimation(mContext,
                R.anim.material_dialog_root_show));
    }

    @Override
    public void dismiss() {
        Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.material_dialog_main_hide);
        anim.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                contentView.post(new Runnable() {
                    @Override
                    public void run() {
                        SliderDialog.super.dismiss();
                    }
                });
            }
        });
        contentView.startAnimation(anim);
        Animation backAnim =
                AnimationUtils.loadAnimation(mContext, R.anim.material_dialog_root_hide);
        backgroundView.startAnimation(backAnim);
    }

    /**
     * Set title, message, button text and listener;
     */
    /**
     * step 1
     */
    public void setTitle(int resid) {
        titleTextView.setText(resid);
    }

    public void setTitle(CharSequence text) {
        titleTextView.setText(text);
    }

    /**
     * step 3
     */
    public void setSureButton(@NonNull int resid, final OnSureListener listener) {
        if (resid < 0) {
            return;
        }
        setSureButton(mContext.getResources().getString(resid), listener);
    }

    public void setSureButton(@NonNull CharSequence text, final OnSureListener listener) {
        sureButton.setText(text);
        sureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (null != listener) {
                    listener.onSure(slider.getValue());
                }
            }
        });
    }

    /**
     * step 4
     */
    public void setCancelButton(int resid, View.OnClickListener listener) {
        if (resid < 0) {
            return;
        }
        setCancelButton(mContext.getResources().getString(resid), listener);
    }

    public void setCancelButton(CharSequence text, final View.OnClickListener listener) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        cancelButton.setVisibility(View.VISIBLE);
        cancelButton.setText(text);
        cancelButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
                if (null != listener) {
                    listener.onClick(v);
                }
            }
        });
    }

    /**
     * step 5
     */
    @Override
    public void setCancelable(boolean flag) {
        super.setCancelable(flag);
        mCancelable = flag;
    }

    public interface OnSureListener {
        void onSure(int value);
    }
}
