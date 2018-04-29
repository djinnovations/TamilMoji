package dj.example.main.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.OnClick;
import dj.example.main.R;
import dj.example.main.activities.BaseActivity;
import dj.example.main.MyApplication;
import dj.example.main.uiutils.ColoredSnackbar;

/**
 * Created by DJphy on 09-01-2017.
 */

public class LoginUserPasswordFragment extends PrimaryBaseFragment {

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnLoginAcct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (canContinue()) {
                    String shaUserId = /*RandomUtils.textToSHA256(*/etUserName.getText().toString().trim()/*)*/;
                    String shaPass = /*RandomUtils.textToSHA256(*/etPassword.getText().toString().trim()/*)*/;
                    //((BaseActivity) getActivity()).queryForLogin(shaUserId, shaPass);
                    return;
                }
                ColoredSnackbar.alert(Snackbar.make(btnLoginAcct, "Fill all fields", Snackbar.LENGTH_SHORT)).show();
            }
        });

        Runnable runnable = new Runnable() {
            public void run() {
                etUserName.requestFocus();
            }
        };
        MyApplication.getInstance().getUiHandler().postDelayed(runnable, 300);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_login_normal;
    }

    @Override
    protected void onGarbageCollection() {

    }

    @Override
    protected String getFragmentTitle() {
        return "";
    }

    @BindView(R.id.etUserName)
    EditText etUserName;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.btnLoginAcct)
    Button btnLoginAcct;

    @OnClick(R.id.forgotPasswordButton)
    void onPlayClicked() {
        if (getActivity() instanceof BaseActivity)
            ((BaseActivity) getActivity()).performForgotPassword();
    }


    private boolean canContinue(){
        return !(TextUtils.isEmpty(etPassword.getText().toString()) || TextUtils.isEmpty(etUserName.getText().toString()));
    }
}
