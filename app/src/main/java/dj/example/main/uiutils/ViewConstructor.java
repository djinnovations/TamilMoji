package dj.example.main.uiutils;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import dj.example.main.R;


/**
 * Created by COMP on 1/22/2016.
 */
public class ViewConstructor {


    private static ViewConstructor thisInstance;

    public interface InfoDisplayListener {

        void onNegativeSelection(Dialog alertDialog);

        void onPositiveSelection(Dialog alertDialog);
    }

    private DisplayProperties mDispProp;
    private float pixels_per_Ycell;
    private float pixels_per_Xcell;
    private Context appContext;

    private ViewConstructor(Context appContext) {

        this.appContext = appContext;
        DisplayProperties mDispProp = DisplayProperties.getInstance(1);
        this.pixels_per_Xcell = mDispProp.getXPixelsPerCell();
        this.pixels_per_Ycell = mDispProp.getYPixelsPerCell();
    }

    public static ViewConstructor getInstance(Context appContext) {
        if (thisInstance == null) {
            thisInstance = new ViewConstructor(appContext);
        }
        return thisInstance;
    }

    public static void clearInstance() {
        thisInstance = null;
    }

    public AlertDialog displayInfo(Activity activity, String title, String infoMsg, String positiveText
            , int infoMsgColor, boolean cancelable, final InfoDisplayListener mInfoListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(infoMsg).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
                mInfoListener.onNegativeSelection(alert);
            }
        }).setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        mInfoListener.onPositiveSelection(alert);
                    }
                });
        if (!TextUtils.isEmpty(title))
            builder.setTitle(title);
        builder.setCancelable(cancelable);

        /*if(showTwoOptions){
            builder.setNegativeButton(negativeBtnText, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setCancelable(false);
        }*/

        alert = builder.create();
        alert.getWindow().getAttributes().windowAnimations = R.style.AlertDialogAnimHelp;
        alert.show();

        Button btnPositive = (alert.getButton(DialogInterface.BUTTON_POSITIVE));
        btnPositive.setTextColor(ResourceReader.getInstance().getColorFromResource(infoMsgColor));
        btnPositive.setAllCaps(false);
        Button btnNegative = (alert.getButton(DialogInterface.BUTTON_NEGATIVE));
        btnNegative.setTextColor(ResourceReader.getInstance().getColorFromResource(R.color.colorMaterialBlack));
        btnNegative.setAllCaps(false);
        /*if(showTwoOptions){
            Button btnNegative = (alert.getButton(DialogInterface.BUTTON_NEGATIVE));
            btnNegative.setTextSize(TypedValue.COMPLEX_UNIT_PX, pixels_per_Xcell*2.2f);
            btnNegative.setTextColor(ResourceReader.getInstance(appContext).getColorFromResource(R.color.colorAppMainBg));
            btnNegative.setAllCaps(false);
        }*/

        TextView tvMsg = ((TextView) alert.findViewById(android.R.id.message));
        //tvMsg.setTextSize(TypedValue.COMPLEX_UNIT_PX, pixels_per_Xcell * 2.4f);
        tvMsg.setTextColor(ResourceReader.getInstance().getColorFromResource(infoMsgColor));

        return alert;
    }


    public AlertDialog displayInfo(Activity activity, String title, String infoMsg, String positiveBtnText,
                                   String negativeBtnText, boolean showTwoOptions, final InfoDisplayListener mInfoListener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(infoMsg).setPositiveButton(positiveBtnText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mInfoListener.onPositiveSelection(alert);
                    }
                }).setTitle(title);

        if (showTwoOptions) {
            builder.setNegativeButton(negativeBtnText, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setCancelable(false);
        }

        /*AlertDialog*/
        alert = builder.create();
        alert.getWindow().getAttributes().windowAnimations = R.style.AlertDialogAnimHelp;
        alert.show();


        Button btnPositive = (alert.getButton(DialogInterface.BUTTON_POSITIVE));
        btnPositive.setTextSize(TypedValue.COMPLEX_UNIT_PX, pixels_per_Xcell * 2.2f);
        btnPositive.setTextColor(ResourceReader.getInstance().getColorFromResource(R.color.colorAppPrimaryBg));
        btnPositive.setAllCaps(false);
        if (showTwoOptions) {

            Button btnNegative = (alert.getButton(DialogInterface.BUTTON_NEGATIVE));
            btnNegative.setTextSize(TypedValue.COMPLEX_UNIT_PX, pixels_per_Xcell * 2.2f);
            btnNegative.setTextColor(ResourceReader.getInstance().getColorFromResource(R.color.colorAppPrimaryBg));
            btnNegative.setAllCaps(false);
        }

        ((TextView) alert.findViewById(android.R.id.message)).
                setTextSize(TypedValue.COMPLEX_UNIT_PX, pixels_per_Xcell * 2.7f);

        return alert;
    }

    public AlertDialog displayInfo(Activity activity, String title, String infoMsg, String positiveText, int infoMsgColor) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(infoMsg).setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        if (!TextUtils.isEmpty(title))
            builder.setTitle(title);

        /*if(showTwoOptions){
            builder.setNegativeButton(negativeBtnText, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setCancelable(false);
        }*/

        AlertDialog alert = builder.create();
        alert.getWindow().getAttributes().windowAnimations = R.style.AlertDialogAnimHelp;
        alert.show();

        Button btnPositive = (alert.getButton(DialogInterface.BUTTON_POSITIVE));
        btnPositive.setTextSize(TypedValue.COMPLEX_UNIT_PX, pixels_per_Xcell * 2.2f);
        btnPositive.setTextColor(ResourceReader.getInstance().getColorFromResource(infoMsgColor));
        btnPositive.setAllCaps(false);
        /*if(showTwoOptions){
            Button btnNegative = (alert.getButton(DialogInterface.BUTTON_NEGATIVE));
            btnNegative.setTextSize(TypedValue.COMPLEX_UNIT_PX, pixels_per_Xcell*2.2f);
            btnNegative.setTextColor(ResourceReader.getInstance(appContext).getColorFromResource(R.color.colorAppMainBg));
            btnNegative.setAllCaps(false);
        }*/

        TextView tvMsg = ((TextView) alert.findViewById(android.R.id.message));
        tvMsg.setTextSize(TypedValue.COMPLEX_UNIT_PX, pixels_per_Xcell * 2.4f);
        tvMsg.setTextColor(ResourceReader.getInstance().getColorFromResource(infoMsgColor));

        return alert;
    }


    private AlertDialog alert;

    public AlertDialog displayInfo(Activity activity, String title, String infoMsg, String positiveText
            , int infoMsgColor, final InfoDisplayListener mInfoListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(infoMsg).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
                mInfoListener.onNegativeSelection(alert);
            }
        }).setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        mInfoListener.onPositiveSelection(alert);
                    }
                });
        if (!TextUtils.isEmpty(title))
            builder.setTitle(title);

        /*if(showTwoOptions){
            builder.setNegativeButton(negativeBtnText, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setCancelable(false);
        }*/

        alert = builder.create();
        alert.getWindow().getAttributes().windowAnimations = R.style.AlertDialogAnimHelp;
        alert.show();

        Button btnPositive = (alert.getButton(DialogInterface.BUTTON_POSITIVE));
        //btnPositive.setTextSize(TypedValue.COMPLEX_UNIT_PX, pixels_per_Xcell * 2.2f);
        btnPositive.setTextColor(ResourceReader.getInstance().getColorFromResource(infoMsgColor));
        btnPositive.setAllCaps(false);
        /*if(showTwoOptions){
            Button btnNegative = (alert.getButton(DialogInterface.BUTTON_NEGATIVE));
            btnNegative.setTextSize(TypedValue.COMPLEX_UNIT_PX, pixels_per_Xcell*2.2f);
            btnNegative.setTextColor(ResourceReader.getInstance(appContext).getColorFromResource(R.color.colorAppMainBg));
            btnNegative.setAllCaps(false);
        }*/

        TextView tvMsg = ((TextView) alert.findViewById(android.R.id.message));
        //tvMsg.setTextSize(TypedValue.COMPLEX_UNIT_PX, pixels_per_Xcell * 2.4f);
        tvMsg.setTextColor(ResourceReader.getInstance().getColorFromResource(infoMsgColor));

        return alert;
    }


    public AlertDialog displayViewInfo(Activity activity, String title, int viewResId, String positiveBtnText, boolean showTwoOptions,
                                       final InfoDisplayListener mInfoListener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(viewResId).setPositiveButton(positiveBtnText,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        mInfoListener.onPositiveSelection(alert);
                    }
                })/*.setCancelable(false)*/;

        if (title != null) {
            builder.setTitle(title);
        }
        if (showTwoOptions) {
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }

        /*AlertDialog */
        alert = builder.create();
        alert.getWindow().getAttributes().windowAnimations = R.style.AlertDialogAnimHelp;
        alert.show();


        Button btnPositive = (alert.getButton(DialogInterface.BUTTON_POSITIVE));
        //btnPositive.setTextSize(TypedValue.COMPLEX_UNIT_PX, pixels_per_Xcell * 2);
        btnPositive.setAllCaps(false);
        btnPositive.setTextColor(ResourceReader.getInstance().getColorFromResource(R.color.colorAppPrimaryBg));

        if (showTwoOptions) {
            Button btnNegative = (alert.getButton(DialogInterface.BUTTON_NEGATIVE));
            btnNegative.setAllCaps(false);
            //btnNegative.setTextSize(TypedValue.COMPLEX_UNIT_PX, pixels_per_Xcell * 2);
            btnNegative.setTextColor(ResourceReader.getInstance().getColorFromResource(R.color.colorAppPrimaryBg));
        }

        ((TextView) alert.findViewById(android.R.id.message)).
                setTextSize(TypedValue.COMPLEX_UNIT_PX, pixels_per_Xcell * 3);

        return alert;
    }


    public Dialog displayDialog(Activity activity, View bodyView) {

        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //View tempView = LayoutInflater.from(appContext).inflate(R.layout.dialog_cart_new, null);
        WindowManager.LayoutParams tempParams = new WindowManager.LayoutParams();
        tempParams.copyFrom(dialog.getWindow().getAttributes());

		/*tempParams.width = dialogWidthInPx;
        tempParams.height = dialogHeightInPx;*/
        tempParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        tempParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        tempParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        tempParams.dimAmount = 0; // Dim level. 0.0 - no dim, 1.0 - completely opaque

        dialog.setContentView(bodyView);
        dialog.setCancelable(false);
        dialog.getWindow().setAttributes(tempParams);
        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        //dialog.getWindow().setBackgroundDrawableResource(android.R.drawable.editbox_dropdown_dark_frame);
/*		if(keyPadOnTop)
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);*/

        return dialog;
    }


}
