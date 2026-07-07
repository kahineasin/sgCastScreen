//package com.sellgirl.castScreen.android;
//
////import org.jupnp.UpnpServiceImpl;
////import org.jupnp.registry.DefaultRegistryListener;
////import org.jupnp.model.meta.RemoteDevice;
////import org.fourthline.cling.UpnpService;
////import org.fourthline.cling.UpnpServiceImpl;
////import org.fourthline.cling.registry.DefaultRegistryListener;
////import org.fourthline.cling.registry.Registry;
////import org.fourthline.cling.model.meta.RemoteDevice;
////import org.fourthline.cling.model.meta.Device;
////import org.fourthline.cling.model.types.UDN;
//import android.util.Log;
//
//public class UpnpServiceController {
//    private static final String TAG = "UpnpServiceController";
//    private UpnpService upnpService;
////    private DeviceDiscoveryListener listener;
////
////    public interface DeviceDiscoveryListener {
////        void onDeviceDiscovered(String deviceName, String locationUrl);
////    }
////
////    public UpnpServiceController(DeviceDiscoveryListener listener) {
////        this.listener = listener;
////    }
////
//    public void startDiscovery() {
//        upnpService = new UpnpServiceImpl(new DefaultRegistryListener() {
//            @Override
//            public void deviceAdded(Registry registry, RemoteDevice device) {
//                // 发现新设备时的回调
//                String deviceName = device.getDetails().getFriendlyName();
//                String location = device.getIdentity().getDescriptorURL().toString();
//                Log.d(TAG, "Device Added: " + deviceName);
//                if (listener != null) {
//                    listener.onDeviceDiscovered(deviceName, location);
//                }
//            }
//            // 其他方法可选择性实现
//        });
//        upnpService.getControlPoint().search();
//    }
////
////    public void stopDiscovery() {
////        if (upnpService != null) {
////            upnpService.shutdown();
////        }
////    }
//}
