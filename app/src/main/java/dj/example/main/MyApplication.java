package dj.example.main;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import dj.example.main.activities.ExploreActivity;
import dj.example.main.activities.HomeActivity;
import dj.example.main.activities.LoginActivity;
import dj.example.main.activities.MainActivity;
import dj.example.main.activities.WebActivity;
import dj.example.main.fragments.HomeTabFragment;
import dj.example.main.model.NavigationDataObject;
import dj.example.main.model.UserInfo;
import dj.example.main.uiutils.DisplayProperties;
import dj.example.main.uiutils.UiRandomUtils;
import dj.example.main.uiutils.WindowUtils;
import dj.example.main.utils.MyPrefManager;
import dj.example.main.utils.RandomUtils;

/**
 * Created by DJphy on 28-09-2016.
 */
public class MyApplication extends Application {

    public static final String API_KEY_TW = "";
    public static final String API_SECRET_TW = "";
    public static final String OAUTH_WEBCLIENT_ID_GL = "1098955210882-p9dhi34jaj4bjgd2fpsf9d2lf5rf7qd2.apps.googleusercontent.com";
    public static final String ERR_MSG_NETWORK = "network ERR";


    private static MyApplication ourInstance;
    private String TAG = "MyApplication";

    public static MyApplication getInstance(){
        return ourInstance;
    }

    public interface MenuSelectionListener {
        void onMenuSelected(Object data);
    }

    private Handler uiHandler;
    public Handler getUiHandler() {
        return uiHandler;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ourInstance = this;
        uiHandler = new Handler();
        setMenuItems();
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            private int num = 0;
            private int num1 = 0;
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                num1++;
            }

            @Override
            public void onActivityStarted(Activity activity) {
                if (num == 0){
                    Log.d(TAG, "app foreground");
                }
                num++;
            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {
                num--;
                if (num == 0){
                    Log.d(TAG, "app background");
                    //MixPanelHelper.logEventMixPanel(AppAnalyticsEvents.EXIT_APP, null);
                    //FirebaseAnalyticsHelper.logEventFirebase(AppAnalyticsEvents.EXIT_APP, null);
                    //MixPanelHelper.flushDataToMixPanel();
                }
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                num1--;
                if (num1 == 0){
                    Log.d(TAG, "app closed");
                    onAppExit();
                }
            }
        });
    }

    public void onAppExit(){
        //clearOwnedPackages();
        clearUserInfo();
        //clearFavList();
        //MyApplication.getInstance().clearConfiguration();
        MyPrefManager.clearInstance();
        RandomUtils.clearInstance();
        UiRandomUtils.clearInstance();
        WindowUtils.clearInstance();
        DisplayProperties.clearInstance();
    }

    private Map<Integer, NavigationDataObject> menuItems = new HashMap<>();
    public NavigationDataObject getNavigationObj(Integer viewId){
        return menuItems.get(viewId);
    }
    private void setMenuItems(){
        menuItems.clear();
        menuItems.put(R.id.nav_menu_home, new NavigationDataObject(R.id.nav_menu_home, HomeActivity.class, NavigationDataObject.ACTIVITY));
        menuItems.put(R.id.nav_menu_explore, new NavigationDataObject(R.id.nav_menu_explore, ExploreActivity.class, NavigationDataObject.ACTIVITY));
        menuItems.put(R.id.nav_menu_settings, new NavigationDataObject(R.id.nav_menu_settings, null, NavigationDataObject.ACTIVITY));
        menuItems.put(R.id.nav_menu_contact_us, new NavigationDataObject(R.id.nav_menu_contact_us, null, NavigationDataObject.ACTIVITY));
        menuItems.put(R.id.nav_menu_profile, new NavigationDataObject(R.id.nav_menu_profile,
                null, NavigationDataObject.ACTIVITY));
        menuItems.put(R.id.nav_menu_about, new NavigationDataObject(R.id.nav_menu_about, null, "About Us",
                NavigationDataObject./*WEB_ACTIVITY*/ACTIVITY, "urlhere"));
        menuItems.put(R.id.nav_menu_logout, new NavigationDataObject(R.id.nav_menu_logout, LoginActivity.class, NavigationDataObject.LOGOUT));

    }


    private String fcmToken;

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    private UserInfo userInfo;

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void clearUserInfo(){
        userInfo = null;
    }

    public interface IFragmentWatcher {
        void onDestroy(Fragment var1);

        void onCreate(Fragment var1);
    }

    public interface IFragmentData {
        Class getView();

        String getID();

        String getName();

        Object getParam();

        void setParam(Object var1);

        Object getActionValue();

        String getActionType();
    }

}
