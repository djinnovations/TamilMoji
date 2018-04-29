package dj.example.main.modules.sociallogins;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;

import org.json.JSONObject;

import java.util.Arrays;

import dj.example.main.R;
import dj.example.main.activities.BaseActivity;
import dj.example.main.MyApplication;
import dj.example.main.model.LoginInputParams;
import dj.example.main.model.UserInfo;
import dj.example.main.uiutils.ResourceReader;
import dj.example.main.utils.MyPrefManager;
import dj.example.main.utils.RandomUtils;
import dj.example.main.utils.UserSession;
import io.fabric.sdk.android.Fabric;

/**
 * Created by COMP on 5/6/2016.
 */
public class SocialLoginUtil implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "SocialLoginUtil";
    private static final String PLATFORM_FACEBOOK = "facebook";
    private static final String PLATFORM_GOOGLE = "google";
    private static SocialLoginUtil ourInstance;
    private static Context mAppContext;
    private String login_mode = "";

    public static SocialLoginUtil getInstance() {
        mAppContext = MyApplication.getInstance();
        if (ourInstance == null) {
            ourInstance = new SocialLoginUtil();
        }
        return ourInstance;
    }

    protected SocialLoginUtil() {
        Log.d(TAG, "Social login new Obj creation");
        initializeFbAndGlTw(mAppContext);
        setFbCallBacks();
    }

    private Dialog dialog;
    /*****************
     * Facebook stuffs
     ***********************/
    protected CallbackManager mFbCallbackManager;
    protected String[] permissionArr = new String[]{"public_profile", "user_location", "user_birthday", "email"};
    /*******************************************************/
    //private UserSession mUserSession;
    /*****************
     * Gmail stuffs
     ***********************/
    private GoogleApiClient mGoogleApiClient;
    private GoogleSignInOptions mGoogleSignInOpt;
    private ConnectionResult mConnectionResult;
    private SignInButton btnGmailLogin;
    private final int GMAIL_RC_SIGN_IN = 1991;
    private boolean isSignedIn = false;
    private boolean mIntentInProgress = false;

    /**************************************************************/
    private TwitterCore twitCore;
    private TwitterAuthClient twitAuthClient;


    private void initializeFbAndGlTw(Context context) {
        TwitterAuthConfig authConfig =
                new TwitterAuthConfig(MyApplication.API_KEY_TW,
                        MyApplication.API_SECRET_TW);

        Fabric.with(context, new Twitter(authConfig));
        twitCore = Twitter.getInstance().core;
        twitAuthClient = new TwitterAuthClient();

        /*************************************Facebook stuffs********************************************/
        FacebookSdk.sdkInitialize(MyApplication.getInstance());
        mFbCallbackManager = CallbackManager.Factory.create();
        /********************************************************************************************/

        /**************************************Gmail stuffs**********************************************/
        //// TODO: 5/6/2016
        mGoogleSignInOpt = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                //.requestScopes(Plus.SCOPE_PLUS_LOGIN, new Scope("email"))
                .requestScopes(new Scope("profile"))
                //.requestScopes(new Scope(Scopes.PLUS_LOGIN))
                .requestIdToken(MyApplication.OAUTH_WEBCLIENT_ID_GL)
                .build();


        // Initializing google plus api client
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addApi(Auth.GOOGLE_SIGN_IN_API, mGoogleSignInOpt)
                .addApi(Plus.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
        MyApplication.getInstance().getUiHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mGoogleApiClient.connect(GoogleApiClient.SIGN_IN_MODE_OPTIONAL);
            }
        }, 200);


        /*mGoogleSignInOpt = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(OAUTH_WEBCLIENT_ID_GL)
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(mAppContext)
                .addApi(Auth.GOOGLE_SIGN_IN_API, mGoogleSignInOpt)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();*/


        /*************************************************************************************************/
    }


    public void onGoogleLogin(Activity mActivity) {
        if (isSignedIn) {
            if (mGoogleApiClient.isConnected()) {
                performGoogleLogout();
            }
        } else {
            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
            mActivity.startActivityForResult(signInIntent, GMAIL_RC_SIGN_IN);
        }
    }


    public void performGoogleLogout() {
        indicateSignedOut();
        Log.d(TAG, "performGoogleLogout() signed in stat" + isSignedIn);
        if (!isSignedIn)
            return;
        /*if (mGoogleApiClient.isConnected()) {*/
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        Log.d(TAG, "performGoogleLogout() success");
                        processRevokeRequest();
                        isSignedIn = false;
                    }
                });
        //}
    }


    public boolean isGoogleConnected() {
        if (!mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }
        return mGoogleApiClient.isConnected();
    }


    private Activity mActivity;

    public void onFacebookLogin(Activity mActivity) {
        if (isFbSignedIn) {
            performFbLogout();
            return;
        }
        LoginManager.getInstance().logInWithReadPermissions(mActivity, Arrays.asList(permissionArr));
    }

    private boolean isFbSignedIn;

    public void performFbLogout() {
        if (!isFbSignedIn)
            return;
        indicateSignedOut();
        LoginManager.getInstance().logOut();
        Log.d(TAG, "performFbLogout() success: ");
        isFbSignedIn = false;
    }

    public void clearFbPermission() {
        try {
            new GraphRequest(
                    AccessToken.getCurrentAccessToken(),
                    "/me/permissions",
                    null,
                    HttpMethod.DELETE,
                    new GraphRequest.Callback() {
                        public void onCompleted(GraphResponse response) {
                /* handle the result */
                            Log.d(TAG, "response: clearFbPermission() " + response.toString());
                        }
                    }
            ).executeAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void setResultListenerFb(final com.facebook.login.LoginResult loginResult) {
        GraphRequest request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.v(TAG, " setResultListenerFb : " + object.toString());
                        try {
                            login_mode = MyPrefManager.MODE_SOCIAL_FB;
                            LoginInputParams params = new LoginInputParams();
                            String name = "N/A";
                            name = object.getString("name");
                            params.setName(name);
                            params.setImage(object.getJSONObject("picture").getJSONObject("data").getString("url"));
                            if (!object.isNull("email"))
                                params.setEmail(object.getString("email"));
                            if (!object.isNull("birthday"))
                                params.setBirthday(object.getString("birthday"));
                            if (!object.isNull("location"))
                                params.setLocation(object.getJSONObject("location").getString("name"));
                            params.setGender(object.getString("gender"));
                            params.setOauth_access_token(loginResult.getAccessToken().getToken());
                            params.setUid(loginResult.getAccessToken().getUserId());
                            params.setProvider("facebook");
                            params.setResource_class("User");
                            /*List<String> list = new ArrayList<String>();
                            list.add("intern");
                            params.setRoles(list);*/
                            checkLoginStatAndProceed(params);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,gender,location,birthday,picture");
        request.setParameters(parameters);
        request.executeAsync();
    }


    Callback<TwitterSession> mCallBackTwit = new Callback<TwitterSession>() {
        @Override
        public void success(Result<TwitterSession> result) {
            //onSuccessfulLogin(new FbGoogleTweetLoginResult(null, null, result, PLATFORM_TWITTER));
        }

        @Override
        public void failure(TwitterException exception) {
            exception.printStackTrace();
            /*if (mActivity instanceof BaseActivity) {
                ((BaseActivity) mActivity).setErrMsg(MyApplication.ERR_MSG_NETWORK);
            }*/
        }
    };


    public void onTwitterLogin(Activity mActivity) {
        twitAuthClient.authorize(mActivity, mCallBackTwit);
    }

    public void performTwitterLogout() {
        twitCore.logOut();
    }

    public void onActivityStart(Activity mActivity) {
        this.mActivity = mActivity;
    }


    private Dialog displayOverlay(String infoMsg, int colorResId) {
        Dialog dialog = new Dialog(mActivity);
        WindowManager.LayoutParams tempParams = new WindowManager.LayoutParams();
        tempParams.copyFrom(dialog.getWindow().getAttributes());

		/*tempParams.width = dialogWidthInPx;
        tempParams.height = dialogHeightInPx;*/
        tempParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        tempParams.height = WindowManager.LayoutParams.MATCH_PARENT;

        tempParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        tempParams.dimAmount = 0.0f;

        View overLay = LayoutInflater.from(mAppContext).inflate(R.layout.dialog_overlay, null);
        TextView tvTemp = (TextView) overLay.findViewById(R.id.tvOverlayInfo);
        if (infoMsg != null) {
            tvTemp.setText(infoMsg);
            tvTemp.setTextColor(ResourceReader.getInstance().getColorFromResource(colorResId));
        } else tvTemp.setVisibility(View.GONE);
        dialog.setContentView(overLay);
        dialog.setCancelable(false);

        dialog.getWindow().setAttributes(tempParams);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        return dialog;
    }


    private void setFbCallBacks() {
        LoginManager.getInstance().registerCallback(mFbCallbackManager,
                new FacebookCallback<com.facebook.login.LoginResult>() {
                    @Override
                    public void onSuccess(com.facebook.login.LoginResult loginResult) {
                        Log.d(TAG, "Login fb success");
                        //// TODO: 5/4/2016
                        setResultListenerFb(loginResult);
                        isFbSignedIn = true;
                        /*authFromServer(new FbGoogleTweetLoginResult(null, loginResult, null, PLATFORM_FACEBOOK),
                                PLATFORM_FACEBOOK);*/
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(mAppContext, "Login Cancelled", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        //probably no network connection at the moment (most of the times)
                        //Toast.makeText(mAppContext, exception.getMessage(), Toast.LENGTH_LONG).show();
                        /*if (mActivity instanceof BaseActivity) {
                            ((BaseActivity) mActivity).setErrMsg(MyApplication.ERR_MSG_NETWORK);
                        }*/
                    }
                });

    }


    private void displayOverlayDialog() {
        dialog = displayOverlay(null, R.color.colorAccent);
        dialog.show();
    }


    public void indicateSignedOut() {
        //Application.getInstance().getPrefManager().setSocialLoginStat(false);
    }


    private void dismissOverlayView() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        });
    }


    private void processRevokeRequest() {
        /*if (mGoogleApiClient.isConnected()) {*/
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient)
                .setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status arg0) {
                        //Log.d(TAG, "on revoke");
                        //Toast.makeText(getBaseContext(), "Sign-out, successful", Toast.LENGTH_SHORT).show();
                        mGoogleApiClient.connect();
                    }

                });
        // }

    }


    public void handleActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "handleActivityResult resultCode: " + resultCode);
        if (requestCode == GMAIL_RC_SIGN_IN && resultCode == Activity.RESULT_OK) {
            Log.d(TAG, "handleActivityResult GoogleSignIn - Result_Ok");
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
        mFbCallbackManager.onActivityResult(requestCode, resultCode, data);
        twitAuthClient.onActivityResult(requestCode, resultCode, data);
    }


    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            // Signed in successfully.
            isSignedIn = true;
            //authFromServer(new FbGoogleTweetLoginResult(result, null, null, PLATFORM_GOOGLE), PLATFORM_GOOGLE);
            GoogleSignInAccount accountInfo = result.getSignInAccount();
            fillGoogleLoginInputs(accountInfo);
        } else {
            // Signed out.
            isSignedIn = false;
        }
    }


    public String getLogin_mode() {
        return login_mode;
    }

    private void fillGoogleLoginInputs(GoogleSignInAccount signInAccount) {
        Log.v(TAG, " fillGoogleLoginInputs : " + signInAccount.toString());
        try {
            login_mode = MyPrefManager.MODE_SOCIAL_GL;
            LoginInputParams params = new LoginInputParams();
            String name = "N/A";
            name = signInAccount.getDisplayName();
            params.setName(name);
            Uri uri = signInAccount.getPhotoUrl();
            if (uri != null)
                params.setImage(uri.toString());
            params.setEmail(signInAccount.getEmail());
            //params.setGender();
            params.setOauth_access_token(signInAccount.getIdToken());
            params.setUid(signInAccount.getId());
            params.setProvider("google");
            params.setResource_class("User");
            /*List<String> list = new ArrayList<>();
            list.add("intern");
            params.setRoles(list);*/
            UserInfo userInfo = new UserInfo();
            userInfo.emailId = params.getEmail();
            userInfo.name = params.getName();
            userInfo.profileImgUrl = params.getImage();
            userInfo.uniqueId = params.getUid();
            UserSession.getInstance().setUserInfo(userInfo);

            checkLoginStatAndProceed(params);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void checkLoginStatAndProceed(LoginInputParams params){
        try {
            UserInfo userInfo = new UserInfo();
            userInfo.emailId = params.getEmail();
            userInfo.name = params.getName();
            userInfo.profileImgUrl = params.getImage();
            userInfo.uniqueId = params.getUid();
            userInfo.gender = params.getGender();
            userInfo.location = params.getLocation();
            userInfo.birthday = params.getBirthday();
            UserSession.getInstance().setUserInfo(userInfo);

            if (MyPrefManager.getInstance().getLoginStatus()){
                RandomUtils.getInstance().launchHome(mActivity, true);
                return;
            }

            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            try {
                JSONObject jsonObject = new JSONObject(ow.writeValueAsString(params));
                if (mActivity instanceof BaseActivity) {
                    ((BaseActivity) mActivity).queryForSocialLogin(jsonObject);
                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            /*
Authorizing...please wait*/
            Toast.makeText(mAppContext, "Hi "
                    + params.getName() + ", Welcome to App", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.d(TAG, "on connection failed");
    }


    @Override
    public void onConnected(Bundle arg0) {
        Log.d(TAG, "on connected");
    }


    @Override
    public void onConnectionSuspended(int arg0) {

        Log.d(TAG, "on connection suspended");
        mGoogleApiClient.connect();
    }
}
