package com.sellgirl.castScreen.android.permission;

import android.Manifest;
import android.accessibilityservice.AccessibilityService;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

//import com.sellgirl.gamepadtool.R;
//import com.sellgirl.gamepadtool.android.PermissionUtils;

import com.sellgirl.castScreen.R;

import java.util.LinkedList;
import java.util.Queue;

public class PermissionManager {
    private static final String TAG = "PermissionManager";

    // 权限请求队列
    private Queue<PermissionRequest> permissionQueue = new LinkedList<>();
    private Context context;
    private PermissionRequest currentRequest;

    // 回调接口
    public interface PermissionCallback {
        void onAllPermissionsCompleted(boolean allGranted);
        void onSinglePermissionResult(String permission, boolean granted);
    }

    public PermissionManager(Context context) {
        this.context = context;
    }

    /**
     * 添加权限请求到队列
     */
    public PermissionManager addPermission(PermissionRequest request) {
        permissionQueue.offer(request);
        return this; // 支持链式调用
    }

    /**
     * 开始顺序申请权限
     */
    public void startRequest(Activity activity, PermissionCallback callback) {
        if (permissionQueue.isEmpty()) {
            callback.onAllPermissionsCompleted(true);
            return;
        }

        processNextPermission(activity, callback);
        String aa="aa";
    }

    private void processNextPermission(Activity activity, PermissionCallback callback) {
        if (permissionQueue.isEmpty()) {
            callback.onAllPermissionsCompleted(true);
            return;
        }

        currentRequest = permissionQueue.poll();

        // 检查是否已有权限
        if (hasPermission(currentRequest)) {
            executeGrantedAction(currentRequest);
            callback.onSinglePermissionResult(currentRequest.getPermissionName(), true);
            processNextPermission(activity, callback);
        } else {
            requestPermission(activity, currentRequest, callback);
        }
    }

    private boolean hasPermission(PermissionRequest request) {
        if (request.getPermissionName().equals("OVERLAY")) {
//            return false;
            return hasOverlayPermission(context);
        } else {
            return ContextCompat.checkSelfPermission(context, request.getPermissionName())
                    == PackageManager.PERMISSION_GRANTED;
        }
    }

    /**
     * 检查是否拥有悬浮窗权限
     */
    private static boolean hasOverlayPermission(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return Settings.canDrawOverlays(context);
        }
        // Android 6.0 以下默认有权限
        return true;
    }
    private static void requestOverlayPermission(Activity activity, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + activity.getPackageName()));
            activity.startActivityForResult(intent, requestCode);
        }
    }

    private static void requestMediaPermission(Activity activity, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_VIDEO}, requestCode);
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, requestCode);
        }
    }
