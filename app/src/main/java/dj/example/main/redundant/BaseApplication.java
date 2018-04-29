package dj.example.main.redundant;

import android.app.Application;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import dj.example.main.MyApplication;
import dj.example.main.utils.RandomUtils;

public abstract class BaseApplication extends Application implements MyApplication.IFragmentWatcher {
    private String uuid;
    private boolean isTablet = false;
    private boolean is7inchTablet = false;
    private boolean is10inchTablet = false;
    private boolean isPhone = true;
    private boolean watchLeak = false;

    public BaseApplication() {
    }

    public void onDestroy(Fragment fragment) {
    }

    public void onCreate(Fragment fragment) {
    }

    public void onLowMemory() {
        super.onLowMemory();
    }

    public boolean isConnected() {
        try {
            return RandomUtils.getInstance().isConnected(this);
        } catch (Exception var2) {
            return false;
        }
    }

    public boolean isWatchLeak() {
        return this.watchLeak;
    }

    public final void onCreate() {
        super.onCreate();
        this.uuid = Settings.Secure.getString(this.getContentResolver(), "android_id");
        if(RandomUtils.getInstance().isTablet(this)) {
            this.isTablet = true;
            this.isPhone = false;
        } else {
            this.isTablet = false;
            this.isPhone = true;
        }

        if(this.isTablet) {
            DisplayMetrics metrics = new DisplayMetrics();
            WindowManager windowManager = (WindowManager)this.getSystemService(WINDOW_SERVICE);
            windowManager.getDefaultDisplay().getMetrics(metrics);
            float widthInInches = (float)metrics.widthPixels / metrics.xdpi;
            float heightInInches = (float)metrics.heightPixels / metrics.ydpi;
            double sizeInInches = Math.sqrt(Math.pow((double)widthInInches, 2.0D) + Math.pow((double)heightInInches, 2.0D));
            this.is7inchTablet = sizeInInches >= 6.5D && sizeInInches <= 8.0D;
            this.is10inchTablet = !this.is7inchTablet;
        } else {
            this.is7inchTablet = false;
            this.is10inchTablet = false;
        }

        this.configApplication();
    }

    public abstract void configApplication();

    public String getAccountPhone() {
        return "";
    }

    public boolean isTablet() {
        return this.isTablet;
    }

    public boolean isIs7inchTablet() {
        return this.is7inchTablet;
    }

    public boolean isIs10inchTablet() {
        return this.is10inchTablet;
    }

    public boolean isPhone() {
        return this.isPhone;
    }

    public String getUUID() {
        return this.uuid;
    }
}
