package dj.example.main.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import dj.example.main.MyApplication;
import dj.example.main.R;
import dj.example.main.fragments.LoginUserPasswordFragment;

/**
 * Created by User on 26-01-2017.
 */

public class NormalLoginActivity extends BaseActivity {

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
    @BindView(R.id.llContainer)
    LinearLayout llLoginContainer;

    private LoginUserPasswordFragment loginFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_normal);
        ButterKnife.bind(this);

        Runnable runnable = new Runnable() {
            public void run() {
                getSupportFragmentManager().beginTransaction().replace(llLoginContainer.getId(),
                        loginFragment = new LoginUserPasswordFragment()).commit();
            }
        };
        MyApplication.getInstance().getUiHandler().postDelayed(runnable, 200);
    }

    @Override
    protected void onGarbageCollection() {

    }
}
