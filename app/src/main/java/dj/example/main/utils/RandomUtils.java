package dj.example.main.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import dj.example.main.activities.MainActivity;

/**
 * Created by DJphy on 08-07-2017.
 */

public class RandomUtils {


    private static RandomUtils ourInstance;

    public static RandomUtils getInstance(){
        if (ourInstance == null){
            ourInstance = new RandomUtils();
        }
        return ourInstance;
    }

    private RandomUtils(){

    }

    public static void clearInstance(){
        ourInstance = null;
    }

    public boolean isConnected(Context c) {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;
        ConnectivityManager cm = (ConnectivityManager)c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if(ni != null) {
            if(ni.getType() == 1 && ni.isConnectedOrConnecting()) {
                haveConnectedWifi = true;
            }

            if(ni.getType() == 0 && ni.isConnectedOrConnecting()) {
                haveConnectedMobile = true;
            }
        }

        return haveConnectedWifi || haveConnectedMobile;
    }

    public boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & 15) >= 3;
    }

    public boolean isPhone(Context context) {
        boolean isTab = isTablet(context);
        return !isTab;
    }

    public void launchHome(Activity activity, boolean isNewTask){
        Intent intent = new Intent(activity, MainActivity.class);
        if (isNewTask)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
        //finish();
    }
}
