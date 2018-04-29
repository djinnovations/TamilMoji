/*
 * Copyright (c) 2015. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package dj.example.main.customviews;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by DJphy on 7/29/15.
 */
public class ScaleImageView extends AppCompatImageView {
    private ImageChangeListener imageChangeListener;
    private boolean scaleToWidth = false; // this flag determines if should measure height manually dependent of width

    public ScaleImageView(Context context) {
        super(context);
        init();
    }

    public ScaleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public ScaleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        this.setScaleType(ScaleType.CENTER_INSIDE);
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        if (imageChangeListener != null)
            imageChangeListener.changed((bm == null));
    }

    @Override
    public void setImageDrawable(Drawable d) {
        super.setImageDrawable(d);
        if (imageChangeListener != null)
            imageChangeListener.changed((d == null));
    }

    @Override
    public void setImageResource(int id){
        super.setImageResource(id);
    }

    public interface ImageChangeListener {
        // a callback for when a change has been made to this imageView
        void changed(boolean isEmpty);
    }

    public ImageChangeListener getImageChangeListener() {
        return imageChangeListener;
    }

    public void setImageChangeListener(ImageChangeListener imageChangeListener) {
        this.imageChangeListener = imageChangeListener;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        /**
         * if both width and height are set scale width first. modify in future if necessary
         */

        if(widthMode == MeasureSpec.EXACTLY || widthMode == MeasureSpec.AT_MOST){
            scaleToWidth = true;
        }else if(heightMode == MeasureSpec.EXACTLY || heightMode == MeasureSpec.AT_MOST){
            scaleToWidth = false;
        }else throw new IllegalStateException("width or height needs to be set to match_parent or a specific dimension");

        if(getDrawable()==null || getDrawable().getIntrinsicWidth()==0 ){
            // nothing to measure
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }else{
            final boolean isAboveKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
            int maxH=getMaxHeight();
            if(maxH>2000)
                maxH=-1;
            if(scaleToWidth){
                int iw = this.getDrawable().getIntrinsicWidth();
                int ih = this.getDrawable().getIntrinsicHeight();
                int heightC = width*ih/iw;
                if(height > 0)
                    if(heightC>height){
                        // dont let hegiht be greater then set max
                        heightC = height;
                        width = heightC*iw/ih;
                    }
                this.setScaleType(ScaleType.CENTER_CROP);


                if(isAboveKitKat && maxH!=-1 && heightC>maxH)
                    heightC=maxH;

                setMeasuredDimension(width, heightC);

            }else{
                // need to scale to height instead
                int marg = 0;
                if(getParent()!=null){
                    if(getParent().getParent()!=null){
                        marg+= ((RelativeLayout) getParent().getParent()).getPaddingTop();
                        marg+= ((RelativeLayout) getParent().getParent()).getPaddingBottom();
                    }
                }

                int iw = this.getDrawable().getIntrinsicWidth();
                int ih = this.getDrawable().getIntrinsicHeight();

                width = height*iw/ih;
                height-=marg;
                if(isAboveKitKat && maxH!=-1 &&  height>maxH)
                    height=maxH;
                setMeasuredDimension(width, height);
            }

        }
    }

}