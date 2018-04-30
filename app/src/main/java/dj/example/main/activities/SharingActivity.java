package dj.example.main.activities;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import butterknife.BindView;
import butterknife.ButterKnife;
import dj.example.main.MyApplication;
import dj.example.main.R;
import dj.example.main.fragments.GenericFragment;


/**
 * Created by CSC on 4/30/2018.
 */

public class SharingActivity extends BaseActivity {

    private String TAG = "SharingActivity";

    @Override
    public ProgressBar getProgressBar() {
        return progressBar;
    }

    @Override
    public Activity getSelf() {
        return this;
    }

    @Override
    public View getViewForLayoutAccess() {
        return ivMain;
    }

    @BindView(R.id.ivMain)
    ImageView ivMain;
    @BindView(R.id.bottomCarousel)
    ViewGroup bottomCarousel;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.ivBack)
    ImageView ivBack;

    private String primaryImageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sharing);
        ButterKnife.bind(this);
        getSupportFragmentManager().beginTransaction()
                .replace(bottomCarousel.getId(), new GenericFragment()).commit();
        int dimen = (int) (55 * displayProperties.getXPixelsPerCell());
        ivMain.getLayoutParams().width = dimen;
        ivMain.getLayoutParams().height = dimen;
        Uri uri = getIntent().getData();
        if (uri != null)
            primaryImageUrl = uri.toString();
        Picasso.with(MyApplication.getInstance())
                .load(primaryImageUrl)
                .memoryPolicy(MemoryPolicy.NO_STORE, MemoryPolicy.NO_CACHE)
                .into(ivMain);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public List<ResolveInfo> getImageApps(){
        PackageManager pm = getPackageManager();
        Intent mainIntent = new Intent(Intent.ACTION_SEND, null);
        mainIntent.setType("image/*");
        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(mainIntent, 0); // returns all applications which can listen to the SEND Intent
        List<ResolveInfo> custom = new ArrayList<>();
        custom.addAll(new ArrayList<ResolveInfo>(resolveInfos));
        for (ResolveInfo info : custom) {
            ApplicationInfo applicationInfo = info.activityInfo.applicationInfo;
            if (isSystemPackage(applicationInfo)){
                resolveInfos.remove(info);
            }

            //get package name, icon and label from applicationInfo object and display it in your custom layout

            else Log.d(TAG, "App name: & packageName & activity name"
                    +applicationInfo.loadLabel(pm).toString() +" && "+ applicationInfo.packageName
                    +" && "+ applicationInfo.name);
            //App icon = applicationInfo.loadIcon(pm);
            //App name  = applicationInfo.loadLabel(pm).toString();
            //App package name = applicationInfo.packageName;
        }
        custom.clear();
        Set<String> set = new TreeSet<>();
        for (ResolveInfo resolveInfo: resolveInfos){
            if (set.add(resolveInfo.activityInfo.applicationInfo.packageName)){
                custom.add(resolveInfo);
            }
        }
        return /*resolveInfos*/custom;
    }

    private boolean isSystemPackage(ApplicationInfo pkgInfo) {
        return ((pkgInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) ? true
                : false;
    }

    @Override
    protected void onGarbageCollection() {

    }

    public void shareNow(final ResolveInfo data) {
        Picasso.with(MyApplication.getInstance())
                .load(primaryImageUrl)
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        // loaded bitmap is here (bitmap)
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.putExtra(Intent.EXTRA_TEXT, "Tamil Moji sharing");
                        String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "", null);
                        Uri screenshotUri = Uri.parse(path);
                        intent.setComponent(new ComponentName(
                                        data.activityInfo.packageName,
                                        data.activityInfo.name));
                        intent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
                        intent.setType("image/*");
                        startActivity(intent);
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });

    }
}
