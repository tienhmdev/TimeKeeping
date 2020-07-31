package hvcg.edu.timekeeping.permission;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import java.util.ArrayList;
import java.util.List;

public class PermissionUtil {
    public static final int REQUEST_PERMISSION_CODE = 104389;
    public static void requestPermission(Activity activity, String[] permissions, IPermissionAccess requestPermissionResult) {
        List<String> permissionsRequest = new ArrayList<>();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String permission : permissions) {
                if (activity.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                    permissionsRequest.add(permission);
                }
            }
            if (permissionsRequest.size() != 0){
                activity.requestPermissions(permissionsRequest.toArray(new String[permissionsRequest.size()]), REQUEST_PERMISSION_CODE);
            }else {
                requestPermissionResult.permissionResult(true);
            }
        } else {
            requestPermissionResult.permissionResult(true);
        }
    }
}