//    private void requestPermission(Activity activity, PermissionRequest request, PermissionCallback callback) {
//        if (request.getPermissionName().equals("OVERLAY")) {
//            // 悬浮窗特殊权限
//            showPermissionDialog(activity, request, callback);
//        } else {
//            // 普通运行时权限
//            ActivityCompat.requestPermissions(activity,
//                    new String[]{request.getPermissionName()},
//                    request.getRequestCode());
//        }
//    }

    private void requestPermission(Activity activity, PermissionRequest request, PermissionCallback callback) {
        switch (request.getPermissionType()) {
            case PermissionRequest.TYPE_OVERLAY:
                // 悬浮窗权限
//                showOverlayPermissionDialog(activity, request, callback);
                showPermissionDialog(activity, request, callback);
                break;

            case PermissionRequest.TYPE_ACCESSIBILITY:
                // 无障碍权限
                showAccessibilityPermissionDialog(activity, request, callback);
//                showPermissionDialog(activity, request, callback);
                break;
            case PermissionRequest.TYPE_MEDIA:
                requestMediaPermission(activity,request.getRequestCode());
                break;
            case PermissionRequest.TYPE_NORMAL:
            default:
                // 普通运行时权限
                ActivityCompat.requestPermissions(activity,
                        new String[]{request.getPermissionName()},
                        request.getRequestCode());
                break;
        }
    }


    private void showPermissionDialog(Activity activity, PermissionRequest request, PermissionCallback callback) {
        new AlertDialog.Builder(activity)
                .setTitle("需要" + request.getPermissionDesc())
                .setMessage("请开启" + request.getPermissionDesc() + "以使用相关功能")
                .setPositiveButton("去开启", (dialog, which) -> {
                    if (request.getPermissionName().equals("OVERLAY")) {
                        requestOverlayPermission(activity, request.getRequestCode());
                    }
                })
                .setNegativeButton("取消", (dialog, which) -> {
                    executeDeniedAction(request);
                    callback.onSinglePermissionResult(request.getPermissionName(), false);
                    processNextPermission(activity, callback);
                })
                .setCancelable(false)
                .show();
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (currentRequest != null && currentRequest.getRequestCode() == requestCode) {
            boolean granted = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;

            if (granted) {
                executeGrantedAction(currentRequest);
            } else {
                executeDeniedAction(currentRequest);
            }
        }
    }

    //此方法有效，但好像有问题，只处理了overlay? todo
    public void onActivityResult(int requestCode) {
        if (currentRequest != null && currentRequest.getRequestCode() == requestCode) {
            if (currentRequest.getPermissionName().equals("OVERLAY") &&
                    hasOverlayPermission(context)) {
                executeGrantedAction(currentRequest);
            } else {
                executeDeniedAction(currentRequest);
            }
        }
    }

    private void executeGrantedAction(PermissionRequest request) {
        if (request.getOnGrantedAction() != null) {
            request.getOnGrantedAction().run();
        }
    }

    private void executeDeniedAction(PermissionRequest request) {
        if (request.getOnDeniedAction() != null) {
            request.getOnDeniedAction().run();
        }
    }

    //------------------------无障碍权限-----------------------------

    /**
     * 检查无障碍权限是否已开启
     * @param serviceName accessibilityService的类全名
     */
    public static boolean isAccessibilityServiceEnabled(Context context, String serviceName) {
        try {
            int accessibilityEnabled = Settings.Secure.getInt(
                    context.getContentResolver(),
                    Settings.Secure.ACCESSIBILITY_ENABLED
            );

            if (accessibilityEnabled == 1) {
                String enabledServices = Settings.Secure.getString(
                        context.getContentResolver(),
                        Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
                );

                if (enabledServices != null) {
                    return enabledServices.toLowerCase().contains(serviceName.toLowerCase());
                }
            }
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 跳转到无障碍设置页面
     */
    public static void requestAccessibilityPermission(Activity activity) {
        try {
            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            activity.startActivity(intent);
        } catch (Exception e) {
            // 备用方案：打开应用详情页
            try {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.setData(Uri.parse("package:" + activity.getPackageName()));
                activity.startActivity(intent);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }


    private void showAccessibilityPermissionDialog(Activity activity, PermissionRequest request, PermissionCallback callback) {
        new AlertDialog.Builder(activity)
                .setTitle("需要无障碍权限")
                .setMessage(request.getPermissionDesc() + "\n\n请在弹出的页面中找到并开启「" +
                        activity.getString(R.string.app_name) + "」的无障碍服务")
                .setPositiveButton("去开启", (dialog, which) -> {
                    requestAccessibilityPermission(activity);
                    // 标记正在等待无障碍权限
                    currentRequest = request;
                })
                .setNegativeButton("取消", (dialog, which) -> {
                    executeDeniedAction(request);
                    callback.onSinglePermissionResult(request.getPermissionName(), false);
                    processNextPermission(activity, callback);
                })
                .setCancelable(false)
                .show();
    }

    /**
     * 检查无障碍权限状态（在 onResume 中调用）
     */
    public void checkAccessibilityPermission(Activity activity,
                                             Class<? extends AccessibilityService> serviceCls,
                                             PermissionCallback callback

                                             ) {
        if (currentRequest != null &&
                currentRequest.getPermissionType() == PermissionRequest.TYPE_ACCESSIBILITY) {

            // 构建服务完整名称
//            String serviceName = activity.getPackageName() + "/.MyAccessibilityService";
//            String serviceName = activity.getPackageName() + "."+serviceCls.getName();
            String serviceName = serviceCls.getName();

            if (isAccessibilityServiceEnabled(activity, serviceName)) {
                executeGrantedAction(currentRequest);
                callback.onSinglePermissionResult(currentRequest.getPermissionName(), true);
                currentRequest = null;
                processNextPermission(activity, callback);
            } else {
                // 用户可能没有开启，保持当前状态等待下次检查
                // 可以在这里添加重试逻辑或超时处理
            }
        }
    }
    //------------------------无障碍权限 END-----------------------------

    //------------------benjamin 加的方法-----------------------
    //似乎一般不需要全权限，因为有些时候，自己都不知道某权限是不是必须的
//    public void initOK(){
//        inited=true;
//    }
//    //为了避免未加完权限，就判断为allOK
//    private boolean inited=false;
//    public boolean isAllOK(){
//        return inited&&(null==permissionQueue||permissionQueue.isEmpty());
//    }
}
