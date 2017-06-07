package com.example.myungjong.musicfun.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myungjong.musicfun.Helper.UrlConnect;
import com.example.myungjong.musicfun.R;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    Button submit_btn;
    EditText et_name,et_email,et_pw;
    String name,email,pw;
   // String uri="http://webapplication5020160822045658.azurewebsites.net/api/Account/Register";
    String response_message;
    ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;
    MyApplication application;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        FitViewById();
        SetProgress();
        sharedPreferences=getSharedPreferences("token", Context.MODE_PRIVATE);
        application= (MyApplication) this.getApplication();

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name=et_name.getText().toString();
                email=et_email.getText().toString();
                pw=et_pw.getText().toString();
                final JSONObject json=new JSONObject();
                if(!email.isEmpty()&&!pw.isEmpty()){
                    progressDialog.show();
                    try {
                        json.put("name",name);
                        json.put("email",email);
                        json.put("password",pw);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    new Thread(){
                        @Override
                        public void run() {
                            super.run();
                            UrlConnect conn=new UrlConnect(MyApplication.uri+"Register","POST",UrlConnect.JSON_DATA,null);
                            conn.setJsonData(json);
                            response_message=conn.endToConnect().message;
                            Message msg=Message.obtain();
                            msg.obj=response_message;
                            mHandle.sendMessage(msg);
                        }
                    }.start();
                }else {
                    Toast.makeText(RegisterActivity.this,"帳號、密碼不能為空!!",Toast.LENGTH_LONG).show();
                }


            }
        });
    }

    private void SetProgress(){
        progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(true);
        progressDialog.setMessage("註冊中...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

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
                Intent intent=new Intent(RegisterActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }else {
                progressDialog.dismiss();
                Toast.makeText(RegisterActivity.this,token,Toast.LENGTH_LONG).show();
            }

        }
        /// }
    };
   /* private void storeToken(String token){
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("token",token);
    }
    private String getToken(){
        return sharedPreferences.getString("token","");
    }*/
    private void FitViewById() {
        submit_btn= (Button) findViewById(R.id.register_submit);
        et_name= (EditText) findViewById(R.id.register_name);
        et_email= (EditText) findViewById(R.id.register_email);
        et_pw= (EditText) findViewById(R.id.register_pw);
    }
}
