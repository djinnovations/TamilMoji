package dj.example.main.activities;

import android.util.Log;

import com.androidquery.callback.AjaxStatus;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import dj.example.main.adapters.ThumbnailAdapter;
import dj.example.main.fragments.ExploreFragment;
import dj.example.main.model.HeaderThumbnailData;
import dj.example.main.model.response.HomeDataResponse;
import dj.example.main.model.response.SearchDataResponse;
import dj.example.main.utils.NetworkResultValidator;

/**
 * Created by CSC on 4/30/2018.
 */

public class ExploreActivity extends MainActivity {

    private String TAG = "ExploreActivity";

    ExploreFragment exploreFragment;

    @Override
    protected void onCreateDelegate() {
        //super.onCreateDelegate();
        setTitle("Explore");
        getSupportFragmentManager().beginTransaction().replace(flHolder.getId(),exploreFragment = new ExploreFragment()).commit();
    }

    public void performSearch(String txt){
        startProgress();
        queryForSearch(txt);
    }


    @Override
    public void serverCallEnds(int id, String url, Object json, AjaxStatus status) {
        Log.d(TAG, "url queried-" + url);
        Log.d(TAG, "response-" + json);
        stopProgress();
        if (id == SEARCH_API_CALL) {
            final boolean success = NetworkResultValidator.getInstance().isResultOK((String) json, status,
                    getViewForLayoutAccess());
            if (success){
                SearchDataResponse response = new Gson().fromJson((String) json, SearchDataResponse.class);
                List<HeaderThumbnailData.ThumbnailData> thumbnailDataList = new ArrayList<>();
                if (response.getResponse().getData() != null && response.getResponse().getData().size()<=0){
                    setInfoMsg("No emojis found for the keyword");
                }
                for (SearchDataResponse.SearchResponse.SearchData emojis: response.getResponse().getData()){
                    HeaderThumbnailData.ThumbnailData thumbnailData
                            = new HeaderThumbnailData.ThumbnailData(ThumbnailAdapter.SQUARE, emojis._id,
                            emojis.img, "");
                    thumbnailDataList.add(thumbnailData);
                }
                exploreFragment.changeData(thumbnailDataList);
            }
        }

        else super.serverCallEnds(id, url, json, status);
    }
}
