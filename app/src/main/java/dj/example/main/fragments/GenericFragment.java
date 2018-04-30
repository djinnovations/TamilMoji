package dj.example.main.fragments;

import android.Manifest;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import dj.example.main.MyApplication;
import dj.example.main.activities.BaseActivity;
import dj.example.main.activities.HomeActivity;
import dj.example.main.activities.SharingActivity;
import dj.example.main.adapters.ThumbnailAdapter;
import dj.example.main.model.HeaderThumbnailData;
import dj.example.main.uiutils.MyLinearLayoutManager;

/**
 * Created by CSC on 4/30/2018.
 */

public class GenericFragment extends SingleMenuFragment{

    ThumbnailAdapter adapter;

    @Override
    protected void onGarbageCollection() {
        adapter = null;
    }

    @Override
    public boolean isAddSnapper() {
        return true;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(getActivity() instanceof SharingActivity){
            changeData(((SharingActivity) getActivity()).getImageApps());
        }
        //int start = RandomUtils.convertDpToPixel();
        llBody.setPadding(0, 0, 0 , 0);
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        return new MyLinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        adapter = new ThumbnailAdapter();
        return adapter;
    }

    @Override
    public void changeData(List dataList) {
        if (adapter != null){
            try {
                adapter.changeData(dataList);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Subscribe
    public void onEventReceived(final Object event) {
        if (event instanceof ResolveInfo) {
            PermissionListener permissionlistener = new PermissionListener() {
                @Override
                public void onPermissionGranted() {
                    //Toast.makeText(NormalActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
                    ResolveInfo data = (ResolveInfo) event;
                    //((BaseActivity) getActivity()).setInfoMsg(data.getTitle());
                    if (getActivity() instanceof SharingActivity){
                        ((SharingActivity) getActivity()).shareNow(data);
                    }
                }

                @Override
                public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                    Toast.makeText(MyApplication.getInstance(), "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT)
                            .show();
                }


            };
            TedPermission.with(getActivity())
                    .setPermissionListener(permissionlistener)
                    //.setRationaleTitle("ratinal title")
                    //.setRationaleMessage("rational msg")
                    .setDeniedTitle("Permission denied")
                    .setDeniedMessage(
                            "If you reject permission, you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                    .setGotoSettingButtonText("Settings")
                    .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE/*, Manifest.permission.ACCESS_FINE_LOCATION*/)
                    .check();
        } else if (event instanceof HeaderThumbnailData.ThumbnailData) {
            HeaderThumbnailData.ThumbnailData data = (HeaderThumbnailData.ThumbnailData) event;
            //((BaseActivity) getActivity()).setInfoMsg(data.getTitle());
            if (getActivity() instanceof BaseActivity){
                ((BaseActivity) getActivity()).launchSharing(data.getThumbnailUrl());
            }
        }
    }
}
