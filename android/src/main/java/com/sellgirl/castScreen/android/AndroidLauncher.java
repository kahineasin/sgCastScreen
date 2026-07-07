package com.sellgirl.castScreen.android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.sellgirl.castScreen.CastScreen;
import com.sellgirl.castScreen.IDLNADeviceCaster;
import com.sellgirl.castScreen.android.send.DLNACastManager;
import com.sellgirl.castScreen.model.DeviceIp;

import org.jupnp.UpnpService;

/** Launches the Android application. */
public class AndroidLauncher extends AndroidApplication {
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
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (captureManager != null) {
//            captureManager.onActivityResult(requestCode, resultCode, data, this);
//        }
//    }
    //-----------------------------------

protected DLNADeviceScanner2 scanner;
//    private DLNADeviceScanner scanner;
    private DLNACastManager castManager;

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
        game.setScanner(scanner);

        castManager = new DLNACastManager();
        game.setCaster(new IDLNADeviceCaster() {
            @Override
            // 用户选择设备后调用
            public void startCasting(DeviceIp ip//, UpnpService upnpService
            ) {
                UpnpService upnpService=scanner.getUpnpService();
                DLNADeviceScanner2.DLNADevice device= scanner.getDlnaDevice(ip);
                castManager.startStreaming(AndroidLauncher.this, device, upnpService);
            }
        });

        new Thread(() -> scanner.startScan()).start();
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
}
