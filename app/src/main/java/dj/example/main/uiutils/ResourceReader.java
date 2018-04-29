package dj.example.main.uiutils;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;

import dj.example.main.MyApplication;


/**
 * Created by COMP on 2/9/2016.
 */
public class ResourceReader {

    private static ResourceReader ourInstance;
    private static Context mContext;

    private ResourceReader(Context mContext) {
        ResourceReader.mContext = mContext;
    }

    public static ResourceReader getInstance() {
        if (ourInstance == null) {
            ourInstance = new ResourceReader(MyApplication.getInstance());
        }
        return ourInstance;
    }

    @TargetApi(Build.VERSION_CODES.M)
    public int getColorFromResource(int colorResId){
        if(Build.VERSION.SDK_INT == Build.VERSION_CODES.M)
            return mContext.getResources().getColor(colorResId, mContext.getTheme());
        else
            return mContext.getResources().getColor(colorResId);
    }


    public String getStringFromResource(int stringResId){
        return mContext.getResources().getString(stringResId);
    }


    public Drawable getDrawableFromResId(int drawableResId){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
           return mContext.getResources().getDrawable(drawableResId, mContext.getTheme());
        }
        else return mContext.getResources().getDrawable(drawableResId);
    }
}
