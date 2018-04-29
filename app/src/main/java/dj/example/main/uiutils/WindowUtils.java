package dj.example.main.uiutils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import dj.example.main.R;
import dj.example.main.MyApplication;


public class WindowUtils {

    private Context appContext;
    ViewConstructor mViewConstructor;
    DisplayProperties mDispProp;
    private static WindowUtils thisInstance;
    //private final String networkInfoMsg = "Please turn on your mobile DATA or WIFI";

    private WindowUtils(Context appContext) {
        this.appContext = appContext;
        mViewConstructor = ViewConstructor.getInstance(appContext);
        mDispProp = DisplayProperties.getInstance(DisplayProperties.ORIENTATION_PORTRAIT);
    }


    public static WindowUtils getInstance() {
        if (thisInstance == null) {
            thisInstance = new WindowUtils(MyApplication.getInstance());
        }
        return thisInstance;
    }

    public static void clearInstance() {
        thisInstance = null;
        ViewConstructor.clearInstance();
    }

    public void genericPermissionInfoDialog(Activity activity, String message) {
        mViewConstructor.displayInfo(activity, "Permission info", message, "OKAY", "",
                false, new ViewConstructor.InfoDisplayListener() {
                    @Override
                    public void onPositiveSelection(Dialog alertDialog) {
                        alertDialog.dismiss();
                    }

                    @Override
                    public void onNegativeSelection(Dialog alertDialog) {

                    }
                });
    }


    public void genericInfoMsgWithOkay(Activity activity, String title, String infoMsg, int colorInfoMsg) {
        mViewConstructor.displayInfo(activity, title, infoMsg, "Okay", colorInfoMsg);
    }

    public void genericInfoMsgWithOK(Activity activity, String title, String infoMsg, int colorInfoMsg) {
        mViewConstructor.displayInfo(activity, title, infoMsg, "OK", colorInfoMsg);
    }

    public void genericInfoMsgWithOKCallBack(Activity activity, String title, String infoMsg, int colorInfoMsg, String posBtnTxt,
                                             final ViewConstructor.InfoDisplayListener mInfoListener) {
        mViewConstructor.displayInfo(activity, title, infoMsg, posBtnTxt, colorInfoMsg, mInfoListener);
    }

    public void genericInfoMsgWithOKCallBack(Activity activity, String title, String infoMsg, int colorInfoMsg,
                                             final ViewConstructor.InfoDisplayListener mInfoListener){
        mViewConstructor.displayInfo(activity, title, infoMsg, "OK", colorInfoMsg, mInfoListener);
    }


    public void genericInfoMsgWithCallBack(Activity activity, String title, String infoMsg, int colorInfoMsg, String posBtnTxt,
                                           final ViewConstructor.InfoDisplayListener mInfoListener) {
        mViewConstructor.displayInfo(activity, title, infoMsg, posBtnTxt, colorInfoMsg, true, mInfoListener);
    }


    public void invokeForgotPasswordDialog(Activity activity, final ViewConstructor.InfoDisplayListener mInfoListener) {
        final android.support.v7.app.AlertDialog dialog = mViewConstructor.displayViewInfo(activity, "Forgot your password",
                R.layout.layout_edittext, "Send", true, mInfoListener);
        final EditText editText = (EditText) dialog.findViewById(R.id.etItem);
        final TextView textView = (TextView) dialog.findViewById(R.id.tvItem);
        editText.setHint("Email ID to receive a password");
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String data = "";
                if (editText != null)
                    data = editText.getText().toString().trim();
                if (isValidEmail(data)) {
                    if (textView != null)
                        textView.setVisibility(View.GONE);
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTag(data);
                    mInfoListener.onPositiveSelection(dialog);
                } else {
                    if (textView != null) {
                        textView.setVisibility(View.VISIBLE);
                        textView.setText("enter a valid email address");
                        textView.setTextColor(ResourceReader.getInstance().getColorFromResource(R.color.redStatus));
                    }
                }
            }
        });
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                mInfoListener.onNegativeSelection(dialog);
            }
        });
    }


    boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


    public static float dialogDimAmount = 0.0f;

    public Dialog displayDialogNoTitle(Context activityContext, View layout/*, String title*/) {

        Dialog dialog = new Dialog(activityContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        /*TextView alertTitle=(TextView) dialog.getWindow().getDecorView().findViewById(android.R.id.title);
        alertTitle.setBackgroundColor(new ResourceReader(activityContext).getColorFromResource(R.color.colorBlackDimText));
        alertTitle.setTextColor(Color.WHITE);
        alertTitle.setGravity(Gravity.CENTER);
        dialog.setTitle(title);*/

        WindowManager.LayoutParams tempParams = new WindowManager.LayoutParams();
        tempParams.copyFrom(dialog.getWindow().getAttributes());

		/*tempParams.width = dialogWidthInPx;
        tempParams.height = dialogHeightInPx;*/
        tempParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        tempParams.height = WindowManager.LayoutParams.MATCH_PARENT;

        tempParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        tempParams.dimAmount = dialogDimAmount;

        View tempView = layout;
        dialog.setContentView(tempView);
        dialog.setCancelable(false);

        dialog.getWindow().setAttributes(tempParams);
        //dialog.getWindow().setBackgroundDrawableResource(android.R.drawable.editbox_dropdown_dark_frame);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
/*		if(keyPadOnTop)
			dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);*/

        return dialog;
    }


    public static final int PROGRESS_FRAME_GRAVITY_TOP = 9001;
    public static final int PROGRESS_FRAME_GRAVITY_CENTER = 9003;
    public static final int PROGRESS_FRAME_GRAVITY_BOTTOM = 9004;


    public static boolean justPlainOverLay = false;
    public static int marginForProgressViewInGrid = 5;

    private void setGravity(View proView, int gravity) {
        RelativeLayout.LayoutParams rlParams = /*new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)*/ (RelativeLayout.LayoutParams) proView.getLayoutParams();
        if (gravity == PROGRESS_FRAME_GRAVITY_CENTER) {
            rlParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        } else if (gravity == PROGRESS_FRAME_GRAVITY_TOP) {
            rlParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            rlParams.topMargin = (int) (marginForProgressViewInGrid * mDispProp.getYPixelsPerCell());
        } else if ((gravity == PROGRESS_FRAME_GRAVITY_BOTTOM)) {
            rlParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            rlParams.bottomMargin = (int) (marginForProgressViewInGrid * mDispProp.getYPixelsPerCell());
        }
        proView.setLayoutParams(rlParams);
    }


    private void startAnim(View view, int animResID) throws Exception {
        Animation anim = AnimationUtils.loadAnimation(appContext, animResID);
        anim.setDuration(1200);
        view.startAnimation(anim);
    }


    public Dialog displayViewDialog(Activity activity, View cutomizationView) {
        return mViewConstructor.displayDialog(activity, cutomizationView);
    }

}
