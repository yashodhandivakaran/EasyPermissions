package easypermissions.yashodhandivakaran.com.libray;

import android.os.Bundle;

/**
 * Created by Yashodhan on 03/11/15.
 */
public interface EasyPermissionCallback {
    void permissionAlreadyGrantedContinueTask(int requestCode,Bundle bundle);
    void permissionGrantedContinueTask(int requestCode, Bundle bundle);
    void permissionDeniedContinueTask(int requestCode,Bundle bundle);
    void processBeforePermissionRequested(int requestCode, Bundle bundle);
    void processBeforePermissionRationaleRequested(int requestCode,Bundle bundle);
}