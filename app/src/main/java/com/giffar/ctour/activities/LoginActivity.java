package com.giffar.ctour.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.giffar.ctour.APP;
import com.giffar.ctour.Preferences;
import com.giffar.ctour.R;
import com.giffar.ctour.callbacks.OnMemberCallback;
import com.giffar.ctour.controllers.GCMController;
import com.giffar.ctour.controllers.MemberController;
import com.giffar.ctour.entitys.Member;
import com.loopj.android.http.RequestParams;

import java.util.List;

import mehdi.sakout.fancybuttons.FancyButton;


/**
 * Created by nazar on 4/21/2016.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {
    FancyButton btnLogin,btnSignup;
    EditText etUsername,etPassword;
    MemberController memberController;
    ProgressDialog progressDialog;
    @Override
    public void initView() {
        progressDialog = new ProgressDialog(context);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setMessage("Loading.....");
        progressDialog.setCancelable(true);
        memberController = new MemberController(context);
        etUsername = (EditText) findViewById(R.id.et_username);
        etPassword = (EditText) findViewById(R.id.et_password);
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

    public void onLogin(){
        progressDialog.show();
        RequestParams requestParams = new RequestParams();
        requestParams.put(Member.USERNAME,etUsername.getText().toString());
        requestParams.put(Member.PASSWORD,etPassword.getText().toString());
        requestParams.put(Member.GCM_ID,APP.getConfig(context, GCMController.PROPERTY_REG_ID));
        memberController.postResponseLogin(requestParams, new OnMemberCallback() {
            @Override
            public void onSuccess(Member member, String message) {
                if(APP.getConfig(context, Preferences.CLUB_ID).equals("null")|| APP.getConfig(context, Preferences.CLUB_ID).equals("")){
                    changeActivity(PickClubActivity.class,true,null,0);
                }else{
                    changeActivity(MainActivity.class,true,null,0);
                }


            }

            @Override
            public void onSuccess(List<Member> members, String message) {

            }

            @Override
            public void onFailed(String message) {
                Toast.makeText(context,message,Toast.LENGTH_SHORT).show();

            }

            @Override
            public void OnFinish() {
                progressDialog.dismiss();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == btnLogin) {
            onLogin();
        }else if (v == btnSignup){
            changeActivity(SignUpDetailActivity.class,false,null,0);
        }

    }
}
