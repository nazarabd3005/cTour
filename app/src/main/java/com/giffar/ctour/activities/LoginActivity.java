package com.giffar.ctour.activities;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.View;

import com.giffar.ctour.R;

import mehdi.sakout.fancybuttons.FancyButton;


/**
 * Created by nazar on 4/21/2016.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {
    FancyButton btnLogin,btnSignup;
    @Override
    public void initView() {
          btnLogin = (FancyButton) findViewById(R.id.btn_login);
        btnSignup = (FancyButton) findViewById(R.id.btn_signup);
//        final MaterialLoginView login = (MaterialLoginView) findViewById(R.id.login);
//        login.setListener(new MaterialLoginViewListener() {
//            @Override
//            public void onRegister(TextInputLayout registerUser, TextInputLayout registerPass, TextInputLayout registerPassRep) {
//                //Handle register
//                changeActivity(SignUpDetailActivity.class,true,null,0);
//            }
//
//            @Override
//            public void onLogin(TextInputLayout loginUser, TextInputLayout loginPass) {
//                //Handle login
//                changeActivity(MainActivity.class,true,null,0);
//            }
//        });
    }

    @Override
    public void setUICallbacks() {
        btnLogin.setOnClickListener(this);
        btnSignup.setOnClickListener(this);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_login;
    }

    @Override
    public void updateUI() {

    }

    @Override
    public void onClick(View v) {
        if (v == btnLogin) {
            changeActivity(MainActivity.class, true, null, 0);
        }else if (v == btnSignup){
            changeActivity(SignUpDetailActivity.class,true,null,0);
        }

    }
}
