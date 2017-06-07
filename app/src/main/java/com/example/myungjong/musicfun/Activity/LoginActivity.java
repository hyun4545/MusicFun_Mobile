package com.example.myungjong.musicfun.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.myungjong.musicfun.Helper.UrlConnect;
import com.example.myungjong.musicfun.R;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    Button submit_btn,register_btn;
    EditText et_email,et_pw;
    String email,pw;
    ImageView iv_icon;
    //String uri="http://webapplication5020160822045658.azurewebsites.net/api/Account/Login";
    String response_message;
    LinearLayout login_column;
    JSONObject json;
    ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;
    MyApplication application;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sharedPreferences=getSharedPreferences("token", Context.MODE_PRIVATE);
        application= (MyApplication) this.getApplication();
        Log.w("tnn",application.getToken());

        FitViewById();
        AnimateSet();
        SetProgress();
        if(!application.getToken().isEmpty()){
            //Log.w("tnn",getToken());
            //Toast.makeText(this,application.getToken(),Toast.LENGTH_LONG).show();
          Intent intent=new Intent(this,MainActivity.class);
            startActivity(intent);
            finish();
           // Toast.makeText(this,getToken(),Toast.LENGTH_LONG).show();
        }
        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email=et_email.getText().toString();
                pw=et_pw.getText().toString();
                if(!email.isEmpty()&&!pw.isEmpty()){
                    progressDialog.show();
                    json=new JSONObject();
                    try {
                        json.put("email",email);
                        json.put("password",pw);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    new Thread(){
                        @Override
                        public void run() {
                            super.run();
                            UrlConnect conn=new UrlConnect(MyApplication.uri+"Login","POST",UrlConnect.JSON_DATA,null);
                            conn.setJsonData(json);
                            response_message=conn.endToConnect().message;
                            Message msg=Message.obtain();
                            msg.obj=response_message;
                            mHandle.sendMessage(msg);
                        }
                    }.start();

                }else {
                    Toast.makeText(LoginActivity.this,"欄位不能為空!!",Toast.LENGTH_LONG).show();
                }



            }
        });
    }
    private void SetProgress(){
        progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(true);
        progressDialog.setMessage("登入中...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

    }
    private void AnimateSet(){

        Animation slide_am = new TranslateAnimation(0.0f, 0.0f, 0.0f, -500.0f);
        slide_am.setFillEnabled(true);
        slide_am.setDuration(1800);
        slide_am.setFillEnabled(true);

        slide_am.setRepeatCount(0);

        Animation Alpha_am = new AlphaAnimation(0f, 1.0f);
        Alpha_am.setFillEnabled(true);
        Alpha_am.setDuration(1800);

        Alpha_am.setRepeatCount(0);

        final AnimationSet amSet = new AnimationSet(false);
        amSet.addAnimation(slide_am);
        amSet.addAnimation(Alpha_am);
        amSet.setFillAfter(true);
        iv_icon.startAnimation(amSet);
        amSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                login_column.setAlpha(1);
                Animation Alpha_am = new AlphaAnimation(0f, 1.0f);
                Alpha_am.setFillEnabled(true);
                Alpha_am.setDuration(800);
                Alpha_am.setRepeatCount(0);
                Alpha_am.setFillAfter(true);
                login_column.startAnimation(Alpha_am);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }
    private Handler mHandle = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            String token=null;
            int code=12;
            String message = (String) msg.obj;
            try {
                JSONObject json=new JSONObject(message);
                token=json.getString("message");
                code=json.getInt("status");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(code==1){
                application.setToken(token);
                Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }else {
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this,token,Toast.LENGTH_LONG).show();
            }

        }
        /// }
    };
    /*private void storeToken(String token){
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("token",token).commit();
    }

    private String getToken(){

            return sharedPreferences.getString("token","");


    }*/

    @Override
    protected void onDestroy() {
        progressDialog.dismiss();
        super.onDestroy();
    }

    private void FitViewById() {
        submit_btn= (Button) findViewById(R.id.login_submit);
        register_btn= (Button) findViewById(R.id.btn_register);
        et_email= (EditText) findViewById(R.id.login_email);
        et_pw= (EditText) findViewById(R.id.login_pw);
        iv_icon= (ImageView) findViewById(R.id.login_logo);
        login_column= (LinearLayout) findViewById(R.id.login_column);
    }
}
