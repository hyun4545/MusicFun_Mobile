package com.example.myungjong.musicfun.Helper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.myungjong.musicfun.Activity.DialogActivity;
import com.example.myungjong.musicfun.R;

/**
 * Created by myungjong on 2017/2/16.
 */

public class DialogHelper {

    public final static int EDIT_DIALOG=0;
    public final static int UPLOAD_DIALOG=1;
    Context context;
    int dialogNum;
    public DialogHelper(Context context,int dialogNum){
        this.context=context;
        this.dialogNum=dialogNum;

    }
    public void bulid(){
        Intent intent=new Intent(context,DialogActivity.class);
        Bundle bundle=new Bundle();
        bundle.putInt("dialogNum",dialogNum);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
    private View getDialogView(){
        View dialodView=View.inflate(context, R.layout.dialog_edit,null);
        return dialodView;
    };



}
