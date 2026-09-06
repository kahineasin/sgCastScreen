package com.sellgirl.castScreen.android;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.sellgirl.castScreen.CastScreen;
import com.sellgirl.castScreen.IDLNADeviceCaster;
import com.sellgirl.castScreen.android.sendimg.SimpleCastManager;
import com.sellgirl.castScreen.model.DeviceIp;
//import com.sellgirl.castScreen.android.send.DLNACastManager;
import com.sellgirl.castScreen.android.sendimg.DLNACastManager;
import com.sellgirl.castScreen.android.permission.PermissionManager;
import com.sellgirl.castScreen.android.permission.PermissionRequest;
import com.sellgirl.sgJavaHelper.config.SGDataHelper;

import org.jupnp.UpnpService;

import java.io.IOException;
import java.io.InputStream;

/** Launches the Android application. */
public class AndroidLauncher extends AndroidApplication {
    private  final String TAG="AndroidLauncher";
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        AndroidApplicationConfiguration configuration = new AndroidApplicationConfiguration();
//        configuration.useImmersiveMode = true; // Recommended, but not required.
//        initialize(new CastScreen(), configuration);
////        com.sellgirl.sgJavaHelper.
//
//        DLNADeviceScanner scanner=new DLNADeviceScanner();
//        scanner.startScan();
//    }

//-----------------------------------------------

//    private DLNADeviceScanner scanner;
//    private ScreenCaptureManager captureManager;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        // 初始化 libGDX
//        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
//        config.useImmersiveMode = true;
//        initialize(new CastScreen(), config);  // 你的主游戏类
//
//        // 初始化 DLNA 扫描器（要在 libGDX 初始化之后，但无严格顺序）
//        scanner = new DLNADeviceScanner();
//        scanner.setListener(new DLNADeviceScanner.OnDeviceScanListener() {
//            @Override
//            public void onDeviceFound(DLNADeviceScanner.DLNADevice device) {
//                // 可以通过 Gdx.app.postRunnable() 更新 libGDX 界面
//                Gdx.app.postRunnable(() -> {
//                    // 例如，在游戏内部显示设备列表
//                });
//                Log.d("DLNA", "Found: " + device.getFriendlyName());
//            }
//
//            @Override
//            public void onDeviceLost(DLNADeviceScanner.DLNADevice device) {
//
//            }
//
//            @Override
//            public void onScanStarted() {
//
//            }
//
//            @Override
//            public void onScanStopped() {
//
//            }
//            // 实现其他回调...
//        });
//
//        // 启动扫描（建议在子线程中执行，因为 UPnP 是阻塞式网络操作）
//        new Thread(() -> {
//            scanner.startScan();
//        }).start();
//
//        // 初始化屏幕捕获管理器（需要用户授权）
//        captureManager = new ScreenCaptureManager();
//        // 在需要投屏时调用 captureManager.requestScreenCapture(this);
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (scanner != null) {
//            scanner.stopScan();
//        }
//        if (captureManager != null) {
//            captureManager.stopStreaming();
//        }
//    }
//
//    // 处理屏幕捕获权限的返回结果
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (captureManager != null) {
//            captureManager.onActivityResult(requestCode, resultCode, data, this);
//        }

//        if (castManager3 != null&&null!=castManager3.captureManager) {
//            castManager3.captureManager.onActivityResult(requestCode, resultCode, data, this);
//        }

        // 将结果传递给 captureManager
        if (requestCode==PerCode.CAST_SCREEN&&castManager3 != null) {
            castManager3.captureManager.onActivityResult(requestCode, resultCode, data, this);
        }
        if (requestCode==PerCode.REQUEST_CODE_PICK_VIDEO
            //&& resultCode == RESULT_OK
            &&data != null) {
            Uri selectedUri = data.getData();
            if (selectedUri != null) {
                handleSelectedFile(selectedUri);
            }
        }
    }
    //-----------------------------------

protected DLNADeviceScanner2 scanner;
//    private DLNADeviceScanner scanner;
    private DLNACastManager castManager;
    private SimpleCastManager castManager2;
    private com.sellgirl.castScreen.android.send2.DLNACastManager castManager3;
    private com.sellgirl.castScreen.android.sendfile.DLNACastManager castManager4;

    private String videoUrl;
    private DeviceIp ip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.useImmersiveMode = true;
        CastScreen game=new CastScreen();
        initialize(game, config);

        scanner = new DLNADeviceScanner2();
        scanner.init(this);
        scanner.setListener(new DLNADeviceScanner2.OnDeviceScanListener() {
            @Override
            public void onDeviceFound(DLNADeviceScanner2.DLNADevice device) {
                Gdx.app.postRunnable(() -> {
                    Log.d("DLNA", "Found: " + device.getFriendlyName());
                });
            }
            @Override public void onDeviceLost(DLNADeviceScanner2.DLNADevice device) {}
            @Override public void onScanStarted() { Log.d("DLNA", "Scan started"); }
            @Override public void onScanStopped() { Log.d("DLNA", "Scan stopped"); }
            @Override public void onError(String error) {
                Log.e("DLNA", "Error: " + error);
            }
        });
//        scanner.addDeviceToRegistry(scanner.loadDeviceFromLocation("http://192.168.10.22:39520/description.xml"));
        game.setScanner(scanner);


//        SGDataHelper.sgLog=new SGLibGdxLog();

        castManager2 = new SimpleCastManager();

        castManager = new DLNACastManager();
        castManager3 = new com.sellgirl.castScreen.android.send2.DLNACastManager();
        castManager4 = new com.sellgirl.castScreen.android.sendfile.DLNACastManager();
        game.setCaster(new IDLNADeviceCaster() {
            @Override
            // 用户选择设备后调用
            public void startCasting(DeviceIp ip//, UpnpService upnpService
            ) {
                //send旧方案,有多广播端关键帧计算混乱的现象
//                UpnpService upnpService=scanner.getUpnpService();
//                DLNADeviceScanner2.DLNADevice device= scanner.getDlnaDevice(ip);
//
//                try {
//                    String videoUrl=castManager3.startStreaming2(AndroidLauncher.this, device, upnpService);
//                    if(true){return;}// todo 先用浏览器测试,通过再用电视
//                    new Thread(() -> {
//
//                        try {
//                            Thread.sleep(5000);
//                        } catch (InterruptedException e) {
//                            throw new RuntimeException(e);
//                        }
//                        String location=ip.location;
//                        if (location != null && castManager2.loadDevice(location)) {
//                            // 投屏测试
//////                String videoUrl = "http://你的手机IP:8080/stream.ts"; // 先填一个测试视频 URL
////                    String videoUrl="http://mp3.sellgirl.com/mp3/v/IGNITE_%E5%AE%8C%E6%95%B4%E7%89%88.mp4";
////                            String videoUrl=webUrl;
//                            if (castManager2.setAVTransportURI(videoUrl, "Test Stream", null)) {
//                                castManager2.play();
//                            }
//                        }
////                        if (castManager2.setAVTransportURI(videoUrl, "Test Stream", null)) {
////                            castManager2.play();
////                        }
//                    }).start();
//
//                }catch (Exception e){
//                    SGDataHelper.getLog().printException(e,TAG);
//                }
////                // 打印设备名称和服务信息
////                Log.d(TAG, "Device: " + device.getFriendlyName());
////                for (Service service : device.getRawDevice().getServices()) {
////                    ServiceType type = service.getServiceType();
////                    Log.d(TAG, "  Service: " + type.getNamespace() + ":" + type.getType() + " v" + type.getVersion());
////                    Log.d(TAG, "  url: "+device.getLocation());
////                }

                //send1
                UpnpService upnpService=scanner.getUpnpService();
                DLNADeviceScanner2.DLNADevice device= scanner.getDlnaDevice(ip);

                // 1. 初始化并请求录屏权限
                castManager3.startCasting( AndroidLauncher.this);

////                // 2. 连接DLNA设备（假设已扫描到）
//                castManager3.connectDevice(device, upnpService);
//
////                // 3. 发送投屏指令
                String videoUrl = "http://" + castManager3.getLocalIpAddress() + ":8080/stream.ts";
//                castManager3.castToDevice(videoUrl, "Screen Mirroring");

                String location=ip.location;
                if (location != null && castManager2.loadDevice(location)) {
                    if (castManager2.setAVTransportURI(videoUrl, "Test Stream", null)) {
                        castManager2.play();
                    }
                }
            }

            @Override
            public void startCastingWeb(DeviceIp ip, String webUrl) {

                String location=ip.location;
                if (location != null && castManager2.loadDevice(location)) {
                    // 投屏测试
////                String videoUrl = "http://你的手机IP:8080/stream.ts"; // 先填一个测试视频 URL
//                    String videoUrl="http://mp3.sellgirl.com/mp3/v/IGNITE_%E5%AE%8C%E6%95%B4%E7%89%88.mp4";
                    String videoUrl=webUrl;
                    if (castManager2.setAVTransportURI(videoUrl, "Test Stream", null)) {
                        castManager2.play();
                    }
                }
            }

            @Override
            public void startCastingFile(DeviceIp ip, String path) {

                UpnpService upnpService=scanner.getUpnpService();
                DLNADeviceScanner2.DLNADevice device= scanner.getDlnaDevice(ip);


//                castManager.startStreaming(AndroidLauncher.this, device, upnpService);
//                castManager.startStreaming2(AndroidLauncher.this, device, upnpService);
                try {
                     videoUrl=castManager4.startStreaming2(AndroidLauncher.this, device, upnpService);
                     AndroidLauncher.this.ip=ip;
                    pickVideo();

//                    new Thread(() -> {
//
//                        try {
//                            Thread.sleep(5000);
//                        } catch (InterruptedException e) {
//                            throw new RuntimeException(e);
//                        }
//                        String location=ip.location;
//                        if (location != null && castManager2.loadDevice(location)) {
//                            // 投屏测试
//////                String videoUrl = "http://你的手机IP:8080/stream.ts"; // 先填一个测试视频 URL
////                    String videoUrl="http://mp3.sellgirl.com/mp3/v/IGNITE_%E5%AE%8C%E6%95%B4%E7%89%88.mp4";
////                            String videoUrl=webUrl;
//                            if (castManager2.setAVTransportURI(videoUrl, "Test Stream", null)) {
//                                castManager2.play();
//                            }
//                        }
////                        if (castManager2.setAVTransportURI(videoUrl, "Test Stream", null)) {
////                            castManager2.play();
////                        }
//                    }).start();

                }catch (Exception e){
                    SGDataHelper.getLog().printException(e,TAG);
                }
//                // 打印设备名称和服务信息
//                Log.d(TAG, "Device: " + device.getFriendlyName());
//                for (Service service : device.getRawDevice().getServices()) {
//                    ServiceType type = service.getServiceType();
//                    Log.d(TAG, "  Service: " + type.getNamespace() + ":" + type.getType() + " v" + type.getVersion());
//                    Log.d(TAG, "  url: "+device.getLocation());
//                }
            }
        });

        new Thread(() -> {
            scanner.startScan();
            scanner.addDeviceToRegistry(scanner.loadDeviceFromLocation("http://192.168.10.22:39520/description.xml"));

            startScreenCapture();
//            SimpleCastManager castManager2 = new SimpleCastManager();

//            // 从缓存加载 location
//            String location ="http://192.168.10.22:39520/description.xml";// deviceCache.getCachedLocation();
//            if (location != null && castManager2.loadDevice(location)) {
//                // 投屏测试
////                String videoUrl = "http://你的手机IP:8080/stream.ts"; // 先填一个测试视频 URL
//                String videoUrl="http://mp3.sellgirl.com/mp3/v/IGNITE_%E5%AE%8C%E6%95%B4%E7%89%88.mp4";
//                if (castManager2.setAVTransportURI(videoUrl, "Test Stream", null)) {
//                    castManager2.play();
//                }
//            }
            int aa=1;
        }).start();
        int aa=1;

//        if(!checkAndRequestPermission()){
//            requestPermission();
//        }

        checkPermissionsAfterInit();
    }
    private void checkPermissionsAfterInit() {
        // 延迟检查，确保游戏已经初始化完成
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                initPermissionManager();
                permissionManager.startRequest(AndroidLauncher.this,null);
//                if (!allRequiredPermissionsGranted()) {
//                    // 通知游戏显示权限申请界面
//                    myGame.showPermissionRequest();
//                } else {
//                    // 所有权限已获取，正常启动游戏
//                    permissionsGranted = true;
//                    myGame.onAllPermissionsGranted();
//                    startOverlayService();
//                }
            }
        });
    }
    private PermissionManager permissionManager;
    private void initPermissionManager() {
        permissionManager = new PermissionManager(this);

        // 按优先级添加权限
        permissionManager
            .addPermission(new PermissionRequest.Builder()
                .setName(Manifest.permission.READ_EXTERNAL_STORAGE)
                .setDesc("相册权限")
                .setCode(PerCode.P_SELECT_PIC)
                .setType(PermissionRequest.TYPE_MEDIA)
//                .setOnGranted(this::onStoragePermissionGranted)
//                .setOnDenied(this::onStoragePermissionDenied)
//                .setRequired(true)
                .build())
//            // 悬浮窗权限（游戏工具必备）
//            .addPermission(new PermissionRequest.Builder()
//                .setName("OVERLAY")
//                .setDesc("悬浮窗权限")
//                .setCode(REQUEST_OVERLAY_PERMISSION)
//                .setType(PermissionRequest.TYPE_OVERLAY)
//                .setOnGranted(this::onOverlayPermissionGranted)
//                .setOnDenied(this::onOverlayPermissionDenied)
//                .setRequired(true)
//                .build())
//
//            // 2. 无障碍权限（高级功能）
//            .addPermission(new PermissionRequest.Builder()
//                .setName("ACCESSIBILITY")
//                .setDesc("无障碍权限")
//                .setCode(REQUEST_ACCESSIBILITY_PERMISSION)
//                .setType(PermissionRequest.TYPE_ACCESSIBILITY)
//                .setOnGranted(this::onAccessibilityPermissionGranted)
//                .setOnDenied(this::onAccessibilityPermissionDenied)
//                .setRequired(false) // 设为非必需，因为申请流程复杂
//                .build())
//
//            // 存储权限（保存游戏数据）
//            .addPermission(new PermissionRequest.Builder()
//                .setName(Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                .setDesc("存储权限")
//                .setCode(REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION)
//                .setType(PermissionRequest.TYPE_NORMAL)
//                .setOnGranted(this::onStoragePermissionGranted)
//                .setOnDenied(this::onStoragePermissionDenied)
//                .setRequired(true)
//                .build())

//                    // 网络权限（游戏更新、排行榜等）
//                    .addPermission(new PermissionRequest.Builder()
//                            .setName(Manifest.permission.INTERNET)
//                            .setDesc("网络权限")
//                            .setCode(1004)
//                            .setOnGranted(this::onNetworkPermissionGranted)
//                            .setRequired(true)
//                            .build())
        ;
//            permissionManager.initOK();
    }

    // 用户选择设备后调用
    private void startCasting(DLNADeviceScanner2.DLNADevice device, UpnpService upnpService) {
        castManager = new DLNACastManager();
        castManager.startStreaming(this, device, upnpService);
    }

