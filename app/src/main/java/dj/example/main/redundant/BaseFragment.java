package dj.example.main.redundant;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.gun0912.tedpermission.PermissionListener;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import dj.example.main.R;
import dj.example.main.MyApplication;
import dj.example.main.customviews.DefaultWebView;

public abstract class BaseFragment extends Fragment{
    private static final String DATA = "data";
    private static final String LOG_TAG = "BaseFragment";
    protected MyApplication.IFragmentData fragmentData;
    public BaseFragment() {
    }

    public static AlertDialog openInDialog(FragmentActivity activity, BaseFragment fragment, boolean cancelable) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View view = inflater.inflate(R.layout.bottom_sheet_dialog_fragment_holder, null);
        AlertDialog.Builder b = (new AlertDialog.Builder(activity)).setView(view).setCancelable(cancelable);
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.bottom_sheet_fragment_container, fragment);
        return b.show();
    }

    public static BaseFragment newInstance(MyApplication.IFragmentData object) {
        try {
            if(object.getView() != null) {
                Constructor e = object.getView().getConstructor(new Class[0]);
                BaseFragment baseFragment = (BaseFragment)e.newInstance(new Object[0]);
                if(baseFragment != null) {
                    baseFragment.setFragmentData(object);
                    return baseFragment;
                }
            }
        } catch (Exception var3) {
            System.out.println(var3);
        }

        Log.e("BaseFragment", "BaseFragment creation fail. ID:" + object.getID() + "  Name:" + object.getName());
        return null;
    }

    public static BaseFragment newWebViewInstance(MyApplication.IFragmentData object) {
        Object fragment = null;
        Constructor constructor = null;

        try {
            if(object.getView() != null) {
                constructor = object.getView().getConstructor(new Class[0]);
                BaseFragment e = (BaseFragment)constructor.newInstance(new Object[0]);
                if(e != null) {
                    fragment = e;
                    e.setFragmentData(object);
                }
            } else {
                DefaultWebView e1 = new DefaultWebView();
                if(e1 != null) {
                    fragment = e1;
                    e1.setFragmentData(object);
                }
            }
        } catch (Exception var4) {
            System.out.println(var4);
        }

        if(fragment == null) {
            Log.e("BaseFragment", "BaseFragment creation fail. ID:" + object.getID() + "  Name:" + object.getName());
        }

        return (BaseFragment)fragment;
    }

    public abstract View onCreateView(LayoutInflater var1, ViewGroup var2, Bundle var3);

    protected abstract void garbageCollectorCall();

    public void onDestroyItem() {
    }

    public BaseApplication getApplication() {
        return this.getActivity().getApplication() instanceof BaseApplication?(BaseApplication)this.getActivity().getApplication():null;
    }

    public boolean allowedBack() {
        return true;
    }

    public void onStart() {
        super.onStart();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setHasOptionsMenu(this.getHasOptionsMenu());
        if(this.getActivity() != null && this.getActivity() instanceof MyApplication.IFragmentWatcher) {
            ((MyApplication.IFragmentWatcher)this.getActivity()).onCreate(this);
        }

        if(this.getApplication() != null && this.getApplication() instanceof BaseApplication) {
            this.getApplication().onCreate(this);
        }

        if(this.getAutoHandlePermissions()) {
            this.handlePermissions();
        }

    }

    protected boolean getAutoHandlePermissions() {
        return true;
    }

    protected String[] requirePermissions() {
        return null;
    }

    protected String[] getFriendlyPermissionsMeaning() {
        return null;
    }

    protected void handlePermissions() {
        PermissionsHelper permissionsHelper = this.createPermissionsHelper();
        permissionsHelper.check(this.requirePermissions(), this.getFriendlyPermissionsMeaning());
    }

    PermissionListener onPermission = new PermissionListener() {
        public void onPermissionGranted() {
            BaseFragment.this.permissionGranted();
        }

        public void onPermissionDenied(ArrayList<String> arrayList) {
            BaseFragment.this.permissionDenied(arrayList);
        }
    };

    protected PermissionsHelper createPermissionsHelper() {
        PermissionsHelper permissionsHelper = new PermissionsHelper(this.getContext());
        permissionsHelper.setShowRationaleConfirm(this.getShowRationaleConfirm());
        permissionsHelper.setOnPermission(this.onPermission);
        permissionsHelper.setRationaleConfirmText(this.getRationaleConfirmText());
        permissionsHelper.setRationaleMessage(this.getRationaleMessage());
        permissionsHelper.setShowDeniedMessage(this.getShowDeniedMessage());
        permissionsHelper.setDeniedCloseButtonText(this.getDeniedCloseButtonText());
        permissionsHelper.setDeniedMessage(this.getDeniedMessage());
        return permissionsHelper;
    }

    protected boolean getShowDeniedMessage() {
        return true;
    }

    protected boolean getShowRationaleConfirm() {
        return true;
    }

    protected void permissionGranted() {
    }

    protected void permissionDenied(ArrayList<String> arrayList) {
    }

    protected String getDeniedCloseButtonText() {
        return "Close";
    }

    protected String getRationaleConfirmText() {
        return "Confirm";
    }

    protected String getRationaleMessage() {
        return "Rational";
    }

    protected String getDeniedMessage() {
        return "Denied";
    }

    protected boolean getHasOptionsMenu() {
        return false;
    }

    public void onDestroy() {
        super.onDestroy();
        this.garbageCollectorCall();
        if(this.getActivity() != null && this.getActivity() instanceof MyApplication.IFragmentWatcher) {
            ((MyApplication.IFragmentWatcher)this.getActivity()).onDestroy(this);
        }

        if(this.getApplication() != null && this.getApplication() instanceof BaseApplication) {
            this.getApplication().onDestroy(this);
        }

    }

    public void onDestroyView() {
        super.onDestroyView();
        this.garbageCollectorCall();
        this.fragmentData = null;
    }

    public void configFragmentData(MyApplication.IFragmentData data) {
        if(data.getParam() != null && data.getParam() instanceof Map) {
            Map args2 = (Map)data.getParam();
            Bundle args1 = new Bundle();
            Iterator i$ = args2.entrySet().iterator();

            while(i$.hasNext()) {
                Map.Entry entry = (Map.Entry)i$.next();
                args1.putString((String)entry.getKey(), (String)entry.getValue());
            }

            this.setArguments(args1);
        } else {
            Bundle args;
            if(data.getParam() != null && data.getParam() instanceof String) {
                args = new Bundle();
                args.putString("data", (String)data.getParam());
                this.setArguments(args);
            } else if(data.getParam() != null) {
                args = new Bundle();
                args.putString("data", (new Gson()).toJson(data.getParam()));
                this.setArguments(args);
            }
        }

    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    public MyApplication.IFragmentData getFragmentData() {
        return this.fragmentData;
    }

    public void setFragmentData(MyApplication.IFragmentData object) {
        this.fragmentData = object;
        this.configFragmentData(this.fragmentData);
    }

    public Object getPramas() {
        return this.fragmentData != null?this.fragmentData.getParam():null;
    }

    public void setPramas(Object object) {
        if(this.fragmentData != null) {
            this.fragmentData.setParam(object);
        }

    }
}
