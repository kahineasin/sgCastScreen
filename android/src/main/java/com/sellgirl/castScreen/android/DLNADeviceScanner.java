package com.sellgirl.castScreen.android;

import android.util.Log;
import org.jupnp.UpnpService;
import org.jupnp.UpnpServiceImpl;
import org.jupnp.android.AndroidUpnpServiceConfiguration;
import org.jupnp.model.meta.Device;
import org.jupnp.model.meta.RemoteDevice;
import org.jupnp.model.types.UDN;
import org.jupnp.registry.DefaultRegistryListener;
import org.jupnp.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class DLNADeviceScanner {
    private static final String TAG = "DLNADeviceScanner";
    private UpnpService upnpService;
    private final List<DLNADevice> deviceList = new CopyOnWriteArrayList<>();
    private OnDeviceScanListener listener;

    public interface OnDeviceScanListener {
        void onDeviceFound(DLNADevice device);
        void onDeviceLost(DLNADevice device);
        void onScanStarted();
        void onScanStopped();
    }

    public void setListener(OnDeviceScanListener listener) { this.listener = listener; }

    public void startScan() {
        if (upnpService != null) return;
        if (listener != null) listener.onScanStarted();

        // 使用 Android 专用配置，避免 OSGi 依赖问题
        upnpService = new UpnpServiceImpl(new AndroidUpnpServiceConfiguration());

        // 注册设备监听器
        upnpService.getRegistry().addListener(new DefaultRegistryListener() {
            @Override
            public void deviceAdded(Registry registry, Device device) {
                DLNADevice dlnaDevice = new DLNADevice((RemoteDevice)device);
                if (!deviceList.contains(dlnaDevice)) {
                    deviceList.add(dlnaDevice);
                    Log.d(TAG, "发现设备: " + dlnaDevice.getFriendlyName());
                    if (listener != null) listener.onDeviceFound(dlnaDevice);
                }
            }
            @Override
            public void deviceRemoved(Registry registry,Device  device) {
                DLNADevice dlnaDevice = new DLNADevice((RemoteDevice)device);//RemoteDevice
                deviceList.remove(dlnaDevice);
                Log.d(TAG, "设备离线: " + dlnaDevice.getFriendlyName());
                if (listener != null) listener.onDeviceLost(dlnaDevice);
            }
        });

        upnpService.startup();//.start();
        // 发送 UPnP 搜索请求
        upnpService.getControlPoint().search();
    }
    public void startScan2() {
        if (upnpService != null) return;
        if (listener != null) listener.onScanStarted();

        try {
            // 使用 Android 专用配置（内部可能使用 HttpURLConnection，但仍建议保留 Jetty）
            upnpService = new UpnpServiceImpl(new AndroidUpnpServiceConfiguration());

            // 先调用 start()，确保所有组件初始化完毕
//            upnpService.start();
            upnpService.startup();//.start();

            // 获取 Registry，并检查是否为 null
            Registry registry = upnpService.getRegistry();
            if (registry == null) {
                throw new IllegalStateException("Registry is null after start()");
            }

//            // 注册监听器
//            registry.addListener(new DefaultRegistryListener() {
//                @Override
//                public void deviceAdded(Registry registry, RemoteDevice device) {
//                    // ... 处理设备发现
//                }
//                // 其他回调
//            });
            registry.addListener(new DefaultRegistryListener() {
                @Override
                public void deviceAdded(Registry registry, Device device) {
                    DLNADevice dlnaDevice = new DLNADevice((RemoteDevice)device);
                    if (!deviceList.contains(dlnaDevice)) {
                        deviceList.add(dlnaDevice);
                        Log.d(TAG, "发现设备: " + dlnaDevice.getFriendlyName());
                        if (listener != null) listener.onDeviceFound(dlnaDevice);
                    }
                }
                @Override
                public void deviceRemoved(Registry registry,Device  device) {
                    DLNADevice dlnaDevice = new DLNADevice((RemoteDevice)device);//RemoteDevice
                    deviceList.remove(dlnaDevice);
                    Log.d(TAG, "设备离线: " + dlnaDevice.getFriendlyName());
                    if (listener != null) listener.onDeviceLost(dlnaDevice);
                }
            });

            // 发送搜索请求
            upnpService.getControlPoint().search();

        } catch (Exception e) {
            Log.e(TAG, "Failed to start UPnP service", e);
            if (listener != null) listener.onScanStopped();
            // 清理资源
            if (upnpService != null) {
                upnpService.shutdown();
                upnpService = null;
            }
        }
    }
    public void stopScan() {
        if (listener != null) listener.onScanStopped();
        if (upnpService != null) {
            upnpService.shutdown();
            upnpService = null;
        }
        deviceList.clear();
    }

    public List<DLNADevice> getDeviceList() { return new ArrayList<>(deviceList); }

    // 设备数据封装类
    public static class DLNADevice {
        private final RemoteDevice device;
        public DLNADevice(RemoteDevice device) { this.device = device; }
        public String getFriendlyName() { return device.getDetails().getFriendlyName(); }
        public String getModelName() { return device.getDetails().getModelDetails().getModelName(); }
        public String getLocation() { return device.getIdentity().getDescriptorURL().toString(); }
        public UDN getUDN() { return device.getIdentity().getUdn();/*.getUDN(); */}
        public RemoteDevice getRawDevice() { return device; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            DLNADevice that = (DLNADevice) o;
            return getUDN().equals(that.getUDN());
        }
        @Override
        public int hashCode() { return getUDN().hashCode(); }
    }
}