//    // 用户选择设备后调用
//    private void startCasting2(DeviceIp ip//, UpnpService upnpService
//    ) {
//        UpnpService upnpService=scanner.getUpnpService();
//        DLNADeviceScanner2.DLNADevice device= scanner.getDlnaDevice(ip);
//        castManager = new DLNACastManager();
//        castManager.startStreaming(this, device, upnpService);
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (scanner != null) {scanner.stopScan();}
        if (castManager != null) {castManager.stopStreaming();}
//        if (castManager2 != null) {castManager2.stopStreaming();}
        if (castManager3 != null) {castManager3.stopCasting();}
        if (castManager4 != null) {castManager4.stopStreaming();}
    }
//    public static class DLNACaster implements IDLNADeviceCaster {
//        protected DLNADeviceScanner2 scanner;
//        public DLNACaster(){
//
//        }
//        // 用户选择设备后调用
//        public void startCasting(DeviceIp ip//, UpnpService upnpService
//        ) {
//            UpnpService upnpService=scanner.getUpnpService();
//            DLNADeviceScanner2.DLNADevice device= scanner.getDlnaDevice(ip);
//            castManager = new DLNACastManager();
//            castManager.startStreaming(this, device, upnpService);
//        }
//    }


    private MediaProjectionService service; // 不需要持有，只需启动
    private void startScreenCapture() {
        // 1. 启动前台服务（确保 MediaProjection 可用）
        Intent serviceIntent = new Intent(this, MediaProjectionService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent);
        } else {
            startService(serviceIntent);
        }

