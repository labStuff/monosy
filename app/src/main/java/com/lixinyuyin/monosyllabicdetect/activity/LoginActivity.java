package com.lixinyuyin.monosyllabicdetect.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.lixinyuyin.monosyllabicdetect.R;
import com.lixinyuyin.monosyllabicdetect.database.userdata.UserDao;
import com.lixinyuyin.monosyllabicdetect.listener.DelayClickListener;
import com.lixinyuyin.monosyllabicdetect.model.VAccount;
import com.lixinyuyin.monosyllabicdetect.util.StatusBarUtil;
import com.lixinyuyin.monosyllabicdetect.util.ToastUtil;
import com.lixinyuyin.monosyllabicdetect.view.FloatingEditText;
import com.lixinyuyin.monosyllabicdetect.view.PaperButton;

/**
 * Created by Administrator on 2015/8/14.
 */
public class LoginActivity extends Activity {

    private final int mDelay = 500;

    PaperButton registerButton;
    PaperButton loginButton;

    FloatingEditText userNameEditText;
    FloatingEditText passwordEditText;

    private String userName;
    private String passWord;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (VAccount.hasAccount()) {
            OptionActivity.start(this);
            finish();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        StatusBarUtil.setStatusBarColor(this);
        initView();
        mContext = this;
    }

    private void initView() {
        registerButton = (PaperButton) findViewById(R.id.button_register);
        registerButton.setOnClickListener(new DelayClickListener(mDelay) {
            @Override
            public void doClick(View v) {
                RegisterActivity.start(mContext);
            }
        });
        loginButton = (PaperButton) findViewById(R.id.button_login);
        loginButton.setOnClickListener(new DelayClickListener(mDelay) {
            @Override
            public void doClick(View v) {
                if (checkInput()) {
                    UserDao userDao = new UserDao(mContext);
                    if (userDao.isValid(userName, passWord)) {
                        userDao.close();
                        VAccount account = new VAccount(userName, passWord);
                        VAccount.saveAccount(account);
                        OptionActivity.start(mContext);
                        finish();
                    } else {
                        ToastUtil.toast(mContext, R.string.login_error);
                        userDao.close();
                    }
                } else {
                    ToastUtil.toast(mContext, R.string.input_error);
                }
            }
        });

        userNameEditText = (FloatingEditText) findViewById(R.id.edittext_username);
        passwordEditText = (FloatingEditText) findViewById(R.id.edittext_password);
    }


    private boolean checkInput() {
        userName = userNameEditText.getText().toString();
        passWord = passwordEditText.getText().toString();
        if (userName.isEmpty())
            return false;
        if (passWord.isEmpty())
            return false;
        return true;
    }


    public static void start(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }
}
