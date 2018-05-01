package dj.example.main.modules.stickers;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;
import android.util.Log;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.firebase.appindexing.FirebaseAppIndex;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;
import java.util.List;

import dj.example.main.MyApplication;
import dj.example.main.adapters.ThumbnailAdapter;
import dj.example.main.model.HeaderThumbnailData;
import dj.example.main.model.response.HomeDataResponse;
import dj.example.main.utils.MyPrefManager;
import dj.example.main.utils.URLHelper;

public class AppIndexingUpdateService extends JobIntentService {
    // Job-ID must be unique across your whole app.
    private static final int UNIQUE_JOB_ID = 42;
    private static String TAG = "IndexUpdateService";

    public static void enqueueWork(final Context context) {
        if (MyPrefManager.getInstance().iSStickersAdded())
            return;
            AjaxCallback ajaxCallback = new AjaxCallback(){
                @Override
                public void callback(String url, Object object, AjaxStatus status) {
                    if (object == null)
                        return;
                    Log.d(TAG, "url queried-" + url);
                    Log.d(TAG, "response-" + object);
                    try {
                        HomeDataResponse response = new Gson().fromJson((String) object, HomeDataResponse.class);
                        List<HomeDataResponse.HomeResponse.HomeData> data = response.getResponse().getData();
                        List<HomeDataResponse.HomeResponse.HomeData.Emojis> emojisList = new ArrayList<>();
                        for (HomeDataResponse.HomeResponse.HomeData homeData: data){
                            emojisList.addAll(homeData.emojis);
                        }
                        MyApplication.getInstance().setPrimaryEmojis(emojisList);
                        enqueueWork(context, AppIndexingUpdateService.class, UNIQUE_JOB_ID, new Intent());
                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                    }
                }
            };
            ajaxCallback.method(AQuery.METHOD_GET);
            String url = URLHelper.getInstance().getHomeAPI();
            Log.d(TAG, "GET url- enqueueWork()" + TAG + ": " + url);
            new AQuery(context).ajax(url, String.class, ajaxCallback);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        AppIndexingUtil.setStickers(getApplicationContext(), FirebaseAppIndex.getInstance());
    }
}