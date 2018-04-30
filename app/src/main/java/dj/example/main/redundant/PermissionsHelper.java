package dj.example.main.redundant;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;

/**
 * Created by DJphy on 09-07-2017.
 */

public class PermissionsHelper {
    private final Context context;
    PermissionListener onPermission;
    TedPermission permission;
    PermissionListener permissionlistener = new PermissionListener() {
        public void onPermissionGranted() {
            if(PermissionsHelper.this.onPermission != null) {
                PermissionsHelper.this.onPermission.onPermissionGranted();
            }

            PermissionsHelper.this.permissionGranted();
        }

        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            if(PermissionsHelper.this.onPermission != null) {
                PermissionsHelper.this.onPermission.onPermissionDenied(deniedPermissions);
            }

            PermissionsHelper.this.permissionDenied(deniedPermissions);
        }
    };
    private String deniedCloseButtonText = "It\\\'s ok, I don\'t want this";
    private String rationaleDenyText;
    private String rationaleConfirmText = "Give Permission";
    private String rationaleMessage = "we need below permission to run the app smothly";
    private String deniedMessage = "If you reject permission,you can not use this service\\n\\nPlease turn on permissions at [Setting] > [Permission]";
    private boolean showRationaleConfirm = true;
    private boolean showDeniedMessage = true;
    private int rationaleView = 0;
    private int deniedView = 0;
    private boolean showRationaleConfirmView = false;
    private boolean showDeniedMessageView = false;

    public String getRationaleDenyText() {
        return this.rationaleDenyText;
    }

    public void setRationaleDenyText(String rationaleDenyText) {
        this.rationaleDenyText = rationaleDenyText;
    }

    public boolean isShowRationaleConfirmView() {
        return this.showRationaleConfirmView;
    }

    public void setShowRationaleConfirmView(boolean showRationaleConfirmView) {
        this.showRationaleConfirmView = showRationaleConfirmView;
    }

    public boolean isShowDeniedMessageView() {
        return this.showDeniedMessageView;
    }

    public void setShowDeniedMessageView(boolean showDeniedMessageView) {
        this.showDeniedMessageView = showDeniedMessageView;
    }

    public int getRationaleView() {
        return this.rationaleView;
    }

    public void setRationaleView(int rationaleView) {
        this.rationaleView = rationaleView;
    }

    public int getDeniedView() {
        return this.deniedView;
    }

    public void setDeniedView(int deniedView) {
        this.deniedView = deniedView;
    }

    public PermissionsHelper(Context context) {
        this.context = context;
    }

    public PermissionListener getOnPermission() {
        return this.onPermission;
    }

    public PermissionsHelper setOnPermission(PermissionListener onPermission) {
        this.onPermission = onPermission;
        return this;
    }

    public void check(String[] permissions, String[] friendlyPermissionsMeaning) {
        if(friendlyPermissionsMeaning == null || friendlyPermissionsMeaning.length == 0) {
            friendlyPermissionsMeaning = permissions;
        }

        /*if(permissions != null && permissions.length != 0) {
            this.permission = new TedPermission(this.context);
            this.permission.setPermissionListener(this.permissionlistener);
            this.permission.setPermissions(permissions);
            String msg;
            int i;
            String permissionVal;
            String s1;
            if(this.isShowDeniedMessageView() && this.getDeniedView() <= 0) {
                this.permission.setDenyView(this.getDeniedView());
            } else if(this.isShowDeniedMessage()) {
                if(this.getDeniedCloseButtonText() != null) {
                    this.permission.setDeniedCloseButtonText(this.getDeniedCloseButtonText());
                }

                if(this.getDeniedMessage() != null) {
                    msg = this.getDeniedMessage();
                    msg = msg + "\n";

                    for(i = 0; i < friendlyPermissionsMeaning.length; ++i) {
                        permissionVal = friendlyPermissionsMeaning[i];
                        if(permissionVal.lastIndexOf(".") != -1) {
                            permissionVal = permissionVal.substring(permissionVal.lastIndexOf(".") + 1, permissionVal.length());
                        }

                        s1 = permissionVal.substring(0, 1).toUpperCase();
                        permissionVal = s1 + permissionVal.substring(1).toLowerCase();
                        permissionVal = permissionVal.replaceAll("_", " ");
                        msg = msg + "\n(" + (i + 1) + ")" + permissionVal;
                    }

                    this.permission.setDeniedMessage(msg);
                }
            }

            if(this.isShowRationaleConfirmView() && this.getRationaleView() <= 0) {
                this.permission.setRationaleView(this.getRationaleView());
            } else if(this.isShowRationaleConfirm()) {
                if(this.getRationaleConfirmText() != null) {
                    this.permission.setRationaleConfirmText(this.getRationaleConfirmText());
                }

                if(this.getRationaleDenyText() != null) {
                    this.permission.setRationaleDenyText(this.getRationaleDenyText());
                }

                if(this.getRationaleMessage() != null) {
                    msg = this.getRationaleMessage();
                    msg = msg + "\n";

                    for(i = 0; i < friendlyPermissionsMeaning.length; ++i) {
                        permissionVal = friendlyPermissionsMeaning[i];
                        if(permissionVal.lastIndexOf(".") != -1) {
                            permissionVal = permissionVal.substring(permissionVal.lastIndexOf(".") + 1, permissionVal.length());
                        }

                        if(permissionVal.lastIndexOf("_") != -1) {
                            s1 = permissionVal.substring(0, 1).toUpperCase();
                            permissionVal = s1 + permissionVal.substring(1).toLowerCase();
                            permissionVal = permissionVal.replaceAll("_", " ");
                        }

                        msg = msg + "\n(" + (i + 1) + ")" + permissionVal;
                    }

                    this.permission.setRationaleMessage(msg);
                }
            }

            this.permission.check();
        }*/

    }

    public void check(String[] permissions) {
        this.check(permissions, permissions);
    }

    public String getDeniedCloseButtonText() {
        return this.deniedCloseButtonText;
    }

    public PermissionsHelper setDeniedCloseButtonText(String val) {
        this.deniedCloseButtonText = val;
        return this;
    }

    public String getRationaleConfirmText() {
        return this.rationaleConfirmText;
    }

    public PermissionsHelper setRationaleConfirmText(String val) {
        this.rationaleConfirmText = val;
        return this;
    }

    public String getRationaleMessage() {
        return this.rationaleMessage;
    }

    public PermissionsHelper setRationaleMessage(String val) {
        this.rationaleMessage = val;
        return this;
    }

    public String getDeniedMessage() {
        return this.deniedMessage;
    }

    public PermissionsHelper setDeniedMessage(String val) {
        this.deniedMessage = val;
        return this;
    }

    protected void permissionGranted() {
    }

    protected void permissionDenied(ArrayList<String> deniedPermissions) {
    }

    public boolean requiredPermission(String[] permissions) {
        return permissions != null && permissions.length != 0;
    }

    public boolean hasPermission(String[] permissions) {
        boolean has = true;
        if(permissions != null && permissions.length != 0) {
            for(int i = 0; i < permissions.length; ++i) {
                int result = ContextCompat.checkSelfPermission(this.context, permissions[i]);
                if(result != 0) {
                    has = false;
                    break;
                }
            }
        }

        return has;
    }

    public boolean isShowRationaleConfirm() {
        return this.showRationaleConfirm;
    }

    public void setShowRationaleConfirm(boolean showRationaleConfirm) {
        this.showRationaleConfirm = showRationaleConfirm;
    }

    public boolean isShowDeniedMessage() {
        return this.showDeniedMessage;
    }

    public void setShowDeniedMessage(boolean showDeniedMessage) {
        this.showDeniedMessage = showDeniedMessage;
    }
}
