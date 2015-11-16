package com.lixinyuyin.monosyllabicdetect.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.lixinyuyin.monosyllabicdetect.R;
import com.lixinyuyin.monosyllabicdetect.database.userdata.UserDao;
import com.lixinyuyin.monosyllabicdetect.model.VAccount;
import com.lixinyuyin.monosyllabicdetect.util.ToastUtil;

/**
 * Created by Administrator on 2015/8/14.
 */
public class LoginActivity extends Activity implements View.OnClickListener {

    Button registerButton;
    Button loginButton;

    EditText userNameEditText;
    EditText passwordEditText;

    private String userName;
    private String passWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (VAccount.hasAccount()) {
            OptionActivity.start(this);
            finish();
        }
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {
        registerButton = (Button) findViewById(R.id.button_register);
        registerButton.setOnClickListener(this);
        loginButton = (Button) findViewById(R.id.button_login);
        loginButton.setOnClickListener(this);

        userNameEditText = (EditText) findViewById(R.id.edittext_username);
        passwordEditText = (EditText) findViewById(R.id.edittext_password);
    }

    @Override
    public void onClick(View v) {
        if (v == registerButton) {
            RegisterActivity.start(this);
        } else if (v == loginButton) {
            if (checkInput()) {
                UserDao userDao = new UserDao(this);
                if (userDao.isValid(userName, passWord)) {
                    userDao.close();
                    VAccount account = new VAccount(userName, passWord);
                    VAccount.saveAccount(account);
                    OptionActivity.start(this);
                    finish();
                } else {
                    ToastUtil.toast(this, R.string.login_error);
                    userDao.close();
                }
            } else {
                ToastUtil.toast(this, R.string.input_error);
            }
        }
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
