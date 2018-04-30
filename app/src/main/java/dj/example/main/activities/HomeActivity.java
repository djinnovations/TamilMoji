package dj.example.main.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.androidquery.callback.AjaxStatus;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dj.example.main.R;
import dj.example.main.adapters.ThumbnailAdapter;
import dj.example.main.fragments.MainFragment;
import dj.example.main.model.HeaderThumbnailData;
import dj.example.main.model.response.HomeDataResponse;
import dj.example.main.utils.NetworkResultValidator;

/**
 * Created by CSC on 4/29/2018.
 */

public class HomeActivity extends BaseDrawerActivity {

    private String TAG = "HomeActivity";

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
        return mainHolder;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        promptBeforeExit();
        //super.onBackPressed();
    }

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.mainHolder)
    ViewGroup mainHolder;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    private MainFragment mainFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        setTitle("TAMILMOJI");
        getSupportFragmentManager().beginTransaction().replace(mainHolder.getId(), mainFragment = new MainFragment()).commit();
    }

    public void initiateAPIcall(){
        queryForHomeMojis();
    }

    @Override
    public void serverCallEnds(int id, String url, final Object json, AjaxStatus status) {
        Log.d(TAG, "url queried-" + url);
        Log.d(TAG, "response-" + json);
        stopProgress();
        if (id == HOME_API_CALL){
            final boolean success = NetworkResultValidator.getInstance().isResultOK((String) json, status,
                    getViewForLayoutAccess());
            Runnable runnable = new Runnable() {
                public void run() {
                    if (success) {
                        if (mainFragment != null){
                            if (mainFragment.isAdded()){
                                HomeDataResponse response = new Gson().fromJson((String) json, HomeDataResponse.class);
                                List<HomeDataResponse.HomeResponse.HomeData> data = response.getResponse().getData();
                                List<HeaderThumbnailData> dataList = new ArrayList<>();
                                for (HomeDataResponse.HomeResponse.HomeData homeData: data){
                                    List<HeaderThumbnailData.ThumbnailData> thumbnailDataList = new ArrayList<>();
                                    for (HomeDataResponse.HomeResponse.HomeData.Emojis emojis: homeData.emojis){
                                        HeaderThumbnailData.ThumbnailData thumbnailData
                                                = new HeaderThumbnailData.ThumbnailData(ThumbnailAdapter.SQUARE, emojis._id,
                                                emojis.img, "");
                                        thumbnailDataList.add(thumbnailData);
                                    }
                                    dataList.add(new HeaderThumbnailData(homeData.title, thumbnailDataList));
                                }
                                mainFragment.changeData(dataList);
                            }
                        }
                    }
                }
            };
            new Thread(runnable).start();
        }
        super.serverCallEnds(id, url, json, status);
    }

    @Override
    protected void onGarbageCollection() {

    }
}