//        // 2. 请求屏幕捕获权限（原有逻辑）
//        if (captureManager == null) {
//            captureManager = new ScreenCaptureManager();
//        }
//        captureManager.requestCapture(this);
    }

    private static final int REQUEST_CODE_PERMISSION = PerCode.P_SELECT_PIC;

    private boolean checkAndRequestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13+
            return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_VIDEO) == PackageManager.PERMISSION_GRANTED;
        } else { // Android 12L 及以下
            return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        }
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_VIDEO}, REQUEST_CODE_PERMISSION);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION) {
            // 处理授权结果
        }
    }

    private static final int REQUEST_CODE_PICK_IMAGE = PerCode.REQUEST_CODE_PICK_IMAGE;
    private static final int REQUEST_CODE_PICK_VIDEO = PerCode.REQUEST_CODE_PICK_VIDEO;

    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
    }

    private void pickVideo() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
//        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE_PICK_VIDEO);
    }

    private void handleSelectedFile(Uri uri) {
        try {
//            // 通过 ContentResolver 打开输入流，直接读取文件内容
            InputStream inputStream = getContentResolver().openInputStream(uri);
//            // 在这里处理你的文件，例如保存到 App 私有目录
//            File appPrivateFile = new File(getFilesDir(), "selected_file");
//            FileOutputStream outputStream = new FileOutputStream(appPrivateFile);
//            byte[] buffer = new byte[1024];
//            int length;
//            while ((length = inputStream.read(buffer)) > 0) {
//                outputStream.write(buffer, 0, length);
//            }
//            outputStream.close();
//            inputStream.close();
//            // 现在 appPrivateFile 就是你可以随意操作的文件了
//            Log.d("FilePicker", "File saved to: " + appPrivateFile.getAbsolutePath());

            // 如果需要使用 File 对象进行投屏，可以用这个 appPrivateFile
            // 注意：直接使用 File 路径进行投屏可能仍会失败，建议使用 Content Uri

            new Thread(() -> {
//                castManager4.streamServer.file = appPrivateFile;
                castManager4.streamServer.inputStream = inputStream;

                String location = ip.location;
                if (location != null
                    && castManager2.loadDevice(location)//此方法不能在主线程
                ) {
                    // 投屏测试
////                String videoUrl = "http://你的手机IP:8080/stream.ts"; // 先填一个测试视频 URL
//                    String videoUrl="http://mp3.sellgirl.com/mp3/v/IGNITE_%E5%AE%8C%E6%95%B4%E7%89%88.mp4";
//                            String videoUrl=webUrl;
                    if (castManager2.setAVTransportURI(videoUrl, "Test Stream", null)) {
                        castManager2.play();
                    }
                }
            }).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
