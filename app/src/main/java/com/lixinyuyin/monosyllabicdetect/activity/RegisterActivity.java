package com.lixinyuyin.monosyllabicdetect.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.lixinyuyin.monosyllabicdetect.R;
import com.lixinyuyin.monosyllabicdetect.database.userdata.UserDao;
import com.lixinyuyin.monosyllabicdetect.listener.DelayClickListener;
import com.lixinyuyin.monosyllabicdetect.util.StatusBarUtil;
import com.lixinyuyin.monosyllabicdetect.util.ToastUtil;
import com.lixinyuyin.monosyllabicdetect.view.FloatingEditText;
import com.lixinyuyin.monosyllabicdetect.view.PaperButton;

/**
 * Created by Administrator on 2015/8/14.
 */
public class RegisterActivity extends Activity implements View.OnClickListener {

    ImageView backImageView;
    PaperButton registerButton;
    FloatingEditText usernameEditText;
    FloatingEditText passwordEditText;
    FloatingEditText rePasswordEditText;

    String userName;
    String passWord;
    String rePassword;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        StatusBarUtil.setStatusBarColor(this);
        initView();
        mContext = this;
    }

    private void initView() {
        backImageView = (ImageView) findViewById(R.id.imageView_back);
        backImageView.setOnClickListener(this);
        registerButton = (PaperButton) findViewById(R.id.button_register);
        registerButton.setOnClickListener(new DelayClickListener(500) {
            @Override
            public void doClick(View v) {
                if (checkInput()) {
                    UserDao userDao = new UserDao(mContext);
                    if (userDao.isExists(userName)) {
                        ToastUtil.toast(mContext, R.string.already_exists);
                        userDao.close();
                        return;
                    }
                    userDao.add(userName, passWord);
                    userDao.close();
                    ToastUtil.toast(mContext, R.string.register_succeed);
                    finish();
                } else {
                    ToastUtil.toast(mContext, R.string.input_error);
                    return;
                }
            }
        });
        usernameEditText = (FloatingEditText) findViewById(R.id.edittext_username);
        passwordEditText = (FloatingEditText) findViewById(R.id.edittext_password);
        rePasswordEditText = (FloatingEditText) findViewById(R.id.edittext_repassword);
    }

    @Override
    public void onClick(View v) {
        if (v == backImageView) {
            finish();
        }
    }

    private boolean checkInput() {
        userName = usernameEditText.getText().toString();
        passWord = passwordEditText.getText().toString();
        rePassword = rePasswordEditText.getText().toString();
        if (userName.isEmpty())
            return false;
        if (passWord.isEmpty())
            return false;
        if (rePassword.isEmpty())
            return false;
        if (!passWord.equals(rePassword))
            return false;
        return true;
    }

    /*
    start activity
     */
    public static void start(Context context) {
        Intent intent = new Intent(context, RegisterActivity.class);
        context.startActivity(intent);
    }
}
