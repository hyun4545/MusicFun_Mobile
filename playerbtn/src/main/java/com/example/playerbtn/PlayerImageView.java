package com.example.playerbtn;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by myungjong on 2017/2/19.
 */

public class PlayerImageView extends ImageView {
    public interface PlayerClickListener{
        void play();
        void pause();
    }
    int mode=0;
    Drawable image_drawable,image_drawable2;
    PlayerClickListener listener;
    public PlayerImageView(Context context) {
        super(context);
        init(context,null);

    }
public PlayerImageView(Context context, AttributeSet attrs){
    super(context,attrs);

    init(context,attrs);

}
    public PlayerImageView(Context context, AttributeSet attrs, int defStyle){
        super(context,attrs,defStyle);

        init(context,attrs);

    }
    public boolean isPlay(){
      return PlayerImageView.this.getDrawable()==image_drawable;
    }
    public void play(){
       this.setImageDrawable(image_drawable);
        mode =1;
        Drawable drawable=PlayerImageView.this.getDrawable();
        ((Animatable)drawable).start();
        Log.e("ImageDrawable","play");
    }
    public void pause(){
        this.setImageDrawable(image_drawable2);
        mode =0;
        Drawable drawable=PlayerImageView.this.getDrawable();
        ((Animatable)drawable).start();
    }
    public void setPlayerClickListener(PlayerClickListener listener){
        this.listener=listener;
    }
private void init(Context context, @Nullable AttributeSet attrs){

    String height = attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "layout_height");
    TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.PlayerImageView);
    int uu=ta.getColor(R.styleable.PlayerImageView_viewColor,Color.WHITE);
   // String color = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto","viewColor");

    Log.e("height",height);
    //Log.e("color",Integer.toString(uu));
    this.setBackground(getResources().getDrawable(R.drawable.stroke,null));
    GradientDrawable drawable = (GradientDrawable) this.getBackground();
    image_drawable = (getResources().getDrawable(R.drawable.anim,null));
    image_drawable2 =  (getResources().getDrawable(R.drawable.anim2,null));
    int hei=Integer.parseInt(height.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)")[0]);
    Log.e("hei",height.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)")[0]);
    //image_drawable.setColor(uu);
    //image_drawable2.setColor(uu);
    setColorFilter(uu);
    drawable.setCornerRadius(hei*2);
    drawable.setStroke(hei/13, uu);
    int i=hei/4;
    setPadding(i,i,i,i);
    this.setBackground(drawable);
    this.setImageDrawable(image_drawable);
    this.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.e("mode",Integer.toString(mode));
            if(mode==0){
                mode =1;

                PlayerImageView.this.setImageDrawable(image_drawable);
                listener.play();

            }else {
                mode =0;
                PlayerImageView.this.setImageDrawable(image_drawable2);
                listener.pause();
            }
            Drawable drawable=PlayerImageView.this.getDrawable();
            ((Animatable)drawable).start();


        }
    });
}
}
