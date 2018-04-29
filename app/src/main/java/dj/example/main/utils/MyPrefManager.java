package dj.example.main.utils;

import android.content.SharedPreferences;
import android.util.Log;

import dj.example.main.MyApplication;

/**
 * Created by DJphy on 08-07-2017.
 */

public class MyPrefManager {

    private static final String TAG = "MyPrefManager";
    private SharedPreferences pref;
    private static MyPrefManager mPrefManager;

    private SharedPreferences.Editor editor;
    // Shared pref mode
    int PRIVATE_MODE = 0;

    private final String PREF_NAME = "finder_primary_pref_file";
    public static final String KEY_LOGIN_STATUS = "login_stat";
    public static final String KEY_LOGIN_MODE = "login_mode";

    private final String KEY_IS_INTIMATION_STOP = "intimation_stat";
    private final String KEY_INTIMATION_COUNT = "intimation_count";

    private static final String KEY_NOTIFICATIONS_FCM = "notifications";
    private final String KEY_IS_APP_RATING_DONE = "app_rating";
    private final String KEY_RATING_SESSION_COUNT = "app_rate_session_count";
    private final String KEY_RATING_IS_DAYS_COUNT_STARTED = "app_rate_day_count";
    private final String KEY_RATING_START_TIME = "app_rate_start_time";

    public static final String MODE_SOCIAL = "social";
    public static final String MODE_NORMAL = "normal";
    public static final String MODE_SOCIAL_GL = "social_gl";
    public static final String MODE_SOCIAL_FB = "social_fb";

    private MyPrefManager() {
        pref = MyApplication.getInstance().getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }


    public void setIsLoginDone(String signInMode, boolean status){
        editor.putBoolean(KEY_LOGIN_STATUS, status);
        editor.putString(KEY_LOGIN_MODE, signInMode);
        editor.commit();
        Log.d(TAG, "setIsLoginDone - MyPrefManager: " + status);
    }

    public static MyPrefManager getInstance() {
        if (mPrefManager == null) {
            mPrefManager = new MyPrefManager();
        }
        return mPrefManager;
    }

    public static void clearInstance() {
        mPrefManager = null;
    }

    public boolean getLoginStatus(){
        boolean txt = pref.getBoolean(KEY_LOGIN_STATUS, false);
        Log.d(TAG, "getLoginStatus - MyPrefManager: " + txt);
        return txt;
    }

    public void clearLoginParams(){
        editor.putBoolean(KEY_LOGIN_STATUS, false);
        editor.commit();
        Log.d(TAG, "clearLoginParams - MyPrefManager: " + "cleared success");
    }


    public void setAppRatingDone() {
        editor.putBoolean(KEY_IS_APP_RATING_DONE, true);
        editor.commit();
    }

    public boolean getIsAppRatingDone() {
        Log.d("dj", "getIsAppRatingDone- DjphyPreferenceManager: " + /*pref.getBoolean(KEY_IS_APP_RATING_DONE, false)*/"true");
        return /*pref.getBoolean(KEY_IS_APP_RATING_DONE, false)*/ true;
    }


    public final static int DAYS_UNTIL_PROMPT = 5;
    public final static int LAUNCHES_UNTIL_PROMPT = 5;

    public boolean canStartForRatingApp() {
        if (getDaysDiffFromStarted() >= DAYS_UNTIL_PROMPT
                || getSessionCount() >= LAUNCHES_UNTIL_PROMPT) {
            if (getSessionCount() >= LAUNCHES_UNTIL_PROMPT) {
                Log.d(TAG, "canStartForRatingApp(): true");
                return true;
            }
        }
        Log.d(TAG, "canStartForRatingApp(): false");
        return false;
    }

    /*private void setTriggerForRating() {
        editor.putBoolean(KEY_RATING_TRIGGER, true);
    }*/

    public void updateSessionCounts() {
        editor.putInt(KEY_RATING_SESSION_COUNT, getSessionCount() + 1);
        editor.commit();
    }

    public void resetAppRateSessionCount() {
        editor.putInt(KEY_RATING_SESSION_COUNT, 0);
        editor.commit();
    }

    private int getSessionCount() {
        return pref.getInt(KEY_RATING_SESSION_COUNT, 0);
    }

    public void startDayCountForRating() {
        if (getIsDaysCountStartedForRating())
            return;
        setDaysCountStartedForRating();
    }

    private int getDaysDiffFromStarted() {
        int difference = Math.abs((int) ((System.currentTimeMillis() / (24 * 60 * 60 * 1000))
                - (int) (getStartTimeForRating() / (24 * 60 * 60 * 1000))));
        Log.d("dj", "getDaysDiffFromStarted- DjphyPreferenceManager: " + difference);
        return difference;
    }

    private boolean getIsDaysCountStartedForRating() {
        return pref.getBoolean(KEY_RATING_IS_DAYS_COUNT_STARTED, false);
    }

    private void setDaysCountStartedForRating() {
        editor.putBoolean(KEY_RATING_IS_DAYS_COUNT_STARTED, true);
        editor.putLong(KEY_RATING_START_TIME, System.currentTimeMillis());
        editor.commit();
    }


    private long getStartTimeForRating() {
        return pref.getLong(KEY_RATING_START_TIME, System.currentTimeMillis());
    }


    public void addNotification(String notification) {
        // get old notifications
        Log.d(TAG, "add notification: ");
        String oldNotifications = getNotifications();

        if (oldNotifications != null) {
            oldNotifications += "|" + notification;
        } else {
            oldNotifications = notification;
        }

        editor.putString(KEY_NOTIFICATIONS_FCM, oldNotifications);
        editor.commit();
    }

    public String getNotifications() {
        Log.d(TAG, "get notification: " + pref.getString(KEY_NOTIFICATIONS_FCM, null));
        return pref.getString(KEY_NOTIFICATIONS_FCM, null);
    }

    public void clearNotificationMsgs() {
        if (pref.contains(KEY_NOTIFICATIONS_FCM)) {
            editor.remove(KEY_NOTIFICATIONS_FCM);
            editor.commit();
        }
    }
}
