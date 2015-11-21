package easypermissions.yashodhandivakaran.com.libray;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.view.View;

import java.util.HashMap;

/**
 * Created by Yashodhan on 17/09/15.
 */
public final class EasyPermissions implements EasyPermissionRequestCodes{

//    public static final String SHOW_SNACKBAR_INITIALLY = "show_snackbar_initially";
//    public static final String PERMISSION_ALREADY_GRANTED = "permission_already_granted";

    private Activity mActivity;
    private View mRootView;
    private EasyPermissionCallback mEasyPermissionCallback;

    private int requestMessageSnackBarDelay = Snackbar.LENGTH_LONG;
    private int deniedMessageSnackBarDelay = Snackbar.LENGTH_LONG;
    private int rationaleRequestMessageDelay = Snackbar.LENGTH_LONG;

    private HashMap<Integer, Bundle> mBundleMap = new HashMap<Integer, Bundle>();
    private HashMap<Integer, String> mPermissionDeniedMessages = new HashMap<Integer, String>();
    private HashMap<Integer,Boolean> mShowSnackBarInitially = new HashMap<Integer,Boolean>();

    private String actionText;
    private int snackbarActionTextColor;

    public static class Builder {
        private Activity mActivity;
        private View mRootView;
        private EasyPermissionCallback mEasyPermissionCallback;

        private int requestMessageSnackBarDelay = Snackbar.LENGTH_LONG;
        private int deniedMessageSnackBarDelay = Snackbar.LENGTH_LONG;
        private int rationaleRequestMessageDelay = Snackbar.LENGTH_LONG;

        private String actionText = "OK";
        private int snackbarActionTextColor = -1;

        public Builder(Activity mActivity, View mRootView, EasyPermissionCallback callback) {
            this.mActivity = mActivity;
            this.mRootView = mRootView;
            this.mEasyPermissionCallback = callback;
        }

        public Builder snackbarActionTextColor(int color) {
            this.snackbarActionTextColor = color;
            return this;
        }

        public Builder snackbarActionText(String action) {
            this.actionText = action;
            return this;
        }

        public Builder requestMessageSnackbarDelay(int delay) {
            this.requestMessageSnackBarDelay = delay;
            return this;
        }

        public Builder deniedMessageSnackbarDelay(int delay) {
            this.deniedMessageSnackBarDelay = delay;
            return this;
        }

        public Builder rationaleRequestMessageDelay(int delay) {
            this.rationaleRequestMessageDelay = delay;
            return this;
        }


        public EasyPermissions build() {
            return new EasyPermissions(this);
        }
    }

    private EasyPermissions(Builder builder) {

        mActivity = builder.mActivity;
        mRootView = builder.mRootView;
        mEasyPermissionCallback = builder.mEasyPermissionCallback;
        snackbarActionTextColor = builder.snackbarActionTextColor;
        actionText = builder.actionText;

        requestMessageSnackBarDelay = builder.requestMessageSnackBarDelay;
        deniedMessageSnackBarDelay = builder.deniedMessageSnackBarDelay;
        rationaleRequestMessageDelay = builder.rationaleRequestMessageDelay;
    }

    public void setBundleForRequest(int requestCode, Bundle bundle) {
        mBundleMap.put(requestCode, bundle);
    }

    public void setPermissionDeniedMessage(int requestCode, String message) {
        mPermissionDeniedMessages.put(requestCode, message);
    }

    public void showPermissionDeniedMessage(int requestCode) {
        Snackbar.make(mRootView, mPermissionDeniedMessages.get(requestCode),
                deniedMessageSnackBarDelay).show();
    }

    public void showSnackBarInitially(int requestCode,boolean value){
        mShowSnackBarInitially.put(requestCode,value);
    }

    /**
     * Request android system for permission
     * @param requestCode This can be any integer value predefined ones are added for eg: PERMISSION_REQUEST_STORAGE
     * @param permission
     * @param rationaleRequestMessage
     * @param requestMessage
     */
    public void grantPermission(int requestCode,
                                String permission,
                                String rationaleRequestMessage,
                                String requestMessage) {

        Bundle bundle = mBundleMap.containsKey(requestCode) ? mBundleMap.get(requestCode) : null;

        if (bundle == null) {
            bundle = new Bundle();
            mBundleMap.put(requestCode, bundle);
        }

        if (ActivityCompat.checkSelfPermission(mActivity, permission)
                == PackageManager.PERMISSION_GRANTED) {
//            bundle.putBoolean(PERMISSION_ALREADY_GRANTED, true);
            mEasyPermissionCallback.permissionAlreadyGrantedContinueTask(requestCode,bundle);
            mEasyPermissionCallback.permissionGrantedContinueTask(requestCode, bundle);
        } else {
//            bundle.putBoolean(PERMISSION_ALREADY_GRANTED, false);
            requestPermission(permission, requestCode, rationaleRequestMessage, requestMessage);
        }

    }

    /**
     * User must call this method from activity which have implemented ActivityCompat.OnRequestPermissionsResultCallback from onRequestPermissionsResult
     * overridden method
     * @param requestCode
     * @param grantResults
     */
    public void processResult(int requestCode, int[] grantResults) {
        if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            mEasyPermissionCallback.permissionGrantedContinueTask(requestCode,
                    mBundleMap.containsKey(requestCode) ? mBundleMap.get(requestCode) : null);
        } else if (mPermissionDeniedMessages.containsKey(requestCode)) {
            showPermissionDeniedMessage(requestCode);
            mEasyPermissionCallback.permissionDeniedContinueTask(requestCode,
                    mBundleMap.containsKey(requestCode) ? mBundleMap.get(requestCode) : null);
        }
    }

    private void requestPermission(final String permission,
                                   final int requestCode,
                                   final String rationaleRequestMessage,
                                   final String requestMesssage) {

        Bundle bundle = mBundleMap.containsKey(requestCode) ? mBundleMap.get(requestCode) : null;

        if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity,
                permission)) {

            mEasyPermissionCallback.processBeforePermissionRationaleRequested(requestCode,bundle);
            Snackbar.make(mRootView, rationaleRequestMessage,
                    rationaleRequestMessageDelay).setAction(actionText, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Request the permission
                    ActivityCompat.requestPermissions(mActivity,
                            new String[]{permission},
                            requestCode);
                }
            }).show();

        } else {
            mEasyPermissionCallback.processBeforePermissionRequested(requestCode,bundle);
            if (!mShowSnackBarInitially.containsKey(requestCode) || mShowSnackBarInitially.get(requestCode)) {

                Snackbar.make(mRootView,
                        requestMesssage,
                        requestMessageSnackBarDelay).show();
            }
            ActivityCompat.requestPermissions(mActivity, new String[]{permission},
                    requestCode);
        }

    }

}
