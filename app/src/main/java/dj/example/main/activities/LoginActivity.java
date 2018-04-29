package dj.example.main.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.androidquery.callback.AjaxStatus;

import butterknife.BindView;
import butterknife.ButterKnife;
import dj.example.main.MyApplication;
import dj.example.main.R;
import dj.example.main.fragments.SocialLoginFragment;
import dj.example.main.utils.NetworkResultValidator;
import dj.example.main.modules.sociallogins.SocialLoginUtil;

/**
 * Created by DJphy on 25-01-2017.
 */

public class LoginActivity extends BaseActivity{


    private SocialLoginUtil mSocialLoginInstance;

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
        return progressBar;
    }

    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.llSocialLoginContainer)
    LinearLayout llSocialLoginContainer;

    private SocialLoginFragment loginFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        mSocialLoginInstance = SocialLoginUtil.getInstance();

        Runnable runnable = new Runnable() {
            public void run() {
                getSupportFragmentManager().beginTransaction().replace(llSocialLoginContainer.getId(),
                        loginFragment = new SocialLoginFragment()).commit();
            }
        };
        MyApplication.getInstance().getUiHandler().postDelayed(runnable, 200);
    }

    public SocialLoginUtil getmSocialLoginInstance() {
        return mSocialLoginInstance;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mSocialLoginInstance.handleActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mSocialLoginInstance.onActivityStart(this);
    }

    @Override
    public void serverCallEnds(int id, String url, Object json, AjaxStatus status) {
        super.serverCallEnds(id, url, json, status);
        if (id == SOCIAL_LOGIN_CALL){
            try {
                boolean success = NetworkResultValidator.getInstance().isResultOK((String) json, status, progressBar);
                if (success) {
                    /*LoginResponse response = new Gson().fromJson((String) json, LoginResponse.class);
                    response.onParse();*/
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            //ColoredSnackbar.alert(Snackbar.make(progressBar, "UNKNOWN_ERR", Snackbar.LENGTH_SHORT)).show();
        }
    }

    @Override
    protected void onGarbageCollection() {
        mSocialLoginInstance = null;
        loginFragment = null;
    }
}
