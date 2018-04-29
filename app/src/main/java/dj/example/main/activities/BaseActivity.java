package dj.example.main.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.util.AQUtility;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import dj.example.main.MyApplication;
import dj.example.main.R;
import dj.example.main.model.NavigationDataObject;
import dj.example.main.uiutils.ColoredSnackbar;
import dj.example.main.uiutils.DisplayProperties;
import dj.example.main.uiutils.ViewConstructor;
import dj.example.main.uiutils.WindowUtils;
import dj.example.main.utils.IDUtils;
import dj.example.main.utils.URLHelper;

/**
 * Created by DJphy on 25-01-2017.
 */

public abstract class BaseActivity extends AppCoreActivity {

    private String TAG = "BaseActivity";

    protected DisplayProperties displayProperties;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int orient = this.getResources().getConfiguration().orientation;
        displayProperties = DisplayProperties.getInstance(orient);
    }

    public void setErrMsgWithOk(String msg, String btnTxt){
        final Snackbar snackbar = Snackbar.make(getViewForLayoutAccess(), msg, Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(btnTxt, new View.OnClickListener() {
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        ColoredSnackbar.info(snackbar).show();
    }

    public void setErrMsg(String msg){
        ColoredSnackbar.alert(Snackbar.make(getViewForLayoutAccess(), msg, Snackbar.LENGTH_SHORT)).show();
    }

    public void setErrMsgIndefinite(String msg){
        ColoredSnackbar.alert(Snackbar.make(getViewForLayoutAccess(), msg, Snackbar.LENGTH_INDEFINITE)).show();
    }

    public void setErrMsg(String msg, boolean lengthLong) {
        int time = Snackbar.LENGTH_SHORT;
        if (lengthLong)
            time = Snackbar.LENGTH_LONG;
        setErrMsg(msg);
    }

    public void setErrMsg(Object json) {
        String msg = "UNKNOWN_ERR";
        try {
            msg = new JSONObject(json.toString()).getString("message");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setErrMsg(msg);
    }

    public void setWarningMsg(String msg){
        ColoredSnackbar.warning(Snackbar.make(getViewForLayoutAccess(), msg, Snackbar.LENGTH_SHORT)).show();
    }

    public void setInfoMsg(String msg){
        ColoredSnackbar.info(Snackbar.make(getViewForLayoutAccess(), msg, Snackbar.LENGTH_SHORT)).show();
    }

    public void setInfoMsg(String msg, int length){
        ColoredSnackbar.info(Snackbar.make(getViewForLayoutAccess(), msg, length)).show();
    }

    public void setInfoMsgIndefinite(String msg){
        ColoredSnackbar.info(Snackbar.make(getViewForLayoutAccess(), msg, Snackbar.LENGTH_INDEFINITE)).show();
    }

    public void showDialogInfo(String msg, boolean isPositive) {
        int color;
        color = isPositive ? R.color.colorPrimary : R.color.redStatus;
        WindowUtils.getInstance().genericInfoMsgWithOK(this, null, msg, color);
    }

    protected final int HOME_API_CALL = IDUtils.generateViewId();

    public void queryForHomeMojis(){
        AjaxCallback ajaxCallback = getAjaxCallback(HOME_API_CALL);
        ajaxCallback.method(AQuery.METHOD_GET);
        String url = URLHelper.getInstance().getHomAPI();
        Log.d(TAG, "GET url- queryForHomeMojis()" + TAG + ": " + url);
        getAQuery().ajax(url, String.class, ajaxCallback);
    }


    public final int SOCIAL_LOGIN_CALL = IDUtils.generateViewId();
    public void queryForSocialLogin(JSONObject inputParams){
        startProgress();
        AjaxCallback ajaxCallback = getAjaxCallback(SOCIAL_LOGIN_CALL);
        ajaxCallback.method(AQuery.METHOD_POST);
        ajaxCallback.header("content-type", "application/json");
        String url = /*URLHelper.getInstance().getSocialLoginAPI()*/ " "; //// TODO: 08-07-2017  add social login actual API
        Log.d(TAG, "POST url- queryForSocialLogin()" + TAG + ": " + url);
        Map<String,Object> params = null;
        try {
            params = new ObjectMapper().readValue(inputParams.toString(), HashMap.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (params == null){
            ColoredSnackbar.alert(Snackbar.make(getViewForLayoutAccess(), "Sign in Failed", Snackbar.LENGTH_SHORT)).show();
            return;
        }
        Log.d(TAG, "POST reqParams- queryForSocialLogin()" + TAG + ": " + params);
        getAQuery().ajax(url, params, String.class, ajaxCallback);
    }

    public final int NORMAL_LOGIN_CALL = IDUtils.generateViewId();
    public void queryForLogin(String userId, String password) {
        startProgress();
        AjaxCallback ajaxCallback = getAjaxCallback(NORMAL_LOGIN_CALL);
        ajaxCallback.method(AQuery.METHOD_POST);
        String url = /*URLHelper.getInstance().getNormalLoginAPI()*/ " "; // TODO: 08-07-2017 add normal login API
        Log.d(TAG, "POST url- queryForLogin()" + TAG + ": " + url);
        Map<String, String> params = new HashMap<>();
        params.put("email", userId);
        params.put("password", password);
        params.put("role", "intern");
        Log.d(TAG, "POST reqParams- queryForLogin()" + TAG + ": " + params);
        getAQuery().ajax(url, params, String.class, ajaxCallback);
    }

    protected boolean isClearTask;

    public boolean action(NavigationDataObject navigationDataObject) {
        int actionType = navigationDataObject.getTargetType();
        Log.d(TAG, "actionType: "+actionType);
        Intent intent;
        if (actionType == NavigationDataObject.ACTIVITY){
            Class target = navigationDataObject.getTargetClass();
            if (target != null) {
                intent = new Intent(this, target);
                //add flags if any

                startActivity(intent);
            }
        }else if (actionType == NavigationDataObject.WEB_ACTIVITY){

        }else if (actionType == NavigationDataObject.LOGOUT){
            //// TODO: 08-07-2017  perform logout
            Class target = navigationDataObject.getTargetClass();
            if (target != null) {
                intent = new Intent(this, target);
                //intent.putExtra("lycavidurl", url);
                if (isClearTask)
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                else intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                MyApplication.getInstance().getUiHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getSelf().finish();
                    }
                }, 500);
            }
        }else if (actionType == NavigationDataObject.SHARE){
            //// TODO: 08-07-2017  any share actions here 
        }else if (actionType == NavigationDataObject.RATE_US){
            //// TODO: 08-07-2017 rate us on playstore here 
        }
        return true;
    }

    @Override
    public void serverCallEnds(int id, String url, Object json, AjaxStatus status) {
        Log.d(TAG, "url queried-" + TAG + ": " + url);
        Log.d(TAG, "response-" + TAG + ": " + json);

    }


    Dialog alertDialogForgotPassword;

    public void performForgotPassword(){
        alertDialogForgotPassword = null;
        WindowUtils.getInstance().invokeForgotPasswordDialog(this, new ViewConstructor.InfoDisplayListener() {
            @Override
            public void onNegativeSelection(Dialog alertDialog) {

            }

            @Override
            public void onPositiveSelection(Dialog alertDialog) {
                alertDialogForgotPassword = alertDialog;
                String txt = (String) ((AlertDialog) alertDialog)
                        .getButton(AlertDialog.BUTTON_POSITIVE).getTag();
                //queryForRenewSubs(txt);
                //performChangeFromLogOffToCurrent(StaticTopBarFragment.MENU_SEARCH);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(isTaskRoot()){
            //clean the file cache with advance option
            //long triggerSize = 3000000; //starts cleaning when cache size is larger than 3M
            //long targetSize = 2000000;  //remove the least recently used files until cache size is less than 2M
            AQUtility.cleanCacheAsync(this/*, triggerSize, targetSize*/);
        }
    }
}
