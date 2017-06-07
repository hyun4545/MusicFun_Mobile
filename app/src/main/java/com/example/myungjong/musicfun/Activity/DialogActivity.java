package com.example.myungjong.musicfun.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.example.myungjong.musicfun.Helper.DialogHelper;
import com.example.myungjong.musicfun.R;
import com.fivehundredpx.android.blur.BlurringView;

import eightbitlab.com.blurview.BlurView;

public class DialogActivity extends AppCompatActivity {

    private int dialogNum=-1;
    FrameLayout dialog_parent;
    BlurView dialog_blurView;
    RelativeLayout root;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_dialog);
        Bundle bundle=getIntent().getExtras();
        setBlurView();

        if(bundle!=null){
            dialogNum=bundle.getInt("dialogNum");
            addDialogView();
        }
    }

    private void addDialogView(){
        dialog_parent= (FrameLayout) findViewById(R.id.dialog_layout);
        View dialogView=View.inflate(this,getViewID(),null);
        dialog_parent.addView(dialogView);

    }
    private void setBlurView(){
        root= (RelativeLayout) findViewById(R.id.activity_dialog);
        dialog_blurView= (BlurView) findViewById(R.id.blurView_dialog);
        dialog_blurView.setupWith(root).windowBackground(getWindow().getDecorView().getBackground()).blurRadius(8f);
        dialog_blurView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private int getViewID(){
        int view_id=-1;
        switch (dialogNum){
            case DialogHelper.EDIT_DIALOG:
                view_id= R.layout.dialog_edit;
                break;
            case DialogHelper.UPLOAD_DIALOG:
                view_id= R.layout.modify_dialog;
                break;
        }
        return view_id;
    }
}
