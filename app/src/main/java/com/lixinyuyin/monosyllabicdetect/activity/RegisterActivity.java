package com.lixinyuyin.monosyllabicdetect.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.lixinyuyin.monosyllabicdetect.R;
import com.lixinyuyin.monosyllabicdetect.database.userdata.UserDao;
import com.lixinyuyin.monosyllabicdetect.util.ToastUtil;

/**
 * Created by Administrator on 2015/8/14.
 */
public class RegisterActivity extends Activity implements View.OnClickListener {

    ImageView backImageView;
    Button registerButton;
    EditText usernameEditText;
    EditText passwordEditText;
    EditText rePasswordEditText;

    String userName;
    String passWord;
    String rePassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        setContentView(R.layout.activity_register);
        initView();
    }

    private void initView() {
        backImageView = (ImageView) findViewById(R.id.imageView_back);
        backImageView.setOnClickListener(this);
        registerButton = (Button) findViewById(R.id.button_register);
        registerButton.setOnClickListener(this);
        usernameEditText = (EditText) findViewById(R.id.edittext_username);
        passwordEditText = (EditText) findViewById(R.id.edittext_password);
        rePasswordEditText = (EditText) findViewById(R.id.edittext_repassword);
    }

    @Override
    public void onClick(View v) {
        if (v == backImageView) {
            finish();
        } else if (v == registerButton) {
            if (checkInput()) {
                UserDao userDao = new UserDao(this);
                if (userDao.isExists(userName)) {
                    ToastUtil.toast(this, R.string.already_exists);
                    userDao.close();
                    return;
                }
                userDao.add(userName, passWord);
                userDao.close();
                ToastUtil.toast(this, R.string.register_succeed);
                finish();
            } else {
                ToastUtil.toast(this, R.string.input_error);
                return;
            }
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
