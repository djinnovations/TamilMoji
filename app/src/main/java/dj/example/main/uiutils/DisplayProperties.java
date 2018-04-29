package dj.example.main.uiutils;

import android.content.Context;
import android.content.res.Configuration;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import dj.example.main.MyApplication;


public class DisplayProperties {

    public static final float gridX = 100;
    public static final float gridY = 60;

    private static final String TAG = "DisplayProperties";

    private float pixels_per_cell_X;
    private float pixels_per_cell_Y;

    private DisplayMetrics metrics;
    private WindowManager wm;

    //private static Context mContext;
    //private static int orientation;
    private static DisplayProperties mDispPropInstance;

    private final int SMALL_FONT_CELLS = 2;
    private final int MEDIUM_FONT_CELLS = 3;
    private final int LARGE_FONT_CELLS = 4;
    private final int XTRA_LARGE_FONT_CELLS = 5;
    private final int HUGE_FONT_CELLS = 6;

    public float SMALL_FONT;
    public float MEDIUM_FONT;
    public float LARGE_FONT;
    public float XTRA_LARGE_FONT;
    public float HUGE_FONT;

    public static int ORIENTATION_LANDSCAPE = Configuration.ORIENTATION_LANDSCAPE;
    public static int ORIENTATION_PORTRAIT = Configuration.ORIENTATION_PORTRAIT;

    private static int mScreenOrientation;

    private DisplayProperties(int orientation) {
        metrics = new DisplayMetrics();
        wm = (WindowManager) MyApplication.getInstance().getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);

        calcPixelsPerCell(orientation);
        calcFontSizes();
    }


    public static void clearInstance() {
        mDispPropInstance = null;
    }


    public static DisplayProperties getInstance(/*Context context,*/ int screenOrientation) {
        try {
            //DisplayProperties.mContext = context;
            //DisplayProperties.orientation = screenOrientation;
            /**Creating an instance of Database helper class to fetch necessary data */
            if (mDispPropInstance == null) {
                mDispPropInstance = new DisplayProperties(screenOrientation);
            }
            else if(mScreenOrientation != screenOrientation){
                mScreenOrientation = screenOrientation;
                mDispPropInstance = new DisplayProperties(screenOrientation);
            }
            return mDispPropInstance;
        } catch (Exception ex) {
            Log.d(TAG, "Error in getting message. Details: \n" + ex.getMessage());
            return null;
        }
    }


    private void calcFontSizes() {

        SMALL_FONT = SMALL_FONT_CELLS * pixels_per_cell_X;

        MEDIUM_FONT = MEDIUM_FONT_CELLS * pixels_per_cell_X;

        LARGE_FONT = LARGE_FONT_CELLS * pixels_per_cell_X;

        XTRA_LARGE_FONT = XTRA_LARGE_FONT_CELLS * pixels_per_cell_X;

        HUGE_FONT = HUGE_FONT_CELLS * pixels_per_cell_X;
    }


    private void calcPixelsPerCell(int orientation) {
        if (/*DisplayProperties.*/orientation == ORIENTATION_LANDSCAPE) {
            pixels_per_cell_X = (metrics.widthPixels) / (gridX);
            pixels_per_cell_Y = (metrics.heightPixels) / (gridY);

        } else if (/*DisplayProperties.*/orientation == ORIENTATION_PORTRAIT) {// 1==portrait ; 0 == landscape
            pixels_per_cell_X = (metrics.widthPixels) / (gridY);
            pixels_per_cell_Y = (metrics.heightPixels) / (gridX);
        }
    }


    public float getXPixelsPerCell() {
        //Log.d("dj", "pixels x: " + pixels_per_cell_X);
        return pixels_per_cell_X;
    }


    public float getYPixelsPerCell() {
        //Log.d("dj", "pixels y: " + pixels_per_cell_Y);
        return pixels_per_cell_Y;
    }


}
